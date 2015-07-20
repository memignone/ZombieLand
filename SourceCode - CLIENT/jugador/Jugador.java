package jugador;

public class Jugador {	
	private String usuario = "";
	private String nick = "";
	private int puntaje, jugados, ganados, puntajeTotal;
	private int posx, posy;
	private double promPPP;
	private boolean esZombie;

	//CONSTRUCTOR------------------------	
	public Jugador() {
	}
	
	public Jugador (String  nom, int puntajeTotalArg, int ganados, boolean esZombie, int x, int y) {
		this.usuario = nom;
		this.puntaje = 0;
		this.puntajeTotal = puntajeTotalArg;
		this.ganados = ganados;
		this.esZombie = esZombie;
		this.posx = x;
		this.posy = y;
	}
		
	/**
	 * Constructor utilizado para mostrar el nick y el puntaje de la partida en la 
	 * ventana del juego en desarrollo
	 * msj recibido desde el servidor
	 * NICK DEL JUGADOR, PUNTAJE DE LA PARTIDA
	 */
	public Jugador(String nickArg, int puntajePartidaArg) {
		this.nick = nickArg;
		this.puntaje = puntajePartidaArg;
	}
	
	public Jugador (String usuario, String nickArg, int puntajePartidaArg) {
		this.usuario = usuario;
		this.nick = nickArg;
		this.puntaje = puntajePartidaArg;
	}
	/**
	 * CONSTRUCTOR UTILIZADO PARA EL RANKING
	 * @param nom
	 * @param puntajeTotalArg
	 * @param partidasGanadas
	 */
	public Jugador(String usr, int puntajeTotal, int partidasJugadas, int partidasGanadas, double promPPP){
		this.usuario = usr;
		this.puntajeTotal = puntajeTotal;
		this.setJugados(partidasJugadas);
		this.ganados = partidasGanadas;
		this.setPromPPP(promPPP);
	}
	
	/**
	 * CONSTRUCTOR UTILIZADO PARA SETEAR EL NOMBRE DEL JUGADOR CON EL MISMO QUE TIENE EL CLIENTE 
	 * QUE SE CONECTO. DE ESTA MANERA SE PUEDE IDENTIFICAR AL QUIEN ES EL DUE�O DE LA PANTALLA
	 * DENTRO DE UNA LISTA DE JUGADORES. QUIEN SOY YO EN UNA LISTA DE JUGADORES
	 * SE UTLIZA EL NOMBRE COMO COMPARACION 
	 */
	public Jugador (String nomArg){
		this.usuario = nomArg;
	}
	
	//GETTERS & SETTERS--------------------------	
	public String getUsuario() {
		return usuario;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setUsuario(String nombre) {
		this.usuario = nombre;
	}
	
	public int getPuntaje() {
		return puntaje;
	}
	
	public boolean getEsZombie(){
		return this.esZombie;
	}
	
	public int getPosX(){
		return this.posx;
	}
	
	public int getPosY(){
		return this.posy;
	}
	
	public int getpuntajeTotal() {
		return puntajeTotal;
	}
	
	public void setNick(String nickArg){
		this.nick = nickArg;
	}
	
	public void setPosX(int x){
		this.posx =  x;
	}
	
	public void setPosY(int y){
		this.posx =  y;
	}
	
	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}
	
	public int getGanados() {
		return ganados;
	}
	
	public void setGanados(int ganados) {
		this.ganados = ganados;
	}
	
	public void setEsZombie(){
		this.esZombie = true;
	}
	
	public void setNoZombie(){
		this.esZombie = false;
	}
	
	public void setpuntajeTotal(int puntaTotalArg) {
		 this.puntajeTotal = puntaTotalArg;
	}

	public int getJugados() {
		return jugados;
	}

	public void setJugados(int jugados) {
		this.jugados = jugados;
	}

	public double getPromPPP() {
		return promPPP;
	}

	public void setPromPPP(double promPPP) {
		this.promPPP = promPPP;
	}
	
	public String toString(){
		return this.nick+" "+this.puntaje;
	}
}