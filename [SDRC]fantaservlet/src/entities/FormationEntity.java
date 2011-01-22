package entities;

import java.util.ArrayList;
import java.util.List;

import exceptions.BadModuleException;

import utils.Config;
import utils.GenericUtilities;

public class FormationEntity {
	
	// difensori
	// stato: 0 panchina, 1 riserva, 2 titolare
	private Integer def1 = null;
	private Integer def2 = null;
	private Integer def3 = null;
	private Integer def4 = null;
	private Integer def5 = null;
	private Integer def6 = null;
	private Integer def7 = null;
	private Integer def8 = null;
	// id difensori
	private Integer idDef1 = null;
	private Integer idDef2 = null;
	private Integer idDef3 = null;
	private Integer idDef4 = null;
	private Integer idDef5 = null;
	private Integer idDef6 = null;
	private Integer idDef7 = null;
	private Integer idDef8 = null;
	
	// centrocampisti
	private Integer cen1 = null;
	private Integer cen2 = null;
	private Integer cen3 = null;
	private Integer cen4 = null;
	private Integer cen5 = null;
	private Integer cen6 = null;
	private Integer cen7 = null;
	private Integer cen8 = null;
	// id centrocampisti
	private Integer idCen1 = null;
	private Integer idCen2 = null;
	private Integer idCen3 = null;
	private Integer idCen4 = null;
	private Integer idCen5 = null;
	private Integer idCen6 = null;
	private Integer idCen7 = null;
	private Integer idCen8 = null;
	
	// attaccanti
	private Integer att1 = null;
	private Integer att2 = null;
	private Integer att3 = null;
	private Integer att4 = null;
	private Integer att5 = null;
	private Integer att6 = null;
	// id attaccanti
	private Integer idAtt1 = null;
	private Integer idAtt2 = null;
	private Integer idAtt3 = null;
	private Integer idAtt4 = null;
	private Integer idAtt5 = null;
	private Integer idAtt6 = null;
	
	// portieri
	private Integer golkeep1 = null;
	private Integer golkeep2 = null;
	private Integer golkeep3 = null;
	// id portieri
	private Integer idGolkeep1 = null;
	private Integer idGolkeep2 = null;
	private Integer idGolkeep3 = null;

	// id squadra
	private Integer team;
	
	// id giornata
	private Integer day;
	
	// indice formazione
	private Integer formation;
	
	/**
	 * costruttore di default
	 */
	public FormationEntity() {}
	
