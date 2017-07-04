package br.ufpe.cin.routesmq.distribution.service;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tjamir on 6/24/17.
 */
public class ServiceDescriptor implements Serializable{

    private String serviceName;

    private String serviceDescription;

    private UUID serviceUUid;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public UUID getServiceUUid() {
        return serviceUUid;
    }

    public void setServiceUUid(UUID serviceUUid) {
        this.serviceUUid = serviceUUid;
    }

    @Override
    public String toString() {
        return "ServiceDescriptor{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceDescription='" + serviceDescription + '\'' +
                ", serviceUUid=" + serviceUUid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceDescriptor that = (ServiceDescriptor) o;

        return serviceUUid.equals(that.serviceUUid);
    }

    @Override
    public int hashCode() {
        return serviceUUid.hashCode();
    }
}
