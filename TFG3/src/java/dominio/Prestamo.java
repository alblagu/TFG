package dominio;

import java.util.Date;


/**
 *
 * @author alberto
 */
public class Prestamo {
	private final int id;
	private final Ejemplar ejemplar;
	private final Usuario usuario;
	private final boolean enCurso;
	private final Date fechaIni;
	private final Date fechaFin;

	public Prestamo(int id,Ejemplar ejemplar, Usuario usuario, boolean enCurso, Date fechaIni, Date fechaFin) {
		this.id=id;
		this.ejemplar=ejemplar;
		this.usuario=usuario;
		this.enCurso=enCurso;
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

	public boolean getEnCurso() {
		return enCurso;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}
}
