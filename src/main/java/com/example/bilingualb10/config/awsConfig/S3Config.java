//package com.example.bilingualb10.config.awsConfig;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import software.amazon.awssdk.regions.Region;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.services.s3.S3Client;
//
//@Configuration
//public class S3Config {
//    @Value("${cloud.aws.credentials.access-key}")
//    private String AWS_ACCESS_KEY;
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String AWS_SECRET_KEY;
//    @Value("${cloud.aws.region.static}")
//    private String REGION;
//
//    @Bean
//    public AmazonS3 amazonS3() {
//        return AmazonS3Client.builder()
//                .withRegion(REGION)
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)))
//                .build();
//    }
//
//    @Bean
//    S3Client s3Client() {
//        Region region = Region.of(REGION);
//        final AwsBasicCredentials credentials =
//                AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY);
//        return S3Client.builder().region(region).
//                credentialsProvider(StaticCredentialsProvider
//                        .create(credentials)).build();
//    }
//}