package com.myInventory.web;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// Defines class as a controller in the MVC architecture
@Controller
/* The directory used by the controller will be /report and any mapping values hereinafter
 * will be assumed to be under the /report directory
 */
@RequestMapping("/report")
public class ReportController {

    // Declaration of 'log' for debugging purposes
    private static final Logger log= LoggerFactory.getLogger(ProductController.class);

    // Declaring and connecting to the Dao class and dataSource to use in this controller
    @Autowired
    private DataSource dataSource;

    // This really big method allow us to download a pdf report for an invoice
    @RequestMapping("/invoice/{invoice_id}")
    public String invoice(HttpSession session, HttpServletResponse response, @PathVariable Long invoice_id) {
        // initialize context
        ServletContext context = session.getServletContext();
        // The directory for the compiled jasper reports
        String reportPath = context.getRealPath("/WEB-INF/classes/reports") + "/";
        // The name of the main report
        File reportFile = new File(reportPath + " Invoice.jasper");
        // initialize variable parameters, for the values needed later
        Map<String, Object> parameters = new HashMap<>();
        // Inputs the values into the jasper reports from the variable 'parameters'
        parameters.put("SUBREPORT_DIR", reportPath);
        parameters.put("INVOICE_ID", invoice_id);

        JasperPrint jasperPrint = null;

        Connection conn = null;
        /**
         * If no errors or incorrect data types are not given then everything
         * under, this try should execute and jasperPrint will connect and will
         * pass on the parameters to fill the report file.
         */

        try {
            JasperReport jasperReport = (JasperReport) JRLoader
                    .loadObjectFromFile(reportFile.getPath());

            conn = dataSource.getConnection();

            jasperPrint = JasperFillManager.fillReport(jasperReport,
                    parameters, conn);
            /**
             * if something failed we cannot
             * fill the report, an exception will be thrown
             * and a log message will say "can't create report" and we will be
             * taken to a very basic html page that says JasperReports encountered
             * this error (specific error printed here)
             */
        } catch (JRException | SQLException e) {
            log.error("Can't create report", e);
            response.setContentType("text/html");
            try {
                PrintWriter out = response.getWriter();
                out.println("<html>");
                out.println("<head>");
                out.println("<title>My_Inventory</title>");
                out.println("</head>");

                out.println("<body bgcolor=\"white\">");

                out
                        .println("<span class=\"bnew\">JasperReports encountered this error :</span>");
                out.println("<pre>");

                e.printStackTrace(out);

                out.println("</pre>");

                out.println("</body>");
                out.println("</html>");
            } catch (Exception ex) {
            }

            return null;
        } finally {
            // If the connection is working close connection
            if(conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
        // If we got something in the jasperPrint variable
        if(jasperPrint != null) {
            byte[] pdf = null;
            /**
             * if everything is working fine, everything under the try statement
             * will execute. JasperExportManager will export the file to a pdf
             * and we get to call the file we download whatever we want
             */
            try {
                pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                response.setContentType("application/pdf");
                response.setContentLength(pdf.length);
                response.setHeader("Content-disposition",
                        "attachment; filename=\"Invoice" + invoice_id + ".pdf\"");
                /**
                 * if something's wrong JRException will throw and exception and there
                 * will be 'cannot create pdf' written in the server logs
                 */
            } catch (JRException ex) {
                log.error("cannot create pdf", ex);
            }

            ServletOutputStream out;
            // out.write allows us to download the file
            try {
                out = response.getOutputStream();
                out.write(pdf);
                // if something went wrong we get a log message saying we failed
            } catch (IOException e) {
                log.error("cannot write report to stream", e);
            }
            /*
             * If jasperPrint was empty to begin theres a problem!
             * so we get this very lame and basic html page saying we got
             * an empty response, but hey at least its in bold letters
             */
        } else {
            response.setContentType("text/html");
            PrintWriter out;
            try {
                out = response.getWriter();
                out.println("<html>");
                out.println("<head>");
                out.println("<title>My_Inventory</title>");
                out.println("</head>");

                out.println("<body bgcolor=\"white\">");

                out.println("<span class=\"bold\">Empty response.</span>");

                out.println("</body>");
                out .println("</html>");

                /*
                 * if for some reason we failed with the very basic lame html page
                 * all we get is another short message in the server logs saying
                 * 'couldn't write'
                 */
            } catch (IOException e) {
                log.error("couldn't write", e);
            }
        }
        return null;
    }

}
