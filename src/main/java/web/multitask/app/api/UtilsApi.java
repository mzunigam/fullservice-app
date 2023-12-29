package web.multitask.app.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.multitask.app.model.FileRequest;
import web.multitask.app.utils.CommonUtils;

import java.io.File;
import java.util.Arrays;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("utils")
public class UtilsApi {

    CommonUtils  commonUtils;

    public UtilsApi(CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
    }

    @RequestMapping(path = "/public/file/base64", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> fileBase64(@ModelAttribute FileRequest request) {
        JSONObject response = new JSONObject();
        try{
            MultipartFile[] files = request.getFiles();
            JSONArray jsonArray = new JSONArray();
            Arrays.stream(files).forEach(file -> {
                jsonArray.put(commonUtils.convertToBase64(file));
            });
            response.put("files", jsonArray);
            response.put("message", "Success");
            response.put("status", true);
            return ResponseEntity.ok(response.toMap());
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/public/base64/file")
    public ResponseEntity<Resource> base64File(@RequestBody  String json) {
        try{
            JSONObject bodyJson = new JSONObject(json);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + bodyJson.getString("name"));
            File outputFile = commonUtils.convertToFile(bodyJson.getString("base64"),bodyJson.getString("name"));
            Resource resource = new FileSystemResource(outputFile);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}