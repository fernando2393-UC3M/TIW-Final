package es.uc3m.tiw.rent.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Booking>> getUsers(){
				
		List<Booking> bookList = daoBk.findAll();
		ResponseEntity<List<Booking>> response;
		
		if(bookList.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(bookList, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping("/rents/homes/{id}")
	public ResponseEntity<List<Booking>> getBookingByHomeId(@PathVariable @Validated Integer id){
		Home home = daoHm.findById(id).orElse(null);
		List<Booking> bookList = daoBk.findByHome(home);
		
		ResponseEntity<List<Booking>> response;
		if(bookList.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(bookList, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping("/rents/users/{id}")
	public ResponseEntity<List<Booking>> getBookingByUserId(@PathVariable @Validated Integer id){		
		User user = daoUs.findById(id).orElse(null);
		List<Booking> bookList = daoBk.findByUser(user);
		
		ResponseEntity<List<Booking>> response;
		if(bookList.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(bookList, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value="/rents")
	public @ResponseBody Booking saveBooking(@Validated Booking pbooking){
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