package co.edu.icesi.dev.uccareapp.transport.service.interfaces;

import java.util.Optional;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectAlreadyExistException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;

public interface SalesTerritoryHistoryService {

	public void add(Salesterritoryhistory salesTerritoryHistory,Integer businessId, Integer territoryId) throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException;
	public void edit(Salesterritoryhistory salesTerritoryHistory) throws InvalidValueException, ObjectDoesNotExistException;

	public Optional<Salesterritoryhistory> findById(Integer id);
	public Iterable<Salesterritoryhistory> findAll();
	
	public void clear();
	public void delete(Salesterritoryhistory salesterritoryhistory);
}
