package es.uc3m.tiw.messages.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uc3m.tiw.messages.model.Admin;
import es.uc3m.tiw.messages.model.Message;
import es.uc3m.tiw.messages.model.MessagesAdmin;
import es.uc3m.tiw.messages.model.User;
import es.uc3m.tiw.messages.dao.MessagesDao;
import es.uc3m.tiw.messages.dao.MessagesAdminDao;

@Controller
@CrossOrigin
public class MessagesController {

	@Autowired
	private MessagesDao daoMs;
	private MessagesAdminDao daoAd;

	/* Get Message between users from a user*/
	@RequestMapping(method=RequestMethod.GET, value="/user")
	public ResponseEntity<List<Message>> getMessagesByUser2(@RequestBody @Validated User pUser) {	
		List<Message> list = daoMs.findByUser2(pUser);
		ResponseEntity<List<Message>> response;
		
		if(list.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		return response;
	}

	/* Get AdminMessage to a user */
	@RequestMapping(method=RequestMethod.GET, value="/admin/user")
	public ResponseEntity<List<MessagesAdmin>> getMessagesAdminByUser(@RequestBody @Validated User pUser) {	
		List<MessagesAdmin> list = daoAd.findByUserAndMessageFromAdmin(pUser, (byte) 0);
		ResponseEntity<List<MessagesAdmin>> response;
		
		if(list.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		return response;
	}

	/* Get AdminMessage to an admin */
	@RequestMapping(method=RequestMethod.GET, value="/admin/admin")
	public ResponseEntity<List<MessagesAdmin>> getMessagesAdminByAdmin(@RequestBody @Validated Admin pAdmin) {	
		List<MessagesAdmin> list = daoAd.findByAdminAndMessageFromAdmin(pAdmin, (byte) 1);
		ResponseEntity<List<MessagesAdmin>> response;
		
		if(list.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		return response;
	}

	/* Store new Message */
	public void saveMessage(Message message){
		daoMs.save(message);		
	}

	/* Store new AdminMessage */
	public void saveAdminMessage(MessagesAdmin message){
		daoAd.save(message);		
	}

	/* Set User messages to read */
	@SuppressWarnings("rawtypes")
	@RequestMapping(method=RequestMethod.GET, value="/user/setread")
	public ResponseEntity setReadMessage(@RequestBody @Validated User pUser, byte read){
		List<Message> aux = daoMs.findByUser2AndMessageRead(pUser, (byte) 0);
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