	/**
	 * costruttore che inserisce i calciatori della formazione, 
	 * i non giocanti restano con id nullo
	 * @param att id attaccanti titolari
	 * @param cen id centrocampisti titolari
	 * @param def id difensori titolari
	 * @param golkeep id portieri titolari
	 * @param resAtt id attaccanti riserve
	 * @param resCen id centrocampisti riserve
	 * @param resDef id difensori riserve
	 * @param resGolkeep id portieri riserve
	 * @param team id squadra
	 * @param day id giornata
	 */
	public FormationEntity(
			List<Integer> att, List<Integer> cen, List<Integer> def, List<Integer> golkeep,
			List<Integer> resAtt, List<Integer> resCen, List<Integer> resDef, List<Integer> resGolkeep,
			Integer team, Integer day){
		// id squadra
		this.team = team;
		// id giornata
		this.day = day;
		try {
			// salva il modulo della formazione
			formation = Config.getFormationIndex(def.size(), cen.size(), att.size());
			// inserisci i difensori titolari
			switch(def.size()){
			case 8: setIdDef8(def.get(7)); setDef8(2);
			case 7: setIdDef7(def.get(6)); setDef7(2);
			case 6: setIdDef6(def.get(5)); setDef6(2);
			case 5: setIdDef5(def.get(4)); setDef5(2);
			case 4: setIdDef4(def.get(3)); setDef4(2);
			case 3: setIdDef3(def.get(2)); setDef3(2);	
			case 2: setIdDef2(def.get(1)); setDef2(2);
			case 1: setIdDef1(def.get(0)); setDef1(2);
			}
			// inserisci i centrocampisti titolari
			switch(cen.size()){
			case 8: setIdCen8(cen.get(7)); setCen8(2);
			case 7: setIdCen7(cen.get(6)); setCen7(2);
			case 6: setIdCen6(cen.get(5)); setCen6(2);
			case 5: setIdCen5(cen.get(4)); setCen5(2);
			case 4: setIdCen4(cen.get(3)); setCen4(2);
			case 3: setIdCen3(cen.get(2)); setCen3(2);	
			case 2: setIdCen2(cen.get(1)); setCen2(2);
			case 1: setIdCen1(cen.get(0)); setCen1(2);
			}
			// inserisci gli attaccanti titolari
			switch(att.size()){
			case 6: setIdAtt6(att.get(5)); setAtt6(2);
			case 5: setIdAtt5(att.get(4)); setAtt5(2);
			case 4: setIdAtt4(att.get(3)); setAtt4(2);
			case 3: setIdAtt3(att.get(2)); setAtt3(2);	
			case 2: setIdAtt2(att.get(1)); setAtt2(2);
			case 1: setIdAtt1(att.get(0)); setAtt1(2);
			}
			// inserisci i portieri titolari
			switch(golkeep.size()){
			case 3: setIdGolkeep3(golkeep.get(2)); setGolkeep3(2);	
			case 2: setIdGolkeep2(golkeep.get(1)); setGolkeep2(2);
			case 1: setIdGolkeep1(golkeep.get(0)); setGolkeep1(2);
			}			
			// inserisci i difensori riserve
			int count = resDef.size();
			switch(resDef.size()+def.size()){
			case 8: setIdDef8(resDef.get(count-1)); setDef8(1); count--; if(count==0) break;
			case 7: setIdDef7(resDef.get(count-1)); setDef7(1); count--; if(count==0) break;
			case 6: setIdDef6(resDef.get(count-1)); setDef6(1); count--; if(count==0) break;
			case 5: setIdDef5(resDef.get(count-1)); setDef5(1); count--; if(count==0) break;			
			case 4: setIdDef4(resDef.get(count-1)); setDef4(1); count--; if(count==0) break;
			case 3: setIdDef3(resDef.get(count-1)); setDef3(1); count--; if(count==0) break;
			case 2: setIdDef2(resDef.get(count-1)); setDef2(1); count--; if(count==0) break;
			case 1: setIdDef1(resDef.get(count-1)); setDef1(1); count--; if(count==0) break;			
			}
			// inserisci i centrocampisti riserve
			count = resCen.size();
			switch(resCen.size()+cen.size()){
			case 8: setIdCen8(resCen.get(count-1)); setCen8(1); count--; if(count==0) break;
			case 7: setIdCen7(resCen.get(count-1)); setCen7(1); count--; if(count==0) break;
			case 6: setIdCen6(resCen.get(count-1)); setCen6(1); count--; if(count==0) break;
			case 5: setIdCen5(resCen.get(count-1)); setCen5(1); count--; if(count==0) break;
			case 4: setIdCen4(resCen.get(count-1)); setCen4(1); count--; if(count==0) break;
			case 3: setIdCen3(resCen.get(count-1)); setCen3(1); count--; if(count==0) break;
			case 2: setIdCen2(resCen.get(count-1)); setCen2(1); count--; if(count==0) break;
			case 1: setIdCen1(resCen.get(count-1)); setCen1(1); count--; if(count==0) break;			
			}
			// inserisci gli attaccanti riserve
			count = resAtt.size();
			switch(resAtt.size()+att.size()){
			case 6: setIdAtt6(resAtt.get(count-1)); setAtt6(1); count--; if(count==0) break;
			case 5: setIdAtt5(resAtt.get(count-1)); setAtt5(1); count--; if(count==0) break;
			case 4: setIdAtt4(resAtt.get(count-1)); setAtt4(1); count--; if(count==0) break;
			case 3: setIdAtt3(resAtt.get(count-1)); setAtt3(1); count--; if(count==0) break;
			case 2: setIdAtt2(resAtt.get(count-1)); setAtt2(1); count--; if(count==0) break;
			case 1: setIdAtt1(resAtt.get(count-1)); setAtt1(1); count--; if(count==0) break;			
			}
			// inserisci i portieri riserve
			count = resGolkeep.size();
			switch(resGolkeep.size()+golkeep.size()){
			case 3: setIdGolkeep3(resGolkeep.get(count-1)); setGolkeep3(1); count--; if(count==0) break;
			case 2: setIdGolkeep2(resGolkeep.get(count-1)); setGolkeep2(1); count--; if(count==0) break;
			case 1: setIdGolkeep1(resGolkeep.get(count-1)); setGolkeep1(1); count--; if(count==0) break;			
			}
		} catch (BadModuleException e) {
			// in caso di formazione non valida salta il completamento dei dati
			//e.printStackTrace();
		}
		
	}

