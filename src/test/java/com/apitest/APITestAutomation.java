package com.apitest;

import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class APITestAutomation
{
    public static void main( String[] args )
    {
         AppTest appTest = new AppTest();
         try {
             String filePath = "C:\\Users\\LENOVO\\Desktop\\API_Testcases_doc.docx";
             List<TestCase> testCases = ApiTestExtractor.getTestCases(filePath);
             for(TestCase testCase: testCases) {
                 String testCaseId = testCase.getId();
                 String endpoint = testCase.getEndpoint();
                 String operation = testCase.getOperation();
                 String contentType = testCase.getContentType();
                 String token = testCase.getToken();
                 String requestBody = testCase.getRequestBody();
                 String assertions = testCase.getAssertions();
                 Map<String, String> resultMap = null;
                 if (operation.equals("GET") && !token.isBlank())
                     resultMap = TestExecutor.executeTest(appTest::shouldFindJobPostById, endpoint, token);
                 else
                     resultMap = TestExecutor.executeTest(appTest::testGetRandomCatBreeds, endpoint);

                 String status = resultMap.get("status");
                 String errorMessage = resultMap.get("errorMessage");
                 if(status.equals("Pass")){
                     System.out.println("Test Case with ID "+testCaseId+ " is Pass");
                 }else {
                     System.out.println("Test Case with ID "+testCaseId+ " is Fail");
                     System.out.println(errorMessage);
                 }

             }
         }catch (Exception e){
             System.out.println( e.getMessage());
         }
;
    }
}
