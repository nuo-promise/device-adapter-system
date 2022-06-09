package cn.sparking.device.adapter.service.movebroad;

import cn.sparking.device.adapter.BaseAdapter;
import cn.sparking.device.configure.properties.RabbitmqProperties;
import cn.sparking.device.constant.MoveBroadConstants;
import cn.sparking.device.model.movebroad.LockCallBackRequest;
import cn.sparking.device.model.movebroad.LockInfoRequest;
import cn.sparking.device.model.movebroad.LockStatusEnum;
import cn.sparking.device.model.movebroad.LockStatusRequest;
import cn.sparking.device.model.movebroad.PublishLockInfoModel;
import cn.sparking.device.model.movebroad.PublishLockStatusModel;
import cn.sparking.device.model.movebroad.TMoteInfoModel;
import cn.sparking.device.model.movebroad.TMoteStatusModel;
import cn.sparking.device.tools.DateTimeUtils;
import cn.sparking.device.tools.ReactiveRedisUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

import static cn.sparking.device.constant.MoveBroadConstants.MB_REQUEST_TMOTEINFO;
import static cn.sparking.device.constant.MoveBroadConstants.MB_REQUEST_TMOTESTATUS;
import static cn.sparking.device.constant.MoveBroadConstants.MB_TYPE;

