/*
 * (C) Copyright IBM Corp. 2015
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.portal.samples.resolver;

import static com.ibm.portal.samples.resolver.Constants.KEY_PRIVATE_PARAM;
import static com.ibm.portal.samples.resolver.Constants.KEY_PRIVATE_URI;
import static com.ibm.portal.samples.resolver.Constants.PRIVATE_PARAM_DEFAULT;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.portal.resolver.exceptions.ResolutionException;
import com.ibm.portal.resolver.portlet.RenderEventListener;
import com.ibm.portal.resolver.portlet.RenderEventRequest;
import com.ibm.portal.resolver.portlet.RenderEventResponse;
import com.ibm.portal.samples.resolver.api.ResolverSampleURI;
import com.ibm.portal.state.exceptions.StateException;

/**
 * Implementation of a resolver that initializes the render parameters of the
 * portlet. The resolver is called as part of the resolution process of the
 * target page. The classname of the listener is references in the
 * <code>plugin.xml</code>.
 *
 * @author Dr. Carsten Leue
 */
public class ResolverRenderEventListener implements RenderEventListener {

	/** class name for the logger */
	private static final String LOG_CLASS = ResolverRenderEventListener.class.getName();

	/** logging level */
	private static final Level LOG_LEVEL = Level.FINER;

	/** class logger */
	private static final Logger LOGGER = Logger.getLogger(LOG_CLASS);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ibm.portal.resolver.portlet.RenderEventListener#onRenderEvent(com
	 * .ibm.portal.resolver.portlet.RenderEventRequest,
	 * com.ibm.portal.resolver.portlet.RenderEventResponse)
	 */
	@Override
	public boolean onRenderEvent(final RenderEventRequest req, final RenderEventResponse resp)
			throws ResolutionException, StateException {
		// logging support
		final String LOG_METHOD = "onRenderEvent(req, resp)";
		final boolean bIsLogging = LOGGER.isLoggable(LOG_LEVEL);
		if (bIsLogging) {
			LOGGER.entering(LOG_CLASS, LOG_METHOD);
		}
		// decode the URI
		final ResolverSampleURI sampleURI = new ResolverSampleURI(req.getURI());
		// log this
		if (bIsLogging) {
			LOGGER.logp(LOG_LEVEL, LOG_CLASS, LOG_METHOD, "Sample URI [{0}].", sampleURI);
		}
		// transcode the source into the private render parameters
		resp.setParameter(KEY_PRIVATE_PARAM, req.getParameter(KEY_PRIVATE_PARAM, PRIVATE_PARAM_DEFAULT));
		resp.setParameter(KEY_PRIVATE_URI, sampleURI.getInfo());
		// exit trace
		if (bIsLogging) {
			LOGGER.exiting(LOG_CLASS, LOG_METHOD);
		}
		// we actually decoded the data
		return true;
	}

}
