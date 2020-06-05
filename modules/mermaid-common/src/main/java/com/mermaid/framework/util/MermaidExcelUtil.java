package com.mermaid.framework.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.*;
import cn.hutool.poi.excel.cell.CellUtil;
import com.mermaid.framework.annotation.Cell;
import com.mermaid.framework.annotation.Excel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.poi.excel.cell.CellUtil.getMergedRegionValue;
import static cn.hutool.poi.excel.cell.CellUtil.isMergedRegion;

/**
 * Excel读写工具类
 */
public class MermaidExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(MermaidExcelUtil.class);

    /**
     * 读取excel文件
     * @param filePath 文件位置，相对/绝对路径
     * @param clazz 对象类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> read(String filePath, Class<T> clazz) throws Exception{
        File file = getFile(filePath);
        return read(file,clazz);
    }

    /**
     * 读取Excel并转化为目标对象列表
     * @param file Excel文件
     * @param clazz 目标对象类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> read(File file,Class<T> clazz) throws Exception{
        if (null == file) {
            throw new FileNotFoundException();
        }

        logger.info("文件大小为{}KB",file.length() / 1024);
        Excel excel = clazz.getAnnotation(Excel.class);

        if (null == excel) {
            throw new Exception("目标对象必须拥有注解" + Excel.class.getName());
        }

        ExcelReader reader = ExcelUtil.getReader(file);
        logger.info("Excel有[{}]个sheet",reader.getSheetCount());

        List<T> dataList = new LinkedList<>();
        for (Sheet sheet : reader.getSheets()) {
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            List<T> subList = new LinkedList<>();
            for (int i = excel.startRow();i < sheet.getLastRowNum(); i ++) {
                List<Object> objects = reader.readRow(i);
                T t = generalInstance(objects, clazz);
                if (null != t) {
                    subList.add(t);
                }

            }
            dataList.addAll(mergeData(subList,clazz));
        }

        logger.info("读取到有效数据共[{}]行",dataList.size());
        return dataList;
    }

    private static <T> Collection<? extends T> mergeData(List<T> subList,Class<T> clazz) throws IllegalAccessException,InstantiationException {
        List<Field> fieldList = getFieldList(clazz);
        Field groupField = null;
        for (Field field : fieldList) {
            Cell cell = field.getAnnotation(Cell.class);
            if (cell.isGroupClumon()) {
                groupField = field;
                break;
            }
        }
        if (null == groupField) {
            groupField = fieldList.get(0);
        }

        List result = new LinkedList();

        T t = subList.get(0);

        boolean merge =false;
        for (int i = 1;i<subList.size();i ++) {
            Object val = groupField.get(t);
            if (val.equals(groupField.get(subList.get(i)))) {

                //合并同类项
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (Iterable.class.isAssignableFrom(field.getType())) {
                        //如果是集合类型，则合并，其他保持不变
                        merge = true;
                        List list = (List) field.get(t);
                        list.addAll((List) field.get(subList.get(i)));
                        field.set(t,list);
                    }
                }

            } else {
                //不是同类，则继续向下
                result.add(copyInstance(t));
                t = subList.get(i);
                merge =false;
            }

            if (merge && i == subList.size() -1) {
                result.add(copyInstance(t));
            }
        }

        return result;
    }

    private static <T> T copyInstance(T t) throws IllegalAccessException, InstantiationException {
        T newInstance = (T) t.getClass().newInstance();
        Field[] fields= t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(newInstance,field.get(t));
        }
        return newInstance;
    }

    /**
     * 读取Excel数据流并转化为目标对象列表
     * @param inputStream 数据流
     * @param clazz 目标对象类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> read(InputStream inputStream,Class<T> clazz) throws Exception{
        Excel excel = clazz.getAnnotation(Excel.class);

        if (null == excel) {
            throw new Exception("目标对象必须拥有注解" + Excel.class.getName());
        }

        ExcelReader reader = ExcelUtil.getReader(inputStream);
        logger.info("Excel有[{}]个sheet",reader.getSheetCount());

            List<T> dataList = new LinkedList<>();
        int rowCount = reader.getRowCount();
        for (Sheet sheet : reader.getSheets()) {
            for (int i = excel.startRow();i < rowCount; i ++) {
                List<Object> objects = reader.readRow(i);
                T t = generalInstance(objects,clazz);
                if (null != t) {
                    dataList.add(t);
                }
            }
        }

            logger.info("读取到有效数据共[{}]行",dataList.size());
            return dataList;
    }

    public static <T> void write(String destFilePath,List<T> dataList) throws Exception {
        write(destFilePath,dataList,null);
    }


    public static <T> void write(String destFilePath, List<T> dataList, Map<String,String> headerAliasMap) throws Exception {
        write(destFilePath,dataList,headerAliasMap,null);
    }

    public static <T> void write(String destFilePath,List<T> dataList,Map<String,String> headerAliasMap, String sheetName) throws Exception {
       write(destFilePath,dataList,headerAliasMap,sheetName,null,null);
    }

    public static <T> void write(String destFilePath,List<T> dataList,Map<String,String> headerAliasMap, String sheetName,Integer mergeLastColumnNum,String mergeContent) throws Exception {
        if (null == dataList || dataList.isEmpty()) {
            throw new Exception("数据为空");
        }

        File file = getFile(destFilePath);
        ExcelWriter writer = null;
        try {
            if (com.mermaid.framework.util.StringUtils.hasText(sheetName)) {
                writer = ExcelUtil.getWriter(file,sheetName);
            } else {
                writer = ExcelUtil.getWriter(file);
            }

            final List<Node> headerNodeList = new ArrayList<>();
            if (null == headerAliasMap || headerAliasMap.isEmpty()) {
                headerNodeList.addAll(getHeaderAliasMap(dataList.get(0).getClass()));
            } else {

                headerAliasMap.forEach((key,val) -> {
                    Node node = new Node();
                    node.setFiledName(key);
                    node.setClumonName(val);
                    headerNodeList.add(node);
                });
            }

            for (Node node : headerNodeList) {
                writer.addHeaderAlias(node.getFiledName(),node.getClumonName());
            }

            if (null != mergeLastColumnNum) {
                if (com.mermaid.framework.util.StringUtils.isEmpty(mergeContent)) {
                    mergeContent = "";
                }
                writer.merge(mergeLastColumnNum,mergeContent);
            }

            List<Map<String,Object>> result = filterDataList(dataList);
            writer = mergeRowWriter(writer,dataList);
            writer.write(result,true);
            writer.flush(file);

        } catch (Exception e) {
            throw e;
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    /**
     * 分析需要合并的单元格
     * @param writer
     * @param dataList
     * @param <T>
     * @return
     */
    private static <T> ExcelWriter mergeRowWriter(ExcelWriter writer, List<T> dataList) throws IllegalAccessException {
//        Field[] fieldList = dataList.get(0).getClass().getDeclaredFields();
        List<Field> fieldList = getFieldList(dataList.get(0).getClass());

        Excel excel = dataList.get(0).getClass().getAnnotation(Excel.class);
        int startRow = excel.startRow();
        if (startRow > 0) {
            startRow = startRow -1;

        }
        int maxMerge = 0;
        for (int row = 0; row < dataList.size();row ++) {
            for (Field field : fieldList) {
                Cell cell = field.getAnnotation(Cell.class);
                //获取当前列需要merge的行数
                int mergeRowCount = getMergeRowCount(dataList.get(row),cell.clumonNum(),null);
                maxMerge = maxMerge > mergeRowCount ? maxMerge : mergeRowCount;
                if (mergeRowCount > 1) {
                    int firstRow = row +startRow;
                    int endRow = firstRow + mergeRowCount -1;

                    writer.merge(firstRow,endRow,cell.clumonNum() -1 ,cell.clumonNum() -1 ,getCellVal(dataList.get(row),cell.clumonNum()),false);
                }
            }
            startRow += maxMerge;
        }

        return writer;
    }

    private static <T> Object getCellVal(T t, int clumonNum) throws IllegalAccessException {
//        System.out.println(String.format("columnNum = %d",clumonNum));
        Field[] fieldList = t.getClass().getDeclaredFields();

        for (Field field : fieldList) {
            field.setAccessible(true);
            Cell cell = field.getAnnotation(Cell.class);
            if (null != cell) {
                if (field.getAnnotation(Cell.class).clumonNum() == clumonNum) {
                    return field.get(t);
                }
            } else {
                Class<?> type = field.getType();
                if (Iterable.class.isAssignableFrom(type) || type.isArray()) {
                    Object o = field.get(t);
                    List list = (List) o;
                    for (Object item : list) {
                        return getCellVal(item,clumonNum);
                    }
                } else if (!isJavaClass(type)) {

                    return getCellVal(field.get(t),clumonNum);
                }
            }
        }
        return null;
    }
    static int maxColumn = 0;
    private static <T> int getMergeRowCount(T t, int columnNum, List<Field> fieldWithCellList) throws IllegalAccessException {
        int max = 0;

        Field[] declaredFields = t.getClass().getDeclaredFields();

        //所有被Cell注解的字段
        if (CollectionUtil.isEmpty(fieldWithCellList)) {
            fieldWithCellList = getFieldList(t.getClass());
        }

        for (Field field : declaredFields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            Cell cell = field.getAnnotation(Cell.class);
            if (null != cell && cell.clumonNum() == columnNum) {
                continue;
            }

            //判断其他字段是否有复合对象
            if (Iterable.class.isAssignableFrom(type) || type.isArray()) {
                Collection<Object> objects = (Collection<Object>) field.get(t);
                int size = 0;
                if (null != objects && objects.size() > 0) {
                    for (Object object : objects) {
                        size += getMergeRowCount(object, columnNum,fieldWithCellList);
                    }
                }
                max = setMax(size, max);

            } else if (!isJavaClass(type)) {
                 max = setMax(getMergeRowCount(field.get(t), columnNum,fieldWithCellList), max);
            } else {
                max = 1;
            }
        }
        //如果只有一个，那么表示不用合并单元格，返回1
        return max;
    }

    private static int setMax(int a, int max){
        if(a > max){
            max = a;
        }
        return max;
    }
    /**
     * 只输出那些被@Cell注解的属性
     * @param dataList
     * @return
     * @throws Exception
     */
    private static List<Map<String,Object>> filterDataList(List<?> dataList) throws Exception {
        List<Map<String,Object>> result = new LinkedList<>();
        if (CollectionUtils.isEmpty(dataList)) {
            return result;
        }

        for (Object item : dataList) {
            result.addAll(getFieldVal(item,null));
        }
        return result;
    }

    public static List<Map<String,Object>> getFieldVal(final Object obj,Map<String,Object> map) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        List<Map<String,Object>> result = new LinkedList<>();

        final Map<String,Object> objectList = new HashMap<>();
        if (null != map) {
            map.forEach((key,val) ->{
                objectList.put(key,val);
            });
        }

        boolean addFlag = true;
        for (Field field : fields) {
            field.setAccessible(true);
            if (null != field.getAnnotation(Cell.class)) {
                objectList.put(field.getName(),field.get(obj));
            } else if (!isBasicType(field.getType())){
                //如果字段没有被@Cell修饰，则判断属性是否是对象/List/Map
                Class<?> type = field.getType();
                if (Iterable.class.isAssignableFrom(type) || type.isArray()) {
                    //List集合，则获取集合类型的字段
                    List<?> list = (List<?>) field.get(obj);
                    if (null != list && list.size() > 0) {
                        addFlag = false;
                        for (Object item : list) {
                            result.addAll(getFieldVal(item,objectList));
                        }
                    }
                } else if (!isJavaClass(type)) {
                    addFlag =false;
                    result.addAll(getFieldVal(field.get(obj),objectList));
                }

            }
        }
        if (addFlag) {
            result.add(objectList);
        }

        return result;
    }

    public static List<Field> getFieldList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        List<Field> fieldList = new LinkedList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            if (null != field.getAnnotation(Cell.class)) {
                fieldList.add(field);
            } else if (!isBasicType(field.getType())){
                //如果字段没有被@Cell修饰，则判断属性是否是对象/List/Map
                Class<?> type = field.getType();
                if (Iterable.class.isAssignableFrom(type) || type.isArray()) {
                    //List集合，则获取集合类型的字段
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    fieldList.addAll(getFieldList((Class<?>)parameterizedType.getActualTypeArguments()[0]));
                } else if (!isJavaClass(type)) {
                    fieldList.addAll(getFieldList(type));
                }

            }
        }

        //按照Column升序
        fieldList.sort((o1,o2)-> {
            Cell cell1 = o1.getAnnotation(Cell.class);
            Cell cell2 = o2.getAnnotation(Cell.class);
            return cell1.clumonNum() - cell2.clumonNum();
        });

        return fieldList;
    }

    public static boolean isJavaClass(Class<?> type) {
        return type != null && type.getClassLoader() == null;
    }

    /**
     * 检查是否是基础类型
     * @param type
     * @return
     */
    private static boolean isBasicType(Class<?> type) {

        if(type.isAssignableFrom(Number.class)) {
            return true;
        }

        if (type.isInstance(CharSequence.class)) {
            return true;
        }

        if (type.isAssignableFrom(Boolean.class)) {
            return true;
        }

        if (type.isAssignableFrom(String.class)) {
            return true;
        }

        if (type.isInstance(Comparable.class)) {
            return true;
        }

        if (type.isAssignableFrom(Date.class)) {
            return true;
        }

        if (type.getName().startsWith("java.lang")) {
            return true;
        }


        if (type.isAssignableFrom(int.class) || type.isAssignableFrom(float.class) || type.isAssignableFrom(long.class)
                || type.isAssignableFrom(double.class) || type.isAssignableFrom(short.class) || type.isAssignableFrom(char.class)
                || type.isAssignableFrom(boolean.class)) {
            return true;
        }
        return false;
    }

    public static <T> void write(OutputStream outputStream,List<T> dataList,Map<String,String> headerAliasMap) throws Exception {
        if (null == dataList || dataList.isEmpty()) {
            throw new Exception("数据为空");
        }

        ExcelWriter writer = null;

        try {
            writer = ExcelUtil.getWriter();

            List<Node> nodeList = new ArrayList<>();
            if (null == headerAliasMap || headerAliasMap.isEmpty()) {
                nodeList = getHeaderAliasMap(dataList.get(0).getClass());
            } else {
                for (Map.Entry<String,String> entry : headerAliasMap.entrySet()) {
                    Node node = new Node();
                    node.setFiledName(entry.getKey());
                    node.setClumonName(entry.getValue());
                    nodeList.add(node);
                }
            }

            for (Map.Entry<String,String> entry : headerAliasMap.entrySet()) {
                writer.addHeaderAlias(entry.getKey(),entry.getValue());
            }

            writer.write(nodeList,true);
            writer.flush(outputStream);
        } catch (IORuntimeException e) {
            throw new Exception(e);
        } finally {
            if (null != writer) {
                writer.close();
            }
            IoUtil.close(outputStream);
        }
    }

    private static class Node {
        private String clumonName;
        private String filedName;

        public String getClumonName() {
            return clumonName;
        }

        public void setClumonName(String clumonName) {
            this.clumonName = clumonName;
        }

        public String getFiledName() {
            return filedName;
        }

        public void setFiledName(String filedName) {
            this.filedName = filedName;
        }
    }
    private static <T> List<Node> getHeaderAliasMap(Class<T> clazz) {

        List<Node> result = new ArrayList<>();

        List<Field> fieldList = getFieldList(clazz);

        for (Field field : fieldList) {
            field.setAccessible(true);
            Cell cell = field.getAnnotation(Cell.class);
            if (null != cell) {
                String name = field.getName();
                String cellName = cell.name();
                Node node = new Node();
                node.setFiledName(name);
                node.setClumonName(cellName);
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 将读取到的数据转化为目标对象
     * @param <T>
     * @param objects row数据集合
     * @param clazz 目标对象
     * @return
     * @throws Exception
     */
    private static <T> T generalInstance(List<Object> objects, Class<T> clazz) throws Exception{
        if (null == objects || objects.size() ==0) {
            return null;
        }

        T t = null;
        try {
            t = clazz.newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Class<?> declaringClass = field.getDeclaringClass();
                if (Iterable.class.isAssignableFrom(field.getType())) {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Object o = field.get(t);
                    List list = null;
                    if (null != o) {
                        list = (List) o;
                    } else {
                        list = new LinkedList();
                    }
                    list.add(generalInstance(objects,Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName())));
                    field.set(t,list);
                } else if (!isJavaClass(field.getType())) {
                    //自定义类型
                    Object o = generalInstance(objects, field.getDeclaringClass());
                    field.set(t,o);
                } else {
                    Cell annotation = field.getAnnotation(Cell.class);
                    if (null != annotation) {
                        int columnNum = Integer.valueOf(annotation.clumonNum());
                        Class<?> type = field.getType();
                        if (columnNum > objects.size() || columnNum < 1) {
                            throw new IndexOutOfBoundsException(declaringClass.getName() +"类:字段" +annotation.name() + "的columnNum设置不正确");
                        }
                        Object val = objects.get(columnNum -1);
                        if (annotation.required() && ObjectUtil.isEmpty(val)) {
                            String msg = annotation.errMsg();
                            if (!com.mermaid.framework.util.StringUtils.hasText(msg)) {
                                msg = annotation.name()+"is required";
                            }
                            throw new IllegalArgumentException(msg);
                        }

                        Object convert = convert(val, type);
                        if (type.getClass().getName().equals("java.lang.String") && com.mermaid.framework.util.StringUtils.hasText(annotation.regex())) {
                            Pattern pattern = Pattern.compile(annotation.regex());
                            Matcher matcher = pattern.matcher(String.valueOf(convert));
                            if (!matcher.find()) {
                                String msg = annotation.errMsg();
                                if (!com.mermaid.framework.util.StringUtils.hasText(msg)) {
                                    msg = annotation.name()+"与正则["+annotation.regex()+"]不匹配";
                                }
                                throw new IllegalArgumentException(msg);
                            }
                        }

                        if (type.equals(int.class) || type.equals(Integer.class) || type.equals(long.class) || type.equals(Long.class)
                                || type.equals(double.class) || type.equals(Double.class) || type.equals(BigDecimal.class)) {
                            if ((int) convert > annotation.max()) {
                                String msg = annotation.errMsg();
                                if (!com.mermaid.framework.util.StringUtils.hasText(msg)) {
                                    msg = annotation.name()+"超过了最大值";
                                }
                                throw new IllegalArgumentException(msg);
                            }
                        }
                        field.set(t,convert);
                    }
                }


            }
        } catch (Exception e) {
            throw e;
        }
        return t;
    }

    /**
     * 根据对象的字段类型将数据转化为属性的类型
     * @param obj
     * @param type
     * @param <T>
     * @return
     */
    private static <T> T convert(Object obj,Class<T> type) {
        if (obj != null && StringUtils.isNotBlank(obj.toString())) {
            if (type.equals(Integer.class)||type.equals(int.class)) {
                return (T)new Integer(obj.toString());
            } else if (type.equals(Long.class)||type.equals(long.class)) {
                return (T)new Long(obj.toString());
            } else if (type.equals(Boolean.class)||type.equals(boolean.class)) {
                return (T) new Boolean(obj.toString());
            } else if (type.equals(Short.class)||type.equals(short.class)) {
                return (T) new Short(obj.toString());
            } else if (type.equals(Float.class)||type.equals(float.class)) {
                return (T) new Float(obj.toString());
            } else if (type.equals(Double.class)||type.equals(double.class)) {
                return (T) new Double(obj.toString());
            } else if (type.equals(Byte.class)||type.equals(byte.class)) {
                return (T) new Byte(obj.toString());
            } else if (type.equals(Character.class)||type.equals(char.class)) {
                return (T)new Character(obj.toString().charAt(0));
            } else if (type.equals(String.class)) {
                return (T)  String.valueOf(obj);
            } else if (type.equals(BigDecimal.class)) {
                return (T) new BigDecimal(obj.toString());
            } else if (type.equals(LocalDateTime.class)) {
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return (T) LocalDateTime.parse(obj.toString());
            } else if (type.equals(Date.class)) {
                try
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    return (T) formatter.parse(obj.toString());
                }
                catch (ParseException e)
                {
                    throw new RuntimeException(e.getMessage());
                }

            }else{
                return null;
            }
        } else {
            if (type.equals(int.class)) {
                return (T)new Integer(0);
            } else if (type.equals(long.class)) {
                return (T)new Long(0L);
            } else if (type.equals(boolean.class)) {
                return (T)new Boolean(false);
            } else if (type.equals(short.class)) {
                return (T)new Short("0");
            } else if (type.equals(float.class)) {
                return (T) new Float(0.0);
            } else if (type.equals(double.class)) {
                return (T) new Double(0.0);
            } else if (type.equals(byte.class)) {
                return (T) new Byte("0");
            } else if (type.equals(char.class)) {
                return (T) new Character('\u0000');
            }else {
                return null;
            }
        }
    }

    /**
     * 从文件位置获取文件
     * @param filePath
     * @return
     */
    private static File getFile(String filePath) {
        logger.info("获取文件，path=[{}]",filePath);
        File file = null;
        file = new File(filePath);
        if (null != file && file.exists() && file.isFile()) {
            return file;
        }

        ClassPathResource resource = new ClassPathResource(filePath);
        file = resource.getFile();
        if (null != file && file.exists() && file.isFile()) {
            return file;
        }

        FileResource fileResource = new FileResource(file);
        file = fileResource.getFile();
        return file;
    }
}
