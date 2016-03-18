package main;

import java.time.Duration;

import message.GreetingMessage;
import message.PingMessage;
import actors.GreetingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;

public class Starter {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        
        ActorSystem system = ActorSystem.create("MySystem"); 
        ActorRef greeter1, greeter2, greeter3;
        
        Object[] empty = new Object[0];
        
        greeter1 = system.actorOf(Props.create(GreetingActor.class, empty), "greeter1");
        greeter2 = system.actorOf(Props.create(GreetingActor.class, empty), "greeter2");
        greeter3 = system.actorOf(Props.create(GreetingActor.class, empty), "greeter3");
        
        /* Envoi de messages */
        
        GreetingMessage greeting = new GreetingMessage("Charlie Parker");
        
        greeter1.tell(greeting, ActorRef.noSender()); 
        
        greeter2.tell(new GreetingMessage("Bob Marley"), ActorRef.noSender()); 
        
       
        // le code qui suit ne va pas ->  greeter1.forward(greeting, getsystem());
        
        /* Fin d'envoi de message */
        /*
        Inbox inbox = Inbox.create(system); 
        inbox.send(greeter3, new PingMessage()); 
        
        Object reply = inbox.receive(Duration.create(5, TimeUnit.SECONDS));
        */
        
        
        system.shutdown();  // arrêt du système (fin des acteurs) 

    }

}
