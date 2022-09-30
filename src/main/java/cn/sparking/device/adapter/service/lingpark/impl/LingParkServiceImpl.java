package cn.sparking.device.adapter.service.lingpark.impl;

import cn.sparking.device.adapter.service.lingpark.LingParkService;
import cn.sparking.device.model.lingpark.LPBoardStatusInfo;
import cn.sparking.device.model.lingpark.LPDeviceWarnInfo;
import cn.sparking.device.model.lingpark.LPParkStatusInfo;
import cn.sparking.device.model.lingpark.LPParkingInfo;
import cn.sparking.device.model.lingpark.LingControlModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import cn.sparking.device.model.response.lingpark.LingParkResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LingParkServiceImpl implements LingParkService {
    @Override
    public LingParkResponse parkingStatus(LPParkingInfo lpParkingInfo) {
        return null;
    }

    @Override
    public LingParkResponse boardStatus(LPBoardStatusInfo lpBoardStatusInfo) {
        return null;
    }

    @Override
    public LingParkResponse parkStatus(LPParkStatusInfo lpParkStatusInfo) {
        return null;
    }

    @Override
    public LingParkResponse parkWarn(LPDeviceWarnInfo lpDeviceWarnInfo) {
        return null;
    }

    @Override
    public DeviceAdapterResult controlCmd(LingControlModel lingControlModel) {
        return null;
    }
}
