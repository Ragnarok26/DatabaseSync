package com.DatabaseSync;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkTask extends AsyncTask<String, Void, String> {
	private String name, url, method, soap;
	private String ipDataBase;
	private String userDataBase;
	private String passwordDataBase;
	private String catalogDataBase;
	private String tablesDataBase;
	private String separatorDataBase;
	private String queriesDataBase;
	private String clientKey;
	private int op;
	private int type;
	private String Cadena;
	boolean useUrlConnection;

	NetworkTask(String n, String u, String m, String s, int operation) {
		name = n;
		url = u;
		method = m;
		soap = s;
		op = operation;
		type = 0;
	}

	public void setDatos(String ipDataBase, String userDataBase, String passwordDataBase, String catalogDataBase, String tablesDataBase, String separatorDataBase, String queriesDataBase) {
		this.ipDataBase = ipDataBase;
		this.userDataBase = userDataBase;
		this.passwordDataBase = passwordDataBase;
		this.catalogDataBase = catalogDataBase;
		this.tablesDataBase = tablesDataBase;
		this.separatorDataBase = separatorDataBase;
		this.queriesDataBase = queriesDataBase;
		type = 4;
	}

	public void setDatos(String clientKey, String ipDataBase, String userDataBase, String passwordDataBase, String catalogDataBase, String tablesDataBase, String separatorDataBase, String queriesDataBase) {
		this.ipDataBase = ipDataBase;
		this.userDataBase = userDataBase;
		this.passwordDataBase = passwordDataBase;
		this.catalogDataBase = catalogDataBase;
		this.tablesDataBase = tablesDataBase;
		this.separatorDataBase = separatorDataBase;
		this.queriesDataBase = queriesDataBase;
		this.clientKey = clientKey;
		type = 3;
	}

	public void setDatos(String ipDataBase, String userDataBase, String passwordDataBase, String catalogDataBase, String tablesDataBase, String separatorDataBase) {
		this.ipDataBase = ipDataBase;
		this.userDataBase = userDataBase;
		this.passwordDataBase = passwordDataBase;
		this.catalogDataBase = catalogDataBase;
		this.tablesDataBase = tablesDataBase;
		this.separatorDataBase = separatorDataBase;
		type = 2;
	}

	public void setDatos(String ipDataBase, String userDataBase, String passwordDataBase, String catalogDataBase, String query) {
		this.ipDataBase = ipDataBase;
		this.userDataBase = userDataBase;
		this.passwordDataBase = passwordDataBase;
		this.catalogDataBase = catalogDataBase;
		this.Cadena = query;
		type = 1;
	}

	public boolean isUrlConnection() {
		return this.useUrlConnection;
	}

	public void UseUrlConnection(boolean useUrlConnection) {
		this.useUrlConnection = useUrlConnection;
	}

	@Override
	protected String doInBackground(String... arg0) {
		String res = "";
		boolean flag;
		try {
			if (useUrlConnection) {
				String request = "";
				if (op == Operation.Sync_MSSQLServer_To_SQLite) {
					flag = true;
					request += "<ipDataBase>" + ipDataBase + "</ipDataBase>";
					request += "<userDataBase>" + userDataBase + "</userDataBase>";
					request += "<passwordDataBase>" + passwordDataBase + "</passwordDataBase>";
					request += "<catalogDataBase>" + catalogDataBase + "</catalogDataBase>";
					request += "<tables>" + tablesDataBase + "</tables>";
					request += "<separator>" + separatorDataBase + "</separator>";
					if (type == 3 || type == 4) {
						if (type == 3) {
							request += "<key>" + clientKey + "</key>";
						}
						request += "<queries>" + queriesDataBase + "</queries>";
					}
				} else if (op == Operation.Sync_SQLite_To_MSSQLServer) {
					flag = true;
					request += "<ipDataBase>" + ipDataBase + "</ipDataBase>";
					request += "<userDataBase>" + userDataBase + "</userDataBase>";
					request += "<passwordDataBase>" + passwordDataBase + "</passwordDataBase>";
					request += "<catalogDataBase>" + catalogDataBase + "</catalogDataBase>";
					request += "<queryArray>" + Cadena + "</queryArray>";
				} else {
					flag = false;
				}
				if (flag) {
					request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
							"<SOAP-ENV:Envelope \n" +
							"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
							"xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" \n" +
							"SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" \n" +
							"xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"> \n" +
							"<SOAP-ENV:Body> \n" +
							"<" + method + " xmlns=\"" + name + "\">" + request + "</" + method + "> \n" +
							"</SOAP-ENV:Body> \n" +
							"</SOAP-ENV:Envelope>";
					String charset = "UTF-8";
					URLConnection connection = new URL(url).openConnection();
					connection.setDoOutput(true); // Triggers POST.
					connection.setRequestProperty("Accept-Charset", charset);
					connection.setRequestProperty("Content-Type", "text/xml;charset=" + charset);
					connection.setRequestProperty("Soapaction", soap);
					connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
					OutputStream output = connection.getOutputStream();
					try {
						output.write(request.getBytes(charset));
						request = null;
						output.close();
						output = null;
						System.gc();
						res = new Scanner(connection.getInputStream(), charset).useDelimiter("\\A").next();
						charset = null;
						connection = null;
						System.gc();
						res = res.replace("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><" + method + "Response xmlns=\"" + name + "\"><" + method + "Result>", "");
						res = res.replace("</" + method + "Result></" + method + "Response></s:Body></s:Envelope>", "");
						/*res = res.replace("True", "1");
						res = res.replace("False", "0");*/
						System.gc();
					} catch (Exception ex) {
						res = "#E2";
					}
				} else {
					res = "#E3";
				}
			} else {
				SoapObject request = new SoapObject(name, method);
				if (op == Operation.Sync_MSSQLServer_To_SQLite) {
					request.addProperty("ipDataBase", ipDataBase);
					request.addProperty("userDataBase", userDataBase);
					request.addProperty("passwordDataBase", passwordDataBase);
					request.addProperty("catalogDataBase", catalogDataBase);
					request.addProperty("tables", tablesDataBase);
					request.addProperty("separator", separatorDataBase);
					if (type == 3 || type == 4) {
						if (type == 3) {
							request.addProperty("key", clientKey);
						}
						request.addProperty("queries", queriesDataBase);
					}
					flag = true;
				} else if (op == Operation.Sync_SQLite_To_MSSQLServer) {
					request.addProperty("ipDataBase", ipDataBase);
					request.addProperty("userDataBase", userDataBase);
					request.addProperty("passwordDataBase", passwordDataBase);
					request.addProperty("catalogDataBase", catalogDataBase);
					request.addProperty("queryArray", Cadena);
					flag = true;
				} else {
					flag = false;
				}
				if (flag) {
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					//HttpTransportSE transporte = new HttpTransportSE(url);
					HttpTransportSE transporte = new HttpTransportSE(url, 0);
					try {
						transporte.call(soap, envelope);
						SoapPrimitive resultado_xml = (SoapPrimitive)envelope.getResponse();
						res = resultado_xml.toString();
						/*res = res.replace("True", "1");
						res = res.replace("False", "0");*/
					} catch (IOException e) {
						res = "#E1";
					} catch (XmlPullParserException e) {
						res = "#E2";
					} catch (ClassCastException e) {
						Object resultado_xml = envelope.bodyIn;
						res = resultado_xml.toString();
					}
				} else {
					res = "#E3";
				}
			}
		} catch (Exception e) {
			res = "#E4";
		}
		try {
			Log.e("DatabaseSync", "HttpResponse: " + res);
			return res;
		}
		finally {
			res = null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// this.cancel(true);
	}
}
