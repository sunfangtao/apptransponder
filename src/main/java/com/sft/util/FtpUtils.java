package com.sft.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FtpUtils {

    public static void main(String[] args) {
        System.out.println(new ChangeToPinYin().getStringPinYin("金融"));
    }

    private static Logger logger = Logger.getLogger(FtpUtils.class);

    private FTPClient ftpClient = null;
    private String url = null;
    private int port = -1;
    private String username = null;
    private String password = null;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 构造函数
     */
    public FtpUtils() {
        if (this.url == null) {
            this.url = "192.168.17.106";
            this.username = "ftpname";
            this.password = "ftpname";
            this.port = 21;
        }
        ftpClient = new FTPClient();
    }

    /**
     * 连接ftp服务器
     *
     * @return Boolean 连接是否成功，<b>null</b> - 异常
     */
    public boolean conn() {
        try {
            ftpClient.connect(url, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                close();
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            close();
            return false;
        }
    }

    /**
     * 断开ftp服务器
     */
    public void close() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {

        }
    }

    /**
     * ftp上传单个文件
     *
     * @param directory 上传至ftp的路径名不包括ftp地址
     * @param srcFile   要上传的文件
     * @param destName  上传至ftp后存储的文件名
     * @throws IOException
     */
    public boolean upload(String directory, MultipartFile srcFile, String destName) throws IOException {
        InputStream fis = null;
        try {
            fis = srcFile.getInputStream();
            if (conn()) {
                // 设置上传目录
                if (!StringUtils.hasText(directory)) {
                    directory = File.separator;
                }
                ftpClient.changeWorkingDirectory(directory);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("gbk");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                return ftpClient.storeFile(destName, fis);
            }
            return false;
        } catch (NumberFormatException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } finally {
            close();
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * @param directory 要删除的文件所在ftp的路径名不包含ftp地址
     * @param fileName  要删除的文件名
     * @return
     * @throws IOException
     */
    public boolean remove(String directory, String fileName) throws IOException {
        try {
            if (conn()) {
                ftpClient.changeWorkingDirectory(directory);
                return ftpClient.deleteFile(fileName);// 删除远程文件
            }
            return false;
        } catch (NumberFormatException e) {
            throw e;
        } catch (IOException e) {
            throw new IOException();
        } finally {
            close();
        }
    }

    /**
     * 递归创建远程服务器目录
     *
     * @param remote 远程服务器文件绝对路径
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean makeDirecrotys(String remote, String split) throws IOException {
        try {
            if (!StringUtils.hasText(remote)) {
                return false;
            }
            if (!StringUtils.hasText(split)) {
                split = "/";
            }
            if (conn()) {
                String parentName = getCurPath();
                String[] directory = remote.split(split);
                for (int i = 0; i < directory.length; i++) {
                    String finalName = parentName;
                    for (int j = 0; j <= i; j++) {
                        finalName += ("/" + directory[j]);
                    }
                    if (!ftpClient.changeWorkingDirectory(finalName)) {
                        ftpClient.makeDirectory(finalName);
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean isExistFile(String fileName) {
        try {
            if (conn()) {
                FTPFile[] files = ftpClient.listFiles("ftp/webapps/ROOT/" + fileName.substring(fileName.lastIndexOf("ftp/")));
                return files.length > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public static boolean isExistFiles(String... fileNames) {
        if (fileNames.length == 0) {
            return false;
        }
        boolean isExist = true;
        FtpUtils ftpUtil = new FtpUtils();
        for (int i = 0; i < fileNames.length; i++) {
            isExist &= ftpUtil.isExistFile(fileNames[i]);
        }
        return isExist;
    }

    public String getCurPath() {
        try {
            String[] rt = ftpClient.doCommandAsStrings("pwd", "");
            Pattern p = Pattern.compile("\"(.*?)\"");
            Matcher m = p.matcher(rt[0]);
            return m.group(0).replace("\"", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/data/ftp/ftp/webapps/ROOT/ftp/";
    }
}