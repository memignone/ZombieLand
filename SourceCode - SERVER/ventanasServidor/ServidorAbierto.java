package ventanasServidor;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import javax.swing.JScrollBar;

import servidor.Manejador;
import servidor.Servidor;
import servidor.ThreadServidor;

import java.awt.Panel;
import java.awt.List;
import java.awt.Label;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

import logicaDelJuego.Partida;
import logicaDelJuego.ThreadCuentaAtras;

@SuppressWarnings("unused")
public class ServidorAbierto extends JFrame {
	/**
	 * ATRIBUTOS
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane partidasPanel;
	private final JList<String> partidasList = new JList<String>();
	private Panel clientesPanel;
	private List clientesList;
	private Label clientesLabel;
	private AgregarPartida agregarPart;
	private JButton button_AgregarPartida;
	private String partida;
	
	/**
	 * referencias y estructuras de datos
	 */
	private ArrayList<Partida> listaDePartidas = new ArrayList<Partida>();
	private Manejador manejador;
	
	public ServidorAbierto(AbrirServidor abrirServidor, Manejador manejador) {
		this.manejador = manejador;
		setTitle("Servidor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 590, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		button_AgregarPartida = new JButton("Agregar Partida");
		button_AgregarPartida.setBounds(426, 15, 147, 25);
		button_AgregarPartida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				/*Abrir ventana de agregar partida*/
				lanzarAgregarPartida();
				
				
			}
		});
		contentPane.setLayout(null);
		contentPane.add(button_AgregarPartida);
		
		partidasPanel = new JScrollPane();
		contentPane.add(partidasPanel);
		partidasPanel.setBounds(30, 69, 223, 247);
		
		partidasList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		partidasList.setDragEnabled(true);
		partidasList.setDoubleBuffered(true);
		partidasPanel.setViewportView(partidasList);
		partidasList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
		        @SuppressWarnings("rawtypes")
				JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2 && list.locationToIndex(evt.getPoint()) != -1) {// Double-click on list detected
		            int index = list.locationToIndex(evt.getPoint());
		            lanzarEstadisticasPartida(index);
		        }
		    }
		});
		
		Label partidasLabel = new Label("Partidas abiertas:");
		partidasLabel.setBounds(30, 42, 120, 21);
		contentPane.add(partidasLabel);
		
		clientesPanel = new Panel();
		clientesPanel.setBounds(315, 69, 251, 247);
		contentPane.add(clientesPanel);
		clientesPanel.setLayout(new BoxLayout(clientesPanel, BoxLayout.X_AXIS));
		
		clientesList = new List();
		clientesPanel.add(clientesList);
		
		clientesLabel = new Label("Clientes Activos:");
		clientesLabel.setBounds(315, 42, 110, 21);
		contentPane.add(clientesLabel);
	
	}
	
	private void lanzarAgregarPartida(){
		agregarPart = new AgregarPartida(this, manejador);
		agregarPart.addWindowListener(new WindowCloseListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				button_AgregarPartida.setEnabled(true);
			}
		});
		agregarPart.setVisible(true);
		button_AgregarPartida.setEnabled(false);
		
	}
	
	private void lanzarEstadisticasPartida(int index) {
		listaDePartidas.get(index).getVentanaEstadisticas().setVisible(true);
	}

	public void setPartida(String partida) {
		this.partida = partida;
	}
	/**
	 * Se manda la partida agregada a los clientes conectados con formato "Nombre;Estado;cantMinima;cantConectados"
	 * 
	 * @param partida
	 */
	public void agregarPartida(Partida partida){
		DefaultListModel<String> modelo = new DefaultListModel<String>();

		listaDePartidas.add(partida);
		for (Partida part : listaDePartidas) {
			modelo.addElement(part.getNombrePartida());
		}
		partidasList.setModel(modelo);
		manejador.getServidor().sendPartida(partida.getNombrePartida()+";"+partida.getEstado()+";"+
							Integer.toString(partida.getCantidadMinima())+";"+ 
					        Integer.toString(partida.getCantConectados()));
	}
	
	public boolean existePartida(String nombreDePartida){
		for (Partida partida : listaDePartidas) {
			if(partida.getNombrePartida().equals(nombreDePartida)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Se obtienen todas las partidas creadas para enviarselas al cliente que recien se conecto.
	 * @return String[] con las lineas de formato "Nombre;Estado;cantMinima;cantConectados"
	 * 			o NULL si no hay partidas creadas.
	 * @author Fernando
	 * 
	 */
	public String[] obtenerPartidas(){
		String[] partidas = null;
		int i = 0;
		if(!listaDePartidas.isEmpty()){
			partidas = new String[listaDePartidas.size()];
			for (Partida partida : listaDePartidas) {
				partidas [i]= ";" + partida.getNombrePartida()+";"+partida.getEstado()+";"+
						Integer.toString(partida.getCantidadMinima())+";"+ 
				        Integer.toString(partida.getCantConectados());
				i++;
			}
		}
		return partidas;
	}
	
	/**
	 * Le llega el String de identificacion de cliente para eliminarlo de la lista
	 * @param identificadorDeCliente
	 * @author Fernando
	 */
	public void eliminasClienteDeLista(String identificadorDeCliente){
		clientesList.remove(identificadorDeCliente);
	}
	/**
	 * Le llega el String de identificacion de cliente para agregarlo de la lista
	 * @param identificadorDeCliente
	 * @author Fernando
	 */
	public void agregarClienteALALista(String identificadorDeCliente) {
		clientesList.add(identificadorDeCliente);
	}
	
	/**
	 * Obtenemos la lista de partidas para fijarnos cuantas partidas hay
	 * 
	 * @return ArrayList<Partidas>
	 */
	public ArrayList<Partida> getListaDePartidas() {
		return listaDePartidas;
	}
	
	
	/**
	 * valida si se puede unir y le envia el cliente para que lo agrege a su lista 
	 * @param threadServidor cliente que se une a la partida
	 * @param partidaSeleccionada nombre de la aprtida
	 */
	public void unirseAPartida(String nombrePartida, ThreadServidor cliente) {
		ThreadCuentaAtras threadCuentaatras;
		for (Partida partida : listaDePartidas) {
			if(partida.getNombrePartida().equals(nombrePartida)){
				if(partida.getEstado().equals("En espera") && 
						partida.getCantConectados()<=partida.getCantidadMaxima()){
					partida.aceptarCliente(cliente);
					if(partida.getCantConectados()==partida.getCantidadMinima()){
						threadCuentaatras = new ThreadCuentaAtras (5, partida);
						threadCuentaatras.start();
					}
				}else{
					cliente.enviarMensaje("ERROR_AL_UNIRSE");
				}
			}
		}
	}
	/**
	 * busca en la lista de partidas al jugador caido y lo saca de la lista de los clientes activos de
	 * esa partida
	 * @param threadServidor
	 */
	public void eliminarClienteDeLaListaDeLaPartidas(ThreadServidor threadServidor) {
		for (Partida partida : listaDePartidas) {
			if(partida.getClientesActivos().remove(threadServidor)){
				partida.descontarCliente(threadServidor);
			}
		}
	}
	/**
	 * acumula los mensaje de los movimientos que resive el servidor para pasarcelo a la logica
	 * del juego
	 * @param movimiento
	 * @param cliente
	 */
	public void mandarMovimientoALaPartida(String movimiento, ThreadServidor cliente) {
		for (Partida partida : listaDePartidas) {
			if(partida.getClientesActivos().contains(cliente)){
				partida.recibirMovimiento(cliente.getUsuario(), movimiento);
			}
		}		
	}

	public void elminarCienteDePartidaEnEspera(ThreadServidor cliente) {
		for (Partida partida : listaDePartidas) {
			if(partida.getClientesActivos().contains(cliente)){
				partida.descontarCliente(cliente);
			}
		}
		
	}

	public void desconectarJugadorEnJuego(ThreadServidor cliente) {
		for (Partida partida : listaDePartidas) {
			if(partida.getClientesActivos().contains(cliente)){
				partida.descontarClienteDePartidaEnCurso(cliente);
			}
		}
	}

}
