package utils;

import entities.DayEntity;
import entities.TeamEntity;

public class Match {
	private DayEntity day;
	private TeamEntity team1;
	private TeamEntity team2;
	private Double pointsTeam1;
	private Double pointsTeam2;
	
	/**
	 * costruttore di default
	 */
	public Match(){}
	
	/**
	 * costruttore
	 * @param day giornata
	 * @param team1 prima squadra
	 * @param team2 seconda squadra
	 * @param pointsTeam1 punti prima squadra
	 * @param pointsTeam2 punti seconda squadra
	 * @param evaluatedDay flag giorno valutato
	 */
	public Match(DayEntity day, TeamEntity team1, TeamEntity team2,
			Double pointsTeam1, Double pointsTeam2) {
		super();
		this.day = day;
		this.team1 = team1;
		this.team2 = team2;
		this.pointsTeam1 = pointsTeam1;
		this.pointsTeam2 = pointsTeam2;
	}


	/**
	 * @return the day
	 */
	public DayEntity getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(DayEntity day) {
		this.day = day;
	}
	/**
	 * @return the team1
	 */
	public TeamEntity getTeam1() {
		return team1;
	}
	/**
	 * @param team1 the team1 to set
	 */
	public void setTeam1(TeamEntity team1) {
		this.team1 = team1;
	}
	/**
	 * @return the team2
	 */
	public TeamEntity getTeam2() {
		return team2;
	}
	/**
	 * @param team2 the team2 to set
	 */
	public void setTeam2(TeamEntity team2) {
		this.team2 = team2;
	}
	/**
	 * @return the pointsTeam1
	 */
	public Double getPointsTeam1() {
		return pointsTeam1;
	}
	/**
	 * @param pointsTeam1 the pointsTeam1 to set
	 */
	public void setPointsTeam1(Double pointsTeam1) {
		this.pointsTeam1 = pointsTeam1;
	}
	/**
	 * @return the pointsTeam2
	 */
	public Double getPointsTeam2() {
		return pointsTeam2;
	}
	/**
	 * @param pointsTeam2 the pointsTeam2 to set
	 */
	public void setPointsTeam2(Double pointsTeam2) {
		this.pointsTeam2 = pointsTeam2;
	}
	
}
