package cn.sparking.device.model.lingpark;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LPOrderPaidInfo extends LingParkBaseRequest {
    private String notifyInfo;

    @Data
    @Builder
    static class OrderPaid {
        // 订单ID
        private String orderId;

        // 订单总金额
        private double totalCost;

        // 实付金额
        private double payCost;

        // 优惠金额
        private double deduCost;

        // 优惠方式 1 包月 2 优惠劵 3 新能源优惠 4 优惠放行
        private short deduType;

        // 支付时间
        private String payTime;

        // 支付订单号
        private String tradeNo;

        // 支付方式 1 支付宝 2 微信
        private short payStyle;

        // 订单列表
        private List<Order> payOrders;
    }

    /**
     * 订单结构.
     */
    @Data
    @Builder
    static class Order{
        // 订单开始时间 yyyy-MM-dd HH:mm:ss
        private String beginTime;

        // 取车编码
        private String code;

        // 订单结束时间
        private String endTime;

        // 订单金额
        private double totalCost;

        // 实付金额
        private double payCost;

        // 订单状态 3 计费中 5 未支付
        private short state;

        // 车牌号
        private String plate;

        // 订单编号
        private String orderNo;

    }

    /**
     * List -> JSONArray.
     * @param payOrders
     * @return
     */
    public JSONArray PayOrdersConvertJSONArray(final List<Order> payOrders) {
        JSONArray jsonArray = new JSONArray();
        payOrders.forEach(item -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("beginTime", item.getBeginTime());
            jsonObject.put("code", item.getCode());
            jsonObject.put("endTime", item.getEndTime());
            jsonObject.put("totalCost", item.getTotalCost());
            jsonObject.put("payCost", item.getPayCost());
            jsonObject.put("state", item.getState());
            jsonObject.put("plate", item.getPlate());
            jsonObject.put("orderNo", item.getOrderNo());
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }

    /**
     * 将对象转成字符串.
     * @param orderPaid
     * @return
     */
    public String OrderPaidConvert(final OrderPaid orderPaid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", orderPaid.getOrderId());
        jsonObject.put("totalCost", orderPaid.getTotalCost());
        jsonObject.put("payCost", orderPaid.getPayCost());
        jsonObject.put("deduCost", orderPaid.getDeduCost());
        jsonObject.put("deduType", orderPaid.getDeduType());
        jsonObject.put("payTime", orderPaid.getPayTime());
        jsonObject.put("tradeNo", orderPaid.getTradeNo());
        jsonObject.put("payStyle", orderPaid.getPayStyle());
        jsonObject.put("payOrders", PayOrdersConvertJSONArray(orderPaid.getPayOrders()));
        return jsonObject.toJSONString();
    }

}
