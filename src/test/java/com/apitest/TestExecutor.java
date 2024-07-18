package com.apitest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TestExecutor {

    public static Map<String, String> executeTest(Consumer<Object[]> testMethod, Object... params) {
        String status = null;
        String errorMessage=null;
        Map<String, String> statusMap = new HashMap<String, String>();
        try {
            testMethod.accept(params);
            status="Pass";
            statusMap.put("status",status);
            statusMap.put("errorMessage",errorMessage);
        } catch (AssertionError e) {
            status = "Fail";
            errorMessage= e.getMessage();
            statusMap.put("status",status);
            statusMap.put("errorMessage",errorMessage);
        } catch (Exception e) {
           // System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        return statusMap;
    }
}

