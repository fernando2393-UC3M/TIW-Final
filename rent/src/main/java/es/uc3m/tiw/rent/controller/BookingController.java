package es.uc3m.tiw.rent.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.uc3m.tiw.rent.dao.BookingDao;
import es.uc3m.tiw.rent.dao.HomesDao;
import es.uc3m.tiw.rent.dao.UsersDao;
import es.uc3m.tiw.rent.model.Booking;
import es.uc3m.tiw.rent.model.Home;
import es.uc3m.tiw.rent.model.User;

@Controller
@CrossOrigin
public class BookingController {

	@Autowired
	BookingDao daoBk;
	@Autowired
	UsersDao daoUs;
	@Autowired
	HomesDao daoHm;


	@RequestMapping("/rents")
	public @ResponseBody List<Booking> getUsers(){
		return daoBk.findAll();
	}
	
	@RequestMapping("/rents/homes/{id}")
	public @ResponseBody List<Booking> getBookingByHomeId(@PathVariable @Validated Integer homeId){
		Home home = daoHm.findById(homeId).orElse(null);
		return daoBk.findByHome(home);
	}
	
	@RequestMapping("/rents/users/{id}")
	public @ResponseBody List<Booking> getBookingByUserId(@PathVariable @Validated Integer userId){
		User user = daoUs.findById(userId).orElse(null);
		return daoBk.findByUser(user);
	}

	@RequestMapping(method = RequestMethod.POST, value="/rents")
	public @ResponseBody Booking saveBooking(@PathVariable @Validated Booking pbooking){
		return daoBk.save(pbooking);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/rents/{id}")
	public @ResponseBody void deleteBooking(@PathVariable @Validated Integer id){
		Booking bk = daoBk.findById(id).orElse(null);
		if (bk != null){
			daoBk.delete(bk);
		}
	}

	// We only update the confirmed state
	@RequestMapping(method = RequestMethod.PUT, value="/rents/{id}")
	public @ResponseBody Booking updateBooking(@PathVariable @Validated Integer id, @RequestBody Booking pBooking){
		Booking bk = daoBk.findById(id).orElse(null);
		if(bk != null){
			bk.setBookingConfirmed(bk.getBookingConfirmed());
		}		
		return daoBk.save(bk);
	}
	
}