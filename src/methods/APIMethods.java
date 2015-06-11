package methods;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;

import java.io.IOException;
import java.util.Map;

import jxl.Cell;
import jxl.read.biff.BiffException;

import org.testng.Assert;

import utils.commonUtils;

import com.jayway.restassured.response.Response;

public class APIMethods {
	
	public static void getJSONResponse(String sheetName,String uniqueValue) throws BiffException, IOException{
		Cell[] apiData=commonUtils.webReadExcel(sheetName, uniqueValue);
		String apiUri=apiData[1].getContents();
		String[] paramKeys;
		String[] paramValues;
		String[] object=apiData[4].getContents().split(",");
		String[] objectPath=apiData[5].getContents().split(",");
		String[] objectValue=apiData[6].getContents().split(",");
		Map<String,String> params;
		Response response;
		
		if(apiData[2].getContents().equals("NA")){
			response=APIMethods.getJSONResponseWithoutParameters(apiUri);
			
		}
		else{
			paramKeys=apiData[2].getContents().split(" ");
			paramValues=apiData[3].getContents().split(" ");
			params=commonUtils.createParamsMap(paramKeys, paramValues);
			response=APIMethods.getJSONResponseWithParameters(params, apiUri);
		}
		String stringResponse=convertResponseToString(response);
		assertJSONResponseObjects(stringResponse, object, objectPath, objectValue);
	}
	
	public static String convertResponseToString(Response response){
		String stringResponse=response.asString();
		return stringResponse;
	}
	
	public static Response getJSONResponseWithParameters(Map<String,String> params,String apiUri){
		Response response=given().queryParams(params).when().get(apiUri);
		return response;
	}
	
	public static Response getJSONResponseWithoutParameters(String apiUri){
		Response response=get(apiUri);
		return response;
	}
	
	public static String getJSONObjectValue(String stringResponse, String objectPath){
		String string=from(stringResponse).get(objectPath);
		return string;
	}
	
	public static void assertJSONResponseObjects(String stringResponse, String[] object, String[] objectPath, String[] objectValue){
		for(int i=0;i<objectPath.length;i++){
			String value=getJSONObjectValue(stringResponse, objectPath[i]);
			Assert.assertEquals(value, objectValue[i]);
			System.out.println("Asserted "+object[i]+": "+value);
		}
	}

}
