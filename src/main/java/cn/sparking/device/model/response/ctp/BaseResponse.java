package cn.sparking.device.model.response.ctp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -5136497835902517767L;

    @JSONField(name = "ErrorCode")
    private Integer errorCode;

    @JSONField(name = "ErrorMsg")
    private String errorMsg;
}
