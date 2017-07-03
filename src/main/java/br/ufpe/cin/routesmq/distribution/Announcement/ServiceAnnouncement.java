package br.ufpe.cin.routesmq.distribution.Announcement;

import br.ufpe.cin.routesmq.distribution.Announcement.Announcement;
import br.ufpe.cin.routesmq.distribution.message.ServiceDestination;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;
import br.ufpe.cin.routesmq.distribution.service.ServiceDescriptor;
import sun.print.ServiceDialog;

import java.io.Serializable;

/**
 * Created by tjamir on 6/24/17.
 */
public class ServiceAnnouncement extends Announcement{

    private ServiceDescriptor serviceDescriptor;

    private PeerDescriptor peerDescriptor;

    public ServiceAnnouncement(ServiceDescriptor serviceDescriptor, PeerDescriptor peerDescriptor) {
        this.serviceDescriptor = serviceDescriptor;
        this.peerDescriptor = peerDescriptor;
    }

    public ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

    public void setServiceDescriptor(ServiceDescriptor serviceDescriptor) {
        this.serviceDescriptor = serviceDescriptor;
    }

    public PeerDescriptor getPeerDescriptor() {
        return peerDescriptor;
    }

    public void setPeerDescriptor(PeerDescriptor peerDescriptor) {
        this.peerDescriptor = peerDescriptor;
    }
}
