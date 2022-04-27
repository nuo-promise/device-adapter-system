package cn.sparking.device.model.movebroad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * refreshToken.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenModel implements Serializable {

    private static final long serialVersionUID = 3308408715983635030L;

    private String appId;

    private String secret;

    private String refreshToken;
}
