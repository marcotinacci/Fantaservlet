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
import org.apache.poi.ss.usermodel.Row;
import dataconnection.MySQLConnection;
import entities.PlayerEntity;
import entities.ReportEntity;

/**
 * Implementazione dei metodi di lettura da file Excel (xls)
 * @author Markov
 */
public class ReadXLS implements IReadFile {

	// TODO come eliminare la connessione al database da getreports?
	
	@Override
	public List<ReportEntity> getReports(InputStream in) {
		List<ReportEntity> lr = new ArrayList<ReportEntity>();
		try {
			// estrai i dati dal file xls
			POIFSFileSystem fileSystem = null;

			fileSystem = new POIFSFileSystem(in);
			HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workBook.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			
			// connessione al database
			MySQLConnection dbc = new MySQLConnection();
			dbc.init();
			
			// salta la prima riga di titolo
			rows.next();
			// per ogni calciatore
			while(rows.hasNext()){
				Row row = rows.next();
				// recupera l'id del calciatore da database
				Integer pid = dbc.getPlayerId(
					// nome calciatore
					row.getCell(1).getStringCellValue(),
					// squadra di provenienza
					row.getCell(2).getStringCellValue());

				// gol segnati (1)
				for(int i = (int)row.getCell(5).getNumericCellValue(); i > 0; i--){
					lr.add(new ReportEntity(1, null, pid));
				}
				// rigore sbagliato (2)
				for(int i = (int)row.getCell(13).getNumericCellValue(); i > 0; i--){
					lr.add(new ReportEntity(2, null, pid));
				}
				// rigore parato (3)
				for(int i = (int)row.getCell(12).getNumericCellValue(); i > 0; i--){
					lr.add(new ReportEntity(3, null, pid));
				}				
				// autogol (4)
				for(int i = (int)row.getCell(14).getNumericCellValue(); i > 0; i--){
					lr.add(new ReportEntity(4, null, pid));
				}
				// gol subito (5)
				for(int i = (int)row.getCell(6).getNumericCellValue(); i > 0; i--){
					lr.add(new ReportEntity(5, null, pid));
				}				
				// espulsione (6)
				for(int i = (int)row.getCell(11).getNumericCellValue(); i > 0; i--){
					lr.add(new ReportEntity(6, null, pid));
				}
				// ammonizione (7)
				for(int i = (int)row.getCell(10).getNumericCellValue(); i > 0; i--){
					lr.add(new ReportEntity(7, null, pid));
				}
			}	
		}catch(IOException e){
			lr = null;
			e.printStackTrace();
		}catch(SQLException sqle){
			lr = null;
			sqle.printStackTrace();				
		}
		return lr;
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