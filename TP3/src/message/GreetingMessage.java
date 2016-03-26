package message;

import java.io.Serializable;

public class GreetingMessage implements Serializable{

    String text;
    
    public GreetingMessage(String text){
    	this.text = text;
    }
    
    public String getText(){
    	return this.text;
    }
    
    
    
}
