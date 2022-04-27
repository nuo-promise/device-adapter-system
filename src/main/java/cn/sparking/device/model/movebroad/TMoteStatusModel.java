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
public class TMoteStatusModel implements Serializable {

    private static final long serialVersionUID = -7762129597074175809L;

    @JsonProperty("Status")
    private int status;

    @JsonProperty("Count")
    private int count;

    @JsonProperty("Time")
    private String time;

    @JsonProperty("Rssi")
    private int rssi;

    @JsonProperty("Snr")
    private int snr;
}
