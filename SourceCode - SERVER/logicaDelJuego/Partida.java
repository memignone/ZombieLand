package logicaDelJuego;

import java.util.ArrayList;
import servidor.Manejador;
import servidor.ThreadServidor;
import ventanasServidor.EstadisticasPartida;

/**
 * Es la encargada de manejar la ventana de estadisticas, y de controlar que clientes estan jugando en cada partida.
 * @author fernando
 */
public class Partida {
	/**
	 * macro que define el tama�o en pixeles de las imagenes
	 */
	private Manejador manejador;
	final int PIXELES = 40;
	
	private ArrayList<ThreadServidor> clientesActivos = new ArrayList<ThreadServidor>();
	private ArrayList<String> mensajesAProcesar = new ArrayList<String>();
	private int cantidadMinima;
	private int cantidadMaxima;
	private String nombrePartida;
	private String estado;
	private int cantConectados;
	private LogicaMovimientos logicaMovimientos;
	private ThreadSegundosDelJuego threadSegundosDelJuego;
	private int numeroDeRonda;
	private EstadisticasPartida ventanaDeEstadisticas;
	
	/**
	 * @return LogicaMovimientos
	 */
	public LogicaMovimientos getLogicaMovimientos(){
		return logicaMovimientos;
	}
	
	/**
	 * Devuelve el estado de la partida 
	 * @return String con el estado 
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * @param estado String
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return int cantConectados
	 */
	public int getCantConectados() {
		return cantConectados;
	}
	/**
	 * Inicializa la partida, y le crea la referencia a la interfaz EstadisticasPartida
	 * @param cantidadMinima
	 * @param cantidadMaxima
	 * @param nombrePartida
	 * @param manejador
	 */
	public Partida(int cantidadMinima, int cantidadMaxima, String nombrePartida, Manejador manejador){
		this.manejador = manejador;
		this.cantidadMinima = cantidadMinima;
		this.cantidadMaxima = cantidadMaxima;
		this.nombrePartida = nombrePartida;
		this.estado = "En espera";
		this.cantConectados = 0;
		this.numeroDeRonda = 0;
		this.ventanaDeEstadisticas = new EstadisticasPartida();
		this.ventanaDeEstadisticas.setTitle(nombrePartida);
		this.ventanaDeEstadisticas.escribirLineaDeDatos("Estado de la partida: " + 
													estado + ".");
		this.threadSegundosDelJuego = new ThreadSegundosDelJuego(this);
	}
	
	public String getNombrePartida(){
		return this.nombrePartida;
	}

	public int getCantidadMinima() {
		return cantidadMinima;
	}

	public int getCantidadMaxima() {
		return cantidadMaxima;
	}

	
	/**
	 * prepara la partida para que acepte al nuevo cliente y dispara el mensaje de confirmacion de la partida
	 * 
	 * @param cliente
	 */
	public void aceptarCliente(ThreadServidor cliente) {
		cantConectados++;
		clientesActivos.add(cliente);
		cliente.setPuntajeTotal(0);
		this.ventanaDeEstadisticas.escribirLineaDeDatos("Cliente " + cliente.getNick() + " se agrego a la partida.");
		cliente.enviarConfirmacionDePartida(this.cantidadMaxima);
		manejador.getServidor().actualizarListaDePartidas(this);
	}
	
	/**
	 * cuando se llega a la cantidad minima de clientes deberia lanzar la partida
	 * 
	 */
	public void empezarPartida() {
		this.cambiarEstado("Jugando");		
		this.logicaMovimientos = new LogicaMovimientos(clientesActivos);
		this.logicaMovimientos.enviarNuevasPosiciones(0);
		for (ThreadServidor cliente : clientesActivos){
			cliente.enviarMensaje("ARRANCAR_PARTIDA");
		}
		this.enviarPuntajes(0);
		arrancarRondaDeJuego();
	}
	
	/**
	 * manda mensaje de que arranca la ronda la primera ronda de la partida
	 */
	private void arrancarRondaDeJuego() {
		/*for (ThreadServidor cliente : clientesActivos) {
			cliente.setPuntajeTotal(0);
		}*/
		if(!logicaMovimientos.getFinRonda()){
			for (Jugador jugador : logicaMovimientos.getListaJugadores()){
				jugador.getCliente().enviarMensaje("ARRANCAR_NUEVA_RONDA");
			}
		}
		this.numeroDeRonda++;
		if(numeroDeRonda == 1)
				this.threadSegundosDelJuego.start();

	}
	
