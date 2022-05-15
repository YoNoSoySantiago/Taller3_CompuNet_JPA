package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

public interface SalespersonDao {
	public void save(Salesperson entity);
	public void update(Salesperson entity);
	public void delete(Salesperson entity);
	public Salesperson findById(Integer id);
	public List<Salesperson> findAll();
	
	public List<Salesperson> findByTerritoryId(Integer id);
	public List<Salesperson> findByCommisionPct(BigDecimal commisionpct);
	public List<Salesperson> findBySalesquota(BigDecimal salesquota);
	public List<Object[]> xd11();
	public List<Salesperson> xd12(Salesterritory salesterritory, Date startDate, Date endDate);
}
