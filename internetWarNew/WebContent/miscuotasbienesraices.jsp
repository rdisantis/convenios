<%@ page import="java.io.*,java.util.*,java.util.Date,java.text.NumberFormat,java.rmi.*,javax.rmi.*,javax.naming.*,java.sql.*,javax.ejb.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.persona.*,cl.decalink.tgr.convenios.deudas.StringExt"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.generapropuestainternet.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.DeudaWeb"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.Perfil"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.sesionconvenios.*"%>
<%@ page import="cl.decalink.tgr.convenios.deudas.consultarexclusiones.*"%>
<%@ page import="cl.tesoreria.admin.ejb.session.*"%>
<%@ page import="cl.tesoreria.busnmodel.*"%>
<%@ page import="lecturaconfig.*"%>

<%

      SesionConvenios rastreoSesionConvenios = (SesionConvenios) session.getAttribute("rastreoSesionConvenios");
      String   RutContribuyente=request.getParameter("RUT");
      String   DV =request.getParameter("DV");
      Context ctxSesionConvenios = new InitialContext(new ObtenerServicios().env);
      Object homeSesionConvenios = ctxSesionConvenios.lookup("cobranzas.convenios.negocio.ejb.session.stateful.SesionConvenios");
      SesionConveniosHome sesionConveniosHome = (SesionConveniosHome) PortableRemoteObject.narrow(homeSesionConvenios,SesionConveniosHome.class);

      rastreoSesionConvenios = sesionConveniosHome.create();

      //rastreoSesionConvenios.setIdUsuario(new Integer(login.getUsuario()));
      rastreoSesionConvenios.loginInternet();
      rastreoSesionConvenios.setRutContribuyente(RutContribuyente);


Perfil PerfilSession = null;
int Perfil=0;
String NombrePag = "miscuotasbienesraices.jsp";
consultarexclusiones VSessionBeanExclusiones=null;
//String RutContribuyente ="";

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
    RutContribuyente =rastreoSesionConvenios.getRutContribuyente();

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


String evento = request.getParameter("evento");
if (evento==null){evento="";}
String Comuna=request.getParameter("txtSelcComu");
if (Comuna==null){Comuna="";}
String Rol=request.getParameter("Rol");
if (Rol==null){Rol="";}
String SubRol=request.getParameter("SubRol");
if (SubRol==null){SubRol="";}

String RolBienRaiz="";



GeneraPropuestaInternet sesionGeneraPropuestaInternet = (GeneraPropuestaInternet) session.getValue("sesionGeneraPropuestaInternet");



if (sesionGeneraPropuestaInternet==null){
Context ctxGeneraPropuesta = new InitialContext(new ObtenerServicios().env);
   Object homeGeneraPropuesta = ctxGeneraPropuesta.lookup("cobranzas.convenios.negocio.ejb.session.stateful.GeneraPropuestaInternet");
   GeneraPropuestaInternetHome  GeneraPropuestaInternetHome = (GeneraPropuestaInternetHome) PortableRemoteObject.narrow(homeGeneraPropuesta,GeneraPropuestaInternetHome.class);
   sesionGeneraPropuestaInternet = GeneraPropuestaInternetHome.create();
   session.putValue("sesionGeneraPropuestaInternet",sesionGeneraPropuestaInternet);

}

Collection ListarComunas = (Collection) sesionGeneraPropuestaInternet.getListarComunas();
String TipoCalle="";
String Calle="";
String Numero="";
String Depto="";
String Block="";
String Villa="";
String Manzana="";
String Predio="";
String ComunaDetalle="";
String CantidadDeudas="";
int noLiq=0;


persona contribuyente=null;

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

             }
         }
       } catch (Exception ep){out.println("Mensaje recuperar persona= "+ep);}
}


Collection datos=null;


if (evento.equals("") && sesionGeneraPropuestaInternet!=null){

	String rutrol = sesionGeneraPropuestaInternet.getRutRol();
	if (rutrol==null){rutrol="";}

	if (!rutrol.equals("")){
	   String a = "-";
	   String[] srutrol =  StringExt.split(rutrol,a);

	   Comuna =  srutrol[0];
	   Rol=  srutrol[1];
	   SubRol =  srutrol[2];
	   RolBienRaiz=Comuna+Rol+SubRol;

	   evento="RECUPERAR";
	}

}



