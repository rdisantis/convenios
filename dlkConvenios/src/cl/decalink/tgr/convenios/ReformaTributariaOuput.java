package cl.decalink.tgr.convenios;

import java.io.Serializable;

/**
Clase encargada de Seters and geters para utilizar caracteristicas de Reforma Tributaria Output
ademas tiene logica contenida para agrupar configuraciones y ser la clase encargada de retornar valores luego de ser procesados
la logica del nuevo arbol de condonaci�n.
* @author  Cristian Candia Cid
* @since  1.0
*/
public class ReformaTributariaOutput implements Serializable {

	private Integer maxCuota;
	private Integer maxCondonacionI;
	private Integer maxCondonacionM;
	private Integer minCuotaContado;
	private Integer codError;
	private String descError;
	private Double beneficioPp=new Double(0);
	
	/**
	 * Retorna valor de beneficio pronto pago
	 * @return
	 */
	public Double getBeneficioPp() {
		return beneficioPp;
	}
	
	/**
	 * Setea valor de beneficio pronto pago
	 * @param beneficioPp
	 */
	public void setBeneficioPp(Double beneficioPp) {
		this.beneficioPp = beneficioPp;
	}
	
	/**
	 * Retorna maxima cantidad de cuotas para un convenio
	 * @return
	 */
	public Integer getMaxCuota() {
		return maxCuota;
	}
	
	/**
	 * Setea maxima cantidad de cuotas para un convenio
	 * @param maxCuota
	 */
	public void setMaxCuota(Integer maxCuota) {
		this.maxCuota = maxCuota;
	}
	
	/**
	 * Retorna maxima condonacion intereses (la condonaci�n es solo una, tanto para intereses u multas)
	 * @return
	 */
	public Integer getMaxCondonacionI() {
		return maxCondonacionI;
	}
	
	/**
	 * Setea maxima condonaci�n intereses
	 * @param maxCondonacionI
	 */
	public void setMaxCondonacionI(Integer maxCondonacionI) {
		this.maxCondonacionI = maxCondonacionI;
	}
	/**
	 * Retorna maxima condonacion multas (la condonaci�n es solo una, tanto para intereses u multas)
	 * @return
	 */
	public Integer getMaxCondonacionM() {
		return maxCondonacionM;
	}
	
	/**
	 * Setea maxima condonaci�n multas
	 * @param maxCondonacionM
	 */
	public void setMaxCondonacionM(Integer maxCondonacionM) {
		this.maxCondonacionM = maxCondonacionM;
	}
	
	/**
	 * Retorna minimo porcentaje de cuota contado
	 * @return
	 */
	public Integer getMinCuotaContado() {
		return minCuotaContado;
	}
	
	/**
	 * Setea minimo porcentaje de cuota contado
	 * @param minCuotaContado
	 */
	public void setMinCuotaContado(Integer minCuotaContado) {
		this.minCuotaContado = minCuotaContado;
	}
	
	/**
	 * Retorna codigo de error servicio generico
	 * @return
	 */
	public Integer getCodError() {
		return codError;
	}
	
	/**
	 * Setea codigo de error servicio generico
	 * @param codError
	 */
	public void setCodError(Integer codError) {
		this.codError = codError;
	}
	
	/**
	 * Retorna descripci�n de error servicio generico
	 * @return
	 */
	public String getDescError() {
		return descError;
	}
	
	/**
	 * Setea descripci�n de error servicio generico
	 * @param descError
	 */
	public void setDescError(String descError) {
		this.descError = descError;
	}
}