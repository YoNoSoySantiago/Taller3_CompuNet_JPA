package co.edu.icesi.dev.uccareapp.transport.tests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import co.edu.icesi.dev.uccareapp.transport.Taller2ShApplication;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectAlreadyExistException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
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
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.BusinessentityService;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.CountryRegionService;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesPersonQuotaHistoryService;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesPersonService;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryHistoryService;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@ContextConfiguration(classes = Taller2ShApplication.class)
class Taller1ShApplicationIntegrationTest {
	
	
	private SalesPersonService salesPersonService;
	private SalesPersonQuotaHistoryService salesPersonQuotaHistoryService;
	private SalesTerritoryService salesTerritoryService;
	private SalesTerritoryHistoryService salesTerritoryHistoryService;
	
	private SalesPersonDao salesPersonDao;
	private SalesPersonQuotaHistoryDao salesPersonQuotaHistoryDao;
	private SalesTerritoryDao salesTerritoryDao;
	private SalesTerritoryHistoryDao salesTerritoryHistoryDao;
	
	private BusinessentityService businessentityService;
	private CountryRegionService countryRegionService;
	
	private String countryCodeColombia;
	private String countryCodeMexico;
	
	@Autowired
	public Taller1ShApplicationIntegrationTest(SalesPersonService sps,SalesPersonQuotaHistoryService spqs,
			SalesTerritoryService sts,SalesTerritoryHistoryService sths,BusinessentityService bes,CountryRegionService crs,
			SalesPersonDao spr, SalesPersonQuotaHistoryDao spqhr, SalesTerritoryDao str, SalesTerritoryHistoryDao sthr) {
		this.salesPersonService = sps;
		this.salesPersonQuotaHistoryService = spqs;
		this.salesTerritoryService = sts;
		this.salesTerritoryHistoryService = sths;
		
		this.salesPersonDao =spr;
		this.salesPersonQuotaHistoryDao = spqhr;
		this.salesTerritoryDao = str;
		this.salesTerritoryHistoryDao = sthr;
		
		this.businessentityService = bes;
		this.countryRegionService = crs;
		setUpInstantiate();
	}
	
	void setUpInstantiate() {
		Businessentity businessEntity = new Businessentity();
		Businessentity businessEntity2 = new Businessentity();
		
		businessentityService.add(businessEntity); // will have code 1
		businessentityService.add(businessEntity2); // will have code 2
		
		Countryregion countryRegion = new Countryregion();
		countryRegion.setName("Colombia");
		Countryregion countryRegion2 = new Countryregion();
		countryRegion2.setName("Mexico");
		
		countryRegionService.add(countryRegion);
		countryRegionService.add(countryRegion2);
		
		Iterable<Countryregion> countries = countryRegionService.findAll();
		for(Countryregion country:countries) {
			if(country.getName().equals("Colombia")) {
				countryCodeColombia = country.getCountryregioncode();
			}else {
				countryCodeMexico = country.getCountryregioncode();
			}
		}
	}
	
	Salesterritory setUpSaveSalesTerritory() throws InvalidValueException, ObjectDoesNotExistException {
		Salesterritory salesTerritory = new Salesterritory();
		salesTerritory.setName("TR-SH");
		salesTerritory.setCountryregioncode(countryCodeColombia);
		return salesTerritory;
	}
	
	@Test
	@Order(1)
	void saveSalesTerritoryCountryCodeNotExistTest() throws InvalidValueException, ObjectDoesNotExistException {
		Salesterritory salesTerritory = setUpSaveSalesTerritory();
		salesTerritory.setCountryregioncode("ABC");
		assertThrows(ObjectDoesNotExistException.class, ()->{
			this.salesTerritoryService.add(salesTerritory);
		});
		
		assertTrue(salesTerritoryDao.findById(1).isEmpty());
	}
	
	@Test
	@Order(1)
	void saveSalesTerritoryCountrytExistTest() throws InvalidValueException, ObjectDoesNotExistException {
		Salesterritory salesTerritory = setUpSaveSalesTerritory();
		this.salesTerritoryService.add(salesTerritory);
		
		assertTrue(!salesTerritoryDao.findById(1).isEmpty());
	}
	
