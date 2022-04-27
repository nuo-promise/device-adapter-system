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
public class LockInfoRequest implements Serializable {

    private static final long serialVersionUID = 1925381908457336770L;

    private String sn;

    private String name;

    private JSONObject tMoteInfo;
}
