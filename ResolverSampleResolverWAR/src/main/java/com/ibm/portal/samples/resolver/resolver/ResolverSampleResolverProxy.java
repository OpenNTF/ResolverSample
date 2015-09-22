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

import org.eclipse.core.runtime.IExecutableExtensionFactory;

import com.ibm.portal.resolver.cor.helper.DefaultContentOperationsRegistryFactory;

/**
 * This class is registered with the <code>plugin.xml</code>. We use this proxy
 * because it automatically constructs and fills in the dependencies interface
 * for the {@link ResolverSampleResolver}. Since the proxy implements
 * {@link IExecutableExtensionFactory} the eclipse instantiation process will
 * actually return the resulting class as the real implementation.
 * 
 * @author cleue
 */
public class ResolverSampleResolverProxy extends DefaultContentOperationsRegistryFactory
		implements IExecutableExtensionFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.portal.resolver.cor.helper.
	 * DefaultContentOperationsRegistryFactory #getTargetClass()
	 */
	@Override
	protected Class<ResolverSampleResolver> getTargetClass() {
		// construct this class
		return ResolverSampleResolver.class;
	}
}
