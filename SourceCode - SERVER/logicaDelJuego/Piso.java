package logicaDelJuego;

public class Piso implements PiezaDeTablero{
	
	public Piso(){
		
	}
	public String getType() {
		return "piso";
	}

	@Override
	public int getPosX() {
		return 0;
	}

	@Override
	public int getPosY() {
		return 0;
	}
}
