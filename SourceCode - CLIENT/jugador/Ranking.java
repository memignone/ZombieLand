package jugador;
import java.util.ArrayList;

public class Ranking {
	private ArrayList<Jugador> listaJugadores;
	
	//CONSTRUCTOR-------------------------------
	public Ranking(){		
		//this.listaJugadores = new ArrayList<Jugador>();
	}
	
	//GETTERS & SETTERS-------------------------
	public ArrayList<Jugador> getListaJugadores() {
		return listaJugadores;
	}
	
	public void setListaJugadores(ArrayList<Jugador> listaRanking) {
		this.listaJugadores = listaRanking;
	}
}