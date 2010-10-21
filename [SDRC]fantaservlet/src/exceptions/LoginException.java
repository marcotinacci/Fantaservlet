package exceptions;

/**
 * classe di eccezione lanciata da un'azione scorretta di login
 * @author Markov
 *
 */
public class LoginException extends Exception {

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return "Errore di login.";
	}
	
}
