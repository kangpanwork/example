package com.sanri.test.apachecommons;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

/**
 * apache commons codec
 */
public class Codec {

	static Charset charset = Charset.forName("utf-8");

	/**
	 *  功能 : 用于把字节转换成可打印字符 , 转换后会增大数据体积
	 *
	 *  bas64 是指使用 6 个 bit 为一个单元,对应某个可打印字符,因为 2^6 = 64 所以叫 base64
	 * 	源数据最小单位是 byte ,所以 8 bit 和 6 bit 最大公约数为 24 ,
	 * 	即刚好可以 3 个 byte 对应四个可打印字符,转换后的大小为源大小的 4/3
	 * 	又因为源字节数不一定刚好是 3 的倍数,所以当不够时在末尾补 0 ,base64 中用 = 填充
	 */
	@Test
	public void base64() throws UnsupportedEncodingException{
		String str="中文.";
		byte[] bytes = str.getBytes(charset);
		byte[] encodeBase64 = Base64.encodeBase64(bytes);
		System.out.println(new String(encodeBase64));
		byte[] decodeBase64 = Base64.decodeBase64(encodeBase64);
		System.out.println(new String(decodeBase64,charset));
	}

	/**
	 * 功能 : 用于把字节转换成可打印字符 , 转换后会增大数据体积
	 *
	 * hex 在网上没有找到,但照这个情况应该是 4bit 一编码成一个字母,"中文" utf-8 编码是 6 字节,所以最终生成 12 位
	 * hex 编码原理 取 1 字节 的前4 位和后 4 位在 DIGITS 表中的映射,可自定义 digits 表,一般用数字和字母的组合
	 *
	 * 这个就是高 4 位对映一个可打印字符 , 低 四对映一个可打印字符 , 所以目标大小是源大小的 2 倍 ,不需要填充字符
	 * @throws DecoderException
	 */
	@Test
	public void hex() throws DecoderException{
		String str="中文";
		char[] encodeHex = Hex.encodeHex(str.getBytes(charset));
		System.out.println(new String(encodeHex));
		byte[] decodeHex = Hex.decodeHex(encodeHex);
		System.out.println(new String(decodeHex,charset));
		System.out.println("e4b8ade69687".length());
	}

	/**
	 * md5 本身是没有密匙的,可以自定义一个密匙和密码做一定规则的合并,然后调用 md5 可以达到自定义密匙的作用
	 * md5 生成的是 byte [] , 可以使用 base64 或 hex 工具转成可见字符
	 *
	 * 16 字节的 md5 转 hex 为 32 位
	 */
	@Test
	public void md5(){
		byte[] md5 = DigestUtils.md5("h196944sanri".getBytes(charset));
		System.out.println(Hex.encodeHexString(md5));

		System.out.println(DigestUtils.md5Hex("h196944{sanri}"));
		System.out.println("8a733b82333ef6f70acaa07f5b2e0b30".length());
	}

	/**
	 * 和 md5 一样,也是任何一个数据都只对应唯一一个固定长度的值 , 一般用于加密和验证数据是否被篡改
	 */
	@Test
	public void sha(){
		// 20 个字节 hex 对应 40 位
		byte [] sha1 = DigestUtils.sha1("abc123456");
		// 32 个字节 hex 对应 64 位
		byte[] sha256 = DigestUtils.sha256("abc123456");
		// 48 个字节 hex 对应 96 位
		byte[] sha384 = DigestUtils.sha384("abc123456");
		// 64 个字节 hex 对应 128 位
		byte[] sha512 = DigestUtils.sha512("abc123456");

		System.out.println(Hex.encodeHexString(sha1));
		System.out.println(Hex.encodeHexString(sha256));
		System.out.println(Hex.encodeHexString(sha384));
		System.out.println(Hex.encodeHexString(sha512));
	}
	
}
