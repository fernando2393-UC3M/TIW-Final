package es.uc3m.tiw;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.uc3m.tiw.domains.*;

@Controller
@CrossOrigin
public class MyController {

	@Autowired
	UserDAO daous;
	
	@Autowired
	AddressDAO daoaddress;

	
	@RequestMapping("/users")
	public @ResponseBody List<User> getUsers(){
		return daous.findAll();
	}
	
	@RequestMapping("/users/{name}")
	public @ResponseBody User getUserByName(@PathVariable String name){
		//return daous.findByName(name);
		 return daous.findTop1ByName(name);
	}
	
	@RequestMapping("/users/{name}/{surname}")
	public @ResponseBody List<User> getUserByNameAndSurname(@PathVariable String name,
											  @PathVariable String surname){
		return daous.findByNameAndSurname(name, surname);
	}
	
	@RequestMapping("/userstreet/{street}")
	public @ResponseBody List<User> getUserByStreet(@PathVariable String street){
		return daous.findByAddressStreet(street);
	}
	
	@RequestMapping("/address/{streetName}")
	public @ResponseBody List<Address> getAdressByStreet(@PathVariable String streetName){
		return daoaddress.findByStreet(streetName);
	}

	@RequestMapping(method = RequestMethod.POST, value="/user")
	public @ResponseBody User saveUser(@RequestBody @Validated User puser) {
		return daous.save(puser);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/user/{id}")
	public @ResponseBody void deleteUser(@PathVariable @Validated Long id) {
		User us = daous.findById(id).orElse(null);
		if (us != null) {
			daous.delete(us);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value="/user/{id}")
	public @ResponseBody User updateUser(@PathVariable @Validated Long id, @RequestBody User pUser) {
		//Optional<User> us = daous.findById(id);
		User us = daous.findById(id).orElse(null);
		us.setName(pUser.getName());
		us.setSurname(pUser.getSurname());				
		return daous.save(us);
	}
}
