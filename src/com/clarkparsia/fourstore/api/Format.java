/*
 * Copyright (c) 2005-2010 Clark & Parsia, LLC. <http://www.clarkparsia.com>
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

package com.clarkparsia.fourstore.api;

/**
 * <p>Mimetype constants for various RDF formats.</p>
 *
 * @author Michael Grove
 * @since 0.1
 */
public enum Format {
	Turtle("application/x-turtle"),
	RdfXml("application/rdf+xml"),
	N3("application/rdf+n3"),
	NTriples("application/rdf+nt"),
	Trig("application/x-trig");

	private final String mMimeType;

	Format(final String theMimeType) {
		mMimeType = theMimeType;
	}

	public String getMimeType() {
		return mMimeType;
	}
}
