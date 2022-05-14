package co.edu.icesi.dev.uccareapp.transport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;
import co.edu.icesi.dev.uccareapp.transport.model.person.Countryregion;
import co.edu.icesi.dev.uccareapp.transport.model.UserApp;
import co.edu.icesi.dev.uccareapp.transport.model.UserType;
import co.edu.icesi.dev.uccareapp.transport.repository.BusinessentityRepository;
import co.edu.icesi.dev.uccareapp.transport.repository.CountryRegionRepository;
import co.edu.icesi.dev.uccareapp.transport.repository.UserRepository;

@SpringBootApplication
public class Taller2ShApplication {

	public static void main(String[] args) {
		SpringApplication.run(Taller2ShApplication.class, args);
	}
	
	@Bean
	public Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}

	@Bean
	public CommandLineRunner clr(UserRepository userRepository,CountryRegionRepository countryRegionRepository,BusinessentityRepository businessentityRepository) {
		return (args->{
			UserApp userAdmin = new UserApp();
			userAdmin.setUsername("YoNoSoySantiago");
			userAdmin.setPassword("{noop}123456789");
			userAdmin.setType(UserType.admin);
			
			UserApp userOp = new UserApp();
			userOp.setUsername("Cpasuy06");
			userOp.setPassword("{noop}987654321");
			userOp.setType(UserType.operator);
			
			userRepository.save(userAdmin);
			userRepository.save(userOp);
			
			File file = new File("data"+File.separator+"countryRegion.txt");
			System.out.println("=======================================================================");
			System.out.println(file.getAbsolutePath());
			System.out.println("=======================================================================");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = br.readLine();
			
			for(int i = 0;i<5;i++) {
				businessentityRepository.save(new Businessentity());
			}
			
			while(str!=null) {
				Countryregion cr = new Countryregion();
				cr.setName(str);
				countryRegionRepository.save(cr);
				str = br.readLine(); 
			}
			br.close();
		});
	}
}
