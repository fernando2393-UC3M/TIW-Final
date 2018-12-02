package es.uc3m.tiw.rent.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uc3m.tiw.rent.model.Booking;
import es.uc3m.tiw.rent.model.Home;
import es.uc3m.tiw.rent.model.User;

public interface BookingDao extends CrudRepository<Booking, Integer>{

	public List<Booking> findByHome(Home home);
	public List<Booking> findByUser(User user);	
	public List<Booking> findAll();
	
}
