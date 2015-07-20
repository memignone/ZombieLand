package propiedadesDelJuego;

public class PuntoObstaculos {	
	int posx, posy;
	boolean estado;
	int ancho, altura;
	
	//CONSTRUCTOR----------------------------------
	public PuntoObstaculos() {
		this.posx = this.posy = 0;
	}

	public PuntoObstaculos(int x, int y) {
		this.posx =  x; 
		this.posy = y;
	}
	
	public PuntoObstaculos(int x, int y, int w, int h) {
		this.posx =  x; 
		this.posy = y;
		this.ancho = w;
		this.altura = h;		
	}
	
	//GETTERS & SETTERS---------------------------
	public int getPosx() {
		return posx;
	}
	
	public void setPosx(int posx) {
		this.posx = posx;
	}
	
	public int getPosy() {
		return posy;
	}
	
	public void setPosy(int posy) {
		this.posy = posy;
	}
	
	public boolean isEstado() {
		return estado;
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public int getAncho() {
		return ancho;
	}
	
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
	
	public int getAltura() {
		return altura;
	}
	
	public void setAltura(int altura) {
		this.altura = altura;
	}	
}