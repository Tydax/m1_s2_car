package main;

import java.util.ArrayList;
import java.util.List;

import message.GreetingMessage;
import actors.Actors;
import actors.GreetingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Starter {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        
        ActorSystem system1 = ActorSystem.create("FirstSystem"); 
        ActorSystem system2 = ActorSystem.create("SecondSystem"); 

        ActorRef greeter1, greeter2, greeter3, greeter4, greeter5, greeter6;
        String nodeName1 = "greeter1", nodeName2 = "greeter2", nodeName3 = "greeter3", nodeName4 = "greeter4", nodeName5 = "greeter5", nodeName6 = "greeter6";
        
        
        
        Object[] argsConstructors = new Object[2];
        List<String> neighbours = null;
        

        /* Node 1 */
        neighbours = new ArrayList<String>();
        neighbours.add(nodeName2);
        neighbours.add(nodeName5);
        argsConstructors[0] = (Object)nodeName1;
        argsConstructors[1] = (Object)neighbours;
        greeter1 = system1.actorOf(Props.create(GreetingActor.class, argsConstructors), nodeName1);
        
        /* Node 2 */
        neighbours = new ArrayList<String>();
        neighbours.add(nodeName1);
        neighbours.add(nodeName3);
        neighbours.add(nodeName4);
        argsConstructors[0] = (Object)nodeName2;
        argsConstructors[1] = (Object)neighbours;
        greeter2 = system2.actorOf(Props.create(GreetingActor.class, argsConstructors), nodeName2);
        
        /* Node 3 */
        neighbours = new ArrayList<String>();
        neighbours.add(nodeName2);
        argsConstructors[0] = (Object)nodeName3;
        argsConstructors[1] = (Object)neighbours;
        greeter3 = system1.actorOf(Props.create(GreetingActor.class, argsConstructors), nodeName3);
        
        /* Node 4 */
        neighbours = new ArrayList<String>();
        neighbours.add(nodeName2);
        neighbours.add(nodeName6);
        argsConstructors[0] = (Object)nodeName4;
        argsConstructors[1] = (Object)neighbours;
        greeter4 = system2.actorOf(Props.create(GreetingActor.class, argsConstructors), nodeName4);
        
        /* Node 5 */
        neighbours = new ArrayList<String>();
        neighbours.add(nodeName1);
        neighbours.add(nodeName6);
        argsConstructors[0] = (Object)nodeName5;
        argsConstructors[1] = (Object)neighbours;
        greeter5 = system1.actorOf(Props.create(GreetingActor.class, argsConstructors), nodeName5);
        
        /* Node 6 */
        neighbours = new ArrayList<String>();
        neighbours.add(nodeName5);
        neighbours.add(nodeName4);
        argsConstructors[0] = (Object)nodeName6;
        argsConstructors[1] = (Object)neighbours;
        greeter6 = system2.actorOf(Props.create(GreetingActor.class, argsConstructors), nodeName6);
        
        /* Filling the hashmap */
        Actors.init();
        
        Actors.add(nodeName1, greeter1);
        Actors.add(nodeName2, greeter2);
        Actors.add(nodeName3, greeter3);
        Actors.add(nodeName4, greeter4);
        Actors.add(nodeName5, greeter5);
        Actors.add(nodeName6, greeter6);

        
        /* Envoi de messages */
        
        GreetingMessage greeting = new GreetingMessage("Charlie Parker");
        
        greeter1.tell(greeting, ActorRef.noSender()); 
        
        //greeter2.tell(new GreetingMessage("Bob Marley"), ActorRef.noSender()); 
        
       
        // le code qui suit ne va pas ->
        // greeter1.forward(greeting, system);
        
        /* Fin d'envoi de message */
        /*
        Inbox inbox = Inbox.create(system); 
        inbox.send(greeter3, new PingMessage()); 
        
        Object reply = inbox.receive(Duration.create(5, TimeUnit.SECONDS));
        */
        
        
        system1.shutdown();  // arrêt du système (fin des acteurs) 

    }

}
