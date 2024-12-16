package org.tatasu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Paths.*;

/**
 * New Branch 11
 */
public class ConvertToJson {
    /**
     *
     * @param inDirectory
     * @param fileOut
     * @throws IOException
     */
    public void convertFilesInDirectory(String inDirectory, String fileOut) throws IOException {
        List<Path> files = this.readFiles(inDirectory);
        StringBuilder sb = this.parseFiles(files);
        this.writeSbToFile(sb, fileOut);

    }

    /**
     * 
     * @param inDirectory
     * @return
     * @throws IOException
     */
    public List<Path> readFiles(String inDirectory) throws IOException {
        List<Path> pathsList = new ArrayList<Path>();
        try (Stream<Path> paths = Files.walk(get(inDirectory))) {
                paths.filter(Files::isRegularFile)
                    //.forEach(System.out::println);
                    .forEach(item -> pathsList.add(item));
            };
        return  pathsList;
    }

    private StringBuilder parseFiles(List<Path> singleFilePath) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"wells\":[");
        final boolean[] first = {true, true};

        singleFilePath.forEach(path -> {
            List<String> fileStrings = this.readFileLineByLine(path);
            //fileStrings.forEach(System.out::println);
            fileStrings.forEach(item -> {
                if(item.indexOf("Well name") > -1) {
                    if(first[1]) {
                        json.append("{ \"WellName\": \"" + item.replace("Well name:","").trim() + "\",");
                        first[1] = false;
                    } else {
                        json.append(",{ \"WellName\": \"" + item.replace("Well name:","").trim() + "\",");
                    }
                    json.append("\"data\":[");
                    first[0] = true;
                } else {
                    String s[] = item.split(" ");
                    if(first[0]) {
                        json.append("{\"x\":" + s[0].trim() + ",");
                        first[0] = false;
                    } else {
                        json.append(",{\"x\":" + s[0].trim() + ",");
                    }
                    json.append("\"y\":" + s[1].trim() + ",");
                    json.append("\"z\":" + s[2].trim() + ",");
                    json.append("\"l\":" + s[3].trim());
                    json.append("}");
                }
            });
            json.append("]}");
        });
        json.append("]}");
        return json;
    }

    private  List<String> readFileLineByLine(Path singleFilePath) {
        List<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(singleFilePath)) {
            list = stream
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void writeSbToFile(StringBuilder sb, String fileName) throws IOException {
        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(fileName),false));
        bwr.write(sb.toString());
        bwr.flush();
        bwr.close();
    }
}
