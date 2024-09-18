package RC.TestData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import RC.TestComponents.GetExcelData;

public class DataProviderData {
	
	public List<HashMap<Object, Object>> getJsonDataToMap( String filePath) throws IOException {
		//json file
		File file = new File(filePath);
				
		// converting json file content to string
		String jsonString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		// converting string to list of HashMaps using jackson databind
		ObjectMapper mapper = new ObjectMapper();
		//readValue takes TypeReference object i.e which type to return, here it is List<HashMap<Oject,Object>>
		List<HashMap<Object, Object>> data = mapper.readValue(jsonString,
				new TypeReference<List<HashMap<Object, Object>>>() {
				});
		return data;

	}
	
	
	@DataProvider(name = "data")
	public Object[][] getData(Method method) throws IOException {
		// getJsonDataToMap(uses jackson binder to convert string to list of HashMaps)
		// which is written in BaseTest returns list of hashMaps.

		// getting value of testName from @Test annotations
		String testCase = method.getAnnotation(Test.class).testName();
		
		//(Refer) GetExcelData class for getting filePath of JSON data file 
		GetExcelData excelData = new GetExcelData();
		FileInputStream fis = new FileInputStream(
				"C:\\Users\\SUNNY\\eclipse-workspace\\GitSample\\TestCasesDataFilePathMapper.xlsx");
		XSSFWorkbook workBook = new XSSFWorkbook(fis);
		XSSFSheet sheet = excelData.getSheet(workBook, "filePath");
		int testCaseNameColumnNumber = excelData.getColumnNumber(sheet,0, "testCaseName");
		int dataFilePathColumnNumber = excelData.getColumnNumber(sheet,0, "DataFilePath");
		int testCaseRowNumber = excelData.getRowNumber(sheet, testCaseNameColumnNumber, testCase);
		String filePath = System.getProperty("user.dir")
				+ excelData.getCellValue(sheet, testCaseRowNumber, dataFilePathColumnNumber);

		List<HashMap<Object, Object>> data = getJsonDataToMap(filePath);
		// data.get(index) is a hashMap
		Object[][] obj=new Object[data.size()][1];
		for(int i=0;i<data.size();i++) {
			obj[i][0]=data.get(i);
		}
			 
		return obj;
		 
	}

}
