package com.example.sh24consultas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.google.gwt.dom.client.Style;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
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
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AnadirDocumento extends CustomComponent {

	String carpeta = "";
	String tfNombreFichero = "";
	ByteArrayOutputStream outputBuffer = null;
	//String encString = "";

	class ImageUploader implements Receiver, StartedListener {
	    public File file;
	    ByteArrayOutputStream outputBuffer = null;
	    
	    public InputStream getStream() {
	        if (outputBuffer == null) {
	            return null;
	        }
	        return new ByteArrayInputStream(outputBuffer.toByteArray());
	    }	    

	    public OutputStream receiveUpload(String filename,
	                                      String mimeType) {
	        // Create and return a file output stream
	    	
	    	System.out.println("______________ImageUploader_________________" + filename  );
	    	
	    	outputBuffer = new ByteArrayOutputStream();
	        return outputBuffer;
	    	
	    	/*outputBuffer = new ByteArrayOutputStream();
	    	
	    	//Get the uploaded image
			System.out.println("Outputleng:" + outputBuffer.size());
		    final byte[] data = outputBuffer.toByteArray();
			Base64 enc = new Base64();
			String encString = enc.encodeToString(data);				
			System.out.println("Codificado:"+ encString);
			// Pasamos el fichero a Base64
	        return outputBuffer;*/
	    	
	    }

		@Override
		public void uploadStarted(StartedEvent event) {
			// TODO Auto-generated method stub
			
		}
	    

	};

	ImageUploader receiver = new ImageUploader();
	
	
	
	Upload upload = new Upload("Seleccionar fichero a subir", receiver);
	
	
	
	public static Integer arLinea = 0;
	public TextField txExp = new TextField();
	public TextArea texto = new TextArea();
	public int pendiente = 0;
	public Button btDocumentacion = new Button("Documentación");

	String nomFichero = "";
	public Button btSubir = new Button("Confirmar la subida del fichero a Liberty");
	public Button btLog = new Button("Log");

	TextArea textoLog = new TextArea();

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
	public Table arTabla = new Table();
	CheckBox ckBorrarLog = new CheckBox("Borrar Log");;
	GridLayout glLog = new GridLayout(2, 1);
	
	public AnadirDocumento() {

		btAceptar.setStyleName(ValoTheme.BUTTON_FRIENDLY);


		btLog.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btLog.setVisible(false);

		glLog.setWidth("100%");
		glLog.setMargin(true);
		glLog.addComponent(arTabla, 0, 0);

		glLog.setColumnExpandRatio(0, 100);
		glLog.setColumnExpandRatio(1, 0);

		textoLog.setWidth("100%");
		textoLog.setReadOnly(false);

		VerticalLayout hlIcono = new VerticalLayout();
		Embedded icono = new Embedded( null, new ThemeResource("img/upload.png") ) ;
		icono.setWidth("30px");
		icono.setStyleName("imagen-opciones-menu");
		hlIcono.addComponent(icono);
		hlIcono.setWidth("100%");
		hlIcono.setComponentAlignment(icono, Alignment.MIDDLE_CENTER);
		
		
		Label lblCon = new Label("<center><b>Subir Documentación a Liberty</b><br><hr>", ContentMode.HTML);
		lblCon.setCaptionAsHtml(true);
		hlIcono.addComponent(lblCon);
		
		HorizontalLayout hl = new HorizontalLayout();
		txExp.setEnabled(false);

		hl.addComponent(new Label("Añadir documentación al Expediente Liberty: "));
		hl.addComponent(txExp);
		hl.addComponent(btAceptar);

		
		hl.setMargin(true);

		HorizontalLayout hl2 = new HorizontalLayout();
		hl2.addComponent(new Label("Seleccione la categoría a la que pertenece el documento: "));

		btAceptar.setCaption("Refrescar");

		// Ejecutamos la consulta con el listener

		btAceptar.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				//// System.out.println("Consumimos los servicios");

				Connection conexion = null;
				String consulta;
				Boolean resprod;
				Clob esquema;

				SAXBuilder saxBuilder = new SAXBuilder();
				Document documentRespuesta;
				Document document;

				try {

					String pContenido = "";
					arTabla.removeAllItems();

					// System.out.println("Llamamos al SErvicio");
					java.math.BigDecimal exp = new BigDecimal(txExp.getValue());
					java.math.BigDecimal dw = new BigDecimal(0);

					Context initialContext = new InitialContext();
					Context environmentContext = (Context) initialContext.lookup("java:comp/env");
					String dataResourceName = "jdbc/PgeDB";

					DataSource dataSource = (DataSource) environmentContext.lookup(dataResourceName);
					Connection conn = dataSource.getConnection();
					StringBuilder msg = new StringBuilder();

					// hacemos la llamada

					String callQuery = "{?=call PAC_SHWS.FF_GEDOX_SUBIRDOC_LIBERTY_WS(?,?,?,?,?,?)}";

					CallableStatement cStmt = conn.prepareCall(callQuery);
					String USERNAME = conn.getMetaData().getUserName().toUpperCase();
					cStmt.setObject(2, txExp.getValue().toString());
					cStmt.setObject(3, "0");
					cStmt.registerOutParameter(1, java.sql.Types.INTEGER); // Valor
																			// de
																			// "RETURN"
					cStmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR); // Valor
																					// de
																					// "cursor"
					cStmt.registerOutParameter(5, java.sql.Types.VARCHAR); // Valor
																			// de
																			// "URL
					cStmt.registerOutParameter(6, java.sql.Types.VARCHAR); // Valor
																			// de
																			// "USUARIO
					cStmt.registerOutParameter(7, java.sql.Types.VARCHAR); // Valor
																			// de
																			// "PWD

					cStmt.execute();

					HashMap retVal = new HashMap();
					try {
						retVal.put("RETURN", cStmt.getObject(1));
					} catch (SQLException e) {
						retVal.put("RETURN", null);
					}

					try {

						ResultSet rset = (ResultSet) cStmt.getObject(4);
						while (rset.next()) {
							System.out.println(rset.getString("NAME"));
							Object newItemId = arTabla.addItem();
							Item row1 = arTabla.getItem(newItemId);
							row1.getItemProperty("Id").setValue(rset.getString("ID"));
							row1.getItemProperty("Carpeta").setValue(rset.getString("NAME"));
							row1.getItemProperty("Fecha").setValue(rset.getString("FECHA"));
						}

						url = cStmt.getObject(5).toString();
						usuario = cStmt.getObject(6).toString();
						pwd = cStmt.getObject(7).toString();

					} catch (SQLException e) {
						arTabla.removeAllItems();

					} finally {
						// Release connection back to the pool
						if (conn != null) {
							conn.close();
						}
						conn = null; // prevent any future access
					}

				} catch (Exception e) {
					e.printStackTrace();

				}

			}

		});
		
	
		
		arTabla.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("**********click listener**********************");
			}
		});

		arTabla.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				
				System.out.println("Entramos");
				// Mostramos o ocultamos el detalle en funciÃ³n de si hemos
				// seleccionado el registro o no
				Item row = arTabla.getItem(arTabla.getValue());
				// System.out.print("Valor:"
				// +row.getItemProperty("fecha").getValue());
				if (event.getProperty().getValue() == null) {

					upload.setVisible(false);
					carpeta = "";

				} else {
					if (row.getItemProperty("Fecha").getValue() != ""
							&& row.getItemProperty("Fecha").getValue() != null) {
						// System.out.println("Ocultamos:"+row.getItemProperty("fecha").getValue());
						upload.setVisible(true);
						

						// ********* Generamos el resource del fichero
						Item rowUpload = arTabla.getItem(arTabla.getValue());
						//nomFichero = rowUpload.getItemProperty("Id").getValue().toString().replace("+", "") + "_"
						//		+ rowUpload.getItemProperty("Fecha").getValue().toString() + "."
						//		+ rowUpload.getItemProperty("Id").getValue().toString();
						
						carpeta = rowUpload.getItemProperty("Id").getValue().toString();
						System.out.println("Carpeta visualizar: " + carpeta);
						btSubir.setVisible(true);

					} else {
						btSubir.setVisible(false);
						carpeta = "";
						upload.setVisible(false);
						btLog.setVisible(false);
					}

				}

			}
		});

		arTabla.setCellStyleGenerator(new Table.CellStyleGenerator() {
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {

				return "normal";

			}
		});

		hl.setMargin(true);
		hl.setSpacing(true);

		hl2.setMargin(true);
		hl2.setSpacing(true);

		vlGlobal.setMargin(false);
		vlGlobal.setSpacing(false);
		vlGlobal.addComponent(hlIcono);
		
		vlGlobal.addComponent(hl);
		vlGlobal.addComponent(hl2);

		vlGlobal.addComponent(glLog);
		//vlGlobal.addComponent(new Label(""));

		vlGlobal.addComponent(btLog);
		vlGlobal.setComponentAlignment(btLog, Alignment.TOP_LEFT);

		HorizontalLayout hlf = new HorizontalLayout();
		hlf.addComponent(upload);
		hlf.setMargin(true);
		hlf.addComponent(new Label());
		hlf.addComponent(new Label());
		hlf.addComponent(btSubir);		
		vlGlobal.addComponent(hlf);
		
		btSubir.setVisible(false);
		btSubir.setStyleName(ValoTheme.BUTTON_PRIMARY);
		hlf.setComponentAlignment(btSubir, Alignment.BOTTOM_RIGHT);
		hlf.setWidth("95%");
		

		hl.setHeight("10%");

		// ventana.setContent(vlGlobal);
		// ventana.setCaption("Consultar Documentación Expedientes Liberty");

		// ConfiraciÃ³n de las tablas
		String[] columnsGarantias = { "Id", "Carpeta", "Fecha" };
		Object[] typesGarantias = { String.class, String.class, String.class };
		Object[] visibleColumnsGarantias = new Object[] { "Carpeta" };

		defineTable(arTabla, columnsGarantias, typesGarantias, visibleColumnsGarantias, true);
		arTabla.setColumnHeaders(new String[] { "Categoría" });

		// arTabla.setPageLength(10);
		arTabla.setHeight(Integer.valueOf((int) (super.getHeight())).toString() + "px");
		arTabla.setSelectable(true);
		arTabla.setColumnExpandRatio("Id", 10);
		arTabla.setColumnExpandRatio("Carpeta", 90);
		arTabla.setPageLength(6);
		// arTabla.setSizeFull();
		arTabla.setImmediate(true);
		arTabla.setPageLength(8);

		setCompositionRoot(vlGlobal);
		
		
		btSubir.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			
				System.out.println("Categoria"+arTabla.getValue() );
				

				upload.setImmediate(false);
				upload.submitUpload();
			}
		});
		
		
		
		upload.addStartedListener(new StartedListener() {
			
			
			@Override
			public void uploadStarted(StartedEvent event) {
				// TODO Auto-generated method stub
				System.out.println("****************** STARTED *******************");
				System.out.println("Longitud del fichero:" +  event.getContentLength() );
				
				if ( event.getContentLength()==0 ) {
					upload.interruptUpload();
					new Notification("Error fichero",
							"Seleccione primero un fichero a subir",
							Notification.Type.ERROR_MESSAGE, true)
							.show(Page.getCurrent());
					
					
					
				}
			}
		});
		
		upload.addFinishedListener(new FinishedListener() {
			
			@Override
			public void uploadFinished(FinishedEvent event) {
				// TODO Auto-generated method stub
		
				System.out.println("****************** FINISHED *******************");
				
				System.out.println("Nombredel fichero: " + event.getFilename() );
				System.out.println("Longitud del fichero: " + event.getLength() );
				System.out.println("Event: " + event);
				System.out.println("Longitud del fichero: " + receiver.outputBuffer.size() );
			    final byte[] data = receiver.outputBuffer.toByteArray();
				Base64 enc = new Base64();
				String encString = enc.encodeToString(data);				
				System.out.println("Codificado:"+ encString.length());
				
				// Aqui tenemos que llamar al Servicio para que se envié el documento
				
				Connection conexion = null;
				String consulta;
				Boolean resprod;
				Clob esquema;

				SAXBuilder saxBuilder = new SAXBuilder();
				Document documentRespuesta;
				Document document;

				try {

					String pContenido = "";
					

					java.math.BigDecimal exp = new BigDecimal(txExp.getValue());
					java.math.BigDecimal dw = new BigDecimal(0);

					Context initialContext = new InitialContext();
					Context environmentContext = (Context) initialContext.lookup("java:comp/env");
					String dataResourceName = "jdbc/PgeDB";

					DataSource dataSource = (DataSource) environmentContext.lookup(dataResourceName);
					Connection conn = dataSource.getConnection();
					StringBuilder msg = new StringBuilder();

					// hacemos la llamada

					String callQuery = "{?=call PAC_SHWS.FF_GEDOX_SUBIRDOC_LIBERTY_WS(?,?,?,?,?,?,?,?,?)}";

					CallableStatement cStmt = conn.prepareCall(callQuery);
					String USERNAME = conn.getMetaData().getUserName().toUpperCase();
					cStmt.setObject(2, txExp.getValue().toString());
					cStmt.setObject(3, "1");
					cStmt.registerOutParameter(1, java.sql.Types.INTEGER); // Valor
																			// de
																			// "RETURN"
					cStmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR); // Valor
																					// de
																					// "cursor"

					
					cStmt.registerOutParameter(5, java.sql.Types.VARCHAR); // Valor
																			// de
																			// "URL
					cStmt.registerOutParameter(6, java.sql.Types.VARCHAR); // Valor
																			// de
																			// "USUARIO
					cStmt.registerOutParameter(7, java.sql.Types.VARCHAR); // Valor
																			// de
																			// "PWD

					cStmt.setObject(8, carpeta); // Valor
					// de
					// "CARPETA
					cStmt.setObject(9, event.getFilename().substring(event.getFilename().indexOf(".")+1)); // Valor
					// de
					// "EXTENSION"
					cStmt.setObject(10, encString); // Valor
					// de
					// "CONTENIDO DEL FICHERO EN BASE64"
					
					/*System.out.println("Carpeta:" + carpeta);
					System.out.println("Extension:" + event.getFilename().substring(event.getFilename().indexOf(".")+1));
					System.out.println("Contenido:" + encString);*/

					cStmt.execute();
					
					System.out.println(cStmt.getObject(1));
					
					if ( cStmt.getObject(1).toString().equals("0") ) {
						new Notification("Error al subir el documento",
    							"Se ha producido un error al subir el documento",
    							Notification.Type.ERROR_MESSAGE, true)
    							.show(Page.getCurrent());
					} else {

						new Notification("Proceso finalizado correctamente",
    							"Se ha subido el fichero.",
    							Notification.Type.TRAY_NOTIFICATION, true)
    							.show(Page.getCurrent());
						
					}					

					HashMap retVal = new HashMap();
					try {
						retVal.put("RETURN", cStmt.getObject(1));
					} catch (SQLException e) {
						retVal.put("RETURN", null);
					} finally {
						// Release connection back to the pool
						if (conn != null) {
							conn.close();
						}
						conn = null; // prevent any future access
					}
					


				} catch (Exception e) {
					e.printStackTrace();

				}				
				
				
			}
		});

		


	}

	private static void defineTable(Table table, String[] columns, Object[] typeColumns, Object[] visibleColumns,
			Boolean fullSize) {

		if (table != null && columns != null && typeColumns != null && columns.length == typeColumns.length) {
			if (fullSize)
				table.setSizeFull();
			int index = 0;
			for (String column : columns) {
				table.addContainerProperty(column, (Class<?>) typeColumns[index], null);
				index++;
			}
			if (visibleColumns != null)
				table.setVisibleColumns(visibleColumns);
		}
	}

}
