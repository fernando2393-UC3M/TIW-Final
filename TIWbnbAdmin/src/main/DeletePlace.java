package main;

import javax.persistence.EntityManager;

import model.Home;

public class DeletePlace {
	
	public static void delete(EntityManager em, int homeId) {
		
		Home place = em.find(Home.class, homeId); // Look for the place by id
		
		em.remove(place); // Remove place from database
		
	}
}
