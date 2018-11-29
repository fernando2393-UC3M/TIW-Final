package rent.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import model.User;
import model.Home;

public interface HomesDao extends CrudRepository<Home, Integer>{

	public List<Home> findByUser(User user);
	public List<Home> findAll();
	
}
