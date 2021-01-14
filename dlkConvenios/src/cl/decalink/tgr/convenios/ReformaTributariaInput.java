package cl.decalink.tgr.convenios;

import java.io.Serializable;
/*
 * ejemplojavadoc.java
 *
 * Creado 06/11/2015
 */

/**
Clase encargada de Seters and geters para utilizar caracteristicas de Reforma Tributaria Input
ademas tiene logica contenida para agrupar configuraciones y poder centralizar las variables, su finalidad es concetrar los
input para el correcto funcionamiento del arbol de desiciones 
* @author  Cristian Candia Cid
* @since  1.0
*/
public class ReformaTributariaInput implements Serializable {
    
	private Boolean isReformaTributaria;
	private Boolean pagoVoluntario;
	private Boolean pagoTotalConvenio;
	private Boolean pagoPresencial;
	private Boolean beneficioInternet;
	private Boolean beneficioPyme;
	private Boolean tieneGarantia;
	private Boolean isCanalPresencial;
	private Integer comportamientoConvenio;
	private Integer rutContribuyente;
	private Integer antiguedadDeuda;
	private Integer codConvenios=null;
	private Boolean pagoContado;
	private String contextoAmbiente;
	private Boolean isIntranet;
	
	/**
	 * Obtiene codigo de convenio
	 * @return 
	 */
	public Integer getCodConvenios() {
		return codConvenios;
	}
	/**
	 * Setea codigo de convenios
	 * @param codConvenios
	 */
	public void setCodConvenios(Integer codConvenios) {
		this.codConvenios = codConvenios;
	}
	/**
	 * Obtiene Rut contribuyente
	 * @return
	 */
	public Integer getRutContribuyente() {
		return rutContribuyente;
	}
	
	/**
	 * Setea Rut contribuyente
	 * @param rutContribuyente
	 */
	public void setRutContribuyente(Integer rutContribuyente) {
		this.rutContribuyente = rutContribuyente;
	}
	/**
	 * Obtiene  valor pago voluntario
	 * @return
	 */
	public Boolean getPagoVoluntario() {
		return pagoVoluntario;
	}
	/**
	 * Setea pago voluntario
	 * @param pagoVoluntario
	 */
	public void setPagoVoluntario(Boolean pagoVoluntario) {
		this.pagoVoluntario = pagoVoluntario;
	}
	/**
	 * Obtiene pago total convenio
	 * @return
	 */
	public Boolean getPagoTotalConvenio() {
		return pagoTotalConvenio;
	}
	/**
	 * Setea pago total convenio
	 * @param pagoTotalConvenio
	 */
	public void setPagoTotalConvenio(Boolean pagoTotalConvenio) {
		this.pagoTotalConvenio = pagoTotalConvenio;
	}
	/**
	 * Obtiene pago presencial
	 * @return
	 */
	public Boolean getPagoPresencial() {
		return pagoPresencial;
	}
	/**
	 * Setea pago presencial
	 * @param pagoPresencial
	 */
	public void setPagoPresencial(Boolean pagoPresencial) {
		this.pagoPresencial = pagoPresencial;
	}
	/**
	 * Obtiene beneficio internet
	 * @return
	 */
	public Boolean getBeneficioInternet() {
		return beneficioInternet;
	}
	
	/**
	 * Sete beneficio intrnet
	 * @param beneficioInternet
	 */
	public void setBeneficioInternet(Boolean beneficioInternet) {
		this.beneficioInternet = beneficioInternet;
	}
	
	/**
	 * Obtiene beneficio pyme
	 * @return
	 */
	public Boolean getBeneficioPyme() {
		return beneficioPyme;
	}
	
	/**
	 * Setea beneficio pyme
	 * @param beneficioPyme
	 */
	public void setBeneficioPyme(Boolean beneficioPyme) {
		this.beneficioPyme = beneficioPyme;
	}
	
	/**
	 * Obtiene garantia
	 * @return
	 */
	public Boolean getTieneGarantia() {
		return tieneGarantia;
	}
	
	/**
	 * Setea garantia
	 * @param tieneGarantia
	 */
	public void setTieneGarantia(Boolean tieneGarantia) {
		this.tieneGarantia = tieneGarantia;
	}
	
	/**
	 * Obtiene comportamiento
	 * @return
	 */
	public Integer getComportamientoConvenio() {
		return comportamientoConvenio;
	}
	
	/**
	 * Setea comportamiento
	 * @param comportamientoConvenio
	 */
	public void setComportamientoConvenio(Integer comportamientoConvenio) {
		this.comportamientoConvenio = comportamientoConvenio;
	}
	/**
	 * Obtiene si es reforma tributaria
	 * @param isReformaTributaria
	 */
	public void setIsReformaTributaria(Boolean isReformaTributaria) {
		this.isReformaTributaria = isReformaTributaria;
	}
	
	/**
	 * Setea si es reforma tributaria
	 * @return
	 */
	public Boolean getIsReformaTributaria() {
		return isReformaTributaria;
	}
	
	/**
	 * Obtiene si es canal presencial
	 * @param isCanalPresencial
	 */
	public void setIsCanalPresencial(Boolean isCanalPresencial) {
		this.isCanalPresencial = isCanalPresencial;
	}
	
	/**
	 * Setea si es canal presencial
	 * @return
	 */
	public Boolean getIsCanalPresencial() {
		return isCanalPresencial;
	}	
	
	/**
	 * Obtiene antiguedad
	 * @param antiguedadDeuda
	 */
	public void setAntiguedadDeuda(Integer antiguedadDeuda) {
		this.antiguedadDeuda = antiguedadDeuda;
	}
	
	/**
	 * Setea antiguedad
	 * @return
	 */
	public Integer getAntiguedadDeuda() {
		return antiguedadDeuda;
	}	
	
