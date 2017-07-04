package br.ufpe.cin.routesmq.distribution;

import br.ufpe.cin.routesmq.distribution.Announcement.*;
import br.ufpe.cin.routesmq.distribution.message.*;
import br.ufpe.cin.routesmq.distribution.packet.*;
import br.ufpe.cin.routesmq.distribution.service.PeerDescriptor;
import br.ufpe.cin.routesmq.distribution.service.ServiceDescriptor;
import br.ufpe.cin.routesmq.infrastructutre.SocketClientRequestHandler;
import br.ufpe.cin.routesmq.infrastructutre.SocketServerRequestHandler;
import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by tjamir on 7/1/17.
 */
public class MessageRouter {

    private Marshaller marshaller;

    private PeerDescriptor me;


    private Map<PeerDescriptor, List<PeerDescriptor>> topology;

    private Map<PeerDescriptor, String> reachableRoutes;

    private Map<ServiceDescriptor, List<PeerDescriptor>> serviceList;

    private QueueManager queueManager;

    private List<AnnouncementListener> listeners;

    private ExecutorService pingExecutor;

    private Set<ApplicationMessage> outgoingMessages;


    public void init() throws IOException {
        topology = new ConcurrentHashMap<>();
        topology.put(me, new CopyOnWriteArrayList<>());
        reachableRoutes = new ConcurrentHashMap<>();
        serviceList = new ConcurrentHashMap<>();
        listeners = new CopyOnWriteArrayList<>();
        SocketServerRequestHandler socketServerRequestHandler = new SocketServerRequestHandler(me.getPort());
        socketServerRequestHandler.setRouter(this);
        socketServerRequestHandler.setMarshaller(marshaller);
        socketServerRequestHandler.start();
        pingExecutor = Executors.newFixedThreadPool(1);



    }

    public List<PeerDescriptor> getReachablePeers() {
        return new ArrayList<>(reachableRoutes.keySet());
    }

    public boolean sendMessage(Message message) {
        if (message instanceof PeerApplicationMessage) {
            return sendPeerMessage((PeerApplicationMessage) message);
        } else if (message instanceof ServiceApplicationMessage) {
            return sendServiceMessage((ServiceApplicationMessage) message);
        }
        return false;


    }


