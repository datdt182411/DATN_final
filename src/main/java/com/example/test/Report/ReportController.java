package com.example.test.Report;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    @GetMapping("/reports")
    public String viewSaleReportHome() {
        return "Report/reports";
    }
}
