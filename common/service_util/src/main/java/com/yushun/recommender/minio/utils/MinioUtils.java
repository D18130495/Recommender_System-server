package com.yushun.recommender.minio.utils;

import com.yushun.recommender.minio.vo.MinioObject;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Minio Utils
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-20
 */

@Component
public class MinioUtils {
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    // check if the bucket is existing or create a new bucket
    public void existBucket(String name) {
        try{
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());

            if(!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create a new bucket
    public Boolean makeBucket(String bucketName) {
        try{
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }catch (Exception e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    // delete bucket
    public Boolean removeBucket(String bucketName) {
        try{
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }catch (Exception e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    // upload multiple file
    public List<String> upload(MultipartFile[] multipartFile) {
        List<String> names = new ArrayList<>(multipartFile.length);

        for (MultipartFile file : multipartFile) {
            String fileName = file.getOriginalFilename();

            String[] split = fileName.split("\\.");

            if(split.length > 1) {
                fileName = split[0] + "_" + System.currentTimeMillis() + "." + split[1];
            }else {
                fileName = fileName + System.currentTimeMillis();
            }

            InputStream in = null;

            try{
                in = file.getInputStream();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(in, in.available(), -1)
                        .contentType(file.getContentType())
                        .build()
                );
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(in != null) {
                    try{
                        in.close();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            names.add(fileName);
        }

        return names;
    }

    // download file
    public ResponseEntity<byte[]> download(String fileName) {
        ResponseEntity<byte[]> responseEntity = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;

        try{
            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);

            // encapsulate the return value
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();

            try{
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            headers.setContentLength(bytes.length);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccessControlExposeHeaders(Arrays.asList("*"));
            responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if(in != null) {
                    try{
                        in.close();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(out != null) {
                    out.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseEntity;
    }

    // check the bucket object
    public List<MinioObject> listObjects(String bucketName) {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());

        List<MinioObject> minioObjects = new ArrayList<>();

        try{
            for(Result<Item> result : results) {
                Item item = result.get();

                MinioObject minioObject = new MinioObject();
                minioObject.setObjectName(item.objectName());
                minioObject.setSize(item.size());
                minioObjects.add(minioObject);
            }
        }catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return minioObjects;
    }

    // delete multiple file
    public Iterable<Result<DeleteError>> removeObjects(String bucketName, List<String> objects) {
        List<DeleteObject> dos = objects.stream().map(e -> new DeleteObject(e)).collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());

        return results;
    }
}
