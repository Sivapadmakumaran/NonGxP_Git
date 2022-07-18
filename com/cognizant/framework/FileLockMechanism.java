package com.cognizant.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.*;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class FileLockMechanism {
	
	long lngTimeOutInSeconds;
	
	public  FileLockMechanism(long lngTimeOutInSeconds) {
		this.lngTimeOutInSeconds = lngTimeOutInSeconds;
	}

	@SuppressWarnings("resource")
	public FileLock SetLockOnFile(String FileName)
	{
		File objFolderFileLock = new File( System.getProperty("user.dir")+Util.getFileSeparator()+"Lock_Files");
		if(!objFolderFileLock.exists()) {
			objFolderFileLock.mkdir();
		}
		File objLockFile = new File(objFolderFileLock.getAbsolutePath()+Util.getFileSeparator()+FileName+".xls");
		if(!objLockFile.exists()) {
			HSSFWorkbook workbook = new HSSFWorkbook();
			String absoluteFilePath = objLockFile.getAbsolutePath();
			FileOutputStream fileOutputStream;
			try {
				fileOutputStream = new FileOutputStream(absoluteFilePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new FrameworkException("The specified file \""
						+ absoluteFilePath + "\" does not exist!");
			}
			try {
				workbook.write(fileOutputStream);
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new FrameworkException(
					"Error while creating file lock temporary Excel workbook \""
							+ absoluteFilePath + "\"");
			}
		}
		FileChannel objFileChannel;
		FileLock objLock = null;
		int intCounter = 0;
		CounterLoop:
		while(intCounter<=lngTimeOutInSeconds) {
			if(objLock!=null) {
				break CounterLoop;
			}
			while(objLock==null) {
				try {
					objFileChannel = new RandomAccessFile(objLockFile.getAbsolutePath(), "rw").getChannel();
					objLock = objFileChannel.lock();
					break CounterLoop;
				} catch(OverlappingFileLockException | IOException e) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException IE) {
						e.printStackTrace();
					}
					intCounter++;
				}
			}
		}
		if(objLock!=null) {
			return objLock;
		}
		else
		{
			throw new FrameworkException("Error","Unable to Set Lock on the file "+objLockFile.getAbsolutePath());
		}
	}
	
	public void ReleaseLockOnFile(FileLock objFileLock,String FileName)
	{
		File objFolderFileLock = new File( System.getProperty("user.dir")+Util.getFileSeparator()+"Lock_Files");
		String strLockFilePath =  objFolderFileLock.getAbsolutePath()+Util.getFileSeparator()+FileName+".xls";
		if(objFileLock!=null)
		{
			try {
				FileChannel objFileChannel = objFileLock.channel();
				objFileLock.close();
				objFileChannel.close();
			} catch (IOException e) {
				throw new FrameworkException("Error","Unable to Release Lock on the file "+strLockFilePath);
			}
		}
	}
}


