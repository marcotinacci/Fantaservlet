package entities;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import utils.GenericUtilities;
import dataconnection.MySQLConnection;

/**
 * Entità utente
 * @author Markov
 */
public class UserEntity{

	// identificativo utente
	private Integer id;
	// nome utente
	private String name;
	// password
	private String password;
	// conferma password
	private String confirm;
	// abilitazione amministratore
	private boolean admin = false;
	
	/**
	 * Costruttore
	 */
	public UserEntity() {}
	
	/**
	 * Costruttore
	 * @param name nome utente
	 * @param password password utente
	 */
	public UserEntity(String name, String password){
		this.name = name;
		this.password = password;
	}
	
	/**
	 * Costruttore
	 * @param name nome utente
	 * @param password password utente
	 * @param admin abilitazione amministrazione
	 */
	public UserEntity(String name, String password, Boolean admin){
		this.name = name;
		this.password = password;
		this.admin = admin;
	}
	
	/**
	 * Costruttore con tutti i dati dell'utente, usato per recuperare i dati dal database
	 * @param id identificativo utente
	 * @param name nome utente
	 * @param password password utente
	 * @param admin abilitazione amministrazione
	 */
	public UserEntity(Integer id, String name, String password,
			Boolean admin) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.admin = admin;
	}

	/**
	 * Getter del nome utente
	 * @return nome utente
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter del nome utente
	 * @param name nome utente
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Setter password
	 * @param password password utente
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Getter permessi amministrazione
	 * @return vero se l'utente è amministratore
	 */
	public boolean isAdmin(){
		return admin;
	}
	
	/**
	 * Setter permessi amministrazione
	 * @param admin
	 */
	public void setAdmin(boolean admin){
		this.admin = admin;
	}
	
	/**
	 * Override metodo equals
	 * @return vero se nome utente e password sono uguali e non nulli
	 */
	@Override
	public boolean equals(Object obj) {
		return name != null && password != null &&
			name.equals(((UserEntity)obj).name) && 
			password.equals(((UserEntity)obj).password);
	}

	/**
	 * Getter password di conferma
	 * @return password di conferma
	 */
	public String getConfirm() {
		return confirm;
	}

	/**
	 * Setter password di conferma
	 * @param confirm password di conferma
	 */
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	
	/**
	 * Controllo correttezza password
	 * @return vero se password e conferma sono uguali e non nulle
	 */
	public boolean isConfirmed(){
		return getPassword() != null && getConfirm() != null && getPassword().equals(getConfirm());
	}
	
	/**
	 * Controllo assenza del nome nel database
	 * @return vero se il nome non è presente
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public boolean isAvailableName() throws SQLException {
		// TODO passaggio connessione a database dall'esterno  
		List<UserEntity> lu = MySQLConnection.getUsers();
		for(Iterator<UserEntity> it = lu.listIterator(); it.hasNext();){
			UserEntity t = it.next();
			if(name.equals(t.getName())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Controllo completezza dei campi fondamentali
	 * @return vero se esistono i valori nome, password e conferma
	 */
	public boolean isComplete(){
		return GenericUtilities.hasValue(getName()) &&
			GenericUtilities.hasValue(getPassword()) &&
			GenericUtilities.hasValue(getConfirm());			
	}

	/**
	 * Getter identificativo utente
	 * @return id utente
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Setter identificativo utente
	 * @param id id utente
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}