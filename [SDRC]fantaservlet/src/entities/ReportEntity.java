package entities;

import utils.GenericUtilities;

public class ReportEntity {
	private Integer id;
	private Integer vote;
	private Integer day;
	private Integer player;
	
	public ReportEntity() {
	}

	public ReportEntity(Integer vote, Integer day, Integer player) {
		this.vote = vote;
		this.day = day;
		this.player = player;
	}

	public ReportEntity(Integer id, Integer vote, Integer day, Integer player) {
		this.id = id;
		this.vote = vote;
		this.day = day;
		this.player = player;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getPlayer() {
		return player;
	}

	public void setPlayer(Integer player) {
		this.player = player;
	}
	
	public Boolean isComplete(){
		return GenericUtilities.hasValue(getVote()) &&
			GenericUtilities.hasValue(getDay()) &&
			GenericUtilities.hasValue(getPlayer());
	}

	@Override
	public String toString() {
		return "ReportEntity [vote=" + vote + ", day=" + day + ", player="
				+ player + "]";
	}
}
