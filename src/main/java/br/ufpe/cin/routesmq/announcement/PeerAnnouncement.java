package br.ufpe.cin.routesmq.announcement;

import br.ufpe.cin.routesmq.service.PeerDescriptor;

/**
 * Created by tjamir on 7/1/17.
 */
public class PeerAnnouncement extends Announcement {

    private PeerDescriptor peerDescriptor;

    public PeerAnnouncement(PeerDescriptor peerDescriptor) {
        this.peerDescriptor = peerDescriptor;
    }

    public PeerDescriptor getPeerDescriptor() {
        return peerDescriptor;
    }
}
