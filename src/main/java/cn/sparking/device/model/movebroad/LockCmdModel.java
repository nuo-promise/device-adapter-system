package cn.sparking.device.model.movebroad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockCmdModel implements Serializable {

    private static final long serialVersionUID = -3311008802103345947L;

    private String url;

    private String appKey;

    private String accessToken;

    private String sn;

    private String method;

    private LockControlModel lockControlModel;

    private SetLockModel setLockModel;

    private String operator;

    private String time;
}
