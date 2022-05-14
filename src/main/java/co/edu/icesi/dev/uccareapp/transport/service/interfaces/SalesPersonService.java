package co.edu.icesi.dev.uccareapp.transport.service.interfaces;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectAlreadyExistException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;

public interface SalesPersonService {
	
	public void add(Salesperson salesPerson,Integer BusinessId,Integer territoryId) throws InvalidValueException, ObjectAlreadyExistException, ObjectDoesNotExistException;
	public void edit(Salesperson salesPerson) throws InvalidValueException, ObjectDoesNotExistException ;
	public void delete(Salesperson salesperson);
	
	public Optional<Salesperson> findById(Integer id);
	public Iterable<Salesperson> findAll();
	
	public void clear();
	
}
