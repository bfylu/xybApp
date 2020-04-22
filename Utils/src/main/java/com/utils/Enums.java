package com.utils;

/**
 * Created by Administrator on 2016/1/29.
 */
public class Enums {

    public enum httpType {
        获取密钥("000001"),
        登录("000002"),
        检查升级("000003"),
        注册("000004"),
        交易概况("000005"),
        统计查询("000006"),
        提交订单("000007"),
        流水查询("000008"),
        结算查询("000009"),
        发起结算("000010"),
        收款信息("000011"),
        操作信息("000012"),
        商户固定码("000013"),
        订单状态查询("000014");


        private String index;

        // 构造方法
        httpType(String index) {
            this.index = index;
        }

        public String getString() {
            return index;
        }
    }


    public enum apiState {
        成功("000000"),
        失败("000001"),
        异常("000002"),
        报文字段不能为空("000003"),
        报文字段格式错误("000004"),
        服务没找到("000005"),
        服务方法没有返回("000006"),
        无效的商户编号("000007"),
        用户名或密码错误("000008"),
        没有找到密钥("000009"),
        密钥已过期("000010"),
        用户名已被使用("000011"),
        电子邮箱已被使用("000012"),
        手机号码已被使用("000013"),
        用户未找到("000014");

        private String index;

        public String getString() {
            return index;
        }

        apiState(String index) {
            this.index = index;
        }

    }

    public enum payMethod {
        刷卡("1"),
        扫码("2"),
        二维码("3");

        public static String getNameString(String data) {
            for (payMethod type : values()) {
                if (type.getIndex().equals(data)) {
                    return type.name();
                }
            }
            return data + "";
        }

        private String index;

        public String getIndex() {
            return index;
        }

        payMethod(String i) {
            this.index = i;
        }
    }


    public enum payMethodName {
        刷卡("bankcard"),
        扫码("scan"),
        二维码("qrcode");

        private String index;

        public String getString() {
            return index;
        }

        public static String getNameString(String data) {
            for (payMethodName type : values()) {
                if (type.getString().equals(data)) {
                    return type.name();
                }
            }
            return data;
        }

        payMethodName(String i) {
            this.index = i;
        }
    }

    public enum payType {
        支付宝("alipay"),
        微信("weixin"),
        银联("unionpay");

        private String index;

        public String getString() {
            return index;
        }

        public static String getNameString(String data) {
            for (payType type : values()) {
                if (type.getString().equals(data)) {
                    return type.name();
                }
            }
            return data;
        }

        payType(String i) {
            this.index = i;
        }
    }

    public enum httpPayType {
        智能支付("00"),
        支付宝("01"),
        微信支付("02"),
        银联支付("03"),
        预授信("04");

        static int[] rIds = {R.mipmap.baidu, R.mipmap.zhifubao, R.mipmap.weixin, R.mipmap.baidu,R.mipmap.baidu};
        private String index;

        public String getString() {
            return index;
        }

        public static String getNameString(String data) {
            for (httpPayType httpPayType : values()) {
                if (httpPayType.getString().equals(data)) {
                    return httpPayType.name();
                }
            }
            return data;
        }

        public static int getImageResource(String data) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i].getString().equals(data)) {
                    return rIds[i];
                }
            }
            return R.mipmap.baidu;
        }

        httpPayType(String i) {
            this.index = i;
        }
    }

    public enum orderStatus {
        处理中("0"),
        成功("1"),
        失败("2");

        private String index;

        public static String getNameString(String data) {
            for (orderStatus type : values()) {
                if (type.getString().equals(data)) {
                    return type.name();
                }
            }
            return data;
        }

        public String getString() {
            return index;
        }

        orderStatus(String i) {
            this.index = i;
        }
    }

    public enum orderStatusName {
        处理中("process"),
        成功("success"),
        失败("failure");

        private String index;

        public static String getNameString(String data) {
            for (orderStatusName type : values()) {
                if (type.getString().equals(data)) {
                    return type.name();
                }
            }
            return data;
        }

        public String getString() {
            return index;
        }

        orderStatusName(String i) {
            this.index = i;
        }
    }

    public enum orderTypeName {
        付款("pay"),
        退款("refund");

        private String index;

        public static String getNameString(String data) {
            for (orderTypeName type : values()) {
                if (type.getString().equals(data)) {
                    return type.name();
                }
            }
            return data;
        }

        public String getString() {
            return index;
        }

        orderTypeName(String i) {
            this.index = i;
        }
    }

    public enum transType {
        所有("all"),
        成功("success"),
        已收款("acquire"),
        待收款("payment");

        public static String getNameString(String data) {
            for (transType type : values()) {
                if (type.getString().equals(data)) {
                    return type.name();
                }
            }
            return data;
        }

        private String index;

        public String getString() {
            return index;
        }

        transType(String i) {
            this.index = i;
        }
    }

    public enum shopOrder {
        全部(""),
        待付款("0"),
        待发货("1"),
        已发货("2"),
        已完成("3"),
        已关闭("4"),
        维权全部(""),
        待买家处理("0"),
        同意退款("1"),
        维权撤销("2");

        public static String getNameString(String data) {
            for (shopOrder type : values()) {
                if (type.getString().equals(data)) {
                    return type.name();
                }
            }
            return data;
        }

        private String index;

        public String getString() {
            return index;
        }

        shopOrder(String i) {
            this.index = i;
        }
    }

    public enum clientType {
        客户类型("type"),
        客户标签("tag");

        public static String getNameString(String data) {
            for (clientType type : values()) {
                if (type.getString().equals(data)) {
                    return type.name();
                }
            }
            return data;
        }

        private String index;

        public String getString() {
            return index;
        }

        clientType(String i) {
            this.index = i;
        }
    }

}
