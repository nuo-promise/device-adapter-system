package cn.sparking.device.tools;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.CodeSource;

public final class VersionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(VersionUtils.class);

    private static final String VERSION = getVersion(VersionUtils.class, "1.0.0");

    private static final String JAR = ".jar";

    private VersionUtils() {

    }

    /**
     * get version.
     * @return the version
     */
    public static String getVersion() {
        return VERSION;
    }

    /**
     * get version.
     * @param cls {@link Class}
     * @param defaultVersion default version
     * @return the version
     */
    public static String getVersion(final Class<?> cls, final String defaultVersion) {
        String version = cls.getPackage().getImplementationVersion();
        if (StringUtils.isBlank(version)) {
            version = cls.getPackage().getSpecificationVersion();
        }
        if (StringUtils.isNoneBlank(version)) {
            return version;
        }
        // guess version fro jar file name if nothing's found from MANIFEST.MF
        CodeSource codeSource = cls.getProtectionDomain().getCodeSource();

        if (codeSource == null) {
            LOG.info("No codeSource for class {} when getVersion, use default version {}", cls.getName(), defaultVersion);
            return defaultVersion;
        }
        String file = codeSource.getLocation().getFile();
        if (file != null && file.endsWith(JAR)) {
            file = file.substring(0, file.length() - 4);
            int i = file.lastIndexOf('/');
            if (i >= 0) {
                file = file.substring(i + 1);
            }
            i = file.indexOf("-");
            if (i >= 0) {
                file = file.substring(i + 1);
            }
            while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
                i = file.indexOf("-");
                if (i < 0) {
                    break;
                }
                file = file.substring(i + 1);
            }
            version = file;
        }
        // return default version if no version info is found
        return StringUtils.isBlank(version) ? defaultVersion : version;
    }

}
