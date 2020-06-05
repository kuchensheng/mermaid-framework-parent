package com.mermaid.framework.util.ExcelUtilTests;

import cn.hutool.core.io.FileUtil;
import com.mermaid.framework.annotation.Cell;
import com.mermaid.framework.util.MermaidExcelUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
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
        List<UserParamDTO> read = MermaidExcelUtil.read(inputFilePath,UserParamDTO.class);
//        for (int i = 0; i <2;i ++) {
//            UserParamDTO userParamDTO = new UserParamDTO();
//            Field[] declaredFields = userParamDTO.getClass().getDeclaredFields();
//            for (Field field : declaredFields) {
//                field.setAccessible(true);
//                Class<?> type = field.getType();
//                if (field.getDeclaringClass() != userParamDTO.getClass() && !MermaidExcelUtil.isJavaClass(field.getDeclaringClass()))  {
//                    field.set(type.newInstance(),field.getAnnotation(Cell.class).name()+i);
//                } else {
//                    field.set(userParamDTO,field.getAnnotation(Cell.class).name()+i);
//                }
//            }
//            read.add(userParamDTO);
//
//        }
//        ParamDTO paramDTO = new ParamDTO();
////        paramDTO.setDeviceId("1111");
//        paramDTO.setDeviceName("测试测试");
//
//        List<ParamDTO> dtoList = new ArrayList<>();
//        for (int i= 0;i <3;i++) {
//            ParamDTO dto = new ParamDTO();
////            dto.setDeviceId("1111"+i);
//            dto.setDeviceName("测试测试"+i);
//            dtoList.add(dto);
//        }
//        List<TestDTO> dtos = new ArrayList<>();
//        for (int i = 0;i<5;i ++) {
//            TestDTO testDTO = new TestDTO();
//            testDTO.setAha(i);
//            testDTO.setTestName("aha"+i);
//            dtos.add(testDTO);
//        }
//        read.get(0).setTestDTOList(dtos);
//        read.get(0).setParamDTOList(dtoList);
        MermaidExcelUtil.write(outputFilePath,read);
    }

    @Test
    public void testWrite() {
    }
}