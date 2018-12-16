package es.uc3m.tiw.homes.dao;

import java.util.List;
import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.data.repository.CrudRepository;

import es.uc3m.tiw.homes.model.Home;
import es.uc3m.tiw.homes.model.User;

public interface HomesDao extends CrudRepository<Home, Integer>{

	public List<Home> findByUser(User user);
	public List<Home> findAll();
	public List<Home> findByHomeCityAndHomeTypeAndHomeGuestsGreaterThanAndHomePriceNightBetweenAndHomeAvDateInitBeforeAndHomeAvDateFinAfter
				(String homeCity, String homeType, int homeGuests, BigDecimal lowPriceBound, BigDecimal highPriceBound, java.sql.Date homeAvDateInit, java.sql.Date homeAvDateFin);
	
}
