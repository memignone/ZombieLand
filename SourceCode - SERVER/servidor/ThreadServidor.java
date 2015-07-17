package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import logicaDelJuego.Partida;

public class ThreadServidor extends Thread{
	private Socket skCli = null;
	private DataInputStream entrada = null;
	private DataOutputStream salida = null;
	/**
	 * Datos del cliente
	 */
	//private String partidaActual = null;
	private String usuario = null;
	private String nick = null;
	private int puntajeTotal;
	private int cantPartidasGanadas;
	private Manejador manejador;
	private boolean fueZombie = false;

	public ThreadServidor(Socket cli, Manejador manejador) {
		this.manejador=manejador;
		skCli = cli;
	}
	
	public void run() {
		
		System.out.println("Cliente agregado");
		
		try {
			entrada = new DataInputStream(skCli.getInputStream());
			salida = new DataOutputStream(skCli.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String linea;
		String[] datos;
		String key;
		
		
		/**
		 * cuando este implementado lo del usuario hay que modificar lo que le manda
		 */
		while (true){
			try {
				linea = entrada.readUTF();
				datos = linea.split(";");
				/**
				 * 
				 * Swich que llama al metodo correspondiente 
				 * para cada mensaje que recive del cliente 
				 * 
				 */
				key = datos[0];
				switch (key) {
				case "AUTENTICAR":
					autenticar(datos[1], datos[2]);
					break;
				case "CAMBIO_NICK":
					setearNuevoNick(datos[1]);
					break;
				case "SELECCIONPARTIDA":
					unirseAPartida(datos[1]);
					break;
				case "PIDO_LISTADO_JUEGOS":
					pedirPartidas();
					break;
				case "MOVIMIENTO":
					buscarJugadorEnPartida(datos[1]);
					break;
				case "CAMBIO_PASS":
					cambiarContrasenia(datos);
					break;
				case "LANZAR_CAMBIO_PASS":
					buscarUsuario(datos[1]);
					break;
				case "CREAR_NUEVO_JUGADOR":
					agregarJugador(datos);
					break;
				case "SALIR_PARTIDA":
					salirPartida();
					break;
				case "DESCONECTAME":
					manejador.getServidorAbierto().desconectarJugadorEnJuego(this);
					break;
				default:
					break;
				}
				if(linea.length() > 1){
					System.out.println(linea);
				}
			} catch (IOException e) {
				System.out.println("El cliente termino la conexion");
				if(this.usuario!=null){
					manejador.eliminarClienteDeLaListaDeLaInterfazista(this.usuarioMasNickToString());
					manejador.getServidorAbierto().eliminarClienteDeLaListaDeLaPartidas(this);
					manejador.getServidor().eliminarDeListaCliente(this);
				}
				break;
			}
		}
	}

	/**
	 * saca a un cliente que estaba esperando e la lista de un juego sin empezar.
	 */
	private void salirPartida() {
		manejador.getServidorAbierto().elminarCienteDePartidaEnEspera(this);
	}

	/**
	 * 
	 * @param string contraseña a cambiar
	 */
	private void cambiarContrasenia(String[] datos) {
		String []datosJugador = manejador.getServidor().getDb().getJugadorDB(datos[1]);
		if(datosJugador[1].equals(datos[2]) && datosJugador[3].equals(datos[4])) {
			this.manejador.getServidor().getDb().cambiarPassword(datos[1], datos[3]);
			enviarMensaje("PASS_CAMBIADA");
		}
		else
			enviarMensaje("ERROR_CAMBIO_PASS");
	}

	/**
	 * buscamos al jugador en la partida a si le manda los movimientos a esa partida
	 * 
	 * @param String movimiento que realizo el cliente
	 */
	 private void buscarJugadorEnPartida(String movimiento) {
		manejador.getServidorAbierto().mandarMovimientoALaPartida(movimiento, this);	
	}

	/**
	 * Revisa si no se llego a la cantidad maxima de jugadores y
	 * le envia la confirmacion de que se unuio a la partida
	 * 
	 
	 * @param String nombre de la partida
	 */
	private void unirseAPartida(String nombrePartida) {
		manejador.getServidorAbierto().unirseAPartida(nombrePartida, this);
	}


	/**
	 * 
	 * Setea el nuevo nick con el que le pasa el cliente, se cupone que no podria tener ; o rompe el 
	 * protocolo de comunicacion, y no esta validado. Deberia ya estar validado en el cliente.
	 * 
	 * @param nuevoNick String que contiene el nuevo nick 
	 * 
	 * @author Fernando
	 */
	private void setearNuevoNick(String nuevoNick) {
		manejador.eliminarClienteDeLaListaDeLaInterfazista(this.usuarioMasNickToString());
		this.nick = nuevoNick;		
		manejador.agregarClienteALaListaDeLaInterfazista(this.usuarioMasNickToString());
	}

	/**
	 * Eventualmente deberia llamar a una base de datos, pero ahora hace algo super negro XD
	 * Va a llamar a una clase de envoltura que es enviarMensaje y le envia lo que correspode ya formateado
	 * 
	 * @param usuario
	 * @param contrase�a
	 * 
	 * @author Fernando
	 */
	private void autenticar(String usuario, String contrasenia) {
		if(this.manejador.getServidor().getDb().autenticarJugador(usuario, contrasenia)){
			enviarMensaje("SIAUTENTICA");
			this.usuario = usuario;
			this.nick = usuario;
			manejador.agregarClienteALaListaDeLaInterfazista(this.usuarioMasNickToString());
			manejador.getServidor().getClientesActivos().add(this);
		}else{
			enviarMensaje("NOAUTENTICA");
		}
	}
	

	/**
	 * metodo de envoltura, asi solamente se mandan cosas desde aca
	 * 
	 * @param linea String formateado para mandar
	 */
	public void enviarMensaje(String linea) {
		try {
			salida.writeUTF(linea);
		} catch (IOException e) {
			System.out.println("El cliente termino la conexion: escritura fallo");
		}		
	}

	
	
	/**
	 * Se manda a traves de el metodo de envoltura enviarMensaje.
	 * Mandamos los datos de la partida a cada cliente.
	 * Concatena al String de entrada linea "INFO_NUEVA_PARTIDA;"
	 * Y lo manda por el socket al cliente.
	 * @param linea es un string del formato "cantMin;cantMax;NombrePartida"
	 * @author Fernando
	 */
	public void agregarUnapartidaALaLista(String linea){
		enviarMensaje("INFO_NUEVA_PARTIDA;" + linea);
	}
	
	/**
	 * Pedimos partidas a la interfaz ServidorAbierto para despues mandarselas al cliente
	 * 
	 * @author Fernando
	 */
	public void pedirPartidas(){
		String[] partidas = manejador.getServidorAbierto().obtenerPartidas();	
		String linea = "LISTADO_JUEGOS";
		if(partidas != null)
			for (String string : partidas) {
			linea += string;
		}
		enviarMensaje(linea);
		enviarRanking();
	}
	
	/**
	 * hace la consulta de los usurios en la base de datos, la ordena y le envia los primeros 20 ordenados
	 */
	public void enviarRanking() {
		LinkedList <String[]> datosJugador;
		datosJugador = manejador.getServidor().getDb().listarDatosJugadores();
		burbujeo(datosJugador);
		String linea = "ENVIO_RANKING";
		for(int i = 0; i < datosJugador.size() && i < 20; i++){
			for(int j = 0; j < 4; j++)
				linea += ";"+ datosJugador.get(i)[j];
			if(Integer.parseInt(datosJugador.get(i)[2]) != 0)
				linea += ";" + Double.toString(Double.parseDouble(datosJugador.get(i)[1])/Double.parseDouble(datosJugador.get(i)[2]));
			else
				linea += ";0";
		}
		for (String[] strings : datosJugador) {
			if(this.usuario.equals(strings[0])){
				linea += ";" + Integer.toString(datosJugador.indexOf(strings)+1);
				for(int j = 0; j < 4; j++)
					linea += ";"+ strings[j];
				if(Integer.parseInt(strings[2]) != 0)
					linea += ";" + Double.toString(Double.parseDouble(strings[1])/Double.parseDouble(strings[2]));
				else
					linea += ";0";
			}
		}
		this.enviarMensaje(linea);
	}
	
	/**
	 * metodo de ordenamoento de los rankins de jugadores
	 */
	private void burbujeo(LinkedList <String[]> datosJugador) {
		for(int i = datosJugador.size()-1; i >0; i--){
			for(int j = i-1; j >= 0; j--){
				if(Integer.parseInt(datosJugador.get(i)[2])!= 0 && Integer.parseInt(datosJugador.get(j)[2]) != 0){
					if((Double.parseDouble(datosJugador.get(i)[1]) / Double.parseDouble(datosJugador.get(i)[2])) 
							> (Double.parseDouble(datosJugador.get(j)[1]) / Double.parseDouble(datosJugador.get(j)[2]))){
						String aux[] = datosJugador.get(j);
						datosJugador.remove(j);
						datosJugador.add(i,aux);
					}
				}else{
					if(Integer.parseInt(datosJugador.get(i)[2]) != 0 && Integer.parseInt(datosJugador.get(j)[2]) == 0){
						String aux[] = datosJugador.get(j);
						datosJugador.remove(j);
						datosJugador.add(i,aux);
					}	
				}
			}
		}
	}

	/**
	 * Se le pasan las propiedades del juego (la lista de obstaculos) "SET_PROPIEDADES_JUEGO;NOMBRE_PARTIDA
	 * ;POSX;POSY;........
	 * 
	 * @param partida que el cliente va a iniciar un juego
	 */
	public void enviarPropiedadesDelJuego(Partida partida){
		enviarMensaje("SET_PROPIEDADES_JUEGO;" + "falta completar");
	}
	
	/**
	 * Se le pasan al cliente KEY , NOMBRE DEL JUGADOR, PUNTAJE DE LA PARTIDA, SI ES ZOMBIE O NO, POS X, POSY
	 * no agregar ; al funal de la clave, pq los clientes ya la concatenan
	 * @param partida que el cliente inicio juego
	 */
	public void enviarListaJugadores(Partida partida){
		enviarMensaje("SET_LISTA_JUGADORES" + "falta completar" );
	}
	
	/**
	 * envia el mwensaje de que se acepto el cliente a la partida
	 * 
	 */
	public void enviarConfirmacionDePartida(int maximo) {
		enviarMensaje("CONFIRMAR_PARTIDA;" + Integer.toString(maximo));
	}
	
	
	/**
	 * devuelde en string del usiario y nick del cliente
	 * 
	 * @return String "usr:" + this.usuario + " nick:" + this.nick
	 */
	public String usuarioMasNickToString(){
		return "usr:" + this.usuario + " nick:" + this.nick;
	}
	
	/**
	 * cada vez que hay un cambio en alguna partida se le manda la lista de partidas
	 */
	public void actualizarListas(Partida partida) {	
		enviarMensaje("ACTUALIZACION_DE_LISTA_PARTIDAS"+";"+ 
					   partida.getNombrePartida()+";"+
					   partida.getEstado()+";"+
					   Integer.toString(partida.getCantidadMinima())+";"+
					   Integer.toString(partida.getCantConectados()));
	}
	/**
	 * Agrega al usuario nuevo a la base de datos y envia el mensaje JUGADOR_NUEVO_CREADO si se creo bien.
	 * Si el jugador esta duplicado envia JUGADOR_DUPLICADO
	 * @param datos
	 */
	private void agregarJugador(String[] datos) {
		try {
			if(manejador.getServidor().getDb().crearJugadorDB(datos[1], datos[2], datos[3], datos[4]))
				enviarMensaje("JUGADOR_NUEVO_CREADO");
			else
				enviarMensaje("JUGADOR_DUPLICADO");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void buscarUsuario(String usr) {
		String datosUsuario[] = manejador.getServidor().getDb().getJugadorDB(usr);
		if(datosUsuario.length != 0)
			enviarMensaje("DATOS_USR;"+ datosUsuario[2]);
	}
	
	
	///////////// GETERS Y SETERS
	public String getNick(){
		return this.nick;
	}

	public String getUsuario() {
		return usuario;
	}
	public boolean getFueZombie(){
		return this.fueZombie;
	}

	public void setFueZombie(boolean fueZombie) {
		this.fueZombie = fueZombie;
	}

	public int getPuntajeTotal() {
		return puntajeTotal;
	}

	public void setPuntajeTotal(int puntajeTotal) {
		this.puntajeTotal = puntajeTotal;
	}

	public int getCantPartidasGanadas() {
		return cantPartidasGanadas;
	}

	public void setCantPartidasGanadas(int cantPartidasGanadas) {
		this.cantPartidasGanadas = cantPartidasGanadas;
	}
	
	
}
