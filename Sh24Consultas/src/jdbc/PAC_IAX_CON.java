package jdbc; //WLS-Ready

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.csi_ti.itaca.custom.general.server.jdbc.AccesoPL;

public class PAC_IAX_CON extends AccesoPL {
    static Log logger = LogFactory.getLog(PAC_IAX_CON.class);
    private Connection conn = null;

    public PAC_IAX_CON(Connection conn) {
        this.conn = conn;
    }
    
     //--START-PAC_IAX_CON.F_CERRAR_PUESTO(PUSUARIO)
             private HashMap callPAC_IAX_CON__F_CERRAR_PUESTO (String pPUSUARIO) throws Exception {
                     String callQuery = "{?=call PAC_IAX_CON.F_CERRAR_PUESTO(?,?,?)}";
                     logCall (callQuery, new String[] {"pPUSUARIO"}, new Object[] {pPUSUARIO});
                     CallableStatement cStmt = conn.prepareCall(callQuery);
                     String USERNAME = conn.getMetaData().getUserName().toUpperCase();
                     cStmt.setObject (2, pPUSUARIO);
                     cStmt.registerOutParameter (1, java.sql.Types.NUMERIC); // Valor de "RETURN"
                     cStmt.registerOutParameter (3, java.sql.Types.NUMERIC); // Valor de "PSINTERF"
                     cStmt.registerOutParameter (4, oracle.jdbc.OracleTypes.ARRAY, UsuarioBean.fixOwner(USERNAME, "T_IAX_MENSAJES".toUpperCase())); // Valor de "MENSAJES"
                     cStmt.execute ();
                     HashMap retVal = new HashMap ();
                     try{
                             retVal.put ("RETURN", cStmt.getObject (1));
                     }catch (SQLException e){
                             retVal.put ("RETURN", null);
                     }
                     try{
                             retVal.put ("PSINTERF", cStmt.getObject (3));
                     }catch (SQLException e){
                             retVal.put ("PSINTERF", null);
                     }
                     try{
                             retVal.put ("MENSAJES", cStmt.getObject (4));
                     }catch (SQLException e){
                             retVal.put ("MENSAJES", null);
                     }
                     retVal=new ConversionUtil().convertOracleObjects(retVal);//AXIS-WLS1SERVER-Ready
            cStmt.close();//AXIS-WLS1SERVER-Ready
                     return retVal;
             }

             public HashMap ejecutaPAC_IAX_CON__F_CERRAR_PUESTO (String pPUSUARIO) throws Exception {
                     return this.callPAC_IAX_CON__F_CERRAR_PUESTO(pPUSUARIO); 
             }
             //--END-PAC_IAX_CON.F_CERRAR_PUESTO

              //--START-PAC_IAX_CON.F_IMPORTE_FINANCIACION_PDTE(PSSEGURO)
              private HashMap callPAC_IAX_CON__F_IMPORTE_FINANCIACION_PDTE (java.math.BigDecimal pPSSEGURO) throws Exception {
                  String callQuery="{?=call PAC_IAX_CON.F_IMPORTE_FINANCIACION_PDTE(?, ?, ?)}";
                  
                  logCall(callQuery, new String[] {"pPSSEGURO"}, new Object[] {pPSSEGURO});
                  CallableStatement cStmt=conn.prepareCall(callQuery);
                  String USERNAME=conn.getMetaData().getUserName().toUpperCase();
                  cStmt.setObject(2, pPSSEGURO);
                  cStmt.registerOutParameter(1, java.sql.Types.NUMERIC); // Valor de "RETURN"
                  cStmt.registerOutParameter(3, java.sql.Types.NUMERIC); // Valor de "PIMPORTE"
                  cStmt.registerOutParameter(4, oracle.jdbc.OracleTypes.ARRAY, UsuarioBean.fixOwner(USERNAME, "T_IAX_MENSAJES".toUpperCase())); // Valor de "MENSAJES"
                  cStmt.execute();
                  HashMap retVal=new HashMap();
                  try {
                      retVal.put("RETURN", cStmt.getObject(1));
                  }
                  catch (SQLException e) {
                      retVal.put("RETURN", null);
                  }
                  try {
                      retVal.put("PIMPORTE", cStmt.getObject(3));
                  }
                  catch (SQLException e) {
                      retVal.put("PIMPORTE", null);
                  }
                  try {
                      retVal.put("MENSAJES", cStmt.getObject(4));
                  }
                  catch (SQLException e) {
                      retVal.put("MENSAJES", null);
                  }
                  retVal=new ConversionUtil().convertOracleObjects(retVal);//AXIS-WLS1SERVER-Ready
                  cStmt.close();//AXIS-WLS1SERVER-Ready
                  
                  return retVal;
              }

