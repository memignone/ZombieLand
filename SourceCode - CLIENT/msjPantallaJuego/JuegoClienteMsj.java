package msjPantallaJuego;
import java.util.ArrayList;
import java.util.Random;

import jugador.*;

public class JuegoClienteMsj {	
	private ArrayList<Jugador> listaPlayerJuego = new ArrayList<Jugador>();
	
	//CONSTRUCTOR------------------------------------
	public JuegoClienteMsj() {
		generaJu(listaPlayerJuego);
	}
		
	//GETTERS & SETTERS------------------------------
	public ArrayList<Jugador> getListaPlayerJuego() {
		return listaPlayerJuego;
	}
		
	public void setListaPlayerJuego(ArrayList<Jugador> listaPlayerJuego) {
		this.listaPlayerJuego = listaPlayerJuego;
	}
	
	//OTROS METODOS-----------------------------------
	/**
	 * genera un listado de jugadores donde el primero es una zombie y
	 * ademas se agrega uno a mano para utilizarlo como propietario
	 * en la clase Juego(pantalla del juego en desarrollo
	 * @param lista
	 * @return
	 */
	public static ArrayList<Jugador> generaJu(ArrayList<Jugador> lista) {
		String nombre=new String(); 
		boolean auxZombie = true;
		int posx,posy;
		
		posx = posy = 0;
		for ( int i = 0; i<3;i++) {
			Jugador j1 = new Jugador(nomAleatorio(nombre),(i*1000+100),(i+20),auxZombie,posx,posy);
			lista.add(j1);
			if(auxZombie )
				auxZombie = false;
			if (i == 0) {
				posx = 400;
			}
			else if(i == 1) {
				posx = 0;
				posy = 400;
			}
		}
		Jugador j2 = new Jugador("diego",(20*1000+100),20,false,400,400);
		lista.add(j2); 
		return lista;
	}
	
	/**
	 * Genera nombres aleatorios
	 * @param nom
	 * @return
	 */
	public static String nomAleatorio ( String nom) {
		Random rnd = new Random();
		char n =(char)(rnd.nextDouble() * 26.0 + 65.0 );
		
		for (int i=0; i < 10 ; i++) {
			n = (char)(rnd.nextDouble() * 26.0 + 65.0 );
			nom += n; 
		}
		return nom;
	}	
}