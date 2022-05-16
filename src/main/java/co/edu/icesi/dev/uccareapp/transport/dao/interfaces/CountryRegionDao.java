package co.edu.icesi.dev.uccareapp.transport.dao.interfaces;

import java.util.Optional;

import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;

public interface CountryRegionDao {
	public void save(Countryregion countryRegion);
	public Optional<Countryregion> findById(String coruntryCode);
	public Iterable<Countryregion> findAll();
	public void deleteAll();
}
