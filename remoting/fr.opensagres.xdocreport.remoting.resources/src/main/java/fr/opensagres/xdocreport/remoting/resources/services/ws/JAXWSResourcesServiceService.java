package fr.opensagres.xdocreport.remoting.resources.services.ws;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 2.6.1
 * 2012-06-09T22:09:23.021+02:00
 * Generated source version: 2.6.1
 *
 */
@WebServiceClient(name = "JAXWSResourcesServiceService",
                  wsdlLocation = "JAXWSResourcesServiceService.wsdl",
                  targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/")
public class JAXWSResourcesServiceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://services.resources.remoting.xdocreport.opensagres.fr/", "JAXWSResourcesServiceService");
    public final static QName ResourcesServicePort = new QName("http://services.resources.remoting.xdocreport.opensagres.fr/", "ResourcesServicePort");
    static {
        URL url = JAXWSResourcesServiceService.class.getResource("JAXWSResourcesServiceService.wsdl");
        if (url == null) {
            java.util.logging.Logger.getLogger(JAXWSResourcesServiceService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "JAXWSResourcesServiceService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public JAXWSResourcesServiceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public JAXWSResourcesServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public JAXWSResourcesServiceService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     *
     * @return
     *     returns ResourcesService
     */
    @WebEndpoint(name = "ResourcesServicePort")
    public ResourcesService getResourcesServicePort() {
        return super.getPort(ResourcesServicePort, ResourcesService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ResourcesService
     */
    @WebEndpoint(name = "ResourcesServicePort")
    public ResourcesService getResourcesServicePort(WebServiceFeature... features) {
        return super.getPort(ResourcesServicePort, ResourcesService.class, features);
    }

}
