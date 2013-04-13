package es.udc.smunin.empresauriostic.ordermanager.model.objectmodels;

public class LoginInfo {

	private String sessionId;

	private String email;

	public LoginInfo(String email, String sessionId) {
		this.email = email;
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getEmail() {
		return email;
	}
}
