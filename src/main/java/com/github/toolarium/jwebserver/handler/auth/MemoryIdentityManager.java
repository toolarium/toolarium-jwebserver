/*
 * MemoryIdentityManager.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.auth;

import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * A memory identity manager
 * 
 * @author patrick
 */
public class MemoryIdentityManager implements IdentityManager {
    private final Map<String, char[]> users;

    /**
     * Constructor for MemoryIdentityManager
     *
     * @param users the users
     */
    public MemoryIdentityManager(final Map<String, char[]> users) {
        this.users = users;
    }

    
    /**
     * @see io.undertow.security.idm.IdentityManager#verify(io.undertow.security.idm.Account)
     */
    @Override
    public Account verify(Account account) {
        // An existing account so for testing assume still valid.
        return account;
    }

    
    /**
     * @see io.undertow.security.idm.IdentityManager#verify(java.lang.String, io.undertow.security.idm.Credential)
     */
    @Override
    public Account verify(String id, Credential credential) {
        Account account = getAccount(id);
        if (account != null && verifyCredential(account, credential)) {
            return account;
        }

        return null;
    }

    
    /**
     * @see io.undertow.security.idm.IdentityManager#verify(io.undertow.security.idm.Credential)
     */
    @Override
    public Account verify(Credential credential) {
        return null;
    }

    
    /**
     * Verify the credential 
     *
     * @param account the account
     * @param credential the credential
     * @return true if it is valid
     */
    private boolean verifyCredential(Account account, Credential credential) {
        if (credential instanceof PasswordCredential) {
            char[] password = ((PasswordCredential) credential).getPassword();
            char[] expectedPassword = users.get(account.getPrincipal().getName());

            return Arrays.equals(password, expectedPassword);
        }
        return false;
    }

    
    /**
     * Get an account 
     *
     * @param id the account id
     * @return the account
     */
    private Account getAccount(final String id) {
        if (users.containsKey(id)) {
            return new Account() {
                private static final long serialVersionUID = -2295318059471579884L;
                private final Principal principal = new Principal() {
                    /**
                     * @see java.security.Principal#getName()
                     */
                    @Override
                    public String getName() {
                        return id;
                    }
                };


                /**
                 * @see io.undertow.security.idm.Account#getPrincipal()
                 */
                @Override
                public Principal getPrincipal() {
                    return principal;
                }

                
                /**
                 * @see io.undertow.security.idm.Account#getRoles()
                 */
                @Override
                public Set<String> getRoles() {
                    return Collections.emptySet();
                }
            };
        }
        
        return null;
    }
}
