package logicaDelJuego;


public class ThreadSegundosDelJuego extends Thread {
	private int segundos;
	private Partida partida;
	private boolean isRuning = true;
	
	
	public ThreadSegundosDelJuego(Partida partida) {
		this.partida = partida;
		this.segundos = 0;
	}
	
	public void run(){
			try {
					while(partida.getEstado().equals("Jugando") && isRuning){
						Thread.sleep(1000);
						if(partida.getLogicaMovimientos().getFinRonda()){
								partida.enviarPuntajes(1);	
							partida.nuevaRonda();
						}
						segundos++;
						if(segundos % 10 == 0)
							partida.getVentanaEstadisticas().escribirLineaDeDatos("Van " + Integer.toString(segundos) + " segundos de juego.");
					}
			} catch (InterruptedException e) {
				System.out.println("No cerro a tiempo el thread y si la logica de la partida maldito java u.u");
			}
	}
	
	public int getSegundos(){
		return this.segundos;
	}
	
	public void kill(){
		this.isRuning = false;
	}
	
}
