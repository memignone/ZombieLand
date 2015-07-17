package ventanasServidor;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class EstadisticasPartida extends JFrame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextPane textPane;
	private JScrollPane scrollPane;
	/**
	 * Create the frame.
	 */
	public EstadisticasPartida() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 430, 250);
		
		contentPane.add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		this.setResizable(false);
	}

	public void escribirLineaDeDatos(String linea){
		this.textPane.setText(this.textPane.getText() + linea + "\n");
		
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {	
			@Override
			public void adjustmentValueChanged(AdjustmentEvent evento) {
				evento.getAdjustable().setValue(evento.getAdjustable().getMaximum());
			}
		});
	}
}
