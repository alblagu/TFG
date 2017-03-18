/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import dominio.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author alberto
 */
public class GestorUsuario {

	public static Usuario selectUsuarioByDNI(String dni) throws ClassNotFoundException, SQLException{
        	String laQuery = ("select * from BIBLIOTECA.USUARIO where dni='"+dni+"'");
		ResultSet rs =ConexionBD.getInstancia().select(laQuery);

		while(rs.next())
			return new Usuario(rs.getString("dni")); 
		return null;
		
	}
	
}
