package cn.sparking.device.model.ctp;

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
public class WorkModeModel implements Serializable {
    private static final long serialVersionUID = -3827345749793344359L;

    @JSONField(name = "DeviceNo")
    private String deviceNo;
}
