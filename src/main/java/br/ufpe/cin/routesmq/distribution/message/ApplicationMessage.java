package br.ufpe.cin.routesmq.distribution.message;

import java.io.Serializable;

import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 6/24/17.
 */
public abstract class ApplicationMessage extends Message{


    protected PeerDescriptor source;

    protected byte[] payload;


    public abstract Destination getDestination();

    public abstract void setDestination(Destination destination);

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
