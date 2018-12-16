package es.uc3m.tiw.messages.controller;

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

import es.uc3m.tiw.messages.model.Admin;
import es.uc3m.tiw.messages.model.Message;
import es.uc3m.tiw.messages.model.MessagesAdmin;
import es.uc3m.tiw.messages.model.User;
import es.uc3m.tiw.messages.dao.MessagesDao;
import es.uc3m.tiw.messages.dao.UsersDao;
import es.uc3m.tiw.messages.dao.AdminDao;
import es.uc3m.tiw.messages.dao.MessagesAdminDao;

@Controller
@CrossOrigin
public class MessagesController {

	@Autowired
	private MessagesDao daoMs;
	@Autowired
	private MessagesAdminDao daoAd;
	@Autowired
	private UsersDao daoUs;
	@Autowired
	private AdminDao daoAdmin;

	@RequestMapping(method=RequestMethod.GET, value="/test")
	public ResponseEntity<List<Message>> getMessages(){
		List<Message> list = daoMs.findAll();
		ResponseEntity<List<Message>> response;
		
		if(list.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(list, HttpStatus.OK);
		}
		return response;
	}
	@RequestMapping(method=RequestMethod.GET, value="/testadmin")
	public ResponseEntity<List<MessagesAdmin>> getMessagesAdmin(){
		List<MessagesAdmin> list = daoAd.findAll();
		ResponseEntity<List<MessagesAdmin>> response;
		
		if(list.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(list, HttpStatus.OK);
		}
		return response;
	}
	
	/* Get Message between users from a user*/
	@RequestMapping(method=RequestMethod.GET, value="/user/{id}")
	public ResponseEntity<List<Message>> getMessagesByUser2(@PathVariable @Validated int id) {	
		User user = daoUs.findById(id).orElse(null);
		List<Message> list = daoMs.findByUser2(user);
		ResponseEntity<List<Message>> response;
		
		if(list.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		return response;
	}

	/* Get AdminMessage to a user */
	@RequestMapping(method=RequestMethod.GET, value="/admin/user/{id}")
	public ResponseEntity<List<MessagesAdmin>> getMessagesAdminByUser(@PathVariable @Validated int id) {	
		User user = daoUs.findById(id).orElse(null);
		List<MessagesAdmin> list = daoAd.findByUserAndMessageFromAdmin(user, (byte) 0);
		
		ResponseEntity<List<MessagesAdmin>> response;
		
		if(list.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		return response;
	}

	/* Get AdminMessage to an admin */
	@RequestMapping(method=RequestMethod.GET, value="/admin/admin/{id}")
	public ResponseEntity<List<MessagesAdmin>> getMessagesAdminByAdmin(@PathVariable @Validated int id) {	
		Admin admin = daoAdmin.findById(id).orElse(null);
		List<MessagesAdmin> list = daoAd.findByAdminAndMessageFromAdmin(admin, (byte) 1);
		ResponseEntity<List<MessagesAdmin>> response;
		
		if(list.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		return response;
	}

	/* Store new Message */
	@RequestMapping(method = RequestMethod.POST, value="/sendMessage")
	public ResponseEntity<Message> saveMessage(@RequestBody @Validated Message message){
		daoMs.save(message);		
		return (new ResponseEntity<>(HttpStatus.OK));
	}
	
	/* Store new AdminMessage */
	@RequestMapping(method = RequestMethod.POST, value="/sendAdminMessage")
	public ResponseEntity<MessagesAdmin> saveAdminMessage(@RequestBody @Validated MessagesAdmin message){
		daoAd.save(message);		
		return (new ResponseEntity<>(HttpStatus.OK));
	}

	/* Set User received messages to read */
	@SuppressWarnings("rawtypes")
	@RequestMapping(method=RequestMethod.GET, value="/setRead/{id}")
	public ResponseEntity setReadMessage(@PathVariable @Validated int id){
		User user = daoUs.findById(id).orElse(null);		
		List<Message> aux = daoMs.findByUser2AndMessageRead(user, (byte) 0);
		ResponseEntity response;
		
		if(aux.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {			
			for(Message msg: aux){
				msg.setMessageRead((byte) 1);
				daoMs.save(msg);
			}			
			response = new ResponseEntity<>(HttpStatus.OK);
		}
		
		return response;
	}
	
}
