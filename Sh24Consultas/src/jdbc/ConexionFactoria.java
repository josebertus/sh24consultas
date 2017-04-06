package jdbc;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.ui.UI;

import lombok.extern.apachecommons.CommonsLog;
import oracle.jdbc.driver.OracleConnection;

@CommonsLog
public class ConexionFactoria {
    public static int CC=0;
    static Log log=LogFactory.getLog(ConexionFactoria.class);

    private static HashMap datasources;


    private static Connection conn;

    public static Connection establecerConexion2() {
        try {
        	
	        java.util.Properties props = new java.util.Properties();
	        
	        props.setProperty("password","inicio");
	        props.setProperty("user","AMA_ADMON");
	        props.setProperty("program","[[[ PWP ]]]");
	        String URL=UI.getCurrent().getSession().getAttribute("url").toString();
	        System.out.println(" URl de conextionfactoria: " + URL );
	        props.setProperty(OracleConnection.CONNECTION_PROPERTY_INSTANCE_NAME, "instancia**");
	        props.setProperty(
	        	       OracleConnection.CONNECTION_PROPERTY_THIN_VSESSION_PROGRAM,
	        	       "[[[[ PWP ]]]]" );
	        try {
				DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}     
	        if (conn==null) {
	        	System.out.println("La conexión es null ");
	        	DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
	        	conn = DriverManager.getConnection(URL, props);
	        }
			
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return conn;
        
    }
    public static Connection getConnection(String dbxx, String user, String password) throws Exception {
    	System.out.println(">>>>> Con 4");
        //log.debug("acceso a db01 para user:"+user+" driver:"+ConfigCache.getConfig().getProperty(dbxx+".driver")+" url:"+ConfigCache.getConfig().getProperty(dbxx+".url"));
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("password",password);
        props.setProperty("user",user);
        props.put("v$session.osuser", System.getProperty("user.name").toString());
        props.put("v$session.machine", InetAddress.getLocalHost().getCanonicalHostName());
        props.put("v$session.program", "PGE");
        
        
        Class.forName(ConfigCache.getConfig().getProperty(dbxx+".driver"));
        Connection con=DriverManager.getConnection(ConfigCache.getConfig().getProperty(dbxx+".url"), props);
        con.setAutoCommit(false);
        return con;
    }

    public static Connection getConnection(String dbxx) throws Exception {
    	System.out.println(">>>>> Con 5");
        if (datasources==null)
            datasources=new HashMap();

        if (datasources.get(dbxx)==null) {
            ConexionFactoria.setupDatasource(dbxx);
        }

        System.out.println(">>>>> Con 7");
        Connection con=((DataSource)datasources.get(dbxx)).getConnection();

        //Checkear conexi�n estado Datasourde - Evitar bucles LCF 9/12/2009
        checkConnection(con, dbxx);
        con.setAutoCommit(false);
        return con;
    }

    public static DataSource getDataSource(String dbxx) throws Exception {
        if (datasources==null)
            datasources=new HashMap();

        if (datasources.get(dbxx)==null) {
            ConexionFactoria.setupDatasource(dbxx);
        }

        return (DataSource)datasources.get(dbxx);
    }

    public static DataSource getDataSource() throws Exception {
        return getDataSource("db01");
    }

    public static void setupDatasource(String dbxx) throws Exception {
        if (datasources==null)
            datasources=new HashMap();

        Context ctx=new InitialContext();
        DataSource ds=(DataSource)datasources.get(dbxx);
        if (ds==null) {
            String[] dbxxJndiNames=ConfigCache.getConfig().getProperty("datasource."+dbxx).split(",");
            if (dbxxJndiNames.length==0)
                throw new Exception("No se ha encontrado jndiNames para:"+dbxx+".jndi");
            for (int i=0; i<dbxxJndiNames.length; i++) {
                //log.info("probando con:"+dbxxJndiNames[i].trim());
                try {
                    ds=(DataSource)ctx.lookup(dbxxJndiNames[i].trim());
                    //log.info("jndiDS encontrado con:"+dbxxJndiNames[i].trim());
                    datasources.put(dbxx, ds);
                    //log.info("ds por jndi OK");
                    break;
                }
                catch (Exception e) {
                    log.error("No se ha podido encontrar un DS con ["+dbxxJndiNames[i].trim()+"]. Probando alternativas...", e);
                }
            }
            if (ds==null)
                throw new Exception("No se ha podido establecer un datasource para:"+dbxx);
        }

    }


    public static void restartDatasource(String dbxx) throws Exception {

        javax.sql.DataSource ds=null;
        java.sql.Connection con=null;

        try {

            String[] dbxxJndiNames=ConfigCache.getConfig().getProperty("datasource."+dbxx).split(",");
            if (dbxxJndiNames.length==0)
                throw new Exception("No se ha encontrado jndiNames para:"+dbxx+".jndi");

            for (int i=0; i<dbxxJndiNames.length; i++) {

                javax.naming.InitialContext ctx=new javax.naming.InitialContext();
                ds=(DataSource)ctx.lookup(dbxxJndiNames[i].trim());

                System.out.println(">>>>> Con 6");
                con=ds.getConnection();
                con.close();
            }

            setupDatasource(dbxx);

        }
        catch (Exception e) {
            log.error("Error en restartDataSource data source"+e.toString());
        }


    }


    /**
     * Cierra de forma silenciosa la conexion "conn" a la BBDD (en un caso de un pool - lo retorna al gestor). Pero antes intenta ejecutar scripts de limpieza de temporales
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn!=null) {
            //log.info("intento cerrar la conexion, lanzando los scripts de limpieza de temporales");
            try {
                //new PAC_IAX_SUPLEMENTOS(conn).ejecutaPAC_IAX_SUPLEMENTOS__LIMPIARTEMPORALES();
            }
            catch (Exception e) {
                log.error("error lanzando un script de limpieza de temporales de suplementos", e);
            }
            try {
                //new PAC_IAX_PRODUCCION(conn).ejecutaPAC_IAX_PRODUCCION__LIMPIARTEMPORALES();
            }
            catch (Exception e) {
                log.error("error lanzando un script de limpieza de temporales de produccion", e);
            }
            try {
                //new PAC_IAX_SIMULACIONES(conn).ejecutaPAC_IAX_SIMULACIONES__LIMPIARTEMPORALES();
            }
            catch (Exception e) {
                log.error("error lanzando un script de limpieza de temporales de simulaciones", e);
            }
        }

        try {
            conn.clearWarnings();
        }
        catch (Exception e) {
        }
        try {
            conn.rollback();
        }
        catch (Exception e) {
        }
        try {
            conn.close();
        }
        catch (Exception e) {
        }
        //log.debug("Conexion "+conn+" nulled/cerrada");
    }


    public static void checkConnection(Connection conn, String dbxx) throws Exception {
        try {
            //AccesoSQL accesoSQL=new AccesoSQL(conn);
            String sql="select sysdate from dual";

            PreparedStatement pstmt=conn.prepareStatement(sql);
            ResultSet rs=pstmt.executeQuery();

            //ecg20130124 http://mantis.srvcsi.com/view.php?id=25799#c135722 faltaba cerrar rs y stmt
            rs.close();
            pstmt.close();


        }
        catch (Exception e) {
            if (e.toString().indexOf("No hay mas datos")>=0) {
                log.error("Error en checkConnection:"+e.toString());
                restartDatasource(dbxx);
            }
            else if (e.toString().indexOf("OALL8")>=0) {
                log.error("Error en checkConnection:"+e.toString());
                restartDatasource(dbxx);
            }
            else if (e.toString().indexOf("socket write error")>=0) {
                log.error("Error en checkConnection:"+e.toString());
                restartDatasource(dbxx);
            }

        }

    }


}
