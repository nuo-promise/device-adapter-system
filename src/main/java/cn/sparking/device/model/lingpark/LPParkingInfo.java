package cn.sparking.device.model.lingpark;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆进出推送信息.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LPParkingInfo extends LingParkBaseRequest {

    // 车辆进出场信息
    private String pushData;

    @Data
    @Builder
    static class ParkingInfo {
        // 设备编号
        private String deviceSn;
        // 取车编码
        private String code;
        // 车牌
        private String plate;
        // 入场时间
        private String inTime;
        // 离场时间
        private String outTime;
        // 进出类型 1 入场 2 离场
        private short type;
        // 离场类型 1 正常离场 2 逃逸离场
        private short outType;
    }

    /**
     * 将字符串转成对象.
     * @return ParkingInfo
     */
    public ParkingInfo PushDataConvertParkingInfo() {
        return JSONObject.parseObject(pushData, ParkingInfo.class);
    }
}
