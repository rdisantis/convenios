<%@ page import="java.io.*,java.util.*,java.rmi.*,javax.rmi.*"%>
<%@ page import="javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.sesionconvenios.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.entity.persona.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateless.comprobantepagototal.*"%>
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

if (rastreoSesionConvenios == null){
	%>
	<script>
	  alert("Termin\u00f3 su sesi\u00f3n debe logearse nuevamente");
	  window.open("menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>","_self");
	</script>
	<%
}
else{
    RutContribuyente = rastreoSesionConvenios.getRutContribuyente();

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

}


String VtxtNombre="";
String VtxtEvento="";
String Vdv="";
Integer VIdPersonas = null;

VtxtEvento = request.getParameter("txtEvento");
//RutContribuyente = request.getParameter("txtRut");
VtxtNombre = request.getParameter("txtNombre");

if (VtxtEvento == null)
{
 VtxtEvento ="";
}
if (VtxtNombre == null)
{
 VtxtNombre="";
}
if (RutContribuyente == null)
{
 RutContribuyente="";
}



Context ctx = new InitialContext(new ObtenerServicios().env);



	   Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.entity.persona");
	   personaHome contribuyenteHome = (personaHome) PortableRemoteObject.narrow(home,personaHome.class);
	   Integer rut = new Integer(RutContribuyente);
	   Collection rutsContribuyentes = contribuyenteHome.findByRut(new Integer(RutContribuyente));
	   if (!rutsContribuyentes.isEmpty())
	       {
	       Iterator it = rutsContribuyentes.iterator();
	       while (it.hasNext())
	          {
	          persona element = (persona) it.next();

	          String Nombr=element.getNombres();

	          if (Nombr==null)
	          {
	          	Nombr="";
	          }

	          String AP=element.getApellidoPaterno();

		  if (AP==null)
		  {
			AP="";
  		  }
	          String AM=element.getApellidoMaterno();
	          if (AM==null)
		  {
			AM="";
  		  }

	          VtxtNombre = Nombr+"  "+AP+"  "+AM;
	          VIdPersonas= element.getIdPersona();
	          Vdv= element.getDv();

	          //VIdPersonas = element.getIdPersona();
	          //out.println();

	         }
	       }
	   else
	       {
	       %>
	       <script>
	              alert("No existe rut");
	       </script>
	       <%
               }



%>


<html>
<head>
	<title>Imprimir Cuotas</title>
	<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"  SRC="Funciones.inc"></SCRIPT>
	<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="EstiloPagina.css" type="text/css">
</head>


<body bgcolor="#FFFFFF" text="#000000" vlink="#0000FF" alink="#0000FF">

<FORM ACTION="imprimir_cuotas2.jsp" name="Imprimir_comprobante_resolucion" METHOD="post">
<input type="hidden" name="txtEvento">
<input type="hidden" name="RUT" value="<%=RutContribuyente%>">
<input type="hidden" name="DV" value="<%=DV%>">

<script LANGUAGE="javascript">
<!--
   imagen = new Image()
   function tituloDeLaPagina(titulo) {

    document.write("<table class='tituloTabla' border=1 cellspacing=1 width='100%'>");
    document.write("  <tr>");
    document.write("  <td width=100% align='middle'><strong>"+titulo+"</strong></td>");
    document.write("  </tr>");
    document.write("</table>");
 }


 tituloDeLaPagina('IMPRIMIR CUOTAS');

//-->
</script>
<br>
<table width="90%" border="0" cellspacing="1" cellpadding="1" align="center">
  <tr>
    <td height="29">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" class="EstiloTextoBold">
        <tr >
          <td colspan="2">Convenios Activos y Propuestas Aceptadas<br></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table width="90%" border="1" cellpadding="0" cellspacing="0" align="center" class="ftotabladatos" bordercolorlight="#006699" bordercolordark="CCECF0" >
  <tr class="fondo2 tituloColumna">
    <td width="10%" height="15">
      <div align="center">N&ordm;</div>
    </td>
    <td width="15%" height="15">
      <div align="center">N&ordm; Resoluci&oacute;n</div>
    </td>
    <td width="15%" height="15">
      <div align="center">Fecha Resoluci&oacute;n</div>
    </td>
    <td width="15%" height="15">
      <div align="center">Tipo de Convenio</div>
    </td>
    <td width="15%" height="15">
      <div align="center">N&deg; Cuotas</div>
    </td>


