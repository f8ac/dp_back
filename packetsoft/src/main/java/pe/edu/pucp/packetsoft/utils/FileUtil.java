package pe.edu.pucp.packetsoft.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    private FileUtil() {
        // restrict instantiation
      }
    
      //public static final String folderPath =  "/dp_backincoming-files//";
      public static final String folderPath =  "dp_back/packetsoft/src/main/resources/incoming-files/";
      public static final Path filePath = Paths.get(folderPath);
    
}
