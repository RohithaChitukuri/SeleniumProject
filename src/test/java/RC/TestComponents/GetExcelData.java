package RC.TestComponents;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetExcelData {
	public static void main(String[] args) throws IOException {
		//1.Get hold of XFFSWorkbook; 2.Get hold of the intended sheet; 3. Identify testCase column number by scanning entire first row(headings)
		//4. Once column is identified, scan for required testCase name by scanning the rows in that column; 5. Once the row of the intended testCase name is identified, pull the data from that row
		
		GetExcelData excelData=new GetExcelData();
		FileInputStream fis=new FileInputStream("C:\\Users\\SUNNY\\eclipse-workspace\\GitSample\\TestCasesDataFilePathMapper.xlsx");
		XSSFWorkbook workBook=new XSSFWorkbook(fis);
		XSSFSheet sheet=excelData.getSheet(workBook,"filePath");
		int testCaseNameColumnNumber=excelData.getColumnNumber(sheet, "testCaseName");
		int dataFilePathColumnNumber=excelData.getColumnNumber(sheet, "DataFilePath");
		int testCaseRowNumber=excelData.getRowNumber(sheet, testCaseNameColumnNumber,"testCase1");
		String filePath=System.getProperty("user.dir")+ excelData.getCellValue(sheet, testCaseRowNumber, dataFilePathColumnNumber);
		System.out.println(filePath);
		  
		 
		
		}
		
	
	public static XSSFSheet getSheet(XSSFWorkbook workBook,String sheetName) {
		int totalSheets=workBook.getNumberOfSheets();
		XSSFSheet sheet = null;
		for(int i=0;i<totalSheets;i++) {
			if(workBook.getSheetName(i).equalsIgnoreCase(sheetName)){
				sheet=workBook.getSheetAt(i);
			}
		}
		return sheet;
	}
	public static Integer getColumnNumber(XSSFSheet sheet,String columnName) {
		Iterator<Row> rowPointer= sheet.iterator();
		Row firstRow=rowPointer.next();
		
		Iterator<Cell> cellPointer=firstRow.cellIterator();
		int k=0;Integer column = null;
		while(cellPointer.hasNext()) {
			Cell cell=cellPointer.next();
			if(cell.getStringCellValue().equalsIgnoreCase(columnName)) {
				column=k;
			}
			k++;
		}
		return column;
		
	}
	
	public static Integer getRowNumber(XSSFSheet sheet,int columnNumber,String rowValue) {
		Integer rowNumber=null;
		Iterator<Row> rowPointer=sheet.iterator();
		int k=0;
		while(rowPointer.hasNext()) {
			Row row=rowPointer.next();
			if(row.getCell(columnNumber).getStringCellValue().equals(rowValue)) {
				rowNumber=k;
			}
			k++;
		}
		return rowNumber;
	}
	public static String getCellValue(XSSFSheet sheet, int rowNumber,int columnNumber) {
		Row row=sheet.getRow(rowNumber);
		String cellValue=row.getCell(columnNumber).getStringCellValue();
		return cellValue;
		
	}
}
