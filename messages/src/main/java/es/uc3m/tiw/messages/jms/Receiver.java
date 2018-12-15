package es.uc3m.tiw.messages.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
	
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(String content) {
        System.out.println("Received <" + content + ">");
	 }
}
