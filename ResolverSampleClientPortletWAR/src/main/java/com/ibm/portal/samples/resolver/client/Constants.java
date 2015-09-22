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

/**
 * Some constants used by the viewer portlet
 * 
 * @author Dr. Carsten Leue
 */
public interface Constants {

	/**
	 * Request attribute used to identify the bean. Used in clear text in the
	 * JSP.
	 */
	String KEY_BEAN = "bean";

	/**
	 * Key of the language input field, implementation detail.
	 */
	String KEY_LANGUAGE = "l";

	/**
	 * Key of the info input field, implementation detail.
	 */
	String KEY_INFO = "i";

	/**
	 * Key of the submit button, implementation detail.
	 */
	String KEY_SUBMIT = "s";

	/**
	 * Name of the public render parameter that encodes a POC URI Must match the
	 * <code>supported-public-render-parameter</code> element in the
	 * <code>portlet.xml</code>
	 */
	String KEY_URI = "uri";
}
