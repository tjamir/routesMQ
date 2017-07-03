package br.ufpe.cin.routesmq.distribution.packet;

import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 7/2/17.
 */
public class PongPacket extends Packet {


    public PongPacket(PeerDescriptor peerDescriptor, String inetAddress) {
        super(null);
        this.descriptor=peerDescriptor;
        this.inetAddress = inetAddress;
    }


    private PeerDescriptor descriptor;

    private String inetAddress;


    public PeerDescriptor getDescriptor() {
        return descriptor;
    }

    public String getInetAddress() {
        return inetAddress;
    }
}
