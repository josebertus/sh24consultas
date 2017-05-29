package com.example.sh24consultas;

import java.math.BigDecimal;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


//@Theme("sh24consultas")
@Theme("tests-valo-reindeer")
@UIScope
public class Sh24consultasUI extends UI {

	
	private ExpedientesPolizas expPolizas;
	private DocsLiberty gedoxLiberty;
	private AnadirDocumento anadirDocumento;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Sh24consultasUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		

		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		


		VaadinServletRequest vsRequest = (VaadinServletRequest)request;
		HttpServletRequest hsRequest = vsRequest.getHttpServletRequest();
		String id = hsRequest.getParameter("id");
		
		Page.getCurrent().getWebBrowser().getBrowserApplication();
		if(Page.getCurrent().getWebBrowser().isTouchDevice()){
			UI.getCurrent().getSession().setAttribute("resolucion","grande");
			//System.out.println("Es touch");
		}
		else {
			UI.getCurrent().getSession().setAttribute("resolucion","pequena");
			//System.out.println("No Es touch");
		}
		
		if (id!=null && id.equals("SRVURG")) {


			java.math.BigDecimal idsms = new BigDecimal(hsRequest.getParameter("idsms"));
	        if (idsms!=null) {
	           ServiciosUrgencia srvUrg = new ServiciosUrgencia(idsms);
		       layout.addComponent(srvUrg);
	        }
	        

		}		
		
		if (id!=null && id.equals("LIBCON")) {
	        // opcion gedox liberty
			//System.out.println("Abrimos la pantalla");
	        gedoxLiberty = new DocsLiberty();
	        gedoxLiberty.arArbol.removeAllItems();
			//gedoxLiberty.btDocumentacion.click();
	        layout.addComponent(gedoxLiberty);
	        
	        String expediente = hsRequest.getParameter("expediente");
	        if (expediente!=null) {
	           gedoxLiberty.txExp.setValue(expediente);
	           gedoxLiberty.btAceptar.click();
	           
	        }
		}
		
		if (id!=null && id.equals("ADDFIL")) {
	        // opcion gedox liberty
			//System.out.println("Abrimos la pantalla");
	        anadirDocumento = new AnadirDocumento();
	        
	        layout.addComponent(anadirDocumento);
	        
	        String expediente = hsRequest.getParameter("expediente");
	        if (expediente!=null) {
	        	anadirDocumento.txExp.setValue(expediente);
	        	anadirDocumento.btAceptar.click();
	           
	           
	        }
			
		}		

		if (id!=null && id.equals("LISSIN")) {
	        // opcion gedox liberty
			//System.out.println("Abrimos la pantalla");
	        expPolizas = new ExpedientesPolizas();
	        expPolizas.arArbol.removeAllItems();
			//gedoxLiberty.btDocumentacion.click();
	        layout.addComponent(expPolizas);
	        
	        String cia = hsRequest.getParameter("cia");
	        String poliza = hsRequest.getParameter("poliza");
	        String producto = hsRequest.getParameter("producto");
	        if (cia!=null && poliza!=null && producto!=null) {
	           expPolizas.txCia.setValue(cia);
	           expPolizas.txPoliza.setValue(poliza);
	           expPolizas.txProducto.setValue(producto);
	           expPolizas.btAceptar.click();
	           
	        }
			
		}
		

       setContent(layout);
	}

}