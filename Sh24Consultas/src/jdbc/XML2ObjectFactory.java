package jdbc;

import java.io.ByteArrayInputStream;
import java.io.File;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class XML2ObjectFactory extends DefaultHandler {
    protected static final String test_XML_FILE_NAME = "src/axis/util/testXML/OB_IAX_PRODUCTO.XML";
    
    private String rootNodeName = "";
    private Class currentNodeClass = null;
    private String currentNodeValue = "";
    private LinkedList listOfParents = new LinkedList();
    private List collectionsNames = new ArrayList();
    
    public static void main (String argv [])
    {
        /* Test como fichero */

        Object object = new XML2ObjectFactory().createObjectFromXMLFile(test_XML_FILE_NAME);
        //log.debug("Objeto creado a partir de XML " + test_XML_FILE_NAME + ":\n");        
        if (object != null) {
            //log.debug("Tipo del objeto: " + object.getClass().getName() + "\n");        
            //log.debug(object);
        } 
        
        /* Test como String */
         
         /*String XMLString ="<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
         "<OB_IAX_PRODUCTO jclass=\"java.util.HashMap\">" + 
         " <PGAEXEX jclass=\"java.lang.String\">Hola</PGAEXEX>" +
         "<CTERMFIN jclass=\"java.math.BigDecimal\">1</CTERMFIN>" + 
         "</OB_IAX_PRODUCTO>";
        
        logger.debug(new XML2ObjectFactory().createObjectFromXMLString(XMLString));        
        */
    }


    /**
     * Dado un String conteniendo c�digo XML v�lido, devuelve el objeto resultado
     * de "unmarshalear" el XML a objetos Java.
     * 
     * El fichero XML debe ser un XML normal, con un atributo "jclass" por
     * cada nodo, que especifique la clase Java del atributo.
     * 
     * Para conseguir un XML de prueba, ejecutar 
     * <code>ConversionUtil.printAsXML(Object o, String rootElement)</code>.
     * 
     * @param XML_STRING String conteniendo un fichero XML.
     * @return Object El objeto creado que representa el XML.
     */
    public Object createObjectFromXMLString(String XML_STRING) {
        return this.createObject(XML_STRING, false);
    }
    
    /**
     * Dado un fichero XML existente, devuelve el objeto resultado
     * de "unmarshalear" el XML a objetos Java.
     * 
     * El fichero XML debe ser un XML normal, con un atributo "jclass" por
     * cada nodo, que especifique la clase Java del atributo.
     * 
     * Para conseguir un XML de prueba, ejecutar 
     * <code>ConversionUtil.printAsXML(Object o, String rootElement)</code>.
     * 
     * @param XML_FILE_NAME Path hacia el fichero XML.
     * @return Object El objeto creado que representa el XML.
     */
     public Object createObjectFromXMLFile(String XML_FILE_NAME) {
        return this.createObject(XML_FILE_NAME, true);
     }
     
     
     private Object createObject(String XML_STRING_or_FILE_NAME, boolean isFile) {
        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            if (isFile)
                saxParser.parse( new File(XML_STRING_or_FILE_NAME), this);            
            else {              
                // Convertir el String a InputStream
                StringBuffer sb = new StringBuffer(XML_STRING_or_FILE_NAME);
                ByteArrayInputStream bis = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
                saxParser.parse(bis, this);            
            }
            
        } catch (Throwable t) {
            t.printStackTrace ();
        }        
        return (listOfParents.get(0));        
    }


    //===========================================================
    // Methods in SAX DocumentHandler 
    //===========================================================

    public void startDocument ()
    throws SAXException
    {
    }

    public void endDocument ()
    throws SAXException
    {
    }

    public void startElement (String uri, String localName,
                              String qName, Attributes attrs)
    throws SAXException
    {
        try {   
            // System.out.println(qName);            
            currentNodeClass = getJClass(attrs);

            if (currentNodeClass != null) {                                              
                if (listOfParents.isEmpty()) { 
                    // Elemento Root del documento XML                                
                    Object rootObject = instantiate(currentNodeClass, null);
                    rootNodeName = qName;
                    listOfParents.add(rootObject);                 
                } else {
                    // Otros objetos que no son el root
                     if (currentNodeClass.getName().indexOf("java.util.") >= 0) {
                        // Es una colecci�n
                        // System.out.println("Colecci�:" + currentNodeClass.getName());                        
                        // A�adir a una tabla adicional, para saber que no tenemos que tratar su "endElement"
                        collectionsNames.add(qName);
                        // Recuperamos el padre currentRootObject
                        Object currentRootObject = listOfParents.getLast();
                        // Instanciamos el hijo currentObject
                        Object currentObject = instantiate(currentNodeClass, isEmpty(currentNodeValue) ? null : currentNodeValue);                        
                        
                        // A�adirlo al currentRootObject (su padre)                                         
                        if (currentRootObject instanceof java.util.Map) {
                            Map map = (Map) currentRootObject;
                            map.put(qName, currentObject);
                        } else if (currentRootObject instanceof java.util.List) {
                            List list = (List) currentRootObject;
                            list.add(currentObject);
                        }                             
                         
                        listOfParents.add(currentObject);                                                                                  
                     }
                }
            }    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void endElement (String uri, String localName, String qName)
    throws SAXException
    {    
        if (collectionsNames.contains(qName)) {
            // Es el cierre de una colecci�n, quitarla de las listas
            collectionsNames.remove(qName); 
            listOfParents.removeLast();
        }
        
        else if (qName != rootNodeName && currentNodeClass != null && currentNodeClass.getName().indexOf("java.util.") < 0) {     
            // No es una colecci�n, sino un primitivo
            
            // System.out.println(currentNodeClass.getName());
            Object currentObject = instantiate(currentNodeClass, isEmpty(currentNodeValue) ? null : currentNodeValue);                                                         
            currentNodeValue = "";
            
            // Recuperar padre del elemento primitivo (currentRootObject)
            Object currentRootObject = listOfParents.getLast();
            
            // A�adir el dato primitivo a su currentRootObject
            if (currentRootObject instanceof java.util.Map) {
                Map map = (Map) currentRootObject;
                map.put(qName, currentObject);
            } else if (currentRootObject instanceof java.util.List) {
                List list = (List) currentRootObject;
                list.add(currentObject);
            }
        }
    }

    public void characters (char buf [], int offset, int len)
    throws SAXException
    {
        // Recuperar valor del elemento
        String s = new String(buf, offset, len);
        currentNodeValue = s;          
    }

    //===========================================================
    // Helpers Methods
    //===========================================================
    
    private boolean isEmpty(String s) {
        return (s == null || s.trim().equals(""));
    }
    
    private Class getJClass (Attributes attrs) {
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength (); i++) {
                if ("jclass".equals(attrs.getQName(i))) {                                                
                    try {
                        return Class.forName(attrs.getValue (i));
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }        
        return null;
    }
    
    private Object instantiate (Class klass, String initValue) {
        try {
            if (isEmpty(initValue)) {
                // Constructor por defecto
                return klass.newInstance();
            } else {
                // No se puede instanciar porque no tiene un constructor sin argumentos
                 Constructor constructor = klass.getConstructor(new Class[] {String.class});
                 return constructor.newInstance(new Object[] {initValue});
            }
        } catch (Exception ex) {
            return null;
        }
    }
}