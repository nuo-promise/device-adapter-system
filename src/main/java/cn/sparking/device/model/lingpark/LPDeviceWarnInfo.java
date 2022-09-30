package cn.sparking.device.model.lingpark;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车位锁告警信息.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LPDeviceWarnInfo extends LingParkBaseRequest {

   private String pushData;

   @Data
   @Builder
   static class ParkWarnInfo {
      // 设备编号
      private String deviceSn;

      // 取车编码
      private String code;

      // 告警开始时间
      private String beginTime;

      // 1 重要 2 提示
      private short level;

      // 25 离线 39 偏低 38 平缓 15 车检
      private short type;

      // 19 离线 69 偏低 68 平缓 50 车检
      private short warningCode;

      // 告警内容
      private String warningInfo;
   }

   /**
    * 将字符串转成对象.
    * @return
    */
   public ParkWarnInfo PushDataConvertParkWarnInfo() {
      return JSONObject.parseObject(pushData, ParkWarnInfo.class);
   }
}
