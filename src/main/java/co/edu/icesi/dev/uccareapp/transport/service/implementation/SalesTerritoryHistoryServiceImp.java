package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectAlreadyExistException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryHistoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryHistoryService;
@Service
public class SalesTerritoryHistoryServiceImp implements SalesTerritoryHistoryService {
	
	private SalesTerritoryHistoryDao salesTerritoryHistoryDao;
	private SalesTerritoryDao salesTerritoryDao;
	private SalesPersonDao salesPersonDao;
	@Autowired
	public SalesTerritoryHistoryServiceImp(SalesTerritoryHistoryDao sthr,SalesTerritoryDao str,SalesPersonDao spr) {
		salesTerritoryHistoryDao = sthr;
		salesTerritoryDao = str;
		salesPersonDao = spr;
	}

	@Override
	public void add(Salesterritoryhistory salesTerritoryHistory,Integer businessId, Integer territoryId) throws InvalidValueException, ObjectDoesNotExistException, ObjectAlreadyExistException {
		if(
			salesTerritoryHistory.getEnddate()==null||
			salesTerritoryHistory.getStartdate()==null||
			businessId == null||
			territoryId==null){
			
				throw new NullPointerException("Values empties or null");
		}
		
		Optional<Salesterritory> opTerritory = this.salesTerritoryDao.findById(territoryId);
		Optional<Salesperson> opSalesPerson = this.salesPersonDao.findById(businessId);
		
		if(salesTerritoryHistory.getEnddate().compareTo(Timestamp.valueOf(LocalDateTime.now()))>0) {
			throw new InvalidValueException("The end date have to be lower than the current date");
		}
		if(salesTerritoryHistory.getStartdate().compareTo(salesTerritoryHistory.getEnddate())>=0) {
			throw new InvalidValueException("The moddified data no should be equals or higgier to the end data");
		}
		if(opSalesPerson.isEmpty()) {
			throw new ObjectDoesNotExistException("this sales person does not exist");
		}
		if(opTerritory.isEmpty()) {
			throw new ObjectDoesNotExistException("The territory does not exist");
		}
		salesTerritoryHistory.setSalesPerson(opSalesPerson.get());
		salesTerritoryHistory.setSalesTerritory(opTerritory.get());
		this.salesTerritoryHistoryDao.save(salesTerritoryHistory);
	}

	@Override
	public void edit(Salesterritoryhistory salesTerritoryHistory) throws InvalidValueException, ObjectDoesNotExistException {
		if(		salesTerritoryHistory.getSalesPerson()==null||
				salesTerritoryHistory.getSalesTerritory()==null||
				salesTerritoryHistory.getId()==null||
				salesTerritoryHistory.getEnddate()==null||
				salesTerritoryHistory.getStartdate()==null){
				
					throw new NullPointerException("Values empties or null");
		}
		Optional<Salesterritoryhistory> opTerritoryHistory =  this.salesTerritoryHistoryDao.findById(salesTerritoryHistory.getId());
		
		if(!opTerritoryHistory.isEmpty()) {
			if(salesTerritoryHistory.getEnddate().compareTo(Timestamp.valueOf(LocalDateTime.now()))>0) {
				throw new InvalidValueException("The end date have to be lower than the current date");
			}
			
			if(salesTerritoryHistory.getStartdate().compareTo(salesTerritoryHistory.getEnddate())>=0) {
				
				throw new InvalidValueException("The moddified data no should be equals or higgier to the end data");
			}
			Salesterritoryhistory oldTerritoryHistory = opTerritoryHistory.get();
			oldTerritoryHistory.setEnddate(salesTerritoryHistory.getEnddate());
			oldTerritoryHistory.setStartdate(salesTerritoryHistory.getStartdate());
			this.salesTerritoryHistoryDao.update(oldTerritoryHistory);
		}else {
			throw new ObjectDoesNotExistException("This id does not exist");
		}
	}

	@Override
	public Optional<Salesterritoryhistory> findById(Integer id) {
		
		return this.salesTerritoryHistoryDao.findById(id);
	}

	@Override
	public Iterable<Salesterritoryhistory> findAll() {
		return this.salesTerritoryHistoryDao.findAll();
	}

	@Override
	public void clear() {
		this.salesTerritoryHistoryDao.deleteAll();
	}

	@Override
	public void delete(Salesterritoryhistory salesterritoryhistory) {
		this.salesTerritoryHistoryDao.delete(salesterritoryhistory);
	}


}