	/**
	 * este metodo se encarga de verificar si hay que jugar otra ronda y de lanzar esa ronda
	 */
	public void nuevaRonda(){
		boolean condicionDeNuevaRonda = false;
		for (ThreadServidor cliente : clientesActivos) {
			if(!cliente.getFueZombie() && !condicionDeNuevaRonda){
				condicionDeNuevaRonda = true;
			}
		}
		if(condicionDeNuevaRonda){
			logicaMovimientos = new LogicaMovimientos(clientesActivos);
			arrancarRondaDeJuego();
		}else{
			mandarFinPartida();
		}
		
	}
	
	/**
	 * mandael mensaje a todos del fin de partida y resetea los parametros de partidas
	 * y el FueZombie de los clientes
	 */
	private void mandarFinPartida(){
		for (ThreadServidor cliente : clientesActivos) {
			manejador.getServidor().getDb().actualizarPuntajeJugador(cliente.getUsuario(), cliente.getPuntajeTotal());
			cliente.setFueZombie(false);
			cliente.enviarMensaje("FIN_PARTIDA");
		}
		int aux = 0;
		ThreadServidor caux = null;
		for (Jugador jugador : logicaMovimientos.getListaJugadores()) {
			if(jugador.getPuntajeActual()>aux){
				aux = jugador.getCliente().getPuntajeTotal();
				caux = jugador.getCliente();
			}
		}
		caux.setCantPartidasGanadas(caux.getCantPartidasGanadas()+1);
		ventanaDeEstadisticas.escribirLineaDeDatos("Fin de partida.");
		ventanaDeEstadisticas.escribirLineaDeDatos(caux.getNick() + " fue el ganador con: " + Integer.toString(aux) + " puntos.");
		manejador.getServidor().getDb().incrementarPartidasGanadas(caux.getUsuario());
		this.cantConectados = 0;
		this.cambiarEstado("En espera");
		for (ThreadServidor cliente : clientesActivos){
			cliente.setPuntajeTotal(0);
			cliente.enviarRanking();
		}
		this.clientesActivos.clear();
		this.threadSegundosDelJuego.kill();
		this.threadSegundosDelJuego = new ThreadSegundosDelJuego(this);
		this.logicaMovimientos = null;
		this.numeroDeRonda = 0;
		this.mensajesAProcesar.clear();
		ventanaDeEstadisticas.escribirLineaDeDatos("Cantidad de conectados: " + 
													Integer.toString(cantConectados) + ".");
	}

	/**
	 * 
	 * @return Lista de clientes de esa partida
	 */
	public ArrayList<ThreadServidor> getClientesActivos() {
		return clientesActivos;
	}

	/**
	 * 
	 * @param estado String estado en la que va a quedar la partida
	 */
	private void cambiarEstado(String estado) {
		this.estado = estado;	
		ventanaDeEstadisticas.escribirLineaDeDatos("Actualizacion del estado de la partida: " + 
													estado + ".");
		manejador.getServidor().actualizarListaDePartidas(this);
	}
	
	/**
	 * cuando se elimina un cliente se descuenta la canidad de conectados
	 */
	public void descontarCliente(ThreadServidor cliente) {
		if(cantConectados>0)
			this.cantConectados--;
		ventanaDeEstadisticas.escribirLineaDeDatos(cliente.getNick() + " se desconecto de la partida.");
		if(this.estado.equals("En espera")){
			ventanaDeEstadisticas.escribirLineaDeDatos("Cantidad de conectados: " +
														Integer.toString(this.cantConectados) + ".");
			if(this.cantConectados < this.cantidadMinima)
				ventanaDeEstadisticas.escribirLineaDeDatos("Faltan " + Integer.toString(this.cantidadMinima - this.cantConectados) + "jugadores.");
		}
		if(cantConectados==0){
			this.cambiarEstado("En espera");
			if(this.threadSegundosDelJuego != null){
				threadSegundosDelJuego.kill();
				threadSegundosDelJuego = new ThreadSegundosDelJuego(this);
			}
			logicaMovimientos = null;
			if(this.mensajesAProcesar != null){
				mensajesAProcesar.clear();
			}
			this.numeroDeRonda = 0;
			ventanaDeEstadisticas.escribirLineaDeDatos("se detuvo la partidapor falta de jugadores");
		}	
		manejador.getServidor().actualizarListaDePartidas(this);
	}
	