	@Test
	@Order(2)
	void editSalesTerritoryCountryCodeNotExistTest() throws InvalidValueException, ObjectDoesNotExistException {
		this.salesTerritoryService.add(setUpSaveSalesTerritory());
		Salesterritory salesTerritory = new Salesterritory();
		salesTerritory.setName("TR-SH");
		salesTerritory.setCountryregioncode("ABC");
		salesTerritory.setTerritoryid(1);
		assertThrows(ObjectDoesNotExistException.class, ()->{
			this.salesTerritoryService.edit(salesTerritory);
		});
		assertTrue(!salesTerritoryDao.findById(1).get().getCountryregioncode().equals("ABC"));
	}
	
	@Test
	@Order(2)
	void editSalesTerritoryIdNotExistTest() throws InvalidValueException, ObjectDoesNotExistException {
		this.salesTerritoryService.add(setUpSaveSalesTerritory());
		Salesterritory salesTerritory = new Salesterritory();
		salesTerritory.setName("TR-SH");
		salesTerritory.setCountryregioncode(countryCodeMexico);
		salesTerritory.setTerritoryid(-1);
		assertThrows(ObjectDoesNotExistException.class, ()->{
			this.salesTerritoryService.edit(salesTerritory);
		});
	}
	
	@Test
	@Order(2)
	void editSalesTerritoryTest() throws InvalidValueException, ObjectDoesNotExistException {
		this.salesTerritoryService.add(setUpSaveSalesTerritory());
		Salesterritory salesTerritory2 = new Salesterritory();
		salesTerritory2.setName("TR-SHS");
		salesTerritory2.setCountryregioncode(countryCodeMexico);
		salesTerritory2.setTerritoryid(1);
		this.salesTerritoryService.edit(salesTerritory2);
		
		Salesterritory territory =  salesTerritoryDao.findById(1).get();
		assertTrue(territory.getName().equals("TR-SHS") && territory.getCountryregioncode().equals(countryCodeMexico));
	}
	
	Salesperson setUpSaveSalesPerson() throws InvalidValueException, ObjectDoesNotExistException  {
		this.salesTerritoryService.add(setUpSaveSalesTerritory());
		Salesperson salesPerson = new Salesperson();
		salesPerson.setSalesquota(new BigDecimal("99999.99999"));
		salesPerson.setCommissionpct(new BigDecimal("0.5"));
		salesPerson.setBusinessentityid(1);
		salesPerson.setSalesterritory(salesTerritoryService.findById(1).get());
		return salesPerson;
	}	
	
	@Test
	@Order(2)
	void saveSalesPersonTerritoryNotExist() throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException {
		
		Salesperson salesPerson =  setUpSaveSalesPerson();
		salesPerson.setBusinessentityid(3);
		Salesterritory falseTerritory = new Salesterritory();
		falseTerritory.setTerritoryid(-1);
		salesPerson.setSalesterritory(falseTerritory);
		assertThrows(ObjectDoesNotExistException.class, ()->{
			salesPersonService.add(salesPerson,3,-1);
		});
		assertTrue(salesPersonDao.findById(1).isEmpty());
	}
	
	@Test
	@Order(2)
	void saveSalesPersonBusinessIdNotExist() throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException {
		
		Salesperson salesPerson =  setUpSaveSalesPerson();
		Salesterritory falseTerritory = new Salesterritory();
		falseTerritory.setTerritoryid(1);
		salesPerson.setSalesterritory(falseTerritory);
		assertThrows(ObjectDoesNotExistException.class, ()->{
			salesPersonService.add(salesPerson,Integer.MAX_VALUE,1);
		});
		assertTrue(salesPersonDao.findById(1).isEmpty());
	}
	
	@Test
	@Order(3)
	void saveSalesPersonTest() throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException {
		Salesperson salesPerson =  setUpSaveSalesPerson();
		salesPersonService.add(salesPerson,1,1);
		assertTrue(!salesPersonDao.findById(1).isEmpty());
	}
	
