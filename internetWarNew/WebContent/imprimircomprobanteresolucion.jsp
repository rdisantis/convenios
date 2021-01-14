<%@ page import="java.io.*,java.util.*,java.rmi.*,javax.rmi.*"%>
<%@ page import="javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.persona.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.comprobanteresolucion.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.sesionconvenios.*"%>
<%@ page import="cl.tesoreria.admin.ejb.session.*"%>
<%@ page import="cl.tesoreria.busnmodel.*"%>
<%@ page import="lecturaconfig.*"%>


<%
SesionConvenios rastreoSesionConvenios = null;
String   RutContribuyente="";
String   DV="";
try
{
      rastreoSesionConvenios = (SesionConvenios) session.getAttribute("rastreoSesionConvenios");
      RutContribuyente=request.getParameter("RUT");
      DV=request.getParameter("DV");
      Context ctxSesionConvenios = new InitialContext(new ObtenerServicios().env);
      Object homeSesionConvenios = ctxSesionConvenios.lookup("cobranzas.convenios.negocio.ejb.session.stateful.SesionConvenios");
      SesionConveniosHome sesionConveniosHome = (SesionConveniosHome) PortableRemoteObject.narrow(homeSesionConvenios,SesionConveniosHome.class);

      rastreoSesionConvenios = sesionConveniosHome.create();

      //rastreoSesionConvenios.setIdUsuario(new Integer(login.getUsuario()));
      rastreoSesionConvenios.loginInternet();
      rastreoSesionConvenios.setRutContribuyente(RutContribuyente);

}
catch(Exception e)
{
      rastreoSesionConvenios=null;

}

String IdPersonas="";

if (rastreoSesionConvenios == null)
{

%>
<script>
	alert("Termin\u00f3 su sesi\u00f3n debe logearse nuevamente");
	window.open("menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>","_self");

</script>
<%
}
else{

	try
        {
		RutContribuyente = rastreoSesionConvenios.getRutContribuyente();
        }
	catch(Exception e)
	{
		RutContribuyente="";
	}
       //Control acceso internet 05-07-2004
       if (!RutContribuyente.equals(""))
       {
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
       		//Prueba control acceso internet 05-07-2004

                Context ctx2 = new InitialContext(new ObtenerServicios().env);

       		Object home2 = ctx2.lookup("cobranzas.convenios.negocio.ejb.entity.persona");
       		personaHome contribuyenteHome = (personaHome) PortableRemoteObject.narrow(home2,personaHome.class);
       		Collection rutsContribuyentes = contribuyenteHome.findByRut(new Integer(RutContribuyente));

		if (!rutsContribuyentes.isEmpty())
           	{
                   Iterator it = rutsContribuyentes.iterator();
                   while (it.hasNext())
    	  	   {
    	  		persona element = (persona) it.next();
    	  		IdPersonas= element.getIdPersona().toString();
                   }

       		}
            }
        catch (Exception e){
      	%>
        <script>
		alert("Termin\u00f3 su sesi\u00f3n debe logearse nuevamente");
		window.open("menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>","_self");

	</script>
	<%

        }

     }
     else
     {
	%>
        <script>
	alert("Termin\u00f3 su sesi\u00f3n debe logearse nuevamente");
	window.open("menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>","_self")
       </script>
       <%
    }

}


%>
<html>
<head>
	<title>Imprimir comprobante resoluci&oacute;n</title>
	<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="EstiloPagina.css" type="text/css">
</head>
<body bgcolor="#FFFFFF" text="#000000" vlink="#0000FF" alink="#0000FF">
<FORM ACTION="imprimir_comprobante_resolucion.jsp" name="Imprimir_comprobante_resolucion" METHOD="post">
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

 tituloDeLaPagina('IMPRIMIR COMPROBANTE DE RESOLUCION');

//-->
</script>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
    <tr height="30">
    </tr>
    <tr height="50">
      <td><p align="justify">
      El siguiente es un listado de todas las Resoluciones que usted ha celebrado con Tesorer&iacute;a.
      Si desea ver y emitir el comprobante de Resoluci&oacute;n presione el tipo de beneficio ubicado en la
      &uacute;ltima columna a la derecha del recuadro.
      </p></td>
    </tr>
    </tr>
        <tr height="20">
    </tr>
</table>


