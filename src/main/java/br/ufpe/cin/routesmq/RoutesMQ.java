package br.ufpe.cin.routesmq;

import br.ufpe.cin.routesmq.distribution.Configuration;
import br.ufpe.cin.routesmq.distribution.PeerManager;
import br.ufpe.cin.routesmq.distribution.message.Destination;
import br.ufpe.cin.routesmq.distribution.message.PeerApplicationMessage;
import br.ufpe.cin.routesmq.distribution.message.PeerDestination;
import br.ufpe.cin.routesmq.distribution.service.ServiceDescriptor;
import br.ufpe.cin.routesmq.services.DirectMessageListener;
import br.ufpe.cin.routesmq.services.ServiceMessageListener;

import java.io.IOException;
import java.net.SocketException;
import java.util.UUID;

/**
 * Created by tjamir on 7/3/17.
 */
public class RoutesMQ {



    public static RoutesMQ create(Configuration configuration) throws IOException {
        RoutesMQ routesMQ=new RoutesMQ();
        routesMQ.peerManager = new PeerManager();

        routesMQ.peerManager.init(configuration.getPeerid(), configuration.getPort(), configuration.getSeeds(),
                configuration.getPingInterval(), configuration.getAnnouncementInterval());
        return routesMQ;
    }

    private PeerManager peerManager;

    public void addDirectMessageListener(DirectMessageListener listener){
        this.peerManager.getQueueManager().addDirectMessageLisener(listener);
    }

    public  void addServiceMessageLister(ServiceDescriptor serviceDescriptor, ServiceMessageListener listener){
        this.peerManager.provideService(serviceDescriptor);
        this.peerManager.getQueueManager().addServiceMessageListener(listener);

    }

    public void sendMessage(byte[] message, Destination destination){
       if(destination instanceof PeerDestination){
           PeerApplicationMessage peerApplicationMessage=new PeerApplicationMessage();
           peerApplicationMessage.setDestination(destination);
           peerApplicationMessage.setPayload(message);
           peerApplicationMessage.setSource(peerManager.getMe());
           this.peerManager.getQueueManager().queueMessage(peerApplicationMessage);
       }

    }


    public UUID getPeerId() {
        return peerManager.getMe().getPeerId();
    }

    public int getPort(){
        return peerManager.getPort();
    }
}
