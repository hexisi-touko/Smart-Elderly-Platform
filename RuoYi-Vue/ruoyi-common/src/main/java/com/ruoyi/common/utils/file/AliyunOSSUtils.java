package com.ruoyi.common.utils.file;

import java.io.InputStream;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ruoyi.common.utils.StringUtils;

/**
 * 阿里云 OSS 工具类
 */
@Component
public class AliyunOSSUtils {
    private static final Logger log = LoggerFactory.getLogger(AliyunOSSUtils.class);

    @Value("${aliyun.oss.endpoint:}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret:}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName:}")
    private String bucketName;

    @Value("${aliyun.oss.domain:}")
    private String domain;

    /**
     * 上传文件到 OSS
     *
     * @param file 文件对象
     * @return 返回文件的访问 URL
     */
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 1. 获取上传文件流
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            log.error("获取文件输入流失败", e);
            return null;
        }

        // 2. 检查配置是否完整
        if (StringUtils.isEmpty(endpoint) || StringUtils.isEmpty(accessKeyId) || 
            StringUtils.isEmpty(accessKeySecret) || StringUtils.isEmpty(bucketName) || 
            StringUtils.isEmpty(domain)) {
            log.warn("OSS 配置不完整，跳过 OSS 上传，尝试本地存储");
            return null;
        }

        // 3. 构造文件名 (日期/UUID.扩展名)
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = "upload/" + UUID.randomUUID().toString() + extension;

        // 4. 创建 OSS 客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 4. 上传
            ossClient.putObject(bucketName, fileName, inputStream);
            
            // 5. 拼装访问 URL
            String url = domain;
            if (!url.endsWith("/")) {
                url += "/";
            }
            url += fileName;
            
            log.info("文件上传成功: {}", url);
            return url;
        } catch (Exception e) {
            log.error("OSS 文件上传失败", e);
            return null;
        } finally {
            // 6. 关闭客户端
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
