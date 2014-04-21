package com.tomecode.jdev.extension12g.showmepassword;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
public final class Login {

        private final String credentialStoreKey;
        private final String passwordKey;
        /**
         * login type
         */
        private final String type;

        /**
         * connection name
         */
        private final String name;

        /**
         * user
         */
        private String user;

        /**
         * password
         */
        private String pass = "";

        public Login(String credentialStoreKey, String passwordKey, String type, String name, String user) {
                this.credentialStoreKey = (credentialStoreKey == null ? "" : credentialStoreKey);
                this.passwordKey = passwordKey;
                this.type = (type == null ? "" : type);
                this.name = (name == null ? "" : name);
                this.user = (user == null ? "" : user);
        }

        public final String getCredentialStoreKey() {
                return credentialStoreKey;
        }

        public String getPasswordKey() {
                return passwordKey;
        }

        public final String getType() {
                return type;
        }

        public final String getUser() {
                return user;
        }

        public final String getPass() {
                return pass;
        }

        public final String getName() {
                return name;
        }

        public void setPass(String pass) {
                this.pass = pass;
        }

        public final String[] toArray() {
                return new String[] { type, name, user, pass };
        }

}
