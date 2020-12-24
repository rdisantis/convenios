/**
 * 
 */
package cl.decalink.tgr.convenios.ejb.session.stateless.consultarexclusiones;

import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import cl.decalink.tgr.convenios.StringExt;
import cl.decalink.tgr.convenios.deudas.DetalleDeudaExclusion;
import cl.decalink.tgr.convenios.deudas.DeudaWeb;
import cl.decalink.tgr.convenios.deudas.DeudorExclusionAlerta;


/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="consultarexclusiones"	
 *           description="An EJB named consultarexclusiones"
 *           display-name="consultarexclusiones"
 *           jndi-name="cobranzas.convenios.negocio.ejb.session.stateless.consultarexclusiones"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * @weblogic.enable-call-by-reference
 *   True
 * @ejb.transaction
 *   type="Required"
 *   
 *   
 *   
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public class consultarexclusionesBean implements javax.ejb.SessionBean {

	
	 private Connection connection;

	  private DataSource dataSource;
	  private SessionContext sessionContext;
	  private Connection conn;
	// private java.sql.ResultSet  ResultSet;

	/** Tipo de Contribuyente */
	 public Long TIP_CONT_in;
	  public Long rUT_ROL_in; /** Tipo de Formulario */
	 public Long TIP_FORM_in;
	 /** Folio */
	 public Long FOLIO_in;
	 /** Fecha de Vencimiento */
	 //public String FECHA_VENCIMIENTO_in;
	 public java.sql.Date FECHA_VENCIMIENTO_in;
	 /** C�digo de la exclusi�n del deudor referenciado */
	 public Long COD_EXCLUSION_DEUDOR_in;

	 private Collection xconsultarExclusiones_R;
	 private Collection xtVerificaRoles_R;

	 private HashMap Parametros_Exclusiones;
	 private String Rol;

	
	
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
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
		 //System.out.println("<----consultarexclusionesBean---->");
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
	public consultarexclusionesBean() {
		// TODO Auto-generated constructor stub
	}
	
    /** 
	 * Realiza una b�squeda de las exclusiones de Condonaci�n por a�o Calendario
	 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
	 * @return el conjunto de deudas excluidas.
	 */
	public ResultSet exclusionesCondonacionAgno(HashMap parametros)
	{   
	     PreparedStatement ps = null;
	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();
	     try {

	        
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Conconacion_Ano.fExclusiones(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	 }


	public ResultSet exclusionesCondonacionAgnoF30(HashMap parametros)
	{
	     PreparedStatement ps = null;
	
	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();
	     try {
	  
	       CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Conconacion_Ano_30.fExclusiones(?,?)}");

	       call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	       String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	       call.setString(2, ID_PERSONAS);

	       String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	       call.setString(3, RUT_CONTR);

	       call.execute();

	       ResultSet rs = (ResultSet) call.getObject(1);
	       return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	 }


	public ResultSet exclusionesCondonacionAgnoP1(HashMap parametros)
	{
	     PreparedStatement ps = null;
	
	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();
	     try {

	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Conconacion_Ano_p1.fExclusiones(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	 }


	public ResultSet exclusionesCondonacionAgnoP2(HashMap parametros)
	{
		 
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();
	     try {
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Conconacion_Ano_p2.fExclusiones(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	 }

	
	/*M�todo que determina las exclusiones por Formulario 42*/
	public ResultSet exclusionesCondonacionForm42(HashMap parametros)
	{
		
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();
	     try {

	      
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Cond_Form_42.fExclusiones(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	 }



    /** 
	 * Realiza una b�squeda de las exclusiones RMH
	 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
	 * @return el conjunto de deudas excluidas.
	 */
	  public ResultSet exclusiones_RMH(HashMap parametros)
	{
	
	     PreparedStatement ps = null;
	     //Connection conn = null;
	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();
	     try {
	     
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Rmh.fExclusiones(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	 }



	    /** 
		 * Realiza una b�squeda de las exclusiones convenios caducados
		 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
		 * @return el conjunto de deudas excluidas.
		 */
	 public ResultSet exclusionConvenioCaducado(HashMap parametros)
	 {
		
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();

	     try {
	    
	      CallableStatement  call = conn.prepareCall("{? = call Exclusionesconvcaducado.fExclusiones(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }

	/** 
	 * Realiza una b�squeda de las exclusiones generales
	 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
	 * @return el conjunto de deudas excluidas.
	 */
	public ResultSet exclusionesART192Generales(HashMap parametros)
	  {
		
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();

	     try {
	    	 
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Gnrales_Art_192.fCurEXCLUSIONESGNRALESART192(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }

    /** 
	 * Realiza una b�squeda de las exclusiones generales
	 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
	 * @return el conjunto de deudas excluidas.
	 */
	public ResultSet exclusionesRMHGernerales(HashMap parametros)
	  {
	
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();

	     try {
	   
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Generales_Rmh.fCurEXCLUSIONESGENERALESRMH(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }


    /** 
	 * Realiza una b�squeda de las exclusiones por deudas vigentes o convenio activo
	 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
	 * @return el conjunto de deudas excluidas.
	 */
	public ResultSet exclusionesPropuestConActivo(HashMap parametros)
	  {
		
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();

	     try {

	      CallableStatement  call = conn.prepareCall("{? = call EXCLU_PROP_CONVENIO_ACTIVO.FEXCLUSIONES_PRO_CON_ACTIVO(?,?)}");
	
	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }


    /** 
	 * Realiza una b�squeda de las exclusiones fiscales
	 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
	 * @return el conjunto de deudas excluidas.
	 */
	public ResultSet exclusionesRMHfiscales(HashMap parametros)
	  {
		
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();

	     try {
	    	 
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Rmh_Fiscal.fExclusiones_Fiscal(?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      String RUT_CONTR = (String) parametros.get("RUT_CONTR");
	      call.setString(3, RUT_CONTR);
	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }




    /** 
	 * Realiza una b�squeda de las exclusiones formularios 30
	 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
	 * @return el conjunto de deudas excluidas.
	 */
	public ResultSet exclusionesFormularios(HashMap parametros)
	  {
		
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();

	     try {
	      
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Formulario.fExclusiones_Formulario(?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      String ID_PERSONAS = (String) parametros.get("ID_PERSONAS");
	      call.setString(2, ID_PERSONAS);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }



    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * Realiza una b�squeda de las exclusiones formularios 30
	 * @param  ROL Rol a Consultar
	 * @return el conjunto de deudas excluidas.
	 */

	 //LUIS 10/11/2003

	public ResultSet exclusionesFormulariosROL(String ROL)
	  {
		
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();


	     try {
	    	 
	      CallableStatement  call = conn.prepareCall("{? = call Exclusiones_Formulario.fExclusiones_FormularioROL(?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      call.setString(2, ROL);

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);
	      return rs;

	    }
	    catch(SQLException e) {
	    	e.printStackTrace();
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }

	/**
	 * Modifica los valores de las exclusiones
	 * @param  parametros Contiene la lista de par�metros para ingresar en la consulta
	 */
	private void getModif_CutRepli_CvExclusiones(HashMap parametros)
	  {
		
	     PreparedStatement ps = null;

	     int i=0;
	     int cantidadPatametros = parametros.size();

	     try {
	    	 
	      CallableStatement  call = conn.prepareCall("{? = call Modificarexclusion.ModCvExclusiones(?,?,?,?,?,?)}");

	      call.registerOutParameter(1, java.sql.Types.VARCHAR);

	      Long TIP_CONT_in = (Long) parametros.get("TIP_CONT");
	      Long RUT_ROL_in = (Long) parametros.get("RUT_ROL");
	      Long TIP_FORM_in = (Long) parametros.get("TIP_FORM");
	      Long FOLIO_in = (Long) parametros.get("FOLIO");
	      java.sql.Date FECHA_VENCIMIENTO_in = (java.sql.Date) parametros.get("FECHA_VENCIMIENTO");
	      Long COD_EXCLUSION_DEUDOR_in = (Long) parametros.get("COD_EXCLUSION_DEUDOR");

	      call.setLong(2, TIP_CONT_in.longValue());
	      call.setLong(3, RUT_ROL_in.longValue());
	      call.setLong(4, TIP_FORM_in.longValue());
	      call.setLong(5, FOLIO_in.longValue());
	      call.setDate (6, FECHA_VENCIMIENTO_in);
	      call.setLong(7, COD_EXCLUSION_DEUDOR_in.longValue());


	      this.TIP_CONT_in=TIP_CONT_in;
	      this.rUT_ROL_in= RUT_ROL_in;
	      this.TIP_FORM_in=TIP_FORM_in;
	      this.FOLIO_in =FOLIO_in;
	      this.FECHA_VENCIMIENTO_in= FECHA_VENCIMIENTO_in;
	      this.COD_EXCLUSION_DEUDOR_in=COD_EXCLUSION_DEUDOR_in;


	      call.execute();
	      call.close();

	    }
	    catch(SQLException e) {
	      throw new EJBException("******** "+ e.toString() + "<br>Datos =" + this.TIP_CONT_in+"/<br>"+this.rUT_ROL_in+"/<br>"+this.TIP_FORM_in+"/<br>"+this.FOLIO_in+"/<br>"+this.FECHA_VENCIMIENTO_in+"/<br>"+this.COD_EXCLUSION_DEUDOR_in+"<br>");
	    }


	  }


	/**
	 * Realiza una b�squeda de las deudas no ubicadas en CUT
	 * @param  parametros Contiene la lista de parametros para ingresar en la consulta
	 * @return el conjunto de deudas no CUT asociadas.
	 */
	 private ResultSet getbuscaDeudasNoCut(HashMap parametros)
	  {
		 
	     PreparedStatement ps = null;

	     int i=0;
	     int cantidadPatametros = parametros.size();

	     try {
	      CallableStatement  call = conn.prepareCall("{? = call ConsultarExclusiones.fConsultarDeudasNoCut(?,?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      Long VRutIn = (Long) parametros.get("RutV");
	      Long VTIP_FORM = (Long) parametros.get("TipoFormulario");
	      Long VFOLIOR = (Long) parametros.get("Folio");

	      call.setLong(2, VRutIn.longValue());
	      call.setLong(3, VTIP_FORM.longValue());
	      call.setLong(4, VFOLIOR.longValue());

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      return rs;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }

	 /**
	 * Realiza una b�squeda de las deudas ubicadas en CUT
	 * @param  parametros Contiene la lista de parametros para ingresar en la consulta
	 * @return el conjunto de deudas CUT asociadas.
	 */
	 private ResultSet getbuscaCutReplica(HashMap parametros)
	  {
		 
	     PreparedStatement ps = null;

	     int i=0;
	     int cantidadPatametros = parametros.size();

	     try {
	    	 
	      CallableStatement  call = conn.prepareCall("{? = call ConsultarExclusiones.fConsultarCutReplica(?,?,?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      Long VRutIn = (Long) parametros.get("RutV");
	      Long VTIP_FORM = (Long) parametros.get("TipoFormulario");
	      Long VFOLIOR = (Long) parametros.get("Folio");

	      call.setLong(2, VRutIn.longValue());
	      call.setLong(3, VTIP_FORM.longValue());
	      call.setLong(4, VFOLIOR.longValue());


	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      return rs;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }

	  }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * Recupera los datos del Contribuyente
	 * @param  parametros Contiene la lista de parametros para ingresar en la consulta
	 * @return Los datos del contribuyente
	 */

	public Collection DatosContribuyente(HashMap parametros)
	  {
	     PreparedStatement ps = null;
	     Connection conn = null;
	     Vector retorno = new Vector();

	     int cantidadPatametros = parametros.size();
	     try {

	    	 conn = this.getConnection();

	      CallableStatement  call = conn.prepareCall("{? = call ConsultarExclusiones.fDatosContribuyente(?)}");
	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      Long idpersona = (Long) parametros.get("IDPERSONA");
	      call.setLong(2, idpersona.longValue());

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);


	      while(rs.next())
	      {
	            HashMap sqlRetorno = new HashMap();

	            sqlRetorno.put("FECHAACTUAL",(String) rs.getString(1));
	            sqlRetorno.put("NOMBRECOMPLETO",(String) rs.getString(2));
	            sqlRetorno.put("RUT",(String) rs.getString(3));
	            sqlRetorno.put("CALLE",(String) rs.getString(4));
	            sqlRetorno.put("NUMERO",(String) rs.getString(5));
	            sqlRetorno.put("DPTO",(String) rs.getString(6));
	            sqlRetorno.put("BLOCK",(String) rs.getString(7));
	            sqlRetorno.put("MANZANA",(String) rs.getString(8));
	            sqlRetorno.put("PREDIO",(String) rs.getString(9));
	            sqlRetorno.put("DESCRIPCION",(String) rs.getString(10));

	            retorno.addElement((HashMap) sqlRetorno);
	      }//termina while

	      rs.close();
	      call.close();

	      return retorno;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL " + e.toString());
	    }
	    finally
	    {
	      this.closeConnection();
	      try {
	        conn.close();
	      }
	      catch (SQLException ex) {
	      }
	    }
	  }


    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * Obtiene el listado de Exclusiones
	 * @param  parametros Contiene la lista de parametros para ingresar en la consulta
	 * @return Listado de Exclusiones
	 */
	 public Collection getconsultarExclusiones(HashMap parametros)
	  {
		
	     PreparedStatement ps = null;

	     Vector retorno = new Vector();
	     int cantidadPatametros = parametros.size();

	     String ID_PERSONAS="";
	     String RUT_CONTR ="";

	     int vDiaVenc=1;
	     int vMesVenc=0;
	     int vAgnoVenc=1900;
	     GregorianCalendar grFechaVenc=null;
	     java.sql.Date vFechaVenc=null;


	     try {
	  
	       conn = this.getConnection();

	       //exclusionesPropuestConActivo
	       //LLAMA A exclusionesCondonacionAgno PARA RECUPRERAR ResultSet
	       Long idpersona = (Long) parametros.get("IDPERSONA");
	       Long rut = (Long) parametros.get("RUT");
	       Long Perfil = (Long) parametros.get("Perfil");
	       //System.out.println("PERFIL MASIVO o NO : "+Perfil);
	       ID_PERSONAS = idpersona.toString() ;
	       RUT_CONTR = rut.toString();

	       HashMap ParametrosexclusionesPropuestConActivo = new HashMap();

	       ParametrosexclusionesPropuestConActivo.put("ID_PERSONAS",ID_PERSONAS);
	       ParametrosexclusionesPropuestConActivo.put("RUT_CONTR",RUT_CONTR);

	       ResultSet rsexclusionesPropuestConActivo =(ResultSet) exclusionesPropuestConActivo((HashMap) ParametrosexclusionesPropuestConActivo);
	       
	       
	       while(rsexclusionesPropuestConActivo.next())
	       {//System.out.println("rsexclusionesPropuestConActivo");
	    	   
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesPropuestConActivo.getLong(1)));
	         sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesPropuestConActivo.getLong(2)));
	         sqlRetorno.put("EFECTO",(String) rsexclusionesPropuestConActivo.getString(3));
	         sqlRetorno.put("TIPO",(String) rsexclusionesPropuestConActivo.getString(4));
	         sqlRetorno.put("DESCRIPCION",(String) rsexclusionesPropuestConActivo.getString(5));
	         sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesPropuestConActivo.getLong(6)));
	         sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesPropuestConActivo.getLong(7)));
	         //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rsexclusionesPropuestConActivo.getDate(8));
	         sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesPropuestConActivo.getString(9));

	         vDiaVenc = rsexclusionesPropuestConActivo.getInt(10);
	         vMesVenc = rsexclusionesPropuestConActivo.getInt(11);
	         vAgnoVenc = rsexclusionesPropuestConActivo.getInt(12);

	         grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	         vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	         sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);
	         retorno.addElement((HashMap) sqlRetorno);
	       }

	       rsexclusionesPropuestConActivo.close();
	       rsexclusionesPropuestConActivo=null;

	//TERMINA  exclusionesPropuestConActivo

	       idpersona = (Long) parametros.get("IDPERSONA");
	       rut = (Long) parametros.get("RUT");
	       Perfil = (Long) parametros.get("Perfil");

	       if(Perfil.longValue() <5)
	       {

	            CallableStatement  call = conn.prepareCall("{? = call ConsultarExclusiones.fConsultarExclusiones(?)}");

	            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	            call.setLong(2, idpersona.longValue());

	            ID_PERSONAS = idpersona.toString() ;
	            RUT_CONTR = rut.toString();

	            call.execute();

	            ResultSet rs = (ResultSet) call.getObject(1);

	            Long VTIPOFORMULARIO=new Long(0);

	            while(rs.next())
	            {//System.out.println("ConsultarExclusiones.fConsultarExclusiones");
	                Long COD_TIPO_EXCLUSION=new Long(rs.getInt(2));
	                Long VFOLIO=new Long(rs.getLong(6));
	                Long VFolio_SII=new Long(rs.getLong(4));
	                if (COD_TIPO_EXCLUSION.intValue()==2)
	                {
	                  VTIPOFORMULARIO=new Long(21);
	                }
	                if (COD_TIPO_EXCLUSION.intValue()==3)
	                {
	                  VTIPOFORMULARIO=new Long(45);
	                }
	                if ((COD_TIPO_EXCLUSION.intValue()==1)||
	                    (COD_TIPO_EXCLUSION.intValue()==4)||
	                    (COD_TIPO_EXCLUSION.intValue()==5)||
	                    (COD_TIPO_EXCLUSION.intValue()==6)||
	                    (COD_TIPO_EXCLUSION.intValue()==7)||
	                    (COD_TIPO_EXCLUSION.intValue()==8))
	                {

	                    VTIPOFORMULARIO=new Long(0);
	                }

	                if (((COD_TIPO_EXCLUSION.intValue()==2) || (COD_TIPO_EXCLUSION.intValue()==3)) && ((VFOLIO.longValue()!=0)  || (VFolio_SII.longValue()!=0)))
	                {

	                  HashMap sqlRetorno = new HashMap();

	                  sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	                  sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	                  sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	                  sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	                  //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rs.getDate(5));
	                  sqlRetorno.put("FOLIO",(Long) new Long(rs.getLong(6)));
	                  sqlRetorno.put("TIP_FORM",VTIPOFORMULARIO);
	                  sqlRetorno.put("RUT_ROL",(Long) new Long(rs.getLong(8)));
	                  sqlRetorno.put("TIP_CONT",(Long) new Long(rs.getLong(9)));
	                  sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	                  sqlRetorno.put("CAUSA",(String) rs.getString(11));
	                  sqlRetorno.put("EFECTO",(String) rs.getString(12));
	                  sqlRetorno.put("TIPO",(String) rs.getString(13));
	                  sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	                  sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));
	                  sqlRetorno.put("COD_EFECTO",(Long) new Long(rs.getLong(15)));

	                  vDiaVenc = rs.getInt(16);
	                  vMesVenc = rs.getInt(17);
	                  vAgnoVenc = rs.getInt(18);

	                  grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                  vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                  sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                  retorno.addElement((HashMap) sqlRetorno);
	                }
	                else
	                {
	                    Long VFOLIO_SII=new Long(rs.getLong(4));
	                    Long VFOLIOREC=new Long(rs.getLong(6));

	                    if (COD_TIPO_EXCLUSION.intValue()==2)
	                    {
	                        VTIPOFORMULARIO=new Long(21);
	                    }
	                    if (COD_TIPO_EXCLUSION.intValue()==3)
	                    {
	                        VTIPOFORMULARIO=new Long(45);
	                    }

	                    if (VFOLIOREC.longValue() >0  && VFOLIO_SII.longValue() ==0)
	                    {
	                        VFOLIO_SII=VFOLIOREC;
	                    }
	                    else
	                    {
	                        if (VFOLIO_SII.longValue()>0 && VFOLIOREC.longValue()==0)
	                        {
	                            VFOLIO_SII=VFOLIO_SII;
	                        }
	                    }


	                    if ((COD_TIPO_EXCLUSION.intValue()==1)||
	                        (COD_TIPO_EXCLUSION.intValue()==4)||
	                        (COD_TIPO_EXCLUSION.intValue()==5)||
	                        (COD_TIPO_EXCLUSION.intValue()==6)||
	                        (COD_TIPO_EXCLUSION.intValue()==7)||
	                        (COD_TIPO_EXCLUSION.intValue()==8))
	                      {
	                        VFOLIO_SII=new Long(0);
	                        VTIPOFORMULARIO=new Long(0);
	                      }

	                    HashMap Parametros = new HashMap();

	                    Parametros.put("RutV",(Long) rut);
	                    Parametros.put("TipoFormulario",(Long) VTIPOFORMULARIO);
	                    Parametros.put("Folio",(Long) VFOLIO_SII);
                        //System.out.println("<----------ingreso-------->");
                        //System.out.println("rut--------> "+rut);
                        //System.out.println("formu--------> "+VTIPOFORMULARIO);
                        //System.out.println("folio--------> "+VFOLIO_SII);
                        //System.out.println("<------------------------->"); 
	                    ResultSet resulsetCut_replica =(ResultSet) getbuscaCutReplica((HashMap) Parametros);

	                    while(resulsetCut_replica.next())
	                    {//System.out.println("getbuscaCutReplica");
	                    	//System.out.println("ingreso result bd 1-------->");
	                      HashMap sqlRetorno = new HashMap();

	                      sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	                      sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	                      sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	                      sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	                      //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) resulsetCut_replica.getDate(1));
	                      sqlRetorno.put("FOLIO",(Long) new Long(resulsetCut_replica.getLong(2)));
	                      sqlRetorno.put("TIP_FORM",(Long) new Long(resulsetCut_replica.getLong(3)));
	                      sqlRetorno.put("RUT_ROL",(Long) new Long(resulsetCut_replica.getLong(4)));
	                      sqlRetorno.put("TIP_CONT",(Long) new Long(resulsetCut_replica.getLong(5)));
	                      sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	                      sqlRetorno.put("CAUSA",(String) rs.getString(11));
	                      sqlRetorno.put("EFECTO",(String) rs.getString(12));
	                      sqlRetorno.put("TIPO",(String) rs.getString(13));
	                      sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	                      sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));
	                      sqlRetorno.put("COD_EFECTO",(Long) new Long(rs.getLong(15)));

	                      vDiaVenc = resulsetCut_replica.getInt(6);
	                      vMesVenc = resulsetCut_replica.getInt(7);
	                      vAgnoVenc = resulsetCut_replica.getInt(8);

	                      grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                      vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                      sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                      /*
	                       *Modifica cv_exclsiones_deudor seg�n campo encontrado
	                       *en cc_cut_replica
	                       */

	                      if ((COD_TIPO_EXCLUSION.intValue()==2) || (COD_TIPO_EXCLUSION.intValue()==3))
	                      {
	                    	//  System.out.println("ingreso result bd 2-------->");
	                        HashMap Mod_CVExclusiones = new HashMap();

	                        Mod_CVExclusiones.put("TIP_CONT",(Long) new Long(resulsetCut_replica.getLong(5)));
	                        Mod_CVExclusiones.put("RUT_ROL",(Long) new Long(resulsetCut_replica.getLong(4)));
	                        Mod_CVExclusiones.put("TIP_FORM",(Long) new Long(resulsetCut_replica.getLong(3)));
	                        Mod_CVExclusiones.put("FOLIO",(Long) new Long(resulsetCut_replica.getLong(2)));
	                        //Mod_CVExclusiones.put("FECHA_VENCIMIENTO",(java.sql.Date) resulsetCut_replica.getDate(1));
	                        Mod_CVExclusiones.put("COD_EXCLUSION_DEUDOR",(Long) new Long(rs.getLong(1)));

	                        vDiaVenc = resulsetCut_replica.getInt(6);
	                        vMesVenc = resulsetCut_replica.getInt(7);
	                        vAgnoVenc = resulsetCut_replica.getInt(8);

	                        grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                        vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                        Mod_CVExclusiones.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                        getModif_CutRepli_CvExclusiones(Mod_CVExclusiones);//Llamada al m�todo modificacion cv_exclusiones_deudor
	                      }
	                      /*Termina*/

	                      retorno.addElement((HashMap)  sqlRetorno);
	                      
	                      //System.out.println("termino sin problemas "+retorno.size());
	                    }
	                    /****Termina con Deudas Cut****/

	                    /****Comienza con deudas no cut****/

	                    HashMap ParametrosDeudasNoCut = new HashMap();

	                    ParametrosDeudasNoCut.put("RutV",(Long) rut);
	                    ParametrosDeudasNoCut.put("TipoFormulario",(Long) VTIPOFORMULARIO);
	                    ParametrosDeudasNoCut.put("Folio",(Long) VFOLIO_SII);

	                    ResultSet resulsetDeudasNocut =(ResultSet) getbuscaDeudasNoCut((HashMap) ParametrosDeudasNoCut);

	                    while(resulsetDeudasNocut.next())
	                    {
	                      HashMap sqlRetorno = new HashMap();

	                      sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	                      sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	                      sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	                      sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	                      // sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) resulsetDeudasNocut.getDate(1));
	                      sqlRetorno.put("FOLIO",(Long) new Long(resulsetDeudasNocut.getLong(2)));
	                      sqlRetorno.put("TIP_FORM",(Long) new Long(resulsetDeudasNocut.getLong(3)));
	                      sqlRetorno.put("RUT_ROL",(Long) new Long(resulsetDeudasNocut.getLong(4)));
	                      sqlRetorno.put("TIP_CONT",(Long) new Long(resulsetDeudasNocut.getLong(5)));
	                      sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	                      sqlRetorno.put("CAUSA",(String) rs.getString(11));
	                      sqlRetorno.put("EFECTO",(String) rs.getString(12));
	                      sqlRetorno.put("TIPO",(String) rs.getString(13));
	                      sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	                      sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));

	                      vDiaVenc = resulsetDeudasNocut.getInt(6);
	                      vMesVenc = resulsetDeudasNocut.getInt(7);
	                      vAgnoVenc = resulsetDeudasNocut.getInt(8);

	                      grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                      vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                      sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                      retorno.addElement((HashMap)  sqlRetorno);
	                    }

	                    resulsetDeudasNocut.close();
	                    resulsetDeudasNocut=null;
	               }//termina if
	      }//termina while


	//COMIENZA 2 VALIDACIONES EXCLUSIONES GENERALES Y LAS QUE FALTAN

	//LLAMA A exclusiones_RMH PARA RECUPRERAR ResultSet
	      HashMap ParametrosExclusionesRMH = new HashMap();

	      ParametrosExclusionesRMH.put("ID_PERSONAS",ID_PERSONAS);
	      ParametrosExclusionesRMH.put("RUT_CONTR",RUT_CONTR);

	      ResultSet rsExclusionRMH  =(ResultSet) exclusiones_RMH((HashMap) ParametrosExclusionesRMH);

	      while(rsExclusionRMH.next())
	      {
	    	 // System.out.println("ingreso result bd 3-------->");
	        HashMap sqlRetorno = new HashMap();

	        sqlRetorno.put("FOLIO",(Long) new Long(rsExclusionRMH.getLong(4)));
	        sqlRetorno.put("TIP_FORM",(Long) new Long(rsExclusionRMH.getLong(3)));
	        sqlRetorno.put("EFECTO",(String) rsExclusionRMH.getString(5));
	        sqlRetorno.put("TIPO",(String) rsExclusionRMH.getString(1));
	        sqlRetorno.put("DESCRIPCION",(String) rsExclusionRMH.getString(2));
	        sqlRetorno.put("TIP_CONT",(Long) new Long(rsExclusionRMH.getLong(6)));
	        sqlRetorno.put("RUT_ROL",(Long) new Long(rsExclusionRMH.getLong(7)));
	        //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rsExclusionRMH.getDate(8));
	        sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsExclusionRMH.getString(9));

	        vDiaVenc = rsExclusionRMH.getInt(10);
	        vMesVenc = rsExclusionRMH.getInt(11);
	        vAgnoVenc = rsExclusionRMH.getInt(12);

	        grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	        vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	        sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	        retorno.addElement((HashMap) sqlRetorno);
	      }

	      rsExclusionRMH.close();
	      rsExclusionRMH=null;

	//TERMINA  exclusiones_RMH

	//LLAMA A exclusionConvenioCaducado PARA RECUPRERAR ResultSet
	/*
	El Perfil solo toma en cuenta para validar que esta exclusi�n opera en Internet y no opera en intranet
	*/
	      if (Perfil.longValue()==0)
	      {//System.out.println("ingreso result bd 4-------->");
	        HashMap ParametrosexclusionConvenioCaducado = new HashMap();

	        ParametrosexclusionConvenioCaducado.put("ID_PERSONAS",ID_PERSONAS);
	        ParametrosexclusionConvenioCaducado.put("RUT_CONTR",RUT_CONTR);

	        ResultSet rs1 =(ResultSet) exclusionConvenioCaducado((HashMap) ParametrosexclusionConvenioCaducado);

	        while(rs1.next())
	        {
	          HashMap sqlRetorno = new HashMap();

	          sqlRetorno.put("FOLIO",(Long) new Long(rs1.getLong(4)));
	          sqlRetorno.put("TIP_FORM",(Long) new Long(rs1.getLong(3)));
	          sqlRetorno.put("EFECTO",(String) rs1.getString(5));
	          sqlRetorno.put("TIPO",(String) rs1.getString(1));
	          sqlRetorno.put("DESCRIPCION",(String) rs1.getString(2));
	          sqlRetorno.put("TIP_CONT",(Long) new Long(rs1.getLong(6)));
	          sqlRetorno.put("RUT_ROL",(Long) new Long(rs1.getLong(7)));
	          //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rs1.getDate(8));
	          sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs1.getString(9));

	          vDiaVenc = rs1.getInt(10);
	          vMesVenc = rs1.getInt(11);
	          vAgnoVenc = rs1.getInt(12);

	          grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	          vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	          sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	          retorno.addElement((HashMap) sqlRetorno);
	        }

	        rs1.close();
	        rs1=null;
	//TERMINA  exclusionConvenioCaducado
	}

	//LLAMA A exclusionesRMHGernerales PARA RECUPRERAR ResultSet

	       HashMap ParametrosexclusionesRMHGernerales = new HashMap();

	       ParametrosexclusionesRMHGernerales.put("ID_PERSONAS",ID_PERSONAS);
	       ParametrosexclusionesRMHGernerales.put("RUT_CONTR",RUT_CONTR);

	       ResultSet rs2 =(ResultSet) exclusionesRMHGernerales((HashMap) ParametrosexclusionesRMHGernerales);

	       while(rs2.next())
	       {
	         /*Validaci�n del Procedimiento almacenado retorna   1= si pertenece exclusi�n  2=no pertenece exclusi�n*/
	         if (!rs2.getString(6).equals("2"))
	         {
	           HashMap sqlRetorno = new HashMap();

	           sqlRetorno.put("FOLIO",(Long) new Long(rs2.getLong(4)));
	           sqlRetorno.put("TIP_FORM",(Long) new Long(rs2.getLong(3)));
	           sqlRetorno.put("EFECTO",(String) rs2.getString(7));
	           sqlRetorno.put("TIPO",(String) rs2.getString(9));
	           sqlRetorno.put("DESCRIPCION",(String) rs2.getString(8));
	           sqlRetorno.put("TIP_CONT",(Long) new Long(rs2.getLong(1)));
	           sqlRetorno.put("RUT_ROL",(Long) new Long(rs2.getLong(2)));
	           //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rs2.getDate(5));
	           sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs2.getString(10));

	           vDiaVenc = rs2.getInt(11);
	           vMesVenc = rs2.getInt(12);
	           vAgnoVenc = rs2.getInt(13);

	           grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	           vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	           sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	           //System.out.println("ingreso result bd 5-------->");
	           retorno.addElement((HashMap) sqlRetorno);
	         }

	       }
	       rs2.close();
	       rs2=null;
	//TERMINA  exclusionesRMHGernerales

	//LLAMA A exclusionesArt192Generales PARA RECUPRERAR ResultSet

	       HashMap ParametrosexclusionesART192Generales = new HashMap();

	       ParametrosexclusionesART192Generales.put("ID_PERSONAS",ID_PERSONAS);
	       ParametrosexclusionesART192Generales.put("RUT_CONTR",RUT_CONTR);

	       ResultSet rs3 =(ResultSet) exclusionesART192Generales((HashMap) ParametrosexclusionesART192Generales);

	       while(rs3.next())
	       {


	         /*Validaci�n del Procedimiento almacenado retorna   1= si pertenece exclusi�n  2=no pertenece exclusi�n*/
	         if (!rs3.getString(6).equals("2"))
	         {
	           HashMap sqlRetorno = new HashMap();

	           sqlRetorno.put("FOLIO",(Long) new Long(rs3.getLong(4)));
	           sqlRetorno.put("TIP_FORM",(Long) new Long(rs3.getLong(3)));
	           sqlRetorno.put("EFECTO",(String) rs3.getString(7));
	           sqlRetorno.put("TIPO",(String) rs3.getString(9));
	           sqlRetorno.put("DESCRIPCION",(String) rs3.getString(8));
	           sqlRetorno.put("TIP_CONT",(Long) new Long(rs3.getLong(1)));
	           sqlRetorno.put("RUT_ROL",(Long) new Long(rs3.getLong(2)));
	           //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rs3.getDate(5));
	           sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs3.getString(10));

	           vDiaVenc = rs3.getInt(11);
	           vMesVenc = rs3.getInt(12);
	           vAgnoVenc = rs3.getInt(13);

	           grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	           vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	           sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	           retorno.addElement((HashMap) sqlRetorno);
	         }
	       }
	       rs3.close();
	       rs3=null;
	//TERMINA  exclusionesArt192Generales

	//LLAMA A exclusionesCondonacionAgno (para formularios 30) PARA RECUPRERAR ResultSet

	       HashMap ParametrosexclusionesCondonacionAgno = new HashMap();

	       ParametrosexclusionesCondonacionAgno.put("ID_PERSONAS",ID_PERSONAS);
	       ParametrosexclusionesCondonacionAgno.put("RUT_CONTR",RUT_CONTR);

	       ResultSet rsexclusionesCondonacionAgno =(ResultSet) exclusionesCondonacionAgno((HashMap) ParametrosexclusionesCondonacionAgno);

	       while(rsexclusionesCondonacionAgno.next())
	       {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesCondonacionAgno.getLong(1)));
	         sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesCondonacionAgno.getLong(2)));
	         sqlRetorno.put("EFECTO",(String) rsexclusionesCondonacionAgno.getString(3));
	         sqlRetorno.put("TIPO",(String) rsexclusionesCondonacionAgno.getString(4));
	         sqlRetorno.put("DESCRIPCION",(String) rsexclusionesCondonacionAgno.getString(5));
	         sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesCondonacionAgno.getLong(6)));
	         sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesCondonacionAgno.getLong(7)));
	         //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rsexclusionesCondonacionAgno.getDate(8));
	         sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesCondonacionAgno.getString(9));

	         vDiaVenc = rsexclusionesCondonacionAgno.getInt(10);
	         vMesVenc = rsexclusionesCondonacionAgno.getInt(11);
	         vAgnoVenc = rsexclusionesCondonacionAgno.getInt(12);

	         grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	         vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	         sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	         retorno.addElement((HashMap) sqlRetorno);
	       }
	       rsexclusionesCondonacionAgno.close();
	       rsexclusionesCondonacionAgno=null;
	//TERMINA  exclusionesCondonacionAgno

	//LLAMA A exclusionesCondonacionAgnoP1 PARA RECUPRERAR ResultSet

	       HashMap ParametrosexclusionesCondonacionAgnoP1 = new HashMap();

	       ParametrosexclusionesCondonacionAgnoP1.put("ID_PERSONAS",ID_PERSONAS);
	       ParametrosexclusionesCondonacionAgnoP1.put("RUT_CONTR",RUT_CONTR);

	       ResultSet rsexclusionesCondonacionAgnoP1 =(ResultSet) exclusionesCondonacionAgnoP1((HashMap) ParametrosexclusionesCondonacionAgnoP1);

	       while(rsexclusionesCondonacionAgnoP1.next())
	       {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesCondonacionAgnoP1.getLong(1)));
	         sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesCondonacionAgnoP1.getLong(2)));
	         sqlRetorno.put("EFECTO",(String) rsexclusionesCondonacionAgnoP1.getString(3));
	         sqlRetorno.put("TIPO",(String) rsexclusionesCondonacionAgnoP1.getString(4));
	         sqlRetorno.put("DESCRIPCION",(String) rsexclusionesCondonacionAgnoP1.getString(5));
	         sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesCondonacionAgnoP1.getLong(6)));
	         sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesCondonacionAgnoP1.getLong(7)));
	         //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rsexclusionesCondonacionAgnoP1.getDate(8));
	         sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesCondonacionAgnoP1.getString(9));

	         vDiaVenc = rsexclusionesCondonacionAgnoP1.getInt(10);
	         vMesVenc = rsexclusionesCondonacionAgnoP1.getInt(11);
	         vAgnoVenc = rsexclusionesCondonacionAgnoP1.getInt(12);

	         grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	         vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	         sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	         retorno.addElement((HashMap) sqlRetorno);
	       }
	       rsexclusionesCondonacionAgnoP1.close();
	       rsexclusionesCondonacionAgnoP1=null;
	//TERMINA  exclusionesCondonacionAgnoP1

	//LLAMA A exclusionesCondonacionAgnoP2 PARA RECUPRERAR ResultSet
	       HashMap ParametrosexclusionesCondonacionAgnoP2 = new HashMap();

	       ParametrosexclusionesCondonacionAgnoP2.put("ID_PERSONAS",ID_PERSONAS);
	       ParametrosexclusionesCondonacionAgnoP2.put("RUT_CONTR",RUT_CONTR);

	       ResultSet rsexclusionesCondonacionAgnoP2 =(ResultSet) exclusionesCondonacionAgnoP2((HashMap) ParametrosexclusionesCondonacionAgnoP2);

	       while(rsexclusionesCondonacionAgnoP2.next())
	       {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesCondonacionAgnoP2.getLong(1)));
	         sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesCondonacionAgnoP2.getLong(2)));
	         sqlRetorno.put("EFECTO",(String) rsexclusionesCondonacionAgnoP2.getString(3));
	         sqlRetorno.put("TIPO",(String) rsexclusionesCondonacionAgnoP2.getString(4));
	         sqlRetorno.put("DESCRIPCION",(String) rsexclusionesCondonacionAgnoP2.getString(5));
	         sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesCondonacionAgnoP2.getLong(6)));
	         sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesCondonacionAgnoP2.getLong(7)));
	         //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rsexclusionesCondonacionAgnoP2.getDate(8));
	         sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesCondonacionAgnoP2.getString(9));

	         vDiaVenc = rsexclusionesCondonacionAgnoP2.getInt(10);
	         vMesVenc = rsexclusionesCondonacionAgnoP2.getInt(11);
	         vAgnoVenc = rsexclusionesCondonacionAgnoP2.getInt(12);

	         grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	         vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	         sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	         retorno.addElement((HashMap) sqlRetorno);
	       }
	       rsexclusionesCondonacionAgnoP2.close();
	       rsexclusionesCondonacionAgnoP2=null;

	//TERMINA  exclusionesCondonacionAgnoP2

	//LLAMA A exclusionesRMHfiscales PARA RECUPRERAR ResultSet

	/*SE DEPRECA ESTA EXCLUSION 22/07/2015  OJO CBT, CLH, CBOCA, CCC*/       
	     /*  HashMap ParametrosexclusionesRMHfiscales = new HashMap();
           
	       java.util.Date FechaActual;
	       java.util.Date Limite_Inferior;
	       java.util.Date Limite_Superior;

	       ParametrosexclusionesRMHfiscales.put("ID_PERSONAS",ID_PERSONAS);
	       ParametrosexclusionesRMHfiscales.put("RUT_CONTR",RUT_CONTR);
            
	       ResultSet rsexclusionesRMHfiscales =(ResultSet) exclusionesRMHfiscales((HashMap) ParametrosexclusionesRMHfiscales);
	      // System.out.println("llego a exclusionesRMHfiscales--->");
	       
	       while(rsexclusionesRMHfiscales.next())
	       {
	    	  // System.out.println("llego a exclusionesRMHfiscales--->");

	         FechaActual=(java.util.Date) rsexclusionesRMHfiscales.getDate(10);
	         Limite_Inferior=(java.util.Date) rsexclusionesRMHfiscales.getDate(11);
	         Limite_Superior=(java.util.Date) rsexclusionesRMHfiscales.getDate(12);
              

	         if ((Limite_Inferior.compareTo(FechaActual)==1) || (Limite_Inferior.compareTo(FechaActual)==0)|| (Limite_Superior.compareTo(FechaActual)==-1))
	         {
	        	// System.out.println("ingreso al seteo-------->");
	           HashMap sqlRetorno = new HashMap();

	           sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesRMHfiscales.getLong(1)));
	           sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesRMHfiscales.getLong(2)));
	           sqlRetorno.put("EFECTO",(String) rsexclusionesRMHfiscales.getString(3));
	           sqlRetorno.put("TIPO",(String) rsexclusionesRMHfiscales.getString(4));
	           sqlRetorno.put("DESCRIPCION",(String) rsexclusionesRMHfiscales.getString(5));
	           sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesRMHfiscales.getLong(6)));
	           sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesRMHfiscales.getLong(7)));
	           //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rsexclusionesRMHfiscales.getDate(8));
	           sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesRMHfiscales.getString(13));

	           vDiaVenc = rsexclusionesRMHfiscales.getInt(14);
	           vMesVenc = rsexclusionesRMHfiscales.getInt(15);
	           vAgnoVenc = rsexclusionesRMHfiscales.getInt(16);

	           grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	           vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	           sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	           retorno.addElement((HashMap) sqlRetorno);
	         }
	       }
	       rsexclusionesRMHfiscales.close();
	       rsexclusionesRMHfiscales=null;*/

	//TERMINA  exclusionesCondonacionAgno

	//LLAMA A exclusionesFormularios PARA RECUPRERAR ResultSet
	       
	       /*SE DEPRECA 22/07/2015  CBT, CBOCA, CCC, CLH*/

	      /* HashMap ParametrosexclusionesFormularios = new HashMap();

	       ParametrosexclusionesFormularios.put("ID_PERSONAS",ID_PERSONAS);

	       ResultSet rsexclusionesexclusionesFormularios =(ResultSet) exclusionesFormularios((HashMap) ParametrosexclusionesFormularios);

	       while(rsexclusionesexclusionesFormularios.next())
	       {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesexclusionesFormularios.getLong(1)));
	         sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesexclusionesFormularios.getLong(2)));
	         sqlRetorno.put("EFECTO",(String) rsexclusionesexclusionesFormularios.getString(3));
	         sqlRetorno.put("TIPO",(String) rsexclusionesexclusionesFormularios.getString(4));
	         sqlRetorno.put("DESCRIPCION",(String) rsexclusionesexclusionesFormularios.getString(5));
	         sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesexclusionesFormularios.getLong(6)));
	         sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesexclusionesFormularios.getLong(7)));
	         //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rsexclusionesexclusionesFormularios.getDate(8));
	         sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesexclusionesFormularios.getString(9));

	         vDiaVenc = rsexclusionesexclusionesFormularios.getInt(10);
	         vMesVenc = rsexclusionesexclusionesFormularios.getInt(11);
	         vAgnoVenc = rsexclusionesexclusionesFormularios.getInt(12);

	         grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	         vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	         sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	         retorno.addElement((HashMap) sqlRetorno);
	       }

	       rsexclusionesexclusionesFormularios.close();
	       rsexclusionesexclusionesFormularios=null;*/
	//TERMINA  exclusionesFormularios

	       HashMap ParametrosexclusionesRemate = new HashMap();


	        ResultSet rsexclusionesRemate =(ResultSet) exclusionesRemateRut(new Long(ID_PERSONAS).longValue());

	        while(rsexclusionesRemate.next())
	        {
	          HashMap sqlRetorno = new HashMap();

	          sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesRemate.getLong(1)));
	          sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesRemate.getLong(2)));
	          sqlRetorno.put("EFECTO",(String) rsexclusionesRemate.getString(3));
	          sqlRetorno.put("TIPO",(String) rsexclusionesRemate.getString(4));
	          sqlRetorno.put("DESCRIPCION",(String) rsexclusionesRemate.getString(5));
	          sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesRemate.getLong(6)));
	          sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesRemate.getLong(7)));
	          sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesRemate.getDate(8));
	          sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesRemate.getString(9));

	         vDiaVenc = rsexclusionesRemate.getInt(10);
	         vMesVenc = rsexclusionesRemate.getInt(11);
	         vAgnoVenc = rsexclusionesRemate.getInt(12);

	         grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	         vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	         sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	          retorno.addElement((HashMap) sqlRetorno);
	        }

	        rsexclusionesRemate.close();
	        rsexclusionesRemate=null;

	      }

	       //System.out.println("devuelvo retorno--------> "+retorno.size());
	      return retorno;
	    }
	    catch(SQLException e) {
	      e.printStackTrace();
	      //System.out.println("ingreso result bd 9-------->");
	      throw new EJBException("Error ejecutando el SQL "+"<br>ID_PERSONAS ="+ID_PERSONAS+"<br>RUT_CONTR ="+RUT_CONTR +"<br>"+ e.toString());
	     }
	     finally
	     {
	      this.closeConnection();
	      try {
	        conn.close();
	      }
	      catch (SQLException exs) {
		        System.out.print("error sql");
			       exs.printStackTrace();
	      }
	      catch (Exception ex) {
	       // throw new EJBException("Error "+ ex.printStackTrace());
	        System.out.print("error");
	       ex.printStackTrace();
	      }
	     }
	  }




   /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * Obtiene las Exclusiones y verifica deudas excluidas
	 * @ID_PERSONAS  parametros Contiene id personas para ingresar en la consulta
	 * @RUT_CONTR  parametros Contiene rut contribuyente para ingresar en la consulta
	 * @deudaWeb parametros Contiene clave de la deuda
	 * @return Listado de Exclusiones
	 */
	public DeudaWeb getVerificaExclusion(long ID_PERSONAS,long RUT_CONTR,DeudaWeb deudaWeb,long Perfil)
	{
     //System.out.println("*************************************INGRESO A CODIGO**********************************");
     System.out.println("");
	String Convenio="S";
	String Condonacion="S";


	Long FOLIO_R  = new Long(0);
	Long FOLIO_SII_R  = new Long(0);
	Long TIP_FORM_R  = new Long(0);
	String EFECTO_R  = "";
	String TIPO_R  = "";
	String DESCRIPCION_R  = "";
	Long TIP_CONT_R  = new Long(0);
	Long RUT_ROL_R  = new Long(0);
	java.sql.Date FECHA_VENCIMIENTO_R;
	Long cod_origen = new Long(0);
	Long cod_efecto = new Long(0);
	Long VCOD_TIPO_EXCLUSION = new Long(0);
	boolean VerificaRxclusionAgnoCalendario=false;

	if (ID_PERSONAS!=0 && RUT_CONTR!=0)
	{

	    if (xconsultarExclusiones_R == null || ((Long) Parametros_Exclusiones.get("IDPERSONA")).longValue() != ID_PERSONAS || (Perfil == 12))
	    {
	      Parametros_Exclusiones = new HashMap();

	      Parametros_Exclusiones.put("IDPERSONA",new Long(ID_PERSONAS));
	      Parametros_Exclusiones.put("RUT",new Long(RUT_CONTR));
	      Parametros_Exclusiones.put("Perfil",new Long(Perfil));

	      xconsultarExclusiones_R = new Vector();
	      xconsultarExclusiones_R.clear();
	      xconsultarExclusiones_R.addAll((Collection) getconsultarExclusiones((HashMap) Parametros_Exclusiones));
	    }

	    if (!xconsultarExclusiones_R.isEmpty())
	    {
	      int contador=0;
	      Iterator it = xconsultarExclusiones_R.iterator();

	      while (it.hasNext())
	      {
	        HashMap x = (HashMap) it.next();

	        VCOD_TIPO_EXCLUSION = (Long) x.get("COD_TIPO_EXCLUSION");
	        FOLIO_R = (Long) x.get("FOLIO");
	        FOLIO_SII_R = (Long) x.get("FOLIO_SII");
	        TIP_FORM_R = (Long) x.get("TIP_FORM");
	        EFECTO_R = (String) x.get("EFECTO");
	        TIPO_R = (String) x.get("TIPO");
	        DESCRIPCION_R = (String) x.get("DESCRIPCION");
	        TIP_CONT_R = (Long) x.get("TIP_CONT");
	        RUT_ROL_R = (Long) x.get("RUT_ROL");
	        FECHA_VENCIMIENTO_R = (java.sql.Date) x.get("FECHA_VENCIMIENTO");
	        
	       
	        //System.out.println("folio---->"+FOLIO_R);
	        //System.out.println("folio_SII---->"+FOLIO_SII_R);
	        

	        
	        if (x.get("COD_ORIGEN_EXCLUSION") != null) {
	          cod_origen = (Long) x.get("COD_ORIGEN_EXCLUSION");
	        }
	        else {
	          cod_origen = new Long(0);
	        }

	        if (x.get("COD_EFECTO") != null) {
	          cod_efecto = (Long) x.get("COD_EFECTO");
	        }
	        else {
	          cod_efecto = new Long(0);
	        }
	        
	        if ((cod_origen.longValue()==1) &&((VCOD_TIPO_EXCLUSION.intValue()==2)||(VCOD_TIPO_EXCLUSION.intValue()==3)) ){
	        	FOLIO_R=FOLIO_SII_R;
	        }

	      
	   
	        /*System.out.println("folio-nuevo--->"+FOLIO_R);
	        System.out.println("cod_origen--->"+cod_origen);
	        System.out.println("cod_efecto--->"+cod_efecto);
	        System.out.println("EFECTO_R--->"+EFECTO_R);
	        System.out.println("TIPO_R--->"+TIPO_R);
	       // System.out.println("getOrigen--->"+deudaWeb.getOrigen());
	       // System.out.println("VCOD_TIPO_EXCLUSION--->"+VCOD_TIPO_EXCLUSION.intValue());
	        
	        System.out.println("<----------------------------------------->");*/
	      
	        DeudaWeb Deudas = new DeudaWeb( (TIP_CONT_R).intValue(),
	                                        (TIP_FORM_R).intValue(),
	                                        (RUT_ROL_R).longValue(),
	                                        (FOLIO_R).longValue(),
	                                         FECHA_VENCIMIENTO_R);
	       /*
	       System.out.println("<----------------------------------------->");
	        System.out.println("getTipoContribuyente--->"+Deudas.getTipoContribuyente());
	        System.out.println("getTipoFormulario--->"+Deudas.getTipoFormulario());
	        System.out.println("getRutRol--->"+Deudas.getRutRol());
	        System.out.println("getFolio--->"+Deudas.getFolio());
	        System.out.println("getFechaVencimiento--->"+Deudas.getFechaVencimiento());
	        System.out.println("------deudaWeb----");
	        System.out.println("getTipoContribuyente--->"+deudaWeb.getTipoContribuyente());
	        System.out.println("getTipoFormulario--->"+deudaWeb.getTipoFormulario());
	        System.out.println("getRutRol--->"+deudaWeb.getRutRol());
	        System.out.println("getFolio--->"+deudaWeb.getFolio());
	        System.out.println("getFechaVencimiento--->"+deudaWeb.getFechaVencimiento());
	        System.out.println("<------------------------------------------>");
	       */
	        

	        //if ((cod_origen.longValue()==1) &&((TIPO_R.equals("2"))||(TIPO_R.equals("3"))) )
	        if ((cod_origen.longValue()==1) &&((VCOD_TIPO_EXCLUSION.intValue()==2)||(VCOD_TIPO_EXCLUSION.intValue()==3)) )
	        {	//System.out.println("entro 1 folio--->"+FOLIO_R);
	          if((deudaWeb.getRutRol() == Deudas.getRutRol())&&(deudaWeb.getFolio()==Deudas.getFolio()))
	          { //System.out.println("entro 2 folio--->"+FOLIO_R);
	            Condonacion = "N";
	          }
	        }
	      
	       //System.out.println("**************ANTES DECOMPARAR****************");
	        //if (deudaWeb.equals(Deudas)) 
	        
	        if (Deudas.getTipoContribuyente()==deudaWeb.getTipoContribuyente()&&
        		Deudas.getTipoFormulario()==deudaWeb.getTipoFormulario()&&
        		Deudas.getRutRol()==deudaWeb.getRutRol()&&
        		Deudas.getFolio()==deudaWeb.getFolio()&&
        		Deudas.getFechaVencimiento().compareTo(deudaWeb.getFechaVencimiento())==0
	        )
	        {
	        	 //System.out.println("**************DESPUES DECOMPARAR****************");
	        	//System.out.println("entro 3 folio--->"+FOLIO_R);
	          if (cod_origen.longValue() == 1) {
	        	  //System.out.println("entro 4 folio--->"+FOLIO_R);
	            if (!deudaWeb.getOrigen().equals("M"))
	            {	//System.out.println("entro 5 folio--->"+FOLIO_R);
	              if (cod_efecto.longValue() == 1) {
	            	  //System.out.println("entro 6 folio--->"+FOLIO_R);
	                if (!Convenio.equals("N")) {
	                	//System.out.println("entro 7 folio--->"+FOLIO_R);
	                  Convenio = "S";
	                }
	               // System.out.println("entro 8 folio--->"+FOLIO_R);
	                Condonacion = "N";

	              }

	              if (cod_efecto.longValue() == 2) {
	            	 // System.out.println("entro 10 folio--->"+FOLIO_R);
	                Convenio = "N";
	                if (!Condonacion.equals("N")) {
	                	//System.out.println("entro 11 folio--->"+FOLIO_R);
	                  Condonacion = "S";
	                }
	              }

	              if (cod_efecto.longValue() == 3) {
	            	//  System.out.println("entro7 folio--->"+FOLIO_R);
	                Convenio = "N";
	                Condonacion = "N";
	              }
	            }
	          }
	          else {
	        	 // System.out.println("entro a otro origen exclusion");
	            if (!deudaWeb.getOrigen().equals("M"))
	            {
	              // RMH TIPO IMPUESTO //fExclusiones_Formulario
	              if (TIPO_R.equals("CONDONACI�N ANUAL CON A-R")) {
	                if (EFECTO_R.equals("NO CONDONA MULTAS NI INTERESES")) {
	                  if (!Convenio.equals("N")) {
	                    Convenio = "S";
	                  }
	                  Condonacion = "N";
	                }
	              }
	              /*package Exclusiones_Conconacion_Ano*/
	              if (TIPO_R.equals("NO CONDONACI�N POR A�O CALENDARIO")) {
	                if (EFECTO_R.equals("NO CONDONA MULTAS NI INTERESES")) {
	                  if (!Convenio.equals("N")) {
	                    Convenio = "S";
	                  }

	                  Condonacion = "N";

	                }
	              }

	              /*package Exclusionesconvcaducado*/

	              /*package Exclusiones_Generales_Rmh*/

	              if (EFECTO_R.equals("NO CONVENIO")) {
	                Convenio = "N";
	                if (!Condonacion.equals("N")) {
	                  Condonacion = "S";
	                }
	              }

	              if (EFECTO_R.equals("NO CONDONACION")) {
	                if (!Convenio.equals("N")) {
	                  Convenio = "S";
	                }
	                Condonacion = "N";
	              }

	              if (EFECTO_R.equals("NO EXCLUYE CONVENIO ,NO EXCLUYE CONDONACION")) {
	                if (!Convenio.equals("N")) {
	                  Convenio = "S";
	                }
	                Condonacion = "N";
	              }

	              /*package Exclusiones_Rmh*/
	              if (EFECTO_R.equals("CONDONA SOLO INTERES")) {
	                if (!Convenio.equals("N")) {
	                  Convenio = "S";
	                }
	                if (!Condonacion.equals("N")) {
	                  Condonacion = "S";
	                }
	              }

	              if (EFECTO_R.equals("CONDONA SOLO MULTAS")) {
	                if (!Convenio.equals("N")) {
	                  Convenio = "S";
	                }
	                if (!Condonacion.equals("N")) {
	                  Condonacion = "S";
	                }
	              }

	              /*package Exclusiones_Rmh_Fiscal*/
	              if (EFECTO_R.equals("NO CONDONACI�N POR ESTAR FUERA DE PLAZO")) {
	                if (!Convenio.equals("N")) {
	                  Convenio = "S";
	                }
	                Condonacion = "N";
	              }
	            }

	            if (EFECTO_R.equals("NO CONVENIO,NO CONDONACI�N")) {
	              Convenio = "N";
	              Condonacion = "N";
	            }

	            if (EFECTO_R.equals("NO CONVENIO ,NO CONDONACION")) {
	              Convenio = "N";
	              Condonacion = "N";
	            }

	          } //termina if que verifica deuda

	        } //termina if
	      }
	    }//termina while

	   /* if ((deudaWeb.getCondonacionAplicadaA().equals("N"))&&(Perfil<5))
	    {
	      Condonacion="N";
	    }*/
	    
	    if ((deudaWeb.getDerechoCondonacion().equals("N"))&&(Perfil<5))
	    {
	      Condonacion="N";
	    } 
	    
       // System.out.println("ojo esto no esta en ciclo");
	    /*deudaWeb.setPosibilidadConvenio(Convenio);
	    deudaWeb.setPosibilidadCondonacion(Condonacion);
	    deudaWeb.setExclusion(VerificaRxclusionAgnoCalendario);*/
        /*seteo para reforma tributaria*/
	 	/*deudaWeb.setDerechoConvenio(Convenio); 
	 	deudaWeb.setDerechoCondonacion(Condonacion);*/
	 	
	 	deudaWeb.setExclusion(VerificaRxclusionAgnoCalendario);
	 	if (!deudaWeb.getDerechoConvenio().equals("N")){
	 		deudaWeb.setPosibilidadConvenio(Convenio);
	 		deudaWeb.setDerechoConvenio(Convenio); 
	 	}
	 	
	 	if (!deudaWeb.getDerechoCondonacion().equals("N")){
	 		 deudaWeb.setPosibilidadCondonacion(Condonacion);
	 		 deudaWeb.setDerechoCondonacion(Condonacion);
	 	}
	
	    return deudaWeb;
	}
	else
	{
	  if (this.xconsultarExclusiones_R != null)
	  {
	    this.xconsultarExclusiones_R.clear();
	  }
	  this.xconsultarExclusiones_R = null;
	  return null;
	}
	}

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * Obtiene las Exclusiones y verifica rol
	 * @Rol  parametros Contiene Rol para ingresar en la consulta
	 * @return  Listado de Exclusiones
	 */
	public Collection getVerificaExclusionRol(String Rol)
	{
	  PreparedStatement ps = null;
	  
	  Vector retorno = new Vector();
	  String vRol="";
	  Long vPerfil = new Long(0);

	  String[] arrRolPerfil = StringExt.split(Rol,",");
	  vRol = arrRolPerfil[0];

	  if (arrRolPerfil.length >1)
	  {
	    if ((!arrRolPerfil[1].equals(""))&&(arrRolPerfil[1]!=null))
	    {
	      vPerfil = new Long(arrRolPerfil[1]);
	    }
	  }

	  try {
	    conn = this.getConnection();
	    //System.out.println("Perfil exclusion rol:"+vPerfil);
	    if (vPerfil.longValue() <5)
	    {
	      //LLAMA A exclusionesCondonacionAgno PARA RECUPRERAR ResultSet
	      HashMap ParametrosexclusionesCondonacionAgno = new HashMap();

	      ParametrosexclusionesCondonacionAgno.put("ID_PERSONAS","-1");
	      ParametrosexclusionesCondonacionAgno.put("RUT_CONTR",vRol);

	      ResultSet rsexclusionesCondonacionAgno =(ResultSet) exclusionesCondonacionAgnoF30((HashMap) ParametrosexclusionesCondonacionAgno);

	      while(rsexclusionesCondonacionAgno.next())
	      {
	        HashMap sqlRetorno = new HashMap();

	        sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesCondonacionAgno.getLong(1)));
	        sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesCondonacionAgno.getLong(2)));
	        sqlRetorno.put("EFECTO",(String) rsexclusionesCondonacionAgno.getString(3));
	        sqlRetorno.put("TIPO",(String) rsexclusionesCondonacionAgno.getString(4));
	        sqlRetorno.put("DESCRIPCION",(String) rsexclusionesCondonacionAgno.getString(5));
	        sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesCondonacionAgno.getLong(6)));
	        sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesCondonacionAgno.getLong(7)));
	        sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesCondonacionAgno.getDate(8));
	        //sqlRetorno.put("FECHA_VENCIMIENTO",(String) rsexclusionesCondonacionAgno.getString(8));

	        retorno.addElement((HashMap) sqlRetorno);
	      }
	      rsexclusionesCondonacionAgno.close();
	      rsexclusionesCondonacionAgno=null;
	//TERMINA  exclusionesCondonacionAgno

	//LLAMA A exclusionesCondonacionAgno PARA RECUPRERAR ResultSet

	      HashMap ParametrosexclusionesPropuestConActivo = new HashMap();

	      ParametrosexclusionesPropuestConActivo.put("ID_PERSONAS","-1");
	      ParametrosexclusionesPropuestConActivo.put("RUT_CONTR",vRol);

	      ResultSet rsexclusionesPropuestConActivo =(ResultSet) exclusionesPropuestConActivo((HashMap) ParametrosexclusionesCondonacionAgno);

	      while(rsexclusionesPropuestConActivo.next())
	      {
	        HashMap sqlRetorno = new HashMap();

	        sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesPropuestConActivo.getLong(1)));
	        sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesPropuestConActivo.getLong(2)));
	        sqlRetorno.put("EFECTO",(String) rsexclusionesPropuestConActivo.getString(3));
	        sqlRetorno.put("TIPO",(String) rsexclusionesPropuestConActivo.getString(4));
	        sqlRetorno.put("DESCRIPCION",(String) rsexclusionesPropuestConActivo.getString(5));
	        sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesPropuestConActivo.getLong(6)));
	        sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesPropuestConActivo.getLong(7)));
	        sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesPropuestConActivo.getDate(8));

	        retorno.addElement((HashMap) sqlRetorno);
	      }
	      rsexclusionesPropuestConActivo.close();
	      rsexclusionesPropuestConActivo=null;
	//TERMINA  exclusionesPropuestConActivo


	//Luis 10/11/2003

	//LLAMA A exclusionesFormularios PARA RECUPRERAR ResultSet
	      /*SE DEPRECA 22/07/2015  CBT, CBOCA, CCC, CLH*/
	      
	     /* ResultSet rsexclusionesexclusionesFormularios =(ResultSet) exclusionesFormulariosROL(vRol);

	      while(rsexclusionesexclusionesFormularios.next())
	      {
	        HashMap sqlRetorno = new HashMap();

	        sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesexclusionesFormularios.getLong(1)));
	        sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesexclusionesFormularios.getLong(2)));
	        sqlRetorno.put("EFECTO",(String) rsexclusionesexclusionesFormularios.getString(3));
	        sqlRetorno.put("TIPO",(String) rsexclusionesexclusionesFormularios.getString(4));
	        sqlRetorno.put("DESCRIPCION",(String) rsexclusionesexclusionesFormularios.getString(5));
	        sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesexclusionesFormularios.getLong(6)));
	        sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesexclusionesFormularios.getLong(7)));
	        sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesexclusionesFormularios.getDate(8));
	        sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesexclusionesFormularios.getString(9));

	        retorno.addElement((HashMap) sqlRetorno);
	      }

	      rsexclusionesexclusionesFormularios.close();
	      rsexclusionesexclusionesFormularios=null;*/
	//TERMINA  exclusionesFormularios

	//Fin Luis 10/11/2003

	      HashMap ParametrosexclusionesRemate = new HashMap();

	       ParametrosexclusionesRemate.put("ID_PERSONAS","-1");
	       ParametrosexclusionesRemate.put("RUT_CONTR",vRol);

	       ResultSet rsexclusionesRemate =(ResultSet) exclusionesRemate((HashMap) ParametrosexclusionesRemate);

	       while(rsexclusionesRemate.next())
	       {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesRemate.getLong(1)));
	         sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesRemate.getLong(2)));
	         sqlRetorno.put("EFECTO",(String) rsexclusionesRemate.getString(3));
	         sqlRetorno.put("TIPO",(String) rsexclusionesRemate.getString(4));
	         sqlRetorno.put("DESCRIPCION",(String) rsexclusionesRemate.getString(5));
	         sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesRemate.getLong(6)));
	         sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesRemate.getLong(7)));
	         sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesRemate.getDate(8));
	         sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesRemate.getString(9));

	         retorno.addElement((HashMap) sqlRetorno);
	       }
	       rsexclusionesRemate.close();
	       rsexclusionesRemate=null;


	    }

	    return retorno;

	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL "+"<br>getVerificaRol"+ e.toString());
	     }
	finally
	     {
	       this.closeConnection();
	      try {
	        conn.close();
	      }
	      catch (SQLException ex) {
	      }
	     }
	}


	   private Collection getVerificaExclusionRolConvAct(String Rol)
	   {
	     PreparedStatement ps = null;
	     Vector retorno = new Vector();


	     try {

	       conn = this.getConnection();

	//LLAMA A exclusionesCondonacionAgno PARA RECUPRERAR ResultSet

	       HashMap ParametrosexclusionesPropuestConActivo = new HashMap();

	       ParametrosexclusionesPropuestConActivo.put("ID_PERSONAS","-1");
	       ParametrosexclusionesPropuestConActivo.put("RUT_CONTR",Rol);

	       ResultSet rsexclusionesPropuestConActivo =(ResultSet) exclusionesPropuestConActivo((HashMap) ParametrosexclusionesPropuestConActivo);

	       while(rsexclusionesPropuestConActivo.next())
	       {
	         HashMap sqlRetorno = new HashMap();

	         sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesPropuestConActivo.getLong(1)));
	         sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesPropuestConActivo.getLong(2)));
	         sqlRetorno.put("EFECTO",(String) rsexclusionesPropuestConActivo.getString(3));
	         sqlRetorno.put("TIPO",(String) rsexclusionesPropuestConActivo.getString(4));
	         sqlRetorno.put("DESCRIPCION",(String) rsexclusionesPropuestConActivo.getString(5));
	         sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesPropuestConActivo.getLong(6)));
	         sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesPropuestConActivo.getLong(7)));
	         sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesPropuestConActivo.getDate(8));

	         retorno.addElement((HashMap) sqlRetorno);
	       }

	       rsexclusionesPropuestConActivo.close();
	       rsexclusionesPropuestConActivo=null;
	//TERMINA  exclusionesPropuestConActivo

	       return retorno;

	       }
	       catch(SQLException e) {
	         throw new EJBException("Error ejecutando el SQL "+"<br>getVerificaRol"+ e.toString());
	        }
	        finally
	        {
	          this.closeConnection();
	          try {
	            conn.close();
	          }
	          catch (SQLException ex) {
	          }
	        }

	   }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 *
	 * Obtiene las Exclusiones y verifica Roles
	 * @Rol  parametros Contiene Rol para ingresar en la consulta
	 * @deudaWeb parametros Contiene clave de la deuda
	 * @return Listado de Exclusiones
	 */

	public DeudaWeb getVerificaRoles(String Rol,DeudaWeb deudaWeb)
	{

	String Convenio="S";
	String Condonacion="S";

	Long FOLIO_R  = new Long(0);
	Long TIP_FORM_R  = new Long(0);
	String EFECTO_R  = "";
	String TIPO_R  = "";
	String DESCRIPCION_R  = "";
	Long TIP_CONT_R  = new Long(0);
	Long RUT_ROL_R  = new Long(0);
	java.sql.Date FECHA_VENCIMIENTO_R;

	String[] arrRolPerfil = StringExt.split(Rol,",");
	String vRol = arrRolPerfil[0];


	boolean VerificaRxclusionAgnoCalendario=false;
	int cont=0;
	if (!Rol.equals(""))
	{

	    if ((xtVerificaRoles_R == null) || (!this.Rol.equals(Rol)))
	    {
	      xtVerificaRoles_R= new Vector();
	      xtVerificaRoles_R.clear();
	      xtVerificaRoles_R.addAll((Collection) getVerificaExclusionRol((String) Rol));
	      this.Rol=vRol;

	    }

	    if (!xtVerificaRoles_R.isEmpty())
	    {
	      int contador=0;
	      Iterator it = xtVerificaRoles_R.iterator();

	      while (it.hasNext())
	      {
	        HashMap x = (HashMap) it.next();

	        FOLIO_R = (Long) x.get("FOLIO");
	        TIP_FORM_R = (Long) x.get("TIP_FORM");
	        EFECTO_R = (String) x.get("EFECTO");
	        TIPO_R = (String) x.get("TIPO");
	        DESCRIPCION_R = (String) x.get("DESCRIPCION");
	        TIP_CONT_R = (Long) x.get("TIP_CONT");
	        RUT_ROL_R = (Long) x.get("RUT_ROL");
	        FECHA_VENCIMIENTO_R = (java.sql.Date) x.get("FECHA_VENCIMIENTO");

	        DeudaWeb Deudas = new DeudaWeb( (TIP_CONT_R).intValue(),
	                                        (TIP_FORM_R).intValue(),
	                                        (RUT_ROL_R).longValue(),
	                                        (FOLIO_R).longValue(),
	                                        FECHA_VENCIMIENTO_R);


	        if (deudaWeb.equals(Deudas)) {

	          /*package Exclusiones_Conconacion_Ano*/
	          if (EFECTO_R.equals("NO CONDONA MULTAS NI INTERESES")) {
	            if (!Convenio.equals("N")) {
	              Convenio = "S";
	            }
	            VerificaRxclusionAgnoCalendario = true;
	            Condonacion = "N";
	          }

	          /*package Exclusiones_Rmh_Fiscal*/
	          if (EFECTO_R.equals("NO CONVENIO ,NO CONDONACION")) {
	            if (!Convenio.equals("N")) {
	              Convenio = "N";
	            }
	            Condonacion = "N";
	          }

	          //----Luis  10/11/2003  es muy parecida a la de abajo...
	          // RMH TIPO IMPUESTO //fExclusiones_Formulario
	          if (EFECTO_R.equals("NO CONDONA MULTAS NI INTERESES")) {
	            if (!Convenio.equals("N")) {
	              Convenio = "S";
	            }
	            Condonacion = "N";
	          }

	          //Fin ----Luis  10/11/2003

	          cont++;

	        } //termina if que verifica deuda

	      } //termina if

	    }//termina while

	/*   System.out.println("--------------TERMINO CONSULTA EXCLUSION POR ROL------------------------");*/
	   /* deudaWeb.setPosibilidadConvenio(Convenio);
	    deudaWeb.setPosibilidadCondonacion(Condonacion);
	    deudaWeb.setExclusion(VerificaRxclusionAgnoCalendario);
	 	deudaWeb.setDerechoConvenio(Convenio); 
	 	deudaWeb.setDerechoCondonacion(Condonacion);*/
	    
	 	deudaWeb.setExclusion(VerificaRxclusionAgnoCalendario);
	 	if (!deudaWeb.getDerechoConvenio().equals("N")){
	 		deudaWeb.setPosibilidadConvenio(Convenio);
	 		deudaWeb.setDerechoConvenio(Convenio); 
	 	}
	 	
	 	if (!deudaWeb.getDerechoCondonacion().equals("N")){
	 		 deudaWeb.setPosibilidadCondonacion(Condonacion);
	 		 deudaWeb.setDerechoCondonacion(Condonacion);
	 	}	    
	 	
	 	
	 	
	    return deudaWeb;
	}
	else
	{
	  if (this.xtVerificaRoles_R != null)
	  {
	    this.xtVerificaRoles_R.clear();
	  }
	  this.xtVerificaRoles_R = null;
	  return null;
	}
	}

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
   */    
	  public DeudaWeb getVerificaRolesConvAct(String Rol,DeudaWeb deudaWeb)
	  {

	    String Convenio="S";
	    String Condonacion="S";

	    Long FOLIO_R  = new Long(0);
	    Long TIP_FORM_R  = new Long(0);
	    String EFECTO_R  = "";
	    String TIPO_R  = "";
	    String DESCRIPCION_R  = "";
	    Long TIP_CONT_R  = new Long(0);
	    Long RUT_ROL_R  = new Long(0);
	    java.sql.Date FECHA_VENCIMIENTO_R;

	    boolean VerificaRxclusionAgnoCalendario=false;
	    int cont=0;
	    if (!Rol.equals(""))
	    {

	      if ((xtVerificaRoles_R == null) || (!this.Rol.equals(Rol)))
	      {
	        xtVerificaRoles_R= new Vector();
	        xtVerificaRoles_R.clear();
	        xtVerificaRoles_R.addAll((Collection) getVerificaExclusionRolConvAct((String) Rol));
	        this.Rol=Rol;

	      }

	      if (!xtVerificaRoles_R.isEmpty())
	      {
	        int contador=0;
	        Iterator it = xtVerificaRoles_R.iterator();

	        while (it.hasNext())
	        {
	          HashMap x = (HashMap) it.next();

	          FOLIO_R  = (Long) x.get("FOLIO");
	          TIP_FORM_R  = (Long) x.get("TIP_FORM");
	          EFECTO_R  = (String) x.get("EFECTO");
	          TIPO_R  = (String) x.get("TIPO");
	          DESCRIPCION_R  = (String) x.get("DESCRIPCION");
	          TIP_CONT_R  = (Long) x.get("TIP_CONT");
	          RUT_ROL_R  = (Long) x.get("RUT_ROL");
	          FECHA_VENCIMIENTO_R  = (java.sql.Date) x.get("FECHA_VENCIMIENTO");

	          DeudaWeb  Deudas=new DeudaWeb( (TIP_CONT_R).intValue(), (TIP_FORM_R).intValue(),(RUT_ROL_R).longValue(), (FOLIO_R).longValue(), FECHA_VENCIMIENTO_R);

	          if (deudaWeb.equals(Deudas))
	          {

	            /*package Exclusiones_Rmh_Fiscal*/
	            if (EFECTO_R.equals("NO CONVENIO ,NO CONDONACION")){
	              if (!Convenio.equals("N")){Convenio="N";}
	              Condonacion="N";}

	          }//termina if que verifica deuda

	        }//termina if
	      }//termina while


	    /*  deudaWeb.setPosibilidadConvenio(Convenio);
	      deudaWeb.setPosibilidadCondonacion(Condonacion);
	      deudaWeb.setExclusion(VerificaRxclusionAgnoCalendario);
		  deudaWeb.setDerechoConvenio(Convenio); 
		  deudaWeb.setDerechoCondonacion(Condonacion);*/
		  
		 	deudaWeb.setExclusion(VerificaRxclusionAgnoCalendario);
		 	if (!deudaWeb.getDerechoConvenio().equals("N")){
		 		deudaWeb.setPosibilidadConvenio(Convenio);
		 		deudaWeb.setDerechoConvenio(Convenio); 
		 	}
		 	
		 	if (!deudaWeb.getDerechoCondonacion().equals("N")){
		 		 deudaWeb.setPosibilidadCondonacion(Condonacion);
		 		 deudaWeb.setDerechoCondonacion(Condonacion);
		 	}
	      return deudaWeb;
	  }
	  else
	  {
	    if (this.xtVerificaRoles_R != null)
	    {
	      this.xtVerificaRoles_R.clear();
	    }
	    this.xtVerificaRoles_R = null;
	    return null;
	  }
	  }


	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 *
	 * Obtiene las Exclusiones y verifica Roles
	 * @Rol  parametros Contiene Rol para ingresar en la consulta
	 * @return Listado de Exclusiones
	 */
	public Collection getVerificaExclusionPorRol(String Rol)
	{

	    if (!Rol.equals("")) {

	      if ( (xtVerificaRoles_R == null) || (!this.Rol.equals(Rol))) {
	        xtVerificaRoles_R = new Vector();
	        xtVerificaRoles_R.clear();
	        xtVerificaRoles_R.addAll( (Collection) getVerificaExclusionRol( (
	            String) Rol));
	        this.Rol = Rol;
	      }

	      return xtVerificaRoles_R;
	    }
	    else {
	      if (this.xtVerificaRoles_R != null) {
	        this.xtVerificaRoles_R.clear();
	      }
	      this.xtVerificaRoles_R = null;

	      return null;
	    }


	}
	 private void closeConnection(){
	        try {
	            if (this.connection != null){
	                if (!this.connection.isClosed()){
	                    this.connection.close();
	                }
	                this.connection = null;
	            }
	        } catch (SQLException e) {
	            //System.out.println("Error GeneraPropuestaBean::closeConnection();");
	            throw new EJBException("Error GeneraPropuestaBean::closeConnection();"+e);
	        }
	    }


	  public Connection getConnection() throws SQLException {
	        if (this.connection == null) {
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
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	     * Obitene listado de Contribuyentes MYPE, excluidos por SII
	     * @param parametros
	     * @return
	     */
	public Collection getExcluidosMype(){
	      PreparedStatement ps = null;
	       Vector retorno = new Vector();
	      
	       
	       try {
	         conn = this.getConnection();

	         CallableStatement  call = conn.prepareCall("{? = call ConsultarExclusiones.ListarExclusionesMype()}");
	         call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	         call.execute();
	         ResultSet rs = (ResultSet) call.getObject(1);
	         while(rs.next())
	         {
	           HashMap sqlRetorno = new HashMap();

	           sqlRetorno.put("Rut",(Long) new Long(rs.getLong(1)));
	           sqlRetorno.put("Nombre",(String) rs.getString(2));
	           retorno.addElement((HashMap) sqlRetorno);
	         }
	         rs.close();
	         call.close();
	         return retorno;
	       }
	       catch (SQLException e) {
	         throw new EJBException("Error ejecutando el SQL Exclusiones MYPE");

	       }
	       finally{
	         try {
	           conn.close();
	         }
	         catch (SQLException ex) {
	         }
	       }

	     }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * Obtiene el listado de Exclusiones SII
	 * @param  parametros Contiene la lista de parametros para ingresar en la consulta
	 * @return Listado de Exclusiones SII
	 */
	 public Collection getconsultarExclusionesSII(HashMap parametros)
	  {
	
	    PreparedStatement ps = null;
	    Vector retorno = new Vector();
	    int cantidadPatametros = parametros.size();

	    String ID_PERSONAS="";
	    String RUT_CONTR ="";

	    int vDiaVenc=1;
	    int vMesVenc=0;
	    int vAgnoVenc=1900;
	    GregorianCalendar grFechaVenc=null;
	    java.sql.Date vFechaVenc=null;



	    try {
          conn = this.getConnection();
	      //conn = dataSource.getConnection();

	      CallableStatement  call = conn.prepareCall("{? = call ConsultarExclusiones.fConsultarExclusiones(?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      Long idpersona = (Long) parametros.get("IDPERSONA");
	      call.setLong(2, idpersona.longValue());

	      Long rut = (Long) parametros.get("RUT");
	      Long Perfil = (Long) parametros.get("Perfil");

	      ID_PERSONAS = idpersona.toString() ;
	      RUT_CONTR = rut.toString();

	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);


	      while(rs.next())
	      {
	                Long COD_TIPO_EXCLUSION=new Long(rs.getLong(2));
	                Long VFOLIO=new Long(rs.getLong(6));
	                Long VFolio_SII=new Long(rs.getLong(4));

	                if (((COD_TIPO_EXCLUSION.intValue()==2) || (COD_TIPO_EXCLUSION.intValue()==3)) && ((VFOLIO.intValue()!=0)  && (VFolio_SII.intValue()!=0)))
	                {

	                  HashMap sqlRetorno = new HashMap();

	                  sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	                  sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	                  sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	                  sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	                  //sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rs.getDate(5));
	                  sqlRetorno.put("FOLIO",(Long) new Long(rs.getLong(6)));
	                  sqlRetorno.put("TIP_FORM",(Long) new Long(rs.getLong(7)));
	                  sqlRetorno.put("RUT_ROL",(Long) new Long(rs.getLong(8)));
	                  sqlRetorno.put("TIP_CONT",(Long) new Long(rs.getLong(9)));
	                  sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	                  sqlRetorno.put("CAUSA",(String) rs.getString(11));
	                  sqlRetorno.put("EFECTO",(String) rs.getString(12));
	                  sqlRetorno.put("TIPO",(String) rs.getString(13));
	                  sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	                  sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));


	                  vDiaVenc = rs.getInt(16);
	                  vMesVenc = rs.getInt(17);
	                  vAgnoVenc = rs.getInt(18);

	                  grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                  vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                  sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                  String origen = (String) rs.getString(14);

	                  if (origen.equalsIgnoreCase("SII"))
	                    retorno.addElement( (HashMap) sqlRetorno);
	                }
	                else
	                {
	                  Long VTIPOFORMULARIO=new Long(0);
	                  Long VFOLIO_SII=new Long(rs.getLong(4));
	                  Long VFOLIOREC=new Long(rs.getLong(6));

	                  if (COD_TIPO_EXCLUSION.intValue()==2)
	                  {
	                    VTIPOFORMULARIO=new Long(21);
	                  }
	                  if (COD_TIPO_EXCLUSION.intValue()==3)
	                  {
	                    VTIPOFORMULARIO=new Long(45);
	                  }

	                  if (VFOLIOREC.intValue()>0 && VFOLIO_SII.intValue()==0)
	                  {
	                    VFOLIO_SII=VFOLIOREC;
	                  }
	                  else
	                  {
	                    if (VFOLIO_SII.intValue()>0 && VFOLIOREC.intValue()==0)
	                    {
	                      VFOLIO_SII=VFOLIO_SII;
	                    }
	                  }


	                  if ((COD_TIPO_EXCLUSION.intValue()==1)||
	                      (COD_TIPO_EXCLUSION.intValue()==4)||
	                      (COD_TIPO_EXCLUSION.intValue()==5)||
	                      (COD_TIPO_EXCLUSION.intValue()==6)||
	                      (COD_TIPO_EXCLUSION.intValue()==7)||
	                      (COD_TIPO_EXCLUSION.intValue()==8))
	                      {
	                        VFOLIO_SII=new Long(0);
	                        VTIPOFORMULARIO=new Long(0);
	                      }


	                      HashMap Parametros = new HashMap();

	                      Parametros.put("RutV",(Long) rut);
	                      Parametros.put("TipoFormulario",(Long) VTIPOFORMULARIO);
	                      Parametros.put("Folio",(Long) VFOLIO_SII);

	                      ResultSet resulsetCut_replica =(ResultSet) getbuscaCutReplica((HashMap) Parametros);

	                      while(resulsetCut_replica.next())
	                      {
	                        HashMap sqlRetorno = new HashMap();

	                        sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	                        sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	                        sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	                        sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	                        //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) resulsetCut_replica.getDate(1));
	                        sqlRetorno.put("FOLIO",(Long) new Long(resulsetCut_replica.getLong(2)));
	                        sqlRetorno.put("TIP_FORM",(Long) new Long(resulsetCut_replica.getLong(3)));
	                        sqlRetorno.put("RUT_ROL",(Long) new Long(resulsetCut_replica.getLong(4)));
	                        sqlRetorno.put("TIP_CONT",(Long) new Long(resulsetCut_replica.getLong(5)));
	                        sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	                        sqlRetorno.put("CAUSA",(String) rs.getString(11));
	                        sqlRetorno.put("EFECTO",(String) rs.getString(12));
	                        sqlRetorno.put("TIPO",(String) rs.getString(13));
	                        sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	                        sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));

	                        vDiaVenc = resulsetCut_replica.getInt(6);
	                        vMesVenc = resulsetCut_replica.getInt(7);
	                        vAgnoVenc = resulsetCut_replica.getInt(8);

	                        grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                        vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                        sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                        /*
	                         *Modifica cv_exclsiones_deudor segun campo encontrado
	                         *en cc_cut_replica
	                         */
	                        if ((COD_TIPO_EXCLUSION.intValue()==2) || (COD_TIPO_EXCLUSION.intValue()==3))
	                        {
	                          HashMap Mod_CVExclusiones = new HashMap();

	                          Mod_CVExclusiones.put("TIP_CONT",(Long) new Long(resulsetCut_replica.getLong(5)));
	                          Mod_CVExclusiones.put("RUT_ROL",(Long) new Long(resulsetCut_replica.getLong(4)));
	                          Mod_CVExclusiones.put("TIP_FORM",(Long) new Long(resulsetCut_replica.getLong(3)));
	                          Mod_CVExclusiones.put("FOLIO",(Long) new Long(resulsetCut_replica.getLong(2)));
	                          //Mod_CVExclusiones.put("FECHA_VENCIMIENTO",(String) resulsetCut_replica.getString(1));
	                          //Mod_CVExclusiones.put("FECHA_VENCIMIENTO",(java.util.Date) resulsetCut_replica.getDate(1));
	                          Mod_CVExclusiones.put("COD_EXCLUSION_DEUDOR",(Long) new Long(rs.getLong(1)));

	                          vDiaVenc = resulsetCut_replica.getInt(6);
	                          vMesVenc = resulsetCut_replica.getInt(7);
	                          vAgnoVenc = resulsetCut_replica.getInt(8);

	                          grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                          vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                          Mod_CVExclusiones.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                          getModif_CutRepli_CvExclusiones(Mod_CVExclusiones);//Llamada al m�todo modificaci�n cv_exclusiones_deudor
	                        }
	                        /*Termina*/
	                        String origen = (String) rs.getString(14);

	                        if (origen.equalsIgnoreCase("SII"))
	                        retorno.addElement((HashMap)  sqlRetorno);
	                      }
	/****Termina con Deudas Cut****/


	/****Comienza con deudas no Cut****/

	                      HashMap ParametrosDeudasNoCut = new HashMap();

	                      ParametrosDeudasNoCut.put("RutV",(Long) rut);
	                      ParametrosDeudasNoCut.put("TipoFormulario",(Long) VTIPOFORMULARIO);
	                      ParametrosDeudasNoCut.put("Folio",(Long) VFOLIO_SII);

	                      ResultSet resulsetDeudasNocut =(ResultSet) getbuscaDeudasNoCut((HashMap) ParametrosDeudasNoCut);

	                      while(resulsetDeudasNocut.next())
	                      {
	                        HashMap sqlRetorno = new HashMap();

	                        sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	                        sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	                        sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	                        sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	                        //sqlRetorno.put("FECHA_VENCIMIENTO",(String) resulsetDeudasNocut.getString(1));
	                        //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) resulsetDeudasNocut.getDate(1));
	                        sqlRetorno.put("FOLIO",(Long) new Long(resulsetDeudasNocut.getLong(2)));
	                        sqlRetorno.put("TIP_FORM",(Long) new Long(resulsetDeudasNocut.getLong(3)));
	                        sqlRetorno.put("RUT_ROL",(Long) new Long(resulsetDeudasNocut.getLong(4)));
	                        sqlRetorno.put("TIP_CONT",(Long) new Long(resulsetDeudasNocut.getLong(5)));
	                        sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	                        sqlRetorno.put("CAUSA",(String) rs.getString(11));
	                        sqlRetorno.put("EFECTO",(String) rs.getString(12));
	                        sqlRetorno.put("TIPO",(String) rs.getString(13));
	                        sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	                        sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));

	                        vDiaVenc = resulsetDeudasNocut.getInt(6);
	                        vMesVenc = resulsetDeudasNocut.getInt(7);
	                        vAgnoVenc = resulsetDeudasNocut.getInt(8);

	                        grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                        vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                        sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                        String origen = (String) rs.getString(14);

	                        if (origen.equalsIgnoreCase("SII"))
	                        retorno.addElement((HashMap)  sqlRetorno);
	                      }
	                      resulsetDeudasNocut.close();
	                      resulsetDeudasNocut=null;
	                    }//termina if
	                  }//termina while

	                  return retorno;
	                }
	                catch(SQLException e) {
	                  throw new EJBException("Error ejecutando el SQL "+"<br>ID_PERSONAS ="+ID_PERSONAS+"<br>RUT_CONTR ="+RUT_CONTR +"<br>"+ e.toString());
	                }
	                finally
	                {
	                  this.closeConnection();
	                  try {
	                    conn.close();
	                  }
	                  catch (SQLException ex) {
	                  }
	                }

	              }

	// ***********************************************************************************************************

	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * Obtiene el listado de Exclusiones Tesorer�a
	 * @param  parametros Contiene la lista de parametros para ingresar en la consulta
	 * @return Listado de Exclusiones Tesorer�a.
	 */
	 public Collection getconsultarExclusionesTesoreria(HashMap parametros)
	 {
	
	    PreparedStatement ps = null;
	    Vector retorno = new Vector();
	    int cantidadPatametros = parametros.size();
	    int vDiaVenc=1;
	    int vMesVenc=0;
	    int vAgnoVenc=1900;
	    GregorianCalendar grFechaVenc=null;
	    java.sql.Date vFechaVenc=null;

	    String ID_PERSONAS="";
	    String RUT_CONTR ="";

	    try {

	      conn = this.getConnection();

	      Long idpersona = (Long) parametros.get("IDPERSONA");
	      Long rut = (Long) parametros.get("RUT");
	      Long Perfil = (Long) parametros.get("Perfil");

	      ID_PERSONAS = idpersona.toString() ;
	      RUT_CONTR = rut.toString();

	//INGRESO LOGICA DE CONSULTAREXCLUSIONES.FCONSULTAEXCLUSIONES YA QUE NO EXISTE CCC Y CATA BUSTOS 15-05-2012
	      CallableStatement  call = conn.prepareCall("{? = call ConsultarExclusiones.fConsultarExclusiones(?)}");

	      call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	      call.setLong(2, idpersona.longValue());
	      call.execute();

	      ResultSet rs = (ResultSet) call.getObject(1);

	      Long VTIPOFORMULARIO=new Long(0);

	      while(rs.next())
	      {
	          Long COD_TIPO_EXCLUSION=new Long(rs.getInt(2));
	          Long VFOLIO=new Long(rs.getLong(6));
	          Long VFolio_SII=new Long(rs.getLong(4));
	          if (COD_TIPO_EXCLUSION.intValue()==2)
	          {
	            VTIPOFORMULARIO=new Long(21);
	          }
	          if (COD_TIPO_EXCLUSION.intValue()==3)
	          {
	            VTIPOFORMULARIO=new Long(45);
	          }
	          if ((COD_TIPO_EXCLUSION.intValue()==1)||
	              (COD_TIPO_EXCLUSION.intValue()==4)||
	              (COD_TIPO_EXCLUSION.intValue()==5)||
	              (COD_TIPO_EXCLUSION.intValue()==6)||
	              (COD_TIPO_EXCLUSION.intValue()==7)||
	              (COD_TIPO_EXCLUSION.intValue()==8))
	          {

	              VTIPOFORMULARIO=new Long(0);
	          }

	          if (((COD_TIPO_EXCLUSION.intValue()==2) || (COD_TIPO_EXCLUSION.intValue()==3)) && ((VFOLIO.longValue()!=0)  || (VFolio_SII.longValue()!=0)))
	          {

	            HashMap sqlRetorno = new HashMap();

	            sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	            sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	            sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	            sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	            //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) rs.getDate(5));
	            sqlRetorno.put("FOLIO",(Long) new Long(rs.getLong(6)));
	            sqlRetorno.put("TIP_FORM",VTIPOFORMULARIO);
	            sqlRetorno.put("RUT_ROL",(Long) new Long(rs.getLong(8)));
	            sqlRetorno.put("TIP_CONT",(Long) new Long(rs.getLong(9)));
	            sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	            sqlRetorno.put("CAUSA",(String) rs.getString(11));
	            sqlRetorno.put("EFECTO",(String) rs.getString(12));
	            sqlRetorno.put("TIPO",(String) rs.getString(13));
	            sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	            sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));
	            sqlRetorno.put("COD_EFECTO",(Long) new Long(rs.getLong(15)));

	            vDiaVenc = rs.getInt(16);
	            vMesVenc = rs.getInt(17);
	            vAgnoVenc = rs.getInt(18);

	            grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	            vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	            sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);
	            String origen = (String) rs.getString(14);

	            if (!origen.equalsIgnoreCase("SII"))
	            retorno.addElement((HashMap) sqlRetorno);
	          }
	          else
	          {
	              Long VFOLIO_SII=new Long(rs.getLong(4));
	              Long VFOLIOREC=new Long(rs.getLong(6));

	              if (COD_TIPO_EXCLUSION.intValue()==2)
	              {
	                  VTIPOFORMULARIO=new Long(21);
	              }
	              if (COD_TIPO_EXCLUSION.intValue()==3)
	              {
	                  VTIPOFORMULARIO=new Long(45);
	              }

	              if (VFOLIOREC.longValue() >0  && VFOLIO_SII.longValue() ==0)
	              {
	                  VFOLIO_SII=VFOLIOREC;
	              }
	              else
	              {
	                  if (VFOLIO_SII.longValue()>0 && VFOLIOREC.longValue()==0)
	                  {
	                      VFOLIO_SII=VFOLIO_SII;
	                  }
	              }


	              if ((COD_TIPO_EXCLUSION.intValue()==1)||
	                  (COD_TIPO_EXCLUSION.intValue()==4)||
	                  (COD_TIPO_EXCLUSION.intValue()==5)||
	                  (COD_TIPO_EXCLUSION.intValue()==6)||
	                  (COD_TIPO_EXCLUSION.intValue()==7)||
	                  (COD_TIPO_EXCLUSION.intValue()==8))
	                {
	                  VFOLIO_SII=new Long(0);
	                  VTIPOFORMULARIO=new Long(0);
	                }

	              HashMap Parametros = new HashMap();

	              Parametros.put("RutV",(Long) rut);
	              Parametros.put("TipoFormulario",(Long) VTIPOFORMULARIO);
	              Parametros.put("Folio",(Long) VFOLIO_SII);


	              ResultSet resulsetCut_replica =(ResultSet) getbuscaCutReplica((HashMap) Parametros);

	              while(resulsetCut_replica.next())
	              {
	                HashMap sqlRetorno = new HashMap();

	                sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	                sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	                sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	                sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	                //sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) resulsetCut_replica.getDate(1));
	                sqlRetorno.put("FOLIO",(Long) new Long(resulsetCut_replica.getLong(2)));
	                sqlRetorno.put("TIP_FORM",(Long) new Long(resulsetCut_replica.getLong(3)));
	                sqlRetorno.put("RUT_ROL",(Long) new Long(resulsetCut_replica.getLong(4)));
	                sqlRetorno.put("TIP_CONT",(Long) new Long(resulsetCut_replica.getLong(5)));
	                sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	                sqlRetorno.put("CAUSA",(String) rs.getString(11));
	                sqlRetorno.put("EFECTO",(String) rs.getString(12));
	                sqlRetorno.put("TIPO",(String) rs.getString(13));
	                sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	                sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));
	                sqlRetorno.put("COD_EFECTO",(Long) new Long(rs.getLong(15)));

	                vDiaVenc = resulsetCut_replica.getInt(6);
	                vMesVenc = resulsetCut_replica.getInt(7);
	                vAgnoVenc = resulsetCut_replica.getInt(8);

	                grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                /*
	                 *Modifica cv_exclsiones_deudor seg�n campo encontrado
	                 *en cc_cut_replica
	                 */

	                if ((COD_TIPO_EXCLUSION.intValue()==2) || (COD_TIPO_EXCLUSION.intValue()==3))
	                {
	                  HashMap Mod_CVExclusiones = new HashMap();

	                  Mod_CVExclusiones.put("TIP_CONT",(Long) new Long(resulsetCut_replica.getLong(5)));
	                  Mod_CVExclusiones.put("RUT_ROL",(Long) new Long(resulsetCut_replica.getLong(4)));
	                  Mod_CVExclusiones.put("TIP_FORM",(Long) new Long(resulsetCut_replica.getLong(3)));
	                  Mod_CVExclusiones.put("FOLIO",(Long) new Long(resulsetCut_replica.getLong(2)));
	                  //Mod_CVExclusiones.put("FECHA_VENCIMIENTO",(java.sql.Date) resulsetCut_replica.getDate(1));
	                  Mod_CVExclusiones.put("COD_EXCLUSION_DEUDOR",(Long) new Long(rs.getLong(1)));

	                  vDiaVenc = resulsetCut_replica.getInt(6);
	                  vMesVenc = resulsetCut_replica.getInt(7);
	                  vAgnoVenc = resulsetCut_replica.getInt(8);

	                  grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                  vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                  Mod_CVExclusiones.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                  getModif_CutRepli_CvExclusiones(Mod_CVExclusiones);//Llamada al m�todo modificacion cv_exclusiones_deudor
	                }
	                /*Termina*/

	                String origen = (String) rs.getString(14);

	               if (!origen.equalsIgnoreCase("SII"))
	                retorno.addElement((HashMap)  sqlRetorno);
	              }
	              /****Termina con Deudas Cut****/

	              /****Comienza con deudas no cut****/

	              HashMap ParametrosDeudasNoCut = new HashMap();

	              ParametrosDeudasNoCut.put("RutV",(Long) rut);
	              ParametrosDeudasNoCut.put("TipoFormulario",(Long) VTIPOFORMULARIO);
	              ParametrosDeudasNoCut.put("Folio",(Long) VFOLIO_SII);

	              ResultSet resulsetDeudasNocut =(ResultSet) getbuscaDeudasNoCut((HashMap) ParametrosDeudasNoCut);

	              while(resulsetDeudasNocut.next())
	              {
	                HashMap sqlRetorno = new HashMap();

	                sqlRetorno.put("COD_EXCLUSION_DEUDOR",(String) rs.getString(1));
	                sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs.getLong(2)));
	                sqlRetorno.put("ID_PERSONA",(Long) new Long(rs.getLong(3)));
	                sqlRetorno.put("FOLIO_SII",(Long) new Long(rs.getLong(4)));
	                // sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) resulsetDeudasNocut.getDate(1));
	                sqlRetorno.put("FOLIO",(Long) new Long(resulsetDeudasNocut.getLong(2)));
	                sqlRetorno.put("TIP_FORM",(Long) new Long(resulsetDeudasNocut.getLong(3)));
	                sqlRetorno.put("RUT_ROL",(Long) new Long(resulsetDeudasNocut.getLong(4)));
	                sqlRetorno.put("TIP_CONT",(Long) new Long(resulsetDeudasNocut.getLong(5)));
	                sqlRetorno.put("COD_ORIGEN_EXCLUSION",(Long) new Long(rs.getLong(10)));
	                sqlRetorno.put("CAUSA",(String) rs.getString(11));
	                sqlRetorno.put("EFECTO",(String) rs.getString(12));
	                sqlRetorno.put("TIPO",(String) rs.getString(13));
	                sqlRetorno.put("DESCRIPCION",(String) rs.getString(14));
	                sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs.getString(13));

	                vDiaVenc = resulsetDeudasNocut.getInt(6);
	                vMesVenc = resulsetDeudasNocut.getInt(7);
	                vAgnoVenc = resulsetDeudasNocut.getInt(8);

	                grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);

	                vFechaVenc = new java.sql.Date(grFechaVenc.getTime().getTime());

	                sqlRetorno.put("FECHA_VENCIMIENTO",(java.sql.Date) vFechaVenc);

	                String origen = (String) rs.getString(14);

	               if (!origen.equalsIgnoreCase("SII"))
	                retorno.addElement((HashMap)  sqlRetorno);
	              }

	              resulsetDeudasNocut.close();
	              resulsetDeudasNocut=null;
	         }//termina if
	}//termina while




	///TERMINA LOGICA

	//COMIENZA 2 VALIDACIONES EXCLUSIONES GENERALES Y LAS QUE FALTAN

	//LLAMA A exclusiones_RMH PARA RECUPRERAR ResultSet
	      HashMap ParametrosExclusionesRMH = new HashMap();

	      ParametrosExclusionesRMH.put("ID_PERSONAS",ID_PERSONAS);
	      ParametrosExclusionesRMH.put("RUT_CONTR",RUT_CONTR);

	      ResultSet rsExclusionRMH  =(ResultSet) exclusiones_RMH((HashMap) ParametrosExclusionesRMH);

	      while(rsExclusionRMH.next())
	      {
	        HashMap sqlRetorno = new HashMap();

	        sqlRetorno.put("FOLIO",(Long) new Long(rsExclusionRMH.getLong(4)));
	        sqlRetorno.put("TIP_FORM",(Long) new Long(rsExclusionRMH.getLong(3)));
	        sqlRetorno.put("EFECTO",(String) rsExclusionRMH.getString(5));
	        sqlRetorno.put("TIPO",(String) rsExclusionRMH.getString(1));
	        sqlRetorno.put("DESCRIPCION",(String) rsExclusionRMH.getString(2));
	        sqlRetorno.put("TIP_CONT",(Long) new Long(rsExclusionRMH.getLong(6)));
	        sqlRetorno.put("RUT_ROL",(Long) new Long(rsExclusionRMH.getLong(7)));
	        sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsExclusionRMH.getDate(8));
	        //sqlRetorno.put("FECHA_VENCIMIENTO",(String) rsExclusionRMH.getString(8));
	        sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsExclusionRMH.getString(9));
	        sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rsExclusionRMH.getLong(9)));

	        retorno.addElement((HashMap) sqlRetorno);
	      }
	      rsExclusionRMH.close();
	      rsExclusionRMH=null;
	//TERMINA  exclusiones_RMH

	//LLAMA A exclusionConvenioCaducado PARA RECUPRERAR ResultSet
	      if (Perfil.longValue()==0)
	      {
	        HashMap ParametrosexclusionConvenioCaducado = new HashMap();

	        ParametrosexclusionConvenioCaducado.put("ID_PERSONAS",ID_PERSONAS);
	        ParametrosexclusionConvenioCaducado.put("RUT_CONTR",RUT_CONTR);

	        ResultSet rs1 =(ResultSet) exclusionConvenioCaducado((HashMap) ParametrosexclusionConvenioCaducado);

	        while(rs1.next())
	        {
	          HashMap sqlRetorno = new HashMap();

	          sqlRetorno.put("FOLIO",(Long) new Long(rs1.getLong(4)));
	          sqlRetorno.put("TIP_FORM",(Long) new Long(rs1.getLong(3)));
	          sqlRetorno.put("EFECTO",(String) rs1.getString(5));
	          sqlRetorno.put("TIPO",(String) rs1.getString(1));
	          sqlRetorno.put("DESCRIPCION",(String) rs1.getString(2));
	          sqlRetorno.put("TIP_CONT",(Long) new Long(rs1.getLong(6)));
	          sqlRetorno.put("RUT_ROL",(Long) new Long(rs1.getLong(7)));
	          sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rs1.getDate(8));
	          sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs1.getString(9));
	          sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs1.getLong(9)));

	          retorno.addElement((HashMap) sqlRetorno);
	        }
	        rs1.close();
	        rs1=null;
	//TERMINA  exclusionConvenioCaducado
	      }

	//LLAMA A exclusionesRMHGernerales PARA RECUPRERAR ResultSet

	      HashMap ParametrosexclusionesRMHGernerales = new HashMap();

	      ParametrosexclusionesRMHGernerales.put("ID_PERSONAS",ID_PERSONAS);
	      ParametrosexclusionesRMHGernerales.put("RUT_CONTR",RUT_CONTR);

	      ResultSet rs2 =(ResultSet) exclusionesRMHGernerales((HashMap) ParametrosexclusionesRMHGernerales);

	      while(rs2.next())
	      {
	        /*Validaci�n del Procedimiento almacenado retorna   1= si pertenece exclusi�n  2=no pertenece exclusi�n*/
	        if (!rs2.getString(6).equals("2"))
	        {
	          HashMap sqlRetorno = new HashMap();

	          sqlRetorno.put("FOLIO",(Long) new Long(rs2.getLong(4)));
	          sqlRetorno.put("TIP_FORM",(Long) new Long(rs2.getLong(3)));
	          sqlRetorno.put("EFECTO",(String) rs2.getString(7));
	          sqlRetorno.put("TIPO",(String) rs2.getString(9));
	          sqlRetorno.put("DESCRIPCION",(String) rs2.getString(8));
	          sqlRetorno.put("TIP_CONT",(Long) new Long(rs2.getLong(1)));
	          sqlRetorno.put("RUT_ROL",(Long) new Long(rs2.getLong(2)));
	          sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rs2.getDate(5));
	          sqlRetorno.put("Cod_Tipo_Exclusion",(String) rs2.getString(10));
	          sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rs2.getLong(10)));

	          retorno.addElement((HashMap) sqlRetorno);
	        }

	      }
	      rs2.close();
	      rs2=null;
	//TERMINA  exclusionesRMHGernerales

	//LLAMA A exclusionesCondonacionAgno PARA RECUPRERAR ResultSet

	      HashMap ParametrosexclusionesCondonacionAgno = new HashMap();

	      ParametrosexclusionesCondonacionAgno.put("ID_PERSONAS",ID_PERSONAS);
	      ParametrosexclusionesCondonacionAgno.put("RUT_CONTR",RUT_CONTR);

	      ResultSet rsexclusionesCondonacionAgno =(ResultSet) exclusionesCondonacionAgno((HashMap) ParametrosexclusionesCondonacionAgno);

	      while(rsexclusionesCondonacionAgno.next())
	      {
	        HashMap sqlRetorno = new HashMap();

	        sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesCondonacionAgno.getLong(1)));
	        sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesCondonacionAgno.getLong(2)));
	        sqlRetorno.put("EFECTO",(String) rsexclusionesCondonacionAgno.getString(3));
	        sqlRetorno.put("TIPO",(String) rsexclusionesCondonacionAgno.getString(4));
	        sqlRetorno.put("DESCRIPCION",(String) rsexclusionesCondonacionAgno.getString(5));
	        sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesCondonacionAgno.getLong(6)));
	        sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesCondonacionAgno.getLong(7)));
	        sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesCondonacionAgno.getDate(8));
	        //sqlRetorno.put("FECHA_VENCIMIENTO",(String) rsexclusionesCondonacionAgno.getString(8));
	        sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesCondonacionAgno.getString(9));
	        sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rsexclusionesCondonacionAgno.getLong(9)));

	        retorno.addElement((HashMap) sqlRetorno);
	      }

	      rsexclusionesCondonacionAgno.close();
	      rsexclusionesCondonacionAgno=null;
	//TERMINA  exclusionesCondonacionAgno


	//LLAMA A exclusionesRMHfiscales PARA RECUPRERAR ResultSet
	      
	      /*SE DEPRECA  22/07/2015   CCC, CBT, CBOCA, CLH*/

	     /* HashMap ParametrosexclusionesRMHfiscales = new HashMap();

	      java.util.Date FechaActual;
	      java.util.Date Limite_Inferior;
	      java.util.Date Limite_Superior;

	      ParametrosexclusionesRMHfiscales.put("ID_PERSONAS",ID_PERSONAS);
	      ParametrosexclusionesRMHfiscales.put("RUT_CONTR",RUT_CONTR);

	      ResultSet rsexclusionesRMHfiscales =(ResultSet) exclusionesRMHfiscales((HashMap) ParametrosexclusionesCondonacionAgno);
	      while(rsexclusionesRMHfiscales.next())
	      {

	        FechaActual=(java.util.Date) rsexclusionesRMHfiscales.getDate(10);
	        Limite_Inferior=(java.util.Date) rsexclusionesRMHfiscales.getDate(11);
	        Limite_Superior=(java.util.Date) rsexclusionesRMHfiscales.getDate(12);

	         if ((Limite_Inferior.compareTo(FechaActual)==1) || (Limite_Inferior.compareTo(FechaActual)==0)|| (Limite_Superior.compareTo(FechaActual)==-1))
	        //if ((Limite_Inferior.compareTo(FechaActual)==1) || (Limite_Superior.compareTo(FechaActual)==-1)) se cambia if igual que el de intranet
	        {
	          HashMap sqlRetorno = new HashMap();

	          sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesRMHfiscales.getLong(1)));
	          sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesRMHfiscales.getLong(2)));
	          sqlRetorno.put("EFECTO",(String) rsexclusionesRMHfiscales.getString(3));
	          sqlRetorno.put("TIPO",(String) rsexclusionesRMHfiscales.getString(4));
	          sqlRetorno.put("DESCRIPCION",(String) rsexclusionesRMHfiscales.getString(5));
	          sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesRMHfiscales.getLong(6)));
	          sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesRMHfiscales.getLong(7)));
	          sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesRMHfiscales.getDate(8));
	          sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesRMHfiscales.getString(13));
	          sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rsexclusionesRMHfiscales.getLong(13)));

	          retorno.addElement((HashMap) sqlRetorno);
	        }
	      }

	      rsexclusionesRMHfiscales.close();
	      rsexclusionesRMHfiscales=null;*/
	//TERMINA  exclusionesCondonacionAgno


	//exclusionesPropuestConActivo
	//LLAMA A exclusionesCondonacionAgno PARA RECUPRERAR ResultSet

	      HashMap ParametrosexclusionesPropuestConActivo = new HashMap();

	      ParametrosexclusionesPropuestConActivo.put("ID_PERSONAS",ID_PERSONAS);
	      ParametrosexclusionesPropuestConActivo.put("RUT_CONTR",RUT_CONTR);

	      ResultSet rsexclusionesPropuestConActivo =(ResultSet) exclusionesPropuestConActivo((HashMap) ParametrosexclusionesCondonacionAgno);

	      while(rsexclusionesPropuestConActivo.next())
	      {
	        HashMap sqlRetorno = new HashMap();

	        sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesPropuestConActivo.getLong(1)));
	        sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesPropuestConActivo.getLong(2)));
	        sqlRetorno.put("EFECTO",(String) rsexclusionesPropuestConActivo.getString(3));
	        sqlRetorno.put("TIPO",(String) rsexclusionesPropuestConActivo.getString(4));
	        sqlRetorno.put("DESCRIPCION",(String) rsexclusionesPropuestConActivo.getString(5));
	        sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesPropuestConActivo.getLong(6)));
	        sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesPropuestConActivo.getLong(7)));
	        sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesPropuestConActivo.getDate(8));
	        sqlRetorno.put("Cod_Tipo_Exclusion",(String) rsexclusionesPropuestConActivo.getString(9));
	        sqlRetorno.put("COD_TIPO_EXCLUSION",(Long) new Long(rsexclusionesPropuestConActivo.getLong(9)));
