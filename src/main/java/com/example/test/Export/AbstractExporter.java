package com.example.test.Export;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {
    public void setResponseHeader(HttpServletResponse response, String contentType,
               String extension)  throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = dateFormat.format(new Date());
        String fileName = "Danh muc san pham bao tri " + timestamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }
}
