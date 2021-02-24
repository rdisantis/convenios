/**
 * 
 */
package condonacion;


import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import java.rmi.*;
import javax.ejb.*;
import java.util.*;
import java.math.*;
//import javax.sql.*;
import java.text.*;
import javax.rmi.*;
import javax.naming.*;
import java.io.*;
import lecturaconfig.*;
import cl.obcom.eculink.*;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="Condonacion"	
 *           description="An EJB named Condonacion"
 *           display-name="Condonacion"
 *           jndi-name="cobranzas/corporativo/servicioCondonacion/Condonacion"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * @ejb.transaction
 *  type="Required" 
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */

public class CondonacionBean implements javax.ejb.SessionBean {
	  SessionContext sessionContext;
	  private String cadena = null;
	  private String valorRet = null;
	  private transient ClientLink myLink = null;

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
	public void setSessionContext(SessionContext sessionContext) throws EJBException,
			RemoteException {
		 this.sessionContext = sessionContext;

	}

	/**
	 * 
	 */
	public CondonacionBean() {
		// TODO Auto-generated constructor stub
	}
	
	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
  public void setCadena(String valor) {
    this.cadena = valor;
  }
  
	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
  public String getCadena() {
    return cadena;
  }
  
	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
  public void setValorRet(String valor) {
    this.valorRet = valor;
  }
  
	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
  public String getValorRet() {
    return valorRet;
  }
		/* public void condona(String tipCont, String rutRol, String form, String folio, String venc, String res, String fechaVig, String cond, String codigo ) throws Exception {

		    String reply1 = null;
		    String reply2 = null;
		    String entrada = tipCont + rutRol + form+folio + venc + res + fechaVig + cond + codigo;

		    String respuesta = "";
		    //String[] cmd = {"/usr/users/desa_gco/ejecutaCondona.sh",entrada};
		    String equ = "";

		    //************************agregado 14/04/2004
		    try{
		        lecturaconfig.ObtenerServicios configuracion = new lecturaconfig.ObtenerServicios();
		        equ = (String)configuracion.hashSal.get("EQU_DIR");

		    }
		    catch(Exception e){
		      e.printStackTrace();
		    }

		    //*************************


		    String[] cmd = {equ+"ejecutaCondona.sh",entrada};
		    Process ls_proc = null;


		    try{
		          String ls_str;
		          ls_proc = Runtime.getRuntime().exec(cmd);
		          DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
		          try {
		            while ((ls_str = ls_in.readLine()) != null) {
		                respuesta = respuesta + ls_str;
		            }
		            ls_in.close();
		          } catch (IOException e) {
		            ls_in.close();
		            ls_proc.destroy();

		          }
		          ls_in.close();
		        }
		          catch (IOException e1) {
		              System.err.println(e1);
		              ls_proc.destroy();

		          }
		          finally{
		              ls_proc.destroy();
		          }


		        this.cadena = respuesta;
		        this.valorRet = respuesta.substring(respuesta.indexOf("TESO-T16") + 20, respuesta.indexOf("TESO-T16") + 22) ;
		  }
		*/
	/** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="both"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 */
		 public void condona(String tipCont, String rutRol, String form, String folio, String venc, String res, String fechaVig, String cond, String codigo ) throws Exception {

		   BigDecimal vTipCont=new BigDecimal(tipCont);
		   BigDecimal vRutRol=new BigDecimal(rutRol);
		   BigDecimal vForm=new BigDecimal(form);
		   BigDecimal vFolio=new BigDecimal(folio);
		   BigDecimal vVenc=new BigDecimal(venc);
		   BigDecimal vRes=new BigDecimal(res);
		   BigDecimal vFechaVig=new BigDecimal(fechaVig);
		   BigDecimal vCond=new BigDecimal(cond);
		   BigDecimal vCodigo=new BigDecimal(codigo);

		   this.condonacion(vTipCont,vRutRol,vForm,vFolio,vVenc,vRes,vFechaVig,vCond,vCodigo);


		  }

			/** 
		 *
		 * <!-- begin-xdoclet-definition --> 
		 * @ejb.interface-method view-type="both"
		 * <!-- end-xdoclet-definition --> 
		 * @generated
		 */
		  public void reintentoCondona(String tipCont, String rutRol, String form, String folio, String venc, String res, String fechaVig, String cond, String codigo, String FechaInicio ) throws Exception {

		   String reply1 = null;
		   String reply2 = null;
		   String entrada = tipCont + rutRol + form+folio + venc + res + fechaVig + cond + codigo+FechaInicio;
		   //System.out.println("entrada reintento:"+entrada);
		   String respuesta = "";
		   //String[] cmd = {"/usr/users/desa_gco/ejecutaCondona.sh",entrada};
		   String equ = "";

		   //************************agregado 14/04/2004
		   try{
		       lecturaconfig.ObtenerServicios configuracion = new lecturaconfig.ObtenerServicios();
		       equ = (String)configuracion.hashSal.get("EQU_DIR");

		   }
		   catch(Exception e){
		     e.printStackTrace();
		   }

		   //*************************


		   String[] cmd = {equ+"ejecutaReintCondona.sh",entrada};
		   Process ls_proc = null;


		   try{
		         String ls_str;
		         ls_proc = Runtime.getRuntime().exec(cmd);
		         DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
		         try {
		           while ((ls_str = ls_in.readLine()) != null) {
		               respuesta = respuesta + ls_str;
		           }
		           ls_in.close();
		         } catch (IOException e) {
		           ls_in.close();
		           ls_proc.destroy();

		         }
		         ls_in.close();
		       }
		         catch (IOException e1) {
		             System.err.println(e1);
		             ls_proc.destroy();

		         }
		         finally{
		             ls_proc.destroy();
		         }

		       //System.out.println("Respuesta reintento:"+respuesta);

		       this.cadena = respuesta;
		       this.valorRet = respuesta.substring(respuesta.indexOf("TESO-T16") + 20, respuesta.indexOf("TESO-T16") + 22) ;
		 }
		  
