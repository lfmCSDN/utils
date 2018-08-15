package com.sunyard.aps.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunyard.aps.common.dto.CMBCHttpReqDto;
import com.sunyard.aps.common.dto.CMBCHttpRespDto;
import com.sunyard.aps.common.exception.CryptoException;
import com.sunyard.aps.common.security.CommonUtil;
import com.sunyard.aps.common.security.CryptoUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhangxin on 2017/8/29.
 */
public class CryptoUtils {

    private static Logger log = LoggerFactory.getLogger(CryptoUtils.class);

    private static String charset="utf-8";

    /**
     *  对称加密返回加密后的结果
     * @param xmlData   需要加密的xml报文
     * @param AESKey    对称密钥(每次交易随机生成)
     * @return
     */
    public static String AESEncrypt(String xmlData, String AESKey) throws CryptoException{
        String encryptedResult = null;
        try {
            //获取对称加密后的字节数组
            byte[] encryptedBytes = CryptoUtil.AESEncrypt(xmlData.getBytes(charset),AESKey.getBytes(charset),"AES","AES/ECB/PKCS5Padding",null);
            //获取Base64编码后的字节数组
            byte[] encodeBase64Bytes = Base64.encodeBase64(encryptedBytes);
            encryptedResult = new String(encodeBase64Bytes,charset);
        } catch (Exception e) {
            throw new CryptoException("AES对称加密失败",e);
        }
        return encryptedResult;
    }

    /**
     *  根据公钥地址将对称密钥加密
     * @param AESKey    对称密钥
     * @param pubKeyPath    公钥文件全路径
     * @return
     * @throws CryptoException
     */
    public static String RSAEncrypt(String AESKey, String pubKeyPath) throws CryptoException {
        PublicKey publicKey = getPubKeyByPath(pubKeyPath);
        return RSAEncrypt(AESKey,publicKey);
    }

    /**
     * 根据公钥将对称密钥加密
     * @param AESKey    对称密钥
     * @param publicKey 公钥
     * @return
     * @throws CryptoException
     */
    public static String RSAEncrypt(String AESKey, PublicKey publicKey) throws CryptoException{
        String encryptedResult = null;
        try {
            byte[] encryptedBytes = CryptoUtil.RSAEncrypt(AESKey.getBytes(charset), publicKey, 2048, 11,"RSA/ECB/PKCS1Padding");
            byte[] encodeBase64Bytes = Base64.encodeBase64(encryptedBytes);
            encryptedResult = new String(encodeBase64Bytes,charset);
        } catch (Exception e) {
            throw new CryptoException("RSA公钥加密失败",e);
        }
        return encryptedResult;
    }

    /**
     * 根据公钥地址获取公钥
     * @param pubKeyPath    公钥地址
     * @return
     * @throws CryptoException
     */
    public static PublicKey getPubKeyByPath(String pubKeyPath) throws CryptoException{
        try {
            return CryptoUtil.getRSAPublicKeyByFileSuffix(pubKeyPath, "pem", "RSA");
        } catch (Exception e) {
            throw new CryptoException("获取RSA公钥失败",e);
        }
    }

    /**
     * 使用Java UUID生成唯一识别码/对称密钥
     * 民生银行要求16位字母或数字
     * @return  对称密钥
     */
    public static String getAESKey(){
        // 随机字母+数字
        return CommonUtil.generateLenString(16);
//        return getAESKey(16);
    }

    /**
     * 使用uuid获取唯一纯数字ID
     * @param length
     * @return
     */
    public static String getAESKey(int length){
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // length 代表长度为length
        // d 代表参数为正数型
        return String.format("%0"+length+"d", hashCodeV);
    }

    /**
     * RSA私钥签名
     * @param xmlData   签名数据
     * @param priKeyPath    私钥全路径
     * @return
     * @throws CryptoException
     */
    public static String digitalSign(String xmlData, String priKeyPath) throws CryptoException{
        PrivateKey privateKey = getPriKeyByPath(priKeyPath);
        return digitalSign(xmlData, privateKey);
    }

    /**
     * RSA私钥签名
     * @param xmlData   签名数据
     * @param privateKey    私钥
     * @return
     * @throws CryptoException
     */
    public static String digitalSign(String xmlData, PrivateKey privateKey) throws CryptoException{
        byte[] encryptedBytes = null;
        byte[] encodeBase64Bytes = null;
        String encryptedResult = null;
        try {
            encryptedBytes = CryptoUtil.digitalSign(xmlData.getBytes(charset), privateKey, "SHA1WithRSA");
            encodeBase64Bytes = Base64.encodeBase64(encryptedBytes);
            encryptedResult = new String(encodeBase64Bytes,charset);
        } catch (Exception e) {
            throw new CryptoException("使用私钥签名失败",e);
        }
        return encryptedResult;
    }

