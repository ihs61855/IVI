package com.example.hansol.sosohanhan.utility;


import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class FTPUtils {
    private final String TAG = "Connect FTP";
    public FTPClient mFTPClient;

    public FTPUtils() {
        mFTPClient = new FTPClient();
        mFTPClient.setControlEncoding("euc-kr");
    }

    //Connect
    public void ftpConnect(String host, String username, String password, int port) {
        boolean result = false;
        try{
            mFTPClient.connect(host, port);
            if(FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                result = mFTPClient.login(username, password);
                mFTPClient.enterLocalPassiveMode();
            }
        }catch (Exception e){
            Log.d(TAG, "Couldn't connect to host");
        }
    }

    //Disconnect
    public void ftpDisconnect() {
        boolean result = false;
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "Failed to disconnect with server");
        }
    }

    //현재 작업 경로 가져오기
    public String ftpGetDirectory(){
        String directory = null;
        try{
            directory = mFTPClient.printWorkingDirectory();
        } catch (Exception e){
            Log.d(TAG, "Couldn't get current directory");
        }
        return directory;
    }

    //작업 경로 수정
    private boolean ftpChangeDirctory(String directory) {
        try{
            mFTPClient.changeWorkingDirectory(directory);
            return true;
        }catch (Exception e){
            Log.d(TAG, "Couldn't change the directory");
        }
        return false;
    }

    //Directory 내 파일 리스트 가져오기
    public String[] ftpGetFileList(String directory) {
        String[] fileList = null;
        int i = 0;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(directory);
            fileList = new String[ftpFiles.length];
            for(FTPFile file : ftpFiles) {
                String fileName = file.getName();

                if (file.isFile()) {
                    fileList[i] = "(File) " + fileName;
                } else {
                    fileList[i] = "(Directory) " + fileName;
                }

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    //새로운 directory 생성
    public boolean ftpCreateDirectory(String directory) {
        boolean result = false;
        try {
            result =  mFTPClient.makeDirectory(directory);
        } catch (Exception e){
            Log.d(TAG, "Couldn't make the directory");
        }
        return result;
    }

    //directory 삭제
    public boolean ftpDeleteDirectory(String directory) {
        boolean result = false;
        try {
            result = mFTPClient.removeDirectory(directory);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't remove directory");
        }
        return result;
    }

    //파일 삭제
    public boolean ftpDeleteFile(String file) {
        boolean result = false;
        try{
            result = mFTPClient.deleteFile(file);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't remove the file");
        }
        return result;
    }

    //파일이름 변경
    public boolean ftpRenameFile(String from, String to) {
        boolean result = false;
        try {
            result = mFTPClient.rename(from, to);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't rename file");
        }
        return result;
    }

    //파일 다운로드
    public boolean ftpDownloadFile(String srcFilePath, String desFilePath) {
        boolean result = false;
        try{
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            FileOutputStream fos = new FileOutputStream(desFilePath);
            result = mFTPClient.retrieveFile(srcFilePath, fos);
            fos.close();
        } catch (Exception e){
            Log.d(TAG, "Download failed");
        }
        return result;
    }

    //FTP서버에 파일 업로드
    public boolean ftpUploadFile(String srcFilePath, String desFileName, String desDirectory) {
        boolean result = false;
        try {
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE); // 바이너리 파일
            FileInputStream fis = new FileInputStream(srcFilePath);
            if(ftpChangeDirctory(desDirectory)) {
                result = mFTPClient.storeFile(desFileName, fis);
            }
            fis.close();
        } catch(Exception e){
            Log.d(TAG, "Couldn't upload the file");
        }
        return result;
    }



}
