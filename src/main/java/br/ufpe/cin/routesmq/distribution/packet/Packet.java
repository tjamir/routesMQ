package br.ufpe.cin.routesmq.distribution.packet;

import br.ufpe.cin.routesmq.distribution.message.Message;

import java.io.Serializable;

/**
 * Created by tjamir on 7/1/17.
 */
public class Packet implements Serializable{


    public Message getMessage() {
        return message;
    }


    protected Message message;

    public Packet(Message message) {
        this.message = message;
    }
}
