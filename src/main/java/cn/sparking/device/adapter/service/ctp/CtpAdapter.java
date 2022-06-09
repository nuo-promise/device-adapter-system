package cn.sparking.device.adapter.service.ctp;

import cn.sparking.device.adapter.BaseAdapter;
import cn.sparking.device.adapter.service.movebroad.MoveBroadAdapter;
import cn.sparking.device.configure.properties.RabbitmqProperties;
import cn.sparking.device.constant.CtpConstants;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.ctp.CtpRequest;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.ctp.SearchBoardModel;
import cn.sparking.device.model.ctp.WorkModeModel;
import cn.sparking.device.tools.DateTimeUtils;
import cn.sparking.device.tools.ReactiveRedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static cn.sparking.device.constant.CtpConstants.BTN_DOWN;
import static cn.sparking.device.constant.CtpConstants.CTP_TYPE;
import static cn.sparking.device.constant.CtpConstants.FREE_PARK;
import static cn.sparking.device.constant.CtpConstants.HEARTBEAT;
import static cn.sparking.device.constant.CtpConstants.ONLINE;
import static cn.sparking.device.constant.CtpConstants.RESET;
import static cn.sparking.device.constant.CtpConstants.STATUS_UPLOAD;
import static cn.sparking.device.constant.CtpConstants.WORK_ALL_TIME;
import static cn.sparking.device.exception.CtpErrorCode.SUCCESS;

@Component("CtpAdapter")
public abstract class CtpAdapter extends BaseAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadAdapter.class);

    private static final int REDIS_EXPIRED_INTERVAL = 400;

    @Resource
    private RabbitmqProperties rabbitmqProperties;

    public CtpAdapter() {
        super(CtpConstants.CTP_ADAPTER);
    }

    @Override
    public Object adapted(Object parameters) {
        try {
            CtpRequest<Object> ctpRequest = (CtpRequest<Object>) parameters;
            if (Objects.nonNull(ctpRequest)) {
                switch(ctpRequest.getCmd()) {
                    case CtpConstants.CTP_REQUEST_PARK_STATUS:
                        ParkStatusModel parkStatusModel = JSON.parseObject(JSON.toJSONString(ctpRequest.getBody()), ParkStatusModel.class);
                        return parkStatus(parkStatusModel);
                    case CtpConstants.CTP_REQUEST_WORK_MODE:
                        WorkModeModel workModeModel = JSON.parseObject(JSON.toJSONString(ctpRequest.getBody()), WorkModeModel.class);
                        return workModel(workModeModel);
                    case CtpConstants.CTP_REQUEST_SEARCH_BOARD:
                        SearchBoardModel searchBoardModel = JSON.parseObject(JSON.toJSONString(ctpRequest.getBody()), SearchBoardModel.class);
                        return searchBoard(searchBoardModel);
                    default:
                        LOG.warn("CTP CMD: " + ctpRequest.getCmd() + " Have Not Match");
                        break;
                }
            }
        } catch (SparkingException e) {
            LOG.error(CtpConstants.CTP_FLAG + " adapted Error: " + e);
        }
        return null;
    }

    private JSONObject parkStatus(final ParkStatusModel parkStatusModel) {
        JSONObject result = new JSONObject();
        return Optional.ofNullable(parkStatusModel).map(item -> {
            boolean parkStatus = false;
            int status = ONLINE;
            String armsStatus = "DOWN";
            item.setLockType(CTP_TYPE);
            item.setStatus(ONLINE);
            item.setWarn(false);
            // 数泊平台接收到数据时间
            item.setEventTime(DateTimeUtils.currentSecond());
            switch(parkStatusModel.getDataType()) {
                case CtpConstants.ARMS_DOWN:
                    armsStatus = "DOWN";
                    break;
                case CtpConstants.ARMS_UP:
                    armsStatus = "UP";
                    break;
                case HEARTBEAT:
                    status = HEARTBEAT;
                    break;
                case CtpConstants.PARK_ENTER:
                    parkStatus = true;
                    break;
                case CtpConstants.PARK_LEAVE:
                    parkStatus = false;
                    break;
                case RESET:
                    status = RESET;
                    break;
                case FREE_PARK:
                    status = FREE_PARK;
                    break;
                case STATUS_UPLOAD:
                    status = STATUS_UPLOAD;
                    break;
                case BTN_DOWN:
                    status = BTN_DOWN;
                    break;
                default:
                    LOG.warn("CTP Request Method not Support => " + item.getLockCode() + "-" + item.getDataType());
                    break;
            }
            item.setStatus(status);
            item.setParkStatus(parkStatus);
            item.setArmsStatus(armsStatus);
            LOG.info("CTP Build PublishLockStatus Data: " + JSON.toJSONString(item));

            /**
             * save redis
             */
            opsValue(item);
            /**
             * producer mq.
             */
            new CtpProducer(rabbitmqProperties).publishLockStatus(item);
            result.put("ErrorCode", SUCCESS);
            result.put("ErrorMsg", "操作成功");
            return result;
        }).orElse(null);
    }

    /**
     * 工作模式,根据设备编号请求设置的工作模式.
     * @param workModeModel {@link WorkModeModel}
     * @return {@link JSONObject}
     */
    private JSONObject workModel(final WorkModeModel workModeModel) {
       JSONObject result = new JSONObject();
       result.put("ErrorCode", SUCCESS);
       result.put("ErrorMsg", "操作成功");
       result.put("WorkMode", WORK_ALL_TIME);
       return result;
    }

    /**
     * 查询是否可以降板.
     * @param searchBoard {@link SearchBoardModel}
     * @return {@link JSONObject}
     */
    private JSONObject searchBoard(final SearchBoardModel searchBoard) {
        JSONObject result = new JSONObject();
        result.put("ErrorCode", SUCCESS);
        result.put("ErrorMsg", "操作成功");
        return result;
    }
    @Override
    public Object antiAdapted(Object parameters) {
        return null;
    }

    /**
     * save data.
     * @param parkStatusModel {@link ParkStatusModel}
     */
    private void opsValue(final ParkStatusModel parkStatusModel) {
        ReactiveRedisUtils.putValue(parkStatusModel.getLockCode(), JSON.toJSONString(parkStatusModel), REDIS_EXPIRED_INTERVAL).subscribe(
                flag -> {
                    if (flag) {
                        LOG.info("CTP Lock Key= " + parkStatusModel.getLockCode() + " save redis success!");
                    } else {
                        LOG.info("CTP Lock Key= " + parkStatusModel.getLockCode() + " save redis failed!");
                    }
                }
        );
    }
}
