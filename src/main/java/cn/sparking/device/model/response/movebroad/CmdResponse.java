package cn.sparking.device.model.response.movebroad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmdResponse implements Serializable {

    private static final long serialVersionUID = -4765802962054845490L;

    private int resultCode;

    private String resultMsg;

    // 下发的命令ID,
    private String commandId;
}
