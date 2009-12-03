package fourstore;

import com.clarkparsia.sesame.utils.ExtendedGraph;
import com.clarkparsia.sesame.utils.SesameIO;
import com.clarkparsia.utils.io.IOUtil;
import fourstore.api.Format;
import fourstore.api.results.Binding;
import fourstore.api.QueryException;
import fourstore.api.results.ResultSet;
import fourstore.api.Store;
import fourstore.impl.StoreImpl;
import fourstore.impl.TabbedResultSetFormatter;
import org.openrdf.model.Statement;
import org.openrdf.sesame.constants.RDFFormat;
import web.Method;
import web.Request;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

/**
 * Title: <br/>
 * Description: <br/>
 * Company: Clark & Parsia, LLC. <http://clarkparsia.com><br/>
 * Created: Dec 1, 2009 2:45:04 PM<br/>
 *
 * @author Michael Grove <mike@clarkparsia.com><br/>
 */
public class Test {
	public static void main(String[] args) throws Exception {
		Store aStore = new StoreImpl(new URL("http://hume.int.clarkparsia.com:8000/"));

//		System.err.println(IOUtil.getURLAsString(new URL("http://hume.int.clarkparsia.com:8000/status")));
//
//		System.err.println(new Request(Method.HEAD, new URL("http://hume.int.clarkparsia.com:8000/status")).execute().getContent());
		
		aStore.connect();
		
		System.err.println("Status: " + aStore.status());

		System.err.println(aStore.size());

		String aQuery = "select distinct ?p where { ?s ?p ?o }";

		// this is actually not an easy query on a big result set since the spo pulls in the entire db
		aStore.setSoftLimit(1000);

		if (aStore.getSoftLimit() != 1000) {
			throw new Exception("setting soft limit did not work");
		}

		ResultSet aResults = aStore.query(aQuery);

		System.err.println("There are " + aResults.size() + " results.");

		for (Binding aBinding : aResults) {
			System.err.println("p = " + aBinding.get("p"));
		}

		aQuery = "select ?s ?aLabel where {?s rdf:type ?type. ?s rdfs:label ?aLabel.}";

		aResults = aStore.query(aQuery);

		System.err.println(new TabbedResultSetFormatter().format(aResults));
	}

	private static void goodTest() throws Exception {
		// TODO: refactor into junit tests

		Store aStore = StoreFactory.create(new URL("http://hume.int.clarkparsia.com/"));

		aStore.connect();

		String aQuery = "select ?p where { ?s ?p ?o }";

		// this is actually not an easy query on a big result set since the spo pulls in the entire db
		aStore.setSoftLimit(1000);

		if (aStore.getSoftLimit() != 1000) {
			throw new Exception("setting soft limit did not work");
		}

		ResultSet aResults = aStore.query(aQuery);

		System.err.println("There are " + aResults.size() + " results.");

		for (Binding aBinding : aResults) {
			System.err.println("p = " + aBinding.get("p"));
		}

		aQuery = "select ?uri ?aLabel where {?s rdf:type ?type. ?s rdfs:label ?aLabel.}";

		aResults = aStore.query(aQuery);

		System.err.println(new TabbedResultSetFormatter().format(aResults));

		aQuery = "This is not a valid query";

		try {
			aStore.query(aQuery);
		}
		catch (QueryException ex) {
			System.err.println("we expected this...");
			ex.printStackTrace();
		}

		// TODO: need some tests to make sure we actually got the correct query results.
		// TODO: need tests for graph queries, describe & ask.

		// try adding some data
		aStore.add(getDataToAdd(), Format.Turtle, null);

		verifyAdd(aStore, null, getDataToAdd());

		aStore.add(getOtherDataToAdd(), Format.Turtle, getGraphURI());

		verifyAdd(aStore, getGraphURI(), getOtherDataToAdd());

		if (aStore.size() != getExpectedSize()) {
			throw new Exception("Adds didn't work?");
		}

		// TODO: try adding some invalid data

		// try removing some data

		// this data does not exist in this graph
		aStore.delete(getDataToAdd(), Format.Turtle, getGraphURI());

		// so lets make sure the old data is still there...
		verifyAdd(aStore, getGraphURI(), getOtherDataToAdd());

		// and that the data "removed" is not there
		verifyDelete(aStore, getGraphURI(), getDataToAdd());

		// k, now, lets try and delete a blob of data
		aStore.delete(getDataToAdd(), Format.Turtle, null);

		// make sure its gone
		verifyDelete(aStore, null, getDataToAdd());

		// lets try deleting an entire named graph
		aStore.delete(getGraphURI());

		// and make sure its gone...
		verifyDelete(aStore, getGraphURI(), getOtherDataToAdd());

		if (aStore.size() != 0) {
			throw new Exception("Deletes didn't work?");
		}

		// TODO: try removing invalid data

		// try updating some data
		aStore.add(getDataToAdd(), Format.Turtle, getGraphURI());
		verifyAdd(aStore, getGraphURI(), getDataToAdd());

		// TODO: i assume adding to an existing named graph overwrites?
		aStore.add(getOtherDataToAdd(), Format.Turtle, getGraphURI());

		verifyAdd(aStore, getGraphURI(), getOtherDataToAdd());
		verifyDelete(aStore, getGraphURI(), getDataToAdd());

		aStore.append(getDataToAdd(), Format.Turtle, getGraphURI());
		verifyAdd(aStore, getGraphURI(), getDataToAdd());
		verifyAdd(aStore, getGraphURI(), getOtherDataToAdd());

		// TODO: make sure we get a notice that the soft limit was hit?

		aStore.disconnect();

		// TODO: make sure we can't do operations after disconnection
	}

	private static String getDataToAdd() throws IOException {
		return IOUtil.getFileAsString("test/data1.ttl");
	}

	private static String getOtherDataToAdd() throws IOException {
		return IOUtil.getFileAsString("test/data2.ttl");
	}

	private static java.net.URI getGraphURI() {
		return java.net.URI.create("http://example.org/graph");
	}

	private static void verifyAdd(Store theStore, java.net.URI theGraph, String theData) throws Exception {
		throw new RuntimeException("NYI");
//		ExtendedGraph aGraph = SesameIO.readGraph(new StringReader(theData), RDFFormat.TURTLE);
//		SesameFourStoreFactory aFactory = new SesameFourStoreFactory();
//
//		for (Statement aStmt : aGraph) {
//			theStore.hasStatement()
//		}
	}

	private static void verifyDelete(Store theStore, java.net.URI theGraph, String theData) throws Exception {
		throw new RuntimeException("NYI");
	}

	private static int getExpectedSize() throws Exception {
		// yes, this is not very efficient...
		return SesameIO.readGraph(new FileInputStream("test/data1.ttl"), RDFFormat.TURTLE).numStatements() +
			   SesameIO.readGraph(new FileInputStream("test/data2.ttl"), RDFFormat.TURTLE).numStatements();

	}
}
