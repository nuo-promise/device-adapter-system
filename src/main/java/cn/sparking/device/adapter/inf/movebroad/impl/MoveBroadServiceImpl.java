package cn.sparking.device.adapter.inf.movebroad.impl;

import cn.sparking.device.adapter.factory.AdapterManager;
import cn.sparking.device.adapter.inf.movebroad.MoveBroadService;
import cn.sparking.device.constant.MoveBroadConstants;
import cn.sparking.device.model.movebroad.LockCallBackRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MoveBroadServiceImpl implements MoveBroadService {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadServiceImpl.class);

    private final AdapterManager adapterManager;

    public MoveBroadServiceImpl(final AdapterManager adapterManager) {
        this.adapterManager = adapterManager;
    }

    @Override
    public HttpStatus lockCallback(final LockCallBackRequest lockCallBackRequest) {
        try {
            adapterManager.getAdaptedService(MoveBroadConstants.MOVE_BROAD_ADAPTER).adapted(lockCallBackRequest);
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return HttpStatus.OK;
    }
}