	/**
	 * metodo che ritorna una lista contenente gli attaccanti titolari
	 * @return lista di id
	 */
	public List<Integer> getAtt(){
		List<Integer> players = new ArrayList<Integer>();
		if(att1 != null && att1.equals(2)) players.add(idAtt1);
		if(att2 != null && att2.equals(2)) players.add(idAtt2);
		if(att3 != null && att3.equals(2)) players.add(idAtt3);
		if(att4 != null && att4.equals(2)) players.add(idAtt4);
		if(att5 != null && att5.equals(2)) players.add(idAtt5);
		if(att6 != null && att6.equals(2)) players.add(idAtt6);
		return players;
	}
	
	/**
	 * metodo che ritorna una lista contenente i centrocampisti titolari
	 * @return lista di id
	 */	
	public List<Integer> getCen(){
		List<Integer> players = new ArrayList<Integer>();
		if(cen1 != null && cen1.equals(2)) players.add(idCen1);
		if(cen2 != null && cen2.equals(2)) players.add(idCen2);
		if(cen3 != null && cen3.equals(2)) players.add(idCen3);
		if(cen4 != null && cen4.equals(2)) players.add(idCen4);
		if(cen5 != null && cen5.equals(2)) players.add(idCen5);
		if(cen6 != null && cen6.equals(2)) players.add(idCen6);	
		if(cen7 != null && cen7.equals(2)) players.add(idCen7);
		if(cen8 != null && cen8.equals(2)) players.add(idCen8);
		return players;
	}	

	/**
	 * metodo che ritorna una lista contenente i difensori titolari
	 * @return lista di id
	 */
	public List<Integer> getDef(){
		List<Integer> players = new ArrayList<Integer>();
		if(def1 != null && def1.equals(2)) players.add(idDef1);
		if(def2 != null && def2.equals(2)) players.add(idDef2);
		if(def3 != null && def3.equals(2)) players.add(idDef3);
		if(def4 != null && def4.equals(2)) players.add(idDef4);
		if(def5 != null && def5.equals(2)) players.add(idDef5);
		if(def6 != null && def6.equals(2)) players.add(idDef6);	
		if(def7 != null && def7.equals(2)) players.add(idDef7);
		if(def8 != null && def8.equals(2)) players.add(idDef8);	
		return players;
	}
	
	/**
	 * metodo che ritorna una lista contenente i portieri titolari
	 * @return lista di id
	 */
	public List<Integer> getGolkeep(){
		List<Integer> players = new ArrayList<Integer>();
		if(golkeep1 != null && golkeep1.equals(2)) players.add(idGolkeep1);
		if(golkeep2 != null && golkeep2.equals(2)) players.add(idGolkeep2);
		if(golkeep3 != null && golkeep3.equals(2)) players.add(idGolkeep3);
		return players;
	}

	/**
	 * metodo che ritorna una lista contenente gli attaccanti riserve
	 * @return lista di id
	 */
	public List<Integer> getResAtt(){
		List<Integer> players = new ArrayList<Integer>();
		if(att1 != null && att1.equals(1)) players.add(idAtt1);
		if(att2 != null && att2.equals(1)) players.add(idAtt2);
		if(att3 != null && att3.equals(1)) players.add(idAtt3);
		if(att4 != null && att4.equals(1)) players.add(idAtt4);
		if(att5 != null && att5.equals(1)) players.add(idAtt5);
		if(att6 != null && att6.equals(1)) players.add(idAtt6);
		return players;
	}
	
