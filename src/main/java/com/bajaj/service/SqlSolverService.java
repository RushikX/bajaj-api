package com.bajaj.service;

import com.bajaj.config.AppProperties;
import com.bajaj.dto.WebhookGenerateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SqlSolverService {

    private static final Logger logger = LoggerFactory.getLogger(SqlSolverService.class);
    
    private final AppProperties appProperties;

    public SqlSolverService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String solveSqlProblem(WebhookGenerateResponse response) {
        // Extract registration number to determine which question to solve
        String regNo = appProperties.getUser().getRegNo();
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);
        
        logger.info("Registration number: {}, Last two digits: {}", regNo, lastTwoDigits);
        
        // Determine if odd or even
        boolean isOdd = (lastTwoDigitsInt % 2) == 1;
        
        if (isOdd) {
            logger.info("Last two digits are odd. Solving Question 1");
            return solveQuestion1(response);
        } else {
            logger.info("Last two digits are even. Solving Question 2");
            return solveQuestion2(response);
        }
    }

    private String solveQuestion1(WebhookGenerateResponse response) {
        // Problem: Find the highest salaried employee per department,
        // excluding payments made on the 1st day of the month
        
        logger.info("Question 1 details: {}", response.getQuestion());
        logger.info("Question 1 data: {}", response.getData());
        
        // SQL Query Solution:
        // 1. Filter out payments made on 1st day of month
        // 2. Calculate total salary per employee (excluding 1st day payments)
        // 3. Find maximum salary per department
        // 4. Join with EMPLOYEE and DEPARTMENT tables
        // 5. Calculate age from DOB
        // 6. Format employee name as "FIRST_NAME LAST_NAME"
        
        return "SELECT " +
               "    d.DEPARTMENT_NAME, " +
               "    emp_salary.total_salary AS SALARY, " +
               "    CONCAT(emp.FIRST_NAME, ' ', emp.LAST_NAME) AS EMPLOYEE_NAME, " +
               "    TIMESTAMPDIFF(YEAR, emp.DOB, CURDATE()) AS AGE " +
               "FROM ( " +
               "    SELECT " +
               "        e.DEPARTMENT, " +
               "        p.EMP_ID, " +
               "        SUM(p.AMOUNT) AS total_salary, " +
               "        ROW_NUMBER() OVER (PARTITION BY e.DEPARTMENT ORDER BY SUM(p.AMOUNT) DESC) AS rn " +
               "    FROM PAYMENTS p " +
               "    INNER JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
               "    WHERE DAY(p.PAYMENT_TIME) != 1 " +
               "    GROUP BY e.DEPARTMENT, p.EMP_ID " +
               ") emp_salary " +
               "INNER JOIN EMPLOYEE emp ON emp_salary.EMP_ID = emp.EMP_ID " +
               "INNER JOIN DEPARTMENT d ON emp.DEPARTMENT = d.DEPARTMENT_ID " +
               "WHERE emp_salary.rn = 1 " +
               "ORDER BY d.DEPARTMENT_NAME;";
    }

    private String solveQuestion2(WebhookGenerateResponse response) {
        // TODO: Implement SQL query based on Question 2
        // For now, return a placeholder query
        
        logger.info("Question 2 details: {}", response.getQuestion());
        logger.info("Question 2 data: {}", response.getData());
        
        // Placeholder - replace with actual SQL query once question is provided
        return "SELECT * FROM table_name WHERE condition = 'value';";
    }
}

