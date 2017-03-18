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
public class Libro {
	private final String isbn10;
	private final String isbn13;
	private final String titulo;
	private final String urlFoto;

	public Libro(String isbn10,String isbn13, String titulo, String urlFoto) {
		this.isbn10=isbn10;
		this.isbn13=isbn13;
		this.titulo=titulo;
		this.urlFoto=urlFoto;
	}

	public String getISBN10() {
		return isbn10;
	}
	
	public String getISBN13() {
		return isbn13;
	}
	
	public String getTitulo(){
		return titulo;
	}

	public String getUrlFoto() {
		return urlFoto;
	}

}
