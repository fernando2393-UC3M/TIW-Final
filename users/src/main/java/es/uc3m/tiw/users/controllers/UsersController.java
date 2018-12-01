package es.uc3m.tiw.users.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.uc3m.tiw.users.model.User;
import es.uc3m.tiw.users.dao.UsersDao;

@Controller
@CrossOrigin
public class UsersController {

	@Autowired
	private UsersDao daoUs;

	@RequestMapping("/users")
	public @ResponseBody List<User> getUsers(){
		return daoUs.findAll();
	}
	
	@RequestMapping("/users/{id}")
	public ResponseEntity<User> getUserByUserId(@PathVariable int id){
		User user = daoUs.findById(id).orElse(null);
		ResponseEntity<User> response;
		if(user == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(user, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(value="/users", method = RequestMethod.GET)
	public @ResponseBody List<User> getUserByUserEmail(@RequestParam(name="email") String email){
		return daoUs.findByUserEmail(email);
	}
	
	@RequestMapping("/users/{email}/{password}")
	public @ResponseBody List<User> getUserByUserEmailAndUserPassword(@PathVariable String email,
											@PathVariable String password){
		return daoUs.findByUserEmailAndUserPassword(email, password);
	}

	@RequestMapping(method = RequestMethod.POST, value="/users")
	public @ResponseBody User saveUser(@PathVariable @Validated User puser){
		return daoUs.save(puser);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/users/{id}")
	public @ResponseBody void deleteUser(@PathVariable @Validated Integer id){
		User us = daoUs.findById(id).orElse(null);
		if (us != null){
			daoUs.delete(us);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value="/users/{id}")
	public @ResponseBody User updateUser(@PathVariable @Validated Integer id, @RequestBody User pUser){
		User us = daoUs.findById(id).orElse(null);
		us.setUserEmail(pUser.getUserEmail());
		us.setUserPassword(pUser.getUserPassword());
		us.setUserName(pUser.getUserName());
		us.setUserSurname(pUser.getUserSurname());
		us.setUserBirthdate(pUser.getUserBirthdate());
		return daoUs.save(us);
	}	

}