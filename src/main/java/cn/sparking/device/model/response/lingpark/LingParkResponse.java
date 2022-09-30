package cn.sparking.device.model.response.lingpark;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LingParkResponse {

    private JSONObject res;

    @Data
    static class CommonResponse {
       private String code;

       private String desc;

       private String procsTime;
    }
}
