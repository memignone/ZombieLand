package logicaDelJuego;

import servidor.ThreadServidor;

public class Jugador implements PiezaDeTablero{

	private String usuario;
	private int posX;
	private int posY;
	private int exX;
	private int exY;
	private boolean esZombie = false;
	private boolean fueZombie = false;
	private ThreadServidor cliente;
	private int cantidadRondasGanadas;
	private int puntajeActual;

	public Jugador(String usuario, int posX, int posY, boolean esZombie,ThreadServidor cliente) {
		
		this.usuario = usuario;
		this.posX = posX;
		this.posY = posY;
		this.esZombie = esZombie;
		this.cliente=cliente;
		this.cantidadRondasGanadas = 0;
		this.puntajeActual = 0;
	}
	
	
	
	public ThreadServidor getCliente() {
		return cliente;
	}

	public String getUsuario() {
		return usuario;
	}
	public String getType() {
		if(esZombie)
			return "zombie";
		return "humano";
	}
	public boolean getEsZombie() {
		return esZombie;
	}
	public void setEsZombie(boolean esZombie) {
		this.esZombie = esZombie;
	}
	public boolean getFueZombie() {
		return fueZombie;
	}
	public void setFueZombie(boolean fueZombie) {
		cliente.setFueZombie(fueZombie);
		this.fueZombie = fueZombie;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	@Override
	public int getPosX() {
		return this.posX;
	}
	
	
	@Override
	public int getPosY() {
		return this.posY;
	}
	
	/**
	 * formatea los datos del cliente (nick, puntaje actual, sie es zombie, pos x pos y)para que la partida se los mante al cliente
	 * @return String con los datos del clientes para ser concatenados con los demas clientes de una partida
	 */
	public String toString(){
		char tipo=' ';
		if(esZombie)
			tipo='Z';
		else tipo='H';
		return ";"+ tipo + ";" + Integer.toString(posX*40) + ";" + Integer.toString(posY*40);	
	}

	public int getExX() {
		return exX;
	}

	public void setExX(int exX) {
		this.exX = exX;
	}

	public int getExY() {
		return exY;
	}

	public void setExY(int exY) {
		this.exY = exY;
	}

	public int getCantidadRondasGanadas() {
		return cantidadRondasGanadas;
	}

	public void setCantidadRondasGanadas(int cantidadRondasGanadas) {
		this.cantidadRondasGanadas = cantidadRondasGanadas;
	}

	public int getPuntajeActual() {
		return puntajeActual;
	}

	public void setPuntajeActual(int puntajeActual) {
		this.puntajeActual = puntajeActual;
	}
	
}

