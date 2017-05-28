package com.DatabaseSync;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import android.content.Context;
import android.content.res.AssetManager;

public class AssetsPropertyReader {
	private Context context;

    public AssetsPropertyReader(Context context) {
    	this.context = context;
    }

    public Properties getProperties(String FileName) {
    	Properties properties = new Properties();
    	try {
    		AssetManager assetManager = this.context.getAssets();
    		InputStream inputStream = assetManager.open(FileName);
    		properties.load(inputStream);
    	} catch (IOException e) {
    		properties = null;
    	}
    	return properties;
    }
}