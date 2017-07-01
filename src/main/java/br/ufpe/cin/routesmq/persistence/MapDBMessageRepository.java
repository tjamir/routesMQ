package br.ufpe.cin.routesmq.persistence;

import br.ufpe.cin.routesmq.message.*;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.IndexTreeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tjamir on 7/1/17.
 */
public class MapDBMessageRepository implements MessageRepository {


    private DB db;

    public void init(){
        db = DBMaker.fileDB(new File("localdata")).transactionEnable().make();

    }

    @Override
    public void addMessage(ApplicationMessage message){
        if(message instanceof PeerApplicationMessage){
            addPeerMessage((PeerApplicationMessage) message);
        }else if( message instanceof ServiceApplicationMessage){
            addServiceMessage((ServiceApplicationMessage) message);
        }
    }

    private void addServiceMessage(ServiceApplicationMessage serviceApplicationMessage) {
        IndexTreeList<Object> list = getServiceMessageList(serviceApplicationMessage.getDestination());
        list.add(serviceApplicationMessage);
    }

    private IndexTreeList<Object> getServiceMessageList(ServiceDestination service) {
        DB.IndexTreeListMaker<Object> treeListMaker = db.indexTreeList("service:" + service.getDestination().getServiceUUid().toString());
        return treeListMaker.createOrOpen();
    }

    private void addPeerMessage(PeerApplicationMessage message) {
        IndexTreeList<Object> list = getPeerMessageList(message.getDestination());
        list.add(message);
    }

    private IndexTreeList<Object> getPeerMessageList(PeerDestination peerDestination) {
        DB.IndexTreeListMaker<Object> treeListMaker = db.indexTreeList("peer:" + peerDestination.
                getDestinationPeer().getPeerId().toString());
        return treeListMaker.createOrOpen();
    }

    public List<ApplicationMessage> getMessages(PeerDestination destination) {
        List<ApplicationMessage> list =new ArrayList<>();
        list.addAll(getPeerMessageList(destination).stream().map(o -> (PeerApplicationMessage)o).collect(Collectors.toList()));
        return list;

    }

    public List<ApplicationMessage> getMessages(ServiceDestination serviceDestination) {
        List<ApplicationMessage> list =new ArrayList<>();
        list.addAll(getServiceMessageList(serviceDestination).stream().map(o -> (PeerApplicationMessage)o).collect(Collectors.toList()));
        return list;
    }
}
