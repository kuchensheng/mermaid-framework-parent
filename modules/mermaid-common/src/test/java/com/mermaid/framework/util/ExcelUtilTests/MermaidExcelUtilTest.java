package com.mermaid.framework.util.ExcelUtilTests;

import com.mermaid.framework.util.MermaidExcelUtil;
import org.junit.Assert;
import org.junit.Test;

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

        MermaidExcelUtil.write(outputFilePath,read);
    }

    @Test
    public void testWrite() {
    }
}