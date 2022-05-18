package co.edu.icesi.dev.uccareapp.transport.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.util.Arrays;
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
public class Taller3ShAplicationDaoTest {
	
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
			salesTerritoryDao.save(st);
		}
		
		for(int i = 1;i<=n_sp;i++) {
			Salesperson sp = new Salesperson();
			sp.setCommissionpct(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf((i%(n_sp/4)))));
			sp.setSalesquota(BigDecimal.valueOf((i%(n_st))*1500+7500));
			sp.setSalesterritory(salesTerritoryDao.findById(i%n_st+1).get());
			sp.setBusinessentityid(i);
			salesPersonDao.save(sp);
		}
		
		for(int i = 1;i<=n_spqh;i++) {
			Salespersonquotahistory spqh = new Salespersonquotahistory();
			spqh.setSalesquota(BigDecimal.valueOf(1500*(i%(n_spqh/10))));
			spqh.setSalesperson(salesPersonDao.findById(i%n_sp+1).get());
			spqh.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(i%10)));
			//spqh.setId(i);
			salesPersonQuotaHistoryDao.save(spqh);
		}
		
		for(int i = 1;i<=n_sth;i++) {
			Salesterritoryhistory sth = new Salesterritoryhistory();
			sth.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(10+i)));
			sth.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(i)));
			sth.setSalesTerritory(salesTerritoryDao.findById(i%n_st+1).get());
			sth.setSalesPerson(salesPersonDao.findById(i%n_sp+1).get());
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
	public void salesPersonSpecialQuery() {
		Salesterritory st = salesTerritoryDao.findById(1).get();
		Date startdate = Date.valueOf(LocalDate.now().minusDays(50));
		Date enddate = Date.valueOf(LocalDate.now().minusDays(20));
		Map<Salesperson,Integer> map = salesPersonDao.specialQuery(st, startdate, enddate);
		for(Salesperson sp: st.getSalespersons()) {
			boolean contained = false;
			for(Salesterritoryhistory sth:sp.getSalesterritoryhistories()) {
				if(sth.getStartdate().compareTo(startdate)>=0 && sth.getEnddate().compareTo(enddate)<=0) {
					contained = true;
					break;
				}
			}
			if(contained) {
				assertTrue(map.containsKey(sp));
				assertTrue(sp.getSalesterritoryhistories().size()+1==map.get(sp));
			}else {
				assertTrue(!map.containsKey(sp));
			}
		}
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesTerritorySecialQuery() {
		Map<Integer,Integer> st_repeats = new HashMap<>();
		
		for(int i = 0; i<n_sp;i++) {
			if(!st_repeats.containsKey(i%n_st+1)) {
				st_repeats.put(i%n_st+1, 0);
			}
			if((i%(n_st))*1500+7500>10000){
				st_repeats.put(i%n_st+1, st_repeats.get(i%n_st+1)+1);
			}
		}
		List<Salesterritory> listFiltered = salesTerritoryDao.specialQuery();
		List<Salesterritory> list = salesTerritoryDao.findAll();
		
		for(Salesterritory st:list) {
			if(st_repeats.get(st.getTerritoryid())>=2) {
				assertTrue(listFiltered.contains(st));
			}else {
				assertTrue(!listFiltered.contains(st));
			}
		}
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
