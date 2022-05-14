package co.edu.icesi.dev.uccareapp.transport.service.interfaces;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;
public interface CountryRegionService {
	public void add(Countryregion countryRegion);
	public Optional<Countryregion> findById(String coruntryCode);
	public Iterable<Countryregion> findAll();
	public void clear();
}
