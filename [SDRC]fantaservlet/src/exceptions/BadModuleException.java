package exceptions;

public class BadModuleException extends Exception {
	@Override
	public String getMessage() {
		return "A wrong formation has been selected";
	}
}
