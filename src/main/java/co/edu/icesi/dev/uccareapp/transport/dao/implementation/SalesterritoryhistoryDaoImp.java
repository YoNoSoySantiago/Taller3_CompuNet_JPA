package co.edu.icesi.dev.uccareapp.transport.dao.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesterritoryhistoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;

@Repository
@Scope("singleton")
public class SalesterritoryhistoryDaoImp implements SalesterritoryhistoryDao{

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public void save(Salesterritoryhistory entity) {
		entityManager.persist(entity);
	}

	@Transactional
	@Override
	public void update(Salesterritoryhistory entity) {
		entityManager.merge(entity);
	}

	@Transactional
	@Override
	public void delete(Salesterritoryhistory entity) {
		entityManager.remove(entity);
	}

	@Override
	public Salesterritoryhistory findById(Integer id) {
		return entityManager.find(Salesterritoryhistory.class,id);
	}

	@Override
	public List<Salesterritoryhistory> findAll() {
		String jpql = "Select sth from Salesterritoryhistory sth ";
		return 	entityManager.createQuery(jpql).getResultList();
	}
	
	@Override
	public List<Salesterritoryhistory> findByTerritoryid(Integer id) {
		String jpql = "SELECT st.salesterritoryhistories FROM Salesterritory st " 
					+ "WHERE st.territoryid="+id;
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	@Override
	public List<Salesterritoryhistory> findByBusinessentity(Integer id) {
		String jpql = "SELECT sp.salesterritoryhistories FROM Salesperson sp "
					+ "WHERE sp.businessentityid="+id;
		return 	entityManager.createQuery(jpql).getResultList();	
	}
}
