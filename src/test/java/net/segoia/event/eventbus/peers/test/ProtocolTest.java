package net.segoia.event.eventbus.peers.test;

import org.junit.Test;

public class ProtocolTest {

    @Test
    public void testIdentityKeyGeneration() {
	/*
	 * - generate random key - identity secret key
	 * - use above key and a static string ( peer id ) to generate an id token using HmacSHA256
	 * - generate random initialization vector
	 * - specify iterations
	 * - specify keySize
	 * 
	 * - send encrypted identity secret key to peer, along with iv vector and iterations
	 * - use id token as key of peer identity
	 * 
	 * 
	 * - during authentication peer sends the id token along with a newly
	 * 
	 * 
	 * - generate secret key from above using pbkdf2
	 */
    }
}
