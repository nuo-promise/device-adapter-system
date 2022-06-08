package cn.sparking.device.adapter.service.ctp;

import cn.sparking.device.adapter.BaseAdapter;
import cn.sparking.device.adapter.service.movebroad.MoveBroadAdapter;
import cn.sparking.device.configure.properties.RabbitmqProperties;
import cn.sparking.device.constant.CtpConstants;
import cn.sparking.device.constant.EmsonGeomagneticConstants;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.ctp.CtpRequest;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.ctp.SearchBoardModel;
import cn.sparking.device.model.ctp.WorkModeModel;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component("CtpAdapter")
public class CtpAdapter extends BaseAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadAdapter.class);

    private static final int REDIS_EXPIRED_INTERVAL = 400;

    @Resource
    private RabbitmqProperties rabbitmqProperties;

    public CtpAdapter() {
        super(CtpConstants.CTP_ADAPTER);
    }

    @Override
    public void adapted(Object parameters) {
        try {
            CtpRequest<Object> ctpRequest = (CtpRequest<Object>) parameters;
            if (Objects.nonNull(ctpRequest)) {
                switch(ctpRequest.getCmd()) {
                    case CtpConstants.CTP_REQUEST_PARK_STATUS:
                        ParkStatusModel parkStatusModel = JSON.parseObject(JSON.toJSONString(ctpRequest.getBody()), ParkStatusModel.class);
                        break;
                    case CtpConstants.CTP_REQUEST_WORK_MODE:
                        WorkModeModel workModeModel = JSON.parseObject(JSON.toJSONString(ctpRequest.getBody()), WorkModeModel.class);
                        break;
                    case CtpConstants.CTP_REQUEST_SEARCH_BOARD:
                        SearchBoardModel searchBoardModel = JSON.parseObject(JSON.toJSONString(ctpRequest.getBody()), SearchBoardModel.class);
                        break;
                    default:
                        LOG.warn("CTP CMD: " + ctpRequest.getCmd() + " Have Not Match");
                        break;
                }
            }
        } catch (SparkingException e) {
            LOG.error(CtpConstants.CTP_FLAG + " adapted Error: " + e);
        }
    }

    @Override
    public void antiAdapted(Object parameters) {

    }
}
