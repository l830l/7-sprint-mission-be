package com.sprint.mission.discodeit.service.basic;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
@Profile("prod")
@RequiredArgsConstructor
public class S3FileService {

  private S3Client s3Client;

  @Value("${spring.cloud.aws.credentials.accessKey}")
  private String accessKey;
  @Value("${spring.cloud.aws.credentials.secretKey}")
  private String secretKey;
  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucketName;
  @Value("${spring.cloud.aws.region.static}")
  private String region;

  //S3에 연결해서 인증을 처리 : Value 셋팅으로 인하여 PostConstruct 셋팅
  @PostConstruct
  private void initializeAmazonS3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  //S3에 파일 업로드
  public String uploadToS3Bucket(MultipartFile file) {
    return uploadToS3Bucket("", file);
  }

  //prefix를 붙여서 S3에 파일 업로드 : 파일 구분.
  public String uploadToS3Bucket(String path, MultipartFile file) {
    String fileName = path + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    PutObjectRequest req = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .contentType(file.getContentType())
        .build();
    try {
      s3Client.putObject(req,
          RequestBody.fromBytes(file.getBytes()));
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e); //바꿔주기
    }

    return s3Client.utilities()
        .getUrl(b -> b.bucket(bucketName).key(fileName))
        .toString();
  }

  // S3 파일 삭제
  public void deleteFile(String imgUrl) {
    try {
      URL url = new URL(imgUrl);

      //getPath() -> 키값 앞에 있는 프로토콜과 IP 주소 혹은 도메인 포트 번호를 제외한 리소스 내부 경로만 받음.
      String decodeUrl = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
      //맨 앞 '/' 제거
      String key = decodeUrl.substring(1);
      DeleteObjectRequest req = DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();
      s3Client.deleteObject(req);
    } catch (MalformedURLException e) {

      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  // S3 파일 여러 개 삭제
  public void deleteFiles(List<String> imgUrls) {
    List<String> fileNames = imgUrls.stream()
        .map(S3FileService::extractFileNameFromUrl)
        .toList();
    List<ObjectIdentifier> objectIdentifiers = fileNames.stream()
        .map(fileName -> ObjectIdentifier.builder().key(fileName).build()).toList();
    Delete delete = Delete.builder().objects(objectIdentifiers).build();

    //객체 생성 및 메서드 명 조심(s가 붙어있음)
    DeleteObjectsRequest req = DeleteObjectsRequest.builder()
        .bucket(bucketName).delete(delete).build();

    s3Client.deleteObjects(req);
  }

  //URL 에서 파일 명만 추출
  private static String extractFileNameFromUrl(String imgUrl) {
    String decodeUrl;
    try {
      URL url = new URL(imgUrl);
      decodeUrl = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    } catch (MalformedURLException e) {

      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
    return decodeUrl;
  }

  //파일 다운로드 요청
  public byte[] downloadFile(String fileUrl) {
    String fileName = extractFileNameFromUrl(fileUrl);

    GetObjectRequest req = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .build();

    ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(req);
    return objectAsBytes.asByteArray();
  }

  //파일 존재 여부 확인
  public boolean isFileExist(String fileUrl) {
    String fileName = null;
    try {
      fileName = extractFileNameFromUrl(fileUrl);
      HeadObjectRequest req = HeadObjectRequest.builder().bucket(bucketName).key(fileName).build();
      HeadObjectResponse res = s3Client.headObject(req);
      log.info("파일 정보 확인 : {}", res);
    } catch (S3Exception e) {
      if (e.statusCode() == 404) {
        return false;
      }
      throw new RuntimeException(e.getMessage(), e);
    }
    return true;
  }
}
