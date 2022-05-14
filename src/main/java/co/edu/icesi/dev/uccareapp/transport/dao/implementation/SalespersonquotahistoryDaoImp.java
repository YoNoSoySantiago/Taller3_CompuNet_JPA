package co.edu.icesi.dev.uccareapp.transport.dao.implementation;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalespersonquotahistoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salespersonquotahistory;

@Repository
@Scope("singleton")
public class SalespersonquotahistoryDaoImp implements SalespersonquotahistoryDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public void save(Salespersonquotahistory entity) {
		entityManager.persist(entity);
	}

	@Transactional
	@Override
	public void update(Salespersonquotahistory entity) {
		entityManager.merge(entity);
	}
	
	@Transactional
	@Override
	public void delete(Salespersonquotahistory entity) {
		entityManager.remove(entity);
	}

	@Override
	public Salespersonquotahistory findById(Integer id) {
		return entityManager.find(Salespersonquotahistory.class,id);
	}

	@Override
	public List<Salespersonquotahistory> findAll() {
		String jpql = "Select spqh from Salespersonquotahistory spqh ";
		return 	entityManager.createQuery(jpql).getResultList();
	}
	
	@Override
	public List<Salespersonquotahistory> findByBusinessentityid(Integer id) {
		String jpql = "SELECT sp.salespersonquotahistories FROM Salesperson sp "
					+ "WHERE sp.businessentityid="+id;
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	@Override
	public List<Salespersonquotahistory> findBySalesquota(BigDecimal salesquota) {
		String jpql = "SELECT spqh FROM Salespersonquotahistory spqh "
					+ "WHERE spqh.salesquota="+salesquota;
		return 	entityManager.createQuery(jpql).getResultList();
	}

	

}
