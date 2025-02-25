//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
//import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
//import software.amazon.awssdk.services.s3.model.S3Object;
//
//import java.io.File;
//import java.nio.file.Paths;
//import java.util.List;
//
//public class S3ConnectionTest {
//    private static String bucketName;
//    private static String prefix;
//    private static S3Client s3;
//
//    @BeforeAll
//    public static void init(){
//
//        bucketName = "partiqldata";
//        prefix = "";
//        s3 = S3Client.builder()
//                .region(Region.US_EAST_2)
//                .credentialsProvider(DefaultCredentialsProvider.create())
//                .build();
//    }
//
//    @Test
//    public void TestConnection(){
//
//        ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucketName).prefix(prefix).build();
//
//        ListObjectsV2Response response = s3.listObjectsV2(request);
//        List<S3Object> objects = response.contents();
//        for(S3Object object : objects){
//            System.out.println("File:" + object.key());
//        }
//    }
//
//    @Test
//    public void GetObjectTest(){
//        s3.getObject(GetObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key("data_1.jsonl")
//                        .build(),
//                Paths.get("/Users/chatyang/repo/playground/partiql-lang-kotlin/playground/src/test/resources/data_1.jsonl"));
//    }
//
//    @Test
//    public void GetFileTest(){
//        File file = new File("/Users/chatyang/repo/playground/partiql-lang-kotlin/playground/src/test/resources/","data_1.jsonl");
//        if(!file.exists()){
//            System.out.println("FILE IS NOT THERE");
//        }else {
//            System.out.println(file);
//        }
//    }
//
//    @Test
//    public void LoadS3FilesTest(){
//        JsonFilesLoader loader = new JsonFilesLoader(bucketName,prefix);
//        loader.load();
//    }
//
//    @Test
//    public void resolveFilesystemPathTest(){
//        File current = new File("/Users/chatyang/repo/playground/partiql-lang-kotlin/playground/src/test/", "resources");
//
//        if(!current.exists()){
//            System.out.println("the file doesn't exist" + String.valueOf(current.isDirectory()));
//        }else{
//            System.out.println("the file is there" + String.valueOf(current.isDirectory()));
//        }
//
//        if(current.isDirectory()){
//            File[] files = current.listFiles();
//            if(files != null){
//                for(File file : files){
//                    if(file.getName().endsWith(".json")){
//                        System.out.println(file.getName() + " ");
//                    }
//                }
//            }
//        }
//
//
//
//
//    }
//}
