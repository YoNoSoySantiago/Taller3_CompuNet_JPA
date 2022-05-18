package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

public interface SalesTerritoryDao {
	public void save(Salesterritory entity);
	public void update(Salesterritory entity);
	public void delete(Salesterritory entity);
	public Optional<Salesterritory> findById(Integer id);
	public List<Salesterritory> findAll();
	public List<Salesterritory> specialQuery();
	public void deleteAll();
}
