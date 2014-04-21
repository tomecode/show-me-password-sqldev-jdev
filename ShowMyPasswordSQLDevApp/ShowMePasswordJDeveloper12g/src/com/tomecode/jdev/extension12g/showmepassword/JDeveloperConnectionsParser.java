package com.tomecode.jdev.extension12g.showmepassword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.security.jps.internal.credstore.ssp.SspCredentialStoreProvider;
import oracle.security.jps.service.credstore.Credential;
import oracle.security.jps.service.credstore.CredentialMap;
import oracle.security.jps.service.credstore.CredentialStore;
import oracle.security.jps.service.credstore.GenericCredential;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import oracle.ide.Context;
//import oracle.ide.controller.Controller;
//import oracle.ide.controller.IdeAction;
//import oracle.ide.extension.RegisteredByExtension;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
public final class JDeveloperConnectionsParser {

	private final List<Login> logins;

	public JDeveloperConnectionsParser() {
		this.logins = new ArrayList<Login>();
	}

	public final List<Login> parseConnections(Properties properties) throws Exception {
		File jdevConnFolder = findJdeveloperConnectionFolder(properties);

		File connXml = new File(jdevConnFolder.getPath() + File.separator + "connections" + File.separator + "connections.xml");

		parseConnectionXml(connXml);

		File walletFile = new File(jdevConnFolder.getPath() + File.separator + "cwallet.sso");
		File jpsFile = new File(jdevConnFolder.getPath() + File.separator + "rescat-jps-config.xml");

		if (walletFile.exists()) {
			parseWallet(jpsFile, walletFile);
		}
		return logins;
	}

