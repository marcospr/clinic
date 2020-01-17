package br.com.clinic.service.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.jboss.crypto.CryptoUtil;

import br.com.clinic.model.security.UserSystem;
import br.com.clinic.service.UserServiceRemote;

@Singleton
public class UserService implements UserServiceRemote {

	/** Logger. */
	private Logger log = Logger.getLogger(UserService.class.getCanonicalName());

	@PersistenceContext(unitName = ConstantsService.PERSISTENCE_UNIT)
	private EntityManager em;

	@Override
	public void save(UserSystem entity) {
		if (entity.getId() == null) {
			if (entity.getPassword() == null) {
				entity.setPassword(ConstantsService.SENHA_CADASTRO);// senha default para 1o cadastro
			}
			entity.setPassword(encriptPassword(entity.getPassword()));
			em.persist(entity);
		} else {
			em.merge(entity);
		}
	}

	private String encriptPassword(String password) {
		return (CryptoUtil.createPasswordHash("MD5", CryptoUtil.BASE64_ENCODING, null, null, password));
	}

	@Override
	public void delete(UserSystem entity) {
		UserSystem userMerged = em.merge(entity);
		em.remove(userMerged);
//		em.remove(em.getReference(UserSystem.class, entity.getId()));
	}

	@Override
	public List<UserSystem> findAll() {
		CriteriaQuery<UserSystem> query = em.getCriteriaBuilder().createQuery(UserSystem.class);
		query.select(query.from(UserSystem.class));

		List<UserSystem> lista = em.createQuery(query).getResultList();

		return lista;
	}

	@Override
	public UserSystem findById(Long id) {
		return em.find(UserSystem.class, id);
	}

	@Override
	public UserSystem findByEmail(String email) {
		UserSystem user = null;
		try {
			user = em.createNamedQuery("UserSystem.findByEmail", UserSystem.class).setParameter("email", email)
					.getSingleResult();
		} catch (NoResultException e) {

		}
		return user;
	}

	@Override
	public List<UserSystem> findAllByActive(boolean actived) {
		List<UserSystem> users = null;
		users = em.createNamedQuery("UserSystem.findAllByActived", UserSystem.class).setParameter("actived", actived)
				.getResultList();
		return users;
	}

	@Override
	public List<UserSystem> findByLikeName(String likeName) {
		List<UserSystem> users = null;
		users = em.createNamedQuery("UserSystem.findByLikeName", UserSystem.class)
				.setParameter("likeName", likeName + "%").getResultList();
		return users;
	}

	@Override
	public UserSystem findByToken(String token) {
		UserSystem user = null;
		try {
			user = em.createNamedQuery("UserSystem.findByToken", UserSystem.class).setParameter("token", token)
					.getSingleResult();
		} catch (NoResultException | IllegalArgumentException e) {

		}
		return user;
	}

	@Override
	public List<UserSystem> findAllByTokenIsNotNull() {
		List<UserSystem> users = null;
		users = em.createNamedQuery("UserSystem.findAllByTokenIsNotNull", UserSystem.class).getResultList();
		return users;
	}

	@Override
	public void changePassword(Long id, String novaSenha) {
		UserSystem user = findById(id);
		// Persistir nova senha e limpar token
		user.setToken(null);
		user.setPasswordToken(null);
		user.setPassword(encriptPassword(novaSenha));
		save(user);
	}

	@Override
	public void clearToken(UserSystem entity) {
		UserSystem user = findById(entity.getId());
		// Persistir nova senha e limpar token
		user.setToken(null);
		user.setPasswordToken(null);
		save(user);
	}

}
