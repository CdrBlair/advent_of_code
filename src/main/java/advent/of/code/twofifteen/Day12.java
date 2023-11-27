package advent.of.code.twofifteen;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day12 {

    public int accountingSum() throws IOException {
        var result = 0;

        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/bookinjson.txt"));) {

            var line = br.readLine();

            line = line.replaceAll("[^-?0-9]+", " ");
            List<Integer> numbers = Arrays.stream(line.trim().split(" ")).map(Integer::parseInt).toList();
            result = numbers.stream().mapToInt(Integer::intValue).sum();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }


        return result;
    }

    public int accountingSumCorrected() throws IOException {

        var result = 0;

        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/bookinjson.txt"));) {

            var line = br.readLine();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode wholeJson = mapper.readTree(line);
            result = resolveNodes(wholeJson);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }


    private int resolveNodes(@NotNull JsonNode jsoneNode) {

        var result = 0;
        if (jsoneNode.isInt()) {
            result = jsoneNode.asInt();
        } else if (jsoneNode.isArray()) {
            for (var i = 0; i < jsoneNode.size(); i++) {
                result += resolveNodes(jsoneNode.get(i));
            }
        } else if (jsoneNode.isObject()) {
            var fieldNames = jsoneNode.fieldNames();
            while (fieldNames.hasNext()) {
                var fieldName = fieldNames.next();
                var fieldNode = jsoneNode.get(fieldName);
                if (fieldNode.isTextual() && fieldNode.asText().equals("red")) {
                    return 0;
                }
                result += resolveNodes(fieldNode);
            }
        }
        return result;


    }

}
