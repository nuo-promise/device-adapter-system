package cn.sparking.device.model.movebroad;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TMoteInfoModel implements Serializable {

    private static final long serialVersionUID = -5982512576737612080L;

    // 电池电压
    @JSONField(name = "Batt")
    private int batt;

    // 设备温度
    @JSONField(name = "Temp")
    private int temp;

    // 物联网卡的卡号
    @JSONField(name = "Sim")
    private String sim;

    // 设备联网模组编号
    @JSONField(name = "Imei")
    private int imei;
}
