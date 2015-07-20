package ventanasCliente;
import dibujable.*;
import jugador.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.List;

public class Juego extends JFrame {
	private static final long serialVersionUID = 2083578666119018746L;
	private JPanel contentPane;
	private Integer contadorTiempo = 5;
	private Imagen fondo;
	private Imagen campo = null;
	private Imagen miImagen;
	private Principal principal;
	private String movimientoAEnviar = new String();
	private String nomPartida;
	private ArrayList<Jugador> listaDeJugadores;
	private ArrayList<ObjetoDibujable> listaDeDibujables = new ArrayList<ObjetoDibujable>();
	private JLabel tiempoLbl;
	private JLabel movEnviadoLbl;
	private JLabel movInfoLbl;
	private JLabel paraMoverLbl;
	private Imagen  []  vecImagen;
	private JButton arribaBtn;
	private JButton abajoBtn ;
	private JButton izquierdaBtn;
	private JButton btnDerecha;
	private JButton quietoBtn;
	private ThreadCuenta cuentaThread = null;
	private List list_puntajes;
	
	//GETTERS & SETTERS------------------------------------
	public String getMovimiento() {
		return this.movimientoAEnviar;
	}
	
	public void setListaDibujables(ArrayList<ObjetoDibujable> listaObj) {
		this.listaDeDibujables = listaObj;
	}
	
	public int getContadorTiempo() {
		return this.contadorTiempo;
	}

	//CONSTRUCTOR------------------------------------------
	public Juego(Principal principalArg,String nom, ArrayList<ObjetoDibujable> listaDeDibujablesArg ) {
		setResizable(false);
		//-----------------SETEO DE VARIABLES MIEMBRO --------------------------		
		this.principal = principalArg;
		this.nomPartida = nom;
		this.listaDeDibujables = listaDeDibujablesArg;
		//--------- seteado de propiedades del frame----------------------
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 803, 615);
		setTitle("PARTIDA: "+this.nomPartida+" -- "+"USUARIO: "+this.principal.getCliente().getUsuario() );
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBounds(0, 0, 830, 615);
		contentPane.setLayout(null);
		setContentPane(contentPane);
				
		fondo = new Imagen(800,600,"/imagenes/fondo.jpg");
		fondo.setBounds(0, 0, 800, 600);
		fondo.setLayout(null);
		contentPane.add(fondo);

		//--------------------BOTONES---------------------------------
		arribaBtn = new JButton("Arriba");
		abajoBtn = new JButton("Abajo");
		izquierdaBtn = new JButton("Izquierda");
		btnDerecha = new JButton("Derecha");
		quietoBtn = new JButton("Quieto");
		
		abajoBtn.setBounds(157, 531, 89, 23);
		arribaBtn.setBounds(157, 460, 89, 23);
		btnDerecha.setBounds(268, 495, 101, 23);
		izquierdaBtn.setBounds(43, 495, 101, 23);
		quietoBtn.setBounds(157, 493, 89, 23);
		
