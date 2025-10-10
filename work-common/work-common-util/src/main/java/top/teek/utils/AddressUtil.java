package top.teek.utils;

import cn.hutool.core.net.NetUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Teeker
 * @date 2023/11/17 0:50
 * @note
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressUtil {

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIp(String ip) {
        if (!StringUtil.hasText(ip)) {
            return UNKNOWN;
        }
        // 内网不查询
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip.replaceAll("(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)", "");
        if (NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
        return RegionUtil.getCityInfo(ip);
    }
}
