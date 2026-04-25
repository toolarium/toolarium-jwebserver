/*
 * MemoryIdentityManagerTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.jwebserver.handler.auth.MemoryIdentityManager;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.PasswordCredential;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;


/**
 * Tests for MemoryIdentityManager password verification.
 *
 * @author patrick
 */
public class MemoryIdentityManagerTest {
    private static final String TEST_USER = "admin";
    private static final String TEST_PASS = "secret123";
    private static final String WRONG_PASS = "wrongpass";
    private static final String UNKNOWN_USER = "unknown";


    /**
     * Test correct password is accepted.
     */
    @Test void testCorrectPasswordAccepted() {
        Map<String, char[]> users = new HashMap<>();
        users.put(TEST_USER, TEST_PASS.toCharArray());
        MemoryIdentityManager manager = new MemoryIdentityManager(users);

        Account result = manager.verify(TEST_USER, new PasswordCredential(TEST_PASS.toCharArray()));
        assertNotNull(result);
    }


    /**
     * Test wrong password is rejected.
     */
    @Test void testWrongPasswordRejected() {
        Map<String, char[]> users = new HashMap<>();
        users.put(TEST_USER, TEST_PASS.toCharArray());
        MemoryIdentityManager manager = new MemoryIdentityManager(users);

        Account result = manager.verify(TEST_USER, new PasswordCredential(WRONG_PASS.toCharArray()));
        assertNull(result);
    }


    /**
     * Test unknown user is rejected.
     */
    @Test void testUnknownUserRejected() {
        Map<String, char[]> users = new HashMap<>();
        users.put(TEST_USER, TEST_PASS.toCharArray());
        MemoryIdentityManager manager = new MemoryIdentityManager(users);

        Account result = manager.verify(UNKNOWN_USER, new PasswordCredential(TEST_PASS.toCharArray()));
        assertNull(result);
    }


    /**
     * Test empty password is rejected when expected password is not empty.
     */
    @Test void testEmptyPasswordRejected() {
        Map<String, char[]> users = new HashMap<>();
        users.put(TEST_USER, TEST_PASS.toCharArray());
        MemoryIdentityManager manager = new MemoryIdentityManager(users);

        Account result = manager.verify(TEST_USER, new PasswordCredential("".toCharArray()));
        assertNull(result);
    }


    /**
     * Test empty password matches when expected is also empty.
     */
    @Test void testEmptyPasswordMatchesEmpty() {
        Map<String, char[]> users = new HashMap<>();
        users.put(TEST_USER, "".toCharArray());
        MemoryIdentityManager manager = new MemoryIdentityManager(users);

        Account result = manager.verify(TEST_USER, new PasswordCredential("".toCharArray()));
        assertNotNull(result);
    }


    /**
     * Test that existing account verification passes through.
     */
    @Test void testVerifyExistingAccount() {
        Map<String, char[]> users = new HashMap<>();
        users.put(TEST_USER, TEST_PASS.toCharArray());
        MemoryIdentityManager manager = new MemoryIdentityManager(users);

        Account account = manager.verify(TEST_USER, new PasswordCredential(TEST_PASS.toCharArray()));
        assertNotNull(account);

        // re-verify an existing account
        Account reVerified = manager.verify(account);
        assertNotNull(reVerified);
    }
}
