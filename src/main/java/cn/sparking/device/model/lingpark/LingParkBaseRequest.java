package cn.sparking.device.model.lingpark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LingParkBaseRequest {
    // 身份ID,领泊发放
    private String appId;

    // 身份ID
    private String comId;

    // 模块名称
    private String module;

    // 服务名称
    private String service;

    // 方法名称
    private String method;

    // 事件推送时间
    private String timestamp;

    // 接口版本
    private String ve;

    //签名
    private String sign;
}
