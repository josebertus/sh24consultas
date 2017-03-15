package com.sh24Consultas.utils;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Clase que permite recuperar las propiedades generales del terminal.
 * <ul>
 * 
 * <li><code>Fecha de creación</code>: Setiembre 2007
 * </ul>
 * 
 */
public class ApplicationProperties {

	/**
	 * Logger de la clase ApplicationProperties
	 */
	//static Log logger = LogFactory.getLog("com.sh24Consultas");
	/**
	 * Objeto de la misma clase
	 */
	private static ApplicationProperties yo;
	/**
	 * Fecha ultima actualización
	 */
	private static Date ultimaActualizacion;
	/**
	 * Variable con las propiedades
	 */
	private static Properties props;
	// Tiempo maximo por defecto
	private static final long porDefectoMAXIMOCACHEOENMILISEGUNDOS = 1000 * 60 * 60;
	/**
	 * Tiempo maximo
	 */  
	private static long MAXIMOCACHEOENMILISEGUNDOS = porDefectoMAXIMOCACHEOENMILISEGUNDOS;
	/**
	 * Nombre de las propiedades
	 */

	/**
	 * Instancia de ApplicationProperties
	 */
	private ApplicationProperties() {
		try {
			ApplicationProperties.recargar();
		} catch (Exception e) {
			//logger.fatal("Error durante la incializacion y primera carga de ApplicationProperties.", e);
		}
	}

	/**
	 * Recupera una instancia de ApplicationProperties

	 */
	public static ApplicationProperties getInstance() {
		return yo;
	}
	/**
	 * Recupera las propiedades del terminal. Si ha pasado del tiempo máximo recarga previamente las propiedades
	 * @return java.util.Properties
	 * @throws Exception
	 */
	public static Properties getProps() throws Exception {
		if (ultimaActualizacion == null || (ultimaActualizacion.getTime() + ApplicationProperties.MAXIMOCACHEOENMILISEGUNDOS) < (new Date()).getTime()) {
			recargar();
		}
		return ApplicationProperties.props;
	}
	/**
	 * Modificación del tiempo máximo de las propiedades
	 * @param tiempo
	 */
	public static void setMAXIMOCACHEOENMILISEGUNDOS(long tiempo) {
		ApplicationProperties.MAXIMOCACHEOENMILISEGUNDOS=tiempo;
		if (ApplicationProperties.MAXIMOCACHEOENMILISEGUNDOS<0)
			ApplicationProperties.MAXIMOCACHEOENMILISEGUNDOS = ApplicationProperties.porDefectoMAXIMOCACHEOENMILISEGUNDOS;
	}

	/**
	 * Método que recarga las propiedades del terminal
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean recargar() throws Exception {
		if (props == null) {
			props = new Properties();
		}
		else {
			props.clear();
		}
		props.load(ApplicationProperties.class.getResourceAsStream("../../../sh24Consultas.properties"));
		//logger.debug("Propiedades (re-)cargadas:"+props);
		ultimaActualizacion=new Date();
		return true;
	}

}
