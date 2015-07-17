package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import dataBase.ZombieLandDB;
import logicaDelJuego.Partida;


public class Servidor extends Thread{
	private final ArrayList<ThreadServidor> clientesActivos = new ArrayList<ThreadServidor>();
	private ServerSocket skSrvr = null;
	private Manejador manejador;
	private ThreadDePruebaDeMensajes threadDePruevas;
	private ZombieLandDB db;
	private boolean inRuning = true;

	
	/**
	 * Constructor
	 * @param s_Socket el numero de socket
	 */
	public Servidor(int s_Socket, Manejador manejador){
		this.manejador = manejador;
		try {
			skSrvr = new ServerSocket(s_Socket);
			this.db = new ZombieLandDB();
		} catch (IOException e1) {
			skSrvr=null;
		}
		this.threadDePruevas = new ThreadDePruebaDeMensajes(this);
		this.threadDePruevas.start();
	}

	public ArrayList<ThreadServidor> getClientesActivos() {
		return clientesActivos;
	}

	public void run(){
			
			while(inRuning){
				try {
					Socket sock = skSrvr.accept();
					ThreadServidor user = new ThreadServidor(sock, manejador);
					user.start();
					
				} catch (IOException e ) {
					//e.printStackTrace();
					System.out.println("Quedo un cliente cuando se quiso cerrar el socket");
					break;
				}
				
			}
	}

	public void cerrarSocket() {
		try {
			skSrvr.close();
			this.clientesActivos.clear();
			this.manejador.setServidorAbierto(null);
			this.db.desconectar();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	/**
	 * Le mandamos una nueva partida para que se agrege en la lista de partidas.
	 * Va a hacer un foreach de los clientes activos y les va a mandar los datos necesarios
	 * @param String : formato "cantMin;cantMax;NombrePartida"
	 * @author Fernando
	 */
	public void sendPartida(String linea){
		for (ThreadServidor threadServidor : clientesActivos) {
			threadServidor.agregarUnapartidaALaLista(linea);
		}
	}
	
	/**
	 * Se elimina al cliente que se cerro el socket.
	 * Se lo llama desde el thread cuando falla la lectura del socket
	 * @param threadServidor
	 * @author Fernando
	 */
	public void eliminarDeListaCliente(ThreadServidor threadServidor) {
		this.clientesActivos.remove(threadServidor);
	}
	
	/**
	 * getSkSrvs seutiliza para es si se hizo bien el blind al socket
	 * falla cuando ya esta en uso
	 * @return null si falla el bind
	 */
	public ServerSocket getSkSrvr() {
		return skSrvr;
	}
	/**
	 * Le envia a todos los clientes la actualizacion de las listas de partidas cada
	 * vez que aya un cambio
	 */
	public void actualizarListaDePartidas(Partida partida) {
		for (ThreadServidor cliente : clientesActivos) {
			cliente.actualizarListas(partida);
		}	
	}

	public ZombieLandDB getDb() {
		return db;
	}
	
	public void kill(){
		this.inRuning = false;
	}
	
}
