package calendargenerator;

public class BadTeamsNumberException extends Exception {

	private static final long serialVersionUID = 1L;
	private Integer numTeams;
	
	public BadTeamsNumberException(Integer n){
		this.numTeams = n;
	}

	public Integer getNumTeams() {
		return numTeams;
	}

	public void setNumTeams(Integer numTeams) {
		this.numTeams = numTeams;
	}
	
	public Boolean isInRange(){
		return numTeams >= 6 && numTeams <= 12;
	}
	
	public Boolean isEven(){
		return (numTeams % 2) == 0;
	}
}
