<%@ page import="java.io.*,java.util.*,java.util.Date,java.text.NumberFormat,java.rmi.*,javax.rmi.*,javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.entity.persona.*,cl.decalink.tgr.convenios.StringExt"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.generapropuestainternet.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.DeudaWeb"%>
<%@ page import="cl.decalink.tgr.convenios.sesion.Perfil"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.sesionconvenios.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateless.consultarexclusiones.*"%>
<%@ page import="cl.tesoreria.admin.ejb.session.*"%>
<%@ page import="cl.tesoreria.busnmodel.*"%>
<%@ page import="lecturaconfig.*"%>
<%
 SesionConvenios rastreoSesionConvenios = (SesionConvenios) session.getAttribute("rastreoSesionConvenios");
      String   RutContribuyente=request.getParameter("RUT");
      String   DV=request.getParameter("DV");
      Context ctxSesionConvenios = new InitialContext(new ObtenerServicios().env);
      Object homeSesionConvenios = ctxSesionConvenios.lookup("cobranzas.convenios.negocio.ejb.session.stateful.SesionConvenios");
      SesionConveniosHome sesionConveniosHome = (SesionConveniosHome) PortableRemoteObject.narrow(homeSesionConvenios,SesionConveniosHome.class);

      rastreoSesionConvenios = sesionConveniosHome.create();

      //rastreoSesionConvenios.setIdUsuario(new Integer(login.getUsuario()));
      rastreoSesionConvenios.loginInternet();
      rastreoSesionConvenios.setRutContribuyente(RutContribuyente);




Perfil PerfilSession = null;
int Perfil=0;
Integer vIdPersona=new Integer(0);
consultarexclusiones VSessionBeanExclusiones=null;

if (rastreoSesionConvenios == null){
	%>
	<script>
	alert("Termin\u00f3 su sesi\u00f3n debe logearse nuevamente");
	window.open("menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>","_self");
	  </script>
	<%
}
else{
    PerfilSession = (Perfil) rastreoSesionConvenios.getPerfilSesion();
    Perfil = rastreoSesionConvenios.getPerfil();
    RutContribuyente = rastreoSesionConvenios.getRutContribuyente();
    
}

//Control acceso internet 05-07-2004

try{
	      boolean valido = false;

              Context ctx = new InitialContext(new ObtenerServicios().env_portal);
              Object home = ctx.lookup("LoginSession");
              LoginSessionHome loginSessionHome = (LoginSessionHome)PortableRemoteObject.narrow(home,LoginSessionHome.class);
              LoginSession loginSession = loginSessionHome.create();


              DatosAccesoPortalModel datos = new DatosAccesoPortalModel();
              datos.setRut(new Long(RutContribuyente).longValue());


              ResponseVO vo = loginSession.checkAccesoPortal(datos);

              SuscriptorModel model = (SuscriptorModel) vo.getData();

              if (model.getPersona().getDv() != null)
              	 valido = true;

         if (valido==false){
 		valido=true;
        }


    	}catch (Exception e){
      		e.printStackTrace();
    	}


//Prueba control acceso internet 05-07-2004




String EventoEjecutar=request.getParameter("EventoEjecutar");
Collection  deudasContribuyente = null;
int TotalDeudas=0;
boolean posibilidaDeudasConveniosActivos=false;
persona contribuyente = null;

String rutContribuyente = "";
String digContribuyente = "";



if (EventoEjecutar==null){EventoEjecutar="";}
if  (RutContribuyente== null){	RutContribuyente=""; }



GeneraPropuestaInternet sesionGeneraPropuestaInternet = (GeneraPropuestaInternet) session.getValue("sesionGeneraPropuestaInternet");



if (sesionGeneraPropuestaInternet==null){
   //out.println("<br>Paso por aqui");
   Context ctxGeneraPropuesta = new InitialContext(new ObtenerServicios().env);
      Object homeGeneraPropuesta = ctxGeneraPropuesta.lookup("cobranzas.convenios.negocio.ejb.session.stateful.GeneraPropuestaInternet");
      GeneraPropuestaInternetHome  GeneraPropuestaInternetHome = (GeneraPropuestaInternetHome) PortableRemoteObject.narrow(homeGeneraPropuesta,GeneraPropuestaInternetHome.class);
   sesionGeneraPropuestaInternet = GeneraPropuestaInternetHome.create();
   session.putValue("sesionGeneraPropuestaInternet",sesionGeneraPropuestaInternet);

            sesionGeneraPropuestaInternet.setContribuyente(contribuyente);
            sesionGeneraPropuestaInternet.setPerfil(Perfil);
            sesionGeneraPropuestaInternet.setPerfilSesion(PerfilSession);

 }



