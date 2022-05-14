package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.util.List;

import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

public interface SalesterritoryDao {
	public void save(Salesterritory entity);
	public void update(Salesterritory entity);
	public void delete(Salesterritory entity);
	public Salesterritory findById(Integer id);
	public List<Salesterritory> findAll();
}
