<%@ page import="java.io.*,java.util.*,java.util.Date,java.text.NumberFormat,java.rmi.*,javax.rmi.*,javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.DateExt"%>
<%@ page import="cl.decalink.tgr.convenios.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.generapropuestainternet.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.DeudaWeb"%>
<%@ page import="cl.decalink.tgr.convenios.propuestas.ConvenioMasivo"%>
<%@ page import="cl.decalink.tgr.convenios.cuotas.*"%>
<%@ page import="cl.decalink.tgr.convenios.propuestas.ConvenioInternet"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.sesionconvenios.*"%>
<%@ page import="cl.decalink.tgr.convenios.sesion.Perfil"%>
<%@ page import="cl.tesoreria.admin.ejb.session.*"%>
<%@ page import="cl.decalink.tgr.convenios.ejb.session.stateful.propuesta.*"%>
<%@ page import="cl.tesoreria.busnmodel.*"%>
<%@ page import="lecturaconfig.*"%>



<%

/*-------lee session genera propuesta ------*/

/*----page import="cl.tgr.giradoresws.cliente.GiradorSoap"
page import="cl.tgr.giradoresws.cliente.Girador_Impl"
---*/

GeneraPropuestaInternet sesionGeneraPropuestaInternet = (GeneraPropuestaInternet) session.getValue("sesionGeneraPropuestaInternet");


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
//intancio para eliminar la propuestas mrc
      Propuesta propuestaElimina = null;
      Context ctxPropuesta = new InitialContext(new ObtenerServicios().env);
      Object homePropuesta = ctxSesionConvenios.lookup("cobranzas.convenios.negocio.ejb.session.stateful.Propuesta");
      PropuestaHome propuestaHome = (PropuestaHome) PortableRemoteObject.narrow(homePropuesta,PropuestaHome.class);

      propuestaElimina = propuestaHome.create();

Integer IdFuncionario = null;
Integer IdTesoreria = null;
//String RutContribuyente="";
long ITotal=0;
String STotal="";
String Dv="";

String scheme = request.getScheme();

if (rastreoSesionConvenios == null){
   %>
   <script>
   	alert("Termin\u00f3 su sesi\u00f3n debe logearse nuevamente");
	window.open("menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>","_self");
   </script>
   <%
}
else{
    IdFuncionario = rastreoSesionConvenios.getIdFuncionario();
    IdTesoreria = rastreoSesionConvenios.getIdTesoreria();
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
              {
              	 valido = true;
                 Dv = model.getPersona().getDv();
              }
         if (valido==false){
 		valido=true;
        }


    	}catch (Exception e){
      		e.printStackTrace();
    	}


//Prueba control acceso internet 05-07-2004


//-----------//
String nombrePropuesta = request.getParameter("NombrePropuesta");

String CantidadCuotas= request.getParameter("slctNumeroCuotas");
String Porcentaje= request.getParameter("Porcentaje");
String MontoCuotaContado = request.getParameter("txtMontoPago");
String evento = request.getParameter("evento");

Collection deudas = null;
ArrayList deudasPropuesta = null;
Collection cuotas= null;
Long numeroCuotas=null;
Long porcentaje=null;Monto
long montoDefecto=0;
String origen="";
String resultado="";

if (CantidadCuotas==null){CantidadCuotas="";}
if (Porcentaje==null){Porcentaje="";}
if (evento==null){evento="";}

ConvenioInternet convenio=null;


if (sesionGeneraPropuestaInternet!=null){
	if (nombrePropuesta!=null){

	    sesionGeneraPropuestaInternet.setPropuestaSeleccionada(nombrePropuesta);
	    convenio = sesionGeneraPropuestaInternet.getAplicaCondonacion();
	    sesionGeneraPropuestaInternet.setNombrePagina("listadepropuestafiscales.jsp");
	}
}


if (evento.equals("Calcular")){
	
	try{
    convenio = (ConvenioInternet) sesionGeneraPropuestaInternet.setCalcular(new Long(Porcentaje),new Long(CantidadCuotas), new Long(MontoCuotaContado));
	}catch (Exception e){
  		e.printStackTrace();
  		e.getMessage();
  	     %>
         <script>
         alert('<%=e.getMessage()%>');
         history.back();
         </script>
         <%
	}
}



convenio = sesionGeneraPropuestaInternet.getPropuestaSeleccionada();

