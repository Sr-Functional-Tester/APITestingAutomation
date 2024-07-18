package com.apitest;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiTestExtractor {

    public static List<TestCase> getTestCases(String filePath) {
        List<TestCase> testCases = null;
        try {
            testCases = extractTestCases(filePath);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return testCases;
    }

    private static List<TestCase> extractTestCases(String filePath) throws IOException {
        List<TestCase> testCases = new ArrayList<>();
        String fileExtension = getFileExtension(filePath);

        if (fileExtension.equalsIgnoreCase("docx")) {
            extractFromDocx(filePath, testCases);
        } else if (fileExtension.equalsIgnoreCase("doc")) {
            extractFromDoc(filePath, testCases);
        } else {
            System.err.println("Unsupported file format: " + fileExtension);
        }

        return testCases;
    }

    private static void extractFromDocx(String filePath, List<TestCase> testCases) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            String currentTestCase = "";
            String description = "";
            String operation = "";
            String endpoint = "";
            String contentType = "";
            String token = "";
            String requestBody = "";
            String assertions = "";
            boolean readingHeaders = false, readingBody = false, readingAssertions = false;

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText().trim();

                if (text.startsWith("TC-")) {
                    if (!currentTestCase.isEmpty()) {
                        testCases.add(new TestCase(currentTestCase, description, operation, endpoint, contentType, token, requestBody, assertions));
                    }
                    String[] parts = text.split(": ", 2);
                    currentTestCase = parts[0]; // TC ID
                    description = parts.length > 1 ? parts[1] : ""; // Description
                    operation = endpoint = contentType = token = requestBody = assertions = ""; // Reset for new test case
                } else if (text.startsWith("Operation:")) {
                    operation = text.replace("Operation:", "").trim();
                } else if (text.startsWith("Endpoint:")) {
                    endpoint = text.replace("Endpoint:", "").trim();
                } else if (text.startsWith("Request-Header:")) {
                    readingHeaders = true;
                    contentType = token = ""; // Reset for new headers
                } else if (text.startsWith("Request-Body:")) {
                    readingBody = true;
                    requestBody = ""; // Reset for new body
                    readingHeaders = false; // Stop reading headers
                } else if (text.startsWith("Assertions:")) {
                    readingAssertions = true;
                    assertions = ""; // Reset for new assertions
                    readingBody = false; // Stop reading body
                } else if (readingHeaders) {
                    if (!text.isEmpty()) {
                        String[] headers = text.split(",");
                        for (String header : headers) {
                            String[] parts = header.split("=");
                            if (parts.length >= 2) {
                                if (parts[0].trim().equalsIgnoreCase("Content-Type")) {
                                    contentType = parts[1].trim();
                                } else if (parts[0].trim().equalsIgnoreCase("Authorization-Bearer-Token")) {
                                    token = parts[1].replace("\"", "").trim();
                                }
                            }
                        }
                    }
                } else if (readingBody) {
                    requestBody += text + "\n"; // Accumulate lines for the request body
                } else if (readingAssertions) {
                    assertions += text + "\n"; // Accumulate lines for assertions
                }
            }
            // Add the last test case
            if (!currentTestCase.isEmpty()) {
                testCases.add(new TestCase(currentTestCase, description, operation, endpoint, contentType, token, requestBody.trim(), assertions.trim()));
            }
        }
    }

    private static void extractFromDoc(String filePath, List<TestCase> testCases) throws IOException {
        HWPFDocument document = null;

        try (FileInputStream fis = new FileInputStream(filePath)) {
            document = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(document);

            String currentTestCase = "";
            String description = "";
            String operation = "";
            String endpoint = "";
            String contentType = "";
            String token = "";
            String requestBody = "";
            String assertions = "";
            boolean readingHeaders = false, readingBody = false, readingAssertions = false;

            String[] paragraphs = extractor.getParagraphText();
            for (String text : paragraphs) {
                text = text.trim();
                if (text.startsWith("TC-")) {
                    if (!currentTestCase.isEmpty()) {
                        testCases.add(new TestCase(currentTestCase, description, operation, endpoint, contentType, token, requestBody, assertions));
                    }
                    String[] parts = text.split(": ", 2);
                    currentTestCase = parts[0]; // TC ID
                    description = parts.length > 1 ? parts[1] : ""; // Description
                    operation = endpoint = contentType = token = requestBody = assertions = ""; // Reset for new test case
                } else if (text.startsWith("Operation:")) {
                    operation = text.replace("Operation:", "").trim();
                } else if (text.startsWith("Endpoint:")) {
                    endpoint = text.replace("Endpoint:", "").trim();
                } else if (text.startsWith("Request-Header:")) {
                    readingHeaders = true;
                    contentType = token = ""; // Reset for new headers
                } else if (text.startsWith("Request-Body:")) {
                    readingBody = true;
                    requestBody = ""; // Reset for new body
                    readingHeaders = false; // Stop reading headers
                } else if (text.startsWith("Assertions:")) {
                    readingAssertions = true;
                    assertions = ""; // Reset for new assertions
                    readingBody = false; // Stop reading body
                } else if (readingHeaders) {
                    if (!text.isEmpty()) {
                        String[] headers = text.split(",");
                        for (String header : headers) {
                            String[] parts = header.split("=");
                            if (parts.length >= 2) {
                                if (parts[0].trim().equalsIgnoreCase("Content-Type")) {
                                    contentType = parts[1].trim();
                                } else if (parts[0].trim().equalsIgnoreCase("Authorization-Bearer-Token")) {
                                    token = parts[1].replace("\"", "").trim();
                                }
                            }
                        }
                    }
                } else if (readingBody) {
                    requestBody += text + "\n"; // Accumulate lines for the request body
                } else if (readingAssertions) {
                    assertions += text + "\n"; // Accumulate lines for assertions
                }
            }
            // Add the last test case
            if (!currentTestCase.isEmpty()) {
                testCases.add(new TestCase(currentTestCase, description, operation, endpoint, contentType, token, requestBody.trim(), assertions.trim()));
            }
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    private static String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf('.') + 1);
    }

    private static void printTestCase(TestCase testCase) {
        System.out.println("Test Case ID: " + testCase.id);
        System.out.println("Description: " + testCase.description);
        System.out.println("Operation: " + testCase.operation);
        System.out.println("Endpoint: " + testCase.endpoint);
        System.out.println("Content-Type: " + testCase.contentType);
        System.out.println("Authorization Token: " + testCase.token);
        System.out.println("Request Body: " + testCase.requestBody);
        System.out.println("Assertions: " + testCase.assertions);
        System.out.println("-----------------------------");
    }


}
