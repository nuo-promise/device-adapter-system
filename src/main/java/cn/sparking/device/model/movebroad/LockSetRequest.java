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
public class LockSetRequest implements Serializable {

    private static final long serialVersionUID = 5893957321964831730L;

    private String lockCode;

    private String projectNo;

    private String method;

    private int mode;

    private String operator;

    private Long cmdTime;
}
