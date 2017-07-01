package br.ufpe.cin.routesmq.infrastructutre;

import java.io.IOException;

/**
 * Created by tjamir on 6/23/17.
 */
public interface ServerRequestHandler {

	public void RequestHandler(int port) throws IOException;
	
    byte[] receiveRequest() throws IOException, Throwable;

    void sendReply(byte[] data) throws IOException, InterruptedException;

}
