package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.util.List;

import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;

public interface SalesterritoryhistoryDao {
	
	public void save(Salesterritoryhistory entity);
	public void update(Salesterritoryhistory entity);
	public void delete(Salesterritoryhistory entity);
	public Salesterritoryhistory findById(Integer id);
	public List<Salesterritoryhistory> findAll();
	
	public List<Salesterritoryhistory> findByTerritoryid(Integer id);
	public List<Salesterritoryhistory> findByBusinessentity(Integer id);
}
