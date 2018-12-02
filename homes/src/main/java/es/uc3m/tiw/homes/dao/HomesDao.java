package es.uc3m.tiw.homes.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uc3m.tiw.homes.model.Home;
import es.uc3m.tiw.homes.model.User;

public interface HomesDao extends CrudRepository<Home, Integer>{

	public List<Home> findByUser(User user);
	public List<Home> findAll();
	
}