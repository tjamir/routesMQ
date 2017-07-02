package br.ufpe.cin.routesmq.distribution.packet;

import br.ufpe.cin.routesmq.distribution.message.Message;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

import java.util.List;

/**
 * Created by tjamir on 7/1/17.
 */
public class GossipPacket extends Packet{

    private List<PeerDescriptor> visitedList;


    public GossipPacket(Message message) {
        super(message);
    }

    public List<PeerDescriptor> getVisitedList() {
        return visitedList;
    }

    public void setVisitedList(List<PeerDescriptor> visitedList) {
        this.visitedList = visitedList;
    }
}
