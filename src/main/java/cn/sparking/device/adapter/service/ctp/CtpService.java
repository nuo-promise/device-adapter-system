package cn.sparking.device.adapter.service.ctp;

import cn.sparking.device.model.ctp.ControlModel;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import com.alibaba.fastjson.JSONObject;

public interface CtpService {

     /**
      * 上报状态数据
      *
      * @param parkStatusModel {@link ParkStatusModel}
      * @return {@link JSONObject}
      */
     JSONObject parkStatus(String sign, ParkStatusModel parkStatusModel);


     /**
      * 查询工作模式
      * @param deviceNo device no
      * @return {@link JSONObject}
      */
     JSONObject workMode(String sign, String deviceNo);


     /**
      * 查询是否可以降板
      * @param deviceNo {@link String}
      * @return {@link JSONObject}
      */
     JSONObject searchBoard(String sign, String deviceNo);

     /**
      * 地锁控制接口.
      * @param controlModel {@link ControlModel}
      * @return {@link DeviceAdapterResult}
      */
     DeviceAdapterResult controlCmd(ControlModel controlModel);
}
