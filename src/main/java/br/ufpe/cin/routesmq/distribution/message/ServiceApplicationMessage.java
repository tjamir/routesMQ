package br.ufpe.cin.routesmq.distribution.message;

/**
 * Created by tjamir on 7/1/17.
 */
public class ServiceApplicationMessage extends ApplicationMessage {


    private ServiceDestination destination;

    public ServiceDestination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination= (ServiceDestination) destination;
    }
}
