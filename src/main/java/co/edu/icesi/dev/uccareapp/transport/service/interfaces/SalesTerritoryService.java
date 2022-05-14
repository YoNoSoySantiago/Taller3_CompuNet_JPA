package co.edu.icesi.dev.uccareapp.transport.service.interfaces;

import java.util.Optional;


import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;

public interface SalesTerritoryService {
	
	public void add(Salesterritory salesTerritory) throws InvalidValueException, ObjectDoesNotExistException;
	public void edit(Salesterritory salesTerritory) throws InvalidValueException, ObjectDoesNotExistException;
	public void delete(Salesterritory salesTerritory);

	public Optional<Salesterritory> findById(Integer id);
	public Iterable<Salesterritory> findAll();
	
	public void clear();
}
