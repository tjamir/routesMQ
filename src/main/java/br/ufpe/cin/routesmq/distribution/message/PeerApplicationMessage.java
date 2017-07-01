package br.ufpe.cin.routesmq.distribution.message;

/**
 * Created by tjamir on 7/1/17.
 */
public class PeerApplicationMessage extends ApplicationMessage {


    private PeerDestination peerDestination;


    public PeerDestination getDestination() {
        return peerDestination;
    }

    public void setDestination(Destination destination) {
        this.peerDestination = (PeerDestination) destination;
    }
}