long numResolucion=0;
Long CodPropuesta=new Long(0);
String RespuestaElimina ="";
if (convenio!=null){

try{
    if (convenio!=null){
       //deudas = convenio.getDeudas();
       deudasPropuesta = convenio.getDeudasPropuesta();

       numeroCuotas = convenio.getNumeroCuotas();
       porcentaje = convenio.getPorcentajeCuotaContado();
       cuotas=   convenio.getColeccionCuotas();

       ConvenioMasivo masivo =  convenio.getConvenioMasivo();

       if (masivo!=null){
         numResolucion = masivo.getNumeroResolucion();
       }


    }
   }catch (Exception e){out.println("e="+e);}

 }

 if (evento.equals("Aceptar")){

    if (IdFuncionario!=null){
    	     sesionGeneraPropuestaInternet.setCodigoFuncionario(new Long(IdFuncionario.toString()));
    }
    if (IdTesoreria!=null){
                sesionGeneraPropuestaInternet.setIdTesoreria(new Long(IdTesoreria.toString()));
    }

    Long codigoPropuesta = sesionGeneraPropuestaInternet.insertaPropuesta();

    String urlWSDL="";
    String urlPago="";


    if (codigoPropuesta.longValue()!=0){


      String vCodCuotaContado="";
	try {


         vCodCuotaContado=String.valueOf(sesionGeneraPropuestaInternet.getCodCuotaContado());

	 HashMap urls = new HashMap();
	 urls    = sesionGeneraPropuestaInternet.ServiciosPagoEnLinea();
     urlWSDL = scheme + "://" + (String) urls.get("URL_WSDL");
	 urlPago = scheme + "://" + (String) urls.get("URL_PAGO");

  //mrc       GiradorSoap client = new Girador_Impl().getgiradorSoap();
       //mrc  resultado = client.informarGiro(xml);
           resultado = sesionGeneraPropuestaInternet.GeneraXML(codigoPropuesta.longValue(),1, RutContribuyente,Dv);


      } catch (Exception ex) {
          resultado ="0";
	  out.println("Error a GRABAR A PORTAL="+ex.toString());
          ex.printStackTrace();
      }


//mrc    String xml = sesionGeneraPropuestaInternet.GeneraXML(codigoPropuesta.longValue(),1, RutContribuyente,Dv);
        session.setAttribute("nombrePagina","listadepropuestafiscales.jsp");
    //response.sendRedirect("http://164.77.244.251/portal/portlets/consultas/verConvenios.do?redireccion=verConveniosExterna&externa=0&criterio=main&convenios=true");
//out.println("<br>Resultado:"+resultado);
//out.println("<br>urlPago:"+urlPago);


if (!resultado.equals("0"))
      {

	%>
    <script>

  window.open("<%=urlPago%>?idext=<%=resultado%>&org=CNV","_self");

    </script>
    <%
      }
     else
       {
        RespuestaElimina = propuestaElimina.getEliminarPropuestas(codigoPropuesta.toString());
	//CodPropuesta=sesionGeneraPropuestaInternet.eliminaPropuestaInternet();
        %>
       <script>
       alert('En este momento no se puede realizar el pago, intente generar el convenio m\u00e1s tarde');
       </script>
       <%}
    }
 }


String RutRol="";

if (sesionGeneraPropuestaInternet != null) {
           RutRol = sesionGeneraPropuestaInternet.getRutRol();
    }

%>
<html>
<head>
	<title>Presentaci&oacute;n Propuesta</title>
	<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"  SRC="Funciones.inc"></SCRIPT>
	<meta http-equiv="PRAGMA" content="NO-CACHE">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="EstiloPagina.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000" vlink="#0000FF" alink="#0000FF">

<FORM ACTION="presentacionpropuesta.jsp" name="presentacionPropuesta" METHOD="post">
<input type="hidden" name="evento" value="<%=evento%>" >
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

 tituloDeLaPagina('PRESENTACION PROPUESTA DE CONVENIO');

//-->
</script>

<table width="90%" border="0" cellspacing="1" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
</tr>
<tr height="30">
<td>
</td>
</tr>
</table>

<table width="50%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
             <tr class="fondo2 tituloColumna" height="30">
             <td align="center">RUT:  <b>  <%=RutRol%></b></td>
             </tr>


  </table> <br>
