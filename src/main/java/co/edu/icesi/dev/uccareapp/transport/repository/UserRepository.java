package co.edu.icesi.dev.uccareapp.transport.repository;

import java.util.Optional;


import org.springframework.data.repository.CrudRepository;

import co.edu.icesi.dev.uccareapp.transport.model.UserApp;

public interface UserRepository extends CrudRepository<UserApp, Long>{
	public Optional<UserApp> findByUsername(String username);
}
