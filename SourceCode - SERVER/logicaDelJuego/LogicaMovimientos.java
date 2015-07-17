package logicaDelJuego;
import java.util.ArrayList;

import servidor.ThreadServidor;

public class LogicaMovimientos {
	private int anchoDelTablero=10;
	private int altoDelTablero=10;
	private ArrayList<ThreadServidor> clientesActivos;
	private ArrayList<Jugador> JugadoresActivos = new ArrayList<Jugador>();
	private PiezaDeTablero tablero[][] = new PiezaDeTablero[10][10]; 
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
	
	private int posIniHumanos[][]= {{0,1},{0,8},{9,8},{9,1},{1,0},{1,9},{8,9},{8,0},};
	private int cantZombies;
	private int cantHumanos;
	private boolean finRonda;
	private Piso piso;



	/**
	 * Constructor crea el tablero y lo carga con las posiciones iniciales de todos los obstaculos y jugadores
	 * @author Rodri
	 */
	public LogicaMovimientos(ArrayList<ThreadServidor> clientesActivos){
	
		this.clientesActivos=clientesActivos;	
		cantZombies=0;
		cantHumanos=0;
		finRonda=false;
		this.piso = new Piso();
		/*Cargo los obstaculos*/
		for (int i = 0; i < anchoDelTablero; i++) {
			for (int j = 0; j < altoDelTablero; j++) {
				if(obstaculos[j][i]==1){
					tablero[j][i] = new Obstaculo(i,j);
				}else{
					tablero[j][i] = piso;
				}
			}
		}
		
		int i=0,f,c;
		
		while(i<this.clientesActivos.size()){
			/*Si no hay zombie y no fue zombie*/
			if(cantZombies==0 && !this.clientesActivos.get(i).getFueZombie()){
				if(Math.random()<0.5)
					f=4;
				else 
					f=5;
					 
				Jugador zombie = new Jugador(this.clientesActivos.get(i).getUsuario(),f,f,true,clientesActivos.get(i));
				zombie.setFueZombie(true);
				JugadoresActivos.add(zombie);
				tablero[f][f]=zombie;
				cantZombies++;
			}
			else{
				f =posIniHumanos[cantHumanos][0];
				c =posIniHumanos[cantHumanos][1];
				Jugador humano = new Jugador(this.clientesActivos.get(i).getUsuario(),f,c,false,clientesActivos.get(i));
				JugadoresActivos.add(humano);
				tablero[c][f]=humano;
				cantHumanos++;
			}
			i++;			
		}
		if(cantHumanos==2){
			for (Jugador j : JugadoresActivos) {
				if(!j.getEsZombie()){//si es humano
					j.setPuntajeActual(j.getPuntajeActual()+2);
				}
			}
		}
		if(cantHumanos==1){
			for (Jugador j : JugadoresActivos) {
				if(!j.getEsZombie()){//si es humano
					j.setPuntajeActual(j.getPuntajeActual()+3);
					j.setCantidadRondasGanadas(j.getCantidadRondasGanadas()+1);
				}
			}
		}
	
		
	}
	
	/**
	 * 
	 * @param movimientos tiene KEY;NICK1;DIRECCION1;NICK2;DIRECCION2;... 
	 * ej-> MOVIMIENTOS;pepe;ARRIBA;carlos;ABAJO;
	 * 
	 * Este método recibe los movimientos de un turno de todos los jugadores
	 * Debe procesar los movimientos y registrar la nueva posicion de cada jugador y cambios a zombie
	 * en la lista de jugadores activos y en el tablero
	 * @author Rodri
	 */

