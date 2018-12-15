package es.uc3m.tiw.messages.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@CrossOrigin
public class JMSController {
	/*
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
	*/
}
