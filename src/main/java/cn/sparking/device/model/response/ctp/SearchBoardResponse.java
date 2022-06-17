package cn.sparking.device.model.response.ctp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBoardResponse {
    // 0 - 不可以 1 - 可以
    private Integer status;
}
