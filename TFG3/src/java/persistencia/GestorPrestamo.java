/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import dominio.Prestamo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author alberto
 */
public class GestorPrestamo {

	public static ArrayList<Prestamo> selectPrestamosByEjemplar(String ejemplar) throws ClassNotFoundException, SQLException {
		String laQuery = ("select * from BIBLIOTECA.PRESTAMO where ejemplar='"+ejemplar+"'");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);
		ArrayList<Prestamo> prestamos=new ArrayList<>(); 
		
		while(rs.next()){
			prestamos.add(new Prestamo(rs.getInt("id"),GestorEjemplar.selectEjemplarByCodigo(rs.getString("ejemplar")),GestorUsuario.selectUsuarioByDNI(rs.getString("usuario")),rs.getString("estado"),rs.getDate("fecha_ini"),rs.getDate("fecha_fin")));
	}
		return prestamos;	
        	
	}

	public static ArrayList<Prestamo> selectPrestamosByUsuario(String usuario) throws ClassNotFoundException, SQLException {
		String laQuery = ("select * from BIBLIOTECA.PRESTAMO where usuario='"+usuario+"' AND estado='enCurso'");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);
		ArrayList<Prestamo> prestamos=new ArrayList<>(); 
		
		while(rs.next()){
			prestamos.add(new Prestamo(rs.getInt("id"),GestorEjemplar.selectEjemplarByCodigo(rs.getString("ejemplar")),GestorUsuario.selectUsuarioByDNI(rs.getString("usuario")),rs.getString("estado"),rs.getDate("fecha_ini"),rs.getDate("fecha_fin")));
	}
		return prestamos;	
	}
}