	/**
	 * metodo che ritorna una lista contenente i centrocampisti riserve
	 * @return lista di id
	 */	
	public List<Integer> getResCen(){
		List<Integer> players = new ArrayList<Integer>();
		if(cen1 != null && cen1.equals(1)) players.add(idCen1);
		if(cen2 != null && cen2.equals(1)) players.add(idCen2);
		if(cen3 != null && cen3.equals(1)) players.add(idCen3);
		if(cen4 != null && cen4.equals(1)) players.add(idCen4);
		if(cen5 != null && cen5.equals(1)) players.add(idCen5);
		if(cen6 != null && cen6.equals(1)) players.add(idCen6);	
		if(cen7 != null && cen7.equals(1)) players.add(idCen7);
		if(cen8 != null && cen8.equals(1)) players.add(idCen8);
		return players;
	}	

	/**
	 * metodo che ritorna una lista contenente i difensori riserve
	 * @return lista di id
	 */
	public List<Integer> getResDef(){
		List<Integer> players = new ArrayList<Integer>();
		if(def1 != null && def1.equals(1)) players.add(idDef1);
		if(def2 != null && def2.equals(1)) players.add(idDef2);
		if(def3 != null && def3.equals(1)) players.add(idDef3);
		if(def4 != null && def4.equals(1)) players.add(idDef4);
		if(def5 != null && def5.equals(1)) players.add(idDef5);
		if(def6 != null && def6.equals(1)) players.add(idDef6);	
		if(def7 != null && def7.equals(1)) players.add(idDef7);
		if(def8 != null && def8.equals(1)) players.add(idDef8);	
		return players;
	}
	
	/**
	 * metodo che ritorna una lista contenente i portieri riserve
	 * @return lista di id
	 */
	public List<Integer> getResGolkeep(){
		List<Integer> players = new ArrayList<Integer>();
		if(golkeep1 != null && golkeep1.equals(1)) players.add(idGolkeep1);
		if(golkeep2 != null && golkeep2.equals(1)) players.add(idGolkeep2);
		if(golkeep3 != null && golkeep3.equals(1)) players.add(idGolkeep3);
		return players;
	}	
	
	/**
	 * metodo che conferma se l'oggetto contiene tutti i dati necessari
	 * @return true se sono stati settati in modo valido tutti i valori provati
	 */
	public Boolean isComplete(){
		return 
			GenericUtilities.hasValue(getTeam()) &&
			GenericUtilities.hasValue(getDay()) &&
			GenericUtilities.hasValue(getFormation()) &&
			// attaccanti
			GenericUtilities.hasValue(getAtt1()) && GenericUtilities.hasValue(getIdAtt1()) &&  
			GenericUtilities.hasValue(getAtt2()) && GenericUtilities.hasValue(getIdAtt2()) &&
			GenericUtilities.hasValue(getAtt3()) && GenericUtilities.hasValue(getIdAtt3()) &&
			GenericUtilities.hasValue(getAtt4()) && GenericUtilities.hasValue(getIdAtt4()) &&
			GenericUtilities.hasValue(getAtt5()) && GenericUtilities.hasValue(getIdAtt5()) &&
			GenericUtilities.hasValue(getAtt6()) && GenericUtilities.hasValue(getIdAtt6()) &&
			// difensori
			GenericUtilities.hasValue(getDef1()) && GenericUtilities.hasValue(getIdDef1()) &&
			GenericUtilities.hasValue(getDef2()) && GenericUtilities.hasValue(getIdDef2()) &&
			GenericUtilities.hasValue(getDef3()) && GenericUtilities.hasValue(getIdDef3()) &&
			GenericUtilities.hasValue(getDef4()) && GenericUtilities.hasValue(getIdDef4()) &&
			GenericUtilities.hasValue(getDef5()) && GenericUtilities.hasValue(getIdDef5()) &&
			GenericUtilities.hasValue(getDef6()) && GenericUtilities.hasValue(getIdDef6()) &&
			GenericUtilities.hasValue(getDef7()) && GenericUtilities.hasValue(getIdDef7()) &&
			GenericUtilities.hasValue(getDef8()) && GenericUtilities.hasValue(getIdDef8()) &&
			// centrocampisti
			GenericUtilities.hasValue(getCen1()) && GenericUtilities.hasValue(getIdCen1()) &&
			GenericUtilities.hasValue(getCen2()) && GenericUtilities.hasValue(getIdCen2()) &&
			GenericUtilities.hasValue(getCen3()) && GenericUtilities.hasValue(getIdCen3()) &&
			GenericUtilities.hasValue(getCen4()) && GenericUtilities.hasValue(getIdCen4()) &&
			GenericUtilities.hasValue(getCen5()) && GenericUtilities.hasValue(getIdCen5()) &&
			GenericUtilities.hasValue(getCen6()) && GenericUtilities.hasValue(getIdCen6()) &&
			GenericUtilities.hasValue(getCen7()) && GenericUtilities.hasValue(getIdCen7()) &&
			GenericUtilities.hasValue(getCen8()) && GenericUtilities.hasValue(getIdCen8()) &&
			// portieri
			GenericUtilities.hasValue(getGolkeep1()) && GenericUtilities.hasValue(getIdGolkeep1()) &&
			GenericUtilities.hasValue(getGolkeep2()) && GenericUtilities.hasValue(getIdGolkeep2()) &&
			GenericUtilities.hasValue(getGolkeep3()) && GenericUtilities.hasValue(getIdGolkeep3());			
	}
	
