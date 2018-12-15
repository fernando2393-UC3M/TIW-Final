package es.uc3m.tiw.users.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="USER_ID")
	private int userId;

	//@Temporal(TemporalType.DATE)
	@Column(name="USER_BIRTHDATE")
	private Date userBirthdate;

	@Column(name="USER_EMAIL")
	private String userEmail;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="USER_PASSWORD")
	private String userPassword;

	@Column(name="USER_SURNAME")
	private String userSurname;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="user")
	@JsonIgnore
	private List<Booking> bookings;

	//bi-directional many-to-one association to Home
	@OneToMany(mappedBy="user")
	@JsonIgnore
	private List<Home> homes;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="user1")
	@JsonIgnore
	private List<Message> messages1;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="user2")
	@JsonIgnore
	private List<Message> messages2;

	//bi-directional many-to-one association to MessagesAdmin
	@OneToMany(mappedBy="user")
	@JsonIgnore
	private List<MessagesAdmin> messagesAdmins;

	public User() {
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getUserBirthdate() {
		return this.userBirthdate;
	}

	public void setUserBirthdate(Date userBirthdate) {
		this.userBirthdate = userBirthdate;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserSurname() {
		return this.userSurname;
	}

	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

}