package com.tomecode.showme.password.sqldeveloper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import oracle.jdeveloper.db.ConnectionException;
import oracle.jdeveloper.db.DatabaseConnectionStores;
import oracle.jdeveloper.db.DatabaseConnections;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
public final class SQLDeveloperConnectionsParser {

	private static final Logger log = Logger.getLogger("SQLDeveloperConnectionsParser");

	public SQLDeveloperConnectionsParser() {
		super();
	}

	public static final List<DBLogin> loadDbConfig() {
		List<DBLogin> dbLogins = new ArrayList<>();

		// System.out.println("stores: " +
		// DatabaseConnectionStores.getInstance().listStores());

		DatabaseConnections dbs = DatabaseConnectionStores.getInstance().getStore("IdeConnections");
		// System.out.println("dbs: " + dbs);
		// System.out.println(dbs.listConnections());
		if (dbs != null) {
			if (dbs.listConnections() != null) {
				for (String connectionName : dbs.listConnections()) {

					String hostname = "";
					String dbSid = "";
					String serviceName = "";
					String user = "";
					String pass = "";

					try {
						Properties p = dbs.getProperties(connectionName);
						if (p != null) {
							hostname = (String) p.getProperty("hostname");
							pass = (String) p.get("password");
							dbSid = (String) p.get("sid");
							user = (String) p.get("user");
							serviceName = (String) p.get("serviceName");
							// System.out.println("password: " + p.get("password"));
							// System.out.println("SavePassword: " + p.get("SavePassword"));
							// for (Entry<Object, Object> e : p.entrySet()) {
							// System.out.println("p: " + e.getKey() + " v: " + e.getValue());
							// }
						}
						DBLogin dbLogin = new DBLogin(connectionName, hostname, dbSid, serviceName, user, pass);
						dbLogins.add(dbLogin);
					} catch (ConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return dbLogins;
	}

}
