package com.example.sh24consultas;

import com.google.gwt.dom.client.Style;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ServiciosUrgencia extends CustomComponent  {
    

    public Label lbMensaje = new Label();
   
    VerticalLayout vlGlobal = new VerticalLayout();
	public Button btAceptar = new Button("SI");
	public Button btCancelar= new Button("NO");
	

    
    
    public ServiciosUrgencia ( )  {
    	
    	// Configuración para pantalla ordenador o mobil/tableta

    	lbMensaje.setValue("Esto es una prueba del mensaje. A ver si cabe en la pantalla");
    	
    	

		Resource res = new ThemeResource("img/logosh24prov.jpg");
		Image image = new Image(null, res);
		image.setWidth("40%");
		

    	vlGlobal.setMargin(true);
    	vlGlobal.setSpacing(true);
    	
    	HorizontalLayout hl = new HorizontalLayout();
    	VerticalLayout hlm = new VerticalLayout();
    	
    	
    	hl.addComponent(btAceptar);
    	hl.addComponent(btCancelar);
    	hl.setMargin(true);
    	hl.setSpacing(true);
    	
    	
    	
    	hl.setComponentAlignment(btAceptar, Alignment.MIDDLE_LEFT);
    	hl.setComponentAlignment(btCancelar, Alignment.MIDDLE_RIGHT);
    	
    	hlm.setMargin(true);
    	hlm.setSpacing(true);
    	hlm.addComponent(image);
    	//hlm.addComponent(lbMensaje);
    	hlm.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
    	
    	
    	vlGlobal.addComponent(hlm);
    	vlGlobal.addComponent(lbMensaje);
    	vlGlobal.addComponent(hl);
    	vlGlobal.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
    	vlGlobal.setComponentAlignment(lbMensaje, Alignment.MIDDLE_CENTER);

    	btAceptar.addStyleName("v-button-verde-"+UI.getCurrent().getSession().getAttribute("resolucion"));
	    btCancelar.addStyleName("v-button-rojo-"+UI.getCurrent().getSession().getAttribute("resolucion"));
	    lbMensaje.setStyleName("v-texto-"+UI.getCurrent().getSession().getAttribute("resolucion"));
	    
    	setCompositionRoot(vlGlobal);

    }
    
	
    
     
}


