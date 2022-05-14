package co.edu.icesi.dev.uccareapp.transport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;

public interface BusinessentityRepository extends CrudRepository<Businessentity,Integer>{

}
