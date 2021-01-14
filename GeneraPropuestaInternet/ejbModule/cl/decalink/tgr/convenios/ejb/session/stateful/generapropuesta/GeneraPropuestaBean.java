/**
 * 
 */
package cl.decalink.tgr.convenios.ejb.session.stateful.generapropuesta;
/*
import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;*/
import java.math.BigDecimal;
import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.SessionSynchronization;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.rmi.PortableRemoteObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.GregorianCalendar;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import lecturaconfig.ObtenerServicios;
import liquidador.ServicioLiquidacion;
import liquidador.ServicioLiquidacionHome;

import oracle.jdbc.OracleTypes;

import cl.decalink.tgr.convenios.ejb.entity.persona.*;

import cl.decalink.tgr.convenios.ejb.session.stateful.agregarrol.AgregarRol;
import cl.decalink.tgr.convenios.ejb.session.stateful.agregarrol.AgregarRolHome;
import cl.decalink.tgr.convenios.ejb.session.stateless.auditoria.auditoria;
import cl.decalink.tgr.convenios.ejb.session.stateless.auditoria.auditoriaHome;
import cl.decalink.tgr.convenios.ejb.session.stateless.consultarexclusiones.consultarexclusionesHome;
import cl.decalink.tgr.convenios.ejb.session.stateless.consultarexclusiones.consultarexclusiones;

import cl.decalink.tgr.convenios.DateExt;
import cl.decalink.tgr.convenios.ReformaTributariaInput;
import cl.decalink.tgr.convenios.ReformaTributariaOutput;
import cl.decalink.tgr.convenios.StringExt;
import cl.decalink.tgr.convenios.Global;

import cl.decalink.tgr.convenios.cuotas.Cuotas;
import cl.decalink.tgr.convenios.cuotas.TiposPagoCuotas;
import cl.decalink.tgr.convenios.cuotas.NumeroCuotas;

import cl.decalink.tgr.convenios.propuestas.TipoConvenio;
import cl.decalink.tgr.convenios.propuestas.TipoPago;
import cl.decalink.tgr.convenios.propuestas.ConvenioMasivo;
import cl.decalink.tgr.convenios.propuestas.Etapas;

import cl.decalink.tgr.convenios.deudas.DetalleDeudaExclusion;
import cl.decalink.tgr.convenios.deudas.DeudaExclusion;
import cl.decalink.tgr.convenios.deudas.Deudas;
import cl.decalink.tgr.convenios.deudas.DeudaWeb;

import cl.decalink.tgr.convenios.sesion.Perfil;

import cl.decalink.tgr.convenios.ejb.session.stateless.consultacheques.consultacheques;
import cl.decalink.tgr.convenios.ejb.session.stateless.consultacheques.consultachequesHome;

//import liquidador.ServicioLiquidacion;
//import liquidador.ServicioLiquidacionHome;
import cl.tgr.convenios.ejb.session.stateless.utilconv.*;

//import cl.tgr.convenios.ejb.session.stateless.utilconv.

import condonacion.Condonacion;
import condonacion.CondonacionHome;


