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
        
        // SQL Query as provided in requirements
        return "SELECT " +
               "    d.DEPARTMENT_NAME, " +
               "    t.max_salary AS SALARY, " +
               "    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME, " +
               "    FLOOR(DATEDIFF(CURDATE(), e.DOB) / 365) AS AGE " +
               "FROM ( " +
               "    SELECT " +
               "        emp.DEPT_ID, " +
               "        p.EMP_ID, " +
               "        MAX(p.AMOUNT) AS max_salary " +
               "    FROM PAYMENTS p " +
               "    JOIN EMPLOYEE emp ON emp.EMP_ID = p.EMP_ID " +
               "    WHERE DAY(p.PAYMENT_TIME) <> 1 " +
               "    GROUP BY emp.DEPT_ID, p.EMP_ID " +
               ") t " +
               "JOIN ( " +
               "    SELECT " +
               "        DEPT_ID, " +
               "        MAX(max_salary) AS highest_salary " +
               "    FROM ( " +
               "        SELECT " +
               "            emp.DEPT_ID, " +
               "            p.EMP_ID, " +
               "            MAX(p.AMOUNT) AS max_salary " +
               "        FROM PAYMENTS p " +
               "        JOIN EMPLOYEE emp ON emp.EMP_ID = p.EMP_ID " +
               "        WHERE DAY(p.PAYMENT_TIME) <> 1 " +
               "        GROUP BY emp.DEPT_ID, p.EMP_ID " +
               "    ) s " +
               "    GROUP BY DEPT_ID " +
               ") m " +
               "    ON t.DEPT_ID = m.DEPT_ID AND t.max_salary = m.highest_salary " +
               "JOIN EMPLOYEE e ON e.EMP_ID = t.EMP_ID " +
               "JOIN DEPARTMENT d ON d.DEPARTMENT_ID = t.DEPT_ID;";
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

