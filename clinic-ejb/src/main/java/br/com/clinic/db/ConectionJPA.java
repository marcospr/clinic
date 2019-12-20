package br.com.clinic.db;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import br.com.clinic.model.EntityDefault;

public class ConectionJPA<T extends EntityDefault, L> implements ConectionDefaultDB<T, L> {

	private EntityManager em;

	@Override
	public void save(T entity) {
		if (entity.getId() == null)
			em.persist(entity);
		else
			em.merge(entity);

	}

	@Override
	public void delete(T entity) {
		em.remove(entity);

	}

	@Override
	public List<T> findAll() {
		String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
				.getTypeName();
		Class<T> clazz;
		List<T> lista = null;
		try {
			clazz = (Class<T>) Class.forName(className);
			CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(clazz);
			query.select(query.from(clazz));

			lista = em.createQuery(query).getResultList();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return lista;
	}

	@Override
	public T findById(L id) {
		String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
				.getTypeName();
		Class<T> clazz;
		T find = null;
		try {
			clazz = (Class<T>) Class.forName(className);
			find = em.find(clazz, id);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return find;
	}

}
