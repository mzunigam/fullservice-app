package web.multitask.app.api;

import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.multitask.app.service.PDFService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("pdf")
public class PDFApi {

    PDFService pdfService;

    public PDFApi(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/public/html")
    public ResponseEntity<Resource> htmlToPdf(@RequestBody String json) {
        try {
            JSONObject bodyJson = new JSONObject(json);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + bodyJson.getString("name"));
            ByteArrayResource resource = pdfService.htmlToPdf(bodyJson.getString("html"));
            return ResponseEntity.ok().headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(resource);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body(null);
        }
    }

}