package co.edu.icesi.dev.uccareapp.transport.controller.implementation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.icesi.dev.uccareapp.transport.customexeptions.InvalidValueException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectAlreadyExistException;
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritoryhistory;
import co.edu.icesi.dev.uccareapp.transport.repository.SalesPersonRepository;
import co.edu.icesi.dev.uccareapp.transport.repository.SalesTerritoryRepository;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryHistoryService;

@Controller
public class SalesTerritoryHistoryControllerImp {
	
	private SalesTerritoryHistoryService salesTerritoryHistoryService;
	private SalesPersonRepository salesPersonRepository;
	private SalesTerritoryRepository salesTerritoryRepository;
	
	public SalesTerritoryHistoryControllerImp(SalesTerritoryHistoryService sths, SalesPersonRepository spr, SalesTerritoryRepository str) {
		this.salesTerritoryHistoryService = sths;
		this.salesPersonRepository = spr;
		this.salesTerritoryRepository = str;
	}
	
	@GetMapping("/sales_territories_history")
	public String salesTerritories(Model model) {
		model.addAttribute("territories_history", salesTerritoryHistoryService.findAll());
		return "history/territory/index";
	}
	
	@GetMapping("/sales_territories_history/add")
	public String addSalesTerritory(Model model) {
		model.addAttribute("salesterritoryhistory", new Salesterritoryhistory());
		model.addAttribute("sales_persons", salesPersonRepository.findAll());
		model.addAttribute("sales_territories", salesTerritoryRepository.findAll());
		return "history/territory/add-sales-territory-history";
	}
	
	@PostMapping("/sales_territories_history/add")
	public String saveSalesTerritory(@Validated @ModelAttribute Salesterritoryhistory salesTerritoryHistory,
			BindingResult bindingResult,Model model, @RequestParam(value = "action", required = true) String action,
			@RequestParam(value="EndDate",required=true) String EndDate,
			@RequestParam(value="StartDate",required=true) String StartDate) {
		if (action != null && !action.equals("Cancel")) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime startdate = null;
			LocalDateTime enddate = null;
			try {
				StartDate = StartDate.replaceFirst("T"," ");
				
				startdate = LocalDateTime.parse(StartDate,dateTimeFormatter);
				salesTerritoryHistory.setStartdate(Timestamp.valueOf(startdate));
			}catch(Exception e) {
				bindingResult.addError(new ObjectError("StartDate", "value is no valid"));
			}
			try {
				EndDate = EndDate.replaceFirst("T"," ");
				
				enddate = LocalDateTime.parse(EndDate,dateTimeFormatter);
				salesTerritoryHistory.setEnddate(Timestamp.valueOf(enddate));
			}catch(Exception e) {
				bindingResult.addError(new ObjectError("enddate", "value is no valid"));
			}
			try {
				if(startdate!=null) {
					if(startdate.isAfter(LocalDateTime.now())) {
						bindingResult.addError(new ObjectError("startdate", "start date must to be lower than the current date"));
					}
				}
				
				if(enddate!=null) {
					if(enddate.isBefore(startdate)) {
						bindingResult.addError(new ObjectError("enddate", "end date must to be higger than the start date"));
					}
				}
				
				if(bindingResult.hasErrors()) {
					model.addAttribute("salesterritoryhistory", salesTerritoryHistory);
					model.addAttribute("sales_persons", salesPersonRepository.findAll());
					model.addAttribute("sales_territories", salesTerritoryRepository.findAll());
					return "history/territory/add-sales-territory-history";
				}
				
				salesTerritoryHistory.setModifieddate(Timestamp.valueOf(LocalDateTime.now()));
				salesTerritoryHistoryService.add(salesTerritoryHistory,
						salesTerritoryHistory.getSalesPerson().getBusinessentityid(),
						salesTerritoryHistory.getSalesTerritory().getTerritoryid());
			} catch (InvalidValueException | ObjectDoesNotExistException | ObjectAlreadyExistException e) {
				e.printStackTrace();
			}
		}
		return "redirect:/sales_territories_history";
	}
	
	@GetMapping("/sales_territories_history/edit/{id}")
	public String editSalesTerritory(@PathVariable("id") int id,Model model) {
		Optional<Salesterritoryhistory> territory_history = salesTerritoryHistoryService.findById(id);
		if(!territory_history.isEmpty()) {
			model.addAttribute("salesterritoryhistory", territory_history.get());
			model.addAttribute("sales_persons", salesPersonRepository.findAll());
			model.addAttribute("sales_territories", salesTerritoryRepository.findAll());
			return "history/territory/update-sales-territory-history";
		}
		return "redirect:/sales_territories_history";
	}
	
	@PostMapping("/sales_territories_history/edit/{id}")
	public String updateUser(@PathVariable("id") int id,@ModelAttribute Salesterritoryhistory salesterritoryhistory, 
			BindingResult bindingResult, Model model, @RequestParam(value = "action", required = true) String action,
			@RequestParam(value="EndDate",required=true) String EndDate,
			@RequestParam(value="StartDate",required=true) String StartDate) {
		if (action != null && !action.equals("Cancel")) {
			EndDate = EndDate.replaceFirst("T"," ");
			StartDate = StartDate.replaceFirst("T"," ");
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			
			LocalDateTime enddate = LocalDateTime.parse(EndDate,dateTimeFormatter);
			salesterritoryhistory.setEnddate(Timestamp.valueOf(enddate));
			
			LocalDateTime startdate = LocalDateTime.parse(StartDate,dateTimeFormatter);
			salesterritoryhistory.setStartdate(Timestamp.valueOf(startdate));
			if(bindingResult.hasErrors()) {
				model.addAttribute("salesterritoryhistory", salesterritoryhistory);
				model.addAttribute("sales_persons", salesPersonRepository.findAll());
				model.addAttribute("sales_territories", salesTerritoryRepository.findAll());
				return "history/territory/update-sales-territory-history";
			}
			try {
				salesterritoryhistory.setId(id);
				salesTerritoryHistoryService.edit(salesterritoryhistory);
			} catch (InvalidValueException | ObjectDoesNotExistException e) {
				e.printStackTrace();
			}
			model.addAttribute("territories_history", salesTerritoryHistoryService.findAll());
		}
		return "redirect:/sales_territories_history";
	}
	
	@GetMapping("/sales_territories_history/del/{id}")
	public String deleteUser(@PathVariable("id") int id, Model model) {
		Optional<Salesterritoryhistory> territory_history = salesTerritoryHistoryService.findById(id);
		if(!territory_history.isEmpty()) {
			salesTerritoryHistoryService.delete(territory_history.get());
			model.addAttribute("territories_history", salesTerritoryHistoryService.findAll());
		}
		return "redirect:/sales_territories_history";
	}
	
	@GetMapping("/sales_territories_history/sales_person_info/{id}")
	public String showSalesPersonInfo(@PathVariable("id") int id, Model model) {
		model.addAttribute("back", "/sales_territories_history");
		model.addAttribute("salesperson", salesPersonRepository.findById(id).get());
		return "sales/person/info-sales-person";
	}
	
	@GetMapping("/sales_territories_history/sales_territory_info/{id}")
	public String showSalesTerritoryInfo(@PathVariable("id") int id, Model model) {
		model.addAttribute("back", "/sales_territories_history");
		model.addAttribute("salesterritory", salesTerritoryRepository.findById(id).get());
		return "sales/territory/info-sales-territory";
	}
}
