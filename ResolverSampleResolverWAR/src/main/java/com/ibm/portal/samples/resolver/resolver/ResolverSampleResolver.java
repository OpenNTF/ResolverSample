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
package com.ibm.portal.samples.resolver.resolver;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.content.operations.registry.api.Context;
import com.ibm.portal.ObjectID;
import com.ibm.portal.cor.service.StateManagerServiceHome;
import com.ibm.portal.identification.Identification;
import com.ibm.portal.resolver.Binding;
import com.ibm.portal.resolver.ResolutionService;
import com.ibm.portal.resolver.Resolved;
import com.ibm.portal.resolver.exceptions.ResolutionException;
import com.ibm.portal.resolver.exceptions.ResolutionSerializationException;
import com.ibm.portal.resolver.service.CorPocServiceHome;
import com.ibm.portal.samples.resolver.api.ResolverSampleURI;
import com.ibm.portal.serialize.SerializationException;
import com.ibm.portal.state.StateHolderController;
import com.ibm.portal.state.accessors.locale.LocaleAccessorController;
import com.ibm.portal.state.accessors.locale.LocaleAccessorFactory;
import com.ibm.portal.state.exceptions.StateException;
import com.ibm.portal.state.service.StateManagerService;

/**
 * Implementation of the main {@link ResolutionService}. This service detects
 * the target page and then dispatches to the portlets on the target page to
 * initialize themselves.
 *
 * @author cleue
 */
public class ResolverSampleResolver implements ResolutionService {

	public interface Dependencies {

		Identification getIdentification();

		CorPocServiceHome getPocServiceHome();

		StateManagerServiceHome getStateManagerServiceHome();
	}

	/** class name for the logger */
	private static final String LOG_CLASS = ResolverSampleResolver.class.getName();

	/** logging level */
	private static final Level LOG_LEVEL = Level.FINER;

	/** class logger */
	private static final Logger LOGGER = Logger.getLogger(LOG_CLASS);

	/**
	 * Hardcode the page for this example. In a real world example the target
	 * page would be decoded from the URI and parameters passed to this
	 * resolver.
	 */
	private static final String PAGE_UNIQUE_NAME = "ResolverSampleResolver.page";

	/**
	 * identification service
	 */
	private final Identification identification;

	/**
	 * access to some POC APIs
	 */
	private final CorPocServiceHome pocServiceHome;

	/**
	 * access to the state APIs
	 */
	private final StateManagerServiceHome stateManagerServiceHome;

	public ResolverSampleResolver(final Dependencies aDeps) {
		stateManagerServiceHome = aDeps.getStateManagerServiceHome();
		identification = aDeps.getIdentification();
		pocServiceHome = aDeps.getPocServiceHome();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ibm.portal.resolver.ResolutionService#resolve(com.ibm.portal.resolver
	 * .Resolved, java.net.URI, java.lang.String, java.util.Map, java.util.Set,
	 * com.ibm.content.operations.registry.api.Context)
	 */
	@Override
	public boolean resolve(final Resolved aResolved, final URI aURI, final String aMode,
			final Map<String, String[]> aParams, final Set<Binding> aBinding, final Context aContext)
					throws ResolutionException, StateException {
		// logging support
		final String LOG_METHOD = "resolve(aResolved, aURI, aMode, aParams, aBinding, aContext)";
		final boolean bIsLogging = LOGGER.isLoggable(LOG_LEVEL);
		if (bIsLogging) {
			LOGGER.entering(LOG_CLASS, LOG_METHOD);
		}
		// result of the resolution process
		boolean bResult;
		try {
			/**
			 * Decode the URI. The subsequent initialization process should
			 * depend on the decoded URI and the passed parameter map.
			 */
			final ResolverSampleURI sampleURI = new ResolverSampleURI(aURI);
			assert sampleURI != null;
			// state to modify
			final StateHolderController state = aResolved.getStateHolderController();
			assert state != null;

			/**
			 * In our example the URI contains a locale selection. Since this is
			 * part of the global state configuration, we initialize the locale
			 * in this layer (and not in the portlet layer).
			 */
			final StateManagerService stateService = stateManagerServiceHome.getStateManagerService(aContext);
			try {
				// set the locale
				final LocaleAccessorFactory localeFct = stateService.getAccessorFactory(LocaleAccessorFactory.class);
				final LocaleAccessorController localeCtrl = localeFct.getLocaleAccessorController(state);
				try {
					// update the locale
					localeCtrl.setLocale(sampleURI.getLocale());
				} finally {
					// release the controller
					localeCtrl.dispose();
				}
			} finally {
				// make sure to release the service
				stateService.dispose();
			}
			// find the target page
			final ObjectID pageID = identification.deserialize(PAGE_UNIQUE_NAME);
			final String serializedID = identification.serialize(pageID);
			/**
			 * Construct the URI to delegate to. The "nm" resolver will select
			 * the given page and also dispatch the resolution to all portlets
			 * on that page. So after the dispatching we expect a page selection
			 * and initialized portlets on the selected target page.
			 */
			final URI modelURI = new URI("nm", "oid:" + serializedID + ":" + aURI, null);
			// delegate
			final ResolutionService delegateService = pocServiceHome.getResolutionService();
			bResult = delegateService.resolve(aResolved, modelURI, aMode, aParams, aBinding, aContext);
		} catch (final SerializationException ex) {
			// bail out
			throw new ResolutionSerializationException(ex);
		} catch (final URISyntaxException ex) {
			// just fail the resolution
			bResult = false;
		}
		// exit trace
		if (bIsLogging) {
			LOGGER.exiting(LOG_CLASS, LOG_METHOD, bResult);
		}
		// ok
		return bResult;
	}
}
