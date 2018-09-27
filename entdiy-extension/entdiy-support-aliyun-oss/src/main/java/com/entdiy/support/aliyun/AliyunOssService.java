/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.support.aliyun;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.sys.entity.AttachmentFile;
import com.entdiy.sys.service.AttachmentFileStoreService;
import com.entdiy.sys.service.LocalAttachmentFileStoreService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

@Service
public class AliyunOssService implements AttachmentFileStoreService {

    private final Logger logger = LoggerFactory.getLogger(AliyunOssService.class);

    /** 开发模式采用本地存储模式，避免不必要的阿里云OSS存储空间和访问流量使用 */
    private LocalAttachmentFileStoreService localAttachmentFileStoreService = new LocalAttachmentFileStoreService();

    //private static String key = "<key>";

    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
    // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
    // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
    // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
    @Value("${oss.aliyun.endpoint:}")
    private String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
    // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
    // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
    @Value("${oss.aliyun.access.key.id:}")
    private String accessKeyId;
    @Value("${oss.aliyun.access.key.secret:}")
    private String accessKeySecret;

    // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
    @Value("${oss.aliyun.bucket.name:}")
    private String bucketName;

    //延迟构造单例对象
    private static OSS ossClient;

    protected OSS buildOssClient() throws ClientException {
        if (ossClient == null) {
            // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
            logger.debug("Building Aliyun OSS Client: {}, {}", endpoint, accessKeyId);
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        }
        return ossClient;
    }

    private String putObjectToOss(String newRelativeFilePath) {
        //参数配置未指定bucket参数（一般开发或测试环境配置），使用本地存储文件，减少不必要的云存储资源使用
        if (StringUtils.isBlank(bucketName)) {
            return newRelativeFilePath;
        }

        // 判断文件是否存在，如果已存在则直接返回
        boolean found = buildOssClient().doesObjectExist(bucketName, newRelativeFilePath);
        if (found) {
            logger.debug("Skipped Exist Aliyun OSS File: {}", newRelativeFilePath);
            return newRelativeFilePath;
        }

        logger.debug("Putting Aliyun OSS File: {}", newRelativeFilePath);
        String storePrefix = AppContextHolder.getFileWriteRootDir();
        File toFile = new File(storePrefix + newRelativeFilePath);
        // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
        buildOssClient().putObject(bucketName, newRelativeFilePath, toFile);
        return newRelativeFilePath;
    }

    @Override
    public AttachmentFile storeFileData(Long lastModified, AttachmentFile.AccessModeEnum accessMode, InputStream fis, String subDir, String fileName, String contentType, Long fileLength) {
        //先调用本地存储服务，在服务器本地留存
        AttachmentFile attachmentFile = localAttachmentFileStoreService.storeFileData(lastModified, accessMode, fis, subDir, fileName, contentType, fileLength);
        //线上存储
        putObjectToOss(attachmentFile.getRelativePath());

        return attachmentFile;
    }

    @Override
    public String imageZoomOut(String relativeFilePath, int maxWidth, int maxHeight) {
        //先调用本地存储服务，在服务器本地留存
        String newRelativeFilePath = localAttachmentFileStoreService.imageZoomOut(relativeFilePath, maxWidth, maxHeight);
        //线上存储
        return putObjectToOss(newRelativeFilePath);
    }

    @Override
    public String imageWatermark(String relativeFilePath, BufferedImage watermarkImage) {
        //先调用本地存储服务，在服务器本地留存
        String newRelativeFilePath = localAttachmentFileStoreService.imageWatermark(relativeFilePath, watermarkImage);
        //线上存储
        return putObjectToOss(newRelativeFilePath);
    }
}
