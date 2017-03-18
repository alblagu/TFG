/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicioweb;

import dominio.Ejemplar;
import dominio.Libro;
import dominio.Prestamo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Exception;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
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

/**
 * REST Web Service
 *
 * @author alberto
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
	public String getLibros() {
		ArrayList<Libro> libros= new ArrayList<>();
		JSONArray libros2=new JSONArray();

		try {
			libros=GestorLibro.selectAll();
		} catch (SQLException | ClassNotFoundException ex) {
			Logger.getLogger(ServicioWeb.class.getName()).log(Level.SEVERE, null, ex);
		}

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
	
	@GET
	@Path("libros/{isbn}")
        @Produces(MediaType.TEXT_PLAIN)
	public String getLibro(@PathParam ("isbn") String isbn) {
		boolean internet=false;	
		Libro libro;
		try {
			libro=GestorLibro.selectLibroByISBN(isbn);
			if(libro==null)throw new IllegalArgumentException("");
		} catch ( Exception ex) {
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
			for(int i=0;i<isbns.length();i++){
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

		return libroJson.toString();
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

	
	@GET
	@Path("ejemplares/{isbn}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getEjemplaresByLibro(@PathParam ("isbn") String isbn){
		ArrayList<Ejemplar> ejemplares=null;
		JSONArray ejemplares2=new JSONArray();

		try {
			ejemplares=GestorEjemplar.selectEjemplaresByISBN(isbn);
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(ServicioWeb.class.getName()).log(Level.SEVERE, null, ex);
		}
		for(int i=0;i<ejemplares.size();i++){
			JSONObject ejemplar=new JSONObject();
			ejemplar.put("codigo", ejemplares.get(i).getCodigo());
			ejemplar.put("isbn10", ejemplares.get(i).getLibro().getISBN10());
			ejemplares2.put(ejemplar);
		}
		return ejemplares2.toString();
	}	

	
	@POST
	@Path("ejemplares/{codigo}/{ejemplar}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void addEjemplarToLibro( @PathParam("codigo")String codigo, @PathParam("ejemplar") String ejemplar){
		System.out.println(ejemplar);
		//JSONObject a=new JSONObject(libro.toString());
		//System.out.println(a.getString("isbn10"));
//		if(true){
//			
//		}
//		else{
//			GestorLibro.create(new Libro(libro.getString("isbn10")));
//		}
//		
//			
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

	@GET
	@Path("prestamos/{ejemplar}")
        @Produces(MediaType.TEXT_PLAIN)
	public String getPrestamosByEjemplar(@PathParam ("ejemplar") String ejemplar) {
		ArrayList<Prestamo> prestamos=null;
		JSONArray prestamos2 = new JSONArray();
		try {
			prestamos=GestorPrestamo.selectPrestamosByEjemplar(ejemplar);
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(ServicioWeb.class.getName()).log(Level.SEVERE, null, ex);
		}
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
	
	@POST
	@Path("usuarios/{usuario}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addNuevoUsuario( JSONObject a, @PathParam("codigo")String codigo){
	}
	
}
