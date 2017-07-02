package br.ufpe.cin.routesmq.distribution;

import br.ufpe.cin.routesmq.distribution.message.AnnouncementMessage;
import br.ufpe.cin.routesmq.distribution.message.Message;
import br.ufpe.cin.routesmq.distribution.packet.GossipPacket;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tjamir on 7/1/17.
 */
public class MessageRouter {


    private Map<PeerDescriptor, List<PeerDescriptor>> topology;

    private Map<PeerDescriptor, String> reachableRoutes;

    public List<PeerDescriptor> getReachablePeers(){
        return new ArrayList<>(reachableRoutes.keySet());
    }

    public void sendMessage(Message message) {

    }

    public void broadCastMesse(Message message){
        GossipPacket packet = new GossipPacket(message);
        List<PeerDescriptor> list = getReachablePeers();
        packet.setVisitedList(list);
        list.stream().forEach(
                
        );
    }
}
