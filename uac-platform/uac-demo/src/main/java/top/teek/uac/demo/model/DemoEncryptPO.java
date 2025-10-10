package top.teek.uac.demo.model;

import top.teek.encrypt.annotation.EncryptField;
import top.teek.encrypt.enumerate.AlgorithmType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Teeker
 * @date 2024/6/9 01:13:28
 * @note
 */
@Data
@TableName("test_encrypt")
public class DemoEncryptPO {
    
    @TableId
    private Long id;

    @EncryptField(algorithm = AlgorithmType.RSA,
            privateKey = """
                    MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCcHvPEBJOit8uM
                    OJcL/6LwxBGrslfVU6iL/ka7W8Ux5ifr/lJUe+e0atMFb9hYL5VbfbexdI189uEu
                    UCT9U9aDgPW8A8SWg7DR0mXT/+tEnnl/RdsaeFke4HTDwOZJaBP4ljE0+pJnH5Xj
                    qxT803qo5EtXOdjv6fyysutpsAYLPu65nwWnKIGLyFvdvHrNAsM6i2TSWqpU6AAK
                    6EN3Jht83T1PvJfBL7AanjhUGMSbCAzl6w5sh9IJw0SnOA1Nk1/nRqWwJSJYf8Ri
                    3WvuRPUq8rjwU1tpvFNndlNtrtZeheCxBTAqpdq3RIHMkYHb/A7rfhinEqAJ+J6m
                    atYJ/tdFAgMBAAECggEAf6iboVwwsKxjh6w6brhBL2jUHjZ9tdVri9WHVurKi2vs
                    lv9lqOmMZK25EcgL+sgl9CMPks6AZ3+kT+9+35qvXOaViYD3PjA+5MOLOlgYtAp1
                    xMmFSNbB4Qqez/arF7lAI1QEtpJyJlrggMLDLZ1rvjC3HoMRzdPiKuk8sDYcKxKq
                    j1JIEULy3DYdPvrFI2FmcOsMtikP2bbKY4USStgA8r5KF7CtjJjjyjrkYwc61IIJ
                    haL+pFQf0Im+BEjPWoNXUK3LHNTIq8S8BirzibZmTfZCfnZzi7olvzJRc+l3XTz3
                    SgQb9Y/A3KOlrXd7bse+OzkuSDq9izR2x4ejayWO/QKBgQDPRTF7BBhrS6aq4Rkm
                    crtbYyHZ68kfyROZ3EIi+I/7nBPqnDaFYceEAIN2P6/25PPLBiEi5fI5DMnvzIT1
                    2PoFGSKTh6Y34VL6oFn3Fd5uHdmpLguaHu/lSN1M+Gry9E/mqh2zijZJLCKKWgY8
                    LX66eNnVdMftSMHFOSSli0KmHwKBgQDA00XwAls5r6/ClwxR7hDEGZ2iQ7L8UZWP
                    iT1aCPkKRvjjsO0TDyxmO+KJy9tMB1B49xpyPkApzErqueLyEEWYB0i3kSrTtGzM
                    J1MjMSPkRkgoJH4x4MW03eHPkDVa5q+oDBhVMX0yEQXWZ1iC3saRSgPlybE68Mja
                    FgwP2qpuGwKBgFPZ6T+cE4jsrPt6XyNXzQYWn646nj4WqbBYFAVzy0P+C2yhT8k8
                    GmwDjSt8bmKSkzIyQ5uLrSd5TgSOF8ghxFvlpEBM42i95kTwNBUqqrafqtuvfhAW
                    rfRzOtwVr6akQeLONX/ZzUZi7YJNEzKrMRadJ3scaHlNMt7n1DSIlyj/AoGABhEf
                    rDOGx0Pd1dOG0bUZ1fGwYgCbSxEOEZwR0BlkLIybHB7e9rCNhxHvSMKfPb8lKwkr
                    Tdjjj+0bllMO7urQJb5k7VGl9U9B9RJvrTXImVAUyR6M0ejuj4hDqJIy+48yi6kF
                    wvhxpfefJWXPBR8ZREz93mcAKoiU6Te0XXNV1W0CgYBVIsQGigYQTDiUpkL0mBHH
                    +10kMc/NWgOYPKJ5R8sOIC0YdDem368Udg6g0naOpyIQX9HR7knERtw9/fwWWdcs
                    mCP8Xuw4gHoOjjlVWPGM4Gx5f7+MPxxdwUrTf0jnnuNWuSysQ+cfGkbuyot/8Ar7
                    zE90AMVK2EgAPpuYtxv9gA==
                    """,
            publicKey = """
                    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnB7zxASTorfLjDiXC/+i
                    8MQRq7JX1VOoi/5Gu1vFMeYn6/5SVHvntGrTBW/YWC+VW323sXSNfPbhLlAk/VPW
                    g4D1vAPEloOw0dJl0//rRJ55f0XbGnhZHuB0w8DmSWgT+JYxNPqSZx+V46sU/NN6
                    qORLVznY7+n8srLrabAGCz7uuZ8FpyiBi8hb3bx6zQLDOotk0lqqVOgACuhDdyYb
                    fN09T7yXwS+wGp44VBjEmwgM5esObIfSCcNEpzgNTZNf50alsCUiWH/EYt1r7kT1
                    KvK48FNbabxTZ3ZTba7WXoXgsQUwKqXat0SBzJGB2/wO634YpxKgCfiepmrWCf7X
                    RQIDAQAB
                    """
    )
    private String testKey;

    // @EncryptField // 什么也不写走默认yml配置
    // @EncryptField(algorithm = AlgorithmType.SM4, password = "10rfylhtccpuyke5")
    @EncryptField(algorithm = AlgorithmType.AES, password = "10rfylhtccpuyke5")
    private String value;
}
