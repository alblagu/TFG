/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.sql.SQLException;
import java.util.ArrayList;
import persistencia.GestorLibro;

/**
 *
 * @author alberto
 */
public class Libro {
	private String isbn10;
	private String isbn13;
	private String titulo;


	public Libro(String isbn10,String isbn13, String titulo) {
		this.isbn10=isbn10;
		this.isbn13=isbn13;
		this.titulo=titulo;
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

}
