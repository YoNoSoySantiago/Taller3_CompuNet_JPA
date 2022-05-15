package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.util.Optional;

import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;

public interface BusinessentityDao {
	public void add(Businessentity businessEntiry);
	public Optional<Businessentity> findById(Integer id);
	public Iterable<Businessentity> findAll();
	public void deleteAll();
}
