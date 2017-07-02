package br.ufpe.cin.routesmq.distribution.packet;

import br.ufpe.cin.routesmq.distribution.message.Message;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 7/2/17.
 */
public class PongPacket extends Packet {


    public PongPacket(PeerDescriptor peerDescriptor) {
        super(null);
        this.descriptor=peerDescriptor;
    }


    private PeerDescriptor descriptor;


    public PeerDescriptor getDescriptor() {
        return descriptor;
    }
}
