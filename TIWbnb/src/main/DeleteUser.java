package main;

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

public class DeleteUser {
		
	public void deletion(int id, EntityManager em) {
		
		User user = em.find(User.class, id); // Look for the user by id
		
		em.remove(user); // Remove user from database
		
	}
}
