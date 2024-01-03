package web.multitask.app.api;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("excel")
public class ExcelApi {
    @PostMapping("/public/generate")
    public ResponseEntity<Resource> generateExcel() {
        return null;
    }
}