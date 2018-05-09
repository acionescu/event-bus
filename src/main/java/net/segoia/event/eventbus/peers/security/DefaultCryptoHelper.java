package net.segoia.event.eventbus.peers.security;

import java.nio.charset.Charset;
import java.util.Base64;

import net.segoia.util.crypto.CryptoUtil;

public class DefaultCryptoHelper implements CryptoHelper {

    @Override
    public byte[] base64Decode(String input) {
	return CryptoUtil.base64Decode(input);
    }

    @Override
    public byte[] base64EncodeToBytes(byte[] input) {
	return Base64.getEncoder().encode(input);
    }

    @Override
    public byte[] base64DecodeToBytes(byte[] input) {
	return Base64.getDecoder().decode(input);
    }

    @Override
    public String base64Encode(byte[] input) {
	return new String(Base64.getEncoder().encode(input), Charset.forName("UTF-8"));
    }

}
