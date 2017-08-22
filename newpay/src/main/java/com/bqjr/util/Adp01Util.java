package com.bqjr.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

import com.bqjr.constant.Adp01Constant;

/**
 * @author wanglh
 * @version V1.0
 * @Description:标准代扣工具类
 * @Package com.hnapay.adp01demo.util
 * @date 2017/8/7 9:50
 **/
public class Adp01Util {

    private static int readTimeOut = 30000;
    private static int connectTimeOut = 30000;

    private static HttpTransport httpTransport = new HttpTransport();

    private static HttpPost method = null;

    //以下为收款密钥的常量  需要从新生portal安全中心处下载  ，把 商户号_时间戳_0.zip解压 ，从txt文件中读取值，对应的值 放到常量中
    public static String RECV_STROREPWD = "kxqNoa"; //对应TXT文件中的 hnapay.partner.storepass
    public static String RECV_KEYALIAS = "hnapaySH";//对就TXT文件中的 hnapay.partner.alias
    public static String RECV_PRIKEYPWD = "p9QurB"; //对应TXT文件中的 hnapay.partner.pwd
    //此处为新生的公钥,对应TXT文件中的 hnapay.gateway.pubkey
    public static String HNAPAY_PUBLIC_KEY_HEX = "30819f300d06092a864886f70d010101050003818d00308189028181009fdb5cc9a3de547fd28a3cbc5a82acda4fe2f47efb0ab8b1b9716e6bcf31cac207def13914dbf6672364f40e8c11bf3ef0f7c91f2812b1bb4abf555f10576d548bf03139775fadb40443f415497b45f0db42a5a5ea71239d35017d743369c7f56b0e969aaefeb1a7fe277db78095ffade8875491fa3c473d0d7b97e2869b12470203010001";


    //加密字段
    public static Map<String, String[]> encryptField = new HashMap<String, String[]>();
    //签名字段
    public static Map<String, String[]> signField = new HashMap<String, String[]>();
    //验签字段
    public static Map<String, String[]> verifyField = new HashMap<String, String[]>();
    //提交到新生的字段
    public static Map<String, String[]> submitField = new HashMap<String, String[]>();

    static {
        method = new HttpPost();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(readTimeOut)
                .setConnectTimeout(connectTimeOut)
                .setConnectionRequestTimeout(connectTimeOut)
                .build();
        method.setHeader(HTTP.CONTENT_ENCODING, "UTF-8");
        method.setHeader(HTTP.USER_AGENT, "Rich Powered/1.0");
        method.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
        method.setConfig(requestConfig);

        //以下为加密的字段
        encryptField.put("ADP01", new String[]{"tranAmt", "cardNo", "holderName", "identityType", "identityCode", "mobileNo", "notifyUrl", "riskExpand", "goodsInfo", "merUserIp"});

        //以下为签名字段
        String[] geSignField = new String[]{"version", "tranCode", "merId", "merOrderId", "submitTime", "msgCiphertext", "signType"};
        signField.put("ADP01", geSignField);

        //以下为收到新生响应及后台通知时的验签字段
        verifyField.put("ADP01", new String[]{"version", "tranCode", "merOrderId", "merId", "charset", "signType", "resultCode", "hnapayOrderId"});

        //需要提交到新生的字段
        String[] expGeSubmitField = new String[]{"version", "tranCode", "merId", "merOrderId", "submitTime", "msgCiphertext", "signType", "signValue", "merAttach", "charset"};
        submitField.put("ADP01", expGeSubmitField);

    }

