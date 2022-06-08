package cn.sparking.device.adapter.service.ctp;

import cn.sparking.device.model.ctp.ControlModel;
import cn.sparking.device.model.ctp.ParkStatusModel;
import cn.sparking.device.model.ctp.SearchBoardModel;
import cn.sparking.device.model.ctp.WorkModeModel;
import cn.sparking.device.model.response.DeviceAdapterResult;
import cn.sparking.device.model.response.ctp.BaseResponse;
import cn.sparking.device.model.response.ctp.SearchBoardResponse;
import cn.sparking.device.model.response.ctp.WorkModeResponse;

public interface CtpService {

     /**
      * 上报状态数据
      * @param parkStatusModel {@link ParkStatusModel}
      * @return {@BaseResponse}
      */
     BaseResponse parkStatus(String sign, ParkStatusModel parkStatusModel);


     /**
      * 查询工作模式
      * @param workMode {@link WorkModeModel}
      * @return {@link WorkModeResponse}
      */
     WorkModeResponse workMode(String sign, WorkModeModel workMode);


     /**
      * 查询是否可以降板
      * @param searchBoard {@link SearchBoardModel}
      * @return {@link SearchBoardResponse}
      */
     SearchBoardResponse searchBoard(String sign, SearchBoardModel searchBoard);

     /**
      * 地锁控制接口.
      * @param controlModel {@link ControlModel}
      * @return {@link DeviceAdapterResult}
      */
     DeviceAdapterResult controlCmd(ControlModel controlModel);
}
