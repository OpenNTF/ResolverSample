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

import javax.naming.Context;
import javax.naming.NamingException;

import com.ibm.portal.model.LanguageListHome;
import com.ibm.portal.portlet.service.PortletServiceHome;
import com.ibm.portal.portlet.service.PortletServiceUnavailableException;
import com.ibm.portal.portlet.service.model.PortletLocalizedContextHome;
import com.ibm.portal.samples.resolver.client.ResolverPortlet.Dependencies;

public class ResolverDependencies implements Dependencies {

	private final LanguageListHome languageListHome;

	private final PortletLocalizedContextHome portletLocalizedContextHome;

	public ResolverDependencies(final Context aContext) throws NamingException, PortletServiceUnavailableException {
		languageListHome = (LanguageListHome) aContext.lookup(LanguageListHome.JNDI_NAME);
		// lookup the portlet service
		final PortletServiceHome psh = (PortletServiceHome) aContext.lookup(PortletLocalizedContextHome.JNDI_NAME);
		portletLocalizedContextHome = (PortletLocalizedContextHome) psh
				.getPortletService(PortletLocalizedContextHome.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.portal.samples.resolver.client.Bean.Dependencies#
	 * getLanguageListHome()
	 */
	@Override
	public LanguageListHome getLanguageListHome() {
		// the language list
		return languageListHome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.portal.samples.resolver.client.Bean.Dependencies#
	 * getLocalizedContextHome()
	 */
	@Override
	public PortletLocalizedContextHome getLocalizedContextHome() {
		// localization APIs
		return portletLocalizedContextHome;
	}

}
