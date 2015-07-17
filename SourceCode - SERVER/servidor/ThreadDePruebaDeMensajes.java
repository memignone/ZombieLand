package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThreadDePruebaDeMensajes extends Thread {
	private Servidor servidor;
	
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
