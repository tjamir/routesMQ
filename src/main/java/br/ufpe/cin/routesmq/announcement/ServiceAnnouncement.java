package br.ufpe.cin.routesmq.announcement;

import br.ufpe.cin.routesmq.message.ServiceDestination;

/**
 * Created by tjamir on 7/1/17.
 */
public class ServiceAnnouncement extends Announcement{

    private ServiceDestination serviceDestination;


    public ServiceAnnouncement(ServiceDestination serviceDestination) {
        this.serviceDestination = serviceDestination;
    }

    public ServiceDestination getServiceDestination() {
        return serviceDestination;
    }
}
