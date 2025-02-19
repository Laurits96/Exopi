README

Overview
This is a Java-based command-line tool for downloading PDF files based on URLs specified in an Excel file. The program reads the provided Excel file, downloads the PDFs, organizes them into a newly created folder, and generates a log file summarizing the downloads.

Features
•	Reads an Excel file containing PDF URLs.
•	Downloads PDFs and stores them in a new folder within the same directory as the provided Excel file.
•	Generates an Excel log file detailing the download process.

Requirements
•	Java 17 or later
•	Apache POI (included in the packaged JAR)

Installation
1.	Ensure you have Java 17 or later installed.
2.	Download the JAR file: pdf-downloader.jar.

Usage
Run the program from the terminal with the following syntax:
java -jar pdf-downloader.jar <path-to-excel-file> <yes|no>

Arguments:
1.	<path-to-excel-file> – The absolute path to the Excel file.
2.	<yes|no> – If the program should reference previoulsy downloaded files or not (yes - use memory to skip previously downloaded, no - download all files a new).
Example:
java -jar pdf-downloader.jar /home/user/documents/urls.xlsx yes

Output
•	A new folder (Downloaded_PDFs) will be created in the same directory as the provided Excel file, containing the downloaded PDFs.
•	A log file (Download_Results.xlsx) will be generated to summarize which PDFs were successfully downloaded and which failed.

Troubleshooting
•	Ensure Java is installed: Run java -version to verify your installation.
•	Excel file format: Ensure the file is in .xlsx format.
•	Check network connection: The program requires an active internet connection to download PDFs.

