package mainScripts;

import java.io.IOException;

import jxl.read.biff.BiffException;
import methods.APIMethods;

import org.json.JSONException;
import org.testng.annotations.Test;

public class RestTest {
	public static String sheetName="GET";
	public static String uniqueValue="YathiFacebookGraph";

	@Test
	public void test() throws JSONException, BiffException, IOException{
		
		APIMethods.getJSONResponse(sheetName, uniqueValue);
	}
	
}
