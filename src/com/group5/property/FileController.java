package com.group5.property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import android.content.Context;

public class FileController {
	
	private static Context context;
	private static String pathFiles;
	public FileController(){
		
	}
	
	public static void setContext(Context context){
		FileController.context = context;
		pathFiles = context.getFilesDir().getAbsolutePath()+"/";
	}
	
	@SuppressWarnings("resource")
	public static String readFile(String filename){
		String path = pathFiles + filename;
		File file = new File(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String buf = null;
			StringBuilder sb = new StringBuilder();
			while((buf = br.readLine())!=null){
				sb.append(buf);
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static File getFile(String filename){
		String path = pathFiles+filename;
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}
	
}
