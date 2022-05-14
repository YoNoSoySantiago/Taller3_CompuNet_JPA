package co.edu.icesi.dev.uccareapp.transport.tests;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
	
	@Autowired
	private SalespersonDao salesPersonDao;
	
	@Autowired
	private SalesterritoryDao salesTerritoryDao;
	
	@Autowired 
	private SalespersonquotahistoryDao salesPersonQuotaHistoryDao;
	
	@Autowired
	private SalesterritoryhistoryDao salesTerritoryHistoryDao;
	
	private int n_st = 5;
	private int n_sp = 20;
	private int n_sth = 80;
	private int n_spqh = 100;
	
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
			sp.setCommissionpct(BigDecimal.valueOf(0.05*i));
			sp.setSalesquota(BigDecimal.valueOf(i*500+7500));
			sp.setSalesterritory(salesTerritoryDao.findById(i%n_st));
			sp.setBusinessentityid(i);
			salesPersonDao.save(sp);
		}
		
		for(int i = 1;i<=n_spqh;i++) {
			Salespersonquotahistory spqh = new Salespersonquotahistory();
			spqh.setSalesquota(BigDecimal.valueOf(1000+Math.random()*10000));
			spqh.setSalesperson(salesPersonDao.findById(i%n_sp));
			spqh.setModifieddate(Timestamp.valueOf(LocalDateTime.now().minusDays(i%10)));
			//spqh.setId(i);
			salesPersonQuotaHistoryDao.save(spqh);
		}
		
		for(int i = 1;i<=n_sth;i++) {
			Salesterritoryhistory sth = new Salesterritoryhistory();
			sth.setStartdate(Timestamp.valueOf(LocalDateTime.now().minusDays(10+i)));
			sth.setEnddate(Timestamp.valueOf(LocalDateTime.now().minusDays(i)));
			sth.setSalesTerritory(salesTerritoryDao.findById(i%n_st));
			sth.setSalesPerson(salesPersonDao.findById(i%n_sp));
			//sth.setId(i);
			salesTerritoryHistoryDao.save(sth);
		}
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void salesPersonFindByTerritoryIdTest() {
		System.out.println("hola");
		List<Salesperson> list =  salesPersonDao.findByTerritoryId(1);
		System.out.println(Arrays.toString(list.toArray()));
	}	
}
