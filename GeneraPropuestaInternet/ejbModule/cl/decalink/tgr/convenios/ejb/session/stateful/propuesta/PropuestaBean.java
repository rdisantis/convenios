/**
 * 
 */
package cl.decalink.tgr.convenios.ejb.session.stateful.propuesta;

import java.rmi.RemoteException;
import java.sql.Connection;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import java.util.Collection;
import java.util.Vector;
import java.util.HashMap;
import java.util.Date;
import java.sql.*;
import javax.sql.DataSource;
import oracle.jdbc.*;
import cl.decalink.tgr.convenios.StringExt;
import cl.decalink.tgr.convenios.DateExt;
import condonacion.Condonacion;
import condonacion.CondonacionHome;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="Propuesta"	
 *           description="An EJB named Propuesta"
 *           display-name="Propuesta"
 *           jndi-name="Propuesta"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public class PropuestaBean implements javax.ejb.SessionBean {
	  /** Variable que mantiene el total a pagar de la propuesta
	   *
	   */
	  public Long totalPagar;

	  /** Variable que guarda el contexto del EJB (propio de uns EJB)
	  */
	  private SessionContext sessionContext;

	  /** Variable que guarda la fuente de datos para la conexi�n con la base de
	   *  datos
	   */
	  private transient DataSource dataSource;

	  /** Variable que guarda la conexi�n con la base de
	   *  datos
	   */
	  private Connection connection;
	/** 
	 * <!-- begin-xdoclet-definition --> 
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.create-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {
		totalPagar = new Long(0);
	}

	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public String foo(String param) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {
		 this.closeConnection();

	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
	    this.sessionContext = arg0;
	    try {
	      javax.naming.Context context = new javax.naming.InitialContext();
	      try {
	        dataSource = (DataSource) context.lookup("oracleds");
	      }
	      catch(Exception e) {
	        throw new EJBException("Error buscando la fuente de datos (dataSource): " + e.toString());
	      }
	    }
	    catch(Exception e) {
	      throw new EJBException("Error inicializando el contexto:" + e.toString());
	    }

	}

	/**
	 * 
	 */
	public PropuestaBean() {
		// TODO Auto-generated constructor stub
	}
	
	
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *Entrega datos de la tabla cv_propuestas
	    *  @param Parametros(Hashmap), que contiene el c�digo de la propuesta (CodPropuesta)
	    *  @return Una colecci�n(Collection) , que contienen los resultados de la b�squeda,
	    *  las claves del HashMap son : CodPropuesta,CodTipoPagoCuota,CodTipoPago,
	    *  CodTipoConvenio,DescEtapa,DescTipoPago,DescTipoConvenio,IdPersona,
	    *  IdTesoreria,CodFuncionario,CodPropuestaPadre,FechaGeneracion,NumResolucion,
	    *  FechaResolucion,FechaVigenciaPropuesta,FechaActivacion,PorcCuotaContado,NumCuotas,
	    *  MontoCuotaContado,MontoCuotaFija,MontoCuotaAjuste,SituacionCaducidad,AceptaCompensacion,
	    *  Resuelta,FechaVigencia,Rut,Dv,NombreContribuyente,FechaSolicitud,NumCuotasInicial,
	    *  NumCuotasSolic,NumCuotasOtorgado,PorcCuotaContInicial,PorcCuotaContSolic,
	    *  PorcCuotaContOtorgado,Observaciones
	    */
	   public Collection getDatosPropuesta(HashMap parametros)
	  {
	     Connection conn = null;
	     Vector retorno = new Vector();
	     int i=0;

	     try {
	      // conn = dataSource.getConnection();
	      conn = this.getConnection();

	      CallableStatement  call = conn.prepareCall("{? = call DATOSPROPUESTA.fCurDatosPropuesta(?)}");

	      call.registerOutParameter(++i, oracle.jdbc.OracleTypes.CURSOR);

	      Long CodPropuesta = (Long) parametros.get("CodPropuesta");
	      call.setLong(++i, CodPropuesta.longValue());


	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      while(rs.next())
	      {

	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("CodPropuesta",(Long) new Long(rs.getLong(1)));/* c�digo propuesta */
	         sqlRetorno.put("CodEtapaConvenio",(Long) new Long(rs.getLong(2)));/* c�digo etapa convenio */
	         sqlRetorno.put("CodTipoPago",(Long) new Long(rs.getLong(3)));/* c�digo tipo pago */
	         sqlRetorno.put("CodTipoConvenio",(Long)  new Long(rs.getLong(4)));/* c�digo tipo convenio */
	         sqlRetorno.put("DescEtapa",(String) rs.getString(5));/* descripci�n etapa convenio */
	         sqlRetorno.put("DescTipoPago",(String) rs.getString(6));/* descripci�n tipo pago */
	         sqlRetorno.put("DescTipoConvenio",(String) rs.getString(7));/* descripci�n tipo convenio */
	         sqlRetorno.put("IdPersona",(Long)  new Long(rs.getLong(8)));/* id persona */
	         sqlRetorno.put("IdTesoreria",(Long)  new Long(rs.getLong(9)));/* id tesorer�a*/
	         sqlRetorno.put("CodFuncionario",(Long)  new Long(rs.getLong(10)));/* c�digo funcionario */
	         sqlRetorno.put("IdReprogOrigen",(Long)  new Long(rs.getLong(11)));/* Id Reparo Origen  */
	         sqlRetorno.put("FechaGeneracion",(String) rs.getString(12));/* fecha generaci�n  propuesta */
	         sqlRetorno.put("NumResolucion",(Long)  new Long(rs.getLong(13)));/* numero resoluci�n */
	         sqlRetorno.put("FechaResolucion",(String) rs.getString(14));/* fecha resoluci�n */
	         sqlRetorno.put("FechaVigenciaPropuesta",(String) rs.getString(15));/* fecha vigencia propuesta formante */
	         sqlRetorno.put("FechaActivacion",(String) rs.getString(16));/* fecha activaci�n propuesta*/
	         sqlRetorno.put("PorcCuotaContado",(Long)  new Long(rs.getLong(17)));/* porcentaje cuota contado */
	         sqlRetorno.put("NumCuotas",(Long)  new Long(rs.getLong(18)));/* numero de cuotas */
	         sqlRetorno.put("MontoCuotaContado",(Long)  new Long(rs.getLong(19)));/* monto cuota contado */
	         sqlRetorno.put("MontoCuotaFija",(Long)  new Long(rs.getLong(20)));/* monto cuota fija */
	         sqlRetorno.put("MontoCuotaAjuste",(Long)  new Long(rs.getLong(21)));/* monto cuota ajuste */
	         sqlRetorno.put("SituacionCaducidad",(String) rs.getString(22));/* situaci�n caducidad */
	         sqlRetorno.put("AceptaCompensacion",(String) rs.getString(23));/* acepta compensacion */
	         sqlRetorno.put("Resuelta",(String) rs.getString(24));/* resuelta */
	         sqlRetorno.put("FechaVigencia",(java.sql.Date) rs.getDate(25));/* fecha vigencia propuesta */
	         sqlRetorno.put("Rut",(Long) new Long(rs.getLong(26)));/* rut contribuyente */
	         sqlRetorno.put("Dv",(String) rs.getString(27));/* d�gito contribuyente */
	         sqlRetorno.put("NombreContribuyente",(String) rs.getString(28));/* nombre contribuyente */
	         sqlRetorno.put("FechaSolicitud",(String) rs.getString(29));/* fecha solicitud */
	         sqlRetorno.put("NumCuotasInicial",(Long) new Long(rs.getLong(30)));/* numero cuotas inicial cv_solicitudes */
	         sqlRetorno.put("NumCuotasSolic",(Long) new Long(rs.getLong(31)));/* numero cuotas solicitado cv_solicitudes */
	         sqlRetorno.put("NumCuotasOtorgado",(Long) new Long(rs.getLong(32)));/* numero cuotas otorgado cv_solicitudes */
	         sqlRetorno.put("PorcCuotaContInicial",(Long) new Long(rs.getLong(33)));/* porcentaje cuota contado inicial cv_solicitudes */
	         sqlRetorno.put("PorcCuotaContSolic",(Long) new Long(rs.getLong(34)));/* porcentaje cuota contado solicitado cv_solicitudes */
	         sqlRetorno.put("PorcCuotaContOtorgado",(Long) new Long(rs.getLong(35)));/* porcentaje cuota contado otorgado cv_solicitudes */
	         sqlRetorno.put("Observaciones",(String) rs.getString(36));/* observaciones */
	         sqlRetorno.put("IdReprogDestino",(Long)  new Long(rs.getLong(37)));/* Id Reprog Destino  */

	         /*String DescTipoConvenio = (String) new String(rs.getString(7));
	         String DescTipoDeuda = (String) new String(rs.getString(38));

	           System.out.println("DescTipoConvenio ::" + DescTipoConvenio);
	             System.out.println("DescTipoDeuda ::" + DescTipoDeuda);
	         if ((DescTipoConvenio.equals("RMH")&& DescTipoDeuda.equals("F"))||(DescTipoConvenio.equals("ART.192")))
	              {

	                  DescTipoConvenio = "DEUDAS FISCALES";

	        }
	              else
	              {
	                  if ((DescTipoConvenio.equals("RMH"))&&(DescTipoDeuda.equals("T")))
	                  {
	                      DescTipoConvenio = "DEUDAS TERRITORIALES";

	                  }

	               }
	*/
	  //       sqlRetorno.put("DescTipoConvenio",(String) DescTipoConvenio);/* descripci�n tipo convenio */
	         retorno.addElement(sqlRetorno);
	      }

	      rs.close();
	      call.close();

	      return retorno;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	    finally{
	      this.closeConnection();
	    }
	  }


	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 *Entrega de la base de datos las cuotas de la propuesta
	    *  @param parametros(Hashmap), que contiene el c�digo de la propuesta (CodPropuesta)
	    *  @return Una colecci�n(Collection) , que contienen los resultados de la b�squeda,
	    *  las claves del HashMap son : CodCuota,CodTipoPagoCuota,CodPropuesta,
	    *  NumCuota,Pagada,FechaPago,AnuladoCheque_pr,DescPagoCuotas
	    */
	    public Collection getCuotasPropuesta(HashMap parametros)
	     {
	     PreparedStatement ps = null;
	     Connection conn = null;
	     Vector retorno = new Vector();
	     int i=0;

	     try {
	      // conn = dataSource.getConnection();
	      conn = this.getConnection();

	      CallableStatement  call = conn.prepareCall("{? = call DATOSPROPUESTA.fCurCuotasPropuesta(?)}");

	      call.registerOutParameter(++i, oracle.jdbc.OracleTypes.CURSOR);

	      Long CodPropuesta = (Long) parametros.get("CodPropuesta");

	      call.setLong(++i, CodPropuesta.longValue());

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      while(rs.next())
	      {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("CodCuota",(Long) new Long(rs.getLong(1))); /* c�digo cuota */
	         sqlRetorno.put("CodTipoPagoCuota",(Long) new Long(rs.getLong(2)));/* c�digo tipo pago cuotas */
	         sqlRetorno.put("CodPropuesta",(Long) new Long(rs.getLong(3)));/* c�digo propuesta */
	         sqlRetorno.put("NumCuota",(Long)  new Long(rs.getLong(4)));/* numero de cuota */
	         sqlRetorno.put("Monto",(String) rs.getString(5));/*monto cuota*/
	         sqlRetorno.put("FechaVenc",(String) rs.getString(6));/* fecha vencimiento de la cuota */
	         sqlRetorno.put("Pagada",(String) rs.getString(7));/* pagada */
	         sqlRetorno.put("FechaPago",(String) rs.getString(8));/* fecha pago de cuota */
	         sqlRetorno.put("AnuladoChequePr",(String) rs.getString(9));/* anulado cheque protestado */
	         sqlRetorno.put("DescPagoCuotas",(String) rs.getString(10));/* descripci�n tipo pago cuotas */

	         retorno.addElement(sqlRetorno);
	      }

	      rs.close();
	      call.close();

	      return retorno;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	    finally{
	      this.closeConnection();
	    }

	  }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Entrega de la base de datos la deudas incluidas en la propuesta
	   *  @param Parametros(HashMap), contiene c�digo de propuesta (CodPropuesta)
	   *  @return Una colecci�n que contienen los resultados de la b�squeda,
	   *  las claves del HashMap son: CodPropuesta, CodDeuda, PorcCondonacion,
	   *  PorcCondSolicitado, PorcCondInicial, MontoNeto,Reajustes,Intereses, Reajustes,TotalPagar,
	   *  FechaVigenciaCond, AplicaCond,DescDeuda,RutRol,TipForm,Folio,FechaVencimiento,Origen
	   */
	  public Collection getDeudasPropuesta(HashMap parametros)
	    {
	     PreparedStatement ps = null;
	     Connection conn = null;
	     Vector retorno = new Vector();
	     int i=0;

	     try {
	      //conn = dataSource.getConnection();
	      conn = this.getConnection();

	      CallableStatement  call = conn.prepareCall("{? = call DATOSPROPUESTA.fCurDeudasPropuesta(?)}");

	      call.registerOutParameter(++i, oracle.jdbc.OracleTypes.CURSOR);

	      Long CodPropuesta = (Long) parametros.get("CodPropuesta");

	      call.setLong(++i, CodPropuesta.longValue());

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      long totalPagar =0;

	      while(rs.next())
	      {

	        totalPagar = totalPagar + rs.getLong(10);

	        HashMap sqlRetorno = new HashMap();


	         sqlRetorno.put("CodPropuesta",(Long) new Long(rs.getLong(1))); /* c�digo propuesta */
	         sqlRetorno.put("CodDeuda",(Long) new Long(rs.getLong(2)));/* c�digo deuda */
	         sqlRetorno.put("PorcCondonacion",(Long) new Long(rs.getLong(3)));/* porcentaje condonaci�n */
	         sqlRetorno.put("PorcCondSolicitado",(Long) new Long(rs.getLong(4)));/* porcentaje condonaci�n solicitado */
	         sqlRetorno.put("PorcCondInicial",(Long)  new Long(rs.getLong(5)));/* porcentaje condonaci�n inicial */
	         sqlRetorno.put("MontoNeto",(Long) new Long(rs.getLong(6)));/* monto neto */
	         sqlRetorno.put("Reajustes",(Long) new Long(rs.getLong(7)));/* reajustes */
	         sqlRetorno.put("Multas",(Long) new Long(rs.getLong(8)));/* multas */
	         sqlRetorno.put("Intereses",(Long) new Long(rs.getLong(9)));/* intereses */
	         sqlRetorno.put("TotalPagar",(Long) new Long(rs.getLong(10)));/* total a pagar */
	         sqlRetorno.put("FechaVigenciaCond",(String) rs.getString(11));/* fecha vigencia condonaci�n  */
	         sqlRetorno.put("AplicaCond",(String) rs.getString(12));/* aplica condonaci�n a */
	         sqlRetorno.put("DescDeuda",(String) rs.getString(13));/* descripci�n deuda */
	         sqlRetorno.put("TipCont",(Long) new Long(rs.getLong(14)));/* descripci�n tipo contribuyente */
	         sqlRetorno.put("RutRol",(Long) new Long(rs.getLong(15)));/* rut rol */
	         sqlRetorno.put("TipForm",(Long) new Long(rs.getLong(16)));/* tipo formulario */
	         sqlRetorno.put("Folio",(Long) new Long(rs.getLong(17)));/* folio */
	         sqlRetorno.put("FechaVencimiento",(String) rs.getString(18));/* fecha vencimiento */
	         sqlRetorno.put("Origen",(String) rs.getString(19));/* origen */
	         sqlRetorno.put("TotalPagarFormateado",(String) rs.getString(20)); /* total pagar formateado */
	         sqlRetorno.put("PorcCondOtorgado",(Long) new Long(rs.getLong(21))); /* porcentaje condonaci�n otorgado */
	         sqlRetorno.put("PorcCondIntereses",(Long) new Long(rs.getLong(22))); /* porcentaje condonaci�n intereses */
	         sqlRetorno.put("PorcCondMultas",(Long) new Long(rs.getLong(23))); /* porcentaje condonaci�n multas */
	         sqlRetorno.put("PorcCondInicialIntereses",(Long) new Long(rs.getLong(24))); /* porcentaje condonaci�n inicial intereses */
	         sqlRetorno.put("PorcCondInicialMultas",(Long) new Long(rs.getLong(25))); /* porcentaje condonaci�n inicial multas */
	         sqlRetorno.put("PorcCondSolicitadoIntereses",(Long) new Long(rs.getLong(26))); /* porcentaje condonaci�n solicitado intereses */
	         sqlRetorno.put("PorcCondSolicitadoMultas",(Long) new Long(rs.getLong(27))); /* porcentaje condonaci�n solicitado multas */
	         sqlRetorno.put("PorcCondOtorgadoIntereses",(Long) new Long(rs.getLong(28))); /* porcentaje condonaci�n otorgado intereses */
	         sqlRetorno.put("PorcCondOtorgadoMultas",(Long) new Long(rs.getLong(29))); /* porcentaje condonaci�n otorgado multas */


	         retorno.addElement(sqlRetorno);
	      }
	      this.totalPagar = new Long(totalPagar);


	      rs.close();
	      call.close();

	      return retorno;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	    finally{
	      this.closeConnection();
	    }

	  }



	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Entrega de la base de datos las propuestas vigentes
	    *  @param Parametros (Hashmap), que contiene id_persona del contribuyente
	    *  @return  Una colecci�n (Collection) que contienen los resultados de la b�squeda,
	    *  las claves del HashMap son: CodPropuesta,NumResolucion,FechaGeneracion,
	    *  FechaVigenciaPropuesta,DescTipoConvenio,DescEtapaConvenio,DescTipoDeuda,TotalPagar,
	    *  CantDeudas,resuelta
	    */
	  public Collection getPropuestasVigentes(HashMap parametros)
	  {
	     PreparedStatement ps = null;
	     Connection conn = null;
	     Vector retorno = new Vector();
	     int i=0;

	     try {
	      //conn = dataSource.getConnection();
	      conn = this.getConnection();

	      CallableStatement  call = conn.prepareCall("{? = call DATOSPROPUESTA.fCurPropuestasVigentes(?,?)}");

	      call.registerOutParameter(++i, oracle.jdbc.OracleTypes.CURSOR);
	      Long IdPersona = (Long) parametros.get("IdPersona");
	      String Estado = (String) parametros.get("Estado");

	      call.setLong(++i, IdPersona.longValue());
	      call.setString(++i, Estado);
	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      while(rs.next())
	      {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("CodPropuesta",(Long) new Long(rs.getLong(1)));/* c�digo propuesta */
	         sqlRetorno.put("NumResolucion",(Long) new Long(rs.getLong(2)));/* numero resoluci�n */
	         sqlRetorno.put("FechaGeneracion",(String) rs.getString(3));/* fecha generaci�n */
	         sqlRetorno.put("FechaVigenciaPropuesta",(String) rs.getString(4));/* fecha vigencia propuesta */
	         sqlRetorno.put("DescTipoConvenio",(String)  rs.getString(5));/* descripci�n tipo convenio */
	         sqlRetorno.put("DescEtapaConvenio",(String)  rs.getString(6));/* descripci�n etapa convenio */
	         sqlRetorno.put("DescTipoDeuda",(String)  rs.getString(7));/* descripci�n tipo deuda */
	         sqlRetorno.put("TotalPagar",(String) rs.getString(8));/* total a pagar */
	         sqlRetorno.put("CantDeudas",(Long) new Long(rs.getLong(9)));/* cantidad de deudas */
	         sqlRetorno.put("CodEtapaConvenio",(Long) new Long(rs.getLong(10)));/* c�digo etapa de convenio */
	         sqlRetorno.put("MontoCuotaContado",(String) rs.getString(11));/* monto cuota contado */
	         sqlRetorno.put("DescTipoPago",(String)  rs.getString(12));/* descripci�n tipo convenio */
	         sqlRetorno.put("idTesoreriaFunc",(Long)  new Long(rs.getLong(13)));/* descripci�n tipo convenio */

	         /*String DescTipoConvenio = (String) new String(rs.getString(5));
	         String DescTipoDeuda = (String) new String(rs.getString(7));

	         System.out.println("DescTipoConvenio ::" + DescTipoConvenio);
	         System.out.println("DescTipoDeuda ::" + DescTipoDeuda);

	         if ((DescTipoConvenio.equals("RMH")&& DescTipoDeuda.equals("FISCAL"))||(DescTipoConvenio.equals("ART.192")))
	              {

	                  DescTipoConvenio = "DEUDAS FISCALES";

	        }
	              else
	              {
	                  if ((DescTipoConvenio.equals("RMH"))&&(DescTipoDeuda.equals("TERRITORIAL")))
	                  {
	                      DescTipoConvenio = "DEUDAS TERRITORIALES";

	                  }

	               }

	         sqlRetorno.put("DescTipoConvenio",(String) DescTipoConvenio);/* descripci�n tipo convenio */



	         retorno.addElement(sqlRetorno);
	      }

	      rs.close();
	      call.close();



	      return retorno;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	    finally{
	      this.closeConnection();
	    }

	  }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 */
	  public Collection getPropuestasVigentesRol(HashMap parametros)
	    {
	       PreparedStatement ps = null;
	       Connection conn = null;
	       Vector retorno = new Vector();
	       int i=0;

	       try {
	        //conn = dataSource.getConnection();
	        conn = this.getConnection();

	        CallableStatement  call = conn.prepareCall("{? = call DATOSPROPUESTA.fCurPropuestasVigentesRol(?,?)}");

	        call.registerOutParameter(++i, oracle.jdbc.OracleTypes.CURSOR);
	        Long Rol = (Long) parametros.get("Rol");
	        String Estado = (String) parametros.get("Estado");

	        call.setLong(++i, Rol.longValue());
	        call.setString(++i, Estado);
	        call.execute();

	        ResultSet rs = (ResultSet) call.getObject(1);

	        while(rs.next())
	        {
	           HashMap sqlRetorno = new HashMap();

	           sqlRetorno.put("CodPropuesta",(Long) new Long(rs.getLong(1)));/* c�digo propuesta */
	           sqlRetorno.put("NumResolucion",(Long) new Long(rs.getLong(2)));/* numero resoluci�n */
	           sqlRetorno.put("FechaGeneracion",(String) rs.getString(3));/* fecha generaci�n */
	           sqlRetorno.put("FechaVigenciaPropuesta",(String) rs.getString(4));/* fecha vigencia propuesta */
	           sqlRetorno.put("DescTipoConvenio",(String)  rs.getString(5));/* descripci�n tipo convenio */
	           sqlRetorno.put("DescEtapaConvenio",(String)  rs.getString(6));/* descripci�n etapa convenio */
	           sqlRetorno.put("DescTipoDeuda",(String)  rs.getString(7));/* descripci�n tipo deuda */
	           sqlRetorno.put("TotalPagar",(String) rs.getString(8));/* total a pagar */
	           sqlRetorno.put("CantDeudas",(Long) new Long(rs.getLong(9)));/* cantidad de deudas */
	           sqlRetorno.put("CodEtapaConvenio",(Long) new Long(rs.getLong(10)));/* c�digo etapa de convenio */
	           sqlRetorno.put("MontoCuotaContado",(String) rs.getString(11));/* monto cuota contado */
	           sqlRetorno.put("DescTipoPago",(String)  rs.getString(12));/* descripci�n tipo convenio */

	           retorno.addElement(sqlRetorno);
	        }

	        rs.close();
	        call.close();



	        return retorno;
	      }
	      catch(SQLException e) {
	        throw new EJBException("Error ejecutando el SQL " + e.toString());
	      }
	      finally{
	        this.closeConnection();
	      }

	    }


	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Entrega de la base de datos las solicitud del contribuyente
	    *  @param Parametros (Hashmap), que contiene id_persona del contribuyente
	    *  @return   Una colecci�n (Collection) que contienen los resultados de la b�squeda,
	    *  las claves del HashMap son: CodPropuesta,FechaSolicitud,Resuelta,
	    *  DescTipoConvenio,DescTipoDeuda,CantDeudas
	    */
	   public Collection getSolicitudPropuesta(HashMap parametros)
	  {
	     PreparedStatement ps = null;
	     Connection conn = null;
	     Vector retorno = new Vector();
	     int i=0;

	     try {
	      //conn = dataSource.getConnection();
	      conn = this.getConnection();

	      CallableStatement  call = conn.prepareCall("{? = call DATOSPROPUESTA.fCurSolicitudPropuesta(?)}");

	      call.registerOutParameter(++i, oracle.jdbc.OracleTypes.CURSOR);
	      Long IdPersona = (Long) parametros.get("IdPersona");
	      call.setLong(++i, IdPersona.longValue());
	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      while(rs.next())
	      {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("CodPropuesta",(Long) new Long(rs.getLong(1)));/* codigo propuesta */
	         sqlRetorno.put("FechaSolicitud",(String) rs.getString(2));/* fecha solicitud */
	         sqlRetorno.put("Resuelta",(String)  rs.getString(3));/* resuelta */
	         sqlRetorno.put("DescTipoConvenio",(String)  rs.getString(4));/* descripci�n tipo convenio */
	         sqlRetorno.put("DescTipoDeuda",(String)  rs.getString(5));/* descripci�n tipo deuda */
	         sqlRetorno.put("cod_etapa_convenio",(Long) new Long(rs.getLong(6)));/* c�digo de la etapa del convenio*/
	         sqlRetorno.put("etapa",(String) rs.getString(7));/*descripci�n de la etapa del convenio*/
	         sqlRetorno.put("CantDeudas",(Long) new Long(rs.getLong(8)));/* cantidad de deudas */

	         /*String DescTipoConvenio = (String) new String(rs.getString(4));
	         String DescTipoDeuda = (String) new String(rs.getString(5));

	         System.out.println("DescTipoConvenio ::" + DescTipoConvenio);
	         System.out.println("DescTipoDeuda ::" + DescTipoDeuda);

	         if ((DescTipoConvenio.equals("RMH")&& DescTipoDeuda.equals("FISCAL"))||(DescTipoConvenio.equals("ART.192")))
	              {

	                  DescTipoConvenio = "DEUDAS FISCALES";

	        }
	              else
	              {
	                  if ((DescTipoConvenio.equals("RMH"))&&(DescTipoDeuda.equals("TERRITORIAL")))
	                  {
	                      DescTipoConvenio = "DEUDAS TERRITORIALES";

	                  }

	               }

	         sqlRetorno.put("DescTipoConvenio",(String) DescTipoConvenio);/* descripci�n tipo convenio */



	         retorno.addElement(sqlRetorno);
	      }

	      rs.close();
	      call.close();

	      return retorno;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	    finally{
	      this.closeConnection();
	    }

	  }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Cambia el c�digo de la etapa de la propuesta e inserta en historial convenios
	   *  @param Parametros (Hashmap), que contiene CodPropuesta, CodEtapaConvenio,
	   *  CodCausalCaducidad, Observaciones
	   *  @return mensaje
	   */
	    public String getCambioEtapaPropuesta(HashMap parametros)
	     {
	     Connection conn = null;
	     String retorno = "";
	     try {
	        // conn = dataSource.getConnection();
	        conn = this.getConnection();

	        CallableStatement  call = conn.prepareCall("{?= call DATOSPROPUESTA.CambioEtapaPropuesta(?,?,?,?,?)}");

	        Long CodPropuesta = (Long) parametros.get("CodPropuesta");
	        Long CodEtapaConvenio = (Long) parametros.get("CodEtapaConvenio");
	        Long CodCausalCaducidad = (Long) parametros.get("CodCausalCaducidad");
	        String Observaciones = (String) parametros.get("Observaciones");
	        String FechaActivacion = (String) parametros.get("FechaActivacion");

	        call.registerOutParameter(1,java.sql.Types.VARCHAR);
	        call.setLong(2,CodPropuesta.longValue());
	        call.setLong(3,CodEtapaConvenio.longValue());
	        call.setLong(4,CodCausalCaducidad.longValue());
	        call.setString(5,Observaciones);
	        call.setString(6,FechaActivacion);
	        call.execute();

	        retorno = (String) call.getString(1);
	        call.close(); call=null;

	        return retorno;

	        }
	     catch(SQLException e) {

	        throw new EJBException("Error ejecutando el SQL " + e.toString());
	        }
	     finally{
	        this.closeConnection();
	     }
	     }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Revierte caducidad en historial convenios
	   *  @param Parametros (Hashmap), que contiene C�digo de la Propuesta
	   *  @return mensaje
	   */
	    public String getRevertirCaducidad(HashMap parametros)
	     {
	     Connection conn = null;
	     String retorno="";

	     try {
	        // conn = dataSource.getConnection();
	        conn = this.getConnection();

	        CallableStatement  call = conn.prepareCall("{? = call DATOSPROPUESTA.RevertirCaducidad(?,?)}");

	        Long CodPropuesta = (Long) parametros.get("CodPropuesta");
	        String Observaciones = (String) parametros.get("Observaciones");

	        call.registerOutParameter(1,java.sql.Types.VARCHAR);
	        call.setLong(2,CodPropuesta.longValue());
	        call.setString(3,Observaciones);
	        call.execute();
	        retorno = (String) call.getString(1);

	        call.close(); call=null;

	        return retorno;

	        }
	     catch(SQLException e) {
	        throw new EJBException("Error ejecutando el SQL " + e.toString());
	        }
	    finally{
	       this.closeConnection();
	    }
	     }


	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Cambia de etapa la (s) propuesta(s) a etapa propuesta eliminada
	   * @param PropuestasAEliminar, corresponde a un string con los c�digos de las propuestas a eliminar
	   * @return mensaje
	   */
	 public String getEliminarPropuestas(String propuestasAEliminar)
	     {
	     String separador = ",";
	     String[] spropuestasAEliminar = StringExt.split(propuestasAEliminar,separador);
	     String retorno = "0";
	     Connection conn = null;
	     try {
	       //conn = dataSource.getConnection();
	       conn = this.getConnection();

	       conn.setAutoCommit(false);

	       for (int index = 0; index < spropuestasAEliminar.length; index++) {

	          Long codigo = new Long(spropuestasAEliminar[index]);
	          CallableStatement call = conn.prepareCall("{? = call DatosPropuesta.EliminarPropuesta(?)}");
	          call.registerOutParameter(1,java.sql.Types.VARCHAR);
	          call.setLong(2, codigo.longValue());
	          call.execute();

	           retorno = (String) call.getString(1);//registra mensaje de error(-1) o exito(0)

	           call.close();  call = null;
	       }

	       conn.commit();

	       return retorno;

	       }
	     catch (SQLException e){
	        throw new EJBException("Error ejecutando el SQL " + e.toString());
	       }
	       finally{
	          this.closeConnection();
	       }

	    }


	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Cambia de etapa la (s) condonacion(es) otorgada(s) a etapa condonaci�n no vigente
	  * @param PropuestasAEliminar, corresponde a un string con los c�digos de las condonaciones a cambiar de etapa
	  * @return mensaje
	  */
	 public String getEliminarCondonacion(String propuestasAEliminar) throws Exception, RemoteException, SQLException
	     {
	     String separador = ",";
	     String[] spropuestasAEliminar = StringExt.split(propuestasAEliminar,separador);
	     String retorno = "0";
	     Connection conn = null;
	     int ret = 0;
	     String resp = " ";
	     String TCont ="1";
	     String vCodigoItemDeuda="";
	     try {
	       //conn = dataSource.getConnection();
	       conn = this.getConnection();

	       conn.setAutoCommit(false);

	       Context ctx = new InitialContext();
	       Object home = ctx.lookup("cobranzas.corporativo.servicioCondonacion.Condonacion");
	       CondonacionHome condonacionHome = (CondonacionHome) PortableRemoteObject.narrow(home,CondonacionHome.class);
	       Condonacion condonacion = condonacionHome.create();

	       for (int index = 0; index < spropuestasAEliminar.length; index++) {

	          Long codigo = new Long(spropuestasAEliminar[index]);


	          CallableStatement call2 = conn.prepareCall("{? = call DatosPropuesta.fCurDeudasPropuesta(?)}");
	          call2.registerOutParameter(1, OracleTypes.CURSOR);
	          call2.setLong(2, codigo.longValue());
	          call2.execute();
	          ResultSet rs = (ResultSet) call2.getObject(1);
	          while(rs.next()) {
	               long codDeuda = rs.getLong(2);
	               //java.util.Date vigencia = (java.util.Date) rs.getDate(30);
	               String vigencia="19800101";
	               int tipo_cont = rs.getInt(14);
	               long rut_rol = rs.getLong(15);
	               int tipo_form = rs.getInt(16);
	               long folio = rs.getLong(17);
	               java.util.Date vcto = (java.util.Date) rs.getDate(31);

	               vCodigoItemDeuda=StringExt.format("0000",rs.getInt(32));

	               CallableStatement  callRES = conn.prepareCall("{? = call InsertaPropuesta.ObtieneNumeroResolucion(?)}");
	               callRES.registerOutParameter(1,java.sql.Types.VARCHAR);
	               callRES.setLong(2,codigo.longValue());/* c�digo propuesta */
	               callRES.execute();
	               String sResol = callRES.getString(1);
	               callRES.close();

	               Long num_res = new Long(sResol);

	               //System.out.println("La resolucion es :"+num_res);
	               if (tipo_form==30){
	                 TCont = "2";
	               }
	               else
	               {
	                  TCont = "1";
	               }
	               String  vencimiento = DateExt.format("AAAAMM",vcto);
	               if (!vencimiento.equals("000000")){
	                   if (tipo_form == 29) {
	                       String vencimientoAnno = vencimiento;
	                       String vencimientoMes = vencimiento;

	                       vencimientoAnno = vencimientoAnno.substring(0, 4);
	                       vencimientoMes = vencimientoMes.substring(4, 6);
	                      // System.out.println("vencimientoAnno=" + vencimientoAnno);
	                      // System.out.println("vencimientoMes=" + vencimientoMes);

	                       int anno = Integer.parseInt(vencimientoAnno, 10);
	                       int mes = Integer.parseInt(vencimientoMes, 10);

	                       if (mes == 1) {
	                         mes = 12;
	                         anno = anno - 1;
	                       }
	                       else {
	                         mes = mes - 1;
	                       }

	                       if (mes < 10) {
	                          vencimiento = "" + anno + "0" + mes;
	                       }
	                       else {
	                          vencimiento = "" + anno + mes;
	                       }
	               }
	}

	               condonacion.condona(TCont, StringExt.format("00000000000000",rut_rol), StringExt.format("000",tipo_form), StringExt.format("0000000000",folio), vencimiento, StringExt.format("0000000",num_res.longValue()),vigencia,StringExt.format("000",0),vCodigoItemDeuda);
	               ret = 0;
	               resp = condonacion.getCadena();
	               boolean condonacionRealizada = true;
	               try {
	                     if (!resp.substring(4,5).equals(" "))
	                     {
	                        ret = 2;
	                     }
	                     else
	                     {
	                       ret = Integer.parseInt(condonacion.getValorRet());
	                       if (ret ==0)
	                       {
	                         //System.out.println("La condonacion en CERO se realiz� exitosamente a VMS");
	                       }
	                     }
	               } catch(Exception e) {
	                       ret = 2;
	                       //System.out.println("La condonacion en CERO NO se realiz� con exito a VMS");
	                       throw new EJBException("La condonacion en CERO NO se realiz� con exito a VMS"+e);
	                }

	                if (ret != 0){
	                // La condonaci�n en l�nea no pudo efectuarse
	                   CallableStatement  callAC = conn.prepareCall("{? = call InsertaPropuesta.ErrorCondonacionVax(?,?)}");
	                   callAC.registerOutParameter(1,java.sql.Types.VARCHAR);
	                   callAC.setLong(2,codigo.longValue());/* c�digo propuesta */
	                   callAC.setLong(3,codDeuda);/* c�digo deuda */
	                   callAC.execute();

	                   String sRes = callAC.getString(1);
	                   if (!sRes.equals("0")){
	                          //System.out.println("no se actualizo con exito la condonaci�n de deuda incluida ::" + codigo + "::" + codDeuda);
	                    }
	                    callAC.close();
	                }



	          }

	          //rs.close();
	          //call2.close();
	         // rs=null;
	          //call2=null;
	         // System.out.println("Estoy por ir a eliminar la propuesta, el codigo es :"+codigo);
	          CallableStatement call = conn.prepareCall("{? = call DatosPropuesta.EliminarCondonacion(?)}");
	          call.registerOutParameter(1,java.sql.Types.VARCHAR);
	          call.setLong(2, codigo.longValue());
	          call.execute();

	           retorno = (String) call.getString(1);//registra mensaje de error(-1) o exito(0)

	           call.close();  call = null;
	       }

	       condonacion = null;
	       condonacionHome = null;
	       home = null;

	       conn.commit();

	       return retorno;

	       }
	     catch (SQLException e){
	        throw new EJBException("Error ejecutando el SQL " + e.toString());
	       }
	       finally{
	          this.closeConnection();
	       }
	    }


	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Entrega el total a pagar, es decir la sumatoria del total a pagar de las
	   *  deudas de la propuesta
	   *  @ return Long totalPagar
	  */
	  public Long getTotalPagar()
	     {
	     return totalPagar;
	     }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Entrega el numero de resoluci�n de la propuesta de origen
	   *  @param - CodPropuestaOrigen, c�digo de la propuesta de destino
	   *  @ return - Long con el numero de resoluci�n de la propuesta de origen
	   */
	  public Long NumResolucionPropuestaOrigen(Long CodPropuestaDestino)
	   {
	     try{

	     Connection conn = this.getConnection();
	     CallableStatement  call = conn.prepareCall("{? = call DATOSPROPUESTA.ObtienePropuestaOrigen(?)}");

	     call.registerOutParameter(1,java.sql.Types.VARCHAR);
	     call.setLong(2,CodPropuestaDestino.longValue());
	     call.execute();

	     Long NumResolucion  = new Long(-1);
	     String strNumResolucion = (String) call.getString(1);
	     try{
	          NumResolucion = new Long(strNumResolucion);
	     } catch (NumberFormatException e){
	           throw new SQLException ("Obtiene Numero Resolucion " + strNumResolucion + e.toString());
	     }

	     strNumResolucion = null;
	     call.close(); call=null;



	     return NumResolucion;

	     }catch (SQLException e){
	        throw new EJBException("Obtiene Numero Resolucion " + e.toString());
	       }
	       finally{
	          this.closeConnection();
	       }


	   }
	    /** 
		 *
		 * M�todo privado que retorna la conexi�n a la base de datos
  	    *
	     */
	    private Connection getConnection() throws SQLException {
	        if (this.connection == null){
	            this.connection = this.dataSource.getConnection();
	        }

	        if (this.connection.isClosed()){
	            this.connection = this.dataSource.getConnection();
	        }

	        //this.connection.setAutoCommit(true);

	        return this.connection;
	    }

	    /** 
		 *
		 * M�todo privado que cierra la conexi�n a la base de datos
	     *
	     */
	    private void closeConnection(){
	      try {
	            if (this.connection != null){
	                if (!this.connection.isClosed()){
	                    this.connection.close();
	                }
	                this.connection = null;
	            }
	        } catch (SQLException e) {
	            //System.out.println("Error closeConnection();");
	            throw new EJBException("Error closeConnection();"+e);
	        }
	    }

	
	
}