<%@ page import="java.io.*,java.util.*,java.util.Date,java.text.NumberFormat,java.rmi.*,javax.rmi.*,javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.generapropuestainternet.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.entity.persona.*,cl.decalink.tgr.convenios.StringExt"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.DeudaWeb"%>
<%@ page import="cl.decalink.tgr.convenios.sesion.Perfil"%>
<%@ page import="cl.decalink.tgr.convenios.propuestas.ConvenioInternet"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.sesionconvenios.*"%>
<%@ page import="cl.tesoreria.admin.ejb.session.*"%>
<%@ page import="cl.tesoreria.busnmodel.*"%>
<%@ page import="lecturaconfig.*"%>


<%

response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);

/*-------lee session genera propuesta ------*/

GeneraPropuestaInternet sesionGeneraPropuestaInternet = (GeneraPropuestaInternet) session.getAttribute("sesionGeneraPropuestaInternet");


//------------//
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


Integer IdFuncionario = null;
Integer IdTesoreria = null;
//String RutContribuyente="";

if (rastreoSesionConvenios != null)
   {
   IdFuncionario = rastreoSesionConvenios.getIdFuncionario();
   IdTesoreria = rastreoSesionConvenios.getIdTesoreria();
   RutContribuyente = rastreoSesionConvenios.getRutContribuyente();
}
else
{

if (sesionGeneraPropuestaInternet != null){
   sesionGeneraPropuestaInternet = null;
   session.putValue("sesionGeneraPropuestaInternet",null);
   }
   %>
   <script>
   	alert("Termin\u00f3 su sesi\u00f3n debe logearse nuevamente");
	window.open("menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>","_self");

   </script>
   <%
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

//-----------//



/*------ selecciona las deudas------*/
Collection propuestas = null;

try{
     if (sesionGeneraPropuestaInternet!=null){


	propuestas  =  sesionGeneraPropuestaInternet.getPropuestasConvenio();


         sesionGeneraPropuestaInternet.setNombrePagina("misdeudasfiscales.jsp");

     }
}catch (Exception e){out.println("Lista propuestas= "+e);}



String RutRol="";

if (sesionGeneraPropuestaInternet != null) {
           RutRol = sesionGeneraPropuestaInternet.getRutRol();
    }
%>
<html>
	<head>
	<title>Propuestas de Convenios</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"  SRC="Funciones.inc"></SCRIPT>
	<meta http-equiv="PRAGMA" content="NO-CACHE">
	<link rel="stylesheet" href="EstiloPagina.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000" vlink="#0000FF" alink="#0000FF">

<FORM ACTION="listadepropuesta1.jsp" name="MisDeudasFiscales" METHOD="post">
<input type="hidden" name="RUT" value="<%=RutContribuyente%>">
<input type="hidden" name="DV" value="<%=DV%>">


<script LANGUAGE="javascript">
<!--
 imagen = new Image();
 function tituloDeLaPagina(titulo) {

  document.write("<table class='tituloTabla' border=1 cellspacing=1 width='100%'>");
  document.write("  <tr>");
  document.write("  <td width=100% align='middle'><strong>"+titulo+"</strong></td>");
  document.write("  </tr>");
  document.write("</table>");
 }

 tituloDeLaPagina('PROPUESTAS DE CONVENIOS');

//-->
</script>
<br>
  <table width="50%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
             <tr class="fondo2 tituloColumna" height="30">
             <td align="center">RUT:  <b>  <%=RutRol%></b></td>
             </tr>
      </table> <br>



<table width="90%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
     	          <tr class="fondo2 tituloColumna">
     	          <td width="10%"  align="center">N&deg;</td>
     	          <td width="60%"  align="center">Propuestas</td>
     	          <td width="15%"  align="center">N&deg; Deudas</td>
     	          <td width="15%"  align="center">Suma Saldo Neto($)</td>

     	           </tr>
   <%
   try{

   if (propuestas!=null){
	int validaMontoTotal=0;
	int validaRenta=0;
       String msj=null;


	   if (!propuestas.isEmpty()){

		    Iterator itx = propuestas.iterator();
		    int cuenta=0;
		    while (itx.hasNext()){
			   ConvenioInternet convenio =(ConvenioInternet) itx.next();

                           String NombrePropuesta = (String) convenio.getNombrePropuesta();

                           String vNombrePropuesta = "";

                           if ((convenio.getNombrePropuesta().equals("Art. 192 Fiscal"))||(convenio.getNombrePropuesta().equals("RMH Fiscal")))
                           {
                              vNombrePropuesta = "Deudas Fiscales";
                           }
                           else
                           {
                              vNombrePropuesta = (String) convenio.getNombrePropuesta();
                           }

                           //String NombrePropuesta = (String) convenio.getNombrePropuesta();
			   int NumeroDeudas = convenio.getNumeroDeudas();
			   long SumaSaldoNeto = convenio.getSaldoNetoPropuesta();
                          String Saldo = StringExt.separaMiles(SumaSaldoNeto);
	                %>
			<tr>
                          <%if (convenio.getNombrePropuesta().equals("Cr�ditos de Estudios Superiores Ley 20.027")){

                		validaMontoTotal = rastreoSesionConvenios.validaMontoTotal(new Long(convenio.getSaldoNetoPropuesta()));
                		validaRenta = rastreoSesionConvenios.validaRenta(new Long(rastreoSesionConvenios.getRutContribuyente()));

               			if (validaMontoTotal==-2)
                    			msj="No es posible generar convenio, debido a que no cumple con el valor m�nimo exigido por cuota. El valor corresponde al 10% del valor de la UTM.";
                		if (validaMontoTotal==-1)
                                         msj="Error en logica BD";
                		if (validaRenta==-2)
                   				 msj="No es posible generar convenio, debido a que no se posee informaci�n correspondiente de Rentas necesario para pactar convenio. Para realizar convenio debe dirigirse a cualquier oficina de la Tesorer�a General de la Rep�blica con un Certificado de Cotizaciones Previsionales emitido por su AFP.";
                		if (validaRenta==-1)
                   			 msj="Error en logica BD";

                               %>
                                 <%if (msj==null){%>
 			    		 <td width="10%"  align="center"><A href="presentacionpropuestaLey20027.jsp?NombrePropuesta=<%=NombrePropuesta%>&RUT=<%=RutContribuyente%>&DV=<%=DV%>" target="_self">
			     			 <%=++cuenta%></a>
                            		 </td>
                                 <%}else{%>
                                  	<td width="10%"  align="center">
                                            <a onclick="alert('<%=msj%>')" id="num_cuota" href="#"><%=++cuenta%></a>

                                        </td>
                                  <%}%>


                             <%}else{%>
                             <td width="10%"  align="center"><A href="presentacionpropuesta.jsp?NombrePropuesta=<%=NombrePropuesta%>&RUT=<%=RutContribuyente%>&DV=<%=DV%>" target="_self">
			      <%=++cuenta%></a>
                             </td>
                          <%}%>

			  <td width="60%"  align="center"><%=vNombrePropuesta%></td>
			  <td width="15%"  align="center"><%=NumeroDeudas%></td>
			  <td width="15%"  align="right"><%=Saldo%></td>


			  </tr>
	               <%
			}
	   }
	   else{
	   %>
	   <script>
           //window.open('misdeudasfiscales.jsp?RutContribuyente=RutContribuyente','_self');
           window.open('limpiasessionstateful.jsp?redir=misdeudasfiscales.jsp&RUT=<%=RutContribuyente%>&DV=<%=DV%>','_self');
	   </script>
	   <%
	   }
	}
 }catch (Exception e){out.println("Error al lista propuestas"+e);}

%>

</table>
<br>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="30">
    <td> <p align="justify">
    Para ver el detalle de la propuesta, haga "click" sobre el n&uacute;mero ubicado a la izquierda de la propuesta de convenio.
    </p></td>
  </tr>
</table>
<br>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center">
       <input type="button" name="btnSalir" value="Volver" class="boton2" onclick="window.open('<%=sesionGeneraPropuestaInternet.getNombrePagina()%>?RUT=<%=RutContribuyente%>&DV=<%=DV %>','_self')">
    </td>
  </tr>
</table>
</FORM>
</body>
</html>