              public HashMap ejecutaPAC_IAX_CON__F_IMPORTE_FINANCIACION_PDTE (java.math.BigDecimal pPSSEGURO) throws Exception {
                  return this.callPAC_IAX_CON__F_IMPORTE_FINANCIACION_PDTE(pPSSEGURO);//AXIS-WLS1SERVER-Ready 
              }
              //--END-PAC_IAX_CON.F_IMPORTE_FINANCIACION_PDTE

                //--START-PAC_IAX_CON.F_ANTIGUEDADCONDUCTOR(PTIPODOC,  PNNUMIDE,  PSINTERF)
                private HashMap callPAC_IAX_CON__F_ANTIGUEDADCONDUCTOR (String pPTIPODOC, String pPNNUMIDE, java.math.BigDecimal pPSINTERF) throws Exception {
                    String callQuery="{?=call PAC_IAX_CON.F_ANTIGUEDADCONDUCTOR(?, ?, ?, ?, ?, ?, ?)}";
                    
                    logCall(callQuery, new String[] {"pPTIPODOC", "pPNNUMIDE", "pPSINTERF"}, new Object[] {pPTIPODOC, pPNNUMIDE, pPSINTERF});
                    CallableStatement cStmt=conn.prepareCall(callQuery);
                    String USERNAME=conn.getMetaData().getUserName().toUpperCase();
                    cStmt.setObject(2, pPTIPODOC);
                    cStmt.setObject(3, pPNNUMIDE);
                    cStmt.setObject(7, pPSINTERF);
                    cStmt.registerOutParameter(1, java.sql.Types.NUMERIC); // Valor de "RETURN"
                    cStmt.registerOutParameter(4, java.sql.Types.NUMERIC); // Valor de "PANTIGUEDAD"
                    cStmt.registerOutParameter(5, java.sql.Types.NUMERIC); // Valor de "PCOMPANI"
                    cStmt.registerOutParameter(6, java.sql.Types.NUMERIC); // Valor de "PSINIES"
                    cStmt.registerOutParameter(7, java.sql.Types.NUMERIC); // Valor de "PSINTERF"
                    cStmt.registerOutParameter(8, oracle.jdbc.OracleTypes.ARRAY, UsuarioBean.fixOwner(USERNAME, "T_IAX_MENSAJES".toUpperCase())); // Valor de "MENSAJES"
                    cStmt.execute();
                    HashMap retVal=new HashMap();
                    try {
                        retVal.put("RETURN", cStmt.getObject(1));
                    }
                    catch (SQLException e) {
                        retVal.put("RETURN", null);
                    }
                    try {
                        retVal.put("PANTIGUEDAD", cStmt.getObject(4));
                    }
                    catch (SQLException e) {
                        retVal.put("PANTIGUEDAD", null);
                    }
                    try {
                        retVal.put("PCOMPANI", cStmt.getObject(5));
                    }
                    catch (SQLException e) {
                        retVal.put("PCOMPANI", null);
                    }
                    try {
                        retVal.put("PSINIES", cStmt.getObject(6));
                    }
                    catch (SQLException e) {
                        retVal.put("PSINIES", null);
                    }
                    try {
                        retVal.put("PSINTERF", cStmt.getObject(7));
                    }
                    catch (SQLException e) {
                        retVal.put("PSINTERF", null);
                    }
                    try {
                        retVal.put("MENSAJES", cStmt.getObject(8));
                    }
                    catch (SQLException e) {
                        retVal.put("MENSAJES", null);
                    }
                    retVal=new ConversionUtil().convertOracleObjects(retVal);//AXIS-WLS1SERVER-Ready
                    cStmt.close();//AXIS-WLS1SERVER-Ready
                    
                    return retVal;
                }

