package br.ufpe.cin.routesmq.message;

import br.ufpe.cin.routesmq.service.ServiceDescriptor;

/**
 * Created by tjamir on 6/24/17.
 */
public class ServiceDestination implements Destination{

    public ServiceDescriptor getServiceDestination() {
        return serviceDestination;
    }

    public void setServiceDestination(ServiceDescriptor serviceDestination) {
        this.serviceDestination = serviceDestination;
    }

    private ServiceDescriptor serviceDestination;
}
