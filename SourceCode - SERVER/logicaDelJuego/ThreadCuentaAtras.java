package logicaDelJuego;

import java.util.concurrent.TimeUnit;

public class ThreadCuentaAtras extends Thread{
	private int tiempo;
	private Partida partida;
	
	public ThreadCuentaAtras(int tiempo, Partida partida) {
		this.tiempo = tiempo;
		this.partida = partida;
	}
	
	public void run(){
			try {
				partida.getVentanaEstadisticas().escribirLineaDeDatos("El juego comenzara en: " + Integer.toString(tiempo) + " segundos");
				for(int i = 0; i < tiempo; i++){
				TimeUnit.SECONDS.sleep(1);
				}
				partida.empezarPartida();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
	}
}
