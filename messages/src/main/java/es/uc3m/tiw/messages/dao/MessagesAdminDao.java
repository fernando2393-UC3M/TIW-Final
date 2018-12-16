package es.uc3m.tiw.messages.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uc3m.tiw.messages.model.Admin;
import es.uc3m.tiw.messages.model.MessagesAdmin;
import es.uc3m.tiw.messages.model.User;

public interface MessagesAdminDao extends CrudRepository<MessagesAdmin, Integer>{
	
	public List<MessagesAdmin> findByUserAndMessageFromAdmin(User user, byte fromAdmin);
	public List<MessagesAdmin> findByAdminAndMessageFromAdmin(Admin admin, byte fromAdmin);
	public List<MessagesAdmin> findAll();

}
