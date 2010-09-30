package entities;

public class VoteEntity {
	private Integer id;
	private String action;
	private Float point;
	
	public VoteEntity() {
	}
	
	public VoteEntity(String action, Float point) {
		this.action = action;
		this.point = point;
	}

	public VoteEntity(Integer id, String action, Float point) {
		this.id = id;
		this.action = action;
		this.point = point;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Float getPoint() {
		return point;
	}
	public void setPoint(Float point) {
		this.point = point;
	}
}
