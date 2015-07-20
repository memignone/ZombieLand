package cliente;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import ventanasCliente.*;

public class ThreadCliente extends Thread {
	private Cliente cliente;
	private Principal principal;
	private DataInputStream entrada = null;
	private boolean executeThread = true;
	
	//CONSTRUCTOR-------------------------
	public ThreadCliente(Socket communication, Cliente cliente){
		this.cliente = cliente;
		this.principal = cliente.getPrincipal();
		try {
			this.entrada = new DataInputStream(communication.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//GETTERS & SETTERS-------------------------
	public Principal getPrincipal() {
		return this.principal;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	//OTROS METODOS-------------------------
	/**
	 * Ejecuciòn continua del threadCliente para escucha de mensajes desde el servidor
	 */
	public void run() {
		String linea;
		String[] datos;
		String key = null;
		
		while(executeThread) {
			try {
				linea = entrada.readUTF();
				datos = linea.split(";");
				key = datos[0];

				switch(key) {
				case "SIAUTENTICA":
					principal.getCliente().setcondicionDeAutenticacion("autenticado");
					break;				
				case "NOAUTENTICA":
					principal.getCliente().setcondicionDeAutenticacion("noautenticado");					
					break;
				case "ENVIO_RANKING":
					principal.reciboDatosVentanaSeleccion(key,datos);
					break;				
				case "LISTADO_JUEGOS":
					principal.reciboDatosVentanaSeleccion(key, datos);
					break;			
				case "INFO_NUEVA_PARTIDA":
					principal.reciboDatosVentanaSeleccion(key, datos);
					break;					
				case "ACTUALIZACION_DE_LISTA_PARTIDAS":
					principal.reciboDatosVentanaSeleccion(key, datos);
					break;
				case "ARRANCAR_PARTIDA":
					principal.ventanaJuego(key, datos);
					break;
				case "CONFIRMAR_PARTIDA":
					principal.reciboDatosVentanaSeleccion(key, datos);
				case "ERROR_AL_UNIRSE":
					principal.reciboDatosVentanaSeleccion(key, datos);
					break;
				case "LISTA_OBJETOS_PARA_DIBUJAR":
					principal.ventanaJuego(key, datos);
					break;
				case "JUGADOR_NUEVO_CREADO":
					principal.recibirMsj(key);
					break;
				case "JUGADOR_DUPLICADO":
					principal.recibirMsj(key);
					break;
				case "ACTUALIZAR_MOVIMIENTOS":
					principal.ventanaJuego(key, datos);
					break;
				case "DATOS_USR":
					principal.reciboDatosVentanaSeleccion(key, datos);
					break;
				case "ERROR_CAMBIO_PASS":
					principal.recibirMsj(key);
					break;
				case "PASS_CAMBIADA":
					principal.recibirMsj(key);
					break;
				case "PUNTAJE_JUGADORES":
					principal.ventanaJuego(key, datos);
					break;
				case "FIN_PARTIDA":
					principal.volverAJugar();
					break;
				case "ARRANCAR_NUEVA_RONDA":
					principal.ventanaJuego(key, datos);
					break;
				default:
					break;
				}
				
				if(linea.length() > 1){
					System.out.println(linea);
				}
			} catch (IOException e) {
				System.out.println("El servidor termino la conexion");
				principal.cerrarSesion();
				break;
			}
		}
	}
	
	/**
	 * Detiene la execuciòn del fucking thread
	 */
	public void stopTheFuckingThread() {
		this.executeThread = false;
	}
	
	/**
	 * Cierra el socket
	 * @author Fernando
	 */
	public void cerrarSocket() {
		try {
			entrada.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}