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
	
	//GET request and assert reponse complete 
	public static void getJSONResponse(String sheetName,String uniqueValue) throws BiffException, IOException{
		//reading api data from excel
		Cell[] apiData=commonUtils.webReadExcel(sheetName, uniqueValue);
		String apiUri=apiData[1].getContents();
		String[] paramKeys;
		String[] paramValues;
		String[] object=apiData[4].getContents().split(",");
		String[] objectPath=apiData[5].getContents().split(",");
		String[] objectValue=apiData[6].getContents().split(",");
		
		Map<String,String> params;
		Response response;
		
		//if no parameters are given then it GET's without parameters
		if(apiData[2].getContents().equals("NA")){
			response=APIMethods.getJSONResponseWithoutParameters(apiUri);
			
		}
		//if there are parameters then it GET's using parameters
		else{
			paramKeys=apiData[2].getContents().split(",");
			paramValues=apiData[3].getContents().split(",");
			
			//calling method to create MAP for parameter key, value pairs
			params=commonUtils.createParamsMap(paramKeys, paramValues);
			
			//getting response from GET request
			response=APIMethods.getJSONResponseWithParameters(params, apiUri);
		}
		
		//calling method to convert Response to string
		String stringResponse=convertResponseToString(response);
		assertJSONResponseObjects(stringResponse, object, objectPath, objectValue);
	}
	
	//converts Response to string
	public static String convertResponseToString(Response response){
		String stringResponse=response.asString();
		return stringResponse;
	}
	
	//getting Response using parameters
	public static Response getJSONResponseWithParameters(Map<String,String> params,String apiUri){
		Response response=given().queryParams(params).when().get(apiUri);
		return response;
	}
	
	//getting Response without using parameters
	public static Response getJSONResponseWithoutParameters(String apiUri){
		Response response=get(apiUri);
		return response;
	}
	
	//getting JSON object value from String converted Response
	public static String getJSONObjectValue(String stringResponse, String objectPath){
		String string=from(stringResponse).get(objectPath);
		return string;
	}
	
	//Asserting each JSON Object value with expected value
	public static void assertJSONResponseObjects(String stringResponse, String[] object, String[] objectPath, String[] objectValue){
		for(int i=0;i<objectPath.length;i++){
			String value=getJSONObjectValue(stringResponse, objectPath[i]);
			Assert.assertEquals(value, objectValue[i]);
			System.out.println("Asserted "+object[i]+": "+value);
		}
	}

}
