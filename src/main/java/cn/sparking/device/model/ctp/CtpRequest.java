package cn.sparking.device.model.ctp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CtpRequest<T> implements Serializable {

    private static final long serialVersionUID = -1681575835119009711L;

    private String cmd;

    private T body;
}