	public void procesarMovimientos(ArrayList<String> movimientos){//cambiar a (lista de String)
		String split[];
		String usuario;
		String direccion;
		Jugador jugadorAux;
		ArrayList<Movimiento> listaDeMovimientos = new ArrayList<Movimiento>();
	
		while(!movimientos.isEmpty()) {
			split=movimientos.remove(0).split(";");
			usuario=split[0];
			direccion=split[1];
			
			int j=0;
			int pos=-1;
			while(j < JugadoresActivos.size()){//busco el jugador en la lista de jugadores
				//jugadorAux = JugadoresActivos.get(j);
				 if(JugadoresActivos.get(j).getUsuario().equals(usuario)){
					 pos=j;
					 break;
				 } 
				j++;
			}
			jugadorAux = JugadoresActivos.get(pos);
			//agrego el movimiento en la lista de movimientos
			listaDeMovimientos.add(new Movimiento(jugadorAux,direccion));

//AQUI ELIMINE TOD0						
						
			
		}
		//Aqui ya tengo todos los movimientos cargados en la lista de movimientos para todos los jugadores activos
		//solo restaria ver colisiones entre jugadores
		//ver si se pisan las nuevas posiciones de dos jugadores //ver si un humano y un zombie quedaron a 1 de distancia
		boolean huboPisados=true;//para que entre al while
		//boolean yaMovio[] = new boolean[listaDeMovimientos.size()];
		int noSePisan=0;

		while(noSePisan<listaDeMovimientos.size() && huboPisados){
			//ver si se pisan las nuevas posiciones de dos jugadores 
			huboPisados = false;
			noSePisan=0;
			/*for (boolean b : yaMovio) {
				  b = false;
			}*/
			boolean yaMovio[] = new boolean[listaDeMovimientos.size()];
			for (int i = 0; i < listaDeMovimientos.size(); i++) {
				   yaMovio[i]=true;
				for (int j = i+1; j < listaDeMovimientos.size(); j++) {
					
					if(listaDeMovimientos.get(i).getNueX() == listaDeMovimientos.get(j).getNueX() 
					   && listaDeMovimientos.get(i).getNueY() == listaDeMovimientos.get(j).getNueY()){//si los dos movieron a la misma pos
						
						if(yaMovio[j]){															 //si el segundo ya movio
						   listaDeMovimientos.get(i).setNueX(listaDeMovimientos.get(i).getExX());//vuelvo para atras el primero
						   listaDeMovimientos.get(i).setNueY(listaDeMovimientos.get(i).getExY());
						   huboPisados = true;
						   break;
						}
						else{																		//si el segundo no movio
							   listaDeMovimientos.get(j).setNueX(listaDeMovimientos.get(j).getExX());//vuelvo para atras el segundo
							   listaDeMovimientos.get(j).setNueY(listaDeMovimientos.get(j).getExY());
							   yaMovio[j] = true;
							   huboPisados = true;
						}
					}
					
					else{//no se pisan
						noSePisan++;
					}
				}
			}
		}
	    for (Movimiento movimiento : listaDeMovimientos) {
	    	//actualizar el tablero y actualizar la posicion de cada jugador
	    	movimiento.getJugador().setExX(movimiento.getExX());
			movimiento.getJugador().setExY(movimiento.getExY());
			movimiento.getJugador().setPosX(movimiento.getNueX());
			movimiento.getJugador().setPosY(movimiento.getNueY());
			tablero[movimiento.getExY()][movimiento.getExX()] = piso;
			tablero[movimiento.getNueY()][movimiento.getNueX()] = movimiento.getJugador();
		}
	    boolean nuevoZombie = false;
	    while(!nuevoZombie){
	    	    nuevoZombie = true;
			    for (Jugador jugador : JugadoresActivos) {
					if(!jugador.getEsZombie()){//si es humano
						if(verificarConversion(jugador))
							nuevoZombie = false;
					}
				}
	    }
	}
	
