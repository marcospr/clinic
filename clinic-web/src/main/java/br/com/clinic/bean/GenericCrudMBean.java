package br.com.clinic.bean;

import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.clinic.model.EntityDefault;

@Named
@ViewScoped
public abstract class GenericCrudMBean<T extends EntityDefault, L> extends GenericMBean<T> {
	private static final long serialVersionUID = 1L;

	@Override
	protected ExternalContext externalContext() {
		return facesContext().getExternalContext();
	}

	@Override
	public String getRequestContextPath() {
		return externalContext().getRequestContextPath();
	}

	@Override
	protected FacesContext facesContext() {
		return FacesContext.getCurrentInstance();
	}

	protected abstract void save(T entity);

	protected abstract void delete(T entity);

	protected abstract List<T> findAll();

	protected abstract T findById(L id);

}
