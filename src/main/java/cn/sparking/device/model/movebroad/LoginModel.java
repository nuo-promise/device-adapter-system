package cn.sparking.device.model.movebroad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * login.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel implements Serializable {

    private static final long serialVersionUID = -2108124252392804715L;

    private String url;

    private String appId;

    private String secret;

    private String refreshToken;
}