//	        sqlRetorno.put("COD_EFECTO",(String) rsexclusionesPropuestConActivo.getString(10));

	        retorno.addElement((HashMap) sqlRetorno);
	      }
	      rsexclusionesPropuestConActivo.close();
	      rsexclusionesPropuestConActivo=null;
	//TERMINA  exclusionesPropuestConActivo

	      return retorno;
	    }
	    catch(SQLException e) {
	      throw new EJBException("Error ejecutando el SQL "+"<br>ID_PERSONAS ="+ID_PERSONAS+"<br>RUT_CONTR ="+RUT_CONTR +"<br>"+ e.toString());
	     }
	     finally
	     {
	        this.closeConnection();
	        try {
	          conn.close();
	        }
	        catch (SQLException ex) {
	        }
	     }

	  }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
    */   
	  public Double verificaSaldo(int TipCont, long rutRol, int TipForm, long Folio, java.sql.Date FechaVenc)
	  {
	      Double saldo = new Double(0);
	      Connection conn = null;
	      try
	      {
	      conn = this.getConnection();
	      CallableStatement  callVerificaDeuda = conn.prepareCall("{? = call DeudasContribuyente.VerificaDeuda(?,?,?,?,?)}");

	      callVerificaDeuda.registerOutParameter(1,java.sql.Types.NUMERIC);

	      callVerificaDeuda.setLong(2,TipCont);
	      callVerificaDeuda.setLong(3,rutRol);
	      callVerificaDeuda.setLong(4,TipForm);
	      callVerificaDeuda.setLong(5,Folio);
	      callVerificaDeuda.setDate(6,FechaVenc);

	      callVerificaDeuda.execute();
	      saldo = new Double(callVerificaDeuda.getDouble(1));// si viene 0 indica �xito /
	      callVerificaDeuda.close();
	      //this.closeConnection();
	      return saldo;
	      }
	      catch(SQLException e)
	      {
	        throw new EJBException("Error ejecutando el SQL "+e.toString());
	      }
	      finally
	      {
	        this.closeConnection();
	        try {
	          conn.close();
	        }
	        catch (SQLException ex) {
	        }
	      }


	  }
	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 *
	    */   
	public int getVerificaDemanda(int ITipCont, long IRutRol)
	  {
	    int verifica=0;
	    Connection conn = null;
	      try
	      {
	      conn =  this.getConnection();
	      CallableStatement  callVerificaDeuda = conn.prepareCall("{? = call exclusion_demanda.fverifica_demanda(?,?)}");

	      callVerificaDeuda.registerOutParameter(1,java.sql.Types.NUMERIC);

	      callVerificaDeuda.setLong(2,ITipCont);
	      callVerificaDeuda.setLong(3,IRutRol);

	      callVerificaDeuda.execute();
	      verifica = callVerificaDeuda.getInt(1);
	      callVerificaDeuda.close();
	      //this.closeConnection();

	      }
	      catch(SQLException e)
	      {
	        throw new EJBException("Error ejecutando el SQL "+e.toString());
	      }
	      finally
	      {
	        this.closeConnection();
	        try {
	          conn.close();
	        }
	        catch (SQLException ex) {
	        }
	      }

	    return verifica;

	  }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
   */   
	  public Collection getVerificaExclusionRemate(String Rol)
	     {

	       PreparedStatement ps = null;
	       Vector retorno = new Vector();

	       try {

	         conn = this.getConnection();

	//LLAMA A exclusionesCondonacionAgno PARA RECUPRERAR ResultSet

	         HashMap ParametrosexclusionesRemate = new HashMap();

	         ParametrosexclusionesRemate.put("ID_PERSONAS","-1");
	         ParametrosexclusionesRemate.put("RUT_CONTR",Rol);

	         ResultSet rsexclusionesRemate =(ResultSet) exclusionesRemate((HashMap) ParametrosexclusionesRemate);

	         while(rsexclusionesRemate.next())
	         {
	           HashMap sqlRetorno = new HashMap();

	           sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesRemate.getLong(1)));
	           sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesRemate.getLong(2)));
	           sqlRetorno.put("EFECTO",(String) rsexclusionesRemate.getString(3));
	           sqlRetorno.put("TIPO",(String) rsexclusionesRemate.getString(4));
	           sqlRetorno.put("DESCRIPCION",(String) rsexclusionesRemate.getString(5));
	           sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesRemate.getLong(6)));
	           sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesRemate.getLong(7)));
	           sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesRemate.getDate(8));

	           retorno.addElement((HashMap) sqlRetorno);
	         }

	         rsexclusionesRemate.close();
	         rsexclusionesRemate=null;
	//TERMINA  exclusionesPropuestConActivo

	         return retorno;

	         }
	         catch(SQLException e) {
	           throw new EJBException("Error ejecutando el SQL "+"<br>getVerificaRol"+ e.toString());
	          }
	          finally
	          {
	            this.closeConnection();
	            try {
	              conn.close();
	            }
	            catch (SQLException ex) {
	            }
	          }

	     }

      public ResultSet exclusionesRemate(HashMap parametros)
	     {
	    	
	          PreparedStatement ps = null;

	          Vector retorno = new Vector();

	          int cantidadPatametros = parametros.size();
	          try {
	        
	            CallableStatement  call = conn.prepareCall("{? = call Exclusion_Remate.fExclusionRemate(?)}");

	            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	            String rolBienRaiz = (String) parametros.get("RUT_CONTR");
	            long lRolBienRaiz = Long.parseLong(rolBienRaiz,10);

	            //System.out.println("lRolBienRaiz="+lRolBienRaiz);
	            call.setLong(2, lRolBienRaiz);

	            call.execute();

	            ResultSet rs = (ResultSet) call.getObject(1);
	            return rs;

	         }
	         catch(SQLException e) {
	           //System.out.println("Error verifica remate:"+e.toString());
	           throw new EJBException("Error ejecutando el SQL " + e.toString());
	         }
	      }


	      public ResultSet exclusionesRemateRut(long IIdPersona)
	    {
	    	
	         PreparedStatement ps = null;

	         Vector retorno = new Vector();


	         try {
	        	 
	           CallableStatement  call = conn.prepareCall("{? = call Exclusion_Remate.fVerificaRematesRut(?)}");

	           call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	           call.setLong(2, IIdPersona);

	           call.execute();

	           ResultSet rs = (ResultSet) call.getObject(1);
	           return rs;

	        }
	        catch(SQLException e) {
	          //System.out.println("Error verifica remate:"+e.toString());
	          throw new EJBException("Error ejecutando el SQL " + e.toString());
	        }
	     }

	      
	    /** 
	 	 *
	 	 * <!-- begin-xdoclet-definition --> 
	 	 * @ejb.interface-method view-type="both"
	 	 * <!-- end-xdoclet-definition --> 
	 	 * @generated
	 	 *
	     */   	      
	      
	     public Collection getVerificaExclusionRemate(long IIdPersona)
	     {
	    
	       PreparedStatement ps = null;
	       Vector retorno = new Vector();

	       try {

	         conn = this.getConnection();

	//LLAMA A exclusionesCondonacionAgno PARA RECUPRERAR ResultSet

	         HashMap ParametrosexclusionesRemate = new HashMap();


	         ResultSet rsexclusionesRemate =(ResultSet) exclusionesRemateRut(IIdPersona);

	         while(rsexclusionesRemate.next())
	         {
	           HashMap sqlRetorno = new HashMap();

	           sqlRetorno.put("FOLIO",(Long) new Long(rsexclusionesRemate.getLong(1)));
	           sqlRetorno.put("TIP_FORM",(Long) new Long(rsexclusionesRemate.getLong(2)));
	           sqlRetorno.put("EFECTO",(String) rsexclusionesRemate.getString(3));
	           sqlRetorno.put("TIPO",(String) rsexclusionesRemate.getString(4));
	           sqlRetorno.put("DESCRIPCION",(String) rsexclusionesRemate.getString(5));
	           sqlRetorno.put("TIP_CONT",(Long) new Long(rsexclusionesRemate.getLong(6)));
	           sqlRetorno.put("RUT_ROL",(Long) new Long(rsexclusionesRemate.getLong(7)));
	           sqlRetorno.put("FECHA_VENCIMIENTO",(java.util.Date) rsexclusionesRemate.getDate(8));

	           retorno.addElement((HashMap) sqlRetorno);
	         }

	         rsexclusionesRemate.close();
	         rsexclusionesRemate=null;
	//TERMINA  exclusionesPropuestConActivo

	         return retorno;

	         }
	         catch(SQLException e) {
	           throw new EJBException("Error ejecutando el SQL "+"<br>getVerificaRol"+ e.toString());
	          }
	          finally
	          {
	            this.closeConnection();
	            try {
	              conn.close();
	            }
	            catch (SQLException ex) {
	            }
	          }

	     }	
	
	     /** 
	 	 *
	 	 * <!-- begin-xdoclet-definition --> 
	 	 * @ejb.interface-method view-type="both"
	 	 * <!-- end-xdoclet-definition --> 
	 	 * @generated
	 	 *
	     */   	      
	      
	     public List getExclusionesDeudor(long tipoContribuyente, long rutContribuyente)
	     {
	    
     	    	 Connection conn = null;
     	    	CallableStatement  call=null;
		            try {
		        	conn =  this.getConnection();
		            System.out.println("---------------> "+tipoContribuyente);
		            System.out.println("---------------> "+rutContribuyente);
		        	
		        	call = conn.prepareCall("{call PKG_EXCLUSIONES_NEW.ALERTA_EXCLUSIONES_CONVENIOS(? , ? , ?)}");
		            call.setLong(1, rutContribuyente);
		            call.setLong(2, tipoContribuyente);

		            call.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
		            System.out.println("caller-----------------------> "+call.toString());
		            call.execute();
                    List listaExclusiones = new ArrayList();
		            ResultSet rs = (ResultSet) call.getObject(3);
		            DeudorExclusionAlerta deudorExclusionAlerta = null;
		            System.out.println("pasa por el cursor ------->");
		            
		            while (rs.next()) {
		            	deudorExclusionAlerta = new DeudorExclusionAlerta();
		            	deudorExclusionAlerta.setTipoContribuyente((int) rs.getLong("TIP_CONT"));
		            	deudorExclusionAlerta.setRutRol((int) rs.getLong("RUT_ROL"));
		            	deudorExclusionAlerta.setTipoFormulario((int) rs.getLong("TIP_FORM"));
		            	deudorExclusionAlerta.setFolio(rs.getLong("FOLIO"));
		            	deudorExclusionAlerta.setFechaVencimiento(rs.getDate("FECHA_VENCIMIENTO"));
		            	deudorExclusionAlerta.setCodigoExclusion((int) rs.getLong("COD_EXCLUSION"));
		            	deudorExclusionAlerta.setOrigenExclusion(rs.getString("ORIGEN_EXCLUSION"));
		            	deudorExclusionAlerta.setDescExclusion(rs.getString("DESC_EXCLUSION"));
		            	deudorExclusionAlerta.setEfectoExclusion(rs.getString("EFECTO_EXCLUSION"));
		            	listaExclusiones.add(deudorExclusionAlerta);

		              }

		            return listaExclusiones;
		         }catch(SQLException e) {
		           //System.out.println("Error verifica remate:"+e.toString());
		           throw new EJBException("Error ejecutando el SQL " + e.toString());
		         }catch(Exception ex) {
		        	    ex.printStackTrace();
			           //System.out.println("Error verifica remate:"+e.toString());
			           throw new EJBException("Error ejecutando  " + ex.toString());
			         }finally
			          {
				            this.closeConnection();
				            try {
				              conn.close();
				            }
				            catch (SQLException ex) {
				            }
				          }
		      }

	
}