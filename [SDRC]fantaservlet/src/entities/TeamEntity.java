package entities;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import utils.GenericUtilities;

import dataconnection.MySQLConnection;

public class TeamEntity {
	private Integer id;
	private String name;
	private Integer championship;
	private Integer user;
	
	public TeamEntity(Integer id, String name, Integer championship,
			Integer user) {
		this.id = id;
		this.name = name;
		this.championship = championship;
		this.user = user;
	}

	public TeamEntity(){}
	
	public TeamEntity(String name, Integer championship, Integer user) {
		this.name = name;
		this.championship = championship;
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getChampionship() {
		return championship;
	}

	public void setChampionship(Integer championship) {
		this.championship = championship;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public boolean isAvailableName() throws SQLException{
		List<TeamEntity> lt = MySQLConnection.getTeams();
		for(Iterator<TeamEntity> it = lt.listIterator(); it.hasNext();){
			TeamEntity t = it.next();
			if(name.equals(t.getName())){
				return false;
			}
		}
		return true;		
	}
	
	public boolean isComplete(){
		return GenericUtilities.hasValue(getName()) &&
			GenericUtilities.hasValue(getChampionship()) &&
			GenericUtilities.hasValue(getUser());			
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
