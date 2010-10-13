package login;

/**
 * eccezione di formato dei dati errato nel login
 * @author Markov
 *
 */
public class WrongLoginInputException extends LoginException {

	/**
	 * il messaggio informa che l'errore riguarda i dati inseriti
	 * @see login.LoginException#getMessage()
	 */
	@Override
	public String getMessage() {
		return super.getMessage() + " Nome utente o password sono incorretti.";
	}
	
}
