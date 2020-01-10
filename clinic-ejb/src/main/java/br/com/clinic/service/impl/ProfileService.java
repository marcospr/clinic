package br.com.clinic.service.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import br.com.clinic.model.security.Profile;
import br.com.clinic.service.ProfileServiceRemote;

@Singleton
public class ProfileService implements ProfileServiceRemote {

	/** Logger. */
	private Logger log = Logger.getLogger(ProfileService.class.getCanonicalName());

	@PersistenceContext(unitName = "ClinicPU")
	private EntityManager em;

	@Override
	public void save(Profile entity) {
		if (entity.getId() == null) {
			em.persist(entity);
		} else {
			em.merge(entity);
		}
	}

	@Override
	public void delete(Profile entity) {
		em.remove(entity);
	}

	@Override
	public List<Profile> findAll() {
		CriteriaQuery<Profile> query = em.getCriteriaBuilder().createQuery(Profile.class);
		query.select(query.from(Profile.class));

		List<Profile> lista = em.createQuery(query).getResultList();

		return lista;
	}

	@Override
	public Profile findById(Long id) {
		return em.find(Profile.class, id);
	}

}
