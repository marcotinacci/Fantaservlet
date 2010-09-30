package entities;

import utils.GenericUtilities;

public class CalendarEntity{
	private Integer idChampionship;
	private Long startDate;
	
	public CalendarEntity() {
	}
	public Integer getIdChampionship() {
		return idChampionship;
	}
	public void setIdChampionship(Integer idChampionship) {
		this.idChampionship = idChampionship;
	}
	public Long getStartDate() {
		return startDate;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	
	public Boolean isComplete(){
		return GenericUtilities.hasValue(getIdChampionship()) &&
			GenericUtilities.hasValue(getStartDate());
	}
}
