package entities;

import utils.GenericUtilities;

public class CalendarEntity{
	private Integer idChampionship;
	private String name;
	
	public CalendarEntity() {
	}
	public Integer getIdChampionship() {
		return idChampionship;
	}
	public void setIdChampionship(Integer idChampionship) {
		this.idChampionship = idChampionship;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public Boolean isComplete(){
		return GenericUtilities.hasValue(getName());
	}
}
