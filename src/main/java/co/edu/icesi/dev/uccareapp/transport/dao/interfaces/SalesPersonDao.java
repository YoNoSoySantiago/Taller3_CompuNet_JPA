package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

public interface SalesPersonDao {
	public void save(Salesperson entity);
	public void update(Salesperson entity);
	public void delete(Salesperson entity);
	public Optional<Salesperson> findById(Integer id);
	public List<Salesperson> findAll();
	
	public List<Salesperson> findByTerritoryId(Integer id);
	public List<Salesperson> findByCommisionPct(BigDecimal commisionpct);
	public List<Salesperson> findBySalesquota(BigDecimal salesquota);
	public Map<Salesperson, Integer> specialQuery(Salesterritory salesterritory, Date startDate, Date endDate);
	public void deleteAll();
}
