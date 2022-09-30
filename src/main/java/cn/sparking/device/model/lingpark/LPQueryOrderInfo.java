package cn.sparking.device.model.lingpark;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询车位订单信息.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LPQueryOrderInfo extends LingParkBaseRequest {

    private String searchInfo;

    @Data
    @Builder
    static class QueryOrder {
        // 取车编码
        private String code;
    }

    /**
     * 将对象转换成json 字符.
     * @param queryOrder deviceControl
     * @return String
     */
    public String QueryOrderConvert(final QueryOrder queryOrder) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", queryOrder.getCode());
        return jsonObject.toJSONString();
    }
}
