package com.bqjr.process;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.bqjr.constant.Adp01Constant;
import com.bqjr.util.Adp01Util;
import com.bqjr.util.DateUtils;
import com.bqjr.util.HnapayRandom;
import com.bqjr.util.JsonUtil;

/**
 * @author wanglh
 * @version V1.0
 * @Description:标准代扣
 * @Package com.hnapay.adp01demo.process
 * @date 2017/8/7 9:50
 **/
public class Adp01Process {

    private static String version = "2.0";//版本号
    private static String merId = "11000002996";//商户号
    private static String signType = "1";//签名类型 RSA
    private static String charset = "1";//编码类型 UTF-8
    private static String adpayUrl = "https://gatewaymer.hnapay.com/adpay/pay.do";//标准代扣URL

    /**
     * 封装标准代扣报文
     * @return
     */
    private static Map<String,String> buildAdp01Param() {
        String tranCode = "ADP01";//交易类型(APD01为标准代扣)
        Map<String, String> adp01Param = new HashMap<String, String>();
        // 报文密文参数
        String amt = HnapayRandom.genRandomDec(2);
        amt = new BigDecimal(amt).toString();
        adp01Param.put(Adp01Constant.TRANAMT,amt);//支付金额
        adp01Param.put(Adp01Constant.CARDNO, "6230202012622180");//银行卡号
        adp01Param.put(Adp01Constant.HOLDNAME, "李四");//持卡人姓名
        adp01Param.put(Adp01Constant.IDENTITYTYPE, "01");//证件类型 01代表身份证
        adp01Param.put(Adp01Constant.IDENTITYCODE, "410883198605262036");//证件号码
        adp01Param.put(Adp01Constant.MOBILENO, "18510410526");//银行预留手机号
        adp01Param.put(Adp01Constant.NOTIFYURL, "http://www.baidu.com");// 商户异步通知地址
        adp01Param.put(Adp01Constant.RISKEXPAND, "");// 风控扩展信息
        adp01Param.put(Adp01Constant.GOODSINFO, "金庸全集");// 商品信息
        adp01Param.put(Adp01Constant.MERUSERIP, "127.0.0.1");//请求ip

        // 报文明文参数
        adp01Param.put(Adp01Constant.VERSION, version);// 版本号
        adp01Param.put(Adp01Constant.TRANCODE, tranCode);// 交易代码
        adp01Param.put(Adp01Constant.MERID, merId);// 商户ID
        String merOrderId = tranCode + "_" + DateUtils.getCurrDate("yyyyMMddHHmmssSSS");
        adp01Param.put(Adp01Constant.MERORDERID, merOrderId);
        adp01Param.put(Adp01Constant.SUBMITTIME, DateUtils.getCurrDate());// 请求提交时间
        adp01Param.put(Adp01Constant.MSGCIPHERTEXT, Adp01Util.encrpt(tranCode, adp01Param));// 报文密文
        adp01Param.put(Adp01Constant.SIGNTYPE, signType);// 签名类型:RSA
        adp01Param.put(Adp01Constant.MERATTACH, "");// 附加数据
        adp01Param.put(Adp01Constant.CHARSET, charset);// 编码方式:UTF-8
         //签名值
        try {
            adp01Param.put(Adp01Constant.SIGNVALUE, Adp01Util.sign(tranCode, adp01Param));
        } catch (Exception e) {
            e.printStackTrace();
            adp01Param.put(Adp01Constant.SIGNVALUE, "");
        }
        return adp01Param;
    }

    /**
     * Main入口
     * @param args
     */
    public static void main(String[] args){

        System.out.println("********调用标准代扣接口开始********");
        //封装标准代扣报文
        Map<String, String> params = buildAdp01Param();
        try {
            // POST请求新生标准代扣
            String response = Adp01Util.submit(params.get(Adp01Constant.TRANCODE), adpayUrl, params);
            System.out.println("---标准代扣POST请求提交返回结果："+response);
            //对返回结果验签
            Map<String, Object> resultMap = JsonUtil.jsonToMap(response);
            boolean verify = Adp01Util.verify("ADP01", resultMap);
            System.out.println("---标准代扣返回验签结果为："+verify);
            if(!verify) {
                System.out.println("---标准代扣返回结果验签失败---");
                return;
            }
            System.out.println("********调用标准代扣接口成功结束********");
            //获取返回新生订单号
            String hanpayOrderId = (String) resultMap.get(Adp01Constant.HNAPAYORDERID);
            System.out.println("********标准代扣返回新生订单号为：hanpayOrderId = " + hanpayOrderId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
