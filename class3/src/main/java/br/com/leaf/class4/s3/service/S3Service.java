package br.com.leaf.class4.s3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private final String BUCKET;

    @Value("${aws.s3.folder-root}")
    private final String FOLDER_ROOT;

    public S3Service(S3Client s3Client, String bucket, String folderRoot) {
        this.s3Client = s3Client;
        this.BUCKET = bucket;
        this.FOLDER_ROOT = folderRoot;
    }

    public void upload(MultipartFile file) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(FOLDER_ROOT + "/" + file.getOriginalFilename())
                .build();

        s3Client.putObject(
                request,
                software.amazon.awssdk.core.sync.RequestBody
                        .fromBytes(file.getBytes())
        );
    }

    public List<String> listFiles() {

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(BUCKET)
                .prefix(FOLDER_ROOT + "/")
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        return response.contents()
                .stream()
                .map(S3Object::key)
                .toList();
    }
}
