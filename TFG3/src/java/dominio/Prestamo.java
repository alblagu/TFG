/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.sql.Date;

/**
 *
 * @author alberto
 */
public class Prestamo {
	private final int id;
	private final Ejemplar ejemplar;
	private final Usuario usuario;
	private final Date fechaIni;
	private final Date fechaFin;

	public Prestamo(int id,Ejemplar ejemplar, Usuario usuario, Date fechaIni, Date fechaFin) {
		this.id=id;
		this.ejemplar=ejemplar;
		this.usuario=usuario;
		this.fechaIni=fechaIni;
		this.fechaFin=fechaFin;
	}

	public int getId() {
		return id;
	}

	public Ejemplar getEjemplar() {
		return ejemplar;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}


	
}