	private final void parseWallet(File jpsFile, File walletFile) {
		FileInputStream fisWallet = null;

		try {

			System.setProperty("oracle.security.jps.config", walletFile.getPath());

			fisWallet = new FileInputStream(walletFile);
			SspCredentialStoreProvider credentialStoreProvider = new SspCredentialStoreProvider();
			CredentialStore store = credentialStoreProvider.getInstance("", new Hashtable<String, String>(), fisWallet);

			for (String map : store.getMapNames()) {

				CredentialMap credentialMap = store.getCredentialMap(map);

				if (credentialMap != null) {

					for (String key : credentialMap.keySet()) {

						String[] k = key.split("#");
						if (k.length != 2) {
							continue;
						}
						String credentialStoreKey = k[1];
						Login login = findLogin(credentialStoreKey);
						if (login != null) {

							Credential credential = credentialMap.getCredential(key);
							if (credential instanceof GenericCredential) {
								GenericCredential genericCredential = (GenericCredential) credential;
								//
								if (genericCredential.getCredential() instanceof Hashtable) {
									Hashtable<?, ?> data = (Hashtable<?, ?>) genericCredential.getCredential();
									Object d = data.get(login.getPasswordKey());
									if (d instanceof char[]) {
										login.setPass(new String((char[]) d));
									} else {
										login.setPass(d.toString());
									}
								}

							}
						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fisWallet != null) {
				try {
					fisWallet.close();
				} catch (Exception eee) {

				}

			}
		}

	}

	private final Login findLogin(String credentialStoreKey) {
		for (Login login : this.logins) {
			if (login.getCredentialStoreKey().equalsIgnoreCase(credentialStoreKey)) {
				return login;
			}
		}
		return null;
	}

	/**
	 * parse connections from XML
	 * 
	 * @param connectionXml
	 * @return
	 */
	private final void parseConnectionXml(File connectionXml) {
		if (connectionXml != null && connectionXml.exists()) {

			Element eReferences = parseDocument(connectionXml);
			if (eReferences != null && "References".equals(eReferences.getNodeName())) {
				NodeList list = eReferences.getElementsByTagName("Reference");
				if (list != null) {
					for (int i = 0; i <= list.getLength() - 1; i++) {
						Element element = (Element) list.item(i);

						String providerType = getAttrVal(element, "className");
						String credentialStoreKey = getAttrVal(element, "credentialStoreKey");

						if (credentialStoreKey != null && credentialStoreKey.trim().length() != 0) {

							// bpm
							if ("oracle.bpm.fusion.studio.mds.resourcepalette.BPMConnectionProvider".equalsIgnoreCase(providerType)) {
							  Login login = new Login(credentialStoreKey, //
							                  parseSecureRefAddr(element), "BPM Connection", //
							                  getAttrVal(element, "name"), //
							                  parseRefAddr2(element, "StringRefAddr", "user"));
								this.logins.add(login);
							} else

							// mds
							if ("oracle.tip.tools.ide.common.resourcepalette.adapter.mds.MDSProvider".equalsIgnoreCase(providerType)) {
							  Login login = new Login(credentialStoreKey, //
							                  parseSecureRefAddr(element), "MDS Connection", //
							                  getAttrVal(element, "name"), //
							                  parseRefAddr2(element, "StringRefAddr", "DBMDS_JDBC_USERID"));
								this.logins.add(login);
							} else

							// WSIL
							if ("oracle.jdeveloper.rcwsiladapter.WSILProvider".equalsIgnoreCase(providerType)) {

								Login login = new Login(credentialStoreKey, //
										parseSecureRefAddr(element), "WSIL Connection", //
										getAttrVal(element, "name"), //
										parseRefAddr2(element, "StringRefAddr", "USERNAME"));
								this.logins.add(login);
							} else

							// WEB DAV
							if ("oracle.adf.rc.adapter.webdav.WebDAVProvider".equalsIgnoreCase(providerType)) {

								Login login = new Login(credentialStoreKey, //
										parseSecureRefAddr(element), "WEB DAV Connection", //
										getAttrVal(element, "name"), //
										parseRefAddr2(element, "StringRefAddr", "userName"));
								this.logins.add(login);

							} else
							// URL
							if ("oracle.adf.model.connection.url.HttpURLConnection".equalsIgnoreCase(providerType)) {

								Login login = new Login(credentialStoreKey, //
										parseSecureRefAddr(element), "URL Connection", //
										getAttrVal(element, "name"), //
										getAttrVal(element, "name"));
								this.logins.add(login);

							} else

							// JMX
							if ("oracle.adf.share.connection.jmx.JMXConnection".equalsIgnoreCase(providerType)) {

								Login login = new Login(credentialStoreKey, //
										parseSecureRefAddr(element), "JMX Connection",//
										getAttrVal(element, "name"),//
										parseRefAddr2(element, "MetadataRefAddr", "username-property-name"));
								this.logins.add(login);

							} else

							// app server
							if ("oracle.rc.asadapter.connection.AppServerProvider".equalsIgnoreCase(providerType)) {

								Login login = new Login(credentialStoreKey, //
										parseSecureRefAddr(element), "App Server",//
										getAttrVal(element, "name"), //
										parseRefAddr2(element, "StringRefAddr", "user"));
								this.logins.add(login);

							} else // oer
							if ("oracle.jdeveloper.rcoeradapter.palette.OERProvider".equalsIgnoreCase(providerType)) {

								Login login = new Login(credentialStoreKey, //
										parseSecureRefAddr(element), "OER Server",//
										getAttrVal(element, "name"), //
										parseRefAddr2(element, "StringRefAddr", "USERNAME"));
								this.logins.add(login);
							} else
							// bam
							if ("oracle.tip.tools.ide.bam.rc.connection.BAMProviderImpl".equalsIgnoreCase(providerType)) {

								Login login = new Login(credentialStoreKey, //
										parseSecureRefAddr(element), "BAM",//
										getAttrVal(element, "name"),//
										parseRefAddr2(element, "StringRefAddr", "USER_NAME"));
								this.logins.add(login);

							} else
							// db
							if ("oracle.jdeveloper.db.adapter.DatabaseProvider".equalsIgnoreCase(providerType)) {

								Login login = new Login(credentialStoreKey, //
										parseSecureRefAddr(element), "Database",//
										getAttrVal(element, "name"), //
										parseRefAddr2(element, "StringRefAddr", "user"));
								this.logins.add(login);

							}

						}

					}
				}
			}

		}

	}

	private final String parseRefAddr2(Element element, String rootElementType, String attrName) {
		NodeList nlRefAddresses = element.getElementsByTagName(rootElementType);
		if (nlRefAddresses != null) {
			for (int ix = 0; ix <= nlRefAddresses.getLength() - 1; ix++) {

				Element eRefAddress = (Element) nlRefAddresses.item(ix);
				if (eRefAddress.getNodeType() == Element.ELEMENT_NODE) {
					for (int a = 0; a <= eRefAddress.getAttributes().getLength() - 1; a++) {
						Node attr = eRefAddress.getAttributes().item(a);
						if (attrName.equalsIgnoreCase(attr.getNodeValue())) {

							NodeList nlc = eRefAddress.getChildNodes();
							for (int e = 0; e <= nlc.getLength() - 1; e++) {
								Node node = nlc.item(e);
								if ("Contents".equalsIgnoreCase(node.getNodeName())) {
									return node.getTextContent();
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private final String parseSecureRefAddr(Element element) {
		NodeList nlRefAddresses = element.getElementsByTagName("RefAddresses");
		if (nlRefAddresses != null) {
			for (int ix = 0; ix <= nlRefAddresses.getLength() - 1; ix++) {

				try {
					Element eRefAddress = (Element) nlRefAddresses.item(ix);
					if ("RefAddresses".equals(eRefAddress.getNodeName())) {
						NodeList nl = eRefAddress.getElementsByTagName("SecureRefAddr");
						if (nl != null) {
							for (int i = 0; i <= nl.getLength() - 1; i++) {
								Node node = nl.item(i);
								if ("SecureRefAddr".equals(node.getNodeName())) {
									Node attr = node.getAttributes().getNamedItem("addrType");
									return attr.getNodeValue();
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return null;
	}

	private final String getAttrVal(Element element, String attrName) {
		Attr attr = element.getAttributeNode(attrName);
		if (attr != null) {
			return attr.getValue();
		}
		return null;
	}

	private final static Element parseDocument(File connectionXml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(connectionXml);
			return dom.getDocumentElement();
		} catch (Exception e) {
			e.printStackTrace();
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

	// user.dir

	private static final File findJdeveloperConnectionFolder(Properties properties) {
		String os = System.getProperty("ide.pref.dir");

		File jDeveloperFolder = makeDeveloperFolder(os, properties.getProperty("VER_FULL"));

		for (File file : jDeveloperFolder.listFiles()) {
			if (file.getName().contains("o.jdeveloper.rescat2.model")) {
				return file;
			}
		}

		return null;
	}

	private static final File makeDeveloperFolder(String os, String version) {
		if (version != null) {
			return new File(os + File.separator + "system" + version);
		}
		return new File(os + File.separator + "system");
	}

	/**
	 * load product version
	 */
	private final static Properties loadProductVersionProperties() throws Exception {
		Properties properties = new Properties();

		File f = new File(System.getProperty("user.dir") + File.separator + "version.properties");
		if (f.exists()) {
			properties.load(new FileReader(f));
		}

		return properties;
	}

	public static final List<Login> parseConnection() {

		try {

			Properties properties = loadProductVersionProperties();
			String product = properties.getProperty("PRODUCT");
			// if (product != null) {
			JDeveloperConnectionsParser connectionsParser = new JDeveloperConnectionsParser();
			return connectionsParser.parseConnections(properties);

			// }

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ShowMePasswordJdeveloper Error", "Error: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
		return new ArrayList<Login>();
	}
}
