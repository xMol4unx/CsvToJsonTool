package org.tatasu;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private final static  String DIR = "D:\\data\\results";
    private final static  String DIR_OUT = "D:\\data\\results\\all_wells.json";

    public static void main(String[] args) throws IOException {
        ConvertToJson c = new ConvertToJson();
        c.convertFilesInDirectory(DIR, DIR_OUT);
        /*List<Path> files = c.readFiles(DIR);
        files.forEach(item -> System.out.println(item.getFileName()));*/
    }
}