		this.list_puntajes = new List();
		list_puntajes.setBounds(538, 85, 211, 192);
		fondo.add(list_puntajes);
		fondo.add(abajoBtn);
		fondo.add(arribaBtn);
		fondo.add(izquierdaBtn);
		fondo.add(btnDerecha);
		fondo.add(quietoBtn);
		//-----------------ACCIONES DE LOS BOTONES----------------------	
		arribaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movimientoAEnviar = "ARRIBA";
				movInfoLbl.setText(movimientoAEnviar);	
			}
		});
		
		abajoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				movimientoAEnviar = "ABAJO";
				movInfoLbl.setText(movimientoAEnviar);				
			}
		});
					
		izquierdaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				movimientoAEnviar = "IZQUIERDA";	
				movInfoLbl.setText(movimientoAEnviar);
			}
		});
		
		btnDerecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {						
				movimientoAEnviar = "DERECHA";	
				movInfoLbl.setText(movimientoAEnviar);
			}
		});
		
		quietoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movimientoAEnviar = "QUIETO";	
				movInfoLbl.setText(movimientoAEnviar);
			}
		});
		//--------------LABEL DE PUNTAJES---------------------------	
		JLabel puntajeLbl = new JLabel("PUNTAJE");
		puntajeLbl.setHorizontalAlignment(SwingConstants.CENTER);
		puntajeLbl.setForeground(new Color(255, 0, 0));
		puntajeLbl.setFont(new Font("Terror Pro", Font.PLAIN, 50));
		puntajeLbl.setBounds(509, 20, 263, 59);
		fondo.add(puntajeLbl);
		//--------------------TIEMPO---------------------------------
		tiempoLbl = new JLabel("5");
		tiempoLbl.setVerticalAlignment(SwingConstants.TOP);
		tiempoLbl.setForeground(new Color(204, 0, 102));
		tiempoLbl.setHorizontalAlignment(SwingConstants.CENTER);
		tiempoLbl.setFont(new Font("Tahoma", Font.PLAIN, 50));
		tiempoLbl.setBounds(562, 484, 66, 68);
		fondo.add(tiempoLbl);
		
		paraMoverLbl = new JLabel("TIEMPO PARA MOVER");
		paraMoverLbl.setForeground(new Color(255, 102, 255));
		paraMoverLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
		paraMoverLbl.setHorizontalAlignment(SwingConstants.CENTER);
		paraMoverLbl.setBounds(442, 455, 315, 30);
		fondo.add(paraMoverLbl);
		
		movEnviadoLbl = new JLabel("MOVIMIENTO ENVIADO");
		movEnviadoLbl.setForeground(new Color(255, 51, 153));
		movEnviadoLbl.setFont(new Font("Tahoma", Font.BOLD, 20));
		movEnviadoLbl.setBounds(480, 495, 280, 16);
		fondo.add(movEnviadoLbl);
		movEnviadoLbl.setVisible(false);
		
		movInfoLbl = new JLabel("");
		movInfoLbl.setHorizontalAlignment(SwingConstants.CENTER);
		movInfoLbl.setForeground(new Color(255, 255, 0));
		movInfoLbl.setFont(new Font("Tahoma", Font.BOLD, 20));
		movInfoLbl.setBounds(20, 425, 127, 33);
		fondo.add(movInfoLbl);
	
		// posiciona a los jugadores y los obstaculos  tomado desde la lista de dibujables 
		generaImgJugadores ();
		seteathread();		
		}
	
	//OTROS METODOS---------------------------------------------
	public void generaImgJugadores () {
		if(this.campo != null)
			fondo.remove(campo);
		vecImagen = new Imagen[this.listaDeDibujables.size()];
		campo = new Imagen(400,400,"/imagenes/campo.png");
		campo.setBounds(20, 13, 400, 400);
		campo.setLayout(null);
		fondo.add(campo);
		//EL PRIMER JUGADOR DE LA LISTA SIEMPRE SO YO, SOLO SABER SI ES ZOMBIE O NO
		 if (this.listaDeDibujables.get(0).getTipo()=='Z'){
		 	miImagen = new Imagen(40,40,"/imagenes/miZombie.png");
		 	miImagen.setAutoscrolls(true);
		 	miImagen.setBounds(this.listaDeDibujables.get(0).getPosX(), this.listaDeDibujables.get(0).getPosY(),40 , 40);
		 	miImagen.setLayout(null);
		 	campo.add(miImagen);
		 }
		 else {
			miImagen = new Imagen(40,40,"/imagenes/miCarita.png");
		 	miImagen.setAutoscrolls(true);
		 	miImagen.setBounds(this.listaDeDibujables.get(0).getPosX(), this.listaDeDibujables.get(0).getPosY(),40 , 40);
		 	miImagen.setLayout(null);
		 	campo.add(miImagen);
		 }
		 System.out.println(" YO SOY UN: "+this.listaDeDibujables.get(0).getTipo());
		 for (int i = 1; i < this.listaDeDibujables.size(); i++) {
			 if (this.listaDeDibujables.get(i).getTipo() == 'H') {
				vecImagen[i] = new Imagen(40,40,"/imagenes/carita.png");
				vecImagen[i].setAutoscrolls(true);
				vecImagen[i].setBounds(this.listaDeDibujables.get(i).getPosX(), this.listaDeDibujables.get(i).getPosY(),40 , 40);
				vecImagen[i].setLayout(null);
				campo.add(vecImagen[i]);	
			 } else if (this.listaDeDibujables.get(i).getTipo() == 'Z') {
					vecImagen[i] = new Imagen(40,40,"/imagenes/zombie.png");
					vecImagen[i].setAutoscrolls(true);
					vecImagen[i].setBounds(this.listaDeDibujables.get(i).getPosX(),this.listaDeDibujables.get(i).getPosY(),40,40);
					vecImagen[i].setLayout(null);
					campo.add(vecImagen[i]);
			 } else if (this.listaDeDibujables.get(i).getTipo() == 'O') {					
					vecImagen[i] = new Imagen(40,40,"/imagenes/caja.jpg");
					vecImagen[i].setAutoscrolls(true);
					vecImagen[i].setBounds(this.listaDeDibujables.get(i).getPosX(),this.listaDeDibujables.get(i).getPosY(),40,40);
					vecImagen[i].setLayout(null);
					campo.add(vecImagen[i]);			
			 } else if (this.listaDeDibujables.get(i).getTipo() == 'P') {
					vecImagen[i] = new Imagen(40,40,"/imagenes/piso.png");
					vecImagen[i].setAutoscrolls(true);
					vecImagen[i].setBounds(this.listaDeDibujables.get(i).getPosX(),this.listaDeDibujables.get(i).getPosY(),40,40);
					vecImagen[i].setLayout(null);
					campo.add(vecImagen[i]);
			 } else {
				System.err.println("NO SE PUEDO IDENTIFICAR QUE TIPO DE IMAGEN ES");
			 }
		 }
	 	this.movimientoAEnviar = "QUIETO";
	 	movInfoLbl.setText(movimientoAEnviar);
	 	campo.repaint();
	}

	public void setListaJugadores(ArrayList<Jugador> listaDeJugadoresArg) {
		this.listaDeJugadores = listaDeJugadoresArg;
	}
	
	public void seteaPuntaje() {
		list_puntajes.removeAll();
		for (Jugador j : this.listaDeJugadores) {
			this.list_puntajes.add(j.toString());
		}
	}
	
	public void enviaMovimiento() {
		seteaEstadoBotones(false);
		System.out.println("ENVIANDO MOVIMIENTO");
		this.cuentaThread.interrupt();;
		this.cuentaThread = null;
		this.principal.recibirMsj("JUEGO_PRINCIPAL_ME_MUEVO");
	}
	
	private void seteaEstadoBotones(boolean estado) {
		this.arribaBtn.setEnabled(estado);
		this.abajoBtn.setEnabled(estado);
		this.izquierdaBtn.setEnabled(estado);
		this.btnDerecha.setEnabled(estado);
		this.quietoBtn.setEnabled(estado);
	}
	
	public void setTiempoLbl() {
		this.contadorTiempo--;	
		if (this.contadorTiempo >= 0 ) {
			this.tiempoLbl.setText(Integer.toString(this.contadorTiempo));
			this.tiempoLbl.setVisible(true);
			System.out.println("Contador 1");
		}
		else if(this.contadorTiempo < 0 && this.contadorTiempo > -2) {
			this.tiempoLbl.setVisible(false);
			this.movEnviadoLbl.setVisible(true);
			System.out.println("Contador 2");
		}
		else {
			System.out.println("Contador 2");
			this.enviaMovimiento();	
		}	
	}
	
	private void seteathread() {
		if(this.cuentaThread == null) {
	 		cuentaThread = new ThreadCuenta(this);
	 		this.cuentaThread.start();
	 	}
	}
	
	public void listoActualiza() {
		generaImgJugadores();
		seteaEstadoBotones(true);
		this.contadorTiempo = 5;
		this.movEnviadoLbl.setVisible(false);
		this.tiempoLbl.setText(Integer.toString(this.contadorTiempo));
		this.tiempoLbl.setVisible(true);
		seteathread();
	}
	
	public void arrancaNuevaRonda() {
		this.tiempoLbl.setVisible(false);
		this.movEnviadoLbl.setVisible(false);
		this.tiempoLbl.setVisible(false);		
	}
}