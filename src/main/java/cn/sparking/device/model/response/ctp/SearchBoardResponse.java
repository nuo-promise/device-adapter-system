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
public class SearchBoardResponse extends BaseResponse {

    private static final long serialVersionUID = -145920688690484119L;

    // 查询是否可以降板 0 不可以 1 可以
    @JSONField(name = "Status")
    private Integer status;
}
