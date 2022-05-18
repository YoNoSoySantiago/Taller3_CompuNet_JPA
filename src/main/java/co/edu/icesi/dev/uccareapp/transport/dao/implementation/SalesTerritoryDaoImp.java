package co.edu.icesi.dev.uccareapp.transport.dao.implementation;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

@Repository
@Scope("singleton")
public class SalesTerritoryDaoImp implements SalesTerritoryDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public SalesTerritoryDaoImp(EntityManager em) {
		this.entityManager = em;
	}
	
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
	public Optional<Salesterritory> findById(Integer id) {
		Salesterritory entity = entityManager.find(Salesterritory.class,id);
		
		if(entity==null) return Optional.empty();
		return Optional.of(entity);
	}

	@Override
	public List<Salesterritory> findAll() {
		String jpql = "Select st from Salesterritory st ";
		return 	entityManager.createQuery(jpql).getResultList();
	}
	
	@Override
	public List<Salesterritory> specialQuery(){
		String jpql = "SELECT st FROM Salesterritory st "
					+ "WHERE (SELECT COUNT(sp) FROM Salesperson sp WHERE sp MEMBER OF st.salespersons "
					+ "AND sp.salesquota>10000)>=2";
	return 	entityManager.createQuery(jpql).getResultList();
	}

	@Override
	public void deleteAll() {
		String jpql = "DELETE FROM Salesterritory";
		entityManager.createQuery(jpql).executeUpdate();
	}

	
}
