package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.GenericUtilities;

public class GroupHireEntity {
	private Integer[] att;
	private Integer[] cen;
	private Integer[] def;
	private Integer[] golkeep;
	private Integer team;
	
	public GroupHireEntity() {
	}

	public GroupHireEntity(Integer[] att, Integer[] cen, Integer[] def,
			Integer[] golkeep, Integer team) {
		this.att = att;
		this.cen = cen;
		this.def = def;
		this.golkeep = golkeep;
		this.team = team;
	}

	public Integer[] getAtt() {
		return att;
	}

	public void setAtt(Integer[] att) {
		this.att = att;
	}

	public Integer[] getCen() {
		return cen;
	}

	public void setCen(Integer[] cen) {
		this.cen = cen;
	}

	public Integer[] getDef() {
		return def;
	}

	public void setDef(Integer[] def) {
		this.def = def;
	}

	public Integer[] getGolkeep() {
		return golkeep;
	}

	public void setGolkeep(Integer[] golkeep) {
		this.golkeep = golkeep;
	}

	public Integer getTeam() {
		return team;
	}

	public void setTeam(Integer team) {
		this.team = team;
	}
	
	/**
	 * metodo che ritorna un'unica lista contenente tutti i giocatori della rosa
	 * @return lista di id dei giocatori
	 */
	public List<Integer> getPlayers(){
		List<Integer> players = new ArrayList<Integer>();
		players.addAll(Arrays.asList(getAtt()));
		players.addAll(Arrays.asList(getCen()));
		players.addAll(Arrays.asList(getDef()));
		players.addAll(Arrays.asList(getGolkeep()));
		return players;
	}
	
	public Boolean isComplete(){
		return GenericUtilities.hasValue(getTeam()) &&
			GenericUtilities.hasValue(getAtt()) &&
			GenericUtilities.hasValue(getCen()) &&
			GenericUtilities.hasValue(getDef()) &&
			GenericUtilities.hasValue(getGolkeep());
	}
	
	public Boolean isCorrect(){
		// TODO leggere da file di configurazione il numero di attaccanti, centrocampisti, difensori e portieri
		return isComplete() &&
			getAtt().length == 6 &&
			getCen().length == 8 &&
			getDef().length == 8 &&
			getGolkeep().length == 3;
	}
	
}
