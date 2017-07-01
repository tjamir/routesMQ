package br.ufpe.cin.routesmq.packet;

import br.ufpe.cin.routesmq.message.ApplicationMessage;

/**
 * Created by tjamir on 6/24/17.
 */
public class DirectPacket extends Packet {

    private String hostAddress;

    private int port;

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
