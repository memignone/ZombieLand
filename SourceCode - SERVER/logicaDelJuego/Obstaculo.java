package logicaDelJuego;

public class Obstaculo implements PiezaDeTablero{
	public int posY;
	public int posX;
	
	public Obstaculo(int posX, int posY){
		this.posX = posX;
		this.posY = posY;
	}
	public String getType() {
		return "obstaculo";
	}
	public int getPosY() {
		return posY;
	}

	public int getPosX() {
		return posX;
	}

	@Override
	public String toString() {
		return ";"+ 'O' + ";" + Integer.toString(posX *40) + ";" + Integer.toString(posY*40);
	}

}
