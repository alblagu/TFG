/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import dominio.Ejemplar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author alberto
 */
public class GestorEjemplar {
	
	/**
	 * Obtiene un ejemplar a partir de su codigo.
	 * @param codigo es el codigo a partir del cual se busca el ejemplar.
	 * @return un ejemplar si lo encuentra y null si no lo encuentra.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static Ejemplar selectEjemplarByCodigo(String codigo) throws SQLException, ClassNotFoundException{
        	String laQuery = ("select * from BIBLIOTECA.EJEMPLAR where codigo='"+codigo+"'");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);

		while(rs.next())
			return new Ejemplar(rs.getString("codigo"),GestorLibro.selectLibroByISBN(rs.getString("libro")),rs.getBoolean("disponible")); 
		return null;
	}

	/**
	 * Obtiene los ejemplares que el sistema tiene asociados a un libro. 
	 * @param isbn del libro al que estan asociados.
	 * @return un ArrayList de Ejemplar con los ejemplares encontrados.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static ArrayList<Ejemplar> selectEjemplaresByISBN(String isbn) throws ClassNotFoundException, SQLException {
		String laQuery=("select * from BIBLIOTECA.EJEMPLAR where libro='"+isbn+"'");
		ResultSet rs=ConexionBD.getInstancia().select(laQuery);

		ArrayList<Ejemplar> ejemplares=new ArrayList<>();
		while(rs.next())
			ejemplares.add(new Ejemplar(rs.getString("codigo"),GestorLibro.selectLibroByISBN(rs.getString("libro")),rs.getBoolean("disponible")));
		return ejemplares;
	}
	
	/**
	 * Añade un nuevo ejemplar al sistema.
	 * @param ejemplar es el ejemplar que va a ser añadido.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static void create(Ejemplar ejemplar) throws ClassNotFoundException, SQLException {
		String laQuery=("insert into BIBLIOTECA.EJEMPLAR(CODIGO,LIBRO,ESTADO) values('"+ejemplar.getCodigo()+"','"+ejemplar.getLibro().getISBN10()+"','"+ejemplar.getDisponible()+"')");
		ConexionBD.getInstancia().update(laQuery);
	}

	/**
	 * 
	 * @param codigo
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	public static void deleteByCodigo(String codigo) throws ClassNotFoundException, SQLException {
		String laQuery=("delete * from BIBLIOTECA.EJEMPLAR where codigo='"+codigo+"'");
		ConexionBD.getInstancia().update(laQuery);
	}
	
}
