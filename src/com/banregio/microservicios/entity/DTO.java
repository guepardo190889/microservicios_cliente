package com.banregio.microservicios.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author blackdeath
 *
 */
public class DTO implements Serializable {

	private static final long serialVersionUID = -7084315064057977631L;

	private Long id;

	private String nombre;

	private String apellido;

	private String email;

	private Date createAt;

	private String foto;

	public DTO() {
	}

	public String getNombreConApellido() {
		return getNombre() + " " + getApellido();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@Override
	public String toString() {
		return "DTO [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + ", createAt="
				+ createAt + ", foto=" + foto + "]";
	}

}