<%
if (numResolucion!=0){

%>
<table width="90%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0"><tr><td>
<table width="95%" border="0" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
     <tr height="30">
     <td> Convenio  Masivo   N&deg; Resoluci&oacute;n:  <input type="text" name="numResolucion" STYLE="FONT-FAMILY: Arial; FONT-SIZE: 11px;TEXT-ALIGN: right" class="estilotexto" maxlength="10" size="10" VALUE="<%=numResolucion%>" readonly><br>
     <p align="justify">Antes de Aceptar este convenio verifique que no posee otras deudas candidatas
       a masivo que requieran de Reprogramaci&oacute;n(deudas que participan de covenios activos)
       <a href="deudascandidatasreprogramacion.jsp?nombrePagina=presentacionpropuesta.jsp&RUT=<%=RutContribuyente%>&DV=<%=DV%>" target="_self">Ver deudas candidatas a Reprogramaci�n</a>
     </p>
     </td>
     </tr>
  </table> </td></tr></table><br>
<%
}
%>

      <table width="90%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
     	          <tr class="fondo2 tituloColumna">
     	          <td width="10%"  align="center">N&deg;</td>
     	          <td width="12%"  align="center">N&deg; Form.</td>
     	          <td width="20%"  align="center">Folio</td>
     	          <td width="15%"  align="center">Fecha Vencimiento</td>
     	          <%--<td width="12%"  align="center">Condonaci&oacute;n Otorgada Multas(%)</td>
     	          <td width="12%"  align="center">Condonaci&oacute;n Otorgada Intereses(%)</td> --%>
     	          <td width="22%"  align="center">Condonaci&oacute;n Otorgada Recargos Legales</td>
                  <td width="20%"  align="center">Total a Pagar al contado($)</td>
	          </tr>
      <%
      try{
      if (!deudasPropuesta.isEmpty()){
          Iterator it = deudasPropuesta.iterator();
          int cuenta=0;
          Long CondonacioOtorgada =new Long(0);
          while (it.hasNext()){

              DeudaWeb deuda = (DeudaWeb) it.next();


              String fechaVencimiento = (String) deuda.getFechaVencimientoFMT();

   	      Long folio = (Long) new Long(deuda.getFolio());
   	      String fechaVencimientoFMT = (String) deuda.getFechaVencimientoFMT();
   	      Long saldo = (Long) new Long (deuda.getMontoConCondonacion());
   	      Long tipoCont = (Long) new Long (deuda.getTipoContribuyente());
              Long tipoForm = (Long) new Long (deuda.getTipoFormulario());
              Long PorcCondOtor = (Long) new Long (deuda.getPorcentajeCondonacion());
              String AplicadaA = (String) deuda.getCondonacionAplicadaA();
              Long PorcCondOtorMultas = (Long) new Long (deuda.getPorcentajeCondonacionMultas());
              Long PorcCondOtorIntereses = (Long) new Long (deuda.getPorcentajeCondonacionIntereses());

                origen = deuda.getOrigen();
              long saldito = saldo.longValue();
              String saldoS = StringExt.separaMiles(saldito);
              ITotal = ITotal+saldito;
              STotal = StringExt.separaMiles(ITotal);

               /******************bloque que se queda solo con un porcentaje de ******************************/
                if (PorcCondOtorMultas.intValue()>0){
                	CondonacioOtorgada=PorcCondOtorMultas;
                }else if(PorcCondOtorIntereses.intValue()>0){
                	CondonacioOtorgada=PorcCondOtorIntereses;
                }else{
                	CondonacioOtorgada=new Long(0);
                }
               
               /*************************************************************************************************/
      %>
      		<tr>
      		  <td width="10%"  align="center"><%=++cuenta%></td>
      		  <td width="12%"  align="right"><%=tipoForm%></td>
   		  <td width="20%"  align="right"><%=folio%></td>
   		  <td width="15%"  align="center"><%=fechaVencimientoFMT%></td>
   		  <%--<td width="12%"  align="center"><%=PorcCondOtorMultas%></td>
          <td width="12%"  align="center"><%=PorcCondOtorIntereses%></td> --%>
          <td width="12%"  align="center"><%=CondonacioOtorgada%></td>
   		  <td width="20%"  align="right"><%=saldoS%></td>
   		  </tr>
      <%
         }
      }
     }catch (Exception e){out.println("Error listar deudas"+e);}
     %>



