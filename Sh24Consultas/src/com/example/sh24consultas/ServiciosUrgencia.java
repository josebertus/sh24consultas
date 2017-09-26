package com.example.sh24consultas;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sh24Consultas.utils.ConversionUtil;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


public class ServiciosUrgencia extends CustomComponent  {
    
    public Label lbMensaje = new Label();
    String operacionAceptada = "N";
    public java.math.BigDecimal idSms = null;
    VerticalLayout vlGlobal = new VerticalLayout();
	public Button btSi = new Button("SI");
	public Button btNo= new Button("NO");
	Connection conn = null;
	String resultado;
	Label msgResultado = new Label("",ContentMode.HTML);
	String msgError;
    public ServiciosUrgencia ( java.math.BigDecimal pIdSms )  {
    	
    	
    	// Configuración para pantalla ordenador o mobil/tableta
    	
    	idSms = pIdSms;
    	
		String consulta;
		Boolean resprod;
		Clob esquema;

		try{
			
			
			Context initialContext = new InitialContext();
			Context environmentContext = (Context) initialContext.lookup("java:comp/env");
			String dataResourceName = "jdbc/PgeDB";
			
			DataSource dataSource = (DataSource) environmentContext.lookup(dataResourceName);					
			conn = dataSource.getConnection();
			StringBuilder msg = new StringBuilder();


			String callQuery="{?=call PAC_SHWEB_LISTAS.F_QUERY(?)}";
	        CallableStatement cStmt=conn.prepareCall(callQuery);
	        cStmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR); // Valor de "RETURN"
	        cStmt.setObject (2, "select rgenvio, cdasiste, "+
	        		" (select REPLACE(VALOR,'%texto%',( select txinform from ex_asistencias where cdasiste = exuah_mensajes_enviar.cdasiste )  ) from exuah_paramproc where cdproceso = 'ENVIO_SMS_URG' AND CDPARAM = 'TEXTO_MSJE_URL') TEXTO " +
	        		" , SWLINKPROC from exuah_mensajes_enviar" +
	        		" where rgenvio = " + idSms.toString() );
	        cStmt.execute();
	        HashMap retVal=new HashMap();

	        try {
	            retVal.put("RETURN", cStmt.getObject(1));
	        }
	        catch (SQLException e) {
	            retVal.put("RETURN", null);
	        }
	        try {
				retVal=new ConversionUtil().convertOracleObjects(retVal);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //AXIS-WLS1SERVER-Ready

	        

	        Map<String, Object> retorno = new HashMap<String, Object>(retVal);
	        ArrayList res = (ArrayList) retorno.get("RETURN");
			Map<String, String> m = (Map<String, String>) res.get(0);
	        lbMensaje.setValue(m.get("TEXTO"));
	        operacionAceptada = m.get("SWLINKPROC");


		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
				// Release connection back to the pool
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				conn = null; // prevent any future access
			}    	

    	
		Resource res = new ThemeResource("img/logosh24prov.jpg");
		Image image = new Image(null, res);
		image.setWidth("40%");

    	vlGlobal.setMargin(true);
    	vlGlobal.setSpacing(true);
    	HorizontalLayout hl = new HorizontalLayout();
    	HorizontalLayout hl2 = new HorizontalLayout();
    	VerticalLayout hlm = new VerticalLayout();
    	hl.addComponent(btSi);
    	hl.addComponent(new Label("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+
    			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+
    			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ",ContentMode.HTML));
    	hl.addComponent(btNo);
    	hl.setMargin(true);
    	hl.setSpacing(true);
    	
    	hl2.setMargin(true);
    	hl2.setSpacing(true);

    	hlm.setMargin(true);
    	hlm.setSpacing(true);
    	hlm.addComponent(new Label("<br><br><br>",ContentMode.HTML));
    	hlm.addComponent(image);
    	hlm.addComponent(new Label("<br><br><br>",ContentMode.HTML));
    	hlm.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

    	vlGlobal.addComponent(hlm);
    	vlGlobal.addComponent(lbMensaje);
    	vlGlobal.addComponent(msgResultado);
    	msgResultado.setVisible(false);
    	vlGlobal.addComponent(hl2);
    	vlGlobal.addComponent(hl);
    	hl2.addComponent(new Label("<br><br><br>",ContentMode.HTML));

    	vlGlobal.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
    	vlGlobal.setComponentAlignment(lbMensaje, Alignment.MIDDLE_CENTER);

    	btSi.addStyleName("v-button-verde-"+UI.getCurrent().getSession().getAttribute("resolucion"));
	    btNo.addStyleName("v-button-rojo-"+UI.getCurrent().getSession().getAttribute("resolucion"));
	    lbMensaje.setStyleName("v-texto-"+UI.getCurrent().getSession().getAttribute("resolucion"));
	    msgResultado.setStyleName("v-texto-"+UI.getCurrent().getSession().getAttribute("resolucion"));
	    
    	setCompositionRoot(vlGlobal);
    	
    	// Si la operación ya se ha hecho mostramos un mensajes de aviso y no dejamos pulsar los botones
    	
        if (operacionAceptada.equals("S")) {
        	lbMensaje.setValue("Esta operación ya se ha realizado. No se puede volver a realizar");
        	btSi.setVisible(false);
        	btNo.setVisible(false);
        }
        if (operacionAceptada.equals("E")) {
        	lbMensaje.setValue("Esta operación ya ha expirado. No se puede volver a realizar");
        	btSi.setVisible(false);
        	btNo.setVisible(false);
        }        
        
        
        // Eventos botones Si o No
        
        btSi.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				resultado = accionBotones("S");
				lbMensaje.setVisible(false);
				btSi.setVisible(false);
				btNo.setVisible(false);
				msgResultado.setVisible(true);
				if ( resultado.equals("0")) {
					msgResultado.setValue("<font color='green'>Operación realizada correctamente</font>");
				} else {
					msgResultado.setValue("<font color='red'>Se ha producido un error al realizar la operación.<BR>"+msgError +"</font>");
				}
				
			}
		});
        
