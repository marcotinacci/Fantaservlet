package dataimport;

import java.io.InputStream;
import java.util.List;

import utils.Pair;

import entities.JudgeEntity;
import entities.PlayerEntity;
import entities.ReportEntity;

/**
 * Interfaccia di lettura giocatori e voti da file, si assume come precondizione per metodi che la struttura
 * dei file sia ben formato secondo le specifiche di ogni implementazione dell'interfaccia
 * @author Markov
 */
public interface IReadFile {
	/**
	 * metodo che estrae i dati dei calciatori da un file
	 * @param req richiesta http
	 * @param fileName nome del file salvato in locale
	 * @return lista di calciatori completi di nome e squadra di provenienza
	 */
	public List<PlayerEntity> getPlayers(InputStream in);
	/**
	 * metodo che estrai i dati dei voti (reports) da un file
	 * @param req richiesta http
	 * @param fileName nome del file salvato in locale
	 * @return coppia di lista dei voti e lista dei giudizi completi di codice voto 
	 * e codice calciatore (il codice giornata è nullo e deve essere impostato 
	 * successivamente)
	 */
	public Pair<List<ReportEntity>,List<JudgeEntity>> getReports(InputStream in);
}
