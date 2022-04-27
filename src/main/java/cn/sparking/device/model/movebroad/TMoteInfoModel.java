package cn.sparking.device.model.movebroad;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("Batt")
    private int batt;

    // 设备温度
    @JsonProperty("Temp")
    private int temp;

    // 物联网卡的卡号
    @JsonProperty("Sim")
    private String sim;

    // 设备联网模组编号
    @JsonProperty("Imei")
    private int imei;
}
