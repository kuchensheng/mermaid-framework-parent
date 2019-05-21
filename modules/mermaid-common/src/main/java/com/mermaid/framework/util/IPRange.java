package com.mermaid.framework.util;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/5/11 22:05
 * version 1.0
 */
public class IPRange {
    /** IP address */
    private IPAddress ipAddress             = null;

    /** IP subnet mask */
    private IPAddress ipSubnetMask          = null;

    /** extended network prefix */
    private int       extendedNetworkPrefix = 0;

    // -------------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param range String representation of the IP address. The two following formats are supported :<br/>
     * <li/>xxx.xxx.xxx.xxx/xxx.xxx.xxx.xxx <li/>xxx.xxx.xxx.xxx/xx <- extended network prefix
     * @exception IllegalArgumentException Throws this exception when the specified string doesn't represent a valid IP
     * address.
     */
    public IPRange(String range){
        parseRange(range);
    }

    // -------------------------------------------------------------------------
    /**
     * Return the encapsulated IP address.
     *
     * @return The IP address.
     */
    public final IPAddress getIPAddress() {
        return ipAddress;
    }

    // -------------------------------------------------------------------------
    /**
     * Return the encapsulated subnet mask
     *
     * @return The IP range's subnet mask.
     */
    public final IPAddress getIPSubnetMask() {
        return ipSubnetMask;
    }

    // -------------------------------------------------------------------------
    /**
     * Return the extended extended network prefix.
     *
     * @return Return the extended network prefix.
     */
    public final int getExtendedNetworkPrefix() {
        return extendedNetworkPrefix;
    }

    // -------------------------------------------------------------------------
    /**
     * Convert the IP Range into a string representation.
     *
     * @return Return the string representation of the IP Address following the common format xxx.xxx.xxx.xxx/xx (IP
     * address/extended network prefixs).
     */
    public String toString() {
        return ipAddress.toString() + "/" + extendedNetworkPrefix;
    }

    // -------------------------------------------------------------------------
    /**
     * Parse the IP range string representation.
     *
     * @param range String representation of the IP range.
     * @exception IllegalArgumentException Throws this exception if the specified range is not a valid IP network range.
     */
    final void parseRange(String range) {
        if (range == null) {
            throw new IllegalArgumentException("Invalid IP range");
        }

        int index = range.indexOf('/');
        String subnetStr = null;
        if (index == -1) {
            ipAddress = new IPAddress(range);
        } else {
            ipAddress = new IPAddress(range.substring(0, index));
            subnetStr = range.substring(index + 1);
        }

        // try to convert the remaining part of the range into a decimal
        // value.
        try {
            if (subnetStr != null) {
                extendedNetworkPrefix = Integer.parseInt(subnetStr);
                if ((extendedNetworkPrefix < 0) || (extendedNetworkPrefix > 32)) {
                    throw new IllegalArgumentException("Invalid IP range [" + range + "]");
                }
                ipSubnetMask = computeMaskFromNetworkPrefix(extendedNetworkPrefix);
            }
        } catch (NumberFormatException ex) {

            // the remaining part is not a valid decimal value.
            // Check if it's a decimal-dotted notation.
            ipSubnetMask = new IPAddress(subnetStr);

            // create the corresponding subnet decimal
            extendedNetworkPrefix = computeNetworkPrefixFromMask(ipSubnetMask);
            if (extendedNetworkPrefix == -1) {
                throw new IllegalArgumentException("Invalid IP range [" + range + "]", ex);
            }
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Compute the extended network prefix from the IP subnet mask.
     *
     * @param mask Reference to the subnet mask IP number.
     * @return Return the extended network prefix. Return -1 if the specified mask cannot be converted into a extended
     * prefix network.
     */
    private int computeNetworkPrefixFromMask(IPAddress mask) {

        int result = 0;
        int tmp = mask.getIPAddress();

        while ((tmp & 0x00000001) == 0x00000001) {
            result++;
            tmp = tmp >>> 1;
        }

        if (tmp != 0) {
            return -1;
        }

        return result;
    }

    public static String toDecimalString(String inBinaryIpAddress) {
        StringBuilder decimalIp = new StringBuilder();
        String[] binary = new String[4];

        for (int i = 0, c = 0; i < 32; i = i + 8, c++) {
            binary[c] = inBinaryIpAddress.substring(i, i + 8);
            int octet = Integer.parseInt(binary[c], 2);
            decimalIp.append(octet);
            if (c < 3) {

                decimalIp.append('.');
            }
        }
        return decimalIp.toString();
    }

    // -------------------------------------------------------------------------
    /**
     * Convert a extended network prefix integer into an IP number.
     *
     * @param prefix The network prefix number.
     * @return Return the IP number corresponding to the extended network prefix.
     */
    private IPAddress computeMaskFromNetworkPrefix(int prefix) {

        /*
         * int subnet = 0; for (int i=0; i<prefix; i++) { subnet = subnet << 1; subnet += 1; }
         */

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            if (i < prefix) {
                str.append("1");
            } else {
                str.append("0");
            }
        }

        String decimalString = toDecimalString(str.toString());
        return new IPAddress(decimalString);

    }

    // -------------------------------------------------------------------------
    /**
     * Check if the specified IP address is in the encapsulated range.
     *
     * @param address The IP address to be tested.
     * @return Return <code>true</code> if the specified IP address is in the encapsulated IP range, otherwise return
     * <code>false</code>.
     */
    public boolean isIPAddressInRange(IPAddress address) {
        if (ipSubnetMask == null) {
            return this.ipAddress.equals(address);
        }

        int result1 = address.getIPAddress() & ipSubnetMask.getIPAddress();
        int result2 = ipAddress.getIPAddress() & ipSubnetMask.getIPAddress();

        return result1 == result2;
    }
}