	/**
	 * acumula los movimientos de podoslos clientes para procesarlos
	 */
	public void recibirMovimiento(String usuario, String movimiento) {
		this.mensajesAProcesar.add(usuario + ";" + movimiento );
		System.out.println(usuario + ";" + movimiento);
		if(mensajesAProcesar.size() == clientesActivos.size()){
			for (String stringMovimiento : mensajesAProcesar) {
				ventanaDeEstadisticas.escribirLineaDeDatos(stringMovimiento);
			}
			logicaMovimientos.procesarMovimientos(mensajesAProcesar);
			logicaMovimientos.enviarNuevasPosiciones(1);
			this.mensajesAProcesar.clear();
		}
		
		for(Jugador jugador : logicaMovimientos.getListaJugadores()){
			String linea = "";
			linea += ("Usuario: " + jugador.getUsuario() + " ");
			if(jugador.getEsZombie())
				linea += "Es zombie ";
			else 
				linea += "Es humano ";
			linea += ("y esta en el casillero: " + "x: " + jugador.getPosX()+ " " + "y:  " +jugador.getPosY() );
			ventanaDeEstadisticas.escribirLineaDeDatos(linea);
		}
	}
	
	/**
	 * @return ventanaDeEstadisticas referencia a la ventana de estadisticas propia de esta partida
	 */
	public EstadisticasPartida getVentanaEstadisticas(){
		return this.ventanaDeEstadisticas;
	}
	
	/**
	 * envia los puntajes al finalizar la ronda
	 */
	public void enviarPuntajes(int caso) {
		if(caso == 0){
			for (ThreadServidor cliente : clientesActivos){
				String linea = "PUNTAJE_JUGADORES";
				for(int i = 0; i < clientesActivos.size(); i++){
					linea += ";" + clientesActivos.get(i).getUsuario()+ ";" + clientesActivos.get(i).getNick() + ";" + Integer.toString(0);
				}
				cliente.enviarMensaje(linea);
			}
		}else{
			for (Jugador jugador : logicaMovimientos.getListaJugadores()) {
				jugador.getCliente().enviarMensaje(logicaMovimientos.formatearPuntaje());
			}
			
			for (ThreadServidor cliente : clientesActivos) {
				for (Jugador jugador : logicaMovimientos.getListaJugadores())
					if(cliente.getUsuario().equals(jugador.getUsuario()))
						cliente.setPuntajeTotal(cliente.getPuntajeTotal()+jugador.getPuntajeActual());
			}
		}
	}

	public void descontarClienteDePartidaEnCurso(ThreadServidor cliente) {
		manejador.getServidor().getDb().actualizarPuntajeJugador(cliente.getUsuario(), cliente.getPuntajeTotal());
		cliente.setFueZombie(false);
		cliente.enviarMensaje("FIN_PARTIDA");
		cliente.setCantPartidasGanadas(cliente.getCantPartidasGanadas()+1);
		ventanaDeEstadisticas.escribirLineaDeDatos("Fin de partida.");
		ventanaDeEstadisticas.escribirLineaDeDatos(cliente.getNick() + " fue el ganador con: " + Integer.toString(cliente.getPuntajeTotal()) + " puntos.");
		manejador.getServidor().getDb().incrementarPartidasGanadas(cliente.getUsuario());
		cliente.setPuntajeTotal(0);
		cliente.enviarRanking();
		this.clientesActivos.clear();
		this.threadSegundosDelJuego.kill();
		this.threadSegundosDelJuego = new ThreadSegundosDelJuego(this);
		this.logicaMovimientos = null;
		this.numeroDeRonda = 0;
		this.mensajesAProcesar.clear();
		ventanaDeEstadisticas.escribirLineaDeDatos("Cantidad de conectados: " + 
													Integer.toString(cantConectados) + ".");
		this.cantConectados = 0;
		this.cambiarEstado("En espera");
	}
}
