<%@ page import="java.io.*,java.util.*,java.rmi.*,javax.rmi.*"%>
<%@ page import="javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.entity.persona.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateless.consultarexclusiones.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.sesionconvenios.*"%>
<%@ page import="cl.tesoreria.admin.ejb.session.*"%>
<%@ page import="cl.tesoreria.busnmodel.*"%>
<%@ page import="lecturaconfig.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.DeudorExclusionAlerta"%>

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



%>


<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"  SRC="Funciones.inc"></SCRIPT>

<html>
<head>
<title>Consultar Exclusiones</title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="EstiloPagina.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" vlink="#0000FF" alink="#0000FF" text="#000000">

<FORM ACTION="consultar_exclusiones.jsp" name="Consultar_Exclusiones" METHOD="post">

<input type="hidden" name="txtEvento">
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
  tituloDeLaPagina('EXCLUSIONES SII');

 //-->
</script>

  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
    <tr height="30">
    </tr>
    <tr height="50">
      <td><p align="justify">
  	El siguiente es un listado de todas sus Exclusiones enviadas por Servicio de Impuestos Internos.
  	El tipo de Exclusi&oacute;n usted la ver&aacute; en la segunda columna del recuadro y el detalle de la misma en "Exclusiones SII".
      </p></td>
    </tr>
    <tr height="20">
    </tr>
  </table>

<table width="90%" border="0" align="center" class="EstiloTextoBold">
  <tr >
    <td colspan="2" height="28">Exclusiones de Convenio y/o Condonaci&oacute;n<br>
    </td>
  </tr>
</table>

<table width="90%" border="1" align="center" cellpadding="0" cellspacing="0"  class="ftotabladatos" bordercolorlight="#006699" bordercolordark="CCECF0" >
  <tr class="fondo2 tituloColumna">
    <td width="8%" >
      <div align="center">N&ordm;</div>
    </td>
    <td width="10%" >
      <div align="center">Tipo Exclusi&oacute;n</div>
    </td>
    <td width="10%" >
      <div align="center">Origen</div>
    </td>
    <td width="12%">
      <div align="center">N&deg; Formulario</div>
    </td>
    <td width="12%">
          <div align="center">N&deg; Folio</div>
    </td>
    <td width="25%">
      <div align="center">Efecto</div>
    </td>
  </tr>
<%
String VCOD_EXCLUSION_DEUDOR;
Long VCOD_TIPO_EXCLUSION;
Long VID_PERSONA;
Long VFOLIO_SII;
java.util.Date VFECHA_VENCIMIENTO;
Long VFOLIO;
Long VTIP_FORM;
Long VRUT_ROL;
Long VTIP_CONT;
Long VCOD_ORIGEN_EXCLUSION;
String VCAUSA = "";
String VEFECTO = "";
String  VTIPO="";
String VDESCRIPCION ="";
String Cod_Tipo_Exclusion="";
int Contador=0;

Long VIdPersonas1= new Long(IdPersonas);
Long VtxtRut1=new Long(RutContribuyente);

