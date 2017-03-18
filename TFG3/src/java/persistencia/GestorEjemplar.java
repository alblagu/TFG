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

	public static Ejemplar selectEjemplarByCodigo(String codigo) throws SQLException, ClassNotFoundException{
        	String laQuery = ("select * from BIBLIOTECA.EJEMPLAR where codigo='"+codigo+"'");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);

		while(rs.next())
			return new Ejemplar(rs.getString("codigo"),GestorLibro.selectLibroByISBN(rs.getString("libro"))); 
		return null;
	}

	public static ArrayList<Ejemplar> selectEjemplaresByISBN(String isbn) throws ClassNotFoundException, SQLException {
		String laQuery=("select * from BIBLIOTECA.EJEMPLAR where libro='"+isbn+"'");
		ResultSet rs=ConexionBD.getInstancia().select(laQuery);

		ArrayList<Ejemplar> ejemplares=new ArrayList<>();
		while(rs.next())
			ejemplares.add(new Ejemplar(rs.getString("codigo"),GestorLibro.selectLibroByISBN(rs.getString("libro"))));
		return ejemplares;
	}
	

	public static void create(Ejemplar ejemplar) throws ClassNotFoundException, SQLException {
		String laQuery=("insert into BIBLIOTECA.EJEMPLAR(CODIG0,LIBRO) values('"+ejemplar.getCodigo()+"','"+ejemplar.getLibro().getISBN10()+")");
		ConexionBD.getInstancia().update(laQuery);
	}

	public static void deleteByCodigo(String codigo) throws ClassNotFoundException, SQLException {
		String laQuery=("delete * from BIBLIOTECA.EJEMPLAR where codigo='"+codigo+"'");
		ConexionBD.getInstancia().update(laQuery);
	}
	
}