if  (!RutContribuyente.equals("")){
    try{
          Context ctx = new InitialContext(new ObtenerServicios().env);
          Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.entity.persona");
          personaHome contribuyenteHome = (personaHome) PortableRemoteObject.narrow(home,personaHome.class);
          Collection rutsContribuyentes = contribuyenteHome.findByRut(new Integer(RutContribuyente));


          if (!rutsContribuyentes.isEmpty()){
             Iterator it = rutsContribuyentes.iterator();

             while (it.hasNext()){
                contribuyente = (persona) it.next();
                rutContribuyente = contribuyente.getRut().toString();
                digContribuyente = contribuyente.getDv();
		vIdPersona=contribuyente.getIdPersona();
             }
         }
       } catch (Exception ep){out.println("Mensaje recuperar persona= "+ep);}



}


if (EventoEjecutar.equals("PROPUESTAS"))
{
    try
    {
         if (sesionGeneraPropuestaInternet!=null)
         {

            int verifica = sesionGeneraPropuestaInternet.setSeleccionaDeudasContribuyente();

            if (verifica!=0)
            {
              sesionGeneraPropuestaInternet.setContribuyente(contribuyente);
	      if (verifica==1)
              {
                  %>
	  	  <script>
	  	  alert("Falta informaci\u00f3n o la informaci\u00f3n no es correcta en alguna de las deudas, acerquese a la tesorer\u00eda m\u00e1s cercana");
	  	  // window.open("MisDeudasFiscales1.jsp","_self");
	  	  </script>
	          <%
              }
              else
              {
                  if (verifica==3)
                  {
                  %>
                  <script>
	  	  alert("Alguna de las deudas no ha podido ser liquidada, acerquese a la tesorer\u00eda m\u00e1s cercana");
	  	  // window.open("MisDeudasFiscales1.jsp","_self");
	  	  </script>
                  <%
                  }
                  else
                  {
                   %>
                   <script>
	  	  alert("Falta informaci\u00f3n o la informaci\u00f3n no es correcta en alguna de las deudas, acerquese a la tesorer\u00eda m\u00e1s cercana");
	  	  // window.open("MisDeudasFiscales1.jsp","_self");
	  	  </script>
                   <%
                    }
                  /*else
                  {
                      if (verifica==4)
                      {*/
                      %>
                      <script>
	  	      //alert("El monto neto de las deudas no cumple con los montos m\u00e1ximo o m\u00ednimo seg\u00fan su perfil");
	  	      // window.open("MisDeudasFiscales1.jsp","_self");
	  	      </script>
                      <%
                      //}
                  //}
              }
            }
            else{
//            {response.sendRedirect("listadepropuestafiscales.jsp?RutContribuyente="+RutContribuyente);
              %>
    	      <script>
    	      window.open('listadepropuestafiscales.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>','_self');
    	      </script>
    	      <%
            }

            EventoEjecutar="";
        }

     }
     catch (Exception e){out.println("setSeleccionaDeudasContribuyente = "+e);}

}

if (EventoEjecutar.equals("SALIR")){

    if (sesionGeneraPropuestaInternet != null) {
           sesionGeneraPropuestaInternet = null;
           session.putValue("sesionGeneraPropuestaInternet",null);
    }

   %>
   <script>
    window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>','_self');
   </script>
   <%
}

String RutRol="";

if (sesionGeneraPropuestaInternet != null) {
           RutRol = sesionGeneraPropuestaInternet.getRutRol();
    }


