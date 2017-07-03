package br.ufpe.cin.routesmq.services;

import br.ufpe.cin.routesmq.distribution.message.PeerApplicationMessage;

/**
 * Created by tjamir on 7/3/17.
 */
public interface DirectApplicationMessageListener {

    void onMessage(PeerApplicationMessage message);

}
