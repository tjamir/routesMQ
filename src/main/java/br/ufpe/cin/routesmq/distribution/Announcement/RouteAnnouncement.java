package br.ufpe.cin.routesmq.distribution.Announcement;

import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 7/1/17.
 */
public class RouteAnnouncement extends Announcement{


    private PeerDescriptor source;

    private PeerDescriptor destination;


    public RouteAnnouncement(PeerDescriptor source, PeerDescriptor destination) {
        this.source = source;
        this.destination = destination;
    }

    public PeerDescriptor getSource() {
        return source;
    }

    public PeerDescriptor getDestination() {
        return destination;
    }
}
