package calendargenerator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.GenericUtilities;

import dataconnection.MySQLConnection;
import entities.CalendarEntity;
import entities.DayEntity;
import entities.MatchEntity;
import entities.TeamEntity;

public class CalendarGenerator {
	private CalendarEntity calendar;
	private Long time;

	public CalendarGenerator(CalendarEntity calendar, Long time) {
		this.calendar = calendar;
		this.time = time;
	}

	public CalendarEntity getCalendar() {
		return calendar;
	}

	public void setCalendar(CalendarEntity calendar) {
		this.calendar = calendar;
	}
	
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}	
	
	public void generate() throws BadTeamsNumberException, SQLException
	{
		Integer nTeam;
		Integer nDay;
		
		// connessione al database
		MySQLConnection dc = new MySQLConnection();
		dc.init();
		// prendi la lista delle squadre
		List<TeamEntity> lt = dc.getTeamsOfChampionship(calendar.getIdChampionship());
		// numero squadre
		nTeam = lt.size();
		if(nTeam % 2 == 1 || nTeam < 6 || nTeam > 12){
			// se il numero di squadre è dispari o fuori dall'intervallo [6,12]
			throw new BadTeamsNumberException(nTeam);
		}
		// numero delle giornate, andata e ritorno
		nDay = (nTeam-1)*2;
		
		// genera le GIORNATE
		List<DayEntity> lde = new ArrayList<DayEntity>();
		for(Integer i = 0; i < nDay; i++, time += 1000*60*60*24*7){
			// crea giornata
			DayEntity d = new DayEntity(
				// campionato di appartenenza
				calendar.getIdChampionship(),
				// data della giornata incrementata di i settimane
				new Date(time),
				// la giornata viene creata aperta a modifiche
				false,
				// la giornata deve essere ancora valutata
				false
			);
			lde.add(d);
		}
		// inserisci le giornate nel database
		dc.insertDays(lde);
		
		// TODO la lista delle giornate è già presente, è necessario riprendere tutti i dati dal database per recuperare gli id autogenerati?
		// recupera la lista delle giornate complete di identificativo
		lde = dc.getDayOfChampionship(calendar.getIdChampionship());
		// prepara la lista delle partite
		List<MatchEntity> lme = new ArrayList<MatchEntity>();
		// genera le PARTITE per ogni giornata
		for(int i=0; i<nDay; i++){
			for(int j=0; j < nTeam/2; j++){
				if(i < nDay/2){
					// andata
					lme.add(new MatchEntity(
						lt.get(j).getId(),
						lt.get(nTeam-1-j).getId(),
						lde.get(i).getId()
					));
				}else{
					// ritorno
					lme.add(new MatchEntity(
						lt.get(nTeam-1-j).getId(),
						lt.get(j).getId(),
						lde.get(i).getId()
					));					
				}
			}
			GenericUtilities.rotate(lt, 1, lt.size()-1);
		}
		dc.insertMatches(lme);
		// chiudi connessione al database
		dc.destroy();
	}
	
}
