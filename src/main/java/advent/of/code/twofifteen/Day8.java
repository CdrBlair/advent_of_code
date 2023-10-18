package advent.of.code.twofifteen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day8 {

    public int codeToTextdiff() throws IOException {
        var result = 0;
        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/escapeStrings.txt"));) {

            List<String> lines = br.lines().collect(Collectors.toList());
            List<String> linesCopy = lines.stream().collect(Collectors.toList());

            var sumOfCode = lines.stream().mapToInt(l -> l.length()).sum();
            System.out.println(sumOfCode);
            lines = lines.stream().map(l -> l.substring(1, l.length() - 1)).collect(Collectors.toList());

            lines = lines.stream().map(l -> StringUtils.replace(l, "\\\\", "ö")).collect(Collectors.toList());
            lines = lines.stream().map(l -> StringUtils.replace(l, "\\\"", "\"")).collect(Collectors.toList());
            lines = lines.stream().map(l -> {
                if (!StringUtils.contains(l, "\\x")) {
                    return l;
                }
                // more then one

                var indexOfX = 0;
                while (indexOfX >= 0) {

                    indexOfX = StringUtils.indexOf(l, "\\x");
                    if (indexOfX < 0) {
                        break;
                    }
                    System.out.println("current Line :" + l + " index of x: " + indexOfX);
                    var hex = l.substring(indexOfX + 2, indexOfX + 4);
                    var intValue = Integer.parseInt(hex, 16);
                    var asciiChar = (char) Integer.valueOf(intValue).intValue();
                    l = StringUtils.replace(l, "\\x" + hex, String.valueOf(asciiChar));

                }

                return l;
            }).collect(Collectors.toList());
            lines = lines.stream().map(l -> StringUtils.replace(l, "ö", "\\")).collect(Collectors.toList());
            System.out.println(lines);
            var SumOfText = lines.stream().mapToInt(l -> l.length()).sum();
            System.out.println(SumOfText);

            result = sumOfCode - SumOfText;

            linesCopy = linesCopy.stream().map(l -> StringUtils.replace(l, "\\", "\\\\")).collect(Collectors.toList());
            linesCopy = linesCopy.stream().map(l -> StringUtils.replace(l, "\"", "\\\"")).collect(Collectors.toList());
            linesCopy = linesCopy.stream().map(l -> "\"" + l + "\"").collect(Collectors.toList());

            var sumOfCode2 = linesCopy.stream().mapToInt(l -> l.length()).sum();

            result = sumOfCode2 - sumOfCode;

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return result;

    }
}
