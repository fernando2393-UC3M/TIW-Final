package domains;

import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.transaction.Transactional;


/**
 * The persistent class for the users database table.
 * 
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long iduser;

	private String name;

	private String surname;
	
	Set<Address> address;	

	public User() {
	}

	public Long getIduser() {
		return this.iduser;
	}

	public void setIduser(Long iduser) {
		this.iduser = iduser;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}