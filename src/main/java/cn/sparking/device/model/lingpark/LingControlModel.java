package cn.sparking.device.model.lingpark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LingControlModel {
    // 设备编号
    private String deviceSn;

    // 1 升起(暂不开放) 2 降下
    private short type;
}
