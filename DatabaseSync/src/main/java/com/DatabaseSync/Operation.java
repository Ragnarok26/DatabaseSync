package com.DatabaseSync;

public class Operation {
	public static final int Sync_MSSQLServer_To_SQLite = 0;
	public static final int Sync_SQLite_To_MSSQLServer = 1;
	public static final int CONNECTION_STATUS_BOOTH_OK = 0;
	public static final int CONNECTION_STATUS_WIFI_OK = 1;
	public static final int CONNECTION_STATUS_MOBILE_OK = 2;
	public static final int CONNECTION_STATUS_DISABLED = -1;
	public static final int CONNECTION_STATUS_ERROR_EXCEPTION = -2;
	public static final int QUERY_INSERT = 0;
	public static final int QUERY_UPDATE_DELETE = 1;
	public static final int QUERY_CREATE_DROP = 2;
}