if (evento.equals("RECUPERAR") && contribuyente!=null){
   try{
       RolBienRaiz=Comuna+Rol+SubRol;
       if (sesionGeneraPropuestaInternet!=null){

           sesionGeneraPropuestaInternet.setContribuyente(contribuyente);
	   sesionGeneraPropuestaInternet.setPerfil(Perfil);
	   sesionGeneraPropuestaInternet.setPerfilSesion(PerfilSession);

           boolean verifica = sesionGeneraPropuestaInternet.verificaRolBienRaiz(RolBienRaiz);
           if (!verifica){
		   %>
		   <script>
		   alert("Rol Bien Ra\u00edz ingresado no existe");
		   </script>
		   <%
		   Comuna="";
		   Rol="";
		   SubRol="";
                   }
           else{

           Context ctxexclusiones = new InitialContext(new ObtenerServicios().env);
	   Object home = ctxexclusiones.lookup("cobranzas.convenios.negocio.ejb.session.stateless.consultarexclusiones");
	   consultarexclusionesHome ConveniosDeudorHome = (consultarexclusionesHome) PortableRemoteObject.narrow(home,consultarexclusionesHome.class);

	   VSessionBeanExclusiones=ConveniosDeudorHome.create();
		
		//MAN000002653 se deshabilta la validacion fecha 17.03.2020 torellana
 	    //int verificaDem=VSessionBeanExclusiones.getVerificaDemanda(2,new Long(RolBienRaiz).longValue());
		int verificaDem = 0;

           if (verificaDem>0)
	   {
	   %>
	   <script>
		alert('Usted posee deudas en estado avanzado de cobranza judicial. Para mayor antecedentes dirigase a la oficina de Tesoreria de su juridicci\u00f3n');
		//window.open("cobadmmender01.jsp","_self");
	   </script>
	  <%
	  }
	  else
          {
           datos =(Collection) sesionGeneraPropuestaInternet.getDatosyDeudasRolBienRaiz(RolBienRaiz,"T");
           Collection deudas = (Collection) sesionGeneraPropuestaInternet.getDeudasFiscales();


           if (!datos.isEmpty() && deudas.size()>0){
             Collection deudasTmp = new ArrayList();
             deudasTmp.addAll(deudas);
             deudas.clear();
              /*estoy viendo si es liquidable */
              Iterator itdeu = deudasTmp.iterator();
              while (itdeu.hasNext()){
                DeudaWeb deu = (DeudaWeb)itdeu.next();
                if (deu.getCod92Liq().longValue()>=0){
                   deudas.add(deu);
                }else{
                 noLiq++;
                }

              }


              //actualiza las dedas del contribuyente
             sesionGeneraPropuestaInternet.setDeudasFiscales(deudas);


             CantidadDeudas = (new Long(deudas.size())).toString();


              Iterator itd = datos.iterator();
              while (itd.hasNext()){
                 HashMap x = (HashMap) itd.next();

                 TipoCalle = (String) x.get("TipoCalle");
                 Calle= (String) x.get("Calle");
		 Numero= (String) x.get("Numero");
		 Depto= (String) x.get("Depto");
		 Block= (String) x.get("Block");
		 Villa= (String) x.get("Villa");
		 Manzana= (String) x.get("Manzana");
		 Predio= (String) x.get("Predio");
		 ComunaDetalle= (String) x.get("Comuna");
		 //CantidadDeudas= ((Long) x.get("CantidadDeudas")).toString();

               }

              }
            else{
                %>
                   <script>
			alert("Rol Bien Ra\u00edz ingresado no tiene deudas morosas para realizar propuestas");
		   </script>

                <%
            }
           }
           }
       }
       //evento="";
    } catch (Exception e){out.println("Mensaje recuperar = "+e);}

}

