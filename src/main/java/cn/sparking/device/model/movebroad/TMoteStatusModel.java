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
public class TMoteStatusModel implements Serializable {

    private static final long serialVersionUID = -7762129597074175809L;

    @JSONField(name = "Status")
    private int status;

    @JSONField(name = "Count")
    private int count;

    @JSONField(name = "Time")
    private String time;

    @JSONField(name = "Rssi")
    private int rssi;

    @JSONField(name = "Snr")
    private int snr;
}
