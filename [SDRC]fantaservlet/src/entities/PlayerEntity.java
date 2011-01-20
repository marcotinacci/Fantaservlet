package entities;

import java.util.Iterator;
import java.util.List;

import dataconnection.MySQLConnection;
import utils.GenericUtilities;

public class PlayerEntity {
	private Integer id;
	private String name;
	private Character rule;
	private String team;
	
	public PlayerEntity(){}
	
	public PlayerEntity(String name, char rule, String team) {
		this.name = name;
		this.rule = rule;
		this.team = team;
	}
	
	public PlayerEntity(Integer id, String name, Character rule, String team) {
		this.id = id;
		this.name = name;
		this.rule = rule;
		this.team = team;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getRule() {
		return rule;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public boolean isComplete(){
		return GenericUtilities.hasValue(getName()) &&
			GenericUtilities.hasValue(getRule()) &&
			GenericUtilities.hasValue(getTeam());
	}
	
	public boolean isNameAvailable(){
		MySQLConnection dbc = new MySQLConnection();
		dbc.startup();		
		List<PlayerEntity> lp = dbc.getPlayers();
		dbc.destroy();
		for(Iterator<PlayerEntity> it = lp.listIterator(); it.hasNext();){
			PlayerEntity t = it.next();
			if(name.equals(t.getName())){
				return false;
			}
		}
		return true;		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setRule(Character rule) {
		this.rule = rule;
	}

	public Boolean isAtt(){
		return getRule() == 'A';
	}
	
	public Boolean isCen(){
		return getRule() == 'C';
	}	
	
	public Boolean isDef(){
		return getRule() == 'D';
	}	
	
	public Boolean isGoalKeep(){
		return getRule() == 'P';
	}
	
	@Override
	public String toString() {
		return "PlayerEntity [name=" + name + ", rule=" + rule + ", team="
				+ team + "]";
	}	
}
