package br.ufpe.cin.routesmq.distribution.message;

import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 6/24/17.
 */
public class PeerDestination implements Destination{

    private PeerDescriptor peerDestination;


    public PeerDestination(PeerDescriptor peerDestination) {
        this.peerDestination = peerDestination;
    }

    public PeerDescriptor getDestinationPeer(){
        return peerDestination;
    }
}