			/** 
			 *
			 * <!-- begin-xdoclet-definition --> 
			 * @ejb.interface-method view-type="both"
			 * <!-- end-xdoclet-definition --> 
			 * @generated
			 */		  
		 public void condonacion
		            (BigDecimal tipCont,
		             BigDecimal rutRol,
		             BigDecimal form,
		             BigDecimal folio,
		             BigDecimal venc,
		             BigDecimal res,
		             BigDecimal fechaVig,
		             BigDecimal cond,
		             BigDecimal codigo) throws Exception{
		        ClientSlot slot = null;

		        int codigoRetorno = -1;
		        BigDecimal rutIra = null;
		        String rutIraDv = null;

		        try {
		            slot = getLinkSlot();


		            Message mensajeIn = new Message();

		            mensajeIn.setLayout(myLink.getLayout("TESOT16IN"));
		            mensajeIn.setString("NOMBRESERV", "TESOT16");
		            mensajeIn.setNumber("SINCROSERV", new BigDecimal(0));
		            mensajeIn.setNumber("TIPCONT", tipCont);
		            mensajeIn.setNumber("RUT_ROL", rutRol);
		            mensajeIn.setNumber("NUMFORM", form);
		            mensajeIn.setNumber("FOLIO",folio);
		            mensajeIn.setNumber("PERIODO", venc);
		            mensajeIn.setNumber("RESOLUCION",res);
		            mensajeIn.setNumber("VIGENCIA", fechaVig);
		            mensajeIn.setNumber("CONDONACION",cond);
		            mensajeIn.setNumber("CODCND",codigo);

		            //writeConsole(mensajeIn.getData());


		            Message mensajeOut = slot.sendTransaction("TESOEC.TESOT16",
		                    mensajeIn, false, false, 10000);

		             mensajeOut.setLayout(myLink.getLayout("TESOT16OUT"));
		             System.out.println(mensajeOut.getLayout().getName());

		            if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
		                throw new Exception(mensajeOut.getData());
		            } else {



		              codigoRetorno = mensajeOut.getNumber("RCODE").intValue();

		                /*switch (codigoRetorno) {
		                case 20:
		                    error = errorCode20;
		                    break;

		                case 23:
		                    error = errorCode23AR;
		                    break;

		                default:
		                    error = errorCodeDefault + " (."
		                            + Integer.toString(codigoRetorno) + ")";
		                    break;
		                }*/
		                //resultPagoArVax.setResultCode(BigDecimal$ONE);
		                //resultPagoArVax.setResultMessage(errorPagoARVax + error);
		            } /*else {
		                resultPagoArVax.setResultCode(BigDecimal$ZERO);

		                if (mensajeOut.getString("MSJ_RET")!= null)
		                    resultPagoArVax.setResultMessage(mensajeOut.getString("MSJ_RET"));
		                else
		                    resultPagoArVax.setResultMessage(mensajeOut.getString("OK"));
		            }*/
		        } catch (Exception e) {
		          this.valorRet="1";
		          this.cadena="     1";
		          throw new Exception("Error al mandar condonaci�n:"+e);
		          //System.out.println("Error condona:"+e.toString());
		            /*resultPagoArVax.setResultCode(ProcesaTrnSafResult.evaluateTrnSafException(e));
		            resultPagoArVax.setResultMessage(
		                    formatException(e, errorPagoARVax, true, 0));*/
		        } finally {
		            if (slot != null) {
		                slot.release();
		            }
		        }
		        this.valorRet=String.valueOf(codigoRetorno);

		        if (codigoRetorno == 0)
		        {
		             this.cadena="      ";
		        }
		        else
		        {
		            this.cadena="    "+this.valorRet;
		        }
		        //return resultPagoArVax;
		    }

		    /**
		     * Returns a new ClientSlot instance using our ClientLink. If we dont have
		     * a ClientLink, a new one will be created. If we have a ClientLink that has
		     * been killed by the System Operator using the "kill client" command, then
		     * a new ClientLink will also be created using the property values provided
		     * in the "CajaSrvEculink.properties" file.
		     */
		    protected ClientSlot getLinkSlot
		            () throws Exception {
		        if (myLink == null || !myLink.isAlive()) {
		            Properties props = ClientLink.loadProperties(
		                    "convEcuLink.properties");
		            String ecuIpPort = props.getProperty("eculink.ipport");
		            String clientSuffix = props.getProperty("client.suffix");
		            String weblogicName = System.getProperty("weblogic.Name");
		            String clientName = weblogicName + clientSuffix;
		            String layoutsURL = props.getProperty("client.layoutsurl");

		            myLink = ClientLink.create(clientName, ecuIpPort, layoutsURL);
		        }
		        return myLink.newSlot();
		    }
	
}