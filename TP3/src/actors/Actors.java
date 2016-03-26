package actors;

import java.util.HashMap;

import akka.actor.ActorRef;

public class Actors {
	public static HashMap<String, ActorRef> actors;
	
	public static void init(){
		actors = new HashMap<String, ActorRef>();
	}
	
	public static void add(String nodeName, ActorRef actor){
		actors.put(nodeName, actor);
	}
	
	public static ActorRef getActor(String nodeName){
		return actors.get(nodeName);
	}
}
