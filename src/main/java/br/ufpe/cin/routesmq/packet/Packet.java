package br.ufpe.cin.routesmq.packet;

import br.ufpe.cin.routesmq.message.Message;

/**
 * Created by tjamir on 7/1/17.
 */
public class Packet {


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    protected Message message;
}