    public void setQueueManager(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setMe(PeerDescriptor me) {
        this.me = me;
    }

    private boolean sendServiceMessage(ServiceApplicationMessage message) {
        boolean result = false;
        List<PeerDescriptor> destinations = serviceList.get(message.getDestination().getDestination());
        List<List<PeerDescriptor>> routes = destinations.stream().map(peerDescriptor -> findBestRoute(me, peerDescriptor))
                .filter(list -> list != null).sorted(Comparator.comparingInt(List::size)).collect(Collectors.toList());
        if (routes != null && routes.size() > 0) {
            List<PeerDescriptor> route = routes.get(0);
            PeerDescriptor finalDestination = route.get(route.size() - 1);
            result = sendByRoute(message, finalDestination, route);
        }
        return result;

    }

    private boolean sendPeerMessage(PeerApplicationMessage message) {
        PeerDescriptor peerDescriptor = message.getDestination().getDestinationPeer();
        boolean result = false;
        if (topology.containsKey(peerDescriptor)) {
            List<PeerDescriptor> route = findBestRoute(me, peerDescriptor);
            result = sendByRoute(message, peerDescriptor, route);
        }
        return result;

    }

    private boolean sendByRoute(Message message, PeerDescriptor destination, List<PeerDescriptor> route) {
        boolean result = false;
        if (route != null && !route.isEmpty()) {
            PeerDescriptor nextHop = route.get(1);
            Packet packet = null;
            if (route.size() == 2) {
                packet = new DirectPacket(message, reachableRoutes.get(destination), route.get(1).getPort());
            } else {
                packet = new RoutedPacket(message, route);
            }

            try {
                byte[] reply = sendPacket(marshaller.marshall(packet), nextHop, true);
                Packet replyPacket = (Packet) marshaller.unMarshall(reply);
                if (replyPacket instanceof AckPacket) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }

        }
        return result;
    }


    private List<PeerDescriptor> findBestRoute(PeerDescriptor origin, PeerDescriptor peerDestination) {

        DirectedGraph<PeerDescriptor, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        topology.keySet().forEach(peerDescriptor -> graph.addVertex(peerDescriptor));
        topology.entrySet().forEach(e ->
                e.getValue().forEach(v -> graph.addEdge(e.getKey(), v))
        );
        DijkstraShortestPath<PeerDescriptor, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<PeerDescriptor, DefaultEdge>(graph);
        GraphPath<PeerDescriptor, DefaultEdge> path = dijkstraShortestPath.getPath(origin, peerDestination);
        return path.getVertexList();

    }

    public void broadCastMessage(Message message) {
        GossipPacket packet = new GossipPacket(message);
        List<PeerDescriptor> list = getReachablePeers();
        packet.setVisitedList(new ArrayList<>(list));
        packet.getVisitedList().add(me);
        sendGossipPacket(packet, list);

    }

    private void sendGossipPacket(GossipPacket packet, List<PeerDescriptor> list) {
        try {
            byte[] packetData = marshaller.marshall(packet);
            list.stream().forEach(

                    peerDescriptor -> {
                        try {
                            sendPacket(packetData, peerDescriptor, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] sendPacket(byte[] packetData, PeerDescriptor peerDescriptor, boolean expectedResponse) throws IOException, InterruptedException {
        SocketClientRequestHandler socketClientRequestHandler = new SocketClientRequestHandler(reachableRoutes.get(peerDescriptor), peerDescriptor.getPort(),
                expectedResponse);
        socketClientRequestHandler.send(packetData);
        if (expectedResponse) {
            return socketClientRequestHandler.receive();
        } else {
            return null;
        }

    }


    public void pingSeed(Seed seed) {
        PingPacket pingPacket = new PingPacket(me, seed.getHost());

        try {
            SocketClientRequestHandler socketClientRequestHandler = new SocketClientRequestHandler(seed.getHost(), seed.getPort(), true);
            socketClientRequestHandler.send(marshaller.marshall(pingPacket));
            byte[] replyData = socketClientRequestHandler.receive();
            Object reply = marshaller.unMarshall(replyData);
            processPongPacket(reply);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    public void pingKnownPeers() {


        for (PeerDescriptor peerDescriptor : topology.keySet()) {
            pingPeer(peerDescriptor);
        }
    }

    private void pingPeer(PeerDescriptor peerDescriptor) {
        if (!peerDescriptor.equals(me)) {
            for (String host : peerDescriptor.getLocalInterfaces()) {
                PingPacket pingPacket = new PingPacket(me, host);
                try {
                    SocketClientRequestHandler socketClientRequestHandler = new SocketClientRequestHandler(host, peerDescriptor.getPort(), true);
                    socketClientRequestHandler.send(marshaller.marshall(pingPacket));
                    byte[] replyData = socketClientRequestHandler.receive();
                    Object reply = marshaller.unMarshall(replyData);
                    processPongPacket(reply);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processPongPacket(Object reply) {
        if (reply != null) {
            if (reply instanceof PongPacket) {
                PongPacket pongPacket = (PongPacket) reply;
                if (!this.topology.containsKey(pongPacket.getDescriptor())) {
                    topology.put(pongPacket.getDescriptor(), new CopyOnWriteArrayList<>());
                }
                if (!topology.get(me).contains(pongPacket.getDescriptor())) {
                    topology.get(me).add(pongPacket.getDescriptor());
                    try {
                        listeners.forEach(listener -> listener.routeDiscovered(
                                new RouteAnnouncement(me, pongPacket.getDescriptor())));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                this.reachableRoutes.put(pongPacket.getDescriptor(), pongPacket.getInetAddress());
            }
        }
    }

    public PeerDescriptor getMe() {
        return me;
    }

    public void processGossipPacket(GossipPacket gossipPacket) {
        Message message = gossipPacket.getMessage();
        processMessage(message);
        List<PeerDescriptor> nextPeers = reachableRoutes.keySet().stream()
                .filter(peerDescriptor -> !gossipPacket.getVisitedList().contains(peerDescriptor))
                .collect(Collectors.toList());

        if (!nextPeers.isEmpty()) {
            gossipPacket.getVisitedList().addAll(nextPeers);
            sendGossipPacket(gossipPacket, nextPeers);
        }


    }

    private void processMessage(Message message) {

        if (message instanceof AnnouncementMessage) {
            List<Announcement> announcements = ((AnnouncementMessage) message).getAnnouncement();

            announcements.forEach(announcement -> processAnnouncements(announcement));
        } else if (message instanceof ApplicationMessage) {
            queueManager.processMessage((ApplicationMessage) message);
        }
    }

    private void processAnnouncements(Announcement announcement) {
        if (announcement instanceof PeerAnnouncement) {
            PeerAnnouncement peerAnnouncement = (PeerAnnouncement) announcement;
            if (!topology.containsKey(peerAnnouncement.getPeerDescriptor())) {
                topology.put(peerAnnouncement.getPeerDescriptor(), new CopyOnWriteArrayList<>());
            }
        } else if (announcement instanceof RouteAnnouncement) {
            RouteAnnouncement routeAnnouncement = (RouteAnnouncement) announcement;
            if (!topology.containsKey(routeAnnouncement.getSource())) {
                topology.put(routeAnnouncement.getSource(), new CopyOnWriteArrayList<>());
            }
            if (!topology.get(routeAnnouncement.getSource()).contains(routeAnnouncement.getDestination())) {
                topology.get(routeAnnouncement.getSource()).add(routeAnnouncement.getDestination());
                try {
                    listeners.forEach(listener -> listener.routeDiscovered(routeAnnouncement));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } else if (announcement instanceof ServiceAnnouncement) {
            ServiceAnnouncement serviceAnnouncement = (ServiceAnnouncement) announcement;
            if (!serviceList.containsKey(serviceAnnouncement.getServiceDescriptor())) {
                serviceList.put(serviceAnnouncement.getServiceDescriptor(), new CopyOnWriteArrayList<>());
            }
            if (!serviceList.get(serviceAnnouncement.getServiceDescriptor()).contains(serviceAnnouncement.getPeerDescriptor())) {
                serviceList.get(serviceAnnouncement.getServiceDescriptor()).add(serviceAnnouncement.getPeerDescriptor());
                try {
                    listeners.forEach(listener -> listener.serviceDiscovered(serviceAnnouncement));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    public void processRoutedPacket(RoutedPacket routedPacket) {

        processMessage(routedPacket.getMessage());

    }


    public void processDirectPacket(DirectPacket directPacket) {
        processMessage(directPacket.getMessage());
    }

    public Map<ServiceDescriptor, List<PeerDescriptor>> getServiceList() {
        return serviceList;
    }

    public void addAnnouncementListener(AnnouncementListener listener) {
        this.listeners.add(listener);
    }

    public void processPingPacket(PingPacket packet) {

        pingExecutor.execute(() -> {
            PeerDescriptor descriptor = packet.getPeerDescriptor();
            if (!reachableRoutes.containsKey(descriptor)) {
                pingPeer(descriptor);
            }
            if (!topology.containsKey(descriptor)) {
                topology.put(descriptor, new CopyOnWriteArrayList<>());
            }
            if (!topology.get(descriptor).contains(me)) {
                topology.get(descriptor).add(me);
                try {
                    listeners.forEach(listener -> listener.routeDiscovered(new RouteAnnouncement(descriptor, me)));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
