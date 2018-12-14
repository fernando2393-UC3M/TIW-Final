package es.uc3m.tiw.users.controllers;

import java.util.Date;
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

import es.uc3m.tiw.users.model.User;
import es.uc3m.tiw.users.dao.UsersDao;

@Controller
@CrossOrigin
public class UsersController {

	@Autowired
	private UsersDao daoUs;

	@RequestMapping(method=RequestMethod.GET, value="/users")
	public ResponseEntity<List<User>> getUsers(){
		List<User> userList = daoUs.findAll();
		ResponseEntity<List<User>> response;
		
		if(userList.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(userList, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/users/{id}")
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
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<User> getUserByUserEmail(@RequestParam(name="email", required=true) String email){
		User user = daoUs.findByUserEmail(email).orElse(null);
		ResponseEntity<User> response;
		
		if(user == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(user, HttpStatus.ACCEPTED);
		}
		
		return response;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/users/{email}/{password}")
	public ResponseEntity<User> getUserByUserEmailAndUserPassword(@PathVariable String email,
											@PathVariable String password){
		User user = daoUs.findByUserEmailAndUserPassword(email, password).orElse(null);
		ResponseEntity<User> response;
		
		if(user == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(user, HttpStatus.OK);
		}
		
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value="/users")
	public ResponseEntity<User> saveUser(@RequestBody @Validated User pUser){
		User us = daoUs.findByUserEmail(pUser.getUserEmail()).orElse(null);
		ResponseEntity<User> response;
		
		if(us == null) {
			daoUs.save(pUser);
			response = new ResponseEntity<>(HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/users/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable @Validated Integer id){
		User us = daoUs.findById(id).orElse(null);
		ResponseEntity<User> response;
		
		if (us != null){
			daoUs.delete(us);
			response = new ResponseEntity<>(HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value="/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable @Validated Integer id, @RequestBody User pUser){
		User us = daoUs.findById(id).orElse(null);
		ResponseEntity<User> response;
		
		if(us != null) {
			us.setUserPassword(pUser.getUserPassword());
			us.setUserName(pUser.getUserName());
			us.setUserSurname(pUser.getUserSurname());
			if(pUser.getUserBirthdate() == null)
				us.setUserBirthdate(new Date(1970, 01, 01));
			else
				us.setUserBirthdate(pUser.getUserBirthdate());
			
			daoUs.save(us);
			
			response = new ResponseEntity<>(HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return response;
	}	

}