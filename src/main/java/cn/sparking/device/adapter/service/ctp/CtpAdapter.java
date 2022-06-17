package cn.sparking.device.adapter.service.ctp;

import cn.hutool.http.HttpResponse;
import cn.sparking.device.adapter.BaseAdapter;
import cn.sparking.device.adapter.service.movebroad.MoveBroadAdapter;
import cn.sparking.device.configure.properties.RabbitmqProperties;
import cn.sparking.device.constant.CtpConstants;
import cn.sparking.device.exception.SparkingCommonCode;
import cn.sparking.device.exception.SparkingException;
import cn.sparking.device.model.ctp.CtpRequest;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.ctp.SearchBoardModel;
import cn.sparking.device.model.ctp.WorkModeModel;
import cn.sparking.device.model.response.ctp.WorkModeResponse;
import cn.sparking.device.tools.DateTimeUtils;
import cn.sparking.device.tools.HttpUtils;
import cn.sparking.device.tools.ReactiveRedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static cn.sparking.device.constant.CtpConstants.ARMS_DOWN;
import static cn.sparking.device.constant.CtpConstants.ARMS_UP;
import static cn.sparking.device.constant.CtpConstants.BTN_DOWN;
import static cn.sparking.device.constant.CtpConstants.CTP_DATA_SEND_RESOURCE;
import static cn.sparking.device.constant.CtpConstants.CTP_SEARCH_BOARD;
import static cn.sparking.device.constant.CtpConstants.CTP_TYPE;
import static cn.sparking.device.constant.CtpConstants.CTP_WORK_MODEL;
import static cn.sparking.device.constant.CtpConstants.DATA_ARM_SAVE;
import static cn.sparking.device.constant.CtpConstants.DATA_EVENT;
import static cn.sparking.device.constant.CtpConstants.DATA_SAVE;
import static cn.sparking.device.constant.CtpConstants.FREE_PARK;
import static cn.sparking.device.constant.CtpConstants.HEARTBEAT;
import static cn.sparking.device.constant.CtpConstants.ONLINE;
import static cn.sparking.device.constant.CtpConstants.PARK_ENTER;
import static cn.sparking.device.constant.CtpConstants.PARK_LEAVE;
import static cn.sparking.device.constant.CtpConstants.RESET;
import static cn.sparking.device.constant.CtpConstants.STATUS_UPLOAD;
import static cn.sparking.device.exception.CtpErrorCode.DATA_SUCCESS;
import static cn.sparking.device.exception.CtpErrorCode.DEVICE_NOT_RECORD;
import static cn.sparking.device.exception.CtpErrorCode.SUCCESS;

