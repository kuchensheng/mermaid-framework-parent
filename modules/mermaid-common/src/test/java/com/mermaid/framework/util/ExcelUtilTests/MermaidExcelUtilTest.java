package com.mermaid.framework.util.ExcelUtilTests;

import com.mermaid.framework.util.MermaidExcelUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MermaidExcelUtilTest {

    private static final String inputFilePath = "用户导入模板_new.xlsx";
    private static final String outputFilePath = "用户导出报表.xlsx";
    @Test
    public void read() throws Exception{
        List<UserParamDTO> read = MermaidExcelUtil.read(inputFilePath, UserParamDTO.class);
        Assert.assertNotNull(read);
    }

    @Test
    public void write() throws Exception {
        List<UserParamDTO> read = MermaidExcelUtil.read(inputFilePath, UserParamDTO.class);
        ParamDTO paramDTO = new ParamDTO();
        paramDTO.setDeviceId("1111");
        paramDTO.setDeviceName("测试测试");

        List<ParamDTO> dtoList = new ArrayList<>();
        for (int i= 0;i <3;i++) {
            ParamDTO dto = new ParamDTO();
            dto.setDeviceId("1111"+i);
            dto.setDeviceName("测试测试"+i);
            dtoList.add(dto);
        }
        read.get(0).setParamDTOList(dtoList);
        MermaidExcelUtil.write(outputFilePath,read);
    }

    @Test
    public void testWrite() {
    }
}