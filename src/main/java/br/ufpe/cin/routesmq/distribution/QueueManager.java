package br.ufpe.cin.routesmq.distribution;

import br.ufpe.cin.routesmq.distribution.Announcement.AnnouncementListener;
import br.ufpe.cin.routesmq.distribution.Announcement.RouteAnnouncement;
import br.ufpe.cin.routesmq.distribution.Announcement.ServiceAnnouncement;
import br.ufpe.cin.routesmq.distribution.message.*;
import br.ufpe.cin.routesmq.distribution.persistence.MapDBMessageRepository;
import br.ufpe.cin.routesmq.distribution.persistence.MessageRepository;
import br.ufpe.cin.routesmq.services.DirectMessageListener;
import br.ufpe.cin.routesmq.services.ServiceMessageListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tjamir on 7/2/17.
 */
public class QueueManager implements AnnouncementListener{

    private final static int NUMBER_OF_WORKERS=10;

    private MessageRouter messageRouter;

    private MessageRepository messageRepository;

    private ExecutorService incomingWorkers;

    private ExecutorService outgoingWorkers;







    private List<DirectMessageListener> directMessageListeners;
    private List<ServiceMessageListener> serviceMessageListeners;




    public  void init() throws IOException {
        MapDBMessageRepository mapDBMessageRepository = new MapDBMessageRepository();
        messageRepository = mapDBMessageRepository;
        incomingWorkers = Executors.newFixedThreadPool(NUMBER_OF_WORKERS);
        outgoingWorkers = Executors.newFixedThreadPool(NUMBER_OF_WORKERS);
        directMessageListeners = new CopyOnWriteArrayList<>();
        serviceMessageListeners=new CopyOnWriteArrayList<>();

        mapDBMessageRepository.setLocalFile(messageRouter.getMe().getPeerId().toString());
        mapDBMessageRepository.init();

    }



    public void queueMessage(ApplicationMessage message){
       messageRepository.addMessage(message);
       outgoingWorkers.submit(new OutgoingQueueProcessor(message));

    }

    public void processMessage(ApplicationMessage message) {
       incomingWorkers.submit(new IncomingQueueProcessor(message));
    }

    public void addDirectMessageLisener(DirectMessageListener listener) {
        directMessageListeners.add(listener);
    }

    public void addServiceMessageListener(ServiceMessageListener listener) {
        serviceMessageListeners.add(listener);
    }

    @Override
    public void routeDiscovered(RouteAnnouncement routeAnnouncement) {
        System.out.println("Route found to"+routeAnnouncement.getDestination());
        messageRepository.getMessages(new PeerDestination(routeAnnouncement.getDestination())).forEach(message ->
        outgoingWorkers.submit(new OutgoingQueueProcessor(message)));
    }

    @Override
    public void serviceDiscovered(ServiceAnnouncement serviceAnnouncement) {

    }


    class IncomingQueueProcessor implements Runnable{


        ApplicationMessage message;

        public IncomingQueueProcessor(ApplicationMessage message) {
            this.message = message;
        }

        @Override
        public void run() {
            Destination destination=message.getDestination();
            if(destination instanceof PeerDestination ){
                PeerDestination peerDestination = (PeerDestination) destination;
                if(peerDestination.getDestinationPeer().equals(messageRouter.getMe())){
                    try{
                        directMessageListeners.forEach(l->l.onMessage((PeerApplicationMessage)message));
                    }catch (Throwable t){
                        t.printStackTrace();
                    }

                }else{
                    queueMessage(message);
                }

            }else if(destination instanceof ServiceDestination) {
                ServiceDestination serviceDestination = (ServiceDestination) destination;
                if (messageRouter.getServiceList().containsKey(serviceDestination.getDestination())
                        && messageRouter.getServiceList().get(serviceDestination.getDestination())
                        .contains(messageRouter.getMe())) {
                    serviceMessageListeners.forEach(l -> l.onMessage((ServiceApplicationMessage) message));

                }else {
                    queueMessage(message);
                }

            }

        }
    }


    class OutgoingQueueProcessor implements Runnable{


        ApplicationMessage message;

        public OutgoingQueueProcessor(ApplicationMessage message) {
            this.message = message;
        }

        @Override
        public void run() {
            if(messageRouter.sendMessage(message)){
                messageRepository.removeMessage(message.getDestination(), message);
            }

        }
    }

    public void setMessageRouter(MessageRouter messageRouter) {
        this.messageRouter = messageRouter;
    }
}
