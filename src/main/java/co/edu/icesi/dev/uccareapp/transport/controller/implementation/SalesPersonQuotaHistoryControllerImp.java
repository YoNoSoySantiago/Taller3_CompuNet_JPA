package co.edu.icesi.dev.uccareapp.transport.controller.implementation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salespersonquotahistory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;
import co.edu.icesi.dev.uccareapp.transport.repository.SalesPersonRepository;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesPersonQuotaHistoryService;

@Controller
public class SalesPersonQuotaHistoryControllerImp {
	
	SalesPersonQuotaHistoryService salesPersonQuotaHistoryService;
	SalesPersonRepository salesPersonRepository;
	
	public SalesPersonQuotaHistoryControllerImp(SalesPersonQuotaHistoryService spqhs, SalesPersonRepository spr) {
		this.salesPersonQuotaHistoryService = spqhs;
		this.salesPersonRepository = spr;
	}
	
	@GetMapping("/sales_persons_history")
	public String salesTerritories(Model model) {
		model.addAttribute("persons_history", salesPersonQuotaHistoryService.findAll());
		return "history/person/index";
	}
	
	@GetMapping("/sales_persons_history/add")
	public String addSalesTerritory(Model model) {
		model.addAttribute("salespersonquotahistory", new Salespersonquotahistory());
		model.addAttribute("sales_persons", salesPersonRepository.findAll());
		return "history/person/add-sales-person-quota-history";
	}
	
	@PostMapping("/sales_persons_history/add")
	public String saveSalesTerritory(@Validated @ModelAttribute Salespersonquotahistory salespersonquotahistory,
			BindingResult bindingResult,Model model, @RequestParam(value = "action", required = true) String action) {
			if (action != null && !action.equals("Cancel")) {
				try {
					if(bindingResult.hasErrors()) {
						model.addAttribute("salespersonquotahistory", salespersonquotahistory);
						model.addAttribute("sales_persons", salesPersonRepository.findAll());
						return "history/person/add-sales-person-quota-history";
					}
					
					salespersonquotahistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now()));
					salesPersonQuotaHistoryService.add(salespersonquotahistory, salespersonquotahistory.getSalesperson().getBusinessentityid());
				} catch (InvalidValueException | ObjectDoesNotExistException | ObjectAlreadyExistException e) {
					e.printStackTrace();
				}
			}
		return "redirect:/sales_persons_history";
	}
	
	@GetMapping("/sales_persons_history/edit/{id}")
	public String editSalesTerritory(@PathVariable("id") int id,Model model) {
		Optional<Salespersonquotahistory> person_history = salesPersonQuotaHistoryService.findById(id);
		if(!person_history.isEmpty()) {
			model.addAttribute("salespersonquotahistory", person_history.get());
			model.addAttribute("sales_persons", salesPersonRepository.findAll());
			return "history/person/update-sales-person-quota-history";
		}
		return "redirect:/sales_persons_history";
	}
	
	@PostMapping("/sales_persons_history/edit/{id}")
	public String updateSalesPersonHistory(@PathVariable("id") int id,@Validated @ModelAttribute Salespersonquotahistory salespersonhistory, 
			BindingResult bindingResult, Model model, @RequestParam(value = "action", required = true) String action) {
		
		if (action != null && !action.equals("Cancel")) {
			salespersonhistory.setId(id);
			if(bindingResult.hasErrors()) {
				model.addAttribute("salespersonquotahistory", salespersonhistory);
				model.addAttribute("sales_persons", salesPersonRepository.findAll());
				return "history/person/update-sales-person-quota-history";
			}
			try {
				salespersonhistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now()));
				salesPersonQuotaHistoryService.edit(salespersonhistory);
			} catch (InvalidValueException | ObjectDoesNotExistException e) {
				e.printStackTrace();
			}
			model.addAttribute("persons_history", salesPersonQuotaHistoryService.findAll());
		}
		return "redirect:/sales_persons_history";
	}
	
	@GetMapping("/sales_persons_history/del/{id}")
	public String deleteUser(@PathVariable("id") int id, Model model) {
		Optional<Salespersonquotahistory> person_history = salesPersonQuotaHistoryService.findById(id);
		if(!person_history.isEmpty()) {
			salesPersonQuotaHistoryService.delete(person_history.get());
			model.addAttribute("persons_history", salesPersonQuotaHistoryService.findAll());
		}
		return "redirect:/sales_persons_history";
	}
	
	@GetMapping("/sales_persons_history/sales_person_info/{id}")
	public String showSalesPersonInfo(@PathVariable("id") int id, Model model) {
		model.addAttribute("back", "/sales_persons_history");
		model.addAttribute("salesperson", salesPersonRepository.findById(id).get());
		return "sales/person/info-sales-person";
	}
}
