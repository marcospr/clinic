package br.com.clinic.service;

import java.util.List;

import br.com.clinic.model.EntityDefault;

public interface CruOperation<T extends EntityDefault, L> {

	public void add(T entity);

	public void update(T entity);

	public List<T> findAll();

	public T findById(L id);
}
