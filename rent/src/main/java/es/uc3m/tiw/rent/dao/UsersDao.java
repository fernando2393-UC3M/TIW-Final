package es.uc3m.tiw.rent.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uc3m.tiw.rent.model.User;

public interface UsersDao extends CrudRepository<User, Integer>{

	public List<User> findByUserEmail(String email);
	public List<User> findByUserEmailAndUserPassword(String email, String password);
	public List<User> findAll();
	
}