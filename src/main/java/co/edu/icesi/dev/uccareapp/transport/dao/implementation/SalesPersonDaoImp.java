package co.edu.icesi.dev.uccareapp.transport.dao.implementation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

@Repository
@Scope("singleton")
public class SalesPersonDaoImp implements SalesPersonDao {
	
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
	public Optional<Salesperson> findById(Integer id) {
		return Optional.of(entityManager.find(Salesperson.class, id));
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
	
	@Override
	public List<Object[]> xd11() {
//		SELECT DISTINCT e FROM Employee e INNER JOIN e.tasks t where t.supervisor='Denise
//		String jpql = "SELECT DISTINCT sp FROM Salesperson as sp "
//					+ "INNER JOIN SELECT COUNT(sth) FROM Salesterritoryhistory sth "
//					+ "WHERE sth MENBER OF sp.salesterritoryhistories"
//					+ "ORDER BY sp.salesquota";
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		Root<Salesperson> country = query.from(Salesperson.class);
//		Expression<Integer> sum = cb.sum(path, param);
		return 	null;
	}
	
	@Override
	public List<Salesperson> xd12(Salesterritory salesterritory, Date startDate, Date endDate ){
		String jpql = "SELECT sp FROM Salesperson sp "
					+ "WHERE (SELECT COUNT(sth) FROM Salesterritoryhistory sth "
					+ "WHERE sth MEMBER OF sp.salesterritoryhistories "
					+ "AND sth.startdate>=startDate "
					+ "AND sth.enddate<=endDate)>0 "
					+ "AND st=sp.salesterritory.territoryid ";
		Query query = entityManager.createQuery(jpql);	
		query.setParameter("st", salesterritory.getTerritoryid());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return 	query.getResultList();
	}

	@Override
	public void deleteAll() {
		String jpql = "DELETE FROM Salesperson";
		entityManager.createQuery(jpql).executeUpdate();
	}

	
}