try
{

	//consultarexclusiones VSessionBeanExclusiones=null;

        Context ctxexclusiones = new InitialContext(new ObtenerServicios().env);
	Object home = ctxexclusiones.lookup("cobranzas.convenios.negocio.ejb.session.stateless.consultarexclusiones");
	consultarexclusionesHome ConveniosDeudorHome = (consultarexclusionesHome) PortableRemoteObject.narrow(home,consultarexclusionesHome.class);

	VSessionBeanExclusiones=ConveniosDeudorHome.create();
	HashMap hmParametros= new HashMap();

	hmParametros.put("IDPERSONA",new Long(vIdPersona.longValue()));
	hmParametros.put("RUT",new Long(rutContribuyente));
	hmParametros.put("Perfil",new Long(Perfil));


	//MAN000002653 se deshabilta la validacion fecha 17.03.2020 torellana 
	//int verifica=VSessionBeanExclusiones.getVerificaDemanda(1,new Long(rutContribuyente).longValue());
	int verifica = 0;

	if (verifica>0)
	{
	%>
	<script>
	alert('Usted posee deudas en estado avanzado de cobranza judicial. Para mayor antecedentes dirigase a la oficina de Tesoreria de su juridicci\u00f3n');
	 window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>','_self');
	</script>
	<%
	}
	else{
	//Collection exclusionesSII=VSessionBeanExclusiones.getconsultarExclusionesSII(hmParametros);
	List exclusionesSII=VSessionBeanExclusiones.getExclusionesDeudor(1, new Long(rutContribuyente).longValue());

			if (exclusionesSII!=null){

				if (exclusionesSII.size()>0){
						%>
				<script>
				//alert('Atenci\u00f3n: El rut ingresado presenta exclusiones con el Servicio de Impuesto Internos. Para mayor informaci\u00f3n sobre estas exclusiones, utilice la opci\u00f3n Consulta Exclusi\u00f3n SII que se encuentra en el men\u00fa principal');
				alert('Atenci\u00f3n: El rut ingresado presenta exclusiones. Para mayor informaci\u00f3n sobre estas exclusiones, utilice la opci\u00f3n Consulta Exclusi\u00f3n SII o Tesoreria que se encuentra en el men\u00fa principal');
				</script>
				<%
				}
			 }
      }
   }catch(Exception e){
	%>
	<script>
	alert('Se ha producido un fallo en operaci\u00f3n, intente nuevamente ');
	window.open("cobadmmender01.jsp","_self");
	</script>
	<%
	}


%>

<html>
<head>
	<title>Mis Deudas Fiscales</title>
	<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"  SRC="Funciones.inc"></SCRIPT>
	<meta http-equiv="PRAGMA" content="NO-CACHE">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="EstiloPagina.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000" vlink="#0000FF" alink="#0000FF">

<FORM ACTION="misdeudasfiscales.jsp" name="MisDeudasFiscales" METHOD="post">
<input type="hidden" name="EventoEjecutar" value="<%=EventoEjecutar%>">
<input type="hidden" name="RUT" value="<%=RutContribuyente%>">
<input type="hidden" name="DV" value="<%=DV%>">

<script LANGUAGE="javascript">
<!--
 imagen = new Image()
 function tituloDeLaPagina(titulo) {

  document.write("<table class='tituloTabla' border=0 cellspacing=1 width='100%'>");
  document.write("  <tr>");
  document.write("  <td width=100% align='middle'><strong>"+titulo+"</strong></td>");
  document.write("  </tr>");
  document.write("</table>");
 }

 tituloDeLaPagina('DEUDAS FISCALES');

