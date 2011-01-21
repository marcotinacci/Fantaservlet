package dataimport;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import utils.Pair;
import dataconnection.MySQLConnection;
import entities.JudgeEntity;
import entities.PlayerEntity;
import entities.ReportEntity;

/**
 * Implementazione dei metodi di lettura da file Excel (xls)
 * @author Markov
 */
public class ReadXLS implements IReadFile {

	// TODO come eliminare la connessione al database da getreports?
	
	@Override
	public Pair<List<ReportEntity>,List<JudgeEntity>> getReports(InputStream in) {
		// lista dei report delle azioni
		List<ReportEntity> lr = new ArrayList<ReportEntity>();
		// lista dei giudizi dei calciatori
		List<JudgeEntity> lg = new ArrayList<JudgeEntity>();
		try {
			// estrai i dati dal file xls
			POIFSFileSystem fileSystem = null;

			fileSystem = new POIFSFileSystem(in);
			HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workBook.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			
			// salta la prima riga di titolo
			rows.next();
			// per ogni calciatore
			while(rows.hasNext()){
				Row row = rows.next();
				// recupera l'id del calciatore da database
				Integer pid = MySQLConnection.getPlayerId(
					// nome calciatore
					row.getCell(1).getStringCellValue(),
					// squadra di provenienza
					row.getCell(2).getStringCellValue());
				
				// giudizio
				if(row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC){
					double temp = row.getCell(4).getNumericCellValue();
					lg.add(new JudgeEntity(null, pid, temp));
				}
				
				// gol segnati (1)
				for(int i = row.getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC ? (int)row.getCell(5).getNumericCellValue() : 0
						; i > 0; i--){
					lr.add(new ReportEntity(1, null, pid));
				}
				// rigore sbagliato (2)
				for(int i = row.getCell(13).getCellType() == Cell.CELL_TYPE_NUMERIC ? (int)row.getCell(13).getNumericCellValue() : 0
						; i > 0; i--){
					lr.add(new ReportEntity(2, null, pid));
				}
				// rigore parato (3)
				for(int i = row.getCell(12).getCellType() == Cell.CELL_TYPE_NUMERIC ? (int)row.getCell(12).getNumericCellValue() : 0
						; i > 0; i--){
					lr.add(new ReportEntity(3, null, pid));
				}				
				// autogol (4)
				for(int i = row.getCell(14).getCellType() == Cell.CELL_TYPE_NUMERIC ? (int)row.getCell(14).getNumericCellValue() : 0
						; i > 0; i--){
					lr.add(new ReportEntity(4, null, pid));
				}
				// gol subito (5)
				for(int i = row.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC ? (int)row.getCell(6).getNumericCellValue() : 0
						; i > 0; i--){
					lr.add(new ReportEntity(5, null, pid));
				}				
				// espulsione (6)
				for(int i = row.getCell(11).getCellType() == Cell.CELL_TYPE_NUMERIC ? (int)row.getCell(11).getNumericCellValue() : 0
						; i > 0; i--){
					lr.add(new ReportEntity(6, null, pid));
				}
				// ammonizione (7)
				for(int i = row.getCell(10).getCellType() == Cell.CELL_TYPE_NUMERIC ? (int)row.getCell(10).getNumericCellValue() : 0
						; i > 0; i--){
					lr.add(new ReportEntity(7, null, pid));
				}
			}
		}catch(IOException e){
			lg = null;
			lr = null;
			e.printStackTrace();
		}catch(SQLException sqle){
			lg = null;
			lr = null;
			sqle.printStackTrace();				
		}
		return new Pair<List<ReportEntity>, List<JudgeEntity>>(lr, lg);
	}
	
	@Override
	public List<PlayerEntity> getPlayers(InputStream in) {
		List<PlayerEntity> lp = new ArrayList<PlayerEntity>();		
		try {
			// estrai i dati dal file xls
			POIFSFileSystem fileSystem = null;

			fileSystem = new POIFSFileSystem(in);
			HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workBook.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			// salta la prima riga di titolo
			rows.next();
			// per ogni riga aggiungi un giocatore alla lista
			while(rows.hasNext()){
				Row row = rows.next();
				lp.add(new PlayerEntity(
					// nome
					row.getCell(1).getStringCellValue(),
					// codice ruolo
					row.getCell(3).getStringCellValue().charAt(0),
					// squadra di provenienza
					row.getCell(2).getStringCellValue()
				));
			}	
		}catch(IOException e){
			e.printStackTrace();
		}		
		return lp;
	}	
}
