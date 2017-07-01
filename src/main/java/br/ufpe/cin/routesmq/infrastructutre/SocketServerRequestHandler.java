package br.ufpe.cin.routesmq.infrastructutre;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tjamir on 6/23/17.
 */
public class SocketServerRequestHandler {
	private int port;
	private ServerSocket Socket = null;

	Socket connectionSocket = null;

	int sentMessageSize;
	int receivedMessageSize;
	DataOutputStream outToClient = null;
	DataInputStream inFromClient = null;

	public void RequestHandler(int port) throws IOException {
		this.port = port;
		this.Socket = new ServerSocket(this.port);
	}

	public byte[] receiveRequest() throws IOException, Throwable {

		byte[] data = null;

		connectionSocket = Socket.accept();

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

		connectionSocket.close();
		outToClient.close();
		inFromClient.close();

		return;
	}

}
