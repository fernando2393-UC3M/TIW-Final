package admin.controller;

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

import model.Admin;
import admin.dao.AdminDao;

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
	public @ResponseBody List<Admin> getUserByAdminEmailAndAdminPassword(@PathVariable String email,
											@PathVariable String password){
		return daoUs.findByAdminEmailAndAdminPassword(email, password);
	}

	
}