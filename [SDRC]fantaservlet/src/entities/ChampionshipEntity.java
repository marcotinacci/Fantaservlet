package entities;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import dataconnection.MySQLConnection;
import utils.GenericUtilities;

public class ChampionshipEntity {
	private Integer id;
	private String name;

	public ChampionshipEntity(){}

	public ChampionshipEntity(String name){
		this.name = name;
	}
	
	public ChampionshipEntity(Integer id){
		this.id = id;
	}
	
	public ChampionshipEntity(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isComplete(){
		return GenericUtilities.hasValue(getName());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isNameAvailable() throws SQLException{
		// TODO gestire il metodo direttamente dalla connessione al database?
		List<ChampionshipEntity> lc = MySQLConnection.getChampionships();
		for(Iterator<ChampionshipEntity> it = lc.listIterator(); it.hasNext();){
			ChampionshipEntity t = it.next();
			if(name.equals(t.getName())){
				return false;
			}
		}
		return true;
	}
	
	public boolean hasUndefinedTeams() throws SQLException{
		// TODO gestire il metodo direttamente dalla connessione al database?
		List<TeamEntity> lte = MySQLConnection.getOpenTeamsOfChampionship(getId());
		return lte.size() > 0;
	}
}
