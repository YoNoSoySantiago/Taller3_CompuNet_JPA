package co.edu.icesi.dev.uccareapp.transport.controller.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectAlreadyExistException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.model.person.Businessentity;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesperson;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.repository.BusinessentityRepository;
import co.edu.icesi.dev.uccareapp.transport.repository.SalesTerritoryRepository;
import co.edu.icesi.dev.uccareapp.transport.service.implementation.SalesPersonServiceImp;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesPersonService;

@Controller
public class SalesPersonControllerImp {
	
	private SalesPersonService salesPersonService;
	private BusinessentityRepository businessentityRepository;
	private SalesTerritoryRepository salesTerritoryRepository;
	
	@Autowired
	public SalesPersonControllerImp(SalesPersonServiceImp sps,BusinessentityRepository br,SalesTerritoryRepository str) {
		this.salesPersonService = sps;
		this.businessentityRepository = br;
		this.salesTerritoryRepository = str;
	}
	
	@GetMapping("/login")
	public String login() {
		return "/login";
	}
	
	@GetMapping("/access-denied")
	public String accessDenied(){
		return "access-denied";
	}
	
	@GetMapping("/sales_persons")
	public String salesPersons(Model model) {
		model.addAttribute("sales_persons", salesPersonService.findAll());
		return "sales/person/index";
	}
	
	@GetMapping("/sales_persons/add")
	public String addSalesPerson(Model model) {
		
		Iterable<Businessentity> entities =	businessentityRepository.findAll();
		List<Integer> bussinessentities_ids =  new ArrayList<Integer>();
		for(Businessentity entity : entities) {
			bussinessentities_ids.add(entity.getBusinessentityid());
		}
		Iterable<Salesterritory> territories = salesTerritoryRepository.findAll();
		
		model.addAttribute("businessentities_ids", bussinessentities_ids.iterator());
		model.addAttribute("territories", territories.iterator());
		model.addAttribute("salesperson", new Salesperson());
		return "sales/person/add-sales-person";
	}
	
	@PostMapping("/sales_persons/add")
	public String saveSalesPerson(@Validated @ModelAttribute Salesperson salesperson,BindingResult bindingResult,Model model,@RequestParam(value = "action", required = true) String action) {
		if (action != null && !action.equals("Cancel")) {
			try {
				if(salesperson.getBusinessentityid()==-1) {
					Businessentity be = new Businessentity();
					businessentityRepository.save(be);
					Businessentity last = null;
					for(Businessentity aux : businessentityRepository.findAll()) {
						if(last==null) {
							last = aux;
						}else if(aux.getBusinessentityid()>last.getBusinessentityid()) {
							last = aux;
						}
					}
					
					salesperson.setBusinessentityid(last.getBusinessentityid());
				}
				if(bindingResult.hasErrors()) {
					Iterable<Businessentity> entities =	businessentityRepository.findAll();
					List<Integer> bussinessentities_ids =  new ArrayList<Integer>();
					for(Businessentity entity : entities) {
						bussinessentities_ids.add(entity.getBusinessentityid());
					}
					Iterable<Salesterritory> territories = salesTerritoryRepository.findAll();
					model.addAttribute("businessentities_ids", bussinessentities_ids.iterator());
					model.addAttribute("territories", territories.iterator());
					model.addAttribute("salesperson", salesperson);
					return "sales/person/add-sales-person";
				}
				salesPersonService.add(salesperson,  salesperson.getBusinessentityid(),salesperson.getSalesterritory().getTerritoryid());
				
			} catch (InvalidValueException | ObjectAlreadyExistException | ObjectDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "redirect:/sales_persons";
	}
	
	@GetMapping("/sales_persons/edit/{id}")
	public String editSalesPerson(@PathVariable("id") int id,Model model) {
		Optional<Salesperson> salesPerson = salesPersonService.findById(id);
		if(!salesPerson.isEmpty()) {
			
			Iterable<Salesterritory> territories = salesTerritoryRepository.findAll();
			
			model.addAttribute("territories", territories.iterator());
			model.addAttribute("salesperson", salesPerson.get());
			return "sales/person/update-sales-person";
		}
		return "redirect:/sales_persons";
	}
	
	@PostMapping("/sales_persons/edit/{id}")
	public String updateSalesPerson(@PathVariable("id") int id,
			@RequestParam(value = "action", required = true) String action,@Validated @ModelAttribute Salesperson salesperson, BindingResult bindingResult, Model model) {
		if (action != null && !action.equals("Cancel")) {
			salesperson.setBusinessentityid(id);
			if(bindingResult.hasErrors()) {
				Iterable<Salesterritory> territories = salesTerritoryRepository.findAll();
				
				model.addAttribute("territories", territories.iterator());
				model.addAttribute("salesperson", salesperson);
				return "sales/person/update-sales-person";
			}
			try {
				salesperson.setBusinessentityid(id);
				salesPersonService.edit(salesperson);
			} catch (InvalidValueException | ObjectDoesNotExistException e) {
				e.printStackTrace();
			}
			model.addAttribute("sales_persons", salesPersonService.findAll());
		}
		return "redirect:/sales_persons";
	}
	
	@GetMapping("/sales_persons/del/{id}")
	public String deleteSalesPerson(@PathVariable("id") int id, Model model) {
		Optional<Salesperson> salesPerson = salesPersonService.findById(id);
		if(!salesPerson.isEmpty()) {
			
			salesPersonService.delete(salesPerson.get());
			model.addAttribute("sales_persons", salesPersonService.findAll());
		}
		return "redirect:/sales_persons";
	}
	
	@GetMapping("/sales_persons/sales_territory_info/{id}")
	public String showTerritoryInfo(@PathVariable("id") int id, Model model){
		model.addAttribute("salesterritory", salesTerritoryRepository.findById(id).get());
		model.addAttribute("back", "/sales_persons");
		return "sales/territory/info-sales-territory";
	}
	
	@GetMapping("/sales_persons/sales_person_quota_history_list/{id}")
	public String showSalesPersonHistory(@PathVariable("id") int id, Model model) {
		model.addAttribute("back", "/sales_persons");
		Salesperson sp = salesPersonService.findById(id).get();
		model.addAttribute("persons_history", sp.getSalespersonquotahistories());
		return "history/person/list-sales-person-quota-history";
	}
	
	@GetMapping("/sales_persons/sales_territory_history_list/{id}")
	public String showSalesTerritoryHistory(@PathVariable("id") int id, Model model) {
		model.addAttribute("back", "/sales_persons");
		Salesperson sp = salesPersonService.findById(id).get();
		model.addAttribute("territories_history", sp.getSalesterritoryhistories());
		return "history/territory/list-sales-territory-history";
	}
}
