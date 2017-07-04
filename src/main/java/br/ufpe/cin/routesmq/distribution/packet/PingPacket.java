package br.ufpe.cin.routesmq.distribution.packet;


import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 7/1/17.
 */
public class PingPacket extends Packet{
    public PingPacket(PeerDescriptor peerDescriptor, String inetAddress) {
        super(null);
        this.peerDescriptor = peerDescriptor;
        this.inetAddress = inetAddress;
    }

    private PeerDescriptor peerDescriptor;

    private String inetAddress;

    public String getInetAddress() {
        return inetAddress;
    }

    public PeerDescriptor getPeerDescriptor() {
        return peerDescriptor;
    }
}
