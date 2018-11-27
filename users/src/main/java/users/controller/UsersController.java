package users.controller;

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
import users.dao.UsersDao;

@Controller
@CrossOrigin
public class UsersController {

	@Autowired
	UsersDao daoUs;

	@RequestMapping("/users")
	public @ResponseBody List<User> getUsers(){
		return daoUs.findAll();
	}
	
	@RequestMapping("/users/{id}")
	public @ResponseBody User getUserByUserId(@PathVariable int id){
		return daoUs.findById(id).orElse(null);
	}
	
	@RequestMapping("/users/{email}")
	public @ResponseBody List<User> getUserByUserEmail(@PathVariable String email){
		return daoUs.findByUserEmail(email);
	}
	
	@RequestMapping("/users/{email}/{password}")
	public @ResponseBody List<User> getUserByUserEmailAndUserPassword(@PathVariable String email,
											@PathVariable String password){
		return daoUs.findByUserEmailAndUserPassword(email, password);
	}

	@RequestMapping(method = RequestMethod.POST, value="/user")
	public @ResponseBody User saveUser(@PathVariable @Validated User puser){
		return daoUs.save(puser);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/user/{id}")
	public @ResponseBody void deleteUser(@PathVariable @Validated Integer id){
		User us = daoUs.findById(id).orElse(null);
		if (us != null){
			daoUs.delete(us);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value="/user/{id}")
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