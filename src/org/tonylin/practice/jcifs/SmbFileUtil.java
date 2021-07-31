package org.tonylin.practice.jcifs;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class SmbFileUtil {
	
	private static NtlmPasswordAuthentication createAuth(String aDomain, String aUser, String aPasswd){
		if( aDomain != null )
			return new NtlmPasswordAuthentication(aDomain, aUser, aPasswd);
		StringBuffer sb = new StringBuffer(aUser); 
		sb.append(':').append(aPasswd);
		return new NtlmPasswordAuthentication(sb.toString());
	}
	
	public static SmbFile createSmbFile(String aDomain, String aUser, String aPasswd, String aTarget) throws IOException {
		NtlmPasswordAuthentication auth = createAuth(aDomain, aUser, aPasswd);
		return new SmbFile(aTarget, auth);
	}
	
	private static void close(Closeable aCloseable){
		if( aCloseable == null )
			return;
		
		try {
			aCloseable.close();
		} catch (IOException e) {
			//  log exception
		}
	}
	
	public static void copyFileFrom(String aUser, String aPasswd, String aSource, String aTarget) throws IOException {
		copyFileFrom(null, aUser, aPasswd, aSource, aTarget);	
	}
	
	public static void copyFileFrom(String aDomain, String aUser, String aPasswd, String aSource, String aTarget) throws IOException {
		SmbFile sFile = createSmbFile(aDomain, aUser, aPasswd, aSource);
		SmbFileInputStream sfis = null;
		FileOutputStream fos = null;
		try {
			sfis = new SmbFileInputStream(sFile);
			fos = new FileOutputStream(new File(aTarget));

			byte[] buf = new byte[1024];
			int len;
			while(( len = sfis.read(buf) )> 0 ){
				fos.write(buf, 0, len);
			}
		} finally {
			close(sfis);
			close(fos);
		}
	}
	
	public static boolean exists(String aDomain, String aUser, String aPasswd, String aTarget) throws IOException {
		SmbFile sFile = createSmbFile(aDomain, aUser, aPasswd, aTarget);
		return sFile.exists();
	}
	
	public static boolean exists(String aUser, String aPasswd, String aTarget) throws IOException {
		return exists(null, aUser, aPasswd, aTarget);
	}
	
	public static void copyFileTo(String aUser, String aPasswd, String aSource, String aTarget) throws IOException {
		copyFileTo(null, aUser, aPasswd, aSource, aTarget);
	}
	
	public static void copyFileTo(String aDomain, String aUser, String aPasswd, String aSource, String aTarget) throws IOException {
		SmbFile sFile = createSmbFile(aDomain, aUser, aPasswd, aTarget);
		SmbFileOutputStream sfos = null;
		FileInputStream fis = null;
		try {
			sfos = new SmbFileOutputStream(sFile);
			fis = new FileInputStream(new File(aSource));

			byte[] buf = new byte[1024];
			int len;
			while(( len = fis.read(buf) )> 0 ){
				sfos.write(buf, 0, len);
			}
		} finally {
			close(sfos);
			close(fis);
		}
	}
	
	public static void deleteFile(String aDomain, String aUser, String aPasswd, String aTarget) throws IOException {
		SmbFile sFile = createSmbFile(aDomain, aUser, aPasswd, aTarget);
		sFile.delete();
	}

	public static void deleteFile(String aUser, String aPasswd, String aTarget) throws IOException {
		deleteFile(null, aUser, aPasswd, aTarget);
	}
	
	public static void createFile(String aDomain, String aUser, String aPasswd, String aTarget, String aContent) throws IOException {
		SmbFile sFile = createSmbFile(aDomain, aUser, aPasswd, aTarget);
		SmbFileOutputStream sfos = null;
		try {
			sfos = new SmbFileOutputStream(sFile);
			sfos.write(aContent.getBytes());
		} finally {
			close(sfos);
		}
	}
	
	public static void createFile(String aUser, String aPasswd, String aTarget, String aContent) throws IOException {
		createFile(null, aUser, aPasswd, aTarget, aContent);
	}
	
}
