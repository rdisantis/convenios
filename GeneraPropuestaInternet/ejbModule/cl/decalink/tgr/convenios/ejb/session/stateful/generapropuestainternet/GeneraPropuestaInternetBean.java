/**
 * 
 */
package cl.decalink.tgr.convenios.ejb.session.stateful.generapropuestainternet;
/*
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import cl.decalink.tgr.convenios.ejb.entity.persona.persona;
import cl.decalink.tgr.convenios.ejb.session.stateful.generapropuesta.GeneraPropuesta;
import cl.decalink.tgr.convenios.sesion.Perfil;
*/

import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.SessionSynchronization;
import javax.ejb.SessionContext;
import javax.ejb.RemoveException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.io.*;
import java.util.StringTokenizer;
import cl.decalink.tgr.convenios.deudas.DeudaWeb;
import cl.decalink.tgr.convenios.propuestas.ConvenioInternet;
import cl.decalink.tgr.convenios.cuotas.Cuota;
import cl.decalink.tgr.convenios.ReformaTributariaInput;
import cl.decalink.tgr.convenios.StringExt;
import cl.decalink.tgr.convenios.DateExt;
import cl.decalink.tgr.convenios.sesion.Perfil;
import cl.decalink.tgr.convenios.Global;
import cl.decalink.tgr.convenios.propuestas.ConvenioMasivo;
import cl.decalink.tgr.convenios.propuestas.TipoPago;
import cl.decalink.tgr.convenios.ejb.session.stateful.generapropuesta.GeneraPropuesta;
import cl.decalink.tgr.convenios.ejb.session.stateful.generapropuesta.GeneraPropuestaHome;
import cl.decalink.tgr.convenios.ejb.entity.persona.persona;
import cl.decalink.tgr.convenios.cuotas.NumeroCuotas;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="GeneraPropuestaInternet"	
 *           description="An EJB named GeneraPropuestaInternet"
 *           display-name="GeneraPropuestaInternet"
 *           jndi-name="cobranzas.convenios.negocio.ejb.session.stateful.GeneraPropuestaInternet"
 *           type="Stateful" 
 *           transaction-type="Container"
 *           
 * @ejb.transaction
 *   type="Required"
 * @weblogic.enable-call-by-reference
 * 					True   
 * @weblogic.cache max-beans-in-cache="300"
 *                                    cache-type="NRU"

 * 
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public class GeneraPropuestaInternetBean implements
		javax.ejb.SessionBean {
	
	  private SessionContext sessionContext;
	  /* Mantiene la fuente de datos para la conexi�n con la base de datos desde
	   *   el pool de conexiones
	   */
	  private transient DataSource dataSource;
	  /* Mantiene el objeto Connection con la conexi�n a la base de datos
	   */
	  private transient Connection connection;
	  /* Mantiene las deudas del contribuyente
	   */
	  private Vector deudasContribuyente;
	  /** Mantiene las propuestas que generadas de un contribuyente
	   */
	  private Vector propuestas;
	  /* Mantiene las demandas de un contribuyente
	   */
	  private HashMap demandas;
	  /* Mantiene el contribuyente que consulta
	   */
	  private HashMap propuestasDem;

	  private HashMap deudasPropuestas;

	  private HashMap propuestasDemandaActual;

	  private HashMap deudasPropuestaDemandaActual;

	  private HashMap propuestasDemTemp;

	  private HashMap deudasPropuestasTemp;

	  private persona contribuyente;
	  
	  /* Mantiene el perfil del contribuyente que se consulta
	   */
	  private Perfil perfilSesion;
	  /* Mantiene el identificador del perfil que se consulta
	   */
	  private int perfil;
	  /* Mantiene el objeto generaPropuesta al cual consulta el bean
	   */
	  private GeneraPropuesta sessionGeneraPropuesta;

	  /* Arreglo de deudas, usado para seleccionar las deudas por grupo
	   */
	  private String ArregloDatos="";

	  /*  Bandera que indica si existen convenios activos
	   */
	  private boolean posibilidaDeudasConveniosActivos = false;

	  private String nombrePropuesta ="";
	  /* Mantiene la tesorer�a del funcionario que consulta
	   *  para este caso la tesorer�a ficticia Internet
	   */
	  private Long IdTesoreria;
	  /** Mantiene el funcionario que consulta el sistema para este caso Internet
	   */
	  private Long IdFuncionario;
	  /* Mantiene el nombre de la p�gina, para poder navegar entre las p�ginas
	   */
	  private String nombrePagina ="";
	  /* Mantiene el rut o rol para el cual se esta consultando
	   */
	  private String RutRol="";
	  /* Mantiene el c�digo de la demanda que se consulta
	   */
	  private String CodDemanda="";
	  /* Mantiene los datos de la demanda activa o la que se consulta en
	   *  determinado momento
	   */

	  private HashMap demandaActiva;

	  private long codCuotaContado;

	  public List excluidosGrupos = new ArrayList();
	  
	  
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
		this.initValues();
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
        if (sessionGeneraPropuesta!=null){

            sessionGeneraPropuesta = null;
       }

	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
		//System.out.println("<----GeneraPropuestaInternetBean---->");
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
	public GeneraPropuestaInternetBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	  /** Inicializar los valores
	  */
	  private void initValues(){

	        deudasContribuyente = null; deudasContribuyente = new Vector(); deudasContribuyente.clear();
	        demandas = null;demandas = new HashMap();demandas.clear();
	        propuestas = null; propuestas = new Vector(); propuestas.clear();
	        propuestasDem = null;propuestasDem = new HashMap();propuestasDem.clear();
	        deudasPropuestas = null;deudasPropuestas = new HashMap();deudasPropuestas.clear();

	        Long IdTesoreria = new Long(0);
	        Long IdFuncionario  = new Long(0);
	        demandaActiva = null;
	        demandaActiva = new HashMap();
	        demandaActiva.clear();
	  }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 *Establece el Perfil del contribuyente
	   *  @param perfilSesion  perfil del usuario que inicia la sesi�n
	   */
	  public void setPerfilSesion(Perfil perfilSesion){
	      this.perfilSesion = perfilSesion;
	  }
	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Retorna el Perfil del usuario que inicio la sesi�n
	   *  @return Perfil del usuario
	   */
	  public Perfil getPerfilSesion(){
	      return this.perfilSesion;
	  }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 *Establece  el perfil del contribuyente
	   *  @param perfil  perfil del usuario que inicia la sesi�n
	   */
	  public void setPerfil(int perfil){
	      this.perfil = perfil;
	  }
	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Establece la persona que inicia la sesi�n
	   *  @param contribuyente  corresponde al contribuyente que ingreso al sistema
	   */
	  public void setContribuyente(persona contribuyente)  {
	      this.contribuyente = contribuyente;
	      this.initValues();
	  }
	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 *Retorna la persona que inicio sesi�n
	  *  @return persona que inicio sesi�n
	  */
	 public persona getContribuyente(){
	      return this.contribuyente;
	 }

	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el Identificador del funcionario
	 *  @param IdFuncionario - identificador del funcionario ficticio, para este caso
	 *  funcionario Internet.
	 */
	 public void  setCodigoFuncionario(Long IdFuncionario){
	     this.IdFuncionario = IdFuncionario;
	 }
	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece la tesorer�a del funcionario
	 *  @param IdTesoreria - identifica la tesorer�a ficticia, para este caso, Internet.
	 */
	 public void  setIdTesoreria(Long IdTesoreria) {
	    this.IdTesoreria = IdTesoreria;
	 }
	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna un indicador de la existencia de convenios activos
	 *  @return indicador de la existencia de convenios activos
	 */
	 public  boolean posibilidaDeudasConveniosActivos(){
	     return this.posibilidaDeudasConveniosActivos;
	 }
	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *  Retorna el rut o rol que esta siendo consultado
	 *  @return String con el rut o rol consultado
	 */
	 public String getRutRol(){
	 return this.RutRol;
	 }
	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	public long getCodCuotaContado()
	 {
	   return this.codCuotaContado;
	 }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	 public void setCodCuotaContado(long codCuotaContado)
	 {
	   this.codCuotaContado = codCuotaContado;
	 }

	 /** Establece la colecci�n de deudas contribuyente.
	 */
	 private void getDeudasContribuyente(HashMap parametros) throws RemoteException, Exception {
	  try{

	    this.InicializarContexto();
	    Collection retornoCollection = (Collection) sessionGeneraPropuesta.getDeudasContribuyente(parametros);
	    this.deudasContribuyente.addAll(retornoCollection);

	  }catch(Exception e) {
	            throw new EJBException("Error GeneraPropuetaInternet.getDeudasContribuyente ::" + e.toString());
	   }
	  }

	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna las deudas del contribuyente
	 */
	  public Collection getDeudasFiscales() {
	   return this.deudasContribuyente;
	  }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Setea las deudas del contribuyente
	  */
	   public void setDeudasFiscales(Collection deudasTmp) {
	    this.deudasContribuyente.clear();
	    this.deudasContribuyente.addAll(deudasTmp);
	   }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Retorna las deudas fiscales del contribuyente
	*   @return Collection con deudas fiscales del contribuyente
	*/
	  public Collection getDeudasContribuyenteFiscales(String estado) throws Exception {

	  Vector deudasContribuyenteTemp = new Vector();
	  Vector deudasConvenioMasivo = new Vector();
	  HashMap parametros = new HashMap();
	  String vFechaNula="00-00-0000";
	  String vFechaVencimiento="";
	  String vFechaEmision="";
	  int tmpTipoConvenio=0;

	  try{
         System.out.println("estoy consultando de nnuevo---->");
	     parametros.put("RUT", new Long(this.contribuyente.getRut().intValue()));
	     parametros.put("INTERNET","INTERNET");
	     if (estado.equals("S")){

	       parametros.put("ESTADOCOBRANZA","S");

	     }
	     else
	     {
	        parametros.put("ESTADOCOBRANZA","T");
	     }

	     this.RutRol = this.contribuyente.getRut().toString()+"-"+this.contribuyente.getDv();

	     this.getDeudasContribuyente(parametros);

	     deudasContribuyenteTemp.addAll(this.deudasContribuyente);
	     deudasContribuyente.clear();

	     Iterator it = deudasContribuyenteTemp.iterator();
	     String  grupoAnterior="";
	     
System.out.println("<-------------------------------INTERNET--------------------------------------->");
System.out.println("VER FLAG----> "+sessionGeneraPropuesta.getReformaTributariaInput().getIsReformaTributaria());
System.out.println("VER FLAG transitoria ----> "+sessionGeneraPropuesta.getReformaTributariaInput().getCodConvenios());
                    sessionGeneraPropuesta.setPagoTotal(new Boolean(true));//agrego esto ya que por internet siempre es pago total;
	     System.out.println("<------------------------------------------------------------------------------->");	     
	     while(it.hasNext()) {

	         DeudaWeb elemento = (DeudaWeb) it.next();
	         String grupo = elemento.getGrupo();
	          System.out.println("<-------------------------------------------------- -------------------->"+elemento.getTipoConvenio());
              System.out.println("veo codigo que tiene -------------------->"+elemento.getTipoConvenio());
	  
	         // modificacion Internet para convenios masivos MRC : 10-01-2006

	         //CAMBIO 24-04-2012 CLH y Cata Bustos
	         //se agrega nuevo cambio  CCC y Cata Bustos
	         //if (("Art. 192 Fiscal LEY 20.460".equals(grupo) )||("Art. 192 Fiscal".equals(grupo)||("Ley Fiscal Reforma Tributaria 2012".equals(grupo)) || ("Ley 20.583 Concesiones Acuicultura".equals(grupo))|| ("Cr�ditos de Estudios Superiores Ley 20.027".equals(grupo)))) {
	         //LEY 2014 36 cuots CLH y CCC
	         //if (("Art. 192 Fiscal LEY 20.460".equals(grupo) )||("Art. 192 Fiscal".equals(grupo)||("Ley Fiscal Reforma Tributaria 2014".equals(grupo)) || ("Ley 20.583 Concesiones Acuicultura".equals(grupo))|| ("Cr�ditos de Estudios Superiores Ley 20.027".equals(grupo)))) {
	        //   if (("C".equals(elemento.getOrigen())) && ("S".equals(elemento.getPosibilidadConvenio()))){
              System.out.println("------>elemento.getDerechoConvenio()"+elemento.getDerechoConvenio());
              System.out.println("------>elemento.getDerechoCondonacion()"+elemento.getDerechoCondonacion());
              System.out.println("------>elemento.getPosibilidadCondonacion()"+elemento.getPosibilidadCondonacion());
              
              if (("C".equals(elemento.getOrigen())) && (elemento.getDerechoConvenio().equalsIgnoreCase("S"))){  
	                    deudasContribuyente.addElement(elemento);
	                 }else{
	                	 excluidosGrupos.add(elemento.getGrupo());
	                 }
	        //}
	        it.remove();
	      }

	     //**Elimina deudas para convenios normales que est�n para un convenio masivo *//
	     if (!deudasConvenioMasivo.isEmpty()){

	        Iterator itera = deudasConvenioMasivo.iterator();

	        while(itera.hasNext()) {

	           DeudaWeb deudaMasivo = (DeudaWeb) itera.next();
	           Iterator iteraM = deudasContribuyente.iterator();

	           while(iteraM.hasNext()) {

	              DeudaWeb elemento = (DeudaWeb) iteraM.next();

	              if (deudaMasivo.equals(elemento) && !"M".equals(elemento.getOrigen())){
	                 iteraM.remove();
	              }
	           }
	        }
	     }

	     deudasConvenioMasivo = null;
	    //* * *//

	    //**Elimina deudas que no tengan fecha emisi�n valida, y fecha vencimiento nula *//

	     deudasContribuyenteTemp.clear();
	     deudasContribuyenteTemp.addAll(this.deudasContribuyente);
	     deudasContribuyente.clear();

	     if(!deudasContribuyenteTemp.isEmpty()){

	     Iterator itera = deudasContribuyenteTemp.iterator();

	   //if(!deudasContribuyente.isEmpty()){

	     //Iterator itera = deudasContribuyente.iterator();

	     while(itera.hasNext()) {

	         DeudaWeb deudaTP = (DeudaWeb) itera.next();

	         vFechaVencimiento=DateExt.format("DD-MM-AAAA",deudaTP.getFechaVencimiento());
	         vFechaEmision=DateExt.format("DD-MM-AAAA",this.getFechaEmision(deudaTP));

	         //System.out.println("vFechaVencimiento="+vFechaVencimiento);
	         //System.out.println("vFechaEmision="+vFechaEmision);

	         
	         //se elimina toda la logica de la fecha de vencimiento null
	         /*
	         if ((deudaTP.getTipoFormulario()== 21)||(deudaTP.getTipoFormulario()== 22)||(deudaTP.getTipoFormulario()== 29)) {

	             //vFechaVencimiento=DateExt.format("DD-MM-AAAA",deudaTP.getFechaVencimiento());
	             //vFechaEmision=DateExt.format("DD-MM-AAAA",this.getFechaEmision(deudaTP));


	             if (vFechaNula.equals(vFechaEmision)) {
	                 itera.remove();
	                 excluidosGrupos.add(deudaTP.getGrupo());
	             }else{
	                 if (vFechaNula.equals(vFechaVencimiento)) {
	                     itera.remove();
	                     excluidosGrupos.add(deudaTP.getGrupo());
	                 }
	                 else
	                 {
	                     deudasContribuyente.addElement(deudaTP);
	                 }

	             }
	         }
	         else {

	                 if (vFechaNula.equals(vFechaVencimiento)) {
	                       itera.remove();
	                       excluidosGrupos.add(deudaTP.getGrupo());
	                 }
	                 else
	                 {
	                    deudasContribuyente.addElement(deudaTP);
	                 }
	            }//else*/
	         
	         deudasContribuyente.addElement(deudaTP);
	         }///while
	      }//if


	   //***crea grupos de deudas

	   Vector grupos = new Vector();

	    if (!deudasContribuyente.isEmpty()){

	        Iterator iteraX = deudasContribuyente.iterator();
	        String grupoA="";

	        while(iteraX.hasNext()) {

	          DeudaWeb deudaX = (DeudaWeb) iteraX.next();

	          if (!grupoA.equals(deudaX.getGrupo())){
	                 grupos.addElement(deudaX);
	                 grupoA = deudaX.getGrupo();
	                 //System.out.println("grupoA:"+grupoA);
	              }
	        }
	     }

	   if (!grupos.isEmpty()){

	      String concatenaGrupos="";
	      String concatena="";
	      int cuentagrupo=0;
	      Iterator itM = grupos.iterator();

	       while(itM.hasNext()) {

	           DeudaWeb deuda = (DeudaWeb) itM.next();
	           String  grupoActual = deuda.getGrupo();
	           //System.out.println("grupoActual:"+grupoActual);
	           int cuenta=0;
	           Iterator itx = deudasContribuyente.iterator();
	           String concatenaDeudas="";

	           while(itx.hasNext()) {

	               DeudaWeb deudaR = (DeudaWeb) itx.next();

	              /* if (grupoActual.equals("Art. 192 Fiscal"))
	               {
	                    if (grupoActual.equals("Art. 192 Fiscal") && deudaR.getTipoFormulario()!=30)
	                    {
	                        ++cuenta;
	                        if (cuenta==1){
	                          concatenaDeudas = deudaR.getTipoContribuyente()+"*"+deudaR.getRutRol()+"*"+deudaR.getFolio()+"*"+deudaR.getTipoFormulario()+"*"+deudaR.getFechaVencimiento().getTime()+"*"+deudaR.getOrigen();
	                        }
	                        else{
	                          concatenaDeudas = concatenaDeudas+","+deudaR.getTipoContribuyente()+"*"+deudaR.getRutRol()+"*"+deudaR.getFolio()+"*"+deudaR.getTipoFormulario()+"*"+deudaR.getFechaVencimiento().getTime()+"*"+deudaR.getOrigen();
	                        }
	                    }
	               }
	               else
	               {*/
	                  if (grupoActual.equals(deudaR.getGrupo())){

	                      ++cuenta;

	                      if (cuenta==1){
	                        concatenaDeudas = deudaR.getTipoContribuyente()+"*"+deudaR.getRutRol()+"*"+deudaR.getFolio()+"*"+deudaR.getTipoFormulario()+"*"+deudaR.getFechaVencimiento().getTime()+"*"+deudaR.getOrigen()+"*"+deudaR.getTipoConvenio()+"*"+deuda.getEnDemanda();
	                        
	                      }
	                      else{
	                        concatenaDeudas = concatenaDeudas+","+deudaR.getTipoContribuyente()+"*"+deudaR.getRutRol()+"*"+deudaR.getFolio()+"*"+deudaR.getTipoFormulario()+"*"+deudaR.getFechaVencimiento().getTime()+"*"+deudaR.getOrigen()+"*"+deudaR.getTipoConvenio()+"*"+deuda.getEnDemanda();
	                      }
	                  }

	               //}
	           }
	           
	           concatenaGrupos = grupoActual+":"+concatenaDeudas;

	           ++cuentagrupo;

	           if (cuentagrupo==1){
	              concatena = concatenaGrupos;
	           }
	           else{

	                if (grupoActual.equals("Art. 192 Fiscal"))
	                {
	                    concatena = concatena+":"+concatenaGrupos;
	                }
	                else
	                {
	                    concatena = concatena+"?"+concatenaGrupos;
	                }
	           }
	      // System.out.println("concatena:"+concatena);
	       this.ArregloDatos = concatena;

	       }
	  }

	  sessionGeneraPropuesta.setDeudasContribuyenteInternet(deudasContribuyente);

	  return deudasContribuyente;

	  }catch(Exception e) {
	            throw new EJBException("Error getDeudasContribuyenteFiscales ::" + e.toString());
	   }
	 }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Retorna las propuestas de convenio generadas
	 *  @return  Collection con las propuestas de convenio generadas
	 */
	public Collection getPropuestasConvenio(){
	 return this.propuestas;
	}
	
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	public HashMap getPropuestasConvenioTemp(){
	   return this.propuestasDemTemp;
	  }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	public HashMap getPropuestaDemanda(){
	   return this.propuestasDem;
	  }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	public HashMap getDeudasPropuestasTemp(){
	    return this.deudasPropuestasTemp;
	    }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	public HashMap getDeudasPropuestas(){
	    return this.deudasPropuestas;
	 }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	 public HashMap getPropuestasDemandaActual(){
	    return this.propuestasDemandaActual;
	   }
	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	 public HashMap getDeudasDemandaActual(){
	     return this.deudasPropuestaDemandaActual;
	     }

	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Selecciona las deudas del contribuyente
	 *
	 */
	 public int setSeleccionaDeudasContribuyente() {


	 int verifica = 0;
	 String separaGrupos="?";
	 String separaClaves=":";
	 String sstring="";

	 propuestas = null; propuestas = new Vector(); propuestas.clear();

	 try {

	      if (!ArregloDatos.equals("")){

	      String[] sGrupos = StringExt.split(ArregloDatos,separaGrupos);

	      if (!sGrupos.equals("")) {

	      String nombreGrupo="";
	      String deudas="";
	      String tipoBeneficio="";
	      int codConvenio=0;
	      ReformaTributariaInput banderaReformaTributariaInput = new ReformaTributariaInput();
	      if (sessionGeneraPropuesta.getReformaTributariaInput()!=null){
	    	  if (sessionGeneraPropuesta.getReformaTributariaInput().getCodConvenios()!=null){
	    		  codConvenio= sessionGeneraPropuesta.getReformaTributariaInput().getCodConvenios().intValue();
	    	  }
	         banderaReformaTributariaInput =sessionGeneraPropuesta.getReformaTributariaInput();
	      }
	      for (int index = 0; index < sGrupos.length; index++) {

	          int numDeudas = 0;
	          long suma = 0;
	          ArrayList deudasProp=new ArrayList();
	          sstring = new String(sGrupos[index].toString());

	          String[] sDeudas = StringExt.split(sstring,separaClaves);

	          nombreGrupo = sDeudas[0];
	          deudas = sDeudas[1];
	          //System.out.println("aqui deudas----->  "+deudas);
	          String[] deuda1 = StringExt.split(deudas,",");
	          //System.out.println("aqui deuda1----->  "+deuda1);
	          String[] detalleDeuda = StringExt.split(deuda1[0],"*");
	      
	          int tipoConvenio = Integer.parseInt(detalleDeuda[6]);
	          
	          int tipoFormulario = Integer.parseInt(detalleDeuda[3]);
	          
	          if (tipoFormulario==30){
	        	  int tieneGarantia = this.getDeterminaGarantiaTerr(deudas);
	        	  sessionGeneraPropuesta.setGarantiaTerritorial( new Integer(tieneGarantia));
	          }
	          
	          
	          System.out.println("aqui esta tipo convenio----->  "+detalleDeuda[6]);
	          
	          /**************************se realiza validacion para obtenr si es transitorio*************************************/
	          System.out.println("aqui esta bandera----->  "+banderaReformaTributariaInput.getCodConvenios());
	          System.out.println("aqui esta bandera-2---->  "+codConvenio);
	          //if (banderaReformaTributariaInput.getCodConvenios()!=null){
	        	   ReformaTributariaInput tmpReformaTributariaInput =sessionGeneraPropuesta.getReformaTributariaInput(); 
	        	   if (codConvenio==tipoConvenio){
	        		   //tipoBeneficio="TR";
	        		   System.out.println("transitorio----->");
	        		   System.out.println("tipo convenio----->  "+tipoConvenio);
	        		   tmpReformaTributariaInput.setCodConvenios(new Integer(tipoConvenio));
	        	   
	        	   }else{
	        		   //tipoBeneficio="PE";
	        		   System.out.println("permenante----->");
	        		   System.out.println("tipo convenio----->  "+tipoConvenio);	        		   
	        		   tmpReformaTributariaInput.setCodConvenios(null);
	        	   }
	        	   sessionGeneraPropuesta.setReformaTributariaInput(tmpReformaTributariaInput);
	        	  // System.out.println("COD INTERNET convenio----->  "+sessionGeneraPropuesta.getReformaTributariaInput().getCodConvenios());
              /***************************************************************/	          
	          
	          
	          
	          //***buscar tipo de pago
              
	          int tipoPago=(seleccionaTipoPago(deudas))? TipoPago.CONVENIO_CON_CONDONACION:TipoPago.CONVENIO_SIN_CONDONACION;
	          Perfil perfilTemp =this.perfilSesion;
	          Long vMaximoNumeroCuotas=sessionGeneraPropuesta.getMaximoNumeroCuotas();
	          Long vPorcentajeCuotaContado = sessionGeneraPropuesta.getPorcentajeCuotaContado();

	          if (nombreGrupo.equals("Art. 192 Fiscal LEY 20.460")||nombreGrupo.equals("Art. 192 Territorial LEY 20.460"))
	          {
	            //perfilTemp.setMontoMinimo(0);
	            perfilTemp.setPorcentajeCuotaContado(3);
	            vPorcentajeCuotaContado=new Long(3);
	            vMaximoNumeroCuotas=new Long(36);
	          }

	          //CAMBIO 24-04-2012 CLH y Cata Bustos
	          if (nombreGrupo.equals("Ley 20.583 Concesiones Acuicultura")) {
	            //perfilTemp.setMontoMinimo(0);
	            //perfilTemp.setPorcentajeCuotaContado(5);
	            vPorcentajeCuotaContado = new Long(5);
	            vMaximoNumeroCuotas = new Long(36);
	          }

	          //cambio nueva ley tributaria 2012
	          if (nombreGrupo.equals("Ley Territorial Reforma Tributaria 2012")||nombreGrupo.equals("Ley Fiscal Reforma Tributaria 2012"))
	          {
	            //perfilTemp.setMontoMinimo(0);
	            perfilTemp.setPorcentajeCuotaContado(3);
	            vPorcentajeCuotaContado=new Long(3);
	            vMaximoNumeroCuotas=new Long(36);
	          }

	          //cambio nueva ley tributaria 2014 CLH y CCC
	          if (nombreGrupo.equals("Ley Territorial Reforma Tributaria 2014")||nombreGrupo.equals("Ley Fiscal Reforma Tributaria 2014"))
	          {
	            //perfilTemp.setMontoMinimo(0);
	            perfilTemp.setPorcentajeCuotaContado(3);
	            vPorcentajeCuotaContado=new Long(3);
	            vMaximoNumeroCuotas=new Long(36);
	          }

	          
	          if (nombreGrupo.equals("Cr�ditos de Estudios Superiores Ley 20.027")){
	            //perfilTemp.setMontoMinimo(0);
	            perfilTemp.setPorcentajeCuotaContado(8);
	            vPorcentajeCuotaContado=new Long(8);
	            vMaximoNumeroCuotas=new Long(12);
	            sessionGeneraPropuesta.setPerfilSesion(perfilTemp);
	          }

	          if (nombreGrupo.equals("Art. 192 Fiscal")||nombreGrupo.equals("Art. 192 Territorial")) {
	            //perfilTemp.setMontoMinimo(0);
	            //perfilTemp.setPorcentajeCuotaContado(5);
	            vPorcentajeCuotaContado = new Long(10);
	          }


	          
	          sessionGeneraPropuesta.setPerfilSesion(perfilTemp);
	         // System.out.println("Monto Minimo:"+sessionGeneraPropuesta.getPerfilSesion().getMontoMinimo());

	          sessionGeneraPropuesta.setTipoPago(tipoPago);
	          sessionGeneraPropuesta.setTransportista(1);
	          sessionGeneraPropuesta.setPerfil(this.perfil);


	            sessionGeneraPropuesta.setDeudasContribuyenteInternet(this.
	                getDeudasContribuyentesPorTipoConvenio(nombreGrupo));

	            sessionGeneraPropuesta.setSeleccionaDeudasContribuyente(deudas);
	            
	            if (sessionGeneraPropuesta.getReformaTributariaInput().getIsReformaTributaria().booleanValue()&&tipoFormulario!=34){
		             System.out.println("entro a setear condiciones condonacion reforma tributaria");
		        	  perfilTemp.setPorcentajeCuotaContado(sessionGeneraPropuesta.getReformaTributariaOutput().getMinCuotaContado().intValue());
		              vPorcentajeCuotaContado=new Long(sessionGeneraPropuesta.getReformaTributariaOutput().getMinCuotaContado().intValue());
		              vMaximoNumeroCuotas=new Long(sessionGeneraPropuesta.getReformaTributariaOutput().getMaxCuota().intValue());
		              sessionGeneraPropuesta.setPerfilSesion(perfilTemp);
		        }
	            
	            int verificaTipoPagoAnt=0;
	            int verificaTipoPago = sessionGeneraPropuesta.verificaTipoPago();

	            if ((verificaTipoPago == 1 || verificaTipoPago == 2 ||
	                verificaTipoPago == 3) && verificaTipoPagoAnt>0) {
	              verifica = 1;
	              return verifica;
	            }
	            else {

	              verificaTipoPagoAnt=verificaTipoPago;

	              int verificaMontoAnt=0;
	              int verificaMonto = sessionGeneraPropuesta.verificaMonto();

	              //verificaMonto=0;

	              if (verificaMonto == 0 && verificaTipoPago==0 && verificaMontoAnt==0) {

	            /*************************************he aqui el importante**********************************/	  
	               Vector deudasContribuyenteRetorno = (Vector)sessionGeneraPropuesta.getDeudasSeleccionadas();
             
	                
	                Vector deudasContribuyenteRetornoTemp = (Vector) deudasContribuyenteRetorno;

	                boolean ExistenDeudasNoLiquidadas = false;

	                if (!deudasContribuyente.isEmpty()) {

	                  Iterator itx = deudasContribuyenteRetorno.iterator();

	                  while (itx.hasNext()) {

	                    DeudaWeb deuda = (DeudaWeb) itx.next();

	                    if (!deuda.estaLiquidada()) {
	                      ExistenDeudasNoLiquidadas = true;
	                    }
	                    numDeudas++;
	                    suma=suma+deuda.getMonto();
	                    deudasProp.add(deuda);
	                  }
	                } //if

	                if (!ExistenDeudasNoLiquidadas) {
	                  /*clase propuesta*/
	                  ConvenioInternet convenioInternet = new ConvenioInternet(
	                      nombreGrupo);

	                  convenioInternet.setDeudas(deudasContribuyenteRetornoTemp);
	                  convenioInternet.setDeudasPropuesta(deudasProp);
	                  convenioInternet.setCodTipoConvenio(sessionGeneraPropuesta.getTipoConvenio());
	                  convenioInternet.setCodTipoPago(sessionGeneraPropuesta.getTipoPago());
	                  //convenioInternet.setNumeroCuotas(sessionGeneraPropuesta.getMaximoNumeroCuotas());
	                  //convenioInternet.setNumeroMaximoCuotas(sessionGeneraPropuesta.getMaximoNumeroCuotas());
	                  convenioInternet.setNumeroCuotas(vMaximoNumeroCuotas);
	                  convenioInternet.setNumeroMaximoCuotas(vMaximoNumeroCuotas);


	                  //convenioInternet.setPorcentajeCuotaContado(sessionGeneraPropuesta.getPorcentajeCuotaContado());
	                  convenioInternet.setPorcentajeCuotaContado(vPorcentajeCuotaContado);
	                  convenioInternet.setConvenioMasivo(sessionGeneraPropuesta.getConvenioMasivo());
	                  convenioInternet.setNumeroDeudas(numDeudas);
	                  convenioInternet.setSaldoNetoPropuesta(suma);

	                  propuestas.addElement( (ConvenioInternet) convenioInternet);

	                }
	                else {
	                  verifica = 3;
	                  return verifica;
	                }

	              } //if monto

	              else {
	                if (verificaMontoAnt >0)
	                {
	                  verifica = 4;
	                  return verifica;
	                }
	                else
	                {
	                  verificaMontoAnt=verificaMonto;
	                }
	              } //if monto

	            } //if tipo pago
	          }
	      }//for


    


	  }//if
	    return verifica;

	     }catch(Exception e) {
	    	 e.printStackTrace();
	            throw new EJBException("Error GeneraPropuestaInternet.setSeleccionaDeudasContribuyente ::" + e.toString());
	   }
	 }


	/** 
	 * Selecciona las deudas de cada demanda del contribuyente
	 *
	 */
	 private int setSeleccionaDeudasDemandasContribuyente() throws RemoteException {

	 int verifica = 0;
	 String separaGrupos="?";
	 String separaClaves=":";
	 String sstring="";

	 propuestas = null; propuestas = new Vector(); propuestas.clear();

	 try {

	      if (!ArregloDatos.equals("")){

	      String[] sGrupos = StringExt.split(ArregloDatos,separaGrupos);

	      if (!sGrupos.equals("")) {

	      String nombreGrupo="";
	      String deudas="";

	      for (int index = 0; index < sGrupos.length; index++) {
	          int numDeudas = 0;
	          long suma = 0;
	          sstring = new String(sGrupos[index].toString());

	          String[] sDeudas = StringExt.split(sstring,separaClaves);

	          nombreGrupo = sDeudas[0];
	          deudas = sDeudas[1];
	          //***buscar tipo de pago

	          int tipoPago=(seleccionaTipoPago(deudas))? TipoPago.CONVENIO_CON_CONDONACION:TipoPago.CONVENIO_SIN_CONDONACION;

	          sessionGeneraPropuesta.setTipoPago(tipoPago);
	          sessionGeneraPropuesta.setTransportista(1);
	          sessionGeneraPropuesta.setPerfil(this.perfil);

	          sessionGeneraPropuesta.setDeudasContribuyenteInternet(this.getDeudasContribuyentesPorTipoConvenio(nombreGrupo));

	          sessionGeneraPropuesta.setSeleccionaDeudasDemandasContribuyente(deudas);
	          Perfil perfilTemp =this.perfilSesion;
	          Long vMaximoNumeroCuotas=sessionGeneraPropuesta.getMaximoNumeroCuotas();
	          Long vPorcentajeCuotaContado = sessionGeneraPropuesta.getPorcentajeCuotaContado();

	          if (nombreGrupo.equals("Art. 192 Fiscal LEY 20.460")||nombreGrupo.equals("Art. 192 Territorial LEY 20.460"))
	          {
	           // perfilTemp.setMontoMinimo(0);
	            vPorcentajeCuotaContado=new Long(3);
	            vMaximoNumeroCuotas=new Long(36);

	          }

	          if (nombreGrupo.equals("Ley Fiscal Reforma Tributaria 2012")||nombreGrupo.equals("Ley Territorial Reforma Tributaria 2012"))
	          {
	           // perfilTemp.setMontoMinimo(0);
	            vPorcentajeCuotaContado=new Long(3);
	            vMaximoNumeroCuotas=new Long(36);

	          }
              //Nueva ley 2014 36 cuotas CLH y CCC
	          if (nombreGrupo.equals("Ley Fiscal Reforma Tributaria 2014")||nombreGrupo.equals("Ley Territorial Reforma Tributaria 2014"))
	          {
	           // perfilTemp.setMontoMinimo(0);
	            vPorcentajeCuotaContado=new Long(3);
	            vMaximoNumeroCuotas=new Long(36);

	          }
	          
	          
	          if (nombreGrupo.equals("Art. 192 Fiscal")||nombreGrupo.equals("Art. 192 Territorial")) {
	            //perfilTemp.setMontoMinimo(0);
	            //perfilTemp.setPorcentajeCuotaContado(5);
	            vPorcentajeCuotaContado = new Long(10);
	          }

	          if (nombreGrupo.equals("Cr�ditos de Estudios Superiores Ley 20.027")){
	            vPorcentajeCuotaContado=new Long(8);
	            vMaximoNumeroCuotas=new Long(12);
	          }


	          sessionGeneraPropuesta.setPerfilSesion(perfilTemp);


	          int verificaTipoPagoAnt=0;
	          int verificaTipoPago = sessionGeneraPropuesta.verificaTipoPago();

	          if ((verificaTipoPago == 1 || verificaTipoPago == 2 ||
	               verificaTipoPago == 3) && verificaTipoPagoAnt>0) {
	             verifica = 1;
	             return verifica;
	           }
	           else {

	             verificaTipoPagoAnt=verificaTipoPago;

	             int verificaMontoAnt=0;
	             int verificaMonto = sessionGeneraPropuesta.verificaMonto();
	             //verificaMonto=0;
	             if (verificaMonto == 0 && verificaTipoPago==0 && verificaMontoAnt==0) {


	              Vector deudasContribuyenteRetorno = (Vector) sessionGeneraPropuesta.getDeudasSeleccionadas();
	              boolean ExistenDeudasNoLiquidadas = false;
	              ArrayList deudasProp = new ArrayList();
	              if (!deudasContribuyente.isEmpty()){
	                  Iterator itx = deudasContribuyenteRetorno.iterator();

	                  while (itx.hasNext()){

	                     DeudaWeb deuda = (DeudaWeb) itx.next();

	                     if (!deuda.estaLiquidada()){
	                         ExistenDeudasNoLiquidadas = true;
	                     }
	                     numDeudas++;
	                    suma=suma+deuda.getMonto();
	                    deudasProp.add(deuda);

	                  }
	              }//if

	              if (!ExistenDeudasNoLiquidadas){
	                 /*clase propuesta*/
	                  ConvenioInternet convenioInternet= new ConvenioInternet(nombreGrupo);
	                  convenioInternet.setDeudas(deudasContribuyenteRetorno);
	                  convenioInternet.setDeudasPropuesta(deudasProp);
	                  convenioInternet.setCodTipoConvenio(sessionGeneraPropuesta.getTipoConvenio());
	                  convenioInternet.setCodTipoPago(sessionGeneraPropuesta.getTipoPago());
	                 // convenioInternet.setNumeroCuotas(sessionGeneraPropuesta.getMaximoNumeroCuotas());
	                  //convenioInternet.setNumeroMaximoCuotas(sessionGeneraPropuesta.getMaximoNumeroCuotas());
	                  convenioInternet.setNumeroCuotas(vMaximoNumeroCuotas);
	                  convenioInternet.setNumeroMaximoCuotas(vMaximoNumeroCuotas);
	                  //convenioInternet.setPorcentajeCuotaContado(sessionGeneraPropuesta.getPorcentajeCuotaContado());
	                  convenioInternet.setPorcentajeCuotaContado(vPorcentajeCuotaContado);
	                  convenioInternet.setConvenioMasivo(sessionGeneraPropuesta.getConvenioMasivo());
	                  convenioInternet.setNumeroDeudas(numDeudas);
	                  convenioInternet.setSaldoNetoPropuesta(suma);
	                  propuestas.addElement((ConvenioInternet) convenioInternet);

	              }else{
	                   verifica = 3;
	                   return verifica;
	                   }

	          }//if monto

	          else{
	            if (verificaMontoAnt >0)
	               {
	                 verifica = 4;
	                 return verifica;
	               }
	               else
	               {
	                 verificaMontoAnt=verificaMonto;
	               }

	          }//if monto

	        }//if tipo pago
	      }//for
	    }//if
	  }//if

	    return verifica;

	     }catch(Exception e) {
	            throw new EJBException("Error GeneraPropuestaInternet.setSeleccionaDeudasContribuyente ::" + e.toString());
	   }
	 }



	 private int setSeleccionaDeudasPropuestasDemandasContribuyente(String CodDemanda) throws RemoteException {

	 int verifica = 0;
	 String separaGrupos="?";
	 String separaClaves=":";
	 String sstring="";

	 propuestas = null; propuestas = new Vector(); propuestas.clear();
	 propuestasDemTemp=null;propuestasDemTemp=new HashMap();propuestasDemTemp.clear();
	 deudasPropuestasTemp=null;deudasPropuestasTemp=new HashMap();deudasPropuestasTemp.clear();

	 try {

	      if (!ArregloDatos.equals("")){

	      String[] sGrupos = StringExt.split(ArregloDatos,separaGrupos);

	      if (!sGrupos.equals("")) {

	      String nombreGrupo="";
	      String deudas="";

	      for (int index = 0; index < sGrupos.length; index++) {
	        int numDeudas = 0;
	          long suma = 0;
	        sstring = new String(sGrupos[index].toString());

	        String[] sDeudas = StringExt.split(sstring,separaClaves);

	        nombreGrupo = sDeudas[0];
	        deudas = sDeudas[1];
	          //***buscar tipo de pago

	        int tipoPago=(seleccionaTipoPago(deudas))? TipoPago.CONVENIO_CON_CONDONACION:TipoPago.CONVENIO_SIN_CONDONACION;

	        //***


	        sessionGeneraPropuesta.setTipoPago(tipoPago);
	        sessionGeneraPropuesta.setTransportista(1);
	        sessionGeneraPropuesta.setPerfil(this.perfil);

	        sessionGeneraPropuesta.setDeudasContribuyenteInternet(this.getDeudasContribuyentesPorTipoConvenio(nombreGrupo));
	        sessionGeneraPropuesta.setSeleccionaDeudasDemandasContribuyente(deudas);

	        Perfil perfilTemp =this.perfilSesion;
	         Long vMaximoNumeroCuotas=sessionGeneraPropuesta.getMaximoNumeroCuotas();
	         Long vPorcentajeCuotaContado = sessionGeneraPropuesta.getPorcentajeCuotaContado();

	         if (nombreGrupo.equals("Art. 192 Fiscal LEY 20.460")||nombreGrupo.equals("Art. 192 Territorial LEY 20.460"))
	         {
	          // perfilTemp.setMontoMinimo(0);
	           vPorcentajeCuotaContado=new Long(3);
	           vMaximoNumeroCuotas=new Long(36);

	         }

	         if (nombreGrupo.equals("Ley Fiscal Reforma Tributaria 2012")||nombreGrupo.equals("Ley Territorial Reforma Tributaria 2012"))
	         {
	          // perfilTemp.setMontoMinimo(0);
	           vPorcentajeCuotaContado=new Long(3);
	           vMaximoNumeroCuotas=new Long(36);

	         }

	         //Nueva Ley 2014 convenios 36 cuotas CLH y CCC
	         if (nombreGrupo.equals("Ley Fiscal Reforma Tributaria 2014")||nombreGrupo.equals("Ley Territorial Reforma Tributaria 2014"))
	         {
	          // perfilTemp.setMontoMinimo(0);
	           vPorcentajeCuotaContado=new Long(3);
	           vMaximoNumeroCuotas=new Long(36);

	         }	         
	         
	         if (nombreGrupo.equals("Art. 192 Fiscal")||nombreGrupo.equals("Art. 192 Territorial")) {
	           //perfilTemp.setMontoMinimo(0);
	           //perfilTemp.setPorcentajeCuotaContado(5);
	           vPorcentajeCuotaContado = new Long(10);
	         }

	         if (nombreGrupo.equals("Cr�ditos de Estudios Superiores Ley 20.027")){
	           vPorcentajeCuotaContado=new Long(8);
	           vMaximoNumeroCuotas=new Long(12);
	         }



	         //System.out.println("vMaximoNumeroCuotas="+vMaximoNumeroCuotas);
	         //System.out.println("vPorcentajeCuotaContado="+vPorcentajeCuotaContado);
	         sessionGeneraPropuesta.setPerfilSesion(perfilTemp);
	         int verificaTipoPagoAnt=0;
	        int verificaTipoPago = sessionGeneraPropuesta.verificaTipoPago();

	        if ((verificaTipoPago == 1 || verificaTipoPago == 2 ||
	                verificaTipoPago == 3) && verificaTipoPagoAnt>0) {
	              verifica = 1;
	              return verifica;
	            }
	            else {

	              verificaTipoPagoAnt=verificaTipoPago;

	              int verificaMontoAnt=0;
	              int verificaMonto = sessionGeneraPropuesta.verificaMonto();
	              //verificaMonto=0;
	              if (verificaMonto == 0 && verificaTipoPago==0 && verificaMontoAnt==0) {


	              Vector deudasContribuyenteRetorno = (Vector) sessionGeneraPropuesta.getDeudasSeleccionadas();
	              boolean ExistenDeudasNoLiquidadas = false;
	              ArrayList deudasProp = new ArrayList();
	              if (!deudasContribuyente.isEmpty()){
	                  Iterator itx = deudasContribuyenteRetorno.iterator();

	                  while (itx.hasNext()){

	                    DeudaWeb deuda = (DeudaWeb) itx.next();

	                     if (!deuda.estaLiquidada()){
	                         ExistenDeudasNoLiquidadas = true;
	                     }

	                     numDeudas++;
	                    suma=suma+deuda.getMonto();
	                    deudasProp.add(deuda);

	                  }
	              }//if

	              if (!ExistenDeudasNoLiquidadas){
	                 /*clase propuesta*/
	                  ConvenioInternet convenioInternet= new ConvenioInternet(nombreGrupo);
	                  convenioInternet.setDeudasPropuesta(deudasProp);
	                  convenioInternet.setCodTipoConvenio(sessionGeneraPropuesta.getTipoConvenio());
	                  convenioInternet.setCodTipoPago(sessionGeneraPropuesta.getTipoPago());
	                  //convenioInternet.setNumeroCuotas(sessionGeneraPropuesta.getMaximoNumeroCuotas());
	                  //convenioInternet.setNumeroMaximoCuotas(sessionGeneraPropuesta.getMaximoNumeroCuotas());
	                  convenioInternet.setNumeroCuotas(vMaximoNumeroCuotas);
	                  convenioInternet.setNumeroMaximoCuotas(vMaximoNumeroCuotas);
	                  //convenioInternet.setPorcentajeCuotaContado(sessionGeneraPropuesta.getPorcentajeCuotaContado());
	                  convenioInternet.setPorcentajeCuotaContado(vPorcentajeCuotaContado);
	                  convenioInternet.setConvenioMasivo(sessionGeneraPropuesta.getConvenioMasivo());
	                  convenioInternet.setNumeroDeudas(numDeudas);
	                  convenioInternet.setSaldoNetoPropuesta(suma);
	                  propuestasDemTemp.put((String) CodDemanda,(ConvenioInternet) convenioInternet);

	                  if (!deudasContribuyente.isEmpty()){

	                  Iterator itx = deudasContribuyenteRetorno.iterator();

	                  while (itx.hasNext()){

	                     DeudaWeb deuda = (DeudaWeb) itx.next();
	                     String llaveDeuda=deuda.getTipoContribuyente()+","+deuda.getRutRol()+","+deuda.getTipoFormulario()+","+deuda.getFolio()+","+deuda.getFechaVencimiento();

	                     deudasPropuestasTemp.put((String) llaveDeuda, (DeudaWeb) deuda);
	                     itx.remove();
	                  }
	              }//if

	              }else{
	                   verifica = 3;
	                   return verifica;
	                   }

	          }//if monto

	          else{
	            if (verificaMontoAnt >0)
	               {
	                 verifica = 4;
	                 return verifica;
	               }
	               else
	               {
	                 verificaMontoAnt=verificaMonto;
	               }

	          }//if monto

	        }//if tipo pago
	      }//for
	    }//if
	  }//if

	  return verifica;

	     }catch(Exception e) {
	            throw new EJBException("Error GeneraPropuestaInternet.setSeleccionaDeudasContribuyente ::" + e.toString());
	   }
	 }

	  /** 
	  *
	  *   Retorna las propuestas de la demanda consultada
	  *  @param CodDemanda - String que representa la demanda a consultar
	  *  @return Collection - contiene las propuestas generadas de la demanda */
	private Collection getPropuestasDemanda(String CodDemanda)  throws RemoteException, Exception {

	   HashMap parametros = new HashMap();
	   Vector deudasContribuyenteTemp = new Vector();
	   Vector propuestasRetorno = new Vector();

	   try{

	      sessionGeneraPropuesta.InicializaDatosInternet();
	      sessionGeneraPropuesta.setDemandaContribuyente(new Long(CodDemanda));

	      parametros.put("RUT", new Long(contribuyente.getRut().longValue()));
	      parametros.put("ESTADOCOBRANZA","D");

	      deudasContribuyenteTemp = (Vector) sessionGeneraPropuesta.getDeudasContribuyente(parametros);
	      deudasContribuyente.clear();

	      Iterator it = deudasContribuyenteTemp.iterator();
	      String  grupoAnterior="";

	      while(it.hasNext()) {

	         DeudaWeb elemento = (DeudaWeb) it.next();

	         if ("C".equals(elemento.getOrigen()) && ("S".equals(elemento.getPosibilidadConvenio()))) {

	             deudasContribuyente.addElement(elemento);
	         }
	        it.remove();
	      }

	     deudasContribuyenteTemp.clear();
	     deudasContribuyenteTemp = null;
	     /*elimina fechas nulas y crea grupos*/
	     this.eliminaDeudasFechaNula();
	     sessionGeneraPropuesta.setDeudasContribuyenteInternet(this.deudasContribuyente);
	     int deudasSeleccionadas = this.setSeleccionaDeudasDemandasContribuyente();

	     if (deudasSeleccionadas==0){
	        propuestasRetorno = (Vector) this.getPropuestasConvenio();
	     }

	     return propuestasRetorno;

	}catch(Exception e) {
	       throw new EJBException("Error GeneraPropuestaInternet.getPropuestasDemanda ::" + e.toString());
	 }
	}
	/*NUEVO*/

	  private HashMap getPropuestasDem(String CodDemanda)  throws RemoteException, Exception {

	     HashMap parametros = new HashMap();
	     Vector deudasContribuyenteTemp = new Vector();
	     HashMap propuestasRetorno = new HashMap();
	     HashMap deudasRetorno = new HashMap();
	     Vector deudasContribuyenteTemp2 = new Vector();

	     try{

	        sessionGeneraPropuesta.InicializaDatosInternet();
	        sessionGeneraPropuesta.setDemandaContribuyente(new Long(CodDemanda));
	        parametros.put("RUT", new Long(contribuyente.getRut().longValue()));
	        parametros.put("ESTADOCOBRANZA","D");

	        deudasContribuyenteTemp = (Vector) sessionGeneraPropuesta.getDeudasContribuyente(parametros);
	        Iterator it = deudasContribuyenteTemp.iterator();
	        String  grupoAnterior="";

	        while(it.hasNext()) {

	           DeudaWeb elemento = (DeudaWeb) it.next();

	           if ("C".equals(elemento.getOrigen()) && ("S".equals(elemento.getPosibilidadConvenio()))) {

	               deudasContribuyenteTemp2.addElement(elemento);

	           }
	          it.remove();
	        }

	        deudasContribuyente = deudasContribuyenteTemp2;
	       /*elimina fechas nulas y crea grupos*/
	       this.eliminaDeudasFechaNula();
	       sessionGeneraPropuesta.setDeudasContribuyenteInternet(this.deudasContribuyente);
	       int deudasSeleccionadas = this.setSeleccionaDeudasPropuestasDemandasContribuyente(CodDemanda);
	       String llaveConjunto ="";

	       if (deudasSeleccionadas==0){
	          propuestasRetorno = (HashMap) this.getPropuestasConvenioTemp();
	          deudasRetorno = (HashMap) this.getDeudasPropuestasTemp();

	          Set propKeySet = propuestasRetorno.keySet();
	          Iterator itp = propKeySet.iterator();

	          while (itp.hasNext())
	          {
	            llaveConjunto = (String) itp.next();
	            propuestasDem.put( (String) llaveConjunto,(ConvenioInternet) propuestasRetorno.get(CodDemanda));
	            deudasPropuestas.put( (String) llaveConjunto, (HashMap) deudasRetorno);
	          }
	       }

	       return propuestasRetorno;

	  }catch(Exception e) {
	         throw new EJBException("Error GeneraPropuestaInternet.getPropuestasDemanda ::" + e.toString());
	   }
	  }

	/*FIN NUEVO */
	  

	private void eliminaDeudasFechaNula(){
	try{

	    String vFechaNula="01-01-1900";
	    String vFechaVencimiento="";
	    String vFechaEmision="";

	    if(!deudasContribuyente.isEmpty()){

	       Iterator itera = deudasContribuyente.iterator();

	       while(itera.hasNext()) {

	          DeudaWeb deudaTP = (DeudaWeb) itera.next();

	          vFechaVencimiento=DateExt.format("DD-MM-AAAA",deudaTP.getFechaVencimiento());
	          vFechaEmision=DateExt.format("DD-MM-AAAA",this.getFechaEmision(deudaTP));

	          if ((deudaTP.getTipoFormulario()== 21)||(deudaTP.getTipoFormulario()== 22)||(deudaTP.getTipoFormulario()== 29)) {
	               if (vFechaNula.compareTo(vFechaEmision) == 0 ) {
	                 itera.remove();
	             }else{ if (vFechaNula.compareTo(vFechaEmision) == 0 ) {
	                     itera.remove();}
	             }
	          } else { if (vFechaNula.compareTo(vFechaEmision) == 0) {
	                       itera.remove();}
	          }
	       }
	    }

	   //***crea grupos de deudas
	   Vector grupos = new Vector();

	    if (!deudasContribuyente.isEmpty()){
	        Iterator iteraX = deudasContribuyente.iterator();
	        String grupoA="";

	        while(iteraX.hasNext()) {

	           DeudaWeb deudaX = (DeudaWeb) iteraX.next();

	           if (!grupoA.equals(deudaX.getGrupo())){
	             grupos.addElement(deudaX);
	             grupoA = deudaX.getGrupo();
	           }
	        }
	     }


	   if (!grupos.isEmpty()){

	      String concatenaGrupos="";
	      String concatena="";
	      int cuentagrupo=0;
	      Iterator itM = grupos.iterator();

	       while(itM.hasNext()) {

	           DeudaWeb deuda = (DeudaWeb) itM.next();
	           String  grupoActual = deuda.getGrupo();
	           int cuenta=0;
	           Iterator itx = deudasContribuyente.iterator();
	           String concatenaDeudas="";

	           while(itx.hasNext()) {

	               DeudaWeb deudaR = (DeudaWeb) itx.next();
	               if (grupoActual.equals(deudaR.getGrupo())){
	            	   

	                  ++cuenta;

	                  if (cuenta==1){concatenaDeudas = deudaR.getTipoContribuyente()+"*"+deudaR.getRutRol()+"*"+deudaR.getFolio()+"*"+deudaR.getTipoFormulario()+"*"+deudaR.getFechaVencimiento().getTime()+"*"+deudaR.getOrigen()+"*"+deudaR.getTipoConvenio()+"*"+deuda.getEnDemanda();}
	                  else{concatenaDeudas = concatenaDeudas+","+deudaR.getTipoContribuyente()+"*"+deudaR.getRutRol()+"*"+deudaR.getFolio()+"*"+deudaR.getTipoFormulario()+"*"+deudaR.getFechaVencimiento().getTime()+"*"+deudaR.getOrigen()+"*"+deudaR.getTipoConvenio()+"*"+deuda.getEnDemanda();
	                  }
	               }
	           }
	           concatenaGrupos = grupoActual+":"+concatenaDeudas;
	           ++cuentagrupo;

	           if (cuentagrupo==1){concatena = concatenaGrupos;}
	           else{concatena = concatena+"?"+concatenaGrupos; }
	           this.ArregloDatos = concatena;
	       }
	  }

	}catch(Exception e) {
	       throw new EJBException("Error GeneraPropuestaInternet.getPropuestasDemanda ::" + e.toString());
	 }
	}

    /** 
	 *
	 * Retorna las deudas del contribuyente
	 *  @param grupo - String que representa el nombre del grupo del que se quieren
	 *  obtener las deudas
	 */
	private Collection getDeudasContribuyentesPorTipoConvenio(String grupo){

	Vector deudasRetorno = new Vector();
	//System.out.println("grupo 2 internet:"+grupo);
	try{

	    if (!this.deudasContribuyente.isEmpty()){

	        Iterator itx = deudasContribuyente.iterator();

	        while (itx.hasNext()){

	          DeudaWeb deuda = (DeudaWeb) itx.next();

	         // if ((grupo.equals("RMH Fiscal"))||(grupo.equals("Art. 192 Fiscal")))
	         /* if (grupo.equals("Art. 192 Fiscal CEC II"))
	          {
	              deudasRetorno.addElement(deuda);
	          }
	          else
	          {*/
	              if (grupo.equals(deuda.getGrupo())){

	                  deudasRetorno.addElement(deuda);
	              }
	          //}
	        }
	    }

	   return deudasRetorno;
	   }catch(Exception e) {
	            throw new EJBException("Error GeneraPropuestaInternet.getDeudasContribuyentesPorTipoConvenio ::" + e.toString());
	   }
	 }

    /** 
	 * Selecciona el tipo de pago
	 *  @param deudas - String que representa el grupo de deudas del cual se desea obtener
	 *  el tipo de pago.
	 */

	private boolean seleccionaTipoPago(String deudas){

	  String separaDeudas  = ",";//separador de deudas
	  String[] sDeudas = StringExt.split(deudas,separaDeudas);
	  String separaClaves ="*";//separador de campos claves de la deuda
	  String sstring = null;
	  boolean posibilidadConvenioCondonacion=false;

	  try {

	    if (!sDeudas.equals("")) {

	        for (int index = 0; index < sDeudas.length; index++) {

	        sstring = new String(sDeudas[index].toString());

	        String[] sClaves = StringExt.split(sstring,separaClaves);

	        String tipoCont = sClaves[0];
	        String rutRol = sClaves[1];
	        String folio = sClaves[2];
	        String tipoForm = sClaves[3];
	        String fecha = sClaves[4];
	        String origen = sClaves[5];

	        Long LFechaVencimiento = new Long(fecha);
	        java.sql.Date TFechaVencimiento = new java.sql.Date(LFechaVencimiento.longValue());

	        Iterator itx = this.deudasContribuyente.iterator();

	        while (itx.hasNext()) {

	               DeudaWeb elemento = (DeudaWeb) itx.next();
	               DeudaWeb newDeudaWeb = new DeudaWeb(((Long) new Long(tipoCont)).intValue(), ((Long) new Long(tipoForm)).intValue(),((Long) new Long(rutRol)).longValue(), ((Long) new Long(folio)).longValue(), TFechaVencimiento);

	               if (elemento.equals(newDeudaWeb) && elemento.getOrigen().equals(origen)) {
	                  if ("S".equals(elemento.getPosibilidadCondonacion())){
	                      posibilidadConvenioCondonacion = true;
	                  }
	              }
	        }//while

	       }//for
	     }//if
	  return posibilidadConvenioCondonacion;
	 } catch (Exception e) {
	            throw new EJBException("Error GeneraPropuestaInternet.seleccionaTipoPago ::" + e.toString());
	  }
	}

    /** 
	 *
	 * Retorno la fecha de emisi�n de la deuda
	 *  @return Date, que representa la fecha de emisi�n
	 */
	 private Date getFechaEmision(DeudaWeb deuda) throws SQLException {

	   CallableStatement call;
	   Date fecha = null;

	    try {

	      if (deuda.getGrupo().equals("RMH Fiscal")) {
	          /* Deudas territoriales de Cut replica  por rol bien ra�z*/
	          Connection connection = this.getConnection();
	          call = connection.prepareCall("{? = call Configurarpropuesta.FechaEmisionDeuda(?,?,?,?,?)}");
	          call.registerOutParameter(1, java.sql.Types.DATE);
	          call.setLong(2, deuda.getTipoContribuyente());
	          call.setLong(3, deuda.getRutRol());
	          call.setLong(4, deuda.getTipoFormulario());
	          call.setLong(5, deuda.getFolio());
	          call.setDate(6, deuda.getFechaVencimiento());
	          call.execute();
	          fecha = new Date(((Date) call.getDate(1)).getTime());

	          call.close();
	          call = null;
	          this.closeConnection();
	      } else {
	          fecha = deuda.getFechaVencimiento();
	      }


	    } catch (SQLException eSQL) {
	      throw new SQLException ("Error SQL GeneraPropuestaInternet.getFechaEmision" + eSQL.toString());
	    }

	    return fecha;
	  }

	  /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Aplica la condonaci�n a las deudas seleccionadas del contribuyente
	   */
	  public ConvenioInternet getAplicaCondonacion (){

	  ConvenioInternet convenioRetorno = null;

	  try{

	      if (!this.propuestas.isEmpty()){
	         Iterator itx = propuestas.iterator();
	         //System.out.println("Entra a primer while");
	         while (itx.hasNext()){
	           ConvenioInternet convenio =(ConvenioInternet) itx.next();
	           String NombrePropuesta = (String) convenio.getNombrePropuesta();
	           //System.out.println("NOmbre propuesta:"+NombrePropuesta);
	           if (NombrePropuesta.equals(this.nombrePropuesta)){

	            // Collection deudas = (Vector) convenio.getDeudas();
	             ArrayList deudas = (ArrayList) convenio.getDeudasPropuesta();
	             ConvenioMasivo convenioMasivo = convenio.getConvenioMasivo();
	             Vector deudasTemp= new Vector();


	             if (!deudas.isEmpty()){

	               Iterator it = deudas.iterator();
	               long porcentajeMaximo = 0;
	               long porcentajeMaximoMultas = 0;
	               long porcentajeMaximoIntereses = 0;

	               int cuenta=0;
	               long porcentajeMaximoCondonacion=0;
	               long porcentajeMaximoCondonacionMultas=0;
	               long porcentajeMaximoCondonacionIntereses=0;

	               long porcentajeMaximoCondOtorgada=0;

	               while (it.hasNext()){
	                 DeudaWeb deuda = (DeudaWeb) it.next();
                     
	                 /*System.out.println("deuda ------> "+deuda.getTipoFormulario());
	                 System.out.println("deuda.getPorcentajeMaximoCondonacion() ------> "+deuda.getPorcentajeMaximoCondonacion());
	                 System.out.println("deuda.getPorcentajeMaximoCondonacionMultas() ------> "+deuda.getPorcentajeMaximoCondonacionMultas());
	                 System.out.println("deuda.getPorcentajeMaximoCondonacionIntereses() ------> "+deuda.getPorcentajeMaximoCondonacionIntereses());*/
	                 porcentajeMaximoCondonacion = deuda.getPorcentajeMaximoCondonacion();
	                 porcentajeMaximoCondonacionMultas = deuda.getPorcentajeMaximoCondonacionMultas();
	                 porcentajeMaximoCondonacionIntereses = deuda.getPorcentajeMaximoCondonacionIntereses();

	                 if (cuenta==0){

	                   porcentajeMaximo = porcentajeMaximoCondonacion;
	                   porcentajeMaximoMultas = porcentajeMaximoCondonacionMultas;
	                   porcentajeMaximoIntereses = porcentajeMaximoCondonacionIntereses;
	                 } else{
	                   if (porcentajeMaximoCondonacion > porcentajeMaximo){
	                     porcentajeMaximo = porcentajeMaximoCondonacion;
	                   }

	                   if (porcentajeMaximoCondonacionMultas > porcentajeMaximoMultas){
	                     porcentajeMaximoMultas = porcentajeMaximoCondonacionMultas;
	                   }

	                   if (porcentajeMaximoCondonacionIntereses > porcentajeMaximoIntereses){
	                     porcentajeMaximoIntereses = porcentajeMaximoCondonacionIntereses;
	                   }
	                 }
	                 deudasTemp.addElement((DeudaWeb) deuda);
	                 cuenta++;
	               }
	               
	                 /*System.out.println("porcentajeMaximo ------> "+porcentajeMaximo);
	                 System.out.println("porcentajeMaximoMultas ------> "+porcentajeMaximoMultas);
	                 System.out.println("porcentajeMaximoIntereses ------> "+porcentajeMaximoIntereses);
	                 System.out.println("porcentajeMaximoCondonacion ------> "+porcentajeMaximoCondonacion);
	                 System.out.println("porcentajeMaximoCondonacionMultas ------> "+porcentajeMaximoCondonacionMultas);
	                 System.out.println("porcentajeMaximoCondonacionIntereses ------> "+porcentajeMaximoCondonacionIntereses);*/

	               /*aplica condonaci�n*/
	                 
	                 /*********************solo voy a creer al porcentaje de la deuda******************************/
	                 /* if (porcentajeMaximoMultas>porcentajeMaximoIntereses){
	                	  porcentajeMaximoCondOtorgada=porcentajeMaximoMultas;
	                  }else{
	                	  porcentajeMaximoCondOtorgada=porcentajeMaximoIntereses;
	                  }*/
	                 
	                 
	                 /********************************************************************************************/

	               //sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(deudas);
	               sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(deudasTemp);


	               if (convenioMasivo==null){


	                 if (convenio.getPorcentajeCuotaContado().longValue()==0){
	                   convenio.setPorcentajeCuotaContado(new Long(this.perfilSesion.getPorcentajeCuotaContado()));
	                 }

	                 if (convenio.getNumeroCuotas().longValue()==0){
	                   convenio.setNumeroCuotas(convenio.getNumeroMaximoCuotas());
	                 }

	                 sessionGeneraPropuesta.setPorcentajeCondonacion(new Long(porcentajeMaximo));

	                 if(!sessionGeneraPropuesta.getReformaTributariaInput().getIsReformaTributaria().booleanValue()){
	                	 sessionGeneraPropuesta.setPorcentajeCondonacionMultas(new Long(porcentajeMaximoMultas));
	                	 sessionGeneraPropuesta.setPorcentajeCondonacionIntereses(new Long(porcentajeMaximoIntereses));
	                 }else{
	                	 
	                	 //sessionGeneraPropuesta.setPorcentajeCondonacionMultas(new Long(sessionGeneraPropuesta.getReformaTributariaOutput().getMaxCondonacionM().longValue()), new Double(0));
	                	 //sessionGeneraPropuesta.setPorcentajeCondonacionIntereses(new Long(sessionGeneraPropuesta.getReformaTributariaOutput().getMaxCondonacionI().longValue()), new Double(0));     
                        	 //sessionGeneraPropuesta.setPorcentajeCondonacionMultas(new Long(sessionGeneraPropuesta.getReformaTributariaOutput().getMaxCondonacionM().longValue()));
                        	 sessionGeneraPropuesta.setPorcentajeCondonacionMultas(new Long(porcentajeMaximoMultas));
                         
                         	//sessionGeneraPropuesta.setPorcentajeCondonacionIntereses(new Long(sessionGeneraPropuesta.getReformaTributariaOutput().getMaxCondonacionI().longValue()));
                         	sessionGeneraPropuesta.setPorcentajeCondonacionIntereses(new Long(porcentajeMaximoIntereses));     
           
                         	                	 
	                 }

	                 //convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadas());
	                 //ojo este obtiene deudas en memoria 
	                 convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadasCollection());

	                 convenio =  this.setCalcular(convenio.getPorcentajeCuotaContado(),convenio.getNumeroCuotas());


	               }else{

	                 sessionGeneraPropuesta.setPorcentajeCondonacion(new Long(convenioMasivo.getPorcentajeCondonacion()));
	                 sessionGeneraPropuesta.setPorcentajeCondonacionMultas(new Long(convenioMasivo.getPorcentajeCondonacion()));
	                 sessionGeneraPropuesta.setPorcentajeCondonacionIntereses(new Long(convenioMasivo.getPorcentajeCondonacion()));

	                 convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadas());
	                 convenio.setPorcentajeCuotaContado(new  Long(convenioMasivo.getPorcentajeCuotaContado()));
	                 convenio.setNumeroCuotas(new Long(convenioMasivo.getNumeroCuotas()));
	                 convenio = this.setCalcular(new  Long(convenioMasivo.getPorcentajeCuotaContado()),new Long(convenioMasivo.getNumeroCuotas()));

	               }

	               sessionGeneraPropuesta.setClaseCuotasInternet(convenio.getCuotas());

	             }//if

	             convenioRetorno = convenio;
	           }//if

	         }//while

	       }//if


	   return  convenioRetorno;

	  } catch (Exception e) {
		  e.printStackTrace();
	            throw new EJBException("Error GeneraPropuestaInternet.getAplicaCondonacion ::" + e.toString());
	          }
	}

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 */
	    public ConvenioInternet getAplicaCondonacionDem (String CodDemanda) {

	      ConvenioInternet convenioRetorno = null;
	      HashMap conveniosDemanda=new HashMap();
	      conveniosDemanda = (HashMap)this.propuestasDem;
	      HashMap hmDeudasDemanda =new HashMap();
	      hmDeudasDemanda= (HashMap)this.deudasPropuestas;

	      /*Modificaci�n 11-10-2006*/
	      long totalDemanda=0;
	      String tipoImpuesto="";
	      Date fechaEmision=null;
	      /*Fin modificaci�n 11-10-2006*/

	      try{

	          if (!conveniosDemanda.isEmpty()){
	            Set keyDemandas = conveniosDemanda.keySet();
	            Iterator itx = keyDemandas.iterator();

	            while (itx.hasNext()){

	              String vCodDemanda = (String) itx.next();

	              if (CodDemanda.equals(vCodDemanda))
	              {

	                ConvenioInternet convenio =(ConvenioInternet) this.propuestasDem.get(CodDemanda);
	                String NombrePropuesta = (String) convenio.getNombrePropuesta();

	                if (CodDemanda.equals(vCodDemanda))
	                {

	                  if (NombrePropuesta.equals(this.nombrePropuesta)){

	                    Vector deudas = new Vector();

	                    Set keyDeudas = hmDeudasDemanda.keySet();
	                    Iterator itd = keyDeudas.iterator();

	                    while(itd.hasNext())
	                    {
	                      String vCodDemandaDeuda = (String) itd.next();

	                      if (CodDemanda.equals(vCodDemandaDeuda))
	                      {
	                        HashMap deudaTemp = new HashMap();
	                        deudaTemp=(HashMap) deudasPropuestas.get((String) vCodDemandaDeuda);

	                        Set deudasKey = deudaTemp.keySet();
	                        Iterator itdt = deudasKey.iterator();

	                        while(itdt.hasNext())
	                         {
	                          DeudaWeb deuda = (DeudaWeb) deudaTemp.get( (String) itdt.next());

	                          this.deudasPropuestaDemandaActual.put((String)CodDemanda, (DeudaWeb) deuda);
	                          deudas.addElement(deuda);

	                          /*Modificacion 11-10-2006*/
	                          totalDemanda=totalDemanda+deuda.getMonto();
	                          fechaEmision=deuda.getFechaEmision();

	                          if (deuda.getTipoContribuyente()==1)
	                          {
	                            tipoImpuesto = "F";
	                          }
	                          else
	                          {
	                            tipoImpuesto="T";
	                          }
	                          /*Fin modificaci�n 11-10-2006*/
	                        }
	                      }

	                    }


	                    ConvenioMasivo convenioMasivo = convenio.getConvenioMasivo();
	                    if (!deudas.isEmpty()){

	                      Iterator it = deudas.iterator();
	                      long porcentajeMaximo = 0;
	                      long porcentajeMaximoMultas = 0;
	                      long porcentajeMaximoIntereses = 0;

	                      int cuenta=0;
	                      long porcentajeMaximoCondonacion=0;
	                      long porcentajeMaximoCondonacionMultas=0;
	                      long porcentajeMaximoCondonacionIntereses=0;

	                      while (it.hasNext()){
	                        DeudaWeb deuda = (DeudaWeb) it.next();

	                        porcentajeMaximoCondonacion = deuda.getPorcentajeMaximoCondonacion();
	                        porcentajeMaximoCondonacionMultas = deuda.getPorcentajeMaximoCondonacionMultas();
	                        porcentajeMaximoCondonacionIntereses = deuda.getPorcentajeMaximoCondonacionIntereses();

	                        if (cuenta==0){
	                          porcentajeMaximo = porcentajeMaximoCondonacion;
	                          porcentajeMaximoMultas = porcentajeMaximoCondonacionMultas;
	                          porcentajeMaximoIntereses = porcentajeMaximoCondonacionIntereses;
	                        } else{
	                          if (porcentajeMaximoCondonacion > porcentajeMaximo){
	                            porcentajeMaximo = porcentajeMaximoCondonacion;
	                          }

	                          if (porcentajeMaximoCondonacionMultas > porcentajeMaximoMultas){
	                            porcentajeMaximoMultas = porcentajeMaximoCondonacionMultas;
	                          }

	                          if (porcentajeMaximoCondonacionIntereses > porcentajeMaximoIntereses){
	                            porcentajeMaximoIntereses = porcentajeMaximoCondonacionIntereses;
	                          }
	                        }
	                        cuenta++;
	                      }

	                      /*aplica condonaci�n*/

	                      sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(deudas);
	                      if (convenioMasivo==null){

	                        if (convenio.getPorcentajeCuotaContado().longValue()==0){
	                          convenio.setPorcentajeCuotaContado(new Long(this.perfilSesion.getPorcentajeCuotaContado()));
	                        }

	                        /*Modificaci�n 11-10-2006*/
	                        NumeroCuotas numeroCuotas = new NumeroCuotas(this.perfil,   sessionGeneraPropuesta.getTipoConvenio(),sessionGeneraPropuesta.getTipoPago(),(int) this.contribuyente.getIdPersona().longValue(), totalDemanda, tipoImpuesto, fechaEmision, this.getConnection());
	                        convenio.setNumeroCuotas(new Long(numeroCuotas.getNumeroCuotas()));
	                        this.closeConnection();
	                        /*Fin modificaci�n 11-10-2006*/

	                        if (convenio.getNumeroCuotas().longValue()==0){
	                          convenio.setNumeroCuotas(convenio.getNumeroMaximoCuotas());
	                        }

	                        sessionGeneraPropuesta.setPorcentajeCondonacion(new Long(porcentajeMaximo));
	                        sessionGeneraPropuesta.setPorcentajeCondonacionMultas(new Long(porcentajeMaximoMultas));
	                        sessionGeneraPropuesta.setPorcentajeCondonacionIntereses(new Long(porcentajeMaximoIntereses));
	                        convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadas());
	                        convenio =  this.setCalcularConvenioDem(convenio.getPorcentajeCuotaContado(),convenio.getNumeroCuotas(),CodDemanda);

	                       }else{

	                         sessionGeneraPropuesta.setPorcentajeCondonacion(new Long(convenioMasivo.getPorcentajeCondonacion()));
	                         sessionGeneraPropuesta.setPorcentajeCondonacionMultas(new Long(convenioMasivo.getPorcentajeCondonacion()));
	                         sessionGeneraPropuesta.setPorcentajeCondonacionIntereses(new Long(convenioMasivo.getPorcentajeCondonacion()));

	                         convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadas());
	                         convenio.setPorcentajeCuotaContado(new  Long(convenioMasivo.getPorcentajeCuotaContado()));
	                         convenio.setNumeroCuotas(new Long(convenioMasivo.getNumeroCuotas()));
	                         convenio = this.setCalcular(new  Long(convenioMasivo.getPorcentajeCuotaContado()),new Long(convenioMasivo.getNumeroCuotas()));

	                       }

	                       sessionGeneraPropuesta.setClaseCuotasInternet(convenio.getCuotas());

	                     }//if

	                     convenioRetorno = convenio;
	                 }//if
	             }
	         }

	       }//while
	     }//if
	     return  convenioRetorno;

	   } catch (Exception e) {
	     throw new EJBException("Error GeneraPropuestaInternet.getAplicaCondonacionDem ::" + e.toString());
	            }
	 }


	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Establece el nombre de la propuesta que se esta configurando
	  */
	  public void setPropuestaSeleccionada(String nombrePropuesta){
	         this.nombrePropuesta = nombrePropuesta;
	         System.out.println("ver pago total-----> "+ this.getPagoTotalConvenioI(nombrePropuesta));
	         
	  }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Retorna la propuesta que se esta configurando
	   */
	  public ConvenioInternet getPropuestaSeleccionada(){

	  ConvenioInternet convenioRetorno = null;
	  try{

	    if (!this.propuestas.isEmpty()){

	      Iterator itx = propuestas.iterator();

	      while (itx.hasNext()){

	        ConvenioInternet convenio =(ConvenioInternet) itx.next();
	        String NombrePropuesta = (String) convenio.getNombrePropuesta();

	        if (NombrePropuesta.equals(this.nombrePropuesta)){

	          convenioRetorno = convenio;
	        }
	      }
	    } 

	    return  convenioRetorno;

	  }catch (Exception e) {
	         throw new EJBException("Error GeneraPropuestaInternet.getPropuestaSeleccionada ::" + e.toString());
	        }
	  }
	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Se establece porcentaje cuota contado y n�mero de cuotas y se realizan los
	  *  valores de las cuotas
	  *  @param porcentajeCuotaContado Poncentaje de cuota contado
	  *  @param NumeroCuotas N�mero de cuotas Cuotas
	  */
	  public ConvenioInternet setCalcular(Long porcentajeCuotaContado, Long NumeroCuotas, Long montoCuotaContado)throws Exception{

	  ConvenioInternet convenioElegido = null;

	  try{
		  //***************************///
	  
		  if(NumeroCuotas.intValue()>sessionGeneraPropuesta.getReformaTributariaOutput().getMaxCuota().intValue()){
		  throw new Exception("Error GeneraPropuestaInternet n�mero de cuotas mayor a permitido");
		  }
		  
		  //********************************//
		  
		  
		  
	      if (!this.propuestas.isEmpty()){
	        Iterator itx = propuestas.iterator();
	        int cuenta=0;

	        while (itx.hasNext()){

	          ConvenioInternet convenio =(ConvenioInternet) itx.next();
	          String NombrePropuesta = (String) convenio.getNombrePropuesta();


	          if (NombrePropuesta.equals(this.nombrePropuesta)){

	            ArrayList deudasProp = convenio.getDeudasPropuesta();
	            Vector deudasTemp =new Vector();


	            Iterator itdp = deudasProp.iterator();

	            while (itdp.hasNext())
	            {

	              DeudaWeb deu = (DeudaWeb) itdp.next();

	              deudasTemp.addElement((DeudaWeb) deu);
	            }


	            sessionGeneraPropuesta.setPorcentajeCuotaContado(porcentajeCuotaContado);
	            sessionGeneraPropuesta.setNumeroCuotas(NumeroCuotas);
	            sessionGeneraPropuesta.setPagoContado(montoCuotaContado);
	            //sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(convenio.getDeudas());

	            sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(deudasTemp);

	            sessionGeneraPropuesta.setValorTotal();

	           /* if (porcentajeCuotaContado.longValue()==3)
	            {
	              sessionGeneraPropuesta.setPagoContado(new Long(sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue()/36));
	            }
	            else
	            {
	              sessionGeneraPropuesta.setPagoContado(new Long((sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue()*sessionGeneraPropuesta.getPorcentajeCuotaContado().longValue() )/100));
	            }
	*/
	           // System.out.println("********************>>>>>>>>>>>>>>> en ejb----> "+sessionGeneraPropuesta.getMaximoNumeroCuotas());

	            sessionGeneraPropuesta.CalcularPropuesta();

	            convenio.setPorcentajeCuotaContado(sessionGeneraPropuesta.getPorcentajeCuotaContado());;

	            convenio.setNumeroCuotas(sessionGeneraPropuesta.getNumeroCuotas());

	            convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadas());

	            convenio.setCuotas(sessionGeneraPropuesta.getCuotasInternet());
	            convenioElegido = convenio;
	          }
	        }
	      }

	      return  convenioElegido ;

	    }catch (EJBException e){
	       throw new EJBException("Error GeneraPropuestaInternet.setCalcular ::" + e.toString());
	      }
	 }


	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 * Se establece porcentaje cuota contado y n�mero de cuotas y se realizan los !!!!!METODO ESPECIAL LEY 20027
	 *  valores de las cuotas
	 *  @param porcentajeCuotaContado Poncentaje de cuota contado
	 *  @param NumeroCuotas N�mero de cuotas Cuotas
	 */
	 public ConvenioInternet setCalcularLey20027(Long porcentajeCuotaContado, Long NumeroCuotas, Long montoCuotaContado, Long montoLey20027, boolean sinAjuste){

	 ConvenioInternet convenioElegido = null;

	 try{
	     if (!this.propuestas.isEmpty()){
	       Iterator itx = propuestas.iterator();
	       int cuenta=0;

	       while (itx.hasNext()){

	         ConvenioInternet convenio =(ConvenioInternet) itx.next();
	         String NombrePropuesta = (String) convenio.getNombrePropuesta();


	         if (NombrePropuesta.equals(this.nombrePropuesta)){

	           ArrayList deudasProp = convenio.getDeudasPropuesta();
	           Vector deudasTemp =new Vector();


	           Iterator itdp = deudasProp.iterator();

	           while (itdp.hasNext())
	           {

	             DeudaWeb deu = (DeudaWeb) itdp.next();

	             deudasTemp.addElement((DeudaWeb) deu);
	           }


	           sessionGeneraPropuesta.setPorcentajeCuotaContado(porcentajeCuotaContado);
	           sessionGeneraPropuesta.setNumeroCuotas(NumeroCuotas);
	           sessionGeneraPropuesta.setPagoContado(montoCuotaContado);
	           //sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(convenio.getDeudas());

	           sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(deudasTemp);

	           sessionGeneraPropuesta.setSinAjuste(sinAjuste);

	           sessionGeneraPropuesta.setMontoTotalLey20027(montoLey20027.longValue());

	           sessionGeneraPropuesta.setValorTotal();

	          /* if (porcentajeCuotaContado.longValue()==3)
	           {
	             sessionGeneraPropuesta.setPagoContado(new Long(sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue()/36));
	           }
	           else
	           {
	             sessionGeneraPropuesta.setPagoContado(new Long((sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue()*sessionGeneraPropuesta.getPorcentajeCuotaContado().longValue() )/100));
	           }
	*/

	           sessionGeneraPropuesta.CalcularPropuesta();

	           convenio.setPorcentajeCuotaContado(sessionGeneraPropuesta.getPorcentajeCuotaContado());;

	           convenio.setNumeroCuotas(sessionGeneraPropuesta.getNumeroCuotas());

	           convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadas());

	           convenio.setCuotas(sessionGeneraPropuesta.getCuotasInternet());
	           convenioElegido = convenio;
	         }
	       }
	     }

	     return  convenioElegido;

	   }catch (Exception e){
	      throw new EJBException("Error GeneraPropuestaInternet.setCalcular ::" + e.toString());
	     }
	}


	 private ConvenioInternet setCalcular(Long porcentajeCuotaContado, Long NumeroCuotas){

	 ConvenioInternet convenioElegido = null;

	 try{
	     if (!this.propuestas.isEmpty()){
	       Iterator itx = propuestas.iterator();
	       int cuenta=0;

	       while (itx.hasNext()){

	         ConvenioInternet convenio =(ConvenioInternet) itx.next();
	         String NombrePropuesta = (String) convenio.getNombrePropuesta();


	         if (NombrePropuesta.equals(this.nombrePropuesta)){

	           ArrayList deudasProp = convenio.getDeudasPropuesta();
	           Vector deudasTemp =new Vector();


	           Iterator itdp = deudasProp.iterator();

	           while (itdp.hasNext())
	           {

	             DeudaWeb deu = (DeudaWeb) itdp.next();

	             deudasTemp.addElement((DeudaWeb) deu);
	           }


	           sessionGeneraPropuesta.setPorcentajeCuotaContado(porcentajeCuotaContado);
	           sessionGeneraPropuesta.setNumeroCuotas(NumeroCuotas);

	           //sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(convenio.getDeudas());

	           sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(deudasTemp);

	           sessionGeneraPropuesta.setValorTotal();

	           if (porcentajeCuotaContado.longValue()==3)
	           {
	             sessionGeneraPropuesta.setPagoContado(new Long(sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue()/36));
	           }
	           else
	           {
	             sessionGeneraPropuesta.setPagoContado(new Long((sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue()*sessionGeneraPropuesta.getPorcentajeCuotaContado().longValue() )/100));
	           }


	           sessionGeneraPropuesta.CalcularPropuesta();

	           convenio.setPorcentajeCuotaContado(sessionGeneraPropuesta.getPorcentajeCuotaContado());;

	           convenio.setNumeroCuotas(sessionGeneraPropuesta.getNumeroCuotas());

	           //convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadas());
	           //ojo este no recalcula
	           convenio.setDeudas((Vector) sessionGeneraPropuesta.getDeudasSeleccionadasCollection());

	           convenio.setCuotas(sessionGeneraPropuesta.getCuotasInternet());
	           convenioElegido = convenio;
	         }
	       }
	     }

	     return  convenioElegido;

	   }catch (Exception e){
	      throw new EJBException("Error GeneraPropuestaInternet.setCalcular ::" + e.toString());
	     }
	}

	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	 public ConvenioInternet setCalcularConvenioDem(Long porcentajeCuotaContado, Long NumeroCuotas, String CodDemanda){

	 ConvenioInternet convenioElegido = null;
	 HashMap conveniosDemanda= new HashMap();
	 conveniosDemanda=(HashMap) this.propuestasDem;

	 try{
	   if (!conveniosDemanda.isEmpty()){

	     Set keyDemandas = conveniosDemanda.keySet();
	     Iterator itx = keyDemandas.iterator();

	     int cuenta=0;

	     while (itx.hasNext()){

	       String vCodDemanda = (String) itx.next();

	       ConvenioInternet convenio =(ConvenioInternet) conveniosDemanda.get(vCodDemanda);
	       String NombrePropuesta = "";

	       if (convenio == null)
	       {
	         NombrePropuesta = "";
	       }
	       else
	       {
	         NombrePropuesta = (String) convenio.getNombrePropuesta();
	       }

	       if (CodDemanda.equals(vCodDemanda))
	       {
	         if (NombrePropuesta.equals(this.nombrePropuesta)) {

	           ArrayList deudasProp=(ArrayList) convenio.getDeudasPropuesta();
	           Vector deudasTemp = new Vector();

	           sessionGeneraPropuesta.setPorcentajeCuotaContado(porcentajeCuotaContado);
	           sessionGeneraPropuesta.setNumeroCuotas(NumeroCuotas);

	           Iterator itdp = deudasProp.iterator();

	           while (itdp.hasNext())
	           {
	               DeudaWeb deu = (DeudaWeb) itdp.next();

	               deudasTemp.addElement((DeudaWeb) deu);

	           }
	           //sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(convenio.getDeudas());
	           sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(deudasTemp);
	           sessionGeneraPropuesta.setValorTotal();

	           if (porcentajeCuotaContado.longValue()==3)
	            {
	              sessionGeneraPropuesta.setPagoContado(new Long(sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue()/36));
	            }
	            else
	            {
	              sessionGeneraPropuesta.setPagoContado(new Long((sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue()*sessionGeneraPropuesta.getPorcentajeCuotaContado().longValue() )/100));
	            }

	           //sessionGeneraPropuesta.setPagoContado(new Long((sessionGeneraPropuesta.getTotalPagarConCondonacion().longValue() * sessionGeneraPropuesta.getPorcentajeCuotaContado().longValue()) / 100));

	           sessionGeneraPropuesta.CalcularPropuesta();

	           convenio.setPorcentajeCuotaContado(sessionGeneraPropuesta.getPorcentajeCuotaContado()); ;
	           convenio.setNumeroCuotas(sessionGeneraPropuesta.getNumeroCuotas());

	           convenio.setDeudas( (Vector) sessionGeneraPropuesta.getDeudasSeleccionadas());
	           convenio.setCuotas(sessionGeneraPropuesta.getCuotasInternet());
	           convenioElegido = convenio;
	         }
	       }
	     }
	   }
	   return  convenioElegido;

	 }catch (Exception e){
	      throw new EJBException("Error GeneraPropuestaInternet.setCalcular ::" + e.toString());
	    }
	}


	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Inserta la (s) propuesta (s) de la demanda seleccionada
	  *  @return - boolean indicando exito(true) o fracaso(false) de la inserci�n
	  */
	 public Long insertaPropuestasDemanda(){

	   Long codigoRetorno = new Long(0);
	   Vector retornoPropuestas = new Vector();
	   boolean Error = false;

	   try{
	      if (this.CodDemanda!=null){

	         Set setDemandasKey = this.demandas.keySet();
	         Iterator it = setDemandasKey.iterator();

	         while (it.hasNext()){

	           String elemento = (String) it.next();
	           HashMap elementoDemandas = (HashMap) this.demandas.get((String) elemento);

	           if (this.CodDemanda.equals(elementoDemandas.get("CodDemanda"))){

	              HashMap propuestasDemanda = new HashMap();
	              propuestasDemanda = (HashMap) this.getPropuestasDemandaActual();

	              if (!propuestasDemanda.isEmpty()){

	                  Set setPropuestasKey = propuestasDemanda.keySet();
	                  Iterator itp = setPropuestasKey.iterator();

	                  while (itp.hasNext()){

	                      String vCodDemanda = (String) itp.next();
	                      ConvenioInternet convenio = (ConvenioInternet) propuestasDemanda.get(vCodDemanda);
	                      this.nombrePropuesta = convenio.getNombrePropuesta();
	                      codigoRetorno = this.insertaPropuestaDem();

	                      if (codigoRetorno.longValue()==0){
	                          Error = true;
	                        }
	                      }
	                    }
	                  }//if this
	                }
	              }
	      return codigoRetorno;

	      }catch (Exception e){
	         throw new EJBException("Error GeneraPropuestaInternet.insertaPropuestasDemanda ::" + e.toString());
	        }
	 }

	 /*Lista las comunas */
	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	 public Collection getListarComunas()
	 {
	   PreparedStatement ps = null;
	   Connection conn = null;
	   Vector retorno = new Vector();

	   try {

	     conn = this.getConnection();

	     CallableStatement  call = conn.prepareCall("{? = call Conveniosinternet.ListaComunas}");

	     call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	     call.execute();

	     ResultSet rs = (ResultSet) call.getObject(1);

	     while(rs.next())
	     {
	       HashMap sqlRetorno = new HashMap();

	       sqlRetorno.put("COD_COMUNA",(String) rs.getString(1));
	       sqlRetorno.put("NOMBRE",(String) rs.getString(2));

	       retorno.addElement(sqlRetorno);
	     }

	     rs.close();
	     call.close();

	   }
	   catch(SQLException e) {
	       throw new EJBException("Error ejecutando el SQL " + e.toString());
	     }
	     finally {
	      try {
	        conn.close();
	      }
	      catch (SQLException ex) {
	      }
	      }
	     return retorno;
	   }


	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *  inserta la propuesta  de convenio
	 *   @return - Long que indica exito(>0) o fracaso(0) de la inserci�n
	 */
	 public Long insertaPropuesta(){

	   Long CodigoPropuesta = new Long(0);

	   try{
	     if (!this.propuestas.isEmpty()){

	       Iterator itx = propuestas.iterator();
	       int cuenta=0;

	       while (itx.hasNext()){

	         ConvenioInternet convenio =(ConvenioInternet) itx.next();
	         String NombrePropuesta = (String) convenio.getNombrePropuesta();

	         if (NombrePropuesta.equals(this.nombrePropuesta)){

	           sessionGeneraPropuesta.setConvenioMasivoInternet(convenio.getConvenioMasivo());
	           sessionGeneraPropuesta.setPorcentajeCuotaContado(convenio.getPorcentajeCuotaContado());
	           sessionGeneraPropuesta.setNumeroCuotas(convenio.getNumeroCuotas());
	           sessionGeneraPropuesta.setIdTesoreria(this.IdTesoreria);
	           sessionGeneraPropuesta.setCodigoFuncionario(this.IdFuncionario);
	           sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(convenio.getDeudas());
	           sessionGeneraPropuesta.setClaseCuotasInternet(convenio.getCuotas());
	           sessionGeneraPropuesta.setTipoConvenio(convenio.getCodTipoConvenio());
	           sessionGeneraPropuesta.setAceptaCompensacion("S");
	           sessionGeneraPropuesta.setIdCodUsuario(this.IdFuncionario);
	           CodigoPropuesta = sessionGeneraPropuesta.getAceptacion();
	           /******************************AQUI REALIZA REGISTRO DE ACTIVIDAD LOG**************************/
	           sessionGeneraPropuesta.registraPoliticasCond(CodigoPropuesta, sessionGeneraPropuesta.getReformaTributariaInput(), convenio.getDeudas(), sessionGeneraPropuesta.getReformaTributariaOutput());
	           /**************************************************************************************************/
	           itx.remove();
	         }
	       }
	  }
	  /*remueva deudas de la propuesta insertadao de la colleccion deudasContribuyente*/
	  if (this.deudasContribuyente.size()>0){
	     Iterator itDeudas = this.deudasContribuyente.iterator();

	     while (itDeudas.hasNext()){

	       DeudaWeb deuda = (DeudaWeb) itDeudas.next();
	       String grupo = deuda.getGrupo();

	       if (grupo.equals(this.nombrePropuesta)){
	         itDeudas.remove();
	       }
	     }
	   }


	   return CodigoPropuesta;
	 }catch (Exception e){
	       throw new EJBException("Error GeneraPropuestaInternet.insertaPropuesta ::" + e.toString());
	      }
	 }

	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	 public Long insertaPropuestaDem(){

	   Long CodigoPropuesta = new Long(0);
	   HashMap hmPropuestasDemanda = new HashMap();

	   try{

	     hmPropuestasDemanda = (HashMap)this.getPropuestasDemandaActual();

	     if (!hmPropuestasDemanda.isEmpty()){

	       Set setPropuestasKey = hmPropuestasDemanda.keySet();
	       Iterator itx = setPropuestasKey.iterator();
	       int cuenta=0;

	       while (itx.hasNext()){

	         String vCodDemanda = (String) itx.next();
	         ConvenioInternet convenio =(ConvenioInternet) hmPropuestasDemanda.get(vCodDemanda);
	         String NombrePropuesta = (String) convenio.getNombrePropuesta();

	         if (NombrePropuesta.equals(this.nombrePropuesta)){

	           sessionGeneraPropuesta.setConvenioMasivoInternet(convenio.getConvenioMasivo());
	           sessionGeneraPropuesta.setPorcentajeCuotaContado(convenio.getPorcentajeCuotaContado());
	           sessionGeneraPropuesta.setNumeroCuotas(convenio.getNumeroCuotas());
	           sessionGeneraPropuesta.setIdTesoreria(this.IdTesoreria);
	           sessionGeneraPropuesta.setCodigoFuncionario(this.IdFuncionario);
	           sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(convenio.getDeudas());
	           sessionGeneraPropuesta.setClaseCuotasInternet(convenio.getCuotas());
	           sessionGeneraPropuesta.setTipoConvenio(convenio.getCodTipoConvenio());
	           sessionGeneraPropuesta.setAceptaCompensacion("S");
	           CodigoPropuesta = sessionGeneraPropuesta.getAceptacion();

	         }
	       }
	   }

	   /*remueva deudas de la propuesta insertadao de la colleccion deudasContribuyente*/
	   if (this.deudasContribuyente.size()>0){

	     Iterator itDeudas = this.deudasContribuyente.iterator();

	     while (itDeudas.hasNext()){

	       DeudaWeb deuda = (DeudaWeb) itDeudas.next();
	       String grupo = deuda.getGrupo();

	       if (grupo.equals(this.nombrePropuesta)){
	         itDeudas.remove();
	       }
	     }
	   }


	   return CodigoPropuesta;
	  }catch (Exception e){
	        throw new EJBException("Error GeneraPropuestaInternet.insertaPropuesta ::" + e.toString());
	       }
	  }


	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *  inserta la propuesta  Territorial de convenio
	 *   @return - Long que indica exito(>0) o fracaso(0) de la inserci�n
	 */
	 public Long insertaPropuestaTerritorial(){

	 Long CodigoPropuesta = new Long(0);

	 try{
	  if (!this.propuestas.isEmpty()){

	   Iterator itx = propuestas.iterator();
	   int cuenta=0;

	   while (itx.hasNext()){

	        ConvenioInternet convenio =(ConvenioInternet) itx.next();
	        String NombrePropuesta = (String) convenio.getNombrePropuesta();

	        if (NombrePropuesta.equals(this.nombrePropuesta)){

	          sessionGeneraPropuesta.setTipoPago(convenio.getCodTipoPago());

	          sessionGeneraPropuesta.setConvenioMasivoInternet(convenio.getConvenioMasivo());
	          sessionGeneraPropuesta.setPorcentajeCuotaContado(convenio.getPorcentajeCuotaContado());
	          sessionGeneraPropuesta.setNumeroCuotas(convenio.getNumeroCuotas());
	          sessionGeneraPropuesta.setIdTesoreria(this.IdTesoreria);
	          sessionGeneraPropuesta.setCodigoFuncionario(this.IdFuncionario);
	          sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(convenio.getDeudas());
	          sessionGeneraPropuesta.setClaseCuotasInternet(convenio.getCuotas());
	          sessionGeneraPropuesta.setTipoConvenio(convenio.getCodTipoConvenio());
	          sessionGeneraPropuesta.setAceptaCompensacion("S");

	          //La Voy a tratar de ingresar eliminada...
	          CodigoPropuesta = sessionGeneraPropuesta.getAceptacion(); // Ojo Ac�!!!
	          itx.remove();
	        }
	      }
	    }

	    /*remueva deudas de la propuesta insertadao de la colleccion deudasContribuyente*/
	    if (this.deudasContribuyente.size()>0){
	      Iterator itDeudas = this.deudasContribuyente.iterator();

	      while (itDeudas.hasNext()){

	        DeudaWeb deuda = (DeudaWeb) itDeudas.next();
	        String grupo = deuda.getGrupo();

	        if (grupo.equals(this.nombrePropuesta)){
	            itDeudas.remove();
	         }
	     }
	  }

	  return CodigoPropuesta;
	 }catch (Exception e){
	       throw new EJBException("Error GeneraPropuestaInternet.insertaPropuestaTerritorial ::" + e.toString());
	      }
	 }

	    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	public Long insertaPropuestaDemandaTerritorial(){

	   Long CodigoPropuesta = new Long(0);
	   HashMap hmPropuestasDemanda = new HashMap();

	   try{
	     hmPropuestasDemanda = (HashMap)this.getPropuestasDemandaActual();

	     if (!hmPropuestasDemanda.isEmpty()){

	       Set setPropuestasKey = hmPropuestasDemanda.keySet();
	       Iterator itx = setPropuestasKey.iterator();
	       int cuenta=0;

	       while (itx.hasNext()){
	         String vCodDemanda = (String) itx.next();
	         ConvenioInternet convenio =(ConvenioInternet) hmPropuestasDemanda.get(vCodDemanda);

	         String NombrePropuesta = (String) convenio.getNombrePropuesta();
	         if (NombrePropuesta.equals(this.nombrePropuesta)){

	           sessionGeneraPropuesta.setTipoPago(convenio.getCodTipoPago());

	           sessionGeneraPropuesta.setConvenioMasivoInternet(convenio.getConvenioMasivo());
	           sessionGeneraPropuesta.setPorcentajeCuotaContado(convenio.getPorcentajeCuotaContado());
	           sessionGeneraPropuesta.setNumeroCuotas(convenio.getNumeroCuotas());
	           sessionGeneraPropuesta.setIdTesoreria(this.IdTesoreria);
	           sessionGeneraPropuesta.setCodigoFuncionario(this.IdFuncionario);
	           sessionGeneraPropuesta.setDeudasContribuyenteSeleccionadasInternet(convenio.getDeudas());
	           sessionGeneraPropuesta.setClaseCuotasInternet(convenio.getCuotas());
	           sessionGeneraPropuesta.setTipoConvenio(convenio.getCodTipoConvenio());
	           sessionGeneraPropuesta.setAceptaCompensacion("S");

	           //La Voy a tratar de ingresar eliminada...
	           CodigoPropuesta = sessionGeneraPropuesta.getAceptacion(); // Ojo Ac�!!!
	           itx.remove();
	         }
	       }
	     }

	     /*remueva deudas de la propuesta insertadao de la colleccion deudasContribuyente*/
	     if (this.deudasContribuyente.size()>0){

	       Iterator itDeudas = this.deudasContribuyente.iterator();

	       while (itDeudas.hasNext()){

	         DeudaWeb deuda = (DeudaWeb) itDeudas.next();
	         String grupo = deuda.getGrupo();

	         if (grupo.equals(this.nombrePropuesta)){
	            itDeudas.remove();
	         }
	     }
	  }


	  return CodigoPropuesta;
	 }catch (Exception e){
	       throw new EJBException("Error GeneraPropuestaInternet.insertaPropuestaTerritorial ::" + e.toString());
	      }
	 }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	public Long eliminaPropuestaInternet()
	 {
	   Long CodigoPropuesta = new Long(0);

	   try
	   {
	     CodigoPropuesta = sessionGeneraPropuesta.getEliminada();
	   }catch(Exception e){
	     throw new EJBException("Error GeneraPropuestaInternet.eliminaPropuestaTerritorial ::" + e.toString());
	   }


	   return CodigoPropuesta;
	 }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *Establece el nombre de la p�gina
	 * @param nombrePagina nombre de la p�gina
	 */
	public void setNombrePagina(String nombrePagina){
	  this.nombrePagina = nombrePagina;
	}

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna el nombre de la p�gina
	 * @return nombre de la p�gina
	 */
	public String getNombrePagina(){
	  return this.nombrePagina;
	}

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Verifica la existencia de rol bien ra�z en la tabla tg_roles
	 *  @return boolean - indicando si existe rol bien raiz(true) o no(false)
	 */
	public boolean verificaRolBienRaiz(String RolBienRaiz) throws SQLException {

	  PreparedStatement pstmt;
	  Connection connection = null;
	  ResultSet rs = null;
	  boolean retorno = false;

	  try {
	    /* Deudas territoriales de Cut replica  por rol bien ra�z*/
	    connection = this.getConnection();

	    pstmt = connection.prepareStatement("select count(cod_rol) from tg_roles where rol_bien_raiz=?" +
	                                        "  and cora_o_rol='R' ");

	    pstmt.setString(1, RolBienRaiz);

	    int cantidad = 0;
	    rs = pstmt.executeQuery();

	    if (rs.next()){
	      cantidad = rs.getInt(1);
	    }

	    if (cantidad >0){ retorno = true;}

	    rs.close();
	    pstmt.close();
	    rs=null;
	    pstmt=null;


	  } catch (SQLException eSQL) {
	      throw new SQLException ("Error SQL GeneraPropuestaInternet.verificaRolBienRaiz ::" + eSQL.toString());
	    }
	    finally{
	      connection.close();
	    }
	 return retorno;
	}

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna los datos del rol bien ra�z y llena el vector deudasContribuyente
	 *  con las deudas territoriales asociadas al  rol bien ra�z
	 * @return Collection - con los datos del rol bien ra�z
	 */
	public Collection getDatosyDeudasRolBienRaiz(String RolBienRaiz,String estado) throws SQLException {

	  CallableStatement call;
	  Connection connection = null;
	  Vector retorno = new Vector();
	  ResultSet rs = null;

	  try {
	    /* Deudas territoriales de Cut replica  por rol bien ra�z*/
	    connection = this.getConnection();
	    call = connection.prepareCall("{? = call deudasContribuyente.fCurDeudasCutRol(?,?)}");
	    call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	    call.setLong(2,  (new Long(RolBienRaiz)).longValue());
	    if (estado.equals("S")){

	      call.setString(3, "S");
	    }
	    else{
	      call.setString(3, "T");
	    }

	    call.execute();
	    rs = (ResultSet) call.getObject(1);

	    while (rs.next()){

	      HashMap sqlRetorno = new HashMap();
	      sqlRetorno.put("Calle",(String) StringExt.nuloAVacio(rs.getString(1)));
	      sqlRetorno.put("Numero",(String) StringExt.nuloAVacio(rs.getString(2)));
	      sqlRetorno.put("Depto",(String) StringExt.nuloAVacio(rs.getString(3)));
	      sqlRetorno.put("Block",(String) StringExt.nuloAVacio(rs.getString(4)));
	      sqlRetorno.put("Villa",(String) StringExt.nuloAVacio(rs.getString(5)));
	      sqlRetorno.put("Predio",(String) StringExt.nuloAVacio(rs.getString(6)));
	      sqlRetorno.put("Comuna",(String) StringExt.nuloAVacio(rs.getString(7)));
	      sqlRetorno.put("CantidadDeudas",(Long) new Long(rs.getLong(9)));
	      this.RutRol = (String) (StringExt.nuloAVacio(rs.getString(10)) + "-" + StringExt.nuloAVacio(rs.getString(11)) + "-" + StringExt.nuloAVacio(rs.getString(12)));
	      sqlRetorno.put("Manzana",(String) StringExt.nuloAVacio(rs.getString(13)));
	      sqlRetorno.put("TipoCalle",(String) StringExt.nuloAVacio(rs.getString(14)));
	      retorno.addElement(sqlRetorno);
	    }
	    rs.close();
	    call.close();
	    rs=null;
	    call=null;


	  } catch (SQLException eSQL) {
	    throw new SQLException ("Error SQL GeneraPropuestaInternet.getDatosyDeudasRolBienRaiz ::" + eSQL.toString());
	  }
	  finally{
	    connection.close();
	  }


	  try{
	    if (retorno.size()>0){
	      if (estado.equals("S")){
	        this.getDeudasTerritoriales(RolBienRaiz, "S");
	      }
	      else
	      {
	        this.getDeudasTerritoriales(RolBienRaiz, "T");
	      }
	    }

	  } catch (Exception e) {
	    throw new EJBException ("Error GeneraPropuestaInternet.getDeudasTerritoriales ::" + e.toString());
	  }

	  return retorno;
	}

    /** 
   	 * Establece las deudas del contribuyente, son las deudas territoriales asociadas
	  *  al rol bien ra�z
	  */
	 private void getDeudasTerritoriales(String RolBienRaiz,String estado) throws RemoteException, Exception {

		
	   Vector deudasContribuyenteTemp = new Vector();
	   Vector deudasConvenioMasivo = new Vector();
	   HashMap parametros = new HashMap();

	   try{
	     parametros.put("RUT", new Long(RolBienRaiz));
	     if (estado.equals("S")){
	       parametros.put("ESTADOCOBRANZA","S");
	     }else
	     {
	        parametros.put("ESTADOCOBRANZA","T");
	     }
	     this.InicializarContexto();
	     sessionGeneraPropuesta.addRutRol(new Long(RolBienRaiz));

	     deudasContribuyenteTemp.addAll((Vector) sessionGeneraPropuesta.getDeudasContribuyente(parametros));
	     deudasContribuyente.clear();

	     Iterator it = deudasContribuyenteTemp.iterator();
	     String  grupoAnterior="";

	     System.out.println("<-------------------------------INTERNET TERRITORIAL--------------------------------------->");
	     System.out.println("VER FLAG----> "+sessionGeneraPropuesta.getReformaTributariaInput().getIsReformaTributaria());
	     ReformaTributariaInput reformaTributariaTerr = sessionGeneraPropuesta.getReformaTributariaInput();
	     System.out.println("veo el rut del contribuyente en territoriales--> "+this.contribuyente.getRut());
		//internet territorial
	     reformaTributariaTerr.setIsReformaTributaria(new Boolean(true));
	     reformaTributariaTerr.setRutContribuyente(this.contribuyente.getRut());
	     reformaTributariaTerr.setBeneficioInternet(new Boolean(true));
	     reformaTributariaTerr.setIsCanalPresencial(new Boolean(true));        		
	     reformaTributariaTerr.setPagoTotalConvenio(new Boolean(true));
	     reformaTributariaTerr.setIsIntranet(new Boolean(false));
	     reformaTributariaTerr.setPagoContado(new Boolean(false));//no se puede realzar por internet
	     sessionGeneraPropuesta.setReformaTributariaInput(reformaTributariaTerr);
	     sessionGeneraPropuesta.setPagoTotal(new Boolean(true));//agrego esto ya que por internet siempre es pago total;
	     System.out.println("<-----------------------------------FIN INTERNET TERRITORIAL-------------------------------------------->");	 	     
	     
	     while(it.hasNext()) {
	         DeudaWeb elemento = (DeudaWeb) it.next();
	         String grupo = elemento.getGrupo();
             System.out.println("el grupo es --> "+grupo);
	         if (Long.valueOf(RolBienRaiz).longValue() == elemento.getRutRol()){

	           //if ("Art. 192 Territorial LEY 20.460".equals(grupo)||"Art. 192 Territorial".equals(grupo)||"Ley Territorial Reforma Tributaria 2012".equals(grupo) || "M".equals(elemento.getOrigen())) {
	             //Nueva Ley 2014 convenios 36 cuotas CLH y CCC
	        	// if ("Art. 192 Territorial LEY 20.460".equals(grupo)||"Art. 192 Territorial".equals(grupo)||"Ley Territorial Reforma Tributaria 2014".equals(grupo) || "M".equals(elemento.getOrigen())) {
	        	   if (("C".equals(elemento.getOrigen()) || "M".equals(elemento.getOrigen())) && ("S".equals(elemento.getPosibilidadConvenio()))){
	                    /* agrega a vector de convenio masivo*/
	                    if ("M".equals(elemento.getOrigen())){
	                       deudasConvenioMasivo.addElement(elemento);
	                    }
	                    deudasContribuyente.addElement(elemento);
	                 }
	            /*bandera para decir que le convenio masivo puede tener deudas en convenios activos*/
	            if ("M".equals(elemento.getOrigen()) && "N".equals(elemento.getPosibilidadConvenio()) && "N".equals(elemento.getPosibilidadCondonacion())){
	                posibilidaDeudasConveniosActivos = true;
	            }
	          //}
	       }
	       it.remove();
	    }


	      deudasContribuyenteTemp.clear();
	      deudasContribuyenteTemp = null;

	     //**Elimina deudas para convenios normales que est�n para un convenio masivo *//
	     if (!deudasConvenioMasivo.isEmpty()){
	        Iterator itera = deudasConvenioMasivo.iterator();

	        while(itera.hasNext()) {
	           DeudaWeb deudaMasivo = (DeudaWeb) itera.next();
	           Iterator iteraM = deudasContribuyente.iterator();

	           while(iteraM.hasNext()) {
	              DeudaWeb elemento = (DeudaWeb) iteraM.next();

	              if (deudaMasivo.equals(elemento) && !"M".equals(elemento.getOrigen())){
	                 iteraM.remove();
	              }
	           }
	        }
	     }
	    deudasConvenioMasivo = null;

	   /*elimina fechas nulas y crea grupos*/
	     this.eliminaDeudasFechaNula();
	     System.out.println("pase elimina deuda");
	     sessionGeneraPropuesta.setDeudasContribuyenteInternet(this.deudasContribuyente);

	  }catch(Exception e) {
	            throw new EJBException("Error getDeudasContribuyenteTerritoriales ::" + e.toString());
	   }
	 }

	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna el c�digo de la demanda seleccionada
	 *  @return String - c�digo de la demanda
	 */
	public String getDemandaSeleccionada(){
	  return this.CodDemanda;
	}


    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna la demanda que se esta configurando
	 *  @return HashMap - que contiene los datos de la Demanda
	 */
	public HashMap getDemandaActiva(){
	return this.demandaActiva;
	}

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna  las demandas
	 * @return HashMap - que contiene los datos de las demandas judiciales
	 * c�digo de la demanda, numero de expediente, a�o del expediente
	 * comuna, rutrol, etapa, propuestas
	 */
	public HashMap getDemandas(){

	  Vector demandasRetorno = new Vector();
	  HashMap propuestasRetorno = new HashMap();
	  Connection conn= null;

	  try{
	    this.InicializarContexto();

	    CallableStatement  call = null;
	    ResultSet rs = null;
	    Vector retorno3 = new Vector();
	    retorno3.clear();

	    conn = this.getConnection();

	    call = conn.prepareCall("{? = call DeudasContribuyente.ListaDemandasRolBienRaiz(?)}");
	    call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	    call.setString(2, contribuyente.getRut().toString());
	    call.execute();

	    rs = (ResultSet) call.getObject(1);

	    while (rs.next()) {

	      HashMap sqlRetorno = new HashMap();

	      sqlRetorno.put("CodDemanda",(Long) new Long(rs.getLong(1)));/* c�digo demanda */
	      sqlRetorno.put("NroExpedienteAdm",(Long) new Long(rs.getLong(2))); /* nro expediente adm */
	      sqlRetorno.put("AgnoExpedienteAdm",(Long) new Long(rs.getLong(3)));/* a�o expediente adm */
	      sqlRetorno.put("Comuna",(String) rs.getString(4));/* comuna */
	      sqlRetorno.put("RutRol",(String) rs.getString(5));/* rut rol */
	      sqlRetorno.put("Etapa",(String) rs.getString(6));/* etapa */
	      sqlRetorno.put("Seleccionada",(String) "N");/* seleccionada */

	      retorno3.addElement(sqlRetorno);
	    }

	    demandasRetorno.addAll(retorno3);

	    rs = null;
	    call = null;

	    if (!demandasRetorno.isEmpty()){

	      Iterator it = demandasRetorno.iterator();

	      while (it.hasNext()){

	        HashMap elemento = (HashMap) it.next();

	        Long CodDemanda = (Long) elemento.get("CodDemanda");
	        Long NroExpedienteAdm = (Long) elemento.get("NroExpedienteAdm");
	        Long AgnoExpedienteAdm = (Long) elemento.get("AgnoExpedienteAdm");
	        String Comuna = (String) elemento.get("Comuna");
	        String RutRol = (String) elemento.get("RutRol");
	        String Etapa = (String) elemento.get("Etapa");
	        propuestasRetorno = (HashMap) this.getPropuestasDem(CodDemanda.toString());

	        if (propuestasRetorno.size()>0){

	          HashMap structDemanda = new HashMap();

	          structDemanda.put("CodDemanda",(String) CodDemanda.toString());
	          structDemanda.put("NroExpedienteAdm",(String) NroExpedienteAdm.toString());
	          structDemanda.put("AgnoExpedienteAdm",(String) AgnoExpedienteAdm.toString());
	          structDemanda.put("Comuna",(String) Comuna);
	          structDemanda.put("RutRol",(String) RutRol);
	          structDemanda.put("Etapa",(String) Etapa);

	          this.demandas.put((String) CodDemanda.toString(),(HashMap) structDemanda);

	        }
	      }
	    }


	  }catch(Exception e) {
	        throw new EJBException("Error getDemandas ::" + e.toString());
	   }
	   finally{
	     try {
	       conn.close();

	     }
	     catch (SQLException ex) {
	     }
	     return demandas;
	   }

	}

    /** 
	 * Agregar datos de las demandas a estructura de retorno
	 *
	 */
	private void addDemandas(String CodDemanda,
	                    String NroExpedienteAdm,
	                    String AgnoExpedienteAdm,
	                    String Comuna,
	                    String RutRol,
	                    String Etapa,
	                    Vector Propuestas){

	  HashMap structDemanda = new HashMap();

	  structDemanda.put("CodDemanda",(String) CodDemanda);
	  structDemanda.put("NroExpedienteAdm",(String) NroExpedienteAdm);
	  structDemanda.put("AgnoExpedienteAdm",(String) AgnoExpedienteAdm);
	  structDemanda.put("Comuna",(String) Comuna);
	  structDemanda.put("RutRol",(String) RutRol);
	  structDemanda.put("Etapa",(String) Etapa);
	  structDemanda.put("Propuestas",(Vector) Propuestas);


	  this.demandas.put((String) CodDemanda,(HashMap) structDemanda);

	}

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que aplica condonaci�n sobre las deudas de las
	 *  propuestas de la demanda seleccionada.
	 */

	public HashMap getDetalleDemanda(String CodDemanda){
	try{




	   HashMap retorno = new HashMap();
	   HashMap hmDemandas = new HashMap();

	   HashMap convenioDemanda = new HashMap();
	   convenioDemanda = (HashMap) this.getPropuestaDemanda();

	   propuestasDemandaActual=null;propuestasDemandaActual=new HashMap();propuestasDemandaActual.clear();
	   deudasPropuestaDemandaActual=null;deudasPropuestaDemandaActual=new HashMap();deudasPropuestaDemandaActual.clear();

	   hmDemandas = (HashMap)this.demandas;
	   Set setDemandasKey = hmDemandas.keySet();
	   Iterator it = setDemandasKey.iterator();
	   ConvenioInternet convenioRetorno = null;


	   while (it.hasNext()){

	     String elemento = (String) it.next();
	     HashMap elementoDemandas = (HashMap) hmDemandas.get((String) elemento);

	     if (CodDemanda.equals(elementoDemandas.get("CodDemanda"))){
	        this.CodDemanda = CodDemanda;
	        this.demandaActiva = (HashMap) elementoDemandas;

	        if (!convenioDemanda.isEmpty()){
	          Set setPropuestasKey = convenioDemanda.keySet();
	          Iterator itp = setPropuestasKey.iterator();

	          while (itp.hasNext()){

	             String vCodDemanda = (String) itp.next();

	             if (CodDemanda.equals(vCodDemanda))
	             {
	               ConvenioInternet convenio = (ConvenioInternet) convenioDemanda.get((String) CodDemanda);
	               this.setPropuestaSeleccionada(convenio.getNombrePropuesta());

	               convenioRetorno = this.getAplicaCondonacionDem(vCodDemanda);
	               this.propuestasDemandaActual.put((String)CodDemanda,(ConvenioInternet) convenioRetorno);

	             }


	           }
	         }

	         retorno = elementoDemandas;

	       }


	     }

	  return retorno;

	 }catch(Exception e) {
	        throw new EJBException("Error getDetalleDemanda ::" + e.toString());
	   }
	}

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que aplica la condonaci�n sobre las deudas de las propuesta
	 *  seleccionada
	 *  @return ConvenioInternet - clase con los datos actualizados
	 */
	public ConvenioInternet getDetallePropuesta(Collection propuestas,String nombrePropuesta){

	ConvenioInternet convenio = null;

	try{
	    this.propuestas = (Vector) propuestas;

	    if (!propuestas.isEmpty()){
	      Iterator it = propuestas.iterator();

	      while (it.hasNext()){

	        convenio = (ConvenioInternet) it.next();

	        if (nombrePropuesta.equals(convenio.getNombrePropuesta())){
	            this.setPropuestaSeleccionada(nombrePropuesta);
	            convenio = this.getAplicaCondonacion();
	        }
	      }
	   }
	 return convenio;
	 }catch(Exception e) {
	        throw new EJBException("getDetallePropuesta ::" + e.toString());
	   }

	}


    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna los datos del rol bien ra�z y llena el vector demandas
	 *  con las demandas territoriales asociadas al  rol bien ra�z
	 * @param RolBienRaiz - corresponde al rol bien ra�z del que se listan las demandas
	 * @return Collection - con los datos del rol bien ra�z
	 */
	public Collection getDatosyDemandasRolBienRaiz(String RolBienRaiz) throws SQLException {

	  CallableStatement call;
	  Connection connection = null;
	  Vector retorno = new Vector();
	  Vector retorno3 = new Vector();
	  ResultSet rs = null;
	  Vector demandasRetorno = new Vector();

	  try {
	          /* Deudas territoriales de Cut replica  por rol bien ra�z*/
	          connection = this.getConnection();
	          call = connection.prepareCall("{? = call deudasContribuyente.fCurDeudasCutRol(?,?)}");
	          call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	          call.setLong(2,  (new Long(RolBienRaiz)).longValue());
	          call.setString(3, "S");

	          call.execute();
	          rs = (ResultSet) call.getObject(1);

	          while (rs.next()){

	              HashMap sqlRetorno = new HashMap();
	              sqlRetorno.put("Calle",(String) StringExt.nuloAVacio(rs.getString(1)));
	              sqlRetorno.put("Numero",(String) StringExt.nuloAVacio(rs.getString(2)));
	              sqlRetorno.put("Depto",(String) StringExt.nuloAVacio(rs.getString(3)));
	              sqlRetorno.put("Block",(String) StringExt.nuloAVacio(rs.getString(4)));
	              sqlRetorno.put("Villa",(String) StringExt.nuloAVacio(rs.getString(5)));
	              sqlRetorno.put("Predio",(String) StringExt.nuloAVacio(rs.getString(6)));
	              sqlRetorno.put("Comuna",(String) StringExt.nuloAVacio(rs.getString(7)));
	              sqlRetorno.put("CantidadDeudas",(Long) new Long(rs.getLong(9)));
	              this.RutRol = (String) (StringExt.nuloAVacio(rs.getString(10)) + "-" + StringExt.nuloAVacio(rs.getString(11)) + "-" + StringExt.nuloAVacio(rs.getString(12)));
	              sqlRetorno.put("Manzana",(String) StringExt.nuloAVacio(rs.getString(13)));
	              sqlRetorno.put("TipoCalle",(String) StringExt.nuloAVacio(rs.getString(14)));
	              retorno.addElement(sqlRetorno);
	            }

	            rs.close();
	            call.close();
	            rs=null;
	            call=null;

	    } catch (SQLException eSQL) {
	      throw new SQLException ("Error SQL GeneraPropuestaInternet.getDatosyDeudasRolBienRaiz ::" + eSQL.toString());
	    }
	    finally{

	      connection.close();
	    }


	    try{
	      if (!retorno.isEmpty()){

	        this.InicializarContexto();
	        connection = this.getConnection();

	        call = connection.prepareCall("{? = call DeudasContribuyente.ListaDemandasRolBienRaiz(?)}");
	        call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	        call.setString(2, RolBienRaiz);
	        call.execute();

	        rs = (ResultSet) call.getObject(1);

	        while (rs.next()) {

	          HashMap sqlRetorno = new HashMap();

	          sqlRetorno.put("CodDemanda",(Long) new Long(rs.getLong(1)));/* c�digo demanda */
	          sqlRetorno.put("NroExpedienteAdm",(Long) new Long(rs.getLong(2))); /* nro expediente adm */
	          sqlRetorno.put("AgnoExpedienteAdm",(Long) new Long(rs.getLong(3)));/* a�o expediente adm */
	          sqlRetorno.put("Comuna",(String) rs.getString(4));/* comuna */
	          sqlRetorno.put("RutRol",(String) rs.getString(5));/* rut rol */
	          sqlRetorno.put("Etapa",(String) rs.getString(6));/* etapa */
	          sqlRetorno.put("Seleccionada",(String) "N");/* seleccionada */

	          retorno3.addElement(sqlRetorno);
	        }

	        demandasRetorno.addAll(retorno3);

	        rs = null;
	        call = null;

	        if (!demandasRetorno.isEmpty()){
	          Iterator it = demandasRetorno.iterator();

	          while (it.hasNext()){

	            HashMap elemento = (HashMap) it.next();
	            Long CodDemanda = (Long) elemento.get("CodDemanda");
	            Long NroExpedienteAdm = (Long) elemento.get("NroExpedienteAdm");
	            Long AgnoExpedienteAdm = (Long) elemento.get("AgnoExpedienteAdm");
	            String Comuna = (String) elemento.get("Comuna");
	            String RutRol = (String) elemento.get("RutRol");
	            String Etapa = (String) elemento.get("Etapa");
	            HashMap propuestasRetorno = (HashMap) this.getPropuestasDem(CodDemanda.toString());

	            if (propuestasRetorno.size()>0){
	              HashMap structDemanda = new HashMap();

	              structDemanda.put("CodDemanda",(String) CodDemanda.toString());
	              structDemanda.put("NroExpedienteAdm",(String) NroExpedienteAdm.toString());
	              structDemanda.put("AgnoExpedienteAdm",(String) AgnoExpedienteAdm.toString());
	              structDemanda.put("Comuna",(String) Comuna);
	              structDemanda.put("RutRol",(String) RutRol);
	              structDemanda.put("Etapa",(String) Etapa);

	              this.demandas.put((String) CodDemanda.toString(),(HashMap) structDemanda);

	            }


	          }
	        }

	      }

	    } catch (Exception e) {
	      throw new EJBException ("Error GeneraPropuestaInternet.getDatosyDemandasRolBienRaiz ::" + e.toString());
	    }
	    finally{

	      connection.close();

	    }

	    return retorno;
	  }


    /** 
	 *
	 * M�todo privado que crea e inicializa en Bean GeneraPropuesta
	 *
	 */

	private void InicializarContexto(){

	 try{

	    Context ctx = new InitialContext();
	    Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.session.stateful.GeneraPropuesta");
	    GeneraPropuestaHome GenerapropuestaHome= (GeneraPropuestaHome) PortableRemoteObject.narrow(home,GeneraPropuestaHome.class);
	    sessionGeneraPropuesta = GenerapropuestaHome.create();
	    sessionGeneraPropuesta.setContribuyente(this.contribuyente);
	    sessionGeneraPropuesta.setPerfilSesion(this.perfilSesion);
	    sessionGeneraPropuesta.setPerfil(this.perfil);

	  }catch(Exception e) {
	        throw new EJBException("Error InicializarContexto() ::" + e.toString());
	   }
	 }

    /** 
	 * M�todo que retorna la conexi�n
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
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
	 public HashMap getDemandasTerritoriales(){
	   return this.demandas;
	 }

	/** 
	 * M�todo que cierra la conexi�n
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
	            //System.out.println("Error GeneraPropuestaInternetBean::closeConnection();");
	            throw new EJBException("Error GeneraPropuestaInternetBean::closeConnection();"+e);
	        }
	    }

    /** 
	 * Retorna los datos a enviar, para el pago en l�nea de la cuota
	     * @param CodPropuesta - corresponde a la propuesta correspondiente a la cuota
	     * @param NumCuota - corresponde al n�mero de la cuota a recuperar
	     * @return Collection - con los datos del rol bien ra�z
	     */
	    private HashMap getDatosCuotaPagoInternet(long CodPropuesta, int NumCuota) throws RemoteException,SQLException {

	        CallableStatement call;
	        Connection connection = null;
	        Vector retorno = new Vector();
	        ResultSet rs = null;
	        HashMap sqlRetorno = new HashMap();

	        try {
	              /* Datos de la cuota a pagar*/
	              connection = this.getConnection();
	              call = connection.prepareCall("{? = call CONVENIOSINTERNET.DATOSCUOTAPAGOINTERNET(?,?)}");
	              call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	              call.setLong(2, CodPropuesta);
	              call.setInt(3,NumCuota);

	              call.execute();
	              rs = (ResultSet) call.getObject(1);

	              while (rs.next()){

	                    sqlRetorno.put("NumResolucion",(String) rs.getString(1));
	                    sqlRetorno.put("CodCuota",(String) rs.getString(2));
	                    sqlRetorno.put("Folio",(String) rs.getString(3));
	                    sqlRetorno.put("FechaVencimiento",(String) rs.getString(4));
	                    sqlRetorno.put("Monto",(String) rs.getString(5));
	                    sqlRetorno.put("NumCuota",(String) rs.getString(6));
	                    sqlRetorno.put("FechaPago",(String) rs.getString(7));
	              }
	              rs.close();
	              call.close();
	              rs=null;
	              call=null;


	        } catch (SQLException eSQL) {
	          throw new SQLException ("Error SQL GeneraPropuestaInternet.getDatosCuotaPagoInternet ::" + eSQL.toString());
	        }
	        finally{
	                connection.close();
	         }

	        return sqlRetorno;
	      }

	    /** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 */
	      public String GeneraXML(long CodPropuesta,int NumCuota,String RutContribuyente,String DvContribuyente) throws SQLException
	      {
	        String stringRetorno = "";
	        String vNumResolucion = "";
	        String vCodCuota = "";
	        String vFolio = "";
	        String vFechaVencimiento = "";
	        String vMonto = "";
	        String vNumCuota = "";
	        String vFechaPago = "";
	        HashMap cuota = new HashMap();
	        CallableStatement call;
	        Connection connection = null;

	        try{

	          cuota = (HashMap)this.getDatosCuotaPagoInternet(CodPropuesta, NumCuota);
	          vNumResolucion = (String) cuota.get("NumResolucion");
	          vCodCuota = (String) cuota.get("CodCuota");
	          vFolio = (String) cuota.get("Folio");
	          vFechaVencimiento = (String) cuota.get("FechaVencimiento");
	          vMonto = (String) cuota.get("Monto");
	          vNumCuota = (String) cuota.get("NumCuota");
	          vFechaPago = (String) cuota.get("FechaPago");

	          setCodCuotaContado(new Long(vCodCuota).longValue());

	          connection = this.getConnection();
	          call = connection.prepareCall("{? = call CONVENIOSINTERNET.INSERTACONTADO(?,?,?,?,?,?,?,?)}");
	          call.registerOutParameter(1,java.sql.Types.VARCHAR);

	          call.setString(2, vFechaPago);
	          call.setString(3, vCodCuota);
	          call.setString(4, RutContribuyente);
	          call.setString(5, DvContribuyente);
	          call.setString(6, vFolio);
	          call.setString(7, vFechaVencimiento);
	          call.setString(8, vMonto);
	          call.setString(9, vNumResolucion);
	          call.execute();
	          String verifica = (String) call.getString(1);
	              /* si viene 0 indica �xito */
	          call.close();
	          call = null;
	          if (!verifica.equals("0")) {
	            stringRetorno = "0";

	          }
	          else
	          {
	            stringRetorno = vCodCuota;
	          }
	        }
	        catch (Exception e)
	        {
	          //System.out.println ("Error SQL GeneraPropuestaInternet.getDatosCuotaPagoInternet ::" + e);
	          throw new EJBException("Error SQL GeneraPropuestaInternet.getDatosCuotaPagoInternet ::" + e);
	        }
	        finally
	        {
	            connection.close();
	        }
	        return stringRetorno;
	      }

	      
	      /** 
	 	 *
	 	 * <!-- begin-xdoclet-definition --> 
	     * @ejb.interface-method view-type="both"
	 	 * <!-- end-xdoclet-definition --> 
	 	 * @generated
	 	 */
	      public HashMap  ServiciosPagoEnLinea()throws Exception{

	          HashMap hash = new HashMap();
	          String arch_param = null;
	          //arch_param = "c:\\proyectos\\proyectos.config";
	          arch_param = "/bea8/user_projects/param/convenios.config";

	          String reg = null;
	          File archP = new File(arch_param);

	          try {
	            BufferedReader canalP = new BufferedReader(new FileReader(archP));
	            reg = canalP.readLine();
	            while (reg != null && reg.length() != 0) {
	              StringTokenizer st = new StringTokenizer(reg, "=");
	              String varnom = st.nextToken().trim();
	              String varatr = st.nextToken().trim();
	              hash.put(varnom, varatr);
	              reg = canalP.readLine();
	            }
	            canalP.close();
	            return hash;

	          }
	          catch (FileNotFoundException nf) {
	            throw new RemoteException("Archivo parametros no encontrado : " + nf);
	          }
	          catch (IOException io) {
	            throw new RemoteException("Error I/O en archivo parametros : " + io);
	          }

	        }	
	      
	      
	     public boolean getPagoTotalConvenioI(String grupo){
	    	 boolean retorno = true;
	    	 
             if (!grupo.equalsIgnoreCase("Cr�ditos de Estudios Superiores Ley 20.027")){ 	    	 
		    	 for (int i=0;i<excluidosGrupos.size();i++){
		    		 
		    		  String resul = excluidosGrupos.get(i).toString();
		    		  if (resul.equalsIgnoreCase(grupo))
		    			  retorno=false;
		    			  
		    		  
		    	 }
             }else {
            	 retorno=false;
             }
	    	 
	    	 return retorno;
	     }
	     
	     public int getDeterminaGarantiaTerr(String deudas){
	    	 System.out.println("<-----------------------------entre a revisar deudas en demanda--------------------------------------->");
	    	 String sstring = null;
	    	 int contEnDemanda =0;
	    	 int retorno=2;
	    	 if (deudas!=null && deudas.length()>0){
	    		  String separaDeudas  = ",";//separador de deudas
	    		  String separaClaves ="*";//separador de campos claves de la deuda
	    		  String[] sDeudas = StringExt.split(deudas,separaDeudas);
	    		    if (!sDeudas.equals("")) {

	    		        for (int index = 0; index < sDeudas.length; index++) {

	    		        sstring = new String(sDeudas[index].toString());

	    		        String[] sClaves = StringExt.split(sstring,separaClaves);

	    		        /*String tipoCont = sClaves[0];
	    		        String rutRol = sClaves[1];
	    		        String folio = sClaves[2];
	    		        String tipoForm = sClaves[3];
	    		        String fecha = sClaves[4];*/
	    		        String enDemanda = sClaves[7];
	    		        System.out.println("enDemanda--------->"+enDemanda);
	    		        if (enDemanda.equalsIgnoreCase("S")){
	    		        	contEnDemanda++;
	    		        }
	    		  
	    	            }
	    		    }
	    		    
	    		    if (contEnDemanda>0){
	    		    	retorno=1;
	    		    }
	    	  }    
	    	 
	    	 return retorno;
	     }
	      
	
}