    /**
     * 根据私钥地址获取私钥
     * @param priKeyPath    私钥全路径
     * @return
     * @throws CryptoException
     */
    public static PrivateKey getPriKeyByPath(String priKeyPath) throws CryptoException{
        try {
            return CryptoUtil.getRSAPrivateKeyByFileSuffix(priKeyPath,"pem", null, "RSA");
        } catch (Exception e) {
            throw  new CryptoException("获取RSA私钥失败",e);
        }
    }

    /**
     *  加密报文并初步组装http请求数据
     * @param message   需要加密的报文
     * @param cmbcPubKeyPath    银行公钥
     * @param priKeyPath    我方私钥
     * @return
     */
    public static CMBCHttpReqDto encodeReqData(String message, String cmbcPubKeyPath, String priKeyPath) {
        log.info("嘉兴银行私钥路径："+priKeyPath);
        // 获取对称加密密钥
        String AESKey = getAESKey();
        log.info("create AESKey:"+AESKey);
        // 加密报文
        String encryptData = AESEncrypt(message, AESKey);
        String signData = digitalSign(message, priKeyPath);
        String encrtptKey = RSAEncrypt(AESKey,cmbcPubKeyPath);
        CMBCHttpReqDto cmbcHttpReqDto = new CMBCHttpReqDto();
        cmbcHttpReqDto.setEncryptData(encryptData);
        cmbcHttpReqDto.setSignData(signData);
        cmbcHttpReqDto.setEncryptKey(encrtptKey);
        log.info("加密后http请求字段:"+JSON.toJSON(cmbcHttpReqDto));
        return cmbcHttpReqDto;
    }

