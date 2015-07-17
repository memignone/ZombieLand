package servidor;

import ventanasServidor.AbrirServidor;
import ventanasServidor.ServidorAbierto;

/**
 * Maneja toda las referencias a las ventanas del servidor y las clases. maneja los mensajes
 * entre ventanas
 * @author fernando
 */

public class Manejador {

	private Servidor servidor;
	private ServidorAbierto servidorAbierto;
	private AbrirServidor abrirServidor;
	
	/**
	 * Constructor
	 * Se le pasa la referencia del abrir servidor
	 * @param abrirServidor
	 */
	public Manejador (AbrirServidor abrirServidor){
		this.abrirServidor = abrirServidor;
	}
	
	
	
	//					Geters y Seters
	
	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public ServidorAbierto getServidorAbierto() {
		return servidorAbierto;
	}

	public void setServidorAbierto(ServidorAbierto servidorAbierto) {
		this.servidorAbierto = servidorAbierto;
	}

	public AbrirServidor getAbrirServidor() {
		return abrirServidor;
	}

	public void setAbrirServidor(AbrirServidor abrirServidor) {
		this.abrirServidor = abrirServidor;
	}
	
	/**
	 * Le mandamos el identificador de cliente para que se lo agrege en la listade la interfaz
	 * @param identificadorDeCliente
	 * @author Fernando
	 */
	public void agregarClienteALaListaDeLaInterfazista(String identificadorDeCliente){
		servidorAbierto.agregarClienteALALista(identificadorDeCliente);
	}
	
	/**
	 * Le manda el identificador cliente a la ventana ServidorAbierto, para que lo elimine de la lista
	 * 
	 * @param identificadorDeCliente String con que se sdentifica al cliente, por ahora va a ser el numero de socket, 
	 * 								 desp va a ser el nombre de usuario cuando se implemente
	 * @author Fernando
	 */
	public void eliminarClienteDeLaListaDeLaInterfazista(String identificadorDeCliente){
		servidorAbierto.eliminasClienteDeLista(identificadorDeCliente);
	}
	
}