	@Test
	@Order(4)
	void editSalesPersonTerritoryNotExist() throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException {
		
		Salesperson salesPerson =  setUpSaveSalesPerson();
		salesPerson.setBusinessentityid(2);
		Salesterritory falseTerritory = new Salesterritory();
		falseTerritory.setTerritoryid(-1);
		salesPerson.setSalesterritory(falseTerritory);
		assertThrows(ObjectDoesNotExistException.class, ()->{
			salesPersonService.edit(salesPerson);
		});
	}
	
	@Test
	@Order(4)
	void editSalesPersonBusinessIdNotExist() throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException {
		
		Salesperson salesPerson =  setUpSaveSalesPerson();
		salesPerson.setBusinessentityid(Integer.MAX_VALUE);
		Salesterritory falseTerritory = new Salesterritory();
		falseTerritory.setTerritoryid(1);
		salesPerson.setSalesterritory(falseTerritory);
		salesPersonService.findAll();
		assertThrows(ObjectDoesNotExistException.class, ()->{
			salesPersonService.edit(salesPerson);
		});
	}
	
	@Test
	@Order(4)
	void editSalesPersonTest() throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException {
		Salesperson salesperson = setUpSaveSalesPerson();
		salesPersonService.add(salesperson,2,1);
		Salesperson salesPerson =  setUpSaveSalesPerson();
		salesPerson.setBusinessentityid(2);
		salesPerson.setCommissionpct(new BigDecimal("0.7"));
		salesPersonService.edit(salesPerson);
		Optional<Salesperson> opSalesPersonEdited = salesPersonDao.findById(2);
		Salesperson salesPersonEdited = opSalesPersonEdited.get();
		assertTrue(salesPersonEdited.getCommissionpct().compareTo(new BigDecimal("0.7"))==0);
	}
	 
	Salespersonquotahistory setUpSaveSalesPersonQuotaHistory() throws InvalidValueException, ObjectAlreadyExistException, ObjectDoesNotExistException {
		
		Salespersonquotahistory salesQuota = new Salespersonquotahistory();
		//salesQuota.setId(1);
		salesQuota.setModifieddate(Timestamp.valueOf(LocalDateTime.now()));
		salesQuota.setSalesquota(BigDecimal.ZERO);
		Salesperson salesperson = salesPersonService.findById(1).get();
		salesQuota.setSalesperson(salesperson);
		return salesQuota;
	}
	
	@Test
	@Order(5)
	void saveSalesPersonQuotaHistoryBusinessIdNotExistTest() throws InvalidValueException, ObjectAlreadyExistException, ObjectDoesNotExistException {
		Salespersonquotahistory salesQuota = setUpSaveSalesPersonQuotaHistory();
		salesQuota.setId(2);
		assertThrows(ObjectDoesNotExistException.class,()->{
			this.salesPersonQuotaHistoryService.add(salesQuota,Integer.MAX_VALUE);
		});
	}
	
	@Test
	@Order(5)
	void saveSalesPersonQuotaHistoryTest() throws InvalidValueException, ObjectAlreadyExistException, ObjectDoesNotExistException {
		Salespersonquotahistory salesQuota = setUpSaveSalesPersonQuotaHistory();
		this.salesPersonQuotaHistoryService.add(salesQuota,1);
		assertTrue(!salesPersonQuotaHistoryDao.findById(1).isEmpty());
	}
	
	@Test
	@Order(6)
	void editSalesPersonQuotaHistoryBusinessIdNotExistTest() throws InvalidValueException, ObjectAlreadyExistException, ObjectDoesNotExistException {
		Salespersonquotahistory salesQuota = setUpSaveSalesPersonQuotaHistory();
		salesQuota.setId(Integer.MAX_VALUE);
		assertThrows(ObjectDoesNotExistException.class,()->{
			this.salesPersonQuotaHistoryService.edit(salesQuota);
		});
	}
	
