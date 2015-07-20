package ventanasCliente;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class NuevoJugador extends JFrame {
	private static final long serialVersionUID = 1981710376176489240L;
	private JPanel principalJPanel;
	private JTextField nomTextField;
	private JLabel lblIngreseSuClave;
	private JTextField claveTextField;
	private JLabel lblPregunta;
	private JTextField preguntaTextField;
	private JLabel lblRespuesta;
	private JTextField respuestaTextField;
	private JButton btnRegistrar;
	private JLabel lblError;
	private JLabel lblReingrese;
	private JTextField reingresoTextField;

	//CONSTRUCTOR-----------------------------------------
	public NuevoJugador(final Principal principal) {
		setAlwaysOnTop(true);
		this.setTitle("Crear Jugador Nuevo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		this.setResizable(false);
		
		principalJPanel = new JPanel();
		principalJPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(principalJPanel);
		principalJPanel.setLayout(null);
		
		nomTextField = new JTextField();
		nomTextField.setBounds(15, 35, 180, 20);
		principalJPanel.add(nomTextField);
		nomTextField.setColumns(10);
		
		JLabel lblIngreseSuNick = new JLabel("Ingrese su nombre o nick:");
		lblIngreseSuNick.setFont(new Font("Dialog", Font.BOLD, 12));
		lblIngreseSuNick.setBounds(15, 10, 190, 14);
		principalJPanel.add(lblIngreseSuNick);
		
		lblIngreseSuClave = new JLabel("Ingrese su contraseña:");
		lblIngreseSuClave.setBounds(15, 80, 180, 14);
		principalJPanel.add(lblIngreseSuClave);
		
		claveTextField = new JPasswordField();
		claveTextField.setBounds(15, 105, 180, 20);
		principalJPanel.add(claveTextField);
		claveTextField.setColumns(10);
		
		lblPregunta = new JLabel("Ingrese su pregunta secreta:");
		lblPregunta.setHorizontalAlignment(SwingConstants.LEFT);
		lblPregunta.setBounds(250, 40, 220, 14);
		principalJPanel.add(lblPregunta);
		
		preguntaTextField = new JTextField();
		preguntaTextField.setBounds(250, 65, 210, 20);
		principalJPanel.add(preguntaTextField);
		preguntaTextField.setColumns(10);
		
		lblRespuesta = new JLabel("Ingrese la respuesta:");
		lblRespuesta.setHorizontalAlignment(SwingConstants.LEFT);
		lblRespuesta.setBounds(250, 110, 210, 14);
		principalJPanel.add(lblRespuesta);
		
		respuestaTextField = new JTextField();
		respuestaTextField.setBounds(250, 135, 210, 20);
		principalJPanel.add(respuestaTextField);
		respuestaTextField.setColumns(10);
		
		btnRegistrar = new JButton("Crear");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Se validan los datos y luego se envian al servidor para crear el nuevo usuario.
				if(hayCamposVacios())
					mostrarMensaje("Complete los campos vacios.");
				else if(!validarCampos())
					mostrarMensaje("No incluya ';' en ninguno de los campos.");
				else if(!claveTextField.getText().equals(reingresoTextField.getText()))
					mostrarMensaje("Atenciòn! La clave no coincide con el reingreso.");
				else {//Si no hay errores.
					principal.recibirMsj("CREAR_NUEVO_JUGADOR");
				}
			}
		});
		btnRegistrar.setBounds(190, 215, 90, 23);
		principalJPanel.add(btnRegistrar);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setBounds(15, 240, 450, 14);
		principalJPanel.add(lblError);
		
		lblReingrese = new JLabel("Reingrese su clave:");
		lblReingrese.setHorizontalAlignment(SwingConstants.LEFT);
		lblReingrese.setBounds(15, 150, 180, 14);
		principalJPanel.add(lblReingrese);
		
		reingresoTextField = new JPasswordField();
		reingresoTextField.setBounds(15, 175, 180, 20);
		principalJPanel.add(reingresoTextField);
		reingresoTextField.setColumns(10);
	}
	
	//GETTERS & SETTERS---------------------------------
	public String getNombreDeUsuario() {
		return nomTextField.getText();
	}

	public String getPassword() {
		return claveTextField.getText();
	}

	public String getPregunta() {
		return preguntaTextField.getText();
	}

	public String getRespuesta() {
		return respuestaTextField.getText();
	}
	
	//OTROS METODOS-------------------------------------
	/**
	 * Verifica si hay campos vacios en la ventana
	 * @return
	 */
	private boolean hayCamposVacios() {
		if(nomTextField.getText().isEmpty()
			|| claveTextField.getText().isEmpty()
				|| reingresoTextField.getText().isEmpty()
					|| preguntaTextField.getText().isEmpty()
						|| respuestaTextField.getText().isEmpty())
			return true;		
		return false;
	}
	
	/**
	 * Verifica que ningùn campo tenga ';' en su contenido
	 * @return
	 */
	private boolean validarCampos() {
		if(nomTextField.getText().contains(";")
			|| claveTextField.getText().contains(";")
				|| reingresoTextField.getText().contains(";")
					|| preguntaTextField.getText().contains(";")
						|| respuestaTextField.getText().contains(";"))
			return false;
		return true;
	}
	
	/**
	 * Establece el mensaje a mostrar en el label de informe de errores
	 * @param error
	 */
	public void mostrarMensaje(String error) {
		this.lblError.setText(error);
	}
}