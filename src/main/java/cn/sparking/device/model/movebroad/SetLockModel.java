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
public class SetLockModel implements Serializable {

    private static final long serialVersionUID = -9007710306206150421L;
    
    private int mode;

    /**
     * 命令状态变化通知地址.
     */
    private String callbackUrl;
}
