package cn.sparking.device.controller.ctp;

import cn.sparking.device.adapter.service.ctp.CtpService;
import cn.sparking.device.model.ctp.ControlModel;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ctp")
public class CtpController {

    private final CtpService ctpService;

    public CtpController(final CtpService ctpService) {
        this.ctpService = ctpService;
    }

    @ResponseBody
    @PostMapping("/ctpParkStatus")
    public JSONObject parkStatus(@RequestHeader("Sign") String sign, @RequestBody final ParkStatusModel parkStatusModel) {
        return ctpService.parkStatus(sign, parkStatusModel);
    }

    @ResponseBody
    @GetMapping("/ctpWorkModel")
    public JSONObject workModel(@RequestHeader("Sign") String sign, @RequestParam("DeviceNo")final String deviceNo) {
        return ctpService.workMode(sign, deviceNo);
    }

    @ResponseBody
    @GetMapping("/ctpSearchBoard")
    public JSONObject searchBoard(@RequestHeader("Sign") String sign, @RequestParam("DeviceNo") final String deviceNo) {
        return ctpService.searchBoard(sign, deviceNo);
    }

    @ResponseBody
    @PostMapping("/ctpControlCmd")
    public DeviceAdapterResult controlCmd(@RequestBody final ControlModel controlModel) {
        return ctpService.controlCmd(controlModel);
    }
}
