package com.budgetapp.filehandler;

import com.budgetapp.domain.BudgetRecord;
import com.budgetapp.exception.ColumnNameValidationException;
import com.budgetapp.repository.BudgetRecordRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.regeni.service.DataCleanerService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Component //Ezt csak kikoskodtam, hogy valószínűleg kelleni fog ide
public class XlsxReader {

//    DataCleanerService dataCleanerService = new DataCleanerService();
    //Hát ebben egyáltalán nem vagyok biztos, hogy ígxy működni fog, de mindjárt kiderül
    private final BudgetRecordRepository budgetDataRepository;

    public XlsxReader(BudgetRecordRepository budgetDataRepository) {
        this.budgetDataRepository = budgetDataRepository;
    }

//    public XlsxReader(BudgetRecordRepository budgetDataRepository) {
//        this.budgetDataRepository = budgetDataRepository;
//    }

    //Ez elvileg megmondja, hogy akkor fusson le a kód, miután a program elindult.
@EventListener(ApplicationReadyEvent.class)
    public void budgetDataCleaner() throws IOException {
        FileInputStream file;
        try {
            file = new FileInputStream("teszt_tranzakciok.xlsx");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);

        try {
            columnNameValidator(sheet, necessaryColumnNames());
        } catch (ColumnNameValidationException e) {
            throw new RuntimeException(e); // Itt lehet, hogy nem RuntimeExceptiont kellene majd dobni
        }

        Iterator<Row> rowIterator = sheet.iterator();

        //Ez azért kell, hogy az első sort kihagyjam, ami az oszlopok neve az xlsx-ben
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            BudgetRecord budgetRecord = new BudgetRecord();

            if (row.getCell(6).getStringCellValue().equals("Nem számolandó / Téves")) {
                continue;
            }

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                persistRecord(cell.getColumnIndex(), cell, budgetRecord);
            }
//            dataCleanerService.persistMonthlyBudget(budgetRecord);
            budgetDataRepository.save(budgetRecord);
        }
        wb.close();
        file.close();
    }

    //Ezt az egészet át kell alakítani úgy, hogy csak egy ellenőrzést csináljon arra vonatkozóan, hogy az előre megadot
    //oszlopszámokhoz tartozó szöveg egyezik-e. Pl. itt feltételezem most, hogy az 1. oszlop az minfig a "Tranzakció dátuma" lesz
    //még egy ideig, mert az OTP nem változtat semmit, ezért csak annyit ellenőrzök, hogy valóban egyezik-e. Ha nem akkor dobok
    //egy exceptiont. Utána a BudgetRecord összemappelésnél ennek megfelelően minmdig csak az adott sorban lévő megfelelő
    // oszlopszámmal mappelem össze a record-ot

    private void columnNameValidator(XSSFSheet sheet, String[] necessaryColumnNames) throws ColumnNameValidationException {
        //A metódus ellenőrzi, hogy a beadott xlsx fájlban megegyeznek az oszlopnevek a megadott listával, mindegyik szerepel-e
        //Ez azért szükséges, hogy ha az OTP változtatna esetleg az oszoplok megnevezésén, akkor se legyen gond
        //A lista tartalmazza az indexeit azoknak az oszlopoknak, amik szükségesek
        List<Integer> necessaryColumnIndexes = new ArrayList<>(Arrays.asList(0, 3, 4, 6, 8, 10));

        Row row = sheet.getRow(0);

        for (int i = 0; i < necessaryColumnIndexes.size(); i++) {
            Cell cell = row.getCell(necessaryColumnIndexes.get(i));
            if (!cell.getStringCellValue().equals(necessaryColumnNames[i])) {
                throw new ColumnNameValidationException();
            }

        }
    }

    private void persistRecord(int indexNumber, Cell cell, BudgetRecord budgetRecord) {
        switch (indexNumber) {
            case 0:
                String truncatedDate = cell.getStringCellValue().substring(0,10);
                budgetRecord.setTranzakcioDatuma(truncatedDate);
                break;
            case 3:
                budgetRecord.setBejovoKimeno(cell.getStringCellValue());
                break;
            case 4:
                budgetRecord.setPartnerNeve(cell.getStringCellValue());
                break;
            case 6:
                budgetRecord.setKoltesiKategoria(cell.getStringCellValue());
                break;
            case 8:
                budgetRecord.setSzamlaNev(cell.getStringCellValue());
                break;
            case 10:
                budgetRecord.setOsszeg(Math.abs((int) cell.getNumericCellValue()));
                break;
            default:
                break;
        }
    }

    private String[] necessaryColumnNames() {
        // A metódus visszaadja azoknak az oszlopoknak a neveit, amik kelleni fognak a db-be
        return new String[]{
                "Tranzakció dátuma",
                "Bejövő/Kimenő",
                "Partner neve",
                "Költési kategória",
                "Számla név",
                "Összeg"
        };
    }
}

