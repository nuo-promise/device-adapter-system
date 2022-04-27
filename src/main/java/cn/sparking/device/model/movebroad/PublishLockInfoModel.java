package cn.sparking.device.model.movebroad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishLockInfoModel implements Serializable {

    private static final long serialVersionUID = -454265349077577157L;

    // 地锁编号
    private String lockCode;

    //设备电池电压
    private int batt;

    // 设备温度
    private int temp;

    // 物联网卡卡号
    private String sim;

    // 联网模组编号
    private int imei;
}
