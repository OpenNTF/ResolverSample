# ResolverSample
SETUP
	
	define "WpsInstallLocation" in your maven settings.xml. It should point to the WebSphere folder of the portal installation

BUILD
	
	mvn clean package
    
INSTALL
	
	./ConfigEngine.sh install-paa -DPAALocation=ResolverSamplePAA-1.0-paa.zip
	./ConfigEngine.sh deploy-paa -DappName=com.ibm.portal.samples-ResolverSamplePAA
	
UNINSTALL

	./ConfigEngine.sh remove-paa -DappName=com.ibm.portal.samples-ResolverSamplePAA
	./ConfigEngine.sh uninstall-paa -DappName=com.ibm.portal.samples-ResolverSamplePAA
	./ConfigEngine.sh delete-paa -DappName=com.ibm.portal.samples-ResolverSamplePAA
		

##OPENNTF##
This project is an OpenNTF project, and is available under the Apache License
V2.0. All other aspects of the project, including contributions, defect
reports, discussions, feature requests and reviews are subject to the
[OpenNTF Terms of Use](http://openntf.org/Internal/home.nsf/dx/Terms_of_Use).
