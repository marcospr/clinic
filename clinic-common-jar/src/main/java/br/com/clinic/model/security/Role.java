package br.com.clinic.model.security;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.clinic.converter.RoleTypeConverter;
import br.com.clinic.model.EntityDefault;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@NoArgsConstructor
public class Role extends EntityDefault {

	private static final long serialVersionUID = 3183567057814358220L;

	@Convert(converter = RoleTypeConverter.class)
	@Column(name = "name")
	private RoleType name;

	private Boolean enabled = Boolean.TRUE;

	public RoleType getName() {
		return name;
	}

	public void setName(RoleType name) {
		this.name = name;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
