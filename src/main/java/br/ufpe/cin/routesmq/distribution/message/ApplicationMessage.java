package br.ufpe.cin.routesmq.distribution.message;

import java.io.Serializable;
import java.util.UUID;

import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 6/24/17.
 */
public abstract class ApplicationMessage extends Message{


    protected UUID messageId;
    public ApplicationMessage(){
        this.messageId=UUID.randomUUID();
    }



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

    public UUID getMessageId() {
        return messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationMessage message = (ApplicationMessage) o;

        return messageId.equals(message.messageId);
    }

    @Override
    public int hashCode() {
        return messageId.hashCode();
    }
}
