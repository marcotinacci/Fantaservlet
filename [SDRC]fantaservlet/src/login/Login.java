package login;

import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import dataconnection.MySQLConnection;
import entities.UserEntity;

/**
 * classe che gestisce la comunicazione con i dati di login nelle variabili di sessione
 * @author Markov
 *
 */
public class Login {
	private HttpSession session;
	
	public Login(HttpSession session){
		this.session = session;
	}
	
	/**
	 * metodo che esegue il login di un utente
	 * @param nome nome utente
	 * @param password password utente
	 * @throws SQLException sollevata quando la query sql fallisce
	 * @throws BlankLoginInfoException sollevata quando nome utente o password sono lasciati vuoti
	 * @throws WrongLoginInputException sollevata quando nome utente e password non 
	 * corrispondono ai record presenti nel database
	 */
	public void login(String nome, String password) 
		throws SQLException, BlankLoginInfoException, WrongLoginInputException{
		// controlla che siano presenti tutti i dati
		if(nome == null || password == null) throw new BlankLoginInfoException();
		// trim
		nome = nome.trim();
		password = password.trim();
		// controlla che i dati inseriti siano corretti
		if(nome.equalsIgnoreCase("") || password.equalsIgnoreCase(""))
			throw new BlankLoginInfoException();

		// controlla che l'utente sia presente nel database
		MySQLConnection dbc = new MySQLConnection();
		dbc.init();
		UserEntity user = dbc.getUser(nome);
		dbc.destroy();
		// se non esiste il nome utente o se la password è sbagliata
		if(user == null || !user.getPassword().equals(password))
			throw new WrongLoginInputException();
		// se tutte le condizioni sono corrette inserisci i dati nella sessione
		// session.setAttribute("utente", user);
		session.setAttribute("utente", nome);
	}
	
	public void logout(){
		session.removeAttribute("utente");
	}

	public Boolean isLogged(){
		return session.getAttribute("utente") != null;
	}
	
	public String getUser(){
		return (String)session.getAttribute("utente");
	}
}