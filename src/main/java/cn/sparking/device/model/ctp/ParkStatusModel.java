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
public class ParkStatusModel implements Serializable {

    private static final long serialVersionUID = -16026974984618756L;

    // 设备编码
    @JSONField(name = "DeviceNo")
    private String deviceNo;

    // 数据类型
    @JSONField(name = "DataType")
    private Integer dataType;

    // 电池电压
    @JSONField(name = "Voltage")
    private String voltage;

    // 状态码1 8位16进制字符串
    @JSONField(name = "StatusOne")
    private String statusOne;

    // 状态码2 12位16进制字符串
    @JSONField(name = "StatusTwo")
    private String statusTwo;

    // 数据时间 yyyy-MM-dd HH:mm:ss
    @JSONField(name = "DataTime")
    private String dataTime;
}
