package dataconnection;

import entities.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.sql.PreparedStatement;

import utils.Pair;

// TODO modificare i nomi dei metodi in lowerChamel (nomeMetodo)

/**
 * @author Michael
 * Questa classe offre i metodi di interazione col database mysql (insert, select, delete)
 */
public class MySQLConnection {
	
	// driver mysql tramite Connector/J
	private final String driver = "com.mysql.jdbc.Driver";
	// percorso del database
	// TODO definire tramite file di configurazione
	protected final String url = "jdbc:mysql://localhost:3306/fsdb";
	// informazioni per l'accesso al database
	protected Properties userInfo = new Properties();
	// connessione di rete al database
	protected Connection connection;
	// statement per l'esecuzione delle query
	protected PreparedStatement preparedStatement;
	
	public MySQLConnection(){}
	
	/**
	 * Inizializzazione della connessione al database 
	 */
	public void init(){
		// definisci i dati di accesso al database
		// TODO definire tramite file di configurazione
		userInfo.put("user", "root");
		userInfo.put("password", "");
		try {
			// Carica il driver, non è necessario in Java 6
			Class.forName(driver);
			// Stabilisci la connessione al database
			connection = DriverManager.getConnection(url,userInfo);
		} catch(Exception e) {
			System.err.println("Errore di connessione: " + e);
		}
	}
	
