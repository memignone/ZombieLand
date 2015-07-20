package ventanasCliente;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import jugador.*;
import propiedadesDelJuego.Partida;
import ventanasServidor.WindowCloseListener;
import cliente.*;
import dibujable.ObjetoDibujable;

public class Principal {
	private Cliente elCliente; 
	private Login loginVentana;
	private NuevoJugador ventanaNuevoJugador;
	private ListaJuegos  seleccionVentana;
	private VentanaDeEspera ventanaEspera;
	private Juego juegoVentana;
	private Ranking ranking = new Ranking();
	private Jugador yoJugador = new Jugador(); 
	private boolean seguroPartida = false;
	private ArrayList<Partida>  listaJuegos = new ArrayList<Partida>();
	private ArrayList<Jugador> listaJugadores = new ArrayList<Jugador>();
	private ArrayList<ObjetoDibujable> listaDeDibujables = new ArrayList<ObjetoDibujable>();
	private VentanaCambiarPassword ventanaCambioPass;
	private int miPosicionRanking;
	private boolean partidaConfirmada;

	//CONSTRUCTOR------------------------------------
	public Principal(Login log) {
		this.loginVentana = log;
	}

	//GETTERS & SETTERS-------------------------------
	public Cliente getCliente() {
		return this.elCliente;
	}

	public Jugador getYoJugador(){
		return this.yoJugador;
	}
	
	public void setNuevoJugador(NuevoJugador ventana) {
		this.ventanaNuevoJugador = ventana;
	}
	
	public void setVentanaCambioPassword(VentanaCambiarPassword ventana) {
		this.ventanaCambioPass = ventana;
	}
	
	public void setYoJugadorNombre(Cliente cliente) {
		this.yoJugador.setUsuario(cliente.getUsuario()); 
	}

	public Login getLogin() {
		return this.loginVentana;
	}
	
	public void setLogin(Login logIn) {
		this.loginVentana = logIn;	
	}
	
	public void setCliente(Cliente cliente) {
		this.elCliente = cliente;
	}
	
	public void setVentanaEspera(VentanaDeEspera ventana) {
		this.ventanaEspera = ventana;
	}
	
	public Juego getJuego(){
		return this.juegoVentana;
	}
	
	public int getMiPosicionRanking() {
		return miPosicionRanking;
	}

	public void setMiPosicionRanking(int miPosicionRanking) {
		this.miPosicionRanking = miPosicionRanking;
	}
	
	public VentanaDeEspera getVentanaEspera() {
		return ventanaEspera;
	}

	//OTROS METODOS---------------------------------------
	/**
	 * FUNCION DE LA CLASE QUE INTERPRETA LOS MSJ QUE LE ENVIAN
	 * LAS OTRAS VENTANAS
	 * 
	 * Formato del los mensajes recibidos por Principal
	 * REMITENTE_DESTINO_OPERACION
	 * 
	 */
	public void recibirMsj(String  msj) {
		String msjAEnviar = new String();
		
		switch (msj) {
		case "LANZAR_CAMBIO_PASS":
			this.ventanaCambioPass = new VentanaCambiarPassword(this);
			this.ventanaCambioPass.addWindowListener(new WindowCloseListener() {
				@Override
				public void windowClosed(WindowEvent e) {
					seleccionVentana.setEnabledGeneral(true);
				}
			});
			seleccionVentana.setEnabledGeneral(false);
			elCliente.enviarMensaje("LANZAR_CAMBIO_PASS;"+ this.elCliente.getUsuario());
			break;
		case "ERROR_CAMBIO_PASS":
			this.ventanaCambioPass.mostrarMsj("Clave o respuesta incorrecta");
			break;
		case "PASS_CAMBIADA":
			this.ventanaCambioPass.mostrarMsj("Clave modificada");
			break;
		case "SERVER_DOWN":
			loginVentana.setEnabledGeneral(true);
			loginVentana.mostrarMensaje("Servidor no encontrado");
			break;
		case "CREAR_NUEVO_JUGADOR":
			msjAEnviar = msj+";"+ventanaNuevoJugador.getNombreDeUsuario()+";"+ventanaNuevoJugador.getPassword()+";"+ventanaNuevoJugador.getPregunta()+";"+ventanaNuevoJugador.getRespuesta();
			this.elCliente.enviarMensaje(msjAEnviar);
			break;
		case "JUGADOR_NUEVO_CREADO":
			this.ventanaNuevoJugador.mostrarMensaje("Usuario creado con èxito. Ahora logueate!");;
			break;
		case "JUGADOR_DUPLICADO":
			this.ventanaNuevoJugador.mostrarMensaje("Ese nombre de usuario ya existe. Elegì otro, Master!");
			break;
		case "LOGIN_PRINCIPAL_SELECCION-APERTURA":
			msjAEnviar = "PIDO_LISTADO_JUEGOS";
			this.elCliente.enviarMensaje(msjAEnviar);
			break;
		case "LISTAJUEGOS_PRINCIPAL_CAMBIO-NICK":
			this.elCliente.enviarMensaje("CAMBIO_NICK;"+this.getYoJugador().getNick());
			break;
		case "LISTAJUEGOS_PRINCIPAL_CAMBIO-PASS":
			this.elCliente.enviarMensaje("CAMBIO_PASS;"+this.elCliente.getPassword());
			break;
		case "SELECCION_PRINCIPAL_UNIRSE":
			msjAEnviar = "SELECCIONPARTIDA;"+this.seleccionVentana.getPartidaSeleccionada();
			this.elCliente.enviarMensaje(msjAEnviar);
			break;
		case "JUEGO_PRINCIPAL_ME_MUEVO":
			msjAEnviar = "MOVIMIENTO;"+ this.juegoVentana.getMovimiento();
			this.elCliente.enviarMensaje(msjAEnviar);
			break;
		default:
			break;
		}
	}

