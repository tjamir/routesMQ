package br.ufpe.cin.routesmq;

import br.ufpe.cin.routesmq.distribution.Configuration;
import br.ufpe.cin.routesmq.distribution.Seed;
import br.ufpe.cin.routesmq.distribution.message.PeerDestination;
import br.ufpe.cin.routesmq.distribution.message.ServiceDestination;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;
import br.ufpe.cin.routesmq.distribution.service.ServiceDescriptor;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by tjamir on 7/3/17.
 */
public class App {


    public static void main(String[] args) throws IOException {
        RoutesMQ routesMQ1 = RoutesMQ.create(new Configuration());
        int port=routesMQ1.getPort();
        RoutesMQ routesMQ2=RoutesMQ.create(
                new Configuration().addSeed(new Seed("127.0.0.1", port))
        );

        RoutesMQ routesMQ3=RoutesMQ.create(
                new Configuration().addSeed(new Seed("127.0.0.1", port))
        );

        routesMQ2.addDirectMessageListener(message -> System.out.println("Received message from"
                +message.getSource().getPeerId()+": "+ new String(message.getPayload())));
        PeerDescriptor peerDescriptor=new PeerDescriptor();
        peerDescriptor.setPeerId(routesMQ2.getPeerId());
        PeerDestination peerDestination=new PeerDestination(peerDescriptor);
        routesMQ3.sendMessage("Hello".getBytes(), peerDestination);

        RoutesMQ routesMQ4=RoutesMQ.create(
                new Configuration().addSeed(new Seed("127.0.0.1", port))
        );
        ServiceDescriptor serviceDescriptor=new ServiceDescriptor();
        serviceDescriptor.setServiceDescription("this is myService");
        serviceDescriptor.setServiceName("myService");
        serviceDescriptor.setServiceUUid(UUID.randomUUID());

        routesMQ4.addServiceMessageLister(serviceDescriptor, message -> {
            System.out.println("Message to myService from "+message.getSource()+": "+new String(message.getPayload()));
        });

        RoutesMQ routesMQ5=RoutesMQ.create(
                new Configuration().addSeed(new Seed("127.0.0.1", port))
        );
        ServiceDestination serviceDestination=new ServiceDestination(serviceDescriptor);
        routesMQ5.sendMessage("Hello Service".getBytes(), serviceDestination);



    }
}
