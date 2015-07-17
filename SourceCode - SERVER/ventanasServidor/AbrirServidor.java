package ventanasServidor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import java.awt.GridLayout;

import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JButton;

import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JFormattedTextField;

import servidor.Manejador;
import servidor.Servidor;
import servidor.ThreadServidor;

import javax.swing.SwingConstants;

@SuppressWarnings("unused")
public class AbrirServidor extends JFrame {

	/**
	 * variables miembro
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textPuerto;
	private Servidor server;
	private JLabel lblMensaje;
	private JButton button_AbrirServidor;
	private ServidorAbierto servidorAbi;
	private Manejador manejador;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AbrirServidor frame = new AbrirServidor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AbrirServidor() {
		/**
		 * asignamos el manejador
		 */
		manejador = new Manejador(this);
		setTitle("Abrir servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		button_AbrirServidor = new JButton("Abrir servidor");
		sl_contentPane.putConstraint(SpringLayout.WEST, button_AbrirServidor, 136, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, button_AbrirServidor, -152, SpringLayout.EAST, contentPane);
		button_AbrirServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					try {
						if( Integer.parseInt(textPuerto.getText()) > 1024 &&
							Integer.parseInt(textPuerto.getText()) < 65535){
							server = new Servidor(Integer.parseInt(textPuerto.getText()),manejador);
							if(server.getSkSrvr()!=null){
								server.start();
								/*abrir ventana servidor abierto*/
								lblMensaje.setVisible(false);
								lanzarServidorAbierto();
							}else{
								lblMensaje.setText("Fallo al hacer el bind");
								lblMensaje.setVisible(true);
							}							
						}else{
							lblMensaje.setText("El puerto debe estar entre 1025 y 65535");
							lblMensaje.setVisible(true);
						}
					} catch (NumberFormatException e) {
						lblMensaje.setText("Error solo se aceptan caracteres numericos");
						lblMensaje.setVisible(true);
					}
			}

		});
		contentPane.add(button_AbrirServidor);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblPuerto, 27, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblPuerto, 136, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, button_AbrirServidor, 82, SpringLayout.SOUTH, lblPuerto);
		contentPane.add(lblPuerto);
		
		
		
		textPuerto = new JTextField();
		
		
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, textPuerto, 23, SpringLayout.SOUTH, lblPuerto);
		sl_contentPane.putConstraint(SpringLayout.WEST, textPuerto, 0, SpringLayout.WEST, button_AbrirServidor);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textPuerto, 45, SpringLayout.SOUTH, lblPuerto);
		sl_contentPane.putConstraint(SpringLayout.EAST, textPuerto, 0, SpringLayout.EAST, button_AbrirServidor);
		contentPane.add(textPuerto);
		
		lblMensaje = new JLabel("mensaje");
		lblMensaje.setVerticalAlignment(SwingConstants.TOP);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMensaje, 37, SpringLayout.SOUTH, button_AbrirServidor);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblMensaje, 45, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblMensaje, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblMensaje, -55, SpringLayout.EAST, contentPane);
		lblMensaje.setVisible(false);
		contentPane.add(lblMensaje);
		
		
	}

	/**
	 * setea la ventana en no visible
	 */
	private void esconder() {
		this.setVisible(false);		
	}
	
	/**
	 * setea la ventana en visible
	 */
	private void mostrar() {
		this.setVisible(true);		
	}
	
	/**
	 * Crea la ventana del servidor abierto y levanta el WindowListener de AbrirServidor.
	 * Cuando se cierra la ventana del servidor abierto, se cierra el socket.
	 */
	private void lanzarServidorAbierto(){
		servidorAbi = new ServidorAbierto(this , manejador);
		manejador.setServidorAbierto(servidorAbi);
		manejador.setServidor(server);
		servidorAbi.addWindowListener(new WindowCloseListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				//button_AbrirServidor.setEnabled(true);
				mostrar();
				server.kill();
				server.cerrarSocket();
				/*NECESITO LIBERAR EL SOCKET*/
			}
		});
		servidorAbi.setVisible(true);
		//button_AbrirServidor.setEnabled(false);
		esconder();
	}

}
