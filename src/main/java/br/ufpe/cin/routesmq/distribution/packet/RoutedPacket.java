package br.ufpe.cin.routesmq.distribution.packet;

import java.util.List;

import br.ufpe.cin.routesmq.distribution.message.Message;
import br.ufpe.cin.routesmq.distribution.message.PeerDestination;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 7/1/17.
 */
public class RoutedPacket extends Packet {


    private List<PeerDescriptor> route;


    public RoutedPacket(Message message, List<PeerDescriptor> route) {
        super(message);
    }

    public List<PeerDescriptor> getRoute() {
        return route;
    }
}
