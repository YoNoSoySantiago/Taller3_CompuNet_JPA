package co.edu.icesi.dev.uccareapp.transport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;

public interface CountryRegionRepository extends CrudRepository<Countryregion,String> {

}
