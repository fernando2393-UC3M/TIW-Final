package es.uc3m.tiw.rent.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uc3m.tiw.rent.model.Home;
import es.uc3m.tiw.rent.model.User;

public interface HomesDao extends CrudRepository<Home, Integer>{

	public List<Home> findByUser(User user);
	public List<Home> findAll();
	
}
