package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.dao.interfaces.BusinessentityDao;
import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.BusinessentityService;

@Service
public class BusinessentityServiceImp implements BusinessentityService {
	
	private BusinessentityDao businessEntityDao;
	
	@Autowired
	public BusinessentityServiceImp(BusinessentityDao  ber) {
		businessEntityDao = ber;
	}
	@Override
	public void add(Businessentity businessEntity) {
		this.businessEntityDao.add(businessEntity);
	}

	@Override
	public Optional<Businessentity> findById(Integer id) {
		return this.businessEntityDao.findById(id);
	}
	@Override
	public Iterable<Businessentity> findAll() {
		return this.businessEntityDao.findAll();
	}
	@Override
	public void clear() {
		this.businessEntityDao.deleteAll();
	}

}
