package to.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonArrayToExcel {
	public XSSFWorkbook workbook;
	public XSSFSheet spreadsheet;
	public String fileNmae;
	public String sheetNmae;

	public JsonArrayToExcel(String fileNmae, String sheetNmae) {
		this.sheetNmae = sheetNmae;
		this.fileNmae = fileNmae;
		this.workbook = new XSSFWorkbook();
		// Create a blank sheet
		this.spreadsheet = workbook.createSheet(sheetNmae);
		// Create row object

	}

	public void doExcelFrom518(JSONArray requestArray) {
		XSSFRow row;
		Map<String, Object[]> tableInfo = new LinkedHashMap<String, Object[]>();
		tableInfo.put("1", new Object[] { "職缺", "公司名稱", "薪水"});
		try {
			for (int i = 0; i < requestArray.length(); i++) {
				JSONObject jsonObj = requestArray.getJSONObject(i);
				tableInfo.put(String.valueOf(i + 2),
						new Object[] { jsonObj.optString("職缺"), jsonObj.optString("公司名稱"), jsonObj.optString( "薪水") });
			}

		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}

		Set<String> keyid = tableInfo.keySet();
		int rowid = 0;
		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = tableInfo.get(key);
			int cellid = 0;
			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue((String) obj);
			}
		}

		try (FileOutputStream out = new FileOutputStream(
				new File("D:\\javaLib\\selenium-java-3.10.0\\Firefox\\" + fileNmae + ".xlsx"));) {
			workbook.write(out);
			System.out.println("Writesheet.xlsx written successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("excel失敗");
		}
	}

}