	/**
	 * metodo che ritorna vero se la squadra ha tutti gli id dei giocatori nulli
	 * @return vero se la formazione è vuota
	 */
	public Boolean isEmpty(){
		return 
			idAtt1 == null && idAtt2 == null && idAtt3 == null && 
			idAtt4 == null && idAtt5 == null && idAtt6 == null &&
			idCen1 == null && idCen2 == null && idCen3 == null &&
			idCen4 == null && idCen5 == null && idCen6 == null &&
			idCen7 == null && idCen8 == null && 
			idDef1 == null && idDef2 == null && idDef3 == null &&
			idDef4 == null && idDef5 == null && idDef6 == null &&
			idDef7 == null && idDef8 == null && 
			idGolkeep1 == null && idGolkeep2 == null && idGolkeep3 == null;
	}
	
	/**
	 * metodo che conferma se i la formazione è valida
	 * @return true se la formazione è valida
	 */
	public Boolean isCorrect(){
		return
			getDef().size() == Config.getFormations().get(formation).getFirst()	&&	
			getCen().size() == Config.getFormations().get(formation).getSecond() &&
			getAtt().size() == Config.getFormations().get(formation).getThird() &&
			getGolkeep().size() == 1 &&
			getResDef().size() == 2 &&
			getResCen().size() == 2 &&
			getResAtt().size() == 2 &&
			getResGolkeep().size() == 1;
	}

	/**
	 * @return the team
	 */	
	public Integer getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */	
	public void setTeam(Integer team) {
		this.team = team;
	}

	/**
	 * @return the day
	 */	
	public Integer getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */	
	public void setDay(Integer day) {
		this.day = day;
	}	

	/**
	 * @return the formation
	 */
	public Integer getFormation() {
		return formation;
	}

	/**
	 * @param formation the formation to set
	 */
	public void setFormation(Integer formation) {
		this.formation = formation;
	}	
	
	/**
	 * @return the def1
	 */
	public Integer getDef1() {
		return def1;
	}

	/**
	 * @param def1 the def1 to set
	 */
	public void setDef1(Integer def1) {
		this.def1 = def1;
	}

	/**
	 * @return the def2
	 */
	public Integer getDef2() {
		return def2;
	}

	/**
	 * @param def2 the def2 to set
	 */
	public void setDef2(Integer def2) {
		this.def2 = def2;
	}

	/**
	 * @return the def3
	 */
	public Integer getDef3() {
		return def3;
	}

	/**
	 * @param def3 the def3 to set
	 */
	public void setDef3(Integer def3) {
		this.def3 = def3;
	}

	/**
	 * @return the def4
	 */
	public Integer getDef4() {
		return def4;
	}

	/**
	 * @param def4 the def4 to set
	 */
	public void setDef4(Integer def4) {
		this.def4 = def4;
	}

