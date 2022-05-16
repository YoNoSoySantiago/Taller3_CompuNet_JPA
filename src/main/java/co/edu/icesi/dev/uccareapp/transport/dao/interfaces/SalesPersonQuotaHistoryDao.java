package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import co.edu.icesi.dev.uccareapp.transport.model.sales.Salespersonquotahistory;

public interface SalesPersonQuotaHistoryDao {
	
	public void save(Salespersonquotahistory entity);
	public void update(Salespersonquotahistory entity);
	public void delete(Salespersonquotahistory entity);
	public Optional<Salespersonquotahistory> findById(Integer id);
	public List<Salespersonquotahistory> findAll();
	
	public List<Salespersonquotahistory> findByBusinessentityid(Integer id);
	public List<Salespersonquotahistory> findBySalesquota(BigDecimal salesquota);
	public void deleteAll();
}
