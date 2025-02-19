package com.pdfdownloader;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    
    private static final int THREAD_POOL_SIZE = 5;    
    public static void main(String[] args) throws IOException{
        if ( args.length!= 2){
            System.out.println("Please provide precisly 2 arguments: path to excel file, and [yes/no] if refence downloads shoudl be used");
            return;
        }

        if(!Files.exists(Paths.get(args[0]))){
            System.out.println("The file doesn't exists at specified location");
            return;
        }

        if (!args[1].equals("yes") && !args[1].equals("no")){
            System.out.println("Second argument invalid: must clearly state 'yes' or 'no'");
            return;
        }

        AtomicInteger activeRowNumber = new AtomicInteger(1);

        File excelInputFile = new File(args[0]);
        FileInputStream excelInputFileStream = new FileInputStream(excelInputFile);
        Workbook wb = new XSSFWorkbook(excelInputFileStream);
        Sheet inputSheet = wb.getSheetAt(0);
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
            futures.add(threadExecutor.submit(new downloadTask(inputSheet, activeRowNumber, referenceMemory, downloadDir)));
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