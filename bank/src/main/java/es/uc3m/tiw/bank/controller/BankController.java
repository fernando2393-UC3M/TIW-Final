package es.uc3m.tiw.bank.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BankController {
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/")
	public ResponseEntity validateCard(@RequestBody String card_num, @RequestBody String cv2,
			@RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.sql.Date date){
		
		boolean cnum_validate = false;
		
		// Check card_num length is 16 (omit digits)
		if(card_num.length() == 16 && isAllDigits(card_num)){
			int second_last = card_num.charAt(card_num.length()-2) - '0';
			int last = card_num.charAt(card_num.length()-1) - '0';
			
			// Check if it's divisible by 4
			cnum_validate = ((second_last-10 + last) % 4 == 0);			
		}
		
		if(!cnum_validate){
			return (new ResponseEntity(HttpStatus.PAYMENT_REQUIRED));
		}
		
		// Validate CV2
		boolean cv2_validate = (cv2.length() == 3 && isAllDigits(cv2));
		
		if(!cv2_validate){
			return (new ResponseEntity(HttpStatus.PAYMENT_REQUIRED));			
		}
		
		// Obtain current date
		java.util.Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
	    java.util.Date inputDate = new java.util.Date(date.getTime());
		
		// Validate Date (format yyyy-MM-dd)
		if(inputDate.before(today)){
			return (new ResponseEntity(HttpStatus.PAYMENT_REQUIRED));
		}
	    
		// Return Status Code 200
		return (new ResponseEntity(HttpStatus.OK));
	}
	
	private boolean isAllDigits(String input){
		boolean ret = true;
		
		for(int i=0; i<input.length(); i++){
			ret = ret && Character.isDigit(input.charAt(i));
		}
		
		return ret;
	}

}
