package br.ufpe.cin.routesmq.distribution.Announcement;

import br.ufpe.cin.routesmq.distribution.message.ServiceDestination;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;

/**
 * Created by tjamir on 7/1/17.
 */
public class ServiceAnnouncement extends Announcement{

    private ServiceDestination serviceDestination;


    private PeerDescriptor serviceProvider;


    public ServiceAnnouncement(ServiceDestination serviceDestination, PeerDescriptor serviceProvider) {
        this.serviceDestination = serviceDestination;
        this.serviceProvider = serviceProvider;
    }

    public ServiceDestination getServiceDestination() {
        return serviceDestination;
    }

    public PeerDescriptor getServiceProvider() {
        return serviceProvider;
    }

}
