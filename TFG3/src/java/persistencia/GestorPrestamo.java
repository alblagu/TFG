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

	public static Prestamo selectPrestamosByEjemplar(String codigo) throws ClassNotFoundException, SQLException {
		String laQuery = ("select * from BIBLIOTECA.PRESTAMO where ejemplar='"+codigo+"' AND enCurso='true'");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);
		while(rs.next()){
			return new Prestamo(rs.getInt("id"),GestorEjemplar.selectEjemplarByCodigo(rs.getString("ejemplar")),GestorUsuario.selectUsuarioByDNI(rs.getString("usuario")),rs.getBoolean("enCurso"),rs.getDate("fecha_ini"),rs.getDate("fecha_fin"));
	}
		return null;	
        	
	}

	public static ArrayList<Prestamo> selectPrestamosByUsuario(String usuario) throws ClassNotFoundException, SQLException {
		String laQuery = ("select * from BIBLIOTECA.PRESTAMO where usuario='"+usuario+"' AND enCurso=true");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);
		ArrayList<Prestamo> prestamos=new ArrayList<>(); 
		
		while(rs.next()){
			prestamos.add(new Prestamo(rs.getInt("id"),GestorEjemplar.selectEjemplarByCodigo(rs.getString("ejemplar")),GestorUsuario.selectUsuarioByDNI(rs.getString("usuario")),rs.getBoolean("enCurso"),rs.getDate("fecha_ini"),rs.getDate("fecha_fin")));
	}
		return prestamos;	
	}

	public static void createNuevoPrestamo(Prestamo prestamo) throws ClassNotFoundException, SQLException {
	String laQuery=("insert into BIBLIOTECA.PRESTAMO(ID,USUARIO,EJEMPLAR,ENCURSO,FECHA_INI,FECHA_FIN) values("+ prestamo.getId()+",'"+prestamo.getUsuario().getDNI()+"','"+prestamo.getEjemplar().getCodigo()+"','"+prestamo.getEnCurso()+"','2016-1-1','2016-2-1')");
	GestorEjemplar.updateEjemplarCambiaDisponible(prestamo.getEjemplar());

		ConexionBD.getInstancia().update(laQuery);
	}

	public static int selectID() throws ClassNotFoundException, SQLException{
		String laQuery = ("select * from BIBLIOTECA.PRESTAMO");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);
		int id=0;
		while(rs.next())
			id=rs.getInt("id");
		return id;
	}
}
