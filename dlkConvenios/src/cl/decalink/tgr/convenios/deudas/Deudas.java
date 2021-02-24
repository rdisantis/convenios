package cl.decalink.tgr.convenios.deudas;

/*
 * Title: Deudas.java
 * Description: Agrupa un conjunto de deudas
 * Copyright:    Copyright (c) 2003
 * Company: Decalink Ltda.
 * @author Gonzalo Ariel Aro D�az
 * @version 1.0
 */

import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.io.Serializable;

import cl.decalink.tgr.convenios.ReformaTributariaInput;
import cl.decalink.tgr.convenios.StringExt;

import cl.decalink.tgr.convenios.propuestas.TipoConvenio;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkException;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.TransactionRolledbackException;
import javax.ejb.*;



/*nuevo*/
import liquidador.ServicioLiquidacion;
import liquidador.ServicioLiquidacionHome;
import cl.decalink.tgr.convenios.DateExt;
import java.math.BigDecimal;
import javax.ejb.*;

public class Deudas implements Serializable {
    private transient Connection connection;
    private long rut;
    private String estadoCobranza;
    private long idPersona;
    private int tipoPago;
    private int transportista;
    private int perfil;
    private long total;
    private Vector deudas;
    private int tipoConvenio;
    private String tipoImpuesto;
    private Date fechaEmision;
    private HashMap hmGiroAdicional;
    private Vector hmGiroAdicionalA192;
    private Vector hmGiroAdicionalRMH;

    private PreparedStatement pstmtA192;
    private PreparedStatement pstmtRMH;
    private PreparedStatement pstmtRes481;
    //private PreparedStatement pstmtVMCR;

    private CallableStatement pstmtVMCR;
    private CallableStatement callCF;
    private CallableStatement callVI;
    private CallableStatement callMI;

    private long daminficado=1;
    private String origen;


    public Deudas(long rut, String estadoCobranza, long idPersona) {
        this.rut = rut;
        this.estadoCobranza = estadoCobranza;
        this.idPersona = idPersona;
        this.deudas = new Vector();
        this.transportista = 1;
        this.tipoImpuesto = "F";
        this.total = 0;
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public String FechaResTribu2012=null;
    public String FechaVigenciaTribu2012=null;
    public String FechaOtorgTribu2012=null;
    public String FechaOtorgF30Tribu2012=null;

    public String FechaResTribu2014=null;
    public String FechaVigenciaTribu2014=null;
    public String FechaOtorgTribu2014=null;
    public String FechaOtorgF30Tribu2014=null;    
    
    public void obtieneParametros() throws SQLException {
      CallableStatement call;
      ResultSet rs;



     try {
      //call = this.connection.prepareCall("{call ConfigurarPropuesta.ObtieneFechasRTributaria2012(?,?,?,?)}");
      call = this.connection.prepareCall("{call ConfigurarPropuesta.ObtieneFechasRTributaria2014(?,?,?,?)}");
      call.registerOutParameter(1, oracle.jdbc.OracleTypes.VARCHAR);
      call.registerOutParameter(2, oracle.jdbc.OracleTypes.VARCHAR);
      call.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
      call.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);

      call.execute();
/*
 * condonacion anteior
      this.FechaResTribu2012=call.getString(1);
      this.FechaVigenciaTribu2012=call.getString(2);
      this.FechaOtorgTribu2012=call.getString(3);
      this.FechaOtorgF30Tribu2012=call.getString(4);*/

      this.FechaResTribu2014=call.getString(1);
      this.FechaVigenciaTribu2014=call.getString(2);
      this.FechaOtorgTribu2014=call.getString(3);
      this.FechaOtorgF30Tribu2014=call.getString(4);      
      
      call.close();
     }
    catch (SQLException eSQL) {
      throw new SQLException(
          "Error SQL ConfigurarPropuesta.ObtieneFechasRTributaria2014 " +
          eSQL.toString());
    }
    catch (Exception e) {
        System.out.print("nose");
      }

    }

    public void setDeudasPropuesta(String rutRoles, boolean agregarRol) throws SQLException{
        CallableStatement call;
        ResultSet rs;
        Vector retorno = new Vector();
        // vamos a obtener para metros ley transitoria
        this.obtieneParametros();

        callCF = this.connection.prepareCall("{? = call ConfigurarPropuesta.verificaMovimientoCutReplica(?,?,?,?,?)}");
        callCF.registerOutParameter(1,java.sql.Types.VARCHAR);

        callVI = this.connection.prepareCall("{? = call ConfigurarPropuesta.VerificaItems(?,?,?,?,?,?)}");
        callVI.registerOutParameter(1,java.sql.Types.VARCHAR);

        callMI = this.connection.prepareCall("{? = call ConfigurarPropuesta.MovimientoItemsArt192(?,?,?,?,?,?,?)}");
        callMI.registerOutParameter(1,java.sql.Types.VARCHAR);

        /*pstmtVMCR  =  this.connection.prepareStatement("SELECT COUNT(cc_mov_cut_replica.COD_MOVIMIENTO) " +
            " FROM cc_mov_cut_replica WHERE tipo_movimiento = 9 " +
 	    " AND valido = 'S' AND tip_cont = ? AND rut_rol = ? AND tip_form = ?  AND folio = ? AND fecha_vencimiento = ?");
       */
       pstmtVMCR=this.connection.prepareCall("{?= call ConfigurarPropuesta.TieneGiroAdicional(?,?,?,?,?)}");
       pstmtVMCR.registerOutParameter(1,java.sql.Types.INTEGER);

       pstmtRMH  =  this.connection.prepareStatement("SELECT tip_form, giro_adicional FROM  COCON_FORM_CONVENIOS " +
            " WHERE tip_convenio = 3 ");


        if (hmGiroAdicional == null) {
            hmGiroAdicional = new HashMap();
        }

        hmGiroAdicional.clear();

        ResultSet rsRMH = pstmtRMH.executeQuery();
        while (rsRMH.next()) {
            hmGiroAdicional.put(new Integer(rsRMH.getInt(1)), rsRMH.getString(2));
        }
        rsRMH.close();
        pstmtRMH.close();

        pstmtA192  =  this.connection.prepareStatement("SELECT tip_form, giro_adicional, DECODE(Codigo_Item,NULL,-1,Codigo_Item) AS Codigo_Item, incluye_excluye_item, EXCLUYE_CONVENIO, EXCLUYE_CONDONACION " +
                  " FROM COCON_EXCLUSIONES_ART192 ");

        if (hmGiroAdicionalA192 == null){
            hmGiroAdicionalA192 = new Vector();
        }

        hmGiroAdicionalA192.clear();
        ResultSet rs192 = pstmtA192.executeQuery();
        while (rs192.next()) {
            HashMap structA192 = new HashMap();
            structA192.put("sGiroAdicional", StringExt.nuloAVacio(rs192.getString(2)));
            structA192.put("lCodigoItem", new Long(rs192.getLong(3)));
            structA192.put("sIncluyeExcluyeItem", StringExt.nuloAVacio(rs192.getString(4)));
            structA192.put("sExcluyeConvenio", StringExt.nuloAVacio(rs192.getString(5)));
            structA192.put("sExcluyeCondonacion", StringExt.nuloAVacio(rs192.getString(6)));
            structA192.put("tipoFormulario",new Integer(rs192.getInt(1)));
            hmGiroAdicionalA192.addElement(structA192);

        }
        rs192.close();
        pstmtA192.close();

        pstmtRes481 = this.connection.prepareStatement("SELECT tip_form, giro_adicional, DECODE(Codigo_Item,NULL,-1,Codigo_Item) AS Codigo_Item, incluye_excluye_item, EXCLUYE_CONVENIO, EXCLUYE_CONDONACION " +
                  " FROM COCON_EXCLUSIONES_RMH ");

        if (hmGiroAdicionalRMH == null){
            hmGiroAdicionalRMH = new Vector();
        }

        hmGiroAdicionalRMH.clear();

        ResultSet rsRes481 = pstmtRes481.executeQuery();

        while (rsRes481.next()) {
            HashMap structRMH = new HashMap();
            structRMH.put("sGiroAdicional", StringExt.nuloAVacio(rsRes481.getString(2)));
            structRMH.put("lCodigoItem", new Long(rsRes481.getLong(3)));
            structRMH.put("sIncluyeExcluyeItem", StringExt.nuloAVacio(rsRes481.getString(4)));
            structRMH.put("sExcluyeConvenio", StringExt.nuloAVacio(rsRes481.getString(5)));
            structRMH.put("sExcluyeCondonacion", StringExt.nuloAVacio(rsRes481.getString(6)));
            structRMH.put("tipoFormulario",new Integer(rsRes481.getInt(1)));
            hmGiroAdicionalRMH.addElement(structRMH);
        }

        rsRes481.close();

        pstmtRes481.close();


        try {

            if (!this.estadoCobranza.equals("D")){
                call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasContribuyenteV2(?,?,?)}");
                call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                call.setString(2, rutRoles);
                call.setLong(3, (agregarRol)? -1 : this.idPersona);
                call.setLong(3, (agregarRol)? -1 : this.idPersona);
                call.setString(4, this.estadoCobranza);
                }
            else{
                //System.out.println("origen:"+this.origen);
             //   call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasDemanda(?)}");
                System.out.println("ingrese a metodo correcto");
            	call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasDemandaV2(?,?)}");
                call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                call.setString(2, rutRoles);
                call.setString(3,this.origen);
                
            }

