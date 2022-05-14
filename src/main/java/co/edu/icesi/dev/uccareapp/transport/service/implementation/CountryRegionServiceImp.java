package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;
import co.edu.icesi.dev.uccareapp.transport.repository.CountryRegionRepository;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.CountryRegionService;

@Service
public class CountryRegionServiceImp implements CountryRegionService {
	
	private CountryRegionRepository countryRegionRepository;
	
	@Autowired
	public CountryRegionServiceImp(CountryRegionRepository crr) {
		countryRegionRepository = crr;
	}
	@Override
	public void add(Countryregion countryRegion) {
		this.countryRegionRepository.save(countryRegion);
	}

	@Override
	public Optional<Countryregion> findById(String coruntryCode) {
		return this.countryRegionRepository.findById(coruntryCode);
	}
	@Override
	public Iterable<Countryregion> findAll() {
		
		return this.countryRegionRepository.findAll();
	}
	@Override
	public void clear() {
		this.countryRegionRepository.deleteAll();
	}
}
