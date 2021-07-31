package org.tonylin.practice.jcifs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class SmbFileUtilTest {

	private String targetFolder = "smb://192.168.1.25/admin$/";
	private String user = "tonylin";
	private String passwd = "xxxxx";
	
	private String remoteTmpFile = null;
	private String localTmpFile = null;
	
	@After
	public void tearDown() throws IOException{
		if( remoteTmpFile != null ){
			SmbFileUtil.deleteFile(user, passwd, remoteTmpFile);
			assertFalse(SmbFileUtil.exists(user, passwd, remoteTmpFile));
		}
		if( localTmpFile != null ){
			new File(localTmpFile).delete();
		}
	}
	
	@Test
	public void copyFile() throws IOException{
		remoteTmpFile = targetFolder+"temp.txt";
		localTmpFile = "temp.tmp";
		SmbFileUtil.copyFileTo(user, passwd, "libs/jcifs-1.3.18.jar", remoteTmpFile);
		assertTrue(SmbFileUtil.exists(user, passwd, remoteTmpFile));
		
		SmbFileUtil.copyFileFrom(user, passwd, remoteTmpFile,  localTmpFile);
		File localFile = new File(localTmpFile);
		assertTrue(localFile.exists());
		
		assertEquals(new File("libs/jcifs-1.3.18.jar").length(), localFile.length());
	}
	
	@Test
	public void createFile() throws IOException{
		remoteTmpFile = targetFolder+"temp.txt";
		SmbFileUtil.createFile(user, passwd , remoteTmpFile, "test");
		assertTrue(SmbFileUtil.exists(user, passwd, remoteTmpFile));
	}
}
