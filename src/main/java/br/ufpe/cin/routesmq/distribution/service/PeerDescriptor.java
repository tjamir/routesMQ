package br.ufpe.cin.routesmq.distribution.service;

import java.util.List;
import java.util.UUID;

/**
 * Created by tjamir on 6/24/17.
 */
public class PeerDescriptor {

    private UUID peerId;

    private List<String> localInterfaces;

    private int port;

    public UUID getPeerId() {
        return peerId;
    }

    public void setPeerId(UUID peerId) {
        this.peerId = peerId;
    }

    public List<String> getLocalInterfaces() {
        return localInterfaces;
    }

    public void setLocalInterfaces(List<String> localInterfaces) {
        this.localInterfaces = localInterfaces;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
