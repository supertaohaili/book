package com.thl.book;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtils {

	public static final String SP_NAME = "config";
	
	public static void saveBoolean(Context context,String key , boolean value){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
		 
	}
	public static void saveString(Context context,String key , String value){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(Context context,String key,String defValue){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
		
	}
	public static void saveLong(Context context,String key , long value){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).commit();
	}
	
	public static long getLong(Context context,String key,long defValue){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getLong(key, defValue);
		
	}
	public static void saveInt(Context context,String key , int value){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	
	public static int getInt(Context context,String key,int defValue){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	public static void clearPreferences(Context context) {
		context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().clear().commit();
	}
}