@Component("CtpAdapter")
public class CtpAdapter extends BaseAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadAdapter.class);

    private static final int REDIS_EXPIRED_INTERVAL = 400;

    @Value("${sparking.shard.data.url}")
    private String shard_data_url;

    @Value("${sparking.bs.url}")
    private String bs_url;

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
                        return workModel((String) ctpRequest.getBody());
                    case CtpConstants.CTP_REQUEST_SEARCH_BOARD:
                        return searchBoard((String) ctpRequest.getBody());
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
            Integer dataType = parkStatusModel.getDataType();
            String dataSendType = "";
            switch(dataType) {
                case ARMS_DOWN:
                    armsStatus = "DOWN";
                    break;
                case ARMS_UP:
                    armsStatus = "UP";
                    break;
                case HEARTBEAT:
                    status = HEARTBEAT;
                    break;
                case PARK_ENTER:
                    parkStatus = true;
                    break;
                case PARK_LEAVE:
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
                    result.put("ErrorCode", DEVICE_NOT_RECORD);
                    result.put("ErrorMsg", "DataType 未识别");
                    break;
            }
            // 常规的事件类型
            if (dataType.equals(HEARTBEAT) || dataType.equals(RESET)
                    || dataType.equals(FREE_PARK) || dataType.equals(STATUS_UPLOAD) || dataType.equals(BTN_DOWN)) {
               dataSendType = DATA_EVENT;
            } else if(dataType.equals(PARK_ENTER) || dataType.equals(PARK_LEAVE)) {
                // 车位状态
                dataSendType = DATA_SAVE;
            } else if(dataType.equals(ARMS_DOWN) || dataType.equals(ARMS_UP)) {
                // 挡板状态
               dataSendType = DATA_ARM_SAVE;
            }
            else {
                return result;
            }
            // 如果数据上传是save类型的那么只更新 车位状态,其他的就是事件类型
            item.setStatus(status);
            item.setParkStatus(parkStatus);
            item.setArmsStatus(armsStatus);

            /**
             * save redis
             */
            opsValue(item);
            /**
             * producer mq.
             */

            if (new CtpProducer(rabbitmqProperties).publishLockStatus(item, dataSendType) == SUCCESS) {
               LOG.info("CTP PublishParkStatus 发送成功,对方已收到" + JSON.toJSONString(item));
            } else {
               if (dataSendType.equals(DATA_SAVE)) {
                   LOG.info("CTP PublishParkStatus ERROR => C 端消费 RPC 设备状态信息返回为空, 下面执行 HTTP 请求: " + JSON.toJSONString(item));
                   JSONObject httpResponse = sendParkStatusDataByHttp(JSON.toJSONString(new CtpProducer.CtpData(JSON.toJSONString(item), dataSendType)));
                   if (Objects.isNull(httpResponse) || !httpResponse.containsKey("code") || !httpResponse.getInteger("code").equals(SparkingCommonCode.SUCCESS)) {
                       LOG.error("HTTP sendParkStatusDataByHttp ERROR ==> [发送设备状态信息处理失败, 将数据保存日志,后续做落库操作] " + JSON.toJSONString(new CtpProducer.CtpData(JSON.toJSONString(item), "save")));
                   }
               } else {
                   LOG.info("事件类型,不是业务关心的车位数据,可以忽略: " + dataSendType);
               }
            }
            result.put("ErrorCode", SUCCESS);
            result.put("ErrorMsg", "操作成功");
            return result;
        }).orElse(null);
    }

    /**
     * MQ RPC 发送数据失败,那么就通过HTTP 发送数据.
     * @param requestParam {@Link String} 发送的数据
     * @return {@link JSONObject}
     */
    private JSONObject sendParkStatusDataByHttp(final String requestParam) {
       JSONObject request = new JSONObject();
       request.put("type", CTP_TYPE);
       request.put("message", requestParam);
       HttpResponse response = HttpUtils.post(shard_data_url + CTP_DATA_SEND_RESOURCE, request.toJSONString());
       if (Objects.isNull(response) || !response.isOk() || Objects.isNull(response.body())) {
           LOG.error("sendParkStatusDataByHttp Error");
       }
       return JSON.parseObject(response.body());
    }
    /**
     * 工作模式,根据设备编号请求设置的工作模式.
     * @param deviceNo {@link String}
     * @return {@link JSONObject}
     */
    private JSONObject workModel(final String deviceNo) {
        JSONObject result = new JSONObject();
        WorkModeResponse workModeResponse = getDeviceWorkModel(deviceNo);
        if (Objects.isNull(workModeResponse)) {
            result.put("ErrorCode", DEVICE_NOT_RECORD);
            result.put("ErrorMsg", "获取工作模式失败");
            return result;
        }
        result.put("ErrorCode", SUCCESS);
        result.put("ErrorMsg", "操作成功");
        result.put("WorkMode", workModeResponse.getWorkMode());
        result.put("WorkStart", workModeResponse.getWorkStart());
        result.put("WorkEnd", workModeResponse.getWorkEnd());
        return result;
    }

    /**
     * 根据设备编号,获取地锁的工作模式.
     * @param deviceNo deviceNO
     * @return {@link JSONObject}
     */
    private WorkModeResponse getDeviceWorkModel(final String deviceNo) {
       JSONObject requestObj = new JSONObject();
       requestObj.put("deviceNo", deviceNo);
       HttpResponse httpResponse = HttpUtils.post(bs_url + CTP_WORK_MODEL, requestObj.toJSONString());
       if (Objects.isNull(httpResponse) || !httpResponse.isOk() || Objects.isNull(httpResponse.body())) {
           LOG.error("CTP getDeviceWorkModel Error");
           return null;
       }
       JSONObject response = JSON.parseObject(httpResponse.body());
       if (Objects.isNull(response) || !response.containsKey("code") || !response.getString("code").equals("00000")) {
           LOG.warn("CTP getDeviceWorkModel Failed " + response.toJSONString());
           return null;
       }
       JSONObject data = response.getJSONObject("data");
       if (Objects.isNull(data)) {
           LOG.warn("CTP getDeviceWorkModel Data Failed " + response.toJSONString());
           return null;
       }
       return WorkModeResponse.builder()
               .workMode(data.getString("workMode"))
               .workStart(data.getString("workStart"))
               .workEnd(data.getString("workEnd"))
               .build();
    }

    /**
     * 根据deviceNo查询CTP 地锁是否可以降板.
     * @param deviceNo device no
     * @return {@link JSONObject}
     */
    private JSONObject findDeviceBoardStatus(final String deviceNo) {
        JSONObject requestObj = new JSONObject();
        requestObj.put("deviceNo", deviceNo);
        HttpResponse httpResponse = HttpUtils.post(shard_data_url + CTP_SEARCH_BOARD, requestObj.toJSONString());
        if (Objects.isNull(httpResponse) || !httpResponse.isOk() || Objects.isNull(httpResponse.body())) {
           LOG.error("CTP findDeviceBoardStatus Error");
           return null;
        }
        JSONObject response = JSON.parseObject(httpResponse.body());
        // code  message data
        if (Objects.isNull(response) || !response.containsKey("code") || !response.getInteger("code").equals(DATA_SUCCESS)) {
            LOG.warn("CTP findDeviceBoardStatus Failed " + response.toJSONString());
            return null;
        }
        JSONObject data = response.getJSONObject("data");
        if (Objects.isNull(data)) {
            LOG.warn("CTP findDeviceBoardStatus Data Failed " + response.toJSONString());
            return null;
        }
        JSONObject result  = new JSONObject();
        result.put("ErrorCode", SUCCESS);
        result.put("ErrorMsg", "操作成功");
        result.put("Status", data.getInteger("status"));
        return result;
    }

    /**
     * 查询是否可以降板.
     * @param deviceNo {@link String}
     * @return {@link JSONObject}
     */
    private JSONObject searchBoard(final String deviceNo) {
        JSONObject result = findDeviceBoardStatus(deviceNo);
        if (Objects.isNull(result)) {
            result.put("ErrorCode", DEVICE_NOT_RECORD);
            result.put("ErrorMsg", "获取工作模式失败");
            return result;
        }
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
