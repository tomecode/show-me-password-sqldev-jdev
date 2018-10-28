package com.tomecode.showme.password.sqldeveloper;


/**
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 */
public final class DBLogin {

    private final String dbName;

    private final String hostName;
    
    private final String dbSid;

    private final String serviceName;

    private final String user;

    private final String pass;
    
    

    public DBLogin(String dbName,String hostName,String dbSid, String serviceName, String user, String pass) {
        this.dbName = (dbName==null?"": dbName);
        this.hostName= (hostName==null?"" :hostName);
        this.serviceName = (serviceName==null?"":serviceName);
        this.dbSid =(dbSid==null ? "": dbSid);
        this.user = (user==null ? "": user);
        this.pass = (pass==null ? "": pass);
    }

    public final String getHostName(){
      return hostName;
    }
    public final String getDbSid(){
      return dbSid;
    }

    public final String getUser() {
        return user;
    }

    public final String getPass() {
        return pass;
    }

    public final String getDbName() {
        return dbName;
    }

    public final String[] toArray() {
        return new String[] { dbName,hostName,dbSid,serviceName, user,pass };
    }

}
