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
	
	@RequestMapping("/admin/{email}")
	public @ResponseBody List<Admin> getAdminByAdminEmail(@PathVariable String email){
		return daoUs.findByAdminEmail(email);
	}
	
	@RequestMapping("/admin/{email}/{password}")
	public @ResponseBody List<Admin> getUserByAdminEmailAndAdminPassword(@PathVariable String email,
											@PathVariable String password){
		return daoUs.findByAdminEmailAndAdminPassword(email, password);
	}

	@RequestMapping(method = RequestMethod.POST, value="/user")
	public @ResponseBody Admin saveAdmin(@PathVariable @Validated Admin padmin){
		return daoUs.save(padmin);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/user/{id}")
	public @ResponseBody void deleteAdmin(@PathVariable @Validated Integer id){
		Admin ad = daoUs.findById(id).orElse(null);
		if (ad != null){
			daoUs.delete(ad);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value="/user/{id}")
	public @ResponseBody Admin updateAdmin(@PathVariable @Validated Integer id, @RequestBody Admin pUser){
		Admin us = daoUs.findById(id).orElse(null);
		us.setAdminEmail(pUser.getAdminEmail());
		us.setAdminPassword(pUser.getAdminPassword());
		return daoUs.save(us);
	}	
	
}