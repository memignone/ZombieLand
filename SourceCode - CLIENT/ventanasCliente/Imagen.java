package ventanasCliente;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Imagen extends JPanel {
	private static final long serialVersionUID = 1L;
	private String ruta;

	//CONSTRUCTOR-----------------------------
	public Imagen() {
	}

	public Imagen(int ancho, int alto, String ruta) {		
		this.setSize(ancho, alto);
		this.ruta = ruta;
	}

	//OTROS METODOS---------------------------
	public void paint (Graphics g) {
		Dimension d = this.getSize(); 
		ImageIcon img = new ImageIcon(getClass().getResource(this.ruta));
		g.drawImage(img.getImage(), 0, 0, d.width, d.height,null);
		setOpaque(false);
		super.paintComponents(g);
	}
}