package br.com.clinic.model.security;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.com.clinic.model.EntityDefault;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile")
@NoArgsConstructor
public class Profile extends EntityDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1663503376532656424L;

	@Column(length = 200, nullable = false)
	private String name;

	private Boolean enabled = Boolean.TRUE;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "profile_role", joinColumns = { @JoinColumn(name = "profile_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
