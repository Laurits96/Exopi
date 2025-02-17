package com.pdfdownloader;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    
    private static final int THREAD_POOL_SIZE = 5;    
    public static void main(String[] args) throws IOException{
        if ( args.length!= 2){
            System.out.println("Please provide precisly 2 arguemnts: path to excel file, and [y/n] if refence downloads shoudl be used");
            return;
        }

        AtomicInteger activeRowNumber = new AtomicInteger(1);
       
        // try {
        File excelInputFile = new File(args[0]);
        // } catch(IOException e){
        //     System.out.println(e);
        // }
        FileInputStream excelInputFileStream = new FileInputStream(excelInputFile);
        Workbook wb = new XSSFWorkbook(excelInputFileStream);
        Sheet sheet0 = wb.getSheetAt(0);
        excelInputFileStream.close();

        // Create a new folder to hold the downloaded PDFs
        Path parentDir = excelInputFile.toPath().getParent();
        Path downloadDir = parentDir.resolve("Downloaded_PDFs");
        if (!Files.exists(downloadDir)) {
            Files.createDirectory(downloadDir);
        }

        // Initiate reference memory
        HashSet<Integer> referenceMemory = new HashSet<>();
        Path refPath = Paths.get("src/main/resources/reference.txt");
        if(args[1] != "no"){
            List<String> referenceFile = Files.readAllLines(refPath);
            referenceFile.stream().forEach(id -> referenceMemory.add(Integer.parseInt(id)));
        }

        ExecutorService threadExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        List<Future<ArrayList<ArrayList<Integer>>>> futures = new ArrayList<>();

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            futures.add(threadExecutor.submit(new downloadTask(sheet0, activeRowNumber, referenceMemory, downloadDir)));
        }

        ArrayList<ArrayList<Integer>> allResults = new ArrayList<>();
        for (Future<ArrayList<ArrayList<Integer>>> future : futures) {
            try {
                allResults.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        writeResultsToExcel(parentDir.resolve("Download_Results.xlsx").toString(), allResults);
        List<String> referenceMemoryList = new ArrayList<>();
        for (ArrayList<Integer> result : allResults) {
            if (result.get(1) == 1) {
                referenceMemoryList.add(result.get(0).toString());
            }
        }
        Files.write(refPath, referenceMemoryList);
        threadExecutor.shutdown();
        wb.close();
    }

    private static void writeResultsToExcel(String filePath, ArrayList<ArrayList<Integer>>  results) throws IOException{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Download Results");

         // Create cell styles for green and red
        CellStyle greenCellStyle = workbook.createCellStyle();
        greenCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        greenCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle redCellStyle = workbook.createCellStyle();
        redCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Write results to the new sheet
        int rowIndex = 0;
        for (ArrayList<Integer> result : results) {
            Row row = sheet.createRow(rowIndex++);
            Cell idCell = row.createCell(0);
            idCell.setCellValue(result.get(0));
            Cell statusCell = row.createCell(1);
           // Set cell style based on status
            if (result.get(1) == 1) {
                statusCell.setCellValue("Succes");
                statusCell.setCellStyle(greenCellStyle);
            } else {
                statusCell.setCellStyle(redCellStyle);
                statusCell.setCellValue("Error");
            }
        }

        // Save the new workbook to a file
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        }
        workbook.close();
    }
}

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