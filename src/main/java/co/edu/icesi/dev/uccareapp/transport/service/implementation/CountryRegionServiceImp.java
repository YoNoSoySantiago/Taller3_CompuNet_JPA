package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.CountryRegionDao;
import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.CountryRegionService;

@Service
public class CountryRegionServiceImp implements CountryRegionService {
	
	private CountryRegionDao countryRegionDao;
	
	@Autowired
	public CountryRegionServiceImp(CountryRegionDao crr) {
		countryRegionDao = crr;
	}
	@Override
	public void add(Countryregion countryRegion) {
		this.countryRegionDao.save(countryRegion);
	}

	@Override
	public Optional<Countryregion> findById(String coruntryCode) {
		return this.countryRegionDao.findById(coruntryCode);
	}
	@Override
	public Iterable<Countryregion> findAll() {
		
		return this.countryRegionDao.findAll();
	}
	@Override
	public void clear() {
		this.countryRegionDao.deleteAll();
	}
}
