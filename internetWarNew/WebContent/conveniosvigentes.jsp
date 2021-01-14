<%@ page import="java.io.*,java.util.*,java.rmi.*,javax.rmi.*"%>
<%@ page import="javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.persona.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.contribuyenteinternet.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.sesionconvenios.*"%>
<%@ page import="cl.tesoreria.admin.ejb.session.*"%>
<%@ page import="cl.tesoreria.busnmodel.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.*"%>
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

String IdPersonas="";

if (rastreoSesionConvenios == null){
	%>
	<script>
	  alert("Termin\u00f3 su sesi\u00f3n debe logearse nuevamente");
	window.open("menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>","_self")
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


    Context ctx = new InitialContext(new ObtenerServicios().env);


   Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.entity.persona");
   personaHome contribuyenteHome = (personaHome) PortableRemoteObject.narrow(home,personaHome.class);
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


contribuyenteInternet VSessionBean=null;

if (rastreoSesionConvenios != null){
	Context ctx = new InitialContext(new ObtenerServicios().env);
	Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.session.stateful.contribuyenteInternet");
	contribuyenteInternetHome ConveniosVigentesHome = (contribuyenteInternetHome) PortableRemoteObject.narrow(home,contribuyenteInternetHome.class);
	VSessionBean=ConveniosVigentesHome.create();
}
%>

<html>
<head>
<title>Convenios vigentes</title>
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"  SRC="Funciones.inc"></SCRIPT>
<meta http-equiv="PRAGMA" content="NO-CACHE">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="EstiloPagina.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000" vlink="#0000FF" alink="#0000FF">
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

 tituloDeLaPagina('CONVENIOS VIGENTES');

//-->
</script>
<br>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="30">
    <td><p align="justify">
    A continuaci&oacute;n se visualizan todos los convenios vigentes que usted tiene con el Tesorer&iacute;a General de la Rep&uacute;blica.
    </p></td>
  </tr>
</table><br>

<table width="90%" border="1" align="center"   cellspacing="0" cellpadding="0" class="ftotabladatos" bordercolorlight="#006699" bordercolordark="#CCECF0">
	<tr class="fondo2 tituloColumna">
	<td width="10%"  align="center">N&deg;</td>
	<td width="15%"  align="center">N&deg; Resoluci&oacute;n</td>
	<td width="15%"  align="center">Fecha Resoluci&oacute;n</td>
	<td width="15%"  align="center">Tipo Pago</td>
	<td width="10%"  align="center">N&deg; Cuotas</td>
	<td width="15%"  align="center">N&deg; Deudas</td>
	<td width="20%"  align="center">Saldo Neto Actual($)</td>
	</tr>

 <%

 if (VSessionBean!=null && !IdPersonas.equals("")){
  int contador=0;
  Collection ConvDeudor;
  ConvDeudor =(Collection) VSessionBean.getConveniosDeudor(IdPersonas);

if (!ConvDeudor.isEmpty())
{

  Iterator it = ConvDeudor.iterator();
  while (it.hasNext())
  {
  HashMap x = (HashMap) it.next();

  contador=contador+1;
  String SaldoS3 = (String) x.get("SALDO");
   SaldoS3 = SaldoS3.trim();
   SaldoS3 = SaldoS3.replace(',','.');
    %>

      <tr >
      <td width="10%">
      <div align="center"><a href="detalle_convenio.jsp?CodPropuesta=<%=x.get("COD_PROPUESTA")%>&NombrePag=conveniosvigentes.jsp&RUT=<%=RutContribuyente%>&DV=<%=DV %>" target="_self"><%=contador%></a></div>
      </td>
      <td width="15%"  align="center"><%=x.get("NUM_RESOLUCION")%>&nbsp;</td>
      <td width="15%"  align="center"><%=x.get("FECHA_RESOLUCION")%>&nbsp;</td>
      <td width="15%"  ><%=x.get("TIPO_PAGO")%>&nbsp;</td>
      <td width="10%"  align="right"><%=x.get("NUM_CUOTAS")%>&nbsp;</td>
      <td width="15%"  align="right"><%=x.get("NUMERO_DEUDAS")%>&nbsp;</td>
      <td width="20%"  align="right"><%=SaldoS3%>&nbsp;</td>
      </tr>

    <%

  }//fin while
}//Fin if
else
{
%>
<tr><td colspan="7" align="center">El contribuyente no posee convenios con Tesorer&iacute;a</td>
</tr>
<%
}
}
  %>
</table>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="30">
    <td><p align="justify">
    Para ver el detalle de los convenios, presione el n&uacute;mero deseado, ubicado en la primera columna(Izquierda)
    del recuadro.
    </p></td>
  </tr>
</table>

<br>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center">
         <input type="button" name="btnSalir" value="Salir" class="boton2" onclick="window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV %>','_self')">
    </td>
  </tr>
</table>
</body>
</html>



