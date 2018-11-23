package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the BOOKING database table.
 * 
 */
@Entity
@Table(name="BOOKING")
@NamedQuery(name="Booking.findAll", query="SELECT b FROM Booking b")
public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="BOOKING_ID")
	private int bookingId;

	@Column(name="BOOKING_CARD_NUM")
	private int bookingCardNum;

	@Column(name="BOOKING_CONFIRMED")
	private byte bookingConfirmed;

	@Column(name="BOOKING_CV2")
	private int bookingCv2;

	@Temporal(TemporalType.DATE)
	@Column(name="BOOKING_DATE_IN")
	private Date bookingDateIn;

	@Temporal(TemporalType.DATE)
	@Column(name="BOOKING_DATE_OUT")
	private Date bookingDateOut;

	@Column(name="BOOKING_EXP_CODE")
	private String bookingExpCode;

	//bi-directional many-to-one association to Home
	@ManyToOne
	@JoinColumn(name="BOOKING_HOME_ID")
	private Home home;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="BOOKING_USER_ID")
	private User user;

	public Booking() {
	}

	public int getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getBookingCardNum() {
		return this.bookingCardNum;
	}

	public void setBookingCardNum(int bookingCardNum) {
		this.bookingCardNum = bookingCardNum;
	}

	public byte getBookingConfirmed() {
		return this.bookingConfirmed;
	}

	public void setBookingConfirmed(byte bookingConfirmed) {
		this.bookingConfirmed = bookingConfirmed;
	}

	public int getBookingCv2() {
		return this.bookingCv2;
	}

	public void setBookingCv2(int bookingCv2) {
		this.bookingCv2 = bookingCv2;
	}

	public Date getBookingDateIn() {
		return this.bookingDateIn;
	}

	public void setBookingDateIn(Date bookingDateIn) {
		this.bookingDateIn = bookingDateIn;
	}

	public Date getBookingDateOut() {
		return this.bookingDateOut;
	}

	public void setBookingDateOut(Date bookingDateOut) {
		this.bookingDateOut = bookingDateOut;
	}

	public String getBookingExpCode() {
		return this.bookingExpCode;
	}

	public void setBookingExpCode(String bookingExpCode) {
		this.bookingExpCode = bookingExpCode;
	}

	public Home getHome() {
		return this.home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}