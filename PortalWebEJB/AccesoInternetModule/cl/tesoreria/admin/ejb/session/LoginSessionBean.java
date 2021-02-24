package cl.tesoreria.admin.ejb.session;

import cl.tesoreria.admin.dao.CountBankDAO;
import cl.tesoreria.admin.ejb.entity.AplicacionPerfilSuscriptorLocalHome;
import cl.tesoreria.admin.ejb.entity.BloqueoIpLocalHome;
import cl.tesoreria.admin.ejb.entity.ClienteBienRaizEntityPK;
import cl.tesoreria.admin.ejb.entity.ClienteBienRaizLocal;
import cl.tesoreria.admin.ejb.entity.ClienteBienRaizLocalHome;
import cl.tesoreria.admin.ejb.entity.ClienteLocal;
import cl.tesoreria.admin.ejb.entity.ClienteLocalHome;
import cl.tesoreria.admin.ejb.entity.ComunaLocal;
import cl.tesoreria.admin.ejb.entity.ComunaLocalHome;
import cl.tesoreria.admin.ejb.entity.DatosAccesoPortalLocal;
import cl.tesoreria.admin.ejb.entity.DatosAccesoPortalLocalHome;
import cl.tesoreria.admin.ejb.entity.OlvidoClaveLocal;
import cl.tesoreria.admin.ejb.entity.OlvidoClaveLocalHome;
import cl.tesoreria.admin.ejb.entity.PerfilLocal;
import cl.tesoreria.admin.ejb.entity.PerfilLocalHome;
import cl.tesoreria.admin.ejb.entity.PrivilegioLocal;
import cl.tesoreria.admin.ejb.entity.PrivilegioLocalHome;
import cl.tesoreria.admin.ejb.entity.SuscriptorLocal;
import cl.tesoreria.admin.ejb.entity.SuscriptorLocalHome;
import cl.tesoreria.admin.ejb.entity.impl.BloqueoIpLocal;
import cl.tesoreria.admin.ejb.session.impl.LoginSessionBean;
import cl.tesoreria.busnmodel.AgenteModel;
import cl.tesoreria.busnmodel.BancoModel;
import cl.tesoreria.busnmodel.BienRaizModel;
import cl.tesoreria.busnmodel.CambioDeClaveModel;
import cl.tesoreria.busnmodel.CuentaBancariaModel;
import cl.tesoreria.busnmodel.DatosAccesoPortalModel;
import cl.tesoreria.busnmodel.DireccionModel;
import cl.tesoreria.busnmodel.LoginModel;
import cl.tesoreria.busnmodel.MessageVO;
import cl.tesoreria.busnmodel.PerfilModel;
import cl.tesoreria.busnmodel.PersonaModel;
import cl.tesoreria.busnmodel.PrivilegioModel;
import cl.tesoreria.busnmodel.ResponseVO;
import cl.tesoreria.busnmodel.RolModel;
import cl.tesoreria.busnmodel.SuscriptorModel;
import cl.tesoreria.dao.DAOFactory;
import cl.tesoreria.delegate.AduanaDelegateHandler;
import cl.tesoreria.delegate.ContribucionesDelegateHandler;
import cl.tesoreria.delegate.MasterDelegateHandler;
import cl.tesoreria.exception.LookupException;
import cl.tesoreria.mail.Carta;
import cl.tesoreria.mail.Cartero;
import cl.tesoreria.util.DateUtil;
import cl.tesoreria.util.FormatBusnModel;
import cl.tesoreria.util.LogUtility;
import cl.tesoreria.util.PropertyManager;
import cl.tesoreria.util.ServiceLocator;
import cl.tesoreria.util.StringUtil;
import cl.tesoreria.util.Util;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.log4j.Logger;

public class LoginSessionBean implements SessionBean {
  private SessionContext sessionContext;
  
  private static Logger logger = Logger.getLogger("cl.tesoreria.PortalWebEjb");
  
  public void ejbCreate() throws CreateException {}
  
  public void ejbRemove() {}
  
  public void ejbActivate() {}
  
  public void ejbPassivate() {}
  
  public void setSessionContext(SessionContext paramSessionContext) {
    this.sessionContext = paramSessionContext;
  }
  
