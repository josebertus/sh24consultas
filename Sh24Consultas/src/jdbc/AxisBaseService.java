package jdbc;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * Super clase para los servicios de la aplicaci�n.
 * Contiene m�todos comunes a todos los servicios.
 * 
 */
public class AxisBaseService {
    static Log logger=LogFactory.getLog(AxisBaseService.class);

    //ecg201201018 hemos de a�adir el control del SOSERVER aqui tb
    private static String SO="WINDOWS";
    private static String SOSLASH="\\"; //es el slash segun OS, por defecto es el backslash para WINDOWS
    private static String SOSLASH_MALO="/"; //es el slash malo (por defectpo el /) que hay q sustituir por el SOSPLASH
    static {
        SO=ConfigCache.getConfig().getProperty("SOSERVER", "WINDOWS");
        if (SO.equals("UNIX")) {
            SOSLASH="/"; //el slash de UNIX
            SOSLASH_MALO="\\"; //ahora el backslash de WINDOWS es el malo
        }
    }

    /**  
     * Comprueba que el String pasado no es nulo ni est� en blanco.
     * @param s El String a comprobar.
     * @return boolean True si es nulo o en blanco, False en otro caso.
     */
    public static boolean isEmpty(String s) {
        return (s==null||s.equals(""));
    }

    /**
     * Comprueba que la Collection pasada no es nula ni est� vac�a.
     * @param col La Collection a comprobar (List, ArrayList, Set...).
     * @return boolean True si es nula o vac�a, False en otro caso.
     */
    public static boolean isEmpty(Collection col) {
        return (col==null||col.isEmpty());
    }

    /**
     * Comprueba que el Map pasado no es nulo ni est� vac�o.
     * @param map El Map a comprobar (Map, HashMap...).
     * @return boolean True si es nulo o vac�o, False en otro caso.
     */
    public static boolean isEmpty(Map map) {
        return (map==null||map.isEmpty());
    }

    /**
     * Comprueba que el array pasado no es nulo ni est� vac�o.
     * @param object El Object[] a comprobar (String[], int[]...).
     * @return boolean True si es nulo o vac�o, False en otro caso.
     */
    public static boolean isEmpty(Object[] object) {
        return (object==null||object.length==0);
    }

    /**
     * Comprueba que el Objeto pasado no es nulo ni est� vac�o.
     * Estar vac�o (no tener elmentos o valor) funciona para Collections, Maps o Strings.
     * Para los dem�s objetos devuelve el resultado de la condici�n object == null.
     * @param object El objeto a comprobar.
     * @return boolean True si es nulo o vac�o, False en otro caso.
     * @throws IllegalArgumentException si el argumento pasado es de tipo err�neo.
     */
    public static boolean isEmpty(Object object) {
        if (object==null)
            return true;
        else if (object instanceof Collection)
            return isEmpty((Collection)object);
        else if (object instanceof Map)
            return isEmpty((Map)object);
        else if (object instanceof String)
            return isEmpty((String)object);
        else if (object instanceof String[])
            return isEmpty((String[])object);
        else
            return false;
        /*throw new IllegalArgumentException("No puedo inspeccionar el valor de " +
                                               object +
                                               " para comprobar si est� vac�o. Tiene que ser un String, un BigDecimal, un Map o un Collection.");
            */
    }

    /**
     * Devuelve un String con una fecha formateada estilo "dd/MM/yyyy".
     * @param requestDate Un objeto Date a formatear.
     * @return Date La fecha formateada.
     */
    public static String datePrintfddMMyyyy(Date requestDate) {
        return datePrintf(requestDate, "dd/MM/yyyy");
    }
    /**
     * Devuelve un String con una fecha formateada.
     * @param requestDate Un objeto Date a formatear.
     * @param finalFormat El patr�n o formato a aplicar, ex. "dd/MM/yyyy".
     * @return Date La fecha formateada.
     */
    public static String datePrintf(Date requestDate, String finalFormat) {
        SimpleDateFormat sdf=new SimpleDateFormat(finalFormat);
        return sdf.format(requestDate);
    }
    
    /**
     * Pasa un string a un BigDecimal.
     * @param value String que queremos convertir
     * @return El valor de BigDecimal
     */
    public static BigDecimal stringToBigDecimal(String value) {
        return stringToBigDecimal(value, null);
    }

    /**
     * Pasa un string a un BigDecimal. Si no hay valor, le pasamos un valor alternativo
     * @param value String que queremos convertir
     * @param valueAlt String alternativo->Puede ser null o un valor num�rico
     * @return El valor de BigDecimal
     */
    public static BigDecimal stringToBigDecimal(String value, String valueAlt) {
        BigDecimal realValue=null;
        BigDecimal emptyValue=null;

        try {
            if (value!=null)
                realValue=new BigDecimal(value);
            if (valueAlt!=null)
                emptyValue=new BigDecimal(valueAlt);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("AxisBaseService->stringToBigDecimal: El valor que quieres pasar como BigDecimal no puede convertirse como tal");
        }
        finally {
            return ((isEmpty(realValue))?emptyValue:realValue);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

      
}
