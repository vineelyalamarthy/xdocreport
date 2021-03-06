package fr.opensagres.xdocreport.remoting.resources.services.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.ObjectFactory;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;

/**
 * This class was generated by Apache CXF 2.6.1
 * 2012-06-09T22:09:22.862+02:00
 * Generated source version: 2.6.1
 *
 */
@WebService(targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/", name = "ResourcesService")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ResourcesService {

    @WebResult(name = "", targetNamespace = "", partName = "")
    @WebMethod(action = "urn:GetRoot")
    public Resource getRoot() throws ResourcesException;

    @WebResult(name = "", targetNamespace = "", partName = "")
    @WebMethod(action = "urn:Download1")
    public BinaryData download1(
        @WebParam(partName = "resourceId", name = "resourceId", targetNamespace = "")
        java.lang.String resourceId
    ) throws ResourcesException;

    @WebResult(name = "name", targetNamespace = "", partName = "name")
    @WebMethod(action = "urn:GetName")
    public java.lang.String getName();

    @WebMethod(action = "urn:Upload")
    public void upload(
        @WebParam(partName = "content", name = "content", targetNamespace = "")
        BinaryData content
    ) throws ResourcesException;

    @WebResult(name = "", targetNamespace = "", partName = "")
    @WebMethod(action = "urn:GetRoot1")
    public Resource getRoot1(
        @WebParam(partName = "filter", name = "filter", targetNamespace = "")
        Filter filter
    ) throws ResourcesException;

    @WebResult(name = "", targetNamespace = "", partName = "")
    @WebMethod(action = "urn:Download")
    public BinaryData download(
        @WebParam(partName = "resourceIds", name = "resourceIds", targetNamespace = "")
        java.lang.String resourceIds
    ) throws ResourcesException;
}
