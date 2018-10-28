package com.tomecode.showme.password.sqldeveloper;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import java.util.logging.Level;

import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import javax.naming.StringRefAddr;

import oracle.jdevimpl.db.adapter.ReferenceWorker;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
final class Decrypter {

    private static final Logger log = Logger.getLogger("Decrypter");


    private final ReferenceWorker referenceWorker;

    public Decrypter(String key) {
        this.referenceWorker = ReferenceWorker.createDefaultWorker(key);
    }

    public final String decrypt(String pass) {
        if (pass != null) {

            try {
                if (referenceWorker != null) {
                    char[] c = referenceWorker.decrypt(new StringRefAddr(null, pass), null);
                    if (c != null) {
                        return new String(c);
                    }

                }

            } catch (Exception e) {
                log.log(Level.FINE, e.getMessage(), e);
            }
        }
        return null;
    }
}
