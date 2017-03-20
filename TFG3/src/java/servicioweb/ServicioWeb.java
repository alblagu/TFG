/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicioweb;

import dominio.Ejemplar;
import dominio.Libro;
import dominio.Prestamo;
import dominio.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;
import persistencia.GestorEjemplar;
import persistencia.GestorLibro;
import persistencia.GestorPrestamo;
import persistencia.GestorUsuario;

/**
 * REST Web Service
 * Servicio Web de la biblioteca que permite realizar busquedas por isbn, 
 * por titulo  hacer prestamos, añadir nuevos libros y ejemplares al sistema,
 * añadir nuevos usuarios...
 * @author Alberto Laguna Humayor.
 */
@Path("generic")
public class ServicioWeb {

	/**********************************************************************
        ******** 			LIBROS				 ******
	**********************************************************************/
	
	/**
	 * Retrieves representation of an instance of servicioweb.ServicioWeb
	 * @return an instance of java.lang.String
	 */
	@GET
	@Path("libros")
        @Produces(MediaType.TEXT_PLAIN)
	public String getLibros() throws SQLException, ClassNotFoundException {
		ArrayList<Libro> libros= new ArrayList<>();
		JSONArray libros2=new JSONArray();

		libros=GestorLibro.selectAll();

		for(int i =0;i<libros.size();i++){
			JSONObject nuevo=new JSONObject();
			nuevo.put("isbn10", libros.get(i).getISBN10());
			nuevo.put("isbn13", libros.get(i).getISBN13());
			nuevo.put("titulo", libros.get(i).getTitulo());
			nuevo.put("urlfoto", libros.get(i).getUrlFoto());
			libros2.put(nuevo);
		}

		return libros2.toString();
	}