	/**
	 * @return the def5
	 */
	public Integer getDef5() {
		return def5;
	}

	/**
	 * @param def5 the def5 to set
	 */
	public void setDef5(Integer def5) {
		this.def5 = def5;
	}

	/**
	 * @return the def6
	 */
	public Integer getDef6() {
		return def6;
	}

	/**
	 * @param def6 the def6 to set
	 */
	public void setDef6(Integer def6) {
		this.def6 = def6;
	}

	/**
	 * @return the def7
	 */
	public Integer getDef7() {
		return def7;
	}

	/**
	 * @param def7 the def7 to set
	 */
	public void setDef7(Integer def7) {
		this.def7 = def7;
	}

	/**
	 * @return the def8
	 */
	public Integer getDef8() {
		return def8;
	}

	/**
	 * @param def8 the def8 to set
	 */
	public void setDef8(Integer def8) {
		this.def8 = def8;
	}

	/**
	 * @return the cen1
	 */
	public Integer getCen1() {
		return cen1;
	}

	/**
	 * @param cen1 the cen1 to set
	 */
	public void setCen1(Integer cen1) {
		this.cen1 = cen1;
	}

	/**
	 * @return the cen2
	 */
	public Integer getCen2() {
		return cen2;
	}

	/**
	 * @param cen2 the cen2 to set
	 */
	public void setCen2(Integer cen2) {
		this.cen2 = cen2;
	}

	/**
	 * @return the cen3
	 */
	public Integer getCen3() {
		return cen3;
	}

	/**
	 * @param cen3 the cen3 to set
	 */
	public void setCen3(Integer cen3) {
		this.cen3 = cen3;
	}

	/**
	 * @return the cen4
	 */
	public Integer getCen4() {
		return cen4;
	}

	/**
	 * @param cen4 the cen4 to set
	 */
	public void setCen4(Integer cen4) {
		this.cen4 = cen4;
	}

	/**
	 * @return the cen5
	 */
	public Integer getCen5() {
		return cen5;
	}

	/**
	 * @param cen5 the cen5 to set
	 */
	public void setCen5(Integer cen5) {
		this.cen5 = cen5;
	}

	/**
	 * @return the cen6
	 */
	public Integer getCen6() {
		return cen6;
	}

	/**
	 * @param cen6 the cen6 to set
	 */
	public void setCen6(Integer cen6) {
		this.cen6 = cen6;
	}

	/**
	 * @return the cen7
	 */
	public Integer getCen7() {
		return cen7;
	}

	/**
	 * @param cen7 the cen7 to set
	 */
	public void setCen7(Integer cen7) {
		this.cen7 = cen7;
	}

	/**
	 * @return the cen8
	 */
	public Integer getCen8() {
		return cen8;
	}

	/**
	 * @param cen8 the cen8 to set
	 */
	public void setCen8(Integer cen8) {
		this.cen8 = cen8;
	}

	/**
	 * @return the att1
	 */
	public Integer getAtt1() {
		return att1;
	}

	/**
	 * @param att1 the att1 to set
	 */
	public void setAtt1(Integer att1) {
		this.att1 = att1;
	}

	/**
	 * @return the att2
	 */
	public Integer getAtt2() {
		return att2;
	}

	/**
	 * @param att2 the att2 to set
	 */
	public void setAtt2(Integer att2) {
		this.att2 = att2;
	}

	/**
	 * @return the att3
	 */
	public Integer getAtt3() {
		return att3;
	}

	/**
	 * @param att3 the att3 to set
	 */
	public void setAtt3(Integer att3) {
		this.att3 = att3;
	}

	/**
	 * @return the att4
	 */
	public Integer getAtt4() {
		return att4;
	}

	/**
	 * @param att4 the att4 to set
	 */
	public void setAtt4(Integer att4) {
		this.att4 = att4;
	}

	/**
	 * @return the att5
	 */
	public Integer getAtt5() {
		return att5;
	}

	/**
	 * @param att5 the att5 to set
	 */
	public void setAtt5(Integer att5) {
		this.att5 = att5;
	}

