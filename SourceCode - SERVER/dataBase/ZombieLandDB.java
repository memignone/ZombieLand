package dataBase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.LinkedList;

public class ZombieLandDB {
	private Connection conn;
	
	/**
	 * Establece la conexiòn con la base de datos
	 */
	public ZombieLandDB() {
		conn = MySQLConnection.getConnection();
	}
	
	/**
	 * Si no existe otro registro con el mismo nombre de usuario, crea un nuevo registro
	 * con los datos del nuevo jugador.
	 * @param usr
	 * @param pass
	 * @param preg_secreta
	 * @param respuesta
	 * @return
	 */
	public boolean crearJugadorDB(String usr, String pass, String preg_secreta, String respuesta) {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("INSERT INTO Jugador VALUES (?, ?, ?, ?, 0, 0, 0)");
			pstmt.setString(1, usr);
			pstmt.setString(2, pass);
			pstmt.setString(3, preg_secreta);
			pstmt.setString(4, respuesta);
			pstmt.execute();
			return true;
		} catch(SQLException sqle) {
			return false;//El jugador ya existe
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Si existe un registro con el nombre de usuario ingresado, reemplaza su password.
	 * @param usr
	 * @param pass
	 */
	public void cambiarPassword(String usr, String pass) {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("UPDATE Jugador SET pass = ? WHERE usr = ?");
			pstmt.setString(1, pass);
			pstmt.setString(2, usr);
			pstmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Obtiene de la base de datos el registro completo de un usuario.
	 * @param usr
	 * @return
	 */
	public String[] getJugadorDB(String usr) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String registroJugador[] = new String[7];
		try {
			pstmt = conn.prepareStatement("SELECT * FROM Jugador WHERE usr = ?");
			pstmt.setString(1, usr);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				registroJugador[0] = rs.getString("usr");
				registroJugador[1] = rs.getString("pass");
				registroJugador[2] = rs.getString("pregunta_secreta");
				registroJugador[3] = rs.getString("respuesta_secreta");
				registroJugador[4] = rs.getString("puntaje");
				registroJugador[5] = rs.getString("partidas_jugadas");
				registroJugador[6] = rs.getString("partidas_ganadas");
			}
			return registroJugador;
		} catch(SQLException sqle) {
			return registroJugador;//El jugador ya existe
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Actualiza el puntaje del jugador en la base de datos.
	 * @param usr
	 * @param puntos
	 */
	public void actualizarPuntajeJugador(String usr, int puntos) {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("UPDATE Jugador SET puntaje = puntaje + ?, partidas_jugadas = partidas_jugadas + 1 WHERE usr = ?");
			pstmt.setInt(1, puntos);
			pstmt.setString(2, usr);
			pstmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Aumenta en uno la cantidad de partidas ganadas del jugador en la base de datos.
	 * @param usr
	 */
	public void incrementarPartidasGanadas(String usr) {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("UPDATE Jugador SET partidas_ganadas = partidas_ganadas + 1 WHERE usr = ?");
			pstmt.setString(1, usr);
			pstmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Obtiene los puntajes y la cantidad de partidas ganadas de todos los jugadores
	 * de la base de datos
	 * @return
	 */
	public LinkedList<String[]> listarDatosJugadores() {
		Statement stmt = null;
		ResultSet rs = null;
		LinkedList<String[]> retorno = new LinkedList<String[]>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT usr, puntaje, partidas_jugadas, partidas_ganadas FROM Jugador");
			while (rs.next()) {
				String datosUsr[] = new String[4];
				String usr = rs.getString(1);
				String puntaje = Integer.toString(rs.getInt(2));
				String partidas_jugadas = Integer.toString(rs.getInt(3));
				String partidas_ganadas = Integer.toString(rs.getInt(4));
				datosUsr[0] = usr;
				datosUsr[1] = puntaje;
				datosUsr[2] = partidas_jugadas;
				datosUsr[3] = partidas_ganadas;
				retorno.add(datosUsr);
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}
	
	/**
	 * Verifica que el usuario a loguear exista y que su clave sea la ingresada.
	 * @param usr
	 * @param pass
	 * @return
	 */
	public boolean autenticarJugador(String usr, String pass) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement("SELECT pass FROM Jugador WHERE usr = ?");
			pstmt.setString(1, usr);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if(pass.equals(rs.getString("pass")))	
					return true;
			}
			return false;//Password incorrecta
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			return false;//El jugador no existe
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Cierra la conexiòn con la base de datos.
	 */
	public void desconectar() {
		MySQLConnection.close();
	}
}