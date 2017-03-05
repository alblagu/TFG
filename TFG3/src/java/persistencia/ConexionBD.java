/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Se encarga de realizar la conexion a la base de datos. Y obtener los datos de
 * ella.
 *
 * @author Alberto Laguna Humayor .
 * ConexionBD es un Singleton
 */
public class ConexionBD {

    // Este es la variable "singleton"
    private static ConexionBD laConexion;

    private final String urlBD = "jdbc:derby://localhost:1527/biblioteca";
    private final String userName = "biblioteca";
    private final String password = "biblioteca";

    private final String driverName = "org.apache.derby.jdbc.ClientDriver";

    private final Connection conexion;

    // Garantiza que el cliente no cree objetos
    private ConexionBD() throws SQLException, ClassNotFoundException {
        Class.forName(driverName);
        conexion = DriverManager.getConnection(urlBD, userName, password);
    }

    /**
     * @return la instancia de la ConexionBD
     * @throws ClassNotFoundException cuando no se pueda realizar la conexion.
     */
    public static ConexionBD getInstancia() throws ClassNotFoundException {
        if (laConexion == null) {
            try {
                laConexion = new ConexionBD();
            } catch (SQLException a) {
                throw new IllegalArgumentException("Conexion rehusada");
            }
        }
        return laConexion;
    }

    /**
     * @param laQuery a partir de la cual se realiza la consulta.
     * @return el resultado de la consulta
     * @throws java.sql.SQLException
     * @throws IllegalArgumentException cuando no encuentre el dato que hay que
     * actualizar
     */
    public ResultSet select(String laQuery) throws SQLException {
        PreparedStatement pst = conexion.prepareStatement(laQuery);
        return pst.executeQuery();
    }

    public void update(String laQuery) throws SQLException {
        try {
            PreparedStatement pst = conexion.prepareStatement(laQuery);
            pst.execute();
        } catch (SQLException a) {
            throw new IllegalArgumentException(a.getMessage());
        }
    }
}