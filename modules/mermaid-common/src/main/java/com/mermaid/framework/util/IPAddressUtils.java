package com.mermaid.framework.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/5/11 21:56
 * version 1.0
 */
public class IPAddressUtils {
    private static final int INADDR4SZ = 4;
    private static final int INADDR16SZ = 16;
    private static final int INT16SZ = 2;

    private IPAddressUtils() {
    }

    public static byte[] textToNumericFormatV4(String var0) {
        byte[] var1 = new byte[4];
        long var2 = 0L;
        int var4 = 0;
        boolean var5 = true;
        int var6 = var0.length();
        if (var6 != 0 && var6 <= 15) {
            for (int var7 = 0; var7 < var6; ++var7) {
                char var8 = var0.charAt(var7);
                if (var8 == 46) {
                    if (var5 || var2 < 0L || var2 > 255L || var4 == 3) {
                        return null;
                    }

                    var1[var4++] = (byte) ((int) (var2 & 255L));
                    var2 = 0L;
                    var5 = true;
                } else {
                    int var9 = Character.digit(var8, 10);
                    if (var9 < 0) {
                        return null;
                    }

                    var2 *= 10L;
                    var2 += (long) var9;
                    var5 = false;
                }
            }

            if (!var5 && var2 >= 0L && var2 < 1L << (4 - var4) * 8) {
                switch (var4) {
                    case 0:
                        var1[0] = (byte) ((int) (var2 >> 24 & 255L));
                    case 1:
                        var1[1] = (byte) ((int) (var2 >> 16 & 255L));
                    case 2:
                        var1[2] = (byte) ((int) (var2 >> 8 & 255L));
                    case 3:
                        var1[3] = (byte) ((int) (var2 >> 0 & 255L));
                    default:
                        return var1;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static byte[] textToNumericFormatV6(String var0) {
        if (var0.length() < 2) {
            return null;
        } else {
            char[] var5 = var0.toCharArray();
            byte[] var6 = new byte[16];
            int var7 = var5.length;
            int var8 = var0.indexOf("%");
            if (var8 == var7 - 1) {
                return null;
            } else {
                if (var8 != -1) {
                    var7 = var8;
                }

                int var1 = -1;
                int var9 = 0;
                int var10 = 0;
                if (var5[var9] == 58) {
                    ++var9;
                    if (var5[var9] != 58) {
                        return null;
                    }
                }

                int var11 = var9;
                boolean var3 = false;
                int var4 = 0;

                while (true) {
                    int var12;
                    while (var9 < var7) {
                        char var2 = var5[var9++];
                        var12 = Character.digit(var2, 16);
                        if (var12 != -1) {
                            var4 <<= 4;
                            var4 |= var12;
                            if (var4 > '\uffff') {
                                return null;
                            }

                            var3 = true;
                        } else {
                            if (var2 != 58) {
                                if (var2 == 46 && var10 + 4 <= 16) {
                                    String var13 = var0.substring(var11, var7);
                                    int var14 = 0;

                                    for (int var15 = 0; (var15 = var13.indexOf(46, var15)) != -1; ++var15) {
                                        ++var14;
                                    }

                                    if (var14 != 3) {
                                        return null;
                                    }

                                    byte[] var16 = textToNumericFormatV4(var13);
                                    if (var16 == null) {
                                        return null;
                                    }

                                    for (int var17 = 0; var17 < 4; ++var17) {
                                        var6[var10++] = var16[var17];
                                    }

                                    var3 = false;
                                    break;
                                }

                                return null;
                            }

                            var11 = var9;
                            if (!var3) {
                                if (var1 != -1) {
                                    return null;
                                }

                                var1 = var10;
                            } else {
                                if (var9 == var7) {
                                    return null;
                                }

                                if (var10 + 2 > 16) {
                                    return null;
                                }

                                var6[var10++] = (byte) (var4 >> 8 & 255);
                                var6[var10++] = (byte) (var4 & 255);
                                var3 = false;
                                var4 = 0;
                            }
                        }
                    }

                    if (var3) {
                        if (var10 + 2 > 16) {
                            return null;
                        }

                        var6[var10++] = (byte) (var4 >> 8 & 255);
                        var6[var10++] = (byte) (var4 & 255);
                    }

                    if (var1 != -1) {
                        var12 = var10 - var1;
                        if (var10 == 16) {
                            return null;
                        }

                        for (var9 = 1; var9 <= var12; ++var9) {
                            var6[16 - var9] = var6[var1 + var12 - var9];
                            var6[var1 + var12 - var9] = 0;
                        }

                        var10 = 16;
                    }

                    if (var10 != 16) {
                        return null;
                    }

                    byte[] var18 = convertFromIPv4MappedAddress(var6);
                    if (var18 != null) {
                        return var18;
                    }

                    return var6;
                }
            }
        }
    }

    public static boolean isIPv4LiteralAddress(String var0) {
        return textToNumericFormatV4(var0) != null;
    }

    public static boolean isIPv6LiteralAddress(String var0) {
        return textToNumericFormatV6(var0) != null;
    }

    public static byte[] convertFromIPv4MappedAddress(byte[] var0) {
        if (isIPv4MappedAddress(var0)) {
            byte[] var1 = new byte[4];
            System.arraycopy(var0, 12, var1, 0, 4);
            return var1;
        } else {
            return null;
        }
    }

    private static boolean isIPv4MappedAddress(byte[] var0) {
        return var0.length < 16 ? false : var0[0] == 0 && var0[1] == 0 && var0[2] == 0 && var0[3] == 0 && var0[4] == 0 && var0[5] == 0 && var0[6] == 0 && var0[7] == 0 && var0[8] == 0 && var0[9] == 0 && var0[10] == -1 && var0[11] == -1;
    }

    public static String getLocalIP() throws Exception {
        String sysIpProps = System.getProperty("local.ip");
        String sysIpPrefixProps = System.getProperty("local.ip.prefix");
        String sysIPNetworkInterfaceProps = System.getProperty("local.network.interface");
        if (StringUtils.hasText(sysIpProps)) {
            return sysIpProps;
        }
        InetAddress address = null;
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        String ret = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                    if(ret == null) {
                        ret = addr.getHostAddress();
                    }

                    if(StringUtils.hasText(sysIPNetworkInterfaceProps) && netInterface.getName().equals(sysIPNetworkInterfaceProps)) {
                        return addr.getHostAddress();
                    }

                    if(StringUtils.hasText(sysIpPrefixProps) && addr.getHostAddress().startsWith(sysIpPrefixProps)) {
                        return addr.getHostAddress();
                    }
                }
            }
        }
        if(ret != null) {
            return ret;
        }
        throw new Exception("获取本地IP地址失败, 请确认网卡配置正确");
    }

    public static boolean matches(String ip, String rangeRules/** comma split rules, 192.168.0.0/16, 16表示掩码匹配前16位即是匹配 */) {
        Assert.hasText(ip, "ip can not be empty");
        Assert.hasText(rangeRules, "rangeRules can not be empty");
        String rules[] = rangeRules.split(",");
        return matches(ip, rules);
    }

    public static boolean matches(String ip, String[] rules) {
        IPAddress ipAddress = new IPAddress(ip);
        for(String rule : rules) {
            IPRange range = new IPRange(rule);
            if(range.isIPAddressInRange(ipAddress)) {
                return true;
            }
        }
        return false;
    }
}
