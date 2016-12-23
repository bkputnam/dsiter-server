package dsiter.server;

import dsiter.dataset.IDataset;
import dsiter.iterator.IDatasetIterator;
import dsiter.pipe.IPipe;
import dsiter.server.exceptions.ClientErrorException;
import dsiter.writer.CsvWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

public abstract class DatasetServlet extends HttpServlet {

	private IDataset dataset;
	private PipeParser pipeParser;

	public DatasetServlet(IDataset dataset) {
		this(dataset, new DefaultPipeMap());
	}

	public DatasetServlet(IDataset dataset, IPipeMap pipeMap) {
		this.dataset = dataset;
		this.pipeParser = new PipeParser(pipeMap);
	}

	public void init() { /* no setup needed? */ }

	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
		throws ServletException, IOException
	{
		String pipeString = null;
		String queryString = request.getQueryString();
		if (queryString == null) {
			pipeString = "";
		}
		else {
			pipeString = URLDecoder.decode(queryString,"utf8");
		}

		IPipe[] pipes = null;
		try {
			pipes = pipeParser.parsePipes(pipeString);
		}
		catch (ClientErrorException e) {
			response.setStatus(e.getStatusCode());
			response.setContentType("text/plain");
			response.getWriter().print(e.getMessage());
			return;
		}

		IDatasetIterator it = null;
		try {
			it = dataset.getIterator();
		}
		catch (Exception e) {
			response.setStatus(500);
			response.setContentType("text/plain");

			PrintWriter w = response.getWriter();
			w.println(e.toString() + "\n");
			e.printStackTrace(w);
			return;
		}

		for(IPipe p : pipes) {
			it = it.pipe(p);
		}

		// TODO - don't hardcode CSV writer

		// Set response content type
		response.setContentType("text/csv");

		// Actual logic goes here.
		CsvWriter writer = new CsvWriter();
		try {
			response.setContentType(writer.getMimeType().toString());
			writer.writeTo(it, response.getOutputStream());
		}
		catch (Exception e) {
			if (!response.isCommitted()) {
				response.setStatus(500);
				response.setContentType("text/plain");
			}

			PrintWriter w = response.getWriter();
			w.println(e.toString());
			e.printStackTrace(w);
			return;
		}
	}

	public void destroy()
	{
		// do nothing.
	}
}
