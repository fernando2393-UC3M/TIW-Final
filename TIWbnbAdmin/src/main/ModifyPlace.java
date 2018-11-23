package main;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import model.Home;

public class ModifyPlace {
	public void updatePlaceData(
			int id,
			String avDateFin,
			String avDateInit,
			String city,
			String descriptionFull,
			String descriptionShort,
			int guests,
			String name,
			String photos,
			BigDecimal priceNight,
			String type,
			EntityManager em) {
			
			Home place = em.find(Home.class, id); //Find the proper user
	
			// TODO: Cuidar los strings formats
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed_avDateFin  = new Date(1970, 01, 01);
			Date parsed_avDateInit = new Date(1970, 02, 06);
//			try {
//				if(parsed_avDateF)
//				parsed_avDateFin = format.parse(avDateFin);
//				parsed_avDateInit = format.parse(avDateInit);
//			} catch (ParseException e) {
//			}
	
			//place.setHomeId(id);
			place.setHomeAvDateFin(parsed_avDateFin);
			place.setHomeAvDateInit(parsed_avDateInit);
			place.setHomeCity(city);
			place.setHomeDescriptionFull(descriptionFull);
			place.setHomeDescriptionShort(descriptionShort);
			place.setHomeGuests(guests);
			place.setHomeName(name);
			place.setHomePhotos(photos);
			place.setHomePriceNight(priceNight);
			place.setHomeType(type);
			
	
			em.merge(place);
	
		}
}
