package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.CountryRegionDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryService;
@Service
public class SalesTerritoryServiceImp implements SalesTerritoryService {
	
	private SalesTerritoryDao salesTerritoryDao;
	private CountryRegionDao countryRegionDao;
	
	@Autowired
	public SalesTerritoryServiceImp(SalesTerritoryDao str,CountryRegionDao crr) {
		this.salesTerritoryDao = str;
		this.countryRegionDao = crr;
	}
	@Override
	public void add(Salesterritory salesTerritory) throws InvalidValueException, ObjectDoesNotExistException {
		if(
			salesTerritory.getName()==null ||
			salesTerritory.getCountryregioncode()==null
			) {
			throw new NullPointerException("Values empties or null");
		}
		
		Optional<Countryregion> countryCode = this.countryRegionDao.findById(salesTerritory.getCountryregioncode());
		if(!countryCode.isEmpty()) {
			if(salesTerritory.getName().length()<5) {
				throw new InvalidValueException("The lenght of the name must to be al least 5");
			}
			this.salesTerritoryDao.save(salesTerritory);
		}else {
			throw new ObjectDoesNotExistException("This region code, does not exist");
		}
		
	}

	@Override
	public void edit(Salesterritory salesTerritory) throws InvalidValueException, ObjectDoesNotExistException {
		if(
				salesTerritory.getName()==null ||
				salesTerritory.getCountryregioncode()==null ||
				salesTerritory.getTerritoryid() == null
				) {
				throw new NullPointerException("Values empties or null");
			}
		Optional<Countryregion> countryCode = this.countryRegionDao.findById(salesTerritory.getCountryregioncode());
		if(!countryCode.isEmpty()) {
			Optional<Salesterritory> optTerritory = findById(salesTerritory.getTerritoryid());
			if(optTerritory.isEmpty()) {
				throw new ObjectDoesNotExistException("Not exist a Sales Territory with this ID");
			}
			if(salesTerritory.getName().length()<5) {
				throw new InvalidValueException("The lenght of the name must to be al least 5");
			}
			Salesterritory oldSalesTerritory = optTerritory.get();
			oldSalesTerritory.setName(salesTerritory.getName());
			oldSalesTerritory.setCountryregioncode(salesTerritory.getCountryregioncode());
			this.salesTerritoryDao.update(oldSalesTerritory);
		}else {
			throw new ObjectDoesNotExistException("This region code, does not exist");
		}
	}
	
	@Override
	public void delete(Salesterritory salesTerritory) {
		this.salesTerritoryDao.delete(salesTerritory);
	}

	@Override
	public Optional<Salesterritory> findById(Integer id) {
		return this.salesTerritoryDao.findById(id);
	}

	@Override
	public Iterable<Salesterritory> findAll() {
		return this.salesTerritoryDao.findAll();
	}
	@Override
	public void clear() {
		this.salesTerritoryDao.deleteAll();
	}

}
