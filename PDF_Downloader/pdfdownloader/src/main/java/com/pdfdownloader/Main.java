package com.pdfdownloader;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
        public static void main(String[] args) throws IOException {
        if (isArgsInvalid(args)) {
            return;
        }

        Path excelFilePath = Paths.get(args[0]);
        String useReference = args[1];

        HashSet<Integer> referenceMemory = loadReferenceMemory(useReference);

        Workbook inputWorkbook = loadWorkbook(excelFilePath);
        Sheet inputSheet = inputWorkbook.getSheetAt(0);
        ArrayList<DownloadRequest> extractedExcelRows = extractExcelRows(inputSheet, referenceMemory);
        inputWorkbook.close();

        Path downloadDir = createDownloadDirectory(excelFilePath);

        List<Future<DownloadResult>> futures = startDownloadTasks(extractedExcelRows, referenceMemory, downloadDir);

        ArrayList<DownloadResult> allResults = collectResults(futures);

        writeResultsToExcel(downloadDir.resolve("Download_Results.xlsx").toString(), allResults);

        updateReferenceMemory(referenceMemory, allResults);
    }

    private static boolean isArgsInvalid(String[] args) {
        if (args.length != 2) {
            System.out.println("Please provide precisely 2 arguments: path to excel file, and [yes/no] if reference downloads should be used");
            return true;
        }

        if (!Files.exists(Paths.get(args[0]))) {
            System.out.println("The file doesn't exist at the specified location");
            return true;
        }

        if (!args[1].equals("yes") && !args[1].equals("no")) {
            System.out.println("Second argument invalid: must clearly state 'yes' or 'no'");
            return true;
        }

        return false;
    }

    private static HashSet<Integer> loadReferenceMemory(String useReference) throws IOException {
        HashSet<Integer> referenceMemory = new HashSet<>();
        if (useReference.equals("yes")) {
            Path refPath = Paths.get("src/main/resources/reference.txt");
            List<String> referenceFile = Files.readAllLines(refPath);
            referenceFile.forEach(id -> referenceMemory.add(Integer.parseInt(id)));
        }
        return referenceMemory;
    }

    private static Workbook loadWorkbook(Path excelFilePath) throws IOException {
        try (FileInputStream excelInputFileStream = new FileInputStream(excelFilePath.toFile())) {
            return new XSSFWorkbook(excelInputFileStream);
        }
    }

    private static ArrayList<DownloadRequest> extractExcelRows(Sheet sheet, HashSet<Integer> referenceMemory){
        ArrayList<DownloadRequest> extractedExcelRows = new ArrayList<>();
        for (int i = 1; i<sheet.getPhysicalNumberOfRows(); i++){
            Row row = sheet.getRow(i);
            if (row == null){
                continue;
            }
            int id = (int) row.getCell(0).getNumericCellValue();
            if (referenceMemory.contains(id)){
                System.out.println("\t" + id + " already downloaded");
                continue;
            }
            String[] urls = new String[2];
            urls[0] = row.getCell(1).getStringCellValue();
            urls[1] = row.getCell(2).getStringCellValue();
            extractedExcelRows.add(new DownloadRequest(id, urls));
        }
        return extractedExcelRows;
    }

    private static Path createDownloadDirectory(Path excelFilePath) throws IOException {
        Path parentDir = excelFilePath.getParent();
        Path downloadDir = parentDir.resolve("Downloaded_PDFs");
        if (!Files.exists(downloadDir)) {
            Files.createDirectory(downloadDir);
        }
        return downloadDir;
    }

    private static List<Future<DownloadResult>> startDownloadTasks(ArrayList<DownloadRequest> extractedExcelRows, HashSet<Integer> referenceMemory, Path downloadDir) {
        ExecutorService threadExecutor = Executors.newCachedThreadPool();
        List<Future<DownloadResult>> futures = new ArrayList<>();

        for (int i = 0; i < extractedExcelRows.size(); i++) {
            futures.add(threadExecutor.submit(new PDFDownloader(extractedExcelRows.get(i), downloadDir)));
        }

        threadExecutor.shutdown();
        return futures;
    }

    private static ArrayList<DownloadResult> collectResults(List<Future<DownloadResult>> futures) {
        ArrayList<DownloadResult> allResults = new ArrayList<>();
        for (Future<DownloadResult> future : futures) {
            try {
                allResults.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return allResults;
    }

    private static void updateReferenceMemory(HashSet<Integer> referenceMemory, ArrayList<DownloadResult> allResults) throws IOException {
        Path refPath = Paths.get("src/main/resources/reference.txt");
        List<String> referenceMemoryList = new ArrayList<>();
        for (DownloadResult result : allResults) {
            if (result.firstUrlSucces() || result.secondUrlSucces()) {
                referenceMemoryList.add(String.valueOf(result.id()));
            }
        }
        Files.write(refPath, referenceMemoryList);
    }

    private static void writeResultsToExcel(String filePath, ArrayList<DownloadResult> results) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Download Results");

        // Create cell styles for green and red
        CellStyle greenCellStyle = workbook.createCellStyle();
        greenCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        greenCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle redCellStyle = workbook.createCellStyle();
        redCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row row = sheet.createRow(0);
        Cell idCell = row.createCell(0);
        idCell.setCellValue("id");
        Cell firstUrlCell = row.createCell(1);
        firstUrlCell.setCellValue("1. URL");
        Cell secondUrlCell = row.createCell(2);
        secondUrlCell.setCellValue("2. URL");

        // Write results to the new sheet
        int rowIndex = 1;
        for (DownloadResult result : results) {
            row = sheet.createRow(rowIndex++);
            idCell = row.createCell(0);
            idCell.setCellValue(result.id());
            firstUrlCell = row.createCell(1);
            secondUrlCell = row.createCell(2);
            // Set cell style based on status
            if (result.firstUrlSucces()) {
                firstUrlCell.setCellValue("Success");
                firstUrlCell.setCellStyle(greenCellStyle);
            } else {
                firstUrlCell.setCellStyle(redCellStyle);
                firstUrlCell.setCellValue("Error");
            }
            if (result.secondUrlSucces()) {
                secondUrlCell.setCellValue("Success");
                secondUrlCell.setCellStyle(greenCellStyle);
            } else {
                secondUrlCell.setCellStyle(redCellStyle);
                secondUrlCell.setCellValue("Error");
            }
        }

        // Save the new workbook to a file
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        }
        workbook.close();
    }
}