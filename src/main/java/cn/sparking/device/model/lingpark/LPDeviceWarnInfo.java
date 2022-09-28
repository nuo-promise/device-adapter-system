package cn.sparking.device.model.lingpark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LPDeviceWarnInfo extends LingParkBaseRequest {
    String pushData;
}
