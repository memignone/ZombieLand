package ventanasServidor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import servidor.Manejador;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import logicaDelJuego.Partida;

public class AgregarPartida extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField cantJugadoresMinimaField;
	private JTextField nombrePartidaField;
	private JTextField cantJugadoresMaximaField;
	
	
	public AgregarPartida(final ServidorAbierto servidorAbi, final Manejador manejador) {
		setTitle("Agregar Partida");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 378, 314);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JButton boton_Agregar = new JButton("Agregar");
		boton_Agregar.addActionListener(new ActionListener() {/*Boton agregar le mando el contenido de los campos*/
			public void actionPerformed(ActionEvent e) {		/*Si no estan vacios*/
				if(nombrePartidaField.getText().length()==0
						||cantJugadoresMinimaField.getText().length()==0
						||cantJugadoresMaximaField.getText().length()==0)
					JOptionPane.showMessageDialog(null, "Hay capos vacios");
				else 
				 {
					if(servidorAbi.existePartida(nombrePartidaField.getText())){
							JOptionPane.showMessageDialog(null, "Nombre de partida repetida");
					}
					else{
						if(servidorAbi.getListaDePartidas().size()<5){
							servidorAbi.agregarPartida(new Partida(Integer.parseInt(cantJugadoresMinimaField.getText()), 
									   Integer.parseInt(cantJugadoresMaximaField.getText()), 
									   nombrePartidaField.getText(), manejador));
							dip();
						}
						else
							JOptionPane.showMessageDialog(null,"Maximo de partidas alcanzadas");
						
						}
				}
			}
		});
		
		sl_contentPane.putConstraint(SpringLayout.WEST, boton_Agregar, 33, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, boton_Agregar, -41, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, boton_Agregar, 138, SpringLayout.WEST, contentPane);
		contentPane.add(boton_Agregar);
		
		JButton boton_Cancelar = new JButton("Cancelar");
		boton_Cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dip();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, boton_Cancelar, 0, SpringLayout.NORTH, boton_Agregar);
		sl_contentPane.putConstraint(SpringLayout.WEST, boton_Cancelar, 184, SpringLayout.WEST, boton_Agregar);
		sl_contentPane.putConstraint(SpringLayout.EAST, boton_Cancelar, -34, SpringLayout.EAST, contentPane);
		contentPane.add(boton_Cancelar);
		
		JLabel CantJugadoresLabel = new JLabel("Cantidad De Jugadores Minima:");
		sl_contentPane.putConstraint(SpringLayout.WEST, CantJugadoresLabel, 0, SpringLayout.WEST, boton_Agregar);
		contentPane.add(CantJugadoresLabel);
		
		cantJugadoresMinimaField = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, cantJugadoresMinimaField, 9, SpringLayout.SOUTH, CantJugadoresLabel);
		sl_contentPane.putConstraint(SpringLayout.WEST, cantJugadoresMinimaField, 0, SpringLayout.WEST, boton_Agregar);
		contentPane.add(cantJugadoresMinimaField);
		cantJugadoresMinimaField.setColumns(10);
		
		JLabel nombreLabel = new JLabel("Nombre De La Partida:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, nombreLabel, 25, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, nombreLabel, 0, SpringLayout.WEST, boton_Agregar);
		contentPane.add(nombreLabel);
		
		nombrePartidaField = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, nombrePartidaField, 33, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, CantJugadoresLabel, 6, SpringLayout.SOUTH, nombrePartidaField);
		sl_contentPane.putConstraint(SpringLayout.NORTH, nombrePartidaField, 6, SpringLayout.SOUTH, nombreLabel);
		contentPane.add(nombrePartidaField);
		nombrePartidaField.setColumns(10);
		
		JLabel cantJugadores2Label = new JLabel("Cantidad De Jugadores Maxima:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, cantJugadores2Label, 6, SpringLayout.SOUTH, cantJugadoresMinimaField);
		sl_contentPane.putConstraint(SpringLayout.WEST, cantJugadores2Label, 0, SpringLayout.WEST, boton_Agregar);
		contentPane.add(cantJugadores2Label);
		
		cantJugadoresMaximaField = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, cantJugadoresMaximaField, 6, SpringLayout.SOUTH, cantJugadores2Label);
		sl_contentPane.putConstraint(SpringLayout.WEST, cantJugadoresMaximaField, 0, SpringLayout.WEST, boton_Agregar);
		contentPane.add(cantJugadoresMaximaField);
		cantJugadoresMaximaField.setColumns(10);
	}
	
	/**
	 * Metodo que hace el dispose de la ventana AgregarPartida
	 */
	private void dip(){
		this.dispose();
	}
}
