package co.edu.icesi.dev.uccareapp.transport.controller.implementation;

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
import co.edu.icesi.dev.uccareapp.transport.customexeptions.ObjectDoesNotExistException;
import co.edu.icesi.dev.uccareapp.transport.model.sales.Salesterritory;
import co.edu.icesi.dev.uccareapp.transport.repository.CountryRegionRepository;
import co.edu.icesi.dev.uccareapp.transport.service.interfaces.SalesTerritoryService;

@Controller
public class SalesTerritoryControllerImp {
	
	private SalesTerritoryService salesTerritoryService;
	private CountryRegionRepository countryRegionRepository;
	
	public SalesTerritoryControllerImp(SalesTerritoryService sts,CountryRegionRepository crr) {
		this.salesTerritoryService = sts;
		this.countryRegionRepository = crr;
	}
	
	@GetMapping("/sales_territories")
	public String salesTerritories(Model model) {
		model.addAttribute("territories", salesTerritoryService.findAll());
		return "sales/territory/index";
	}
	
	@GetMapping("/sales_territories/add")
	public String addSalesTerritory(Model model) {
		model.addAttribute("salesterritory", new Salesterritory());
		model.addAttribute("countries_region", countryRegionRepository.findAll());
		return "sales/territory/add-sales-territory";
	}
	
	@PostMapping("/sales_territories/add")
	public String saveSalesTerritory(@Validated @ModelAttribute Salesterritory salesTerritory,
			BindingResult bindingResult,Model model, @RequestParam(value = "action", required = true) String action) {
		if (!action.equals("Cancel")) {
			try {
				if(bindingResult.hasErrors()) {
					model.addAttribute("salesterritory", salesTerritory);
					model.addAttribute("countries_region", countryRegionRepository.findAll());
					System.out.println(bindingResult.getFieldError().getObjectName()+" ERROR");
					return "sales/territory/add-sales-territory";
				}
				salesTerritoryService.add(salesTerritory);
			} catch (InvalidValueException | ObjectDoesNotExistException e) {
				e.printStackTrace();
			}
		}
		return "redirect:/sales_territories";
	}
	
	@GetMapping("/sales_territories/edit/{id}")
	public String editSalesTerritory(@PathVariable("id") int id,Model model) {
		Optional<Salesterritory> territory = salesTerritoryService.findById(id);
		if(!territory.isEmpty()) {
			model.addAttribute("salesterritory", territory.get());
			model.addAttribute("countries_region", countryRegionRepository.findAll());
			return "sales/territory/update-sales-territory";
		}
		return "redirect:/sales_territories";
	}
	
	@PostMapping("/sales_territories/edit/{id}")
	public String updateUser(@PathVariable("id") int id,
			@RequestParam(value = "action", required = true) String action,@Validated @ModelAttribute Salesterritory salesterritory, BindingResult bindingResult, Model model) {
		if (action != null && !action.equals("Cancel")) {
			salesterritory.setTerritoryid(id);
			if(bindingResult.hasErrors()) {
				model.addAttribute("salesterritory", salesterritory);
				model.addAttribute("countries_region", countryRegionRepository.findAll());
				return "sales/territory/update-sales-territory";
			}
			try {
				salesTerritoryService.edit(salesterritory);
			} catch (InvalidValueException | ObjectDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addAttribute("territories", salesTerritoryService.findAll());
		}
		return "redirect:/sales_territories";
	}
	
	@GetMapping("/sales_territories/del/{id}")
	public String deleteUser(@PathVariable("id") int id, Model model) {
		Optional<Salesterritory> territory = salesTerritoryService.findById(id);
		if(!territory.isEmpty()) {

			salesTerritoryService.delete(territory.get());
			model.addAttribute("territories", salesTerritoryService.findAll());
		}
		return "redirect:/sales_territories";
	}
	
	@GetMapping("/sales_territories/sales_person_list/{id}")
	public String showSalesPersonList(@PathVariable("id") int id,Model model) {
		Salesterritory st = salesTerritoryService.findById(id).get();
		if(st!=null) {
			model.addAttribute("sales_persons", st.getSalespersons());
		}
		model.addAttribute("back", "/sales_territories");
		return "/sales/person/list-sales-person";
	}
	
	@GetMapping("/sales_territories/sales_territory_history_list/{id}")
	public String showSalesTerritoryHistoryList(@PathVariable("id") int id,Model model) {
		Salesterritory st = salesTerritoryService.findById(id).get();
		if(st!=null) {
			model.addAttribute("territories_history", st.getSalesterritoryhistories());
		}
		model.addAttribute("back", "/sales_territories");
		return "/history/territory/list-sales-territory-history";
	}
}
