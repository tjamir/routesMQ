package br.ufpe.cin.routesmq.distribution.packet;

import br.ufpe.cin.routesmq.distribution.message.Message;

/**
 * Created by tjamir on 6/24/17.
 */
public class DirectPacket extends Packet {

    private String hostAddress;

    private int port;

    public DirectPacket(Message message, String hostAddress, int port) {
        super(message);
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
