package cn.sparking.device.controller.lingpark;

import cn.sparking.device.adapter.service.lingpark.LingParkService;
import cn.sparking.device.model.lingpark.LPBoardStatusInfo;
import cn.sparking.device.model.lingpark.LPDeviceWarnInfo;
import cn.sparking.device.model.lingpark.LPParkStatusInfo;
import cn.sparking.device.model.lingpark.LPParkingInfo;
import cn.sparking.device.model.lingpark.LingControlModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import cn.sparking.device.model.response.lingpark.LingParkResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ling-park")
public class LingParkController {

    private final LingParkService lingParkService;

    public LingParkController(final LingParkService lingParkService) {
        this.lingParkService = lingParkService;
    }

    /**
     * 车辆进出信息推送.
     * 1. 先推送的数据目的地验证
     * 2. 所有非空字符按照ascii码排序验证加密
     * @param lpParkingInfo {@link LPParkingInfo}
     * @return {@link LingParkResponse}
     */
    @ResponseBody
    @PostMapping("/lingParking")
    public LingParkResponse parkingStatus(@RequestBody final LPParkingInfo lpParkingInfo) {
        return lingParkService.parkingStatus(lpParkingInfo);
    }

    /**
     * 翻板升降结果.
     * @param lpBoardStatusInfo {@link LPBoardStatusInfo}
     * @return {@link LingParkResponse}
     */
    @ResponseBody
    @PostMapping("/lingBoardStatus")
    public LingParkResponse boardStatus(@RequestBody final LPBoardStatusInfo lpBoardStatusInfo) {
        return lingParkService.boardStatus(lpBoardStatusInfo);
    }


    /**
     * 驻车器车位状态信息.
     * @param lpParkStatusInfo {@link LPParkStatusInfo}
     * @return {@link LingParkResponse}
     */
    @ResponseBody
    @PostMapping("/lingParkStatus")
    public LingParkResponse parkStatus(@RequestBody final LPParkStatusInfo lpParkStatusInfo) {
       return lingParkService.parkStatus(lpParkStatusInfo);
    }

    /**
     * 驻车器车位告警.
     * @param lpDeviceWarnInfo {@link LPDeviceWarnInfo}
     * @return {@link LingParkResponse}
     */
    @ResponseBody
    @PostMapping("/lingDeviceWarn")
    public LingParkResponse parkWarn(@RequestBody final LPDeviceWarnInfo lpDeviceWarnInfo) {
        return lingParkService.parkWarn(lpDeviceWarnInfo);
    }

    /**
     * ling 控制降板命令.
     * @param lingControlModel {@link LingControlModel}
     * @return {@link DeviceAdapterResult}
     */
    @ResponseBody
    @PostMapping("/lingControlCmd")
    public DeviceAdapterResult controlCmd(@RequestBody final LingControlModel lingControlModel) {
       return lingParkService.controlCmd(lingControlModel);
    }
}
