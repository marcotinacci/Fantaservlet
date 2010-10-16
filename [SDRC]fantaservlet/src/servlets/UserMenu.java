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
 * Servlet implementation class UserMenu
 */
@WebServlet("/UserMenu")
public class UserMenu extends HttpServlet {
	private static final String TITLE = "Menu Utente";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserMenu() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(Style.pageHeader(TITLE));
		GenericUtilities.checkLoggedIn(request, response, false);

		// stampa menu utente
		List<String> items = new ArrayList<String>();
		items.add("<a href=\"FormationHandling\">Gestisci le Formazioni</a>");
		items.add("<a href=\"ShowResults\">Visualizza i Risultati</a>");
		items.add("<a href=\"PrintPDF\">Stampa PDF</a>");
		items.add("<a href=\"Logout\">Logout</a>");
		out.println(Style.showList(items, false));
		
		out.println(Style.pageFooter(false));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
