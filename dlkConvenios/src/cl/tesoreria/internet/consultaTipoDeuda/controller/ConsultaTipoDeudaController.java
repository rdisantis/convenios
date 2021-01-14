package cl.tesoreria.internet.consultaTipoDeuda.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cl.teso.reca.pkgcutservicestrx.classes.Messages.ConsultarAvisoReciboResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.IngresarArResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaItems;
import cl.tesoreria.internet.carritocid.consulta.servicio.ws.CarritoCidWs;
import cl.tesoreria.internet.carritocid.consulta.servicio.ws.CarritoCidWsService;
import cl.tesoreria.internet.carritocid.consulta.servicio.ws.ObtenerDeudasIdsessionOSalidaVO;
import cl.tesoreria.internet.consultaTipoDeuda.delegate.EpagoGenerarIdentificadorDelegate;
import cl.tesoreria.internet.consultaTipoDeuda.delegate.PkgCutServicesTrxEJBClientDelegate;
import cl.tesoreria.internet.consultaTipoDeuda.delegate.PkgRegistroDeudasPagos11gDelegate;
import cl.tesoreria.internet.consultaTipoDeuda.utils.GenerarItems;
import cl.tesoreria.internet.consultaTipoDeuda.utils.PropertiesFile;
import cl.tesoreria.internet.consultaTipoDeuda.utils.Utils;
import cl.tesoreria.internet.consultaTipoDeuda.utils.UtilsBase64;
import cl.tesoreria.internet.consultaTipoDeuda.utils.UtilsCarrito;
import cl.tesoreria.internet.consultaTipoDeuda.vo.CarritoVO;
import cl.tesoreria.internet.consultaTipoDeuda.vo.DetallePagosVO;
import cl.tesoreria.internet.consultaTipoDeuda.vo.TipoPagoVO;
import cl.tesoreria.internet.registroDeudas.vo.ComprobanteTransaccionCursorComprobanteVO;
import cl.tesoreria.internet.registroDeudas.vo.ComprobanteTransaccionInVO;
import cl.tesoreria.internet.registroDeudas.vo.ComprobanteTransaccionOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaDeudaPagadaInVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaDeudaPagadaOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGiroExternoCursorGiroExternoVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGiroExternoDetalleInVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGiroExternoDetalleOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGiroExternoInVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGiroExternoOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGiroPatenteCursorGiroPatenteVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGiroPatenteInVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGiroPatenteOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGirosGiradorInVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaGirosGiradorOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaIdLiqPatentesCursorGiroExternoVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaIdLiqPatentesInVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaIdLiqPatentesOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaIrasOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaOrgGiradorInVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaOrgGiradorOutVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaPagoIdOpCursorConsultaPagoVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaPagoIdOpInVO;
import cl.tesoreria.internet.registroDeudas.vo.ConsultaPagoIdOpOutVO;
import cl.tesoreria.internet.registroDeudas.vo.DetalleCarritoCursorDetalleCarritoVO;
import cl.tesoreria.internet.registroDeudas.vo.DetalleCarritoInVO;
import cl.tesoreria.internet.registroDeudas.vo.DetalleCarritoOutVO;
import cl.tesoreria.internet.registroDeudas.vo.DetalleDeudasCursorDetalleDeudasVO;
import cl.tesoreria.internet.registroDeudas.vo.DetalleDeudasInVO;
import cl.tesoreria.internet.registroDeudas.vo.DetalleDeudasOutVO;
import cl.tesoreria.internet.registroDeudas.vo.EpagoGenerarIdentificadorInVO;
import cl.tesoreria.internet.registroDeudas.vo.EpagoGenerarIdentificadorOutVO;

@Controller
public class ConsultaTipoDeudaController {

	private static Logger logger = Logger.getLogger("cl.tesoreria.internet.consultaTipoDeuda");
	
	PkgCutServicesTrxEJBClientDelegate consultaDeudasPortal = new PkgCutServicesTrxEJBClientDelegate();
	PkgRegistroDeudasPagos11gDelegate delegate = new PkgRegistroDeudasPagos11gDelegate();

	Utils utils = new Utils();
	GenerarItems items = new GenerarItems();

