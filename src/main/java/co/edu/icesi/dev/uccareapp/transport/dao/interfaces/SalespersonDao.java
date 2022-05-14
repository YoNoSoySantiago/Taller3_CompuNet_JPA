package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;

public interface SalespersonDao {
	public void save(Salesperson entity);
	public void update(Salesperson entity);
	public void delete(Salesperson entity);
	public Salesperson findById(Integer id);
	public List<Salesperson> findAll();
	
	public List<Salesperson> findByTerritoryId(Integer id);
	public List<Salesperson> findByCommisionPct(BigDecimal commisionpct);
	public List<Salesperson> findBySalesquota(BigDecimal salesquota);
}
