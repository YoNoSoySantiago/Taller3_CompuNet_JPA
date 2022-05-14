package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.repository.CountryRegionRepository;
import co.edu.icesi.dev.uccareapp.transport.repository.SalesTerritoryRepository;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryService;
@Service
public class SalesTerritoryServiceImp implements SalesTerritoryService {
	
	private SalesTerritoryRepository salesTerritoryRespository;
	private CountryRegionRepository countryRegionRegpository;
	
	@Autowired
	public SalesTerritoryServiceImp(SalesTerritoryRepository str,CountryRegionRepository crr) {
		this.salesTerritoryRespository = str;
		this.countryRegionRegpository = crr;
	}
	@Override
	public void add(Salesterritory salesTerritory) throws InvalidValueException, ObjectDoesNotExistException {
		if(
			salesTerritory.getName()==null ||
			salesTerritory.getCountryregioncode()==null
			) {
			throw new NullPointerException("Values empties or null");
		}
		Optional<Countryregion> countryCode = this.countryRegionRegpository.findById(salesTerritory.getCountryregioncode());
		if(!countryCode.isEmpty()) {
			if(salesTerritory.getName().length()<5) {
				throw new InvalidValueException("The lenght of the name must to be al least 5");
			}
			this.salesTerritoryRespository.save(salesTerritory);
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
		Optional<Countryregion> countryCode = this.countryRegionRegpository.findById(salesTerritory.getCountryregioncode());
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
			this.salesTerritoryRespository.save(oldSalesTerritory);
		}else {
			throw new ObjectDoesNotExistException("This region code, does not exist");
		}
	}
	
	@Override
	public void delete(Salesterritory salesTerritory) {
		this.salesTerritoryRespository.delete(salesTerritory);
	}

	@Override
	public Optional<Salesterritory> findById(Integer id) {
		return this.salesTerritoryRespository.findById(id);
	}

	@Override
	public Iterable<Salesterritory> findAll() {
		return this.salesTerritoryRespository.findAll();
	}
	@Override
	public void clear() {
		this.salesTerritoryRespository.deleteAll();
	}

}
