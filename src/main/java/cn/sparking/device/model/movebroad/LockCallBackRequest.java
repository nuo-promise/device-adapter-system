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
public class LockCallBackRequest implements Serializable {

    private static final long serialVersionUID = -4261324766688362021L;

    @JsonProperty("SN")
    private String sn;

    @JsonProperty("BerthCode")
    private String berthCode;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("TMoteStatus")
    private TMoteStatusModel tMoteStatus;

    @JsonProperty("TMoteInfo")
    private TMoteInfoModel tMoteInfo;
}
