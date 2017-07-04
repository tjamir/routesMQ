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

    public String toString(){
        return host+":"+port;
    }

    public static Seed fromString(String str){
        String [] pars=str.split(":");
        return new Seed(pars[0], Integer.parseInt(pars[1]));
    }
}
