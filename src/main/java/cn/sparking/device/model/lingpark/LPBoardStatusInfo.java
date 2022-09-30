package cn.sparking.device.model.lingpark;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LPBoardStatusInfo extends LingParkBaseRequest {
    private String pushData;

    @Data
    @Builder
    static class BoardStatus {
        // 设备编号
        private String deviceSn;

        // 取车编码
        private String code;

        // 翻板状态 1 升起 2 降下 3 未知
        private short lock;

        // 1 有车升翻板 2 降翻板
        private short type;
    }

    /**
     * 将字符串转成对象.
     * @return ParkingInfo
     */
    public BoardStatus PushDataConvertBoardStatus() {
        return JSONObject.parseObject(pushData, BoardStatus.class);
    }

}
