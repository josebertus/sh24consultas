package jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.csi_ti.itaca.custom.general.server.jdbc.AccesoPL;


//WLS-Ready


public class PAC_PERSISTENCIA extends AccesoPL { //AXIS-WLS2CLUSTER-Ready
    static Log logger=LogFactory.getLog(PAC_PERSISTENCIA.class);
    private Connection conn=null;

    public PAC_PERSISTENCIA(Connection conn) {
        this.conn=conn;
    }

    //--START-PAC_PERSISTENCIA.F_GUARDAR_CONTEXTO(PIDSESSION)

    private HashMap callPAC_PERSISTENCIA__F_GUARDAR_CONTEXTO(String pPIDSESSION) throws Exception {
        String callQuery="{?=call PAC_PERSISTENCIA.F_GUARDAR_CONTEXTO(?)}";

        logCall(callQuery, new String[] { "pPIDSESSION" }, new Object[] { pPIDSESSION });
        CallableStatement cStmt=conn.prepareCall(callQuery);
        String USERNAME=conn.getMetaData().getUserName().toUpperCase();
        cStmt.setObject(2, pPIDSESSION);
        cStmt.registerOutParameter(1, java.sql.Types.NUMERIC); // Valor de "RETURN"
        cStmt.execute();
        HashMap retVal=new HashMap();
        try {
            retVal.put("RETURN", cStmt.getObject(1));
        }
        catch (SQLException e) {
            retVal.put("RETURN", null);
        }
        retVal=new ConversionUtil().convertOracleObjects(retVal); //AXIS-WLS1SERVER-Ready
        cStmt.close(); //AXIS-WLS1SERVER-Ready

        return retVal;
    }

    public HashMap ejecutaPAC_PERSISTENCIA__F_GUARDAR_CONTEXTO(String pPIDSESSION) throws Exception {
        return this.callPAC_PERSISTENCIA__F_GUARDAR_CONTEXTO(pPIDSESSION); //AXIS-WLS1SERVER-Ready 
    }
    //--END-PAC_PERSISTENCIA.F_GUARDAR_CONTEXTO
    //--START-PAC_PERSISTENCIA.F_INICIALIZAR_CONTEXTO(PIDSESSION)

    private HashMap callPAC_PERSISTENCIA__F_INICIALIZAR_CONTEXTO(String pPIDSESSION) throws Exception {
        String callQuery="{?=call PAC_PERSISTENCIA.F_INICIALIZAR_CONTEXTO(?)}";

        logCall(callQuery, new String[] { "pPIDSESSION" }, new Object[] { pPIDSESSION });
        CallableStatement cStmt=conn.prepareCall(callQuery);
        String USERNAME=conn.getMetaData().getUserName().toUpperCase();
        cStmt.setObject(2, pPIDSESSION);
        cStmt.registerOutParameter(1, java.sql.Types.NUMERIC); // Valor de "RETURN"
        cStmt.execute();
        HashMap retVal=new HashMap();
        try {
            retVal.put("RETURN", cStmt.getObject(1));
        }
        catch (SQLException e) {
            retVal.put("RETURN", null);
        }
        retVal=new ConversionUtil().convertOracleObjects(retVal); //AXIS-WLS1SERVER-Ready
        cStmt.close(); //AXIS-WLS1SERVER-Ready

        return retVal;
    }

    public HashMap ejecutaPAC_PERSISTENCIA__F_INICIALIZAR_CONTEXTO(String pPIDSESSION) throws Exception {
        return this.callPAC_PERSISTENCIA__F_INICIALIZAR_CONTEXTO(pPIDSESSION); //AXIS-WLS1SERVER-Ready 
    }
    //--END-PAC_PERSISTENCIA.F_INICIALIZAR_CONTEXTO

    //--START-PAC_PERSISTENCIA.F_LIMPIAR_CONTEXTO(PIDSESSION)

    private HashMap callPAC_PERSISTENCIA__F_LIMPIAR_CONTEXTO(String pPIDSESSION) throws Exception {
        String callQuery="{?=call PAC_PERSISTENCIA.F_LIMPIAR_CONTEXTO(?)}";

        logCall(callQuery, new String[] { "pPIDSESSION" }, new Object[] { pPIDSESSION });
        CallableStatement cStmt=conn.prepareCall(callQuery);
        String USERNAME=conn.getMetaData().getUserName().toUpperCase();
        cStmt.setObject(2, pPIDSESSION);
        cStmt.registerOutParameter(1, java.sql.Types.NUMERIC); // Valor de "RETURN"
        cStmt.execute();
        HashMap retVal=new HashMap();
        try {
            retVal.put("RETURN", cStmt.getObject(1));
        }
        catch (SQLException e) {
            retVal.put("RETURN", null);
        }
        retVal=new ConversionUtil().convertOracleObjects(retVal); //AXIS-WLS1SERVER-Ready
        cStmt.close(); //AXIS-WLS1SERVER-Ready

        return retVal;
    }

    public HashMap ejecutaPAC_PERSISTENCIA__F_LIMPIAR_CONTEXTO(String pPIDSESSION) throws Exception {
        return this.callPAC_PERSISTENCIA__F_LIMPIAR_CONTEXTO(pPIDSESSION); //AXIS-WLS1SERVER-Ready 
    }
    //--END-PAC_PERSISTENCIA.F_LIMPIAR_CONTEXTO

}