  public ResponseVO isValidSuscriptor(LoginModel paramLoginModel) throws Exception {
    ResponseVO responseVO = new ResponseVO(null);
    long l = 0L;
    try {
      l = Long.parseLong(paramLoginModel.getUsername());
    } catch (NumberFormatException numberFormatException) {
      return new ResponseVO(null, new MessageVO(2, "Usuario no v));
    } 
    String str = "";
    try {
      AplicacionPerfilSuscriptorLocalHome aplicacionPerfilSuscriptorLocalHome = (AplicacionPerfilSuscriptorLocalHome)ServiceLocator.getLocalHome(AplicacionPerfilSuscriptorLocalHome.class);
      Collection collection = aplicacionPerfilSuscriptorLocalHome.findByRutApli(new BigDecimal(l), new BigDecimal(1.0D));
      try {
        BloqueoIpLocalHome bloqueoIpLocalHome = (BloqueoIpLocalHome)ServiceLocator.getLocalHome(BloqueoIpLocalHome.class);
        BloqueoIpLocal bloqueoIpLocal = bloqueoIpLocalHome.findByPrimaryKey(paramLoginModel.getIp());
        str = PropertyManager.getInstance().getProperty(1, -2009);
        return new ResponseVO(null, new MessageVO(str + " -> " + bloqueoIpLocal.getFecha().toString(), -2009));
      } catch (FinderException finderException) {
        logger.error("Ha ocurrido un error", (Throwable)finderException);
        str = PropertyManager.getInstance().getProperty(1, -2010);
        LogUtility.log(getClass(), str, 1);
      } catch (Exception exception) {
        logger.error("Ha ocurrido un error", exception);
        str = PropertyManager.getInstance().getProperty(1, -2011);
        LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      } 
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      str = PropertyManager.getInstance().getProperty(1, -2012);
      LogUtility.log(getClass(), str, 3);
      return new ResponseVO(null, new MessageVO(str, -2012));
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      str = PropertyManager.getInstance().getProperty(1, -2013);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      return new ResponseVO(null, new MessageVO(2, str, -2013));
    } 
    try {
      LogUtility.log(getClass(), paramLoginModel.toString(), 1);
      SuscriptorLocalHome suscriptorLocalHome = (SuscriptorLocalHome)ServiceLocator.getLocalHome(SuscriptorLocalHome.class);
      SuscriptorLocal suscriptorLocal = suscriptorLocalHome.findByPrimaryKey(new BigDecimal(l));
      if (suscriptorLocal.getFechaBloqueo() != null) {
        str = PropertyManager.getInstance().getProperty(1, -2008);
        return new ResponseVO(null, new MessageVO(str, -2008));
      } 
      if (suscriptorLocal.getIntentos() == null)
        suscriptorLocal.setIntentos(new BigDecimal(0.0D)); 
      if (suscriptorLocal.getIntentos().intValue() >= 3) {
        str = PropertyManager.getInstance().getProperty(1, -2008);
        responseVO = new ResponseVO(null, new MessageVO(str, -2008));
      } else if (paramLoginModel.getPassword().equals(suscriptorLocal.getPassword())) {
        suscriptorLocal.setIntentos(new BigDecimal(0.0D));
        ClienteLocalHome clienteLocalHome = (ClienteLocalHome)ServiceLocator.getLocalHome(ClienteLocalHome.class);
        ClienteLocal clienteLocal = clienteLocalHome.findByPrimaryKey(suscriptorLocal.getRut());
        ComunaLocal comunaLocal = null;
        try {
          ComunaLocalHome comunaLocalHome = (ComunaLocalHome)ServiceLocator.getLocalHome(ComunaLocalHome.class);
          if (clienteLocal.getIdComuna() != null)
            comunaLocal = comunaLocalHome.findByPrimaryKey(clienteLocal.getIdComuna()); 
        } catch (FinderException finderException) {
          logger.error("Ha ocurrido un error", (Throwable)finderException);
          LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)finderException), 2);
        } catch (Exception exception) {
          logger.error("Ha ocurrido un error", exception);
          LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
        } 
        DireccionModel direccionModel = new DireccionModel();
        direccionModel.setCalle(clienteLocal.getDireccion());
        direccionModel.setCelular(clienteLocal.getCelular());
        direccionModel.setTelefono(clienteLocal.getTelefono());
        if (clienteLocal.getIdComuna() != null)
          direccionModel.setCodComuna(clienteLocal.getIdComuna().intValue()); 
        direccionModel.setEmail(clienteLocal.getEmail());
        direccionModel.setFax(clienteLocal.getFax());
        direccionModel.setCodigoPostal(clienteLocal.getCodigoPostal());
        if (comunaLocal != null)
          direccionModel.setCodRegion(comunaLocal.getRegion().intValue()); 
        PersonaModel personaModel = new PersonaModel(clienteLocal.getRut().longValue());
        personaModel.setApellidos(clienteLocal.getApellidos());
        personaModel.setNombre(clienteLocal.getNombre());
        personaModel.setDv(clienteLocal.getDv());
        personaModel.setSexo(clienteLocal.getSexo());
        personaModel.setDireccion(direccionModel);
        BancoModel bancoModel = null;
        if (clienteLocal.getIdBanco() != null)
          bancoModel = new BancoModel(1, clienteLocal.getIdBanco().intValue()); 
        CuentaBancariaModel cuentaBancariaModel = new CuentaBancariaModel(clienteLocal.getCuentaBancaria(), bancoModel);
        SuscriptorModel suscriptorModel = new SuscriptorModel(personaModel, paramLoginModel.getPassword());
        suscriptorModel.setCuentaBancaria(cuentaBancariaModel);
        ResponseVO responseVO1 = getIdSae(suscriptorModel.getPersona().getRutRol());
        if (responseVO1.hasData()) {
          Long long_ = (Long)responseVO1.getData();
          suscriptorModel.setIdSae(long_.longValue());
        } else {
          logger.warn("No se obtuvo el ID SAE");
        } 
        ResponseVO responseVO2 = AduanaDelegateHandler.getInstance().getAgente(l);
        if (responseVO2.hasData())
          suscriptorModel.setAgente((AgenteModel)responseVO2.getData()); 
        responseVO = new ResponseVO(suscriptorModel);
        suscriptorModel.setPerfil(getPerfil(paramLoginModel));
      } else {
        suscriptorLocal.setIntentos(suscriptorLocal.getIntentos().add(new BigDecimal(1.0D)));
        if (suscriptorLocal.getIntentos().intValue() >= 3)
          suscriptorLocal.setFechaBloqueo(new Date(System.currentTimeMillis())); 
        str = PropertyManager.getInstance().getProperty(1, -2001);
        responseVO = new ResponseVO(null, new MessageVO(str, -2001));
      } 
    } catch (FinderException finderException) {
      this.sessionContext.setRollbackOnly();
      logger.error("Fallo la bno encontro al suscriptor", (Throwable)finderException);
      str = PropertyManager.getInstance().getProperty(1, -2002);
      responseVO = new ResponseVO(null, new MessageVO(str, -2002));
    } catch (LookupException lookupException) {
      this.sessionContext.setRollbackOnly();
      logger.error("No encontro el Entity", (Throwable)lookupException);
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)lookupException), 4);
      str = PropertyManager.getInstance().getProperty(1, -2003);
      responseVO = new ResponseVO(null, new MessageVO(2, str, -2003));
    } catch (Exception exception) {
      this.sessionContext.setRollbackOnly();
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      str = PropertyManager.getInstance().getProperty(1, -2003);
      responseVO = new ResponseVO(null, new MessageVO(2, str, -2003));
    } 
    return responseVO;
  }
  
  private ResponseVO getIdSae(long paramLong) {
    ResponseVO responseVO = new ResponseVO(null);
    DAOFactory dAOFactory = DAOFactory.getDAOFactory(1);
    CountBankDAO countBankDAO = dAOFactory.getCountsBankingDAO();
    return countBankDAO.getIdSAE(new Long(paramLong));
  }
  
  public ResponseVO isValidAgente(LoginModel paramLoginModel) {
    ResponseVO responseVO = new ResponseVO(null);
    try {
      ResponseVO responseVO1 = AduanaDelegateHandler.getInstance().loginAgente(paramLoginModel);
      if (responseVO1.hasData()) {
        AgenteModel agenteModel = (AgenteModel)responseVO1.getData();
        LoginModel loginModel = new LoginModel(String.valueOf(agenteModel.getPersona().getRutRol()), paramLoginModel.getPassword());
        ResponseVO responseVO2 = isValidSuscriptor(loginModel);
        if (responseVO2.hasData()) {
          responseVO = responseVO2;
        } else {
          responseVO = new ResponseVO(null, responseVO2.getMessage());
        } 
      } else {
        responseVO = new ResponseVO(null, responseVO1.getMessage());
      } 
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(LoginSessionBean.class, exception.getMessage(), 4);
      responseVO = new ResponseVO(null, new MessageVO(2, exception.getMessage()));
    } 
    return responseVO;
  }
  
  private boolean existSuscriptor(LoginModel paramLoginModel) {
    boolean bool = false;
    try {
      SuscriptorLocalHome suscriptorLocalHome = (SuscriptorLocalHome)ServiceLocator.getLocalHome(SuscriptorLocalHome.class);
      SuscriptorLocal suscriptorLocal = suscriptorLocalHome.findByPrimaryKey(new BigDecimal(paramLoginModel.getUsername()));
      bool = true;
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      LogUtility.log(LoginSessionBean.class, PropertyManager.getInstance().getProperty(1, -2000), 2);
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(LoginSessionBean.class, PropertyManager.getInstance().getProperty(1, -2015), 4);
      bool = false;
    } 
    return bool;
  }
  
  public ResponseVO getFolios(LoginModel paramLoginModel) throws Exception {
    ResponseVO responseVO = new ResponseVO(null);
    String str = "";
    if (existSuscriptor(paramLoginModel)) {
      str = PropertyManager.getInstance().getProperty(1, -2000);
      LogUtility.log(getClass(), str, 3);
      return new ResponseVO(null, new MessageVO(str, -2000));
    } 
    try {
      ArrayList arrayList = new ArrayList();
      ResponseVO responseVO1 = null;
      long l = 0L;
      try {
        l = Long.parseLong(paramLoginModel.getUsername());
      } catch (NumberFormatException numberFormatException) {
        logger.error("Ha ocurrido un error", numberFormatException);
        return new ResponseVO(null, new MessageVO(2, "Usuario no v));
      } 
      responseVO1 = MasterDelegateHandler.getInstance().getFormulario22(l);
      if (responseVO1.hasData())
        arrayList.addAll((Collection)responseVO1.getData()); 
      responseVO1 = MasterDelegateHandler.getInstance().getFormulario29(l);
      if (responseVO1.hasData())
        arrayList.addAll((Collection)responseVO1.getData()); 
      responseVO1 = MasterDelegateHandler.getInstance().getFormulario99(l);
      if (responseVO1.hasData())
        arrayList.addAll((Collection)responseVO1.getData()); 
      responseVO = new ResponseVO(arrayList);
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(null, new MessageVO(2, PropertyManager.getInstance().getProperty(1, -2016)));
    } 
    return responseVO;
  }
  
  private Collection getPerfil(LoginModel paramLoginModel) throws Exception {
    ArrayList arrayList = new ArrayList();
    try {
      PerfilLocalHome perfilLocalHome = (PerfilLocalHome)ServiceLocator.getLocalHome(PerfilLocalHome.class);
      PrivilegioLocalHome privilegioLocalHome = (PrivilegioLocalHome)ServiceLocator.getLocalHome(PrivilegioLocalHome.class);
      Collection collection = perfilLocalHome.findAllPerfilbyRut(paramLoginModel.getUsername(), new BigDecimal(1.0D));
      for (PerfilLocal perfilLocal : collection) {
        Collection collection1 = privilegioLocalHome.findbyPerfil(perfilLocal.getId());
        ArrayList arrayList1 = new ArrayList();
        for (PrivilegioLocal privilegioLocal : collection1) {
          PrivilegioModel privilegioModel = new PrivilegioModel();
          privilegioModel.setId(privilegioLocal.getId().intValue());
          privilegioModel.setNombre(privilegioLocal.getNombre());
          privilegioModel.setDescripcion(privilegioLocal.getDescripcion());
          arrayList1.add(privilegioModel);
        } 
        PerfilModel perfilModel = new PerfilModel();
        perfilModel.setId(perfilLocal.getId().intValue());
        perfilModel.setNombre(perfilLocal.getNombre());
        perfilModel.setDescripcion(perfilLocal.getDescripcion());
        perfilModel.setPrivilegio(arrayList1);
        arrayList.add(perfilModel);
      } 
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      LogUtility.log(LoginSessionBean.class, PropertyManager.getInstance().getProperty(1, -2017), 3);
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)finderException), 2);
      arrayList = new ArrayList();
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(LoginSessionBean.class, PropertyManager.getInstance().getProperty(1, -2018), 4);
      arrayList = new ArrayList();
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
    } 
    return arrayList;
  }
  
  public ResponseVO addNewUser(SuscriptorModel paramSuscriptorModel) throws Exception {
    String str = PropertyManager.getInstance().getProperty(1, 2001);
    ResponseVO responseVO = new ResponseVO(null, new MessageVO(0, str, 2001));
    boolean bool = false;
    try {
      SuscriptorLocalHome suscriptorLocalHome = (SuscriptorLocalHome)ServiceLocator.getLocalHome(SuscriptorLocalHome.class);
      SuscriptorLocal suscriptorLocal = suscriptorLocalHome.create(new BigDecimal(paramSuscriptorModel.getPersona().getRutRol()));
      suscriptorLocal.setPassword(paramSuscriptorModel.getClave());
      Date date = new Date(Calendar.getInstance().getTimeInMillis());
      DateFormat dateFormat = DateFormat.getInstance();
      Date date1 = DateFormat.getInstance().parse(dateFormat.format(date));
      Timestamp timestamp = new Timestamp(date1.getTime());
      suscriptorLocal.setFechaInscripcion(timestamp);
      suscriptorLocal.setIntentos(new BigDecimal(0.0D));
      ClienteLocalHome clienteLocalHome = (ClienteLocalHome)ServiceLocator.getLocalHome(ClienteLocalHome.class);
      LoginModel loginModel = new LoginModel(String.valueOf(paramSuscriptorModel.getPersona().getRutRol()), "");
      ResponseVO responseVO1 = existEnCliente(loginModel);
      ClienteLocal clienteLocal = null;
      if (responseVO1.hasData()) {
        clienteLocal = clienteLocalHome.findByPrimaryKey(new BigDecimal(loginModel.getUsername()));
      } else {
        clienteLocal = clienteLocalHome.create(new BigDecimal(paramSuscriptorModel.getPersona().getRutRol()));
      } 
      String str1 = paramSuscriptorModel.getPersona().getNombre();
      if (str1 != null && str1.length() > 100)
        str1 = str1.trim().substring(0, 100); 
      clienteLocal.setNombre(str1);
      String str2 = paramSuscriptorModel.getPersona().getApellidos();
      if (str2 != null && str2.length() > 100)
        str2 = str2.trim().substring(0, 100); 
      clienteLocal.setApellidos(str2);
      String str3 = paramSuscriptorModel.getPersona().getDireccion().getCalle();
      if (str3 != null && str3.length() > 100)
        str3 = str3.trim().substring(0, 100); 
      clienteLocal.setDireccion(str3);
      String str4 = paramSuscriptorModel.getPersona().getDireccion().getEmail();
      if (str4 != null && str4.length() > 50)
        str4 = str4.trim().substring(0, 50); 
      clienteLocal.setEmail(str4);
      String str5 = paramSuscriptorModel.getPersona().getDireccion().getTelefono();
      if (str5 != null && str5.length() > 20)
        str5 = str5.trim().substring(0, 20); 
      clienteLocal.setTelefono(str5);
      String str6 = paramSuscriptorModel.getPersona().getDireccion().getCelular();
      if (str6 != null && str6.length() > 20)
        str6 = str6.trim().substring(0, 20); 
      clienteLocal.setCelular(str6);
      String str7 = paramSuscriptorModel.getPersona().getDireccion().getFax();
      if (str7 != null && str7.length() > 20)
        str7 = str7.trim().substring(0, 20); 
      clienteLocal.setFax(str7);
      String str8 = paramSuscriptorModel.getPersona().getDireccion().getCodigoPostal().trim();
      if (str8 != null && str8.length() > 8)
        str8 = str8.trim().substring(0, 8); 
      clienteLocal.setCodigoPostal(str8);
      String str9 = paramSuscriptorModel.getPersona().getSexo();
      if (str9 != null && str9.length() > 1)
        str9 = str9.trim().substring(0, 1); 
      clienteLocal.setSexo(str9);
      if (paramSuscriptorModel.getCuentaBancaria() != null) {
        if (paramSuscriptorModel.getCuentaBancaria().getBanco() != null) {
          clienteLocal.setIdBanco(new BigDecimal(paramSuscriptorModel.getCuentaBancaria().getBanco().getCodigoAbif()));
        } else {
          clienteLocal.setIdBanco(new BigDecimal(1.0D));
        } 
        clienteLocal.setCuentaBancaria(paramSuscriptorModel.getCuentaBancaria().getNumero());
      } 
      clienteLocal.setIdComuna(new BigDecimal(paramSuscriptorModel.getPersona().getDireccion().getCodComuna()));
      clienteLocal.setDv(paramSuscriptorModel.getPersona().getDv());
      AplicacionPerfilSuscriptorLocalHome aplicacionPerfilSuscriptorLocalHome = (AplicacionPerfilSuscriptorLocalHome)ServiceLocator.getLocalHome(AplicacionPerfilSuscriptorLocalHome.class);
      aplicacionPerfilSuscriptorLocalHome.create(new BigDecimal(1.0D), new BigDecimal(1.0D), new BigDecimal(loginModel.getUsername()));
    } catch (CreateException createException) {
      logger.error("Ha ocurrido un error", (Throwable)createException);
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)createException), 2);
      str = PropertyManager.getInstance().getProperty(1, -2004);
      responseVO = new ResponseVO(null, new MessageVO(str, -2004));
      bool = true;
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)finderException), 2);
      str = PropertyManager.getInstance().getProperty(1, -2003);
      responseVO = new ResponseVO(null, new MessageVO(str, -2003));
      bool = true;
    } catch (LookupException lookupException) {
      logger.error("No encontro el Entity", (Throwable)lookupException);
      str = PropertyManager.getInstance().getProperty(1, -1000);
      responseVO = new ResponseVO(null, new MessageVO(str, -1000));
      bool = true;
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      str = PropertyManager.getInstance().getProperty(1, -2003);
      responseVO = new ResponseVO(null, new MessageVO(str, -2003));
      bool = true;
    } 
    try {
      if (bool)
        this.sessionContext.setRollbackOnly(); 
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(null, new MessageVO(2, StringUtil.getStackTrace(exception)));
    } 
    return responseVO;
  }
  
  public ResponseVO modifyUser(SuscriptorModel paramSuscriptorModel) throws Exception {
    String str = PropertyManager.getInstance().getProperty(1, 2002);
    ResponseVO responseVO = new ResponseVO(null, new MessageVO(0, str, 2002));
    boolean bool = false;
    try {
      SuscriptorLocalHome suscriptorLocalHome = (SuscriptorLocalHome)ServiceLocator.getLocalHome(SuscriptorLocalHome.class);
      SuscriptorLocal suscriptorLocal = suscriptorLocalHome.findByPrimaryKey(new BigDecimal(paramSuscriptorModel.getPersona().getRutRol()));
      if (!paramSuscriptorModel.getClave().equals(suscriptorLocal.getPassword()))
        return new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -2001))); 
      ClienteLocalHome clienteLocalHome = (ClienteLocalHome)ServiceLocator.getLocalHome(ClienteLocalHome.class);
      ClienteLocal clienteLocal = clienteLocalHome.findByPrimaryKey(new BigDecimal(paramSuscriptorModel.getPersona().getRutRol()));
      bool = true;
      clienteLocal.setApellidos(paramSuscriptorModel.getPersona().getApellidos());
      clienteLocal.setNombre(paramSuscriptorModel.getPersona().getNombre());
      clienteLocal.setDireccion(paramSuscriptorModel.getPersona().getDireccion().getCalle());
      clienteLocal.setEmail(paramSuscriptorModel.getPersona().getDireccion().getEmail());
      clienteLocal.setTelefono(paramSuscriptorModel.getPersona().getDireccion().getTelefono());
      clienteLocal.setCelular(paramSuscriptorModel.getPersona().getDireccion().getCelular());
      clienteLocal.setFax(paramSuscriptorModel.getPersona().getDireccion().getFax());
      clienteLocal.setCodigoPostal(paramSuscriptorModel.getPersona().getDireccion().getCodigoPostal());
      clienteLocal.setSexo(paramSuscriptorModel.getPersona().getSexo());
      clienteLocal.setIdComuna(new BigDecimal(paramSuscriptorModel.getPersona().getDireccion().getCodComuna()));
      Date date = new Date(Calendar.getInstance().getTimeInMillis());
      DateFormat dateFormat = DateFormat.getInstance();
      Date date1 = DateFormat.getInstance().parse(dateFormat.format(date));
      Timestamp timestamp = new Timestamp(date1.getTime());
      clienteLocal.setFechaModificacion(timestamp);
      if (paramSuscriptorModel.getCuentaBancaria() != null) {
        if (paramSuscriptorModel.getCuentaBancaria().getBanco() != null) {
          if (clienteLocal.getIdBanco() == null || paramSuscriptorModel.getCuentaBancaria().getBanco().getCodigoAbif() != clienteLocal.getIdBanco().intValue())
            clienteLocal.setFechaModificacionCuenta(timestamp); 
          clienteLocal.setIdBanco(new BigDecimal(paramSuscriptorModel.getCuentaBancaria().getBanco().getCodigoAbif()));
        } else {
          clienteLocal.setIdBanco(new BigDecimal(1.0D));
        } 
        if (clienteLocal.getCuentaBancaria() != null && !clienteLocal.getCuentaBancaria().equals(paramSuscriptorModel.getCuentaBancaria().getNumero()))
          clienteLocal.setFechaModificacionCuenta(timestamp); 
        clienteLocal.setCuentaBancaria(paramSuscriptorModel.getCuentaBancaria().getNumero());
      } 
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      str = PropertyManager.getInstance().getProperty(1, -2000);
      responseVO = new ResponseVO(null, new MessageVO(str, -2000));
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)finderException), 2);
    } catch (LookupException lookupException) {
      logger.error("Ha ocurrido un error", (Throwable)lookupException);
      str = PropertyManager.getInstance().getProperty(1, -1000);
      responseVO = new ResponseVO(null, new MessageVO(str, -1000));
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)lookupException), 5);
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      str = PropertyManager.getInstance().getProperty(1, -2005);
      responseVO = new ResponseVO(null, new MessageVO(str, -1000));
      if (bool)
        this.sessionContext.setRollbackOnly(); 
    } 
    return responseVO;
  }
  
  public ResponseVO changePassword(CambioDeClaveModel paramCambioDeClaveModel) throws Exception {
    String str = PropertyManager.getInstance().getProperty(1, 2003);
    ResponseVO responseVO = new ResponseVO(null, new MessageVO(0, str, 2003));
    boolean bool = false;
    try {
      SuscriptorLocalHome suscriptorLocalHome = (SuscriptorLocalHome)ServiceLocator.getLocalHome(SuscriptorLocalHome.class);
      SuscriptorLocal suscriptorLocal = suscriptorLocalHome.findByPrimaryKey(new BigDecimal(paramCambioDeClaveModel.getRut()));
      if (!suscriptorLocal.getPassword().equals(paramCambioDeClaveModel.getPassOld())) {
        str = PropertyManager.getInstance().getProperty(1, -2001);
        return new ResponseVO(null, new MessageVO(str, -2001));
      } 
      Date date = new Date(Calendar.getInstance().getTimeInMillis());
      DateFormat dateFormat = DateFormat.getInstance();
      Date date1 = DateFormat.getInstance().parse(dateFormat.format(date));
      Timestamp timestamp = new Timestamp(date1.getTime());
      bool = true;
      suscriptorLocal.setPassword(paramCambioDeClaveModel.getPassNew());
      ClienteLocalHome clienteLocalHome = (ClienteLocalHome)ServiceLocator.getLocalHome(ClienteLocalHome.class);
      ClienteLocal clienteLocal = clienteLocalHome.findByPrimaryKey(new BigDecimal(paramCambioDeClaveModel.getRut()));
      clienteLocal.setFechaModificacion(timestamp);
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      str = PropertyManager.getInstance().getProperty(1, -2002);
      responseVO = new ResponseVO(null, new MessageVO(str, -2002));
    } catch (LookupException lookupException) {
      logger.error("Ha ocurrido un error", (Throwable)lookupException);
      str = PropertyManager.getInstance().getProperty(1, -1000);
      responseVO = new ResponseVO(null, new MessageVO(str, -1000));
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)lookupException), 5);
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      str = PropertyManager.getInstance().getProperty(1, -1000);
      responseVO = new ResponseVO(null, new MessageVO(str, -1000));
      if (bool)
        this.sessionContext.setRollbackOnly(); 
    } 
    return responseVO;
  }
  
  public ResponseVO forgetPassword(PersonaModel paramPersonaModel) throws Exception {
    String str = PropertyManager.getInstance().getProperty(1, 2004);
    ResponseVO responseVO = new ResponseVO(null, new MessageVO(0, str, 2004));
    boolean bool = false;
    try {
      SuscriptorLocalHome suscriptorLocalHome = (SuscriptorLocalHome)ServiceLocator.getLocalHome(SuscriptorLocalHome.class);
      SuscriptorLocal suscriptorLocal = suscriptorLocalHome.findByPrimaryKey(new BigDecimal(paramPersonaModel.getRutRol()));
      ClienteLocalHome clienteLocalHome = (ClienteLocalHome)ServiceLocator.getLocalHome(ClienteLocalHome.class);
      ClienteLocal clienteLocal = clienteLocalHome.findByPrimaryKey(new BigDecimal(paramPersonaModel.getRutRol()));
      String str1 = clienteLocal.getEmail();
      if (StringUtil.isEmpty(str1))
        return new ResponseVO(null, new MessageVO(2, PropertyManager.getInstance().getProperty(1, -2020))); 
      OlvidoClaveLocalHome olvidoClaveLocalHome = (OlvidoClaveLocalHome)ServiceLocator.getLocalHome(OlvidoClaveLocalHome.class);
      bool = true;
      String str2 = DateUtil.getNowDate("dd/MM/yyyy hh:mm:ss a");
      Date date = DateUtil.getDateFromString(str2, "dd/MM/yyyy hh:mm:ss a");
      Timestamp timestamp = new Timestamp(date.getTime());
      OlvidoClaveLocal olvidoClaveLocal = olvidoClaveLocalHome.create(new BigDecimal(paramPersonaModel.getRutRol()), timestamp);
      olvidoClaveLocal.setClave(suscriptorLocal.getPassword());
      olvidoClaveLocal.setAvisado("N");
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      str = PropertyManager.getInstance().getProperty(1, -2002);
      responseVO = new ResponseVO(null, new MessageVO(str, -2002));
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)finderException), 2);
    } catch (LookupException lookupException) {
      logger.error("Ha ocurrido un error", (Throwable)lookupException);
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)lookupException), 5);
      str = PropertyManager.getInstance().getProperty(1, -1000);
      responseVO = new ResponseVO(null, new MessageVO(str, -1000));
    } catch (CreateException createException) {
      logger.error("Ha ocurrido un error", (Throwable)createException);
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)createException), 4);
      str = PropertyManager.getInstance().getProperty(1, -2007);
      responseVO = new ResponseVO(null, new MessageVO(str, -2007));
      this.sessionContext.setRollbackOnly();
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      str = PropertyManager.getInstance().getProperty(1, -1000);
      responseVO = new ResponseVO(null, new MessageVO(str, -1000));
      if (bool)
        this.sessionContext.setRollbackOnly(); 
    } 
    return responseVO;
  }
  
  public ResponseVO existEnCliente(LoginModel paramLoginModel) throws Exception {
    ResponseVO responseVO = new ResponseVO(null);
    try {
      ClienteLocalHome clienteLocalHome = (ClienteLocalHome)ServiceLocator.getLocalHome(ClienteLocalHome.class);
      ClienteLocal clienteLocal = clienteLocalHome.findByPrimaryKey(new BigDecimal(paramLoginModel.getUsername()));
      ComunaLocal comunaLocal = null;
      try {
        ComunaLocalHome comunaLocalHome = (ComunaLocalHome)ServiceLocator.getLocalHome(ComunaLocalHome.class);
        if (clienteLocal.getIdComuna() != null)
          comunaLocal = comunaLocalHome.findByPrimaryKey(clienteLocal.getIdComuna()); 
      } catch (FinderException finderException) {
        logger.error("Comuna no encontrada en la tabla de Comuna: " + clienteLocal.getIdComuna(), (Throwable)finderException);
      } catch (Exception exception) {
        logger.error("Ha ocurrido un error", exception);
        LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      } 
      DireccionModel direccionModel = new DireccionModel();
      direccionModel.setCalle(clienteLocal.getDireccion());
      direccionModel.setCelular(clienteLocal.getCelular());
      direccionModel.setTelefono(clienteLocal.getTelefono());
      if (clienteLocal.getIdComuna() != null)
        direccionModel.setCodComuna(clienteLocal.getIdComuna().intValue()); 
      direccionModel.setEmail(clienteLocal.getEmail());
      direccionModel.setFax(clienteLocal.getFax());
      direccionModel.setCodigoPostal(clienteLocal.getCodigoPostal());
      if (comunaLocal != null)
        direccionModel.setCodRegion(comunaLocal.getRegion().intValue()); 
      PersonaModel personaModel = new PersonaModel(clienteLocal.getRut().longValue());
      personaModel.setApellidos(clienteLocal.getApellidos());
      personaModel.setNombre(clienteLocal.getNombre());
      personaModel.setDv(clienteLocal.getDv());
      personaModel.setSexo(clienteLocal.getSexo());
      personaModel.setDireccion(direccionModel);
      BancoModel bancoModel = null;
      if (clienteLocal.getIdBanco() != null)
        bancoModel = new BancoModel(1, clienteLocal.getIdBanco().intValue()); 
      CuentaBancariaModel cuentaBancariaModel = new CuentaBancariaModel(clienteLocal.getCuentaBancaria(), bancoModel);
      SuscriptorModel suscriptorModel = new SuscriptorModel(personaModel, paramLoginModel.getPassword());
      suscriptorModel.setCuentaBancaria(cuentaBancariaModel);
      suscriptorModel.setPerfil(getPerfil(paramLoginModel));
      ResponseVO responseVO1 = getIdSae(suscriptorModel.getPersona().getRutRol());
      if (responseVO1.hasData()) {
        Long long_ = (Long)responseVO1.getData();
        suscriptorModel.setIdSae(long_.longValue());
      } else {
        logger.warn("No se obtuvo el ID SAE");
      } 
      responseVO = new ResponseVO(suscriptorModel);
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      String str = PropertyManager.getInstance().getProperty(1, -2003);
      responseVO = new ResponseVO(null, new MessageVO(str, -2003));
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)finderException), 2);
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -1000)));
    } 
    return responseVO;
  }
  
  public ResponseVO associateBienRaizToRut(BienRaizModel paramBienRaizModel) throws Exception {
    ResponseVO responseVO = new ResponseVO(null);
    try {
      ClienteBienRaizLocalHome clienteBienRaizLocalHome = (ClienteBienRaizLocalHome)ServiceLocator.getLocalHome(ClienteBienRaizLocalHome.class);
      RolModel rolModel = new RolModel();
      rolModel.setComuna(String.valueOf(paramBienRaizModel.getDireccion().getCodComuna()));
      rolModel.setRol(String.valueOf(paramBienRaizModel.getRutRol()));
      rolModel.setSubRol(paramBienRaizModel.getDv());
      long l = FormatBusnModel.toLong(rolModel);
      ClienteBienRaizLocal clienteBienRaizLocal = null;
      try {
        clienteBienRaizLocal = findBrbyRut(paramBienRaizModel);
      } catch (Exception exception) {
        logger.error("No encontro al BienRaiz " + paramBienRaizModel.getRutRol() + " asociado a un Rut", exception);
      } 
      if (clienteBienRaizLocal == null) {
        clienteBienRaizLocal = clienteBienRaizLocalHome.create(new BigDecimal(paramBienRaizModel.getPropietario().getRutRol()), new BigDecimal(l));
        clienteBienRaizLocal.setFechaModificacion(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        clienteBienRaizLocal.setBorrado("N");
        responseVO = new ResponseVO(null, new MessageVO(0, PropertyManager.getInstance().getProperty(1, 4003)));
      } else if (clienteBienRaizLocal.getBorrado().equals("S")) {
        clienteBienRaizLocal.setBorrado("N");
        clienteBienRaizLocal.setFechaModificacion(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        responseVO = new ResponseVO(null, new MessageVO(0, PropertyManager.getInstance().getProperty(1, 4004)));
      } else {
        responseVO = new ResponseVO(null, new MessageVO(2, PropertyManager.getInstance().getProperty(1, 4003)));
      } 
    } catch (CreateException createException) {
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)createException), 4);
      responseVO = new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -2023)));
      this.sessionContext.setRollbackOnly();
    } catch (LookupException lookupException) {
      logger.error("Ha ocurrido un error", (Throwable)lookupException);
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)lookupException), 5);
      responseVO = new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -2023)));
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -2023)));
      this.sessionContext.setRollbackOnly();
    } 
    return responseVO;
  }
  
  private ClienteBienRaizLocal findBrbyRut(BienRaizModel paramBienRaizModel) throws Exception {
    null = null;
    RolModel rolModel = new RolModel();
    rolModel.setComuna(String.valueOf(paramBienRaizModel.getDireccion().getCodComuna()));
    rolModel.setRol(String.valueOf(paramBienRaizModel.getRutRol()));
    rolModel.setSubRol(paramBienRaizModel.getDv());
    long l = FormatBusnModel.toLong(rolModel);
    ClienteBienRaizLocalHome clienteBienRaizLocalHome = (ClienteBienRaizLocalHome)ServiceLocator.getLocalHome(ClienteBienRaizLocalHome.class);
    ClienteBienRaizEntityPK clienteBienRaizEntityPK = new ClienteBienRaizEntityPK(new BigDecimal(paramBienRaizModel.getPropietario().getRutRol()), new BigDecimal(l));
    return clienteBienRaizLocalHome.findByPrimaryKey(clienteBienRaizEntityPK);
  }
  
  public ResponseVO modifyBienRaizAssociated(BienRaizModel paramBienRaizModel) throws Exception {
    ResponseVO responseVO = new ResponseVO(null);
    try {
      ClienteBienRaizLocal clienteBienRaizLocal = findBrbyRut(paramBienRaizModel);
      if (clienteBienRaizLocal != null) {
        clienteBienRaizLocal.setBorrado("N");
        clienteBienRaizLocal.setFechaModificacion(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        responseVO = new ResponseVO(null, new MessageVO(0, PropertyManager.getInstance().getProperty(1, 4004)));
      } 
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(null, new MessageVO(0, PropertyManager.getInstance().getProperty(1, 4004)));
      this.sessionContext.setRollbackOnly();
    } 
    return responseVO;
  }
  
  public ResponseVO deleteBienRaizByRut(BienRaizModel paramBienRaizModel) throws Exception {
    ResponseVO responseVO = new ResponseVO(null);
    ClienteBienRaizLocal clienteBienRaizLocal = findBrbyRut(paramBienRaizModel);
    try {
      if (clienteBienRaizLocal != null) {
        clienteBienRaizLocal.setBorrado("S");
        clienteBienRaizLocal.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
        responseVO = new ResponseVO(null, new MessageVO(0, PropertyManager.getInstance().getProperty(1, 4005)));
      } 
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -4005)));
      this.sessionContext.setRollbackOnly();
    } 
    return responseVO;
  }
  
  public ResponseVO findAssociatedBienRaizByRut(LoginModel paramLoginModel) throws Exception {
    ArrayList arrayList = new ArrayList();
    ResponseVO responseVO = new ResponseVO(arrayList);
    try {
      ClienteBienRaizLocalHome clienteBienRaizLocalHome = (ClienteBienRaizLocalHome)ServiceLocator.getLocalHome(ClienteBienRaizLocalHome.class);
      Collection collection = clienteBienRaizLocalHome.findByRut(new BigDecimal(paramLoginModel.getUsername()), "N");
      for (ClienteBienRaizLocal clienteBienRaizLocal : collection) {
        RolModel rolModel = FormatBusnModel.toRolModel(clienteBienRaizLocal.getRol().longValue());
        if (rolModel != null) {
          ResponseVO responseVO1 = ContribucionesDelegateHandler.getInstance().getBienRaiz(Integer.valueOf(rolModel.getComuna()).intValue(), Integer.valueOf(rolModel.getRol()).intValue(), Integer.valueOf(rolModel.getSubRol()).intValue());
          if (responseVO1.hasData()) {
            BienRaizModel bienRaizModel = (BienRaizModel)responseVO1.getData();
            arrayList.add(bienRaizModel);
            continue;
          } 
          responseVO = new ResponseVO(new ArrayList(), new MessageVO(PropertyManager.getInstance().getProperty(1, -4006)));
          continue;
        } 
        responseVO = new ResponseVO(new ArrayList(), new MessageVO(PropertyManager.getInstance().getProperty(1, -4007)));
      } 
      if (arrayList.size() == 0) {
        responseVO = new ResponseVO(arrayList, new MessageVO(0, PropertyManager.getInstance().getProperty(1, 4006)));
      } else {
        responseVO = new ResponseVO(arrayList);
      } 
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      String str = PropertyManager.getInstance().getProperty(1, -4006);
      LogUtility.log(getClass(), str, 2);
      responseVO = new ResponseVO(new ArrayList(), new MessageVO(str));
    } catch (LookupException lookupException) {
      logger.error("Ha ocurrido un error", (Throwable)lookupException);
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)lookupException), 5);
      responseVO = new ResponseVO(new ArrayList(), new MessageVO(PropertyManager.getInstance().getProperty(1, -1000)));
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(new ArrayList(), new MessageVO(PropertyManager.getInstance().getProperty(1, -1000)));
    } 
    return responseVO;
  }
  
  public ResponseVO findBienRaizByRut(LoginModel paramLoginModel) throws Exception {
    ArrayList arrayList = new ArrayList();
    ResponseVO responseVO = new ResponseVO(arrayList);
    boolean bool = false;
    try {
      ResponseVO responseVO1 = findAssociatedBienRaizByRut(paramLoginModel);
      ResponseVO responseVO2 = ContribucionesDelegateHandler.getInstance().findBrByRut(paramLoginModel);
      if (responseVO2.hasData()) {
        Collection collection = (Collection)responseVO2.getData();
        if (responseVO1.hasData()) {
          bool = true;
          Collection collection1 = (Collection)responseVO1.getData();
          if (collection1.size() == 0) {
            arrayList.addAll(collection);
          } else {
            for (BienRaizModel bienRaizModel : collection) {
              if (!existInBrAssociated(bienRaizModel, collection1))
                arrayList.add(bienRaizModel); 
            } 
          } 
        } 
      } 
      if (bool) {
        if (arrayList.size() == 0) {
          responseVO = new ResponseVO(arrayList, new MessageVO(0, PropertyManager.getInstance().getProperty(1, 2005)));
        } else {
          responseVO = new ResponseVO(arrayList);
        } 
      } else {
        responseVO = new ResponseVO(new ArrayList(), new MessageVO(PropertyManager.getInstance().getProperty(1, -2024)));
      } 
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(getClass(), StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(new ArrayList(), new MessageVO(PropertyManager.getInstance().getProperty(1, -2024)));
    } 
    return responseVO;
  }
  
  private boolean existInBrAssociated(BienRaizModel paramBienRaizModel, Collection paramCollection) throws Exception {
    boolean bool = false;
    long l = FormatBusnModel.toLong(paramBienRaizModel);
    for (BienRaizModel bienRaizModel : paramCollection) {
      long l1 = FormatBusnModel.toLong(bienRaizModel);
      if (l == l1) {
        bool = true;
        break;
      } 
    } 
    return bool;
  }
  
  public ResponseVO findLastForgetPassword() {
    ResponseVO responseVO = new ResponseVO(null);
    try {
      Properties properties = new Properties();
      properties.put("smtp", PropertyManager.getInstance().getProperty(0, "OLVIDO_CLAVE_SMTP"));
      Cartero.init(properties);
      Cartero cartero = Cartero.getInstance();
      OlvidoClaveLocalHome olvidoClaveLocalHome = (OlvidoClaveLocalHome)ServiceLocator.getLocalHome(OlvidoClaveLocalHome.class);
      Collection collection = olvidoClaveLocalHome.findLastPass();
      Calendar calendar = Calendar.getInstance();
      Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
      Iterator iterator = collection.iterator();
      while (iterator.hasNext()) {
        String str = null;
        OlvidoClaveLocal olvidoClaveLocal = iterator.next();
        LoginModel loginModel = new LoginModel(String.valueOf(olvidoClaveLocal.getRut().longValue()), "");
        ResponseVO responseVO1 = existEnCliente(loginModel);
        if (responseVO1.hasData()) {
          SuscriptorModel suscriptorModel = (SuscriptorModel)responseVO1.getData();
          str = suscriptorModel.getPersona().getDireccion().getEmail();
          Carta carta = new Carta(PropertyManager.getInstance().getProperty(0, "OLVIDO_CLAVE_REMITENTE"));
          carta.addDestinatario(suscriptorModel.getPersona().getDireccion().getEmail());
          carta.setTitulo(PropertyManager.getInstance().getProperty(0, "OLVIDO_CLAVE_TITULO"));
          String str1 = "Srs.";
          if (suscriptorModel.getPersona().getSexo() != null)
            if (suscriptorModel.getPersona().getSexo().equals("M")) {
              str1 = "Sr. ";
            } else {
              str1 = "Sra. ";
            }  
          String str2 = (suscriptorModel.getPersona().getNombre() == null) ? "" : suscriptorModel.getPersona().getNombre();
          carta.setTexto(str1 + str2 + ", " + PropertyManager.getInstance().getProperty(0, "OLVIDO_CLAVE_TEXTO") + "\r\nPara el usuario:\r\n Rut:" + olvidoClaveLocal.getRut().longValue() + "\r\n clave:" + olvidoClaveLocal.getClave() + "\r\n\r\n" + PropertyManager.getInstance().getProperty(0, "OLVIDO_CLAVE_FIRMA"));
          cartero.envia(carta);
          olvidoClaveLocal.setAvisado("S");
        } 
      } 
    } catch (LookupException lookupException) {
      logger.error("Ha ocurrido un error", (Throwable)lookupException);
      LogUtility.log(LoginSessionBean.class, StringUtil.getStackTrace((Exception)lookupException), 5);
      responseVO = new ResponseVO(null, new MessageVO(lookupException.getMessage()));
      this.sessionContext.setRollbackOnly();
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      exception.printStackTrace();
      LogUtility.log(LoginSessionBean.class, StringUtil.getStackTrace(exception), 4);
      responseVO.setMessage(new MessageVO(exception.getMessage()));
      this.sessionContext.setRollbackOnly();
    } 
    return responseVO;
  }
  
  public ResponseVO insertAccesoPortal(DatosAccesoPortalModel paramDatosAccesoPortalModel) {
    ResponseVO responseVO = new ResponseVO(paramDatosAccesoPortalModel);
    try {
      DatosAccesoPortalLocalHome datosAccesoPortalLocalHome = (DatosAccesoPortalLocalHome)ServiceLocator.getLocalHome(DatosAccesoPortalLocalHome.class);
      DatosAccesoPortalLocal datosAccesoPortalLocal = datosAccesoPortalLocalHome.create(new BigDecimal(paramDatosAccesoPortalModel.getRut()), new Timestamp(Calendar.getInstance().getTimeInMillis()), paramDatosAccesoPortalModel.getOrigen());
      datosAccesoPortalLocal.setDv((paramDatosAccesoPortalModel.getDv() == null) ? "" : paramDatosAccesoPortalModel.getDv());
    } catch (LookupException lookupException) {
      this.sessionContext.setRollbackOnly();
      logger.error("Ha ocurrido un error", (Throwable)lookupException);
      LogUtility.log(LoginSessionBean.class, "insertAccesoPortal... " + StringUtil.getStackTrace((Exception)lookupException), 5);
      responseVO = new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -1005)));
      LogUtility.log(getClass(), StringUtil.getStackTrace((Exception)lookupException), 2);
    } catch (CreateException createException) {
      this.sessionContext.setRollbackOnly();
      logger.error("Ha ocurrido un error", (Throwable)createException);
      LogUtility.log(LoginSessionBean.class, "insertAccesoPortal... " + StringUtil.getStackTrace((Exception)createException), 4);
      responseVO = new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -1007)));
    } catch (Exception exception) {
      this.sessionContext.setRollbackOnly();
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(LoginSessionBean.class, "insertAccesoPortal... " + StringUtil.getStackTrace(exception), 4);
      responseVO = new ResponseVO(null, new MessageVO(PropertyManager.getInstance().getProperty(1, -1005)));
    } 
    return responseVO;
  }
  
  public ResponseVO checkAccesoPortal(DatosAccesoPortalModel paramDatosAccesoPortalModel) throws Exception {
    PersonaModel personaModel = new PersonaModel(paramDatosAccesoPortalModel.getRut());
    ResponseVO responseVO = new ResponseVO(new SuscriptorModel(personaModel, ""));
    boolean bool = false;
    try {
      DatosAccesoPortalLocalHome datosAccesoPortalLocalHome = (DatosAccesoPortalLocalHome)ServiceLocator.getLocalHome(DatosAccesoPortalLocalHome.class);
      DatosAccesoPortalLocal datosAccesoPortalLocal = datosAccesoPortalLocalHome.findLastAcceso(new BigDecimal(paramDatosAccesoPortalModel.getRut()));
      Timestamp timestamp = datosAccesoPortalLocal.getFecha();
      Calendar calendar1 = Calendar.getInstance();
      int i = calendar1.get(12);
      int j = calendar1.get(10);
      Calendar calendar2 = Calendar.getInstance();
      calendar2.setTimeInMillis(datosAccesoPortalLocal.getFecha().getTime());
      Calendar calendar3 = Calendar.getInstance();
      if (timestamp != null)
        calendar3.setTimeInMillis(timestamp.getTime()); 
      int k = calendar3.get(12);
      int m = calendar3.get(10);
      int n = 0;
      if (j == m) {
        if (i >= k) {
          n = i - k;
        } else {
          int i1 = 60 - k;
          n = i1 + i;
        } 
        bool = (n <= 10) ? true : false;
      } 
      if (bool) {
        personaModel.setDv(Util.calculaDV(paramDatosAccesoPortalModel.getRut()));
        responseVO = new ResponseVO(new SuscriptorModel(personaModel, ""));
      } 
    } catch (LookupException lookupException) {
      logger.error("Ha ocurrido un error", (Throwable)lookupException);
      LogUtility.log(LoginSessionBean.class, "checkAccesoPortal... No encontro al entity", 5);
    } catch (FinderException finderException) {
      logger.error("Ha ocurrido un error", (Throwable)finderException);
      LogUtility.log(LoginSessionBean.class, "checkAccesoPortal... Registro de accesos", 4);
    } catch (Exception exception) {
      logger.error("Ha ocurrido un error", exception);
      LogUtility.log(LoginSessionBean.class, "checkAccesoPortal... Error en el minsertAcceso", 4);
    } 
    return responseVO;
  }
}


