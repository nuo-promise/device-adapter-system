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
public class LockStatusRequest implements Serializable {

    private static final long serialVersionUID = -4424530318391366403L;

    private String sn;

    private String berthCode;

    private String name;

    private TMoteStatusModel tMoteStatus;
}
