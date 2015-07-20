package propiedadesDelJuego;

public class Partida {
	private String nombre;
	private String estado ;
	private Integer jugadoresReq,conectados;
	
	//CONSTRUCTOR-----------------------------------------------
	public Partida(String nom, String estado, int jugadoresReqArg, int conectadosArg ) {
		this.nombre = nom;
		this.estado = estado;
		this.jugadoresReq = jugadoresReqArg;
		this.conectados = conectadosArg;
	}

	public Partida(String nombreArg) {
		this.nombre = nombreArg;
	}
	
	//GETTERS & SETTERS----------------------------------------
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Integer getJugadoresReq() {
		return jugadoresReq;
	}
	
	public void setJugadoresReq(Integer jugadoresReq) {
		this.jugadoresReq = jugadoresReq;
	}
	
	public Integer getConectados() {
		return conectados;
	}
	
	public void setConectados(Integer conectados) {
		this.conectados = conectados;
	}
	
	public void setConectados(int conectados) {
		this.conectados = conectados;
	}
}