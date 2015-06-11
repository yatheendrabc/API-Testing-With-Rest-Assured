package utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class commonUtils {
	
	public static Map<String,String> createParamsMap(String[] paramKeys,String[] paramValues){
		Map<String,String> params=new HashMap<String,String>();
		for(int i=0;i<paramKeys.length;i++)
			params.put(paramKeys[i], paramValues[i]);
		return params;
	}
	
	public static Cell[] webReadExcel(String sheetName, String uniqueValue) throws BiffException, IOException
	{
		Workbook wrk1 = Workbook.getWorkbook(new File("apiTestData.xls"));
		Sheet sheet1 = wrk1.getSheet(sheetName);

		LabelCell cell=sheet1.findLabelCell(uniqueValue);
		int row=cell.getRow();
		Cell[] record = sheet1.getRow(row);
		return record;
	}

}
