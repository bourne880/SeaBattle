package game;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

// Connection object tries to establish connection between two games and then handles receiving and sending objects between them.
// 1. Connect thread was designed to handle initialization of connection until its successful establishment.
// 2. When connection is established, it will send objects added to send queue, and add objects received to appropriate queues.
// It will handle itself (close) when connection is broken.
public class Connection {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectInputStream in;
	
	private Socket socket;
	private ObjectOutputStream out;
	
	private long pingTime; // time of last ping or read
	private long pingNumber; // randomly generated number used in Ping action
	private boolean pingResult; // result of Ping action
	private boolean connected; // true if connection was established
	
	private LinkedList<Object> received; // queue of received game related objects
	private LinkedList<String> receivedM; // queue of received messages
	private LinkedList<Object> send; // queue of objects to be sent
	
	private ThreadGroup connection;
	
	private static final boolean TESTING = false; // if set to true, will print communicates about exceptions
	
	public Connection(int playerTCP, String friendIP, int friendTCP, ThreadGroup parentTG) {
		connection = new ThreadGroup(parentTG, "Connection");
		
		Thread server = new Thread(connection, new Server(playerTCP));
		server.start();
		
		Thread client = new Thread(connection, new Client(friendIP, friendTCP));
		client.start();
		
		received = new LinkedList<>();
		receivedM = new LinkedList<>();
		send = new LinkedList<>();
	}
	
	// creates serverSocket that is closed after one connection
	// gets friend's clientSocket and its input stream in
	// then runs pingTest and reads objects sent from clientSocket
	private class Server implements Runnable {
		private int tcp;
		
		public Server(int aTCP) {
			tcp = aTCP;
		}
		
		public void run() {
			try {
				serverSocket = new ServerSocket(tcp);
				clientSocket = serverSocket.accept();
				in = new ObjectInputStream(clientSocket.getInputStream());
			} catch (Exception ex) {
				if (TESTING) System.out.println("Server connection ex");
			}
			
			try {
				serverSocket.close();
			} catch (Exception ex) {
				if (TESTING) System.out.println("serverSocket close ex");
			}
			
			pingTest();
			
			read();
			
			try { // when interrupted or connection exception
				clientSocket.close();
			} catch (Exception ex) {
				if (TESTING) System.out.println("clientSocket close ex");
			}
		}
		
		// reads objects from clientSocket's input stream and takes different actions dependent on category
		// if connection exception, closes connection
		// every read operation sets pingTime to current time
		private void read() {
			if (Thread.interrupted())
				return;
			
			Object readco, category, object;
				
			try {
				 // reads object being array of two objects and decomposes it
				// first of type char is category, second is actual object
				// then adds actual object to proper queue or performs action for test
				while ((readco = in.readObject()) != null) {
					Object[] co = (Object[]) readco;
					category = co[0];
					object = co[1];
					
					
					if ((char) category == 'g') // g for game related
						received.add(object);
					else if ((char) category == 'm') // m for messages
						receivedM.add((String) object);
					else if ((char) category == 't') { // t for test
						if (pingNumber == (long) object)
							pingResult = true; // if it is what I send, set pingResult to true
						else
							setSend('t', object); // otherwise it should be friend's, so send it back
					}
					
					pingTime = System.currentTimeMillis();
				}
			} catch (Exception ex) {
				if (TESTING) System.out.println("Read ex");
				close();
			}
		}
	}
	
	// creates socket and tries to connect until successful
	// gets socket's output stream
	// then writes (sends) objects from send queue to friend
	private class Client implements Runnable {
		private String ip;
		private int tcp;
		
		public Client(String anIP, int aTCP) {
			ip = anIP;
			tcp = aTCP;
		}
		
		public void run() {			
			while (!Thread.currentThread().isInterrupted())
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress(ip, tcp), 1000);
					out = new ObjectOutputStream(socket.getOutputStream());
					break;
				} catch (Exception ex) {
					if (TESTING) System.out.println("Client connection ex");
				}

				write();
				
				try { // when interrupted
					socket.close();
				} catch (Exception ex) {
					if (TESTING) System.out.println("socket close ex");
				}
			
			}
		
		// writes objects from send queue to socket's output stream
		// if connection exception, closes connection
		private void write() {
			while (!Thread.interrupted())				
				try {
					while (!send.isEmpty())
						out.writeObject(send.remove());
				} catch (Exception ex) {
					if (TESTING) System.out.println("Write ex");
					close();
				}
		}
		
	}
	
	// the goal is to test if connection was established
	// the result is connected value ( connected() )
	// starts Ping thread
	private class PingTest implements Runnable {
		public void run() {
			Thread pingT = new Thread(connection, new Ping());
			if (!Thread.currentThread().isInterrupted()) pingT.start();
			
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				Thread.currentThread().interrupt();
			}
			
			if (pingResult)
				connected = true;
		}
	}
	
	// if nothing was read for last 5 sec, performs a test
	// enqueues on send pingNumber and waits 1 sec
	// if pingResult is false (pingNumber was not received back), closes connection
	private class Ping implements Runnable {
		public Ping() {
			pingTime = System.currentTimeMillis() - 5000;
		}
		
		public void run() {
			while (!Thread.interrupted())
				if (System.currentTimeMillis() - pingTime > 5000) {
					pingResult = false; // assuming negative result, it should be changed in Server: read
					pingNumber = (long) (Math.random() * 1000000);
					setSend('t', pingNumber);
					
					try {
						Thread.sleep(1000);
					} catch (Exception ex) {
						Thread.currentThread().interrupt();
					}
					
					if (!pingResult)
						close();
					
					pingTime = System.currentTimeMillis(); // update pingTime
				}
		}
	}
	
	// starts PingTest thread
	private void pingTest() {
		Thread thread = new Thread(connection, new PingTest());
		if (!Thread.currentThread().isInterrupted()) thread.start();
	}
	
	public LinkedList<Object> getReceived() {
		return received;
	}
	
	public LinkedList<String> getReceivedM() {
		return receivedM;
	}
	
	// enqueues on send array of 2 objects given:
	// category object ('g' - game related, 'm' - message, 't' - test)
	// and actual object o
	public void setSend(char category, Object object) {
		Object[] co = new Object[2];
		co[0] = category;
		co[1] = object;
		send.add(co);
	}
	
	// returns true/false whether connection was established
	public boolean connected() {	
		return connected;
	}
	
	// returns true/false whether connection was closed
	public boolean isClosed() {
		if (clientSocket == null) // if there was no call to serverSocket, connection is considered open
			return false;
		return clientSocket.isClosed() || socket.isClosed();
	}
	
	// closes connection by interrupting all threads
	public void close() {
		connection.interrupt();
		
		try {
			serverSocket.close(); // in case when serverSocket has not been connected yet
			in.close();
			out.close();
		} catch (Exception ex) {
			if (TESTING) System.out.println("Close ex");
		}
	}

}
