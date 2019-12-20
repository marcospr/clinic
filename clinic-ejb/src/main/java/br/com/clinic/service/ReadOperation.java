package br.com.clinic.service;

import java.util.List;

import br.com.clinic.model.EntityDefault;

public interface ReadOperation<T extends EntityDefault, L> {

	public List<T> findAll();

	public T findById(L id);
}
