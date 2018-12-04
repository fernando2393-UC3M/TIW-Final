package es.uc3m.tiw.admin.controllers;

import java.util.List;
import java.util.Optional;

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

import es.uc3m.tiw.admin.dao.AdminDao;
import es.uc3m.tiw.admin.model.Admin;

@Controller
@CrossOrigin
public class AdminController {

	@Autowired
	AdminDao daoUs;

	@RequestMapping("/admin")
	public @ResponseBody List<Admin> getAdmins(){
		return daoUs.findAll();
	}
	
	@RequestMapping("/admin/{id}")
	public @ResponseBody Admin getAdminByAdminId(@PathVariable int id){
		return daoUs.findById(id).orElse(null);
	}
	
	@RequestMapping("/admin/{email}/{password}")
	public ResponseEntity<Admin> getAdminByAdminEmailAndAdminPassword(@PathVariable String email,
											@PathVariable String password){
		
		Admin admin = daoUs.findByAdminEmailAndAdminPassword(email, password).orElse(null);
		
		ResponseEntity<Admin> response;
		
		if(admin == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(admin, HttpStatus.OK);
		}
		
		return response; 
	}	
}