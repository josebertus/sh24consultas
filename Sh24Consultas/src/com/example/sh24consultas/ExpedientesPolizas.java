package com.example.sh24consultas;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.sh24Consultas.utils.ConversionUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import oracle.sql.CLOB;

public class ExpedientesPolizas extends CustomComponent  {
    

	
	
	Embedded  cuadroNavegador = new Embedded ("");
		

    public static Integer arLinea = 0;
    public TextField txPoliza= new TextField();
    public TextField txCia= new TextField();
    public TextField txProducto= new TextField();
    public TextArea texto = new TextArea();
    public int pendiente = 0;
    //public Button btDocumentacion = new Button("Documentación");
    
    
    FileDownloader downloader;
    String nomFichero = "";
    
    public Button btVisualizar= new Button("Visualizar fichero seleccionado");
    TextArea textoLog  = new TextArea();
    
    VerticalLayout vlGlobal = new VerticalLayout();
    
    String folderId;
    String folderName;
    String fileId;
    String fileName;
    String fileType;
    String fileCreationDate;
    
    String type;
    String creationdate;
    
    String url;
    String usuario;
    String pwd;
	
	public Button btAceptar = new Button();
	
	
    
	public  Table arArbol = new Table();
    
    CheckBox ckBorrarLog = new CheckBox("Borrar Log");;
    
    GridLayout glLog = new GridLayout(2, 1);
    
