package entities;

import utils.GenericUtilities;

public class GiudgeEntity {
	
	private Integer id;
	private Integer day;
	private Integer player;
	private Double vote;
	
	public GiudgeEntity(){
	}
	
	public GiudgeEntity(Integer day, Integer player, Double vote){
		this.day = day;
		this.player = player;
		this.vote = vote;
	}
	
	public GiudgeEntity(Integer id, Integer day, Integer player, Double vote){
		this.id = id;
		this.day = day;
		this.player = player;
		this.vote = vote;
	}	
	
	public Boolean isComplete(){
		return GenericUtilities.hasValue(day) &&
		GenericUtilities.hasValue(player) &&
		GenericUtilities.hasValue(vote);
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the player
	 */
	public Integer getPlayer() {
		return player;
	}
	/**
	 * @param player the player to set
	 */
	public void setPlayer(Integer player) {
		this.player = player;
	}
	/**
	 * @return the vote
	 */
	public Double getVote() {
		return vote;
	}
	/**
	 * @param vote the vote to set
	 */
	public void setVote(Double vote) {
		this.vote = vote;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GudgeEntity [day=" + day + ", player=" + player + ", vote="
				+ vote + "]";
	}	
	
}
