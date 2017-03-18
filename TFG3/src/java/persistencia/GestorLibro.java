/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import dominio.Libro;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author alberto
 */
public class GestorLibro {

	public static ArrayList<Libro> selectAll() throws SQLException, ClassNotFoundException {
        	ArrayList<Libro> libros= new ArrayList<>();
        	String laQuery = ("select * from BIBLIOTECA.LIBRO");
        	ResultSet rs = ConexionBD.getInstancia().select(laQuery);
        	while (rs.next()) {
			libros.add(new Libro(rs.getString("isbn10"),rs.getString("isbn13"),rs.getString("titulo"),rs.getString("urlfoto")));
        	}
        	return libros;
    	}

	public static Libro selectLibroByISBN(String isbn) throws ClassNotFoundException, SQLException {
        	String laQuery = ("select * from BIBLIOTECA.LIBRO where isbn10='"+isbn+"' OR isbn13='"+isbn+"'");
       		ResultSet rs = ConexionBD.getInstancia().select(laQuery);
		while(rs.next())
			return new Libro(rs.getString("isbn10"), rs.getString("isbn13"), rs.getString("titulo"),rs.getString("urlfoto"));
		return null;
	}

	public static void create(Libro libro) throws ClassNotFoundException, SQLException {
		String laQuery=("insert into biblioteca.LIBRO(ISBN10,ISBN13, TITULO,URLFOTO) values('"+libro.getISBN10()+"','" + libro.getISBN13()+ "','" + libro.getTitulo()+ "','"+libro.getUrlFoto()+"')");
		ConexionBD.getInstancia().update(laQuery);
	}

	public static void deleteByISBN(String isbn) throws ClassNotFoundException, SQLException {
        	String laQuery = ("delete * from BIBLIOTECA.LIBRO where isbn10='"+isbn+"' OR isbn13='"+isbn+"'");
		ConexionBD.getInstancia().update(laQuery);
	}
}
