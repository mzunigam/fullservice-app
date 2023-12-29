package web.multitask.app.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommonUtils {

    private final Dotenv dotenv;

    public CommonUtils( Dotenv dotenv ) {
        this.dotenv = dotenv;
    }

    public  JSONObject convertToBase64(MultipartFile file) {
        JSONObject response = new JSONObject();
        try{
        byte[] byteArray = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(byteArray);
        response.put("base64", base64);
        response.put("name", file.getOriginalFilename());
        response.put("size", file.getSize());
        response.put("extension", file.getContentType());
        return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public  File convertToFile(String base64, String name) {
        try{
        byte[] byteArray = Base64.getDecoder().decode(base64);
        String folder = dotenv.get("FILE_FOLDER") + "/" + name;
        File outputFile = new File(folder);
        FileUtils.writeByteArrayToFile(outputFile, byteArray);
        return outputFile;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean deleteAllFiles() {
        try {
            String folder= dotenv.get("FILE_FOLDER");
            assert folder != null;
            File file = new File(folder);
            if (file.isDirectory()) {
                Boolean[]  results = Arrays.stream(Objects.requireNonNull(file.listFiles())).map(File::delete).toArray(Boolean[]::new);
                return Arrays.asList(results).contains(false);
            }else{
                return file.delete();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}