package br.ufpe.cin.routesmq.distribution.packet;


/**
 * Created by tjamir on 7/1/17.
 */
public class PingPacket extends Packet{
    public PingPacket(String inetAddress) {
        super(null);
        this.inetAddress = inetAddress;
    }

    private String inetAddress;

    public String getInetAddress() {
        return inetAddress;
    }
}
