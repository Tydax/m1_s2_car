package actors;
import message.GreetingMessage;
import message.PingMessage;
import message.PongMessage;
import akka.actor.UntypedActor;


public class GreetingActor extends UntypedActor{

    public void onReceive(Object message) throws InterruptedException { 
        
        System.out.println("Message received");
        
        if(message instanceof GreetingMessage){
            System.out.println("Get sender : " + getSender());
            
        } else if( message instanceof PingMessage){ 
            getSender().tell(new PongMessage(), getSelf()); 
        } else if(message instanceof String){ 
        
        } else {
            unhandled(message);
        } 
    }
}
