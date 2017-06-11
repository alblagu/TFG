package dominio;

/**
 *
 * @author alberto
 */
public class Ejemplar {

	private final String codigo;
	private final Libro libro;
	private final boolean disponible;

	public Ejemplar(String codigo, Libro libro, boolean disponible) {
		this.codigo=codigo;
		this.libro=libro;
		this.disponible=disponible;
	}

	public String getCodigo() {
		return codigo;
	}

	public Libro getLibro() {
		return libro;
	}

	public boolean getDisponible(){
		return disponible;
	}	
	
}
