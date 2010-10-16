package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.GenericUtilities;
import view.Style;

/**
 * Servlet implementation class AdminMenu
 */
@WebServlet("/AdminMenu")
public class AdminMenu extends HttpServlet {
	private static final String TITLE = "Amministrazione";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminMenu() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(Style.pageHeader(TITLE));
		GenericUtilities.checkLoggedIn(request, response, true);
		List<String> items = new ArrayList<String>();
		items.add("<a href=\"AddUser\">Crea Utente</a>");
		items.add("<a href=\"AddChampionship\">Crea Campionato</a>");
		items.add("<a href=\"AddTeam\">Crea Squadra</a>");
		items.add("<a href=\"AddPlayer\">Crea Calciatore</a>");
		items.add("<a href=\"HirePlayers\">Convoca Calciatori</a>");
		items.add("<a href=\"ComputeCalendar\">Genera Giornate di Campionato</a>");
		items.add("<a href=\"DayHandling\">Gestisci le Giornate</a>");
		items.add("<a href=\"AddReport\">Assegna i Voti</a>");
		items.add("<a href=\"ImportPlayers\">Importa i Calciatori</a>");
		items.add("<a href=\"ImportReports\">Importa i Voti</a>");
		items.add("<a href=\"Logout\">Logout</a>");
		out.println(Style.showList(items, false));
		out.println(Style.pageFooter(false));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