@Component("MoveBroadAdapter")
public class MoveBroadAdapter extends BaseAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(MoveBroadAdapter.class);

    private static final int REDIS_EXPIRED_INTERVAL = 400;

    @Resource
    private RabbitmqProperties rabbitmqProperties;

    public MoveBroadAdapter() {
        super(MoveBroadConstants.MOVE_BROAD_ADAPTER);
    }

    @Override
    public Object adapted(final Object parameters) {
        try {
            LOG.info("Receive MB Lock Status Info: " + JSON.toJSONString(parameters));
            LockCallBackRequest lockCallBackRequest = (LockCallBackRequest) parameters;
            if (Objects.nonNull(lockCallBackRequest)) {
                switch (lockCallBackRequest.getName()) {
                    case MB_REQUEST_TMOTESTATUS:
                        LockStatusRequest lockStatusRequest = LockStatusRequest.builder()
                                .sn(lockCallBackRequest.getSn())
                                .berthCode(lockCallBackRequest.getBerthCode())
                                .name(lockCallBackRequest.getName())
                                .tMoteStatus(lockCallBackRequest.getTMoteStatus())
                                .build();
                        lockStatus(lockStatusRequest);
                        break;
                    case MB_REQUEST_TMOTEINFO:
                        LockInfoRequest lockInfoRequest = LockInfoRequest.builder()
                                .sn(lockCallBackRequest.getSn())
                                .name(lockCallBackRequest.getName())
                                .tMoteInfo(lockCallBackRequest.getTMoteInfo())
                                .build();
                        lockInfo(lockInfoRequest);
                        break;
                    default:
                        LOG.warn("MoveBroad Request Method not Support => " + lockCallBackRequest.getName());
                        break;
                }
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
        return null;
    }

    @Override
    public Object antiAdapted(final Object parameters) {
        return null;
    }

    private void lockStatus(final LockStatusRequest lockStatusRequest) {
        PublishLockStatusModel publishLockStatusModel = PublishLockStatusModel.builder()
                .lockCode(lockStatusRequest.getSn())
                .parkingCode(lockStatusRequest.getBerthCode())
                .lockType(MB_TYPE)
                .eventTime(DateTimeUtils.currentSecond())
                .build();
        if (Objects.nonNull(lockStatusRequest.getTMoteStatus())) {
            TMoteStatusModel tMoteStatusModel = lockStatusRequest.getTMoteStatus();
            boolean parkStatus = false;
            String armsStatus = "UP";
            boolean warn = false;
            if (Objects.nonNull(tMoteStatusModel)) {
                publishLockStatusModel.setStatus(true);
                publishLockStatusModel.setCount(tMoteStatusModel.getCount());
                publishLockStatusModel.setTime(tMoteStatusModel.getTime());
                publishLockStatusModel.setRssi(tMoteStatusModel.getRssi());
                publishLockStatusModel.setSnr(tMoteStatusModel.getSnr());
                switch (LockStatusEnum.convert(tMoteStatusModel.getStatus())) {
                    case STATUS_UP_FREE_0:
                    case STATUS_UP_FREE_8:
                    case STATUS_UP_FREEING_2:
                    case STATUS_UP_FREEING_10:
                        parkStatus = false;
                        armsStatus = "UP";
                        warn = false;
                        break;
                    case STATUS_UP_NO_FREE_1:
                    case STATUS_UP_NO_FREE_9:
                    case STATUS_UP_NO_FREEING_3:
                    case STATUS_UP_NO_FREEING_11:
                        parkStatus = true;
                        armsStatus = "UP";
                        warn = false;
                        break;
                    case STATUS_DOWN_FREE_4:
                    case STATUS_DOWN_FREE_12:
                    case STATUS_DOWN_FREEING_6:
                    case STATUS_DOWN_FREEING_14:
                        parkStatus = false;
                        armsStatus = "DOWN";
                        warn = false;
                        break;
                    case STATUS_DOWN_NO_FREE_5:
                    case STATUS_DOWN_NO_FREE_13:
                    case STATUS_DOWN_NO_FREEING_7:
                    case STATUS_DOWN_NO_FREEING_15:
                        parkStatus = true;
                        armsStatus = "DOWN";
                        warn = false;
                        break;
                    case STATUS_UP_FREE_16:
                    case STATUS_UP_FREE_24:
                    case STATUS_UP_FREEING_18:
                    case STATUS_UP_FREEING_26:
                        parkStatus = false;
                        armsStatus = "UP";
                        warn = true;
                        break;
                    case STATUS_UP_NO_FREE_17:
                    case STATUS_UP_NO_FREE_25:
                    case STATUS_UP_NO_FREEING_19:
                    case STATUS_UP_NO_FREEING_27:
                        parkStatus = true;
                        armsStatus = "UP";
                        warn = true;
                        break;
                    case STATUS_DOWN_FREE_20:
                    case STATUS_DOWN_FREE_28:
                    case STATUS_DOWN_FREEING_22:
                    case STATUS_DOWN_FREEING_30:
                        parkStatus = false;
                        armsStatus = "DOWN";
                        warn = true;
                        break;
                    case STATUS_DOWN_NO_FREE_21:
                    case STATUS_DOWN_NO_FREE_29:
                    case STATUS_DOWN_NO_FREEING_23:
                    case STATUS_DOWN_NO_FREEING_31:
                        parkStatus = true;
                        armsStatus = "DOWN";
                        warn = true;
                        break;
                    default:
                        break;
                }
                publishLockStatusModel.setParkStatus(parkStatus);
                publishLockStatusModel.setArmsStatus(armsStatus);
                publishLockStatusModel.setWarn(warn);
                LOG.info("MB Build PublishLockStatus Data :" + JSON.toJSONString(publishLockStatusModel));
                /**
                 * save redis.
                 */
                opsValue(publishLockStatusModel);
                /**
                 * producer mq
                 */
                new MoveBroadProducer(rabbitmqProperties).publishLockStatus(publishLockStatusModel);
            } else {
                LOG.error("MB Lock Status TMoteStatus Error: " + lockStatusRequest.getTMoteStatus());
            }
        }
    }

    private void lockInfo(final LockInfoRequest lockInfoRequest) {
        PublishLockInfoModel publishLockInfoModel = PublishLockInfoModel.builder()
                .lockCode(lockInfoRequest.getSn())
                .build();
        if (Objects.nonNull(lockInfoRequest.getTMoteInfo())) {
            TMoteInfoModel tMoteInfoModel = lockInfoRequest.getTMoteInfo();
            if (Objects.nonNull(tMoteInfoModel)) {
                publishLockInfoModel.setBatt(tMoteInfoModel.getBatt());
                publishLockInfoModel.setTemp(tMoteInfoModel.getTemp());
                publishLockInfoModel.setSim(tMoteInfoModel.getSim());
                publishLockInfoModel.setImei(tMoteInfoModel.getImei());

                LOG.info("MB Build PublishLockInfo Data :" + JSON.toJSONString(publishLockInfoModel));

                new MoveBroadProducer(rabbitmqProperties).publishLockInfo(publishLockInfoModel);
            } else {
                LOG.error("MB Lock Info TMoteInfo Error: " + lockInfoRequest.getTMoteInfo());
            }
        }

    }

    /**
     * save data.
     * @param publishLockStatusModel {@link PublishLockStatusModel}
     */
    private void opsValue(final PublishLockStatusModel publishLockStatusModel) {
        ReactiveRedisUtils.putValue(publishLockStatusModel.getLockCode(), JSON.toJSONString(publishLockStatusModel), REDIS_EXPIRED_INTERVAL).subscribe(
            flag -> {
                if (flag) {
                    LOG.info("Key= " + publishLockStatusModel.getLockCode() + " save redis success!");
                } else {
                    LOG.info("Key= " + publishLockStatusModel.getLockCode() + " save redis failed!");
                }
            }
        );
    }
}
