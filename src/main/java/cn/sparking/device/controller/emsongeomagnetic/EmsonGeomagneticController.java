package cn.sparking.device.controller.emsongeomagnetic;

import cn.sparking.device.adapter.service.emsongeomagnetic.EmsonGeomagneticService;
import cn.sparking.device.model.emsongeomagnetic.EmsonGeomagneticRequest;
import cn.sparking.device.model.response.emsongeomagnetic.EmsonGeomagneticResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/EmersonGeomagnetic")
public class EmsonGeomagneticController {

    private final EmsonGeomagneticService emsonGeomagneticService;

    public EmsonGeomagneticController(final EmsonGeomagneticService emsonGeomagneticService) {
        this.emsonGeomagneticService = emsonGeomagneticService;
    }

    /**
     * process api.
     * @param emsonRequest request params
     * @return {@link EmsonGeomagneticResponse}
     */
    @PostMapping(value = "/api")
    public EmsonGeomagneticResponse processApi(@RequestBody final EmsonGeomagneticRequest<Object> emsonRequest) {
        return emsonGeomagneticService.processApi(emsonRequest);
    }
}
