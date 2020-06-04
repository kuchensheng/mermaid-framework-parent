package com.mermaid.framework.util.ExcelUtilTests;

import com.mermaid.framework.annotation.Cell;
import com.mermaid.framework.annotation.Excel;

import java.util.List;

@Excel
public class ParamDTO {
//    @Cell(clumonNum = 8,name = "扩展字段Id")
//    private String deviceId;
    @Cell(clumonNum = 8,name = "扩展字段名称")
    private String deviceName;

//    private List<Object> params;

//    public String getDeviceId() {
//        return deviceId;
//    }
//
//    public void setDeviceId(String deviceId) {
//        this.deviceId = deviceId;
//    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "ParamDTO{" +

                ", deviceName='" + deviceName + '\'' +
                '}';
    }
}
