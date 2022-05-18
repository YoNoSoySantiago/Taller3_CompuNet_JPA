package co.edu.icesi.dev.uccareapp.transport.dao.implementation;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
	
	public SalesPersonDaoImp(EntityManager em) {
		this.entityManager = em;
	}
	
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
		
		Salesperson entity = entityManager.find(Salesperson.class, id);
		if(entity==null) return Optional.empty();
		return Optional.of(entity);
	}

	@Override
	public List<Salesperson> findAll() {
		String jpql = "Select sp from Salesperson sp";
		return 	entityManager.createQuery(jpql,Salesperson.class).getResultList();	
	}
	
	@Override
	public void deleteAll() {
		String jpql = "DELETE FROM Salesperson";
		entityManager.createQuery(jpql).executeUpdate();
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
		return 	entityManager.createQuery(jpql,Salesperson.class).getResultList();	
	}

	@Override
	public List<Salesperson> findBySalesquota(BigDecimal salesquota) {
		String jpql = "SELECT sp FROM Salesperson sp "
					+ "WHERE sp.salesquota="+salesquota;
		return 	entityManager.createQuery(jpql,Salesperson.class).getResultList();
	}

	@Override
	public Map<Salesperson, Integer> specialQuery(Salesterritory salesterritory, Date startDate, Date endDate) {
		
		String jpql = "SELECT sp as salesPerson,(SIZE(sp.salesterritoryhistories)+1) as spCount FROM Salesperson sp, Salesterritoryhistory sthAux "
					+ "WHERE sthAux MEMBER OF sp.salesterritoryhistories "
					+ "AND sp.salesterritory.territoryid= :stID "
					+ "AND sthAux.startdate>= :startdate "
					+ "AND sthAux.enddate<= :enddate "
					+ "GROUP BY sp.businessentityid "
					+ "ORDER BY sp.salesquota";
		TypedQuery<Object[]> query = entityManager.createQuery(jpql,Object[].class);
		query.setParameter("stID",salesterritory.getTerritoryid());
		query.setParameter("startdate", startDate);
		query.setParameter("enddate", endDate);
		
		return query.getResultList().stream()
				.collect(
					    Collectors.toMap(
					        ob -> ((Salesperson) ob[0]),
					        ob -> ((Integer) ob[1])
					    )
					);
	}

	
}
