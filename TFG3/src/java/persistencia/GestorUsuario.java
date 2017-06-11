/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import dominio.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author alberto
 */
public class GestorUsuario {

	public static Usuario selectUsuarioByDNI(String dni) throws ClassNotFoundException, SQLException{
        	String laQuery = ("select * from BIBLIOTECA.USUARIO where dni='"+dni+"'");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);

		while(rs.next())
			return new Usuario(rs.getString("dni"),rs.getString("password"),rs.getString("nombre"),rs.getBoolean("administrador"), rs.getString("telefono")); 
		return null;
	}
	public static ArrayList<Usuario> selectAllUsuariosByFiltros(String dni,String nombre) throws ClassNotFoundException, SQLException{
		String laQuery;
		String filtroDNI=""; 
		String filtroNombre=""; 
		if(null!=dni&&!"".equals(dni)){
			filtroDNI=" dni like '%"+dni+"%'";
		}
		if(null!=nombre&&!"".equals(nombre)){
			if("".equals(filtroDNI)){
				filtroNombre=" nombre like '%"+nombre+"%'";	
			}
			else{
				filtroNombre=" AND (nombre like '%"+nombre+"%')";	
			}
		}
		if("".equals(filtroDNI)&&"".equals(filtroNombre)){
			laQuery=("select * from BIBLIOTECA.USUARIO");
		}
		else{
			laQuery=("select * from BIBLIOTECA.USUARIO Where"+filtroDNI+filtroNombre);
			
		}

		ResultSet rs = ConexionBD.getInstancia().select(laQuery);
		ArrayList<Usuario> usuarios = new ArrayList<>();

		while(rs.next()){
			usuarios.add(new Usuario(rs.getString("dni"),rs.getString("password"),rs.getString("nombre"),rs.getBoolean("administrador"), rs.getString("telefono")));
		}
		return usuarios;
	}
	
	public static void createUsuario(Usuario nuevo)throws ClassNotFoundException, SQLException{
		String laQuery=("insert into BIBLIOTECA.USUARIO(DNI,PASSWORD,NOMBRE,ADMINISTRADOR,TELEFONO) values('"+nuevo.getDNI()+"','"+nuevo.getPassword()+"','"+nuevo.getNombre()+"','"+nuevo.getAdministrador()+"',"+nuevo.getTelefono()+")");
		ConexionBD.getInstancia().update(laQuery);
	}

	public static void deleteUsuarioByDNI(String dni) throws ClassNotFoundException, SQLException {
		String laQuery=("delete FROM BIBLIOTECA.USUARIO WHERE dni='"+dni+"'");  
		ConexionBD.getInstancia().update(laQuery);
	}
	
}
