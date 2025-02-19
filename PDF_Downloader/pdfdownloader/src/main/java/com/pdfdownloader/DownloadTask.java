package com.pdfdownloader;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


import org.apache.poi.ss.usermodel.*;

class downloadTask implements Callable<ArrayList<ArrayList<Integer>>> {
    private final Sheet sheet;
    private final AtomicInteger activeRowNumber;
    private final HashSet<Integer> refMem;
    private final Path downloadDir;

    public downloadTask(Sheet sheet, AtomicInteger activeRowNumber, HashSet<Integer> refMem, Path downloadDir) {
        this.sheet = sheet;
        this.activeRowNumber = activeRowNumber;
        this.refMem = refMem;
        this.downloadDir = downloadDir;
    }

    @Override
    public ArrayList<ArrayList<Integer>> call() throws Exception {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        int rowNum;
        while ((rowNum = activeRowNumber.getAndIncrement()) < sheet.getPhysicalNumberOfRows()) {
            Row row = sheet.getRow(rowNum);
            if (row == null) continue;
            int id = (int) row.getCell(0).getNumericCellValue();
            if (refMem.contains(id)) {
                System.out.println(id + " Already downloaded");
                continue;
            }
            String[] urls = new String[2];
            urls[0] = row.getCell(1).getStringCellValue();
            urls[1] = row.getCell(2).getStringCellValue();

            ArrayList<Integer> downloadResult = PDFDownloader.downloadPDF(urls, id, downloadDir.toString());
            results.add(downloadResult);
        }
        return results;
    }
}