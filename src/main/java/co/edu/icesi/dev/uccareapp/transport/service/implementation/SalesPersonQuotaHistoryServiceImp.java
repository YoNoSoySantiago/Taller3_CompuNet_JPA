package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectAlreadyExistException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonDao;
import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.SalesPersonQuotaHistoryDao;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salespersonquotahistory;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesPersonQuotaHistoryService;
@Service
public class SalesPersonQuotaHistoryServiceImp implements SalesPersonQuotaHistoryService {
	
	private SalesPersonQuotaHistoryDao salesPersonQuotaHistoryDao;
	private SalesPersonDao salesPersonDao;
	
	@Autowired
	public SalesPersonQuotaHistoryServiceImp(SalesPersonQuotaHistoryDao spqhr, SalesPersonDao spr) {
		this.salesPersonDao = spr;
		this.salesPersonQuotaHistoryDao = spqhr;
	}

	@Override
	public void add(Salespersonquotahistory salesPersonQuotaHistory, Integer idSalesPerson) throws InvalidValueException, ObjectAlreadyExistException, ObjectDoesNotExistException {
		if(
			salesPersonQuotaHistory.getModifieddate()==null||
			salesPersonQuotaHistory.getSalesquota() == null) {
				
				throw new NullPointerException("Values empties or null");
		}
		if(salesPersonQuotaHistory.getModifieddate().compareTo(Timestamp.valueOf(LocalDateTime.now()))>0) {
			throw new InvalidValueException("The quota date must to be lower than the current date");
		}
		if(salesPersonQuotaHistory.getSalesquota().compareTo(BigDecimal.ZERO)<0) {
			throw new InvalidValueException("The sales quota must to be a positive number");
		}
		
		Optional<Salesperson> OpSalesPerson = salesPersonDao.findById(idSalesPerson);
		if(OpSalesPerson.isEmpty()) {
			throw new ObjectDoesNotExistException("This id of businessentity does not exist");
		}
		salesPersonQuotaHistory.setSalesperson(OpSalesPerson.get());
		this.salesPersonQuotaHistoryDao.save(salesPersonQuotaHistory);
	}

	@Override
	public void edit(Salespersonquotahistory salesPersonQuotaHistory) throws InvalidValueException, ObjectDoesNotExistException {
		if(
				salesPersonQuotaHistory.getId()==null||
				salesPersonQuotaHistory.getModifieddate()==null||
				salesPersonQuotaHistory.getSalesquota() == null) {
					
					throw new NullPointerException("Values empties or null");
			}
		Optional<Salespersonquotahistory> quotaHistory = findById(salesPersonQuotaHistory.getId());
		if(!quotaHistory.isEmpty()) {
			if(salesPersonQuotaHistory.getModifieddate().compareTo(Timestamp.valueOf(LocalDateTime.now()))>0) {
				throw new InvalidValueException("The quota date must to be lower than the current date");
			}
			if(salesPersonQuotaHistory.getSalesquota().compareTo(BigDecimal.ZERO)<0) {
				throw new InvalidValueException("The sales quota must to be a positive number");
			}
			
			Salespersonquotahistory oldHistory = quotaHistory.get();
			oldHistory.setModifieddate(salesPersonQuotaHistory.getModifieddate());
			oldHistory.setSalesquota(salesPersonQuotaHistory.getSalesquota());
			this.salesPersonQuotaHistoryDao.update(oldHistory);
		}else {
			throw new ObjectDoesNotExistException("This id does not exist");
		}
	}

	@Override
	public Optional<Salespersonquotahistory> findById(Integer id) {
		return this.salesPersonQuotaHistoryDao.findById(id);
	}

	@Override
	public Iterable<Salespersonquotahistory> findAll() {
		return this.salesPersonQuotaHistoryDao.findAll();
	}

	@Override
	public void clear() {
		this.salesPersonQuotaHistoryDao.deleteAll();
	}

	@Override
	public void delete(Salespersonquotahistory salespersonquotahistory) {
		this.salesPersonQuotaHistoryDao.delete(salespersonquotahistory);
	}

}
