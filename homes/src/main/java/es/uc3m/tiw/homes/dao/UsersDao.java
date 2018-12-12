package es.uc3m.tiw.homes.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uc3m.tiw.homes.model.User;

public interface UsersDao extends CrudRepository<User, Integer>{

	public List<User> findByUserEmail(String email);
	public List<User> findByUserEmailAndUserPassword(String email, String password);
	public List<User> findAll();
	
}