	/**
	 * @return the att6
	 */
	public Integer getAtt6() {
		return att6;
	}

	/**
	 * @param att6 the att6 to set
	 */
	public void setAtt6(Integer att6) {
		this.att6 = att6;
	}

	/**
	 * @return the golkeep1
	 */
	public Integer getGolkeep1() {
		return golkeep1;
	}

	/**
	 * @param golkeep1 the golkeep1 to set
	 */
	public void setGolkeep1(Integer golkeep1) {
		this.golkeep1 = golkeep1;
	}

	/**
	 * @return the golkeep2
	 */
	public Integer getGolkeep2() {
		return golkeep2;
	}

	/**
	 * @param golkeep2 the golkeep2 to set
	 */
	public void setGolkeep2(Integer golkeep2) {
		this.golkeep2 = golkeep2;
	}

	/**
	 * @return the golkeep3
	 */
	public Integer getGolkeep3() {
		return golkeep3;
	}

	/**
	 * @param golkeep3 the golkeep3 to set
	 */
	public void setGolkeep3(Integer golkeep3) {
		this.golkeep3 = golkeep3;
	}

	/**
	 * @return the idDef1
	 */
	public Integer getIdDef1() {
		return idDef1;
	}

	/**
	 * @param idDef1 the idDef1 to set
	 */
	public void setIdDef1(Integer idDef1) {
		this.idDef1 = idDef1;
	}

	/**
	 * @return the idDef2
	 */
	public Integer getIdDef2() {
		return idDef2;
	}

	/**
	 * @param idDef2 the idDef2 to set
	 */
	public void setIdDef2(Integer idDef2) {
		this.idDef2 = idDef2;
	}

	/**
	 * @return the idDef3
	 */
	public Integer getIdDef3() {
		return idDef3;
	}

	/**
	 * @param idDef3 the idDef3 to set
	 */
	public void setIdDef3(Integer idDef3) {
		this.idDef3 = idDef3;
	}

	/**
	 * @return the idDef4
	 */
	public Integer getIdDef4() {
		return idDef4;
	}

	/**
	 * @param idDef4 the idDef4 to set
	 */
	public void setIdDef4(Integer idDef4) {
		this.idDef4 = idDef4;
	}

	/**
	 * @return the idDef5
	 */
	public Integer getIdDef5() {
		return idDef5;
	}

	/**
	 * @param idDef5 the idDef5 to set
	 */
	public void setIdDef5(Integer idDef5) {
		this.idDef5 = idDef5;
	}

	/**
	 * @return the idDef6
	 */
	public Integer getIdDef6() {
		return idDef6;
	}

	/**
	 * @param idDef6 the idDef6 to set
	 */
	public void setIdDef6(Integer idDef6) {
		this.idDef6 = idDef6;
	}

	/**
	 * @return the idDef7
	 */
	public Integer getIdDef7() {
		return idDef7;
	}

	/**
	 * @param idDef7 the idDef7 to set
	 */
	public void setIdDef7(Integer idDef7) {
		this.idDef7 = idDef7;
	}

	/**
	 * @return the idDef8
	 */
	public Integer getIdDef8() {
		return idDef8;
	}

	/**
	 * @param idDef8 the idDef8 to set
	 */
	public void setIdDef8(Integer idDef8) {
		this.idDef8 = idDef8;
	}

	/**
	 * @return the idCen1
	 */
	public Integer getIdCen1() {
		return idCen1;
	}

	/**
	 * @param idCen1 the idCen1 to set
	 */
	public void setIdCen1(Integer idCen1) {
		this.idCen1 = idCen1;
	}

	/**
	 * @return the idCen2
	 */
	public Integer getIdCen2() {
		return idCen2;
	}

	/**
	 * @param idCen2 the idCen2 to set
	 */
	public void setIdCen2(Integer idCen2) {
		this.idCen2 = idCen2;
	}

	/**
	 * @return the idCen3
	 */
	public Integer getIdCen3() {
		return idCen3;
	}

	/**
	 * @param idCen3 the idCen3 to set
	 */
	public void setIdCen3(Integer idCen3) {
		this.idCen3 = idCen3;
	}

