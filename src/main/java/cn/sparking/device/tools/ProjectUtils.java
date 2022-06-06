package cn.sparking.device.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectUtils {
    public static Map<Integer, String> projectMap = new ConcurrentHashMap<>();

    /**
     * insert geom parkID and projectNo.
     * @param parkID geom parkID
     * @param projectNo spark projectNo
     */
    public static void save(final Integer parkID, final String projectNo) {
       projectMap.put(parkID, projectNo);
    }

    /**
     * get projectNo by parkId.
     * @param parkID geom parkId
     * @return projectNo
     */
    public static String get(final Integer parkID) {
        return projectMap.get(parkID);
    }
}