	/**
	 * verifica para cada jugador humano se se convirtio en zombie
	 * @param jugador
	 * @return boolean true si se convirtio; false si no se convertio
	 * @author Rodri
	 */
	private boolean verificarConversion(Jugador jugador){		
		int x=jugador.getPosX();						
		int y=jugador.getPosY();
		
		//cuando [x-1] -> x>0
		//cuando [x] -> x
		//cuando [x+1] -> x<9[x-1]
		if((x>0 && y<9 && tablero[y+1][x-1].getType().equals("zombie"))||
		   (x>0         && tablero[y][x-1].getType().equals("zombie"))  ||
		   (x>0 && y>0 && tablero[y-1][x-1].getType().equals("zombie"))||
		   (x<9 && y<9 && tablero[y+1][x+1].getType().equals("zombie"))||
		   (x<9         && tablero[y][x+1].getType().equals("zombie"))  ||
		   (x<9 && y>0 && tablero[y-1][x+1].getType().equals("zombie"))||
		   (	   y<9 && tablero[y+1][x]  .getType().equals("zombie"))||
		   (	   y>0 && tablero[y-1][x]  .getType().equals("zombie"))){
			jugador.setEsZombie(true);
			cantZombies++;
			cantHumanos--;
			if(cantHumanos==2){
				for (Jugador j : JugadoresActivos) {
					if(!j.getEsZombie()){//si es humano
						j.setPuntajeActual(j.getPuntajeActual()+2);
					}
				}
			}
			if(cantHumanos==1){
				for (Jugador j : JugadoresActivos) {
					if(!j.getEsZombie()){//si es humano
						j.setPuntajeActual(j.getPuntajeActual()+1);
						j.setCantidadRondasGanadas(j.getCantidadRondasGanadas()+1);
					}
				}
			}
			terminoRonda();
			return true;
		}
		return false;
	}
	/**
	 * Formatea el String que contiene los datos de las nuevas posiciones que luego se envia al cliente.
	 * @param caso distinge si es el primer mensaje de la ronda o no
	 */
	public void enviarNuevasPosiciones(int caso){
		terminoRonda();
		//PARAHUMANOS---> LISTA_OBJETOS_PARA_DIBUJAR;tipodelJugador;XdelJugador;YdelJgador;tipodevecino;xvecino;yvecino
		//PARAZOMBIES--->todos el mapa
		int x;
		int y;
		String mensajeDibujo = new String ("");
		for (Jugador jugador : JugadoresActivos) {
			x=jugador.getPosX();
			y=jugador.getPosY();
			//System.out.println(x + " "+y);
			if(caso==0)
				mensajeDibujo="LISTA_OBJETOS_PARA_DIBUJAR"+jugador.toString();
			else
				mensajeDibujo="ACTUALIZAR_MOVIMIENTOS"+jugador.toString();
			
			if(jugador.getEsZombie()){			
				for (int i = 0; i < altoDelTablero; i++) {
					for (int j = 0; j < anchoDelTablero; j++) {
						if(tablero[j][i] != piso){
							//System.out.println(i+" "+j);
							if(j==y && i==x){
								
							}else
								mensajeDibujo+=tablero[j][i].toString();
						}
						else{
							mensajeDibujo+=";P;"+Integer.toString(i*40)+";"+Integer.toString(j*40);
						    }
					}
				}
				
			}
			else{//No es Zombie
				//cuando [X-2] -> x>1
				//cuando [x-1] -> x>0
				//cuando [x] -> x
				//cuando [x+1] -> x<9ARRIBA
				//cuando [x+2] -> x<8
				
				if(x>1 && y>1){ 
					if(tablero[y-2][x-2] != piso)
						mensajeDibujo+=tablero[y-2][x-2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-2)*40)+";"+Integer.toString((y-2)*40);
				}
				if(x>1 && y>0){ 
					if(tablero[y-1][x-2] != piso)
						mensajeDibujo+=tablero[y-1][x-2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-2)*40)+";"+Integer.toString((y-1)*40);
				}
				if(x>1){         
					if(tablero[y] [x-2]!= piso)
						mensajeDibujo+=tablero[y][x-2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-2)*40)+";"+Integer.toString(y*40);
				}
				if(x>1 && y<9){ 
					if(tablero[y+1][x-2] != piso)
						mensajeDibujo+=tablero[y+1][x-2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-2)*40)+";"+Integer.toString((y+1)*40);
				}
				if(x>1 && y<8){ 
					if(tablero[y+2][x-2] != piso)
						mensajeDibujo+=tablero[y+2][x-2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-2)*40)+";"+Integer.toString((y+2)*40);
				}
				if(x>0 && y>1){
					if(tablero[y-2] [x-1]!= piso)
						mensajeDibujo+=tablero[y-2][x-1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-1)*40)+";"+Integer.toString((y-2)*40);
				}
				if(x>0 && y>0){ 
					if(tablero[y-1][x-1] != piso)
						mensajeDibujo+=tablero[y-1][x-1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-1)*40)+";"+Integer.toString((y-1)*40);
				}
				if(x>0){         
					if(tablero[y][x-1] != piso)
						mensajeDibujo+=tablero[y][x-1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-1)*40)+";"+Integer.toString(y*40);
				}
				if(x>0 && y<9){
					if(tablero[y+1][x-1] != piso)
						mensajeDibujo+=tablero[y+1][x-1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-1)*40)+";"+Integer.toString((y+1)*40);
				}
				if(x>0 && y<8){ 
					if(tablero[y+2] [x-1]!= piso)
						mensajeDibujo+=tablero[y+2][x-1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x-1)*40)+";"+Integer.toString((y+2)*40);
				}
				if(y>1){
					if(tablero[y-2][x] != piso)
						mensajeDibujo+=tablero[y-2][x].toString();
					else mensajeDibujo+=";P;"+Integer.toString(x*40)+";"+Integer.toString((y-2)*40);
				}
				if(y>0){
					if(tablero[y-1][x] != piso)
						mensajeDibujo+=tablero[y-1][x].toString();
					else mensajeDibujo+=";P;"+Integer.toString(x*40)+";"+Integer.toString((y-1)*40);
				}
				if(y<9){
					if(tablero[y+1][x] != piso)
						mensajeDibujo+=tablero[y+1][x].toString();
					else mensajeDibujo+=";P;"+Integer.toString(x*40)+";"+Integer.toString((y+1)*40);
				}
				if(y<8){ 
					if(tablero[y+2][x] != piso)
						mensajeDibujo+=tablero[y+2][x].toString();
					else mensajeDibujo+=";P;"+Integer.toString(x*40)+";"+Integer.toString((y+2)*40);
				}
				if(x<9 && y>1){
					if(tablero[y-2][x+1] != piso)			
						mensajeDibujo+=tablero[y-2][x+1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+1)*40)+";"+Integer.toString((y-2)*40);
				}
				if(x<9 && y>0){
					if(tablero[y-1][x+1] != piso)
						mensajeDibujo+=tablero[y-1][x+1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+1)*40)+";"+Integer.toString((y-1)*40);
				}
				if(x<9){
					if(tablero[y][x+1] != piso)
						mensajeDibujo+=tablero[y][x+1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+1)*40)+";"+Integer.toString(y*40);
				}
				if(x<9 && y<9){
					if(tablero[y+1] [x+1]!= piso)
						mensajeDibujo+=tablero[y+1][x+1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+1)*40)+";"+Integer.toString((y+1)*40);
				}
				if(x<9 && y<8){
					if(tablero[y+2][x+1] != piso)
						mensajeDibujo+=tablero[y+2][x+1].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+1)*40)+";"+Integer.toString((y+2)*40);
				}	
				if(x<8 && y>1){
					if(tablero[y-2][x+2] != piso)
						mensajeDibujo+=tablero[y-2][x+2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+2)*40)+";"+Integer.toString((y-2)*40);
				}
				if(x<8 && y>0){
					if(tablero[y-1][x+2] != piso)
						mensajeDibujo+=tablero[y-1][x+2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+2)*40)+";"+Integer.toString((y-1)*40);
				}
				if(x<8){
					if(tablero[y][x+2] != piso)
						mensajeDibujo+=tablero[y][x+2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+2)*40)+";"+Integer.toString(y*40);
				}
				if(x<8 && y<9){
					if(tablero[y+1][x+2] != piso)
						mensajeDibujo+=tablero[y+1][x+2].toString();
					else mensajeDibujo+=";P;"+Integer.toString((x+2)*40)+";"+Integer.toString((y+1)*40);
				}
				if(x<8 && y<8){
					if(tablero[y+2][x+2] != piso)
						mensajeDibujo+=tablero[y+2][x+2].toString();	
					else mensajeDibujo+=";P;"+Integer.toString((x+2)*40)+";"+Integer.toString((y+2)*40);
				}
			}
			System.out.println(mensajeDibujo);
			jugador.getCliente().enviarMensaje(mensajeDibujo);
			
		}
	}
	/**
	 * Se fija que se aya terminado la ronda y devuelve el resultado
	 * @return boolean true si termino la ronda
	 */
	private boolean terminoRonda(){
		actualizarConectados();//elimino los jugadores desconectados
		//un juego termina cuando todos son zombies oOo cuando solo queda un jugador activo oOo cuanto todos los jugadores activos son zombies
		int fueronZombie=0;
		if(JugadoresActivos.size() == cantZombies || JugadoresActivos.size()<=1){
			finRonda=true;
			return true;
		}
		for (Jugador jugador : JugadoresActivos) {//me fijo que todos los jugadores no hayan sido inicialmente zombies
			/*Cuento la cantidad de jugadores que fueron zombie*/
			if(jugador.getFueZombie())
				fueronZombie++;
		}
		if (JugadoresActivos.size() == fueronZombie){
			finRonda=true;
			return true;
		}
		
		
		if(cantZombies==0){
			if(cantHumanos>1){//combertir a alguien a zombie	
				Jugador nueZombie = null;
				for (Jugador jugador : JugadoresActivos) {
					if(!jugador.getFueZombie()){
						nueZombie = jugador;
					}
				}
				if(nueZombie!=null){//convrtir a zombie
					nueZombie.setFueZombie(true);
					nueZombie.setEsZombie(true);
					cantZombies++;
					cantHumanos--;
				}
				else{//chau ronda
					finRonda=true;
					return true;
				}
			}
			else{//no hay zombies y quedo un humano o ninguno
				finRonda=true;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * actuliza Clientes activos
	 * 
	 */
	private void actualizarConectados(){
		
		for (int i = 0; i < JugadoresActivos.size(); i++) {
			Jugador jAct =JugadoresActivos.get(i);
			if(!clientesActivos.contains(jAct.getCliente())){/*el jugador se desconectó*/
				int x=jAct.getPosX();
				int y=jAct.getPosY();
				tablero[y][x] = piso;			//lo saco del tablero
				if(jAct.getEsZombie())
					cantZombies --;			//si era zombie hago -1 en cant de zombies
				else 
					cantHumanos--;
				JugadoresActivos.remove(jAct);//lo saco de la lista de jugadores activos
			}
		}
		
	}
	/**
	 * @return boolean finRonda
	 */
	public boolean getFinRonda() {
		return finRonda;
	}

	public ArrayList<Jugador> getListaJugadores(){
		return this.JugadoresActivos;
	}
	
	/**
	 * @return String 
	 */
	public String formatearPuntaje(){
		String linea = "PUNTAJE_JUGADORES";
		for (Jugador jugador : JugadoresActivos) {
			linea += ";" + jugador.getCliente().getUsuario() + ";" + jugador.getCliente().getNick() + ";" + Integer.toString(jugador.getPuntajeActual());
			//jugador.getCliente().setPuntajeTotal(jugador.getCliente().getPuntajeTotal()+jugador.getPuntajeActual());
		}
		return linea;
	}
}