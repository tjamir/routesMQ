package br.ufpe.cin.routesmq.message;

import br.ufpe.cin.routesmq.service.PeerDescriptor;

import java.io.Serializable;

/**
 * Created by tjamir on 6/24/17.
 */
public abstract class Message implements Serializable{

    protected Destination destination;

    protected PeerDescriptor source;

    protected byte[] payload;


    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public PeerDescriptor getSource() {
        return source;
    }

    public void setSource(PeerDescriptor source) {
        this.source = source;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
