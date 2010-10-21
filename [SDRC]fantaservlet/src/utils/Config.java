package utils;

import java.util.ArrayList;
import java.util.List;

import exceptions.BadModuleException;

public class Config {
	
	// lista di formazioni possibili
	private static final List<Tern<Integer, Integer, Integer>> formations;
	
	static{
		formations = new ArrayList<Tern<Integer, Integer, Integer>>();
		// formazioni possibili
		formations.add(new Tern<Integer, Integer, Integer>(3, 4, 3));
		formations.add(new Tern<Integer, Integer, Integer>(3, 5, 2));
		formations.add(new Tern<Integer, Integer, Integer>(4, 5, 1));
		formations.add(new Tern<Integer, Integer, Integer>(4, 4, 2));
		formations.add(new Tern<Integer, Integer, Integer>(4, 3, 3));
		formations.add(new Tern<Integer, Integer, Integer>(5, 4, 1));
		formations.add(new Tern<Integer, Integer, Integer>(5, 3, 2));		
	}
	
	static public List<Tern<Integer, Integer, Integer>> getFormations(){
		return formations;
	}
	
	/**
	 * metodo che ritorna l'indice della formazione nell'array dato il modulo
	 * @param def numero di difensori
	 * @param cen numero di centrocampisti
	 * @param att numero di attaccanti
	 * @return indice del vettore di formazioni
	 * @throws BadModuleException sollevata quando la formazione non è presente nel vettore
	 */
	static public Integer getFormationIndex(Integer def, Integer cen, Integer att) 
			throws BadModuleException{
		for(int i = 0; i < formations.size(); i++){
			Tern<Integer, Integer, Integer> tern = formations.get(i);
			if(tern.getFirst() == def && tern.getSecond() == cen && tern.getThird() == att){
				return i;
			}
		}
		throw new BadModuleException();
	}
}
