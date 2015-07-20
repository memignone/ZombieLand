package ventanasCliente;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JProgressBar;

public class VentanaDeEspera extends JFrame {
	private static final long serialVersionUID = 8211849054393988002L;
	private JPanel contentPane;
	private JLabel lblPartida;
	private JProgressBar progressBar;
	private JLabel lblCantActNum;
	private JLabel lblCantMaxNum;
	private JLabel lblCantMin;
	private JLabel lblCantMinNum;

	//CONSTRUCTOR--------------------------------------------
	public VentanaDeEspera(Principal principal, String partida) {
		principal.setVentanaEspera(this);
		setTitle("ZombieLand - Esperando...");
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 185);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblPartida = new JLabel("Partida: "+ partida);
		lblPartida.setFont(new Font("Dialog", Font.BOLD, 13));
		lblPartida.setHorizontalAlignment(SwingConstants.CENTER);
		lblPartida.setBounds(15, 5, 410, 20);
		contentPane.add(lblPartida);
		
		JLabel lblCantMax = new JLabel("Cantidad maxima");
		lblCantMax.setBounds(280, 50, 125, 15);
		contentPane.add(lblCantMax);
		
		lblCantMaxNum = new JLabel("");
		lblCantMaxNum.setLabelFor(lblCantMax);
		lblCantMaxNum.setHorizontalAlignment(SwingConstants.LEFT);
		lblCantMaxNum.setBounds(350, 75, 60, 15);
		contentPane.add(lblCantMaxNum);
		
		JLabel lblCantAct = new JLabel("Cantidad actual");
		lblCantAct.setBounds(45, 50, 125, 15);
		contentPane.add(lblCantAct);
		
		lblCantActNum = new JLabel("");
		lblCantActNum.setLabelFor(lblCantAct);
		lblCantActNum.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCantActNum.setBounds(40, 75, 60, 15);
		contentPane.add(lblCantActNum);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(110, 75, 220, 17);
		contentPane.add(progressBar);
		
		lblCantMin = new JLabel("Cantidad minima:");
		lblCantMin.setHorizontalAlignment(SwingConstants.LEFT);
		lblCantMin.setBounds(135, 120, 130, 15);
		contentPane.add(lblCantMin);
		
		lblCantMinNum = new JLabel("");
		lblCantMinNum.setLabelFor(lblCantMin);
		lblCantMinNum.setHorizontalAlignment(SwingConstants.LEFT);
		lblCantMinNum.setBounds(270, 120, 50, 15);
		contentPane.add(lblCantMinNum);
	}
	
	//OTROS METODOS---------------------------------------------
	/**
	 * Establece los valores de las label de la ventana de forma dinàmica
	 * para poder actualizarlos
	 * @param max: cantidad màxima de jugadores permitidos para la partida
	 * @param min: cantidad mìnima de jugadores necesarios para comenzar la partida 
	 * @param valorActual: cantidad actual de jugadores conectados a la partida
	 */
	public void setValoresEspera(int max, int min, int valorActual) {
		progressBar.setValue(valorActual);
		lblCantActNum.setText(Integer.toString(valorActual));
		progressBar.setMaximum(max);
		lblCantMaxNum.setText(Integer.toString(max));
		lblCantMinNum.setText(Integer.toString(min));
	}
}