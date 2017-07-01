package br.ufpe.cin.routesmq.distribution.persistence;

import br.ufpe.cin.routesmq.distribution.message.ApplicationMessage;
import br.ufpe.cin.routesmq.distribution.message.PeerApplicationMessage;
import br.ufpe.cin.routesmq.distribution.message.PeerDestination;
import br.ufpe.cin.routesmq.distribution.message.ServiceApplicationMessage;
import br.ufpe.cin.routesmq.distribution.message.ServiceDestination;
import br.ufpe.cin.routesmq.distribution.message.*;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.IndexTreeList;

import java.io.File;
import java.util.List;

/**
 * Created by tjamir on 7/1/17.
 */
public class MapDBMessageRepository implements MessageRepository {


    private DB db;

    public void init(){
        db = DBMaker.fileDB(new File("localdata")).transactionEnable().make();

    }

    public void addMessage (ApplicationMessage message){
        if(message instanceof PeerApplicationMessage){
            addPeerMessage((PeerApplicationMessage) message);
        }else if( message instanceof ServiceApplicationMessage){
            addServiceMessage((ServiceApplicationMessage) message);
        }
    }

    private void addServiceMessage(ServiceApplicationMessage message) {
        DB.IndexTreeListMaker<Object> treeListMaker = db.indexTreeList("service:" + message.getDestination().getDestination().getServiceUUid().toString());
        IndexTreeList<Object> list = treeListMaker.createOrOpen();
        list.add(message);
    }

    private void addPeerMessage(PeerApplicationMessage message) {
        DB.IndexTreeListMaker<Object> treeListMaker = db.indexTreeList("peer:" + message.getDestination().getDestinationPeer().getPeerId().toString());
        IndexTreeList<Object> list = treeListMaker.createOrOpen();
        list.add(message);
    }

    public List<ApplicationMessage> getMessages(PeerDestination destination) {
        return null;
    }

    public List<ApplicationMessage> getMessages(ServiceDestination serviceDestination) {
        return null;
    }
}
