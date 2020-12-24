/**
 * 
 */
package cl.decalink.tgr.convenios.ejb.session.stateful.sesionconvenios;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import javax.ejb.SessionBean;
import javax.ejb.SessionSynchronization;
import javax.ejb.SessionContext;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

import javax.sql.DataSource;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import cl.decalink.tgr.convenios.sesion.Perfil;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="SesionConvenios"	
 *           description="An EJB named SesionConvenios"
 *           display-name="SesionConvenios"
 *           jndi-name="cobranzas.convenios.negocio.ejb.session.stateful.SesionConvenios"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public class SesionConveniosBean implements javax.ejb.SessionBean {
    /** Contexto para el EJB */
    private SessionContext sessionContext;

    /** Mantiene el valor del perfil del usuario que ingresa */
    private int perfil;

    /** Mantiene un objeto perfil que se compone de los valores asociados al perfil de ingreso */
    private Perfil perfilSesion;

    /** Mantiene las opciones a las cuales el usuario puede ingresar */
    private ArrayList opciones;

    /** Mantiene un objeto conexi�n �nico para el bean dentro de su ciclo de vida */
    private Connection connection;

    /** Mantiene la fuente de datos asignada desde el pool de conexiones */
    private transient DataSource dataSource;

    /** Mantiene el �ltimo rut activo digitado para ser intercambiado entre las p�ginas JSP */
    private String rutContribuyente="";

    /** Es verdadero cuando existe la UTM del mes actual o del mes anterior, falso si no es as� */
    private boolean existeUTM;

    /** Es verdadero si existe el IPC del mes actual o del mes anterior, falso si no es as� */
    private boolean existeIPC;

    /** Mantiene el c�digo del funcionario de la tesorer�a que inicio sesi�n */
    public Integer idFuncionario=new Integer(0);

    /** Mantiene el c�digo de la Tesorer�a a la cual pertenece el funcionario */
    public Integer idTesoreria;

    /** Mantiene el c�digo de usuario con el cual se hace el LOG IN */
    public Integer idUsuario;

    /** Mantiene el �mbito del usuario que ingresa "N" Nacional;  "J" Jurisdiccional */
    public String idAmbito;

    /** Mantiene el LOGIN de la persona que ingres� */
    public String login="";

    /** Mantiene la descripci�n del perfil que el usuario que ingresa posee */
    public String descripcionPerfil="";
    
    /** Nombre completo funcionario */
    public String nombreFuncionario;

    /** Descripci�n tesoreria */
    public String desTesoreria="";    

   /** opciones del simulador */
    public String simuladorActivo="N";

    public int cantCuotaContado=0;

    public int porcentCuotaContadoParam=0;

    public int cantValorCuota=0;

    public String botonSimuladorActivo="N";


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
		this.closeConnection();

	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
		 this.sessionContext = arg0;

	}

	/**
	 * 
	 */
	public SesionConveniosBean() {
		// TODO Auto-generated constructor stub
	}
	
	
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Entrega el c�digo del funcionario
     *  @return un Integer con el c�digo del funcionario */
    public Integer getIdFuncionario() {
        return this.idFuncionario;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Entrega el c�digo de la Tesorer�a a la cual pertenece el funcionario
     *  @return un Integer con el c�digo del funcionario */
    public Integer getIdTesoreria() {
        return this.idTesoreria;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Entrega el c�digo del usuario que hizo LOG IN
     *  @return un Integer con el c�digo del funcionario */
    public Integer getIdUsuario() {
      return this.idUsuario;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el c�digo de usuario que hizo el LOG IN
     *  @param idUsuario - Integer que contiene el c�digo del usuario
     */
    public void setIdUsuario(Integer idUsuario) {
      this.idUsuario = idUsuario;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Entrega el �mbito que est� establecido para el usuario
     *  @return un String con "N" si el �mbito es Nacional o "J"  si es Jurisdiccional
     */
    public String getIdAmbito() {
        return idAmbito;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el �mbito que posee el usuario
     *  @param idAmbito - String que contiene el �mbito al que pertenece el usuario
     */
    public void setIdAmbito(String idAmbito) {
        this.idAmbito = idAmbito;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Entrega el login del usuario que ingreso
     *  @return un String con el nombre del usuario
     */
    public String getLogin(){
        return this.login;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Entrega la descripci�n del perfil del usuario que ingreso
     *  @return un String con la descripci�n del perfil del usuario
     */
    public String getDescripcionPerfil(){
        return this.descripcionPerfil;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Entrega el valor del c�digo del perfil
     *  @return un numero (int) con el c�digo del perfil del usuario
     */
    public int getPerfil() {
        return this.perfil;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece el c�digo del perfil del usuario que ingreso
     *  @param un numero (int) con el c�digo del perfil del usuario
     */
    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna una colecci�n con las opciones de usuario
     *  @return un Collection con las opciones de usuario
     */
    public Collection getOpciones(){
        return this.opciones;
    }
    
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */    
    public void setSimuladorActivo(String simuladorActivo) {
        this.simuladorActivo = simuladorActivo;
    }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public String getSimuladorActivo() {
        return this.simuladorActivo;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public void setCantCuotaContado(int cantCuotaContado) {
        this.cantCuotaContado = cantCuotaContado;
    }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public int getCantCuotaContado() {
        return this.cantCuotaContado;
    }
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public void setPorcentCuotaContadoParamo(int porcentCuotaContadoParam) {
        this.porcentCuotaContadoParam = porcentCuotaContadoParam;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public int getPorcentCuotaContadoParam() {
        return this.porcentCuotaContadoParam;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public void setCantValorCuota(int cantValorCuota) {
        this.cantValorCuota = cantValorCuota;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public int getCantValorCuota() {
        return this.cantValorCuota;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public void setBotonSimuladorActivo(String botonSimuladorActivo) {
        this.botonSimuladorActivo = botonSimuladorActivo;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
    public String getBotonSimuladorActivo() {
        return this.botonSimuladorActivo;
    }

    
    /** Devuelve el tipo de permiso que el usuario tiene para acceder al
     *  una opci�n.
     *  @param un Integer con la opci�n
     *  @return un numero (int) con el nivel de permiso que posee el usuario
     */
    public int getTipoPermiso(Integer opcion){
        int permiso = 1;
        Iterator it = this.opciones.iterator();

       // System.out.println("opci�n ingreso:"+opcion);

        while(it.hasNext()){
            HashMap opcionTmp = (HashMap) it.next();
            Integer vOpcion=(Integer)opcionTmp.get("OPCION");
           //System.out.println("opci�n:"+vOpcion);
            if (((Integer) opcionTmp.get("OPCION")).equals(opcion)){
                String op = (String) opcionTmp.get("TIPO_PERMISO");
                //System.out.println("opci�n:"+op);
                if (op.equals("D")) permiso = 1;
                if (op.equals("L")) permiso = 2;
                if (op.equals("M")) permiso = 3;
                if (op.equals("T")) permiso = 4;
            }
        }
        return permiso;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Establece las opciones del usuario
     *  @param Collection con las opciones de usuario
     */
    public void setOpciones(Collection opciones){
        this.opciones = new ArrayList();
        this.opciones.addAll(opciones);
    }
    

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */    
   public void setNombreFuncionario(String nombreFuncionario) {
       this.nombreFuncionario = nombreFuncionario;
   }
   /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
   public String getNombreFuncionario() {
       return this.nombreFuncionario;
   }

   /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */    
 public void setDesTesoreria(String desTesoreria) {
     this.desTesoreria = desTesoreria;
 }
 /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
 public String getDesTesoreria() {
     return this.desTesoreria;
 }   
   
    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Ejecuta la
     *  consulta SQL que obtiene el c�digo de funcionario y el c�digo de la
     *  tesorer�a .
     */
    public void login() throws RemoteException, SQLException, NamingException {
        java.sql.Connection con;
        String query= "";

        try {
            InitialContext ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("oracleds");
        } catch(NamingException nex) {
            nex.printStackTrace();
            throw nex;
        }

        try {
            con = this.getConnection();

            PreparedStatement pstmt = con.prepareCall("select valor_utm(sysdate),valor_ipc(sysdate) from dual");
            if (pstmt.execute()) {
                ResultSet rs = pstmt.getResultSet();

                if (rs.next()){
                    this.existeUTM = (rs.getLong(1) != -1);
                    //System.out.println("this.existeUTM = " + this.existeUTM);
                    this.existeIPC = (rs.getLong(2) != -1);
                    //System.out.println("this.existeIPC = " + this.existeIPC);
                }
            }

            CallableStatement call = con.prepareCall("{? = call SesionConvenios.DatosSesion(?)}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            call.setLong(2, idUsuario.intValue());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                idFuncionario = new Integer(rs.getInt(1));
                this.idAmbito = rs.getString(2);
                this.perfil = rs.getInt(3);
                idTesoreria = new Integer(rs.getInt(4));
                this.login = rs.getString(5);
                this.descripcionPerfil = rs.getString(6);
                this.setNombreFuncionario(rs.getString(7));
                this.setDesTesoreria(rs.getString(8));
            }
            rs.close();

            perfilSesion = new Perfil();

            call = con.prepareCall("{? = call ConfigurarPropuesta.MontoMinimoConvenio}");
            call.registerOutParameter(1, java.sql.Types.VARCHAR);
            call.execute();

            String resultado = call.getString(1);
            long monto;
            try {
                monto = ((Long) new Long(resultado)).longValue();
            } catch (NumberFormatException e) {
                throw new SQLException("Error SQL" + e.toString());
            }

            perfilSesion.setMontoMinimo(monto);

            call = con.prepareCall("{? = call ConfigurarPropuesta.ValoresPorPerfil(?)}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            call.setLong(2, perfil);
            call.execute();

            rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                perfilSesion.setPorcentajeCuotaContado(rs.getInt(2));


                perfilSesion.setNumeroCuotas(rs.getInt(3));
                //System.out.println("numero de cuotas="+perfilSesion.getNumeroCuotas());
                perfilSesion.setMontoMaximoFiscal(rs.getLong(4));
                perfilSesion.setMontoMaximoTerritorial(rs.getLong(5));
            }

             call = con.prepareCall("{? = call PKG_SIMULADOR.obtieneParametroSimulador}");
             call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
             call.execute();

             rs = (ResultSet) call.getObject(1);

             while (rs.next()) {

                 if (rs.getString("VALOR_1")!=null && rs.getString("VALOR_1").length()>0)
                 System.out.println(rs.getString("VALOR_1"));

                 if(rs.getInt("ID_PARAMETRO_SIMULADOR")==1){
                   this.setCantCuotaContado(Integer.parseInt(rs.getString("VALOR")));
                   this.setPorcentCuotaContadoParamo(Integer.parseInt(rs.getString("VALOR_1")));
                 }
                 if(rs.getInt("ID_PARAMETRO_SIMULADOR")==2){
                   this.setCantValorCuota(Integer.parseInt(rs.getString("VALOR")));
                 }
                 if(rs.getInt("ID_PARAMETRO_SIMULADOR")==3){
                   this.setSimuladorActivo(rs.getString("VALOR"));
                 }
                 if(rs.getInt("ID_PARAMETRO_SIMULADOR")==4){
                   this.setBotonSimuladorActivo(rs.getString("VALOR"));
                 }


             }


            rs.close();
            call.close();


        } catch(SQLException sq) {
            sq.printStackTrace();
            throw sq;
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
	 * Ejecuta la
     *  consulta SQL que determina el perfil de Internet.
     */
    public void loginInternet() throws RemoteException, SQLException, NamingException {
        java.sql.Connection con;
        String query= "";

        try {
            InitialContext ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("oracleds");
        } catch(NamingException nex) {
            nex.printStackTrace();
            throw nex;
        }

        try {
            con = this.getConnection();

            PreparedStatement pstmt = con.prepareCall("select valor_utm(sysdate),valor_ipc(sysdate) from dual");
            if (pstmt.execute()) {
                ResultSet rs = pstmt.getResultSet();

                if (rs.next()){
                    this.existeUTM = (rs.getLong(1) != -1);
                    this.existeIPC = (rs.getLong(2) != -1);

                }
            }

            CallableStatement call = con.prepareCall("{? = call SesionConvenios.PerfilInternet}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                idFuncionario = new Integer(rs.getInt(4));
                this.idAmbito = "N";
                this.perfil = rs.getInt(1);
                idTesoreria = new Integer(rs.getInt(2));
                this.descripcionPerfil = rs.getString(3);
            }
            rs.close();

            perfilSesion = new Perfil();

            call = con.prepareCall("{? = call ConfigurarPropuesta.MontoMinimoConvenio}");
            call.registerOutParameter(1, java.sql.Types.VARCHAR);
            call.execute();

            String resultado = call.getString(1);
            long monto;
            try {
                monto = ((Long) new Long(resultado)).longValue();
            } catch (NumberFormatException e) {
                throw new SQLException("Error SQL" + e.toString());
            }

            perfilSesion.setMontoMinimo(monto);

            call = con.prepareCall("{? = call ConfigurarPropuesta.ValoresPorPerfil(?)}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            call.setLong(2, perfil);
            call.execute();

            rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                perfilSesion.setPorcentajeCuotaContado(rs.getInt(2));
                perfilSesion.setNumeroCuotas(rs.getInt(3));
                perfilSesion.setMontoMaximoFiscal(rs.getLong(4));
                perfilSesion.setMontoMaximoTerritorial(rs.getLong(5));
            }

            rs.close();
            call.close();


        } catch(SQLException sq) {
            sq.printStackTrace();
            throw sq;
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
	 * Devuelve verdadero si existe el valor del UTM
     *  @return un boolean si existe o no
     */
    public boolean getExisteUTM(){
        return this.existeUTM;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Devuelve verdadero si existe el valor del IPC
     *  @return un boolean si existe o no
     */
    public boolean getExisteIPC(){
        return this.existeIPC;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Devuelve un objeto Perfil con los valores del perfil de la sesi�n
     *  @return un objeto Perfil
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
	 * Establece el rut actual del contribuyente que se est� usando
     *  @param un String que contiene el rut del contribuyente sin d�gito
     *             verificador ni puntos
     */
    public void setRutContribuyente(String rutContribuyente) {
        this.rutContribuyente = rutContribuyente;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Devuelve el rut actual que se esta usando de un contribuyente
     *  @return un String
     */
    public String getRutContribuyente(){
        return this.rutContribuyente;
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Retorna la conexi�n desde el pool de conexiones
     *  @return un objeto Connection con la conexi�n abierta y
     *  con la propiedad AutoCommit a true
     */
    public Connection getConnection() throws SQLException {
//        System.out.println("ENTRA AL GETCONECTION");
        if (this.connection == null){
            this.connection = this.dataSource.getConnection();
        }

        if (this.connection.isClosed()){
            this.connection = this.dataSource.getConnection();
        }

      // this.connection.setAutoCommit(true);
//        System.out.println("HARA RETURN DE CONECTION");
//        System.out.println("ESTO RETORNA : "+this.connection);
        return this.connection;
    }

    /** 
	 *
	 *  Cierra la conexi�n comprobando antes si es que no est� cerrada
     */
    private void closeConnection() {
        try {
            if (this.connection != null){
                if (!this.connection.isClosed()){
                    this.connection.close();
                }
                this.connection = null;
            }
        } catch (SQLException e) {
            //System.out.println("Error closeConnection();");
            e.printStackTrace();
        }
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 * Ejecuta la
      *  servicio que valida monto minimo para realizar convenio
      */
     public int validaMontoTotal(Long monto ) throws RemoteException, SQLException, NamingException {
         java.sql.Connection conn;
         String query= "";

         try {
             InitialContext ic = new InitialContext();
             dataSource = (DataSource) ic.lookup("oracleds");
         } catch(NamingException nex) {
             nex.printStackTrace();
             throw nex;
         }

         try {
             conn = this.getConnection();


             CallableStatement call = conn.prepareCall("{? = call pkg_ley20027.valida_monto_total(?)}");
             call.registerOutParameter(1, java.sql.Types.INTEGER);
             call.setLong(2, monto.longValue());
             call.execute();

             int resultado = call.getInt(1);
             call.close();

             return resultado;
         } catch(SQLException sq) {
             sq.printStackTrace();
             throw sq;
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
	 * Ejecuta la
         *  servicio que valida si rut evaluado tiene informacion renta o cotizaciones
         */
        public int validaRenta(Long rut ) throws RemoteException, SQLException, NamingException {
            java.sql.Connection conn;

            try {
                InitialContext ic = new InitialContext();
                dataSource = (DataSource) ic.lookup("oracleds");
            } catch(NamingException nex) {
                nex.printStackTrace();
                throw nex;
            }

            try {
                conn = this.getConnection();


                CallableStatement call = conn.prepareCall("{? = call pkg_ley20027.valida_renta(?)}");
                call.registerOutParameter(1, java.sql.Types.INTEGER);
                call.setLong(2, rut.longValue());
                call.execute();

                int resultado = call.getInt(1);
                call.close();

                return resultado;
            } catch(SQLException sq) {
                sq.printStackTrace();
                throw sq;
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
   	 * Ejecuta la
              *  servicio que obtiene porcentaje minimo, segun reglas de caducidad
              */
             public int getPorcentaje20027(Long rut ) throws RemoteException, SQLException, NamingException {
                 java.sql.Connection conn;
                 String query= "";

                 try {
                     InitialContext ic = new InitialContext();
                     dataSource = (DataSource) ic.lookup("oracleds");
                 } catch(NamingException nex) {
                     nex.printStackTrace();
                     throw nex;
                 }

                 try {
                     conn = this.getConnection();


                     CallableStatement call = conn.prepareCall("{? = call pkg_ley20027.valida_renta(?)}");
                     call.registerOutParameter(1, java.sql.Types.INTEGER);
                     call.setLong(2, rut.longValue());
                     call.execute();

                     int resultado = call.getInt(1);
                     call.close();

                     return resultado;
                 } catch(SQLException sq) {
                     sq.printStackTrace();
                     throw sq;
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
        	 * Ejecuta la
             *  servicio que obtiene porcentaje minimo, segun reglas de caducidad
             */
            public HashMap getMontoMinimo(Long rut,Long totalDeuda ) throws RemoteException, SQLException, NamingException {
                java.sql.Connection conn;
                String query= "";
                HashMap valoresMonto=null;

                try {
                    InitialContext ic = new InitialContext();
                    dataSource = (DataSource) ic.lookup("oracleds");
                } catch(NamingException nex) {
                    nex.printStackTrace();
                    throw nex;
                }

                try {
                    conn = this.getConnection();


                    CallableStatement call = conn.prepareCall("{call pkg_ley20027.get_monto_minimo(?,?,?,?,?,?,?,?,?)}");

                    call.registerOutParameter(3, java.sql.Types.INTEGER);
                    call.registerOutParameter(4, java.sql.Types.INTEGER);
                    call.registerOutParameter(5, java.sql.Types.INTEGER);
                    call.registerOutParameter(6, java.sql.Types.INTEGER);
                    call.registerOutParameter(7, java.sql.Types.INTEGER);
                    call.registerOutParameter(8, java.sql.Types.INTEGER);
                    call.registerOutParameter(9, java.sql.Types.INTEGER);
                    call.setLong(1, rut.longValue());
                    call.setLong(2, totalDeuda.longValue());
                    call.execute();

                    int resultado = call.getInt(9);

                      Long vFraccUtm = new Long(call.getLong(3));
                      Long vPorcentaje = new Long(call.getLong(4));
                      Long vValorF22 = new Long(call.getLong(5));
                      Long vValorAfp = new Long(call.getLong(6));
                      Long vMontoValorF22 = new Long(call.getLong(7));
                      Long vMontoValorAfp = new Long(call.getLong(8));


                    valoresMonto = new HashMap();
                    valoresMonto.put("vFraccUtm",vFraccUtm);
                    valoresMonto.put("vPorcentaje",vPorcentaje);
                    valoresMonto.put("vValorF22",vValorF22);
                    valoresMonto.put("vValorAfp",vValorAfp);
                    valoresMonto.put("vValorF22",vValorF22);
                    valoresMonto.put("vValorAfp",vValorAfp);
                    valoresMonto.put("vMontoValorF22",vMontoValorF22);
                    valoresMonto.put("vMontoValorAfp",vMontoValorAfp);
                    valoresMonto.put("resultado",new Long(resultado));




                    call.close();




                    return valoresMonto;
                } catch(SQLException sq) {
                    sq.printStackTrace();
                    throw sq;
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
             *  Define si un usuario pertenece a una tesoreria piloto
            */
                   public int usuarioEnTesoPiloto(Integer codUsuario) throws RemoteException, SQLException, NamingException {
                       java.sql.Connection conn;

                       try {
                           InitialContext ic = new InitialContext();
                           dataSource = (DataSource) ic.lookup("oracleds");
                       } catch(NamingException nex) {
                           nex.printStackTrace();
                           throw nex;
                       }

                       try {
                           conn = this.getConnection();


                           CallableStatement call = conn.prepareCall("{? = call en_teso_piloto(?)}");
                           call.registerOutParameter(1, java.sql.Types.INTEGER);
                           call.setInt(2, codUsuario.intValue());
                           call.execute();

                           int resultado = call.getInt(1);
                           call.close();

                           return resultado;
                       } catch(SQLException sq) {
                           sq.printStackTrace();
                           throw sq;
                       }
                       finally{
                         this.closeConnection();
                      }

                   }
           
           
           
}