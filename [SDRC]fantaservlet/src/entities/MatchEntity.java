package entities;

public class MatchEntity {
	private Integer id;
	private Integer idTeam1;
	private Integer idTeam2;
	private Integer idDay;
	
	public MatchEntity(){
	}
	
	public MatchEntity(Integer idTeam1, Integer idTeam2, Integer idDay) {
		this.idTeam1 = idTeam1;
		this.idTeam2 = idTeam2;
		this.idDay = idDay;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdTeam1() {
		return idTeam1;
	}

	public void setIdTeam1(Integer idTeam1) {
		this.idTeam1 = idTeam1;
	}

	public Integer getIdTeam2() {
		return idTeam2;
	}

	public void setIdTeam2(Integer idTeam2) {
		this.idTeam2 = idTeam2;
	}

	public Integer getIdDay() {
		return idDay;
	}

	public void setIdDay(Integer idDay) {
		this.idDay = idDay;
	}
}
