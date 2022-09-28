package cn.sparking.device.model.lingpark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LPDeviceControlInfo extends LingParkBaseRequest {
    String pushData;

    @Data
    static class DeviceControlInfo {
       String deviceSn;
       String code;
       String plate;
       String inTime;
       String outTime;
       short type;
    }
}
