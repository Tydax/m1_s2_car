package message;

import java.io.Serializable;

public class GreetingMessage implements Serializable{

    String who;
    
    public GreetingMessage(String who){
        this.who = who;
    }
    
    
    
}