        btNo.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				resultado = accionBotones("N");
				lbMensaje.setVisible(false);
				btSi.setVisible(false);
				btNo.setVisible(false);
				msgResultado.setVisible(true);
				if ( resultado.equals("0")) {
					msgResultado.setValue("<font color='green'>Operación realizada correctamente</font>");
				} else {
					msgResultado.setValue("<font color='red'>Se ha producido un error al realizar la operación.<BR>"+msgError +"</font>");
				}
				
			}
		});
        
        

    }
    
	
    private String accionBotones(String pAccion) {
		try{
			
			
			Context initialContext = new InitialContext();
			Context environmentContext = (Context) initialContext.lookup("java:comp/env");
			String dataResourceName = "jdbc/PgeDB";
			DataSource dataSource = (DataSource) environmentContext.lookup(dataResourceName);					
			conn = dataSource.getConnection();
			/*PROCEDURE accion_mensaje_sms ( pi_rgenvio IN NUMBER,
                    pi_accion IN VARCHAR2,
                    po_cderror OUT NUMBER,
                    po_txerror OUT VARCHAR2 ) IS*/
			String callQuery="{call PAC_SHWEB_PROVEEDORES.ACCION_MENSAJE_SMS(?,?,?,?)}";
	        CallableStatement cStmt=conn.prepareCall(callQuery);
	        cStmt.setObject (1, idSms);
	        cStmt.setObject (2, pAccion);
	        cStmt.registerOutParameter(3, oracle.jdbc.OracleTypes.INTEGER); // Valor de "CDERROR"
	        cStmt.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR); // Valor de "TXERROR"

	        cStmt.execute();
	        HashMap retVal=new HashMap();

	        try {
	            retVal.put("RETURN", cStmt.getObject(3));
	        }
	        catch (SQLException e) {
	            retVal.put("RETURN", null);
	        }
	        try {
	            retVal.put("ERROR", cStmt.getObject(4));
	        }
	        catch (SQLException e) {
	            retVal.put("ERROR", null);
	        }	        
	        try {
				retVal=new ConversionUtil().convertOracleObjects(retVal);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //AXIS-WLS1SERVER-Ready

	        
	        System.out.println("El resultado es " + retVal);
	        
	        msgError = (String) retVal.get("ERROR");
	        return retVal.get("RETURN").toString();

	  	     
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
				// Release connection back to the pool
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				conn = null; // prevent any future access
				
			} 
		return "-1";
	
    }
     
}


