package com.budgetapp.controller;

import com.budgetapp.domain.BudgetRecord;
import com.budgetapp.repository.BudgetRecordRepository;
import lombok.extern.slf4j.Slf4j;

//import org.regeni.service.BudgetDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//TODO  Videóban csak sima Controller van RestController helyett, ennek majd utánanézni
@Controller
//@RequestMapping("/") Ez nem biztos, hogy kelleni fog
@Slf4j
public class BudgetRecordController {

    //TODO Itt valószínűleg ehelyett a Service fog majd kelleni, csak előbb videó alapján most megcsinálom így
    //TODO Ide lehet, hogy kell az @Autowired

    private BudgetRecordRepository budgetDataRepository;

    @GetMapping("/showBudget")
    public ModelAndView showBudgetData() {
        ModelAndView modelAndView = new ModelAndView("budget-data");
        List<BudgetRecord> budgetRecords = budgetDataRepository.findAll();
        modelAndView.addObject("budgetRecords", budgetRecords);
        return modelAndView;
    }

}
