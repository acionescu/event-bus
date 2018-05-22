/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus.peers.test;

import java.security.KeyPair;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;

import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.signers.ISO9796d2Signer;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.junit.Test;

import junit.framework.Assert;
import net.segoia.event.eventbus.util.EBus;
import net.segoia.util.crypto.CryptoUtil;

public class EventNodeCryptoTest {

    @Test
    public void testBasicCryptoOperations() throws Exception {
	String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJOE9CDIYjP8EFwTOdBdzM+xe/8w0rnct5EMApD072Dl964kSxnmqqvKwIlTf2FT4Hkus3KMpxLaf0l7Tl/0EbNkj1HcFupDbw+Py0JSJss5HiMH/S+qwWZUjF1gHvUwLP4NDW34j/fFtG2u7pfDILWAbW/biK2LXoPtjgdCZnaQIDAQAB";
	String signedContent = "gsfByCq769FDMIMT5QpAq9oLzxwlwInajgjBTK4HPm/efUR46JkNdF6i3CgGyfa6Sb1lR0jNvIqajgJGe2qmwehCvrR02QhufIUPrZfL72wvUT1FPJe2lrukh3JhPXidDAqhmlCEyAiGxJBN0xbsyjo7yyYFvS4+wMRri7neBEc=";
	String contentSgn = "B8omhIP7TRtrfokiRTGmq0lPj7HSlHDkcT8rb4y/0mgqRbGohLMCeMHLSGzkIbmubiOf6AAUD4mfiUvhrpHYe7cN0OiHjl9xCF7EaKxu99w9BE4dgITBZhkZNdiTFpNydukGuiEY7Bt0Ya0AZFrnFrxnQ6Z2nXgAf6GMRij1pTU=";

	RSAPublicKey pk = (RSAPublicKey) CryptoUtil.getPublicKeyFromBase64EncodedString(pubKey, "RSA");

	System.out.println("Modulus: " + pk.getModulus());
	System.out.println("Exponent: " + pk.getPublicExponent());

	byte[] conent = CryptoUtil.base64Decode(signedContent);
	byte[] sgn = CryptoUtil.base64Decode(contentSgn);

	boolean verifySignature = CryptoUtil.verifySignature(pk, conent, sgn, "SHA256withRSA");

	Assert.assertTrue(verifySignature);

    }

    @Test
    public void testSigning() throws Exception {
	EBus.initialize();
	String input = "This is a test message.";

	byte[] inputBytes = input.getBytes("UTF-8");

	KeyPair kp = CryptoUtil.generateKeyPair("RSA", 1024);

	String sngAlg = "SHA256withRSA";
	Signature defSig = Signature.getInstance(sngAlg);
	System.out.println("def sgn class: " + defSig.getClass());
	defSig.initSign(kp.getPrivate());
	defSig.update(inputBytes);

	byte[] defSigOut = defSig.sign();
	
	SHA256Digest sha256Digest = new SHA256Digest();
	sha256Digest.update(inputBytes, 0, inputBytes.length);
	byte[] hash = new byte[sha256Digest.getDigestSize()];
	sha256Digest.doFinal(hash, 0);
	
	System.out.println("Sha 256 hash: "+CryptoUtil.base64Encode(hash));

	Signature bcSig = Signature.getInstance(sngAlg, "BC");
	System.out.println("bc sgn class: " + bcSig.getClass());
	bcSig.initSign(kp.getPrivate());
	bcSig.update(inputBytes);
	byte[] bcSgnBytes = bcSig.sign();

	String defSgnEncoded = CryptoUtil.base64Encode(defSigOut);
	System.out.println("Def sig: " + defSgnEncoded);
	String bcSgnEncoded = CryptoUtil.base64Encode(bcSgnBytes);
	System.out.println("BC sig: " + bcSgnEncoded);

	Assert.assertEquals(defSgnEncoded, bcSgnEncoded);
	
	
	RSADigestSigner rsaSigner = new RSADigestSigner(sha256Digest);
	
	PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(kp.getPrivate().getEncoded());
	
	rsaSigner.init(true, PrivateKeyFactory.createKey(pkInfo));
	rsaSigner.update(inputBytes,0, inputBytes.length);
	byte[] rsaSgnBytes = rsaSigner.generateSignature();
	
	String rsaSgnEncoded = CryptoUtil.base64Encode(rsaSgnBytes);
	
	System.out.println("rsa signer: "+rsaSgnEncoded);
	
	Assert.assertEquals(defSgnEncoded, rsaSgnEncoded);
	
	SubjectPublicKeyInfo pubInfo = new SubjectPublicKeyInfo((ASN1Sequence)ASN1Primitive.fromByteArray(kp.getPublic().getEncoded()));	
	
//	rsaSgnBytes[0] -= 1;
	
	rsaSigner.init(false, PublicKeyFactory.createKey(pubInfo));
	rsaSigner.update(inputBytes,0, inputBytes.length);
	boolean verifies = rsaSigner.verifySignature(rsaSgnBytes);
	
	Assert.assertTrue(verifies);
	
	
	byte[] sign2Bytes = sign2(PrivateKeyFactory.createKey(pkInfo), inputBytes);
	
	String sign2Encoded = CryptoUtil.base64Encode(sign2Bytes);
	System.out.println("sign 2 :"+sign2Encoded);
	
	boolean verifySign2 = verifySign2(PublicKeyFactory.createKey(pubInfo), inputBytes, rsaSgnBytes);
	
	Assert.assertTrue(verifySign2);
    }
    
    public static boolean verifySign2(AsymmetricKeyParameter keyParam, byte[] payload, byte[] sig) throws Exception {
        RSAEngine engine = new RSAEngine();
        Digest digest = new SHA256Digest();

        RSAKeyParameters rsaPublic = (RSAKeyParameters) keyParam;
        ISO9796d2Signer verifier = new ISO9796d2Signer(engine, digest);
        verifier.init(false, rsaPublic); // false for verify

        verifier.update(payload, 0, payload.length);

//        byte[] sig = ASN1Primitive.fromByteArray(sig).toASN1Primitive().getEncoded("DER");
        if (!verifier.verifySignature(sig)) {
            throw new Exception("Failed signature verification");
        }
        return true;
    }

    public static byte[] sign2(AsymmetricKeyParameter keyParam, byte[] payload) throws Exception {
        RSAEngine engine = new RSAEngine();
        Digest digest = new SHA256Digest();

        RSAKeyParameters rsaPublic = (RSAKeyParameters) keyParam;
        ISO9796d2Signer signer = new ISO9796d2Signer(engine, digest, true);
        signer.init(true, rsaPublic); // false for verify

        signer.update(payload, 0, payload.length);
        return signer.generateSignature();
    }

}
