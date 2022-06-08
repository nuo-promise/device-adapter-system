package cn.sparking.device.model.response.ctp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkModeResponse extends BaseResponse {

    private static final long serialVersionUID = -8750749350664631600L;

    // 工作模式 0 -全时段工作, 1 - 全时段停用 2 - 时段内工作 3 时段内停用 4 - 每天时段内工作
    @JSONField(name = "WorkMode")
    private String workMode;


    // 工作时段开始 格式: MMddHHmm (月日时分) 月日时分为99时 表示无效
    @JSONField(name = "WorkStart")
    private String workStart;


    @JSONField(name = "WorkEnd")
    private String workEnd;
}
