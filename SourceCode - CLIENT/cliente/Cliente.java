package cliente;
import ventanasCliente.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Cliente {
	private static String ip_Servidor = null;
	private static int numeroSocket;
	private static DataOutputStream salida = null;
	private static Socket communication = null;
	private ThreadCliente threadEntrada;
	private String usuario;
	private String nick;
	private String password;
	private Principal principal;
	private String condicionDeAutenticacion = null;

	//GETTERS & SETTERS-------------------------
	public static String getIp_Servidor() {
		return ip_Servidor;
	}

	public static void setIp_Servidor(String ip_Servidor) {
		Cliente.ip_Servidor = ip_Servidor;
	}

	public static int getNumeroSocket() {
		return numeroSocket;
	}

	public  String getUsuario(){
		return this.usuario; 
	}
	
	public Principal getPrincipal(){
		return this.principal;
	}
	
	public  void setUsuario( String usuarioArg){
		 this.usuario = usuarioArg; 
	}
	
	public static void setNumeroSocket(int numeroSocket) {
		Cliente.numeroSocket = numeroSocket;
	}

	public static DataOutputStream getSalida() {
		return salida;
	}

	public static void setSalida(DataOutputStream salida) {
		Cliente.salida = salida;
	}

	public static Socket getCommunication() {
		return communication;
	}

	public static void setCommunication(Socket communication) {
		Cliente.communication = communication;
	}

	public ThreadCliente getThreadEntrada() {
		return threadEntrada;
	}

	public void setThreadEntrada(ThreadCliente threadEntrada) {
		this.threadEntrada = threadEntrada;
	}
	
	public String getPassword() {
		return this.password;
	}
		
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	//CONSTRUCTOR-------------------------
	public Cliente(String ip_Servidor, int numeroSocket, Principal p, String nombre){
		setIp_Servidor(ip_Servidor);
		setNumeroSocket(numeroSocket);
		this.principal = p;
		this.usuario = nombre;
		this.nick = nombre;
	}
	
	//OTROS METODOS-------------------------
	/**
	 * Abre el socket, crea el threadEntrada y lo inicia
	 * @return
	 */
	public boolean conectar() {
		try {
			communication = new Socket(ip_Servidor, numeroSocket);
			threadEntrada = new ThreadCliente(communication, this);
			threadEntrada.start();
			salida = new DataOutputStream(communication.getOutputStream());	
			return true;
		} catch (Exception e) {
			principal.recibirMsj("SERVER_DOWN");
			return false;
		}
	}

	/**
	 * Concatena "AUTENTICAR;" al usuario y contrase�a
	 * y los separa con ; asi lo reconoce el servidor
	 * y llama a la funcion enviarMensaje
	 * @param usuario
	 * @param password
	 * @return boolean true si es correcto o false si no en correcto
	 * @author Fernando
	 */
	public boolean autenticarUsuario(String usuario, String password) {
		condicionDeAutenticacion = "pendiente";
		enviarMensaje("AUTENTICAR;"+usuario+";"+password+";");
		while(condicionDeAutenticacion.equals("pendiente")){
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(condicionDeAutenticacion.equals("autenticado")){
			this.usuario = usuario;
			this.password = password;
			return true;
		}
		return false;
	}
	
	
	/**
	 * Deconectamos al cliente asi se puede volver a conectar si que salga errores
	 * @author Fernando
	 */	
	public void desconectar(){
		try {
			if(salida != null)
				salida.close();
			if(threadEntrada != null) {
				threadEntrada.stopTheFuckingThread();
				threadEntrada.cerrarSocket();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		threadEntrada = null;
		salida = null;
	}
	
	
	/**
	 * es una clase de envoltura, para que solo se manden mensajes al servidor desde esta funcion.
	 * Se le pasa un String para que se envie al servidor.
	 * Maneja el tratamiento de server caido.
	 * 
	 * @param linea String a mandar al servidor ya formateado
	 */
	public void enviarMensaje(String linea){
		try {
			salida.writeUTF(linea);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Se utiliva para camviar el valor de Autenticacion del cliente, si se acepta o no
	 * 
	 * @param valorDeAuteticacion "autenticado" "noautenticado"
	 */
	public void setcondicionDeAutenticacion(String valorDeAuteticacion){
		this.condicionDeAutenticacion = valorDeAuteticacion;
	}
}
