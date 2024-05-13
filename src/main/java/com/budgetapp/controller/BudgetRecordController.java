package com.budgetapp.controller;

import com.budgetapp.domain.BudgetRecord;
import com.budgetapp.repository.BudgetRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
//@RequestMapping("/") Ez nem biztos, hogy kelleni fog
@Slf4j
public class BudgetRecordController {

    //Ide lehet, hogy kell az @Autowired
    private final BudgetRecordRepository budgetDataRepository;

    public BudgetRecordController(BudgetRecordRepository budgetDataRepository) {
        this.budgetDataRepository = budgetDataRepository;
    }

    @GetMapping({"/budget-data", "/", "/list"})
//    @GetMapping("/")
    public ModelAndView showBudgetData() {
        ModelAndView modelAndView = new ModelAndView("budget-data");
        List<BudgetRecord> budgetRecords = budgetDataRepository.findAll();
        modelAndView.addObject("budgetRecords", budgetRecords);
        return modelAndView;
    }

}