	/**
	 * Obtiene un libro a partir de su isbn, si el libro esta ya registrado
	 * en el sistema lo obtiene de ahi; en caso de no ser asi su informacion
	 * es obtenida de la api de google.
	 * @param isbn es el isbn a partir del cual se va a obtener el libro
	 * puede ser de formato 10 numeros o de 13 numeros.
	 * @return un String en formato JSON con la informacion del libro.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */	
	@GET
	@Path("libro/{isbn}")
        @Produces(MediaType.TEXT_PLAIN)
	public String getLibro(@PathParam ("isbn") String isbn) throws ClassNotFoundException, SQLException {
		boolean internet=false;//almacena si la informacion es sacada de la api de google.	
		Libro libro=GestorLibro.selectLibroByISBN(isbn);
		if(libro==null){ //Obtiene la informacion de la api de Google
			Client client=ClientBuilder.newClient();
			WebTarget webTarget =client.target("https://www.googleapis.com/books/v1/volumes?q=isbn:"+isbn);
			String libroString= webTarget.request().get(String.class);
			int posicion=libroString.indexOf("[");
			libroString=libroString.substring(posicion,libroString.length());
			JSONArray libro2= new JSONArray(libroString);
			String titulo= libro2.getJSONObject(0).getJSONObject("volumeInfo").getString("title");
			String urlFoto=libro2.getJSONObject(0).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail");
			String isbn10="";
			String isbn13="";
		
			JSONArray isbns=libro2.getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("industryIdentifiers");
			for(int i=0;i<isbns.length();i++){ //Obtiene ISBNS del libro.
				if(isbns.getJSONObject(i).get("type").equals("ISBN_10"))
					isbn10=isbns.getJSONObject(i).getString("identifier");
				else{
					if(isbns.getJSONObject(i).get("type").equals("ISBN_13"))
						isbn13=isbns.getJSONObject(i).getString("identifier");
				}
			}
			libro= new Libro(isbn10,isbn13,titulo,urlFoto);
			internet=true;
			
		}
		JSONObject libroJson=new JSONObject();
		libroJson.put("isbn10", libro.getISBN10());
		libroJson.put("isbn13", libro.getISBN13());
		libroJson.put("titulo", libro.getTitulo());
		libroJson.put("urlfoto", libro.getUrlFoto());
		libroJson.put("internet", internet);

		return libroJson.toString();
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
	@GET
	@Path("libros/busqueda/{isbn}")
        @Produces(MediaType.TEXT_PLAIN)
	public String getLibrosBusquedaISBN(@PathParam ("isbn") String isbn) throws ClassNotFoundException, SQLException {
		ArrayList<Libro> libros = GestorLibro.selectLibrosByISNB(isbn);
		return getLibros(libros);
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
	@GET
	@Path("libros/busqueda2/{titulo}")
        @Produces(MediaType.TEXT_PLAIN)
	public String getLibrosBusquedaTitutlo(@PathParam ("titulo") String titulo) throws ClassNotFoundException, SQLException {
		ArrayList<Libro> libros = GestorLibro.selectLibrosByTitulo(titulo);
		return getLibros(libros);
		
	}
	/**
	 * Convierte un arrayList de Libro en uno de JSON 
	 * @param libros el arrayList de libros que se va a convertir.
	 * @return en formato String el array de JSON.
	 */
	private String getLibros(ArrayList<Libro> libros){
		JSONArray libros2=new JSONArray();

		for(int i =0;i<libros.size();i++){
			JSONObject nuevo=new JSONObject();
			nuevo.put("isbn10", libros.get(i).getISBN10());
			nuevo.put("isbn13", libros.get(i).getISBN13());
			nuevo.put("titulo", libros.get(i).getTitulo());
			nuevo.put("urlfoto", libros.get(i).getUrlFoto());
			libros2.put(nuevo);
		}

		return libros2.toString();
	}
	

//	@DELETE
//	@Path("{isbn}")
//	@Produces
//	public void deleteLibro(@PathParam("isbn")String isbn){
//		try {
//			GestorLibro.deleteByISBN(isbn);
//		} catch (ClassNotFoundException | SQLException ex) {
//			Logger.getLogger(ServicioWeb.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	}
	
	/**********************************************************************
        ******** 			EJEMPLARES  			 ******
	**********************************************************************/

	/**
	 * Obtiene los ejemplares que el sistema tiene asociados al isbn pasado. 
	 * @param isbn es el isbn a partir del cual se va a obtener los ejemplares.
	 * @return un array JSON en formato String con los ejemplares.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */	
	@GET
	@Path("ejemplares/{isbn}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getEjemplaresByLibro(@PathParam ("isbn") String isbn) throws ClassNotFoundException, SQLException {
		ArrayList<Ejemplar> ejemplares=GestorEjemplar.selectEjemplaresByISBN(isbn);
		JSONArray ejemplares2=new JSONArray();

		for(int i=0;i<ejemplares.size();i++){
			JSONObject ejemplar=new JSONObject();
			ejemplar.put("codigo", ejemplares.get(i).getCodigo());
			ejemplar.put("isbn10", ejemplares.get(i).getLibro().getISBN10());
			ejemplares2.put(ejemplar);
		}
		return ejemplares2.toString();
	}	

	/**
 	* Añade un nuevo ejemplar al sistema; y en caso de que el libro
	* al que corresponde no estuviera añadido; tambien lo añade.
 	* @param json es el libro en formato JSON al que corresponde el ejemplar.
 	* @param codigo es el codigo del ejemplar.
	* @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	* @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
 	*/	
	@POST
	@Path("ejemplares/{codigo}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addEjemplarToLibro(String json, @PathParam("codigo")String codigo) throws ClassNotFoundException, SQLException{
		JSONObject libro=new JSONObject(json);
		if(libro.getBoolean("internet"))
			GestorLibro.create(new Libro(libro.getString("isbn10"),libro.getString("isbn13"),libro.getString("titulo"),libro.getString("urlfoto")));
		else
			GestorEjemplar.create(new Ejemplar(codigo,new Libro(libro.getString("isbn10"),libro.getString("isbn13"),libro.getString("titulo"),libro.getString("urlfoto"))));
			
	}

//	@DELETE
//	@Path("ejemplares/{codigo}")
//	@Produces
//	public void deleteEjemplar(@PathParam("codigo")String codigo){
//		try {
//			GestorEjemplar.delete(codigo);
//		} catch (ClassNotFoundException | SQLException ex) {
//			Logger.getLogger(ServicioWeb.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	}





	
	/**********************************************************************
        ******** 			PRESTAMOS			 ******
	**********************************************************************/
	/**
	 * 
	 * @param ejemplar
	 * @return 
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */
	@GET
	@Path("prestamos/{ejemplar}")
        @Produces(MediaType.TEXT_PLAIN)
	public String getPrestamosByEjemplar(@PathParam ("ejemplar") String ejemplar) throws ClassNotFoundException, SQLException {
		ArrayList<Prestamo> prestamos=GestorPrestamo.selectPrestamosByEjemplar(ejemplar);
		JSONArray prestamos2 = new JSONArray();
		
		for(int i=0;i<prestamos.size();i++){
			JSONObject nuevo=new JSONObject();
			nuevo.put("id", prestamos.get(i).getId());
			nuevo.put("usuario", prestamos.get(i).getUsuario().getDNI());
			nuevo.put("ejemplar", prestamos.get(i).getEjemplar().getCodigo());
			nuevo.put("fecha_ini",prestamos.get(i).getFechaIni());
			nuevo.put("fecha_fin",prestamos.get(i).getFechaFin());
			prestamos2.put(nuevo);
		}
		return prestamos2.toString();
	}
	
	/**********************************************************************
        ******** 			USUARIOS			******
	**********************************************************************/
	/**
	 * 
	 * @param json
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base de datos.
	 */	
	@POST
	@Path("usuarios")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addNuevoUsuario( String json) throws ClassNotFoundException, SQLException{
		JSONObject usuario=new JSONObject(json);	
		if(GestorUsuario.selectUsuarioByDNI(json)!=null){
			throw new IllegalArgumentException("Ya hay un usuario con ese dni");
		}

		GestorUsuario.createUsuario(new Usuario(usuario.getString("dni"),usuario.getString("password"),usuario.getString("nombre"),usuario.getString("apellidos"),usuario.getString("telefono")));

	}
}
