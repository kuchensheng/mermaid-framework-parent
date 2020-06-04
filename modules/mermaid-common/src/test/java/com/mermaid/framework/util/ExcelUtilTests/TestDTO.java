package com.mermaid.framework.util.ExcelUtilTests;


import com.mermaid.framework.annotation.Cell;
import com.mermaid.framework.annotation.Excel;

@Excel
public class TestDTO {
    @Cell(clumonNum = 9,name = "测试名")
    private String testName;

    @Cell(clumonNum = 10,name = "啊哈")
    private int aha;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getAha() {
        return aha;
    }

    public void setAha(int aha) {
        this.aha = aha;
    }
}
