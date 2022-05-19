package co.edu.icesi.dev.uccareapp.transport.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonQuotaHistoryDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryHistoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salespersonquotahistory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Rollback(false)
@TestInstance(Lifecycle.PER_CLASS)
public class Taller3ShAplicationDaoIntegrationTest {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private SalesPersonDao salesPersonDao;
	
	@Autowired
	private SalesTerritoryDao salesTerritoryDao;
	
	@Autowired 
	private SalesPersonQuotaHistoryDao salesPersonQuotaHistoryDao;
	
	@Autowired
	private SalesTerritoryHistoryDao salesTerritoryHistoryDao;
	
	private final int n_st = 5;
	private final int n_sp = 20;
	private final int n_sth = 80;
	private final int n_spqh = 100;
	
	@BeforeAll
	@Transactional
	public void saveSetUp() {
		for(int i = 1;i<=n_st;i++) {
			Salesterritory st = new Salesterritory();
			st.setCountryregioncode("COL");
			salesTerritoryDao.save(st);
		}
		
		for(int i = 1;i<=n_sp;i++) {
			Salesperson sp = new Salesperson();
			sp.setCommissionpct(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf((i%(n_sp/4)))));
			sp.setSalesquota(BigDecimal.valueOf((i%(n_st))*1500+7500));
			sp.setSalesterritory(salesTerritoryDao.findById(1).get());
			sp.setBusinessentityid(i);
			salesPersonDao.save(sp);
		}
		
		for(int i = 1;i<=n_spqh;i++) {
			Salespersonquotahistory spqh = new Salespersonquotahistory();
			spqh.setSalesquota(BigDecimal.valueOf(1500*(i%(n_spqh/10))));
			spqh.setSalesperson(salesPersonDao.findById(1).get());
			spqh.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(i%10)));
			//spqh.setId(i);
			salesPersonQuotaHistoryDao.save(spqh);
		}
		
		for(int i = 1;i<=n_sth;i++) {
			Salesterritoryhistory sth = new Salesterritoryhistory();
			sth.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(10+i)));
			sth.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(i)));
			sth.setSalesTerritory(salesTerritoryDao.findById(1).get());
			sth.setSalesPerson(salesPersonDao.findById(1).get());
			//sth.setId(i);
			salesTerritoryHistoryDao.save(sth);
		}
	}

	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void SalesTerritoryDaoSave() {
		Salesterritory st = new Salesterritory();
		
		assertTrue(salesTerritoryDao.findById(n_st+1).isEmpty());
		salesTerritoryDao.save(st);
		assertFalse(salesTerritoryDao.findById(n_st+1).isEmpty());
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void SalesTerritoryDaoUpdate() {
		Salesterritory st = salesTerritoryDao.findById(1).get();
		String oldCountryCode = st.getCountryregioncode();
		String newCountryCode = "ABC";
		assertTrue(oldCountryCode.equals(salesTerritoryDao.findById(1).get().getCountryregioncode()));
		
		st.setCountryregioncode(newCountryCode);
		salesTerritoryDao.update(st);
		assertFalse(oldCountryCode.equals(salesTerritoryDao.findById(1).get().getCountryregioncode()));
		assertTrue(newCountryCode.equals(salesTerritoryDao.findById(1).get().getCountryregioncode()));
		
		st.setCountryregioncode(oldCountryCode);
		salesTerritoryDao.update(st);
		assertFalse(newCountryCode.equals(salesTerritoryDao.findById(1).get().getCountryregioncode()));
		assertTrue(oldCountryCode.equals(salesTerritoryDao.findById(1).get().getCountryregioncode()));
	}

	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void SalesTerritoryDaoDelete() {
		Salesterritory st = salesTerritoryDao.findById(3).get();
		
		assertFalse(salesTerritoryDao.findById(3).isEmpty());
		salesTerritoryDao.delete(st);
		assertTrue(salesTerritoryDao.findById(3).isEmpty());
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonDaoSave() {
		Salesperson sp = new Salesperson();
		sp.setBusinessentityid(n_sp+1);
		sp.setSalesterritory(salesTerritoryDao.findById(1).get());
		sp.setSalesquota(new BigDecimal("15000"));
		sp.setCommissionpct(new BigDecimal("0.5"));
	
		assertNotNull(salesPersonDao);
		assertNull(entityManager.find(Salesperson.class, n_sp+1));
		salesPersonDao.save(sp);
		assertNotNull(entityManager.find(Salesperson.class, n_sp+1));
	}

	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonDaoUpdate() {
		Salesperson sp = salesPersonDao.findById(1).get();
		BigDecimal oldSalesquota = sp.getSalesquota();
		assertTrue(salesPersonDao.findById(1).get().getSalesquota().compareTo(oldSalesquota)==0);
		
		BigDecimal newSalesquota = new BigDecimal("99999");
		sp.setSalesquota(newSalesquota);
		salesPersonDao.update(sp);
		assertTrue(salesPersonDao.findById(1).get().getSalesquota().compareTo(oldSalesquota)!=0);
		assertTrue(salesPersonDao.findById(1).get().getSalesquota().compareTo(newSalesquota)==0);
		sp.setSalesquota(oldSalesquota);
		salesPersonDao.update(sp);
		assertTrue(salesPersonDao.findById(1).get().getSalesquota().compareTo(oldSalesquota)==0);
		assertTrue(salesPersonDao.findById(1).get().getSalesquota().compareTo(newSalesquota)!=0);
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonDaoDelete() {
		Salesperson sp = new Salesperson();
		sp.setBusinessentityid(Integer.MAX_VALUE);
		sp.setSalesterritory(salesTerritoryDao.findById(1).get());
		sp.setSalesquota(new BigDecimal("15000"));
		sp.setCommissionpct(new BigDecimal("0.5"));
		salesPersonDao.save(sp);
		
		assertNotNull(entityManager.find(Salesperson.class,Integer.MAX_VALUE));
		salesPersonDao.delete(sp);
		assertNull(entityManager.find(Salesperson.class,Integer.MAX_VALUE));
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void SalesTerritoryHistoryDaoSave() {
		Salesterritoryhistory sth = new Salesterritoryhistory();
		sth.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(10)));
		sth.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(0)));
		sth.setSalesTerritory(salesTerritoryDao.findById(1).get());
		sth.setSalesPerson(salesPersonDao.findById(1).get());
		
		assertTrue(salesTerritoryHistoryDao.findById(n_sth+1).isEmpty());
		salesTerritoryHistoryDao.save(sth);
		assertFalse(salesTerritoryHistoryDao.findById(n_sth+1).isEmpty());
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void SalesTerritoryHistoryDaoUpdate() {
		Salesterritoryhistory sth = salesTerritoryHistoryDao.findById(1).get();
		Salesperson sp = sth.getSalesPerson();
		Salesterritory st = sth.getSalesTerritory();
		
		sth.setSalesPerson(salesPersonDao.findById(n_sp).get());
		sth.setSalesTerritory(salesTerritoryDao.findById(n_st).get());
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void SalesTerritoryHistoryDaoDelete() {
		
	}
}
