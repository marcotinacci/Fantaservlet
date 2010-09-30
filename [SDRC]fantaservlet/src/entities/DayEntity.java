package entities;

import java.util.Date;

import utils.GenericUtilities;

public class DayEntity {
	private Integer id;
	private Integer idChampionship;
	private Date date;
	private Boolean close = false;
	
	public DayEntity(){
		date = null;
	}
	
	public DayEntity(Integer idChampionship, Date date) {
		this.idChampionship = idChampionship;
		this.date = date;
	}	
	
	public DayEntity(Integer id, Integer idChampionship, Date date) {
		this.id = id;
		this.idChampionship = idChampionship;
		this.date = date;
	}
	
	public DayEntity(Integer idChampionship, Date date, Boolean close) {
		this.idChampionship = idChampionship;
		this.date = date;
		this.close = close;
	}		

	public DayEntity(Integer id, Integer idChampionship, Date date, Boolean close) {
		this.id = id;
		this.idChampionship = idChampionship;
		this.date = date;
		this.close = close;
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