	/**
	 * Chiusura della connessione al database
	 */
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
	 * @return lista di identificativi delle squadre
	 */
	public ArrayList<Integer> getTeams(int uid){
		ArrayList<Integer> resArray = new ArrayList<Integer>();		
		try 
		{
			String query = "SELECT idSquadra FROM Squadra WHERE Utente_idUtente = " + uid;
			ResultSet resultSet = preparedStatement.executeQuery(query);
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
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<UserEntity> getUsers() throws SQLException{
		List<UserEntity> lu = new ArrayList<UserEntity>();
		preparedStatement = connection.prepareStatement(
			"SELECT idUtente, Nome, Password, Admin FROM Utente");
		ResultSet res = preparedStatement.executeQuery();
		while (res.next()){
			lu.add(new UserEntity(res.getString("Nome"),
				res.getString("Password"), res.getBoolean("Admin")));
		}
		return lu;
	}
	
	/**
	 * metodo che inserisce i dati di un utente nel database se il nome non è già presente
	 * @param user utente da inserire
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void InsertUser(UserEntity user) throws SQLException{
		preparedStatement = connection.prepareStatement(
			"INSERT INTO Utente(Nome,Password,Admin) VALUES (?,?,?)");
		preparedStatement.setString(1, user.getName());
		preparedStatement.setString(2, user.getPassword());
		preparedStatement.setBoolean(3, user.isAdmin());		
		preparedStatement.executeUpdate();
	}

	/**
	 * metodo che restituisce i campionati
	 * @return lista campionati
	 * @throws SQLException sollevata se l'esecuzione della query fallisce
	 */
	public List<ChampionshipEntity> getChampionships() throws SQLException {
		List<ChampionshipEntity> lc = new ArrayList<ChampionshipEntity>();
		String query = "SELECT idCampionato, Nome FROM Campionato";
		preparedStatement = connection.prepareStatement(query);
		ResultSet res = preparedStatement.executeQuery();
		while (res.next()){
			lc.add(new ChampionshipEntity(res.getInt("idCampionato"),res.getString("Nome")));
		}
		return lc;
	}

	/**
	 * metodo che restituisce i calciatori
	 * @return lista calciatori
	 */
	public List<PlayerEntity> getPlayers() {
		List<PlayerEntity> lp = new ArrayList<PlayerEntity>();
		try{
			String query = "SELECT idCalciatore, Nome, Ruolo, Squadra FROM Calciatore";
			preparedStatement = connection.prepareStatement(query);
			ResultSet res = preparedStatement.executeQuery();
			while (res.next()){
				lp.add(new PlayerEntity(res.getInt("idCalciatore"),res.getString("Nome"),
					res.getString("Ruolo").charAt(0),res.getString("Squadra")));
			}
		}catch(Exception e){
			System.err.println("Error: " + e);
			lp = null;			
		}
		return lp;
	}
	
	/**
	 * metodo che inserisce i dati di un calciatore nel database se il nome non è già presente
	 * @param player calciatore da inserire
	 * @throws SQLException eccezione lanciata in caso di violazione del vincolo UNIQUE sui campi Nome e Squadra
	 */
	public void InsertPlayer(PlayerEntity player) throws SQLException{
		// usa uno statement precompilato per prevenire injection, prevalentemente per gli apici
		preparedStatement = connection.prepareStatement("INSERT INTO Calciatore(Nome,Ruolo,Squadra) VALUES (?,?,?)");
		// inserisci il nome
		preparedStatement.setString(1, player.getName());
		// inserisci il ruolo
		preparedStatement.setString(2, player.getRule().toString());
		// inserisci il nome della squadra se presente, altrimenti inserisci null
		if(player.getTeam() != null){ preparedStatement.setString(3, player.getTeam()); }
		else {preparedStatement.setNull(3, Types.VARCHAR);}
		// esegui la query insert
		preparedStatement.executeUpdate();
	}
	
	/**
	 * metodo che inserisce i dati di un campionato nel database se il nome non è già presente
	 * @param championship campionato da inserire
	 * @throws SQLException sollevata quando la query sql fallisce
	 */
	public void InsertChampionship(ChampionshipEntity championship) throws SQLException{
		String query = "INSERT INTO Campionato(Nome) VALUES (?)";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, championship.getName());
		preparedStatement.executeUpdate();
	}
	
	/**
	 * metodo che restituisce le squadre
	 * @return lista squadre
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<TeamEntity> getTeams() throws SQLException {
		List<TeamEntity> lt = new ArrayList<TeamEntity>();
		String query = "SELECT idSquadra, Nome, Campionato_idCampionato, Utente_idUtente FROM Squadra";
		preparedStatement = connection.prepareStatement(query);
		ResultSet res = preparedStatement.executeQuery();
		while (res.next()){
			lt.add(new TeamEntity(res.getInt("idSquadra"),res.getString("Nome"),
				res.getInt("Campionato_idCampionato"),res.getInt("Utente_idUtente")));
		}
		return lt;
	}
	
	/**
	 * metodo che restituisce gli utenti giocanti (non amministratori)
	 * @return lista utenti giocanti
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<UserEntity> getPlayingUsers() throws SQLException {
		List<UserEntity> lpu = new ArrayList<UserEntity>();
		String query = "SELECT idUtente, Nome, Password FROM Utente WHERE Admin = 0";
		preparedStatement = connection.prepareStatement(query);			
		ResultSet res = preparedStatement.executeQuery();
		while (res.next()){
			lpu.add(new UserEntity(res.getInt("idUtente"),res.getString("Nome"),
				res.getString("Password"),false));
		}
		return lpu;
	}
	
	/**
	 * metodo che inserisce i dati di una squadra nel database se il nome non è già presente
	 * @param championship campionato da inserire
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void InsertTeam(TeamEntity team) throws SQLException{
		String query = "INSERT INTO Squadra(Nome,Campionato_idCampionato,Utente_idUtente) VALUES (?,?,?)";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, team.getName());
		preparedStatement.setInt(2, team.getChampionship());
		preparedStatement.setInt(3, team.getUser());
		preparedStatement.executeUpdate();
	}
	
	/**
	 * metodo che restituisce i giorni ancora aperti di un campionato
	 * @param idChamp identificativo del campionato
	 * @return lista delle giornate
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<DayEntity> getOpenDayOfChampionship(Integer idChamp) throws SQLException {
		List<DayEntity> lodoc = new ArrayList<DayEntity>();
		// query di selezione
		preparedStatement = connection.prepareStatement(
			"SELECT idGiornata, Data, Valutata " +
			"FROM Giornata WHERE Chiusa = 0 AND Campionato_idCampionato = ?");
		// inserimento id campionato
		preparedStatement.setInt(1, idChamp);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		while (res.next()){
			lodoc.add(new DayEntity(res.getInt("idGiornata"),idChamp,res.getDate("Data"),
				false, res.getBoolean("Valutata")));
		}
		return lodoc;
	}
	
	/**
	 * metodo che inserisce i dati di una rosa di giocatori di una squadra nel database
	 * @param group rosa di giocatori da inserire
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void insertHireGroup(GroupHireEntity group) throws SQLException{
		StringBuffer query = new StringBuffer(
			"INSERT INTO Convocazione(Calciatore_idCalciatore ,Squadra_idSquadra) VALUES ");
		List<Integer> players = group.getPlayers();
		// inserisci tutti i placeholders
		// TODO il caso senza dati da inserire è veramente da gestire qui?
		if(players.size() > 0){
			query.append("(?,?)");
			for(int i=0; i < players.size()-1; i++){
				query.append(",(?,?)");
			}
		}
		// inserisci i valori nei placeholders
		preparedStatement = connection.prepareStatement(query.toString());
		for(int i=0; i < players.size(); i++){
			// id calciatori
			preparedStatement.setInt(i*2+1, players.get(i));
			// id squadra
			preparedStatement.setInt(i*2+2, group.getTeam());
		}
		// esegui la query
		preparedStatement.executeUpdate();
	}	
	
	/**
	 * metodo che restituisce l'identificativo della squadra selezionata
	 * @param idTeam id squadra selezionata
	 * @return squadra (null in caso di risultato vuoto)
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public TeamEntity getTeam(Integer idTeam) throws SQLException {
		StringBuffer query = new StringBuffer("SELECT * FROM Squadra WHERE idSquadra = ?");
		preparedStatement = connection.prepareStatement(query.toString());
		// inserisci l'id della squadra nella query
		preparedStatement.setInt(1, idTeam);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		TeamEntity team = null;
		if(res.next()){
			team = new TeamEntity(idTeam, res.getString("Nome"), res.getInt("Campionato_idCampionato"), 
				res.getInt("Utente_idUtente"));
		}
		return team;
	}	
	
	/**
	 * metodo che restituisce le squadre di un certo campionato
	 * @param idChamp identificativo del campionato
	 * @return lista di squadre
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<TeamEntity> getTeamsOfChampionship(Integer idChamp) throws SQLException {
		List<TeamEntity> lte = new ArrayList<TeamEntity>();
		StringBuffer query = new StringBuffer("SELECT * FROM Squadra WHERE Campionato_idCampionato = ?");
		preparedStatement = connection.prepareStatement(query.toString());
		// inserisci l'id del campionato nella query
		preparedStatement.setInt(1, idChamp);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		// copia i dati sulla lista
		while(res.next()){
			lte.add(new TeamEntity(res.getInt("idSquadra"), res.getString("Nome"), 
				res.getInt("Campionato_idCampionato"), res.getInt("Utente_idUtente")));
		}
		return lte;
	}		
	
	/**
	 * metodo che restituisce le squadre ancora non convocate di un certo campionato
	 * @param idChamp identificativo del campionato
	 * @return lista di squadre
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<TeamEntity> getOpenTeamsOfChampionship(Integer idChamp) throws SQLException {
		List<TeamEntity> lte = new ArrayList<TeamEntity>();
		StringBuffer query = new StringBuffer("SELECT * FROM Squadra " +
			"WHERE Campionato_idCampionato = ? AND " +
			"idSquadra NOT IN (SELECT Squadra_idSquadra FROM Convocazione)");
		preparedStatement = connection.prepareStatement(query.toString());
		// inserisci l'id del campionato nella query
		preparedStatement.setInt(1, idChamp);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		// copia i dati sulla lista
		while(res.next()){
			lte.add(new TeamEntity(res.getInt("idSquadra"), res.getString("Nome"), 
				res.getInt("Campionato_idCampionato"), res.getInt("Utente_idUtente")));
		}
		return lte;
	}
	
	/**
	 * metodo che restituisce i calciatori non ancora convocati in un campionato
	 * @param idChamp campionato specificato
	 * @return lista di calciatori
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<PlayerEntity> getAvailablePlayers(Integer idChamp) throws SQLException {
		List<PlayerEntity> lpe = new ArrayList<PlayerEntity>();
		// query di recupero calciatori non convocati nel campionato selezionato
		StringBuffer query = new StringBuffer(
			"SELECT * FROM Calciatore WHERE idCalciatore NOT IN " +
			"(SELECT idCalciatore FROM Calciatore INNER JOIN " +
			"Convocazione ON idCalciatore = Calciatore_idCalciatore " +
			"INNER JOIN Squadra ON Squadra_idSquadra = idSquadra " +
			"WHERE Campionato_idCampionato = ?)");
		preparedStatement = connection.prepareStatement(query.toString());
		// inserisci l'id del campionato nella query
		preparedStatement.setInt(1, idChamp);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		// trasferisci i dati su lista
		while(res.next()){
			lpe.add(new PlayerEntity(res.getInt("idCalciatore"),res.getString("Nome"), 
				res.getString("Ruolo").charAt(0),res.getString("Squadra")));
		}
		return lpe;
	}		
	
	/**
	 * metodo che inserisce le giornate di un campionato nel database
	 * @param lde lista delle giornate da inserire
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void insertDays(List<DayEntity> lde) throws SQLException{
		// controlla se ci sono giornate da inserire
		if(lde.size() > 0){
			StringBuffer query = new StringBuffer(
				"INSERT INTO Giornata(Data, Chiusa, Valutata, Campionato_idCampionato) VALUES (?,?,?,?)");
			// inserisci placeholders
			for(int i=0; i < lde.size()-1; i++){
				query.append(",(?,?,?,?)");
			}
			preparedStatement = connection.prepareStatement(query.toString());
			// inserisci i valori nei placeholders
			for(int i=0; i < lde.size(); i++){
				// data
				// TODO util.data -> long -> sql.data, gestire tutto come sql.data?
				preparedStatement.setDate(i*4+1, new java.sql.Date(lde.get(i).getDate().getTime()));
				// chiusura
				preparedStatement.setBoolean(i*4+2, lde.get(i).isClose());
				// valutazione
				preparedStatement.setBoolean(i*4+3, lde.get(i).isEvaluated());				
				// id campionato
				preparedStatement.setInt(i*4+4, lde.get(i).getIdChampionship());				
			}
			// esegui la query
			preparedStatement.executeUpdate();			
		}
	}	
	
	/**
	 * metodo che inserisce le partite generate di un campionato nel database
	 * @param lme lista delle partite da inserire
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void insertMatches(List<MatchEntity> lme) throws SQLException{
		// controlla se ci sono partite da inserire
		if(lme.size() > 0){
			StringBuffer query = new StringBuffer(
				"INSERT INTO Partita(Squadra_idSquadra1, Squadra_idSquadra2, Giornata_idGiornata) VALUES (?,?,?)");
			// inserisci placeholders
			for(int i=0; i < lme.size()-1; i++){
				query.append(",(?,?,?)");
			}
			preparedStatement = connection.prepareStatement(query.toString());
			// inserisci i valori nei placeholders
			for(int i=0; i < lme.size(); i++){
				// prima squadra
				preparedStatement.setInt(i*3+1, lme.get(i).getIdTeam1());
				// seconda squadra
				preparedStatement.setInt(i*3+2, lme.get(i).getIdTeam2());
				// giornata
				preparedStatement.setInt(i*3+3, lme.get(i).getIdDay());
			}
			// esegui la query
			preparedStatement.executeUpdate();			
		}
	}			
	
	/**
	 * metodo che restituisce le giornate di un campionato
	 * @param idChamp campionato specificato
	 * @return lista di giornate
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<DayEntity> getDayOfChampionship(Integer idChamp) throws SQLException {
		List<DayEntity> lde = new ArrayList<DayEntity>();
		// query di recupero delle date
		StringBuffer query = new StringBuffer(
			"SELECT idGiornata, Campionato_idCampionato, Data, Chiusa, Valutata " +
			"FROM Giornata WHERE Campionato_idCampionato = ?");
		preparedStatement = connection.prepareStatement(query.toString());
		// inserisci l'id del campionato nella query
		preparedStatement.setInt(1, idChamp);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		// trasferisci i dati su lista
		while(res.next()){
			lde.add(new DayEntity(res.getInt("idGiornata"),res.getInt("Campionato_idCampionato"),
				res.getDate("Data"), res.getBoolean("Chiusa"), res.getBoolean("Valutata")));
		}
		return lde;
	}		

	/**
	 * metodo che restituisce i campionati non ancora definiti
	 * @return lista di campionati
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<ChampionshipEntity> getUndefinedChampionships() throws SQLException {
		List<ChampionshipEntity> lce = new ArrayList<ChampionshipEntity>();
		// query di recupero dei campionati non definiti
		StringBuffer query = new StringBuffer(
			"SELECT * FROM Campionato WHERE idCampionato NOT IN " +
			"(SELECT DISTINCT Campionato_idCampionato FROM Giornata)");
		preparedStatement = connection.prepareStatement(query.toString());
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		// trasferisci i dati su lista
		while(res.next()){
			lce.add(new ChampionshipEntity(res.getInt("idCampionato"), res.getString("Nome")));
		}
		return lce;
	}
	
	/**
	 * metodo che chiude la giornata selezionata alle modifiche
	 * @param dayId identificativo giornata da chiudere
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void updateCloseDay(Integer dayId) throws SQLException{
		// query di update della data
		preparedStatement = connection.prepareStatement(
			"UPDATE Giornata SET Chiusa = 1 WHERE idGiornata = ?");
		// inserisci l'id della giornata nella query
		preparedStatement.setInt(1, dayId);
		// esegui la query
		preparedStatement.executeUpdate();
	}
	
	/**
	 * metodo che segna una giornata come valutata
	 * @param dayId identificativo giornata da valutare
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void updateEvaluateDay(Integer dayId) throws SQLException{
		// query di update della data
		preparedStatement = connection.prepareStatement(
			"UPDATE Giornata SET Valutata = 1 WHERE idGiornata = ?");
		// inserisci l'id della giornata nella query
		preparedStatement.setInt(1, dayId);
		// esegui la query
		preparedStatement.executeUpdate();
	}	
	
	/**
	 * metodo che restituisce le tipologie di voto
	 * @return lista di voti
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<VoteEntity> getVotes() throws SQLException {
		List<VoteEntity> lve = new ArrayList<VoteEntity>();
		// query di recupero dei voti
		preparedStatement = connection.prepareStatement("SELECT * FROM Voto");
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		// trasferisci i dati su lista
		while(res.next()){
			lve.add(new VoteEntity(res.getInt("idVoto"), res.getString("Azione"),res.getFloat("Punteggio")));
		}
		return lve;
	}	
	
	/**
	 * metodo che inserisce i dati di un report nel database
	 * @param report votazione da inserire
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void InsertReport(ReportEntity report) throws SQLException{
		// query di inserimento votazione in pagella
		String query = 
			"INSERT INTO Pagella(Voto_idVoto,Giornata_idGiornata,Calciatore_idCalciatore) VALUES (?,?,?)";
		preparedStatement = connection.prepareStatement(query);
		// inserisci id voto
		preparedStatement.setInt(1, report.getVote());
		// inserisci id giornata
		preparedStatement.setInt(2, report.getDay());
		// inserisci id calciatore
		preparedStatement.setInt(3, report.getPlayer());
		// esegui la query
		preparedStatement.executeUpdate();		
	}

	/**
	 * metodo che restituisce l'id di un calciatore sapendone il nome e la squadra di provenienza
	 * @return id calciatore (null in caso di calciatore non presente)
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public Integer getPlayerId(String name, String team) throws SQLException {
		// query di recupero dei voti
		preparedStatement = connection.prepareStatement(
			"SELECT idCalciatore FROM Calciatore WHERE nome = ? AND squadra = ?");
		// inserisci il nome
		preparedStatement.setString(1, name);
		// inserisci la squadra
		preparedStatement.setString(2, team);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		Integer id = null;
		if(res.next()){
			id = res.getInt("idCalciatore");
		}
		return id;
	}		
	
	/**
	 * metodo che restituisce la lista dei campionati definiti a cui partecipa un utente specificato
	 * @param uid identificativo dell'utente
	 * @return lista di campionati
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<ChampionshipEntity> getDefChampOfUser(Integer uid) throws SQLException{
		// query di recupero dei campionati
		// la query seleziona i campionati in cui sono presenti le squadre dell'utente
		// e che sono definite, quindi che hanno generate delle giornate
		preparedStatement = connection.prepareStatement(
			"SELECT DISTINCT C.idCampionato, C.Nome FROM Utente U " +
			"INNER JOIN Squadra S ON idUtente = Utente_idUtente " +
			"INNER JOIN Campionato C ON idCampionato = Campionato_idCampionato " +
			"WHERE idUtente = ? AND C.idCampionato IN " +
			"(SELECT DISTINCT C2.idCampionato FROM Campionato C2 " +
			"INNER JOIN Giornata G ON C2.idCampionato = G.Campionato_idCampionato)");
		// inserisci l'id utente
		preparedStatement.setInt(1, uid);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		List<ChampionshipEntity> lc = new ArrayList<ChampionshipEntity>();
		// componi la lista di campionati
		while(res.next()){
			lc.add(new ChampionshipEntity(res.getInt("idCampionato"),res.getString("Nome")));
		}
		return lc;
	}
	
	/**
	 * metodo che restituisce le squadre di un certo utente che giocano una certa giornata
	 * @param uid id utente
	 * @param did id giornata
	 * @return lista di squadre
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<TeamEntity> getTeamsOfUserInDay(Integer uid, Integer did) throws SQLException{
		// query di recupero delle squadre
		// TODO scegliere la query più performante
		
		// queste due query sono identiche come risultato
		
		//preparedStatement = connection.prepareStatement(
			//"SELECT S.idSquadra, S.Campionato_idCampionato, S.Utente_idUtente, S.Nome " +
			//"FROM Giornata G INNER JOIN Campionato C ON C.idCampionato = G.Campionato_idCampionato " +
			//"INNER JOIN Squadra S ON C.idCampionato = S.Campionato_idCampionato " +
			//"WHERE S.Utente_idUtente = ? AND G.idGiornata = ?");
		
		// unione delle squadre in casa e di quelle ospiti		
		preparedStatement = connection.prepareStatement(
			"(SELECT idSquadra,Campionato_idCampionato,Utente_idUtente,Nome " +
			"FROM Squadra S INNER JOIN Partita P ON Squadra_idSquadra1 = idSquadra " +
			"WHERE Utente_idUtente = ? AND Giornata_idGiornata = ?) " +
			"UNION (SELECT idSquadra,Campionato_idCampionato,Utente_idUtente,Nome " +
			"FROM Squadra S INNER JOIN Partita P ON Squadra_idSquadra2 = idSquadra " +
			"WHERE Utente_idUtente = ? AND Giornata_idGiornata = ?)");
		// inserisci l'id utente
		preparedStatement.setInt(1, uid);
		preparedStatement.setInt(3, uid);
		// inserisci l'id della gioranta
		preparedStatement.setInt(2, did);
		preparedStatement.setInt(4, did);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		List<TeamEntity> lt = new ArrayList<TeamEntity>();
		// componi la lista di squadre
		while(res.next()){
			lt.add(new TeamEntity(res.getInt("idSquadra"),res.getString("Nome"),
				res.getInt("Campionato_idCampionato"),res.getInt("Utente_idUtente")));
		}
		return lt;
	}	
	
	/**
	 * metodo per recuperare la formazione di una squadra di una giornata
	 * @param tid id della squadra
	 * @param did id della giornata
	 * @return la formazione
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public FormationEntity getFormation(Integer tid, Integer did) throws SQLException{
		// query di selezione degli schieramenti 
		preparedStatement = connection.prepareStatement(
			"SELECT Convocazione_idConvocazione, idCalciatore, Ruolo " +
			"FROM Schieramento INNER JOIN Convocazione ON idConvocazione = Convocazione_idConvocazione " +
			"INNER JOIN Calciatore ON idCalciatore = Calciatore_idCalciatore " +
			"WHERE Squadra_idSquadra = ? AND Giornata_idGiornata = ?");
		// inserisci l'id utente
		preparedStatement.setInt(1, tid);
		preparedStatement.setInt(2, did);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();

		List<Integer> lAtt = new ArrayList<Integer>();
		List<Integer> lCen = new ArrayList<Integer>();
		List<Integer> lDef = new ArrayList<Integer>();
		List<Integer> lGolkeep = new ArrayList<Integer>();
		// per ogni record
		while(res.next()){
			// dividi i calciatori della formazione per ruolo
			char rule = res.getString("Ruolo").charAt(0);
			switch (rule) {
			case 'P': // portier
				lGolkeep.add(res.getInt("idCalciatore"));
				break;
			case 'D': // difensore
				lDef.add(res.getInt("idCalciatore"));				
				break;
			case 'C': // centrocampista
				lCen.add(res.getInt("idCalciatore"));				
				break;
			case 'A': // attaccante
				lAtt.add(res.getInt("idCalciatore"));				
				break;				
			default: // non esistono altri ruoli
				// TODO lancia eccezione
				break;
			}
		}
		FormationEntity formation = new FormationEntity(
			(Integer[])lAtt.toArray(new Integer[lAtt.size()]), 
			(Integer[])lCen.toArray(new Integer[lCen.size()]), 
			(Integer[])lDef.toArray(new Integer[lDef.size()]), 
			(Integer[])lGolkeep.toArray(new Integer[lGolkeep.size()]), 
			tid, did);
		return formation;
	}		
	
	/**
	 * metodo che restituisce i giocatori convocati di una squadra
	 * @param tid id della squadra
	 * @return lista dei giocatori convocati
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<PlayerEntity> getHiredPlayers(Integer tid) throws SQLException{
		// query di recupero calciatori convocati
		preparedStatement = connection.prepareStatement(
			"SELECT idCalciatore, Nome, Ruolo, Squadra " +
			"FROM Convocazione INNER JOIN Calciatore ON idCalciatore = Calciatore_idCalciatore " +
			"WHERE Squadra_idSquadra = ?");
		// inserisci l'id della squadra
		preparedStatement.setInt(1, tid);
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		List<PlayerEntity> lp = new ArrayList<PlayerEntity>();
		// componi la lista della convocazione
		while(res.next()){
			lp.add(new PlayerEntity(res.getInt("idCalciatore"),res.getString("Nome"),
				res.getString("Ruolo").charAt(0),res.getString("Squadra")));
		}
		return lp;
	}	

	/**
	 * metodo che inserisce la formazione di una squadra di una giornata, si assume che i dati contenuti
	 * nella formazione siano coerenti e quindi un sotto-insieme corretto di quelli convocati 
	 * @param formation formazione da inserire
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void insertFormation(FormationEntity formation) throws SQLException{
		// TODO separare la query che recupera gli id di convocazione in un'altra funzione?
		// query per il recupero degli id di convocazione dei calciatori
		StringBuffer getConvIdQuery = new StringBuffer(
			"SELECT idConvocazione FROM Convocazione " +
			"WHERE Squadra_idSquadra = ?");		
		List<Integer> players = formation.getPlayers();
		// inserisci tutti i placeholders
		// TODO il caso senza dati da inserire è veramente da gestire qui?
		if(players.size() > 0){
			getConvIdQuery.append(" AND ( Calciatore_idCalciatore IN (?");
			for(int i=0; i < players.size()-1; i++){
				getConvIdQuery.append(",?");				
			}
			// chiudi la query delle convocazioni
			getConvIdQuery.append("))");
		}
		
		// inserisci i valori nei placeholders della query delle convocazioni
		preparedStatement = connection.prepareStatement(getConvIdQuery.toString());
		// id squadra
		preparedStatement.setInt(1, formation.getTeam());
		for(int i=0; i < players.size(); i++){
			// id calciatori
			preparedStatement.setInt(i+2, players.get(i));
		}
		// esegui la query del recupero degli id di convocazione
		ResultSet convIds = preparedStatement.executeQuery();
		
		// query per l'inserimento dei giocatori nello schieramento
		StringBuffer insertFormationQuery = new StringBuffer(
			"INSERT INTO Schieramento(Convocazione_idConvocazione , Giornata_idGiornata) VALUES ");		
		// inserisci tutti i placeholders
		// TODO il caso senza dati da inserire è veramente da gestire qui?
		if(convIds.next()){
			insertFormationQuery.append("(?,?)");
			for(int i=0; i < players.size()-1; i++){
				insertFormationQuery.append(",(?,?)");
			}
		}
		// inserisci i valori nei placeholders della query di inserimento dello schieramento
		preparedStatement = connection.prepareStatement(insertFormationQuery.toString());
		// flag per ripetere l'inserimento dei valori
		boolean repeat = true;
		// riavvolgi il result set
		convIds.first();		
		for(int i=0; repeat; i++){
			// id convocazione
			preparedStatement.setInt(i*2+1, convIds.getInt("idConvocazione"));			
			// id giornata
			preparedStatement.setInt(i*2+2, formation.getDay());
			// vai al prossimo record
			repeat = convIds.next();
		}
		// esegui l'inserimento dello schieramento
		preparedStatement.executeUpdate();
	}
	
	/**
	 * metodo che la lista di calciatori corrispondenti agli id forniti
	 * @param idList lista di id dei calciatori
	 * @return lista di calciatori
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<PlayerEntity> getPlayersById(List<Integer> idList) throws SQLException {
		// se non sono presenti id ritorna una lista vuota
		if(idList.isEmpty()) return new ArrayList<PlayerEntity>();
		// query di recupero dei voti
		StringBuffer query = new StringBuffer("SELECT idCalciatore, Nome, Ruolo, Squadra " +
			"FROM Calciatore WHERE idCalciatore IN (");
		// inserisci i placeholders
		for(int i = 0; i < idList.size()-1; i++){
			query.append("?,");
		}
		query.append("?)");
		preparedStatement = connection.prepareStatement(query.toString());
		// inserisci i valori nei placeholders
		for(int i=0; i < idList.size(); i++){
			preparedStatement.setInt(i+1, idList.get(i));
		}
		// esegui la query
		ResultSet res = preparedStatement.executeQuery();
		List<PlayerEntity> players = new ArrayList<PlayerEntity>();
		while(res.next()){
			// id nome ruolo squadra
			players.add(new PlayerEntity(
				res.getInt("idCalciatore"),
				res.getString("Nome"),
				res.getString("Ruolo").charAt(0),
				res.getString("Squadra")
				));
		}
		return players;
	}		

	/**
	 * metodo che ricava il campionato a cui appartiene la squadra specificata
	 * @param tid id della squadra
	 * @return campionato a cui appartiene la squadra
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public ChampionshipEntity getChampionshipOfTeam(Integer tid) throws SQLException{
		preparedStatement = connection.prepareStatement("SELECT C.idCampionato, C.Nome " +
			"FROM Campionato C INNER JOIN Squadra S ON S.Campionato_idCampionato = C.idCampionato " +
			"WHERE S.idSquadra = ?");
		preparedStatement.setInt(1, tid);
		ResultSet res = preparedStatement.executeQuery();
		if(!res.next()){
			// se non ci sono campionati corrispondenti
			// TODO lanciare eccezione (non SQL)
		}
		return new ChampionshipEntity(res.getInt("idCampionato"),res.getString("Nome"));		
	}
	
	/**
	 * metodo che ricava il campionato a cui appartiene la giornata specificata
	 * @param did id della squadra
	 * @return campionato a cui appartiene la giornata
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public ChampionshipEntity getChampionshipOfDay(Integer did) throws SQLException{
		preparedStatement = connection.prepareStatement("SELECT C.idCampionato, C.Nome " +
			"FROM Campionato C INNER JOIN Giornata G ON G.Campionato_idCampionato = C.idCampionato " +
			"WHERE G.idGiornata = ?");
		preparedStatement.setInt(1, did);
		ResultSet res = preparedStatement.executeQuery();
		if(!res.next()){
			// se non ci sono campionati corrispondenti
			// TODO lanciare eccezione (non SQL)
		}
		return new ChampionshipEntity(res.getInt("idCampionato"),res.getString("Nome"));		
	}	
	
	/**
	 * metodo che modifica una formazione già presente nel database, i record del
	 * vecchio schieramento vengono rimossi e poi vengono inseriti quelli nuovi
	 * @param newFormation formazione modificata
	 * @param oldFormation vecchia formazione
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public void updateFormation(FormationEntity newFormation, FormationEntity oldFormation) 
		throws SQLException{
		// prima query: recupera gli id degli schieramenti cancellati
		String query1 = "SELECT idSchieramento " +
			"FROM Schieramento INNER JOIN Convocazione ON idConvocazione = Convocazione_idConvocazione " +
			"WHERE Squadra_idSquadra = "+oldFormation.getTeam()+" AND Giornata_idGiornata = "+oldFormation.getDay();
		ResultSet res = preparedStatement.executeQuery(query1);
		
		// seconda query: cancella gli schieramenti
		if(res.next()){
			StringBuffer query2 = new StringBuffer("" +
				"DELETE FROM Schieramento WHERE idSchieramento IN ("+res.getInt("idSchieramento"));
			while(res.next()){
				query2.append(","+res.getInt("idSchieramento"));
			}
			query2.append(")");
			preparedStatement.executeUpdate(query2.toString());
		}
		
		// terza query: inserisci i nuovi schieramenti
		insertFormation(newFormation);
	}
	
	/**
	 * metodo che recupera i dati delle giornate partendo dagli id
	 * @param ids id delle giornate
	 * @return lista delle giornate
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<DayEntity> getDaysById(List<Integer> ids) throws SQLException{
		// se non ci sono id torna una lista vuota
		if(ids.size() == 0) return new ArrayList<DayEntity>();
		
		Iterator<Integer> it = ids.iterator();
		StringBuffer query = new StringBuffer(
			"SELECT idGiornata, Campionato_idCampionato, Data, Chiusa, Valutata " +
			"FROM Giornata WHERE idGiornata IN ("+(Integer)it.next());
		while(it.hasNext()) query.append(","+((Integer)it.next()));
		query.append(")");
		
		// esegui query
		ResultSet res = preparedStatement.executeQuery(query.toString());
		
		// lista delle giornate
		List<DayEntity> days = new ArrayList<DayEntity>();
		while(res.next()){
			days.add(new DayEntity(res.getInt("idGiornata"),res.getInt("Campionato_idCampionato"),
				res.getDate("Data"),res.getBoolean("Chiusa"),res.getBoolean("Valutata")));
		}
		return days;
	}
	
	/**
	 * metodo che ritorna i campionati a cui partecipa l'utente
	 * @param uid id utente
	 * @return lista di campionati
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<ChampionshipEntity> getChampionshipOfUser(Integer uid) throws SQLException {
		String query = "SELECT DISTINCT C.idCampionato, C.Nome " +
			"FROM Squadra INNER JOIN Campionato C ON idCampionato = Campionato_idCampionato " +
			"WHERE Utente_idUtente = ?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, uid);
		ResultSet res = preparedStatement.executeQuery();
		// crea la lista dei campionati
		List<ChampionshipEntity> lc = new ArrayList<ChampionshipEntity>();
		while(res.next())
			lc.add(new ChampionshipEntity(res.getInt("idCampionato"),res.getString("Nome")));
		return lc;
	}
	
	/**
	 * metodo che ritorna le squadre che si affrontano in una giornata di campionato
	 * @param did id della giornata
	 * @return lista di coppie di squadre 
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public List<Pair<TeamEntity, TeamEntity>> getMatchesOfDay(Integer did) throws SQLException{
		// la query interna effettua una join tra squadra e partita per ricavare i dati della prima squadra
		// mentre quella esterna fa la join tra il primo risultato e nuovamente con squadra per
		// ricavare i dati della seconda
		String query = 
			"SELECT " +
				"T.Squadra_idSquadra1 AS idSquadra1, " +
				"T.Squadra_idSquadra2 AS idSquadra2, " +
				"T.Nome AS nomeSquadra1, " +
				"S2.Nome AS nomeSquadra2," +
				"T.Utente_idUtente AS utenteSquadra1, " +
				"S2.Utente_idUtente AS utenteSquadra2, " +
				"Campionato_idCampionato AS idCampionato " +
			"FROM (" +
				"SELECT P.Squadra_idSquadra1, P.Squadra_idSquadra2, S1.Nome, S1.Utente_idUtente " +
				"FROM Partita P INNER JOIN Squadra S1 ON P.Squadra_idSquadra1 = S1.idSquadra " +
				"WHERE P.Giornata_idGiornata = ?) T " +
			"INNER JOIN Squadra S2 ON T.Squadra_idSquadra2 = S2.idSquadra";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, did);
		ResultSet res = preparedStatement.executeQuery();
		// crea la lista delle coppie di squadre
		List<Pair<TeamEntity, TeamEntity>> lt = new ArrayList<Pair<TeamEntity,TeamEntity>>();
		while(res.next()){
			lt.add(new Pair<TeamEntity, TeamEntity>(
				new TeamEntity(res.getInt("idSquadra1"),res.getString("nomeSquadra1"),
					res.getInt("idCampionato"),res.getInt("utenteSquadra1")), 
				new TeamEntity(res.getInt("idSquadra2"),res.getString("nomeSquadra2"),
					res.getInt("idCampionato"),res.getInt("utenteSquadra2"))
			));
		}
		return lt;
	}
	
	/**
	 * metodo che restituisce il numero di gol della squadra specificando la giornata
	 * @param tid id della squadra
	 * @param did id della giornata
	 * @return numero di gol
	 * @throws SQLException sollevata quando la query fallisce
	 */
	public Integer getGolOfTeamInDay(Integer tid, Integer did) throws SQLException{
		String query = 
			"SELECT " +
				"COUNT(*) AS numeroGol " +
			"FROM " +
				"Pagella P INNER JOIN " +
				"Voto V ON Voto_idVoto = V.idVoto INNER JOIN " +
				"Calciatore CA ON P.Calciatore_idCalciatore = CA.idCalciatore " +
				"INNER JOIN Convocazione CO ON CO.Calciatore_idCalciatore = CA.idCalciatore " +
			"WHERE " +
				"V.Azione = ? AND " +
				"CO.Squadra_idSquadra = ? " +
				"AND P.Giornata_idGiornata = ?";
		preparedStatement = connection.prepareStatement(query);
		// inserisci tipo di azione
		preparedStatement.setString(1, "gol segnato");
		// inserisci squadra
		preparedStatement.setInt(2, tid);
		// inserisci giornata
		preparedStatement.setInt(3, did);		
		// esegui query
		ResultSet res = preparedStatement.executeQuery();
		res.next();
		return res.getInt("numeroGol");
	}
	
}
