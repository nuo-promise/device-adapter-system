package cn.sparking.device.model.lingpark;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LPDeviceControlInfo extends LingParkBaseRequest {

   private String pushData;

   @Data
   @Builder
   static class DeviceControl {

         // 设备编号
         private String deviceSn;

         // 1 升起(暂不开放) 2 降下
         private short type;

         // 取车编码
         private String code;
   }

    /**
     * 将对象转换成json 字符.
     * @param deviceControl deviceControl
     * @return String
     */
   public String DeviceControlConvert(final DeviceControl deviceControl) {
       JSONObject jsonObject = new JSONObject();
       jsonObject.put("code", deviceControl.getCode());
       jsonObject.put("deviceSn", deviceControl.getDeviceSn());
       jsonObject.put("type", deviceControl.getType());
       return jsonObject.toJSONString();
   }
}
