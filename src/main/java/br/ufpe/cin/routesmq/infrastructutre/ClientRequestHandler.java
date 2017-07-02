package br.ufpe.cin.routesmq.infrastructutre;

import java.io.IOException;

/**
 * Created by tjamir on 6/23/17.
 */
public interface ClientRequestHandler {
	

	public byte[] send(byte[] data) throws IOException, InterruptedException;
	
	public byte[] receive() throws IOException, InterruptedException;
	
	public boolean isExpectedReply();
	
	public void setExpectedReply(boolean expectedReply);


}
