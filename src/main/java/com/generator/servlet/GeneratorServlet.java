package com.generator.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.generator.param.GeneratorParam;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;



/**
 * Created by .JA
 * Company 北京众信金融
 */
public class GeneratorServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int BUFFER = 8192;

    /**
     * Constructor of the object.
     */
    public GeneratorServlet() {
        super();
    }
    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        super.init();
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	try {
    		GeneratorParam param = new GeneratorParam();
    		param.setConnection(request.getParameter("connection"));
    		param.setPort(request.getParameter("port"));
    		param.setDataBase(request.getParameter("dataBase"));
    		param.setUserId(request.getParameter("userId"));
    		param.setUserPass(request.getParameter("userPass"));
    		param.setModelPath(request.getParameter("modelPath"));
    		param.setDaoPath(request.getParameter("daoPath"));
    		param.setMappingPath(request.getParameter("mappingPath"));
    		param.setTableNames(request.getParameterValues("tableNames"));
    		param.setModelNames(request.getParameterValues("modelNames"));
    		param.setIsHump(request.getParameter("isHump"));
    		// 信息缓存
    		List<String> warnings = new ArrayList<String>();
    		// 覆盖已有的重名文件
    		boolean overwrite = true;
    		// 准备 配置文件
    		final String path = request.getSession().getServletContext().getRealPath(File.separator);
    		final String srcPath  = "/src" + new Date().getTime();
    		
    		param.setBuildPath(path + srcPath);
    		String config_path = "/WEB-INF/classes/runtimecfg/generatorConfig.xml";
    		File configFile = new File(path + config_path);
    		// 1.创建 配置解析器
    		ConfigurationParser parser = new ConfigurationParser(warnings);
    		// 2.获取 配置信息
    		Configuration config = parser.parseConfiguration(configFile);
    		fixConfig(config, param);/** 封装参数*/
    		// 3.创建 默认命令解释调回器
    		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
    		// 4.创建 mybatis的生成器
    		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
    		// 5.执行，关闭生成器
    		String result = "000000";
    		try {
    			myBatisGenerator.generate(null);
    		} catch (CommunicationsException e) {
    			result = "000001";
    		} catch (ConnectException e) {
    			result = "000002";
    		} catch (MySQLSyntaxErrorException e) {
    			result = "000003";
    		} catch (SQLException e) {
    			result = "000004";
    		} catch (Exception e) {
    			result = "000005";
    		}
    		this.fileToZip(param.getBuildPath(), path + "/tmp", srcPath);/** 打包操作*/
    		this.responseJson(response, result, srcPath + ".zip");
    		new Thread(new Runnable() { /** 执行完毕后删除冗余文件*/
    		    @Override
    		    public void run() {
    		    	try {
    					Thread.sleep(60000);
    					File dirFile = new File(path + srcPath);
    					File zipFile = new File(path + "/tmp" + "/" + srcPath +".zip");
    					deleteDir(dirFile);
    					deleteDir(zipFile);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    		    }
    		}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
    }
    
    protected void responseJson(HttpServletResponse response,String responseCode,String zipName) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write("{\"rspCode\":\"" + responseCode + "\",\"zipName\":\"" + zipName + "\"}");
		out.flush();
	}
    
    public void fixConfig(Configuration config, GeneratorParam param){
		File dirFile = new File(param.getBuildPath());
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		Context context = config.getContexts().get(0);
		//配置数据库属性
		JDBCConnectionConfiguration jdbcConnectionConfiguration = context.getJdbcConnectionConfiguration();
		String connection = "jdbc:mysql://" + param.getConnection() + ":" + param.getPort() + "/" + param.getDataBase();
		jdbcConnectionConfiguration.setConnectionURL(connection);
		jdbcConnectionConfiguration.setUserId(param.getUserId());
		jdbcConnectionConfiguration.setPassword(param.getUserPass());
		//配置模型的包名
		JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
		javaModelGeneratorConfiguration.setTargetPackage(param.getModelPath());
		javaModelGeneratorConfiguration.setTargetProject(param.getBuildPath());
		//DAO的包名
		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
		javaClientGeneratorConfiguration.setTargetPackage(param.getDaoPath());
		javaClientGeneratorConfiguration.setTargetProject(param.getBuildPath());
		//映射文件的包名
		SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = context.getSqlMapGeneratorConfiguration();
		sqlMapGeneratorConfiguration.setTargetPackage(param.getMappingPath());
		sqlMapGeneratorConfiguration.setTargetProject(param.getBuildPath());
		//表集合
		List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
		tableConfigurations.clear();
		for(int i = 0;i<param.getTableNames().length;i++){
			if(StringUtils.isNotBlank(param.getTableNames()[i]) && StringUtils.isNotBlank(param.getModelNames()[i])){
				TableConfiguration tableConfiguration = new TableConfiguration(context);
				tableConfiguration.setTableName(param.getTableNames()[i]);
				tableConfiguration.setDomainObjectName(param.getModelNames()[i]);
				tableConfiguration.setCountByExampleStatementEnabled(false);
				tableConfiguration.setDeleteByExampleStatementEnabled(false);
				tableConfiguration.setSelectByExampleStatementEnabled(false);
				tableConfiguration.setUpdateByExampleStatementEnabled(false);
				//模型是否驼峰命名，为0则为驼峰
				if(param.getIsHump().equals("0"))
					tableConfiguration.getProperties().setProperty("useActualColumnNames", "true");
				tableConfigurations.add(tableConfiguration);
			}
		}
	}
    
    /** 
     * 执行压缩操作 
     * @param srcPathName 被压缩的文件/文件夹 
     */  
    public boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {    
    	boolean flag = false;
        File file = new File(sourceFilePath);    
        if (!file.exists()){  
            throw new RuntimeException(sourceFilePath + "不存在！");    
        }  
        try {    
        	File baseZipPath = new File(zipFilePath);
        	if(!baseZipPath.exists()){
        		baseZipPath.mkdirs();
    		}
        	File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);    
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());    
            ZipOutputStream out = new ZipOutputStream(cos);    
            String basedir = "";    
            compressByType(file, out, basedir);    
            out.close();    
            flag = true;
        } catch (Exception e) {   
            e.printStackTrace();  
            throw new RuntimeException(e);    
        }    
        return flag;
    }
    
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
    /** 
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法 
     * @param file  
     * @param out 
     * @param basedir 
     */  
    private void compressByType(File file, ZipOutputStream out, String basedir) {    
        /* 判断是目录还是文件 */    
        if (file.isDirectory()) {    
            compressDirectory(file, out, basedir);    
        } else {    
            compressFile(file, out, basedir);    
        }    
    }
    
    /** 
     * 压缩一个目录 
     * @param dir 
     * @param out 
     * @param basedir 
     */  
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {    
        if (!dir.exists()){  
             return;    
        }  
             
        File[] files = dir.listFiles();    
        for (int i = 0; i < files.length; i++) {    
            /* 递归 */    
            compressByType(files[i], out, basedir + dir.getName() + "/");    
        }
    }
    
    /** 
     * 压缩一个文件 
     * @param file 
     * @param out 
     * @param basedir 
     */  
    private void compressFile(File file, ZipOutputStream out, String basedir) {    
        if (!file.exists()) {    
            return;    
        }    
        try {
        	String[] arr = basedir.split("/");
        	String dirStr = "src/";
        	if(arr[0].contains("src")){
        		for(int i = 1;i < arr.length; i++){
        			dirStr  +=arr[i] + "/"; 
        		}
        	}
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));    
            ZipEntry entry = new ZipEntry(dirStr + file.getName());    
            out.putNextEntry(entry);    
            int count;    
            byte data[] = new byte[BUFFER];    
            while ((count = bis.read(data, 0, BUFFER)) != -1) {    
                out.write(data, 0, count);    
            }    
            bis.close();    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }
    }
}
