/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicioweb;

import dominio.Libro;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import persistencia.GestorLibro;

/**
 * REST Web Service
 *
 * @author alberto
 */
@Path("generic")
public class ServicioWeb {

	@Context
	private UriInfo context;

	/**
	 * Creates a new instance of ServicioWeb
	 */
	public ServicioWeb() {
	}

	/**
	 * Retrieves representation of an instance of servicioweb.ServicioWeb
	 * @return an instance of java.lang.String
	 */
	@GET
	@Path("libros")
        @Produces(MediaType.TEXT_PLAIN)
	public String getLibros() {
		ArrayList<Libro> libros= new ArrayList<>();
		String librosString="[";

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
			librosString+=nuevo.toString();
			librosString+=",\n";
			
		}
		librosString+="]";
		return librosString;
	}

	
	@GET
	@Path("libros/{isbn}")
        @Produces(MediaType.TEXT_PLAIN)
	public String getLibro(@PathParam ("isbn") String isbn) {
		Libro libro=null;
		try {
			libro=GestorLibro.selectLibroByISBN(isbn);
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(ServicioWeb.class.getName()).log(Level.SEVERE, null, ex);
		}

		
		JSONObject libroJson=new JSONObject();
		libroJson.put("isbn10", libro.getISBN10());
		libroJson.put("isbn13", libro.getISBN13());
		libroJson.put("nombre", libro.getTitulo());
		delete("2");

		return libroJson.toString();
	}

	@POST
        @Consumes(MediaType.TEXT_PLAIN)
	public void postLibro(String isbn) {
		//Obtencion datos de la api de Google
		Client client=ClientBuilder.newClient();
		WebTarget webTarget =client.target("https://www.googleapis.com/books/v1/volumes?q=isbn:"+isbn);
		String libroString= webTarget.request().get(String.class);
		int posicion=libroString.indexOf("[");
		libroString=libroString.substring(posicion,libroString.length());
		JSONArray libro= new JSONArray(libroString);
		
		String titulo= libro.getJSONObject(0).getJSONObject("volumeInfo").getString("title");
		String isbn10="";
		String isbn13="";
		
		
		JSONArray isbns=libro.getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("industryIdentifiers");
		for(int i=0;i<isbns.length();i++){
			if(isbns.getJSONObject(i).get("type").equals("ISBN_10"))
				isbn10=isbns.getJSONObject(i).getString("identifier");
			else{
				if(isbns.getJSONObject(i).get("type").equals("ISBN_13"))
					isbn13=isbns.getJSONObject(i).getString("identifier");
			}
		}

		try {
			GestorLibro.create(new Libro(isbn10,isbn13,titulo));
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(ServicioWeb.class.getName()).log(Level.SEVERE, null, ex);
		}

	
	}
	

	@DELETE
	@Path("{isbn}")
	@Produces
	public void delete(@PathParam("isbn")String isbn){
		try {
			GestorLibro.deleteByISBN(isbn);
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(ServicioWeb.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}


}