    public ExpedientesPolizas ( )  {
    	
	    btAceptar.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	    
	    btVisualizar.setStyleName(ValoTheme.BUTTON_PRIMARY);
	    btVisualizar.setVisible(false);
   	
		glLog.setWidth("100%");
		glLog.setMargin(true);
    	glLog.addComponent(arArbol,0,0);
    	glLog.addComponent(cuadroNavegador,1,0);
    	
    	glLog.setColumnExpandRatio(0, 100);
    	glLog.setColumnExpandRatio(1, 0);
    	
    	

    	textoLog.setWidth("100%");
    	textoLog.setReadOnly(true);

	    HorizontalLayout hl = new HorizontalLayout();
	    txCia.setEnabled(false);
	    hl.addComponent(new Label("Cia:"));
	    hl.addComponent(txCia);
	    
	    txProducto.setEnabled(false);
	    hl.addComponent(new Label("Producto:"));
	    hl.addComponent(txProducto);
	    
	    txPoliza.setEnabled(false);
	    hl.addComponent(new Label("Póliza:"));
	    hl.addComponent(txPoliza);
	    
	    hl.addComponent(btAceptar);
	    hl.setMargin(true);

    	btAceptar.setCaption("Refrescar");
		
		// Ejecutamos la consulta con el listener
		
		btAceptar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				

		        
		        //System.out.println("Consumimos los servicios");

				Connection conexion = null;
				String consulta;
				Boolean resprod;
				Clob esquema;
				
				SAXBuilder saxBuilder = new SAXBuilder();
				Document documentRespuesta;
				Document document;
				
				try{
					
					String pContenido = "";
					arArbol.removeAllItems();
		
					System.out.println("Llamamos al SErvicio");
					String cia = txCia.getValue().toString();
					String poliza = txPoliza.getValue().toString();
					String producto = txProducto.getValue().toString();
					
					Context initialContext = new InitialContext();
					Context environmentContext = (Context) initialContext.lookup("java:comp/env");
					String dataResourceName = "jdbc/PgeDB";
					
					DataSource dataSource = (DataSource) environmentContext.lookup(dataResourceName);					
					Connection conn = dataSource.getConnection();
					StringBuilder msg = new StringBuilder();

					
					// hacemos la llamada
					
					

		            String callQuery = "{?=call PAC_SHWS.FF_SINPOLIZAS_LIBERTY_WS(?,?,?,?)}";
		            
		            CallableStatement cStmt = conn.prepareCall(callQuery);
		            cStmt.setObject (2, txCia.getValue().toString());
		            cStmt.setObject (3, txPoliza.getValue().toString());
		            cStmt.setObject (4, txProducto.getValue().toString());		            
		            cStmt.registerOutParameter (1, java.sql.Types.INTEGER); // Valor de "RETURN"
		            cStmt.registerOutParameter (5, java.sql.Types.CLOB); // Valor de "RESULTADO

		             
		            
		            cStmt.execute ();
		            HashMap retVal = new HashMap ();
		            try{
		                    retVal.put ("RETURN", cStmt.getObject (1));
		            }catch (SQLException e){
		                    retVal.put ("RETURN", null);
		            }
		            
		            try{
		            	
		            	retVal.put ("RESULTADO", cStmt.getObject (5));
						
		                //retVal=new ConversionUtil().convertOracleObjects(retVal);//AXIS-WLS1SERVER-Ready
						
						System.out.println("Llamamos al SErvicio3");
						pContenido = ConversionUtil.clob2String( (CLOB) retVal.get("RESULTADO") ) ;
						oracle.sql.CLOB.freeTemporary((CLOB) cStmt.getObject (5));		            	
		               

   	                    
		            }catch (SQLException e){
		            	
		                retVal.put ("RESULTADO", null);

		                retVal.put ("PWD", null);
					} finally {
						// Release connection back to the pool
						if (conn != null) {
							conn.close();
						}
						conn = null; // prevent any future access
					}
		            
					arArbol.removeAllItems();

	                //
	                
				
	                System.out.println("Llamamos al SErvicio4");
	                
	                //System.out.println("Resultado:"+pContenido);
				    document = saxBuilder.build(new StringReader(pContenido));
					if (pContenido!=null) {
						try {
							 System.out.println("Entramos al if");
							 // borramos el arbol del xml
							 arArbol.removeAllItems();
						     document = saxBuilder.build(new StringReader(pContenido));
						     //arArbol.addItem(new Object[]{document.getRootElement().getName(), null, null}, arLinea);
						     //arArbol.setParent(arLinea, arLinea);
					         Element classElement = document.getRootElement();
					         List<Element> hijos = classElement.getChildren();
					         detalleElemento ( hijos, arLinea );

				
						} catch (JDOMException e) {
						} catch (IOException e) {
						}
					}		            
		 
		
			        
				}catch (Exception e)
				{ 
					System.out.println("Error"+e);

				}

			}
			
		});
		
		

		
		arArbol.setCellStyleGenerator(new Table.CellStyleGenerator() {                
	        @Override
	        public String getStyle(Table source, Object itemId, Object propertyId) {

	                return "normal";

	        }
	    });				

		/*arArbol.addValueChangeListener(new Property.ValueChangeListener() {
	 
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
	            // Mostramos o ocultamos el detalle en funciÃ³n de si hemos seleccionado el registro o no
				Item row = arArbol.getItem(arArbol.getValue());
				//System.out.print("Valor:" +row.getItemProperty("fecha").getValue());
	            if (event.getProperty().getValue() == null ) {
	            	

	            	
	            	
	            	btVisualizar.setVisible(false);
	            }
	            else {
	            	if ( row.getItemProperty("fecha").getValue() !="" && row.getItemProperty("fecha").getValue() != null ) {
	            		System.out.println("Ocultamos:"+row.getItemProperty("fecha").getValue());
	            		btVisualizar.setVisible(true);
	            		
	            		// ********* Generamos el resource del fichero
	    				Item rowDownload = arArbol.getItem(arArbol.getValue());
	    				nomFichero = rowDownload.getItemProperty("Documentacion").getValue().toString() + "." + rowDownload.getItemProperty("tipo").getValue().toString();
	    				System.out.println("Nombre del fichero a visualizar: " + nomFichero);
	    				
    				
	    				
	            	}
	            	else {
	            		btVisualizar.setVisible(false);
	            	}
	            	
	            }
	
			}
	    });	*/	
		

		
		hl.setMargin(false);
		hl.setSpacing(false);
		

    	vlGlobal.setMargin(false);
    	vlGlobal.setSpacing(false);
    	vlGlobal.addComponent(hl);


    	

    	vlGlobal.addComponent(glLog);
    	vlGlobal.addComponent(new Label(""));
    	vlGlobal.addComponent(btVisualizar);
    	
    	vlGlobal.setComponentAlignment(btVisualizar, Alignment.TOP_RIGHT);
    	


    	hl.setHeight("10%");
    	
    	
    	
    	//ventana.setContent(vlGlobal);
    	//ventana.setCaption("Consultar Documentación Expedientes Liberty");
    	
    	// ConfiraciÃ³n de las tablas
		String[] columnsSiniestros = { "Ramo", "Numero", "fechaApertura","Estado","Tipo","Descripción","RiesgoPoliza",
				"FechaAviso","Causa1","Causa2","Rehuse","CodigoFraude","ConfirmaciónFraude","EstadoRecibo"};
		Object[] typesSiniestros = { String.class, String.class, String.class, String.class
				, String.class, String.class, String.class, String.class, String.class, String.class
				, String.class, String.class, String.class, String.class};
		Object[] visibleColumnsSiniestros = new Object[] {  "Ramo", "Numero", "fechaApertura","Estado","Tipo","Descripción","RiesgoPoliza",
				"FechaAviso","Causa1","Causa2","Rehuse","CodigoFraude","ConfirmaciónFraude","EstadoRecibo" };

		defineTable(arArbol, columnsSiniestros, typesSiniestros, visibleColumnsSiniestros, true);
		arArbol.setColumnHeaders(new String[] {  "Ramo", "Expediente", "fechaApertura","Estado","Tipo","Descripción","RiesgoPoliza",
				"FechaAviso","Causa1","Causa2","Rehuse","CodigoFraude","ConfirmaciónFraude","EstadoRecibo" });

		
		arArbol.setPageLength(10);
		arArbol.setHeight(Integer.valueOf((int) (super.getHeight())).toString() + "px");
		arArbol.setSelectable(true);
		arArbol.setImmediate(true);    

		
        setCompositionRoot(vlGlobal);
       
    }
    
	public void detalleElemento(List hijos, Integer padre) {
		

		
		for (int i = 0; i < hijos.size(); i++) {
			
			String valor = null;
			Element nodo = ( Element ) hijos.get(i);

			// Añadimos los registros
			//System.out.println("Nodo:"+nodo.getName().toString());
			if  ( nodo.getName().toString().equals("list") )  {
			
			arLinea++;
			//System.out.println(">" +arLinea + "[" +padre + "]" + " Nodo:" +nodo.getName().toString() );
			if (nodo.getName().toString().equals("list")) {
				/* "Ramo", "Numero", "fechaApertura","Estado","Tipo","Descripción","RiesgoPoliza",
				"FechaAviso","Causa1","Causa2","Rehuse","CodigoFraude","ConfirmaciónFraude","EstadoRecibo"*/
				
				arArbol.addItem(new Object[]{nodo.getChild("ramo").getValue().toString(), 
											 nodo.getChild("numero").getValue().toString(), 
											 nodo.getChild("fechaApertura").getValue().toString().substring(0, 10),
											 nodo.getChild("estado").getValue().toString(),
											 nodo.getChild("tipo").getValue().toString(),
											 nodo.getChild("descripcion").getValue().toString(),
											 nodo.getChild("riesgoPoliza").getValue().toString(),
											 nodo.getChild("fechaAviso").getValue().toString().substring(0, 10),
											 nodo.getChild("causas").getValue().toString(),
											 nodo.getChild("causas").getValue().toString(),
											 nodo.getChild("rehuse").getValue().toString(),
											 nodo.getChild("codigoFraude").getValue().toString(),
											 nodo.getChild("confirmacionFraude").getValue().toString(),
											 nodo.getChild("codigoEstadoRecibo").getValue().toString()},
											 
						arLinea);
				
			}
				
			}
			
			detalleElemento ( nodo.getChildren(), arLinea );
		}

	}	    
    


	private static void defineTable(Table table,String[]columns, Object[] typeColumns, Object[] visibleColumns, Boolean fullSize){
		
		if (table!=null && columns!=null &&  typeColumns!=null && columns.length == typeColumns.length){
			if(fullSize)table.setSizeFull();
			int index =0;
			for(String column: columns){
				table.addContainerProperty(column, (Class<?>) typeColumns[index], null);
				index++;
			}			
			if (visibleColumns!=null)table.setVisibleColumns(visibleColumns);
		}
	}

    
     
}


