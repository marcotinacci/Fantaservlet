package servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Servlet implementation class PrintPDF
 */
@WebServlet("/PrintPDF")
public class PrintPDF extends HttpServlet {
	
//    private static final String TITLE = "Stampa PDF";

	/**
     * @see HttpServlet#HttpServlet()
     */
    public PrintPDF() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException 
	{
//		response.setContentType("text/html");		
//	    PrintWriter out = response.getWriter();
//	    out.println(Style.pageHeader(TITLE));
	    
	    
		Document doc = new Document();
		File file = new File("file.pdf");
		// TODO aggiungere al nome del file l'id dell'utente che lo richiede
		FileOutputStream os = new FileOutputStream(file);
		String str = file.getAbsolutePath();
		try {
			PdfWriter.getInstance(doc,os);
			doc.open();
			doc.add(new Paragraph("Hello World!"));
			doc.close();
			doDownload(request, response, str, "filepdf");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	/**
     *  Sends a file to the ServletResponse output stream.  Typically
     *  you want the browser to receive a different name than the
     *  name the file has been saved in your local database, since
     *  your local names need to be unique.
     *
     *  @param req The request
     *  @param resp The response
     *  @param filename The name of the file you want to download.
     *  @param original_filename The name the browser should receive.
     */
    private void doDownload( HttpServletRequest req, HttpServletResponse resp,
                             String filename, String original_filename )
        throws IOException
    {
        File f = new File(filename);
        int length = 0;
        ServletOutputStream op = resp.getOutputStream();
        ServletContext context  = getServletConfig().getServletContext();
        String mimetype = context.getMimeType( filename );

        //  Set the response and go!
        resp.setContentType( (mimetype != null) ? mimetype : "application/pdf" );
        resp.setContentLength( (int)f.length() );
        resp.setHeader( "Content-Disposition", "attachment; filename=\"" + original_filename + "\"" );

        //  Stream to the requester.
        byte[] bbuf = new byte[3000000];
        DataInputStream in = new DataInputStream(new FileInputStream(f));

        while ((in != null) && ((length = in.read(bbuf)) != -1))
        {
            op.write(bbuf,0,length);
        }

        in.close();
        op.flush();
        op.close();
    }
}
