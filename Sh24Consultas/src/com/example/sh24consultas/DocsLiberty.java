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
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DocsLiberty extends CustomComponent  {
    

	
	
	Embedded  cuadroNavegador = new Embedded ("");
		

    public static Integer arLinea = 0;
    public TextField txExp= new TextField();    
    public TextArea texto = new TextArea();
    public int pendiente = 0;
    //public Button btDocumentacion = new Button("Documentación");
    
    
    FileDownloader downloader;
    String nomFichero = "";
    
    public Button btVisualizar= new Button("Visualizar fichero seleccionado");
    public Button btLog= new Button("Log");
    
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
	
	
    
	public  TreeTable arArbol = new TreeTable();
    
    CheckBox ckBorrarLog = new CheckBox("Borrar Log");;
    
    GridLayout glLog = new GridLayout(2, 1);
    
    public DocsLiberty ( )  {
    	
	    btAceptar.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	    
	    btVisualizar.setStyleName(ValoTheme.BUTTON_PRIMARY);
	    btVisualizar.setVisible(false);
	    btLog.setStyleName(ValoTheme.BUTTON_PRIMARY);
	    btLog.setVisible(false);
   	
		glLog.setWidth("100%");
		glLog.setMargin(true);
    	glLog.addComponent(arArbol,0,0);
    	glLog.addComponent(cuadroNavegador,1,0);
    	
    	glLog.setColumnExpandRatio(0, 100);
    	glLog.setColumnExpandRatio(1, 0);
    	
    	

    	textoLog.setWidth("100%");
    	textoLog.setReadOnly(false);
    	textoLog.setVisible(false);

    	VerticalLayout hlIcono = new VerticalLayout();
		Embedded icono = new Embedded( null, new ThemeResource("img/query.png") ) ;
		icono.setWidth("30px");
		icono.setStyleName("imagen-opciones-menu");
		hlIcono.addComponent(icono);
		hlIcono.setWidth("100%");
		hlIcono.setComponentAlignment(icono, Alignment.MIDDLE_CENTER);
		
		Label lblCon = new Label("<center><b>Consultar Documentación Liberty</b><br><hr>", ContentMode.HTML);
		lblCon.setCaptionAsHtml(true);
		
		hlIcono.addComponent(lblCon);

	    HorizontalLayout hl = new HorizontalLayout();
	    txExp.setEnabled(false);
	    hl.addComponent(new Label("Expediente"));
	    hl.addComponent(txExp);
	    hl.addComponent(btAceptar);
	    hl.setMargin(true);

    	btAceptar.setCaption("Refrescar");
		
		// Ejecutamos la consulta con el listener
		
		btAceptar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				

		        
		        ////System.out.println("Consumimos los servicios");

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
		
					//System.out.println("Llamamos al SErvicio");
					java.math.BigDecimal exp = new BigDecimal(txExp.getValue());
					java.math.BigDecimal dw = new BigDecimal(0);

					Context initialContext = new InitialContext();
					Context environmentContext = (Context) initialContext.lookup("java:comp/env");
					String dataResourceName = "jdbc/PgeDB";
					
					DataSource dataSource = (DataSource) environmentContext.lookup(dataResourceName);					
					Connection conn = dataSource.getConnection();
					StringBuilder msg = new StringBuilder();

					
					// hacemos la llamada
					
		    		
					
		    		
		            String callQuery = "{?=call PAC_SHWS.FF_GEDOX_LIBERTY_WS(?,?,?,?,?,?)}";
		            
		            CallableStatement cStmt = conn.prepareCall(callQuery);
		            String USERNAME = conn.getMetaData().getUserName().toUpperCase();
		            cStmt.setObject (2, txExp.getValue().toString());
		            cStmt.setObject (3, "0");	
		            cStmt.registerOutParameter (1, java.sql.Types.INTEGER); // Valor de "RETURN"
		            cStmt.registerOutParameter (4, java.sql.Types.CLOB); // Valor de "RESULTADO
		            cStmt.registerOutParameter (5, java.sql.Types.VARCHAR); // Valor de "URL
		            cStmt.registerOutParameter (6, java.sql.Types.VARCHAR); // Valor de "USUARIO
		            cStmt.registerOutParameter (7, java.sql.Types.VARCHAR); // Valor de "PWD

		            cStmt.execute ();
		            HashMap retVal = new HashMap ();
		            try{
		                    retVal.put ("RETURN", cStmt.getObject (1));
		            }catch (SQLException e){
		                    retVal.put ("RETURN", null);
		            }
		            
		            try{
		            	
		            	retVal.put ("RESULTADO", cStmt.getObject (4));
		            	retVal.put ("URL", cStmt.getObject (5));
		            	retVal.put ("USUARIO", cStmt.getObject (6));
		            	retVal.put ("PWD", cStmt.getObject (7));
   	                    
		            }catch (SQLException e){
		            	
		                retVal.put ("RESULTADO", null);
		                retVal.put ("URL", null);
		                retVal.put ("USUARIO", null);
		                retVal.put ("PWD", null);
					} finally {
						// Release connection back to the pool
						if (conn != null) {
							conn.close();
						}
						conn = null; // prevent any future access
					}
		            
					arArbol.removeAllItems();
					
	                retVal=new ConversionUtil().convertOracleObjects(retVal);//AXIS-WLS1SERVER-Ready
	                pContenido = retVal.get("RESULTADO").toString();
	                
	                
	                
	                url = retVal.get("URL").toString();
	                
	                
	                usuario = retVal.get("USUARIO").toString();
	                pwd = retVal.get("PWD").toString();
				    document = saxBuilder.build(new StringReader(pContenido));
				    
				    
		            //System.out.println("****** DESPUES DE EJECUTAR ***********"+pContenido);
		            
					if (pContenido!=null) {
						try {
							 //System.out.println("Entramos al if");
							 // borramos el arbol del xml
							 arArbol.removeAllItems();
						     document = saxBuilder.build(new StringReader(pContenido));
						     //arArbol.addItem(new Object[]{document.getRootElement().getName(), null, null}, arLinea);
						     //arArbol.setParent(arLinea, arLinea);
					         Element classElement = document.getRootElement();
					         List<Element> hijos = classElement.getChildren();
					         detalleElemento ( hijos, arLinea );
					         // EXPANDIR ARBOL
					         for (Object itemId: arArbol.getContainerDataSource().getItemIds()) {
					        	 arArbol.setCollapsed(itemId, false);
				
					             if (! arArbol.hasChildren(itemId))
					            	 arArbol.setChildrenAllowed(itemId, false);
					         }
				
						} catch (JDOMException e) {
						} catch (IOException e) {
							
							System.out.println( e ); 
						}
					}		            
		 
		
			        
				}catch (Exception e)
				{ 
					e.printStackTrace();
					System.out.println("Error"+e.getCause().getMessage());

				}

			}
			
		});
		
		

		
			
		btVisualizar.addClickListener(new ClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void buttonClick(ClickEvent event) {
				
				//System.out.println("PUlsamos para bajar el fichero");
                				
			}
		});
		
		
		
		arArbol.setCellStyleGenerator(new Table.CellStyleGenerator() {                
	        @Override
	        public String getStyle(Table source, Object itemId, Object propertyId) {

	                return "normal";

	        }
	    });				

		arArbol.addValueChangeListener(new Property.ValueChangeListener() {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
	            // Mostramos o ocultamos el detalle en funciÃ³n de si hemos seleccionado el registro o no
				Item row = arArbol.getItem(arArbol.getValue());
				//System.out.print("Valor:" +row.getItemProperty("fecha").getValue());
	            if (event.getProperty().getValue() == null ) {
	            	

	            	
	            	
	            	btVisualizar.setVisible(false);
	            	btLog.setVisible(false);
	            }
	            else {
	            	if ( row.getItemProperty("fecha").getValue() !="" && row.getItemProperty("fecha").getValue() != null ) {
	            		//System.out.println("Ocultamos:"+row.getItemProperty("fecha").getValue());
	            		btVisualizar.setVisible(true);
	            		btLog.setVisible(false);
	            		
	            		// ********* Generamos el resource del fichero
	    				Item rowDownload = arArbol.getItem(arArbol.getValue());
	    				nomFichero = rowDownload.getItemProperty("Documentacion").getValue().toString().replace("+", "") + "_" + rowDownload.getItemProperty("fecha").getValue().toString() + "." + rowDownload.getItemProperty("tipo").getValue().toString();
	    				//System.out.println("Nombre del fichero a visualizar: " + nomFichero);
	    				
	    				
	    				if (downloader!=null) downloader.remove();
	    				
	    				downloader = new FileDownloader(new StreamResource(new StreamSource() {
	    					  @Override
	    					  public InputStream getStream () {
	    					    // this is an expensive operation, so we do it on demand

	    						//System.out.println("Entramos en downloadER");
	    		            	ByteArrayInputStream docStream = null;
	    		            	//System.out.println("llamamos a wsdonwload desde attach");
	    		            	WsDownload documento = new WsDownload();
	    						try {
	    							docStream = documento.WsDonwload(DocsLiberty.this , txExp.getValue().toString(), rowDownload.getItemProperty("fecha").getValue().toString(),
	    									                         rowDownload.getItemProperty("tipo").getValue().toString(),
	    									                         url,
	    									                         usuario,
	    									                         pwd
	    									              );
	    						} catch (Exception e) {
	    							// TODO Auto-generated catch block
	    							e.printStackTrace();
	    						}
	    						return docStream;
	    						
	    						
	    					  }
	    					}, nomFichero ));
	    				downloader.extend(btVisualizar);
	    				
	    
	    			    /*Embedded e = new Embedded();
	    			    e.setSizeFull();
	    			    e.setSource(downloader.getFileDownloadResource());
	    			    e.setHeight("650px");
	    			    
	    			    Window plaintReoprtWindow = new Window("Report");
	    			    plaintReoprtWindow.center();
	    			    plaintReoprtWindow.setModal(true);
	    			    plaintReoprtWindow.setHeight("700px");
	    			    plaintReoprtWindow.setWidth("900px");*/
	    			    
	    			    
	    			    //BrowserWindowOpener ventana = new BrowserWindowOpener(downloader.getFileDownloadResource());
	    			    
	    			

	    				/*cuadroNavegador.setSource(downloader.getFileDownloadResource());
	    				cuadroNavegador.setMimeType(rowDownload.getItemProperty("tipo").getValue().toString()); // Unnecessary
	    				cuadroNavegador.setStandby("Cargando....");
	    				cuadroNavegador.setWidth("500px");
	    				cuadroNavegador.setHeight("200px");*/
	    				
	    				
	            	}
	            	else {
	            		btVisualizar.setVisible(false);
	            		btLog.setVisible(false);
	            	}
	            	
	            }
	
			}
	    });		
		

		
		hl.setMargin(false);
		hl.setSpacing(false);
		

    	vlGlobal.setMargin(false);
    	vlGlobal.setSpacing(false);
    	vlGlobal.addComponent(hlIcono);
    	vlGlobal.addComponent(hl);


    	

    	vlGlobal.addComponent(glLog);
    	vlGlobal.addComponent(new Label(""));
    	vlGlobal.addComponent(btVisualizar);
    	vlGlobal.setComponentAlignment(btVisualizar, Alignment.TOP_RIGHT);

    	vlGlobal.addComponent(btLog);
    	vlGlobal.setComponentAlignment(btLog, Alignment.TOP_LEFT);
    	
    	vlGlobal.addComponent(textoLog);
    	vlGlobal.setComponentAlignment(textoLog, Alignment.TOP_LEFT);    	
    	


    	hl.setHeight("10%");
    	
    	
    	
    	//ventana.setContent(vlGlobal);
    	//ventana.setCaption("Consultar Documentación Expedientes Liberty");
    	
    	// ConfiraciÃ³n de las tablas
		String[] columnsGarantias = { "Documentacion", "tipo", "fecha" };
		Object[] typesGarantias = { String.class, String.class, String.class };
		Object[] visibleColumnsGarantias = new Object[] { "Documentacion", "tipo", "fecha" };

		defineTable(arArbol, columnsGarantias, typesGarantias, visibleColumnsGarantias, true);
		arArbol.setColumnHeaders(new String[] { "Documentación", "Tipo fichero", "Id." });

		
		//arArbol.setPageLength(10);
		arArbol.setHeight(Integer.valueOf((int) (super.getHeight())).toString() + "px");
		arArbol.setSelectable(true);
		arArbol.setColumnExpandRatio("Documentacion", 50);
		arArbol.setColumnExpandRatio("tipo", 25);
		arArbol.setColumnExpandRatio("fecha", 25);

		//arArbol.setSizeFull();
		arArbol.setImmediate(true); 
		arArbol.setPageLength(8);

		
        setCompositionRoot(vlGlobal);
       
    }
    
	public void detalleElemento(List hijos, Integer padre) {
		

		
		for (int i = 0; i < hijos.size(); i++) {
			
			String valor = null;
			Element nodo = ( Element ) hijos.get(i);
			
			
			if ( nodo.getChildren().size()==0 ) {
				
				valor = nodo.getValue().toString();
						
			} 
			else {
				valor = null;
			}
			
			
			// aÃ±adimos el registro si no es un soapenv
			
			if  ( nodo.getNamespacePrefix()!=null && 
					(
							nodo.getNamespacePrefix().toLowerCase().equals("soapenv")
						||  nodo.getNamespacePrefix().toLowerCase().equals("soap")
						||  nodo.getNamespacePrefix().toLowerCase().equals("res")
						||  nodo.getName().toString().equals("documentListGd")
						||  nodo.getName().toString().equals("result")
						||  nodo.getName().toString().equals("claimCode")
					) 
				) {
				// no hacemos nada
				
			}
			else {
				
				arLinea++;
				if ( valor == null ) {
					
						//System.out.println(">" +arLinea + "[" +padre + "]" + " Nodo:" +nodo.getName().toString() + " Valor: " +valor);
						if (nodo.getName().toString().equals("documentList")) {
							arArbol.addItem(new Object[]{nodo.getChild("name").getValue().toString(), 
														 nodo.getChild("type").getValue().toString(), 
														 nodo.getChild("id").getValue().toString()}, arLinea);
							
					    	arArbol.setParent(arLinea, padre);
						}
						if (nodo.getName().toString().equals("folderList")) {
							arArbol.addItem(new Object[]{nodo.getChild("name").getValue().toString(), 
														 "", 
														 ""}, arLinea);
					    	arArbol.setParent(arLinea, padre);
						}						
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