    /**
     *  加密报文并初步组装http请求数据
     * @param message   需要加密的报文
     * @param pubKey    银行公钥
     * @param priKey    我方私钥
     * @return
     */
    public static CMBCHttpReqDto encodeReqData(String message, PublicKey pubKey, PrivateKey priKey) {
        // 获取对称加密密钥
        String AESKey = getAESKey();
        log.info("create AESKey:"+AESKey);
        // 加密报文
        String encryptData = AESEncrypt(message, AESKey);
        String signData = digitalSign(message, priKey);
        String encrtptKey = RSAEncrypt(AESKey,pubKey);
        CMBCHttpReqDto cmbcHttpReqDto = new CMBCHttpReqDto();
        cmbcHttpReqDto.setEncryptData(encryptData);
        cmbcHttpReqDto.setSignData(signData);
        cmbcHttpReqDto.setEncryptKey(encrtptKey);
        log.info("加密后http请求字段:"+JSON.toJSON(cmbcHttpReqDto));
        return cmbcHttpReqDto;
    }
    public static PrivateKey getPriKeyByVal(String priKeyStr){
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKeyStr));
        PrivateKey priKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            priKey = keyFactory.generatePrivate(priPKCS8);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("生成密钥工厂时没有此类算法");
        } catch (InvalidKeySpecException e) {
            throw new CryptoException("生成私钥对象异常");
        }
        return priKey;
    }

    public static PublicKey getPubKeyByVal(String pubKeyStr){
        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(pubKeyStr));
        PublicKey pubKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            pubKey = keyFactory.generatePublic(pubX509);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("生成密钥工厂时没有此类算法");
        } catch (InvalidKeySpecException e) {
            throw new CryptoException("生成公钥对象异常");
        }
        return pubKey;
    }

    /**
     * 解密报文并封装DTO
     * @param respStr       民生银行返回数据
     * @param cmbcPubKeyPath    银行公钥
     * @param priKeyPath        我方私钥
     * @return
     */
    public static CMBCHttpRespDto decodeRespData(String respStr, String cmbcPubKeyPath, String priKeyPath){
        // 解析返回数据
//        CMBCHttpRespDto cmbcHttpRespDto = JsonUtil.jsonToBean(respStr, CMBCHttpRespDto.class);
        // 使用上面方式转换DTO，会发生由于json中存在字段，DTO中没有而报错的问题，所以换成阿里的json处理
        JSONObject jsonObject = JSON.parseObject(respStr);
        CMBCHttpRespDto cmbcHttpRespDto = JSON.toJavaObject(jsonObject, CMBCHttpRespDto.class);
        return decodeRespData(cmbcHttpRespDto,cmbcPubKeyPath,priKeyPath);
    }
    /**
     * 解密报文并封装DTO
     * @param respStr       民生银行返回数据
     * @param pubKey    银行公钥
     * @param priKey        我方私钥
     * @return
     */
    public static CMBCHttpRespDto decodeRespData(String respStr, PublicKey pubKey, PrivateKey priKey){
        // 解析返回数据
//        CMBCHttpRespDto cmbcHttpRespDto = JsonUtil.jsonToBean(respStr, CMBCHttpRespDto.class);
        // 使用上面方式转换DTO，会发生由于json中存在字段，DTO中没有而报错的问题，所以换成阿里的json处理
        JSONObject jsonObject = JSON.parseObject(respStr);
        CMBCHttpRespDto cmbcHttpRespDto = JSON.toJavaObject(jsonObject, CMBCHttpRespDto.class);
        return decodeRespData(cmbcHttpRespDto, pubKey, priKey);
    }

    /**
     *  解密报文并封装DTO
     * @param respParams    银行返回的Map数据
     * @param cmbcPubKeyPath    银行公钥
     * @param priKeyPath        我方私钥
     * @return
     * @throws Exception
     */
    public static CMBCHttpRespDto decodeRespData(Map<String, Object> respParams, String cmbcPubKeyPath, String priKeyPath) throws Exception {
        // 解析返回数据
        CMBCHttpRespDto cmbcHttpRespDto = (CMBCHttpRespDto) MapUtil.mapToObject(respParams, CMBCHttpRespDto.class);
        return decodeRespData(cmbcHttpRespDto,cmbcPubKeyPath,priKeyPath);
    }

    /**
     * 解密报文并封装DTO
     * @param cmbcHttpRespDto   封装银行返回数据
     * @param cmbcPubKeyPath    银行公钥
     * @param priKeyPath        我方私钥
     * @return  返回解密、验签后的DTO
     */
    private static CMBCHttpRespDto decodeRespData(CMBCHttpRespDto cmbcHttpRespDto, String cmbcPubKeyPath, String priKeyPath){
        log.info("民生银行公钥路径："+cmbcPubKeyPath+", 嘉兴银行私钥路径："+priKeyPath);
        return decodeRespData(cmbcHttpRespDto, getPubKeyByPath(cmbcPubKeyPath), getPriKeyByPath(priKeyPath));
    }

    /**
     * 解密报文并封装DTO
     * @param cmbcHttpRespDto   封装银行返回数据
     * @param pubKey    银行公钥
     * @param priKey        我方私钥
     * @return  返回解密、验签后的DTO
     */
    private static CMBCHttpRespDto decodeRespData(CMBCHttpRespDto cmbcHttpRespDto, PublicKey pubKey, PrivateKey priKey){
        try {
            // 获取对称加密密钥 使用我方私钥解密对称密钥字段
            byte[] decodeBase64KeyBytes = Base64.decodeBase64(cmbcHttpRespDto.getEncryptKey().getBytes(charset));
            byte[] merchantAESKeyBytes = CryptoUtil.RSADecrypt(decodeBase64KeyBytes, priKey, 2048, 11,"RSA/ECB/PKCS1Padding");
            //  获取解密报文 使用对称密钥解密报文数据
            byte[] decodeBase64DataBytes = Base64.decodeBase64(cmbcHttpRespDto.getEncryptData().getBytes(charset));
            byte[] merchantXmlDataBytes = CryptoUtil.AESDecrypt(decodeBase64DataBytes, merchantAESKeyBytes, "AES","AES/ECB/PKCS5Padding", null);
            cmbcHttpRespDto.setMessage(new String(merchantXmlDataBytes, charset));
            //  使用解密后报文、民生银行公钥、签名数据、签名算法执行验证签名
            boolean signResult = CryptoUtil.verifyDigitalSign(merchantXmlDataBytes, Base64.decodeBase64(cmbcHttpRespDto.getSignData().getBytes(charset)), pubKey, "SHA1WithRSA");
            if(!signResult){
                log.warn("签名验证失败,应答信息:"+ JSON.toJSON(cmbcHttpRespDto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cmbcHttpRespDto;
    }
    public static byte[] decodeBase64(String base64Str){
        return Base64.decodeBase64(base64Str);
    }
}
