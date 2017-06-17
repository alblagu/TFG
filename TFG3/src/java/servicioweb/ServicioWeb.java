package servicioweb;

import dominio.Ejemplar;
import dominio.Libro;
import dominio.Prestamo;
import dominio.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
 * REST Web Service Servicio Web de la biblioteca que permite realizar busquedas
 * por isbn, por titulo hacer prestamos, añadir nuevos libros y ejemplares al
 * sistema, añadir nuevos usuarios...
 *
 * @author Alberto Laguna Humayor.
 */
@Path("generic")
public class ServicioWeb {

	/**
	 * ********************************************************************
	 ******** LIBROS	******
	*********************************************************************
	 */
	/**
	 * Retrieves representation of an instance of servicioweb.ServicioWeb
	 *
	 * @return an instance of java.lang.String
	 */
	@GET
	@Path("libros")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLibros() throws SQLException, ClassNotFoundException {
		ArrayList<Libro> libros = GestorLibro.selectAll();
		JSONArray libros2 = new JSONArray();

		for (int i = 0; i < libros.size(); i++) {
			JSONObject nuevo = new JSONObject();
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
	 *
	 * @param isbn es el isbn a partir del cual se va a obtener el libro
	 * puede ser de formato 10 numeros o de 13 numeros.
	 * @return un String en formato JSON con la informacion del libro.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema
	 * con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base
	 * de datos.
	 */
	@GET
	@Path("libro/{isbn}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLibro(@PathParam("isbn") String isbn) throws ClassNotFoundException, SQLException {
		boolean internet = false;//almacena si la informacion es sacada de la api de google.	
		Libro libro = GestorLibro.selectLibroByISBN(isbn);
		if (libro == null) { //Obtiene la informacion de la api de Google
			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn);
			String libroString = webTarget.request().get(String.class);
			int posicion = libroString.indexOf("[");
			libroString = libroString.substring(posicion, libroString.length());
			JSONArray libro2 = new JSONArray(libroString);
			String titulo = libro2.getJSONObject(0).getJSONObject("volumeInfo").getString("title");
			String urlFoto = libro2.getJSONObject(0).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail");
			String isbn10 = "";
			String isbn13 = "";

			JSONArray isbns = libro2.getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("industryIdentifiers");
			for (int i = 0; i < isbns.length(); i++) { //Obtiene ISBNS del libro.
				if (isbns.getJSONObject(i).get("type").equals("ISBN_10")) {
					isbn10 = isbns.getJSONObject(i).getString("identifier");
				} else {
					if (isbns.getJSONObject(i).get("type").equals("ISBN_13")) {
						isbn13 = isbns.getJSONObject(i).getString("identifier");
					}
				}
			}
			libro = new Libro(isbn10, isbn13, titulo, urlFoto);
			internet = true;

		}
		JSONObject libroJson = new JSONObject();
		libroJson.put("isbn10", libro.getISBN10());
		libroJson.put("isbn13", libro.getISBN13());
		libroJson.put("titulo", libro.getTitulo());
		libroJson.put("urlfoto", libro.getUrlFoto());
		libroJson.put("internet", internet);

		return libroJson.toString();
	}

	@Path("libros/aleatorios")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLibrosAleatorios(@PathParam("isbn") String isbn) throws ClassNotFoundException, SQLException {
		return getLibros(GestorLibro.selectLibrosAleatorios());
		
	}
	
	
	/**
	 * Realiza una busqueda en el sistema de los libros que tienen la cadena
	 * pasada en su isbn ya sea en el de formato 10 numeros o el de 13.
	 *
	 * @param isbn es la cadena de numeros a partir de la cual se va a
	 * realizar la busqueda.
	 * @return un String en formato array JSON con los libros que tienen esa
	 * cadena de numeros en su ISBNS
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema
	 * con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base
	 * de datos.
	 */
	@GET
	@Path("libros/busqueda/{isbn}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLibrosBusquedaISBN(@PathParam("isbn") String isbn) throws ClassNotFoundException, SQLException {
		ArrayList<Libro> libros = GestorLibro.selectLibrosByISNB(isbn);
		return getLibros(libros);
	}

	/**
	 * Realiza una busqueda en el sistema de los libros que tienen la cadena
	 * pasada en su titulo
	 *
	 * @param titulo es la cadena partir de la cual se va a realizar la
	 * busqueda.
	 * @return un String en formato array JSON con los libros que tienen esa
	 * cadena en su titulo.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema
	 * con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base
	 * de datos.
	 */
	@GET
	@Path("libros/busqueda2/{titulo}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLibrosBusquedaTitutlo(@PathParam("titulo") String titulo) throws ClassNotFoundException, SQLException {
		return getLibros(GestorLibro.selectLibrosByTitulo(titulo));
	}

	/**
	 * Convierte un arrayList de Libro en uno de JSON
	 *
	 * @param libros el arrayList de libros que se va a convertir.
	 * @return en formato String el array de JSON.
	 */
	private String getLibros(ArrayList<Libro> libros) {
		JSONArray libros2 = new JSONArray();

		for (int i = 0; i < libros.size(); i++) {
			JSONObject nuevo = new JSONObject();
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
	/**
	 * ********************************************************************
	 ******** EJEMPLARES ******
	*********************************************************************
	 */
	/**
	 * Obtiene los ejemplares que el sistema tiene asociados al isbn pasado.
	 *
	 * @param isbn es el isbn a partir del cual se va a obtener los
	 * ejemplares.
	 * @return un array JSON en formato String con los ejemplares.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema
	 * con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base
	 * de datos.
	 */
	@GET
	@Path("ejemplares/{isbn}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getEjemplaresByLibro(@PathParam("isbn") String isbn) throws ClassNotFoundException, SQLException {
		ArrayList<Ejemplar> ejemplares = GestorEjemplar.selectEjemplaresByISBN(isbn);
		JSONArray ejemplares2 = new JSONArray();

		for (int i = 0; i < ejemplares.size(); i++) {
			JSONObject ejemplar = new JSONObject();
			ejemplar.put("codigo", ejemplares.get(i).getCodigo());
			ejemplar.put("isbn10", ejemplares.get(i).getLibro().getISBN10());
			ejemplar.put("disponible", ejemplares.get(i).getDisponible());
			ejemplares2.put(ejemplar);
		}
		return ejemplares2.toString();
	}

	@GET
	@Path("ejemplares/ejemplar/{codigo}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getEjemplaresByCodigo(@PathParam("codigo") String codigo) throws ClassNotFoundException, SQLException {

		Ejemplar ejemplar = GestorEjemplar.selectEjemplarByCodigo(codigo);
		if (ejemplar == null) {
			throw new IllegalArgumentException("No se ha encontrado el ejemplar");
		}
		JSONObject ejemplar2 = new JSONObject();
		ejemplar2.put("codigo", ejemplar.getCodigo());
		ejemplar2.put("titulo", ejemplar.getLibro().getTitulo());
		ejemplar2.put("urlfoto", ejemplar.getLibro().getUrlFoto());
		ejemplar2.put("disponible", ejemplar.getDisponible());

		return ejemplar2.toString();
	}

	/**
	 * Añade un nuevo ejemplar al sistema; y en caso de que el libro al que
	 * corresponde no estuviera añadido; tambien lo añade.
	 *
	 * @param json es el libro en formato JSON al que corresponde el
	 * ejemplar.
	 * @param codigo es el codigo del ejemplar.
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema
	 * con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base
	 * de datos.
	 */
	@POST
	@Path("ejemplares/{codigo}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addEjemplarToLibro(String json, @PathParam("codigo") String codigo) throws ClassNotFoundException, SQLException {
		JSONObject libro = new JSONObject(json);
		if (libro.getBoolean("internet")) {
			Libro nuevo = new Libro(libro.getString("isbn10"), libro.getString("isbn13"), libro.getString("titulo"), libro.getString("urlfoto"));
			GestorLibro.create(nuevo);
			GestorEjemplar.create(new Ejemplar(codigo, nuevo, true));
		} else {
			GestorEjemplar.create(new Ejemplar(codigo, new Libro(libro.getString("isbn10"), libro.getString("isbn13"), libro.getString("titulo"), libro.getString("urlfoto")), true));
		}

	}

	@POST
	@Path("ejemplares/{isbn}/{titulo}/{codigo}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void addEjemplarToLibro(@PathParam("isbn") String isbn, @PathParam("titulo") String titulo, @PathParam("codigo") String codigo) throws ClassNotFoundException, SQLException{
		if(null==GestorLibro.selectLibroByISBN(String.valueOf(isbn))){
				
		}	
		{
			//GestorEjemplar.create(new Ejemplar(codigo, new Libro(isbn,titulo),),libre);
		}
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
	/**
	 * ********************************************************************
	 ******** PRESTAMOS	******
	*********************************************************************
	 */
	/**
	 *
	 * @param ejemplar
	 * @return
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema
	 * con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base
	 * de datos.
	 */
	@GET
	@Path("prestamo/{codigo}/{dni}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPrestamosByEjemplar(@PathParam("codigo") String codigo, @PathParam("dni") String dni) throws ClassNotFoundException, SQLException {
		Prestamo prestamo=(GestorPrestamo.selectPrestamosByEjemplar(codigo));
		if(prestamo==null || !prestamo.getUsuario().getDNI().equals(dni)){
			throw new IllegalArgumentException("No hay un prestamo en activo para los datos introducidos");
		}
		else{
			JSONObject nuevo = new JSONObject();
			nuevo.put("titulo", prestamo.getEjemplar().getLibro().getTitulo());
			nuevo.put("urlfoto",prestamo.getEjemplar().getLibro().getUrlFoto());
			nuevo.put("fecha_inicio", prestamo.getFechaIni());
			nuevo.put("fecha_fin", prestamo.getFechaFin());
			return nuevo.toString();
		}
			
	}



	
	
	@GET
	@Path("prestamosUsu/{usuario}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPrestamosByUsuario(@PathParam("usuario") String usuario) throws ClassNotFoundException, SQLException {
		return prestamosToString(GestorPrestamo.selectPrestamosByUsuario(usuario));
	}

	private String prestamosToString(ArrayList<Prestamo> prestamos) {
		JSONArray prestamos2 = new JSONArray();

		for (int i = 0; i < prestamos.size(); i++) {
			JSONObject nuevo = new JSONObject();
			nuevo.put("libro", prestamos.get(i).getEjemplar().getLibro().getTitulo());
			nuevo.put("codigo",prestamos.get(i).getEjemplar().getCodigo());
			nuevo.put("fecha_fin", prestamos.get(i).getFechaFin());
			prestamos2.put(nuevo);
		}

		return prestamos2.toString();
	}
	
	@POST
	@Path("prestamos/{dni}/{codigo}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addNuevoPrestamo(@PathParam("dni") String dni, @PathParam("codigo") String codigo,String fecha) throws ClassNotFoundException, SQLException {
		JSONObject fechaJson=new JSONObject(fecha);
		System.out.println(GestorPrestamo.selectID());
		System.out.println(fechaJson.getInt("dia")+"/"+fechaJson.get("mes")+"/"+fechaJson.get("anio"));
		Date inicio=new Date();
		GregorianCalendar fin=new GregorianCalendar(fechaJson.getInt("anio"), fechaJson.getInt ("mes")-1, fechaJson.getInt("dia"));
		Date fin2=fin.getTime(); 
		System.out.println(inicio);
		System.out.println(fin);
		System.out.println(fin2);
		
		GestorPrestamo.createNuevoPrestamo(new Prestamo(GestorPrestamo.selectID()+1,GestorEjemplar.selectEjemplarByCodigo(codigo),GestorUsuario.selectUsuarioByDNI(dni),true,inicio,fin2));	
		
	}

	/**
	 * ********************************************************************
	 ******** 			USUARIOS			******
	*********************************************************************
	 */
	@GET
	@Path("usuario/{dni}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsuarioByDNI(@PathParam("dni") String dni) throws ClassNotFoundException, SQLException {
		Usuario usuario=GestorUsuario.selectUsuarioByDNI(dni);
		if(usuario==null)throw new IllegalArgumentException("No se ha encontrado dni del usuario");
		JSONObject usuario2 = new JSONObject();
		usuario2.put("dni", usuario.getDNI());
		usuario2.put("password", usuario.getPassword());
		usuario2.put("nombre", usuario.getNombre());
		usuario2.put("administrador", usuario.getAdministrador());
		usuario2.put("telefono", usuario.getTelefono());
		return usuario2.toString();
	}
	/**
	 *
	 * @param json
	 * @throws java.lang.ClassNotFoundException al ocurrir algun problema
	 * con la base de datos.
	 * @throws java.sql.SQLException al ocurrir algun problema con la base
	 * de datos.
	 */
	@POST
	@Path("usuarios")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addNuevoUsuario(String json) throws ClassNotFoundException, SQLException {
		JSONObject usuario = new JSONObject(json);
		if (GestorUsuario.selectUsuarioByDNI(usuario.getString("dni")) != null) {
			throw new IllegalArgumentException("Ya hay un usuario con ese dni");
		}

		GestorUsuario.createUsuario(new Usuario(usuario.getString("dni"), usuario.getString("password"), usuario.getString("nombre")+" "+ usuario.getString("apellidos"),false, usuario.getString("telefono")));
	}

	@GET
	@Path("usuarios/{filtros}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllUsuarios(@PathParam("filtros") String filtros ) throws ClassNotFoundException, SQLException{
		JSONObject usu = new JSONObject(filtros);
		ArrayList<Usuario> usuarios=GestorUsuario.selectAllUsuariosByFiltros(usu.getString("dni"),usu.getString("nombre"));

		JSONArray usuarios2 =new JSONArray();
		for(int i = 0 ; i<usuarios.size();i++){
			JSONObject nuevo = new JSONObject();
			nuevo.put("dni", usuarios.get(i).getDNI());
			nuevo.put("password", usuarios.get(i).getPassword());
			nuevo.put("nombre", usuarios.get(i).getNombre());
			nuevo.put("administrador", usuarios.get(i).getAdministrador());
			nuevo.put("telefono", usuarios.get(i).getTelefono());
			usuarios2.put(nuevo);
		}
		return usuarios2.toString();
	}

	@DELETE
	@Path("usuario/{dni}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void deleteUsuarioByDNI(@PathParam("dni") String dni) throws ClassNotFoundException, SQLException {
		GestorUsuario.deleteUsuarioByDNI(dni);
	}
	
}
