package main;

import javax.persistence.EntityManager;
import model.User;

public class Delete {
		
	public void delete(EntityManager em, int id) {
		
		User user = em.find(User.class, id); // Look for the user by id
		
		em.remove(user); // Remove user from database
		
	}
}
