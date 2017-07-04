package br.ufpe.cin.routesmq.infrastructutre;

import br.ufpe.cin.routesmq.distribution.Marshaller;
import br.ufpe.cin.routesmq.distribution.MessageRouter;
import br.ufpe.cin.routesmq.distribution.QueueManager;
import br.ufpe.cin.routesmq.distribution.packet.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tjamir on 6/23/17.
 */
public class SocketServerRequestHandler {

	int numberOfThreads=10;


	private int port;
	private ServerSocket socket = null;

	private MessageRouter router;


	private Marshaller marshaller;



	public void start() throws IOException {
		socket=new ServerSocket(port);
		Runnable service= () ->
		{

			ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
			while(true){

				try {
					Socket clientSocket=socket.accept();
					executorService.submit(new Worker(clientSocket));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(service, "server-request.handler").start();
	}


	public void setRouter(MessageRouter router) {
		this.router = router;
	}

	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	public SocketServerRequestHandler(int port) {
		this.port = port;
	}

	class Worker implements Runnable{

		Socket connectionSocket = null;

		int sentMessageSize;
		int receivedMessageSize;
		DataOutputStream outToClient = null;
		DataInputStream inFromClient = null;

		public Worker(java.net.Socket connectionSocket) {
			this.connectionSocket = connectionSocket;
		}

		byte[] receiveRequest() throws IOException {

			byte[] data = null;


			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			inFromClient = new DataInputStream(connectionSocket.getInputStream());

			receivedMessageSize = inFromClient.readInt();
			data = new byte[receivedMessageSize];

			inFromClient.read(data, 0, receivedMessageSize);

			return data;
		}

		public void sendReply(byte[] data) throws IOException, InterruptedException {

			sentMessageSize = data.length;
			outToClient.writeInt(sentMessageSize);
			outToClient.write(data);
			outToClient.flush();

			close();
		}

		private void close() throws IOException {
			connectionSocket.close();
			outToClient.close();
			inFromClient.close();
		}


		@Override
		public void run() {
			try {
				byte[] data=receiveRequest();
				byte[] reply=processRequest(data);
				if(reply!=null)
					sendReply(reply);
				else
					close();


			} catch (Exception exception) {
				exception.printStackTrace();
			}

		}
	}

	private byte[] processRequest(byte[] data) throws IOException, ClassNotFoundException {
		Packet packet = (Packet) marshaller.unMarshall(data);


		if(packet instanceof PingPacket){
			router.processPingPacket((PingPacket) packet);
			PongPacket pongPacket=new PongPacket(router.getMe(), ((PingPacket) packet).getInetAddress());
			return marshaller.marshall(pongPacket);
		}
		if(packet instanceof GossipPacket){
			GossipPacket gossipPacket= (GossipPacket) packet;
			router.processGossipPacket(gossipPacket);
			return null;
		}
		if(packet instanceof RoutedPacket){
			RoutedPacket routedPacket= (RoutedPacket) packet;
			router.processRoutedPacket(routedPacket);
			AckPacket ackPacket = new AckPacket();
			return marshaller.marshall(ackPacket);
		}
		if(packet instanceof DirectPacket){
			DirectPacket directPacket= (DirectPacket) packet;
			router.processDirectPacket(directPacket);
			AckPacket ackPacket = new AckPacket();
			return marshaller.marshall(ackPacket);
		}

		throw new UnsupportedOperationException("Unknown Package Type");



	}


}
