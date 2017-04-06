package jdbc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class Constantes {
    public static final String DB01CONN="__db01conn"; //AXIS-WLS2CLUSTER-Ready

    public static final String LITERALES_BBDD="db";
    public static final String LITERALES_PROP="properties";
    public static final String AUTOLOGINUSUARIO="__autologinusuario";
    public static final String CONFIGFORM="__configform";
    public static final String FORMDATA="__formdata";
    public static final String USUARIO="__usuario";
    public static final String MENSAJES="__mensajes";
    public static final BigDecimal ANTERIOR=new BigDecimal(1);
    public static final BigDecimal SIGUIENTE=new BigDecimal(2);
    public static final String FLUJODETRABAJO="__flujodetrabajo";
    public static final String MENU="__menu";
    public static final String LOCALE="__locale";
    public static final String LOCALENUMERICO="__locale_formato_numerico";
    public static final String LOCALE_SEPARADOR_DECIMALES="__locale_formato_separadorDecimales";
    public static final String LOCALE_SEPARADOR_MILES="__locale_formato_separadorMiles";
    // public static final String IDIOMA = "__idioma"; pregutar por el CIDIOMA en el UsuarioBean de la session
    public static final String CALLBACK="__callback";
    public static final String AJAXCONTAINER="__ajaxcontainer";
    public static final String AJAXPCONTAINER="__ajaxpcontainer";
    public static final String AJAXPCONTENTYPE="__ajaxpcontenttype";
    public static final String OPERACION_AJAX_OK="OPERACION_AJAX_OK";
    public static final String OPERACION_AJAX_ERROR="OPERACION_AJAX_ERROR";
    public static final String FORWARDACTION="__forwardaction";
    public static final String SALTARDESTINOACTION="__saltardestinoaction";
    public static final String SALTARORIGENACTION="__saltarorigenaction";
    public static final String PASSTOPPILA="__passtoppila";
    public static final String PASSFORMDATA="__passformdata";
    public static final int MENSAJE_INFO=1001;
    public static final int MENSAJE_ERROR=2001;
    public static final int MENSAJE_FATAL=3001;
    public static final int MENSAJE_WARN=4001;
    public static final int num_max_filas=100;

    public static String REALPATH=null;
    public static String DB01_POOLOWNER=null;

    public static final int CPRPTY_VISIBLE=1;
    public static final int CPRPTY_VISIBLE_SI=1;
    public static final int CPRPTY_VISIBLE_NO=0;

    public static final int CPRPTY_MODIFICABLE=2;
    public static final int CPRPTY_MODIFICABLE_SI=1;
    public static final int CPRPTY_MODIFICABLE_NO=0;

    public static final int CPRPTY_OBLIGATORIO=3;
    public static final int CPRPTY_OBLIGATORIO_SI=1;
    public static final int CPRPTY_OBLIGATORIO_NO=0;

    public static final int CPRPTY_DEPENDENCIA=4;

    public static final int CPRPTY_TIPODELCAMPO=6;
    public static final int CPRPTY_TIPODELCAMPO_NUMERICO=1;
    public static final int CPRPTY_TIPODELCAMPO_ALFANUMERICO=2;
    public static final int CPRPTY_TIPODELCAMPO_FECHA=3;
    public static final int CPRPTY_TIPODELCAMPO_LISTA=4;

    public static final int CPRPTY_VALORDEFECTO=7;
    public static final int CPRPTY_LITERALALTERNATIVO=8;
    public static final String CPRPTY_MENSAJE_INI="MENSAJE_INI";
    public static final int CPRPTY_EXPANDER=9;
    public static final int CPRPTY_EXPANDER_SI=1;
    public static final int CPRPTY_EXPANDER_NO=0;


    public static final int CTIPGRU_QUESTIONARI_SALUT=1;
    public static final int CTIPGRU_DESPESES=2;

    // Bug 19490
    public static final int CPRPTY_MASKING=11;
    public static final int CPRPTY_ENCRYPT=12;

    public static final UsuarioBean axisUsuario=new UsuarioBean("__axisUsuario__", null);

    //Bug 21868
    public static final String __jSONObject="__jSONObject";
    public static final String __jsoncallback="__jsoncallback";
    public static final String __solojson="__solojson";

    // Bug 21762
    public static final String CONNECTED="Connected";

    //Bug 0025803: RSA001 - Ampliar los decimales que utiliza iAXIS
    public static final Map FORMAT_CURRENCY_PATTERNS=new HashMap();
    public static String DEFAULT_CMONINT="EUR";
    public static BigDecimal DEFAULT_CMONEDA=new BigDecimal(1);

}
