package com.tomecode.showme.password.sqldeveloper;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

import java.util.logging.Logger;

import oracle.ide.config.Preferences;

import oracle.javatools.data.HashStructure;

import javax.naming.NamingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.ide.ExitNotAllowedException;
import oracle.ide.Ide;
import oracle.ide.ProductInformation;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
@Deprecated
public final class SQLDeveloperConnectionsParser2 {

	private static final Logger log = Logger.getLogger("SQLDeveloperConnectionsParser");

	public SQLDeveloperConnectionsParser2() {
		super();
	}

	public static final List<DBLogin> loadDbConfig() {
		// String productID = ProductInformation.getProductID();
		String path = null;
		try {
			path = (String) "";//TODO: oracle.jdevimpl.db.adapter.DefaultContextWrapper.getInstance().getDatabaseContext().getEnvironment().get("java.naming.provider.url");
		} catch (Exception e) {
			log.log(Level.FINE, e.getMessage(), e);
		}

		if (path == null || path.trim().length() == 0) {
			return new ArrayList<DBLogin>();
		}

		HashStructure prefs = Preferences.getPreferences().getProperties();
		String key = prefs.getString("db.system.id");
		if (key == null || key.trim().length() == 0) {
			return new ArrayList<DBLogin>();
		}

		Decrypter decrytor = new Decrypter(key);

		List<DBLogin> dbLogins = parseConnectionXml(path, decrytor);

		return dbLogins;
	}

	private final static List<DBLogin> parseConnectionXml(String path, Decrypter decrytor) {
		List<DBLogin> dbLogins = new ArrayList<DBLogin>();
		File connectionXml = new File(path);

		if (!connectionXml.exists()) {
			return dbLogins;
		}

		Element eReferences = parseDocument(connectionXml);
		if (eReferences != null && "References".equals(eReferences.getNodeName())) {
			NodeList list = eReferences.getElementsByTagName("Reference");
			if (list != null) {
				for (int i = 0; i <= list.getLength() - 1; i++) {
					Element element = (Element) list.item(i);
					Attr attrCN = element.getAttributeNode("className");
					if (attrCN != null) {
						if (attrCN.getValue() != null) {
							if (attrCN.getValue().contains("Database")) {
								parseDatabaseReference(element, dbLogins, decrytor);
							}
						}

					}
				}
			}
		}

		return dbLogins;
	}

	private static final void parseDatabaseReference(Element element, List<DBLogin> dbLogins, Decrypter decriptor) {
		Attr attr = element.getAttributeNode("name");
		if (attr != null) {
			String dbName = attr.getValue();

			NodeList nlRefAddresses = element.getElementsByTagName("RefAddresses");
			if (nlRefAddresses != null) {
				for (int ix = 0; ix <= nlRefAddresses.getLength() - 1; ix++) {

					try {
						Element eRefAddress = (Element) nlRefAddresses.item(ix);
						if ("RefAddresses".equals(eRefAddress.getNodeName())) {
							String user = parseValue(eRefAddress.getElementsByTagName("StringRefAddr"), "user");
							String pass = parseValue(eRefAddress.getElementsByTagName("StringRefAddr"), "password");
							String sid = parseValue(eRefAddress.getElementsByTagName("StringRefAddr"), "sid");
							String hostname = parseValue(eRefAddress.getElementsByTagName("StringRefAddr"), "hostname");
							String serviceName = parseValue(eRefAddress.getElementsByTagName("StringRefAddr"), "serviceName");

							if (hostname == null || hostname.trim().length() == 0) {
								String customUrl = parseValue(eRefAddress.getElementsByTagName("StringRefAddr"), "customUrl");
								if (customUrl != null && customUrl.trim().length() != 0) {
									int dotIndex = customUrl.lastIndexOf("@");
									if (dotIndex != -1) {
										String customUrl2 = customUrl.substring(dotIndex + 1);
										dotIndex = customUrl2.indexOf(":");
										if (dotIndex != -1) {
											hostname = customUrl2.substring(0, dotIndex);
										}

										if (sid == null || sid.trim().length() == 0) {
											int lastIndex = customUrl2.lastIndexOf(":");
											if (lastIndex != -1) {
												sid = customUrl2.substring(lastIndex + 1);
											}
										}
									}
								}
							}
							pass = decriptor.decrypt(pass);
							dbLogins.add(new DBLogin(dbName, hostname, sid, serviceName, user, pass));
						}
					} catch (Exception e) {
						log.log(Level.FINE, e.getMessage(), e);
					}

				}
			}
		}
	}

	private final static Element parseDocument(File connectionXml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(connectionXml);
			return dom.getDocumentElement();
		} catch (Exception e) {
			log.log(Level.FINE, e.getMessage(), e);

		}
		return null;
	}

	private final static String parseValue(NodeList list, String attributeName) {
		for (int i = 0; i <= list.getLength() - 1; i++) {
			Element e = (Element) list.item(i);
			if ("StringRefAddr".equals(e.getNodeName())) {
				if (attributeName.equals(e.getAttribute("addrType"))) {
					NodeList eContents = e.getElementsByTagName("Contents");
					if (eContents != null && eContents.getLength() != 0) {
						return eContents.item(0).getTextContent();
					}
				}
			}
		}

		return null;
	}

}
