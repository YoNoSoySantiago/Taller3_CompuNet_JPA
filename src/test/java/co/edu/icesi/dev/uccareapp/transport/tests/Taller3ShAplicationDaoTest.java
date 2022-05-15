package co.edu.icesi.dev.uccareapp.transport.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalespersonDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalespersonquotahistoryDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesterritoryDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesterritoryhistoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salespersonquotahistory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Rollback(false)
@TestInstance(Lifecycle.PER_CLASS)
public class Taller3ShAplicationDaoTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private SalespersonDao salesPersonDao;
	
	@Autowired
	private SalesterritoryDao salesTerritoryDao;
	
	@Autowired 
	private SalespersonquotahistoryDao salesPersonQuotaHistoryDao;
	
	@Autowired
	private SalesterritoryhistoryDao salesTerritoryHistoryDao;
	
	private final int n_st = 5;
	private final int n_sp = 20;
	private final int n_sth = 80;
	private final int n_spqh = 100;
	
	@BeforeAll
	@Transactional
	public void saveSetUp() {
		for(int i = 1;i<=n_st;i++) {
			Salesterritory st = new Salesterritory();
			//st.setTerritoryid(i);
			salesTerritoryDao.save(st);
		}
		
		for(int i = 1;i<=n_sp;i++) {
			Salesperson sp = new Salesperson();
			sp.setCommissionpct(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf((i%(n_sp/4)))));
			sp.setSalesquota(BigDecimal.valueOf((i%(n_sp/4))*1500+7500));
			sp.setSalesterritory(salesTerritoryDao.findById(i%n_st+1));
			sp.setBusinessentityid(i);
			salesPersonDao.save(sp);
		}
		
		for(int i = 1;i<=n_spqh;i++) {
			Salespersonquotahistory spqh = new Salespersonquotahistory();
			spqh.setSalesquota(BigDecimal.valueOf(1500*(i%(n_spqh/10))));
			spqh.setSalesperson(salesPersonDao.findById(i%n_sp+1));
			spqh.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(i%10)));
			//spqh.setId(i);
			salesPersonQuotaHistoryDao.save(spqh);
		}
		
		for(int i = 1;i<=n_sth;i++) {
			Salesterritoryhistory sth = new Salesterritoryhistory();
			sth.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(10+i)));
			sth.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(i)));
			sth.setSalesTerritory(salesTerritoryDao.findById(i%n_st+1));
			sth.setSalesPerson(salesPersonDao.findById(i%n_sp+1));
			//sth.setId(i);
			salesTerritoryHistoryDao.save(sth);
		}
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonFindByTerritoryIdTest() {
		//Contador para contar la cantidad de personas vendedoras, con su respectivo territorio bien asignado'
		int count = 0;
		for(int salesTerritoryId = 1;salesTerritoryId<=n_st;salesTerritoryId++) {
			List<Salesperson> list =  salesPersonDao.findByTerritoryId(salesTerritoryId);
			for(int i =1;i<=n_sp;i++) {
				
				//Siguiendo la misma regla de asignanacion, se compueba que una persona vendedora tenga el id del territorio correctamente asignado
				if(i%n_st+1==salesTerritoryId) {
					count++;
					assertTrue(list.contains(entityManager.find(Salesperson.class,i)));
				}else {
					assertTrue(!list.contains(entityManager.find(Salesperson.class,i)));
				}
			}
		}
		
		//Contador debe ser igual a la cantidad de personas vendedoras
		assertEquals(count,n_sp);
	}	
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salespersonFindByCommisionPctTest() {
		//Contador para contar la cantidad de personas vendedoras, con su commision bien asignada
		int count = 0;
		for(int i = 1; i<=n_sp/4;i++) {
			BigDecimal commision = BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf((i%(n_sp/4))));
			List<Salesperson> list =  salesPersonDao.findByCommisionPct(commision);
			for(int j =1;j<=n_sp;j++) {
				//Siguiendo la misma regla de asignanacion, se compueba que una persona vendedora tenga la commision correctamente asignada
				if(commision.equals(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf((j%(n_sp/4)))))) {
					count++;
					assertTrue(list.contains(entityManager.find(Salesperson.class, j)));
				}else {
					assertTrue(!list.contains(entityManager.find(Salesperson.class, j)));
				}
			}
		}
		//Contador debe ser igual a la cantidad de personas vendedoras
		assertEquals(count,n_sp);
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonFindBySalesquotaTest() {
		//Contador para contar la cantidad de personas vendedoras, con su salesQuota bien asignado
		int count = 0;
		for(int i = 1; i<=n_sp/4;i++) {
			BigDecimal salesQuota = BigDecimal.valueOf((i%(n_sp/4))*1500+7500);
			List<Salesperson> list =  salesPersonDao.findBySalesquota(salesQuota);
			for(int j =1;j<=n_sp;j++) {
				//Siguiendo la misma regla de asignanacion, se compueba que una persona vendedora tenga la salesQuota correctamente asignada
				if(salesQuota.equals(BigDecimal.valueOf((j%(n_sp/4))*1500+7500))) {
					count++;
					assertTrue(list.contains(entityManager.find(Salesperson.class, j)));
				}else {
					assertTrue(!list.contains(entityManager.find(Salesperson.class, j)));
				}
			}
		}
		//Contador debe ser igual a la cantidad de personas vendedoras
		assertEquals(count,n_sp);
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonX11() {
//		List<Object[]> list = salesPersonDao.xd11();
//		for(Object[] ob : list) {
//			System.out.println("ID: "+((Salesperson)ob[0]).getBusinessentityid());
//			System.out.println("Count: "+ob[1]);
//		}
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonX12() {
//		Salesterritory st = salesTerritoryDao.findById(1);
//		Date startDate = Date.valueOf(LocalDate.now().minusDays(15));
//		Date endDate = Date.valueOf(LocalDate.now());
//		
//		salesPersonDao.xd12(st, startDate, endDate);
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesTerritoryFindWhenAleastTwoSalesPersonWithSalesquotaHiggerThan10000() {
		
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonQuotaHistoryFindByBusinessentityid() {
		int count=0;
		for(int i = 1;i<=n_sp;i++) {
			List<Salespersonquotahistory> list = salesPersonQuotaHistoryDao.findByBusinessentityid(i);
			for(int j=1;j<=n_spqh;j++) {
				if(j%n_sp+1==i) {
					count++;
					assertTrue(list.contains(entityManager.find(Salespersonquotahistory.class, j)));
				}else {
					assertTrue(!list.contains(entityManager.find(Salespersonquotahistory.class, j)));
				}
			}
		}
		assertEquals(count,n_spqh);
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonQuotaHistoryFindBySalesquota() {
		int count = 0;
		for(int i = 1; i<=n_spqh/10;i++) {
			BigDecimal salesQuota = BigDecimal.valueOf(1500*(i%(n_spqh/10)));
			List<Salespersonquotahistory> list =  salesPersonQuotaHistoryDao.findBySalesquota(salesQuota);
			for(int j =1;j<=n_spqh;j++) {
				if(salesQuota.equals(BigDecimal.valueOf(1500*(j%(n_spqh/10))))) {
					count++;
					assertTrue(list.contains(entityManager.find(Salespersonquotahistory.class, j)));
				}else {
					assertTrue(!list.contains(entityManager.find(Salespersonquotahistory.class, j)));
				}
			}
		}
		assertEquals(count,n_spqh);
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesTerritoryHistoryFindByTerritoryId() {
		int count = 0;
		for(int i = 0; i<=n_st;i++) {
			List<Salesterritoryhistory> list = salesTerritoryHistoryDao.findByTerritoryid(i);
			for(int j = 1;j<=n_sth;j++) {
				if(j%n_st+1 == i) {
					count++;
					assertTrue(list.contains(entityManager.find(Salesterritoryhistory.class, j)));
				}else {
					assertTrue(!list.contains(entityManager.find(Salesterritoryhistory.class, j)));
				}
			}
		}
		assertEquals(count,n_sth);
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesTerritoryHistoryFindByBusinessentity() {
		int count = 0;
		for(int i = 0; i<=n_sp;i++) {
			List<Salesterritoryhistory> list = salesTerritoryHistoryDao.findByBusinessentity(i);
			for(int j = 1;j<=n_sth;j++) {
				if(j%n_sp+1 == i) {
					count++;
					assertTrue(list.contains(entityManager.find(Salesterritoryhistory.class, j)));
				}else {
					assertTrue(!list.contains(entityManager.find(Salesterritoryhistory.class, j)));
				}
			}
		}
		assertEquals(count,n_sth);
	}
}
