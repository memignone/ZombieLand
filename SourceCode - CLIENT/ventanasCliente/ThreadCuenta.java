package ventanasCliente;

public class ThreadCuenta extends Thread {
	private Juego juego;
	
	//CONSTRUCTOR------------------------------
	public ThreadCuenta(Juego j) {
		this.juego = j;
	}

	//OTROS METODOS----------------------------
	public void run() {
		while(this.juego.getContadorTiempo() > -2) {
			try {
				Thread.sleep(1000);
				this.juego.setTiempoLbl();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}	
}