	/**
	 * @return the idCen4
	 */
	public Integer getIdCen4() {
		return idCen4;
	}

	/**
	 * @param idCen4 the idCen4 to set
	 */
	public void setIdCen4(Integer idCen4) {
		this.idCen4 = idCen4;
	}

	/**
	 * @return the idCen5
	 */
	public Integer getIdCen5() {
		return idCen5;
	}

	/**
	 * @param idCen5 the idCen5 to set
	 */
	public void setIdCen5(Integer idCen5) {
		this.idCen5 = idCen5;
	}

	/**
	 * @return the idCen6
	 */
	public Integer getIdCen6() {
		return idCen6;
	}

	/**
	 * @param idCen6 the idCen6 to set
	 */
	public void setIdCen6(Integer idCen6) {
		this.idCen6 = idCen6;
	}

	/**
	 * @return the idCen7
	 */
	public Integer getIdCen7() {
		return idCen7;
	}

	/**
	 * @param idCen7 the idCen7 to set
	 */
	public void setIdCen7(Integer idCen7) {
		this.idCen7 = idCen7;
	}

	/**
	 * @return the idCen8
	 */
	public Integer getIdCen8() {
		return idCen8;
	}

	/**
	 * @param idCen8 the idCen8 to set
	 */
	public void setIdCen8(Integer idCen8) {
		this.idCen8 = idCen8;
	}

	/**
	 * @return the idAtt1
	 */
	public Integer getIdAtt1() {
		return idAtt1;
	}

	/**
	 * @param idAtt1 the idAtt1 to set
	 */
	public void setIdAtt1(Integer idAtt1) {
		this.idAtt1 = idAtt1;
	}

	/**
	 * @return the idAtt2
	 */
	public Integer getIdAtt2() {
		return idAtt2;
	}

	/**
	 * @param idAtt2 the idAtt2 to set
	 */
	public void setIdAtt2(Integer idAtt2) {
		this.idAtt2 = idAtt2;
	}

	/**
	 * @return the idAtt3
	 */
	public Integer getIdAtt3() {
		return idAtt3;
	}

	/**
	 * @param idAtt3 the idAtt3 to set
	 */
	public void setIdAtt3(Integer idAtt3) {
		this.idAtt3 = idAtt3;
	}

	/**
	 * @return the idAtt4
	 */
	public Integer getIdAtt4() {
		return idAtt4;
	}

	/**
	 * @param idAtt4 the idAtt4 to set
	 */
	public void setIdAtt4(Integer idAtt4) {
		this.idAtt4 = idAtt4;
	}

	/**
	 * @return the idAtt5
	 */
	public Integer getIdAtt5() {
		return idAtt5;
	}

	/**
	 * @param idAtt5 the idAtt5 to set
	 */
	public void setIdAtt5(Integer idAtt5) {
		this.idAtt5 = idAtt5;
	}

	/**
	 * @return the idAtt6
	 */
	public Integer getIdAtt6() {
		return idAtt6;
	}

	/**
	 * @param idAtt6 the idAtt6 to set
	 */
	public void setIdAtt6(Integer idAtt6) {
		this.idAtt6 = idAtt6;
	}

	/**
	 * @return the idGolkeep1
	 */
	public Integer getIdGolkeep1() {
		return idGolkeep1;
	}

	/**
	 * @param idGolkeep1 the idGolkeep1 to set
	 */
	public void setIdGolkeep1(Integer idGolkeep1) {
		this.idGolkeep1 = idGolkeep1;
	}

	/**
	 * @return the idGolkeep2
	 */
	public Integer getIdGolkeep2() {
		return idGolkeep2;
	}

	/**
	 * @param idGolkeep2 the idGolkeep2 to set
	 */
	public void setIdGolkeep2(Integer idGolkeep2) {
		this.idGolkeep2 = idGolkeep2;
	}

	/**
	 * @return the idGolkeep3
	 */
	public Integer getIdGolkeep3() {
		return idGolkeep3;
	}

	/**
	 * @param idGolkeep3 the idGolkeep3 to set
	 */
	public void setIdGolkeep3(Integer idGolkeep3) {
		this.idGolkeep3 = idGolkeep3;
	}	
}