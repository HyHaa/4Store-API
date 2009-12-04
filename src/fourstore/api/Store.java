package fourstore.api;

import fourstore.api.rdf.Graph;
import fourstore.api.rdf.Resource;
import fourstore.api.rdf.Statement;
import fourstore.api.rdf.URI;
import fourstore.api.rdf.Value;

import fourstore.api.results.ResultSet;

import java.net.ConnectException;

/**
 * Title: <br/>
 * Description: <br/>
 * Company: Clark & Parsia, LLC. <http://clarkparsia.com><br/>
 * Created: Dec 1, 2009 2:45:19 PM<br/>
 *
 * @author Michael Grove <mike@clarkparsia.com><br/>
 */
public interface Store {
	public void connect() throws ConnectException;
	public void disconnect() throws ConnectException;

	public void setSoftLimit(int theLimit);
	public int getSoftLimit();

	public boolean hasStatement(Statement theStmt) throws StoreException;
	public boolean hasStatement(Resource theSubj, URI thePred, Value theObj) throws StoreException;

	public ResultSet query(String theQuery) throws QueryException;
	public Graph constructQuery(String theQuery) throws QueryException;
	public Graph describe(URI theConcept) throws QueryException;
	public boolean ask(URI theConcept) throws QueryException;

	// TODO: for these operations that modify the data, add methods which take an InputStream rather than
	// a string.  this way the inputstream can be written directly to the HTTP connection rather than being in a
	// string which requires all of it to be in memory

	public boolean add(String theGraph, Format theFormat, java.net.URI theGraphURI) throws StoreException;

	// TODO: why is this not supported??
//	public boolean delete(String theGraph, Format theFormat, java.net.URI theGraphURI) throws StoreException;
	public boolean delete(java.net.URI theGraphURI) throws StoreException;

	public boolean append(String theGraph, Format theFormat, java.net.URI theGraphURI) throws StoreException;

	public long size() throws StoreException;

	public String status() throws StoreException;
}
