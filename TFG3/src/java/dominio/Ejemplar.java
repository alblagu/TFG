/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

/**
 *
 * @author alberto
 */
public class Ejemplar {

	private final String codigo;
	private final Libro libro;

	public Ejemplar(String codigo, Libro libro) {
		this.codigo=codigo;
		this.libro=libro;
	}

	public String getCodigo() {
		return codigo;
	}

	public Libro getLibro() {
		return libro;
	}

	
	
}
