<%@ page import="java.io.*,java.util.*,java.rmi.*,javax.rmi.*"%>
<%@ page import="javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.entity.persona.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.contribuyenteinternet.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.sesionconvenios.*"%>
<%@ page import="cl.decalink.tgr.convenios.*"%>
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

String IdPersonas="";

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


Context ctx = new InitialContext(new ObtenerServicios().env);

Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.session.stateful.contribuyenteInternet");
contribuyenteInternetHome ConveniosVigentesHome = (contribuyenteInternetHome) PortableRemoteObject.narrow(home,contribuyenteInternetHome.class);
contribuyenteInternet VSessionBean=null;

VSessionBean=ConveniosVigentesHome.create();


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

 tituloDeLaPagina('CONVENIOS CADUCADOS');

//-->
</script>


<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="30">
  </tr>
  <tr height="50">
    <td><p align="justify">
 	El siguiente es un listado de todos sus convenios caducados por Tesorer&iacute;a. En la &uacute;ltima columna, a su derecha,
 	aparece un c&oacute;digo y en el recuadro siguiente las causales de caducidad asociadas a ese c&oacute;digo.
    </p></td>
  </tr>
  <tr height="10">
    </tr>
</table>


<table width="90%" border="1" align="center"   cellspacing="0" cellpadding="0" class="ftotabladatos" bordercolorlight="#006699" bordercolordark="#CCECF0">
	<tr class="fondo2 tituloColumna">
	<td width="10%"  align="center">N&deg;</td>
	<td width="15%"  align="center">N&deg; Resoluci&oacute;n</td>
	<td width="15%"  align="center">Fecha Resoluci&oacute;n</td>
	<td width="15%"  align="center">Tipo Pago</td>
	<td width="10%"  align="center">N&deg; Cuotas</td>
	<td width="15%"  align="center">N&deg; Deudas</td>
	<td width="15%"  align="center">Saldo Neto Actual($)</td>
        <td width="5%"  align="center">C&oacute;digo Caducidad</td>
	</tr>

 <%
  int contador=0;
  Collection ConvDeudor;
  ConvDeudor =(Collection) VSessionBean.getConveniosCaducados(IdPersonas);

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
      <td width="10%" align="center">
      <a href="detalle_convenio.jsp?CodPropuesta=<%=x.get("COD_PROPUESTA")%>&NombrePag=convenioscaducados.jsp&RUT=<%=RutContribuyente%>&DV=<%=DV%>" target="_self"><%=contador%></a>
      </td>
      <td width="15%"  align="center"><%=x.get("NUM_RESOLUCION")%>&nbsp;</td>
      <td width="15%"  align="right"><%=x.get("FECHA_RESOLUCION")%>&nbsp;</td>
      <td width="15%"  align="right"><%=x.get("TIPO_PAGO")%>&nbsp;</td>
      <td width="10%"  align="center"><%=x.get("NUM_CUOTAS")%>&nbsp;</td>
      <td width="15%"  align="right"><%=x.get("NUMERO_DEUDAS")%>&nbsp;</td>
      <td width="15%"  align="right"><%=SaldoS3%>&nbsp;</td>
      <td width="5%"  align="center"><%=x.get("MOTIVO_CADUCIDAD")%>&nbsp;</td>
      </tr>

    <%

  }//fin while
}//Fin if
else
{
%>
<tr><td colspan="9" align="center">El contribuyente no presenta convenios caducados</td></tr>
<%
}
  %>



</table>

<br>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="30">
    <td><p align="justify">
	Para ver el detalle de los convenios caducados presione el n&uacute;mero que est&aacute; en la primera columna
	a su izquierda.
    </p></td>
  </tr>
</table>

<br>

<center><font class="EstiloTextoBold"> CAUSALES DE CADUCIDAD </font></center>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="5">
    <td></td>
  </tr>
</table>

<table width="50%" border="1" align="center"   cellspacing="0" cellpadding="0" class="ftotabladatos" bordercolorlight="#006699" bordercolordark="#CCECF0">
	<tr class="fondo2 tituloColumna">
	<td width="10%"  align="center">C&oacute;digo</td>
	<td width="90%"  align="center">Descripci&oacute;n</td>
	</tr>

 <%

  Collection Causales;
  Causales =(Collection) VSessionBean.getCausalesCaducidad();

if (!Causales.isEmpty())
{

  Iterator it = Causales.iterator();
  while (it.hasNext())
  {
  HashMap x = (HashMap) it.next();


    %>

      <tr >
      <td width="10%"  align="center"><%=x.get("CODIGO")%>&nbsp;</td>
      <td width="90%"  align="right"><%=x.get("DESCRIPCION")%>&nbsp;</td>
      </tr>

    <%

  }//fin while
}//Fin if

  %>



</table>
<br>
<table width="90%" border=0 align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center">
         <input type="button" name="btnSalir" value="Salir" class="boton2" onclick="window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>','_self')">
    </td>
  </tr>
</table>
</FORM>
</body>
</html>