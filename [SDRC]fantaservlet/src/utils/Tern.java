package utils;

/**
 * classe che modella il concetto di terna di tre elementi distinti
 * @author Markov
 * @param <F> primo elemento
 * @param <S> secondo elemento
 * @param <T> terzo elemento
 */
public class Tern <F,S,T> extends Pair <F,S> {

	private T third;
	
	/**
	 * costruttore
	 * @param first
	 * @param second
	 * @param third
	 */
	public Tern(F first, S second, T third) {
		super(first, second);
		this.third = third;
	}

	/**
	 * @return terzo elemento
	 */
	public T getThird() {
		return third;
	}

	/**
	 * @param terzo elemento
	 */
	public void setThird(T third) {
		this.third = third;
	}	
	
	
	
}
