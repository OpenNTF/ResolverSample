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
import static com.ibm.portal.samples.resolver.client.Constants.KEY_SUBMIT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ibm.icu.util.ULocale;
import com.ibm.portal.Localized;
import com.ibm.portal.ModelException;
import com.ibm.portal.admin.Language;
import com.ibm.portal.admin.LanguageList;
import com.ibm.portal.model.LanguageListHome;
import com.ibm.portal.model.ResourceBundleProvider;
import com.ibm.portal.portlet.service.model.PortletLocalizedContext;
import com.ibm.portal.portlet.service.model.PortletLocalizedContextHome;

/**
 * Provided convenient access to the information the JSP requires.
 * 
 * @author cleue
 */
public class Bean {

	public interface Dependencies {

		LanguageListHome getLanguageListHome();

		PortletLocalizedContextHome getLocalizedContextHome();
	}

	public enum KEYS {
		SUBMIT
	}

	/**
	 * Bean that provides convenient access to a language supported by portal
	 */
	public final class LanguageBean {

		private final String key;

		private final Locale locale;

		private final String title;

		private LanguageBean(final Locale aLocale, final String aTitle) {
			locale = aLocale;
			title = aTitle;
			key = localeToLanguageTag(aLocale);
		}

		/**
		 * Returns the language tag
		 * 
		 * @return the tag
		 */
		public String getKey() {
			return key;
		}

		/**
		 * The locale identifier
		 * 
		 * @return the locale
		 */
		public Locale getLocale() {
			return locale;
		}

		/**
		 * Title, localized for the portlet
		 * 
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Checks if this locale is the currently selected locale
		 * 
		 * @return <code>true</code> if the locale is selected, else
		 *         <code>false</code>
		 */
		public boolean isSelected() {
			return locale.equals(getSelectedLocale());
		}
	}

	private static final String INITIAL_INFO = "sample";

	private static final String localeToLanguageTag(final Locale aLocale) {
		return ULocale.forLocale(aLocale).toLanguageTag();
	}

	private LanguageList<Language> languageList;

	private final LanguageListHome languageListHome;

	private List<LanguageBean> languages;

	private final PortletLocalizedContextHome localizedContextHome;

	private PortletLocalizedContext locContext;

	private final RenderRequest request;

	/**
	 * Access to the portlet bundle
	 * 
	 * @return the provider
	 */
	private ResourceBundleProvider resourceBundleProvider;

	private final RenderResponse response;

	/**
	 * current locale
	 */
	private Locale selectedLocale;

	public Bean(final RenderRequest aReq, final RenderResponse aResp, final Dependencies aDeps) {
		request = aReq;
		response = aResp;
		languageListHome = aDeps.getLanguageListHome();
		localizedContextHome = aDeps.getLocalizedContextHome();
	}

	/**
	 * Returns the default action URL
	 * 
	 * @return the action URL
	 */
	public final PortletURL getActionURL() {
		return response.createActionURL();
	}

	/**
	 * Returns the current informational string
	 * 
	 * @return the info
	 */
	public String getInfo() {
		return INITIAL_INFO;
	}

	/**
	 * Returns the name of the input field
	 * 
	 * @return name
	 */
	public String getKeyInfo() {
		return KEY_INFO;
	}

	/**
	 * Returns the name of the language field
	 * 
	 * @return name
	 */
	public String getKeyLanguage() {
		return KEY_LANGUAGE;
	}

	/**
	 * Returns the name of the submit button
	 * 
	 * @return name
	 */
	public String getKeySubmit() {
		return KEY_SUBMIT;
	}

	private final LanguageList<Language> getLanguageList() throws ModelException {
		// list of languages
		if (languageList == null) {
			languageList = languageListHome.getLanguageListProvider().getLanguageList();
		}
		return languageList;
	}

	/**
	 * Returns a list of supported languages
	 * 
	 * @return the languages
	 * @throws ModelException
	 */
	public final List<LanguageBean> getLanguages() throws ModelException {
		// returns the list of language beans
		if (languages == null) {
			// language list
			final LanguageList<Language> list = getLanguageList();
			final PortletLocalizedContext locCox = getLocalizedContext();
			// construct a translated list
			languages = new ArrayList<LanguageBean>();
			for (Iterator<Language> i = list.iterator(); i.hasNext();) {
				// next language
				final Language lang = i.next();
				// translate the language
				final Map.Entry<Locale, String> title = locCox.getLocalizedTitle(lang);
				// add the bean
				languages.add(new LanguageBean(lang.getLocale(), title.getValue()));
			}
		}
		return languages;
	}

	/**
	 * Returns the context
	 * 
	 * @return the localized context
	 */
	private final PortletLocalizedContext getLocalizedContext() {
		// the context
		if (locContext == null) {
			locContext = localizedContextHome.getLocalizedContext(request, response);
		}
		return locContext;
	}

	private final Map.Entry<Locale, String> getResource(final KEYS aKey) {
		return getTitle(getResourceBundleProvider().getLocalized(aKey.name()));
	}

	private final ResourceBundleProvider getResourceBundleProvider() {
		if (resourceBundleProvider == null) {
			resourceBundleProvider = getLocalizedContext().getResourceBundleProvider();
		}
		return resourceBundleProvider;
	}

	/**
	 * Returns the localized title of the submit button
	 * 
	 * @return the title
	 */
	public Map.Entry<Locale, String> getResourceSubmit() {
		return getResource(KEYS.SUBMIT);
	}

	/**
	 * Returns the current locale for the portlet
	 * 
	 * @return the locale
	 */
	private final Locale getSelectedLocale() {
		// lazily load the selected locale
		if (selectedLocale == null) {
			selectedLocale = getLocalizedContext().getPreferredSupportedLocale();
		}
		// retuens the selection
		return selectedLocale;
	}

	/**
	 * Returns the localized title for the resource
	 * 
	 * @param aLoc
	 *            the localized object
	 * @return the title
	 */
	private final Map.Entry<Locale, String> getTitle(final Localized aLoc) {
		return getLocalizedContext().getLocalizedTitle(aLoc);
	}
}
