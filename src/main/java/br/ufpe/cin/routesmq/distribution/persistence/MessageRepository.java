package br.ufpe.cin.routesmq.distribution.persistence;

import java.util.List;

import br.ufpe.cin.routesmq.distribution.message.ApplicationMessage;
import br.ufpe.cin.routesmq.distribution.message.PeerDestination;
import br.ufpe.cin.routesmq.distribution.message.ServiceDestination;

/**
 * Created by tjamir on 7/1/17.
 */
public interface MessageRepository {

    void addMessage(ApplicationMessage message);

    public List<ApplicationMessage> getMessages(PeerDestination destination);


    public List<ApplicationMessage> getMessages(ServiceDestination serviceDestination);

}

