package co.edu.icesi.dev.uccareapp.transport.dao.implementation;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.CountryRegionDao;
import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;

@Repository
@Scope("singleton")
public class CountryRegionDaoImp implements CountryRegionDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public void save(Countryregion countryRegion) {
		entityManager.persist(countryRegion);
	}

	@Override
	public Optional<Countryregion> findById(String coruntryCode) {
		
		return Optional.of(entityManager.find(Countryregion.class, coruntryCode));
	}

	@Override
	public Iterable<Countryregion> findAll() {
		String jpql = "Select cr from Countryregion cr";
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	@Override
	public void deleteAll() {
		String jpql = "DELETE FROM Countryregion";
		entityManager.createQuery(jpql).executeUpdate();
	}


}
