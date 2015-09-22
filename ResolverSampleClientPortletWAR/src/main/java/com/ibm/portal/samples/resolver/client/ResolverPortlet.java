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
package com.ibm.portal.samples.resolver.client;

import static com.ibm.portal.samples.resolver.client.Constants.KEY_INFO;
import static com.ibm.portal.samples.resolver.client.Constants.KEY_LANGUAGE;
import static com.ibm.portal.samples.resolver.client.Constants.KEY_URI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ibm.icu.util.ULocale;
import com.ibm.portal.samples.resolver.api.ResolverSampleURI;

/**
 * Portlet that triggers a resolution step
 *
 * @author Dr. Carsten Leue
 */
public class ResolverPortlet extends GenericPortlet {

	public interface Dependencies extends Bean.Dependencies {
	}

	/**
	 * default value for the info field
	 */
	private static final String DEFAULT_INFO = "defaultInfo";

	/**
	 * default value for the language field
	 */
	private static final String DEFAULT_LANGUAGE = "en";

	/** class name for the logger */
	private static final String LOG_CLASS = ResolverPortlet.class.getName();

	/** logging level */
	private static final Level LOG_LEVEL = Level.FINER;

	/** class logger */
	private static final Logger LOGGER = Logger.getLogger(LOG_CLASS);

	/**
	 * suffix for the JSP path
	 */
	private static final String PATH_SUFFIX = ".jsp";

	/**
	 * prefix for the JSP path
	 */
	private static final String PRIVATE_PATH_PREFIX = "/WEB-INF/"
			+ ResolverPortlet.class.getPackage().getName().replace('.', '/') + "/";

	private static final String box(final String aValue, final String aAlternative) {
		return (aValue != null) ? aValue : aAlternative;
	}

	private static final Locale languageTagToLocale(final String aTag) {
		return ULocale.forLanguageTag(aTag).toLocale();
	}

	/**
	 * the dependency object
	 */
	private Dependencies dependencies;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.portlet.GenericPortlet#destroy()
	 */
	@Override
	public void destroy() {
		// logging support
		final String LOG_METHOD = "destroy()";
		final boolean bIsLogging = LOGGER.isLoggable(LOG_LEVEL);
		if (bIsLogging) {
			LOGGER.entering(LOG_CLASS, LOG_METHOD);
		}
		// reset the dependencies
		dependencies = null;
		// shutdown
		super.destroy();
		// exit trace
		if (bIsLogging) {
			LOGGER.exiting(LOG_CLASS, LOG_METHOD);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest,
	 * javax.portlet.RenderResponse)
	 */
	@Override
	protected void doView(final RenderRequest request, final RenderResponse response)
			throws PortletException, IOException {
		// logging support
		final String LOG_METHOD = "doView(request, response)";
		final boolean bIsLogging = LOGGER.isLoggable(LOG_LEVEL);
		if (bIsLogging) {
			LOGGER.entering(LOG_CLASS, LOG_METHOD);
		}
		// init the response
		response.setContentType(request.getResponseContentType());
		// initialize the bean
		final Bean bean = new Bean(request, response, dependencies);
		request.setAttribute(Constants.KEY_BEAN, bean);
		// compute the JSP name
		final String jspPath = PRIVATE_PATH_PREFIX + request.getPortletMode() + PATH_SUFFIX;
		// log this
		if (bIsLogging) {
			LOGGER.logp(LOG_LEVEL, LOG_CLASS, LOG_METHOD, "Including [{0}] ...", jspPath);
		}
		// dispatch
		getPortletContext().getRequestDispatcher(jspPath).include(request, response);
		// exit trace
		if (bIsLogging) {
			LOGGER.exiting(LOG_CLASS, LOG_METHOD);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.portlet.GenericPortlet#init(javax.portlet.PortletConfig)
	 */
	@Override
	public void init(final PortletConfig config) throws PortletException {
		// logging support
		final String LOG_METHOD = "init(config)";
		final boolean bIsLogging = LOGGER.isLoggable(LOG_LEVEL);
		if (bIsLogging) {
			LOGGER.entering(LOG_CLASS, LOG_METHOD);
		}
		// default
		super.init(config);
		try {
			// initialize the dependencies
			dependencies = new ResolverDependencies(new InitialContext());
		} catch (final Exception ex) {
			// bail out
			throw new PortletException(ex);
		}
		// exit trace
		if (bIsLogging) {
			LOGGER.exiting(LOG_CLASS, LOG_METHOD);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.portlet.GenericPortlet#processAction(javax.portlet.ActionRequest,
	 * javax.portlet.ActionResponse)
	 */
	@Override
	public void processAction(final ActionRequest request, final ActionResponse response)
			throws PortletException, IOException {
		// logging support
		final String LOG_METHOD = "processAction(request, response)";
		final boolean bIsLogging = LOGGER.isLoggable(LOG_LEVEL);
		if (bIsLogging) {
			LOGGER.entering(LOG_CLASS, LOG_METHOD);
		}
		try {
			// decode the input data
			final Locale locale = languageTagToLocale(box(request.getParameter(KEY_LANGUAGE), DEFAULT_LANGUAGE));
			final String info = box(request.getParameter(KEY_INFO), DEFAULT_INFO);
			// log this
			if (bIsLogging) {
				LOGGER.logp(LOG_LEVEL, LOG_CLASS, LOG_METHOD, "Locale [{0}] and info [{1}].",
						new Object[] { locale, info });
			}
			// construct the URI to dispatch to
			final URI uri = ResolverSampleURI.createInstance(locale, info);
			// log this
			if (bIsLogging) {
				LOGGER.logp(LOG_LEVEL, LOG_CLASS, LOG_METHOD, "Dispatching to URI [{0}].", uri);
			}
			// initialize the URI parameter
			response.setRenderParameter(KEY_URI, uri.toString());
		} catch (final URISyntaxException ex) {
			// just bail out
			throw new PortletException(ex);
		}
		// exit trace
		if (bIsLogging) {
			LOGGER.exiting(LOG_CLASS, LOG_METHOD);
		}
	}
}
