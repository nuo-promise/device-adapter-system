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
public class PublishLockStatusModel implements Serializable {

    private static final long serialVersionUID = -1879252639557334379L;

    // 地锁编号
    private String lockCode;

    // 车位编号
    private String parkingCode;

    // 地锁类型
    private String lockType;

    // false: 离线 true:在线
    private Boolean status;

    // false: 无车  true: 有车
    private Boolean parkStatus;

    // UP 上升  DOWN 下降
    private String armsStatus;

    // true 报警 false 未报警
    private Boolean warn;

    // 设备状态变化计数 0-65535
    private int count;

    // 状态变化时间 YYYY-MM-DD HH:mm:ss
    private String time;

    // 设备信号强度
    private int rssi;

    // 设备信噪比
    private int snr;

    // 状态变化时间
    private Long eventTime;
}
