package web.multitask.app.service;
//
//import lombok.var;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.stream.IntStream;

//@Service
public class ExcelService {

//    public byte[] generateExcel (JSONObject json){
//        XSSFWorkbook workbook = new XSSFWorkbook();
//    }
//
//    private void createSheet(XSSFWorkbook workbook, JSONObject json){
//        if(!json.optJSONArray("data", new JSONArray()).isEmpty()){
//            XSSFSheet sheet = workbook.createSheet(json.optString("sheet_name", "no_name"));
//            buildSheet(sheet, json);
//        }
//    }
//
//    private void buildSheet (XSSFSheet sheet, JSONObject json){
//
//        int row_index = 0;
//        String title = json.optString("title", "");
//        String title_style = json.optString("title_style", "");
//        String responsible = json.optString("responsible", "");
//        JSONArray headers = json.optJSONArray("headers", new JSONArray());
//        JSONArray style_headers = json.optJSONArray("style_headers", new JSONArray());
//        JSONArray identifiers = json.optJSONArray("identifiers", new JSONArray());
//        JSONArray data = json.optJSONArray("data", new JSONArray());
//        int total_rows = data.length();
//        String currentDate = LocalDateTime.now().toString();
//
//        XSSFRow row_title = sheet.createRow(row_index);
//        row_title.createCell(0).setCellValue(title);
//        CellRangeAddress cellRangeAddress = new CellRangeAddress(row_index, row_index, 0, headers.length() - 1);
//        sheet.addMergedRegion(cellRangeAddress);
//        row_index++;
//        sheet.createRow(row_index);
//        XSSFRow row_responsible = sheet.createRow(row_index);
//        row_responsible.createCell(0).setCellValue(responsible);
//        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(row_index, row_index, 0, headers.length() - 1);
//        sheet.addMergedRegion(cellRangeAddress2);
//        row_index++;
//        XSSFRow row_date = sheet.createRow(row_index);
//        row_date.createCell(0).setCellValue("Fecha: " + currentDate);
//        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(row_index, row_index, 0, headers.length() - 1);
//        sheet.addMergedRegion(cellRangeAddress3);
//        row_index++;
//        XSSFRow row_header = sheet.createRow(row_index);
//        IntStream.range(0, headers.length()).forEach(i -> {
//            row_header.createCell(0).setCellValue(headers.optString(i, "").toUpperCase());
//        });
//
//        for(int i = 0; i < data.length(); i++){
//            row_index++;
//            XSSFRow row_data = sheet.createRow(row_index);
//            int finalI = i;
//            IntStream.range(0, identifiers.length()).forEach(j -> {
//                row_data.createCell(j).setCellValue(data.optJSONObject(finalI).optString(identifiers.optString(j, ""), ""));
//            });
//        }
//    }
}