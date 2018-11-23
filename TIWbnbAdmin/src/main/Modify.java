package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.Locale.Category;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.User;

public class Modify {
	
	public void updateUserData(int id, String name, String surname, String birthdate, String password, EntityManager em) {
		
		User user = em.find(User.class, id); //Find the proper user

		// TODO: Cuidar los strings formats
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = new Date(1970, 01, 01);
		try {
			parsed = format.parse(birthdate);
		} catch (ParseException e) {
		}

		user.setUserName(name);
		user.setUserSurname(surname);
		user.setUserBirthdate(parsed);
		user.setUserPassword(password);

		em.merge(user);

	}

}