//-->
</script>
<br>
<%
int noLiq=0;
if  (!RutContribuyente.equals("")){
try{


       if (sesionGeneraPropuestaInternet.getDeudasFiscales().size()==0){
            sesionGeneraPropuestaInternet.setContribuyente(contribuyente);
            sesionGeneraPropuestaInternet.setPerfil(Perfil);
            sesionGeneraPropuestaInternet.setPerfilSesion(PerfilSession);
            deudasContribuyente =(Collection) sesionGeneraPropuestaInternet.getDeudasContribuyenteFiscales("T");
         }
       else{
            deudasContribuyente =(Collection) sesionGeneraPropuestaInternet.getDeudasFiscales();
           }

     posibilidaDeudasConveniosActivos = sesionGeneraPropuestaInternet.posibilidaDeudasConveniosActivos();
   %>

    <table width="50%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
             <tr class="fondo2 tituloColumna" height="30">
             <td align="center">RUT:  <b>  <%=contribuyente.getRut().toString()%> - <%=contribuyente.getDv()%></b></td>
             </tr>
      </table> <br>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="30">
    <td> <p align="justify">
    En esta opci&oacute;n, usted puede crear una Propuesta de Convenio que incluir&aacute; todas
    sus Deudas Fiscales impagas, est&eacute;n o no en cobranza judicial.
    </p></td>
  </tr>
  <tr height="30">
    <td>  <p align="justify">
    El convenio se concretar&aacute; una vez que el pago contado sea cancelado, a trav&eacute;s de pago electr&oacute;nico.
    </p></td>
  </tr>
</table> <br>


   <table width="90%" border="0" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
          <tr  height="40" class="EstiloTextoBold">
          <td>TOTAL DEUDAS MOROSAS CANDIDATAS A CONVENIOS</td>
          </tr>
   </table>
      <table width="90%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">

	   <tr class="fondo2 tituloColumna">
		   <td width="20%"  align="center">N&deg;</td>
		   <td width="20%"  align="center">N&deg; Form.</td>
		   <td width="20%"  align="center">Folio</td>
		   <td width="20%"  align="center">Fecha Vencimiento</td>
		   <td width="20%"  align="center">Saldo Neto ($)</td>
	   </tr>
     		<%
   	    if (deudasContribuyente != null){
	             if (!deudasContribuyente.isEmpty()){

			Iterator itx = deudasContribuyente.iterator();
			int cuenta=0;

		      String grupoAnterior="";

		      TotalDeudas = deudasContribuyente.size();
		      int contador=0;
                      noLiq=0;
                      Collection deudasTmp = new ArrayList();
	    	      while (itx.hasNext()){

	                   DeudaWeb yy = (DeudaWeb) itx.next();
	                   ++contador;

	                   Long xVRutRol = (Long) new Long(yy.getRutRol());
	                   String xVRutRolFormateado = (String) yy.getRutRolFormateado();

	                   Long xVFolio = (Long) new Long(yy.getFolio());
	                   Date xVFechaVencimiento = (Date) yy.getFechaVencimiento();
	                   String xVFechaVencimientoFMT = (String) yy.getFechaVencimientoFMT();

	                   Long xVSaldo = (Long) new Long (yy.getMonto());

	                   Long xVTipoCont = (Long) new Long (yy.getTipoContribuyente());
	                   Long xVTipoForm = (Long) new Long (yy.getTipoFormulario());
	                   String  xOrigen = (String) yy.getOrigen();
	                   String grupo = (String) yy.getGrupo();
	                   String condonacion = (String) yy.getPosibilidadCondonacion();
                           long saldoV = xVSaldo.longValue();
                           String SaldoS = StringExt.separaMiles(saldoV);

                           //cuentos las no liquidables
                           if (yy.getCod92Liq().longValue()<0){
 				noLiq++;
                           }


                           //solo muestro las liquidables
                          if (yy.getCod92Liq().longValue()>=0){
                          deudasTmp.add(yy);
   		%>
   		<tr>
   		  <td width="20%"  align="center"><%=++cuenta%></td>
   		  <td width="20%"  align="right"><%=xVTipoForm%></td>
		  <td width="20%"  align="right"><%=xVFolio%></td>
		  <td width="20%"  align="center"><%=xVFechaVencimientoFMT%></td>
		  <td width="20%"  align="right"><%=SaldoS%></td>
         	  </tr>
		  <%
                          }

		}

              sesionGeneraPropuestaInternet.setDeudasFiscales(deudasTmp);
	}
	else{




	%>
	<tr><td colspan="6" align="center"> Ud. no tiene deudas o no puede realizar Convenio por Internet </td>
	</tr>
	<%
	}
     }
   	    %>

	     </table>
   <%
   }catch (Exception e){out.println("error recuperar deudas ="+e);}
}
%>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <%
   if (noLiq>0){%>
  <tr height="30">
    <td> <p align="justify">
    <font size="2">
     <b>
     Usted posee deudas que no pueden ser consideradas para un convenio a trav&eacute;s de este medio.
     Por favor ac&eacute;rquese a la Tesorer&iacute;a m&aacute;s cercana donde podr&aacute; regularizar esta situaci&oacute;n.</font>
     </b>
     </p></td>
   </tr>
   <%
     }
  %>

</table>


<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center">
       <input type="button" name="btnPropuestas" value="Propuestas" class="boton2" onclick="document.MisDeudasFiscales.EventoEjecutar.value='PROPUESTAS';document.MisDeudasFiscales.submit()">
       <input type="button" name="btnSalir" value="Salir" class="boton2" onclick="document.MisDeudasFiscales.EventoEjecutar.value='SALIR';document.MisDeudasFiscales.submit()">

    </td>
  </tr>
</table>


</FORM>
<script>
if ("<%=TotalDeudas%>"== 0){
   document.MisDeudasFiscales.btnPropuestas.disabled=true;
}
else{
   document.MisDeudasFiscales.btnPropuestas.disabled=false;
}

</script>
</body>
</html>



