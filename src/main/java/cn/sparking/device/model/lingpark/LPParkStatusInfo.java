package cn.sparking.device.model.lingpark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LPParkStatusInfo extends LingParkBaseRequest {
    String pushData;
}
