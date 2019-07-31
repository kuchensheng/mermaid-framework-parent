package com.mermaid.framework.azkaban.job;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ClassName:JobTemplate
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  15:48
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 15:48   kuchensheng    1.0
 */
public class JobTemplate {

    private static Template getTemplate() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        try {
            String path = JobTemplate.class.getClassLoader().getResource("template").getPath();
            cfg.setDirectoryForTemplateLoading(new File(path));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_26));
            Template template = cfg.getTemplate("template.ftl");
            return template;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建command
     * @param jobName 作业名称，在Azkaban上展示的作业名
     * @param commands 作业的执行命令
     * @param DCS_JOB_ID DCS的JOBID，为查询作业执行状态的参数
     * @param dependencies 依赖的上级job
     * @return
     */
    public static String createCommand(String jobName,String[] commands,String DCS_JOB_ID,String[] dependencies) {

        String res = null;
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bw = new BufferedWriter(stringWriter);
        Map<String,Object> root = new HashMap<>();
        root.put("jobName",jobName);
        root.put("type","command");
        if(null == commands) {
            return null;
        }
        List<String> commandList = new ArrayList<>();
        for (String cs : commands) {
            commandList.add(cs);
            commandList.add("sh /home/test/data/common.sh " + DCS_JOB_ID);
        }
        root.put("commands", commandList);
        if(null != dependencies) {
            String strDepen = "";
            for (String str : dependencies) {
                strDepen = strDepen + str + ",";
            }
            if(strDepen.endsWith(",")) {
                strDepen = strDepen.substring(0,strDepen.length() - 1);
            }
            root.put("dependencies",strDepen);
        }

        try {
            getTemplate().process(root,bw);
            bw.flush();
            res = stringWriter.getBuffer().toString();
            bw.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * 创建zip文件
     * @param map <JobName,Content>
     * @return
     */
    public static File createZipFile(String flowName,Map<String,String> map) {
        String path = JobTemplate.class.getClassLoader().getResource("jobs").getPath();
        try {

            for (Map.Entry<String,String> entry :map.entrySet()) {
                byte[] bytes = entry.getValue().getBytes();
                File file = new File(path+"/"+flowName+"/");
                if(!file.exists()) {
                    file.mkdirs();
                }
                file = new File(path+"/"+flowName+"/" + entry.getKey() +".job");
                if(!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
                fileOutputStream.close();

            }
            File file = new File(path +"/"+flowName+".zip");
//            zip(path+"/"+flowName+"/",path +"/"+flowName+".zip");
            toZip(path+"/"+flowName+"/",path +"/"+flowName+".zip",true);
//            delFile(new File(path+"/"+flowName+"/"));
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void delFile(File file) {
        //删除文件夹及其子文件

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        file.delete();
    }

    public static void zip(String srcDir, String targetFile) throws IOException {
        try (OutputStream fos = new FileOutputStream(targetFile);
             OutputStream bos = new BufferedOutputStream(fos);
             ArchiveOutputStream aos = new ZipArchiveOutputStream(bos)) {

            Path dirPath = Paths.get(srcDir);
            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    ArchiveEntry entry = new ZipArchiveEntry(dir.toFile(), dirPath.relativize(dir).toString());
                    aos.putArchiveEntry(entry);
                    aos.closeArchiveEntry();
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    ArchiveEntry entry = new ZipArchiveEntry(
                            file.toFile(), dirPath.relativize(file).toString());
                    aos.putArchiveEntry(entry);
                    IOUtils.copy(new FileInputStream(file.toFile()), aos);
                    aos.closeArchiveEntry();
                    return super.visitFile(file, attrs);
                }

            });
        }
    }

    private static File zipFiles(String path, String flowName) throws IOException {
        File sourceFile = new File(path+"/"+flowName+"/");
        File[] files = sourceFile.listFiles();
        if(null == files || files.length < 1) {
            throw new FileNotFoundException(path+"/"+flowName+"/里没有文件");
        }
        InputStream inputStream = null;
        ZipArchiveOutputStream zipArchiveOutputStream = null;
        File zipFile = new File(path +"/"+flowName+".zip");
        try {
            zipArchiveOutputStream = new ZipArchiveOutputStream(zipFile);
            //Use Zip64 extensions for all entries where they are required
            zipArchiveOutputStream.setUseZip64(Zip64Mode.AsNeeded);
            for (File file : files) {
                //将每个文件用ZipArchiveEntry封装，使用ZipArchiveOutputStream写到压缩文件
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
                zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);

                inputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024 * 5];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    //把缓冲区的字节写入到ZipArchiveEntry
                    zipArchiveOutputStream.write(buffer, 0, len);
                }
            }
            zipArchiveOutputStream.closeArchiveEntry();
            zipArchiveOutputStream.finish();

            for (File file : files) {
                file.deleteOnExit();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭输入流
                if (null != inputStream) {
                    inputStream.close();
                }
                //关闭输出流
                if (null != zipArchiveOutputStream) {
                    zipArchiveOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return zipFile;
    }


    private static final int  BUFFER_SIZE = 2 * 1024;

        /**
         * 压缩成ZIP 方法1
         * @param srcDir 压缩文件夹路径
         * @param target    目标文件
         * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
         *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
         * @throws RuntimeException 压缩失败会抛出运行时异常
         */
    public static void toZip(String srcDir, String target, boolean KeepDirStructure)
        throws RuntimeException{

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            File file = new File(target);
            FileOutputStream out = new FileOutputStream(file);
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {

            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

        /**
         * 压缩成ZIP 方法2
         * @param srcFiles 需要压缩的文件列表
         * @param out           压缩文件输出流
         * @throws RuntimeException 压缩失败会抛出运行时异常
         */
    public static void toZip(List<File> srcFiles , OutputStream out)throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1){
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


        /**
         * 递归压缩方法
         * @param sourceFile 源文件
         * @param zos        zip输出流
         * @param name       压缩后的名称
         * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
         *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
         * @throws Exception
         */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }

                }
            }
        }
    }
    public static void main(String[] args) {
        String test = JobTemplate.createCommand("test", new String[]{"echo 666"},"15", new String[]{"1","2"});
        System.out.println(test);

        Map<String,String> map = new HashMap<>();
        map.put("job1",test);
        File text = JobTemplate.createZipFile("text", map);
    }
}
