/*
 * Copyright (c) 2009-2010 Clark & Parsia, LLC. <http://www.clarkparsia.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clarkparsia.fourstore.sesame;

import org.openrdf.sail.helpers.NotifyingSailBase;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import java.net.URL;
import java.net.ConnectException;

import com.clarkparsia.fourstore.api.Store;
import com.clarkparsia.fourstore.impl.StoreFactory;
import com.clarkparsia.utils.web.Method;

/**
 * <p>Implementation of a Sesame Sail which is backed by an instance of 4Store.</p>
 *
 * @author Michael Grove
 * @since 0.3
 * @version 0.3.1
 */
public class FourStoreSail extends NotifyingSailBase {

	/**
	 * The underlying 4Store instance.
	 */
	private Store mStore;

	/**
	 * The value factory for this Sail.
	 */
	private ValueFactory mValueFactory = new ValueFactoryImpl();

	/**
	 * Create a new FourStoreSail
	 * @param theURL the URL of the 4Store instance
	 */
	public FourStoreSail(URL theURL) {
		mStore = StoreFactory.create(theURL);
	}

	/**
	 * Create a new FourStoreSail
	 * @param theURL the URL of the 4store instance
	 * @param theUseGet whether or not to use GET when sending sparql query requests
	 */
	public FourStoreSail(URL theURL, boolean theUseGet) {
		mStore = StoreFactory.create(theURL, theUseGet ? Method.GET : Method.POST);
	}

	/**
	 * @inheritDoc
	 */
	protected void shutDownInternal() throws SailException {
		try {
			getFourStore().disconnect();
		}
		catch (ConnectException e) {
			throw new SailException(e);
		}
	}

	/**
	 * @inheritDoc
	 */
	protected NotifyingSailConnection getConnectionInternal() throws SailException {
		return new FourStoreSailConnection(this);
	}

	/**
	 * @inheritDoc
	 */
	public boolean isWritable() throws SailException {
		return true;
	}

	/**
	 * @inheritDoc
	 */
	public ValueFactory getValueFactory() {
		return mValueFactory;
	}

	/**
	 * Return the 4Store instance backing this Sail.
	 * @return the underlying 4Store repository
	 */
	Store getFourStore() {
		return mStore;
	}
}
