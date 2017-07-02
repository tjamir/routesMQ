package br.ufpe.cin.routesmq.distribution;

/**
 * Created by tjamir on 7/1/17.
 */
public class Seed {

    private String host;

    private int port;


    public Seed(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
