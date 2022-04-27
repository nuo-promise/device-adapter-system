package cn.sparking.device.model.response.movebroad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = -8197536109401497994L;

    private String accessToken;

    private String refreshToken;

    private int expiresIn;
}
