package br.ufpe.cin.routesmq.persistence;

import br.ufpe.cin.routesmq.message.ApplicationMessage;
import br.ufpe.cin.routesmq.message.PeerDestination;
import br.ufpe.cin.routesmq.message.ServiceDestination;

import java.util.List;

/**
 * Created by tjamir on 7/1/17.
 */
public interface MessageRepository {

    public List<ApplicationMessage> getMessages(PeerDestination destination);


    public List<ApplicationMessage> getMessages(ServiceDestination serviceDestination);

}

