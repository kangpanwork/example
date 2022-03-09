package mintest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dop {
    @Test
    public void test() throws IOException {
        URL resource = Dop.class.getResource("/insert.sql");
        List<String> lines = FileUtils.readLines(new File(resource.getFile()), "utf-8");
        Pattern tableName = Pattern.compile("\"uam\"\\.\"\\w+\"");
        Pattern uuid = Pattern.compile("'[a-z0-9A-Z]{32}'");
        Pattern idColumn = Pattern.compile("\\(\"(\\w+)\"");
        for (String line : lines) {
            if (StringUtils.isBlank(line)){
                continue;
            }
            Matcher matcher = tableName.matcher(line);
            matcher.find();
            String group = matcher.group(0);

            Matcher matcher1 = uuid.matcher(line);
            matcher1.find();
            String group1 = matcher1.group();

            Matcher matcher2 = idColumn.matcher(line);
            matcher2.find();
            String group2 = matcher2.group(1);

            String sql = "delete from "+group+" where "+group2+" ="+group1+";";
            System.out.println(sql);
        }
    }
}
