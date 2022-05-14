package co.edu.icesi.dev.uccareapp.transport.dao.implementation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalespersonDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

@Repository
@Scope("singleton")
public class SalespersonDaoImp implements SalespersonDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public void save(Salesperson entity) {
		entityManager.persist(entity);	
	}
	
	@Transactional
	@Override
	public void update(Salesperson entity) {
		entityManager.merge(entity);	
	}
	
	@Transactional
	@Override
	public void delete(Salesperson entity) {
		entityManager.remove(entity);	
	}

	@Override
	public Salesperson findById(Integer id) {
		return entityManager.find(Salesperson.class, id);
	}

	@Override
	public List<Salesperson> findAll() {
		String jpql = "Select sp from Salesperson sp";
		return 	entityManager.createQuery(jpql).getResultList();	
	}
	
	@Override
	public List<Salesperson> findByTerritoryId(Integer id) {
		String jpql = "SELECT st.salespersons FROM Salesterritory st "
					+ "WHERE st.territoryid="+id;
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	@Override
	public List<Salesperson> findByCommisionPct(BigDecimal commisionpct) {
		String jpql = "SELECT sp FROM Salesperson sp "
					+ "WHERE sp.commissionpct="+commisionpct;
		return 	entityManager.createQuery(jpql).getResultList();	
	}

	@Override
	public List<Salesperson> findBySalesquota(BigDecimal salesquota) {
		String jpql = "SELECT sp FROM Salesperson sp "
					+ "WHERE sp.salesquota="+salesquota;
		return 	entityManager.createQuery(jpql).getResultList();
	}
	
	public List<Salesperson> xd12(Salesterritory salesterritory, Date startDate, Date endDate ){
		String jpql = "SELECT sp FROM st.salespersons "
					+ "WHERE (SELECT COUNT(sth) FROM Salesterritoryhistory sth "
					+ "WHERE sth MENBER OF sp.salesterritoryhistories "
					+ "AND sth.startdate>=startDate "
					+ "AND sth.enddate<=endDate)>0";
		Query query = entityManager.createQuery(jpql);	
		query.setParameter("st", salesterritory);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
	return 	query.getResultList();
	}

	
}
