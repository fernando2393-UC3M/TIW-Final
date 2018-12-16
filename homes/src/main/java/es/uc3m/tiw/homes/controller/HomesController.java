package es.uc3m.tiw.homes.controller;

import java.util.List;
import java.math.BigDecimal;
import java.sql.Date;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.uc3m.tiw.homes.dao.HomesDao;
import es.uc3m.tiw.homes.dao.UsersDao;
import es.uc3m.tiw.homes.model.Home;
import es.uc3m.tiw.homes.model.User;

@Controller
@CrossOrigin
public class HomesController {
	
	@Autowired
	HomesDao daoHome;
	
	@Autowired
	UsersDao daoUs;

	/*@RequestMapping(method=RequestMethod.POST, value="/homes")
	public ResponseEntity <List<Home>> getHomes(){
		List<Home> homeList = daoHome.findAll();
		ResponseEntity<List<Home>> response;
		System.out.println("AQUI");
		if(homeList.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(homeList, HttpStatus.OK);
		}
		return response;
	}*/
	
	@RequestMapping(method=RequestMethod.GET, value="/homes/{id}")
	public ResponseEntity<Home> getHomeByHomeId(@PathVariable int id){
		Home home = daoHome.findById(id).orElse(null);
		ResponseEntity<Home> response;
		if(home == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(home, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<Home>> getHomeByCity(@RequestParam(name="homeCity", required=true) String city,
													@RequestParam(name="homeInit", required=false) java.util.Date dateInit,
													@RequestParam(name="homeEnd", required=false) java.util.Date dateEnd,
													@RequestParam(name="homePrice", required=true) String price,
													@RequestParam(name="homeType", required=true) String type,
													@RequestParam(name="homeAdults", required=true) int adults,
													@RequestParam(name="homeKids", required=true) int kids)
	{	
		//Main query get matching: city, type of home, guests
		System.out.println("Home city is: " + city);
		
		
		if(city.equals("all")){
			List<Home> homeList = daoHome.findAll();
			ResponseEntity<List<Home>> response;
			System.out.println("AQUI");
			if(homeList.size() == 0) {
				response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				response = new ResponseEntity<>(homeList, HttpStatus.OK);
			}
			return response;
		}
		else{
			int lowPriceBound;
			int highPriceBound;
			//Check price range
			if(price.equals("Hasta 35€")){
				lowPriceBound = 0;
				highPriceBound = 35;
			}
			else if(price.equals("36€ - 69€")){
				lowPriceBound = 36;
				highPriceBound = 69;
			}
			else if(price.equals("70€ - 130€")){
				lowPriceBound = 70;
				highPriceBound = 130;
			}
			else{
				//130+
				lowPriceBound = 131;
				highPriceBound = 3000;
			}
			
			//Adapt types to sql
			java.sql.Date sqlDateInit = new java.sql.Date(dateInit.getTime());
			java.sql.Date sqlDateEnd = new java.sql.Date(dateEnd.getTime());
			
			BigDecimal bdLowPrice = new BigDecimal(lowPriceBound);
			BigDecimal bdHighPrice = new BigDecimal(highPriceBound);
			
			
			List<Home> home = daoHome.findByHomeCityAndHomeTypeAndHomeGuestsGreaterThanAndHomePriceNightBetweenAndHomeAvDateInitBeforeAndHomeAvDateFinAfter
					(city, type, adults+kids, bdLowPrice, bdHighPrice, sqlDateInit, sqlDateEnd);
	
			ResponseEntity<List<Home>> response;
			
			response = new ResponseEntity<>(home, HttpStatus.OK);
			
			return response;
		}
	}
	
	
	@RequestMapping("/homes/users/{id}")
	public ResponseEntity<List<Home>> getHomesByUserId(@PathVariable @Validated int id){
		User user = daoUs.findById(id).orElse(null);
		List<Home> homeList = daoHome.findByUser(user);
		
		ResponseEntity<List<Home>> response;
		
		if(homeList.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(homeList, HttpStatus.OK);
		}
		
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value="/homes/users/{userId}")
	public ResponseEntity<Home> saveHome(@RequestBody @Validated Home pHome, @PathVariable @Validated int userId){
		// Home home = daoHome.findById(pHome.getHomeId()).orElse(null);
		User us = daoUs.findById(userId).orElse(null);
		
		ResponseEntity<Home> response;

		if(pHome != null && us != null){
			pHome.setUser(us);
			daoHome.save(pHome);
			response = new ResponseEntity<>(HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/homes/{id}")
	public ResponseEntity<Home> deleteHome(@PathVariable @Validated Integer id){
		Home hm = daoHome.findById(id).orElse(null);
		ResponseEntity<Home> response;
		
		if (hm != null){
			daoHome.delete(hm);
			response = new ResponseEntity<>(HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value="/homes/{id}")
	public ResponseEntity<Home> updateHome(@PathVariable @Validated Integer id, @RequestBody Home pHome){
		Home hm = daoHome.findById(id).orElse(null);
		ResponseEntity<Home> response;
		
		if(hm != null) {
			hm.setHomeAvDateFin(pHome.getHomeAvDateFin());
			hm.setHomeAvDateInit(pHome.getHomeAvDateInit());
			hm.setHomeCity(pHome.getHomeCity());
			hm.setHomeDescriptionFull(pHome.getHomeDescriptionFull());
			hm.setHomeDescriptionShort(pHome.getHomeDescriptionShort());
			hm.setHomeGuests(pHome.getHomeGuests());
			hm.setHomeName(pHome.getHomeName());
			hm.setHomePhotos(pHome.getHomePhotos());
			hm.setHomePriceNight(pHome.getHomePriceNight());
			hm.setHomeType(pHome.getHomeType());
			
			daoHome.save(hm);
			
			response = new ResponseEntity<>(HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	/*
	// TODO: Ask Telmo
	
	//findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(OffsetDateTime endDate, OffsetDateTime startDate);
	 
	@RequestMapping("/homes/query")
	public @ResponseBody List<Home> getHomebyQuery(@PathVariable @Validated Home phome){
		//Query with phome params
		return null;
	}
	
	@Query("SELECT u FROM User u WHERE u.status = :status and u.name = :name")
	public User findUserByStatusAndNameNamedParams(
	  @Param("status") Integer status, 
	  @Param("name") String name);
	*/
}
