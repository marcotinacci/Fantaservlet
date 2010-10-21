package exceptions;

/**
 * eccezione che indica campi lasciati vuoti in un login
 * @author Markov
 *
 */
public class BlankLoginInfoException extends LoginException {

	/**
	 * metodo che informa che l'errore riguarda campi lasciati in bianco
	 * @see exceptions.LoginException#getMessage()
	 */
	@Override
	public String getMessage() {
		return super.getMessage() + " I campi nome utente e password non possono essere lasciati vuoti.";
	}
	
}
