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
public class LockControlModel implements Serializable {

    private static final long serialVersionUID = 1981767871682902603L;

    private int action;

    /**
     * 命令状态变化通知地址.
     */
    private String callbackUrl;

    private int seconds;
}
