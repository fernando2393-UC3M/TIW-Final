package model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * The persistent class for the HOME database table.
 * 
 */
@Entity
@Table(name="home")
@NamedQuery(name="Home.findAll", query="SELECT h FROM Home h")
public class Home implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="HOME_ID")
	private int homeId;

	@Column(name="HOME_AV_DATE_FIN")
	private Date homeAvDateFin;

	@Column(name="HOME_AV_DATE_INIT")
	private Date homeAvDateInit;

	@Column(name="HOME_CITY")
	private String homeCity;

	@Column(name="HOME_DESCRIPTION_FULL")
	private String homeDescriptionFull;

	@Column(name="HOME_DESCRIPTION_SHORT")
	private String homeDescriptionShort;

	@Column(name="HOME_GUESTS")
	private int homeGuests;

	@Column(name="HOME_NAME")
	private String homeName;

	@Column(name="HOME_PHOTOS")
	private String homePhotos;

	@Column(name="HOME_PRICE_NIGHT")
	private BigDecimal homePriceNight;

	@Column(name="HOME_TYPE")
	private String homeType;

	//many-to-one association to User
	@ManyToOne
	@JoinColumn(name="HOME_EMAIL", referencedColumnName="USER_EMAIL")
	private User user;

	
	public Home() {
	}

	public int getHomeId() {
		return this.homeId;
	}

	public void setHomeId(int homeId) {
		this.homeId = homeId;
	}

	public Date getHomeAvDateFin() {
		return this.homeAvDateFin;
	}

	public void setHomeAvDateFin(Date homeAvDateFin) {
		this.homeAvDateFin = homeAvDateFin;
	}

	public Date getHomeAvDateInit() {
		return this.homeAvDateInit;
	}

	public void setHomeAvDateInit(Date homeAvDateInit) {
		this.homeAvDateInit = homeAvDateInit;
	}

	public String getHomeCity() {
		return this.homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public String getHomeDescriptionFull() {
		return this.homeDescriptionFull;
	}

	public void setHomeDescriptionFull(String homeDescriptionFull) {
		this.homeDescriptionFull = homeDescriptionFull;
	}

	public String getHomeDescriptionShort() {
		return this.homeDescriptionShort;
	}

	public void setHomeDescriptionShort(String homeDescriptionShort) {
		this.homeDescriptionShort = homeDescriptionShort;
	}

	public int getHomeGuests() {
		return this.homeGuests;
	}

	public void setHomeGuests(int homeGuests) {
		this.homeGuests = homeGuests;
	}

	public String getHomeName() {
		return this.homeName;
	}

	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}

	public String getHomePhotos() {
		return this.homePhotos;
	}

	public void setHomePhotos(String homePhotos) {
		this.homePhotos = homePhotos;
	}

	public BigDecimal getHomePriceNight() {
		return this.homePriceNight;
	}

	public void setHomePriceNight(BigDecimal homePriceNight) {
		this.homePriceNight = homePriceNight;
	}

	public String getHomeType() {
		return this.homeType;
	}

	public void setHomeType(String homeType) {
		this.homeType = homeType;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}