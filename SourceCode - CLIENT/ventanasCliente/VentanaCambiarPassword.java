package ventanasCliente;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaCambiarPassword extends JFrame {
	private static final long serialVersionUID = 4138607049715214882L;
	private JPanel contentPane;
	private JPasswordField pwdClaveActual;
	private JLabel lblSuPregSecretaEs;
	private JLabel lblClaveNueva;
	private JPasswordField pwdClaveNueva;
	private JLabel lblPregSecreta;
	private JLabel lblEscribaSuRespusta;
	private JLabel lblError;
	private JTextField txtFieldRespuesta;
	private Principal principal;
	
	//CONSTRUCTOR--------------------------------------------
	public VentanaCambiarPassword(final Principal principal) {
		this.principal = principal;
		principal.setVentanaCambioPassword(this);
		setTitle("ZombieLand - Cambio de password");
		setAlwaysOnTop(true);
		setResizable(false);		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 207);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblClaveActual = new JLabel("Ingrese su clave actual:");
		lblClaveActual.setBounds(15, 10, 170, 15);
		contentPane.add(lblClaveActual);
		
		pwdClaveActual = new JPasswordField();
		pwdClaveActual.setText("");
		pwdClaveActual.setBounds(232, 10, 110, 20);
		contentPane.add(pwdClaveActual);
		
		lblClaveNueva = new JLabel("Ingrese una clave nueva:");
		lblClaveNueva.setBounds(15, 40, 180, 15);
		contentPane.add(lblClaveNueva);
		
		pwdClaveNueva = new JPasswordField();
		pwdClaveNueva.setText("");
		pwdClaveNueva.setBounds(232, 40, 110, 20);
		contentPane.add(pwdClaveNueva);
		
		lblSuPregSecretaEs = new JLabel("Su pregunta secreta es:");
		lblSuPregSecretaEs.setBounds(15, 70, 180, 15);
		contentPane.add(lblSuPregSecretaEs);
		
		lblPregSecreta = new JLabel("");
		lblPregSecreta.setBounds(232, 70, 190, 20);
		contentPane.add(lblPregSecreta);
		
		lblEscribaSuRespusta = new JLabel("Escriba su respuesta:");
		lblEscribaSuRespusta.setBounds(15, 100, 170, 15);
		contentPane.add(lblEscribaSuRespusta);
		
		txtFieldRespuesta = new JTextField();
		txtFieldRespuesta.setToolTipText("");
		txtFieldRespuesta.setBounds(232, 100, 190, 20);
		contentPane.add(txtFieldRespuesta);
		txtFieldRespuesta.setColumns(10);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setBounds(15, 140, 235, 15);
		contentPane.add(lblError);
		
		JButton btnCambiarPass = new JButton("Cambiar clave");
		btnCambiarPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(hayCamposVacios())
					mostrarMsj("Complete todos los campos");
				else if(!validarCampos())
					mostrarMsj("Ninguno de los campos puede contener ';'");
				else
					enviarDatos();
			}
		});
		btnCambiarPass.setBounds(270, 135, 145, 25);
		contentPane.add(btnCambiarPass);
	}	
	
	//OTROS METODOS---------------------------------
	/**
	 * Muestra la pregunta secreta por pantalla
	 * @param text
	 */
	public void setLabels(String text) {
		this.lblPregSecreta.setText(text);
	}
	
	/**
	 * Muestra por pantalla el mnesaje de error
	 * @param txt
	 */
	public void mostrarMsj(String txt) {
		this.lblError.setText(txt);
	}
	
	/**
	 * Envia al servidor el nombre de usuario, clave actual, clave nueva, y respuesta a la pregunta secreta
	 * para cambiar la clave
	 */
	@SuppressWarnings("deprecation")
	public void enviarDatos() {
		principal.getCliente().enviarMensaje("CAMBIO_PASS;"+ this.principal.getCliente().getUsuario() +";"+ this.pwdClaveActual.getText() +";"+ this.pwdClaveNueva.getText() +";"+ this.txtFieldRespuesta.getText());
	}
	
	/**
	 * Verifica si hay campos vacios en la ventana
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean hayCamposVacios() {
		if(pwdClaveActual.getText().isEmpty()
			|| pwdClaveNueva.getText().isEmpty()
				|| txtFieldRespuesta.getText().isEmpty())
			return true;		
		return false;
	}
	
	/**
	 * Valida que ningùn campo contenga el caracter ';'
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean validarCampos() {
		if(pwdClaveActual.getText().contains(";")
			|| pwdClaveNueva.getText().contains(";")
				|| txtFieldRespuesta.getText().contains(";"))
			return false;
		return true;
	}
}