<table width="90%" border="0" cellspacing="1" cellpadding="1" align="center">
  <tr>
    <td height="29">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" class="EstiloTextoBold">
        <tr >
          <td colspan="2">Resoluciones<br>
            <br>
          </td>
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
    <td width="20%" height="15">
      <div align="center">N&ordm; Resoluci&oacute;n</div>
    </td>
    <td width="20%" height="15">
      <div align="center">Fecha Resoluci&oacute;n</div>
    </td>
    <td width="20%" height="15">
      <div align="center">Fecha Activaci&oacute;n</div>
    </td>
    <td width="30%" height="15">
      <div align="center">Tipo de Beneficio</div>
    </td>
  </tr>

<%
 int cont=0;

Integer VIdPersonas= new Integer(0);


if ((IdPersonas!=null) && (!IdPersonas.equals("")))
{
	VIdPersonas= new Integer(IdPersonas);
}
//out.println("VIdPersonas ="+VIdPersonas);

   String V_NUM_RESOLUCION= "";
   String V_FECHA_RESOLUCION= "";
   String V_FECHA_ACTIVACION= "";
   String V_DESCRIPCION= "";
   String V_COD_PROPUESTA= "";
String V_TIPO="";

    try{

      	comprobanteresolucion VSessionBean;
        Context ctx2 = new InitialContext(new ObtenerServicios().env);
    	Object home2 = ctx2.lookup("cobranzas.convenios.negocio.ejb.session.stateless.comprobanteresolucion");
    	comprobanteresolucionHome DevolucionesHome = (comprobanteresolucionHome) PortableRemoteObject.narrow(home2,comprobanteresolucionHome.class);


    	   VSessionBean=DevolucionesHome.create();

    	   HashMap devolucion1 =new HashMap();

           devolucion1.put("VIdPersonas",VIdPersonas);



    	   Collection devolucion =(Collection) VSessionBean.getlistaResoluciones(devolucion1);


        		if (!devolucion.isEmpty())
        		{
        			Iterator it=devolucion.iterator();
      			while(it.hasNext())
      			{
      				HashMap x=(HashMap) it.next();


      				   V_NUM_RESOLUCION= (String) x.get("NUM_RESOLUCION");
    				   V_FECHA_RESOLUCION= (String) x.get("FECHA_RESOLUCION");
    				   V_FECHA_ACTIVACION= (String) x.get("FECHA_ACTIVACION");
    				   V_DESCRIPCION= (String) x.get("DESCRIPCION");
    				   V_COD_PROPUESTA= (String) x.get("COD_PROPUESTA");
                                    V_TIPO=(String) x.get("TIPO");



  		cont++;


  %>

  <tr>
    <td width="10%">
      <div align="center"><%=cont%>

      </div>
    </td>
    <td width="20%">
      <div align="CENTER">&nbsp;<%=V_NUM_RESOLUCION%></div>
    </td>
    <td width="20%">
      <div align="CENTER">&nbsp;<%=V_FECHA_RESOLUCION%></div>
    </td>
    <td width="20%" align="CENTER">&nbsp;<%=V_FECHA_ACTIVACION%></td>
    <td width="30%" align="CENTER">&nbsp;

    <%
    	if (V_DESCRIPCION.compareTo("CONDONACION PAGO CONTADO")==0 ||(V_TIPO.equals("2")))
    	{
    %>

    <a href="comprobante_resolucion_sin_convenio.jsp?V_COD_PROPUESTA=<%=V_COD_PROPUESTA%>&Tipo=<%=V_TIPO%>&RUT=<%=RutContribuyente%>&DV=<%=DV%>"><%=V_DESCRIPCION%></a>


    <%
    	}
    	else
    	{
    %>
    <a href="comprobante_resolucion_con_convenio.jsp?V_COD_PROPUESTA=<%=V_COD_PROPUESTA%>&RUT=<%=RutContribuyente%>&DV=<%=DV%>"><%=V_DESCRIPCION%></a>

    <%
    	}

    %>
    </td>
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
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="20">
    <td><p align="justify">
    </p></td>
  </tr>
</table>
<br>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>
      <div align="center">

        <input type="button" name="Imprimir" value="Imprimir" class="boton2" onClick="window.print()">
        <input type="button" name="Salir" value="Salir" class="boton2" onClick="javascript:window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV %>','_self')">

      </div>
    </td>
  </tr>
</table>

</form>

</body>
</html>
