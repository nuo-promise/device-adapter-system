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
public class LockCmdRequest implements Serializable {

    private static final long serialVersionUID = 3728841784673813764L;

    private String projectNo;

    private String lockCode;

    private String method;

    private int action;

    private int seconds;

    private String operator;

    private Long cmdTime;
}
