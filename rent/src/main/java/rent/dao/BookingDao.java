package rent.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import model.Booking;
import model.Home;
import model.User;

public interface BookingDao extends CrudRepository<Booking, Integer>{

	public List<Booking> findByHome(Home home);
	public List<Booking> findByUser(User user);	
	public List<Booking> findAll();
	
}
