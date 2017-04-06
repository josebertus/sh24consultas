package jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import lombok.extern.apachecommons.CommonsLog;

// import axis.util.Constantes;


@CommonsLog
public class ConfigCache  {
    private static final long defaultMAXCACHEDTIMEINMILLIS = 1000 * 60 * 60;
    private static long MAXCACHEDTIMEINMILLIS = defaultMAXCACHEDTIMEINMILLIS;
    public static final String MINOMBRE = "ConfigCache";
    private static ConfigCache yo;

    static {
        yo = new ConfigCache();
    }

    private ConfigCache() {
        try {
            reload();
        } catch (Exception e) {
            //log.error("Inicializacion de " + MINOMBRE + ".", e);
        }
    }


    public static ConfigCache getInstance() {
        return yo;
    }


    private static HashMap data;
    private static Date lastUpdate;

    public static boolean reload() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
        else {
            data.clear();
        }
        Properties configProperties=new Properties();
        configProperties.load( ConfigCache.class.getResourceAsStream("config.properties" ));
        data.put("configProperties", configProperties);
        Constantes.DB01_POOLOWNER= configProperties.getProperty("db01.poolowner","").trim().toUpperCase();
        lastUpdate=new Date();
        return true;
    }


    public static HashMap getData() throws Exception {
        if (lastUpdate == null || 
            (lastUpdate.getTime() + ConfigCache.MAXCACHEDTIMEINMILLIS) < 
            (new Date()).getTime()) {
            reload();
        }
        return data;
    }

    public static void setMAXCACHEDTIMEINMILLIS(long p) {
        MAXCACHEDTIMEINMILLIS=p;
        if (MAXCACHEDTIMEINMILLIS<0) MAXCACHEDTIMEINMILLIS=defaultMAXCACHEDTIMEINMILLIS;
    }
    
    public static Properties getConfig() {
        return (Properties)data.get("configProperties");
    }

    public static void main(String[] args) throws Exception {
        //log.debug(ConfigCache.getConfig());
        Properties configcache=ConfigCache.getConfig();
        Object[] keySetArray=configcache.keySet().toArray();
        ArrayList locales=new ArrayList();
        for (int i=0;i<keySetArray.length;i++) {
            String tempKey=(String)keySetArray[i];
            if (tempKey.startsWith("locale2CIDIOMA.")) {
                locales.add(tempKey.substring(15));
            }
         }
        Collections.sort(locales);
        //log.debug(locales);        
    }
    
}
