package entities;

import java.util.Date;

import utils.GenericUtilities;

public class DayEntity {
	private Integer id;
	private Integer idChampionship;
	private Date date;
	private Boolean close = false;
	private Boolean evaluated = false;

	public DayEntity(){
	}
	
	public DayEntity(Integer idChampionship, Date date, Boolean close, Boolean evaluated) {
		this.idChampionship = idChampionship;
		this.date = date;
		this.close = close;
		this.evaluated = evaluated;
	}			
	
	public DayEntity(Integer id, Integer idChampionship, Date date, Boolean close, Boolean evaluated) {
		this.id = id;
		this.idChampionship = idChampionship;
		this.date = date;
		this.close = close;
		this.evaluated = evaluated;
	}		
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdChampionship() {
		return idChampionship;
	}

	public void setIdChampionship(Integer idChampionship) {
		this.idChampionship = idChampionship;
	}

	public Date getDate() {
		return date;
	}
	public String getFormatDate(){
		return String.format("%1$tY-%1$tm-%1$td", getDate());
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean isEvaluated() {
		return evaluated;
	}

	public void setEvaluated(Boolean evaluated) {
		this.evaluated = evaluated;
	}	
	
	public Boolean isClose() {
		return close;
	}

	public void setClose(Boolean close) {
		this.close = close;
	}
	
	public Boolean isComplete(){
		return getDate() != null &&
			GenericUtilities.hasValue(getIdChampionship());
	}
}
