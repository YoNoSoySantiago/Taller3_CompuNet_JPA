package co.edu.icesi.dev.uccareapp.transport.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;
import co.edu.icesi.dev.uccareapp.transport.repository.BusinessentityRepository;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.BusinessentityService;

@Service
public class BusinessentityServiceImp implements BusinessentityService {
	
	private BusinessentityRepository businessEntityRepository;
	
	@Autowired
	public BusinessentityServiceImp(BusinessentityRepository  ber) {
		businessEntityRepository = ber;
	}
	@Override
	public void add(Businessentity businessEntiry) {
		this.businessEntityRepository.save(businessEntiry);
	}

	@Override
	public Optional<Businessentity> findById(Integer id) {
		return this.businessEntityRepository.findById(id);
	}
	@Override
	public Iterable<Businessentity> findAll() {
		return this.businessEntityRepository.findAll();
	}
	@Override
	public void clear() {
		this.businessEntityRepository.deleteAll();
	}

}
