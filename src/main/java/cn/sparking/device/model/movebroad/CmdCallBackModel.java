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
public class CmdCallBackModel implements Serializable {

    private static final long serialVersionUID = -1327128677604431428L;

    private String sn;

    private String commandId;

    private String creationTime;

    private String deliveredTime;

    /**
     * SUCCESS, FAIL, TIMEOUT.
     */
    private String status;
}
