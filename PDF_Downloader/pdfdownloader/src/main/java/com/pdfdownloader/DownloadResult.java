package com.pdfdownloader;

public record DownloadResult(int id,
                            boolean firstUrlSucces,
                            boolean secondUrlSucces
                            ){
}
