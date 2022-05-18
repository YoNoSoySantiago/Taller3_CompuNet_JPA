package co.edu.icesi.dev.uccareapp.transport.dao.implementation;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.BusinessentityDao;
import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;

@Repository
@Scope("singleton")
public class BusinessentityDaoImp implements BusinessentityDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public BusinessentityDaoImp(EntityManager em) {
		this.entityManager=em;
	}
	
	@Transactional
	@Override
	public void add(Businessentity businessEntiry) {
		entityManager.persist(businessEntiry);
	}
	
	@Override
	public Optional<Businessentity> findById(Integer id) {
		Businessentity entity = entityManager.find(Businessentity.class, id);
		if(entity==null) return Optional.empty();
		return Optional.of(entity);
	}

	@Override
	public Iterable<Businessentity> findAll() {
		String jpql = "Select be from Businessentity be";
		return 	entityManager.createQuery(jpql).getResultList();	
	}
	
	@Transactional
	@Override
	public void deleteAll() {
		String jpql = "DELETE FROM Businessentity";
		entityManager.createQuery(jpql).executeUpdate();;
	}

}
