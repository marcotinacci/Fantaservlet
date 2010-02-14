package dbconnection;

import entities.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Michael
 *
 */
public class DBConnection {
	
	public static void main(String args[]){
		DBConnection dbc = new DBConnection();
		dbc.init();
		System.out.println("utenti: "+dbc.getUsers());
		//System.out.println("Squadre dell'utente 1: "+dbc.getTeams(1));
		//System.out.println("Attaccanti dell'utente 1: "+dbc.getAttPlayers(1));
		dbc.destroy();
	}
	
	private final String driver = "com.mysql.jdbc.Driver";
	protected final String url = "jdbc:mysql://localhost:3306/fsdb";
	protected Properties userInfo = new Properties();
	protected Connection connection;
	protected Statement statement;
	
	public DBConnection(){}
	
	public void init(){
		userInfo.put("user", "root");
		userInfo.put("password", "sdrc");
		
		try {
			// Load the driver. NOT NEEDED in Java 6!
			Class.forName(driver);
			
			// Establish network connection to database.
			connection = DriverManager.getConnection(url,userInfo);
			
			// Create a statement for executing queries.
			statement = connection.createStatement();
			
		} catch(Exception e) {
			System.err.println("Error with connection: " + e);
		}
	}
	
	public void destroy() {
		try {
			connection.close();			
		} catch(Exception e) {
			System.err.println("Error with connection: " + e);
		}		
	}
	/**
	 * Metodo che prende le squadre appartenenti ad un certo utente
	 * @param uid identificatore utente
	 * @return 
	 */
	public ArrayList<Integer> getTeams(int uid){
		ArrayList<Integer> resArray = new ArrayList<Integer>();		
		try 
		{
			String query = "SELECT idSquadra FROM Squadra WHERE Utente_idUtente = " + uid;
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				resArray.add(resultSet.getInt("idSquadra"));
			}
		} catch(Exception e) {
			System.err.println("Error: " + e);
			resArray = null;
		}
		return resArray;
	}
	
	/**
	 * Metodo che restituisce gli attaccanti appartenenti ad un certa Squadra
	 * @param id_team identificatore squadra
	 * @return 
	 */
	public ArrayList<Integer> getAttPlayers(int id_team){
		ArrayList<Integer> resArray = new ArrayList<Integer>();		
		try 
		{
			String query = "SELECT idCalciatore, Cognome, Club FROM Convocazione INNER JOIN Calciatore ON Convocazione.Calciatore_idCalciatore = Calciatore.idCalciatore where Convocazione.Squadra_idSquadra = "+id_team+" AND Calciatore.Ruolo = A";
			ResultSet resultSet = statement.executeQuery(query);
		//	for(int i=0; i)
			while(resultSet.next()) {
				resArray.add(resultSet.getInt("idSquadra"));
			}
		} catch(Exception e) {
			System.err.println("Error: " + e);
			resArray = null;
		}
		return resArray; 
	}
	
	/**
	 * metodo che recupera i dati degli utenti dal database
	 * @return lista utenti
	 */
	public List<User> getUsers(){
		List<User> lu = new ArrayList<User>();
		try{
			String query = "SELECT idUtente, Nome, Password, Admin FROM Utente";
			ResultSet res = statement.executeQuery(query);
			while (res.next()){
				lu.add(new User(res.getString("Nome"),
					res.getString("Password"), res.getBoolean("Admin")));
			}
		}catch(Exception e){
			System.err.println("Error: " + e);
			lu = null;			
		}
		return lu;
	}
}