            call.execute();
             
            rs = (ResultSet) call.getObject(1);
            this.addInfo(rs, agregarRol);
            rs.close();
            call.close();
        } catch (SQLException eSQL) {
            throw new SQLException ("Error SQL DeudasContribuyente.ListaDeudasContribuyenteV2 " + eSQL.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    try {

            if (!this.estadoCobranza.equals("D")){

            call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasCandidatas(?,?)}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            call.setLong(2, this.rut);
            call.setString(3, this.estadoCobranza);
            call.execute();
            rs = (ResultSet) call.getObject(1);
            this.addInfo(rs, false);

            rs.close();
            call.close();
            }
        } catch (SQLException eSQL) {
            throw new SQLException ("Error SQL DeudasContribuyente.ListaDeudasCandidatas" + eSQL.toString());
        } catch (Exception e) {
  
        }

        rs = null;
        call = null;
    }


//nuevo metodo para activar y desactivar grilla liqu aix ejb  satelites
    
    public void setDeudasPropuesta(String rutRoles, boolean agregarRol, String grillaEjbSatelites) throws SQLException{
        this.setGrillaEjb(grillaEjbSatelites);
      	CallableStatement call;
        ResultSet rs;
        Vector retorno = new Vector();
        // vamos a obtener para metros ley transitoria
        this.obtieneParametros();

        callCF = this.connection.prepareCall("{? = call ConfigurarPropuesta.verificaMovimientoCutReplica(?,?,?,?,?)}");
        callCF.registerOutParameter(1,java.sql.Types.VARCHAR);

        callVI = this.connection.prepareCall("{? = call ConfigurarPropuesta.VerificaItems(?,?,?,?,?,?)}");
        callVI.registerOutParameter(1,java.sql.Types.VARCHAR);

        callMI = this.connection.prepareCall("{? = call ConfigurarPropuesta.MovimientoItemsArt192(?,?,?,?,?,?,?)}");
        callMI.registerOutParameter(1,java.sql.Types.VARCHAR);

        /*pstmtVMCR  =  this.connection.prepareStatement("SELECT COUNT(cc_mov_cut_replica.COD_MOVIMIENTO) " +
            " FROM cc_mov_cut_replica WHERE tipo_movimiento = 9 " +
 	    " AND valido = 'S' AND tip_cont = ? AND rut_rol = ? AND tip_form = ?  AND folio = ? AND fecha_vencimiento = ?");
       */
       pstmtVMCR=this.connection.prepareCall("{?= call ConfigurarPropuesta.TieneGiroAdicional(?,?,?,?,?)}");
       pstmtVMCR.registerOutParameter(1,java.sql.Types.INTEGER);

       pstmtRMH  =  this.connection.prepareStatement("SELECT tip_form, giro_adicional FROM  COCON_FORM_CONVENIOS " +
            " WHERE tip_convenio = 3 ");


        if (hmGiroAdicional == null) {
            hmGiroAdicional = new HashMap();
        }

        hmGiroAdicional.clear();

        ResultSet rsRMH = pstmtRMH.executeQuery();
        while (rsRMH.next()) {
            hmGiroAdicional.put(new Integer(rsRMH.getInt(1)), rsRMH.getString(2));
        }
        rsRMH.close();
        pstmtRMH.close();

        pstmtA192  =  this.connection.prepareStatement("SELECT tip_form, giro_adicional, DECODE(Codigo_Item,NULL,-1,Codigo_Item) AS Codigo_Item, incluye_excluye_item, EXCLUYE_CONVENIO, EXCLUYE_CONDONACION " +
                  " FROM COCON_EXCLUSIONES_ART192 ");

        if (hmGiroAdicionalA192 == null){
            hmGiroAdicionalA192 = new Vector();
        }

        hmGiroAdicionalA192.clear();
        ResultSet rs192 = pstmtA192.executeQuery();
        while (rs192.next()) {
            HashMap structA192 = new HashMap();
            structA192.put("sGiroAdicional", StringExt.nuloAVacio(rs192.getString(2)));
            structA192.put("lCodigoItem", new Long(rs192.getLong(3)));
            structA192.put("sIncluyeExcluyeItem", StringExt.nuloAVacio(rs192.getString(4)));
            structA192.put("sExcluyeConvenio", StringExt.nuloAVacio(rs192.getString(5)));
            structA192.put("sExcluyeCondonacion", StringExt.nuloAVacio(rs192.getString(6)));
            structA192.put("tipoFormulario",new Integer(rs192.getInt(1)));
            hmGiroAdicionalA192.addElement(structA192);

        }
        rs192.close();
        pstmtA192.close();

        pstmtRes481 = this.connection.prepareStatement("SELECT tip_form, giro_adicional, DECODE(Codigo_Item,NULL,-1,Codigo_Item) AS Codigo_Item, incluye_excluye_item, EXCLUYE_CONVENIO, EXCLUYE_CONDONACION " +
                  " FROM COCON_EXCLUSIONES_RMH ");

        if (hmGiroAdicionalRMH == null){
            hmGiroAdicionalRMH = new Vector();
        }

        hmGiroAdicionalRMH.clear();

        ResultSet rsRes481 = pstmtRes481.executeQuery();

        while (rsRes481.next()) {
            HashMap structRMH = new HashMap();
            structRMH.put("sGiroAdicional", StringExt.nuloAVacio(rsRes481.getString(2)));
            structRMH.put("lCodigoItem", new Long(rsRes481.getLong(3)));
            structRMH.put("sIncluyeExcluyeItem", StringExt.nuloAVacio(rsRes481.getString(4)));
            structRMH.put("sExcluyeConvenio", StringExt.nuloAVacio(rsRes481.getString(5)));
            structRMH.put("sExcluyeCondonacion", StringExt.nuloAVacio(rsRes481.getString(6)));
            structRMH.put("tipoFormulario",new Integer(rsRes481.getInt(1)));
            hmGiroAdicionalRMH.addElement(structRMH);
        }

        rsRes481.close();

        pstmtRes481.close();


        try {

            if (!this.estadoCobranza.equals("D")){
            	//validacion de grilla ejb aix
            	if (this.getGrillaEjb().equalsIgnoreCase("S"))
            		call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasContribuyenteV2(?,?,?)}");
            	else
            		call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasContribuyente(?,?,?)}");		
            		
            	call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                call.setString(2, rutRoles);
                //call.setLong(3, (agregarRol)? -1 : this.idPersona);
                call.setLong(3, -1);// se inactiva la consulta de roles en grilla
                call.setString(4, this.estadoCobranza);
                }
            else{
                //System.out.println("origen:"+this.origen);
             //   call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasDemanda(?)}");
            	if (this.getGrillaEjb().equalsIgnoreCase("S"))
            		call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasDemandaV2(?,?)}");
            	else
            		call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasDemanda(?,?)}");
            	
                call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                call.setString(2, rutRoles);
                call.setString(3,this.origen);
            }

            call.execute();

            rs = (ResultSet) call.getObject(1);
            this.addInfo(rs, agregarRol);
            rs.close();
            call.close();
            
        } catch (SQLException eSQL) {
            throw new SQLException ("Error SQL DeudasContribuyente.ListaDeudasContribuyenteV2 " + eSQL.toString());
        } catch (Exception e) {
        
        }
    
    try {

            if (!this.estadoCobranza.equals("D")){

            call = this.connection.prepareCall("{? = call DeudasContribuyente.ListaDeudasCandidatas(?,?)}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            call.setLong(2, this.rut);
            call.setString(3, this.estadoCobranza);
            call.execute();
            rs = (ResultSet) call.getObject(1);
            this.addInfo(rs, false);

            rs.close();
            call.close();
            }
        } catch (SQLException eSQL) {
            throw new SQLException ("Error SQL DeudasContribuyente.ListaDeudasCandidatas" + eSQL.toString());
        } catch (Exception e) {
  
        }

        rs = null;
        call = null;
    }    
    
    
