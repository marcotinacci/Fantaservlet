package entities;

public class User {

	private String nome;
	private String password;
	private boolean admin;
	
	public User() {}
	
	public User(String nome, String password){
		this.nome = nome;
		this.password = password;
	}	
	
	public User(String nome, String password, boolean admin){
		this.nome = nome;
		this.password = password;
		this.admin = admin;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isAdmin(){
		return admin;
	}
	
	public void setAdmin(boolean admin){
		this.admin = admin;
	}
	
	@Override
	public boolean equals(Object obj) {
		return nome.equals(((User)obj).nome) && 
			password.equals(((User)obj).password);
	}
}