	/**
	 * PROCESO LOS MSJ ENTRANTE PARA LAS VENTANA DE SELECCION DE JUEGO
	 * @param msj
	 * @param datos
	 */
	public void reciboDatosVentanaSeleccion(String msj,String []datos) {
		int j;

		switch (msj) {
		case "DATOS_USR":
			ventanaCambioPass.setLabels(datos[1]);
			ventanaCambioPass.setVisible(true);
			break;
		case "ENVIO_RANKING":
			//SE ENVIA EL RANKING CON HASTA 21 JUGADORES ORDENADO DEL PRIMERO AL ULTIMO
			 //EL ULTIMO ES EL JUGADOR ACTUAL EL CUAL SE MUESTRA ESTE O NO ENTRE LOS 20 PRIMEROS 
			 //EL FORMATO DEL ES: USR, PUNTAJE TOTAL, PARTIDAS_JUGADAS, PARTIDAS GANADAS, PROM PPP
			ArrayList<Jugador> listaRanking = new ArrayList<Jugador>();
			int index;
			for (index = 1; index < datos.length - 6; index += 5) {
					listaRanking.add(new Jugador(datos[index], Integer.parseInt(datos[index+1]), Integer.parseInt(datos[index+2]), Integer.parseInt(datos[index+3]), Double.parseDouble(datos[index+4])));
			}
			this.setMiPosicionRanking(Integer.parseInt(datos[index]));
			if(this.getMiPosicionRanking() > 20) {
				index++;
				listaRanking.add(new Jugador(datos[index], Integer.parseInt(datos[index+1]), Integer.parseInt(datos[index+2]), Integer.parseInt(datos[index+3]), Double.parseDouble(datos[index+4])));
			}
			this.ranking.setListaJugadores(listaRanking);
			this.seleccionVentana = new ListaJuegos(listaJuegos,this.ranking,this);
			loginVentana.setVisible(false);
			seleccionVentana.setVisible(true);
			break;
		case "LISTADO_JUEGOS":
			j = 0;
			System.out.println("Obteniendo lista de partidas");
			for (int i = 1; i < datos.length - 1; i += 4) {
				//KEY,  NOMBRE DEL JUEGO,  ESTADO DEL JUEGO, JUGADORES REQUERIDOS , JUGADORES CONECTADOS
				this.listaJuegos.add((j), new Partida(datos[i], datos[i+1], Integer.parseInt(datos[i+2]), Integer.parseInt(datos[i+3])));
				j++;
			}
			break;
		case "INFO_NUEVA_PARTIDA":
			this.listaJuegos.add(new Partida(datos[1],datos[2], Integer.parseInt(datos[3]), Integer.parseInt(datos[4])));
			this.seleccionVentana.setListaPartidas();
			break;
		case "ACTUALIZACION_DE_LISTA_PARTIDAS":
			this.seleccionVentana.actualizarPartida(datos[1], datos[2], Integer.parseInt(datos[3]), Integer.parseInt(datos[4]));
			//Se verifica si el cliente es el ùnico conectado a la partida.
			if(datos[1].equals(this.seleccionVentana.getPartidaSeleccionada()) && datos[2].equals("Jugando") && Integer.parseInt(datos[4]) == 1)
				elCliente.enviarMensaje("DESCONECTAME");
			if(ventanaEspera == null && this.partidaConfirmada) {
				ventanaEspera = new VentanaDeEspera(this, this.seleccionVentana.getPartidaSeleccionada());
				ventanaEspera.setVisible(true);
				ventanaEspera.addWindowListener(new WindowCloseListener() {
					@Override
					public void windowClosed(WindowEvent arg0) {
						if(getJuego() == null) {
							seleccionVentana.setEnabledGeneral(true);
							ventanaEspera = null;
							partidaConfirmada = false;
							elCliente.enviarMensaje("SALIR_PARTIDA");
						}
					}
				});
			}
			this.seleccionVentana.setListaPartidas();
			break;
		case "ERROR_AL_UNIRSE":
			this.seleccionVentana.setEnabledGeneral(true);
			this.seleccionVentana.mostrarInformacion("No pudiste unirte a la partida");
			break;
		case "CONFIRMAR_PARTIDA":
			this.partidaConfirmada = true;
			this.seleccionVentana.setPartidaMaxJugadores(Integer.parseInt(datos[1]));
			break;
		default:
			break;
		}
	}