                public HashMap ejecutaPAC_IAX_CON__F_ANTIGUEDADCONDUCTOR (String pPTIPODOC, String pPNNUMIDE, java.math.BigDecimal pPSINTERF) throws Exception {
                    return this.callPAC_IAX_CON__F_ANTIGUEDADCONDUCTOR(pPTIPODOC, pPNNUMIDE, pPSINTERF);//AXIS-WLS1SERVER-Ready 
                }
                //--END-PAC_IAX_CON.F_ANTIGUEDADCONDUCTOR

               //--START-PAC_IAX_CON.F_ULTIMOSEGUROVEHICULO(PTIPOMATRICULA,  PMATRICULA,  PSINTERF)
               private HashMap callPAC_IAX_CON__F_ULTIMOSEGUROVEHICULO (String pPTIPOMATRICULA, String pPMATRICULA, java.math.BigDecimal pPSINTERF) throws Exception {
                   String callQuery="{?=call PAC_IAX_CON.F_ULTIMOSEGUROVEHICULO(?, ?, ?, ?, ?, ?)}";
                   
                   logCall(callQuery, new String[] {"pPTIPOMATRICULA", "pPMATRICULA", "pPSINTERF"}, new Object[] {pPTIPOMATRICULA, pPMATRICULA, pPSINTERF});
                   CallableStatement cStmt=conn.prepareCall(callQuery);
                   String USERNAME=conn.getMetaData().getUserName().toUpperCase();
                   cStmt.setObject(2, pPTIPOMATRICULA);
                   cStmt.setObject(3, pPMATRICULA);
                   cStmt.setObject(6, pPSINTERF);
                   cStmt.registerOutParameter(1, java.sql.Types.NUMERIC); // Valor de "RETURN"
                   cStmt.registerOutParameter(4, oracle.jdbc.OracleTypes.DATE); // Valor de "PFECHAULTIMAVIG"
                   cStmt.registerOutParameter(5, java.sql.Types.NUMERIC); // Valor de "PCOMPANI"
               //    cStmt.registerOutParameter(6, java.sql.Types.NUMERIC); // Valor de "PSINTERF"
                   cStmt.registerOutParameter(7, oracle.jdbc.OracleTypes.ARRAY, UsuarioBean.fixOwner(USERNAME, "T_IAX_MENSAJES".toUpperCase())); // Valor de "MENSAJES"
                   cStmt.execute();
                   HashMap retVal=new HashMap();
                   try {
                       retVal.put("RETURN", cStmt.getObject(1));
                   }
                   catch (SQLException e) {
                       retVal.put("RETURN", null);
                   }
                   try {
                       retVal.put("PFECHAULTIMAVIG", cStmt.getObject(4));
                   }
                   catch (SQLException e) {
                       retVal.put("PFECHAULTIMAVIG", null);
                   }
                   try {
                       retVal.put("PCOMPANI", cStmt.getObject(5));
                   }
                   catch (SQLException e) {
                       retVal.put("PCOMPANI", null);
                   }
                   try {
                       retVal.put("PSINTERF", cStmt.getObject(6));
                   }
                   catch (SQLException e) {
                       retVal.put("PSINTERF", null);
                   }
                   try {
                       retVal.put("MENSAJES", cStmt.getObject(7));
                   }
                   catch (SQLException e) {
                       retVal.put("MENSAJES", null);
                   }
                   retVal=new ConversionUtil().convertOracleObjects(retVal);//AXIS-WLS1SERVER-Ready
                   cStmt.close();//AXIS-WLS1SERVER-Ready
                   
                   return retVal;
               }