//-----------------------------------------------------------------------------//    

    
//---------------seter  geter parametro de grilla ejb---------------------------//
    private String grillaEjbSat;
    
    public String getGrillaEjb() {
        return this.grillaEjbSat;
    }

    public void setGrillaEjb(String grillaEjbSat){
       this.grillaEjbSat = grillaEjbSat;
    }
//------------------------------------------------------------------------------//    
    
    
    public Collection getDeudas() {
        return this.deudas;
    }

    public void setDeudas(Collection deudas){
        this.deudas.clear();
        this.deudas.addAll(deudas);
    }

    public String getTipoImpuesto(){
        return this.tipoImpuesto;
    }

    public Collection getDeudasSeleccionadas() throws Exception {
        Vector retorno = new Vector();
        this.fechaEmision = null;

        try {
            this.total = 0;
            Iterator it = this.deudas.iterator();

            while (it.hasNext()){
                DeudaWeb deudaSeleccionada = (DeudaWeb) it.next();

                if (deudaSeleccionada.estaSeleccionado()){

                    if (deudaSeleccionada.getOrigen().equals("C"))
                    {
                      if (deudaSeleccionada.estaLiquidada())
                      {
                        this.total += deudaSeleccionada.getMonto();
                      }
                    }
                    else
                    {
                      this.total += deudaSeleccionada.getMonto();
                    }
                    tipoImpuesto = (deudaSeleccionada.getTipoFormulario() == 30)?"T":"F";
                    this.tipoConvenio = deudaSeleccionada.getTipoConvenio();
                    Date fecha = new Date(this.getFechaEmision(deudaSeleccionada).getTime());

                    if (deudaSeleccionada.getPosibilidadCondonacion().equals("S")){
                        if (this.fechaEmision == null){
                            this.fechaEmision = fecha;
                        }

                        if (fecha.compareTo(this.fechaEmision) < 0){
                            this.fechaEmision = new Date(fecha.getTime());
                        }
                    }

                    retorno.addElement(deudaSeleccionada);
                }
            }
        } catch (Exception e) {
             throw new Exception ("Error al recuperar deudas" + e.toString());
        }

        return retorno;
    }

  private Date getFechaEmision(DeudaWeb deuda) throws SQLException {
    CallableStatement call;
    Date fecha = null;

    try {
      if (deuda.getGrupo().equals("RMH Fiscal")) {
          /* Deudas territoriales de cut replica  por rol bien raiz*/
          call = this.connection.prepareCall("{? = call Configurarpropuesta.FechaEmisionDeuda(?,?,?,?,?)}");
          call.registerOutParameter(1, java.sql.Types.DATE);
          call.setLong(2, deuda.getTipoContribuyente());
          call.setLong(3, deuda.getRutRol());
          call.setLong(4, deuda.getTipoFormulario());
          call.setLong(5, deuda.getFolio());
          call.setDate(6, deuda.getFechaVencimiento());
          call.execute();

          fecha = new Date(((Date) call.getDate(1)).getTime());

          deuda.setFechaEmision(fecha);
          call.close();
      } else {
          fecha = deuda.getFechaVencimiento();
      }
    } catch (SQLException eSQL) {
      throw new SQLException ("Error SQL ConfiguracionPropuesta.FechaEmisionDeuda" + eSQL.toString());
    }

    return fecha;
  }

  public Date getFechaEmision(){
    return this.fechaEmision;
  }
  public int getTipoConvenio(){
    return this.tipoConvenio;
  }


    private void aplicadoA(DeudaWeb deudaWeb) throws SQLException{
        CallableStatement call;
        ResultSet rs;

        try {
            /* Deudas territoriales de deudas no cut  por rol bien raiz */
            call = this.connection.prepareCall("{? = call ConfigurarPropuesta.ClaseImpuesto(?,?,?,?,?,?)}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            call.setLong(2, deudaWeb.getTipoContribuyente());
            call.setLong(3, deudaWeb.getRutRol());
            call.setLong(4, deudaWeb.getTipoFormulario());
            call.setLong(5, deudaWeb.getFolio());
            call.setDate(6, deudaWeb.getFechaVencimiento());
            call.setString(7, deudaWeb.getOrigen());
            call.execute();

            rs = (ResultSet) call.getObject(1);

            /* Establece valores por defecto.
             */
            deudaWeb.setTasaInteres(0);
            deudaWeb.setCondonacionAplicadaA("A");
            deudaWeb.setTipoInteres("NO");
            deudaWeb.setTipoMulta("NO");
            deudaWeb.setTipoReajuste("NO");

            while(rs.next()) {

                deudaWeb.setTasaInteres(rs.getInt(2));
                deudaWeb.setCondonacionAplicadaA((rs.getString(3) == null)? "A" : rs.getString(3));
                if (deudaWeb.getCondonacionAplicadaA().equals("N"))
                {
                  deudaWeb.setPosibilidadCondonacion("N");
                }

                deudaWeb.setTipoInteres(rs.getString(4));
                deudaWeb.setTipoMulta(rs.getString(5));
                deudaWeb.setTipoReajuste(rs.getString(6));
            }
            rs.close();
            call.close();
        } catch (SQLException eSQL) {
            throw new SQLException ("Error SQL ConfigurarPropuesta.ClaseImpuesto" + eSQL.toString());
        }
    }

    public int getTipoPago(){
        return this.tipoPago;
    }

    public void setTipoPago(int tipoPago){
        this.tipoPago = tipoPago;
    }

    public int getTransportista(){
        return this.transportista;
    }

    public void setTransportista(int transportista){
        this.transportista = transportista;
    }

    public int getPerfil(){
        return this.perfil;
    }

    public void setPerfil(int perfil){
        this.perfil = perfil;
    }

    public long getTotal(){
        return this.total;
    }

    public long getDamnificado()
    {
       return this.daminficado;
    }

    public void setDamnificado(long damnificado)
    {
      this.daminficado=damnificado;
    }
    public boolean validarTotalMinimo(long montoMinimo) throws Exception{
        try {
            if (this.total < montoMinimo) return false;
            return true;
        } catch (Exception e) {
            throw new Exception ("Error Validar total Minimo");
        }
    }

    public boolean validarTotalMaximo(long montoMaximo) throws Exception {
        try {
            if (this.total > montoMaximo) return false;
            return true;
        } catch (Exception e) {
            throw new Exception ("Error Validar total Minimo");
        }
    }

    public boolean validarTotal(long montoMinimo, long montoMaximo)  throws Exception {
        try {
            if (this.validarTotalMinimo(montoMinimo) && this.validarTotalMaximo(montoMaximo)) return true;
            return false;
        } catch (Exception e) {
            throw new Exception ("Error Validar total");
        }
    }

    public void seleccionaDeudasPropuesta(String arregloDeudas, ReformaTributariaInput reformaTributariaInput) throws Exception {
        String separaDeudas  = ",";//separador de deudas
        String[] sDeudas = StringExt.split(arregloDeudas,separaDeudas);
        String separaClaves ="*";//separador de campos claves de la deuda
        String sstring = null;
        Vector retorno = new Vector();

        try {
            if (!deudas.isEmpty()) {
                Iterator it = deudas.iterator();
                while (it.hasNext()) {
                    DeudaWeb elemento = (DeudaWeb) it.next();
                    elemento.desseleccionar();
                    elemento.setPorcentajeCondonacion(0);
                    retorno.addElement((DeudaWeb) elemento);
                    it.remove();
                }//while (itx.hasNext())
            }//if (!deudasContribuyente.isEmpty())

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

                Iterator itx = retorno.iterator();
                while (itx.hasNext()) {
                    DeudaWeb elemento = (DeudaWeb) itx.next();
                    DeudaWeb newDeudaWeb = new DeudaWeb(((Long) new Long(tipoCont)).intValue(), ((Long) new Long(tipoForm)).intValue(),((Long) new Long(rutRol)).longValue(), ((Long) new Long(folio)).longValue(), TFechaVencimiento);

                    if (elemento.equals(newDeudaWeb) && elemento.getOrigen().equals(origen)) {
                        elemento.seleccionar();
                        elemento.setDamnificado(this.getDamnificado());

                        if (elemento.estaSeleccionado())
                        {
                            elemento.setPosibilidadConvenio("S");

                            if ((elemento.getPorcentajeCondonacionIntereses()>0)||(elemento.getPorcentajeCondonacionMultas()>0))
                            {
                                elemento.setPosibilidadCondonacion("S");
                            }
                        }
                        
                        //se inactiva las condonaciones normales, debido a reformaTributaria
                        if (!reformaTributariaInput.getIsReformaTributaria().booleanValue()){
	                        elemento.setConnection(this.connection);
	
	                        if (!elemento.getOrigen().equals("Q")) {
	                            if (elemento.getTipoFormulario() == 30){
	                                elemento.setPorcentajeCondonacionF30(this.tipoPago, this.perfil);
	                            } else {
	                                elemento.setPorcentajeCondonacionFiscal(this.tipoPago, this.perfil, this.transportista);
	                            }
	                        } else {
	
	                            elemento.setPorcentajeCondonacionFiscal(this.tipoPago, this.perfil, this.transportista);
	                            elemento.setPorcentajeCondonacion(0);
	                            elemento.setPorcentajeMaximoCondonacion(100);
	                            elemento.setPorcentajeMinimoCuotaContado(10);
	                        }
                        }
                    }
                    itx.remove();
                    deudas.addElement((DeudaWeb) elemento);
                }//while (itx.hasNext())
                retorno.addAll(deudas);
                deudas.clear();
            }//fin for

            deudas.addAll(retorno);
        }//if (!sDeudas.equals(""))


    } catch(Exception e) {
        throw new Exception("Error ejecutando en setSeleccionaDeudasContribuyente " +  e.toString());
    }
  }

    private void addInfo(ResultSet rs, boolean exclusionPorRol) {
    	
    	System.out.println("paremtro en addinfo-----> "+this.getGrillaEjb());
    	
        String posConvenio = "S";
        String posCondonacion = "S";
        String nombreGrupo="";
        long multas=0;
        long reajustes=0;
        long intereses=0;
        DeudaWeb deuda;
        String origen = "";
        long codigoConvenioMasivo;
        boolean habilitada = true;
        int vDiaVenc=1;
        int vMesVenc=0;
        int vAgnoVenc=1900;
        GregorianCalendar grFechaVenc=null;
        Date vFechaVenc=null;
        long cod92Liq = 0;
        
        /*nuevas variables*/
        long vMultas=0;
        long vReajustes=0;
        long vIntereses=0;
        long vNeto=0;

       try{
            while (rs.next()) {

            	//System.out.println("entro---> "+rs.getLong(1));
                reajustes = rs.getLong(10);
                multas = rs.getLong(11);
                intereses = rs.getLong(12);
                long saldo = rs.getLong(6);
                int porcentajeCondonacion = 0;
                long saldoConCondonacion = saldo ;//+ multas + reajustes + intereses - (saldo*(porcentajeCondonacion/100));

                vDiaVenc = rs.getInt(14);
                vMesVenc = rs.getInt(15);
                vAgnoVenc = rs.getInt(16);
                cod92Liq = rs.getLong(17);
                
                /*integro nuevo valores para no volver a liquidar*/
                
                if (this.getGrillaEjb().equalsIgnoreCase("S")){
                
                vReajustes=cod92Liq;
                vIntereses=rs.getLong(18);
                vMultas=rs.getLong(19);
                vNeto=rs.getLong(20);
                }
                //System.out.println("vemos el nero:::------------------------> "+ vNeto);
               
               /*termino de codigo cbs y ccc 15-10-2013*/                
                
                grFechaVenc = new GregorianCalendar(vAgnoVenc,vMesVenc,vDiaVenc);
                vFechaVenc = new Date(grFechaVenc.getTime().getTime());

                //vFechaVenc=rs.getString(5);

                deuda = new DeudaWeb(rs.getInt(4), rs.getInt(2), rs.getLong(1), rs.getLong(3), vFechaVenc);
                
             /*   if (deuda.getTipoFormulario()==14||deuda.getTipoFormulario()==19||deuda.getTipoFormulario()==36||deuda.getTipoFormulario()==36){
                 
                	try{
		                	//ingreso a liquidar a vax, con servicio del reca
		                  System.out.println("ingreso antes");	
		                  deuda=liquidaSatelite(deuda);
                	}catch (Exception e) {
            			System.out.println("try especial---> "+e);
            		
            		}	
                
                }*/ 
                
            	/*se integran nuevos valores reforma tributaria 23/07/2015  CCC*/
            	deuda.setVersionFormulario(rs.getString(21));
            	if (rs.getDate(22)!=null){
            		deuda.setFechaAntiguedad(new Date(((Date) rs.getDate(22)).getTime()));
            	}else{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsed =  format.parse("1900-01-01");
                    java.sql.Date sql = new java.sql.Date(parsed.getTime());
            		deuda.setFechaAntiguedad(sql);
            	}
            	deuda.setDerechoConvenio(rs.getString(23));
            	deuda.setDerechoCondonacion(rs.getString(24));
            	deuda.setEnDemanda(rs.getString(25));
            	
            	/*System.out.println("folio----> "+deuda.getFolio());
            	System.out.println("23----> "+rs.getString(23));
            	System.out.println("24----> "+rs.getString(24));
            	System.out.println("25----> "+rs.getString(25));
            	
            	
            	System.out.println("2que tiene----> "+deuda.getDerechoCondonacion());
            	System.out.println("2que tiene2T----> "+deuda.getDerechoCondonacionT());*/
            	
            	/*-------------------------------------------------------------*/                
         
                deuda.setMonto(saldo);
                deuda.setMontoConCondonacion(saldoConCondonacion);
                deuda.setDescripcionTipoDeuda(rs.getString(7));

                deuda.setPosibilidadCondonacion(posCondonacion);
                deuda.setPosibilidadConvenio(posConvenio);

                origen = rs.getString(9);

                deuda.setOrigen(origen);
 
                deuda.desseleccionar();
                deuda.setCod92Liq(new Long(cod92Liq));
         
                if (!"M".equals(origen)) {
                  deuda.setCodigoItemDeuda(rs.getInt(13));

                    //origen distinto convenio masivo(M)
                    nombreGrupo = (String) grupoDeuda(deuda);
                } else {
                  deuda.setCodigoItemDeuda(rs.getInt(15));

                    deuda.setTipoConvenio(TipoConvenio.MASIVO);

                    nombreGrupo = (String) "Masivo " + rs.getString(13);//descripcion convenio
                    codigoConvenioMasivo = rs.getLong(14);

                    CallableStatement call = this.connection.prepareCall("{? = call ReprogramaConvenio.VerificaDeudasReprogramadas(?,?)}");
                    call.registerOutParameter(1, java.sql.Types.NUMERIC);
                    call.setLong(2, codigoConvenioMasivo);
                    call.setLong(3, this.rut);
                    call.execute();

                    long cantidad  = call.getLong(1);
                    habilitada = (cantidad == 0);
                    call.close();
                }

                deuda.setGrupo(nombreGrupo);

                this.aplicadoA(deuda);
                
                /*se modifican estas variables*/
	                if (this.getGrillaEjb().equalsIgnoreCase("S")){
	                deuda.setMultas(vMultas);
	                deuda.setReajustes(vReajustes);
	                deuda.setIntereses(vIntereses);
	                deuda.setMonto(vNeto);
                }else{
                    deuda.setMultas(multas);
                    deuda.setReajustes(reajustes);
                    deuda.setIntereses(intereses);	
                }
                /*fin modificacion  cbs y ccc 15-10-2013*/
                
                deuda.setPorcentajeCondonacion(porcentajeCondonacion);

                deuda.setPorcentajeMaximoCondonacion(100);
                deuda.setRutRolFormateado(rs.getString(8));

                if ("N".equals(posConvenio) && "N".equals(posCondonacion) && !habilitada) {
                    deuda.deshabilitar();
                } else {
                    deuda.habilitar();
                }

                if (exclusionPorRol){
                    deuda.verificarExclusionPorRol();
                }

                /*Se valida que la deuda sea liquidable CCC 27032012*/
                if (deuda.getCod92Liq().longValue()<0){
                  deuda.setPosibilidadCondonacion("N");
                  deuda.setPosibilidadConvenio("N");
                  deuda.deshabilitar();
                }

                if (!deuda.getGrupo().equals("CHQ Protestado")){
                    this.deudas.addElement(deuda);
                }
            }
     
        }/*catch(EJBException e) {
        	System.out.println("voy poraqui--777------->");
        	e.getMessage();
        	e.toString();
        	e.printStackTrace();
            //throw new Exception("Error addInfo:" + e.toString());
        }*/ catch(Exception e) {
             e.printStackTrace();
            //throw new Exception("Error addInfo:" + e.toString());
        }     

        
       
        
    }

    private String grupoDeuda(DeudaWeb deuda) throws Exception{
      String retorno="No Agrupables (Sin derecho a convenio)";

        long existe = 0;
        long iteracion = 0;

        int tipoFormulariox = deuda.getTipoFormulario();

        String fechaInicio="2009-03-30";
        String fechaCompara="2009-03-31";
        String fechaVigencia="2010-06-30";
        String fechaComparaF30="";
        String fechaActual="";

        //nueva ley transitoria tributaria 2012
       /* String StrFechaInicioTribu2012=this.FechaResTribu2012;
        String StrfechaTrib2012=this.FechaOtorgTribu2012;
        String StrfinTrib2012=this.FechaVigenciaTribu2012;

        java.util.Date fechaIniciotribu2012=null;
        java.util.Date fechaTrib2012=null;
        java.util.Date finTrib2012=null;
        java.util.Date fechaEmisionD=null;

        java.util.Date fechaActual2012 = new java.util.Date();
        */
        
        String StrFechaInicioTribu2014=this.FechaResTribu2014 ;
        String StrfechaTrib2014=this.FechaOtorgTribu2014;
        String StrfinTrib2014=this.FechaVigenciaTribu2014;

       /* java.util.Date fechaIniciotribu2012=null;
        java.util.Date fechaTrib2012=null;
        java.util.Date finTrib2012=null;
        java.util.Date fechaEmisionD=null;

        java.util.Date fechaActual2012 = new java.util.Date(); */       

        java.util.Date fechaIniciotribu2014=null;
        java.util.Date fechaTrib2014=null;
        java.util.Date finTrib2014=null;
        java.util.Date fechaEmisionD=null;

        java.util.Date fechaActual2014 = new java.util.Date();        

        
        
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        
        /*
        fechaActual2012.setHours(0);
        fechaActual2012.setMinutes(0);
        fechaActual2012.setSeconds(0);
        String fechaActualStr = formatoDelTexto.format(fechaActual2012);
        fechaActual2012=formatoDelTexto.parse(fechaActualStr);

        fechaTrib2012=formatoDelTexto.parse(StrfechaTrib2012);
        finTrib2012=formatoDelTexto.parse(StrfinTrib2012);
        fechaIniciotribu2012=formatoDelTexto.parse(StrFechaInicioTribu2012);
        */

        fechaActual2014.setHours(0);
        fechaActual2014.setMinutes(0);
        fechaActual2014.setSeconds(0);
        String fechaActualStr = formatoDelTexto.format(fechaActual2014);
        fechaActual2014=formatoDelTexto.parse(fechaActualStr);

        /*
        fechaTrib2012=formatoDelTexto.parse(StrfechaTrib2012);
        finTrib2012=formatoDelTexto.parse(StrfinTrib2012);
        fechaIniciotribu2012=formatoDelTexto.parse(StrFechaInicioTribu2012);
       */

        fechaTrib2014=formatoDelTexto.parse(StrfechaTrib2014);
        finTrib2014=formatoDelTexto.parse(StrfinTrib2014);
        fechaIniciotribu2014=formatoDelTexto.parse(StrFechaInicioTribu2014);
        
        
        int verificaRut = 0;

        try {

             CallableStatement callFecha = this.connection.prepareCall("{? = call Configurarpropuesta.RetornaFechas}");
             callFecha.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

             callFecha.execute();

             ResultSet rs = (ResultSet) callFecha.getObject(1);

             while(rs.next())
             {
               fechaActual=rs.getString(1);
               fechaInicio=rs.getString(2);
               fechaVigencia=rs.getString(3);
               fechaCompara=rs.getString(4);
               fechaComparaF30=rs.getString(5);

             }
             callFecha.close();

            /* ReglaNegocio CCC y Cata Bustos 23-04-2012 */
            if  (tipoFormulariox==37){
              //realizo validacion para casos especiales
              CallableStatement call;
                            int existeDeuda = 0;

                            try {
                                call = this.connection.prepareCall("{? = call convenio_especial.recupera_Ley20583(?,?,?,?,?)}");
                                call.registerOutParameter(1, java.sql.Types.INTEGER);
                                call.setLong(2, deuda.getTipoContribuyente());
                                call.setLong(3, deuda.getRutRol());
                                call.setLong(4, deuda.getTipoFormulario());
                                call.setLong(5, deuda.getFolio());
                                call.setDate(6, deuda.getFechaVencimiento());
                                call.execute();

                                existeDeuda = call.getInt(1);
                                call.close();

                              } catch (SQLException eSQL) {
                                throw new SQLException ("Error SQL convenio_especial.recupera_Ley20583" + eSQL.toString());
                              }

                              if (existeDeuda>0) {
                                retorno="Ley 20.583 Concesiones Acuicultura";
                                deuda.setTipoConvenio(TipoConvenio.Ley_20583);
                                deuda.setNumeroCuotas(36); //cantidad cuotas
                                deuda.setPorcentajeCuotaContado(5); //minimo % pago cuota contado
                                return retorno;
                              }

            }

            /* ReglaNegocio CCC y Cata Bustos 18-02-2013 */
             if  (tipoFormulariox==34){
               //realizo validacion para casos especiales

               retorno="Cr�ditos de Estudios Superiores Ley 20.027";
               deuda.setTipoConvenio(TipoConvenio.Ley_20027);
               deuda.setNumeroCuotas(12); //cantidad cuotas
               deuda.setPorcentajeCuotaContado(12); //minimo % pago cuota contado
               deuda.setPosibilidadCondonacion("N");

               return retorno;


             }

            /* ReglaNegocio 1.2.1 */
            if (tipoFormulariox == 21 || tipoFormulariox == 22 || tipoFormulariox == 29 || tipoFormulariox == 30) {
                existe = 1;

                boolean entraRMH = false;

                String sGiroAdicional = null;
                long lCodigoItem = 0;
                String sIncluyeExcluyeItem = null;
                String sExcluyeConvenio = null;
                String sExcluyeCondonacion = null;

                if (!hmGiroAdicionalRMH.isEmpty()){


                    entraRMH = true;
                }

                if (tipoFormulariox == 30) {


                    if ((fechaComparaF30.compareTo(deuda.getFechaVencimiento().toString())>=0)&&(fechaVigencia.compareTo(fechaActual)>=0)&&(fechaInicio.compareTo(fechaActual)<0))
                    {
                      retorno = "Art. 192 Territorial LEY 20.460";
                      deuda.setTipoConvenio(11);
                    }
                    else
                    {
                      retorno = "Ley 20.780 Territorial";
                      deuda.setTipoConvenio(TipoConvenio.Ley_20780);
                    }

                     fechaEmisionD = formatoDelTexto.parse(deuda.getFechaVencimiento().toString());
                     /*if (fechaEmisionD.compareTo(fechaTrib2012)<=0 && fechaActual2012.compareTo(finTrib2012)<=0 && fechaActual2012.compareTo(fechaIniciotribu2012)>=0)
                     {
                      retorno = "Ley Territorial Reforma Tributaria 2012";
                      deuda.setTipoConvenio(13);
                     }*/

                     if (fechaEmisionD.compareTo(fechaTrib2014)<=0 && fechaActual2014.compareTo(finTrib2014)<=0 && fechaActual2014.compareTo(fechaIniciotribu2014)>=0)
                     {
                      retorno = "Ley Territorial Reforma Tributaria 2014";
                      deuda.setTipoConvenio(15);//id:bd tipo_convenios
                     }                     
                     

                } else {

                  CallableStatement call;
                  Date fecha = null;

                  try {


                      call = this.connection.prepareCall("{? = call Configurarpropuesta.verifica_215(?,?,?,?,?,?)}");
                      call.registerOutParameter(1, java.sql.Types.DATE);
                      call.setLong(2, deuda.getTipoContribuyente());
                      call.setLong(3, deuda.getRutRol());
                      call.setLong(4, deuda.getTipoFormulario());
                      call.setLong(5, deuda.getFolio());
                      call.setDate(6, deuda.getFechaVencimiento());
                      call.setString(7,deuda.getOrigen());
                      call.execute();

                      fecha = new Date(((Date) call.getDate(1)).getTime());

                      deuda.setFechaEmision(fecha);
                      call.close();

                    } catch (SQLException eSQL) {
                      throw new SQLException ("Error SQL ConfiguracionPropuesta.FechaEmisionDeuda" + eSQL.toString());
                    }





                  if (fechaCompara.compareTo(deuda.getFechaEmision().toString())>=0 && (fechaVigencia.compareTo(fechaActual)>=0)&&(fechaInicio.compareTo(fechaActual)<0))
                  {

                    try {

                           CallableStatement callRut = this.connection.prepareCall("{? = call Configurarpropuesta.VerificaRut36(?)}");
                           callRut.registerOutParameter(1, java.sql.Types.INTEGER);

                           callRut.setLong(2, deuda.getRutRol());
                           callRut.execute();

                           verificaRut=callRut.getInt(1);
                           callRut.close();

                         } catch (SQLException eSQL) {
                           throw new SQLException ("Error SQL ConfiguracionPropuesta.FechaEmisionDeuda" + eSQL.toString());
                         }

                       if (verificaRut>0)
                       {
                         retorno = "Art. 192 Fiscal LEY 20.460";
                         deuda.setTipoConvenio(11);
                       }
                       else
                       {
                           retorno = "Ley 20.780 Fiscal";
                           deuda.setTipoConvenio(TipoConvenio.Ley_20780);
                       }
                  }
                  else
                  {
                    retorno = "Ley 20.780 Fiscal";
                    deuda.setTipoConvenio(TipoConvenio.Ley_20780);
                  }



                }

                if (deuda.origenEsCut()){ // modificacion 15-05-2003
                    boolean ejecutaSQLhmGiroAdicional = false;
                    String giroAdicional = null;

                    if (hmGiroAdicional.containsKey(new Integer(deuda.getTipoFormulario())) ){
                        giroAdicional = (String) hmGiroAdicional.get(new Integer(deuda.getTipoFormulario()));
                    } else {
                        giroAdicional = "A";
                    }

                    if (giroAdicional.equals("A")){
                        existe = 0;
                    } else {
                        pstmtVMCR.setLong(2,deuda.getTipoContribuyente());
                        pstmtVMCR.setLong(3,deuda.getRutRol());
                        pstmtVMCR.setLong(4,deuda.getTipoFormulario());
                        pstmtVMCR.setLong(5,deuda.getFolio());
                        pstmtVMCR.setDate(6,deuda.getFechaVencimiento());

                        int cantidad = 0;

                        pstmtVMCR.execute();
                        cantidad=pstmtVMCR.getInt(1);

                        /*ResultSet rsVMCR = pstmtVMCR.executeQuery();
                        if (rsVMCR.next()){
                            cantidad = Integer.parseInt(rsVMCR.getString(1));
                        }*/
                        // pstmtVMCR.close();


                        if (giroAdicional.equals("S") && cantidad > 0){
                            existe = 0;
                        } else {
                            if (giroAdicional.equals("N") && cantidad == 0){
                                existe = 0;
                            } else {
                                existe = 1;
                            }
                        }

                    }

                    if (entraRMH) {

                        Iterator it = hmGiroAdicionalRMH.iterator();
                        int verificaItem = 0;
                        Integer verificaFormulario;
                        while (it.hasNext())
                        {
                          HashMap newStructRMH= (HashMap) it.next();

                          verificaFormulario = (Integer) newStructRMH.get("tipoFormulario");

                          if (tipoFormulariox== verificaFormulario.intValue())
                          {
                            sGiroAdicional = (String) newStructRMH.get("sGiroAdicional");
                            lCodigoItem = ((Long) newStructRMH.get("lCodigoItem")).longValue();
                            sIncluyeExcluyeItem = (String) newStructRMH.get("sIncluyeExcluyeItem");
                            sExcluyeConvenio = (String) newStructRMH.get("sExcluyeConvenio");
                            sExcluyeCondonacion = (String) newStructRMH.get("sExcluyeCondonacion");

                            if (sExcluyeConvenio.equals("S") && sExcluyeCondonacion.equals("S")) {

                              callMI.setLong(2,deuda.getTipoContribuyente());/*Tipo Contribuyente */
                              callMI.setLong(3,deuda.getRutRol());/*Rut Rol */
                              callMI.setLong(4,deuda.getTipoFormulario());/*Formulario */
                              callMI.setLong(5,deuda.getFolio());/*Folio */
                              callMI.setDate(6,deuda.getFechaVencimiento());/*Fecha Vencimiento */
                              callMI.setLong(7, lCodigoItem);
                              callMI.setString(8, sGiroAdicional);
                              callMI.execute();



                              String[] sCantidades = StringExt.split(callMI.getString(1),"*");
                              int cantidad = Integer.parseInt(sCantidades[0]);
                              int iCantidad = Integer.parseInt(sCantidades[1]);

                              if (sGiroAdicional.equals("A")) {
                                  existe = 1;
                              } else {
                                  if (sGiroAdicional.equals("S") && cantidad > 0) {
                                    existe = 1;
                                  } else {
                                      if (sGiroAdicional.equals("N") && cantidad == 0) {
                                          existe = 1;
                                      } else {
                                        existe = 0;
                                      }
                                  }
                              }

                              if (verificaItem==0)
                              {
                                if (lCodigoItem != -1) {
                                    if (sIncluyeExcluyeItem.equals("E") && iCantidad == 0) {
                                        existe = 1;
                                        verificaItem=1;
                                    } else {
                                        if (sIncluyeExcluyeItem.equals("I") && iCantidad > 0) {
                                          existe = 1;
                                          verificaItem=1;
                                        } else {
                                          existe = 0;
                                          verificaItem=0;
                                        }
                                    }
                                }
                              }
                            }
                          }
                      }
                    }

                    if ((existe > 0)&& (this.perfil<5)) {
                        retorno ="No Agrupables (Sin derecho a convenio)";
                    }
                }
            } else {
              if (deuda.origenEsCut()){
               /* if (tipoFormulariox == 6) { se inactiva if que aparta los f6
                    retorno = "CHQ Protestado";
                    deuda.setTipoConvenio(TipoConvenio.CHEQUES_PROTESTADOS);
                } else {*/

                   
	                  try {
	
	                           CallableStatement call;
	                           Date fecha = null;
	
	                           call = this.connection.prepareCall("{? = call Configurarpropuesta.verifica_215(?,?,?,?,?,?)}");
	                           call.registerOutParameter(1, java.sql.Types.DATE);
	                           call.setLong(2, deuda.getTipoContribuyente());
	                           call.setLong(3, deuda.getRutRol());
	                           call.setLong(4, deuda.getTipoFormulario());
	                           call.setLong(5, deuda.getFolio());
	                           call.setDate(6, deuda.getFechaVencimiento());
	                           call.setString(7,deuda.getOrigen());
	                           call.execute();
	
	                           fecha = new Date(((Date) call.getDate(1)).getTime());
	
	                           deuda.setFechaEmision(fecha);
	                           call.close();
	
	                    } catch (SQLException eSQL) {
	                      throw new SQLException ("Error SQL ConfiguracionPropuesta.FechaEmisionDeuda" + eSQL.toString());
	                    }
                        //*se realiza mejora para satelites 25/05/2015*/*/
	                  /*  if(tipoFormulariox==14 || tipoFormulariox==19 || tipoFormulariox==36 || tipoFormulariox==63){
	                    	deuda.setFechaEmision(deuda.getFechaVencimiento());
	                    }*/
                 

                  if (fechaCompara.compareTo(deuda.getFechaEmision().toString())>=0 && (fechaVigencia.compareTo(fechaActual)>=0)&&(fechaInicio.compareTo(fechaActual)<0))

                  {
                    retorno = "Art. 192 Fiscal LEY 20.460";
                    deuda.setTipoConvenio(11);
                  }
                  else
                  {
                    retorno = "Ley 20.780 Fiscal";
                    deuda.setTipoConvenio(TipoConvenio.Ley_20780);

                  }


                    //boolean ejecutaSQLhmGiroAdicional = false;
                    //boolean entraA192 = false;
                    //String giroAdicional = null;

                    //String sGiroAdicional = null;
                    //long lCodigoItem = 0;
                    //String sIncluyeExcluyeItem = null;
                    //String sExcluyeConvenio = null;
                    //String sExcluyeCondonacion = null;

                    /************************************************/
                    /*se inactiva validacion exclusion192, ya que 	
                    lo realiza en exluciones normales 			
                    metodo exclusionesART192Generales  02/03/2016*/			
                    /************************************************/
                    
                   // if (!hmGiroAdicionalA192.isEmpty()){

                   //     entraA192 = true;
                   // }


                  //  if (entraA192) {

                  //       Iterator it = hmGiroAdicionalA192.iterator();
                  //      int verificaItem = 0;
                  //      Integer verificaFormulario;

                  //       while (it.hasNext())
                  //      {

                  //        HashMap structA192= (HashMap) it.next();

                  //        verificaFormulario = (Integer) structA192.get("tipoFormulario");

                  //        if (tipoFormulariox== verificaFormulario.intValue())
                  //        {
                  //            sGiroAdicional = (String) structA192.get("sGiroAdicional");
                  //            lCodigoItem = ((Long) structA192.get("lCodigoItem")).longValue();
                  //            sIncluyeExcluyeItem = (String) structA192.get("sIncluyeExcluyeItem");
                  //            sExcluyeConvenio = (String) structA192.get("sExcluyeConvenio");
                  //            sExcluyeCondonacion = (String) structA192.get("sExcluyeCondonacion");

                  //            if (sExcluyeConvenio.equals("S") && sExcluyeCondonacion.equals("S")) {

                  //              callMI.setLong(2,deuda.getTipoContribuyente());/*Tipo Contribuyente */
                  //              callMI.setLong(3,deuda.getRutRol());/*Rut Rol */
                  //              callMI.setLong(4,deuda.getTipoFormulario());/*Formulario */
                  //              callMI.setLong(5,deuda.getFolio());/*Folio */
                  //              callMI.setDate(6,deuda.getFechaVencimiento());/*Fecha Vencimiento */
                  //              callMI.setLong(7, lCodigoItem);
                  //              callMI.setString(8, sGiroAdicional);
                  //              callMI.execute();

                  //              String[] sCantidades = StringExt.split(callMI.getString(1),"*");
                  //              int cantidad = Integer.parseInt(sCantidades[0]);
                  //              int iCantidad = Integer.parseInt(sCantidades[1]);

                  //              if (sGiroAdicional.equals("A")) {
                  //                existe = 1;
                  //              } else {
                  //                if (sGiroAdicional.equals("S") && cantidad > 0) {
                  //                  existe = 1;
                  //                } else {
                  //                  if (sGiroAdicional.equals("N") && cantidad == 0) {
                  //                      existe = 1;
                  //                  } else {
                  //                    existe = 0;
                  //                  }
                  //              }
                  //            }

                  //            if (verificaItem==0)
                  //            {
                  //              if (lCodigoItem != -1) {
                  //                if (sIncluyeExcluyeItem.equals("E") && iCantidad == 0) {
                  //                    existe = 1;
                  //                } else {
                  //                    if (sIncluyeExcluyeItem.equals("I") && iCantidad > 0) {
                  //                      existe = 1;
                  //                    } else {
                  //                      existe = 0;
                  //                    }
                  //                }
                  //              }
                  //            }
                  //        }
                  //      }
                  //  }
                  //}

                    if ((existe > 0)&&(this.perfil<5))  {
                        retorno ="No Agrupables (Sin derecho a convenio)";
                    }
                //}se inacativa if que aparta lod f6
            }
            else
            {
                     retorno = "Ley 20.780 Fiscal";
                    deuda.setTipoConvenio(TipoConvenio.Ley_20780);
                    //obtengo fecha giro para deudas no cut CCC 26-10-2012
                    try {

                      CallableStatement call;
                      Date fechaGiro = null;

                      call = this.connection.prepareCall(
                          "{? = call deudascontribuyente.getfechagironocut(?,?,?,?,?)}");
                      call.registerOutParameter(1, java.sql.Types.DATE);
                      call.setLong(2, deuda.getTipoContribuyente());
                      call.setLong(3, deuda.getRutRol());
                      call.setLong(4, deuda.getTipoFormulario());
                      call.setLong(5, deuda.getFolio());
                      call.setDate(6, deuda.getFechaVencimiento());
                      call.execute();

                      fechaGiro = new Date( ( (Date) call.getDate(1)).getTime());

                      deuda.setFechaEmision(fechaGiro);
                      call.close();

                    }
                    catch (SQLException eSQL) {
                      throw new SQLException(
                          "Error SQL deudascontribuyente.getfechagironocut" +
                          eSQL.toString());
                    }

            }
            }

            /*Cambio CCC y CBT nuevo reforma tributaria 2012*/
            if (tipoFormulariox!=30){
              fechaEmisionD = formatoDelTexto.parse(deuda.getFechaEmision().toString());
              //System.out.println("fechaEmisionD---> " + fechaEmisionD);
              //System.out.println("fechaActual2012---> " + fechaActual2012);
              //System.out.println("fechaTrib2012---> " + fechaTrib2012);
              //System.out.println("finTrib2012---> " + finTrib2012);

            /* if (tipoFormulariox!=10 && tipoFormulariox!=15 && tipoFormulariox!=17 && tipoFormulariox!=18 && tipoFormulariox!=22 && tipoFormulariox!= 29
                 && tipoFormulariox!=40 && tipoFormulariox!=41 && tipoFormulariox!=50 && tipoFormulariox!=59 && tipoFormulariox!=61 && tipoFormulariox!=62 && tipoFormulariox!=64) {*/


/*
                if (fechaEmisionD.compareTo(fechaTrib2012)<=0 && fechaActual2012.compareTo(finTrib2012)<=0 && fechaActual2012.compareTo(fechaIniciotribu2012)>=0){
                  retorno="Ley Fiscal Reforma Tributaria 2012";
                  deuda.setTipoConvenio(13);//reforma tributaria 2012
                  deuda.setNumeroCuotas(36); //cantidad cuotas
                  deuda.setPorcentajeCuotaContado(3); //minimo % pago cuota contado
                }*/
                
                if (fechaEmisionD.compareTo(fechaTrib2014)<=0 && fechaActual2014.compareTo(finTrib2014)<=0 && fechaActual2014.compareTo(fechaIniciotribu2014)>=0){
                    retorno="Ley Fiscal Reforma Tributaria 2014";
                    deuda.setTipoConvenio(15);//reforma tributaria 2014
                    deuda.setNumeroCuotas(36); //cantidad cuotas
                    deuda.setPorcentajeCuotaContado(3); //minimo % pago cuota contado
                  }
                                  
                
               }
             //}

            return retorno;
        } catch(Exception e) {
        	//e.printStackTrace();
            throw new Exception("Error ejecutando el SQL -"+ retorno + " - " + iteracion + "::" + e.toString());
        }
    }
    /*Actualiza el total neto de las deudas seleccionadas y liquidadas*/
    public void setTotal(Vector deudasSeleccionadas)
    {

        this.total = 0;
        Iterator it = deudasSeleccionadas.iterator();

        while (it.hasNext()){

          DeudaWeb deudaSeleccionada = (DeudaWeb) it.next();

          if (deudaSeleccionada.estaSeleccionado()){
            if (deudaSeleccionada.getOrigen().equals("C")&& this.perfil>0)
            {
              if (deudaSeleccionada.estaLiquidada())
              {
                  this.total += deudaSeleccionada.getMonto();
              }
            }
            else
            {
                this.total += deudaSeleccionada.getMonto();
            }
          }
        }

    }

    public void setOrigen(String origen)
    {
      this.origen=origen;
    }

    public String getOrigen()
    {
      return this.origen;
    }
    
    /*
    public DeudaWeb liquidaSatelite(DeudaWeb deuda) throws Exception{
    	//System.out.println("ingreso servicio");
        String  vencimiento = DateExt.format("AAAAMMDD",deuda.getFechaVencimiento());
        String  periodo = DateExt.format("AAAAMM",deuda.getFechaVencimiento());
     try{	
        Context ctx = new InitialContext();
        Object home = ctx.lookup("cobranzas.corporativo.servicioLiquidacion.ServicioLiquidacion");
        ServicioLiquidacionHome liquidadorHome = (ServicioLiquidacionHome) PortableRemoteObject.narrow(home,ServicioLiquidacionHome.class);
        ServicioLiquidacion liquida = liquidadorHome.create();
        
        //nuevo servicio que integra deudas satelite liquida CBS y CCC 09102013

        
        liquida.liquidaConsultaDeudasAix((BigDecimal)new BigDecimal(deuda.getTipoContribuyente()),
                                         (BigDecimal)new BigDecimal(deuda.getRutRol()),
                                         (BigDecimal)new BigDecimal(deuda.getTipoFormulario()),
                                         (BigDecimal)new BigDecimal(deuda.getFolio()),
                                         vencimiento,
                                         periodo);
        
        //System.out.println("ingreso liquido");
        
        if ( Integer.parseInt(liquida.getValorRet()) == 0){
         
        	deuda.setReajustes((liquida.getValorCod92()==null)?0:StringExt.stringALong(liquida.getValorCod92()).longValue());
        	deuda.setIntereses((liquida.getValorCod93()==null)?0:StringExt.stringALong(liquida.getValorCod93()).longValue());
        	deuda.setMultas((liquida.getValorCod94()==null)?0:StringExt.stringALong(liquida.getValorCod94()).longValue());
        	deuda.setMonto((liquida.getValorCod91()==null)?0:StringExt.stringALong(liquida.getValorCod91()).longValue());
            deuda.setCod92Liq((liquida.getValorCod92()==null)?new Long(0):StringExt.stringALong(liquida.getValorCod92()));
            //System.out.println("ingreso obtengo");
        }else{
        	
        	//System.out.println("verr-------> "+ liquida.getCadena());
        	//no es liquidable
        	//System.out.println("no es liquidable---->");
        	deuda.setCod92Liq(new Long(-1));
        	//throw new RemoteException("33333333333");
        	//throw new EJBException("33333333333");
        	//return deuda;
        }
		}catch(SQLException e) {
        	//ystem.out.println("voy poraqui--2------->");
           
        }catch (EJBException e) {
    	//	System.out.println("ejb exxxxxx----------------<");
    		throw new Exception("33333333333");
    		//e.printStackTrace();
    		//System.out.println("lo mismo--r-->");
    	//return deuda;
        }	catch (RemoteException e) {
			System.out.println("lo mismo--r-->");
			
			 //throw new EJBException("Error en servicio Liquidadoe VMS");
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}catch (Exception e) {
			System.out.println("lo mismo--r-->");
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("lo mismo--r-->");
		}	
		
		return deuda;
       // System.out.print("llego------>");
      // return deuda;
    }*/
}
