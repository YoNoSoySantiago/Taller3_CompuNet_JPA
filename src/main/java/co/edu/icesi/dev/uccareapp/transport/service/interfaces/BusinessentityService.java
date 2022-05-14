package co.edu.icesi.dev.uccareapp.transport.service.interfaces;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;

public interface BusinessentityService  {
	public void add(Businessentity businessEntiry);
	public Optional<Businessentity> findById(Integer id);
	public Iterable<Businessentity> findAll();
	public void clear();
}
