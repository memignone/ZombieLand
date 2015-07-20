package ventanasCliente;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import cliente.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Color;

public class Login extends JFrame {
	private static final long serialVersionUID = 6359034838640026835L;
	private JButton btnConectar;
	private JButton btnSoyNuevo;
	private JPanel contentPane;
	private JTextField ipField;
	private JTextField puertoField;
	private JTextField nombreDeUsuarioField;
	private JPasswordField contraseniaField;
	private JLabel mensajeErrorLabel;
	private Principal principal;
	private static final String PATTERN = 
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	//LANZAR APLICACION---------------------------------------
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	 //GETTERS & SETTERS--------------------------------------
	public Principal getPrincipal() {
		return this.principal;
	}
	
	//CONSTRUCTOR---------------------------------------------
	public Login() {
		setTitle("ZombieLand - LogIn");		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 446, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);
		
		ipField = new JTextField();
		ipField.setBounds(80, 54, 159, 19);
		contentPane.add(ipField);
		ipField.setColumns(10);
		
		JLabel ipLabel = new JLabel("IP del Servidor:");
		ipLabel.setBounds(80, 32, 107, 15);
		contentPane.add(ipLabel);
		
		JLabel puertoLabel = new JLabel("Puerto:");
		puertoLabel.setBounds(80, 79, 53, 15);
		contentPane.add(puertoLabel);
		
		puertoField = new JTextField();
		puertoField.setBounds(80, 100, 114, 19);
		contentPane.add(puertoField);
		puertoField.setColumns(10);
		
		JLabel nombreDeUsuarioLabel = new JLabel("Nombre de usuario:");
		nombreDeUsuarioLabel.setBounds(80, 125, 140, 15);
		contentPane.add(nombreDeUsuarioLabel);
		
		nombreDeUsuarioField = new JTextField();
		nombreDeUsuarioField.setBounds(80, 146, 155, 19);
		contentPane.add(nombreDeUsuarioField);
		nombreDeUsuarioField.setColumns(10);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setBounds(80, 171, 88, 15);
		contentPane.add(lblContrasea);
		
		contraseniaField = new JPasswordField();
		contraseniaField.setBounds(80, 192, 155, 19);
		contentPane.add(contraseniaField);
		contraseniaField.setColumns(10);
		
		btnConectar = new JButton("Conectar");
		btnConectar.setBounds(100, 250, 110, 25);
		btnConectar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				try {
					crearPrincipal();
					if(!validate(ipField.getText())) {
						mostrarMensaje("IP invalida");
					} else {
						if(mensajeErrorLabel.getText()=="IP invalida") {
							mostrarMensaje("");
						}
						if( (Integer.parseInt(puertoField.getText()) < 1024 ||
								Integer.parseInt(puertoField.getText()) > 65535)) {
							mostrarMensaje("Puerto invalido");					
						} else {
							if(mensajeErrorLabel.getText() == "Puerto invalido") {
								mostrarMensaje("");
							}
							if(nombreDeUsuarioField.getText() != "" && contraseniaField.getText() != "" ) {								
								// se toma de la pantalla los parametros de conexion y se crea el cliente el cual es un atributo de la clase Principal
								setEnabledGeneral(false);
								principal.setCliente( new Cliente(ipField.getText() ,Integer.parseInt(puertoField.getText()), principal, nombreDeUsuarioField.getText()) );
								if(principal.getCliente().conectar()){ // eventualmente este conectar validaria si es o no usuario del sistema	
									if(!principal.getCliente().autenticarUsuario(nombreDeUsuarioField.getText(),contraseniaField.getText())) {
										mostrarMensaje("Usuario y/o contrase�a invalidos");
										principal.getCliente().desconectar();
									} else {
										/**
										 *si esta todo bien y el usuario fue indentificado, a principal se le setea el thread de entrada generado por 
										 *el cliente y ademas a al tread se le setea principal para que desde el se puedan llamar a los metodos
										 *de principal
										 */
										principal.recibirMsj("LOGIN_PRINCIPAL_SELECCION-APERTURA");
									}
								}
							}
						}
					}
				}catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});
		contentPane.add(btnConectar);
		
		mensajeErrorLabel = new JLabel("");
		mensajeErrorLabel.setForeground(Color.RED);
		mensajeErrorLabel.setBounds(15, 285, 400, 20);
		contentPane.add(mensajeErrorLabel);
		
		btnSoyNuevo = new JButton("Soy Nuevo");
		btnSoyNuevo.setBounds(270, 250, 110, 25);
		btnSoyNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarMensaje(" ");
				if(!ipField.getText().isEmpty() && !puertoField.getText().isEmpty())
					lanzarVentanaNuevoJugador();
				else
					mostrarMensaje("Llenà la IP y puerto, maquinola!");
			}
		});
		contentPane.add(btnSoyNuevo);
	}
	
	//OTROS METODOS--------------------------------------------------
	/**
	 * Crea el objeto principal y le setea referencia a la ventana LogIn
	 */
	private void crearPrincipal() {
		this.principal = new Principal(this);	
		this.principal.setLogin(this);
	}
	
	/**
	 * Establece el mensaje que se muestra en el label de informaciòn
	 * @param msj: String con mensaje
	 */
	public void mostrarMensaje(String msj) {
		mensajeErrorLabel.setText(msj);
	}
	
	/**
	 * Blanquea los label de la ventana al momento de volver a LogIn desde una sesiòn abierta
	 */
	public void limpiaLogin() {
		this.contraseniaField.setText("");
		this.nombreDeUsuarioField.setText("");
		mostrarMensaje("");
	}
	
	/**
	 * Habilita o deshabilita los elementos activos de la ventana
	 * @param estado
	 */
	public void setEnabledGeneral(boolean estado) {
		ipField.setEnabled(estado);
		puertoField.setEnabled(estado);
		nombreDeUsuarioField.setEnabled(estado);
		contraseniaField.setEnabled(estado);
		btnConectar.setEnabled(estado);
		btnSoyNuevo.setEnabled(estado);
	}
	
	/**
	 * Valida el formato de la direcciòn IP
	 * @param ip
	 * @return
	 */
	public static boolean validate(final String ip) {
	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();
	}
	
	/**
	 * Lanza la ventana de creaciòn de un nuevo jugador.
	 */
	private void lanzarVentanaNuevoJugador() {
		crearPrincipal();
		if(!validate(ipField.getText())) {
			mostrarMensaje("IP invalida");
		}
		else if( (Integer.parseInt(puertoField.getText()) < 1024
				|| Integer.parseInt(puertoField.getText()) > 65535))
				mostrarMensaje("Puerto invalido");
		else {
			NuevoJugador nuevoJugador = new NuevoJugador(principal);
			final Cliente cliente = new Cliente(ipField.getText(), Integer.parseInt(puertoField.getText()), principal, nombreDeUsuarioField.getText());
			if(cliente.conectar()) {
				principal.setCliente(cliente);
				principal.setNuevoJugador(nuevoJugador);
				nuevoJugador.addWindowListener(new WindowCloseListener() {
					@Override
					public void windowClosed(WindowEvent e) {
						setEnabledGeneral(true);
						cliente.desconectar();
					}
				});
				nuevoJugador.setVisible(true);
				setEnabledGeneral(false);
			}
		}
	}
}