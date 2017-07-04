package br.ufpe.cin.routesmq.distribution;

import br.ufpe.cin.routesmq.distribution.Announcement.Announcement;
import br.ufpe.cin.routesmq.distribution.Announcement.PeerAnnouncement;
import br.ufpe.cin.routesmq.distribution.Announcement.RouteAnnouncement;
import br.ufpe.cin.routesmq.distribution.Announcement.ServiceAnnouncement;
import br.ufpe.cin.routesmq.distribution.message.AnnouncementMessage;
import br.ufpe.cin.routesmq.distribution.message.ServiceDestination;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;
import br.ufpe.cin.routesmq.distribution.service.ServiceDescriptor;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by tjamir on 7/1/17.
 */
public class PeerManager {




    private PeerDescriptor me;

    private List<Seed> seeds;

    private List<PeerAnnouncement> knownPeers;

    private MessageRouter messageRouter;

    private QueueManager queueManager;


    private Set<ServiceDescriptor> providedServices;

    private int port;




    public final static Long DEFAULT_PING_INTERVAL=5000L;

    public final static Long DEFAULT_ANNOUNCE_INTERVAL=5000L;



    public void init(UUID peerId, Integer port, List<Seed> seeds, Long pingInterval, Long announceInterval) throws IOException {

        if(peerId ==null){
            peerId=UUID.randomUUID();
        }
        if(port == null){
            port = Math.abs(((int)peerId.getLeastSignificantBits()%10000)+1024);

        }

        this.port = port;

        if(pingInterval==null){
            pingInterval=DEFAULT_PING_INTERVAL;
        }

        if(announceInterval==null){
            announceInterval = DEFAULT_ANNOUNCE_INTERVAL;
        }
        if(seeds==null){
            seeds=new ArrayList<>();
        }

        List<String> networkInterfaces=getNetwokrInterfaces();
        me=new PeerDescriptor();
        me.setLocalInterfaces(networkInterfaces);
        me.setPeerId(peerId);
        me.setPort(port);
        this.seeds=seeds;


        this.knownPeers = new CopyOnWriteArrayList<>();
        this.providedServices =new CopyOnWriteArraySet<>();



       this.me = new PeerDescriptor();
       me.setPort(port);
       me.setPeerId(peerId);
       me.setLocalInterfaces(networkInterfaces);



       this.messageRouter=new MessageRouter();
       this.queueManager = new QueueManager();

       messageRouter.setQueueManager(queueManager);
       queueManager.setMessageRouter(messageRouter);



       messageRouter.setMe(me);
       messageRouter.setMarshaller(new Marshaller());
       messageRouter.init();

       queueManager.init();

       messageRouter.addAnnouncementListener(queueManager);

        Timer timer =new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pingKnownPeers();
            }
        }, 0L, pingInterval);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendLocalAnnouncements();
            }
        }, 0L, announceInterval);

    }

    private void sendLocalAnnouncements() {
        List<Announcement> announcements=new ArrayList<>();
        PeerAnnouncement peerAnnouncement =new PeerAnnouncement(me);
        announcements.add(peerAnnouncement);
        for(PeerDescriptor peer:messageRouter.getReachablePeers()){
            announcements.add(new RouteAnnouncement(me, peer));
        }
        for(ServiceDescriptor serviceDescriptor:providedServices){
            announcements.add(new ServiceAnnouncement(serviceDescriptor, me));
        }

        AnnouncementMessage message = new AnnouncementMessage(announcements);

        messageRouter.broadCastMessage(message);

    }

    private void pingKnownPeers() {
        seeds.forEach(seed -> messageRouter.pingSeed(seed));
        messageRouter.pingKnownPeers();


    }

    private List<String> getNetwokrInterfaces() throws SocketException {
        List<String> networkAddresses = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        for(NetworkInterface networkInterface: Collections.list(networkInterfaces)){
            networkInterface.getInterfaceAddresses().stream().forEach(interfaceAddress -> networkAddresses.add(interfaceAddress.getAddress().getHostAddress()));
        }
        return networkAddresses;
    }


    public QueueManager getQueueManager() {
        return queueManager;
    }

    public void provideService(ServiceDescriptor serviceDescriptor) {
        this.providedServices.add(serviceDescriptor);
        if(!messageRouter.getServiceList().containsKey(serviceDescriptor)){
            messageRouter.getServiceList().put(serviceDescriptor, new CopyOnWriteArrayList<>());
        }

        if(!messageRouter.getServiceList().get(serviceDescriptor).contains(me)){
            messageRouter.getServiceList().get(serviceDescriptor).add(me);
        }

    }

    public PeerDescriptor getMe() {
        return me;
    }


    public int getPort() {
        return port;
    }
}
