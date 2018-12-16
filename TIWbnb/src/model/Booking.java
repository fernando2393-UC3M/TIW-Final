package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the booking database table.
 * 
 */
@Entity
@Table(name="booking")
@NamedQuery(name="Booking.findAll", query="SELECT b FROM Booking b")
public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="BOOKING_ID")
	private int bookingId;

	@Column(name="BOOKING_CARD_NUM")
	private BigInteger bookingCardNum;

	@Column(name="BOOKING_CONFIRMED")
	private String bookingConfirmed;

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

	// many-to-one association to User
	@ManyToOne
	@JoinColumn(name="BOOKING_USER_ID", referencedColumnName="USER_ID")
	private User user;

	// many-to-one association to Home
	@ManyToOne
	@JoinColumn(name="BOOKING_HOME_ID", referencedColumnName="HOME_ID")
	private Home home;

	public Booking() {
	}

	public int getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public BigInteger getBookingCardNum() {
		return this.bookingCardNum;
	}

	public void setBookingCardNum(BigInteger bookingCardNum) {
		this.bookingCardNum = bookingCardNum;
	}

	public String getBookingConfirmed() {
		return this.bookingConfirmed;
	}

	public void setBookingConfirmed(String bookingConfirmed) {
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

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Home getHome() {
		return this.home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

}