package br.ufpe.cin.routesmq.distribution;

import br.ufpe.cin.routesmq.distribution.message.ApplicationMessage;
import br.ufpe.cin.routesmq.distribution.message.Destination;
import br.ufpe.cin.routesmq.distribution.message.PeerApplicationMessage;
import br.ufpe.cin.routesmq.distribution.message.PeerDestination;
import br.ufpe.cin.routesmq.distribution.persistence.MapDBMessageRepository;
import br.ufpe.cin.routesmq.distribution.persistence.MessageRepository;
import br.ufpe.cin.routesmq.services.DirectApplicationMessageListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tjamir on 7/2/17.
 */
public class QueueManager {

    private final static int NUMBER_OF_WORKERS=10;

    private MessageRouter messageRouter;

    private MessageRepository messageRepository;

    private ExecutorService incomingWorkers;

    private ExecutorService outgoingWorkers;




    private List<DirectApplicationMessageListener> directMessageListeners;



    public  void init(){
        messageRepository = new MapDBMessageRepository();
        incomingWorkers = Executors.newFixedThreadPool(NUMBER_OF_WORKERS);
        outgoingWorkers = Executors.newFixedThreadPool(NUMBER_OF_WORKERS);
    }



    public void queueMessage(ApplicationMessage message){
       messageRepository.addMessage(message);
       outgoingWorkers.submit(new OutgoingQueueProcessor(message));

    }

    public void processMessage(ApplicationMessage message) {
       incomingWorkers.submit(new IncomingQueueProcessor(message));
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
                if(((PeerDestination) destination).getDestinationPeer().equals(messageRouter.getMe())){
                    try{
                        directMessageListeners.forEach(l->l.onMessage((PeerApplicationMessage)message));
                    }catch (Throwable t){
                        t.printStackTrace();
                    }

                }else{
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


}
