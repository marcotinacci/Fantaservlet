package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.GenericUtilities;

// TODO estendere da GroupHireEntity?
public class FormationEntity {
	private Integer[] att;
	private Integer[] cen;
	private Integer[] def;
	private Integer[] golkeep;
	private Integer team;
	private Integer day;
	
	public FormationEntity() {
	}

	public FormationEntity(Integer[] att, Integer[] cen, Integer[] def,
			Integer[] golkeep, Integer team, Integer day) {
		super();
		this.att = att;
		this.cen = cen;
		this.def = def;
		this.golkeep = golkeep;
		this.team = team;
		this.day = day;
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

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
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
	
	/**
	 * metodo che conferma se l'oggetto contiene tutti i dati necessari
	 * @return true se sono stati settati in modo valido tutti i valori provati
	 */
	public Boolean isComplete(){
		return GenericUtilities.hasValue(getTeam()) &&
			GenericUtilities.hasValue(getAtt()) &&
			GenericUtilities.hasValue(getCen()) &&
			GenericUtilities.hasValue(getDef()) &&
			GenericUtilities.hasValue(getGolkeep()) &&
			GenericUtilities.hasValue(getDay());		
	}
	
	/**
	 * metodo che conferma se i la formazione è valida e se sono presenti tutti i dati
	 * @return true se la formazione è valida
	 */
	public Boolean isCorrect(){
		// TODO sarebbe possibile centralizzare i codici delle formazioni su database oppure da file di configurazione
		// 3-4-3, 3-5-2, 4-5-1, 4-4-2, 4-3-3, 5-4-1, 5-3-2
		return isComplete() && 
			((def.length == 3 && cen.length == 4 && att.length == 3) ||
			(def.length == 3 && cen.length == 5 && att.length == 2) ||
			(def.length == 4 && cen.length == 5 && att.length == 1) ||
			(def.length == 4 && cen.length == 4 && att.length == 2) ||
			(def.length == 4 && cen.length == 3 && att.length == 3) ||
			(def.length == 5 && cen.length == 4 && att.length == 1) ||
			(def.length == 5 && cen.length == 3 && att.length == 2)) &&
			getAtt().length == att.length && getCen().length == cen.length && 
			getDef().length == def.length && getGolkeep().length == 1;
	}
}