if (evento.equals("PROPUESTAS")){
    try{
         if (sesionGeneraPropuestaInternet!=null){
          sesionGeneraPropuestaInternet.setNombrePagina("miscuotasbienesraices.jsp");
          int verifica = sesionGeneraPropuestaInternet.setSeleccionaDeudasContribuyente();
          //out.println(" verifica ="+verifica);
           if (verifica!=0){
           sesionGeneraPropuestaInternet.setContribuyente(contribuyente);

           if (verifica==4){
                 %>
	      <script>
	      alert("No existen deudas para hacer propuesta, esto se debe a que el total a pagar es inferior al requerido o superior al permitido");
	      </script>
              <%

           }
           else{
	  	      %>
	  	      <script>
	  	      alert("Operaci\u00f3n no se puede realizar en este momento, int\u00e9ntelo mas tarde");
	  	      </script>
	  	      <%
	  	}
           }else{
              %>
    	      <script>
    	      window.open('listadepropuestaterritorial.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>&NombrePag=<%=NombrePag%>','_self');
    	      </script>
    	      <%
          }

      //  evento="";
       }
     }catch (Exception e){out.println("setSeleccionaDeudasContribuyente = "+e);}

}

if (evento.equals("SALIR")){

    if (sesionGeneraPropuestaInternet != null) {
           sesionGeneraPropuestaInternet = null;
           session.putValue("sesionGeneraPropuestaInternet",null);
    }

   %>
   <script>
    window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>','_self')
   </script>
   <%
}



%>

<html>
<head>
	<title>Cuotas Bienes Raices</title>
	<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"  SRC="Funciones.inc"></SCRIPT>
	<meta http-equiv="PRAGMA" content="NO-CACHE">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="EstiloPagina.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000" vlink="#0000FF" alink="#0000FF">

<FORM  id="MisCuotasBienesRaices" name="MisCuotasBienesRaices" ACTION="miscuotasbienesraices.jsp" METHOD="post">
<input type="hidden" name="evento" value="<%=evento%>">
<input type="hidden" name="RUT" value="<%=RutContribuyente%>">
<input type="hidden" name="DV" value="<%=DV%>">

<script LANGUAGE="javascript">
<!--
 imagen = new Image(); 
 function tituloDeLaPagina(titulo) {

  document.write("<table class='tituloTabla' border=0 cellspacing=1 width='100%'>");
  document.write("  <tr>");
  document.write("  <td width=100% align='middle'><strong>"+titulo+"</strong></td>");
  document.write("  </tr>");
  document.write("</table>");
 }

 tituloDeLaPagina('SELECCION ROL MOROSO');

//-->
</script>
<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="10">
    <td> </td>
  </tr>
  <tr height="30">
    <td><p align="justify">
    Mediante esta opci&oacute;n puede crear una propuesta de convenio que incluir&aacute; todas las cuotas de contribuciones impagas,
   est&eacute;n o no en cobranza judicial.
    </p></td>
  </tr>
  <tr height="30">
  <td> <p align="justify">
   El convenio se concreatar&aacute; una vez que el pago de la primera cuota o cuota contado es cancelado mediante pago electr&oacute;nico.
      </p></td>
  </tr>
</table>

   <table width="100%" border="0" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="#CCECF0">
   <tr><td>

      <table width="100%" border="1" cellspacing="0" align="center" class="ftotabladatos"  bordercolorlight="#006699" bordercolordark="#CCECF0">
     	          <tr class="fondo2">
     	          <td width="25%"  align="center">&nbsp;</td>
     	          <td width="25%"  align="center"><strong>Comuna</strong></td>
     	          <td width="25%"  align="center"><strong>Rol</strong></td>
     	          <td width="25%"  align="center"><strong>SubRol</strong></td>
          </tr>



   		<tr>
   		  <td width="25%"  align="center"><b>Rol Bien Ra&iacute;z</b></td>
<%-- COMBO PARA SELECCIONAR COMUNA--%>
          <td width="25%" align="center">
            <select name="txtSelcComu" class="estilocombo">
              <option></option>
		  <%
		    if (!ListarComunas.isEmpty()){
			 Iterator itF =ListarComunas.iterator();
			 int indice=0;

			 while (itF.hasNext()){
			     HashMap Comunas = (HashMap) itF.next();
			     String CODIGO_F = (String) Comunas.get("COD_COMUNA");
			     String NOMBRE_F = (String) Comunas.get("NOMBRE");
                             String vComuna = "";
                             if (Comuna != ""){
                              vComuna = new  Long(Comuna).toString();
                             }
                            if (vComuna.equals(CODIGO_F)){
                             %>
				<option value="<%=CODIGO_F%>" selected><%=NOMBRE_F%></option>
			     <%
                             }
                             else
                             {
                             %>
				<option value="<%=CODIGO_F%>"><%=NOMBRE_F%></option>
                            <%
                              }
			     indice++;
			 }
		      }
		    %>
	  </select>
          </td>
