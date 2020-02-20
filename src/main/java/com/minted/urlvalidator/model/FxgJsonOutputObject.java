package com.minted.urlvalidator.model;

public class FxgJsonOutputObject {
	
	public FxgJsonOutputObject(String url, String rubricStatusCode, String rubricStatusBody, String s7StatusCode,
			String s7StatusBody, String comments) {
		super();
		this.url = url;
		this.rubricStatusCode = rubricStatusCode;
		this.rubricStatusBody = rubricStatusBody;
		this.s7StatusCode = s7StatusCode;
		this.s7StatusBody = s7StatusBody;
		this.comments=comments;
	}
	private String url;
	private String rubricStatusCode;
	private String rubricStatusBody;
	private String s7StatusCode;
	private String s7StatusBody;
	private String comments;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRubricStatusCode() {
		return rubricStatusCode;
	}
	public void setRubricStatusCode(String rubricStatusCode) {
		this.rubricStatusCode = rubricStatusCode;
	}
	public String getRubricStatusBody() {
		return rubricStatusBody;
	}
	public void setRubricStatusBody(String rubricStatusBody) {
		this.rubricStatusBody = rubricStatusBody;
	}
	public String getS7StatusCode() {
		return s7StatusCode;
	}
	public void setS7StatusCode(String s7StatusCode) {
		this.s7StatusCode = s7StatusCode;
	}
	public String getS7StatusBody() {
		return s7StatusBody;
	}
	public void setS7StatusBody(String s7StatusBody) {
		this.s7StatusBody = s7StatusBody;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	

}
