package br.com.clinic.model.security;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.clinic.model.EntityDefault;
import lombok.NoArgsConstructor;

/**
 * Usuario.
 * 
 * @author Marcos
 */
@NamedQueries({
		@NamedQuery(name = "UserSystem.findByToken", query = "select u from UserSystem u where u.token = :token"),
		@NamedQuery(name = "UserSystem.findByEmail", query = "select u from UserSystem u where u.email = :email"),
		@NamedQuery(name = "UserSystem.findByLikeName", query = "select u from UserSystem u where u.name like :likeName"),
		@NamedQuery(name = "UserSystem.findAllByActived", query = "select u from UserSystem u where u.actived = :actived"),
		@NamedQuery(name = "UserSystem.findAllByTokenIsNotNull", query = "select u from UserSystem u where u.token is not null") })
@Entity
@Table(name = "user_system")
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSystem extends EntityDefault {

	private static final long serialVersionUID = 1838344567830360265L;

	@Column(length = 100, nullable = true)
	private String name;

	@Column(length = 100, nullable = false)
	private String email;

	@Column(length = 100)
	private String phone;

//	@Type(type = "encryptedString")
	@Column(length = 100, nullable = false)
	private String password;

	@Column(name = "user_expires_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date userExpiresDate;

	@Column(name = "pass_expires_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date passExpiresDate;

	@Column(length = 1000)
	private String token;

	@Column(length = 1000)
	private String passwordToken;

	/**
	 * Perfil do usuario.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_profile", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "profile_id") })
	private Set<Profile> profile;

	/** Usuario Ativo. */
	private Boolean actived = Boolean.TRUE;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Profile> getProfile() {
		return profile;
	}

	public void setProfile(Set<Profile> profile) {
		this.profile = profile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getUserExpiresDate() {
		return userExpiresDate;
	}

	public void setUserExpiresDate(Date userExpiresDate) {
		this.userExpiresDate = userExpiresDate;
	}

	public Date getPassExpiresDate() {
		return passExpiresDate;
	}

	public void setPassExpiresDate(Date passExpiresDate) {
		this.passExpiresDate = passExpiresDate;
	}

	public Boolean getActived() {
		return actived;
	}

	public void setActived(Boolean actived) {
		this.actived = actived;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

	public String getPasswordToken() {
		return this.passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}

}
