package com.example.sh24consultas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.vaadin.spring.annotation.UIScope;

public class WsUpload {

@UIScope
public ByteArrayInputStream WsUpload(AnadirDocumento anadirDocumento, String exp, String id, String extension, String url, String usuario, String pwd) throws Exception {
	
	ByteArrayInputStream bis = null;
	String soapRequestXml = null;

	
	System.out.println("WSUpload ¡¡¡" + url);

	soapRequestXml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:par=\"http://www.liberty.es/ws/param/\"> "+
				   "<soapenv:Header/>  "+
				   "<soapenv:Body> "+
				   "   <par:DownloadManageGd> "+
				   "      <docManagementFilter> "+
				   "         <claimCode>"+exp+"</claimCode> "+
				   "         <documentId>"+id+"</documentId> "+
				   "         <documentType>"+extension+"</documentType> "+
				   "      </docManagementFilter> "+
				   "   </par:DownloadManageGd> "+
				   "</soapenv:Body> "+
				"</soapenv:Envelope>";
	
	
	
	
    // Create SOAP Connection
	
	MessageFactory msgFactory     = MessageFactory.newInstance();  
	SOAPMessage message           = msgFactory.createMessage();  
	String loginPassword = usuario + ":"+ pwd;
			
	message.getMimeHeaders().addHeader("Authorization", "Basic " + new  String(Base64.encode(loginPassword.getBytes())));


    SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
    SOAPConnection soapConnection = soapConnectionFactory.createConnection();


    SOAPMessage soapRequest = MessageFactory.newInstance().createMessage(message.getMimeHeaders(),
            new ByteArrayInputStream(soapRequestXml.getBytes()));
    
    

    // Send SOAP Message to SOAP Server
    SOAPMessage soapResponse = soapConnection.call(soapRequest, url);
    

    ByteArrayOutputStream soapResponseBaos = new ByteArrayOutputStream();
    soapResponse.writeTo(soapResponseBaos);
    String soapResponseXml = soapResponseBaos.toString();

    System.out.println("Response:"+ soapResponseXml);

    String documento = soapResponseXml.substring(soapResponseXml.indexOf("<content>")+9, soapResponseXml.indexOf("</content>"));
    String tipofichero = soapResponseXml.substring(soapResponseXml.indexOf("<type>")+6, soapResponseXml.indexOf("</type>"));
    System.out.println("Respuesta documento:" + documento);
    System.out.println("Respuesta tipo:" + tipofichero);
    
    anadirDocumento.textoLog.setValue(tipofichero + " >>> " +  documento );

    
    byte[] documentoByte;
    try {
    	documentoByte = Base64.decode(documento);
        bis = new ByteArrayInputStream(documentoByte);
    } catch (Exception e) {
        e.printStackTrace();
    }    
    
    return bis;

}




}