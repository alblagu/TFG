/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

/**
 *
 * @author alberto
 */
public class Usuario {
	private final String dni;
	private final String password;
	private final String nombre;
	private final boolean administrador;
	private final String telefono;

	public Usuario(String dni, String password, String nombre,  boolean administrador, String telefono) {
		this.dni = dni;
		this.password = password;
		this.nombre = nombre;
		this.administrador=administrador;
		this.telefono = telefono;
	}
	
	public String getDNI() {
		return dni;
	}

	public String getPassword() {
		return password;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean getAdministrador(){
		return administrador;
	}

	public String getTelefono() {
		return telefono;
	}

	
}