<%-- FIN COMBO PARA SELECCIONAR COMUNA--%>
<%--   		  <td width="25%"  align="center"><input type="text" name="Comuna" maxlength="3" size="3" class="estilotexto"  value="<%=Comuna%>" onkeyup="Valida_Numerico(document.MisCuotasBienesRaices.Comuna)"></td>--%>
                  <input type="hidden" name="Comunas" maxlength="3" size="3" class="estilotexto"  value="<%=Comuna%>" >
		  <td width="25%"  align="center"><input type="text" name="Rol" maxlength="5" size="5" class="estilotexto"  value="<%=Rol%>" onkeyup="Valida_Numerico(document.MisCuotasBienesRaices.Rol)" ></td>
		  <td width="25%"  align="center"><input type="text" name="SubRol" maxlength="3" size="3" class="estilotexto"  value="<%=SubRol%>" onkeyup="Valida_Numerico(document.MisCuotasBienesRaices.SubRol)" ></td>
		  </tr>
</table>
</td></tr>
<tr>
		      <td  align="right"><input type="button" name="btnRecuperar" class="boton2" value="Recuperar" onclick="valida()"></td>
	   </tr>
</table>
<br><br>
<table width="100%" border="1" cellspacing="0" cellpadding="0" align="center" class="EstiloTextoBold" bordercolorlight="#006699" bordercolordark="#CCECF0" >
   <tr><td>

      <table width="100%" border="0" cellspacing="0" align="center" class="tablaMediaMD ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
     	          <tr>
     	          <td width="20%" >Tipo Calle</td>
     	          <td width="30%" ><input type="text" name="TipoCalle" class="estilotexto" maxlength="30" size="30" value="<%=TipoCalle%>" readonly></td>
     	          <td width="20%" align="right">Calle</td>
     	          <td width="30%" ><input type="text" name="Calle" class="estilotexto" maxlength="30" size="30" value="<%=Calle%>" readonly></td>

	          </tr>
	          <tr>
		  <td width="20%" >N&uacute;mero</td>
		  <td width="30%" ><input type="text" name="Numero" class="estilotexto" readonly maxlength="30" size="30" value="<%=Numero%>"></td>
		  <td width="20%" align="right">Depto.</td>
		  <td width="30%" ><input type="text" name="Depto" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Depto%>"></td>

		  </tr>
		  <tr>
		  <td width="20%" >Block</td>
		  <td width="30%" ><input type="text" name="Block" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Block%>"></td>
		  <td width="20%" align="right">Villa</td>
		  <td width="30%" ><input type="text" name="Villa" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Villa%>"></td>

		  </tr>
		  <tr>
		  <td width="20%" >Manzana</td>
		  <td width="30%" ><input type="text" name="Manzana" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Manzana%>"></td>
		  <td width="20%" align="right">Predio</td>
		  <td width="30%" ><input type="text" name="Predio" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Predio%>"></td>

		  </tr>
		  <tr>
		  <td width="20%" >Comuna</td>
		  <td colspan="3" ><input type="text" name="ComunaDetalle" maxlength="30" size="30" class="estilotexto" readonly  value="<%=ComunaDetalle%>"></td>
		  </tr>
		  <tr>
		  <td width="20%" >N&deg; Deudas Morosas</td>
		  <td colspan="3" ><input type="text" name="NroDeudas" maxlength="3" size="3" class="estilotexto" readonly  value="<%=CantidadDeudas%>"></td>
		  </tr>


</table>

