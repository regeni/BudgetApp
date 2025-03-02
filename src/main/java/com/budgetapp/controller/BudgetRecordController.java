package com.budgetapp.controller;

import com.budgetapp.domain.BudgetRecord;
import com.budgetapp.repository.BudgetRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
public class BudgetRecordController {

    private final BudgetRecordRepository budgetDataRepository;

    public BudgetRecordController(BudgetRecordRepository budgetDataRepository) {
        this.budgetDataRepository = budgetDataRepository;
    }

    @GetMapping({"/budget-data", "/", "/list"})
    public ModelAndView showBudgetData() {
        ModelAndView modelAndView = new ModelAndView("budget-data");
        List<BudgetRecord> budgetRecords = budgetDataRepository.findAll();
        modelAndView.addObject("budgetRecords", budgetRecords);
        return modelAndView;
    }

    @GetMapping("/budget-data/edit/{id}")
    public String showEditBudgetData(@PathVariable Integer id, Model model) {
        BudgetRecord budgetRecord = budgetDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid budget record Id:" + id));
        model.addAttribute("budgetRecord", budgetRecord);
        return "edit-budget-data";
    }

    @PostMapping("/budget-data/update")
    public String updateBudgetRecord(BudgetRecord budgetRecord) {
        budgetDataRepository.save(budgetRecord);
        return "redirect:/budget-data";
    }

    @PostMapping("/budget-data/delete/{id}")
    public String deleteBudgetRecord(@PathVariable Integer id) {
        BudgetRecord budgetRecord = budgetDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid budget record Id:" + id));
        budgetDataRepository.delete(budgetRecord);
        return "redirect:/budget-data";
    }

}





