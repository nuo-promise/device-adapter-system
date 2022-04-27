package cn.sparking.device.model.movebroad;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockCmdModel implements Serializable {

    private static final long serialVersionUID = -3311008802103345947L;

    private String sn;

    private String method;

    private JSONObject param;
}
