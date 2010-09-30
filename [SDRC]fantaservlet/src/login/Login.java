package login;

import javax.servlet.http.HttpSession;

public class Login {
	private String nome;
	private String password;
	private boolean admin;
	private boolean logged;
	
	public Login(HttpSession req) {
		this(req.getAttribute("name").toString(),req.getAttribute("password").toString());		
	}	
	
	public Login(String nome, String password){
		this.nome = nome;
		this.password = password;
		
		if(nome.equals("marco") && password.equals("marco")){
			logged = true;
			admin = true;
		}else if(nome.equals("guest") && password.equals("guest")){
			logged = true;
			admin = false;
		}else{
			logged = false;
			admin = false;
		}
		/*
		DBConnection dbc = new DBConnection();
		dbc.init();		
		List<User> lu = dbc.getUsers();
		dbc.destroy();
		User u = new User(nome, password);
		logged = false;
		admin = false;
		for(Iterator<User> it = lu.listIterator(); it.hasNext();){
			User t = it.next();
			if(t.equals(u)){
				logged = true;
				admin = t.isAdmin();
				break;
			}
		}*/
		
		
		
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public boolean isLogged(){
		return logged;
	}
	
	public boolean isAdmin(){
		if(!isLogged()) throw new RuntimeException("Richiesta isAdmin su utente non loggato");
		return admin;
	}
}