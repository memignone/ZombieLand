package dibujable;

public class ObjetoDibujable {
	private int posX, posY;
	private char tipo;

	//CONSTRUCTOR-------------------------------
	public ObjetoDibujable(char t,int x, int y) {
		this.posX = x;
		this.posY = y;
		this.tipo = t;
	}

	//GETTERS & SETTERS--------------------------
	public int getPosX() {
		return posX;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public char getTipo() {
		return tipo;
	}
	
	public void setTipo(char tipo) {
		this.tipo = tipo;
	}
}