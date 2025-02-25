import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class JsonFilesLoader {
    private String bucketName;
    private String prefix;
    private S3Client s3;
    private String path = "/Users/chatyang/repo/playground/partiql-lang-kotlin/playground/src/main/resources/";

    public JsonFilesLoader(String bucketName, String prefix){
        this.bucketName = bucketName;
        this.prefix = prefix;
        this.s3 = S3Client.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public void load(){
        clean(path);

        ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucketName).prefix(prefix).build();

        ListObjectsV2Response response = s3.listObjectsV2(request);
        List<S3Object> objects = response.contents();

        for(S3Object object: objects){
            s3.getObject(GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(object.key())
                            .build(),
                    Paths.get(path + object.key()));
        }
    }

    public void clean(String path){
        try (Stream<Path> files = Files.list(Paths.get(path))) {
            files.filter(Files::isRegularFile) // Only select files, not directories
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