	@RequestMapping(value = "/enviaMedioPago", method = { RequestMethod.GET, RequestMethod.POST })
	public String enviaMedioPago(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		PropertiesFile.cargarArchivo();
		ConsultaGiroExternoInVO giroExternoIn = new ConsultaGiroExternoInVO();
		ConsultaGiroExternoDetalleInVO giroExternoDetalleIn = new ConsultaGiroExternoDetalleInVO();
		ConsultaGiroExternoOutVO giroExternoOut = null;
		ConsultaGiroExternoDetalleOutVO giroExternoDetalleOut = null;

		ConsultaDeudaPagadaInVO deudaPagadaIn = new ConsultaDeudaPagadaInVO();
		ConsultaDeudaPagadaOutVO deudaPagadaOut = null;
		ConsultaOrgGiradorInVO orgGiradorIn = new ConsultaOrgGiradorInVO();
		ConsultaOrgGiradorOutVO orgGiradorOut = null;

		IngresarArResult ingresarAr = null;
		
		String check = null;
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		BigDecimal formulario = null;
		
		/**
		 * Parametros para giradores externos
		 */

		String idext = request.getParameter("ID_EXT");
		String origen = request.getParameter("ORIGEN");
		String canal =  request.getParameter("CANAL");
		
		/**
		 * Crear cookie para capturar parametro desde nuevo modulo de contribuciones 
		 */
		Cookie ck = new Cookie("contribucionesID", canal);
		response.addCookie(ck);

		model.addAttribute("origenExterno", origen);

		if (idext == null && origen == null) {
			logger.info("PAGO INTERNO");
			check = request.getParameter("listaContribuciones");

		} else {
			logger.info("PAGO EXTERNO");
			try {
				logger.info("Id Externo a consultar: " + idext);
				logger.info("Girador a consultar: " + origen);
				orgGiradorIn.setOrgIn(origen.trim());
				orgGiradorOut = delegate.consultaOrgGirador(orgGiradorIn);
				logger.info("Salida Girador: " + orgGiradorOut.getEsGiradorOut());
				if (orgGiradorOut.getEsGiradorOut().compareTo(new BigDecimal(0)) == 0) {
					logger.info("Girador NO existe en los registros de Tesoreria.");
					model.addAttribute("mensaje", "Girador no existe en los registros de Tesorería.");
					return "jsp/error.jsp";
				} else {
					logger.info("Girador SI existe en los registros de Tesoreria.");
					logger.info("antes de consulta giro externo");
					giroExternoIn.setIdextin(idext);
					giroExternoOut = delegate.consultaGiroExterno(giroExternoIn);
					logger.info("despues de consulta giro externo (tamaño): "
							+ giroExternoOut.getCursorGiroExterno().size());

					if (giroExternoOut.getCursorGiroExterno().size() > 0) {
						for (ConsultaGiroExternoCursorGiroExternoVO ge : giroExternoOut.getCursorGiroExterno()) {
							logger.info("rut: " + ge.getRut());
							logger.info("formulario: " + ge.getFormulario());
							logger.info("folio: " + ge.getFolio());
							logger.info("vcto: " + ge.getFechaVencimiento());
							logger.info("idConvenio: " + ge.getIdConvenio());
							logger.info("id liquidacion tabla giro: " + ge.getIdLiquidacion());

							/**
							 * se agrega model formulario para mostrar boton de
							 * multicaja 
							 * v.1 22-11-2018
							 * v.2 19-03-2019
							 * 
							 */
							formulario = ge.getFormulario();
							model.addAttribute("formulario" , formulario);

							cal2.setTime(ge.getFechaVencimiento());

							giroExternoDetalleIn.setRutIn(ge.getRut());
							giroExternoDetalleIn.setFormularioIn(ge.getFormulario());
							giroExternoDetalleIn.setFolioIn(ge.getFolio());
							giroExternoDetalleIn.setFechaVctoIn(ge.getFechaVencimiento());
							giroExternoDetalleIn.setIdConvenioIn(ge.getIdConvenio());

							deudaPagadaIn.setRutIn(ge.getRut());
							deudaPagadaIn.setFormularioIn(ge.getFormulario());
							deudaPagadaIn.setFolioIn(ge.getFolio());
							deudaPagadaIn.setFechaVctoIn(ge.getFechaVencimiento());
							RecaItems[] vectorItems = null;

							try {
								logger.info("antes de consulta deuda pagada");
								deudaPagadaOut = delegate.consultaDeudaPagada(deudaPagadaIn);
								logger.info("codigo verificacion deuda: " + deudaPagadaOut.getCantidad());
								if (deudaPagadaOut.getCantidad().compareTo(new BigDecimal(0)) == 0) {
									giroExternoDetalleOut = delegate.consultaGiroExternoDetalle(giroExternoDetalleIn);

									/**
									 * SE AGREGA FUNCIONALIDAD PARA NO GENERAR
									 * ITEMS A F702
									 */
									
								
									if (ge.getFormulario().compareTo(new BigDecimal(702)) == 0) {
										logger.info("F" + ge.getFormulario() + " NO genera items");
										vectorItems = null;
									} else {
										logger.info("F" + ge.getFormulario() + " SI genera items");
										vectorItems = items.generaItems(giroExternoDetalleOut);
									}
								
									/**
									 * Verifica si existe id_liquidacion en
									 * Tabla Giro
									 */
									if (ge.getIdLiquidacion() == null || ge.getIdLiquidacion().trim().equals("")) {

										try {
											
											/*Si el pago es en UTM o UF entonces se ingresa AR como peso*/
											BigDecimal moneda;
											BigDecimal montoNeto;
											if( ge.getIdMoneda().intValue() == 0
											 || ge.getIdMoneda().intValue() == 1
											 || ge.getIdMoneda().intValue() == 2){
												moneda = new BigDecimal(0);
												montoNeto = ge.getTotalPagar();
											} else {
												moneda = ge.getIdMoneda();
												montoNeto = ge.getMontoNeto();
											}
											
											logger.info("liquida deuda, va a buscar id liquidacion a servicio Reca");
											ingresarAr = consultaDeudasPortal.ingresar700Reca(cal, cal, cal,
													new BigDecimal(5), ge.getIdGirador().toString(), new BigDecimal(1),
													ge.getRut(), utils.devuelveDV(ge.getRut()), ge.getFormulario(), "A",
													ge.getFolio(), null, cal, cal2, vectorItems, moneda,
													montoNeto, ge.getTotalPagar(), ge.getReajuste(),
													ge.getInteresMulta(), ge.getInteresMulta(), ge.getReajuste(), null);

											if (ingresarAr.getResultCode().equals(IngresarArResult.TRX_ERROR)) {
												throw new Exception(
														"Error en IngresarAr: " + ingresarAr.getResultMessage());
											} else {
												logger.info("GuardarDeuda - insertBD: CODIGO DE BARRA : "
														+ ingresarAr.getCodigoBarra());
												logger.info("GuardarDeuda - insertBD: CODIGO RESULTADO : "
														+ ingresarAr.getResultCode());
												logger.info("GuardarDeuda - insertBD: MENSAJE SALIDA: "
														+ ingresarAr.getResultMessage());
												check = ingresarAr.getCodigoBarra();
											}
										} catch (Exception e) {
											logger.error("GuardarDeuda - insertBD: Error en ingresar700Reca: "
													+ e.getMessage());
											model.addAttribute("mensaje", "Error en ingresar AR al Reca");
											return "jsp/error.jsp";
										}
									} else {
										logger.info("no liquida deuda, manda id liquidacion de la tabla giro");
										check = ge.getIdLiquidacion();
									}
								} else {
									model.addAttribute("mensaje", "Deuda Pagada en Tesoreria");
									return "jsp/error.jsp";
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						model.addAttribute("mensaje", "No existe datos para la deuda generada");
						return "jsp/error.jsp";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Agrega Deudas Al Carrito
		
		boolean swForm = false;
		String base64 = "";
		CarritoCidWsService service = new CarritoCidWsService();
		CarritoCidWs port = service.getCarritoCidWsPort();
		String idCarrito = request.getSession().getId();
		// Sube Deudas al Carrito
		logger.info(idCarrito + "==>" + check);
		port.eliminaCarritoSession(idCarrito);
		port.subeDeudasCarrito(idCarrito, UtilsCarrito.extraeIDLiquidacion(check));

		List<ObtenerDeudasIdsessionOSalidaVO> listaDeudas = null;
		// Trae Deudas Al Carrito para Calculo Monto Total
		listaDeudas = port.obtieneDeudasIdSession(idCarrito);
		// Calculo Monto a Pagar desde El Carrito
		BigDecimal montoPagar = new BigDecimal(0);
		String formularioJSON = ""; 
		if (listaDeudas != null && listaDeudas.size() > 0) {
			for (ObtenerDeudasIdsessionOSalidaVO deuda : listaDeudas) {
				formularioJSON = deuda.getFormCod().toString();
				logger.info("formulario  a buscar en properties (canal): " + canal);
				if (canal == null){
					if (UtilsCarrito.buscarValores(deuda.getFormCod().toString(), PropertiesFile.botonPagoForms)) {
						swForm = true;
					}
					logger.info("canal null, no viene de contribuciones");
				}else {
					if (UtilsCarrito.buscarValores(canal, PropertiesFile.botonPagoForms)) {
						swForm = true;
					}
				}
				
				// Suma Monto a Pagar
				montoPagar = montoPagar.add(deuda.getMontoTotal());
				logger.info("Codigos de barra: " + deuda.getCodigoBarra());
			}
			logger.info("Monto Total a Pagar: " + montoPagar.toString());

			try {
				List<TipoPagoVO> lista = new ArrayList<TipoPagoVO>();

				TipoPagoVO vo1 = new TipoPagoVO();
				vo1.setMoneda("CLP");
				vo1.setValor(montoPagar);
				vo1.setTipo("ONLINE");

				lista.add(vo1);

				// Falta Calcular Esto
				TipoPagoVO vo2 = new TipoPagoVO();
				vo2.setMoneda("CLP");
				vo2.setValor(montoPagar);
				vo2.setTipo("PRESENCIAL");

				lista.add(vo2);

				base64 = UtilsCarrito.envioPagoBaso64(idCarrito, origen, "", lista, formularioJSON);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error al Crear objJSON", e);
				model.addAttribute("mensaje", "Error al crear JSON");
				return "jsp/error.jsp";
			}
			// Falta Consultar Por Monto Presencial

		} else {
			model.addAttribute("mensaje", "Sin deudas en sesion");
			return "jsp/error.jsp";
		}
		
				
		String agente = request.getParameter("agente");
		
		try {
			ConsultaIrasOutVO listaIras = delegate.consultaIras();
			model.addAttribute("listaIras", listaIras.getCursorBancos());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (utils.validarHorarioSuspension()) {

			logger.info("checkeados en pago: " + check);
			model.addAttribute("idCarrito", idCarrito);
			model.addAttribute("listaContribuciones", check);
			model.addAttribute("agente", agente);
			model.addAttribute("base64", base64);
			model.addAttribute("urlBcoPrueba", PropertiesFile.urlBcoPrueba);
			
			if (PropertiesFile.botonPagoActivo && swForm) {
				return "jsp/mediosPagoOriginal.jsp";
			} else {
				return "jsp/mediosPago.jsp";
			}
		} else {
			return "jsp/suspension_por_horario.jsp";
		}
	}

	@RequestMapping(value = "/capturaCarrito", method = { RequestMethod.GET, RequestMethod.POST })

	public String capturaCarritoData(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		UtilsCarrito utilsCarrito = new UtilsCarrito();
		logger.info("Dentro de captura Carrito.");
		CarritoCidWsService service = new CarritoCidWsService();
		BigDecimal formulario = null;
		try {
			CarritoCidWs port = service.getCarritoCidWsPort();
			logger.info("Puerto: " + port);
			List<ObtenerDeudasIdsessionOSalidaVO> listaDeudas = null;
			String idCarrito = utilsCarrito.getIdCarrito(request);
			String base64 = "";
			logger.info("Cookie Session: " + idCarrito);

			listaDeudas = port.obtieneDeudasIdSession(idCarrito);
			logger.info(listaDeudas.size());

			// Calculo Monto a Pagar desde El Carrito
			PropertiesFile.cargarArchivo();
			boolean swForm = false;
			BigDecimal montoPagar = new BigDecimal(0);
			if (listaDeudas != null && listaDeudas.size() > 0) {
				logger.info("entro a if");
				String codBarra = "on, ";
				for (ObtenerDeudasIdsessionOSalidaVO deuda : listaDeudas) {
					formulario = deuda.getFormCod();
					if (UtilsCarrito.buscarValores(deuda.getFormCod().toString(), PropertiesFile.botonPagoForms)) {
						swForm = true;
					}
					// carga parametros calculados por el ADF
					codBarra = codBarra + deuda.getCodigoBarra() + " ,";
					logger.info("Codigos de barra: " + deuda.getCodigoBarra());
					montoPagar = montoPagar.add(deuda.getMontoTotal());
				}

				logger.info("Monto a Pagar: " + montoPagar.toString());

				try {
					List<TipoPagoVO> lista = new ArrayList<TipoPagoVO>();

					TipoPagoVO vo1 = new TipoPagoVO();
					vo1.setMoneda("CLP");
					vo1.setValor(montoPagar);
					vo1.setTipo("ONLINE");

					lista.add(vo1);
					// Falta Calcular Esto
					TipoPagoVO vo2 = new TipoPagoVO();
					vo2.setMoneda("CLP");
					vo2.setValor(montoPagar);
					vo2.setTipo("PRESENCIAL");

					lista.add(vo2);

					base64 = UtilsCarrito.envioPagoBaso64(idCarrito, "", "", lista, String.valueOf(formulario));
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error al Crear objJSON", e);
					model.addAttribute("mensaje", "Error al crear JSON");
					return "jsp/error.jsp";
				}

				String agente = request.getParameter("agente");
				try {
					ConsultaIrasOutVO listaIras = delegate.consultaIras();
					model.addAttribute("listaIras", listaIras.getCursorBancos());

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (utils.validarHorarioSuspension()) {

					logger.info("checkeados en pago: " + codBarra);
					String server = request.getServerName();
					logger.info("server name: " + server);
					model.addAttribute("idCarrito", idCarrito);
					model.addAttribute("listaContribuciones", codBarra);
					model.addAttribute("serverName", server);
					model.addAttribute("agente", agente);
					model.addAttribute("base64", base64);
					if (PropertiesFile.botonPagoActivo && swForm) {
						return "jsp/mediosPago.jsp";
					} else {
						return "jsp/mediosPagoOriginal.jsp";
					}
				} else {
					return "jsp/suspension_por_horario.jsp";
				}

			} else {
				return "jsp/carritoSuspensionHorario.jsp";
			}

		} catch (Exception e) {
			logger.error("Caida en traspaso a carrito.");
			e.printStackTrace();
			model.addAttribute("mensaje", "Problema con la carga del carrito.");
			return "jsp/error.jsp";

		}
	}

	@RequestMapping(value = "/guardaDeudas", method = { RequestMethod.GET, RequestMethod.POST })

	public String guardaDeudas(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		EpagoGenerarIdentificadorDelegate identificador = new EpagoGenerarIdentificadorDelegate();
		EpagoGenerarIdentificadorInVO identificadorIn = new EpagoGenerarIdentificadorInVO();
		ConsultarAvisoReciboResult result = null;
		ConsultaDeudaPagadaInVO deudaPagadaIn = new ConsultaDeudaPagadaInVO();
		ConsultaDeudaPagadaOutVO deudaPagadaOut = null;
		UtilsCarrito utilsCarrito = new UtilsCarrito();
		Utils util = new Utils();
		String idOperacion2 = null;
		String rutRol = null;
		BigDecimal clienteTipo = null;
		String banco = null;
		String banco2 = null;
		String agente = null;
		String moneda = null;
		BigDecimal formulario = new BigDecimal(0);
		BigDecimal codGirador = null;
		BigDecimal folio = null;
		Date fechaVcto = null;
		String idTransaccion = "";
		try {
		logger.info("origenExterno :" + request.getParameter("origenExterno"));
		logger.info("listaContribuciones :" + request.getParameter("listaContribuciones"));

		String idCarrito = request.getParameter("idCarrito");
		logger.info("idCarrito: " + idCarrito);

		logger.info("agente :" + request.getParameter("agente"));
		logger.info("banco :" + request.getParameter("banco"));

		String origenExterno = request.getParameter("origenExterno");

		if (request.getParameter("agente") == null) {
			agente = "";
		} else {
			agente = request.getParameter("agente");
		}

		if (request.getParameter("banco").equals("166")) {
			banco = "16";
		} else {
			banco = request.getParameter("banco");
		}

		logger.info("BANCO A CONSULTAR: " + banco);

		identificadorIn.setTabla("C");
		

			// Inicio Buscar Ids de Liquidacion
			String check = "";
			if (idCarrito != null) {

				CarritoCidWsService service = new CarritoCidWsService();
				CarritoCidWs port = service.getCarritoCidWsPort();
				List<ObtenerDeudasIdsessionOSalidaVO> listaDeudas = port.obtieneDeudasIdSession(idCarrito);

				if (listaDeudas != null && listaDeudas.size() > 0) {
					logger.info("entro a if");
					check = "on, ";
					for (ObtenerDeudasIdsessionOSalidaVO deuda : listaDeudas) {
						check = check + deuda.getCodigoBarra() + " ,";
						logger.info("Codigos de barra: " + deuda.getCodigoBarra());
					}
				} else {
					throw new Exception("Error De Extraccion Deuda Carrito Desde El Servicio");
				}
			} else {
				check = request.getParameter("listaContribuciones");
			}
			
			// Fin Buscar Ids de Liquidacion

			EpagoGenerarIdentificadorOutVO identificadorOutOp = identificador
					.epagoGenerarIdentificador(identificadorIn);
			String idOperacion = identificadorOutOp.getReturnFunc();
			logger.info("ID OP: " + idOperacion);
			logger.info("check: " + check);
			String newCheck = check.replace("on, ", " ");
			String[] check1 = newCheck.trim().split(",");
			logger.info("largo deudas a pagar: " + check1.length);
			BigDecimal sumaTotalPagar = new BigDecimal(0);

			for (String valor : check1) {
				logger.info("idLiquidacion a consultar en guarda deudas: " + valor.trim());
				EpagoGenerarIdentificadorOutVO identificadorOutTr = identificador
						.epagoGenerarIdentificador(identificadorIn);
				idTransaccion = identificadorOutTr.getReturnFunc();
				result = consultaDeudasPortal.consultarAr(valor.trim());
				rutRol = result.recaDeuda.getRecaClave().getRutRol().toString().trim();
				String totalPagar = result.recaDeuda.getMontoTotal().toString().trim();
				clienteTipo = result.recaDeuda.getRecaClave().getClienteTipo();
				sumaTotalPagar = sumaTotalPagar.add(new BigDecimal(totalPagar));
				formulario = result.recaDeuda.getRecaClave().getFormTipo();
				moneda = result.recaDeuda.getRecaClave().getMonedaId().toString();
				folio = result.recaDeuda.getRecaClave().getFormFolio();
				fechaVcto = result.recaDeuda.getRecaClave().getVencimiento().getTime();

				/**
				 * agregar restriccion de duplicidad de deuda
				 */
				deudaPagadaIn.setRutIn(new BigDecimal(rutRol));
				deudaPagadaIn.setFormularioIn(formulario);
				deudaPagadaIn.setFolioIn(folio);
				deudaPagadaIn.setFechaVctoIn(fechaVcto);
				deudaPagadaOut = delegate.consultaDeudaPagada(deudaPagadaIn);
				logger.info("Codigo Deuda Pagada: " + deudaPagadaOut.getCantidad());

				if (deudaPagadaOut.getCantidad().intValue() > 0) {
					logger.info("En estos momentos no presenta deuda en Tesoreria.");
					model.addAttribute("mensaje", "En estos momentos no presenta deuda en Tesoreria.");
					return "jsp/error.jsp";
				} else {
					logger.info("Deuda habilitada para ser pagada.");
					String canal = utilsCarrito.getCookie(request, "contribucionesID");
					logger.info("Parametro canal en Cookie metodo guarda deudas: " + canal);
					
					codGirador = util.insertarListaPago(result, idOperacion, idTransaccion, banco, agente,origenExterno, canal);
					util.insertarListaPagoDetalle(result, idTransaccion);
				}

			}

			logger.info("resultado comparacion: " + formulario.compareTo(new BigDecimal(699)));

			if (formulario.compareTo(new BigDecimal(699)) == 1) {
				idOperacion2 = idOperacion;
				idOperacion = idTransaccion;
				logger.info("idOP2 700: " + idOperacion2);
				logger.info("idTr2 700: " + idOperacion);
			} else {
				logger.info("COD GIRADOR A INGRESAR EN 698: " + codGirador);
				idOperacion2 = util.insertarCarrito(rutRol, idOperacion, sumaTotalPagar, banco, clienteTipo, moneda,
						codGirador);
				logger.info("idOP2 VARIOS: " + idOperacion2);
			}

			model.addAttribute("idOperacion", idOperacion2);

			logger.info("BANCO A CONSULTAR PARA URL: " + request.getParameter("banco"));
			if (request.getParameter("banco").equals("166")) {
				banco2 = "166";
			} else {
				banco2 = request.getParameter("banco");
			}

			String bancoUrl = util.devuelveUrlBanco(banco2, idOperacion2);
			logger.info("URL BANCO A CONSULTAR: " + bancoUrl);

			String urlBase = utils.devuelveUrlBase();

			if (banco.equals("267") || banco.equals("268")) {
				logger.info("id Op 698: " + idOperacion2);
				logger.info("url Transbank: " + bancoUrl);
				logger.info("id abif: " + banco);
				model.addAttribute("idOp698", idOperacion2);
				model.addAttribute("urlTransbank", bancoUrl);
				model.addAttribute("idAbif", banco);
				logger.info("Redireccion a transbank");
				return "jsp/transbank_redirect.jsp";
			}

			logger.info("URL DEL CONTROLADOR: " + urlBase + "/portal/portlets/pagos/redirect8_1.jsp?ID_OPERACION="
					+ idOperacion2 + "&URL_BCO=" + bancoUrl + util.devuelveRet() + "&COD_BCO=" + banco2);
			response.sendRedirect(urlBase + "/portal/portlets/pagos/redirect8_1.jsp?ID_OPERACION=" + idOperacion2
					+ "&URL_BCO=" + bancoUrl + util.devuelveRet() + "&COD_BCO=" + banco2);

		} catch (Exception e) {
			logger.error("Error al guardar Deudas en lista pago: " + e.getMessage());
			e.printStackTrace();
		}

		
		model.addAttribute("mensaje", "Problemas al guardar la información de pago.");
		return "jsp/error.jsp";
	}

	@RequestMapping(value = "/notificacionPago", method = { RequestMethod.GET, RequestMethod.POST })
	public String notificacionPago(HttpServletRequest request, HttpServletResponse response, HttpSession session,ModelMap model) throws IOException {
	//public String notificacionPago(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

		UtilsCarrito utilsCarrito = new UtilsCarrito();
		JSONObject jSonParam = new JSONObject();
		JSONObject jSonData = null;
		PropertiesFile.cargarArchivo();
		boolean swForm = false;
		String urlAWSResultTrx = PropertiesFile.botonPagoResultado;
		String redirectHome = PropertiesFile.redirectHome;
		BigDecimal formulario = null;
		String redireccion = "";

		
		try {

			String idCarrito = utilsCarrito.getIdCarrito(request);

			if (idCarrito != null && idCarrito.length() > 0) {
				// si tengo un valor de cookie quiere decir que existe la
				// posibilidad de carrito y borro

				CarritoCidWsService service = new CarritoCidWsService();
				CarritoCidWs port = service.getCarritoCidWsPort();
				port.eliminaCarritoSession(idCarrito);
				logger.info("Borro carrito");
			}
		} catch (Exception e) {
			logger.error("Error al borrar carrito");
			e.printStackTrace();
		}

		NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "CL"));
		DecimalFormat formatter = (DecimalFormat) nf;
		String idOpCarro = request.getParameter("ID");
		BigDecimal estadoPagoCarrito = null;
		boolean carro = false;
		logger.info("ID OPERACION CARRO DESDE BANCO: " + idOpCarro);

		/**
		 * if para consultar si viene nulo desde TBK al apretar boton anular
		 * (viene null como string)
		 */
		if (idOpCarro.compareTo("null") == 0) {
			model.addAttribute("redirectHome", redirectHome);
			return "jsp/mensajeBco.jsp";
		}

		if (idOpCarro != null) {

			/**
			 * Verificar si idOperacion esta pagado
			 */

			ConsultaPagoIdOpOutVO consultaPagoIdOpOutVO = null;
			ConsultaPagoIdOpInVO consultaPagoIdOpInVO = new ConsultaPagoIdOpInVO();
			consultaPagoIdOpInVO.setIdopIn(new BigDecimal(idOpCarro));

			try {
				consultaPagoIdOpOutVO = delegate.consultaPagoIdOp(consultaPagoIdOpInVO);
				logger.info("consultaPagoIdOpOutVO: " + consultaPagoIdOpOutVO.getCursorConsultaPago().size());
				for (ConsultaPagoIdOpCursorConsultaPagoVO consultaPago : consultaPagoIdOpOutVO
						.getCursorConsultaPago()) {
					estadoPagoCarrito = consultaPago.getIdEstado();
					logger.info("estado de pago carrito : " + consultaPago.getIdEstado());
				}

			} catch (Exception e1) {
				e1.printStackTrace();
				model.addAttribute("mensaje", "error al tratar de obtener el estado de pago del id operacion");
				return "jsp/error.jsp";
			}

			if (estadoPagoCarrito != null && estadoPagoCarrito.compareTo(new BigDecimal(12)) == 0) {

				DetalleCarritoInVO detalleCarritoVO = new DetalleCarritoInVO();
				DetalleCarritoOutVO salidaCarrito = null;
				CarritoVO carritoVo = null;
				detalleCarritoVO.setIdI(new BigDecimal(idOpCarro));
				BigDecimal idTranCarrito = null;
				String banco = null;
				String fechaPago = null;
				List<CarritoVO> carritoList = new ArrayList<CarritoVO>();
				List<DetallePagosVO> detalleList = new ArrayList<DetallePagosVO>();

				PropertiesFile prop = new PropertiesFile();

				//String urlRedirec = null;
				String idExterno = null;
				String tipoMoneda = "";
				BigDecimal totalAPagar = new BigDecimal(0);

				try {
					salidaCarrito = delegate.detalleCarrito(detalleCarritoVO);
					for (DetalleCarritoCursorDetalleCarritoVO carrito : salidaCarrito.getCursorDetalleCarrito()) {
						logger.info("************************* detalle carrito *************************");
						logger.info("id transaccion: " + carrito.getIdTransaccion());
						logger.info("id Externo: " + carrito.getIdExterno());
						logger.info("rut_rol:" + carrito.getRutRol());
						logger.info("formulario: " + carrito.getFormulario());
						logger.info("folio: " + carrito.getFolio());
						logger.info("fecha vencimiento: " + carrito.getFechaVencimiento());
						logger.info("moneda: " + carrito.getNemo());
						logger.info("total pagar: " + carrito.getTotalPagar());

						BigDecimal idOperacionDps = carrito.getIdTransaccion();
						BigDecimal idTransaccionDps = new BigDecimal(idOpCarro);
						formulario = carrito.getFormulario();

						logger.info("idOperacionDps: " + idTransaccionDps);
						logger.info("idTransaccionDps: " + idOperacionDps);
						model.addAttribute("idOperacionDps", idTransaccionDps);
						model.addAttribute("idTransaccionDps", idOperacionDps);

						/**
						 * agrega if para mostrar notificaciones de pago sin
						 * carrito
						 */
						if (carrito.getFormulario().intValue() == 698) {
							carro = true;

						}

						/**
						 * Redireccion a pagina girador
						 */

						if (carrito.getIdExterno() != null) {
							redireccion = prop.cargarParametro("F" + carrito.getFormulario().toString());
							idExterno = carrito.getIdExterno();
							logger.info("urlRedirec : " + redireccion);
							logger.info("idExterno : " + idExterno);
							if (redireccion == null || redireccion == ""){
								logger.info("urlRedirect es null" + redireccion);
							}else {
								logger.info("urlRedirect no es null" + redireccion);
							}
						}

						carritoVo = new CarritoVO();
						carritoVo.setIdTransaccion(carrito.getIdTransaccion().toString());
						if (carrito.getTipoContribuyente().equals("1")) {
							carritoVo.setRutRol(utils.devuelveRutDv(carrito.getRutRol().toString()));
						} else {
							carritoVo.setRutRol(utils.devuelveRol(carrito.getRutRol()));
						}
						carritoVo.setFormulario(carrito.getFormulario().toString());
						carritoVo.setFolio(carrito.getFolio().toString());
						carritoVo.setFechaVencimiento(utils.devuelveVctoDate(carrito.getFechaVencimiento()));
						carritoVo.setNemo(carrito.getNemo());
						/**************************/
						totalAPagar = totalAPagar.add(carrito.getTotalPagar());
						/*************************/
						String vTotal = carrito.getTotalPagar().toString();
						String vTotal1 = formatter.format(Double.parseDouble(vTotal));
						carritoVo.setTotalPagar(vTotal1);
						carritoList.add(carritoVo);

						idTranCarrito = carrito.getIdTransaccion();
						banco = carrito.getNombre();
						fechaPago = utils.devuelveVctoHr(carrito.getFechaPago());
						tipoMoneda = carrito.getNemo();

					} 

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("error al tratar de traer datos del carrito: ", e);
				}

				/************************************/
				jSonData = new JSONObject();
				try {
					jSonData.put("idOperacion", idOpCarro);
					jSonData.put("moneda", tipoMoneda);
					jSonData.put("recaudador", banco);
					jSonData.put("formulario", formulario);
					jSonData.put("estado", "Pago Internet TGR");
					//agregar el idExt a solicitud de Arquitectura 29/05/2020
					jSonData.put("idExt", idExterno);
			
					String[] fecha = fechaPago.split(" ");
					if (fecha.length == 2) {
						jSonData.put("fecha", fecha[0]);
						jSonData.put("hora", fecha[1]);
					} else {
						jSonData.put("fecha", fechaPago);
						jSonData.put("hora", "");
					}

					jSonData.put("clientEmail", "");
					jSonData.put("total", totalAPagar.toString());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				/*************************************/

				model.addAttribute("idExterno", idExterno);
				model.addAttribute("carrito", carritoList);
				model.addAttribute("idTranCarrito", idTranCarrito);
				model.addAttribute("banco", banco);
				model.addAttribute("fechaPago", fechaPago);
				model.addAttribute("tipoMoneda", tipoMoneda);

				DetalleDeudasInVO detalleDeudaVO = new DetalleDeudasInVO();
				DetalleDeudasOutVO salidaDeudas = null;
				DetallePagosVO detallePagos = null;

				logger.info("ID transaccion a buscar en detalle deuda: " + idTranCarrito);
				if (formulario.compareTo(new BigDecimal(699)) == 1) {
					detalleDeudaVO.setIdI(new BigDecimal(idOpCarro));
				}else {
					detalleDeudaVO.setIdI(idTranCarrito);
				}
				
				int cant = 0;

				String rutMinas = null;
				String formularioMinas = null;
				String folioMinas = null;

				try {
					salidaDeudas = delegate.detalleDeudas(detalleDeudaVO);
					for (DetalleDeudasCursorDetalleDeudasVO deudas : salidaDeudas.getCursorDetalleDeudas()) {
						logger.info("DEUDA NUMERO : " + cant);
						logger.info("id transaccion : " + deudas.getIdTransaccion());
						logger.info("rut rol : " + deudas.getRutRol());
						logger.info("formulario : " + deudas.getFormulario());
						logger.info("folio : " + deudas.getFolio());

						rutMinas = deudas.getRutRol().toString();
						formularioMinas = deudas.getFormulario().toString();
						folioMinas = deudas.getFolio().toString();

						logger.info("fecha vencimiento : " + deudas.getFechaVencimiento());
						logger.info("moneda : " + deudas.getNemo());
						logger.info("total pagar : " + deudas.getTotalPagar());
						logger.info("**********************************************");
						detallePagos = new DetallePagosVO();
						detallePagos.setIdTransaccion(deudas.getIdTransaccion().toString().trim());

						if (deudas.getTipoContribuyente().equals("1")) {
							detallePagos.setRutRol(utils.devuelveRutDv(deudas.getRutRol().toString()));
						} else {
							detallePagos.setRutRol(utils.devuelveRol(deudas.getRutRol()));
						}

						detallePagos.setFormulario(deudas.getFormulario().toString());
						detallePagos.setFolio(deudas.getFolio().toString());
						detallePagos.setFechaVencimiento(utils.devuelveVctoDate(deudas.getFechaVencimiento()));
						detallePagos.setNemo(deudas.getNemo());
						String vTotal = deudas.getTotalPagar().toString();
						String vTotal1 = formatter.format(Double.parseDouble(vTotal));
						detallePagos.setTotalPagar(vTotal1);
						detalleList.add(detallePagos);
						cant++;

						/*
						 * Consulta si el formulario corresponde a los
						 * configurados en properties.
						 */
				
						
						String canal = utilsCarrito.getCookie(request, "contribucionesID");
						logger.info("Parametro canal en Cookie: " + canal);
						
						if (canal == null){
							if (UtilsCarrito.buscarValores(deudas.getFormulario().toString(), PropertiesFile.botonPagoForms)) {
								swForm = true; 
							}

						}else {
							if (UtilsCarrito.buscarValores(canal, PropertiesFile.botonPagoForms)) {
								swForm = true; 
							}
						}	
					}
					
					
					jSonData.put("cantidad", cant);
					
					if (PropertiesFile.botonPagoActivo && swForm) {
						/**********************************/
						jSonData.put("cantidad", cant);
						/**********************************/
					} else {
						model.addAttribute("detalle", detalleList);
						model.addAttribute("cantidad", cant);
					}

					/**
					 * agrega if para mostrar notificaciones de pago sin
					 * carrito.
					 */
					logger.info("carro: " + carro);

					// Redirecciones modulos (AduanaAgente, aduanaImportador,
					// impuestos fiscales, cuotas convenios, contribuciones por
					// rut)

					String esPatenteMinera = "";
					String codGirador = null;

					try {
						ConsultaGirosGiradorInVO cgg = new ConsultaGirosGiradorInVO();
						cgg.setFolioIn(new BigDecimal(folioMinas));
						cgg.setFormularioIn(new BigDecimal(formularioMinas));
						cgg.setRutIn(new BigDecimal(rutMinas));
						ConsultaGirosGiradorOutVO codGiradorSalida = delegate.consultaGirosGirador(cgg);
						codGirador = codGiradorSalida.getIdGiradorOut();

						logger.info("codigo girador patente: " + codGirador);
						if (codGirador.equals("851")) {
							redireccion = PropertiesFile.urlBase+"/appPatentesMinerasInternetWeb/Verificacion_de_pagos/redirCert.do";
							esPatenteMinera = "S";
							model.addAttribute("esPatenteMinera", esPatenteMinera);
						} else if (codGirador.equals("701")) { // Se agregar
																// para
																// redireccion
																// del poder
																// judicial
							redireccion = prop.cargarParametro("F93");
						} else {
							if (redireccion == null){
								redireccion = utils.redireccionesPortal(idTranCarrito, request, response);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Error ConsultaGirosGiradorInVO", e);
						codGirador = "0";
						if (codGirador.equals("851")) {
							redireccion = PropertiesFile.urlBase+"/appPatentesMinerasInternetWeb/Verificacion_de_pagos/redirCert.do";
							esPatenteMinera = "S";
							model.addAttribute("esPatenteMinera", esPatenteMinera);
						} else {
							redireccion = utils.redireccionesPortal(idTranCarrito, request, response);
						}
					}
					
					logger.info("redireccion 1 ==>  " + redireccion);
					
					if (redireccion == null || redireccion == ""){
						redireccion = redirectHome;
					}else {
						redireccion = redireccion+"?idExt="+ idExterno;
					}
					
					
					logger.info("redireccion 2 ==>  " + redireccion);

					/**
					 * borra cookie de parametro de contribuciones
					 */
					Cookie ck=new Cookie("contribucionesID","");//deleting value of cookie  
					ck.setMaxAge(0);//changing the maximum age to 0 seconds  
					response.addCookie(ck);//

					if (carro == false) {
						
											
						/*
						 * Consulta si el formulario corresponde a los
						 * configurados en properties. Si es verdadero, como
						 * tambiÃ©n la propiedad botonPagoActivo, entonces va a
						 * la nube
						 */
						
						
						if (PropertiesFile.botonPagoActivo && swForm) {
							return "jsp/notificacionDPS.jsp";
							
						} else {
							jSonParam.put("status", 1);
							jSonParam.put("msg", "null");
							jSonParam.put("endUrl", redireccion);
							jSonParam.put("data", jSonData);
							// Limpia model, para no enviar mas parÃ¡metros
							model.clear();
							
							return "redirect:" + urlAWSResultTrx
									+ UtilsBase64.encodeBase64(jSonParam.toString().getBytes());
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("error al tratar de traer datos de las deudas ", e);
				}
				/*
				 * Consulta si el formulario corresponde a los configurados en
				 * properties. Si es verdadero, como tambiÃ©n la propiedad
				 * botonPagoActivo, entonces se que en tierra
				 */
				if (PropertiesFile.botonPagoActivo && swForm) {
					return "jsp/notificacion.jsp";
				} else {
					if (jSonData != null) {
						try {
							jSonParam.put("status", 1);
							jSonParam.put("msg", "null");
							jSonParam.put("endUrl", redireccion);
							jSonParam.put("data", jSonData);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						try {
							jSonParam.put("status", 0);
							jSonParam.put("msg", "null");
							jSonParam.put("endUrl", redireccion);
							jSonParam.put("data", "null");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					// Limpia model, para no enviar mas parÃ¡metros
					model.clear();
					logger.info("JsonParam ==>  " + jSonParam.toString());
					return "redirect:" + urlAWSResultTrx + UtilsBase64.encodeBase64(jSonParam.toString().getBytes());
				}
			} else {
				model.addAttribute("redirectHome", redirectHome);
				return "jsp/mensajeBco.jsp"; // return de mensaje por id carrito
												// = 1
			}

		}
		
		model.addAttribute("mensaje", "Error al cargar la notificacion de pago");
		/*
		 * Consulta si el formulario corresponde a los configurados en
		 * properties. Si es verdadero, como tambiÃ©n la propiedad
		 * botonPagoActivo, entonces se queda en tierra
		 */
		if (PropertiesFile.botonPagoActivo && swForm) {
			return "jsp/error.jsp";
		} else {
			try {
				jSonParam.put("staus", 0);
				jSonParam.put("msg", "Error al cargar la notificacion de pago");
				jSonParam.put("data", "null");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// Limpia model, para no enviar mas parÃ¡metros
			model.clear();
			return "redirect:" + urlAWSResultTrx + UtilsBase64.encodeBase64(jSonParam.toString().getBytes());
		}
	}

	@RequestMapping(value = "/comprobanteTransaccion", method = { RequestMethod.GET, RequestMethod.POST })
	public String comprobanteTransaccion(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws IOException {

		NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "CL"));
		DecimalFormat formatter = (DecimalFormat) nf;

		String idOp = request.getParameter("idOperacion");
		String idTr = request.getParameter("idTransaccion");

		logger.info("idOp: " + idOp);
		logger.info("idTr: " + idTr);
		PkgRegistroDeudasPagos11gDelegate delegate = new PkgRegistroDeudasPagos11gDelegate();
		ComprobanteTransaccionInVO entrada = new ComprobanteTransaccionInVO();
		DetallePagosVO detallePagos = null;
		ComprobanteTransaccionOutVO salida = null;
		entrada.setIdopIn(new BigDecimal(idOp));
		entrada.setIdtrIn(new BigDecimal(idTr));
		PropertiesFile prop = new PropertiesFile();

		model.addAttribute("PATHIMGFIRMA", prop.cargarRuta("PathImgFirma"));
		model.addAttribute("PATHIMGTIMBRE", prop.cargarRuta("PathImgTimbre"));
		try {
			salida = delegate.comprobanteTransaccion(entrada);
			if (salida.getCursorComprobante().size() == 0) {
				model.addAttribute("mensaje", "Transacción de Pago no válido para extracción de comprobante.");
				return "jsp/error.jsp";
			} else {
				for (ComprobanteTransaccionCursorComprobanteVO datos : salida.getCursorComprobante()) {
					detallePagos = new DetallePagosVO();
					if (datos.getTipoContribuyente().equals("1")) {
						detallePagos.setRutRol(utils.devuelveRutDv(datos.getRutRol().toString()));
					} else {
						detallePagos.setRutRol(utils.devuelveRol(datos.getRutRol()));
					}
					detallePagos.setFormulario(datos.getFormulario().toString());
					detallePagos.setFolio(datos.getFolio().toString());
					detallePagos.setFechaVencimiento(datos.getFechaVencimiento());
					detallePagos.setFechaPago(datos.getFechaPago());
					detallePagos.setNemo(datos.getIsoMoneda());
					String vTotal = datos.getTotalPagar().toString();
					String vTotal1 = formatter.format(Double.parseDouble(vTotal));
					detallePagos.setTotalPagar(vTotal1);
					detallePagos.setInstitucion(datos.getNombre());
					detallePagos.setIdTransaccion(datos.getIdOperacion().toString().substring(8, 16) + "-"
							+ datos.getIdTransaccion().toString().substring(8, 16));
					detallePagos.setCodigoBarra(datos.getIdLiquidacion());
				}

				model.addAttribute("listaDatos", detallePagos);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error comprobante de transaccion: " + e.getMessage());
		}
		return "jsp/comprobante.jsp";
	}

	@RequestMapping(value = "/liquidaPatente", method = { RequestMethod.GET, RequestMethod.POST })
	public String liquidaPatente(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws IOException {

		IngresarArResult ingresarAr = null;
		String check = "on,";
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		ConsultaGiroExternoDetalleOutVO giroExternoDetalleOut = null;
		ConsultaIdLiqPatentesInVO entrada = new ConsultaIdLiqPatentesInVO();
		ConsultaGiroExternoDetalleInVO giroExternoDetalleIn = new ConsultaGiroExternoDetalleInVO();
		ConsultaGiroPatenteInVO entradaPatente = new ConsultaGiroPatenteInVO();

		String idCarritoExterno = request.getParameter("idCarritoExterno");

		StringBuffer checkFinal = new StringBuffer();
		try {
			entrada.setIdCarritoIn(new BigDecimal(idCarritoExterno));
			ConsultaIdLiqPatentesOutVO salida = delegate.consultaIdLiqPatentes(entrada);

			logger.info("largo coleccion de patentes: " + salida.getCursorGiroExterno().size());
			checkFinal.append(check);
			if (salida.getCursorGiroExterno().size() > 0) {
				for (ConsultaIdLiqPatentesCursorGiroExternoVO patente : salida.getCursorGiroExterno()) {
					logger.info("********************************");
					logger.info("RUT: " + patente.getRutRol());
					logger.info("FORMULARIO: " + patente.getFormulario());
					logger.info("FOLIO: " + patente.getFolio());
					logger.info("VENCIMIENTO: " + patente.getFechaVencimiento());
					
					cal2.setTime(patente.getFechaVencimiento());

					entradaPatente.setRutIn(patente.getRutRol());
					entradaPatente.setFormularioIn(patente.getFormulario());
					entradaPatente.setFolioIn(patente.getFolio());
					entradaPatente.setFechaVctoIn(patente.getFechaVencimiento());
					ConsultaGiroPatenteOutVO salidaPatente = delegate.consultaGiroPatente(entradaPatente);
					logger.info("LARGO CONSULTA PATENTE TABLA GIRO: " + salidaPatente.getCursorGiroPatente().size());
					String version = "";
					if (salidaPatente.getCursorGiroPatente().size() > 0) {
						for (ConsultaGiroPatenteCursorGiroPatenteVO ge : salidaPatente.getCursorGiroPatente()) {
							try {
								giroExternoDetalleIn.setRutIn(ge.getRut());
								giroExternoDetalleIn.setFormularioIn(ge.getFormulario());
								giroExternoDetalleIn.setFolioIn(ge.getFolio());
								giroExternoDetalleIn.setFechaVctoIn(ge.getFechaVencimiento());
								giroExternoDetalleIn.setIdConvenioIn(new BigDecimal(0));
								giroExternoDetalleOut = delegate.consultaGiroExternoDetalle(giroExternoDetalleIn);
								RecaItems[] vectorItems = items.generaItems(giroExternoDetalleOut);
								if (ge.getFormulario().compareTo(new BigDecimal(10)) == 0) {
									version = "C";
									logger.info("ES F" + ge.getFormulario() + " version C");
								} else {
									version = "A";
									logger.info("ES F" + ge.getFormulario() + " version A");
								}
								ingresarAr = consultaDeudasPortal.ingresar700Reca(cal, cal, cal, new BigDecimal(5),
										ge.getIdGirador().toString(), new BigDecimal(1), ge.getRut(),
										utils.devuelveDV(ge.getRut()), ge.getFormulario(), version, ge.getFolio(), null,
										cal, cal2, vectorItems, ge.getIdMoneda(), ge.getMontoNeto(), ge.getTotalPagar(),
										ge.getReajuste(), ge.getInteresMulta(), ge.getInteresMulta(), ge.getReajuste(),
										null);

								if (ingresarAr.getResultCode().equals(IngresarArResult.TRX_ERROR)) {
									throw new Exception("Error en IngresarAr: " + ingresarAr.getResultMessage());
								} else {
									logger.info("GuardarDeuda - insertBD: CODIGO DE BARRA : "
											+ ingresarAr.getCodigoBarra());
									logger.info("GuardarDeuda - insertBD: CODIGO RESULTADO : "
											+ ingresarAr.getResultCode());
									logger.info("GuardarDeuda - insertBD: MENSAJE SALIDA: "
											+ ingresarAr.getResultMessage());
									check = ingresarAr.getCodigoBarra();
									checkFinal.append(" " + check);
									checkFinal.append(",");
								}
							} catch (Exception e) {
								logger.error("GuardarDeuda - insertBD: Error en ingresar700Reca: " + e.getMessage());
								throw new Exception("Error en ingresar700Reca: " + e.getMessage());
							}
						}
					} else {
						logger.info("no hay datos a procesar");
					}
				}
				logger.info("check final: " + checkFinal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/enviaMedioPago?listaContribuciones=" + checkFinal;
	}
}
