package es.uc3m.tiw.messages.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uc3m.tiw.messages.model.Message;
import es.uc3m.tiw.messages.model.User;

public interface MessagesDao extends CrudRepository<Message, Integer>{
	
	public List<Message> findByUser2(User user2);
	public List<Message> findByUser2AndMessageRead(User user2, byte read);
	
}
