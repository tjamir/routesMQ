package br.ufpe.cin.routesmq.infrastructutre;

/**
 * Created by tjamir on 6/23/17.
 */
public interface ClientRequestHandler {

    public byte[] send(byte[] data, String host, int port);


}