	/**
	 * Recibe todo los mensajes referidos al  juego en desarrollo
	 * @param msj
	 * @param datos
	 */
	public void ventanaJuego(String msj, String datos[]) {
		switch (msj) {	
		case "PUNTAJE_JUGADORES":
			//KEY ,USUARIO, NICK DEL JUGADOR, PUNTAJE DE LA PARTIDA
			if(listaJugadores.size() == 0) {
				for (int i = 1; i < datos.length - 1; i+=3)
					this.listaJugadores.add(new Jugador(datos[i],datos[i+1], Integer.parseInt(datos[i+2])));
				this.juegoVentana.setListaJugadores(listaJugadores);
				this.juegoVentana.seteaPuntaje();
			}
			else {
				for (int i = 1; i < datos.length - 1; i+=3) {
					for (Jugador j : listaJugadores) {
						if(j.getUsuario().equals(datos[i])) {
							j.setPuntaje(j.getPuntaje()+Integer.parseInt(datos[i+2]));
						}
					}
				}
				this.juegoVentana.seteaPuntaje();
			}
			break;
		case "LISTA_OBJETOS_PARA_DIBUJAR":
			this.listaDeDibujables.clear();
				// KEY MI ESTADO;POS X, POS Y, OBJ,POS X,POS Y...
				for (int i = 1; i < datos.length - 1; i+=3) //cambiar el incremento de i de acuerdo al msj
					this.listaDeDibujables.add(new ObjetoDibujable(datos[i].charAt(0), Integer.parseInt(datos[i+1]), Integer.parseInt(datos[i+2])));
				break;
		case "ACTUALIZAR_MOVIMIENTOS":
			this.listaDeDibujables.clear();
			// KEY MI ESTADO;POS X, POS Y, OBJ,POS X,POS Y...
			for (int i = 1; i < datos.length - 1; i += 3) //cambiar el incremento de i de acuerdo al msj
				this.listaDeDibujables.add(new ObjetoDibujable(datos[i].charAt(0), Integer.parseInt(datos[i+1]), Integer.parseInt(datos[i+2])));
			//PARTE NUEVA			
			this.juegoVentana.setListaDibujables(this.listaDeDibujables);
			this.juegoVentana.listoActualiza();
			break;
		case "ARRANCAR_PARTIDA":
			if(seguroPartida == false) {
				this.juegoVentana = new Juego(this,this.seleccionVentana.getPartidaSeleccionada(), this.listaDeDibujables);
				this.juegoVentana.setVisible(true);
				this.seleccionVentana.setVisible(false);
				this.seguroPartida = true;
				this.ventanaEspera.dispose();
			}
			break;
		case "ARRANCAR_NUEVA_RONDA":
			this.juegoVentana.arrancaNuevaRonda();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Al finalizar la partida, lanza la ventana de pregunta para volver a jugar
	 */
	public void volverAJugar() {
		this.ventanaEspera = null;
		this.partidaConfirmada = false;
		this.listaJugadores.clear();
	    int opcion = JOptionPane.YES_NO_OPTION;
	    this.seguroPartida = false;
        opcion = JOptionPane.showConfirmDialog(null, "¿Quiere seguir jugando en esta partida?",
                "Fin de la partida", opcion);
        if(opcion == JOptionPane.NO_OPTION) {
        	this.seleccionVentana.setEnabledGeneral(true);
        	this.seleccionVentana.setVisible(true);
        	this.juegoVentana.dispose();
        	this.juegoVentana = null;
        } 
        if (opcion == JOptionPane.YES_OPTION) {
        	String msjAEnviar = "SELECCIONPARTIDA;"+ this.seleccionVentana.getPartidaSeleccionada();
			this.elCliente.enviarMensaje(msjAEnviar);
			this.juegoVentana.dispose();
			this.juegoVentana = null;
        }
	}
	
	/**
	 * Ejecuta un cerrado controlado del socket y retorno al punto de logIn.
	 */
	public void cerrarSesion() {
		this.elCliente.desconectar();
		if(this.seleccionVentana != null)
			this.seleccionVentana.dispose();
		if(this.ventanaCambioPass != null)
			this.ventanaCambioPass.dispose();
		if(this.ventanaEspera != null)
			this.ventanaEspera.dispose();
		if(this.juegoVentana != null)
			this.juegoVentana.dispose();
		this.loginVentana.limpiaLogin();
		this.loginVentana.setEnabledGeneral(true);
		this.loginVentana.setVisible(true);	
	}
}