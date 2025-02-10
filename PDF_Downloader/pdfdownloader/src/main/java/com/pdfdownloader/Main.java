package com.pdfdownloader;

import java.io.*;
import java.net.URL;
import java.nio.file.*;

public class Main {
    private static final String SAVE_DIR = "pdfdownloader/target/downloads/";
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String fileUrl = "http://ebooks.exakta.se/aak/2017/hallbarhetsrapport_2016_2017_en/pubData/source/aak_sustainability_report_2016_2017_ebook.pdf";
        String fileName = "Test";
        System.out.println(downloadPdf(fileUrl, fileName));
    }

    public static String downloadPdf(String fileUrl, String fileName) {
        
        try {
            // Ensure the directory exists
            Files.createDirectories(Paths.get(SAVE_DIR));

            // Open connection and download file
            URL url = new URL(fileUrl);
            try (InputStream in = url.openStream()) {
                Path filePath = Paths.get(SAVE_DIR + fileName);
                FileOutputStream fos = new FileOutputStream(new File("test.pdf"));

                int length = -1;
                byte[] buffer = new byte[1024];
                while ((length = in.read(buffer)) > -1){
                    fos.write(buffer, 0, length);
                }
                fos.close();
                in.close();
                return "PDF downloaded successfully: " + filePath.toAbsolutePath();
            }
        } catch (IOException e) {
            return "Failed to download PDF: " + e.getMessage();
        }
    }
}