package actors;
import java.util.ArrayList;
import java.util.List;

import message.GreetingMessage;
import message.PingMessage;
import message.PongMessage;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class GreetingActor extends UntypedActor{

	private List<String> neighbours;
	private String nodeName;
	
	private List<Object> messagesReceived;
	
	public GreetingActor(String nodeName, List<String> neighbours){
		this.nodeName = nodeName;
		this.neighbours = neighbours;
		
		messagesReceived = new ArrayList<Object>();
		
	}

	
    public void onReceive(Object message) throws InterruptedException { 
    	
    	if(messagesReceived.contains(message)){
    		System.out.println("[" + nodeName + "] receive from (" + getSender().toString() + ") a message he already received.");
    		return;
    	}
    	
    	messagesReceived.add(message);
        
        if(message instanceof GreetingMessage){
        	GreetingMessage greetingMessage = (GreetingMessage)message;
        	System.out.println("[" + nodeName + "] receive from (" + getSender().toString() + ") the following message : " + greetingMessage.getText());
        	System.out.println("[" + nodeName + "] transmitting to all his neighbours...");
            for(String neighbour : neighbours){
            	ActorRef actor = Actors.getActor(neighbour);
            	
            	if(actor != getSender())
            		actor.tell(message, this.getSelf());
            }
            
            
        } else if(message instanceof PingMessage){ 
            getSender().tell(new PongMessage(), getSelf()); 
        } else if(message instanceof String){ 
        
        } else {
            unhandled(message);
        } 
    }

}
