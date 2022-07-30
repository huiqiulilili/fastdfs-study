package fastdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author zhuicat
 * @since 2022/7/30 13:05
 */
public class TestFastDFS {

    /**
     * 上传文件
     */
    @Test
    public void testUpload() throws IOException, MyException {
        // 1、全局客户端初始化
        ClientGlobal.init("fastdfs-client.properties");
        // 2、创建tracker客户端对象
        TrackerClient trackerClient = new TrackerClient();
        // 3、获取 trackerService 通过 trackerService 可以知道在 trackerservice 下有哪些可用的 storageService
        TrackerServer trackerServer = trackerClient.getConnection();
        // 4、创建storageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);

        // 参数1：上传文件的本地路径 参数2：上传文件的扩展名 参数3：上传文件的元数据
        NameValuePair[] nameValuePairs = new NameValuePair[]{
                new NameValuePair("originFileName","a.txt"),
                new NameValuePair("upload_author","lyx"),
                new NameValuePair("description",URLEncoder.encode("中文会乱码--解决方式编码","utf-8"))
        };



        // 上传文件
        String[] txts = storageClient.upload_file("J:/img/a.txt", "txt", nameValuePairs);
        for (String txt : txts) {
            System.out.println(txt);
        }

    }

    /**
     * 下载文件
     * group1
     * M00/00/00/wKhYgGLkwgKAG2F1AAAAD_c9pqA881.txt
     */
    @Test
    public void test() throws IOException, MyException {
        // 1、全局客户端初始化
        ClientGlobal.init("fastdfs-client.properties");
        // 2、创建 tracker客户端对象
        TrackerClient trackerClient = new TrackerClient();
        // 3、获取 trackerService 通过 trackerService 可以知道在 trackerService 下有哪些 StorageService
        TrackerServer trackerServer = trackerClient.getConnection();
        // 4、创建 StorageClient 对象
        StorageClient storageClient = new StorageClient(trackerServer, null);


        // 下载方法
        // 不建议这种下载方式
        // storageClient.download_file("group1","M00/00/00/wKhYgGLkwgKAG2F1AAAAD_c9pqA881.txt","J:/img/aaa.txt");

        // 建议
        byte[] bytes = storageClient.download_file("group1", "M00/00/00/wKhYgGLkwgKAG2F1AAAAD_c9pqA881.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(new File("J:/img/aa.txt"));
        fileOutputStream.write(bytes);
    }

    /**
     * 获取文件元数据
     */
    @Test
    public void testGetFileMeta() throws IOException, MyException {
        ClientGlobal.init("fastdfs-client.properties");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient(trackerServer, null);

        NameValuePair[] nameValuePairs = storageClient.get_metadata("group1", "M00/00/00/wKhYgGLk0hSASMe3AAAAD_c9pqA456.txt");
        for (NameValuePair nameValuePair : nameValuePairs) {
            System.out.println(nameValuePair.getName() +"=>"+ URLDecoder.decode(nameValuePair.getValue(),"UTF-8"));
        }
    }

    /**
     * 获取长传文件的基础信息
     */
    @Test
    public void testGetInfo() throws IOException, MyException {
        ClientGlobal.init("fastdfs-client.properties");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);

        // 获取文件基础信息
        FileInfo fileInfo = storageClient.get_file_info("group1", "M00/00/00/wKhYgGLkwgKAG2F1AAAAD_c9pqA881.txt");
        // Crc32：校验算法
        System.out.println("文件Crc32："+fileInfo.getCrc32());
        System.out.println("文件创建时间："+fileInfo.getCreateTimestamp());
        System.out.println("文件大小："+fileInfo.getFileSize());
        System.out.println("文件存在的存储节点的ip地址："+fileInfo.getSourceIpAddr());
    }


}
