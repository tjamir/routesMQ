package br.ufpe.cin.routesmq.message;

import br.ufpe.cin.routesmq.service.PeerDescriptor;

/**
 * Created by tjamir on 6/24/17.
 */
public class PeerDestination implements Destination{

    private PeerDescriptor peerDestination;


    public PeerDescriptor getPeerDestination() {
        return peerDestination;
    }

    public void setPeerDestination(PeerDescriptor peerDestination) {
        this.peerDestination = peerDestination;
    }
}