	@Test
	@Order(6)
	void editSalesPersonQuotaHistoryTest() throws InvalidValueException, ObjectAlreadyExistException, ObjectDoesNotExistException {
		Salespersonquotahistory salesQuota = setUpSaveSalesPersonQuotaHistory();
		salesQuota.setId(1);
		salesQuota.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(3)));
		this.salesPersonQuotaHistoryService.edit(salesQuota);
		Optional<Salespersonquotahistory> opSalesQuotaHistory = salesPersonQuotaHistoryDao.findById(1);
		
		assertTrue(opSalesQuotaHistory.get().getModifieddate().compareTo(Timestamp.valueOf(LocalDateTime.now().minusDays(2)))<0);
		
		salesQuota = setUpSaveSalesPersonQuotaHistory();
		salesQuota.setId(1);
		salesQuota.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		this.salesPersonQuotaHistoryService.edit(salesQuota);
		opSalesQuotaHistory = salesPersonQuotaHistoryDao.findById(1);
		
		assertTrue(opSalesQuotaHistory.get().getModifieddate().compareTo(Timestamp.valueOf(LocalDateTime.now().minusDays(2)))>0);
	}
	
	Salesterritoryhistory setUpSalesTerritoryHistory() {
		Salesterritoryhistory salesterritoryhistory = new Salesterritoryhistory();
		//salesterritoryhistory.setId(1);
		salesterritoryhistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		salesterritoryhistory.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		salesterritoryhistory.setEnddate(Timestamp.valueOf(LocalDateTime.now()));
		return salesterritoryhistory;
	}
	
	@Test
	@Order(7)
	void saveSalesTerritoryHistoryBusinessIdNotExistTest() throws InvalidValueException, ObjectDoesNotExistException {
		Salesterritoryhistory salesterritoryhistory = setUpSalesTerritoryHistory();
		assertThrows(ObjectDoesNotExistException.class, ()->{
			salesTerritoryHistoryService.add(salesterritoryhistory, Integer.MAX_VALUE, 1);
		});
	}
	
	@Test
	@Order(7)
	void saveSalesTerritoryHistoryTerritoryIdNotExistTest() throws InvalidValueException, ObjectDoesNotExistException {
		Salesterritoryhistory salesterritoryhistory = setUpSalesTerritoryHistory();
		salesterritoryhistory.setId(2);
		assertThrows(ObjectDoesNotExistException.class, ()->{
			salesTerritoryHistoryService.add(salesterritoryhistory,1, Integer.MAX_VALUE);
		});
	}
	
	@Test
	@Order(7)
	void saveSalesTerritoryHistoryTest() throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException {
		Salesterritoryhistory salesterritoryhistory = setUpSalesTerritoryHistory();
		salesTerritoryHistoryService.add(salesterritoryhistory,1, 1);
		
		assertTrue(!salesTerritoryHistoryDao.findById(1).isEmpty());
	}
	

	
	@Test
	@Order(8)
	void editSalesTerritoryHistoryTest() throws InvalidValueException, ObjectDoesNotExistException {
		Salesterritoryhistory salesterritoryhistory = setUpSalesTerritoryHistory();
		salesterritoryhistory.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		salesterritoryhistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		salesterritoryhistory.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
		salesterritoryhistory.setSalesPerson(salesPersonService.findById(1).get());
		salesterritoryhistory.setSalesTerritory(salesTerritoryService.findById(1).get());
		salesterritoryhistory.setId(1);
		salesTerritoryHistoryService.edit(salesterritoryhistory);
		
		Salesterritoryhistory salesTerritoryHistoryEdited =  salesTerritoryHistoryDao.findById(1).get();
		
		assertTrue(salesTerritoryHistoryEdited.getEnddate().compareTo(Timestamp.valueOf(LocalDateTime.now()))<0);
		assertTrue(salesTerritoryHistoryEdited.getModifieddate().compareTo(Timestamp.valueOf(LocalDateTime.now().minusDays(1)))<0);
	}
}
