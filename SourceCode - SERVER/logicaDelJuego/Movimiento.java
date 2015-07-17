package logicaDelJuego;

public class Movimiento {
private int ancho = 10;
private int alto =10;            //-------------------->X
private int obstaculos [][] =      {{0,0,0,0,0,0,0,0,0,0},//|
									{0,0,1,0,0,0,0,1,0,0},//|
									{0,1,0,0,0,0,0,0,1,0},//|
									{0,0,0,1,0,0,1,0,0,0},//|
									{0,0,0,0,0,0,0,0,0,0},//|
									{0,0,0,0,0,0,0,0,0,0},//|
									{0,0,0,1,0,0,1,0,0,0},//|
									{0,1,0,0,0,0,0,0,1,0},//|
									{0,0,1,0,0,0,0,1,0,0},//v
									{0,0,0,0,0,0,0,0,0,0},//Y
								 };
private int exX;
private int exY;
private int nueX;
private int nueY;
private Jugador jugador;
	public Movimiento(Jugador jugador, String direccion){
		this.jugador=jugador;
		this.exX=jugador.getPosX();
		this.exY=jugador.getPosY();
		this.nueX=this.exX;
		this.nueY=this.exY;
		if(direccion.equals("ARRIBA") && exY > 0)
		   nueY=exY-1;
		if(direccion.equals("ABAJO") && exY < alto-1)
		   nueY=exY+1;
		if(direccion.equals("IZQUIERDA") && exX > 0)
		   nueX=exX-1;
		if(direccion.equals("DERECHA") && exX < ancho-1)
		   nueX=exX+1;
			
		if(obstaculos[nueY][nueX] == 1){//si despues de moverse pisaria un obstaculo se queda en la ex posicion
			this.nueX=this.exX;
			this.nueY=this.exY;
		}
	}
	public int getExX() {
		return exX;
	}
	public int getExY() {
		return exY;
	}
	public int getNueX() {
		return nueX;
	}
	public int getNueY() {
		return nueY;
	}
	public void setNueX(int nueX) {
		this.nueX = nueX;
	}
	public void setNueY(int nueY) {
		this.nueY = nueY;
	}
	public Jugador getJugador() {
		return jugador;
	}
	
}