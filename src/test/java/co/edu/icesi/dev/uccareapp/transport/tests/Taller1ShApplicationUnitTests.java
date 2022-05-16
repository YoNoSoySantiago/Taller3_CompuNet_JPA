package co.edu.icesi.dev.uccareapp.transport.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import co.edu.icesi.dev.uccareapp.transport.Taller2ShApplication;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.BusinessentityDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.CountryRegionDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonQuotaHistoryDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryHistoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;
import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salespersonquotahistory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;
import co.edu.icesi.dev.uccareapp.transport.service.implementation.SalesPersonQuotaHistoryServiceImp;
import co.edu.icesi.dev.uccareapp.transport.service.implementation.SalesPersonServiceImp;
import co.edu.icesi.dev.uccareapp.transport.service.implementation.SalesTerritoryHistoryServiceImp;
import co.edu.icesi.dev.uccareapp.transport.service.implementation.SalesTerritoryServiceImp;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesPersonQuotaHistoryService;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesPersonService;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryHistoryService;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryService;

@SpringBootTest(classes= Taller2ShApplication.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(Lifecycle.PER_CLASS)
class Taller1ShApplicationUnitTests {
	
	
	private SalesPersonService salesPersonService;
	private SalesPersonQuotaHistoryService salesPersonQuotaHistoryService;
	private SalesTerritoryService salesTerritoryService;
	private SalesTerritoryHistoryService salesTerritoryHistoryService;
	
	@Mock
	private Businessentity businessEntity;
	@Mock
	private Salesterritory salesTerritory;
	
	@Mock
	private SalesPersonDao salesPersonDao;
	@Mock
	private SalesPersonQuotaHistoryDao salesPersonQuotaHistoryDao;
	@Mock
	private SalesTerritoryDao salesTerritoryDao;
	@Mock
	private CountryRegionDao countryRegionDao;
	@Mock
	private SalesTerritoryHistoryDao salesTerritoryHistoryDao;
	@Mock
	private BusinessentityDao businessentityDao;
	

	void setUpIdValues(){
		Optional<Salesperson> opPerson =  Optional.of(new Salesperson());
		Optional<Salesterritory> opTerritory = Optional.of(new Salesterritory());
		Optional<Salespersonquotahistory> opPersonHistory = Optional.of(new Salespersonquotahistory());
		Optional<Salesterritoryhistory> opTerriotyHistory = Optional.of(new Salesterritoryhistory());
		
		when(salesPersonDao.findById(1234)).thenReturn(opPerson);
		when(salesTerritoryDao.findById(4321)).thenReturn(opTerritory);
		when(salesPersonQuotaHistoryDao.findById(1234)).thenReturn(opPersonHistory);
		when(salesTerritoryHistoryDao.findById(1234)).thenReturn(opTerriotyHistory);
	}
	void setUpEmptyIdValues(){
		when(salesPersonDao.findById(1234)).thenReturn(Optional.empty());
		when(salesTerritoryDao.findById(4321)).thenReturn(Optional.empty());
		when(salesPersonQuotaHistoryDao.findById(1234)).thenReturn(Optional.empty());
		when(salesTerritoryHistoryDao.findById(1234)).thenReturn(Optional.empty());
	}

	
	@BeforeEach
	void loadService() {
		salesPersonService = new SalesPersonServiceImp(salesPersonDao,businessentityDao,salesTerritoryDao);
		salesPersonQuotaHistoryService = new SalesPersonQuotaHistoryServiceImp(salesPersonQuotaHistoryDao,salesPersonDao);
		salesTerritoryService = new SalesTerritoryServiceImp(salesTerritoryDao,countryRegionDao);
		salesTerritoryHistoryService = new SalesTerritoryHistoryServiceImp(salesTerritoryHistoryDao, salesTerritoryDao,salesPersonDao);
		Optional<Countryregion> opCountry = Optional.of(new Countryregion());
		Optional<Businessentity> opBusinessEntity = Optional.of(new Businessentity());
		when(countryRegionDao.findById("COL")).thenReturn(opCountry);
		when(salesTerritory.getName()).thenReturn("TR-SH");
		when(salesTerritory.getCountryregioncode()).thenReturn("COL");
		when(salesTerritory.getTerritoryid()).thenReturn(4321);
		when(businessentityDao.findById(1234)).thenReturn(opBusinessEntity);
	}
	
	Salesperson setUpLoadSalesPerson(){
		Optional<Salesterritory> opTerritory = Optional.of(new Salesterritory());
		when(salesTerritoryDao.findById(4321)).thenReturn(opTerritory);
		Salesperson person = new Salesperson();
		person.setBusinessentityid(1234);
		person.setSalesterritory(salesTerritory);
		person.setSalesquota(new BigDecimal("99999.99999"));
		person.setCommissionpct(new BigDecimal("0.5"));
		
		return person;
	}
	@Test
	void saveSalesPersonUnderZeroQuotaTest() {
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setSalesquota(new BigDecimal("-0.9999"));
		
		assertThrows(InvalidValueException.class, ()->{
			salesPersonService.add(person,1234,4321);
		});
	}
	
	@Test
	void saveSalesPersonUnderZeroCommissionTest() {
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setCommissionpct(new BigDecimal("-0.999"));
		
		assertThrows(InvalidValueException.class, ()->{
			salesPersonService.add(person,1234,4321);
		});
	}
	
	@Test
	void saveSalesPersonUpperOneCommisionTest() {
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setCommissionpct(new BigDecimal("1.001"));
		
		assertThrows(InvalidValueException.class, ()->{
			salesPersonService.add(person,1234,4321);
		});
	}
	
	@Test
	void saveSalesPersonCommisionZeroTest(){
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setCommissionpct(new BigDecimal("0"));
		assertDoesNotThrow(()->{
			salesPersonService.add(person,1234,4321);
		});
	}
	
	@Test
	void saveSalesPersonCommisionOneTest() {
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setCommissionpct(new BigDecimal("1"));
		assertDoesNotThrow(()->{
			salesPersonService.add(person,1234,4321);
		});
	}
	
	@Test
	void saveSalesPersonNullIdTest() {
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();
		assertThrows(NullPointerException.class, ()->{
			salesPersonService.add(person,null,4321);
		});
	}
	
	@Test
	void saveSalesPersonNullTerritoryTest() {
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();
		assertThrows(NullPointerException.class, ()->{
			salesPersonService.add(person,1234,null);
		});
	}
	
	@Test
	void saveSalesPersonNullQuotaTest() {
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setSalesquota(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonService.add(person,1234,4321);
		});
	}
	
	@Test
	void saveSalesPersonNullComissionTest() {
		setUpEmptyIdValues();
		Salesperson person = setUpLoadSalesPerson();;
		person.setCommissionpct(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonService.add(person,1234,4321);
		});
	}
	
	@Test
	void editSalesPersonUnderZeroQuotaTest() {
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setSalesquota(new BigDecimal("-0.9999"));
		
		assertThrows(InvalidValueException.class, ()->{
			salesPersonService.edit(person);
		});
	}
	
	@Test
	void editSalesPersonUnderZeroCommissionTest() {
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setCommissionpct(new BigDecimal("-0.999"));
		
		assertThrows(InvalidValueException.class, ()->{
			salesPersonService.edit(person);
		});
	}
	
	@Test
	void editSalesPersonUpperOneCommisionTest() {
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setCommissionpct(new BigDecimal("1.001"));
		
		assertThrows(InvalidValueException.class, ()->{
			salesPersonService.edit(person);
		});
	}
	
	@Test
	void editSalesPersonCommisionZeroTest(){
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setCommissionpct(new BigDecimal("0"));
		assertDoesNotThrow(()->{
			salesPersonService.edit(person);
		});
	}
	
	@Test
	void editSalesPersonCommisionOneTest() {
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setCommissionpct(new BigDecimal("1"));
		assertDoesNotThrow(()->{
			salesPersonService.edit(person);
		});
	}
	
	@Test
	void editSalesPersonNullIdTest() {
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setBusinessentityid(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonService.edit(person);
		});
	}
	
	@Test
	void editSalesPersonNullTerritoryTest() {
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setSalesterritory(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonService.edit(person);
		});
	}
	
	@Test
	void editSalesPersonNullQuotaTest() {
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();
		person.setSalesquota(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonService.edit(person);
		});
	}
	
	@Test
	void editSalesPersonNullComissionTest() {
		setUpIdValues();
		Salesperson person = setUpLoadSalesPerson();;
		person.setCommissionpct(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonService.edit(person);
		});
	}
	
	Salespersonquotahistory setUpSalesQuotaHistory() {
		Optional<Salesperson> opPerson =  Optional.of(new Salesperson());
		when(salesPersonDao.findById(1234)).thenReturn(opPerson);
		
		Salespersonquotahistory salesQuota = new Salespersonquotahistory();
		salesQuota.setId(1234);
		salesQuota.setModifieddate(Timestamp.valueOf(LocalDateTime.now()));
		salesQuota.setSalesquota(BigDecimal.ZERO);
		
		return salesQuota;
	}
	
	@Test
	void saveSalesQuotaHistoryAfterTodayModifiedDateTest() {
		setUpEmptyIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setModifieddate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
		assertThrows(InvalidValueException.class, ()->{
			salesPersonQuotaHistoryService.add(salesQuota,1234);
		});
	}
	
	@Test
	void saveSalesQuotaHistoryQuotaUnderZeroTest() {
		setUpEmptyIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setSalesquota(new BigDecimal("-0.99"));
		assertThrows(InvalidValueException.class, ()->{
			salesPersonQuotaHistoryService.add(salesQuota,1234);
		});
	}
	
	@Test
	void saveSalesQuotaHistoryQuotaUpperTest() {
		setUpEmptyIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setSalesquota(new BigDecimal("9999.9999"));
		assertDoesNotThrow(()->{
			salesPersonQuotaHistoryService.add(salesQuota,1234);
		});
	}
	
	@Test
	void saveSalesQuotaHistoryQuotaZeroTest() {
		setUpEmptyIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setSalesquota(BigDecimal.ZERO);
		assertDoesNotThrow(()->{
			salesPersonQuotaHistoryService.add(salesQuota,1234);
		});
	}
	
	
	@Test
	void saveSalesQuotaHistoryModifiedDateNullTest() {
		setUpEmptyIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setModifieddate(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonQuotaHistoryService.add(salesQuota,1234);
		});
	}
	
	@Test
	void saveSalesQuotaHistoryQuotaNullTest() {
		setUpEmptyIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setSalesquota(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonQuotaHistoryService.add(salesQuota,1234);
		});
	}
	
	@Test
	void editSalesQuotaHistoryAfterTodayModifiedDateTest() {
		setUpIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setModifieddate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
		assertThrows(InvalidValueException.class, ()->{
			salesPersonQuotaHistoryService.edit(salesQuota);
		});
	}
	
	@Test
	void editSalesQuotaHistoryQuotaUnderZeroTest() {
		setUpIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setSalesquota(new BigDecimal("-0.99"));
		assertThrows(InvalidValueException.class, ()->{
			salesPersonQuotaHistoryService.edit(salesQuota);
		});
	}
	
	@Test
	void editSalesQuotaHistoryQuotaUpperTest() {
		setUpIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setSalesquota(new BigDecimal("9999.9999"));
		assertDoesNotThrow(()->{
			salesPersonQuotaHistoryService.edit(salesQuota);
		});
	}
	
	@Test
	void editSalesQuotaHistoryQuotaZeroTest() {
		setUpIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setSalesquota(BigDecimal.ZERO);
		assertDoesNotThrow(()->{
			salesPersonQuotaHistoryService.edit(salesQuota);
		});
	}
	
	@Test
	void editSalesQuotaHistoryIdNullTest() {
		setUpIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setId(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonQuotaHistoryService.edit(salesQuota);
		});
	}
	
	@Test
	void editSalesQuotaHistoryModifiedDateNullTest() {
		setUpIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setModifieddate(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonQuotaHistoryService.edit(salesQuota);
		});
	}
	
	@Test
	void editSalesQuotaHistoryQuotaNullTest() {
		setUpIdValues();
		Salespersonquotahistory salesQuota = setUpSalesQuotaHistory();
		salesQuota.setSalesquota(null);
		assertThrows(NullPointerException.class, ()->{
			salesPersonQuotaHistoryService.edit(salesQuota);
		});
	}
	
	Salesterritory setUpSalesTerritory() {
		Salesterritory salesTerritory = new Salesterritory();
		salesTerritory.setName("TR-SH");
		salesTerritory.setCountryregioncode("COL");
		return salesTerritory;
	}
	
	@Test
	void saveSalesTerritoryShortNameTest() {
		setUpEmptyIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setName("TRSH");
		assertThrows(InvalidValueException.class,()->{
			salesTerritoryService.add(salesTerritory);
		});
	}
	
	@Test
	void saveSalesTerritoryMinLimitSizeNameTest() {
		setUpEmptyIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setName("TR-SH");
		assertDoesNotThrow(()->{
			salesTerritoryService.add(salesTerritory);
		});
	}
	
	@Test 
	void saveSalesTerritoryUpperMinLimitNameTest(){
		setUpEmptyIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setName("TR-SHS");
		assertDoesNotThrow(()->{
			salesTerritoryService.add(salesTerritory);
		});
	}
	
	@Test
	void saveSalesTerritoryNameNullTest() {
		setUpEmptyIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setName(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryService.add(salesTerritory);
		});
		
	}
	@Test
	void saveSalesTerritoryCountryCodeNullTest() {
		setUpEmptyIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setCountryregioncode(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryService.add(salesTerritory);
		});
	}

	@Test
	void editSalesTerritoryShortNameTest() {
		setUpIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setName("TRSH");
		salesTerritory.setTerritoryid(4321);
		assertThrows(InvalidValueException.class,()->{
			salesTerritoryService.edit(salesTerritory);
		});
	}
	
	@Test
	void editSalesTerritoryMinLimitSizeNameTest() {
		setUpIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setName("TR-SH");
		salesTerritory.setTerritoryid(4321);
		assertDoesNotThrow(()->{
			salesTerritoryService.edit(salesTerritory);
		});
	}
	
	@Test 
	void editSalesTerritoryUpperMinLimitNameTest(){
		setUpIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setName("TR-SHS");
		salesTerritory.setTerritoryid(4321);
		assertDoesNotThrow(()->{
			salesTerritoryService.edit(salesTerritory);
		});
	}
	
	@Test
	void editSalesTerritoryNameNullTest() {
		setUpIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setName(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryService.edit(salesTerritory);
		});
		
	}
	@Test
	void editSalesTerritoryCountryCodeNullTest() {
		setUpIdValues();
		Salesterritory salesTerritory = setUpSalesTerritory();
		salesTerritory.setCountryregioncode(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryService.edit(salesTerritory);
		});
	}
	
	Salesterritoryhistory setUpSalesTerritoryHistory() {

		Optional<Salesterritory> opTerritory = Optional.of(new Salesterritory());
		when(salesTerritoryDao.findById(4321)).thenReturn(opTerritory);
		Optional<Salesperson> opPerson =  Optional.of(new Salesperson());
		when(salesPersonDao.findById(1234)).thenReturn(opPerson);
		Salesterritoryhistory salesTerritoryHistory = new Salesterritoryhistory();
		salesTerritoryHistory.setId(1234);
		salesTerritoryHistory.setSalesTerritory(salesTerritory);
		salesTerritoryHistory.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		salesTerritoryHistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		
		return salesTerritoryHistory;
	}
	
	@Test
	void saveSalesTerritoryHistoryEndDateBeforeStartTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		salesTerritoryHistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		salesTerritoryHistory.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		assertThrows(InvalidValueException.class,()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,1234,4321);
		});
	}
	
	@Test
	void saveSalesTerritoryHistoryEndDateAfterNowTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setEnddate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
		salesTerritoryHistory.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		assertThrows(InvalidValueException.class,()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,1234,4321);
		});
	}
	
	@Test
	void saveSalesTerritoryHistoryEqualsDatesTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setEnddate(Timestamp.valueOf(LocalDateTime.now()));
		salesTerritoryHistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now()));
		salesTerritoryHistory.setStartdate(Timestamp.valueOf(LocalDateTime.now()));
		assertThrows(InvalidValueException.class,()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,1234,4321);
		});
	}
	
	@Test
	void saveSalesTerritoryHistoryDateModBeforeDateEndTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		salesTerritoryHistory.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		assertDoesNotThrow(()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,1234,4321);
		});
		
	}
	
	@Test
	void saveSalesTerritoryHistoryIdNullTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setId(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,1234,4321);
		});
	}
	
	@Test
	void saveSalesTerritoryHistoryTerritoryNullTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setSalesTerritory(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,1234,null);
		});
	}
	
	@Test
	void saveSalesTerritoryHistoryBusinessIdNullTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setSalesTerritory(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,null,4321);
		});
	}
	
	@Test
	void saveSalesTerritoryHistoryEndDateNullTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setEnddate(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,1234,4321);
		});
	}
	
	@Test
	void saveSalesTerritoryHistoryModDateNullTest() {
		setUpEmptyIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setModifieddate(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.add(salesTerritoryHistory,1234,4321);
		});
	}
	
	@Test
	void editSalesTerritoryHistoryEndDateBeforeStartTest() {
		setUpIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		salesTerritoryHistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		salesTerritoryHistory.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		salesTerritoryHistory.setSalesPerson(salesPersonService.findById(1234).get());
		salesTerritoryHistory.setSalesTerritory(salesTerritoryService.findById(4321).get());
		assertThrows(InvalidValueException.class,()->{
			salesTerritoryHistoryService.edit(salesTerritoryHistory);
		});
	}
	
	@Test
	void editSalesTerritoryHistoryEndDateAfterNowTest() {
		setUpIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setEnddate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
		salesTerritoryHistory.setStartdate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
		salesTerritoryHistory.setSalesPerson(salesPersonService.findById(1234).get());
		salesTerritoryHistory.setSalesTerritory(salesTerritoryService.findById(4321).get());
		assertThrows(InvalidValueException.class,()->{
			salesTerritoryHistoryService.edit(salesTerritoryHistory);
		});
	}
	
	@Test
	void editSalesTerritoryHistoryEqualsDatesTest() {
		setUpIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setEnddate(Timestamp.valueOf(LocalDateTime.now()));
		salesTerritoryHistory.setStartdate(Timestamp.valueOf(LocalDateTime.now()));
		salesTerritoryHistory.setSalesPerson(salesPersonService.findById(1234).get());
		salesTerritoryHistory.setSalesTerritory(salesTerritoryService.findById(4321).get());
		assertThrows(InvalidValueException.class,()->{
			salesTerritoryHistoryService.edit(salesTerritoryHistory);
		});
	}
	
	@Test
	void editSalesTerritoryHistoryDateModBeforeDateEndTest() {
		setUpIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		salesTerritoryHistory.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		salesTerritoryHistory.setSalesPerson(salesPersonService.findById(1234).get());
		salesTerritoryHistory.setSalesTerritory(salesTerritoryService.findById(4321).get());
		assertDoesNotThrow(()->{
			salesTerritoryHistoryService.edit(salesTerritoryHistory);
		});
		
	}
	
	@Test
	void editSalesTerritoryHistoryIdNullTest() {
		setUpIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setSalesPerson(salesPersonService.findById(1234).get());
		salesTerritoryHistory.setSalesTerritory(salesTerritoryService.findById(4321).get());
		salesTerritoryHistory.setId(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.edit(salesTerritoryHistory);
		});
	}
	
	@Test
	void editSalesTerritoryHistoryTerritoryNullTest() {
		setUpIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setSalesTerritory(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.edit(salesTerritoryHistory);
		});
	}
	
	@Test
	void editSalesTerritoryHistoryEndDateNullTest() {
		setUpIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setEnddate(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.edit(salesTerritoryHistory);
		});
	}
	
	@Test
	void editSalesTerritoryHistoryModDateNullTest() {
		setUpIdValues();
		Salesterritoryhistory salesTerritoryHistory = setUpSalesTerritoryHistory();
		salesTerritoryHistory.setModifieddate(null);
		assertThrows(NullPointerException.class,()->{
			salesTerritoryHistoryService.edit(salesTerritoryHistory);
		});
	}

}
