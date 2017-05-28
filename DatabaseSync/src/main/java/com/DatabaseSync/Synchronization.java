package com.DatabaseSync;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class Synchronization {
	private Context context;
    private Properties properties;
    private AssetsPropertyReader assetsPropertyReader;
    private NetworkTask netServ;
	private String NAMESPACE;
	private String URL;
	private String METHOD_NAME_SYNC_TO_CLIENT;
	private String SOAP_ACTION_SYNC_TO_CLIENT;
	private String METHOD_NAME_SYNC_TO_SERVER;
	private String SOAP_ACTION_SYNC_TO_SERVER;
	private String tables;
	private String separator;
	private String ipDatabase;
	private String userDatabase;
	private String passwordDatabase;
	private String catalogDatabase;
	private String queriesDatabase;
	private String clientKey;
	private boolean urlConnection;
	private final ArrayList<String> ndb = new ArrayList<String>();
	
	public Synchronization(Context context) {
		this.context = context;
		urlConnection = true;
		this.assetsPropertyReader = new AssetsPropertyReader(context);
		this.properties = this.assetsPropertyReader.getProperties("Propiedades.properties");
		if (this.properties != null) {
			NAMESPACE = properties.getProperty("NAMESPACE");
			URL = properties.getProperty("URL");
			METHOD_NAME_SYNC_TO_CLIENT = properties.getProperty("METHOD_NAME_SYNC_TO_CLIENT");
			SOAP_ACTION_SYNC_TO_CLIENT = properties.getProperty("SOAP_ACTION_SYNC_TO_CLIENT");
			METHOD_NAME_SYNC_TO_SERVER = properties.getProperty("METHOD_NAME_SYNC_TO_SERVER");
			SOAP_ACTION_SYNC_TO_SERVER = properties.getProperty("SOAP_ACTION_SYNC_TO_SERVER");
			tables = properties.getProperty("tables");
			separator = properties.getProperty("separator");
			try {
				ipDatabase = Encrypt.desencriptar(properties.getProperty("ipDatabase"));
			} catch (Exception ex) {
				ipDatabase = "";
			}
			try {
				userDatabase = Encrypt.desencriptar(properties.getProperty("userDatabase"));
			} catch (Exception ex) {
				userDatabase = "";
			}
			try {
				passwordDatabase = Encrypt.desencriptar(properties.getProperty("passwordDatabase"));
			} catch (Exception ex) {
				passwordDatabase = "";
			}
			try {
				catalogDatabase = Encrypt.desencriptar(properties.getProperty("catalogDatabase"));
			} catch (Exception ex) {
				catalogDatabase = "";
			}
		}
		else {
			NAMESPACE = "";
			URL = "";
			METHOD_NAME_SYNC_TO_CLIENT = "";
			SOAP_ACTION_SYNC_TO_CLIENT = "";
			METHOD_NAME_SYNC_TO_SERVER = "";
			SOAP_ACTION_SYNC_TO_SERVER = "";
			tables = "";
			separator = "";
			ipDatabase = "";
			userDatabase = "";
			passwordDatabase = "";
			catalogDatabase = "";
		}
		this.assetsPropertyReader = null;
		this.properties = null;
		ndb.add("android_metadata");
		ndb.add("sqlite_sequence");
	}
	
	public void setNameSpace(String NAMESPACE) {
		this.NAMESPACE = NAMESPACE;
	}
	
	public String getNameSpace() {
		return NAMESPACE;
	}
	
	public void setUrl(String URL) {
		this.URL = URL;
	}
	
	public String getUrl() {
		return URL;
	}
	
	public void setMethodNameSyncToClient(String METHOD_NAME_SYNC_TO_CLIENT) {
		this.METHOD_NAME_SYNC_TO_CLIENT = METHOD_NAME_SYNC_TO_CLIENT;
	}
	
	public String getMethodNameSyncToClient() {
		return METHOD_NAME_SYNC_TO_CLIENT;
	}
	
	public void setSoapActionSyncToClient(String SOAP_ACTION_SYNC_TO_CLIENT) {
		this.SOAP_ACTION_SYNC_TO_CLIENT = SOAP_ACTION_SYNC_TO_CLIENT;
	}
	
	public String getSoapActionSyncToClient() {
		return SOAP_ACTION_SYNC_TO_CLIENT;
	}
	
	public void setMethodNameSyncToServer(String METHOD_NAME_SYNC_TO_SERVER) {
		this.METHOD_NAME_SYNC_TO_SERVER = METHOD_NAME_SYNC_TO_SERVER;
	}
	
	public String getMethodNameSyncToServer() {
		return METHOD_NAME_SYNC_TO_SERVER;
	}
	
	public void setSoapActionSyncToServer(String SOAP_ACTION_SYNC_TO_SERVER) {
		this.SOAP_ACTION_SYNC_TO_SERVER = SOAP_ACTION_SYNC_TO_SERVER;
	}
	
	public String getSoapActionSyncToServer() {
		return SOAP_ACTION_SYNC_TO_SERVER;
	}
	
	public void setTables(String tables) {
		this.tables = tables;
	}
	
	public String getTables() {
		return this.tables;
	}
	
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	public String getSeparator() {
		return this.separator;
	}
	
	public void setIpDatabase(String ipDatabase, boolean isEncrypted) {
		if (isEncrypted) {
			try {
				this.ipDatabase = Encrypt.desencriptar(ipDatabase);
			} catch (Exception ex) {
				this.ipDatabase = ipDatabase;
			}
		}
		else {
			this.ipDatabase = ipDatabase;
		}
	}
	
	public String getIpDatabase(boolean returnEncrypted) {
		if (returnEncrypted) {
			try {
				return Encrypt.desencriptar(ipDatabase);
			} catch (Exception ex) {
				return ipDatabase;
			}
		}
		else {
			return this.ipDatabase;
		}
	}
	
	public void setUserDatabase(String userDatabase, boolean isEncrypted) {
		if (isEncrypted) {
			try {
				this.userDatabase = Encrypt.desencriptar(userDatabase);
			} catch (Exception ex) {
				this.userDatabase = userDatabase;
			}
		}
		else {
			this.userDatabase = userDatabase;
		}
	}
	
	public String getUserDatabase(boolean returnEncrypted) {
		if (returnEncrypted) {
			try {
				return Encrypt.desencriptar(userDatabase);
			} catch (Exception ex) {
				return userDatabase;
			}
		}
		else {
			return this.userDatabase;
		}
	}
	
	public void setPasswordDatabase(String passwordDatabase, boolean isEncrypted) {
		if (isEncrypted) {
			try {
				this.passwordDatabase = Encrypt.desencriptar(passwordDatabase);
			} catch (Exception ex) {
				this.passwordDatabase = passwordDatabase;
			}
		}
		else {
			this.passwordDatabase = passwordDatabase;
		}
	}
	
	public String getPasswordDatabase(boolean returnEncrypted) {
		if (returnEncrypted) {
			try {
				return Encrypt.desencriptar(passwordDatabase);
			} catch (Exception ex) {
				return passwordDatabase;
			}
		}
		else {
			return this.passwordDatabase;
		}
	}
		
	public void setCatalogDatabase(String catalogDatabase, boolean isEncrypted) {
		if (isEncrypted) {
			try {
				this.catalogDatabase = Encrypt.desencriptar(catalogDatabase);
			} catch (Exception ex) {
				this.catalogDatabase = catalogDatabase;
			}
		}
		else {
			this.catalogDatabase = catalogDatabase;
		}
	}
	
	public String getCatalogDatabase(boolean returnEncrypted) {
		if (returnEncrypted) {
			try {
				return Encrypt.desencriptar(catalogDatabase);
			} catch (Exception ex) {
				return catalogDatabase;
			}
		}
		else {
			return this.catalogDatabase;
		}
	}
	
	public void setQueriesDatabase(String queriesDatabase) {
		this.queriesDatabase = queriesDatabase;
	}
	
	public String getQueriesDatabase() {
		return queriesDatabase;
	}
	
	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}
	
	public String getClientKey() {
		return clientKey;
	}

	public void UseUrlConnection(boolean urlConnection) {
		this.urlConnection = urlConnection;
	}

	public  boolean isUrlConnection() {
		return urlConnection;
	}

	@SuppressWarnings("finally")
	public int getStatusConnection() {
		int status = Operation.CONNECTION_STATUS_BOOTH_OK;
		try {
			ConnectivityManager connManager = (ConnectivityManager) this.context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        	NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        	NetworkInfo mobiledata = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (wifi != null) {
				if (mobiledata != null) {
					if (wifi.getState() == NetworkInfo.State.CONNECTED && mobiledata.getState() == NetworkInfo.State.CONNECTED) {
						status = Operation.CONNECTION_STATUS_BOOTH_OK;
					} else if (wifi.getState() == NetworkInfo.State.CONNECTED) {
						status = Operation.CONNECTION_STATUS_WIFI_OK;
					} else if (mobiledata.getState() == NetworkInfo.State.CONNECTED) {
						status = Operation.CONNECTION_STATUS_MOBILE_OK;
					} else {
						status = Operation.CONNECTION_STATUS_DISABLED;
					}
				} else if (wifi.getState() == NetworkInfo.State.CONNECTED) {
					status = Operation.CONNECTION_STATUS_WIFI_OK;
				} else {
					status = Operation.CONNECTION_STATUS_DISABLED;
				}
			}
			else if (mobiledata != null) {
				if (mobiledata.getState() == NetworkInfo.State.CONNECTED) {
					status = Operation.CONNECTION_STATUS_MOBILE_OK;
				} else {
					status = Operation.CONNECTION_STATUS_DISABLED;
				}
			}
			else {
				status = Operation.CONNECTION_STATUS_DISABLED;
			}
		} catch (Exception ex) {
			status = Operation.CONNECTION_STATUS_ERROR_EXCEPTION;
		}
		finally {
			return status;
		}
	}
	
	public boolean isFirstSync() {
		boolean f;
		SQLiteDatabase checkDB = null;
		String path = this.context.getApplicationContext().getDatabasePath(this.catalogDatabase).getPath();
	    try {
	        checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	        checkDB.close();
	        try {
		        checkDB = SQLiteDatabase.openDatabase(path + "_Sync", null, SQLiteDatabase.OPEN_READONLY);
		        checkDB.close();
		        f = false;
		    } catch (Exception e) {
		    	f = true;
		    }
	    } catch (Exception e) {
	    	f = true;
	    }
	    return f;
	}
	
	public void Synchronize() {
		boolean firstSync = this.isFirstSync();
		if (firstSync) {
			this.context.getApplicationContext().deleteDatabase(this.catalogDatabase);
			this.context.getApplicationContext().deleteDatabase(this.catalogDatabase + "_Sync");
		}
		netServ = new NetworkTask(this.NAMESPACE, this.URL, this.METHOD_NAME_SYNC_TO_CLIENT, this.SOAP_ACTION_SYNC_TO_CLIENT, Operation.Sync_MSSQLServer_To_SQLite);
		netServ.setDatos(this.ipDatabase, this.userDatabase, this.passwordDatabase, this.catalogDatabase, this.tables, this.separator);
		netServ.UseUrlConnection(urlConnection);
		String res = "";
		try {
			res = netServ.execute().get();
		} catch (InterruptedException e) {
			res = "#E5";
		} catch (ExecutionException e) {
			res = "#E6";
		}
		netServ = null;
		if (!res.startsWith("#E")) {
			SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase, Context.MODE_PRIVATE, null);
			if (!firstSync) {
				ArrayList<String> table = null;
				if (this.tables.trim().equals("*")) {
					Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
					if (c.moveToFirst()) {
						table = new ArrayList<String>();
					    while ( !c.isAfterLast() ) {
					    	if (!ndb.contains(c.getString(0))) {
					    		table.add(c.getString(0));
					    	}
					        c.moveToNext();
					    }
					}
					c.close();
					c = null;
				}
				else {
					table = new ArrayList<String>(Arrays.asList(this.tables.split(this.separator)));
				}
				if (table != null) {
					for (int x = 0; x < table.size(); x++) {
						if (!table.get(x).trim().equals("")) {
							db.execSQL("DROP TABLE " + table.get(x));
						}
					}
					table = null;
				}
			}
			String[] query = res.split(";\n");
			for (int y = 0; y < query.length; y++) {
				try {
					query[y] = query[y].trim();
					if (!query[y].equals("") && !query[y].equals("\n")) {
						db.execSQL(query[y]);
					}
				}
				catch (Exception ex) { }
			}
			query = null;
			db.close();
			db = null;
			db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
			db.close();
			db = null;
		}
		res = null;
	}

	public void SynchronizePartial() {
		boolean firstSync = this.isFirstSync();
		if (firstSync) {
			this.context.getApplicationContext().deleteDatabase(this.catalogDatabase);
			this.context.getApplicationContext().deleteDatabase(this.catalogDatabase + "_Sync");
		}
		netServ = new NetworkTask(this.NAMESPACE, this.URL, this.METHOD_NAME_SYNC_TO_CLIENT, this.SOAP_ACTION_SYNC_TO_CLIENT, Operation.Sync_MSSQLServer_To_SQLite);
		netServ.setDatos(this.ipDatabase, this.userDatabase, this.passwordDatabase, this.catalogDatabase, this.tables, this.separator, this.queriesDatabase);
		netServ.UseUrlConnection(urlConnection);
		String res = "";
		try {
			res = netServ.execute().get();
		} catch (InterruptedException e) {
			res = "#E5";
		} catch (ExecutionException e) {
			res = "#E6";
		}
		netServ = null;
		if (!res.startsWith("#E")) {
			SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase, Context.MODE_PRIVATE, null);
			if (!firstSync) {
				ArrayList<String> table = null;
				if (this.tables.trim().equals("*")) {
					Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
					if (c.moveToFirst()) {
						table = new ArrayList<String>();
						while ( !c.isAfterLast() ) {
							if (!ndb.contains(c.getString(0))) {
								table.add(c.getString(0));
							}
							c.moveToNext();
						}
					}
					c.close();
					c = null;
				}
				else {
					table = new ArrayList<String>(Arrays.asList(this.tables.split(this.separator)));
				}
				if (table != null) {
					for (int x = 0; x < table.size(); x++) {
						if (!table.get(x).trim().equals("")) {
							db.execSQL("DROP TABLE " + table.get(x));
						}
					}
					table = null;
				}
			}
			String[] query = res.split(";\n");
			for (int y = 0; y < query.length; y++) {
				try {
					query[y] = query[y].trim();
					if (query[y].contains("True")) {
						query[y].replace("True", "1");
					}
					if (query[y].contains("False")) {
						query[y].replace("False", "0");
					}
					if (!query[y].equals("") && !query[y].equals("\n")) {
						db.execSQL(query[y]);
					}
				}
				catch (Exception ex) { }
			}
			query = null;
			db.close();
			db = null;
			db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
			db.close();
			db = null;
		}
		res = null;
	}

	public void SynchronizeByKey() {
		boolean firstSync = this.isFirstSync();
		if (firstSync) {
			this.context.getApplicationContext().deleteDatabase(this.catalogDatabase);
			this.context.getApplicationContext().deleteDatabase(this.catalogDatabase + "_Sync");
		}
		netServ = new NetworkTask(this.NAMESPACE, this.URL, this.METHOD_NAME_SYNC_TO_CLIENT, this.SOAP_ACTION_SYNC_TO_CLIENT, Operation.Sync_MSSQLServer_To_SQLite);
		netServ.setDatos(this.clientKey, this.ipDatabase, this.userDatabase, this.passwordDatabase, this.catalogDatabase, this.tables, this.separator, this.queriesDatabase);
		netServ.UseUrlConnection(urlConnection);
		String res = "";
		try {
			res = netServ.execute().get();
			if (res.equals("")) {
				res = "#E7";
			}
		} catch (InterruptedException e) {
			res = "#E5";
		} catch (ExecutionException e) {
			res = "#E6";
		}
		netServ = null;
		if (!res.startsWith("#E")) {
			SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase, Context.MODE_PRIVATE, null);
			if (!firstSync) {
				ArrayList<String> table = null;
				if (this.tables.trim().equals("*")) {
					Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
					if (c.moveToFirst()) {
						table = new ArrayList<String>();
					    while ( !c.isAfterLast() ) {
					    	if (!ndb.contains(c.getString(0))) {
					    		table.add(c.getString(0));
					    	}
					        c.moveToNext();
					    }
					}
					c.close();
					c = null;
				}
				else {
					table = new ArrayList<String>(Arrays.asList(this.tables.split(this.separator)));
				}
				if (table != null) {
					for (int x = 0; x < table.size(); x++) {
						if (!table.get(x).trim().equals("")) {
							db.execSQL("DROP TABLE " + table.get(x));
						}
					}
					table = null;
				}
			}
			String[] query = res.split(";\n");
			for (int y = 0; y < query.length; y++) {
				try {
					query[y] = query[y].trim();
					if (query[y].contains("True")) {
						query[y].replace("True", "1");
					}
					if (query[y].contains("False")) {
						query[y].replace("False", "0");
					}
					if (!query[y].equals("") && !query[y].equals("\n")) {
						db.execSQL(query[y]);
					}
				}
				catch (Exception ex) { }
			}
			query = null;
			db.close();
			db = null;
			db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
			db.close();
			db = null;
		}
		res = null;
	}
	
	public void saveInQueue(String sql) {
		//int rowsAffected = -1;
		String date = DateFormat.getDateTimeInstance().format(new Date());
		SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
		try {
	        db.execSQL("CREATE TABLE IF NOT EXISTS Queue(Id integer PRIMARY KEY AUTOINCREMENT NOT NULL,Date TEXT NOT NULL,Query TEXT NOT NULL,Database TEXT NOT NULL,State INT NOT NULL);");
			Cursor c;
			boolean f = false;
			int n = 0;
			try {
				c = db.rawQuery("SELECT Query FROM Queue WHERE Query = \"" + sql + "\" AND Database = '" + this.catalogDatabase + "' AND State = 0;", null);
				if (c != null) {
					try {
						n = c.getCount();
						f = n > 0;
					} catch (Exception ex) {
						f = false;
					} finally {
						c.close();
					}
				}
				else {
					f = false;
				}
			} catch (Exception e) {
				c = null;
				f = false;
			}
			if (!f) {
				ContentValues values = new ContentValues();
				values.put("Date", date);
				values.put("Query", sql);
				values.put("Database", this.catalogDatabase);
				values.put("State", 0);
				/*rowsAffected = (int)*/db.insert("Queue", null, values);
			}
		} catch (Exception e) {
			//rowsAffected = -1;
	    } finally {
	    	db.close();
			db = null;
	    }
	}
	
	@SuppressLint("NewApi")
	public int ExecuteNonQuery(String sql, int operation) {
		SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase, Context.MODE_PRIVATE, null);
		int rowsAffected = -1;
		db.beginTransaction();
		try {
			SQLiteStatement stm = db.compileStatement(sql);
			switch(operation) {
				case Operation.QUERY_INSERT:
					try {
						rowsAffected = (int)stm.executeInsert();
					} catch (Exception ex) {
						rowsAffected = -1;
						sql = "";
					}
					break;
				case Operation.QUERY_UPDATE_DELETE:
					try {
						rowsAffected = stm.executeUpdateDelete();
					} catch (Exception ex) {
						rowsAffected = -1;
						sql = "";
					}
					break;
				case Operation.QUERY_CREATE_DROP:
					try {
						stm.execute();
						rowsAffected = 1;
					} catch (Exception ex) {
						rowsAffected = -1;
						sql = "";
					}
					break;
				default:
					rowsAffected = -2;
					sql = "";
					break;
			}
			stm.close();
			stm = null;
	        if (rowsAffected > 0) {
	        	db.setTransactionSuccessful();
	        }
	    } catch (Exception e) {
	    	rowsAffected = -3;
	    } finally {
		    db.endTransaction();
		    db.close();
	    }
		db = null;
		if (!sql.equals("") && rowsAffected > 0) {
			saveInQueue(sql);
		}
		return rowsAffected;
	}
	
	public ArrayList<ArrayList<String>> ExecuteSelect(String sql, String[] args) {
		SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase, Context.MODE_PRIVATE, null);
		Cursor c;
		ArrayList<ArrayList<String>> rows;
		ArrayList<String> row;
		try {
			c = db.rawQuery(sql, args);
			if (c != null) {
		      	try {
		       		int tc = c.getColumnCount();
		       		c.moveToFirst();
		       		rows = new ArrayList<ArrayList<String>>();
		       		while (!c.isAfterLast()) {
		       			row = new ArrayList<String>();
		       			for (int y = 0; y < tc; y++) {
	        				row.add(c.getString(y));
	        			}
		       			if (row.size() > 0) {
		       				rows.add(row);
		       			}
		       		    c.moveToNext();
		       		    row = null;
		       		}
		      	} catch (Exception ex) {
		      		rows = null;
		        } finally {
		           	c.close();
	           	}
	       	}
			else {
				rows = null;
			}
	    } catch (Exception e) {
	    	c = null;
	    	rows = null;
	    }
		db.close();
		db = null;
		return rows;
	}
	
	@SuppressLint("NewApi")
	public void ApplyChanges() {
		String res = "";
		String ids = "";
		SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
		try {
			final Cursor cursorSync = db.rawQuery("SELECT Id, Query FROM Queue WHERE State = 0 ORDER BY Date ASC", null);
        	if (cursorSync != null) {
        		try {
        			cursorSync.moveToFirst();
        			while (!cursorSync.isAfterLast()) 
        			{
        				ids += cursorSync.getString(0) + ",";
        				res += cursorSync.getString(1) + ";";
        			    cursorSync.moveToNext();
        			}
            	} finally {
                	cursorSync.close();
            	}
        	}
		} catch (Exception e) {
			res = "";
	    } finally {
	    	db.close();
			db = null;
	    }
		if (!res.equals(""))
		{
			netServ = new NetworkTask(this.NAMESPACE, this.URL, this.METHOD_NAME_SYNC_TO_SERVER, this.SOAP_ACTION_SYNC_TO_SERVER, Operation.Sync_SQLite_To_MSSQLServer);
			netServ.setDatos(this.ipDatabase, this.userDatabase, this.passwordDatabase, this.catalogDatabase, res);
			netServ.UseUrlConnection(urlConnection);
			try {
				res = netServ.execute().get();
			} catch (InterruptedException e) {
				res = "";
			} catch (ExecutionException e) {
				res = "";
			}
			netServ = null;
			if (!res.equals("") && !res.startsWith("#E")) {
				try {
					ids = ids.substring(0, ids.length() - 1);
				} catch (Exception ex) {
					ids = "";
				}
				if (!ids.equals("")) {
					db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
					String sqlQueue = "UPDATE Queue SET State = 1 WHERE Id IN(" + ids + ")";
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
						try {
							db.execSQL(sqlQueue);
						} catch (Exception e) { }
				    } else {
				    	SQLiteStatement stmQueue = db.compileStatement(sqlQueue);
						try {
							stmQueue.executeUpdateDelete();
						} catch (Exception e) { }
						stmQueue.close();
						stmQueue= null;
				    }
					sqlQueue = null;
					db.close();
					db = null;
				}
			}
		}
		res = null;
		ids = null;
	}

	@SuppressLint("NewApi")
	public void ApplyChanges(int n) {
		String res = "";
		String ids = "";
		SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
		try {
			final Cursor cursorSync = db.rawQuery("SELECT Id, Query FROM Queue WHERE State = 0 ORDER BY Date ASC" + (n > 0 ? (" LIMIT " + n) : ""), null);
			if (cursorSync != null) {
				try {
					cursorSync.moveToFirst();
					while (!cursorSync.isAfterLast())
					{
						ids += cursorSync.getString(0) + ",";
						res += cursorSync.getString(1) + ";";
						cursorSync.moveToNext();
					}
				} finally {
					cursorSync.close();
				}
			}
		} catch (Exception e) {
			res = "";
		} finally {
			db.close();
			db = null;
		}
		if (!res.equals(""))
		{
			netServ = new NetworkTask(this.NAMESPACE, this.URL, this.METHOD_NAME_SYNC_TO_SERVER, this.SOAP_ACTION_SYNC_TO_SERVER, Operation.Sync_SQLite_To_MSSQLServer);
			netServ.setDatos(this.ipDatabase, this.userDatabase, this.passwordDatabase, this.catalogDatabase, res);
			netServ.UseUrlConnection(urlConnection);
			try {
				res = netServ.execute().get();
			} catch (InterruptedException e) {
				res = "";
			} catch (ExecutionException e) {
				res = "";
			}
			netServ = null;
			if (!res.equals("") && !res.startsWith("#E")) {
				try {
					ids = ids.substring(0, ids.length() - 1);
				} catch (Exception ex) {
					ids = "";
				}
				if (!ids.equals("")) {
					db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
					String sqlQueue = "UPDATE Queue SET State = 1 WHERE Id IN(" + ids + ")";
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
						try {
							db.execSQL(sqlQueue);
						} catch (Exception e) { }
					} else {
						SQLiteStatement stmQueue = db.compileStatement(sqlQueue);
						try {
							stmQueue.executeUpdateDelete();
						} catch (Exception e) { }
						stmQueue.close();
						stmQueue= null;
					}
					sqlQueue = null;
					db.close();
					db = null;
				}
			}
		}
		res = null;
		ids = null;
	}
	
	@SuppressLint("NewApi")
	public void clearQueue() {
		SQLiteDatabase db = this.context.getApplicationContext().openOrCreateDatabase(this.catalogDatabase + "_Sync", Context.MODE_PRIVATE, null);
		String sqlQueue = "DELETE FROM Queue WHERE State = 1";
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			try {
				db.execSQL(sqlQueue);
			} catch (Exception e) { }
	    } else {
	    	SQLiteStatement stmQueue = db.compileStatement(sqlQueue);
			try {
				stmQueue.executeUpdateDelete();
			} catch (Exception e) { }
			stmQueue.close();
			stmQueue= null;
	    }
		sqlQueue = null;
		db.close();
		db = null;
	}
}