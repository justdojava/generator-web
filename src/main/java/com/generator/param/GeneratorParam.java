package com.generator.param;

/**
 * Created by .JA
 * Company 北京众信金融
 */
public class GeneratorParam {
	
	private String connection;

    private String dataBase;
    
    private String port;
    
    private String userId;
    
    private String userPass;
    
    private String modelPath;
    
    private String mappingPath;
    
    private String daoPath;
    
    private String buildPath;
    
    private String[] tableNames;
    
    private String[] modelNames;
    
    private String isHump;

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getModelPath() {
		return modelPath;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

	public String getMappingPath() {
		return mappingPath;
	}

	public void setMappingPath(String mappingPath) {
		this.mappingPath = mappingPath;
	}

	public String getDaoPath() {
		return daoPath;
	}

	public void setDaoPath(String daoPath) {
		this.daoPath = daoPath;
	}

	public String[] getTableNames() {
		return tableNames;
	}

	public void setTableNames(String[] tableNames) {
		this.tableNames = tableNames;
	}

	public String[] getModelNames() {
		return modelNames;
	}

	public void setModelNames(String[] modelNames) {
		this.modelNames = modelNames;
	}

	public String getBuildPath() {
		return buildPath;
	}

	public void setBuildPath(String buildPath) {
		this.buildPath = buildPath;
	}

	public String getIsHump() {
		return isHump;
	}

	public void setIsHump(String isHump) {
		this.isHump = isHump;
	}
    
}