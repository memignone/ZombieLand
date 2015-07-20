package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Es una clase temporal para mandar mensajes a un cliente por consola. Se utiliza para las prueba de los mensajes del cliente
 **/
 
public class ThreadDePruebaDeMensajes extends Thread {
	private Servidor servidor;
	
	/**
	 * En el constructor se le asigna el servidor al que hace la regerencia. el servidos contiene la lista de los clientes activos a mandar el mensaje de prueva.
	 **/
	public ThreadDePruebaDeMensajes(Servidor servidor) {
		this.servidor = servidor;
	}
	
	public void run(){
		String datos[];
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		while(true){
        try {
			String s = bufferRead.readLine();
			datos=s.split(" ");
			for (ThreadServidor cliente : servidor.getClientesActivos()) {
				if(cliente.getUsuario().equals(datos[0]))
					cliente.enviarMensaje(datos[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}
}
