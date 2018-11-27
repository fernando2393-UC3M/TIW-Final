package homes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import model.User;
import model.Home;
import homes.dao.HomesDao;

@Controller
@CrossOrigin
public class HomesController {
	
	@Autowired
	HomesDao daoHome;

	@RequestMapping("/homes")
	public @ResponseBody List<Home> getHomes(){
		return daoHome.findAll();
	}
	
	@RequestMapping("/homes/{id}")
	public @ResponseBody Home getHomeByHomeId(@PathVariable int id){
		return daoHome.findById(id).orElse(null);
	}
	
	@RequestMapping("/homes/user")
	public @ResponseBody List<Home> getHomesByUser(@RequestBody User user){
		return daoHome.findByUser(user);
	}

	@RequestMapping(method = RequestMethod.POST, value="/homes")
	public @ResponseBody Home saveHome(@PathVariable @Validated Home phome){
		return daoHome.save(phome);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/homes/{id}")
	public @ResponseBody void deleteHome(@PathVariable @Validated Integer id){
		Home hm = daoHome.findById(id).orElse(null);
		if (hm != null){
			daoHome.delete(hm);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value="/homes/{id}")
	public @ResponseBody Home updateHome(@PathVariable @Validated Integer id, @RequestBody Home pHome){
		Home hm = daoHome.findById(id).orElse(null);
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
		hm.setBookings(pHome.getBookings());
		return daoHome.save(hm);
	}
	
	
	/* TODO: Ask Telmo
	
	findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(OffsetDateTime endDate, OffsetDateTime startDate);
	 
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