/**
 *
 * <!-- begin-user-doc -->
 * Ejb encargado de contener logica principal para la consulta y calculos de deudas del contribuyente y su posterior visualizaci�n en la capa cliente.
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="GeneraPropuesta"	
 *           description="An EJB named GeneraPropuesta"
 *           display-name="GeneraPropuesta"
 *           jndi-name="cobranzas.convenios.negocio.ejb.session.stateful.GeneraPropuesta"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public class GeneraPropuestaBean implements javax.ejb.SessionBean {
    /** Mantiene la fuente de datos para la conexi�n con la base de datos desde el pool
     *  de conexiones
     */
	
    private SessionContext sessionContext;
	
    private transient DataSource dataSource;

    /** Mantiene el objeto Connection con la conexi�n a la base de datos
     */
    private Connection connection;

    /** Mantiene el conjunto de deudas que participan en Cheques protestados */
    private Vector chequesProtestados;


    /** Mantiene el porcentaje condonaci�n general a las multas que se le aplica a la
     *  propuesta
     */
    private Long porcentajeCondonacionMultas;

    /** Mantiene el porcentaje condonaci�n general a los intereses que se le aplica a la
     *  propuesta
     */
    private Long porcentajeCondonacionIntereses;

    /** Mantiene el porcentaje condonaci�n general que se le aplica a la
     *  propuesta
     */
    private Long porcentajeCondonacion;

    /** Mantiene el total a pagar de una propuesta, este valor se mantiene
     *  igual desde que es extra�do de la base de datos como valor base de
     *  los c�lculos que se generan, toda aplicaci�n de porcentajes de
     *  condonaci�n, multas e inter�s se aplican sobre
     *  "totalPagarConCondonacion".
     */
    private Long totalPagar;

    /** Mantiene el porcentaje asignado o calculado para la cuota que el
     *  contribuyente deber� pagar al contado.
     */
    private Long porcentajeCuotaContado;

    /** Mantiene el valor en pesos del pago que se debe efectuar al contado
     */
    private Long pagoContado;

    /** Mantiene el n�mero de cuotas asignado o calculado para la propuesta
     *  que se esta generando
     */
    private Long numeroCuotas;

    /** Mantiene el m�ximo n�mero de cuotas que puede tener un convenio o
     *  condonaci�n que se est� generando
     */
    private Long maximoNumeroCuotas;

    /** Mantiene el c�digo de funcionario de la propuesta que se esta generando
     */
    private Long codigoFuncionario;

    /** Mantiene el id de tesorer�a del funcionario
     */
    private Long idTesoreria;

    /** Mantiene el total a pagar con condonaci�n, multas e intereses aplicados,
     *  este valor en primera instancia es igual al "totalPagar", luego de
     *  cualquier aplicaci�n de porcentajes de condonaci�n multas o inter�s
     *  se realiza en esta variable.
     */
    private Long totalPagarConCondonacion;

    /** Mantiene el c�lculo de las cuotas con valor fijo (se excluyen de esta
     *  condici�n el valor de la primera cuota y de la �ltima cuota)
     */
    private Long valorCuota;

    /** Mantiene el tipo de pago que se va a generar, los valores posibles
     *  son "convenio" y "condonacion", estos son dos formas distintas que puede
     *  tomar  la propuesta de convenio.
     */
    private int tipoPago;

    /** Mantiene el tipo de convenio que se est� generando */
    private int tipoConvenio;

    /** Mantiene la respuesta de si el contribuyente acepta la compensaci�n o
     *  no la acepta.
     */
    private String aceptaCompensacion;

    /** Mantiene el c�digo de la propuesta generada en la base de datos
     *  despu�s de que esta se halla aceptado
     */
    private Long codigoPropuestaGenerada;

    /** Mantiene el valor del filtro aplicado a las deudas en la creaci�n de la
     *  propuesta: "Todas" o "Condonaci�n"
     */
    private String estadoCobranza;

    /** Mantiene el c�digo de la propuesta cuando la propuesta es rescatada
     *  de la base de datos
     */
    private Long codigoPropuesta;

    /** Mantiene el arreglo que se genera en la p�gina JSP para ser transmitido
     *  al EJB generaPropuesta y discriminar que deudas fueron seleccionadas
     *  en la p�gina respectiva.
     */
    private String arregloDeudas;

    /** Un entero que se usa para guardar si el contribuyente es transportista*/
    private int transportista;

    /** Un entero que se usa para guardar el c�digo del perfil que ingreso
     *   a la sesi�n
     */
    private int perfil;

    /** Mantiene el valor calculado como total para un convenios que se proyecta
     *  a una o m�s cuotas
     */
    private long totalPagarProyectado = 0;

    /** Mantiene al contribuyente que se consulta, y con el cual se va a
     *  generar una nueva propuesta.
     */
    private persona contribuyente;

    /** Mantiene el conjunto de deudas del contribuyente
     */
    private Vector deudasContribuyente;

    /** Mantiene el conjunto de deudas seleccionadas del contribuyente
     */
    private Vector deudasContribuyenteSeleccionadas;

    /** Un entero que guarda el valor de la evaluaci�n del monto de las deudas
     *  seleccionadas.
     */
    private int montoVerificado=2;

    /** Mantiene el conjunto de cuotas que fueron generadas para una propuesta
     *  en particular.
     */
    private Cuotas cuotas;

    /** Mantiene la fecha de vigencia de la propuesta.
     */
    private java.sql.Date fechaVigencia;

    /** Mantiene el conjunto de RUT y ROLES que se han consultado en la
     *  desde la renovaci�n de cada RUT.
     */
    private Vector rutRoles;

    /** Mantiene los parametros que se le traspasan a la consulta.
     */
    private HashMap param;

    /** Mantiene el rut y roles que se han consultado para a�adir las deudas
     *  al grupo de deudas del contribuyente.
     */
    private HashMap rutRolesConsultados;
    /** Variable que mantiene la clase deudas
    */
    private Deudas deudas;
    /** Variable que mantiene el objeto Perfil
    */
    private Perfil perfilSesion;

    /** Mantiene valor que indica la liquidez de las deudas
     */
    private boolean liquidada;

    /** Variable que mantiene la clase ConvenioMasivo
     *
     */
    private ConvenioMasivo conveniosMasivo;

    /** Variable que indica si la deuda es no es de un rol del contribuyente
     */
    private boolean agregarRol;

    /** Mantiene las demandas del contribuyente.
     */
    private Vector demandasContribuyente;

    /**Demanda seleccionada cuando se elige filtrar por demandas
     */
    private Long demandaSeleccionada;

    private int numOtorgamiento=-1;

    private long damnificado=1;

    private long montoTotalLey20027=-1;

    private boolean sinAjuste=false;
    
    private String paramGrillaEjb = null;
    
    private String paramReformaTributaria = null;
    
    public  ReformaTributariaInput reformaTributaria;
   
    public  ReformaTributariaOutput reformaTributariaOut;
    
    private Boolean pagoTotal = new Boolean(false);

    private Integer rutContribuyenteReforma=null;
    
    private Integer garantiaTerritorial = null;
    
    private Integer cantidadExclusiones = null; 
    
    private boolean tieneExclusion = false;
   // reformaTributaria = new ReformaTributariaInput();
    
    //reformaTributaria.
    
    /** Mantiene el c�digo del usuario
     *  
     */
    private Long idCodUsuario;    
	
	
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
		// TODO Auto-generated method stub

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
	                dataSource = (DataSource) context.lookup("oracleds");//Global.conveniosDataSource);
	            } catch(Exception e) {
	                throw new EJBException("Error buscando la fuente de datos (dataSource): " + e.toString());
	            }
	        } catch(Exception e) {
	            throw new EJBException("Error inicializando el contexto:" + e.toString());
	        }

	}

	/**
	 * 
	 */
	public GeneraPropuestaBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	  /** Retorna el total a pagar del convenio. Este valor no se modifica al
     *  aplicar la condonaci�n general o particular de la propuesta.
     *  @return un Long que contiene el total a pagar por el contribuyente
     */
    public Long getTotalPagar() {
        return totalPagar;
    }

    /** Establece el total a pagar
     *  @param totalPagar - valor del total a pagar que posee el convenio
     */
    public void setTotalPagar(Long totalPagar) {
        this.totalPagar = totalPagar;
    }

	  /** Retorna el total a pagar del convenio. Este valor no se modifica al
     *  aplicar la condonaci�n general o particular de la propuesta.
     *  @return un Long que contiene el total a pagar por el contribuyente
     */
    public boolean getTieneExclusion() {
        return tieneExclusion;
    }

    /** Establece el total a pagar
     *  @param totalPagar - valor del total a pagar que posee el convenio
     */
    public void setTieneExclusion(boolean tieneExclusion) {
        this.tieneExclusion = tieneExclusion;
    }
    
    
    /** Retorna el tipo de convenio de la propuesta
     *  @return int - c�digo del tipo de convenio
     */
    public int getTipoConvenio() {
        this.tipoConvenio = this.deudas.getTipoConvenio();
        return this.tipoConvenio;
    }
   /**Establece el tipo de convenio de la propuesta
    * @param tipoConvenio(int), c�digo del tipo de convenio
    */
    public void setTipoConvenio(int tipoConvenio) {
        this.tipoConvenio = tipoConvenio;
    }

    /** Retorna el filtro aplicado a las deudas que ser�n extra�das de la base
     *  de datos
     *  @return un String que contiene "S" (esta en cobranza) o "N" (no esta en cobranza judicial)
     */
    public String getEstadoCobranza() {
        return estadoCobranza;
    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna el total a pagar de la propuesta con la condonaci�n a multas
     *  y/o intereses aplicado.
     *  @return el total a pagar con condonaci�n calculado o establecido
     */
    public Long getTotalPagarConCondonacion() {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     /* Establecer a 0 el total a pagar, para que sea recalculado */
        long totalCalculado = 0;

        if (this.deudasContribuyenteSeleccionadas != null){
            /* Leer cada elemento de la colecci�n, y verificar cuales est�n seleccionados */
            Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
            while (it.hasNext()) {
                DeudaWeb elemento = (DeudaWeb) it.next();
                /* Si la deuda esta seleccionada (en la p�gina) */
                //10-03-2004
                if (elemento.estaSeleccionado() && elemento.estaLiquidada()) {
                    long saldo = elemento.getMontoConCondonacion();
                    //System.out.println("saldo condonacion---> "+ saldo);
                    totalCalculado += saldo;
                }
            } /*  while (it.hasNext()) */
        }

        this.totalPagarConCondonacion = new Long(totalCalculado);

        return totalPagarConCondonacion;
    }

    public long getTotalPagarProyectado()
    {
      return this.totalPagarProyectado;
    }
    
    
  /** Establece el total a pagar con condonaci�n
   *  @param totalPagarConCondonacion - representa la cantidad de total a pagar
   */
  //public void setTotalPagarConCondonacion(Long totalPagarConCondonacion) {
  //  this.totalPagarConCondonacion = totalPagarConCondonacion;
  //}

  /** Retorna el porcentaje de cuota contado
   *  @return un objeto Long con un numero entre 0 (cero) y 100.
   */
  public Long getPorcentajeCuotaContado() {
    return porcentajeCuotaContado;
  }

  /** Establece el porcentaje de la cuota contado
   *  @param porcentajeCuotaContado - un numero entero entre 0 (cero) y 100
   */
  public void setPorcentajeCuotaContado(Long porcentajeCuotaContado) {
    this.porcentajeCuotaContado = porcentajeCuotaContado;
  }

  /** Retorna el valor calculado para el pago contado
   *  @return un Long con el valor del pago contado
   */
  public Long getPagoContado() {
    return pagoContado;
  }

    /** Establece el valor del pago contado
     *  @param pagoContado - un Long que contiene el valor del pago contado.
     */
    public void setPagoContado(Long pagoContado) {
        this.pagoContado = pagoContado;
    }

    /** Retorna la cantidad de cuotas que fueron calculadas o establecidas
     *  para una propuesta en particular
     *  @return  un Long con la cantidad de cuotas establecidas o calculadas
     */
    public Long getNumeroCuotas() {
        return numeroCuotas;
    }

    /** Retorna el n�mero m�ximo de cuotas
     *  @return Long - con el n�mero m�ximo de cuotas, por defecto el valor es "12".
     */
    public Long getMaximoNumeroCuotas() {
        if (maximoNumeroCuotas == null){
          maximoNumeroCuotas = new Long (12);
        }

        if (maximoNumeroCuotas.longValue()==0){
          maximoNumeroCuotas = new Long (12);
        }
        return maximoNumeroCuotas;
    }


    /** Establece el numero de cuotas que va a tener la propuesta
     *  @param numeroCuotas - un Long con la cantidad de cuotas
     */
    public void setNumeroCuotas(Long numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    /** Retorna la aceptaci�n o rechazo de la compensaci�n ofrecida por la Tesorer�a
     *  cuando se genera un convenio
     *  @return un String con "S" para S� o "N" para el rechazo
     */
    public String getAceptaCompensacion() {
        return this.aceptaCompensacion;
    }

    /** Establece la decisi�n del contribuyente de aceptar o no una compensaci�n
     *  ofrecida por la Tesorer�a
     *  @param aceptaCompensacion - "S" si acepta o "N" si rechaza
     */
    public void setAceptaCompensacion(String aceptaCompensacion) {
        this.aceptaCompensacion = aceptaCompensacion;
    }

    /** Establece el c�digo del funcionario que va a tener la propuesta
     *  @param codigoFuncionario - un Long con la identificaci�n del funcionario
     */
    public void setCodigoFuncionario(Long codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }

    /** Establece el id tesorer�a  del funcionario que va a tener la propuesta
     *  @param idTesoreria - un Long con la identificaci�n de la tesorer�a
     */
    public void setIdTesoreria(Long idTesoreria) {
        this.idTesoreria = idTesoreria;
    }

   /** Retorna el identificador del perfil del funcionario
    *  @return int con el perfil del funcionario
    */
    public int getPerfil(){
      return this.perfil;
    }

   /** Establece el perfil  del funcionario que va a crear la propuesta
    *  @param perfil - un int con la identificador del perfil
    */
   public void setPerfil(int perfil){
      this.perfil = perfil;
    }

    /** Retorna la persona que esta establecida
     *  @return una clase persona con el contribuyente establecido para el convenio
     */
    public persona getContribuyente() {
        return contribuyente;
    }

   /** Retorna convenio masivo cuando la propuesta a crear es de convenio masivo
     *  @return una clase ConvenioMasivo con los datos de la propuesta de tipo convenios masivos
     */
    public ConvenioMasivo getConvenioMasivo(){
      return this.conveniosMasivo;
    }

  /** Retorna el porcentaje de condonaci�n establecido a la propuesta o
   *  convenio
   *  @return un Long con el porcentaje de condonaci�n
   */
  public Long getPorcentajeCondonacion() {
    return porcentajeCondonacion;
  }

   /** Retorna el porcentaje de condonaci�n a las multas establecido a la propuesta o
   *  convenio
   *  @return un Long con el porcentaje de condonaci�n
   */
  public Long getPorcentajeCondonacionMultas() {
    return porcentajeCondonacionMultas;
  }

   /** Retorna el porcentaje de condonaci�n a los intereses establecido a la propuesta o
   *  convenio
   *  @return un Long con el porcentaje de condonaci�n
   */
  public Long getPorcentajeCondonacionIntereses() {
    return porcentajeCondonacionIntereses;
  }

  public long getDamnificado()
  {
    return this.damnificado;
  }

  public void setDamnificado(long damnificado)
  {
    this.damnificado=damnificado;
  }


  public long getMontoTotalLey20027()
  {
    return this.montoTotalLey20027;
  }

  public void setMontoTotalLey20027(long montoTotalLey20027)
  {
    this.montoTotalLey20027=montoTotalLey20027;
  }


  public boolean getSinAjuste()
  {
    return this.sinAjuste;
  }

  public void setSinAjuste(boolean sinAjuste)
  {
    this.sinAjuste=sinAjuste;
  }

  public String getParamGrillaEjb()
  {
    return this.paramGrillaEjb ;
  }

  public void setParamGrillaEjb(String paramGrillaEjb)
  {
    this.paramGrillaEjb=paramGrillaEjb;
  }

  /** setea codigo usuario
   *  @param codUsuario logeado
   */
  public void setIdCodUsuario(Long idCodUsuario) {
      this.idCodUsuario = idCodUsuario;
  }
  
  /**
   * Retorna valor parametrico de reforma tributaria
   * @return
   */
  public String getParamReformaTributaria()
  {
    return this.paramReformaTributaria;
  }

  /**
   * Setea  valor parametrico de reforma tributaria
   * @param paramReformaTributaria
   */
  public void setParamReformaTributaria(String paramReformaTributaria)
  {
    this.paramReformaTributaria=paramReformaTributaria;
  }  
  
  /**
   * Setea  valor pago total  (arbol de condonaci�n)
   * @param pagoTotal
   */
  public void setPagoTotal(Boolean pagoTotal){
	  this.pagoTotal=pagoTotal;
  }
  
  /**
   * Retorna valor pago total  (arbol de condonaci�n)
   * @return
   */
  public Boolean getPagoTotal(){
	  return this.pagoTotal;
  }
    
  /**
   * Retorna garantia territorial
   * @return
   */
  public Integer getGarantiaTerritorial()
  {
    return this.garantiaTerritorial ;
  }

  /**
   * Setea garantia territorial
   * @param garantiaTerritorial
   */
  public void setGarantiaTerritorial(Integer garantiaTerritorial)
  {
    this.garantiaTerritorial=garantiaTerritorial;
  }  
    
  
	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que obtiene objeto reformaTributaria
  *
  */
  public ReformaTributariaInput getReformaTributariaInput() {
    return reformaTributaria;
  }
  

 /** 
 *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.interface-method view-type="both"
 * <!-- end-xdoclet-definition --> 
 * @generated
 * M�todo que obtiene objeto reformaTributaria
*/
public ReformaTributariaOutput getReformaTributariaOutput() {
 return reformaTributariaOut;
}


/**
 * Setea objeto de reforma tributaria input. para persistir (entre ejbs)
 * @param reformaTributaria
 */
public void setReformaTributariaInput(ReformaTributariaInput reformaTributaria) {
    this.reformaTributaria=reformaTributaria;
  }

/**
 * Setea objeto de reforma tributaria output. para persistir (entre ejbs)
 * @param reformaTributariaOut
 */
public void setReformaTributariaOutput(ReformaTributariaOutput reformaTributariaOut) {
    this.reformaTributariaOut=reformaTributariaOut;
  }

public Vector getDeudasSeleccionadasCollection() {
	 return this.deudasContribuyenteSeleccionadas;
	}

  
	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el nuevo porcentaje de condonaci�n a todas las deudas
   *  que est�n seleccionadas. Re calcula el nuevo saldo total.
   *  @param porcentajeCondonacion - numero que contiene el nuevo porcentaje de condonaci�n
   */
  public void setPorcentajeCondonacion(Long porcentajeCondonacion) throws Exception {
    this.porcentajeCondonacion = porcentajeCondonacion;
    /* Mantiene de forma temporal las deudas de los contribuyentes.*/
    //Vector deudasContribuyenteTemp = new Vector();

    /* Leer cada elemento de la colecci�n, y verificar cuales est�n seleccionados
       y cambiar los porcentajes de condonaci�n de estos */
    Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
    while (it.hasNext()) {
      DeudaWeb elemento = (DeudaWeb) it.next();

      /* Si la deuda esta seleccionada (en la p�gina) */
      if (elemento.estaSeleccionado()) {
        /* El porcentaje de condonaci�n general PREVALECE sobre
           el porcentaje de condonaci�n de la deuda en especifico */
        if (this.porcentajeCondonacion.intValue() <= elemento.getPorcentajeMaximoCondonacion()){
          elemento.setPorcentajeCondonacion(this.porcentajeCondonacion.intValue());
        } else {
          elemento.setPorcentajeCondonacion(elemento.getPorcentajeMaximoCondonacion());
        }
      }

      /* Se elimina la deuda del conjunto principal y se traspasa al temporal,
         esto es para que se reflejen los nuevos cambios hechos en los
         elementos de la colecci�n de deudas */
      //it.remove();
      //deudasContribuyenteTemp.addElement((DeudaWeb) elemento);
    }

    /* Se traspasan los datos de la colecci�n TEMPORAL hacia la PRINCIPAL */
    //deudasContribuyenteSeleccionadas.addAll(deudasContribuyenteTemp);

    /* Volver a calcular la propuesta seg�n las deudas existentes y los
       nuevos porcentajes de condonaci�n */
    //this.calcularTotalPropuesta();
  }


	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el nuevo porcentaje de condonaci�n a las multas a todas las deudas
   *  que est�n seleccionadas. Recalcula el nuevo saldo total.
   *  @param porcentajeCondonacion - numero que contiene el nuevo porcentaje de condonaci�n
   */
  public void setPorcentajeCondonacionMultas(Long porcentajeCondonacionMultas) throws Exception {
    this.porcentajeCondonacionMultas = porcentajeCondonacionMultas;
     //System.out.println("entro genera propuesta setPorcentajeCondonacionMultas");
    /* Mantiene de forma temporal las deudas de los contribuyentes.*/
    //Vector deudasContribuyenteTemp = new Vector();

    /* Leer cada elemento de la colecci�n, y verificar cuales est�n seleccionados
       y cambiar los porcentajes de condonaci�n de estos */
    Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
    while (it.hasNext()) {
      DeudaWeb elemento = (DeudaWeb) it.next();
      
      /* Si la deuda esta seleccionada (en la p�gina) */
      if (elemento.estaSeleccionado()) {
        /* El porcentaje de condonaci�n general PREVALECE sobre
           el porcentaje de condonaci�n de la deuda en especifico */
    	 //if (!reformaTributaria.getIsReformaTributaria().booleanValue()) {
	        if (this.porcentajeCondonacionMultas.intValue() <= elemento.getPorcentajeMaximoCondonacionMultas()){
	          elemento.setPorcentajeCondonacionMultas(this.porcentajeCondonacionMultas.intValue());
	        } else {
	          elemento.setPorcentajeCondonacionMultas(elemento.getPorcentajeMaximoCondonacionMultas());
	        }
    	 /*}else{
    		 elemento.setPorcentajeCondonacionMultas(this.porcentajeCondonacionMultas.intValue()); 
    	 }*/
         
      }
      
      

      /* Se elimina la deuda del conjunto principal y se traspasa al temporal,
         esto es para que se reflejen los nuevos cambios hechos en los
         elementos de la colecci�n de deudas */
      //it.remove();
      //deudasContribuyenteTemp.addElement((DeudaWeb) elemento);
    }

    /* Se traspasan los datos de la colecci�n TEMPORAL hacia la PRINCIPAL */
    //deudasContribuyenteSeleccionadas.addAll(deudasContribuyenteTemp);

    /* Volver a calcular la propuesta seg�n las deudas existentes y los
       nuevos porcentajes de condonaci�n */
    //this.calcularTotalPropuesta();
  }

/** 
	 *REFORMA TRIBUTARIA
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el nuevo porcentaje de condonaci�n a las multas a todas las deudas
*  que est�n seleccionadas. Recalcula el nuevo saldo total.
*  @param porcentajeCondonacion - numero que contiene el nuevo porcentaje de condonaci�n
*/
public void setPorcentajeCondonacionMultas(Long porcentajeCondonacionMultas, Double beneficio) throws Exception {
 this.porcentajeCondonacionMultas = porcentajeCondonacionMultas;
 /* Leer cada elemento de la colecci�n, y verificar cuales est�n seleccionados
    y cambiar los porcentajes de condonaci�n de estos */
 Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
 while (it.hasNext()) {
   DeudaWeb elemento = (DeudaWeb) it.next();
   
   /* Si la deuda esta seleccionada (en la p�gina) */
   if (elemento.estaSeleccionado()&&elemento.getPorcentajeMaximoCondonacionMultas()>0) {
  
	   if (beneficio!=null){
		   elemento.setPorcentajeCondonacionMultas(elemento.getPorcentajeMaximoCondonacionMultas()+beneficio.intValue());
	   }else{//se queda con el porcentaje total sumado
		   elemento.setPorcentajeCondonacionMultas(porcentajeCondonacionMultas.intValue());
	   }
   }
 }
} 
  
	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el nuevo porcentaje de condonaci�n a los intereses a todas las deudas
   *  que est�n seleccionadas. Recalcula el nuevo saldo total.
   *  @param porcentajeCondonacion - numero que contiene el nuevo porcentaje de condonaci�n
   */
  public void setPorcentajeCondonacionIntereses(Long porcentajeCondonacionIntereses) throws Exception {
    this.porcentajeCondonacionIntereses = porcentajeCondonacionIntereses;
    //System.out.println("entro genera propuesta setPorcentajeCondonacionIntereses");
    /* Mantiene de forma temporal las deudas de los contribuyentes.*/
    //Vector deudasContribuyenteTemp = new Vector();

    /* Leer cada elemento de la colecci�n, y verificar cuales est�n seleccionados
       y cambiar los porcentajes de condonaci�n de estos */
    Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
    while (it.hasNext()) {
      DeudaWeb elemento = (DeudaWeb) it.next();
      //System.out.println("entro genera propuesta setPorcentajeCondonacionIntereses xx");

      /* Si la deuda esta seleccionada (en la p�gina) */
      if (elemento.estaSeleccionado()) {
        /* El porcentaje de condonaci�n general PREVALECE sobre
           el porcentaje de condonaci�n de la deuda en especifico */
       // if (!reformaTributaria.getIsReformaTributaria().booleanValue()){  
	    	if (this.porcentajeCondonacionIntereses.intValue() <= elemento.getPorcentajeMaximoCondonacionIntereses()){
	          elemento.setPorcentajeCondonacionIntereses(this.porcentajeCondonacionIntereses.intValue());
	        } else {
	          elemento.setPorcentajeCondonacionIntereses(elemento.getPorcentajeMaximoCondonacionIntereses());
	        }
       /* }else{
        	elemento.setPorcentajeCondonacionIntereses(this.porcentajeCondonacionIntereses.intValue());
        }*///
        
      }

      /* Se elimina la deuda del conjunto principal y se traspasa al temporal,
         esto es para que se reflejen los nuevos cambios hechos en los
         elementos de la colecci�n de deudas */
      //it.remove();
      //deudasContribuyenteTemp.addElement((DeudaWeb) elemento);
    }

    /* Se traspasan los datos de la colecci�n TEMPORAL hacia la PRINCIPAL */
    //deudasContribuyenteSeleccionadas.addAll(deudasContribuyenteTemp);

    /* Volver a calcular la propuesta seg�n las deudas existentes y los
       nuevos porcentajes de condonaci�n */
    //this.calcularTotalPropuesta();
  }

	 /** 
	 *REFORMA TRIBUTARIA
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el nuevo porcentaje de condonaci�n a los intereses a todas las deudas
*  que est�n seleccionadas. Recalcula el nuevo saldo total.
*  @param porcentajeCondonacion - numero que contiene el nuevo porcentaje de condonaci�n
*/
public void setPorcentajeCondonacionIntereses(Long porcentajeCondonacionIntereses, Double beneficio) throws Exception {
 this.porcentajeCondonacionIntereses = porcentajeCondonacionIntereses;
 /* Leer cada elemento de la colecci�n, y verificar cuales est�n seleccionados
    y cambiar los porcentajes de condonaci�n de estos */
 Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
 while (it.hasNext()) {
   DeudaWeb elemento = (DeudaWeb) it.next();
   //System.out.println("entro genera propuesta setPorcentajeCondonacionIntereses xx");

   /* Si la deuda esta seleccionada (en la p�gina) */
     if (elemento.estaSeleccionado()&&elemento.getPorcentajeMaximoCondonacionIntereses()>0) {
         if (beneficio!=null){
            elemento.setPorcentajeCondonacionIntereses(elemento.getPorcentajeMaximoCondonacionIntereses()+beneficio.intValue());
         }else{ 
            elemento.setPorcentajeCondonacionIntereses(porcentajeCondonacionIntereses.intValue());
         } 
	 }
  }
}  
  
  
	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el nuevo porcentaje de condonaci�n a las multas a todas las deudas
  *  que est�n seleccionadas. Recalcula el nuevo saldo total.
  *  @param porcentajeCondonacion - numero que contiene el nuevo porcentaje de condonaci�n
  */
 public void setPorcentajeCondonacionMultasSimulacion(Long porcentajeCondonacionMultas) throws Exception {
   this.porcentajeCondonacionMultas = porcentajeCondonacionMultas;

   /* Mantiene de forma temporal las deudas de los contribuyentes.*/
   //Vector deudasContribuyenteTemp = new Vector();

   /* Leer cada elemento de la colecci�n, y verificar cuales est�n seleccionados
      y cambiar los porcentajes de condonaci�n de estos */
   Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
   while (it.hasNext()) {
     DeudaWeb elemento = (DeudaWeb) it.next();
     int vMaximoMultas=0;

     /* Si la deuda esta seleccionada (en la p�gina) */
     if (elemento.estaSeleccionado()) {
       /* El porcentaje de condonaci�n general PREVALECE sobre
          el porcentaje de condonaci�n de la deuda en especifico */

         if ((elemento.getCondonacionAplicadaA().equals("A"))||(elemento.getCondonacionAplicadaA().equals("M")))
             {
               vMaximoMultas=100;
               //System.out.println("Porcentaje M�ximo Condonaci�n multas:"+elemento.getPorcentajeMaximoCondonacionMultas());

             }
          else
          {
            vMaximoMultas=elemento.getPorcentajeMaximoCondonacionMultas();
          }

      // System.out.println("Posibilidad convenio:"+elemento.getPosibilidadConvenio());
      // System.out.println("Posibilidad condonacion:"+elemento.getPosibilidadCondonacion());
      // System.out.println("Aplica condonaci�n a:"+elemento.getCondonacionAplicadaA());



       if (this.porcentajeCondonacionMultas.intValue() <= vMaximoMultas){
         elemento.setPorcentajeCondonacionMultas(this.porcentajeCondonacionMultas.intValue());
       } else {
         elemento.setPorcentajeCondonacionMultas(elemento.getPorcentajeMaximoCondonacionMultas());
       }
     }


   }


 }

 /** 
 *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.interface-method view-type="both"
 * <!-- end-xdoclet-definition --> 
 * @generated
 * Establece el nuevo porcentaje de condonaci�n a los intereses a todas las deudas
  *  que est�n seleccionadas. Recalcula el nuevo saldo total.
  *  @param porcentajeCondonacion - numero que contiene el nuevo porcentaje de condonaci�n
  */
 public void setPorcentajeCondonacionInteresesSimulacion(Long porcentajeCondonacionIntereses) throws Exception {
   this.porcentajeCondonacionIntereses = porcentajeCondonacionIntereses;

   /* Mantiene de forma temporal las deudas de los contribuyentes.*/
   //Vector deudasContribuyenteTemp = new Vector();

   /* Leer cada elemento de la colecci�n, y verificar cuales est�n seleccionados
      y cambiar los porcentajes de condonaci�n de estos */
   Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
   while (it.hasNext()) {
     DeudaWeb elemento = (DeudaWeb) it.next();
     int vMaximoInteres = 0;
     /* Si la deuda esta seleccionada (en la p�gina) */
     if (elemento.estaSeleccionado()) {
       /* El porcentaje de condonaci�n general PREVALECE sobre
          el porcentaje de condonaci�n de la deuda en especifico */

         if ((elemento.getCondonacionAplicadaA().equals("A"))||(elemento.getCondonacionAplicadaA().equals("I")))
             {
               vMaximoInteres=100;
               //System.out.println("Porcentaje M�ximo Condonaci�n Intereses:"+elemento.getPorcentajeMaximoCondonacionIntereses());
             }
             else
             {
               vMaximoInteres=elemento.getPorcentajeMaximoCondonacionIntereses();
             }
       //System.out.println("Posibilidad convenio:"+elemento.getPosibilidadConvenio());
       //System.out.println("Posibilidad condonacion:"+elemento.getPosibilidadCondonacion());
       //System.out.println("Aplica condonaci�n a:"+elemento.getCondonacionAplicadaA());

       if (this.porcentajeCondonacionIntereses.intValue() <= vMaximoInteres){
         elemento.setPorcentajeCondonacionIntereses(this.porcentajeCondonacionIntereses.intValue());
       } else {
         elemento.setPorcentajeCondonacionIntereses(elemento.getPorcentajeMaximoCondonacionIntereses());
       }
     }


   }

  }


    /** 
     * Calcula el valor del total de la propuesta. Esta funci�n es llamada
     *  internamente en las funciones que modifican el conjunto de deudas o los
     *  valores relativos a ellas, como por ejemplo el porcentaje de condonaci�n.
     */
    private void calcularTotalPropuesta() throws Exception {
      try{
        /* porcentaje de condonaci�n que se le aplicar� a las multas */
        float porcentajeCondonacionMultas = 0;
        /* porcentaje de condonaci�n que se le aplicar� a los intereses */
        float porcentajeCondonacionIntereses = 0;

        /* Mantiene de forma temporal las deudas de los contribuyentes.*/
        Vector deudasContribuyenteTemp = new Vector();

        /* Establecer a 0 el total a pagar, para que sea re calculado */
        long totalCalculado = 0;
        //long totalCalculadoProyectado = 0;

//        if (this.deudasContribuyenteSeleccionadas != null){
          /* Leer cada elemento de la coleccion, y verificar cuales estan seleccionados */
          //Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
          Iterator it = this.deudasContribuyente.iterator();
          while (it.hasNext()) {
              DeudaWeb elemento = (DeudaWeb) it.next();

              /* Si la deuda esta seleccionada (en la p�gina) */
              if (elemento.estaSeleccionado() && elemento.estaLiquidada()) {
                long saldo = elemento.getMonto();
                //long saldo = elemento.getMontoConCondonacion();
                long multas = elemento.getMultas();
                long reajustes = elemento.getReajustes();
                long intereses = elemento.getIntereses();
                long porcentajeCondonacionDeuda = elemento.getPorcentajeCondonacion();
                String aplicadoA = (String) elemento.getCondonacionAplicadaA();

                /* Los porcentajes de condonaci�n se aplican solo a multas, solo
                   a intereses o a ambas */
                if (aplicadoA.equals("A")) {
                    porcentajeCondonacionMultas = porcentajeCondonacionDeuda;
                    porcentajeCondonacionIntereses = porcentajeCondonacionDeuda;
                } else if (aplicadoA.equals("M")) {
                    porcentajeCondonacionMultas = porcentajeCondonacionDeuda;
                    porcentajeCondonacionIntereses = 0;
                } else if (aplicadoA.equals("I")) {
                    porcentajeCondonacionMultas = 0;
                    porcentajeCondonacionIntereses = porcentajeCondonacionDeuda;
                }

                /* Se calcula el total a pagar considerando las multas, intereses, y condonaciones */
                float saldoMultasCondonacion = (float) multas - (multas * (porcentajeCondonacionMultas / 100));
                float saldoInteresesConCondonacion = (float) intereses - (intereses * (porcentajeCondonacionIntereses / 100));
                float saldoConCondonacion = (float) saldo +  reajustes + saldoMultasCondonacion + saldoInteresesConCondonacion;
                Float FSaldoConCondonacion = new Float(saldoConCondonacion);
                /* Se establece el nuevo saldo con condonaci�n de la deuda */
                elemento.setMontoConCondonacion(FSaldoConCondonacion.longValue());

                /* Se totaliza la sumatoria */
                totalCalculado += (long) FSaldoConCondonacion.longValue();
                //elemento.setNumeroCuotas(this.numeroCuotas.intValue());
                //elemento.setValorTotal();
                //totalCalculadoProyectado += (long) elemento.getValorTotal();
                //totalCalculado += saldo;
              }

              /* Se elimina la deuda del conjunto principal y se traspasa al temporal,
               esto es para que se reflejen los nuevos cambios hechos en los
               elementos de la colecci�n de deudas */
              it.remove();
              deudasContribuyenteTemp.addElement((DeudaWeb) elemento);
          } /*  while (it.hasNext()) */
  //      }

        this.totalPagarConCondonacion = new Long(totalCalculado);
        //this.totalPagarProyectado = totalCalculadoProyectado;
        /* Se traspasan los datos de la colecci�n TEMPORAL hacia la PRINCIPAL */
        deudasContribuyente.addAll(deudasContribuyenteTemp);
      } catch (Exception e) {
        throw new Exception("calcularTotalPropuesta" + e.toString());
      }
    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el contribuyente que se ha seleccionado para comenzar
     *  la generaci�n de un convenios
     *  @param contribuyente - un objeto de la clase persona
     */
    public void setContribuyente(persona contribuyente) throws RemoteException {
        this.contribuyente = contribuyente;

        this.initValues();
        Integer rut = this.contribuyente.getRut();
        rutRoles.addElement((Long) new Long(rut.longValue()));
        rutRolesConsultados.put((String) rut.toString(),"N");
    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el contribuyente que se ha seleccionado para comenzar
    *  la generaci�n de un convenios
    *  @param contribuyente - un objeto de la clase persona
    */
   public void setContribuyente(Integer vRut) throws RemoteException {
    this.rutContribuyenteReforma=vRut;
    Context ctx = null;
    try {
         ctx = new InitialContext();

         Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.entity.persona");
         personaHome contribuyenteHome = (personaHome) PortableRemoteObject.narrow(home,personaHome.class);
         Collection rutsContribuyentes = contribuyenteHome.findByRut(vRut);

        Iterator it = rutsContribuyentes.iterator();
        persona elementPersona = (persona) it.next();
        this.contribuyente =elementPersona;

        this.initValues();
        Integer rut = this.contribuyente.getRut();
        rutRoles.addElement((Long) new Long(rut.longValue()));
        rutRolesConsultados.put((String) rut.toString(),"N");
   }
   catch (Exception e)
    {
      //System.out.println("Error setContribuyente nuevo="+e);
      e.printStackTrace();
      throw new EJBException("Error setContribuyente nuevo="+e);
    }

     }

    /** Retorna la colecci�n que contiene las cuotas que se generaron
     *  @return una colecci�n con las cuotas
     */
    public Collection getCuotas() {
        return this.cuotas.getCuotas();
    }

    public void setCuotas(Vector cuotas)
    {
      this.cuotas.setCuotas(cuotas);
    }

    /** Adiciona a la colecci�n de RUT y Roles un nuevo RUT o Rol
     *  @param rutRol - un valor Long que representa un RUT o un Rol
     */
    public void addRutRol(Long rutRol) {
        /* Se extrae el tama�o actual de la colecci�n de RUT y Roles a los
           cuales ya se les ha consultado las deudas dentro de la base de datos */
        int i = rutRolesConsultados.size();

        /* Se establece a No consultado el valor String del RUT o Rol */
        rutRolesConsultados.put((String) rutRol.toString(),"N");

        /* Si el tama�o es igual implica que exist�a dentro de la colecci�n */
        if(i == rutRolesConsultados.size()) {
            /* Como existe se restablece su condici�n de consultado */
            rutRolesConsultados.put((String) rutRol.toString(),"S");
        } else {
            /* como no existe se adiciona a la colecci�n de RUT y Roles */
            rutRoles.addElement(rutRol);
            this.agregarRol = true;
        }
    }

    /** Retorna la colecci�n de RUT y Roles que han sido utilizados
     *  en la configuraci�n de la propuesta
     *  @return un objeto de la clase Collection con los RUT y Roles utilizados
     */
    public Collection getRutRoles() {
      return this.rutRoles;
    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Calcula las cuotas asociadas al convenio considerando cuantas cuotas
     *  tiene este. La cuota de ajuste se establece a 0 (cero) ya que este
     *  calculo no aparece en la p�gina, adem�s ese valor ser� calculado
     *  despu�s del pago de la pen�ltima cuota; se excluyen de esto ultimo
     *  los convenios de una sola cuota o las condonaciones.
     */
  public void CalcularPropuesta() {
    /* Define las variables para el trabajo con fechas de vencimiento de las cuotas */
    java.sql.Date fechaCuota = new  java.sql.Date(System.currentTimeMillis());

    if (this.tipoPago == TipoPago.CONDONACION_PAGO_CONTADO) {
      this.cuotas = new Cuotas(0, this.getTotalPagarConCondonacion().longValue(), fechaCuota);
      this.fechaVigencia = this.cuotas.getFechaUltimaCuota();

    } else {
      if (this.getMontoTotalLey20027()>0){

        this.cuotas = new Cuotas((int) this.numeroCuotas.intValue(), this.pagoContado.longValue(), this.getMontoTotalLey20027(), this.getMontoTotalLey20027(), fechaCuota, this.getSinAjuste());
      }else{

        this.cuotas = new Cuotas((int) this.numeroCuotas.intValue(), this.pagoContado.longValue(), this.getTotalPagarConCondonacion().longValue(), this.totalPagarProyectado, fechaCuota);
      }
      this.fechaVigencia = this.cuotas.getFechaUltimaCuota();
    }
  }

	 /** 
	 *Inicializa los valores del EJB. Este procedimiento es llamado cuando
     *  se crea el EJB o cuando se establece un nuevo contribuyente.
     */
    private void initValues() {
        this.closeConnection();
        this.agregarRol = false;
        rutRoles = null; rutRoles = new Vector(); rutRoles.clear();
        deudasContribuyente = null; deudasContribuyente = new Vector(); deudasContribuyente.clear();
        rutRolesConsultados = null; rutRolesConsultados = new HashMap(); rutRolesConsultados.clear();
        param = null; param = new HashMap(); param.clear();

        demandasContribuyente = null; demandasContribuyente = new Vector(); demandasContribuyente.clear();
        demandaSeleccionada=null; demandaSeleccionada= new Long(-1);


        this.conveniosMasivo = null;

        porcentajeCuotaContado = null; porcentajeCuotaContado = new Long(0);
        porcentajeCondonacion = null; porcentajeCondonacion = new Long(0);
        pagoContado = null; pagoContado = new Long(0);
        numeroCuotas = null; numeroCuotas = new Long(0);
        totalPagar = null; totalPagar = new Long(0);
        totalPagarConCondonacion = null; totalPagarConCondonacion = new Long(0);
        arregloDeudas="";
        tipoPago=1;
        estadoCobranza="T";
        codigoPropuesta = new Long(0);
        codigoFuncionario = new Long(0);
        idTesoreria = new Long(0);
        liquidada = false;
    }

    /** Retorna un String con el tipo de convenio que se esta generando
     *  @return un String con el tipo de convenio de la forma "convenio" o "condonacion"
     */
    public int getTipoPago() {
      return this.tipoPago;
    }


    /** Establece el tipo de pago de la propuesta
     *  @param - int con identificador del tipo de pago
     */
    public void setTipoPago(int tipoPago) {
      this.tipoPago = tipoPago;
    }
    /** Establece el objeto Perfil
     * @param - perfilSesion(Perfil), contiene el objeto Perfil del funcionario
     */
    public void setPerfilSesion(Perfil perfilSesion){
      this.perfilSesion = perfilSesion;

    }
  /** Retorna el objeto Perfil
   *  @return - Perfil objeto Perfil, del funcionario
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
	 * M�todo que eval�a el tipo de pago en relaci�n con las deudas seleccionadas,
   *  analiza las fechas de vencimiento de las deudas, seg�n el grupo a que pertenezcan
   *  @return int - valor que retorno un  "0" si no hay error, si el
   *  valor es "1", indica que las deudas seleccionadas no se pueden incluirse
   *  en el tipo de pago elegido,
   *  si el valor es "2", indica que la fecha de emisi�n de las deudas es nula y
   *  si el valor es "3", indica que la fecha de vencimiento es nula.
   */
  public int verificaTipoPago() throws Exception{
    boolean valido = true;
    int iValido = 0;
    String fecha_compara_emision = "1900-01-01";

    long ctdCondonacion = 0;
    Vector deudasAVerificar = null;
    try {

        if (deudasAVerificar == null){
            deudasAVerificar = new Vector();
        }

        if (deudasAVerificar.size() == 0){

            this.deudas.setConnection(this.getConnection());

            deudasAVerificar.addAll(this.deudas.getDeudasSeleccionadas());

            Iterator itx = deudasAVerificar.iterator();

            while (itx.hasNext() && valido){
                DeudaWeb deudaTP = (DeudaWeb) itx.next();
                //modificacion MYPES 02-01-2005
                if (deudaTP.getOrigen().equals("M"))
                {
                  iValido = 4 ;
                  valido = true;
                }
                else
                {
                 if (!deudaTP.origenEsNoCut()) {
                    //2004-01-23

                    if ((deudaTP.getTipoFormulario()==21)||(deudaTP.getTipoFormulario()==22)||(deudaTP.getTipoFormulario()==29)) {
                        if ((fecha_compara_emision.compareTo(deudaTP.getFechaEmision().toString()) == 0 )&&(TipoPago.CONVENIO_SIN_CONDONACION != this.tipoPago)) {

                                iValido = 2;

                            }
                    }
                  }

                  if (fecha_compara_emision.compareTo(deudaTP.getFechaVencimiento().toString()) == 0 && TipoPago.CONDONACION_PAGO_CONTADO != this.tipoPago ) {

                           if ((deudaTP.getTipoMulta().equals("NO")) && (deudaTP.getTipoInteres().equals("NO")) && (deudaTP.getTipoReajuste().equals("NO"))){
                                iValido = 0;
                           }
                           else
                           {
                              iValido = 3;
                           }

                        }


                 if (TipoPago.CONDONACION_PAGO_CONTADO == this.tipoPago && valido){
                    valido = (deudaTP.getPosibilidadCondonacion().equals("S"));
                 } else if (TipoPago.CONVENIO_CON_CONDONACION == this.tipoPago && valido) {
                    valido = (deudaTP.getPosibilidadConvenio().equals("S"));
                    if ((deudaTP.getPosibilidadCondonacion().equals("S"))) ctdCondonacion++;

                 } else if (TipoPago.CONVENIO_SIN_CONDONACION == this.tipoPago && valido) {
                    valido = (deudaTP.getPosibilidadConvenio().equals("S"));
                 }
              }
            }

            if (TipoPago.CONVENIO_CON_CONDONACION == this.tipoPago && valido) {
                valido = (ctdCondonacion > 0);
            }




            if (iValido < 2) iValido = (valido)? 0:1;
            if (iValido == 4){
              iValido = 0;
            }

        }
    } catch (Exception e){
       throw new Exception("Error Verifica Tipo Pago " + e.toString());
    //System.out.println("Error Verifica Tipo Pago " + e.toString());
    }
//    finally{
//      this.closeConnection();
//    }
    return iValido;
  }


	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que verifica el monto de la propuesta, indicando si estan dentro
    *  de los valores requeridos por el perfil del funcionario que crea la propuesta
    *  @return int - valor que indica "0" si esta dentro de lo requerido,
    *  valor "-1" indica que monto esta bajo el m�nimo requerido,
    *  valor "1" indica que el monto es superior al permitido
    */
    public int verificaMonto() throws Exception {

        try {
            int formulario = 0;
            String vGrupo="";
            if (this.deudasContribuyenteSeleccionadas == null){
                this.deudasContribuyenteSeleccionadas = new Vector();

            }
              if (this.deudasContribuyenteSeleccionadas.size() == 0) {
                this.deudasContribuyenteSeleccionadas.addAll(this.deudas.
                    getDeudasSeleccionadas());


                this.deudas.setTotal(this.deudasContribuyenteSeleccionadas);
                Iterator itx = this.deudasContribuyenteSeleccionadas.iterator();

                if (itx.hasNext()) {
                  DeudaWeb deuda = (DeudaWeb) itx.next();
                  formulario = deuda.getTipoFormulario();
                }

              }
              else {

                this.deudas.setTotal(this.deudasContribuyenteSeleccionadas);
                Iterator itx = this.deudasContribuyenteSeleccionadas.iterator();
                if (itx.hasNext()) {
                  DeudaWeb deuda = (DeudaWeb) itx.next();
                  formulario = deuda.getTipoFormulario();
                  vGrupo=deuda.getGrupo();
                }

              }


               if ((this.perfil >= 5)&&(this.perfil<10)) {
                    montoVerificado = 0;

                    return montoVerificado;
                }

              /*  if (vGrupo.equals("Art. 192 Fiscal CEC II")||vGrupo.equals("Art. 192 Territorial CEC II"))
                {
                  montoVerificado = 0;
                  return montoVerificado;

                }
            */
            //modificaci�n para masivos MYPE : 31-01-2005
                if (this.tipoConvenio == 4){
                    montoVerificado = 0;
                   // System.out.println("Convenio Masivo : "+this.tipoConvenio);
                    return montoVerificado;

                }

                if (this.tipoPago==3)
                {
                  montoVerificado = 0;
                  return montoVerificado;
                }
          //Si el perfil es internet
                if ((this.montoVerificado != 2)||(this.perfil==0))
                {


                if(!this.deudas.validarTotalMinimo(this.perfilSesion.getMontoMinimo())){
                    //Enviar Exception

                    this.montoVerificado = -1;
                    return this.montoVerificado;

                }
               /* if (formulario == 30){
                    if (!this.deudas.validarTotalMaximo(this.perfilSesion.getMontoMaximoTerritorial())){
                        //Enviar Exception
                        this.montoVerificado = 1;
                        return this.montoVerificado;
                    }
                } else {
                    if (!this.deudas.validarTotalMaximo(this.perfilSesion.getMontoMaximoFiscal())){
                        //Enviar Exception
                        this.montoVerificado = 1;
                        return this.montoVerificado;
                    }
                }*/
              }
        } catch (Exception e){


         // throw new Exception("Error VerificaMonto " + e.toString());
         //System.err.println("Error VerificaMonto " + e.toString());
         this.montoVerificado = -2;
         return this.montoVerificado;

                }


        this.montoVerificado = 0;

        return this.montoVerificado;
    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que retorna las deudas seleccionadas y liquidadas. Llama al
     *  servicio de liquidaci�n y actualiza los valores de multas, intereses,
     *  reajustes, montos.
     *  @return Collection, con el conjunto de deudas seleccionadas
     */
    public Collection getDeudasSeleccionadas() throws Exception {

    	if (reformaTributaria.getIsReformaTributaria().booleanValue()){
    	 this.getCondonacionConv(reformaTributaria);
    	}
    	
    	
        if (montoVerificado == 0 && !liquidada) {
            Long vReajustes = new Long(0);
            Long vIntereses = new Long(0);
            Long vMultas = new Long(0);
            Long vNeto = new Long(0);

            liquidada = true;
    /*        Context ctx = new InitialContext();
            Object home = ctx.lookup("cobranzas.corporativo.servicioLiquidacion.ServicioLiquidacion");
            ServicioLiquidacionHome liquidadorHome = (ServicioLiquidacionHome) PortableRemoteObject.narrow(home,ServicioLiquidacionHome.class);
            ServicioLiquidacion liquida = liquidadorHome.create();
*/
            Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
            if (!this.getParamGrillaEjb().equalsIgnoreCase("S")){
            	//System.out.println("---->liquida parametro grilla N");
		            while (it.hasNext()){
		                DeudaWeb deuda = (DeudaWeb) it.next();
		                if (!deuda.origenEsNoCut() && !deuda.estaLiquidada()){
		
		                    boolean liquidacionExitosa = false;
		                    try {
		                      //modificacion convenio MYPE 01-02-2005
		
		                      if (deuda.getTipoConvenio() == 4)
		                      {
		                          liquidacionExitosa = true;
		                          vReajustes = (Long) new Long(0);
		                          vIntereses = (Long) new Long(0);
		                          vMultas = (Long) new Long(0);
		                      }
		                      else
		                      {
		                        Connection connLiquida = this.getConnection();
		                        CallableStatement callLiquida = connLiquida.prepareCall("{? = call liquidacion.liquida(?,?,?,?,?,?,?)}");
		                        callLiquida.registerOutParameter(1, OracleTypes.CURSOR);
		                        callLiquida.setLong(2,deuda.getTipoContribuyente());/*Tipo Contribuyente */
		                        callLiquida.setLong(3,deuda.getRutRol());/*Rut Rol */
		                        callLiquida.setLong(4,deuda.getTipoFormulario());/*Formulario */
		                        callLiquida.setLong(5,deuda.getFolio());/*Folio */
		                        callLiquida.setDate(6,deuda.getFechaVencimiento());/*Fecha Vencimiento */
		                        callLiquida.setLong(7,deuda.getMonto());
		                        callLiquida.setDate(8,deuda.getFechaEmision());
		                        callLiquida.execute();
		
		                        ResultSet rsLiquida = (ResultSet) callLiquida.getObject(1);
		
		                        while(rsLiquida.next()) {
		
		                          vReajustes = (Long) new Long(rsLiquida.getLong(1));
		                          vIntereses = (Long) new Long(rsLiquida.getLong(2));
		                          vMultas = (Long) new Long(rsLiquida.getLong(3));
		                          vNeto = (Long) new Long(rsLiquida.getLong(4));
		
		
		                        }
		
		                        rsLiquida.close();
		                        callLiquida.close();
		
		                        liquidacionExitosa = true;
		                      }
		                    } catch(Exception e) {
		
		                      liquidacionExitosa = false;
		
		                      throw new Exception("Error VerificaMonto " + e.toString());
		                    }
		
		
		                    if (liquidacionExitosa){
		
		                        try {
		                            //System.out.println("liquida::reajustes" + vReajustes);
		                            deuda.setReajustes(vReajustes.longValue());
		
		                        } catch (Exception e) {
		                            deuda.setReajustes(0);
		                        }
		
		                        try {
		
		                            deuda.setIntereses(vIntereses.longValue());
		
		                        } catch (Exception e) {
		                            deuda.setIntereses(0);
		                        }
		
		                        try {
		
		                            deuda.setMultas(vMultas.longValue());
		
		                        } catch (Exception e) {
		                            deuda.setMultas(0);
		                        }
		
		                        try {
		
		                            deuda.setMonto(vNeto.longValue());
		
		                        } catch (Exception e) {
		                            deuda.setMonto(0);
		                        }
		
		
		
		
		                        deuda.setReajusteProyectado(deuda.getReajustes());
		                        deuda.setInteresProyectado(deuda.getIntereses());
		                        deuda.setMultasProyectadas(deuda.getMultas());
		                        deuda.setLiquidar(true);
		                        this.tipoConvenio = deuda.getTipoConvenio();
		
		                    }
		                }
		
		                if (deuda.getOrigen().equals("M")){
		                    if (this.conveniosMasivo == null){
		                        Connection conn = this.getConnection();
		                        CallableStatement call = conn.prepareCall("{? = call DatosConveniosMasivo.ObtenerDatosPorDeuda(?,?,?,?,?)}");
		                        call.registerOutParameter(1, OracleTypes.CURSOR);
		                        call.setLong(2,deuda.getTipoContribuyente());/*Tipo Contribuyente */
		                        call.setLong(3,deuda.getRutRol());/*Rut Rol */
		                        call.setLong(4,deuda.getTipoFormulario());/*Formulario */
		                        call.setLong(5,deuda.getFolio());/*Folio */
		                        call.setDate(6,deuda.getFechaVencimiento());/*Fecha Vencimiento */
		
		                        call.execute();
		
		                        ResultSet rs = (ResultSet) call.getObject(1);
		
		                        while(rs.next()) {
		                            this.conveniosMasivo = new ConvenioMasivo(rs.getLong(1),rs.getInt(2),rs.getInt(3),rs.getInt(4));
		                        }
		
		                        rs.close();
		                        call.close();
		                    }
		                } else {
		                    this.conveniosMasivo = null;
		                }
		
		            }

            }else{
            	//System.out.println("---->NO liquida parametro grilla S");
	                while (it.hasNext()){
	                    DeudaWeb deuda = (DeudaWeb) it.next();
	                    
	                    if (!deuda.origenEsNoCut() && !deuda.estaLiquidada()){
	
	                        boolean liquidacionExitosa = false;
	                     
	                          //modificacion convenio MYPE 01-02-2005
	
	                          if (deuda.getTipoConvenio() == 4)
	                          {
	                              liquidacionExitosa = true;
	                              vReajustes = (Long) new Long(0);
	                              vIntereses = (Long) new Long(0);
	                              vMultas = (Long) new Long(0);
	                          }
	                        
	          
	                            deuda.setReajusteProyectado(deuda.getReajustes());
	                            deuda.setInteresProyectado(deuda.getIntereses());
	                            deuda.setMultasProyectadas(deuda.getMultas());
	                            deuda.setLiquidar(true);
	                            this.tipoConvenio = deuda.getTipoConvenio();
	            
	                    }
	                    if (deuda.getOrigen().equals("M")){
	                        if (this.conveniosMasivo == null){
	                            Connection conn = this.getConnection();
	                            CallableStatement call = conn.prepareCall("{? = call DatosConveniosMasivo.ObtenerDatosPorDeuda(?,?,?,?,?)}");
	                            call.registerOutParameter(1, OracleTypes.CURSOR);
	                            call.setLong(2,deuda.getTipoContribuyente());/*Tipo Contribuyente */
	                            call.setLong(3,deuda.getRutRol());/*Rut Rol */
	                            call.setLong(4,deuda.getTipoFormulario());/*Formulario */
	                            call.setLong(5,deuda.getFolio());/*Folio */
	                            call.setDate(6,deuda.getFechaVencimiento());/*Fecha Vencimiento */
	
	                            call.execute();
	
	                            ResultSet rs = (ResultSet) call.getObject(1);
	
	                            while(rs.next()) {
	                                this.conveniosMasivo = new ConvenioMasivo(rs.getLong(1),rs.getInt(2),rs.getInt(3),rs.getInt(4));
	                            }
	
	                            rs.close();
	                            call.close();
	                        }
	                    } else {
	                        this.conveniosMasivo = null;
	                    }
	
	                } 	
            	
            }

            
          if (this.connection==null){
        	  this.getConnection();
          }
            this.deudas.setTotal(this.deudasContribuyenteSeleccionadas);

            NumeroCuotas numeroCuotas = new NumeroCuotas(this.getPerfil(),   this.getTipoConvenio(),this.getTipoPago(),(int) this.contribuyente.getIdPersona().longValue(), deudas.getTotal(), deudas.getTipoImpuesto(), deudas.getFechaEmision(), this.connection);
            this.maximoNumeroCuotas = new Long((long) numeroCuotas.getNumeroCuotas());


        }

        this.calcularTotalPropuesta();
        this.closeConnection();
        return this.deudasContribuyenteSeleccionadas;
    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que retorna las deudas seleccionadas y liquidadas. Llama al
    *  servicio de liquidaci�n y actualiza los valores de multas, intereses,
    *  reajustes, montos.
    *  @return Collection, con el conjunto de deudas seleccionadas
    */
   public Collection getDeudasSeleccionadas_anterior() throws Exception {

	   //System.out.println("entro a :::->>> getDeudasSeleccionadas no esta liquidando"); 
  
       if (montoVerificado == 0 && !liquidada) {
           Long vReajustes = new Long(0);
           Long vIntereses = new Long(0);
           Long vMultas = new Long(0);
           Long vNeto = new Long(0);

           liquidada = true;
  
           Iterator it = this.deudasContribuyenteSeleccionadas.iterator();

           while (it.hasNext()){
               DeudaWeb deuda = (DeudaWeb) it.next();
               if (!deuda.origenEsNoCut() && !deuda.estaLiquidada()){

                   boolean liquidacionExitosa = false;
                
                     //modificacion convenio MYPE 01-02-2005

                     if (deuda.getTipoConvenio() == 4)
                     {
                         liquidacionExitosa = true;
                         vReajustes = (Long) new Long(0);
                         vIntereses = (Long) new Long(0);
                         vMultas = (Long) new Long(0);
                     }
                   
     
                       deuda.setReajusteProyectado(deuda.getReajustes());
                       deuda.setInteresProyectado(deuda.getIntereses());
                       deuda.setMultasProyectadas(deuda.getMultas());
                       deuda.setLiquidar(true);
                       this.tipoConvenio = deuda.getTipoConvenio();
       
               }
               if (deuda.getOrigen().equals("M")){
                   if (this.conveniosMasivo == null){
                       Connection conn = this.getConnection();
                       CallableStatement call = conn.prepareCall("{? = call DatosConveniosMasivo.ObtenerDatosPorDeuda(?,?,?,?,?)}");
                       call.registerOutParameter(1, OracleTypes.CURSOR);
                       call.setLong(2,deuda.getTipoContribuyente());/*Tipo Contribuyente */
                       call.setLong(3,deuda.getRutRol());/*Rut Rol */
                       call.setLong(4,deuda.getTipoFormulario());/*Formulario */
                       call.setLong(5,deuda.getFolio());/*Folio */
                       call.setDate(6,deuda.getFechaVencimiento());/*Fecha Vencimiento */

                       call.execute();

                       ResultSet rs = (ResultSet) call.getObject(1);

                       while(rs.next()) {
                           this.conveniosMasivo = new ConvenioMasivo(rs.getLong(1),rs.getInt(2),rs.getInt(3),rs.getInt(4));
                       }

                       rs.close();
                       call.close();
                   }
               } else {
                   this.conveniosMasivo = null;
               }

           }



           this.deudas.setTotal(this.deudasContribuyenteSeleccionadas);

           NumeroCuotas numeroCuotas = new NumeroCuotas(this.getPerfil(),   this.getTipoConvenio(),this.getTipoPago(),(int) this.contribuyente.getIdPersona().longValue(), deudas.getTotal(), deudas.getTipoImpuesto(), deudas.getFechaEmision(), this.connection);
           this.maximoNumeroCuotas = new Long((long) numeroCuotas.getNumeroCuotas());


       }

       this.calcularTotalPropuesta();
       this.closeConnection();
       return this.deudasContribuyenteSeleccionadas;
      
   }
    
    
    
	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que calcula el valor total proyectado
     *
     */
    public void setValorTotal() throws SQLException {
        Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
        long totalCalculadoProyectado = 0;
        while (it.hasNext()){
            DeudaWeb deuda = (DeudaWeb) it.next();
            deuda.setConnection(this.getConnection());
            deuda.setNumeroCuotas(this.numeroCuotas.intValue());
            deuda.setValorTotal();

            //10-03-2004

            if (deuda.estaLiquidada())
            {

            totalCalculadoProyectado += (long) deuda.getValorTotal();
            }
        }
        System.out.println("total proyectado:"+totalCalculadoProyectado);


        this.totalPagarProyectado = totalCalculadoProyectado;
        this.closeConnection();

    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna la colecci�n con las deudas del contribuyente, es utilizada
   *  siempre despu�s de el llamado a esta misma funci�n con parametros.
   */
  public Collection getDeudasContribuyente() {
    return getDCx(param);
  }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated Retorna la colecci�n con las deudas del contribuyente, es utilizada la
   *  primera vez luego de haber establecido un contribuyente.
   *  @param un HashMap que esta compuesto de RUT (Long) que contiene el RUT del contribuyente, y el ESTADOCOBRANZA (String) que contiene el filtro de las deudas
   */
  public Collection getDeudasContribuyente(HashMap parametros){
    return getDCx(parametros);
  }

    /** 
	 * Retorna la colecci�n de deudas contribuyente. Es llamada por dos de los
     *  m�todos p�blicos.
     */
    private Collection getDCx(HashMap parametros){

        Connection conn;
        CallableStatement  call = null;
        ResultSet rs = null;
        ResultSet rsRolBienRaiz = null;
        Vector retorno = new Vector();
        Vector retornoChq = new Vector();
        Vector retornoChqOrder = new Vector();
        retorno.clear();

        boolean existenNuevosRutRoles = false;

        String fechaInicio="20090330";
        String fechaCompara="20090331";
        String fechaVigencia="20100630";
        String fechaActual="";
        String vFechaEmision="";
        String vFechaVenc="";
        String vOrigen="";

        int vPerfil =1;

        try {
        	
        	String valorParam = this.getObtieneParametrosGrilla();
        	String valorParamReforma = this.getObtieneParametrosReformaTribu();
            //System.out.println("veces *");        	
        	//seteo valor reforma tributaria
        	this.setParamReformaTributaria(valorParamReforma);
        	
        	if (reformaTributaria==null){
        		reformaTributaria = new ReformaTributariaInput();
        	}
        	if (this.getParamReformaTributaria().equalsIgnoreCase("S")){
        		
        		if (!parametros.isEmpty()&&parametros.get("INTERNET")!=null){
        			//internet
	        	    reformaTributaria.setIsReformaTributaria(new Boolean(true));
	        	    reformaTributaria.setRutContribuyente(new Integer(parametros.get("RUT").toString()));
	        	    reformaTributaria.setBeneficioInternet(new Boolean(true));
	        	    reformaTributaria.setIsCanalPresencial(new Boolean(true));        		
	        	    reformaTributaria.setPagoTotalConvenio(new Boolean(true));
	        	    reformaTributaria.setIsIntranet(new Boolean(false));
	        	    reformaTributaria.setPagoContado(new Boolean(false));//no se puede realzar por internet
        		}else{
        			//intranet
	        	    reformaTributaria.setIsReformaTributaria(new Boolean(true));
	        	    reformaTributaria.setRutContribuyente(this.rutContribuyenteReforma);
	        	    reformaTributaria.setBeneficioInternet(new Boolean(false));
	        	    reformaTributaria.setIsCanalPresencial(new Boolean(true));
	        	    reformaTributaria.setIsIntranet(new Boolean(true));
        		}
        	}else{
        		reformaTributaria.setIsReformaTributaria(new Boolean(false));
        	}
        	
        	
        	//System.out.println("**********ReformaTributariaInput**********");
        	//System.out.println("getIsReformaTributaria--->"+ reformaTributaria.getIsReformaTributaria());
        	//System.out.println("getRutContribuyente--->"+ reformaTributaria.getRutContribuyente());
        	//System.out.println("getBeneficioPyme--->"+ reformaTributaria.getBeneficioPyme());
        	//System.out.println("getBeneficioInternet--->"+ reformaTributaria.getBeneficioInternet());
        	//System.out.println("getComportamientoConvenio--->"+ reformaTributaria.getComportamientoConvenio());
        	//System.out.println("getPagoTotalConvenio--->"+ reformaTributaria.getPagoTotalConvenio());
        	//System.out.println("getContextoAmbiente--->"+ reformaTributaria.getContextoAmbienteParam());
        	//System.out.println("******************************************");
        	if (!parametros.isEmpty()){
        	//System.out.println("ver el rut ------> "+parametros.get("RUT").toString());
        	//System.out.println("ver el INTERNET ------> "+parametros.get("INTERNET"));
        	}else{
        		//System.out.println("es por intranet----->");
        	}
        	//System.out.println("valor parametro grilla------> "+ valorParam);
            String sql="Select to_char(sysdate,'yyyymmdd') from dual";
            this.setParamGrillaEjb(valorParam);


            this.param.put("RUT",(Long) parametros.get("RUT"));
            this.param.put("ESTADOCOBRANZA", (String) parametros.get("ESTADOCOBRANZA"));
            this.estadoCobranza = (String) parametros.get("ESTADOCOBRANZA");
            vOrigen=(String) parametros.get("ORIGEN");

            conn = this.getConnection();

            CallableStatement callFecha;
            callFecha = conn.prepareCall(sql);
            ResultSet fechaRes = callFecha.executeQuery();

            while(fechaRes.next())
            {
             fechaActual=fechaRes.getString(1);
            }

            callFecha.close();


            vPerfil=this.perfil;


            String sRutRoles = "";

            Iterator itrs = rutRoles.iterator();

            while(itrs.hasNext()) {
                Long elemento = (Long) itrs.next();
                //System.out.println("antes  VISTA RUTROLES******-------->"+elemento);
                if ("S".equals((String) rutRolesConsultados.get(elemento.toString()))) {
                    continue;
                } else {
                    rutRolesConsultados.put((String) elemento.toString(),"S");

                    if (sRutRoles.length() == 0){
                        sRutRoles = elemento.toString();
                    } else {
                        sRutRoles = sRutRoles + "," + elemento.toString();
                    }

                }
            }

            if (sRutRoles.length() > 0){
            	//System.out.println("VISTA RUTROLES******-------->"+sRutRoles);

                deudas = new Deudas(contribuyente.getRut().longValue(), this.estadoCobranza, contribuyente.getIdPersona().longValue());

                deudas.setConnection(conn);

                 if (this.estadoCobranza.equals("D")){//Para lista las deudas de la demanda, donde Rut = codigo de la demanda

                    if (demandaSeleccionada.longValue()!=-1){

                     sRutRoles =((Long) this.demandaSeleccionada).toString();

                    }

                 }

             deudas.setOrigen(vOrigen);
             deudas.setPerfil(this.getPerfil());
             deudas.setDeudasPropuesta(sRutRoles, this.agregarRol, this.getParamGrillaEjb());

                if (!this.agregarRol) this.agregarRol = true;

                // Inserci�n de cheque
                //se depreca codigo ipn 25/01/2016
                /*
                HashMap IdGrupoCheque = new HashMap();

                Context ctx = new InitialContext();
                Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.session.stateless.consultacheques");
                consultachequesHome ConsultachequesHome= (consultachequesHome) PortableRemoteObject.narrow(home,consultachequesHome.class);
                consultacheques sessionCHQ = ConsultachequesHome.create();
                this.chequesProtestados = (Vector) sessionCHQ.getConsultaCheque(new Long(this.contribuyente.getRut().longValue()));
                System.out.println("rut contribuyente---->"+this.contribuyente.getRut().longValue());
                System.out.println("cantidad de cheques---->"+this.chequesProtestados.size());
                
                Iterator itCHQ = this.chequesProtestados.iterator();
                 while(itCHQ.hasNext()){
                	 System.out.println("--------------------->><>ingreso acheuqes");
                    HashMap deudaCHQ = (HashMap) itCHQ.next();


                    DeudaWeb deudaChq = new DeudaWeb(((Long) deudaCHQ.get("Tipo_Contribuyente")).intValue(), 6, Long.parseLong((String) deudaCHQ.get("Rut_Rol")), ((Long) deudaCHQ.get("folio_f06")).longValue(), (java.sql.Date) deudaCHQ.get("fecha_vencim"));
                    deudaChq.setMonto(((Long) deudaCHQ.get("monto")).longValue());
                    String IdGrupoCheque_S = ((Long) deudaCHQ.get("NUMERO_CHEQUE")).toString() + ":" +((Long) deudaCHQ.get("id_cheque")).toString();
                    deudaChq.setGrupo(IdGrupoCheque_S);
                    System.out.println("----numero cheque--->"+deudaCHQ.get("NUMERO_CHEQUE"));
                    if (!IdGrupoCheque.containsKey(IdGrupoCheque_S)) IdGrupoCheque.put(IdGrupoCheque_S,"S");

                    if (deudaChq.getTipoContribuyente() == 1) {
                        deudaChq.setDescripcionTipoDeuda("Fiscal");
                    } else {
                        deudaChq.setDescripcionTipoDeuda("Territorial");
                    }

                    deudaChq.setOrigen("Q");
                    deudaChq.setPosibilidadCondonacion("S");
                    deudaChq.setPosibilidadConvenio("S");
                    deudaChq.setRutRolFormateado((String) deudaCHQ.get("Rut_Rol"));
                    deudaChq.habilitar();
                    deudaChq.setCondonacionAplicadaA("I");
                    //LOS cheques son clase de impuesto 2, por lo tanto solo se aplica a intereses
                    deudaChq.setTipoConvenio(TipoConvenio.CHEQUES_PROTESTADOS);
                    //deudaChq.setCod92Liq((Long)deudaCHQ.get("cod92Liq"));
                    deudaChq.setCod92Liq(new Long(-2));

                //Se valida que la deuda sea liquidable CCC 27032012
                    if (deudaChq.getCod92Liq().longValue()<0 ){
                      deudaChq.setPosibilidadCondonacion("N");
                      deudaChq.setPosibilidadConvenio("N");
                      deudaChq.deshabilitar();
                    }

                    if (deudaChq.getTipoContribuyente()==3){
                        IdGrupoCheque.put(IdGrupoCheque_S,"N");
                    } else {

                        String BuscaDeuda =(String) sessionCHQ.getBuscaDeuda((String) deudaCHQ.get("Rut_Rol").toString(),(String) deudaCHQ.get("folio_f06").toString(),(java.sql.Date) deudaCHQ.get("fecha_vencim"));


                        if (!BuscaDeuda.equals("0")){
                            deudaChq.setPosibilidadCondonacion("N");
                            deudaChq.setPosibilidadConvenio("N");

                        }
                    }
                    retornoChq.addElement(deudaChq);
                }




                sessionCHQ = null;

                ConsultachequesHome = null;

                while(retornoChq.size() > 0) {
                    Iterator itera = retornoChq.iterator();
                    String grupo;
                    String orden = "?";

                    while(itera.hasNext()) {
                        DeudaWeb elementox = (DeudaWeb) itera.next();

                        grupo = elementox.getGrupo();
                        grupo = grupo + ":" + IdGrupoCheque.get(grupo);
                        elementox.setGrupo(grupo);
                        String nuevoOrden = grupo;

                        if ("?".equals(orden)) orden = nuevoOrden;
                        if (orden.equals(nuevoOrden)) {
                            retornoChqOrder.addElement(elementox);
                            itera.remove();
                        }
                    }
                }
                */
                if (reformaTributaria.getIsReformaTributaria().booleanValue()){
	                //********Obtengo deudas y luego repaso para obtener las transitorias
	                //System.out.println("voy a ingresar grupos--->"+retorno.size());
	                Collection deudasTmp=obtieneGrupoDeuda(deudas.getDeudas());
	                deudas.getDeudas().clear();
	                deudas.setDeudas(deudasTmp);
	                //********
                }
                
                
                //**********opero nuevas exclusione*****************************//
                
                Collection deudasTmpExclusionea=this.obtieneExclusionesDeuda(vPerfil, deudas.getDeudas());
                deudas.getDeudas().clear();
                deudas.setDeudas(deudasTmpExclusionea);
                //**************************************************************//
                
                
                
                 retorno = (Vector) deudas.getDeudas();
                 retorno.addAll(retornoChqOrder);

            }
            
          //bloque eliminado nueva excluisiones
            Context ctx = new InitialContext();
            Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.session.stateless.consultarexclusiones");
            consultarexclusionesHome ConveniosDeudorHome = (consultarexclusionesHome) PortableRemoteObject.narrow(home,consultarexclusionesHome.class);
            consultarexclusiones VSessionBean=null;
            Vector exclusionRutRolRMH = new Vector();

            VSessionBean = ConveniosDeudorHome.create();

            
            DeudaWeb anularExclusiones = null;
            while(retorno.size() > 0) {
                Iterator itera = retorno.iterator();
                String grupo;
                String orden = "?";
                while(itera.hasNext()) {
                    DeudaWeb elemento = (DeudaWeb) itera.next();

                   // vFechaEmision=DateExt.format("AAAAMMDD",elemento.getFechaEmision());
                   // vFechaVenc=DateExt.format("AAAAMMDD",elemento.getFechaVencimiento());


                    anularExclusiones = elemento;
                    grupo = elemento.getGrupo();



                    if ("No Agrupables (Sin derecho a convenio)".equals(grupo)) {

                        elemento.setPosibilidadCondonacion("N");
                        elemento.setPosibilidadConvenio("N");
                        elemento.deshabilitar();

                    }
               
                   /* if (!elemento.getOrigen().equals("Q")){

                        if (elemento.exclusionPorRol()){// 06-06-2003

                            //if (perfil<5)
                            //{
                            	//System.out.println("entre en 1---->"+this.perfil);
                                //elemento = VSessionBean.getVerificaRoles((new Long(elemento.getRutRol())).toString()+","+this.perfil,elemento);
                            //}
                           if (perfil>=5){
                              elemento = VSessionBean.getVerificaRolesConvAct((new Long(elemento.getRutRol())).toString(),elemento);
                            }
                        } else {

                         //if ("Art. 192 Fiscal".equals(grupo) ||"Art. 192 Fiscal LEY 20.460".equals(grupo) ||"Ley 20.583 Concesiones Acuicultura".equals(grupo)||"Ley Fiscal Reforma Tributaria 2012".equals(grupo) || elemento.getOrigen().equals("M")||"Cr�ditos de Estudios Superiores Ley 20.027".equalsIgnoreCase(grupo)) {
                         if (elemento.getTipoFormulario()!=30 && reformaTributaria.getIsReformaTributaria().booleanValue()||"Art. 192 Fiscal".equals(grupo) ||"Art. 192 Fiscal LEY 20.460".equals(grupo) ||"Ley 20.583 Concesiones Acuicultura".equals(grupo)||"Ley Fiscal Reforma Tributaria 2014".equals(grupo) || elemento.getOrigen().equals("M")||"Cr�ditos de Estudios Superiores Ley 20.027".equalsIgnoreCase(grupo)) { 
                        	 //modificaci�n para masivos MYPE : 31-01-2005
                                //if (elemento.getOrigen().equals("M")){
                                    //this.perfil = 12;
                                //}
                               // System.out.println("paso aca");
                             elemento = VSessionBean.getVerificaExclusion(this.contribuyente.getIdPersona().longValue(), this.contribuyente.getRut().longValue(), elemento, vPerfil);
                         
                         }

                            else
                            {
                                 //if ("Art. 192 Territorial".equals(grupo)||"Art. 192 Territorial LEY 20.460".equals(grupo)||"Ley Territorial Reforma Tributaria 2012".equals(grupo))
                                 if (reformaTributaria.getIsReformaTributaria().booleanValue()||"Art. 192 Territorial".equals(grupo)||"Art. 192 Territorial LEY 20.460".equals(grupo)||"Ley Territorial Reforma Tributaria 2014".equals(grupo))	 
                                 {
                                	 //System.out.println("entre en 2---->"+vPerfil);
                                  elemento = VSessionBean.getVerificaRoles((new Long(elemento.getRutRol())).toString()+","+vPerfil,elemento);

                                 }
                            }

                        }

                        if ((elemento.getTipoFormulario() == 21 || elemento.getTipoFormulario() == 22 || elemento.getTipoFormulario() == 29 ) && elemento.estaExcluida()){

                            exclusionRutRolRMH.addElement(new Long(elemento.getRutRol()));

                        }
                    }*/


                    String nuevoOrden = grupo;
                    if ("?".equals(orden)) orden = nuevoOrden;
                    if (orden.equals(nuevoOrden)) {
                        deudasContribuyente.addElement(elemento);
                        itera.remove();
                    }
                }
            }
            
            anularExclusiones = VSessionBean.getVerificaExclusion(0,0, anularExclusiones, (long) vPerfil);
            VSessionBean = null; ConveniosDeudorHome = null; home = null; ctx = null;
             
            //bloque eliminado nueva excluisiones
           if (!exclusionRutRolRMH.isEmpty()) {

            //    System.out.println("exclusionRutRolRMH");
             //   System.out.println("DEUDAS CONTRIBUYENTE HASTA AHORA 2 : "+deudasContribuyente.size());
                retorno.addAll(deudasContribuyente);
                deudasContribuyente.clear();

                Iterator iteraRMH = retorno.iterator();
                String grupo;

                while(iteraRMH.hasNext()) {

                    DeudaWeb elemento = (DeudaWeb) iteraRMH.next();
                    grupo = elemento.getGrupo();

                    if (elemento.getTipoFormulario() == 21 || elemento.getTipoFormulario() == 22 || elemento.getTipoFormulario() == 29 || elemento.getTipoFormulario() == 37) {
                        Iterator itx = exclusionRutRolRMH.iterator();
                        while (itx.hasNext()) {
                            long rutRolX = ((Long) itx.next()).longValue();
                            if (elemento.getRutRol() == rutRolX){

                                elemento.setExclusion(true);

                            }
                        }
                    }
                   
                    iteraRMH.remove();
                    deudasContribuyente.addElement(elemento);
                }

            }

      
            //********intento repasar los satelites*******************////
          
            
           if (this.getParamGrillaEjb().equalsIgnoreCase("S")){ 
           Iterator itera = deudasContribuyente.iterator();
            
            while(itera.hasNext()) {

                DeudaWeb elemento = (DeudaWeb) itera.next();
                   if (elemento.getTipoFormulario() == 14 || elemento.getTipoFormulario() == 19 || elemento.getTipoFormulario() == 36 || elemento.getTipoFormulario() == 63) {
                     elemento=this.liquidaSatelite(elemento);
                   }
            }
            }   
                       
            
            //**************************************************************//
            
            
            
            
            this.ordenaGrupos();

            conn = null;
            //return deudasContribuyente;

        } catch(SQLException e) {
        	//System.out.println("voy poraqui--2------->");
            throw new EJBException("Error ejecutando el SQL 1" + e.toString());
        }catch(RemoteException e) {
        	//System.out.println("voy poraqui--2---xx---->");
            //throw new EJBException("Error ejecutando el SQL 2 ");
        	//e.toString();
        	//e.getMessage();
        }catch(EJBException e) {
        	//System.out.println("voy poraqui--2---xx-444--->");
            //throw new EJBException("Error ejecutando el SQL 2 ");
        	e.toString();
        	e.getMessage();
        }catch(Exception e) {
        	
        	//System.out.println("voy poraqui--2------->");
            throw new EJBException("Error ejecutando el SQL 3" + e.toString());
        }
        finally{
          this.closeConnection();
        }
        return deudasContribuyente;
    }

  /** Retorna una colecci�n con las deudas a modificar de una propuesta
   *
   */
  public  Collection getDeudasContribuyenteModificar() throws RemoteException, Exception {
    return getDeudasContribuyenteModificar(param,this.codigoPropuesta);
  }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna una colecci�n con las deudas a modificar de una propuesta
   * @param - parametros un HashMap que contiene el rut del contribuyente y estado cobranza,
   * CodPropuesta un Long contiene el c�digo de la propuesta a modificar
   */

  public Collection getDeudasContribuyenteModificar(HashMap parametros,Long CodPropuesta)  throws RemoteException, Exception {
    Connection conn = null;
    Vector retorno = new Vector();
    CallableStatement  call = null;
    ResultSet rs = null;

    try {

      conn = this.getConnection();

      //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
      call = conn.prepareCall("{? = call DeudasContribuyente.fCurRolBienRaiz(?,?)}");
      call.registerOutParameter(1, OracleTypes.CURSOR);
      call.setLong(2, contribuyente.getIdPersona().longValue());
      call.setLong(3, CodPropuesta.longValue());
      call.execute();

      rs = (ResultSet) call.getObject(1);

      while (rs.next()) {
        addRutRol(new Long(rs.getLong(1)));
      }

      rs.close();
      call.close();

      Iterator it = getDCx((HashMap) parametros).iterator();
      while (it.hasNext()) {
        DeudaWeb elemento = (DeudaWeb) it.next();

        call = this.getConnection().prepareCall("{? = call Configurarpropuesta.ParticipaDeudaPropuesta(?,?,?,?,?,?)}");

        call.registerOutParameter(1, OracleTypes.CURSOR);
        call.setLong(2, elemento.getTipoContribuyente());
        call.setLong(3, elemento.getRutRol());
        call.setLong(4, elemento.getTipoFormulario());
        call.setLong(5, elemento.getFolio());
        call.setDate(6, elemento.getFechaVencimiento());
        call.setLong(7, CodPropuesta.longValue());

        call.execute();

        rs = (ResultSet) call.getObject(1);

        if(rs.next()) {
          this.tipoPago = (new Long(rs.getLong(1))).intValue();
          if ("S".equals(rs.getString(2))){
            this.setTransportista(2);
          } else{
            this.setTransportista(1);
          }

          elemento.habilitar();
          elemento.seleccionar();


          elemento.setPorcentajeCondonacionIntereses(rs.getInt(3));
          elemento.setPorcentajeCondonacionMultas(rs.getInt(4));

        }

        retorno.addElement((DeudaWeb) elemento);
        it.remove();
        rs.close();
        call.close();
      }//while (it.hasNext())

      deudasContribuyente.clear();
      deudasContribuyente.addAll(retorno);
      rs = null;
      call = null;
      conn = null;


      return deudasContribuyente;
    } catch(SQLException e) {
      throw new EJBException("Error ejecutando el SQL " +  e.toString());
    }
    finally{
      this.closeConnection();
    }
  }

  /**Retorna el c�digo de la propuesta aceptada
   * @param - Long que contiene el c�digo de la propuesta
   */
  public Long getAceptacion() throws Exception, RemoteException, SQLException {
    return insertaPropuesta(2);
  }

  /**Retorna el c�digo de la propuesta rechazada
   * @param - Long que contiene el c�digo de la propuesta
   */
  public Long getRechazo() throws Exception, RemoteException, SQLException {
    return insertaPropuesta(1);
  }

  /**Retorna el c�digo de la propuesta ingresada como eliminada rechazada
   * @param - Long que contiene el c�digo de la propuesta
   */
  public Long getEliminada() throws Exception, RemoteException, SQLException {
    return insertaPropuesta(5);
  }

  /** 
   * M�todo privado que inserta la propuesta y sus deudas en las tablas :
   *  cv_propuestas, cv_deudas, cv_deudas_incluidas
   *  @param - long valor que indica propuesta aceptada "1" o propuesta rechazada "2"
   */

  private Long insertaPropuesta(long aceptadaRechazada) throws Exception, RemoteException, SQLException {
    PreparedStatement ps = null;
    Connection conn = null;
    CallableStatement  call = null;
    boolean existe=true;
    long vCodEtapa=aceptadaRechazada;
    String resp = "";
    int ret = 0;
    String TipCont = "1";
    String resolucion=null;
    String accion=null;
    String Identificacion=null;
    HashMap IngAuditoria =null;
    try {
    	
    	/******CREA EL OBJETO DE AUDITORIA*****/
    	Context ctxAud = new InitialContext(new ObtenerServicios().env);
    	Object home_R = ctxAud.lookup("cobranzas.convenios.negocio.ejb.session.stateless.auditoria");
    	auditoriaHome ConveniosDeudorHome = (auditoriaHome) PortableRemoteObject.narrow(home_R,auditoriaHome.class);
    	auditoria sessionAuditoria=null;
    	sessionAuditoria=ConveniosDeudorHome.create();
    	/*************/	
    	
    	IngAuditoria =new HashMap();
         
    	 IngAuditoria.put("COD_TIPO_EVENTO",new Long(1));
    	 IngAuditoria.put("COD_USUARIO",this.idCodUsuario);
    	// String Identificacion=" N�mero Resoluci�n = "+NumResolucion_R+" - Rut Contribuyente ="+RutContribuyente_R+" - Acci�n = Convenio Activado" ;
    	// IngAuditoria.put("IDENTIFICACION",Identificacion);
    	// IngAuditoria.put("NUM_RESOLUCION",new Long(NumResolucion_R));

    	
    	
      conn = this.getConnection();
      conn.setAutoCommit(false);
      
      Long Codigo = (Long) this.getCodigoPropuesta();
      
      if (Codigo.longValue() > 0) {
        /* llama a function que actualizar la propuesta en cv_propuestas*/
        call = conn.prepareCall("{? = call InsertaPropuesta.UpdatePropuesta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        call.registerOutParameter(1, java.sql.Types.VARCHAR);
        call.setLong(2, aceptadaRechazada);  /* COD_ETAPA_CONVENIO */
        call.setLong(3, tipoPago);   /* COD_TIPO_PAGO */
        call.setInt(4, this.tipoConvenio);   /* COD_TIPO_CONVENIO */
        call.setLong(5, contribuyente.getIdPersona().longValue()); /* ID_PERSONA */
        call.setLong(6, this.idTesoreria.longValue());   /* ID_TESORERIA */
        call.setLong(7, this.codigoFuncionario.longValue());   /* COD_FUNCIONARIO */
        call.setDate(8, new java.sql.Date(System.currentTimeMillis())); /* FECHA_GENERACION */
        call.setDate(9, (java.sql.Date) new java.sql.Date(DateExt.addMes(new java.sql.Date(System.currentTimeMillis())).getTime()));
        call.setNull(10, java.sql.Types.DATE);   /* FECHA_ACTIVACION */
        call.setLong(11, this.porcentajeCuotaContado.longValue()); /* PORC_CUOTA_CONTADO */
        call.setLong(12, this.numeroCuotas.longValue());   /* NUM_CUOTAS */
        call.setLong(13, cuotas.getMontoCuotaContado());   /* MONTO_CUOTA_CONTADO */
        call.setLong(14, cuotas.getMontoCuotaFija());   /* MONTO_CUOTA_FIJA */
        call.setNull(15, java.sql.Types.NUMERIC);   /* MONTO_CUOTA_AJUSTE */
        call.setNull(16, java.sql.Types.VARCHAR); /* SITUACION CADUCIDAD */
        call.setString(17, this.aceptaCompensacion); /* ACEPTA COMPENSACION */
        call.setLong(18, Codigo.longValue()); /* CODIGO PROPUESTA */
        call.setNull(19, java.sql.Types.NUMERIC); /* ID REPROG ORIGEN*/
        call.setNull(20, java.sql.Types.NUMERIC); /* ID REPROG DESTINO */
        call.setString(21,((transportista==1)?"N":"S"));/* ES TRANSPORTISTA */
        call.setString(22,this.deudas.getTipoImpuesto());/* TIPO IMPUESTO */
        call.execute();
        accion="Propuesta Actualizada";
      } else {
      
        //Solo si es propuesta aceptada o condonacion otrogada
        if (aceptadaRechazada==2)
        {
          if (tipoPago == TipoPago.CONDONACION_PAGO_CONTADO &&
            aceptadaRechazada == Etapas.PROPUESTA_ACEPTADA) {

            vCodEtapa=12;
          }
          else
          {
            vCodEtapa=aceptadaRechazada;
          }


          
        /* Verifica si alguna de las deudas esta ya en otra propuesta */
        Iterator itx = this.deudasContribuyenteSeleccionadas.iterator();
        
        while (itx.hasNext()) {
          DeudaWeb elemento = (DeudaWeb) itx.next();
          
          if (elemento.estaSeleccionado() && elemento.estaLiquidada()) {
            //Inserta en cv_deudas
            CallableStatement  callVerificaDeuda = conn.prepareCall("{? = call InsertaPropuesta.VerificaDeuda(?,?,?,?,?,?,?)}");

            callVerificaDeuda.registerOutParameter(1,java.sql.Types.VARCHAR);

            callVerificaDeuda.setLong(2,elemento.getTipoContribuyente());/*Tipo contribuyente */
            callVerificaDeuda.setLong(3,elemento.getRutRol());/*Rut Rol */
            callVerificaDeuda.setLong(4,elemento.getTipoFormulario());/*Formulario */
            callVerificaDeuda.setLong(5,elemento.getFolio());/*Folio */
            callVerificaDeuda.setDate(6,elemento.getFechaVencimiento());/*Fecha Vencimiento */
            callVerificaDeuda.setLong(7,vCodEtapa);
            callVerificaDeuda.setString(8,elemento.getOrigen());

            callVerificaDeuda.execute();
            String verifica = (String) callVerificaDeuda.getString(1);/* si viene 0 indica �xito */

            if (!verifica.equals("0"))
            {
                existe=false;
            }

          }
        }
        }
        
        if (existe)
        {
        /* llama a function que inserta la propuesta en cv_propuestas*/
        call = conn.prepareCall("{? = call InsertaPropuesta.InsertarPropuesta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        call.registerOutParameter(1, java.sql.Types.VARCHAR);
        call.setLong(2, aceptadaRechazada);  /* COD_ETAPA_CONVENIO */
        call.setLong(3, tipoPago);   /* COD_TIPO_PAGO */
        call.setInt(4, this.tipoConvenio);   /* COD_TIPO_CONVENIO */
        call.setLong(5, contribuyente.getIdPersona().longValue()); /* ID_PERSONA */
        call.setLong(6, this.idTesoreria.longValue());   /* ID_TESORERIA */
        call.setLong(7, this.codigoFuncionario.longValue());   /* COD_FUNCIONARIO */
        call.setDate(8, new java.sql.Date(System.currentTimeMillis())); /* FECHA_GENERACION */
        call.setDate(9, new java.sql.Date(System.currentTimeMillis())); /* FECHA_RESOLUCION */
        call.setDate(10, (java.sql.Date) new java.sql.Date(DateExt.addMes(new java.sql.Date(System.currentTimeMillis())).getTime()));
        call.setNull(11, java.sql.Types.DATE);   /* FECHA_ACTIVACION */
        call.setLong(12, this.porcentajeCuotaContado.longValue()); /* PORC_CUOTA_CONTADO */
        call.setLong(13, this.numeroCuotas.longValue());   /* NUM_CUOTAS */
        call.setLong(14, cuotas.getMontoCuotaContado());   /* MONTO_CUOTA_CONTADO */
        call.setLong(15, cuotas.getMontoCuotaFija());   /* MONTO_CUOTA_FIJA */
        call.setNull(16, java.sql.Types.NUMERIC);   /* MONTO_CUOTA_AJUSTE */
        call.setNull(17, java.sql.Types.VARCHAR); /* SITUACION CADUCIDAD */
        call.setString(18, this.aceptaCompensacion); /* ACEPTA COMPENSACION */
        call.setNull(19, java.sql.Types.NUMERIC); /* ID REPROG ORIGEN */
        call.setNull(20, java.sql.Types.NUMERIC); /* ID REPROG DESTINO */
        call.setString(21,((transportista==1)?"N":"S"));/* ES TRANSPORTISTA */
        call.setString(22,this.deudas.getTipoImpuesto());/* TIPO IMPUESTO */
        if (this.conveniosMasivo == null){
          call.setLong(23,-1);
        } else {
          call.setLong(23,this.conveniosMasivo.getNumeroResolucion());
        }
        call.execute();
        
      }
        
        if (aceptadaRechazada==1){
      	  accion="Propuesta Rechazada";
        }else{
        	accion="Propuesta Aceptada";
        }
      }
      
      
      
      
      if (existe)
      {
        String codPropuestaS = call.getString(1);
        try {
          codigoPropuestaGenerada = new Long(codPropuestaS);
        }
        catch (NumberFormatException eNFE) {
          throw new SQLException(
              "Error InsertaPropuesta.InsertarPropuesta o UpdatePropuesta  = " +
              eNFE.toString());
        }

        /* si tipo de pago es convenio pago contado */
        /* entonces cambia a etapa condonaci�n otorgada */

        if (tipoPago == TipoPago.CONDONACION_PAGO_CONTADO &&
            aceptadaRechazada == Etapas.PROPUESTA_ACEPTADA) {

          call = conn.prepareCall(
              "{? = call DatosPropuesta.CambioEtapaPropuesta(?,?,?,?)}");
          call.registerOutParameter(1, java.sql.Types.VARCHAR);
          call.setLong(2, codigoPropuestaGenerada.longValue());
              /* COD_PROPUESTA */
          call.setLong(3, Etapas.CONDONACION_OTORGADA);
              /* COD_ETAPA_CONVENIO  CONDONACION OTORGADA(12)*/
          call.setNull(4, java.sql.Types.NUMERIC); /* CAUSAL CADUCIDAD */
          call.setNull(5, java.sql.Types.VARCHAR); /* OBSERVACIONES */
          call.execute();
          String retorno = (String) call.getString(1);
              /* si viene blanco indica �xito */
        }

        Context ctxp = new InitialContext();
        Object homep = ctxp.lookup("cobranzas.convenios.negocio.ejb.session.stateless.utilConv");
        utilConvHome pondera= (utilConvHome) PortableRemoteObject.narrow(homep,utilConvHome.class);
        utilConv sessionPondera = pondera.create();

        /* inserta deudas en  cv_deudas */
        Iterator it = this.deudasContribuyenteSeleccionadas.iterator();

        while (it.hasNext()) {
          DeudaWeb element = (DeudaWeb) it.next();

          if (element.estaSeleccionado() && element.estaLiquidada()) {
            //Inserta en cv_deudas
            CallableStatement callCodigoDeudaExistente = conn.prepareCall("{? = call InsertaPropuesta.CodigoDeudaExistente(?,?,?,?,?,?,?)}");

            callCodigoDeudaExistente.registerOutParameter(1,java.sql.Types.VARCHAR);

            if (!element.origenEsNoCut()) {
              callCodigoDeudaExistente.setLong(2, element.getTipoContribuyente());
                  /*Tipo contribuyente */
              callCodigoDeudaExistente.setLong(3, element.getRutRol());
                  /*Rut Rol */
              callCodigoDeudaExistente.setLong(4, element.getTipoFormulario());
                  /*Formulario */
              callCodigoDeudaExistente.setLong(5, element.getFolio());
                  /*Folio */
              callCodigoDeudaExistente.setDate(6, element.getFechaVencimiento());

              /*Fecha Vencimiento */
              callCodigoDeudaExistente.setNull(7, java.sql.Types.NUMERIC);
                  /*cod deuda no cut es null*/
            }
            else {
              /* Obtiene cod deuda no cut*/
              CallableStatement callCodDeudaNoCut = conn.prepareCall("{? = call DetalleDeuda.fCurDatosDeudasNoCut(?,?,?,?,?)}");

              callCodDeudaNoCut.registerOutParameter(1, OracleTypes.CURSOR);

              callCodDeudaNoCut.setLong(2, element.getTipoContribuyente());
                  /*Tipo Contribuyente */
              callCodDeudaNoCut.setLong(3, element.getRutRol()); /*Rut Rol */
              callCodDeudaNoCut.setLong(4, element.getTipoFormulario());
                  /*Formulario */
              callCodDeudaNoCut.setLong(5, element.getFolio()); /*Folio */
              callCodDeudaNoCut.setDate(6, element.getFechaVencimiento());
                  /*Fecha Vencimiento */

              callCodDeudaNoCut.execute();

              ResultSet rs = (ResultSet) callCodDeudaNoCut.getObject(1);

              long codDeudaNoCut = 0;
              while (rs.next()) {
                codDeudaNoCut = (long) rs.getLong(1);
              }
              callCodDeudaNoCut.close();
              callCodDeudaNoCut = null;

              callCodigoDeudaExistente.setNull(2, java.sql.Types.NUMERIC);
                  /*Tipo contribuyente */
              callCodigoDeudaExistente.setNull(3, java.sql.Types.NUMERIC);
                  /*Rut Rol */
              callCodigoDeudaExistente.setNull(4, java.sql.Types.NUMERIC);
                  /*Formulario */
              callCodigoDeudaExistente.setNull(5, java.sql.Types.NUMERIC);
                  /*Folio */
              callCodigoDeudaExistente.setNull(6, java.sql.Types.DATE);
                  /*Fecha Vencimiento */
              callCodigoDeudaExistente.setLong(7, codDeudaNoCut);
                  /*cod deuda no cut*/
            }
//27-10-2005
            
            if (element.getOrigen().equals("Q")
                ||
                element.getOrigen().equals("M"))
                {
              callCodigoDeudaExistente.setString(8, "C"); /*origen */
            }
            else {
              callCodigoDeudaExistente.setString(8, element.getOrigen());
                  /*origen */
            }
            callCodigoDeudaExistente.execute();

            Long codDeuda = new Long(0);
            String codDeudaS = callCodigoDeudaExistente.getString(1);
                /* Obtiene cod deuda de cv_deudas*/
            try {
              codDeuda = new Long(codDeudaS);
            }
            catch (NumberFormatException eNFE) {
              throw new SQLException(
                  "Error InsertaPropuesta.CodigoDeudaExistente = " +
                  eNFE.toString());
            }
            callCodigoDeudaExistente.close();
            callCodigoDeudaExistente = null;

            /* Inserta en cv_deudas_incluidas */
            CallableStatement callDeudaIncluida = conn.prepareCall("{? = call InsertaPropuesta.InsertarDeudasIncluidas(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            callDeudaIncluida.registerOutParameter(1, java.sql.Types.VARCHAR);

            callDeudaIncluida.setLong(2, codigoPropuestaGenerada.longValue());
                /* c�digo propuesta */
            callDeudaIncluida.setLong(3, codDeuda.longValue());
                /* c�digo deuda */
              //MYPE
             if   (element.getOrigen().equals("M"))
             {
                callDeudaIncluida.setLong(4,this.conveniosMasivo.getPorcentajeCondonacion());
                callDeudaIncluida.setLong(5,this.conveniosMasivo.getPorcentajeCondonacion());
             }
             else
             {
                 callDeudaIncluida.setLong(4, element.getPorcentajeCondonacion());
                /* porcentaje condonaci�n */
                callDeudaIncluida.setLong(5, element.getPorcentajeCondonacion());
                /* porcentaje condonaci�n inicial */
             }
             //
            callDeudaIncluida.setNull(6, java.sql.Types.NUMERIC);
                /* porcentaje condonaci�n solicitado */
            callDeudaIncluida.setNull(7, java.sql.Types.NUMERIC);
                /* porcentaje condonaci�n otorgado */
            callDeudaIncluida.setLong(8, element.getMonto()); /* monto neto */
            callDeudaIncluida.setLong(9, element.getReajustes());
                /* reajustes */
            callDeudaIncluida.setLong(10, element.getMultas()); /* multas */
            callDeudaIncluida.setLong(11, element.getIntereses());
                /* intereses */
            callDeudaIncluida.setLong(12, element.getMontoConCondonacion());
                /* total a pagar */

            if (this.tipoPago == TipoPago.CONDONACION_PAGO_CONTADO) {
             // System.out.println("fecha pago contado---> "+ (java.sql.Date)new java.sql.Date(((java.util.Date)this.fechaVigencia).getTime()));
             // System.out.println("fecha pago contado---> "+ ((java.util.Date)this.fechaVigencia).getTime());
              callDeudaIncluida.setDate(13,(java.sql.Date)new java.sql.Date(((java.util.Date)this.fechaVigencia).getTime()));
            }
            else if (this.tipoPago == TipoPago.CONVENIO_CON_CONDONACION) {
              long tiempo = DateExt.addMes(this.fechaVigencia, 2).getTime();

              callDeudaIncluida.setDate(13,(java.sql.Date)new java.sql.Date(tiempo));
            }
            else {
              // callDeudaIncluida.setNull(13, java.sql.Types.DATE);
              //se realiza siguiente cambio a petici�n de CBT 04-06-2013  ahora los sin condonaci�n envian fecha validez igual que los con condonaci�n

              long tiempo = DateExt.addMes(this.fechaVigencia, 1).getTime();
              callDeudaIncluida.setDate(13,(java.sql.Date)new java.sql.Date(tiempo));
            }

            
            callDeudaIncluida.setString(14, element.getCondonacionAplicadaA());
                /* aplica condonaci�n */
            callDeudaIncluida.setLong(15, element.getReajusteProyectado());
                /* reajustes vax*/
            callDeudaIncluida.setLong(16, element.getMultasProyectadas());
                /* multas vax*/
            callDeudaIncluida.setLong(17, element.getInteresProyectado());
                /* intereses vax*/
            callDeudaIncluida.setLong(18, element.getValorTotal());
                /* total a pagar vax*/
            callDeudaIncluida.setString(19, element.getPosibilidadCondonacion());
                /*derecho a condonaci�n*/
    //MYPE
                 if   (element.getOrigen().equals("M"))
                 {
                    callDeudaIncluida.setLong(20,this.conveniosMasivo.getPorcentajeCondonacion());
                    callDeudaIncluida.setLong(21,this.conveniosMasivo.getPorcentajeCondonacion());
                 }
                 else
                 {
                   callDeudaIncluida.setLong(20,
                                             element.getPorcentajeCondonacionIntereses());
                       /* porcentaje condonaci�n a intereses*/
                   callDeudaIncluida.setLong(21,
                                             element.getPorcentajeCondonacionIntereses());
                       /* porcentaje condonaci�n inicial a intereses*/
                 }
                 //
            callDeudaIncluida.setNull(22, java.sql.Types.NUMERIC);
                /* porcentaje condonaci�n solicitado a intereses*/
            callDeudaIncluida.setNull(23, java.sql.Types.NUMERIC);
                /* porcentaje condonaci�n otorgado a intereses*/
    //MYPE
                     if   (element.getOrigen().equals("M"))
                     {
                        callDeudaIncluida.setLong(24,this.conveniosMasivo.getPorcentajeCondonacion());
                        callDeudaIncluida.setLong(25,this.conveniosMasivo.getPorcentajeCondonacion());
                     }
                     else
                     {
                       callDeudaIncluida.setLong(24,
                                                 element.getPorcentajeCondonacionMultas());
                           /* porcentaje condonaci�n a multas*/
                       callDeudaIncluida.setLong(25,
                                                 element.getPorcentajeCondonacionMultas());
                           /* porcentaje condonaci�n inicial a multas*/
                     }
                     //

            callDeudaIncluida.setNull(26, java.sql.Types.NUMERIC);
                /* porcentaje condonaci�n solicitado a multas*/
            callDeudaIncluida.setNull(27, java.sql.Types.NUMERIC);
                /* porcentaje condonaci�n otorgado a multas*/

            callDeudaIncluida.setInt(28,element.getCodigoItemDeuda());
            callDeudaIncluida.execute();
            String retornoDeudaIncluidaS = callDeudaIncluida.getString(1);
            Long retornoDeudaIncluida;
            try {
              retornoDeudaIncluida = new Long(retornoDeudaIncluidaS);
            }
            catch (NumberFormatException eNFE) {
              throw new SQLException("InsertaPropuesta.InsertaDeudasIncluidas=" +
                                     eNFE.toString());
            }
            callDeudaIncluida.close();
            callDeudaIncluida = null;
            
            if (this.tipoPago == TipoPago.CONDONACION_PAGO_CONTADO) {
              Context ctx = new InitialContext();
              Object home = ctx.lookup("cobranzas.corporativo.servicioCondonacion.Condonacion");
              CondonacionHome condonacionHome = (CondonacionHome)PortableRemoteObject.narrow(home, CondonacionHome.class);
              Condonacion condonacion = condonacionHome.create();

              CallableStatement callRES = conn.prepareCall("{? = call InsertaPropuesta.ObtieneNumeroResolucion(?)}");
              callRES.registerOutParameter(1, java.sql.Types.VARCHAR);
              callRES.setLong(2, codigoPropuestaGenerada.longValue());
                  /* c�digo propuesta */
              callRES.execute();
              String sResol = callRES.getString(1);
              callRES.close();

              Long VInteres = (Long)new Long(element.getIntereses());
              Long VMultas = (Long)new Long(element.getMultas());
              Long VTotal = (Long)new Long(VInteres.longValue() + VMultas.longValue());
              Long VPorcCondInteres = (Long)new Long(element.getPorcentajeCondonacionIntereses());
              Long VPorcCondMultas = (Long)new Long(element.getPorcentajeCondonacionMultas());
              resolucion=sResol;
              long VPorcCondProm=sessionPondera.CondonacionPonderada(VMultas.longValue(),VPorcCondMultas.longValue(),VInteres.longValue(),VPorcCondInteres.longValue());

             /* BigDecimal VCondInteres = (BigDecimal)new BigDecimal( (VInteres.longValue() * VPorcCondInteres.longValue()) / 100);
              BigDecimal VCondMultas = (BigDecimal)new BigDecimal( (VMultas.longValue() * VPorcCondMultas.longValue()) / 100);
              BigDecimal VCondTotal = (BigDecimal)new BigDecimal(VCondInteres.doubleValue() + VCondMultas.doubleValue());
              BigDecimal VPorcCond = (BigDecimal)new BigDecimal( (VCondTotal.doubleValue()* 100) / VTotal.doubleValue());
              System.out.println("Porcentaje ponderado:"+VPorcCond.doubleValue());*/

            /*  if ((element.getPorcentajeCondonacionIntereses()>0)&&(element.getPorcentajeCondonacionMultas()==0))
              {
                VPorcCondProm=new Long(element.getPorcentajeCondonacionIntereses());
              }
              else
              {
                if ((element.getPorcentajeCondonacionIntereses()==0)&&(element.getPorcentajeCondonacionMultas()>0))
                {
                  VPorcCondProm=new Long(element.getPorcentajeCondonacionMultas());
                }
                else
                {*/
                  //VPorcCondProm = (Long)new Long(Math.round(VPorcCond.doubleValue()));
                /*}
              }*/
              //System.out.println("Porcentaje Promedio:"+VPorcCondProm);
              String  vencimiento = DateExt.format("AAAAMM",element.getFechaVencimiento());
              if (!vencimiento.equals("000000")){
                  if (element.getTipoFormulario() == 29) {

                String vencimientoAnno = vencimiento;
                String vencimientoMes = vencimiento;

                vencimientoAnno = vencimientoAnno.substring(0, 4);
                vencimientoMes = vencimientoMes.substring(4, 6);
                //System.out.println("vencimientoAnno=" + vencimientoAnno);
                //System.out.println("vencimientoMes=" + vencimientoMes);

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

              if (element.getTipoFormulario()==30){
                TipCont = "2";
              }
              else
              {
                TipCont = "1";
              }

              String vCodigoItemDeuda="";

              if (this.perfil==8)
              {
                if ((element.getTipoContribuyente()==1) || (element.getTipoContribuyente()==4))
                {
                  vCodigoItemDeuda="0895";
                }
                else
                {
                  vCodigoItemDeuda="0099";

                }
              }

              else
              {
                vCodigoItemDeuda=StringExt.format("0000",element.getCodigoItemDeuda());
              }

              //System.out.println("TipCont:"+TipCont);
              //System.out.println("rut_rol:"+StringExt.format("00000000000000",element.getRutRol()));
              //System.out.println("Tip_form:"+StringExt.format("000", element.getTipoFormulario()));
              //System.out.println("folio:"+StringExt.format("0000000000",element.getFolio()));
              //System.out.println("vencimiento:"+vencimiento);
              //System.out.println("resolucion:"+StringExt.format("0000000",Long.parseLong(sResol)));
              //System.out.println("fecha vigencia:"+DateExt.format("AAAAMMDD", this.fechaVigencia));
              //System.out.println("porc. prom.:"+StringExt.format("000",VPorcCondProm));
              //System.out.println("item deuda:"+vCodigoItemDeuda);

              condonacion.condona(TipCont,StringExt.format("00000000000000",element.getRutRol()),StringExt.format("000", element.getTipoFormulario()),
                                  StringExt.format("0000000000",element.getFolio()),vencimiento,StringExt.format("0000000",Long.parseLong(sResol)),
                                  DateExt.format("AAAAMMDD", this.fechaVigencia),StringExt.format("000",VPorcCondProm),vCodigoItemDeuda);

              resp = condonacion.getCadena();
              ret = 0;
              boolean condonacionRealizada = true;
              try {
                if (!resp.substring(4,5).equals(" "))
                {
                  ret = 2;
                 // System.out.println("La condonacion NO se realiz� con exito");
                }
                else
                {
                  ret = Integer.parseInt(condonacion.getValorRet());
                  if (ret != 0) {
                    //System.out.println("La condonacion NO se realiz� con exito");
                  }
                  else {
                    //System.out.println("La condonacion se realiz� exitosamente");
                  }
                }
              }
              catch (Exception e) {
                ret = 2;
                //System.out.println("La condonacion NO se realiz� con exito");
                throw new EJBException("La condonacion NO se realiz� con exito:"+e);
              }
              if (ret !=0){
                // modificaci�n mrc 27-07-2005, se reintenta condonaci�n
                      resp = "";
                      ret = 0;
                      condonacionRealizada = true;


                      //System.out.println("TipCont 2:"+TipCont);
                      //System.out.println("rut_rol 2:"+StringExt.format("00000000000000",element.getRutRol()));
                      //System.out.println("Tip_form 2:"+StringExt.format("000", element.getTipoFormulario()));
                      //System.out.println("folio 2:"+StringExt.format("0000000000",element.getFolio()));
                      //System.out.println("vencimiento 2:"+vencimiento);
                      //System.out.println("resolucion 2:"+StringExt.format("0000000",Long.parseLong(sResol)));
                      //System.out.println("fecha vigencia 2:"+DateExt.format("AAAAMMDD", this.fechaVigencia));
                      //System.out.println("porc. prom. 2:"+StringExt.format("000",VPorcCondProm));
                      //System.out.println("item deuda 2:"+vCodigoItemDeuda);

                      condonacion.condona(TipCont,StringExt.format("00000000000000",element.getRutRol()),StringExt.format("000", element.getTipoFormulario()),
                                          StringExt.format("0000000000",element.getFolio()),vencimiento,StringExt.format("0000000",Long.parseLong(sResol)),
                                          DateExt.format("AAAAMMDD", this.fechaVigencia),StringExt.format("000",VPorcCondProm),vCodigoItemDeuda);

                      resp = condonacion.getCadena();
                          try {
                            if (!resp.substring(4,5).equals(" "))
                            {
                              ret = 2;
                              //System.out.println("La condonacion NO se realiz� con exito por segunda");
                            }
                            else
                            {
                              ret = Integer.parseInt(condonacion.getValorRet());
                              if (ret != 0) {
                                //System.out.println("La condonacion NO se realiz� con exito por segunda");
                              }
                              else {
                                //System.out.println("La condonacion se realiz� exitosamente por segunda");
                              }
                            }
                          }
                          catch (Exception e) {
                            ret = 2;
                            //System.out.println("La condonacion NO se realiz� con exito por segunda");
                            throw new EJBException("La condonacion NO se realiz� con exito por segunda:"+e);
                          }
                // fin modificaci�n mrc 27-07-2005
              }
              if (ret !=0) {

                // La condonaci�n en l�nea no pudo efectuarse
                CallableStatement callAC = conn.prepareCall("{? = call InsertaPropuesta.ErrorCondonacionVax(?,?)}");
                callAC.registerOutParameter(1, java.sql.Types.VARCHAR);
                callAC.setLong(2, codigoPropuestaGenerada.longValue());
                    /* c�digo propuesta */
                callAC.setLong(3, codDeuda.longValue()); /* c�digo deuda */
                callAC.execute();

                String sRes = callAC.getString(1);

                callAC.close();
              }

              condonacion = null;
              condonacionHome = null;
              home = null;
            }

          }
        }
        /* while (it.hasNext()) */

        /* inserta cuotas  */
        try {

          cuotas.setRut(this.contribuyente.getRut().longValue());
          cuotas.setInserta(conn, codigoPropuestaGenerada.longValue(),TiposPagoCuotas.CUT);
        }
        catch (SQLException eSQL) {
          throw new SQLException(eSQL.toString());
        }

        call.close();
        call = null;

        conn.commit();
        //conn.setAutoCommit(true);
        conn = null;
        
      }
      
     //inserto auditoria 
 	 
     //antes pregunto por resolucion 
      
      if (resolucion==null){
          conn = this.getConnection();
          conn.setAutoCommit(false);
    	  CallableStatement callRES = conn.prepareCall("{? = call InsertaPropuesta.ObtieneNumeroResolucion(?)}");
          callRES.registerOutParameter(1, java.sql.Types.VARCHAR);
          callRES.setLong(2, codigoPropuestaGenerada.longValue());
              /* c�digo propuesta */
          callRES.execute();
          resolucion = callRES.getString(1);
          callRES.close();
          conn.commit();
          //conn.setAutoCommit(true);
          conn = null;
          callRES=null;
       
      }
      
      Identificacion=" N�mero Resoluci�n = "+resolucion+" - Rut Contribuyente ="+this.contribuyente.getRut()+" - Acci�n = "+accion+" - idTesoreria = "+this.idTesoreria.toString();
	 IngAuditoria.put("IDENTIFICACION",Identificacion);
	 IngAuditoria.put("NUM_RESOLUCION",new Long(resolucion));    
	 sessionAuditoria.setAuditoria(IngAuditoria);      
       
    } catch(SQLException e) {
      conn.rollback();
      //conn.setAutoCommit(true);
      conn = null;

      throw new SQLException("Error ejecutando el SQL " + e.toString());
    }catch (Exception a){
    	a.printStackTrace();
    }
    finally{

      this.closeConnection();
    }


	 
    return codigoPropuestaGenerada;
  }

 /** 
 *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.interface-method view-type="both"
 * <!-- end-xdoclet-definition --> 
 * @generated
 * M�todo que inserta la propuesta, usado solo cuando el total a pagar de
   *  la propuesta se esta creando esta fuera de los montos requeridos por el perfil.
   *  @param aceptadaRechazada(long) valor que indica propuesta aceptada "1" o propuesta rechazada "2"
   *  @return Long - codigo de la propuesta generada, si el valor es cero indica que no se insert� la propuesta.
   */
  public Long insertaPropuestaCasoEspecial(long aceptadaRechazada) throws RemoteException, SQLException {
    PreparedStatement ps = null;
    Connection conn = null;
    CallableStatement  call = null;
    String codPropuestaS = "";
    Long codigoPropuestaGenerada;
    try {
      //conn = dataSource.getConnection();
      conn = this.getConnection();
      conn.setAutoCommit(false);

      try {
        /* llama a function que inserta la propuesta en cv_propuestas*/
        call = conn.prepareCall("{? = call InsertaPropuesta.InsertarPropuesta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        call.registerOutParameter(1, java.sql.Types.VARCHAR);
        call.setLong(2, aceptadaRechazada);  /* COD_ETAPA_CONVENIO */
        call.setLong(3, tipoPago);   /* COD_TIPO_PAGO */
        call.setInt(4,  this.tipoConvenio);   /* COD_TIPO_CONVENIO */
        call.setLong(5, contribuyente.getIdPersona().longValue()); /* ID_PERSONA */
        call.setLong(6, this.idTesoreria.longValue());   /* ID_TESORERIA */
        call.setLong(7, this.codigoFuncionario.longValue());   /* COD_FUNCIONARIO */
        call.setDate(8, new java.sql.Date(System.currentTimeMillis())); /* FECHA_GENERACION */
        call.setDate(9, new java.sql.Date(System.currentTimeMillis())); /* FECHA_RESOLUCION */
        call.setDate(10, (java.sql.Date) new java.sql.Date(DateExt.addMes(new java.sql.Date(System.currentTimeMillis())).getTime()));
        call.setNull(11, java.sql.Types.DATE);   /* FECHA_ACTIVACION */
        call.setNull(12, java.sql.Types.NUMERIC); /* PORC_CUOTA_CONTADO */
        call.setNull(13, java.sql.Types.NUMERIC);   /* NUM_CUOTAS */
        call.setNull(14, java.sql.Types.NUMERIC);   /* MONTO_CUOTA_CONTADO */
        call.setNull(15, java.sql.Types.NUMERIC);   /* MONTO_CUOTA_FIJA */
        call.setNull(16, java.sql.Types.NUMERIC);   /* MONTO_CUOTA_AJUSTE */
        call.setNull(17, java.sql.Types.VARCHAR); /* SITUACION CADUCIDAD */
        call.setString(18, "S"); /* ACEPTA COMPENSACION */
        call.setNull(19, java.sql.Types.NUMERIC); /* ID REPROG ORIGEN */
        call.setNull(20, java.sql.Types.NUMERIC); /* ID REPROG DESTINO */
        call.setString(21,((transportista==1)?"N":"S"));/* ES TRANSPORTISTA */
        call.setString(22,this.deudas.getTipoImpuesto());/* TIPO IMPUESTO */
        call.setLong(23, -1);/* */

        call.execute();

      } catch (SQLException eSQL) {
        throw new SQLException ("InsertaPropuesta.InsertarPropuesta =" + eSQL.toString());
      }

      codPropuestaS = call.getString(1);
      try {
        codigoPropuestaGenerada = new Long(codPropuestaS);
      } catch (NumberFormatException eNFE) {
        throw new SQLException ("Error conversion = " + eNFE.toString());
      }

      /* inserta deudas en  cv_deudas */
      Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
      while (it.hasNext()) {
        DeudaWeb element = (DeudaWeb) it.next();

        if (element.estaSeleccionado()) {
          //Inserta en cv_deudas
          CallableStatement  callCodigoDeudaExistente = conn.prepareCall("{? = call InsertaPropuesta.CodigoDeudaExistente(?,?,?,?,?,?,?)}");

          callCodigoDeudaExistente.registerOutParameter(1,java.sql.Types.VARCHAR);


          if (!element.origenEsNoCut()) {
            callCodigoDeudaExistente.setLong(2,element.getTipoContribuyente());/*Tipo contribuyente */
            callCodigoDeudaExistente.setLong(3,element.getRutRol());/*Rut Rol */
            callCodigoDeudaExistente.setLong(4,element.getTipoFormulario());/*Formulario */
            callCodigoDeudaExistente.setLong(5,element.getFolio());/*Folio */
            callCodigoDeudaExistente.setDate(6,element.getFechaVencimiento());/*Fecha Vencimiento */
            callCodigoDeudaExistente.setNull(7,java.sql.Types.NUMERIC);/*cod deuda no cut es null*/
          } else {
            /* Obtiene cod deuda no cut*/
            CallableStatement  callCodDeudaNoCut = conn.prepareCall("{? = call DetalleDeuda.fCurDatosDeudasNoCut(?,?,?,?,?)}");

            callCodDeudaNoCut.registerOutParameter(1, OracleTypes.CURSOR);
            callCodDeudaNoCut.setLong(2,element.getTipoContribuyente());/*Tipo Contribuyente */
            callCodDeudaNoCut.setLong(3,element.getRutRol());/*Rut Rol */
            callCodDeudaNoCut.setLong(4,element.getTipoFormulario());/*Formulario */
            callCodDeudaNoCut.setLong(5,element.getFolio());/*Folio */
            callCodDeudaNoCut.setDate(6,element.getFechaVencimiento());/*Fecha Vencimiento */

            callCodDeudaNoCut.execute();

            ResultSet rs = (ResultSet) callCodDeudaNoCut.getObject(1);

            long codDeudaNoCut = 0;
            while(rs.next()) {
              codDeudaNoCut = (long) rs.getLong(1);
            }

            callCodDeudaNoCut.close(); callCodDeudaNoCut = null;

            callCodigoDeudaExistente.setNull(2,java.sql.Types.NUMERIC);/*Tipo contribuyente */
            callCodigoDeudaExistente.setNull(3,java.sql.Types.NUMERIC);/*Rut Rol */
            callCodigoDeudaExistente.setNull(4,java.sql.Types.NUMERIC);/*Formulario */
            callCodigoDeudaExistente.setNull(5,java.sql.Types.NUMERIC);/*Folio */
            callCodigoDeudaExistente.setNull(6,java.sql.Types.DATE);/*Fecha Vencimiento */
            callCodigoDeudaExistente.setLong(7,codDeudaNoCut);/*cod deuda no cut*/
          }

           if (element.getOrigen().equals("Q")){
                callCodigoDeudaExistente.setString(8,"C");/*origen */
            } else {
                callCodigoDeudaExistente.setString(8,element.getOrigen());/*origen */
            }

          callCodigoDeudaExistente.execute();

          Long codDeuda = new Long(0);
          String codDeudaS = callCodigoDeudaExistente.getString(1);/* Obtiene cod deuda de cv_deudas*/
          try {
            codDeuda = new Long(codDeudaS);
          } catch (NumberFormatException eNFE) {
            throw new SQLException ("InsertaPropuesta.CodigoDeudaExistente = " + eNFE.toString());
          }

          callCodigoDeudaExistente.close(); callCodigoDeudaExistente= null;
          /* Inserta en cv_deudas_incluidas */
          CallableStatement callDeudaIncluida = conn.prepareCall("{? = call InsertaPropuesta.InsertarDeudasIncluidas(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

          callDeudaIncluida.registerOutParameter(1,java.sql.Types.VARCHAR);

          callDeudaIncluida.setLong(2,codigoPropuestaGenerada.longValue());/* c�digo propuesta */
          callDeudaIncluida.setLong(3,codDeuda.longValue());/* c�digo deuda */
          callDeudaIncluida.setLong(4,element.getPorcentajeCondonacion());/* porcentaje condonaci�n */
          callDeudaIncluida.setLong(5,element.getPorcentajeCondonacion());/* porcentaje condonaci�n inicial */
          callDeudaIncluida.setNull(6,java.sql.Types.NUMERIC);/* porcentaje condonaci�n solicitado */
          callDeudaIncluida.setNull(7,java.sql.Types.NUMERIC);/* porcentaje condonaci�n otorgado */
          callDeudaIncluida.setLong(8,element.getMonto());/* monto neto */
          callDeudaIncluida.setLong(9,element.getReajustes());/* reajustes */
          callDeudaIncluida.setLong(10,element.getMultas());/* multas */
          callDeudaIncluida.setLong(11,element.getIntereses());/* intereses */
          callDeudaIncluida.setLong(12,element.getMontoConCondonacion());/* total a pagar */

          if (this.tipoPago == TipoPago.CONDONACION_PAGO_CONTADO){
              callDeudaIncluida.setDate(13, (java.sql.Date) new java.sql.Date(((java.util.Date) this.fechaVigencia).getTime()));
          } else if (this.tipoPago == TipoPago.CONVENIO_CON_CONDONACION) {
              long tiempo = DateExt.addMes(this.fechaVigencia,2).getTime();
              callDeudaIncluida.setDate(13, (java.sql.Date) new java.sql.Date(tiempo));
          } else {
              callDeudaIncluida.setNull(13,java.sql.Types.DATE);
          }

          callDeudaIncluida.setString(14,element.getCondonacionAplicadaA());/* aplica condonaci�n */
          callDeudaIncluida.setLong(15,element.getReajusteProyectado());/* reajustes vax*/
          callDeudaIncluida.setLong(16,element.getMultasProyectadas());/* multas vax*/
          callDeudaIncluida.setLong(17,element.getInteresProyectado());/* intereses vax*/
          callDeudaIncluida.setLong(18,element.getValorTotal());/* total a pagar vax*/
          callDeudaIncluida.setString(19,element.getPosibilidadCondonacion());/*derecho a condonaci�n*/

          /*derecho a condonaci�n*/
          //MYPE
          if   (element.getOrigen().equals("M"))
          {
               callDeudaIncluida.setLong(20,this.conveniosMasivo.getPorcentajeCondonacion());
               callDeudaIncluida.setLong(21,this.conveniosMasivo.getPorcentajeCondonacion());
          }
          else
          {
               callDeudaIncluida.setLong(20,element.getPorcentajeCondonacionIntereses());
               /* porcentaje condonaci�n a intereses*/
               callDeudaIncluida.setLong(21,element.getPorcentajeCondonacionIntereses());
               /* porcentaje condonaci�n inicial a intereses*/
            }
                //
           callDeudaIncluida.setLong(22, element.getPorcentajeCondonacionSolicitadoIntereses());
               /* porcentaje condonaci�n solicitado a intereses*/
           callDeudaIncluida.setLong(23, element.getPorcentajeCondonacionOtorgadoIntereses());
               /* porcentaje condonaci�n otorgado a intereses*/
   //MYPE
              if   (element.getOrigen().equals("M"))
              {
                       callDeudaIncluida.setLong(24,this.conveniosMasivo.getPorcentajeCondonacion());
                       callDeudaIncluida.setLong(25,this.conveniosMasivo.getPorcentajeCondonacion());
              }
              else
              {
                      callDeudaIncluida.setLong(24,element.getPorcentajeCondonacionMultas());
                      /* porcentaje condonaci�n a multas*/
                      callDeudaIncluida.setLong(25,element.getPorcentajeCondonacionMultas());
                      /* porcentaje condonaci�n inicial a multas*/
                    }
                    //

           callDeudaIncluida.setLong(26, element.getPorcentajeCondonacionSolicitadoMultas());
               /* porcentaje condonaci�n solicitado a multas*/
           callDeudaIncluida.setLong(27, element.getPorcentajeCondonacionOtorgadoMultas());

           callDeudaIncluida.setInt(28,element.getCodigoItemDeuda());
          callDeudaIncluida.execute();

          String retornoDeudaIncluidaS = callDeudaIncluida.getString(1);
          try {
            Long retornoDeudaIncluida = new Long(retornoDeudaIncluidaS);
          } catch (NumberFormatException eNFE) {
            throw new SQLException ("InsertaPropuesta.InsertarDeudasIncluidas = " + eNFE.toString());
          }
          callDeudaIncluida.close(); callDeudaIncluida = null;
        }
      }/* while (it.hasNext()) */

      call.close(); call = null;
      conn.commit();
      //conn.setAutoCommit(true);

    } catch(SQLException e) {
      conn.rollback();
      //conn.setAutoCommit(true);

      throw new SQLException("Error ejecutando el SQL = " +  e.toString());
    }
    finally{
      try{
        conn.close();
      }
      catch (Exception e){

      }
    }
    return codigoPropuestaGenerada;
  }

  /** Retorna el c�digo de la propuesta a modificar
   *  @return Long con el c�digo de la propuesta
   */
  public Long getCodigoPropuesta() {
    return this.codigoPropuesta;
  }
  /**Establece el c�digo de la propuesta a modificar
   * @param codigoPropuesta(Long), c�digo de la propuesta a modificar
   */
  public void setCodigoPropuesta(Long codigoPropuesta) {
    this.codigoPropuesta = codigoPropuesta;

  }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que selecciona las deudas que van a participar en la propuesta, tambi�n
   *  establece valores a otros m�todos de la clase deudas
   *  @param arregloDeudas(String), que contiene la identificaci�n de las deudas
   *  que se van a participar en la propuesta, las deudas separadas por comas(,) y las
   *  claves de la deuda por asterisco(*).
   */
  public void setSeleccionaDeudasContribuyente(String arregloDeudas) throws RemoteException {
    try {

      System.out.println("estoy en setSeleccionaDeudasContribuyente");
      //System.out.println("mira getPagoTotal-->"+this.getPagoTotal());
      reformaTributaria.setPagoTotalConvenio(this.getPagoTotal());
      
   if (reformaTributaria.getIsReformaTributaria().booleanValue()){   
      this.getFactorPoliticaConv(reformaTributaria);
     
    /*reviso tipo de garanta territorial*/
      if (this.getGarantiaTerritorial()!=null){
    	  //System.out.println("**********ESTOY SOBREESCRIBINDO LA GARANTIA NORMAL POR TERRITORIAL**********");
    	  //System.out.println("garantia trae----->"+ this.getGarantiaTerritorial().intValue());
    	  if (this.getGarantiaTerritorial().intValue()==1){
    		  reformaTributaria.setTieneGarantia(new Boolean(true));
    	  }else{
    		  reformaTributaria.setTieneGarantia(new Boolean(false));
    	  }
    	  //System.out.println("**********FIN ESTOY SOBREESCRIBINDO LA GARANTIA NORMAL POR TERRITORIAL**********");
    	  
      }
      
      
    //System.out.println("**********ReformaTributariaInput setSeleccionaDeudasContribuyente**********");
	//System.out.println("getIsReformaTributaria--->"+ reformaTributaria.getIsReformaTributaria());
	//System.out.println("getRutContribuyente--->"+ reformaTributaria.getRutContribuyente());
	//System.out.println("getBeneficioPyme--->"+ reformaTributaria.getBeneficioPyme());
	//System.out.println("getBeneficioInternet--->"+ reformaTributaria.getBeneficioInternet());
	//System.out.println("getComportamientoConvenio--->"+ reformaTributaria.getComportamientoConvenio());
	//System.out.println("getPagoTotalConvenio--->"+ reformaTributaria.getPagoTotalConvenio());
	//System.out.println("**************setSeleccionaDeudasContribuyente****************************");   
      
      
	reformaTributariaOut=this.getComporConv(true,reformaTributaria);
	

		
	//System.out.println("**********reformaTributariaOut setSeleccionaDeudasContribuyente**********");
	//System.out.println("getCodError--->"+ reformaTributariaOut.getCodError());
	//System.out.println("getMaxCuota--->"+ reformaTributariaOut.getMaxCuota());
	//System.out.println("getMinCuotaContado--->"+ reformaTributariaOut.getMinCuotaContado());
	//System.out.println("**************setSeleccionaDeudasContribuyente****************************");   
   }	
     
      this.deudas.setConnection(this.getConnection());

      if ( this.deudasContribuyenteSeleccionadas != null){
        this.deudasContribuyenteSeleccionadas.clear();
        liquidada = false;
        this.porcentajeCondonacion = new Long (0);
        this.porcentajeCuotaContado = new Long (0);
        this.numeroCuotas = new Long (0);
      }

      this.deudas.setPerfil(this.perfil);
      this.deudas.setTransportista(this.transportista);
      this.deudas.setTipoPago(this.tipoPago);
      this.deudas.setDamnificado(this.getDamnificado());
      this.deudas.setDeudas(deudasContribuyente);
      this.deudas.seleccionaDeudasPropuesta(arregloDeudas, reformaTributaria);

      this.deudasContribuyente.clear();
      this.deudasContribuyente.addAll(deudas.getDeudas());

	  	if (!reformaTributaria.getIsIntranet().booleanValue()){
			System.out.println("entre dentro de intranet----------------------");
			System.out.println("entre muetro rut---------------------->"+reformaTributaria.getRutContribuyente());
			String beneficioCovid19=this.getBeneficioCovid19(reformaTributaria.getRutContribuyente().longValue());
			if (beneficioCovid19.equalsIgnoreCase("S")){
				System.out.println("----tiene beneficio---reescribo valores");
				reformaTributariaOut.setMaxCuota(new Integer("24"));
				reformaTributariaOut.setMinCuotaContado(new Integer("3"));
			}
			
		}
      
      this.calcularTotalPropuesta();
    } catch(Exception e) {
      throw new EJBException("Error ejecutando en setSeleccionaDeudasContribuyente " +  e.getMessage());
    }
//    finally{
//      this.closeConnection();
//    }
  }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * M�todo que selecciona las deudas de la demanda que van a participar en la propuesta, tambi�n
   *  establece valores a otros m�todos de la clase deudas
   *  @param arregloDeudas(String), que contiene la identificaci�n de las deudas
   *  que se van a participar en la propuesta, las deudas separadas por comas(,) y las
   *  claves de la deuda por asterisco(*).
   */
  public void setSeleccionaDeudasDemandasContribuyente(String arregloDeudas) throws RemoteException {

    Long vReajustes = new Long(0);
    Long vMultas = new Long(0);
    Long vIntereses = new Long(0);

    try {
      this.deudas.setConnection(this.getConnection());

      if ( this.deudasContribuyenteSeleccionadas != null){
        this.deudasContribuyenteSeleccionadas.clear();
        liquidada = false;
        this.porcentajeCondonacion = new Long (0);
        this.porcentajeCuotaContado = new Long (0);
        this.numeroCuotas = new Long (0);
      }

      this.deudas.setPerfil(this.perfil);
      this.deudas.setTransportista(this.transportista);
      this.deudas.setTipoPago(this.tipoPago);
      this.deudas.setDamnificado(this.getDamnificado());
      this.deudas.setDeudas(deudasContribuyente);
      this.deudas.seleccionaDeudasPropuesta(arregloDeudas, reformaTributaria);


      liquidada = true;

      Iterator it = this.deudasContribuyente.iterator();

            while (it.hasNext()){
                DeudaWeb deuda = (DeudaWeb) it.next();
                //System.out.println("esta Seleccionada ="+deuda.estaSeleccionado());

                if (!deuda.origenEsNoCut() && !deuda.estaLiquidada()&&deuda.estaSeleccionado()){

                    boolean liquidacionExitosa = false;
                    try {


                      Connection connLiquida = this.getConnection();
                      CallableStatement callLiquida = connLiquida.prepareCall("{? = call liquidacion.liquida(?,?,?,?,?,?,?)}");
                      callLiquida.registerOutParameter(1, OracleTypes.CURSOR);
                      callLiquida.setLong(2,deuda.getTipoContribuyente());/*Tipo Contribuyente */
                      callLiquida.setLong(3,deuda.getRutRol());/*Rut Rol */
                      callLiquida.setLong(4,deuda.getTipoFormulario());/*Formulario */
                      callLiquida.setLong(5,deuda.getFolio());/*Folio */
                      callLiquida.setDate(6,deuda.getFechaVencimiento());/*Fecha Vencimiento */
                      callLiquida.setLong(7,deuda.getMonto());
                      callLiquida.setDate(8,deuda.getFechaEmision());
                      callLiquida.execute();

                      ResultSet rsLiquida = (ResultSet) callLiquida.getObject(1);

                      while(rsLiquida.next()) {

                        vReajustes = (Long) new Long(rsLiquida.getLong(1));
                        vIntereses = (Long) new Long(rsLiquida.getLong(2));
                        vMultas = (Long) new Long(rsLiquida.getLong(3));


                      }

                      rsLiquida.close();
                      callLiquida.close();

                      liquidacionExitosa = true;

                    } catch(Exception e) {

                      liquidacionExitosa = false;

                      throw new Exception("Error VerificaMonto " + e.toString());
                    }



                    if (liquidacionExitosa){
                        try {
                          deuda.setReajustes(vReajustes.longValue());

                        } catch (Exception e) {
                            deuda.setReajustes(0);
                        }

                        try {
                          deuda.setIntereses(vIntereses.longValue());

                        } catch (Exception e) {
                            deuda.setIntereses(0);
                        }

                        try {
                          deuda.setMultas(vMultas.longValue());

                        } catch (Exception e) {
                            deuda.setMultas(0);
                        }

                        deuda.setReajusteProyectado(deuda.getReajustes());
                        deuda.setInteresProyectado(deuda.getIntereses());
                        deuda.setMultasProyectadas(deuda.getMultas());
                        deuda.setLiquidar(true);
                        this.tipoConvenio = deuda.getTipoConvenio();
                    }
                }

      }
      this.deudasContribuyente.clear();
      this.deudasContribuyente.addAll(deudas.getDeudas());

      this.calcularTotalPropuesta();
    } catch(Exception e) {
      throw new EJBException("Error ejecutando en setSeleccionaDeudasContribuyente " +  e.toString());
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
	 *Establece las deudas del contribuyente para calcular el total de la propuesta
   * @param deudasContribuyente(Collection), conjunto de deudas del contribuyente
   */
  public void setDeudasContribuyente (Collection deudasContribuyente) throws Exception {
    this.deudasContribuyenteSeleccionadas = (Vector) deudasContribuyente;
    Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
    totalPagar = new Long(0);
    this.calcularTotalPropuesta();
  }

  /**Establece el valor a transportista, este valor solo es considerado cuando existe
   * deudas con formulario 14 0 19 en la propuesta a generar.
   * @param transportista(int), indica si el contribuyente es transportista,
   * "1" no es transportista o "2" es transportista
   */
  public void setTransportista(int transportista){
    this.transportista = transportista;
  }

  /** Retorna valor que indica si el contribuyente es transportista o no
   *  @return int  es transportista valor "1" o  no es valor "2".
   */
  public int getTransportista(){
    return this.transportista;
  }
	 /** 
	 * M�todo privado que ordena las deudas por grupos
     *
     */
  private void ordenaGrupos (){
    Vector retorno = new Vector();
    retorno.addAll(this.deudasContribuyente);
    this.deudasContribuyente.clear();

    while(retorno.size() > 0) {
      Iterator itera = retorno.iterator();
      String grupo;
      String orden = "?";

      while(itera.hasNext()) {
        DeudaWeb elemento = (DeudaWeb) itera.next();

        grupo = elemento.getGrupo();
        String nuevoOrden = grupo;

        if ("No Agrupables (Sin derecho a convenio)".equals(grupo)) orden = nuevoOrden;
        if (orden.equals(nuevoOrden)) {
          this.deudasContribuyente.addElement(elemento);
          itera.remove();
        }
      }

      itera = retorno.iterator();

      while(itera.hasNext()) {
        DeudaWeb elemento = (DeudaWeb) itera.next();

        grupo = elemento.getGrupo();

        String nuevoOrden = grupo;

        if ("Art. 192 Fiscal".equals(grupo)) orden = nuevoOrden;
        if (orden.equals(nuevoOrden)) {
          this.deudasContribuyente.addElement(elemento);
          itera.remove();
        }
      }

      itera = retorno.iterator();

      while(itera.hasNext()) {
        DeudaWeb elemento = (DeudaWeb) itera.next();

        grupo = elemento.getGrupo();

        String nuevoOrden = grupo;

        if ("Art. 192 Territorial".equals(grupo)) orden = nuevoOrden;
        if (orden.equals(nuevoOrden)) {
          this.deudasContribuyente.addElement(elemento);
          itera.remove();
        }
      }

      itera = retorno.iterator();

      while(itera.hasNext()) {
        DeudaWeb elemento = (DeudaWeb) itera.next();

        grupo = elemento.getGrupo();

        String nuevoOrden = grupo;

        if ("?".equals(orden)) orden = nuevoOrden;
        if (orden.equals(nuevoOrden)) {
          this.deudasContribuyente.addElement(elemento);
          itera.remove();
        }
      }
    }
  }

  /**Retorna el nombre del tipo de papo de la propuesta
   * @return nombreTipoPago(String), contiene el nombre del tipo de pago
   */
  public String nombreTipoPago(){
    if (this.tipoPago == TipoPago.CONDONACION_PAGO_CONTADO) return "Condonaci�n Pago Contado";
    else if (this.tipoPago == TipoPago.CONVENIO_SIN_CONDONACION) return "Convenio Sin Condonaci�n";
    else return "Convenio Con Condonaci�n";
  }
  /** Retorna  la identificaci�n de la demanda seleccionada
   *  @return Long con el c�digo de la demanda
   */
  public Long getDemandaSeleccionada(){
    return demandaSeleccionada;
  }
  /** Retorna las demandas del contribuyente ingresado
   *  @return Collection con las demandas del contribuyente
   */
  public Collection getDemandasContribuyente() throws RemoteException, Exception {
         return this.demandasContribuyente;
  }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece la demanda seleccionada
  *  @param demandaSeleccionada(Long), corresponde a la demanda seleccionada
  */
 public void setDemandaContribuyente(Long demandaSeleccionada) throws RemoteException{
    Vector retorno2 = new Vector();
    retorno2.addAll(this.demandasContribuyente);
    this.demandasContribuyente.clear();
    this.demandaSeleccionada = demandaSeleccionada;

    if (retorno2.size()>0){
        Iterator itd = retorno2.iterator();
        while(itd.hasNext()) {
          HashMap elemento2 = (HashMap) itd.next();
          if (((Long) elemento2.get("CodDemanda")).longValue()== demandaSeleccionada.longValue()) {
              elemento2.put("Seleccionada",(String) "S");

           }
          else{
              elemento2.put("Seleccionada",(String) "N");
          }
        this.demandasContribuyente.addElement(elemento2);
        itd.remove();
      }
   }
 }


 /** 
 *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.interface-method view-type="both"
 * <!-- end-xdoclet-definition --> 
 * @generated
 * Retorna la colecci�n de demandas de un rol bien ra�z o un rut del contribuyente
   *  @param estadoCobranza(String), indica si la deuda esta en cobranza "S" o no esta
   *  en cobranza "N" y RolBienRaiz(String), puede recibir el rol bien ra�z o el rut del
   *  contribuyente
   *  @return Collection con las demandas del rol bien ra�z o del rut del contribuyente
  */
  public Collection getDemandasRolBienRaiz(String estadoCobranza, String RolBienRaiz) throws RemoteException, Exception {
        Connection conn;
        CallableStatement  call = null;
        ResultSet rs = null;
        Vector retorno = new Vector();
        retorno.clear();

        try {

            this.estadoCobranza = estadoCobranza;

            conn = this.getConnection();

            call = conn.prepareCall("{? = call DeudasContribuyente.ListaDemandasRolBienRaiz(?)}");
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

             retorno.addElement(sqlRetorno);
            }

            demandasContribuyente.addAll(retorno);

            rs = null;
            call = null;
            conn = null;
            return demandasContribuyente;

        } catch(SQLException e) {
            throw new EJBException("Error ejecutando el SQL en getDemandasRolBienRaiz " + e.toString());
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
	 * Retorna la colecci�n de demandas que pertenecen al contribuyente
   *  @param estadoCobranza(String), indica si la deuda esta en cobranza "S" o no "N"
   *  @return Collection con las demandas del contribuyente.
  */
  public Collection getDemandasContribuyente(String estadoCobranza) throws RemoteException, Exception {
        Connection conn;
        CallableStatement  call = null;
        ResultSet rs = null;
        Vector retorno = new Vector();
        retorno.clear();

        try {

            this.estadoCobranza = estadoCobranza;

            conn = this.getConnection();

            call = conn.prepareCall("{? = call DeudasContribuyente.ListaDemandas(?,?)}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            call.setString(2, contribuyente.getRut().toString());
            call.setString(3, contribuyente.getIdPersona().toString());
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
             sqlRetorno.put("Origen", (String) rs.getString(7));

             retorno.addElement(sqlRetorno);
            }

            demandasContribuyente.addAll(retorno);

            rs = null;
            call = null;
            conn = null;
            return demandasContribuyente;

        } catch(SQLException e) {
            throw new EJBException("Error ejecutando el SQL en getDemandasContribuyente " + e.toString());
        }
        finally{
          this.closeConnection();
        }
    }

   /** Establece las deudas del contribuyente, m�todo para en m�dulo Internet
    *  @param deudascontribuyente(Collection), conjunto de deudas del contribuyente
    */
   public void setDeudasContribuyenteInternet(Collection deudasContribuyente){
       this.deudasContribuyente = (Vector) deudasContribuyente;
   }


   /** Establece las deudas seleccionadas del contribuyente, m�todo para en m�dulo Internet
    *  @param deudascontribuyenteSeleccionadas(Collection), conjunto de deudas
    *  seleccionadas del contribuyente.
    */
   public void setDeudasContribuyenteSeleccionadasInternet(Collection deudasContribuyenteSeleccionadas){

        Collection temp = deudasContribuyenteSeleccionadas;
        this.deudasContribuyenteSeleccionadas = (Vector) temp;
   }

  /** M�todo que inicializa valores, realizado para el m�dulo de Internet
   *
   */
  public void InicializaDatosInternet() throws RemoteException{
        Integer rut = this.contribuyente.getRut();
        rutRoles.addElement((Long) new Long(rut.longValue()));
        rutRolesConsultados.put((String) rut.toString(),"N");
        this.deudasContribuyente.clear();
  }
  /** M�todo que establece la clase convenio masivo, realizado para Internet
   *  @param convenioMasivo clase ConvenioMasivo
   */
  public void setConvenioMasivoInternet(ConvenioMasivo convenioMasivo){
        this.conveniosMasivo = convenioMasivo;
  }

  /** Retorna las cuotas de la propuesta, realizada para el m�dulo Internet
   *  @ return Cuotas clase cuotas.
   */
  public Cuotas getCuotasInternet(){
      return this.cuotas;
  }
  /** Establece las cuotas de la propuesta, realizada para el m�dulo Internet
   *  @ param cuotas(clase cuotas), contiene las cuotas de la propuesta.
   */

  public void setClaseCuotasInternet(Cuotas cuotas){
      this.cuotas = cuotas;
  }

  public int getNumOtorgamiento(){
    return this.numOtorgamiento;
  }
 /** M�todo privado retorna la conexi�n a la base de datos
  *  @return Connection
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

  /** M�todo privado que cierra la conexi�n a la base de datos
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
            //System.out.println("Error GeneraPropuestaBean::closeConnection();");
        }
    }
  
	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public String getValidaEmision(String origen, int tipcont, long rutrol, int tipform, long folio, java.sql.Date fechavenc)throws RemoteException
    {
      try {


        Connection conn = this.getConnection();

        CallableStatement callMC = conn.prepareCall("{? = call ConfigurarPropuesta.verifica_215(?,?,?,?,?,?)}");


        callMC.registerOutParameter(1,java.sql.Types.DATE);

        callMC.setInt(2, tipcont);
        callMC.setLong(3, rutrol);
        callMC.setInt(4, tipform);/*Formulario */
        callMC.setLong(5, folio);
        callMC.setDate(6, fechavenc);
        callMC.setString(7,origen);
        callMC.execute();

        java.sql.Date VFechaEmision = callMC.getDate(1);
        String rsMC = "";

        if (!origen.equals("C")&&!origen.equals("N"))
        {
            rsMC = "215";
        }
        else
        {
            rsMC = DateExt.format("DD-MM-AAAA",VFechaEmision);

            if (rsMC.equals("00-00-0000"))
            {
                rsMC = "0";
            }
        }

        callMC.close();

        return rsMC;

        } catch (SQLException eSQL) {
        throw new RemoteException("ConfigurarPropuesta.CondonacionDeudaFiscal" + eSQL.toString());
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
    public int validaOtorgamiento(String tipoDeuda, long rut) throws RemoteException
    {
       int retorno=-1;


       try {


        Connection conn = this.getConnection();

        CallableStatement callMC = conn.prepareCall("{? = call convenio_especial.valida_otorgamiento(?,?)}");


        callMC.registerOutParameter(1,java.sql.Types.INTEGER);

        callMC.setString(2, tipoDeuda);
        callMC.setLong(3, rut);
        callMC.execute();

        retorno = callMC.getInt(1);
        callMC.close();
        this.numOtorgamiento=retorno;

        } catch (SQLException eSQL) {
        throw new RemoteException("convenio_especial.valida_otorgamiento:" + eSQL.toString());
      }
      finally{
        this.closeConnection();
        return retorno;
      }



    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public boolean validaMontoMinimo(int otorgamiento,long montoOtorgado, long montoTotal)
    {
      boolean retorno=false;
      long fraccionMinima=0;

      if (otorgamiento==1)
      {
        fraccionMinima=Math.round(montoTotal/3);
      }

      if (otorgamiento==2)
      {
        fraccionMinima=Math.round(montoTotal/2);
      }

      if (otorgamiento==3)
      {
        fraccionMinima=montoTotal;
      }

      if (montoOtorgado < fraccionMinima)
      {
        retorno=false;
      }
      else
      {
        retorno=true;
      }
      return retorno;
    }

	 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public long recuperaUltCodProp(String tipoDeuda, long rut) throws RemoteException
   {
      long retorno=-1;


      try {


       Connection conn = this.getConnection();

       CallableStatement callMC = conn.prepareCall("{? = call convenio_especial.recupera_ult_cod_prop(?,?)}");


       callMC.registerOutParameter(1,java.sql.Types.INTEGER);

       callMC.setString(2, tipoDeuda);
       callMC.setLong(3, rut);
       callMC.execute();

       retorno = callMC.getLong(1);
       callMC.close();


       } catch (SQLException eSQL) {
       throw new RemoteException("convenio_especial.recupera_ult_cod_prop:" + eSQL.toString());
     }
     finally{
       this.closeConnection();
       return retorno;
     }



   }
    
    public DeudaWeb liquidaSatelite(DeudaWeb deuda) throws Exception{
    	//System.out.println("ingreso servicio");
    	//System.out.println("<-----------------------------satelites0--------------------------------->");
        String  vencimiento = DateExt.format("AAAAMMDD",deuda.getFechaVencimiento());
        String  periodo = DateExt.format("AAAAMM",deuda.getFechaVencimiento());
       
     try{	
        Context ctx = new InitialContext();
        Object home = ctx.lookup("cobranzas.corporativo.servicioLiquidacion.ServicioLiquidacion");
        ServicioLiquidacionHome liquidadorHome = (ServicioLiquidacionHome) PortableRemoteObject.narrow(home,ServicioLiquidacionHome.class);
        ServicioLiquidacion liquida = liquidadorHome.create();
        
        //nuevo servicio que integra deudas satelite liquida CBS y CCC 09102013
        
        //aqui cambio tipo contribuyente para los cfu 4-->1, por lo tanto es tipo 1 constante
         liquida.liquidaConsultaDeudasAix(//(BigDecimal)new BigDecimal(deuda.getTipoContribuyente()),
        		                         (BigDecimal)new BigDecimal(1),
                                         (BigDecimal)new BigDecimal(deuda.getRutRol()),
                                         (BigDecimal)new BigDecimal(deuda.getTipoFormulario()),
                                         (BigDecimal)new BigDecimal(deuda.getFolio()),
                                         vencimiento,
                                         periodo);
        
       // System.out.println("ingreso liquido");
       // System.out.println("antes liquido que paso--->"+liquida.getValorRet()); 
        if ( Integer.parseInt(liquida.getValorRet()) == 0){
        	//System.out.println("ingreso liquido que paso--->"+liquida.getValorRet()); 
        	
        	deuda.setReajustes((liquida.getValorCod92()==null)?0:StringExt.stringALong(liquida.getValorCod92()).longValue());
        	deuda.setIntereses((liquida.getValorCod93()==null)?0:StringExt.stringALong(liquida.getValorCod93()).longValue());
        	//deuda.setMultas((liquida.getValorCod94()==null)?0:StringExt.stringALong(liquida.getValorCod94()).longValue()); este sobr
        	//los satelites no tienen multas, por lo tanto, vamos a setear en 0  05/08/2014 CCC, MP
        	deuda.setMultas(0);
        	deuda.setMonto((liquida.getValorCod91()==null)?0:StringExt.stringALong(liquida.getValorCod91()).longValue());
            deuda.setCod92Liq((liquida.getValorCod92()==null)?new Long(0):StringExt.stringALong(liquida.getValorCod92()));
           // System.out.println("0-------------> ");
           // System.out.println("antiguedad-------------> "+liquida.getFechaAntiguedad().getTime().getTime());
            deuda.setFechaAntiguedad(new java.sql.Date(liquida.getFechaAntiguedad().getTime().getTime()));
            /****************************SE FUERZA FECHA ANTIGUEDAD PARA CASOS ESPECIALES****/
            if (this.obtieneFechaAntiguedadSatFor(new Long(deuda.getTipoFormulario()))!=null){
            	deuda.setFechaAntiguedad(this.obtieneFechaAntiguedadSatFor(new Long(deuda.getTipoFormulario())));
            }
            /******************************************************************************/
            //deuda.setFechaAntiguedad(this.obtieneFechaAntiguedadSatFor(new Long(deuda.getTipoFormulario())));

           // System.out.println("1-------------> ");
            liquida.getFormVersion();
            
            String retorno=this.obtieneConvCondSatelite(new Long(deuda.getTipoFormulario()), liquida.getFormVersion());
            //System.out.println("1.2-------------> ");
            String[] parte = retorno.split(";");
            String derechoConvenio = parte[0]; 
            String derechoCondonacion = parte[1]; 
            
            //System.out.println("<-----------------------------satelites--------------------------------->");
            //System.out.println("derechoConvenio-------------> "+derechoConvenio);
            //System.out.println("derechoCondonacion-------------> "+derechoCondonacion);
            //System.out.println("getFormVersion-------------> "+liquida.getFormVersion());
            //System.out.println("getFechaAntiguedad-------------> "+new java.sql.Date(liquida.getFechaAntiguedad().getTime().getTime()));
            //System.out.println("<----------------------------------------------------------------------->");
            
            deuda.setDerechoCondonacion(derechoCondonacion);
            deuda.setDerechoConvenio(derechoConvenio);
            
            
            //System.out.println("ingreso obtengo----->");
        }else{
        	//System.out.println("2-------------> ");
        	
        	//no es liquidable
        	//System.out.println("no es liquidable---->");
        	deuda.setCod92Liq(new Long(-1));
            deuda.setPosibilidadCondonacion("N");
            deuda.setPosibilidadConvenio("N");
            deuda.setDerechoCondonacion("N");
            deuda.setDerechoConvenio("N");
            deuda.deshabilitar();
        	//throw new RemoteException("33333333333");
        	//throw new EJBException("33333333333");
        	//return deuda;
        }
		}catch(SQLException e) {
        	deuda.setCod92Liq(new Long(-1));
            deuda.setPosibilidadCondonacion("N");
            deuda.setPosibilidadConvenio("N");
            deuda.setDerechoCondonacion("N");
            deuda.setDerechoConvenio("N");
            deuda.deshabilitar();
            e.printStackTrace();
        }catch (EJBException e) {
    	//	System.out.println("ejb exxxxxx----------------<");
        	deuda.setCod92Liq(new Long(-1));
            deuda.setPosibilidadCondonacion("N");
            deuda.setPosibilidadConvenio("N");
            deuda.setDerechoCondonacion("N");
            deuda.setDerechoConvenio("N");
            deuda.deshabilitar();
    		e.printStackTrace();
    		//System.out.println("lo mismo--r-->");
    	//return deuda;
        }	catch (RemoteException e) {
        	deuda.setCod92Liq(new Long(-1));
            deuda.setPosibilidadCondonacion("N");
            deuda.setPosibilidadConvenio("N");
            deuda.setDerechoCondonacion("N");
            deuda.setDerechoConvenio("N");
            deuda.deshabilitar();
			
			 //throw new EJBException("Error en servicio Liquidadoe VMS");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
        	deuda.setCod92Liq(new Long(-1));
            deuda.setPosibilidadCondonacion("N");
            deuda.setPosibilidadConvenio("N");
            deuda.setDerechoCondonacion("N");
            deuda.setDerechoConvenio("N");
            deuda.deshabilitar();
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}	
		
		return deuda;
       // System.out.print("llego------>");
      // return deuda;
    }    
    
    //servicio obtiene parametros
    public String getObtieneParametrosGrilla()throws Exception {
        Connection conn = null;
        CallableStatement  call = null;
          String result = new String();

        try {

          conn = this.getConnection();

          //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
          call = conn.prepareCall("{? = call PKG_OBTIENE_PARAM.obtieneParametroGrilla}");
          call.registerOutParameter(1, OracleTypes.VARCHAR);
          call.execute();

          result = call.getObject(1).toString();
     
          call.close(); 
          
        }catch (Exception e) {
           e.printStackTrace();
        }
        finally{
          this.closeConnection();
        }
		return result;
    }  
	
    //servicio obtiene parametros Reforma Tributaria
    /**
     * Servicio que obtiene parametro de reforma tributaria
     */
    public String getObtieneParametrosReformaTribu()throws Exception {
        Connection conn = null;
        CallableStatement  call = null;
          String result = new String();

        try {

          conn = this.getConnection();

          //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
          call = conn.prepareCall("{? =call PKG_OBTIENE_PARAM.obtieneParametroReformaTribu}");
          call.registerOutParameter(1, OracleTypes.VARCHAR);
          call.execute();

          result = call.getObject(1).toString();
     
          call.close(); 
          
        }catch (Exception e) {
           e.printStackTrace();
        }
        finally{
          this.closeConnection();
        }
		return result;
    }
    
    
    //servicio obtiene parametros Reforma Tributaria para politica de convenios (comportamiento-garantia-tipoPago)
    /**
     * Servicio obtiene parametros Reforma Tributaria para politica de convenios, es decir, comportamiento y garantia segun un contribuyente
     */
    public void getFactorPoliticaConv(ReformaTributariaInput reformaTributaria)throws Exception {
        Connection conn = null;
        CallableStatement  call = null;
         
        try {

          conn = this.getConnection();
          //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
          call = conn.prepareCall("{call PKG_REFORMA_TRIBUTARIA.Status_Contribuyente(?,?,?,?)}");
          call.setLong(1,reformaTributaria.getRutContribuyente().intValue());/*Rut Contribuyente */
          call.registerOutParameter(2, OracleTypes.INTEGER);/*Comportamiento*/
          call.registerOutParameter(3, OracleTypes.INTEGER);/*Embargo*/
          call.registerOutParameter(4, OracleTypes.INTEGER);/*Mipe*/
           
          
          call.execute();


          reformaTributaria.setComportamientoConvenio(new Integer(call.getObject(2).toString()));
          int garantia = Integer.parseInt(call.getObject(3).toString());
          int pyme = Integer.parseInt(call.getObject(4).toString());
          
          //System.out.println("-------garantia-------- "+garantia);
          //System.out.println("-------pyme-------- "+pyme);
          
          
          //reformaTributaria.setTieneGarantia(tieneGarantia)(new Integer(call.getObject(3).toString()));
          //reformaTributaria.setBeneficioPyme(new Integer(call.getObject(4).toString()));
          if (garantia==1){
        	  reformaTributaria.setTieneGarantia(new Boolean(true));
          }else{
        	  reformaTributaria.setTieneGarantia(new Boolean(false));
          }

          if (pyme==1){
        	  reformaTributaria.setBeneficioPyme(new Boolean(true));
          }else{
        	  reformaTributaria.setBeneficioPyme(new Boolean(false));
          }
          
          
          call.close(); 
          
        }catch (Exception e) {
           e.printStackTrace();
        }
        finally{
          this.closeConnection();
        }
		
    } 
    
   
    
    //servicio obtiene valores Comportamiento o Condonacion Convenios Reforma Tributaria
    /**
     * Servicio obtiene valores de condiciones de Convenios Reforma Tributaria, es decir, Maxima cantidad de cuotas y minimo porcentaje de cuota contado
     */
    public ReformaTributariaOutput getComporConv(boolean comportamiento, ReformaTributariaInput reformaTributaria)throws Exception {
        Connection conn = null;
        CallableStatement  call = null;
        int inComportamiento=0; 
        int inTipoConvenio=0;
        int inEmbargo=0;
        int inPyme=0;
        String ContextoAmbienteParam = null;
        ReformaTributariaOutput reformaTributariaOut =  new ReformaTributariaOutput();
        try {

          conn = this.getConnection();
        String beneficio = this.getBeneficio();
          
        System.out.println("Estoy adentro con parametro beneficio---------------------->"+beneficio);
         
          if (beneficio.equalsIgnoreCase("S")){
	          if (reformaTributaria.getContextoAmbienteParam().endsWith("CNV_INTRA")){
	        	  ContextoAmbienteParam="CNV_INTRA";
	          }else{
	        	  ContextoAmbienteParam="CNV_BENEFICIO_INTER";
	          }
          }else{
        	  ContextoAmbienteParam=reformaTributaria.getContextoAmbienteParam(); 
          }
          //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
          /*
          --------------------------------------------------------------
          -------   Las variables determinantes     --------------------
          -------   para condiciones de convenios   --------------------
          --------------------------------------------------------------
          -------   v_tipo_valor
          -------   v_tipo_convenio
          -------   v_comportamiento
          -------   v_embargo
          -------   v_canal
          --------------------------------------------------------------
          --------------------------------------------------------------          
          */
          if (comportamiento){
        	  inComportamiento=1;
          }else{
        	  inComportamiento=2;
          }
          
          //String parametros=reformaTributaria.pagoTotalConvenio()+";"+reformaTributaria.comportamiento()+";"+reformaTributaria.garantia()+";"+reformaTributaria.canal();
          String parametros=reformaTributaria.pagoTotalConvenio()+";"+reformaTributaria.comportamiento()+";"+reformaTributaria.garantia();
          
          //System.out.println("tipo reforma condonacion--->"+reformaTributaria.getCodConvenios());
          //System.out.println("***************************estoy en comportamiento****************************************");
          //System.out.println("reformaTributaria.garantia()--->"+reformaTributaria.garantia());
          //System.out.println("param getComporConv---> "+parametros);
          //call = conn.prepareCall("{call PKG_REFORMA_TRIBUTARIA.consulta_tabla_valores_conv(?,?,?,?,?,?)}");
          call = conn.prepareCall("{call PKG_REFORMA_TRIBUTARIA.consulta_tabla_valores_conv(?,?,?,?,?,?,?,?)}");
          call.setLong(1,inComportamiento);/*tipo comporamiento � condonaci�n */
          call.setString(2,parametros);/*tipo convenio */
          call.registerOutParameter(3, OracleTypes.INTEGER);/*max_cuotas*/
          call.registerOutParameter(4, OracleTypes.INTEGER);/*min_pie*/
          call.registerOutParameter(5, OracleTypes.INTEGER);/*por_condonacion*/   
          call.registerOutParameter(6, OracleTypes.INTEGER);/*error*/  
          /*se agrega nueva forma de servicio*/
          if (reformaTributaria.getCodConvenios()!=null){//si no es nulo quiere decir que es transitoria
        	  call.setLong(7,reformaTributaria.getCodConvenios().intValue());/*codigo condonacion si es transitoria*/
          }else{//si es null quiere decir que es convenio permanente
        	  call.setNull(7,  OracleTypes.INTEGER);
          }
          //call.setString(8,reformaTributaria.getContextoAmbienteParam());/*ambiente Intranet � internet*/  
          System.out.println("ContextoAmbienteParam---> "+ContextoAmbienteParam);
          call.setString(8,ContextoAmbienteParam);/*ambiente Intranet � internet*/
          call.execute();
          
          int maxCuotas = Integer.parseInt(call.getObject(3).toString());
          int minCuotaContado = Integer.parseInt(call.getObject(4).toString());
    

          int codError = Integer.parseInt(call.getObject(6).toString());
          
          reformaTributariaOut.setCodError(new Integer(codError));
          reformaTributariaOut.setMaxCuota(new Integer(maxCuotas));
          reformaTributariaOut.setMinCuotaContado(new Integer(minCuotaContado));
          call.close(); 
          
        }catch (Exception e) {
        	e.printStackTrace();
        	//throw new RemoteException("No tiene valor de comportamiento convenios:" + e.getMessage());
        }
        finally{
          this.closeConnection();
        }
		return reformaTributariaOut;
    }         
    
    //servicio obtiene valores Comportamiento o Condonacion Convenios Reforma Tributaria
    /**
     * Servicio encargado de porcentaje de condonaci�n para cada deuda seleccionad segun la atiguedad y configuraci�n del arbol
     */
    public void getCondonacionConv(ReformaTributariaInput reformaTributaria)throws Exception {
        Connection conn = null;
        CallableStatement  call = null;
        CallableStatement  call2 = null;
        String ContextoAmbienteParam = null;
        try {

          conn = this.getConnection();
        //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
          /*
	      --------------------------------------------------------------
	      -------   Las variables determinantes     --------------------
	      -------   para condiciones de condonacion --------------------
	      --------------------------------------------------------------
	      -------   v_tipo_valor
	      -------   v_tipo_convenio
	      -------   v_empresa_mype
	      -------   v_antiguedad
	      -------   v_canal
	      --------------------------------------------------------------
	      --------------------------------------------------------------        
          */
          //System.out.println("tipo reforma condonacion--->"+reformaTributaria.getCodConvenios());
          //System.out.println("tipo reforma pago convenio--->"+reformaTributaria.getPagoContado().booleanValue());
          /*realizo evaluacion si es pago condonacion � pago contado*/
         String beneficio = this.getBeneficio();
          
          System.out.println("Estoy adentro con parametro beneficio getCondonacionConv---------------------->"+beneficio);
          //para cambios de periodo
          java.sql.Date fechaSQL=null;           
            if (beneficio.equalsIgnoreCase("S")){
  	         if (reformaTributaria.getContextoAmbienteParam().endsWith("CNV_INTRA")){
  	        	  ContextoAmbienteParam="CNV_INTRA";
  	            /***************************truco de fecha*************************************/
              	//System.out.println("estoy el logica");
  	            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
  	            String dateS = "01012019";
  	            Date date = formatter.parse(dateS);
  	            fechaSQL = new java.sql.Date(date.getTime());
               /******************************************************************************/            
            }else{
  	        	  ContextoAmbienteParam="CNV_BENEFICIO_INTER";
  	          }
            }else{
          	  ContextoAmbienteParam=reformaTributaria.getContextoAmbienteParam(); 
            }
          int tipoValor=0;
          if (reformaTributaria.getPagoContado().booleanValue()){
        	  tipoValor=3;
          }else{
        	  tipoValor=2;
          }
          
          
          
          /*evaluo si es condonacion transitoria o permanente*/
          String tipoConvenioT="PE";
          if (reformaTributaria.getCodConvenios()!=null){
        	  tipoConvenioT="TR";
          }else{
        	  tipoConvenioT="PE";
          }
          

          //primero obtengo el valor antiguedad//
          //System.out.println("cantidad de deudas a calcular--> "+this.deudasContribuyenteSeleccionadas.size());
          Iterator it = this.deudasContribuyenteSeleccionadas.iterator();
          int obtenMaximo=0;
          int cont=0;
                   while (it.hasNext()){
                	   cont++;
		                DeudaWeb deuda = (DeudaWeb) it.next();
                        
		                //System.out.println("fecha de la deuda ANTIGUEDAD--> "+deuda.getFechaAntiguedad());
		                //System.out.println("derecho a condonacion de la deuda--> "+deuda.getPosibilidadCondonacion());
		                //System.out.println("condonacion aplicada--> "+deuda.getCondonacionAplicadaA());
		                //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
		                //truco de fecha cambios de periodo
		                if (fechaSQL!=null){
		                	//System.out.println("estoy dentro");
		                	call = conn.prepareCall("{? = call PKG_REFORMA_TRIBUTARIA.Antiguedad(?,?,?)}");
		                	call.setDate(4,fechaSQL);/*Fecha_forzada */
		                }else{
		                	call = conn.prepareCall("{? = call PKG_REFORMA_TRIBUTARIA.Antiguedad(?,?)}");
		                }
		                call.registerOutParameter(1, OracleTypes.VARCHAR);
		                call.setDate(2,deuda.getFechaAntiguedad());/*Fecha_emision */
		                call.setString(3,tipoConvenioT);/*Fecha_emision */
		                call.execute();

		                String  antiguedad = call.getObject(1).toString();
		                //System.out.println("CODIGO antiguedad de la deuda--> "+antiguedad);
		                
		                
                   
                        //ahora debo obtner porcentaje de condonacion segun parametros
		                
		                reformaTributaria.setAntiguedadDeuda(new Integer(antiguedad));
		                
		                /***
		                 * ojo sacar esto estoy falseanfo codigo 
		                 */
		                
		                if (cont>3){
		                	reformaTributaria.setBeneficioPyme(new Boolean(false));
		                }else{
		                	reformaTributaria.setBeneficioPyme(new Boolean(true));
		                }
		                
		                //String parametros=reformaTributaria.pagoTotalConvenio()+";"+reformaTributaria.pyme()+";"+reformaTributaria.antiguedad()+";"+reformaTributaria.canal();
		                //String parametros=reformaTributaria.pagoTotalConvenio()+";"+reformaTributaria.antiguedad()+";"+reformaTributaria.canal();
		                String parametros=reformaTributaria.pagoTotalConvenio()+";"+reformaTributaria.antiguedad();
		                if (reformaTributaria.getPagoContado().booleanValue()){
		                	 parametros=reformaTributaria.antiguedad();
		                }else{
		                	 parametros=reformaTributaria.pagoTotalConvenio()+";"+reformaTributaria.antiguedad();
		                }
		                //System.out.println("param getComporConv---> "+parametros);
		                
		                //call2 = conn.prepareCall("{call PKG_REFORMA_TRIBUTARIA.consulta_tabla_valores_conv(?,?,?,?,?,?)}");
		                call2 = conn.prepareCall("{call PKG_REFORMA_TRIBUTARIA.consulta_tabla_valores_conv(?,?,?,?,?,?,?,?)}");
		                call2.setLong(1,tipoValor);/*tipo condonaci�n convenio=2, tipo condonaci�n pgo contado=3 */
		                call2.setString(2,parametros);/*tipo convenio */
		                call2.registerOutParameter(3, OracleTypes.INTEGER);/*max_cuotas*/
		                call2.registerOutParameter(4, OracleTypes.INTEGER);/*min_pie*/
		                call2.registerOutParameter(5, OracleTypes.INTEGER);/*por_condonacion*/   
		                call2.registerOutParameter(6, OracleTypes.INTEGER);/*error*/ 
		                /*se agrega nueva forma de servicio*/
		                if (reformaTributaria.getCodConvenios()!=null){//si no es nulo quiere decir que es transitoria
		                	call2.setLong(7,reformaTributaria.getCodConvenios().intValue());/*codigo condonacion si es transitoria*/
		                }else{//si es null quiere decir que es convenio permanente
		                	call2.setNull(7,  OracleTypes.INTEGER);
		                }
		                //call2.setString(8,reformaTributaria.getContextoAmbienteParam());/*ambiente Intranet � internet*/  
		                call2.setString(8,ContextoAmbienteParam);/*ambiente Intranet � internet*/

		                call2.execute();
		                int maxPorcentajeCond =0;
		                if (call2.getObject(5)!=null){
		                	maxPorcentajeCond = Integer.parseInt(call2.getObject(5).toString());
		                	
		                	/*if (obtenMaximo<maxPorcentajeCond){
		                		obtenMaximo=maxPorcentajeCond;
		                	}*/
		                	
		                }else{
		                	maxPorcentajeCond=0;	
		                }
		                
		                
		                //System.out.println("getDerechoCondonacion------------>"+deuda.getDerechoCondonacion());
		                //System.out.println("getCondonacionAplicadaA------------>"+deuda.getCondonacionAplicadaA());
		                if (deuda.getDerechoCondonacion().equalsIgnoreCase("S")){
		                	/***OJO CUANDO TIENE DERECHO A CONDONACI�N SE LE APLICA A AMBAS YA NO EXISTE ***/
		                	/**se acordo este nuevo modelo no importando la posible diferencia en la proyeccion y cuota de ajuste 01/09/2015**/
		                	deuda.setCondonacionAplicadaA("A");
		                	//if (deuda.getDerechoCondonacion().equalsIgnoreCase("S")&&!deuda.getCondonacionAplicadaA().equalsIgnoreCase("N")){		                	
			                deuda.setPorcentajeMaximoCondonacionIntereses(maxPorcentajeCond);
			                deuda.setPorcentajeMaximoCondonacionMultas(maxPorcentajeCond);	
			                
			                //aqui determino maximo porcentaje de condonaci�n, solo para deudas con ese derecho
			                if (obtenMaximo<maxPorcentajeCond){
		                		obtenMaximo=maxPorcentajeCond;
		                	}
			                
		                	/*
		                	if (deuda.getCondonacionAplicadaA().equalsIgnoreCase("A")){
			                	deuda.setPorcentajeMaximoCondonacionIntereses(maxPorcentajeCond);
				                deuda.setPorcentajeMaximoCondonacionMultas(maxPorcentajeCond);
			                }else if(deuda.getCondonacionAplicadaA().equalsIgnoreCase("I")){
			                	deuda.setPorcentajeMaximoCondonacionIntereses(maxPorcentajeCond);
				                deuda.setPorcentajeMaximoCondonacionMultas(0);			                	
			                }else{
			                	deuda.setPorcentajeMaximoCondonacionIntereses(0);
				                deuda.setPorcentajeMaximoCondonacionMultas(maxPorcentajeCond);				                	
			                }*/

		                }else{
		                	deuda.setCondonacionAplicadaA("N");
			                deuda.setPorcentajeMaximoCondonacionIntereses(0);
			                deuda.setPorcentajeMaximoCondonacionMultas(0);		                	
		                }
		                //System.out.println("porcentaje--> "+maxPorcentajeCond);
		                
		                
		                
                   }   
          
                   reformaTributariaOut.setMaxCondonacionI(new Integer(obtenMaximo));
                   reformaTributariaOut.setMaxCondonacionM(new Integer(obtenMaximo));
                   
       
          call.close(); 
          //System.out.println("cantidad de deudas a calcular final--> "+this.deudasContribuyenteSeleccionadas.size());
        }catch (Exception e) {
           e.printStackTrace();
        }
        finally{
          this.closeConnection();
        }
		
    }         
    
    /**
     * Servicio que obtiene grupo de deudas, segun su activaci�n dentro de linea de tiempo en transitoria
     * @param deudas
     * @return
     */
    public Collection obtieneGrupoDeuda(Collection deudas){
        Connection conn = null;
        CallableStatement  call = null;
    	  Iterator itera = deudas.iterator();
    	  Collection newRetorno= new ArrayList();
          //System.out.println("vestoy dentro  grupos--->"+deudas.size());
         // int i=0;
         try{ 
          conn = this.getConnection();	 
          
          while(itera.hasNext()) {
        	  
              DeudaWeb elemento = (DeudaWeb) itera.next();
              
              //System.out.println("tipo formulario--->"+elemento.getTipoFormulario());
              //System.out.println("fecha antiguedad--->"+elemento.getFechaAntiguedad());

              call = conn.prepareCall("{call PKG_REFORMA_TRIBUTARIA.Get_Transitoria_CNV(?,?,?)}");
              
             
              call.setDate(1,elemento.getFechaAntiguedad());/*Fecha_emision */
              call.registerOutParameter(2, OracleTypes.INTEGER);/*cod_tipo_conv*/
              call.registerOutParameter(3, OracleTypes.VARCHAR);/*max_cuotas*/
              call.execute();
              
              if (call.getObject(2)!=null && call.getObject(3)!=null && elemento.getTipoFormulario()!=34){
                 int codTipoConv = Integer.parseInt(call.getObject(2).toString());
                  String nombreGrupo = call.getObject(3).toString();
            	  
                  if (elemento.getTipoFormulario()==30){
                	  nombreGrupo=nombreGrupo+" TERRITORIAL";
                  }else{
                	  nombreGrupo=nombreGrupo+" FISCAL";
                  }
                  elemento.setGrupo(nombreGrupo);
            	  elemento.setTipoConvenio(codTipoConv);
            	  reformaTributaria.setCodConvenios(new Integer(codTipoConv));

            	  //System.out.println("tiene transitoria-->"+elemento.getFolio()+"--"+nombreGrupo+"--"+codTipoConv+"--"+reformaTributaria.getCodConvenios().intValue());	  
              }
              newRetorno.add(elemento);
                
          }
              
          call.close(); 
         }catch (Exception e) {
             e.printStackTrace();
          }
		return newRetorno;
	 	
    }
    
    /**
     * Servicio encargado de registrar todas las variables utilizadas para crear un convenio, es un log para revisar cualquier caso anormal
     * @param codPropuesta
     * @param reformaIn
     * @param deudas
     * @param reformaOut
     * @throws Exception
     */
    public void registraPoliticasCond (Long codPropuesta, ReformaTributariaInput reformaIn, Collection deudas, ReformaTributariaOutput reformaOut)throws Exception{
    	
        Connection conn = null;
        CallableStatement  call = null;
         try{ 
          conn = this.getConnection();
          //codPropuesta=null;//prueba que pasa
          //reformaIn=null;
    	        int canal=0;
    	        int tipoConvenio=0;
    	        int garantia=0;
		    	Iterator itera = deudas.iterator();
		    	//System.out.println("<-------------------------------------Ingreso a registrar opciones--------------------------------------->");
		        while(itera.hasNext()) {
		      	
		        	DeudaWeb elemento = (DeudaWeb) itera.next();
		        	
		        	/*System.out.println("codPropuesta--->"+codPropuesta);
		        	System.out.println("getIsIntranet--->"+reformaIn.getIsIntranet());
		        	System.out.println("getPagoTotalConvenio--->"+reformaIn.getPagoTotalConvenio());
		        	System.out.println("getComportamientoConvenio--->"+reformaIn.getComportamientoConvenio());
		        	System.out.println("getRutContribuyente--->"+reformaIn.getRutContribuyente());
		        	System.out.println("getTieneGarantia--->"+reformaIn.getTieneGarantia());
		        	System.out.println("getTipoContribuyente--->"+elemento.getTipoContribuyente());
		        	System.out.println("getTipoFormulario--->"+elemento.getTipoFormulario());
		        	System.out.println("getRutRol--->"+elemento.getRutRol());
		        	System.out.println("getFolio--->"+elemento.getFolio());
		        	System.out.println("getFechaVencimiento--->"+elemento.getFechaVencimiento());
		        	System.out.println("getFechaAntiguedad--->"+elemento.getFechaAntiguedad());
		        	System.out.println("getPorcentajeCondonacionMultas--->"+elemento.getPorcentajeCondonacionMultas());
		        	System.out.println("getPorcentajeCondonacionIntereses--->"+elemento.getPorcentajeCondonacionIntereses());
		        	System.out.println("getBeneficioPp--->"+reformaOut.getBeneficioPp());
		        	System.out.println("getMinCuotaContado--->"+reformaOut.getMinCuotaContado());
		        	System.out.println("getMaxCuota--->"+reformaOut.getMaxCuota());*/
		        	
		        	call = conn.prepareCall("{call PKG_REFORMA_TRIBUTARIA.INSERTA_CONDICION_CNV(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		        	
		        	
		        	
		        	//obtengo  canal
		        	if (reformaIn.getIsIntranet().booleanValue()){
		        		canal=1;//presencial
		        	}else {
		        		canal=2;//internet
		        	}
		        	
		        	//obtengo tipo convenio
		        	if (reformaIn.getPagoTotalConvenio().booleanValue()){
		        		tipoConvenio=1;//total
		        	}else{
		        		tipoConvenio=2;//parcial
		        	}
		        	
		        	//obtengo garantia
		        	if (reformaIn.getTieneGarantia().booleanValue()){
		        		garantia=1;//con garatia
		        	}else{
		        		garantia=2;//sin garantia
		        	}
		        	
		        	
		        	call.setLong(1, codPropuesta.longValue());
		        	call.setLong(2, canal);//canal;
		        	call.setLong(3, tipoConvenio);//Tipo convenio Pago total-pago parcial
		        	call.setLong(4, reformaIn.getComportamientoConvenio().longValue());//comportamiento Bueno=1 / Regular=2 / Malo=3
		        	call.setLong(5, reformaIn.getRutContribuyente().longValue());//rut evaluado;
		        	call.setLong(6, garantia);//embargo  / o garantia
		        	call.setLong(7, elemento.getTipoContribuyente());//cliente_tipo;
		        	call.setLong(8, elemento.getTipoFormulario());//tipo_forulario;
		        	call.setLong(9, elemento.getRutRol());//rut_rol;
		        	call.setLong(10, elemento.getFolio());//form_folio;
		        	call.setDate(11,elemento.getFechaVencimiento());//fecha_vcto;
		        	call.setDate(12,elemento.getFechaAntiguedad());//fecha_antiguedad;
		        	call.setLong(13, elemento.getPorcentajeCondonacionMultas());//porcentaje condonacion multas
		        	call.setLong(14, elemento.getPorcentajeCondonacionIntereses());//porcentaje condonacion intereses
		        	call.setLong(15, reformaOut.getBeneficioPp().longValue());//beneficio pronto pago o incentivo al pago;
		        	call.setLong(16, reformaOut.getMinCuotaContado().longValue());//porcentaje minimo cuota contado
		        	call.setLong(17, reformaOut.getMaxCuota().longValue());//porcentaje maximo cuotas
		        	call.setNull(18, OracleTypes.VARCHAR);//error
		        	
		        	call.execute();
		        	
	        }
		        
		    call.close(); 
         }catch (Exception e) {
             e.printStackTrace();
          }finally{
              this.closeConnection();
          }		        
    	
    }
    
   /* public void getRolesLista(){
	 try{


		    Context ctxAgregarRol = new InitialContext(new ObtenerServicios().env);
		    Object homeAgregarRol = ctxAgregarRol.lookup("cobranzas.convenios.negocio.ejb.session.stateful.AgregarRol");
		    AgregarRolHome sessionAgregaRolHome = (AgregarRolHome) PortableRemoteObject.narrow(homeAgregarRol,AgregarRolHome.class);

		    AgregarRol sessionAgregaRol = sessionAgregaRolHome.create(); 
		    
		       HashMap parametros = new HashMap();
		       String Rol = VRol1 + VRol2 + VRol3;
		       parametros.put("ROL",new Long(Rol));
		       parametros.put("ESTADOCOBRANZA",(String) "T");

		       Collection x = sessionAgregaRol.getInfoRol(parametros);

		    
		    
     }catch (Exception e) {
         e.printStackTrace();
      }
      finally{
        this.closeConnection();
      }
	 }*/
    
    /**
     * Servicio encargado de obtener derecho a convenios y condonaci�n para satelites
     */
    public String obtieneConvCondSatelite(Long tipoForm, String versionForm){
        Connection conn = null;
        CallableStatement  call = null;
        String retorno = null;
       //System.out.println("--------->obtieneConvCondSatelite");
        
         try{ 
          conn = this.getConnection();	 
          
       
              call = conn.prepareCall("{call DEUDASCONTRIBUYENTE.Form_Conv_Cond(?,?,?,?,?)}");
              
              call.setLong(1, tipoForm.longValue());/*tipo de formulario */
              call.setString(2,versionForm);/*version del formulario */
              call.registerOutParameter(3, OracleTypes.VARCHAR);/*max_cuotas*/
              call.registerOutParameter(4, OracleTypes.VARCHAR);/*max_cuotas*/
              call.registerOutParameter(5, OracleTypes.INTEGER);/*cod_tipo_conv*/

              call.execute();
              
              String derechoConvenio=call.getObject(3).toString();
              String derechoCondonacion=call.getObject(4).toString();
              
              //System.out.println("derecho condonacion---sat---->"+derechoCondonacion);
              //System.out.println("derecho convenios---sat---->"+derechoConvenio);
              
              retorno=derechoConvenio+";"+derechoCondonacion;
              
          call.close(); 
         }catch (Exception e) {
             e.printStackTrace();
          }finally{
              this.closeConnection();
          }
		return retorno;
	 	
    }    
    //SERVICIO que fuerza fecha_antiguedad solo para satelites
    /**
     * SERVICIO que fuerza fecha_antiguedad solo para satelites, esto para casos raros del 100% condonaci�n como por ejemplo F36 
     */
    public java.sql.Date obtieneFechaAntiguedadSatFor(Long tipoForm){
        Connection conn = null;
        CallableStatement  call = null;
        java.sql.Date retorno = null;
         //System.out.println("--------->fecha satelite forzado");
        
         try{ 
          conn = this.getConnection();	 
               call = conn.prepareCall("{call PKG_REFORMA_TRIBUTARIA.GET_FECHA_ANTIGUEDAD_AUX(?,?)}");
              
              call.setLong(1, tipoForm.longValue());/*tipo de formulario */
              call.registerOutParameter(2, java.sql.Types.DATE);/*fecha antiguedad FORZADA*/
              call.execute();
              
        
              //System.out.println("FECHA FORZADA---->"+call.getObject(2));
              retorno=(java.sql.Date) call.getObject(2);
              
              
          call.close(); 
         }catch (Exception e) {
             e.printStackTrace();
          }finally{
              this.closeConnection();
          }
		return retorno;
	 	
    }
    
    /**
     * Servicio que obtiene grupo de deudas, segun su activaci�n dentro de linea de tiempo en transitoria
     * @param deudas
     * @return
     */
    public Collection obtieneExclusionesDeuda(int perfil,Collection deudas){
        Connection conn = null;
        CallableStatement  call = null;
    	  Iterator itera = deudas.iterator();
    	  Collection newRetorno= new ArrayList();
    	  List detalleDeuda = null;
    	  ResultSet rs;
          //System.out.println("vestoy dentro  obtieneExclusionesDeuda deudas:--->"+deudas.size());
          
          
         // int i=0;
         try{ 
          conn = this.getConnection();	 
          
          while(itera.hasNext()) {
        	  DeudaExclusion deudaExclusion = new DeudaExclusion();
              DeudaWeb elemento = (DeudaWeb) itera.next();
              
             /* System.out.println("perfil:---> "+perfil);
              System.out.println("elemento.getTipoContribuyente():---> "+elemento.getTipoContribuyente());
              System.out.println(" elemento.getRutRol():---> "+ elemento.getRutRol());
              System.out.println("getTipoFormulario:---> "+elemento.getTipoFormulario());
              System.out.println("elemento.getFolio():---> "+elemento.getFolio());
              System.out.println("elemento.getFechaAntiguedad():---> "+elemento.getFechaAntiguedad());
              System.out.println("elemento.fecha_vencimiento:---> "+elemento.getFechaVencimiento()); */
              
              call = conn.prepareCall("{call PKG_EXCLUSIONES_NEW.EXCLUSIONES_CONVENIOS(?,?,?,?,?,?,?,?,?,?)}");
              call.setLong(1, perfil);/*perfil*/
              call.setLong(2, elemento.getTipoContribuyente());/*tipo de contribuyente */
              call.setLong(3, elemento.getRutRol());/*rut rol */
              call.setLong(4, elemento.getTipoFormulario());/*tipo de formulario */
              call.setLong(5, elemento.getFolio());/*folio */
              call.setDate(6, elemento.getFechaVencimiento());/*Fecha vencimiento */
              call.registerOutParameter(7, OracleTypes.VARCHAR);/*derecho convenio*/
              call.registerOutParameter(8, OracleTypes.VARCHAR);/*derecho condonacion*/
              call.registerOutParameter(9, OracleTypes.VARCHAR);/*efecto*/
              call.registerOutParameter(10, oracle.jdbc.OracleTypes.CURSOR);/* CURSOR EXCLUSIONES*/

              call.execute();
              
              deudaExclusion.setDerechoConvenio(call.getObject(7).toString());
              deudaExclusion.setDerechoCondonacion(call.getObject(8).toString());
              /*System.out.println("efectyo de exclusion-------------------> "+call.getObject(9).toString());
              System.out.println("setDerechoConvenio-------------------> "+call.getObject(7).toString());
              System.out.println("setDerechoCondonacion-------------------> "+call.getObject(8).toString());
              System.out.println("getDerechoConvenio-------------------> "+deudaExclusion.getDerechoConvenio());
              System.out.println("getDerechoCondonacion-------------------> "+deudaExclusion.getDerechoCondonacion());*/
              /************************SETEO VALORES REALES DE EXCLUSIONES**************************************************/
           
              //System.out.println("getPosibilidadCondonacion  --->"+elemento.getPosibilidadCondonacion());
              //System.out.println("getDerechoCondonacion  --->"+elemento.getDerechoCondonacion());
              
              if (elemento.getDerechoCondonacion().equalsIgnoreCase("S")){//solo se pisa esta informacion si es S
            	  elemento.setPosibilidadCondonacion(deudaExclusion.getDerechoCondonacion());
            	  elemento.setDerechoCondonacion(deudaExclusion.getDerechoCondonacion());
              }
              
              //System.out.println("getPosibilidadConvenio  --->"+elemento.getPosibilidadConvenio());
              //System.out.println("getDerechoConvenio  --->"+elemento.getDerechoConvenio());    
              
              if (elemento.getDerechoConvenio().equalsIgnoreCase("S")){
            	  elemento.setPosibilidadConvenio(deudaExclusion.getDerechoConvenio());
            	  elemento.setDerechoConvenio(deudaExclusion.getDerechoConvenio());
              }
              
              //System.out.println("exclusion condonacion  --->"+deudaExclusion.getDerechoCondonacion());
              //System.out.println("exclusion convenios  --->"+deudaExclusion.getDerechoConvenio());              
              
              
              /***********ahora debo pasar al cursor de exclusiones *******************/
              rs = (ResultSet) call.getObject(10);
              
              detalleDeuda = new ArrayList();
              while (rs.next()) {
            	   //System.out.println("mira el cursor-----> "+rs.getString(2));
            	   //System.out.println("mira el cursor-----> "+rs.getString(1));
            	   DetalleDeudaExclusion detalleDeudaExclusion = new DetalleDeudaExclusion();
            	  
            	   detalleDeudaExclusion.setTipoExclusion(rs.getString(1));
            	   detalleDeudaExclusion.setDescripcionExclusion(rs.getString(2));
            	   detalleDeudaExclusion.setEfectoConvenio(rs.getString(3));
            	   detalleDeudaExclusion.setEfectoCondonacion(rs.getString(4));
            	   detalleDeuda.add(detalleDeudaExclusion);
              }
              
              deudaExclusion.setDetalleDeudaExclusion(detalleDeuda);//lista de exclusiones por deuda
              elemento.setDeudaExclusion(deudaExclusion);//se pasa objeto exclusiones a clase deuda
              newRetorno.add(elemento);//devuelvo nueva liste de dudas completa con exclusiones  
          }
              
          call.close(); 
         }catch (Exception e) {
             e.printStackTrace();
          }
		return newRetorno;
	 	
    }   
	
public String getBeneficio()throws Exception {
	    Connection conn = null;
        CallableStatement  call = null;
        String beneficio = null;
         try{ 
          conn = this.getConnection();
	      	Date fecha = new Date();
	    	java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());

          System.out.println("mira parametro beneficio---------------------------------------------->");
          //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
			 call = conn.prepareCall("{? = call FCN_CNV_BNF_ACT(?)}");
			 call.setDate(2, fechaSQL);
			 call.registerOutParameter(1, OracleTypes.VARCHAR);
			 call.execute();
			 
			 System.out.println("mira parametro beneficio---------------------------------------------->"+call.getObject(1).toString());
			 //result = call.getObject(1).toString();
			  beneficio = call.getObject(1).toString();
		
			  call.close(); 
			 
	    }catch (Exception e) {
	        e.printStackTrace();
	     }
		return beneficio;
  }  
public String getBeneficioCovid19(long rut)throws Exception {
    Connection conn = null;
    CallableStatement  callCov = null;
    String beneficioCovid = null;
     try{ 
      conn = this.getConnection();
             System.out.println("mira parametro beneficio  covid19---------------------------------------------->");
      //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
		 callCov = conn.prepareCall("{? = call PKG_COVID19.AnalizaRut(?)}");
		 callCov.setLong(2, rut);/*tipo de formulario */
		 callCov.registerOutParameter(1, OracleTypes.VARCHAR);
		 callCov.execute();
		 
		 System.out.println("mira parametro beneficio covid19---->"+callCov.getObject(1).toString());
		 //result = call.getObject(1).toString();
		 beneficioCovid = callCov.getObject(1).toString();
	
		 callCov.close(); 
		  
		  if (beneficioCovid.equalsIgnoreCase("S")){
			  
			  Iterator it = this.deudasContribuyente.iterator();// this.deudasContribuyenteSeleccionadas.iterator();
	          int cont=0;
	          //String beneficioFormulario = "N";
	                   while (it.hasNext()){
	                	    DeudaWeb deuda = (DeudaWeb) it.next();
	                        System.out.println("tipo formilario------------>"+deuda.getTipoFormulario());
	                        		switch (deuda.getTipoFormulario()) 
	                                {
	                                  //multiples cases sin declaraciones break
	                                 
	                                    case 9:  
	                                    case 14:
	                                    case 15:
	                                    case 16:
	                                    case 17:
	                                    case 18:
	                                    case 19:
	                                    case 21:
	                                    case 22:
	                                    case 29:
	                                    case 30:
	                                    case 45:
	                                    case 50:
	                                    	cont=cont+1;
	                                        break;
	                                   
	                                }
	                   }
	                   
	                   if (cont==0){
	                	   beneficioCovid="N";
	                   }
			  
		  }else{
			  beneficioCovid="N";
		  }
		 
    }catch (Exception e) {
        e.printStackTrace();
     }
	return beneficioCovid;
}  
	 
public String getBeneficioCovid19Menu(long rut)throws Exception {
    Connection conn = null;
    CallableStatement  callCov = null;
    String beneficioCovid = null;
     try{ 
      conn = this.getConnection();
             System.out.println("mira parametro beneficio  covid19---------------------------------------------->");
      //Deudas que forman parte de la propuesta, pero no son propias del contribuyente
		 callCov = conn.prepareCall("{? = call PKG_COVID19.AnalizaRut(?)}");
		 callCov.setLong(2, rut);/*tipo de formulario */
		 callCov.registerOutParameter(1, OracleTypes.VARCHAR);
		 callCov.execute();
		 
		 System.out.println("mira parametro beneficio covid19---->"+callCov.getObject(1).toString());
		 //result = call.getObject(1).toString();
		 beneficioCovid = callCov.getObject(1).toString();
	
		 callCov.close(); 

		 
    }catch (Exception e) {
        e.printStackTrace();
     }finally{
         this.closeConnection();
         return beneficioCovid;
     }
	
}  
    
}