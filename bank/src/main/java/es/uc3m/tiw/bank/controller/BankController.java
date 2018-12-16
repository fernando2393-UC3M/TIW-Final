package es.uc3m.tiw.bank.controller;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BankController {
	//TODO Request body into a single object
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST, value="/bank/{card_num}/{cv2}/{date}")
	public ResponseEntity validateCard(@PathVariable @Validated String card_num, @PathVariable @Validated String cv2,
			@PathVariable @Validated String date) throws ParseException{
		
		boolean cnum_validate = false;
		
		// Check card_num length is 16 (omit digits)
		if(card_num.length() == 16 && isAllDigits(card_num)){
			int count = 0;
			for(int i=0; i<card_num.length(); ++i)
				count += (card_num.charAt(i) - '0');
			
			// Check if it's divisible by 4
			cnum_validate = count % 4 == 0;
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
		Date today = new Date();
		
		// Obtain date from input
		String [] temp = date.split("-");
		String aux = temp[0] + "/20" + temp[1];
		Date inputDate = new SimpleDateFormat("MM/yyyy").parse(aux);
		
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
