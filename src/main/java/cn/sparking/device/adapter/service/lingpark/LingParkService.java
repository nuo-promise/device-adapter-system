package cn.sparking.device.adapter.service.lingpark;

import cn.sparking.device.model.lingpark.LPBoardStatusInfo;
import cn.sparking.device.model.lingpark.LPDeviceWarnInfo;
import cn.sparking.device.model.lingpark.LPParkStatusInfo;
import cn.sparking.device.model.lingpark.LPParkingInfo;
import cn.sparking.device.model.lingpark.LingControlModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import cn.sparking.device.model.response.lingpark.LingParkResponse;

public interface LingParkService {

    /**
     * 车辆进出动作信息推送.
     * @param lpParkingInfo {@link LPParkingInfo}
     * @return {@link LingParkResponse}
     */
    LingParkResponse parkingStatus(LPParkingInfo lpParkingInfo);

    /**
     * 翻板升降结果.
     * @param lpBoardStatusInfo {@Link LPBoardStatusInfo}
     * @return {@link LingParkResponse}
     */
    LingParkResponse boardStatus(LPBoardStatusInfo lpBoardStatusInfo);

    /**
     * 驻车器车位状态信息.
     * @param lpParkStatusInfo {@link LPParkStatusInfo}
     * @return {@link LingParkResponse}
     */
    LingParkResponse parkStatus(LPParkStatusInfo lpParkStatusInfo);

    /**
     * 设备告警信息.
     * @param lpDeviceWarnInfo {@link LPDeviceWarnInfo}
     * @return {@link LingParkResponse}
     */
    LingParkResponse parkWarn(LPDeviceWarnInfo lpDeviceWarnInfo);

    /**
     * 地锁控制接口.
     * @param lingControlModel {@link LingControlModel}
     * @return {@link DeviceAdapterResult}
     */
    DeviceAdapterResult controlCmd(LingControlModel lingControlModel);
}
