package es.uc3m.tiw.users.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import es.uc3m.tiw.users.model.User;

public interface UsersDao extends CrudRepository<User, Integer>{

	public Optional<User> findByUserEmail(String email);
	public Optional<User> findByUserEmailAndUserPassword(String email, String password);
	public List<User> findAll();
	
}