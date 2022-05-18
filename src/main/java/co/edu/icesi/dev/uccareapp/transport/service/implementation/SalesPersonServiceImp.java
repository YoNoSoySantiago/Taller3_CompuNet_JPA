package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectAlreadyExistException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.BusinessentityDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesTerritoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesPersonService;

@Service
public class SalesPersonServiceImp implements SalesPersonService {
	
	private SalesPersonDao salesPersonDao;
	private BusinessentityDao businessentityDao;
	private SalesTerritoryDao salesTerritoryDao;
	
	@Autowired
	public SalesPersonServiceImp(SalesPersonDao spr,
			BusinessentityDao br,
			SalesTerritoryDao str) {
		this.salesPersonDao = spr;
		this.businessentityDao = br;
		this.salesTerritoryDao = str;
	}

	@Override
	public void add(Salesperson salesPerson,Integer BusinessId,Integer territoryId) throws InvalidValueException, ObjectAlreadyExistException, ObjectDoesNotExistException {
		if(
		salesPerson.getSalesquota() == null ||
		salesPerson.getCommissionpct() == null ||
		BusinessId == null ||
		territoryId == null
		) {
			throw new NullPointerException("Empty values or nulls");
		}
		
		Optional<Salesperson> person = findById(BusinessId);
		if(person.isEmpty()) {
			if(salesPerson.getSalesquota().compareTo(BigDecimal.ZERO)<0) {
				throw new InvalidValueException("sales quota must be a positive number");
			}
			if(salesPerson.getCommissionpct().compareTo(BigDecimal.ZERO)<0) {
				throw new InvalidValueException("commission must to be between 0 and 1");
			}
			if(salesPerson.getCommissionpct().compareTo(BigDecimal.ONE)>0) {
				throw new InvalidValueException("commission must to be between 0 and 1");
			}
			
			Optional<Businessentity> entity = businessentityDao.findById(BusinessId);
			if(entity.isEmpty()) {
				throw new ObjectDoesNotExistException("This id of businessentity does not exist");
			}
			
			Optional<Salesterritory> territory = salesTerritoryDao.findById(territoryId);
			if(territory.isEmpty()) {
				throw new ObjectDoesNotExistException("This id of a Territory does not exist");
			}
			salesPerson.setBusinessentityid(BusinessId);
			salesPerson.setSalesterritory(territory.get());
			this.salesPersonDao.save(salesPerson);
			
		}else {
			throw new ObjectAlreadyExistException("this id already exist");
		}
	}

	@Override
	public void edit(Salesperson salesPerson) throws InvalidValueException, ObjectDoesNotExistException{
		if(
		salesPerson.getBusinessentityid()==null||
		salesPerson.getSalesterritory()==null||
		salesPerson.getSalesquota() == null ||
		salesPerson.getCommissionpct() == null) {
			throw new NullPointerException("Empty values or nulls");
		}
		Optional<Salesperson> person = findById(salesPerson.getBusinessentityid());
		if(!person.isEmpty()) {
			if(salesPerson.getSalesquota().compareTo(BigDecimal.ZERO)<0) {
				throw new InvalidValueException("sales quota must be a positive number");
			}
			if(salesPerson.getCommissionpct().compareTo(BigDecimal.ZERO)<0) {
				throw new InvalidValueException("commission must to be between 0 and 1");
			}
			if(salesPerson.getCommissionpct().compareTo(BigDecimal.ONE)>0) {
				throw new InvalidValueException("commission must to be between 0 and 1");
			}
			Salesperson oldPerson = person.get();
			oldPerson.setCommissionpct(salesPerson.getCommissionpct());
			oldPerson.setSalesquota(salesPerson.getSalesquota());
			
			this.salesPersonDao.update(oldPerson);
		}else {
			throw new ObjectDoesNotExistException("this id does not exist");
		}
	}
	
	@Override
	public void delete(Salesperson salesperson) {
		this.salesPersonDao.delete(salesperson);
	}

	@Override
	public Optional<Salesperson> findById(Integer id) {
		return this.salesPersonDao.findById(id);
	}
	
	@Override
	public Iterable<Salesperson> findAll() {
		return this.salesPersonDao.findAll();
	}

	@Override
	public void clear() {
		
		this.salesPersonDao.deleteAll();
	}

	
}
