package ventanasCliente;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;
import javax.swing.JTable;

import jugador.Jugador;
import jugador.Ranking;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import propiedadesDelJuego.Partida;

import java.awt.ComponentOrientation;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.Font;
import java.awt.Color;

import javax.swing.ScrollPaneConstants;

public class ListaJuegos extends JFrame {
	private static final long serialVersionUID = -4883499042801315273L;
	private JPanel contentPane;
	private JTextField nickTxtField;
	private JButton btnUnirse;
	private JButton btnCambiarNick;
	private JButton btnCambiarPassword;
	private JButton btnCerrarSesion;
	private ArrayList<Partida> listadoPartidas;
	private final JList<String> partidasList = new JList<String>();
	private String partidaSelecionada = "";
	private JLabel lblInfoEspera;
	private JLabel lblInfoEstado;
	private JLabel lblInfoJugadoresReq;
	private JLabel lblInformacion;
	private Principal principal;
	private Ranking ranking;
	private JTable tablaRanking;
	private int partidaMaxJugadores;

	//CONSTRUCTOR--------------------------------------
	public ListaJuegos(ArrayList<Partida> listadoPartidasArg,Ranking rankingArg,Principal principalArg) {
		setTitle("ZombieLand - Seleccion de partida");
		setResizable(false);
		this.listadoPartidas = listadoPartidasArg;
		this.ranking = rankingArg;
		this.principal = principalArg;
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		this.setBounds(100, 100, 800, 550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 35, 150, 81);
		
		partidasList.setDoubleBuffered(true);
		setListaPartidas();
		partidasList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(partidasList);
		partidasList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				mostrarInformacion("");
				if(partidasList.getSelectedIndex() != -1) {//Si hay algo seleccionado.
					partidaSelecionada = listadoPartidas.get(partidasList.getSelectedIndex()).getNombre();
					lblInfoEspera.setText(Integer.toString(listadoPartidas.get(partidasList.getSelectedIndex()).getConectados()));
					lblInfoEstado.setText(listadoPartidas.get(partidasList.getSelectedIndex()).getEstado());
					lblInfoJugadoresReq.setText(Integer.toString(listadoPartidas.get(partidasList.getSelectedIndex()).getJugadoresReq()));
				}
			}
		});

		JLabel lblSeleccionDePartida = new JLabel("PARTIDAS");
		lblSeleccionDePartida.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSeleccionDePartida.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeleccionDePartida.setBounds(15, 10, 150, 14);
		contentPane.add(lblSeleccionDePartida);
		contentPane.add(scrollPane);
		
		btnUnirse = new JButton("Unirse");
		btnUnirse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(partidasList.getSelectedIndex() >= 0) {
					setEnabledGeneral(false);
					principal.recibirMsj("SELECCION_PRINCIPAL_UNIRSE");
				}
			}
		});
		btnUnirse.setBounds(45, 130, 90, 23);
		contentPane.add(btnUnirse);
		
		JLabel lblEstado = new JLabel("Estado");
		lblEstado.setHorizontalTextPosition(SwingConstants.CENTER);
		lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
		lblEstado.setBounds(215, 10, 100, 14);
		contentPane.add(lblEstado);
		
		lblInfoEstado = new JLabel();
		lblInfoEstado.setLabelFor(lblEstado);
		lblInfoEstado.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEstado.setHorizontalTextPosition(SwingConstants.CENTER);
		lblInfoEstado.setBounds(215, 40, 100, 14);
		contentPane.add(lblInfoEstado);

		JLabel lblJugadoresEsperando = new JLabel("Jugadores esperando");
		lblJugadoresEsperando.setBounds(370, 10, 160, 14);
		lblJugadoresEsperando.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblJugadoresEsperando);

		lblInfoEspera = new JLabel();
		lblInfoEspera.setHorizontalTextPosition(SwingConstants.CENTER);
		lblInfoEspera.setBounds(370, 40, 160, 14);
		lblInfoEspera.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoEspera.setLabelFor(lblJugadoresEsperando);
		contentPane.add(lblInfoEspera);
		
		JLabel lblJugadoresReq = new JLabel("Jugadores requeridos");
		lblJugadoresReq.setHorizontalAlignment(SwingConstants.CENTER);
		lblJugadoresReq.setBounds(595, 10, 165, 14);
		contentPane.add(lblJugadoresReq);
		
		lblInfoJugadoresReq = new JLabel();
		lblInfoJugadoresReq.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoJugadoresReq.setBounds(595, 40, 165, 14);
		lblInfoJugadoresReq.setLabelFor(lblJugadoresReq);
		contentPane.add(lblInfoJugadoresReq);
		
		JLabel lblRanking = new JLabel("RANKING");
		lblRanking.setBounds(420, 140, 80, 23);
		lblRanking.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblRanking);
		JScrollPane scrollRanking = new JScrollPane();
		scrollRanking.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		scrollRanking.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollRanking.setBorder(null);	
		scrollRanking.setBounds(165, 180, 595,115);
		
		setearTablaRanking(armarModelo());		
		tablaRanking.setColumnSelectionAllowed(true);
		tablaRanking.setCellSelectionEnabled(true);
		tablaRanking.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		tablaRanking.setRowHeight(17);
		tablaRanking.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		tablaRanking.setAlignmentX(Component.RIGHT_ALIGNMENT);
		scrollRanking.setViewportView(tablaRanking);
		contentPane.add(scrollRanking);
		
		nickTxtField = new JTextField(principal.getCliente().getUsuario());
		nickTxtField.setHorizontalAlignment(SwingConstants.CENTER);
		nickTxtField.setBounds(230, 330, 170, 23);
		contentPane.add(nickTxtField);
		nickTxtField.setColumns(10);
		
		btnCambiarNick = new JButton("Cambiar nick");
		btnCambiarNick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(nickTxtField.getText().isEmpty())
					mostrarInformacion("El nick no puede estar vacìo");
				else if(nickTxtField.getText().contains(";"))
					mostrarInformacion("El nick no puede contener ';'");
				else {
					principal.getYoJugador().setNick(nickTxtField.getText());
					principal.recibirMsj("LISTAJUEGOS_PRINCIPAL_CAMBIO-NICK");
					mostrarInformacion("Tu nick ahora es: "+ nickTxtField.getText());
				}
			}
		});
		btnCambiarNick.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCambiarNick.setBounds(230, 370, 170, 23);
		
		btnCambiarPassword = new JButton("Cambiar password");
		btnCambiarPassword.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCambiarPassword.setBounds(465, 370, 170, 23);
		btnCambiarPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				principal.recibirMsj("LANZAR_CAMBIO_PASS");
			}
		});
		
		contentPane.add(btnCambiarNick);
		contentPane.add(btnCambiarPassword);

		btnCerrarSesion = new JButton("Cerrar sesion");
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listadoPartidas.clear();
				principal.cerrarSesion();
			}
		});
		btnCerrarSesion.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCerrarSesion.setBounds(348, 435, 150, 23);
		contentPane.add(btnCerrarSesion);
		
		lblInformacion = new JLabel("");
		lblInformacion.setForeground(Color.RED);
		lblInformacion.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformacion.setFont(new Font("Dialog", Font.BOLD, 13));
		lblInformacion.setBounds(15, 480, 320, 20);
		contentPane.add(lblInformacion);
	}
	
	//GETTERS & SETTERS-----------------------------------------------------
	public String getPartidaSeleccionada() {
		return this.partidaSelecionada;
	}

	public void setPartidaSeleccionada(String juegoSelecionado) {
		this.partidaSelecionada = juegoSelecionado;
	}
	
	public void setPartidaMaxJugadores(int valor) {
		this.partidaMaxJugadores = valor;
	}

	//OTROS METODOS---------------------------------------------------------
	/**
	 * Actualiza (partidasList) la lista de partidas que se muestra en la ventana
	 */
	public void setListaPartidas() {
		DefaultListModel<String> modelo = new DefaultListModel<String>();
		
		for(int i = 0; i < this.listadoPartidas.size(); i++) {
			modelo.add(i, this.listadoPartidas.get(i).getNombre());
		}
		this.partidasList.setModel(modelo);
		if(!this.partidaSelecionada.equals("")) {
			for (Partida partida : this.listadoPartidas) {
				if(partida.getNombre().equals(this.partidaSelecionada)){
					lblInfoEspera.setText(Integer.toString(partida.getConectados()));
					lblInfoEstado.setText(partida.getEstado());
					lblInfoJugadoresReq.setText(Integer.toString(partida.getJugadoresReq()));
					if(principal.getVentanaEspera() != null)
						principal.getVentanaEspera().setValoresEspera(this.partidaMaxJugadores, partida.getJugadoresReq(), partida.getConectados());
				}
			}
		}
	}
	
	/**
	 * Actualiza los atributos de una partida
	 * @param IDPartida
	 * @param estado
	 * @param jugadoresRequeridos
	 * @param conectados
	 */
	public void actualizarPartida(String IDPartida, String estado, int jugadoresRequeridos, int conectados) {
		for (Partida partida : listadoPartidas) {
			if(partida.getNombre().equals(IDPartida)) {
				partida.setEstado(estado);
				partida.setJugadoresReq(jugadoresRequeridos);
				partida.setConectados(conectados);
			}
		}
	}
	
	/**
	 * Habilita o deshabilita los elementos activos de la ventana
	 * @param estado
	 */
	public void setEnabledGeneral(boolean estado) {
		this.partidasList.setEnabled(estado);
		this.btnCambiarNick.setEnabled(estado);
		this.btnCambiarPassword.setEnabled(estado);
		this.btnCerrarSesion.setEnabled(estado);
		this.btnUnirse.setEnabled(estado);
	}
	
	/**
	 * Actualiza el texto a mostrar en el etiqueta de informaciòn de la ventana
	 * @param info
	 */
	public void mostrarInformacion(String info) {
		this.lblInformacion.setText(info);
	}
	
	/**
	 * Actualiza el modelo de la tablaRanking con los jugadores de la listaRanking
	 * @return
	 */
	public DefaultTableModel armarModelo() {
		String columnNames[] = {"Posici\u00F3n", "Jugador", "Puntaje Total", "Partidas Jugadas", "Partidas Ganadas", "Prom. PPP"};
		@SuppressWarnings("serial")
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		int pos = 0;
		for(Jugador jugador : this.ranking.getListaJugadores()) {
			pos++;
			if(!jugador.getUsuario().equals(this.principal.getCliente().getUsuario()))
				model.addRow(new Object[]{pos, jugador.getUsuario(), jugador.getpuntajeTotal(), jugador.getJugados(), jugador.getGanados(), jugador.getPromPPP()});
			else
				model.addRow(new Object[]{this.principal.getMiPosicionRanking(), jugador.getUsuario(), jugador.getpuntajeTotal(), jugador.getJugados(), jugador.getGanados(), jugador.getPromPPP()});
		}
		return model;
	}
	
	/**
	 * Crea la tabla y la agrega a contentPane
	 * @param model
	 */
	public void setearTablaRanking(DefaultTableModel model) {
		this.tablaRanking = null;
		this.tablaRanking = new JTable(model);
		this.contentPane.add(this.tablaRanking);
	}
}