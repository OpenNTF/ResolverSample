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

/**
 * Some constants used by the portlet and the portlet resolver
 * 
 * @author Dr. Carsten Leue
 */
public interface Constants {

	/**
	 * Private render parameter, only known to the portlet and the resolver
	 */
	String KEY_PRIVATE_PARAM = "privateKey";

	/**
	 * Default value for the {@link #KEY_PRIVATE_PARAM} parameter
	 */
	String PRIVATE_PARAM_DEFAULT = "privateDefault";

	/**
	 * Private render parameter, only known to the portlet and the resolver
	 */
	String KEY_PRIVATE_URI = "privateUri";

}
