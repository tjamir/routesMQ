package br.ufpe.cin.routesmq.message;

import br.ufpe.cin.routesmq.service.ServiceDescriptor;

/**
 * Created by tjamir on 6/24/17.
 */
public class ServiceDestination implements Destination{

    private ServiceDescriptor serviceDestination;

    public ServiceDestination(ServiceDescriptor serviceDestination) {
        this.serviceDestination = serviceDestination;
    }

    public ServiceDescriptor getDestination(){
        return serviceDestination;
    }
}
