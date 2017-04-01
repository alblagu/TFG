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
	private final String estado;
	private final Date fechaIni;
	private final Date fechaFin;

	public Prestamo(int id,Ejemplar ejemplar, Usuario usuario, String estado, Date fechaIni, Date fechaFin) {
		this.id=id;
		this.ejemplar=ejemplar;
		this.usuario=usuario;
		this.estado=estado;
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

	public String getEstado() {
		return estado;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}
}
