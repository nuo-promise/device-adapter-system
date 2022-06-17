package cn.sparking.device.model.response.ctp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkModeResponse {

    // 0 全时段工作 1 全时段停用 2 时段内工作 3 时段内停用 4 每天时段内工作
    private String workMode;

    // MMddHHmm 月日时分
    private String workStart;

    private String workEnd;
}