try{
      Context ctx = new InitialContext(new ObtenerServicios().env);
      Object home = ctx.lookup("cobranzas.convenios.negocio.ejb.session.stateless.consultarexclusiones");
      consultarexclusionesHome ConveniosDeudorHome = (consultarexclusionesHome) PortableRemoteObject.narrow(home,consultarexclusionesHome.class);
      consultarexclusiones VSessionBean=null;

      VSessionBean=ConveniosDeudorHome.create();
      HashMap ConDeudor =new HashMap();
      ConDeudor.put("IDPERSONA",VIdPersonas1);
      ConDeudor.put("RUT",VtxtRut1);
      ConDeudor.put("Perfil",new Long(0));


      //Collection ConvDeudor =(Collection) VSessionBean.getconsultarExclusionesSII(ConDeudor);
      List listaExclusion =VSessionBean.getExclusionesDeudor(1, VtxtRut1.longValue());

    if (!listaExclusion.isEmpty())
    {
    int contador=0;
    Iterator it = listaExclusion.iterator();
        while (it.hasNext())
        {
        	 DeudorExclusionAlerta x = (DeudorExclusionAlerta) it.next();
           /*HashMap x = (HashMap) it.next();

        VCOD_EXCLUSION_DEUDOR = (String) x.get("COD_EXCLUSION_DEUDOR");
        VCOD_TIPO_EXCLUSION = (Long) x.get("COD_TIPO_EXCLUSION");
        VID_PERSONA = (Long) x.get("ID_PERSONA");
        VFOLIO_SII = (Long) x.get("FOLIO_SII");
        VFECHA_VENCIMIENTO = (java.util.Date) x.get("FECHA_VENCIMIENTO");
        VFOLIO = (Long) x.get("FOLIO");
        VTIP_FORM = (Long) x.get("TIP_FORM");
        VRUT_ROL = (Long) x.get("RUT_ROL");
        VTIP_CONT = (Long) x.get("TIP_CONT");
        VCOD_ORIGEN_EXCLUSION = (Long) x.get("COD_ORIGEN_EXCLUSION");
        VCAUSA = (String) x.get("CAUSA");
        VEFECTO = (String) x.get("EFECTO");
        VTIPO = (String) x.get("TIPO");
        VDESCRIPCION = (String) x.get("DESCRIPCION");
        Cod_Tipo_Exclusion= (String) x.get("Cod_Tipo_Exclusion");*/

        if (x.getOrigenExclusion().equalsIgnoreCase("SII")){

        Contador=Contador+1;
        %>

        <tr >
        <td width="8%"><div align="center"><%=Contador%></div></td>
        <td width="10%" align="center"><%=x.getOrigenExclusion()%></td>
        <td width="10%" align="center"><%=x.getCodigoExclusion()%></td>
        <td width="12%" align="center"><%=x.getTipoFormulario()%></td>
        <td width="12%" align="center"><%=x.getFolio()%></td>
        <td width="25%"><%=x.getEfectoExclusion()%></td>
        </tr>


        <%

        }//termina el if
        }//termina while

    }//termina if
    else
    {
%>
<tr><td colspan="6" align="center">El contribuyente no presenta exclusiones con Servicio de Impuestos Internos</td></tr>
<%
}
}
catch(Exception e)
{
out.println("Fallo :"+e+" "+VIdPersonas1+" "+VtxtRut1);
e.printStackTrace();
}

%>



</table>

<br>
<table align="center" cellSpacing=0 cellPadding=1 width="90%" border=0 class="ftotabladatos">
    <tr height="50">
      <td><p align="justify">
  	Para regular su situaci&oacute;n vaya hasta las oficinas del Servicio de Impuestos Internos m&aacute;s cercana a su domicilio.
      </p></td><br>
    </tr>
  <tr>
    <td><strong>EXCLUSIONES SII</strong></td>
  </tr>
</table>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="5">
    <td></td>
  </tr>
</table>


<table width="90%" border="1" align="center" cellSpacing=0 cellPadding=1  class="ftotabladatos" bordercolorlight="#006699" bordercolordark="CCECF0">
  <tr class="fondo2 tituloColumna">
    <td align="middle" width="7%"><strong>Tipo</strong></td>
    <td align="middle"><strong>Descripci&oacute;n</strong></td>
  </tr>
  <tr>
    <td align="middle">1</td>
    <td>Contribuyentes que no hayan incurrido injustificadamente en las citaciones del Servicio de Impuestos Internos</td>

  </tr>
  <tr>
    <td align="middle">2</td>
    <td>Contribuyente est&aacute; en proceso de reclamaci&oacute;n de la liquidaci&oacute;n o del giro practicado por el SII (giro form. 21)</td>
  </tr>
  <tr>
    <td align="middle">3</td>

    <td>Contribuyente est&aacute; en proceso de reclamaci&oacute;n de la liquidaci&oacute;n o del giro practicado por el SII (giro form. 45)</td>
  </tr>
  <tr>
    <td align="middle">4</td>
    <td>Contribuyente est&aacute; en proceso de reclamaci&oacute;n de la liquidaci&oacute;n o del giro practicado por el SII (afecta al RUT)</td>
  </tr>
    <tr>

    <td align="middle">5</td>
    <td>Contribuyente entraba la fiscalizaci&oacute;n del SII</td>
  </tr>
    <tr>
    <td align="middle">6</td>
    <td>Contribuyente querellado</td>
  </tr>

    <tr>
    <td align="middle">7</td>
    <td>Contribuyente procesado o, en su caso, acusado conforme al C&oacute;digo Procesal Penal o que haya sido sancionado por delitos tributarios, hasta el cumplimiento total de su pena</td>
  </tr>
    <tr>
    <td align="middle">8</td>
    <td>Contribuyente inconcurrente despu&eacute;s de haber sido sancionado conforme al Art. 97 N&deg; 21 del C&oacute;digo tributario</td>

  </tr>
</table>
<br>
<table width="90%" border="0" align="center">
  <tr>
    <td>
      <div align="center">
        <input type="button" name="Imprimir" value="Imprimir" class="boton2" onClick="window.print()">
        <input type="button" name="Submit2" value="Salir" class="boton2" onClick="javascript:window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV %>','_self')">
      </div>
    </td>
  </tr>
</table>


</form>
</body>
</html>