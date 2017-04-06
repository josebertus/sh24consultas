package jdbc;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
	
	 public static Object tratarRETURNyMENSAJES(Map map) {
       
        if (map==null)
            throw new IllegalArgumentException("El argumento map no puede ser nulo.");

        if (!isEmpty(map.get("MENSAJES")))
            parsearMensajes((List)map.get("MENSAJES"));
       
        if (!isEmpty(map.get("RETURN")))
            return map.get("RETURN");

        return null;
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
       
    }
    
    public static void parsearMensajes(List MENSAJES) {
    	
        if (MENSAJES==null || MENSAJES.size()==0) return;

        for (int i=0;i<MENSAJES.size();i++) {
            HashMap mensaje=(HashMap)MENSAJES.get(i);
            HashMap OB_IAX_MENSAJES=(HashMap)mensaje.get("OB_IAX_MENSAJES");
            BigDecimal TIPERROR=(BigDecimal)OB_IAX_MENSAJES.get("TIPERROR");
            BigDecimal CERROR=(BigDecimal)OB_IAX_MENSAJES.get("CERROR");
            String TERROR=(String)OB_IAX_MENSAJES.get("TERROR");
            
            int mensajeTipo=Constantes.MENSAJE_INFO;
            switch(TIPERROR.intValue()) {
                case 1: mensajeTipo=Constantes.MENSAJE_ERROR; break;
                case 2: mensajeTipo=Constantes.MENSAJE_INFO; break;
            }
        }
    }

}
