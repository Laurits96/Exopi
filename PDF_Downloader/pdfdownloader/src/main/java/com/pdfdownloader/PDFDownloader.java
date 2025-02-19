package com.pdfdownloader;

import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.ArrayList;
import java.io.IOException;

public class PDFDownloader {
    private static final HttpClient client = HttpClient.newBuilder()
    .followRedirects(HttpClient.Redirect.ALWAYS) // Enable redirection handling
    .connectTimeout(Duration.ofSeconds(15))
    .build();

    public static ArrayList<Integer> downloadPDF(String[] urls, int id, String downloadPath) {    
        // Target path to save the file
        String fileName = id+".pdf";
        Path savePath = Paths.get(downloadPath+"/", fileName);

        ArrayList<Integer> log = new ArrayList<Integer>();
        log.add(id);

        for (String fileURL: urls){
            if (fileURL == null || fileURL.isEmpty()){
                continue;
            }
            try {
                // Create HTTP Request
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(fileURL))
                        .timeout(Duration.ofSeconds(30))
                        .GET()
                        .build();

                // Send request and store response
                HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(savePath));

                // Check HTTP response code
                if (response.statusCode() == 200) {
                    System.out.println("Downloaded: " + fileName);
                    log.add(1);
                    return log; 
                } else {
                    System.out.println("Failed id: " + id + " (HTTP " + response.statusCode() + ")");
                    Files.deleteIfExists(savePath);
                }
            } catch (IOException | InterruptedException | IllegalArgumentException e) {
                System.out.println("Error downloading id: " + id + " -> " + e.getMessage());
                log.add(0);
                return log;
            }
        }
        log.add(0);
        return log;
    }

}
