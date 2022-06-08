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
public class SearchBoardModel implements Serializable {
    private static final long serialVersionUID = -4111961419171951078L;

    @JSONField(name = "DeviceNo")
    private String deviceNo;
}
