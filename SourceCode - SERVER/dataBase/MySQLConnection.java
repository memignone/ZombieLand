package dataBase;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {
	private static Connection conn;
	
	private MySQLConnection() {
	}
	
	public static Connection getConnection() {
		try {
			if(conn == null) {
				String driver="com.mysql.jdbc.Driver"; //el driver varia segun la BD que usemos
				String url="jdbc:mysql://localhost/ZombieLand?autoReconnect=true";
				String pwd="123456";
				String usr="root";
				Class.forName(driver);
				conn = DriverManager.getConnection(url,usr,pwd);
				System.out.println("Se realiz� la conexi�n con �xito");
			}
			else
				System.out.println("La conexi�n se encuentra realizada.");
		} catch (ClassNotFoundException cnfe) {
			System.err.println("No se encuentra el Driver.");
		} catch (SQLException sqle) {
			System.err.println("Error al intentar la conexi�n.");
		}
		return conn;
	}
	
	public static void close() {
		try {
			if(conn != null) {
				conn.close();
				System.out.println("Desconexi�n de la BD exitosa.");
			}
		} catch (SQLException sqle) {
			System.err.println("Error al intentar la desconexi�n.");
		}
	}
}