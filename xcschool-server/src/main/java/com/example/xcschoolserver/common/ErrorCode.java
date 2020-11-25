package com.example.xcschoolserver.common;

/**
 * 枚举类
 * 错误信息定义枚举类
 */
public enum ErrorCode {

    EUCCEED("20000","成功"),

    ERROR("40000", "失败"),

//    ORDER_IS_NOT_EXIST("002", "订单不存在"),
//
//    PHONE_IS_NOT_CORRECT("003", "手机号不正确"),
//
//    ORDER_IS_NOT_WAIT_RECEIVE("004", "该订单不是待收货状态"),
//
//    ORDER_UPDATE_TEST("005", "远程修改订单状态失败"),
//
//    ORDER_SELLER_REFUSED("006", "商家未拒绝,不能申请平台介入"),
//
//    GOODS_IS_NOT_EXIST("007", "调用商品信息失败！"),
//
//    GOODS_TEST("008", "商品回库失败,请稍后重试！"),
//
//    ACTIVTY_GOODS_TEST("009", "活动商品回库失败,请稍后重试！"),
//
//    COUPON_GRANT_TEST("010", "优惠卷回库失败,请稍后重试！"),
//
//    BUSINESS_AGREE("011", "您已同意,无需再次同意！"),
//
//    BUSINESS_RECEIVING_GOODS("012", "您已确认收货,无需再次收货！"),
//
//    BUSINESS_REFUSE_REFUND("013", "您已拒绝退款/退货,无需再次退款/退货！"),
//
//    USER_TEST("014", "远程调用用户信息失败"),
//
//    USER_IS_NOT_EXIST("015", "用户不存在"),
//
//    USER_REVOKE("016", "您已撤销,请不要重复撤销"),
//
//    APPLY_PRICE_IS_NOT_CORRECT("017", "申请价格不得大于支付金额!!!"),
//
//    DUPLICATE_APPLICATION("018", "平台未驳回您的申请,无法重复申请!!!"),

    ;
    private String code;

    private String descreption;

    ErrorCode(String code, String descreption) {

            this.descreption = descreption;

            this.code = code;

        }

    public int getCode() {

        return Integer.parseInt(code);

    }
    public String getDescreption() {

        return descreption;

    }
}
