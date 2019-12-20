package br.com.clinic.db.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

	private static EntityManagerFactory factory;

	static {
		factory = Persistence.createEntityManagerFactory("ClinicTestePU");
	}

	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

}