	//seteo especial
	
	/**
	 * Metodo encargado de devolver de forma predefinida cono es necesario para el servicio de base de datos y corresponde al tag comportamiento por ej ID_COMPORTAMIENTO= si es nulo el valor concatenado es 0. Utilizado en metodo getComporConv perteneciente al ejb GenereaPropuestaBean.
	 */
	public String comportamiento(){
      
	  if (this.getComportamientoConvenio()!=null){
		  return "ID_COMPORTAMIENTO="+this.getComportamientoConvenio();
	  }else{
		  return "ID_COMPORTAMIENTO=0";
	  }

	}
	
	/**
	 * Metodo encargado de devolver de forma predefinida cono es necesario para el servicio de base de datos
	 * y corresponde al tag pagoTotalConvenio por ej. "ID_TIPO_CONVENIO=".
	 * Utilizado en metodo getCondonacionConv perteneciente al ejb GenereaPropuestaBean.
	 */	
	public String pagoTotalConvenio(){
		int inTipoConvenio=0;
		
		if (this.getPagoTotalConvenio()!=null){
	        if (this.getPagoTotalConvenio().booleanValue()){
	      	  inTipoConvenio=1;
	        }else{
	      	  inTipoConvenio=2;          
	        }		
		}     
	      return "ID_TIPO_CONVENIO="+inTipoConvenio; 

		}
	
	/**
	 * Metodo encargado de devolver de forma predefinida cono es necesario para el servicio de base de datos
	 * y corresponde al tag garantia por ej. "ID_EMBARGO=".
	 * Utilizado en metodo getComporConv perteneciente al ejb GenereaPropuestaBean.
	 */		
	public String garantia(){
		int inEmbargo=0;
		
		if (this.getTieneGarantia()!=null){
	        if (this.getTieneGarantia().booleanValue()){
	      	  inEmbargo=1;
	        }else{
	      	  inEmbargo=2;     
	        }
		}
	      
	      return "ID_EMBARGO="+inEmbargo; 

		}	

	/**
	 * @deprecated	
	 * Metodo encargado de devolver de forma predefinida cono es necesario para el servicio de base de datos
	 * y corresponde al tag pyme por ej. "ID_EMPRESA_MYPE=".
	 * Utilizado en metodo getComporConv perteneciente al ejb GenereaPropuestaBean.
	 */	
	public String pyme(){
		int inPyme=0;
		
		if (this.getBeneficioPyme()!=null){
	        if (this.getBeneficioPyme().booleanValue()){
	      	  inPyme=1;
	        }else{
	      	  inPyme=2;     
	        } 
		}  
	      return "ID_EMPRESA_MYPE="+inPyme; 

		}

	/**
	 * Metodo encargado de devolver de forma predefinida como es necesario para el servicio de base de datos
	 * y corresponde al tag canal por ej. "ID_CANAL=".
	 * OJO este tag yo no se utiliza parametrico, debido a que existen la forma default que elimina este tag
	 * Utilizado en metodo registraPoliticasCond perteneciente al ejb GenereaPropuestaBean.
	 */	
	public String canal(){
		int inCanal=0;
		
		if (this.getIsCanalPresencial()!=null){
	        if (this.getIsCanalPresencial().booleanValue()){
	        	inCanal=1;
	        }else{
	        	inCanal=2;    /*internet*/ 
	        } 
		}  
	      return "ID_CANAL="+inCanal; 

		}

	/**
	 * Metodo encargado de devolver de forma predefinida cono es necesario para el servicio de base de datos
	 * y corresponde al tag antiguedad por ej. "ID_ANTIGUEDAD=".
	 * Utilizado en metodo getCondonacionConv perteneciente al ejb GenereaPropuestaBean.
	 */	
	public String antiguedad(){

		if (this.getAntiguedadDeuda()!=null){
	      return "ID_ANTIGUEDAD="+this.getAntiguedadDeuda();
		}else{
		  return "ID_ANTIGUEDAD=0";	
		}

	}
	
    /**
     * setea pago contado
     * @param pagoContado
     */
	public void setPagoContado(Boolean pagoContado) {
		this.pagoContado = pagoContado;
	}
	
	/**
	 * Obtiene pago contado
	 * @return
	 */
	public Boolean getPagoContado() {
		return pagoContado;
	}
	
	/**
	 * Setea contexto ambiente
	 * @param contextoAmbiente
	 */
	public void setContextoAmbiente(String contextoAmbiente) {
		this.contextoAmbiente = contextoAmbiente;
	}
	
	/**
	 * Obtiene contexto ambiente
	 * @return
	 */
	public String getContextoAmbiente() {
		return contextoAmbiente;
	}
	
	/**
	 * Setea es intranet
	 * @param isIntranet
	 */
	public void setIsIntranet(Boolean isIntranet) {
		this.isIntranet = isIntranet;
	}
	
	/**
	 * Obtiene si es intranet
	 * @return
	 */
	public Boolean getIsIntranet() {
		return isIntranet;
	}
	
	
	/**
	 * Metodo encargado de devolver de forma predefinida como es necesario para el servicio de base de datos
	 * y corresponde a la marca contexto, para poder obtener valores default, este metodo devuelve CNV_INTRA o CNV_INTER
	 * Utilizado en metodo getCondonacionConv perteneciente al ejb GenereaPropuestaBean.
	 */		
	public String getContextoAmbienteParam(){
		if (this.getIsIntranet()!=null){
			if (this.getIsIntranet().booleanValue()){
				return "CNV_INTRA";
			}else{
				return "CNV_INTER";
			}
		}else{
			return "CNV_INTRA";	
		}
	}
	
	
	/**********************************************************************/
	
	
}