</table>
<br>
<table width="90%" border="0" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
<tr>
<td><p align="justify">
Si usted desea modificar las condiciones de la propuesta, como el n&uacute;mero de cuotas y/o el porcentaje de la deuda
total que quiere asignar a la primera cuota, presione el bot&oacute;n "Modificar".
</p>
</td>
</tr>
</table>
<br>
<table width="90%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
<tr><td>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="30">
    <td width="30%">N&uacute;mero de Cuotas</td>
    <td colspan="2" ><input type="text" name="Porcentaje" class="estilotexto" maxlength="2" size="15" VALUE="<%=numeroCuotas%>" readonly></td>
  </tr>
  </tr>
   <tr height="30">
      <td width="25%">Cuota Contado</td>

   
		<td width="75%"><input type="text" name="Porcentaje2"  class="estilotexto" maxlength="2" size="15" value="<%=porcentaje%>" readonly>%</td>

	 <td width="75%"><input type="hidden" name="Porcentaje"  class="estilotexto" maxlength="2" size="15" value="<%=porcentaje%>" readonly></td>
  </tr>
  </tr>
   <tr height="30">
      <td width="25%">Total a pagar liquidado a la fecha</td>
      <td width="75%"><input type="text" name="Porcentaje"  class="estilotexto" maxlength="15" size="15" value="<%=STotal%>" readonly></td>
  </tr>
</table>
</td>
</tr>
</table><BR>

<table width="90%" border="1" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos" bordercolorlight="#006699" bordercolordark="CCECF0">
  <tr class="fondo2 tituloColumna">
    <td width="30%" align="center">Cuota N&deg;</td>
    <td width="35%" align="center">Fecha Vencimiento Cuota</td>
    <td width="35%" align="center">Monto Cuota($)</td>
  </tr>
  <%
  int totalcuotas =0;
  if (cuotas!=null){
     if (!cuotas.isEmpty()){
        Iterator itx = cuotas.iterator();
        totalcuotas = cuotas.size();
	int cont = 1;
        while (itx.hasNext()){
          	Cuota cuota = (Cuota) itx.next();
		Long numCuota = (Long) new Long(cuota.getNumeroCuota());
		Date fechaVencimientoCuota = (Date) cuota.getFechaVencimiento();
		String fechaVencimientoCuotaFMT = DateExt.format("DD-MM-AAAA",fechaVencimientoCuota);
		Long montoCuota = (Long) new Long(cuota.getMonto());
                long totcuota = montoCuota.longValue();
                String cuotaS = StringExt.separaMiles(totcuota);

          if (cont<totalcuotas || totalcuotas==1){

	  %>
	  <tr >
	      <td width="30%" align="right"><%=numCuota%></td>
	      <td width="35%" align="center"><%=fechaVencimientoCuotaFMT%></td>
	      <td width="35%" align="right"><%=cuotaS%></td>
    </tr>
	  <%

	     }
	     cont++;
	  }
     }
   }

  %>
</table>

<br>


<table width="90%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
<tr><td>

<table width="90%" border="0" cellspacing="1" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
</tr>
<tr height="10">
<td>
</td>
</tr>
<tr height="30">
<td><p align="justify">El monto total a pagar corresponde al monto liquidado considerando los recargos hasta la fecha de hoy. Al realizar un convenio se le deben adicionar
los recargos proyectados a la fecha del t&eacute;rmino del convenio.</p></td>
</tr>
<tr height="30">
<td><p align="justify">La primera cuota corresponde a la cuota contado, la cual se debe cancelar en forma electr&oacute;nica.</p></td>
</tr>
<tr height="30">
<td>La &uacute;ltima cuota no aparece ya que corresponde a la cuota de ajuste y su valor ser&aacute; calculado una vez pagada la pen&uacute;ltima cuota.</td>
</tr>
<tr height="10">
<td>
</td>
</tr>
</table>

</td></tr>
</table><br>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center">
       <input type="button" name="btnSalir" value="Aceptar" class="boton2" onclick="document.presentacionPropuesta.evento.value='Aceptar';document.presentacionPropuesta.submit()">
       <input type="button" name="btnModificar" value="Modificar" class="boton2" onclick="window.open('configurarpropuestainternet.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>','_self')">

       <input type="button" name="btnVolver" value="Volver" class="boton2" onclick="window.open('<%=sesionGeneraPropuestaInternet.getNombrePagina()%>?RUT=<%=RutContribuyente%>&DV=<%=DV %>','_self')">
    </td>
  </tr>

</table>

<script>
if ("<%=origen%>"=="M"){
document.presentacionPropuesta.btnModificar.disabled=true;
}
</script>

</FORM>

</body>
</html>



