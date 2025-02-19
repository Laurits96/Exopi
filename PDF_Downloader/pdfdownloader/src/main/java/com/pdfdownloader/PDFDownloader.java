package com.pdfdownloader;

import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.concurrent.Callable;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.io.InputStream;

class PDFDownloader implements Callable<DownloadResult> {
    private final HttpClient client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS) // Enable redirection handling
        .connectTimeout(Duration.ofSeconds(25))
        .build();

    private final Duration REQUEST_TIMEOUT = Duration.ofSeconds(40);
    private DownloadRequest downloadRequest;
    private Path downloadDir;

    public PDFDownloader(DownloadRequest downloadRequest, Path downloadDir) {
        this.downloadRequest = downloadRequest;
        this.downloadDir = downloadDir;
    }

    @Override
    public DownloadResult call() throws Exception{
        String fileName = this.downloadRequest.id() + ".pdf";
        Path savePath = Paths.get(this.downloadDir.toString(), fileName);

        boolean firstUrlSuccess = false;
        boolean secondUrlSuccess = false;

        for (int i = 0; i < downloadRequest.urls().length; i++) {
            String fileURL = downloadRequest.urls()[i];
            if (fileURL == null || fileURL.isEmpty()) {
                continue;
            }
            try {
                HttpRequest request = createHttpRequest(fileURL);
                HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(savePath));
                if (!handleResponse(response, savePath, this.downloadRequest.id(), fileName)) {
                    continue;
                }
                if (!validatePDF(savePath)) {
                    Files.deleteIfExists(savePath);
                    continue;
                }
                if (i == 0) {
                    firstUrlSuccess = true;
                } else {
                    secondUrlSuccess = true;
                }
            } catch (IOException | InterruptedException | IllegalArgumentException e) {
                System.out.println("Error downloading id: " + this.downloadRequest.id() + " -> " + e.getMessage());
            }
        }
        return new DownloadResult(this.downloadRequest.id(), firstUrlSuccess, secondUrlSuccess);
    }

    private HttpRequest createHttpRequest(String fileURL) {
        return HttpRequest.newBuilder()
            .uri(URI.create(fileURL))
            .timeout(this.REQUEST_TIMEOUT)
            .GET()
            .build();
    }

    private boolean handleResponse(HttpResponse<Path> response, Path savePath, int id, String fileName) throws IOException {
        if (response.statusCode() == 200) {
            System.out.println("Downloaded: " + fileName);
            return true;
        } else {
            System.out.println("Failed id: " + id + " (HTTP " + response.statusCode() + ")");
            Files.deleteIfExists(savePath);
            return false;
        }
    }

    private boolean validatePDF(Path pdfPath) {
        // Check if file size is greater than 0
        try {
            if (Files.size(pdfPath) == 0) {
                System.out.println("File size is 0: " + pdfPath.getFileName());
                return false;
            }
        } catch (IOException e) {
            System.out.println("Failed to check file size: " + pdfPath.getFileName() + " -> " + e.getMessage());
            return false;
        }

        // Check if the file starts with the PDF magic number (%PDF-)
        try (InputStream is = Files.newInputStream(pdfPath)) {
            byte[] buffer = new byte[5];
            if (is.read(buffer) != 5 || !new String(buffer).equals("%PDF-")) {
                System.out.println("Invalid PDF magic number: " + pdfPath.getFileName());
                return false;
            }
        } catch (IOException e) {
            System.out.println("Failed to check PDF magic number: " + pdfPath.getFileName() + " -> " + e.getMessage());
            return false;
        }

        // Check if the PDF can be loaded and has more than 0 pages
        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
            return document.getNumberOfPages() > 0;
        } catch (IOException e) {
            System.out.println("Failed to validate PDF: " + pdfPath.getFileName() + " -> " + e.getMessage());
            return false;
        }
    }
}