               public HashMap ejecutaPAC_IAX_CON__F_ULTIMOSEGUROVEHICULO (String pPTIPOMATRICULA, String pPMATRICULA, java.math.BigDecimal pPSINTERF) throws Exception {
                   return this.callPAC_IAX_CON__F_ULTIMOSEGUROVEHICULO(pPTIPOMATRICULA, pPMATRICULA, pPSINTERF);//AXIS-WLS1SERVER-Ready 
               }
               //--END-PAC_IAX_CON.F_ULTIMOSEGUROVEHICULO


      //--START-PAC_IAX_CON.F_LISTA_CONTRATOS_PAGO(PIPAGO,  PSPERSON,  PNSINIES,  PSSEGURO)
      private HashMap callPAC_IAX_CON__F_LISTA_CONTRATOS_PAGO (java.math.BigDecimal pPIPAGO, java.math.BigDecimal pPSPERSON, String pPNSINIES, java.math.BigDecimal pPSSEGURO) throws Exception {
          String callQuery="{?=call PAC_IAX_CON.F_LISTA_CONTRATOS_PAGO(?, ?, ?, ?, ?, ?, ?)}";
          
          logCall(callQuery, new String[] {"pPIPAGO", "pPSPERSON", "pPNSINIES", "pPSSEGURO"}, new Object[] {pPIPAGO, pPSPERSON, pPNSINIES, pPSSEGURO});
          CallableStatement cStmt=conn.prepareCall(callQuery);
          String USERNAME=conn.getMetaData().getUserName().toUpperCase();
          cStmt.setObject(2, pPIPAGO);
          cStmt.setObject(3, pPSPERSON);
          cStmt.setObject(4, pPNSINIES);
          cStmt.setObject(5, pPSSEGURO);
          cStmt.registerOutParameter(1, java.sql.Types.NUMERIC); // Valor de "RETURN"
          cStmt.registerOutParameter(6, java.sql.Types.NUMERIC); // Valor de "PSINTERF"
          cStmt.registerOutParameter(7, oracle.jdbc.OracleTypes.ARRAY, UsuarioBean.fixOwner(USERNAME, "T_IAX_SIN_TRAMI_PAGO_CTR".toUpperCase())); // Valor de "PCURCONTRATOS"
          cStmt.registerOutParameter(8, oracle.jdbc.OracleTypes.ARRAY, UsuarioBean.fixOwner(USERNAME, "T_IAX_MENSAJES".toUpperCase())); // Valor de "MENSAJES"
          cStmt.execute();
          HashMap retVal=new HashMap();
          try {
              retVal.put("RETURN", cStmt.getObject(1));
          }
          catch (SQLException e) {
              retVal.put("RETURN", null);
          }
          try {
              retVal.put("PSINTERF", cStmt.getObject(6));
          }
          catch (SQLException e) {
              retVal.put("PSINTERF", null);
          }
          try {
              retVal.put("PCURCONTRATOS", cStmt.getObject(7));
          }
          catch (SQLException e) {
              retVal.put("PCURCONTRATOS", null);
          }
          try {
              retVal.put("MENSAJES", cStmt.getObject(8));
          }
          catch (SQLException e) {
              retVal.put("MENSAJES", null);
          }
          retVal=new ConversionUtil().convertOracleObjects(retVal);//AXIS-WLS1SERVER-Ready
          cStmt.close();//AXIS-WLS1SERVER-Ready
          
          return retVal;
      }

      public HashMap ejecutaPAC_IAX_CON__F_LISTA_CONTRATOS_PAGO (java.math.BigDecimal pPIPAGO, java.math.BigDecimal pPSPERSON, String pPNSINIES, java.math.BigDecimal pPSSEGURO) throws Exception {
          return this.callPAC_IAX_CON__F_LISTA_CONTRATOS_PAGO(pPIPAGO, pPSPERSON, pPNSINIES, pPSSEGURO);//AXIS-WLS1SERVER-Ready 
      }
      //--END-PAC_IAX_CON.F_LISTA_CONTRATOS_PAGO



}
