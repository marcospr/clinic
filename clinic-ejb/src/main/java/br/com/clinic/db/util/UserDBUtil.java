package br.com.clinic.db.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;

import br.com.clinic.model.security.Profile;
import br.com.clinic.model.security.Role;
import br.com.clinic.model.security.RoleType;
import br.com.clinic.model.security.UserSystem;
import sun.misc.BASE64Encoder;

public class UserDBUtil {
	static EntityManager em = null;

	public static void insertUser(String nome, String email, String senha) {

		UserSystem user = new UserSystem();
		user.setName(nome);
		user.setEmail(email);
//		user.setPassword(CryptoUtil.createPasswordHash("MD5", CryptoUtil.BASE64_ENCODING, null, null, senha));
		try {
			user.setPassword(crypt(senha));
		} catch (Exception e) {
			e.printStackTrace();
		}
		em.persist(user);

	}

	public static void insertProfile(String name) {
		Profile profile = new Profile();
		profile.setName(name);
		profile.setEnabled(true);
		em.persist(profile);

		CriteriaQuery<UserSystem> query = em.getCriteriaBuilder().createQuery(UserSystem.class);
		query.select(query.from(UserSystem.class));
		List<UserSystem> users = em.createQuery(query).getResultList();
		for (UserSystem user : users) {
			if (user.getProfile() == null) {
				user.setProfile(new HashSet<>());
			}
			if (user.getEmail().equals("admin@gmail.com")) {

			}
			user.getProfile().add(profile);
			em.persist(user);
		}
	}

	public static void insertProfileRole() {
		CriteriaQuery<Profile> query = em.getCriteriaBuilder().createQuery(Profile.class);
		query.select(query.from(Profile.class));
		List<Profile> profiles = em.createQuery(query).getResultList();

		CriteriaQuery<Role> queryRole = em.getCriteriaBuilder().createQuery(Role.class);
		queryRole.select(queryRole.from(Role.class));
		List<Role> roles = em.createQuery(queryRole).getResultList();

		for (Profile profile : profiles) {
			for (Role role : roles) {
				if (profile.getRoles() == null) {
					profile.setRoles(new HashSet<>());
				}
				profile.getRoles().add(role);
			}
			em.merge(profile);
		}

	}

	public static void insertRole(RoleType name) {
		Role role = new Role();
		role.setName(name);
		role.setEnabled(true);
		em.persist(role);

	}

	public static void deleteAll() {
		em.createNativeQuery("delete from profile_role").executeUpdate();
		em.createQuery("DELETE FROM UserSystem").executeUpdate();
		em.createQuery("DELETE FROM Profile").executeUpdate();
		em.createQuery("DELETE FROM Role").executeUpdate();
	}

	public static void main(String args[]) {
		em = JpaUtil.getEntityManager();
		EntityTransaction trx = em.getTransaction();
		trx.begin();

		deleteAll();

		insertUser("admin", "admin@gmail.com", "123");
		insertUser("marcos", "marcos@gmail.com", "123");

		insertProfile("admin");
		insertProfile("operator");

		insertRole(RoleType.ADMIN_SECURITY);
		insertRole(RoleType.MAIN);

		insertRole(RoleType.PATIENT_REGISTRATION_READ);
		insertRole(RoleType.PATIENT_REGISTRATION_CREATE);
		insertRole(RoleType.PATIENT_REGISTRATION_UPDATE);

		insertProfileRole();

		trx.commit();
		em.close();
	}

	private static String crypt(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(senha.getBytes());
		BASE64Encoder encoder = new BASE64Encoder();
//		System.out.println(encoder.encode(digest.digest()));
		return encoder.encode(digest.digest());
	}

//	public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//		String senha = "admin";
//
//		MessageDigest digest = MessageDigest.getInstance("MD5");
//		digest.update(senha.getBytes());
//		BASE64Encoder encoder = new BASE64Encoder();
//		System.out.println(encoder.encode(digest.digest()));
//
//		String createPasswordHash = CryptoUtil.createPasswordHash("MD5", CryptoUtil.BASE64_ENCODING, null, null, senha);
//		System.out.println(createPasswordHash);
//	}
}