<%

   //out.println("VIdPersonas ="+VIdPersonas);

	String V_COD_PROPUESTA="";
	String V_NUM_RESOLUCION="";
	String V_FECHA_RESOLUCION="";
	String V_DESCRIPCION="";
	String V_NUM_CUOTAS="";


    try{
        int cont=0;
      	comprobantePagoTotal VSessionBean;

        Context ctx2 = new InitialContext(new ObtenerServicios().env);
    	Object home2 = ctx2.lookup("cobranzas.convenios.negocio.ejb.session.stateless.comprobantePagoTotal");
    	comprobantePagoTotalHome DevolucionesHome = (comprobantePagoTotalHome) PortableRemoteObject.narrow(home2,comprobantePagoTotalHome.class);


    	   VSessionBean=DevolucionesHome.create();

    	   HashMap devolucion1 =new HashMap();

           devolucion1.put("VIdPersonas",VIdPersonas);



    	   Collection devolucion =(Collection) VSessionBean.getlistaComprobantePagoTotal(devolucion1);


        		if (!devolucion.isEmpty())
        		{
        			Iterator it=devolucion.iterator();
      			while(it.hasNext())
      			{
      				HashMap x=(HashMap) it.next();


				V_COD_PROPUESTA=(String) x.get("COD_PROPUESTA");
				V_NUM_RESOLUCION=(String) x.get("NUM_RESOLUCION");
				V_FECHA_RESOLUCION=(String) x.get("FECHA_RESOLUCION");
				V_DESCRIPCION=(String) x.get("DESCRIPCION");
				V_NUM_CUOTAS=(String) x.get("NUM_CUOTAS");



  		cont++;


  %>

  <tr>
    <td width="10%">

      <div align="center">
         <%if (V_DESCRIPCION.equalsIgnoreCase("Cr�ditos de Estudios Superiores Ley 20.027")){ %>
	        <a href="imprimir_cuotasLey20027.jsp?VtxtNombre=<%=VtxtNombre%>&VRutContribuyente=<%=RutContribuyente%>&txtNumeroResolucion=<%=V_COD_PROPUESTA%>&txtEvento=RECUPERAR&NombrePagina=imprimircuotas.jsp&RUT=<%=RutContribuyente%>&DV=<%=DV%>"><%=cont%></A>
         <%}else{%>
                <a href="imprimir_cuotas.jsp?VtxtNombre=<%=VtxtNombre%>&VRutContribuyente=<%=RutContribuyente%>&txtNumeroResolucion=<%=V_COD_PROPUESTA%>&txtEvento=RECUPERAR&NombrePagina=imprimircuotas.jsp&RUT=<%=RutContribuyente%>&DV=<%=DV%>"><%=cont%></A>
         <%}%>
       </div>

    </td>
    <td width="15%">
      <div align="CENTER">&nbsp;<%=V_NUM_RESOLUCION%></div>
    </td>
    <td width="15%">
      <div align="CENTER">&nbsp;<%=V_FECHA_RESOLUCION%></div>
    </td>
    <td width="15%" align="CENTER">&nbsp;<%=V_DESCRIPCION%></td>
    <td width="15%" align="CENTER">&nbsp;<%=V_NUM_CUOTAS%></td>


  </tr>
  <%

    			}//Fin While

      		}//Fin if

      	}//Fin Try
      	catch(Exception e)
      	{
      		out.println("Fallo en la obtenci�n de datos "+e);
      	}


  %>
</table>

<br>
<table width="90%" border="0" align="center" class="ftotabladatos">
  <tr >
    <td >Haga "click" sobre el n&uacute;mero ubicado a la izquierda del convenio o propuesta aceptada cuyas cuotas desea imprimir.</td>
  </tr>
</table><br>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>
      <div align="center">
        <input type="button" name="Salir" value="Salir" class="boton2" onClick="javascript:window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV %>','_self')">
      </div>
    </td>
  </tr>
</table>

</form>

</body>
</html>
