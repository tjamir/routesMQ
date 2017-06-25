package br.ufpe.cin.routesmq.service;

import sun.print.ServiceDialog;

import java.io.Serializable;

/**
 * Created by tjamir on 6/24/17.
 */
public class ServiceAnnouncement implements Serializable{

    private ServiceDescriptor serviceDescriptor;

    private PeerDescriptor peerDescriptor;


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
