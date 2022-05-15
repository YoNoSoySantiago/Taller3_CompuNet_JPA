package co.edu.icesi.dev.uccareapp.transport.dao.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesterritoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

@Repository
@Scope("singleton")
public class SalesterritoryDaoImp implements SalesterritoryDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public void save(Salesterritory entity) {
		entityManager.persist(entity);
	}

	@Transactional
	@Override
	public void update(Salesterritory entity) {
		entityManager.merge(entity);
	}

	@Transactional
	@Override
	public void delete(Salesterritory entity) {
		entityManager.remove(entity);
	}

	@Override
	public Salesterritory findById(Integer id) {
		return entityManager.find(Salesterritory.class,id);
	}

	@Override
	public List<Salesterritory> findAll() {
		String jpql = "Select st from Salesterritory st ";
		return 	entityManager.createQuery(jpql).getResultList();
	}
	
	@Override
	public List<Salesterritory> findWhenAleastTwoSalesPersonWithSalesquotaHiggerThan10000(){
		String jpql = "SELECT st FROM Salesterritory st "
					+ "WHERE (SELECT COUNT(sp) FROM Salesperson sp WHERE sp MEMBER OF st.salespersons "
					+ "AND sp.salesquota>10000)>=2";
	return 	entityManager.createQuery(jpql).getResultList();
	}

	
}
