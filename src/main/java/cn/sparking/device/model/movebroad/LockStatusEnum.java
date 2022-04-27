package cn.sparking.device.model.movebroad;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LockStatusEnum {
    UNKNOWN(-1, false, "UNKNOWN", "UNKNOWN"),
    STATUS_UP_FREE_0(0, false, "UP", "空闲"),
    STATUS_UP_FREE_8(8, false, "UP", "空闲"),
    STATUS_UP_FREEING_2(2, false, "UP", "持续空闲"),
    STATUS_UP_FREEING_10(10, false, "UP", "持续空闲"),
    STATUS_UP_NO_FREE_1(1, false, "UP", "占用"),
    STATUS_UP_NO_FREE_9(9, false, "UP", "占用"),
    STATUS_UP_NO_FREEING_3(3, false, "UP", "持续占用"),
    STATUS_UP_NO_FREEING_11(11, false, "UP", "持续占用"),

    STATUS_DOWN_FREE_4(4, false, "DOWN", "空闲"),
    STATUS_DOWN_FREE_12(12, false, "DOWN", "空闲"),
    STATUS_DOWN_FREEING_6(6, false, "DOWN", "持续空闲"),
    STATUS_DOWN_FREEING_14(14, false, "DOWN", "持续空闲"),
    STATUS_DOWN_NO_FREE_5(5, false, "DOWN", "占用"),
    STATUS_DOWN_NO_FREE_13(13, false, "DOWN", "占用"),
    STATUS_DOWN_NO_FREEING_7(7, false, "DOWN", "持续占用"),
    STATUS_DOWN_NO_FREEING_15(15, false, "DOWN", "持续占用"),

    STATUS_UP_FREE_16(16, true, "UP", "空闲"),
    STATUS_UP_FREE_24(24, true, "UP", "空闲"),
    STATUS_UP_FREEING_18(18, true, "UP", "持续空闲"),
    STATUS_UP_FREEING_26(26, true, "UP", "持续空闲"),
    STATUS_UP_NO_FREE_17(17, true, "UP", "占用"),
    STATUS_UP_NO_FREE_25(25, true, "UP", "占用"),
    STATUS_UP_NO_FREEING_19(19, true, "UP", "持续占用"),
    STATUS_UP_NO_FREEING_27(27, true, "UP", "持续占用"),

    STATUS_DOWN_FREE_20(20, true, "DOWN", "空闲"),
    STATUS_DOWN_FREE_28(28, true, "DOWN", "空闲"),
    STATUS_DOWN_FREEING_22(22, true, "DOWN", "持续空闲"),
    STATUS_DOWN_FREEING_30(30, true, "DOWN", "持续空闲"),
    STATUS_DOWN_NO_FREE_21(21, true, "DOWN", "占用"),
    STATUS_DOWN_NO_FREE_29(29, true, "DOWN", "占用"),
    STATUS_DOWN_NO_FREEING_23(23, true, "DOWN", "持续占用"),
    STATUS_DOWN_NO_FREEING_31(31, true, "DOWN", "持续占用");

    private final Integer code;
    private final Boolean warn;
    private final String poker;
    private final String status;

    /**
     * get code desc.
     * @param code lock status code.
     * @return {@link LockStatusEnum}
     */
    public static LockStatusEnum convert(final Integer code) {
        return Stream.of(values())
                .filter(lockStatus -> lockStatus.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
