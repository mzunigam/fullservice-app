package web.multitask.app.service;


import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;

import java.io.StringReader;

@Service
public class PDFService {

    public ByteArrayResource htmlToPdf(String html){
        try{
        byte[] pdfBytes = generatePdf(html);
        return new ByteArrayResource(pdfBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private byte[] generatePdf(String htmlContent) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        HTMLWorker htmlWorker = new HTMLWorker(document);
        htmlWorker.parse(new StringReader(htmlContent));
        document.close();
        writer.close();
        return outputStream.toByteArray();
    }
}