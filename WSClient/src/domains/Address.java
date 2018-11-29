package domains;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Address {
	Long idaddress;
	String street;
	int postcode;
	
	User user;

	public Address(){
	}
	
	public Address(String pStreet, int pPostCode){
		street   = pStreet;
		postcode = pPostCode;
	}

	public void setIdAddress(Long pIdAddress){
		idaddress = pIdAddress;
	}

	public Long getIdAddress(){
		return idaddress;
	}
	

	public void setStreet(String pStreet){
		street = pStreet;
	}
	
	public void setPostCode(int pPostCode){
		postcode = pPostCode;		
	}
	
	public String getStreet(){
		return street;
	}
	
	public int getPostCode(){
		return postcode;
	}
	
	public void setUser(User pUser){
		user = pUser;		
	}
	
	public User getUser(){
		return user;
	}

}
