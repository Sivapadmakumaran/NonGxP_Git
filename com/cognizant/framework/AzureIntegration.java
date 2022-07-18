package com.cognizant.framework;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Class to update test results in Azure DevOps
 *
 */
public class AzureIntegration {

	private String organizationURL;
	private String projectName;
	private String buildID;
	private String releaseURI;
	private String releaseEnvironmentURI;
	private int runID;
	private static final String AUTHORIZATIONHEADER = "Authorization";
	private String accessToken ;
	private static final String MIMETYPEJSON = "application/json";
	
	public AzureIntegration(String strOrganizationURL, String strProjectName, String strOAuthAccessToken, String strReleaseURI,
			String strReleaseEnvironmentURI, String strBuildID) {
		this.organizationURL = strOrganizationURL;
		this.projectName = strProjectName;
		this.accessToken = "Bearer "+strOAuthAccessToken;
		this.releaseURI = strReleaseURI;
		this.releaseEnvironmentURI = strReleaseEnvironmentURI;
		this.buildID = strBuildID;
	}
	
	/**
	 * Method to create Run in tests tab of release 
	 */
	public void createRun() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/test/runs?api-version=5.0");
		String runName = "Run_"+new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss").format(new Date());
		try {
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "{"
		    					+ "\"automated\" : \"true\","
			    				+ "\"name\" : \""+runName+"\","
			    				+ "\"releaseEnvironmentUri\" : \""+releaseEnvironmentURI+"\","
			    				+ "\"releaseUri\" : \""+releaseURI+"\","
			    				+ "\"build\" : "
		    					+ "{"
		    						+ "\"id\" : \""+buildID+"\""
		    					+ "}"
		    				+ "}";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    HttpResponse response = httpClient.execute(postRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();
		    runID = jsonobj.get("id").getAsInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to add Test results to run
	 */
	public void addResultsToRun(List<HashMap<String,Object>> testDetails){
		try {
			String json = new Gson().toJson(testDetails);
			HttpClient httpClient = HttpClientBuilder.create().build();
		    HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/test/Runs/"+runID+"/results?api-version=5.0");
		    postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    StringEntity entity = new StringEntity(json);
		    postRequest.setEntity(entity);
		    httpClient.execute(postRequest);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to update Run Status in Azure
	 */
	public void updateRun() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPatch patchRequest = new HttpPatch(organizationURL+projectName+"/_apis/test/runs/"+runID+"?api-version=5.0");
		try {
			patchRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			patchRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			patchRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "{"
		    					+ "\"state\" : \"Completed\""
		    				+ "}";
		    patchRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    httpClient.execute(patchRequest);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Method to add Attachments to Run in Azure
	 * @param strAttachmentPath - path of the file which has to be attached
	 * @param strAttachmentName - Name of the Attachment in Azure
	 * @param strComments - Comments to be captured against attachment in Azure
	 */
	public void addAttachmentToRun(String strAttachmentPath, String strAttachmentName, String strComments) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/test/Runs/"+runID+"/"+ "attachments?api-version=5.0-preview.1");
		try {
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "{"
		    					+ "\"Stream\" : \""+getStream(strAttachmentPath)+"\","
		    					+"\"fileName\" : \""+strAttachmentName+"\","
		    					+"\"comment\" : \""+strComments+"\","
		    					+"\"attachmentType\" : \"GeneralAttachment\""
		    				+"}";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    httpClient.execute(postRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Method to get base64 encoded file stream
	 * @param strFilePath - path of the file for which encoded string is to be returned 
	 * @return - Base64 encoded file stream
	 */
	public String getStream(String strFilePath) {
		File file = new File(strFilePath);
		String strEncodedString = "";
		byte[] encoded = null;
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
			strEncodedString = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strEncodedString;
	}
}
