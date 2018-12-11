package es.uc3m.tiw.homes.controller;

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

import es.uc3m.tiw.homes.dao.HomesDao;
import es.uc3m.tiw.homes.model.Home;
import es.uc3m.tiw.homes.model.User;

@Controller
@CrossOrigin
public class HomesController {
	
	@Autowired
	HomesDao daoHome;

	@RequestMapping(method=RequestMethod.GET, value="/homes")
	public ResponseEntity <List<Home>> getHomes(){
		List<Home> homeList = daoHome.findAll();
		ResponseEntity<List<Home>> response;
		
		if(homeList.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(homeList, HttpStatus.OK);
		}
		return response;
	}
	
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
	
	@RequestMapping("/homes/{user}")
	public @ResponseBody List<Home> getHomesByUser(@RequestBody User user){
		return daoHome.findByUser(user);
	}

	@RequestMapping(method = RequestMethod.POST, value="/homes")
	public ResponseEntity<Home> saveHome(@RequestBody @Validated Home pHome){
		Home home = daoHome.findById(pHome.getHomeId()).orElse(null);
		ResponseEntity<Home> response;
		
		if(home == null){
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
