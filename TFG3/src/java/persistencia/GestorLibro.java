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
	/**
	 *  
	 * @return
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static ArrayList<Libro> selectAll() throws SQLException, ClassNotFoundException {
        	ArrayList<Libro> libros= new ArrayList<>();
        	String laQuery = ("select * from BIBLIOTECA.LIBRO");
        	ResultSet rs = ConexionBD.getInstancia().select(laQuery);
        	while (rs.next()) {
			libros.add(new Libro(rs.getString("isbn10"),rs.getString("isbn13"),rs.getString("titulo"),rs.getString("urlfoto")));
        	}
        	return libros;
    	}
	/**
	 * Obtiene un libro a partir de su isbn. 
	 * @param isbn es el isbn a partir del cual encuentras el libro.
	 * @return el libro encontrado o null si no lo encuentra.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static Libro selectLibroByISBN(String isbn) throws ClassNotFoundException, SQLException {
        	String laQuery = ("select * from BIBLIOTECA.LIBRO where isbn10='"+isbn+"' OR isbn13='"+isbn+"'");
       		ResultSet rs = ConexionBD.getInstancia().select(laQuery);
		while(rs.next())
			return new Libro(rs.getString("isbn10"), rs.getString("isbn13"), rs.getString("titulo"),rs.getString("urlfoto"));
		return null;
	}
	/**
	 * Añade un nuevo libro al sistema.
	 * @param libro es el libro que va a ser añadido.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static void create(Libro libro) throws ClassNotFoundException, SQLException {
		String laQuery=("insert into biblioteca.LIBRO(ISBN10,ISBN13, TITULO,URLFOTO) values('"+libro.getISBN10()+"','" + libro.getISBN13()+ "','" + libro.getTitulo()+ "','"+libro.getUrlFoto()+"')");
		ConexionBD.getInstancia().update(laQuery);
	}

	public static void deleteByISBN(String isbn) throws ClassNotFoundException, SQLException {
        	String laQuery = ("delete * from BIBLIOTECA.LIBRO where isbn10='"+isbn+"' OR isbn13='"+isbn+"'");
		ConexionBD.getInstancia().update(laQuery);
	}

	/**
	 * Realiza una busqueda en el sistema de los libros que tienen la cadena
	 * pasada en su isbn ya sea en el de formato 10 numeros o el de 13.
	 * @param isbn es la cadena de numeros a partir de la cual se 
	 * va a realizar la busqueda.
	 * @return un String en formato array JSON con los libros que tienen esa
	 * cadena de numeros en su ISBNS
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static ArrayList<Libro> selectLibrosByISNB(String isbn) throws ClassNotFoundException, SQLException{
        	ArrayList<Libro> libros= new ArrayList<>();
        	String laQuery = ("select * from BIBLIOTECA.LIBRO where isbn10 LIKE '%"+isbn+"%' OR isbn13 LIKE '%"+isbn+"%'");
        	ResultSet rs = ConexionBD.getInstancia().select(laQuery);
        	while (rs.next()) {
			libros.add(new Libro(rs.getString("isbn10"),rs.getString("isbn13"),rs.getString("titulo"),rs.getString("urlfoto")));
        	}
        	return libros;
		
	}

	/**
	 * Realiza una busqueda en el sistema de los libros que tienen la cadena
	 * pasada en su titulo
	 * @param titulo es la cadena partir de la cual se 
	 * va a realizar la busqueda.
	 * @return un String en formato array JSON con los libros que tienen esa
	 * cadena en su titulo.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static ArrayList<Libro> selectLibrosByTitulo(String titulo) throws ClassNotFoundException, SQLException{
        	String laQuery = ("select * from BIBLIOTECA.LIBRO where titulo LIKE '%"+titulo+"%'");
        	//String laQuery = ("select * from BIBLIOTECA.LIBRO where  UPPER(translate(titulo, ‘áéíóúÁÉÍÓÚ’, ‘aeiouAEIOU’))  LIKE UPPER(translate('%"+titulo+"%', ‘áéíóúÁÉÍÓÚ’, ‘aeiouAEIOU’");
        	ResultSet rs = ConexionBD.getInstancia().select(laQuery);		
        	ArrayList<Libro> libros= new ArrayList<Libro>();
        	while (rs.next()) {
			libros.add(new Libro(rs.getString("isbn10"),rs.getString("isbn13"),rs.getString("titulo"),rs.getString("urlfoto")));
        	}
        	return libros;
		
	}
	
	
	public static ArrayList<Libro> selectLibrosAleatorios() throws SQLException, ClassNotFoundException{
//		String laQuery="SELECT TOP 6 from BIBLIOTECA.LIBRO";
//		ResultSet rs = ConexionBD.getInstancia().select(laQuery);
//	ArrayList<Libro> libros= new ArrayList<Libro>();
//		while (rs.next()) {
//			libros.add(new Libro(rs.getString("isbn10"),rs.getString("isbn13"),rs.getString("titulo"),rs.getString("urlfoto")));
//        	}
        	return selectAll();
	}
}
