package utils;

/**
 * Classe che gestisce il concetto di coppia
 * @author Markov
 * @param <F> primo elemento
 * @param <S> secondo elemento
 */
public class Pair<F,S> {
	private F first;
	private S second;

	public Pair(F first, S second){
		this.first = first;
		this.second = second;
	}	
	
	public F getFirst() {
		return first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public S getSecond() {
		return second;
	}

	public void setSecond(S second) {
		this.second = second;
	}
	
	public String toString(){
		return null;
	}
}
