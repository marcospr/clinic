package br.com.clinic.service;

import java.util.List;

import br.com.clinic.model.EntityDefault;

public interface CrudOperation<T extends EntityDefault, L> {

	public void save(T entity);

	public void delete(T entity);

	public List<T> findAll();

	public T findById(L id);
}
