package br.ufpe.cin.routesmq.distribution.message;

import br.ufpe.cin.routesmq.distribution.service.ServiceDescriptor;

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
