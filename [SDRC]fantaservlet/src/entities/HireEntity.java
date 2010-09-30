package entities;

public class HireEntity {
	private Integer id;
	private Integer player;
	private Integer team;
	
	public HireEntity() {
	}
	public HireEntity(Integer player, Integer team) {
		this.player = player;
		this.team = team;
	}
	public HireEntity(Integer id, Integer player, Integer team) {
		this.id = id;
		this.player = player;
		this.team = team;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPlayer() {
		return player;
	}
	public void setPlayer(Integer player) {
		this.player = player;
	}
	public Integer getTeam() {
		return team;
	}
	public void setTeam(Integer team) {
		this.team = team;
	}
	
	
}
