package br.ufpe.cin.routesmq.services;

import br.ufpe.cin.routesmq.distribution.message.ServiceApplicationMessage;

/**
 * Created by tjamir on 7/3/17.
 */
public interface ServiceMessageListener {

    void onMessage(ServiceApplicationMessage message);

}