    /**
     * RSA签名，提交到新生时需要做签名
     *
     * @param tranCode 交易码 例如 ADP01
     * @param params   签名字段
     * @return 签名值
     */
    public static String sign(String tranCode, Map<String, String> params) throws IllegalArgumentException {
        System.out.println("***2.RSA签名开始-->");
        if (null == tranCode || "".equals(tranCode))
            throw new IllegalArgumentException("参数无效！");
        if (null == params)
            throw new IllegalArgumentException("参数无效！");
        try {
            //此处请填入商户收款秘钥JKS文件路径
            String jksPath = Adp01Util.class.getClassLoader().getResource("rsa_11000002996_1501827648714_0.jks").getPath();
            System.out.println("---签名前获取商户收款秘钥JKS文件路径为：" + jksPath);
            PrivateKey prikey = getPrivateKeyByJks(jksPath);
            String merData = genSingData(tranCode, params);
            System.out.println("---签名前的明文串：" + merData);
            byte[] b = RSAAlgorithms.getSignByte(prikey, merData);
            String base64 = Base64Util.encode(b);
            base64 = base64.replace("\n", "").replace("\r", "");
            System.out.println("---签名后的值为: " + base64);
            return base64;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 加密 ，对敏感信息的加密 ，签名前需要对敏感信息加密
     *
     * @param tranCode 交易码 例如 ADP01
     * @param params   加密字段
     * @return 密文
     */
    public static String encrpt(String tranCode, Map<String, String> params) throws IllegalArgumentException {
        System.out.println("***1.对商户上送报文中敏感信息加密开始-->");
        if (null == tranCode || "".equals(tranCode))
            throw new IllegalArgumentException("参数无效！");
        if (null == params)
            throw new IllegalArgumentException("参数无效！");
        String plainData = genEncryptJson(tranCode, params);
        System.out.println("---加密前数据：" + plainData);
        try {
            //使用新生公钥加密  RSA算法
            String hexPublicKey = HNAPAY_PUBLIC_KEY_HEX;
            byte[] cipherByte = RSAAlgorithms.encryptByPublicKey(plainData.getBytes("UTF-8"), hexPublicKey);
            String base64 = Base64Util.encode(cipherByte);
            base64 = base64.replace("\n", "").replace("\r", "");
            System.out.println("---加密后数据（base64编码）：" + base64);
            return base64;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 验证新生响应及后台通知的签名
     *
     * @param tranCode 交易码
     * @param params   参数
     * @return true验签通过   false 验签未通过或失败
     * @throws Exception
     */
    public static boolean verify(String tranCode, Map<String, Object> params) throws Exception {
        System.out.println("***4.新生响应及后台通知的签名验证开始-->");
        if (null == tranCode || "".equals(tranCode))
            throw new IllegalArgumentException("参数无效！");
        if (null == params)
            throw new IllegalArgumentException("参数无效！");
        String signVal = params.get(Adp01Constant.SIGNVALUE).toString();
        if (null == signVal || "".equals(signVal)) {
            throw new IllegalArgumentException("签名值不能为空！");
        }
        String verifyData = genVerifyData(tranCode, params);
        System.out.println("---验签的明文为：" + verifyData);
        byte[] bsignVal = Base64Util.decode(signVal);
        return RSAAlgorithms.verify(getPublicKeyByHex(HNAPAY_PUBLIC_KEY_HEX), verifyData, bsignVal);
    }

    /**
     * 生成加密的JSON串
     *
     * @param tranCode
     * @param params
     * @return
     */
    public static String genEncryptJson(String tranCode, Map<String, String> params) {
        String[] field = encryptField.get(tranCode.toUpperCase());
        if (null != field && field.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for (int i = 0; i < field.length; i++) {
                sb.append("\"");
                sb.append(field[i]);
                sb.append("\":\"");
                sb.append(params.get(field[i]));
                sb.append("\"");
                if (i < field.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("}");
            return sb.toString();
        } else {
            return "";
        }

    }

    /**
     * 生成签名明文串
     *
     * @param tranCode 交易码
     * @param params   参数
     * @return 返回签名明文串
     */
    private static String genSingData(String tranCode, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (int i = 0; i < signField.get(tranCode.toUpperCase()).length; i++) {
            sb.append(signField.get(tranCode.toUpperCase())[i]);
            sb.append("=[");
            sb.append(params.get(signField.get(tranCode.toUpperCase())[i]));
            sb.append("]");
        }
        return sb.toString();
    }


    /**
     * 生成验签的明文
     *
     * @param tranCode
     * @param params
     * @return
     */
    private static String genVerifyData(String tranCode, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (int i = 0; i < verifyField.get(tranCode.toUpperCase()).length; i++) {
            sb.append(verifyField.get(tranCode.toUpperCase())[i]);
            sb.append("=[");
            sb.append(params.get(verifyField.get(tranCode.toUpperCase())[i]));
            sb.append("]");
        }
        return sb.toString();
    }

    /**
     * POST提交
     *
     * @param url
     * @param obj
     * @return
     * @throws Exception
     */
    public static String submit(String tranCode, String url, Map<String, String> obj) throws Exception {
        System.out.println("***3.标准代扣POST请求提交开始-->");
        System.out.println("---请求交易码 = " + tranCode);
        System.out.println("---请求地址=" + url);
        System.out.println("---请求参数=" + obj);
        if (null == tranCode || "".equals(tranCode))
            throw new IllegalArgumentException("参数无效！");
        if (null == url || "".equals(url))
            throw new IllegalArgumentException("参数无效！");
        if (null == obj)
            throw new IllegalArgumentException("参数无效！");
        method.setURI(new URI(url + "?v=" + UUID.randomUUID()));
        httpTransport.setMethod(method);
        String response;
        Map<String, String> paraMap = new HashMap<String, String>();

        String[] field = submitField.get(tranCode.toUpperCase());
        for (int i = 0; i < field.length; i++) {
            paraMap.put(field[i], obj.get(field[i]));
        }
        if (url.startsWith("https")) {
            response = httpTransport.submit_https(paraMap);
        } else {
            response = httpTransport.submit(paraMap);
        }
        return response;
    }

    /**
     * @param hex
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyByHex(String hex)
            throws Exception {
        return RSAAlgorithms.getPublicKey(hex);
    }

    /**
     * @param jksPath jks文件路径
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     */
    public static PrivateKey getPrivateKeyByJks(String jksPath) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore recvKeyStore = null;
        PrivateKey recvPriKey = null;
        recvKeyStore = KeyStore.getInstance("JKS");
        recvKeyStore.load(new FileInputStream(jksPath), RECV_STROREPWD.toCharArray());
        recvPriKey = (PrivateKey) recvKeyStore.getKey(RECV_KEYALIAS, RECV_PRIKEYPWD.toCharArray());

        return recvPriKey;
    }

}
