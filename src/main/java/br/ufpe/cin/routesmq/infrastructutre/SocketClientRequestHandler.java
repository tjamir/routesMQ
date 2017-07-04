package br.ufpe.cin.routesmq.infrastructutre;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Created by tjamir on 6/23/17.
 */
public class SocketClientRequestHandler implements ClientRequestHandler{
	private String host;
	private int port;
	private boolean expectedReply;

	int sentMessageSize;
	int receiveMessageSize;

	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	DataInputStream inFromServer = null;

	private ExecutorService executorService;

	public SocketClientRequestHandler(String host, int port, boolean expectedReply) {
		this.host = host;
		this.port = port;
		this.setExpectedReply(expectedReply);
	}



	public byte[] send(byte[] data) throws IOException, InterruptedException {

		clientSocket = new Socket(this.host, this.port);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new DataInputStream(clientSocket.getInputStream());

		sentMessageSize = data.length;
		outToServer.writeInt(sentMessageSize);
		outToServer.write(data, 0, sentMessageSize);
		outToServer.flush();

		if (!this.expectedReply) {
			clientSocket.close();
			outToServer.close();
			inFromServer.close();
		}

		return data;
	}

	public byte[] receive() throws IOException, InterruptedException {

		byte[] data = null;

		if (expectedReply) {
			receiveMessageSize = inFromServer.readInt();
			data = new byte[receiveMessageSize];
			inFromServer.read(data, 0, receiveMessageSize);

			clientSocket.close();
			outToServer.close();
			inFromServer.close();
		}
		return data;
	}



	public boolean isExpectedReply() {
		return expectedReply;
	}

	public void setExpectedReply(boolean expectedReply) {
		this.expectedReply = expectedReply;
	}
}