<table width="100%" border="0" cellspacing="0" align="center" class="tablaMediaXS ftotabladatos"  bordercolorlight="#006699" bordercolordark="CCECF0">
  <tr>
    <td width="20%" >Tipo Calle</td>
    <td width="30%" ><input type="text" name="TipoCalle" class="estilotexto" maxlength="30" size="30" value="<%=TipoCalle%>" readonly></td>
  </tr>
  <tr>
    <td width="20%">Calle</td>
    <td width="30%" ><input type="text" name="Calle" class="estilotexto" maxlength="30" size="30" value="<%=Calle%>" readonly></td>
  </tr>
  <tr>
    <td width="20%" >N&uacute;mero</td>
    <td width="30%" ><input type="text" name="Numero" class="estilotexto" readonly maxlength="30" size="30" value="<%=Numero%>"></td>
  </tr>
  <tr>
    <td width="20%">Depto.</td>
    <td width="30%" ><input type="text" name="Depto" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Depto%>"></td>
  </tr>
  <tr>
    <td width="20%" >Block</td>
    <td width="30%" ><input type="text" name="Block" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Block%>"></td>
  </tr>
  <tr>
    <td width="20%">Villa</td>
    <td width="30%" ><input type="text" name="Villa" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Villa%>"></td>
  </tr>
  <tr>
    <td width="20%" >Manzana</td>
    <td width="30%" ><input type="text" name="Manzana" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Manzana%>"></td>
  </tr>
  <tr>
    <td width="20%">Predio</td>
    <td width="30%" ><input type="text" name="Predio" maxlength="30" size="30" class="estilotexto" readonly  value="<%=Predio%>"></td>
  </tr>
  <tr>
    <td width="20%" >Comuna</td>
    <td colspan="3" ><input type="text" name="ComunaDetalle" maxlength="30" size="30" class="estilotexto" readonly  value="<%=ComunaDetalle%>"></td>
  </tr>
  <tr>
    <td width="20%" >N&deg; Deudas Morosas</td>
    <td colspan="3" ><input type="text" name="NroDeudas" maxlength="3" size="3" class="estilotexto" readonly  value="<%=CantidadDeudas%>"></td>
  </tr>
</table>

</td></tr>
</table>


<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center">
       <input type="button" name="btnPropuestas" value="Ver Propuestas" class="boton2" onclick="document.MisCuotasBienesRaices.evento.value='PROPUESTAS';document.MisCuotasBienesRaices.submit()">
       <%--<input type="button" name="btnSalir" value="Salir" class="estiloBoton" onclick="document.MisCuotasBienesRaices.evento.value='SALIR';document.MisCuotasBienesRaices.submit()">--%>
       <input type="button" name="btnSalir" value="Salir" class="boton2" onclick="window.open('menu.jsp?RUT=<%=RutContribuyente%>&DV=<%=DV%>','_self')">
    </td>
  </tr>
</table>

<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="ftotabladatos">
  <tr height="10">
    <td> </td>
  </tr>
  <%if (noLiq>0){%>
  <tr height="30">
      <td><p align="justify">
     <font size="2">
       <b>
     Usted posee deudas que no pueden ser consideradas para un convenio a trav&eacute;s de este medio.
     Por favor ac&eacute;rquese a la Tesorer&iacute;a m&aacute;s cercana donde podr&aacute; regularizar esta situaci&oacute;n.</font>
       </b>
      </p></td>
  </tr>
  <%}%>

</table>


<script>

if ("<%=CantidadDeudas%>"==""){
   document.MisCuotasBienesRaices.btnPropuestas.disabled=true;
}

function valida(){

   if (document.MisCuotasBienesRaices.txtSelcComu.value==""){
      alert("Debe ingresar comuna");
      document.MisCuotasBienesRaices.txtSelcComu.focus();
      return;
   }
   if (document.MisCuotasBienesRaices.Rol.value==""){
         alert("Debe ingresar Rol");
         document.MisCuotasBienesRaices.Rol.focus();
         return;
      }
   if (document.MisCuotasBienesRaices.SubRol.value==""){
          alert("Debe ingresar SubRol");
          document.MisCuotasBienesRaices.SubRol.focus();
          return;
      }

  document.MisCuotasBienesRaices.Comunas.value=document.MisCuotasBienesRaices.txtSelcComu.value;
  Verifica_Rol_Bien_Raiz(document.MisCuotasBienesRaices.Comunas,document.MisCuotasBienesRaices.Rol,document.MisCuotasBienesRaices.SubRol);

  document.MisCuotasBienesRaices.evento.value="RECUPERAR";
  document.MisCuotasBienesRaices.submit();

}

</script>
</FORM>
</body>
</html>



