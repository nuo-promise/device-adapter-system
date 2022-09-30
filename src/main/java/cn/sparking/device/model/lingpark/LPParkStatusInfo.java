package cn.sparking.device.model.lingpark;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 驻车器车位状态信息.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LPParkStatusInfo extends LingParkBaseRequest {

    String pushData;

    @Data
    @Builder
    static class ParkStatusInfo {
        // 设备编号
        private String deviceSn;
        // 车位状态 5 有车 2 空闲
        private short car;

        // 取车编码
        private String code;

        // 翻板状态 1 升起 2 降下 3 未知
        private short lock;

        // 联网状态 1 在线 0 离线
        private short netState;
    }

    /**
     * 将字符串转成对象.
     * @return ParkingInfo
     */
    public ParkStatusInfo PushDataConvertParkStatusInfo() {
        return JSONObject.parseObject(pushData, ParkStatusInfo.class);
    }
}
