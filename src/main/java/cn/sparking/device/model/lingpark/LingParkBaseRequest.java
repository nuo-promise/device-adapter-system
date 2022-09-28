package cn.sparking.device.model.lingpark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LingParkBaseRequest {
    // 身份ID,领泊发放
    String appid;

    // 身份ID
    String comId;

    // 模块名称
    String module;

    // 服务名称
    String service;

    // 方法名称
    String method;

    // 事件推送时间
    String timestamp;

    // 接口版本
    String ve;

    //签名
    String sign;
}
