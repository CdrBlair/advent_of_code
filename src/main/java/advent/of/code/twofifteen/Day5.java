package advent.of.code.twofifteen;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Day5 {

    public int numberOfNiceStrings() throws IOException {

        AtomicInteger niceLines = new AtomicInteger(0);

        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/niceStrings.txt"));) {

            br.lines().forEach(line -> {
                if (isItStillNice(line))
                    niceLines.incrementAndGet();
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return niceLines.get();
    }

    public boolean isItNice(String inputString) {

        var vowels = "aeiou";
        var forbiddenStrings = Arrays.asList("ab", "cd", "pq", "xy");
        AtomicInteger vowelsCount = new AtomicInteger(0);

        if (StringUtils.containsAny(inputString, forbiddenStrings.toArray(new CharSequence[0]))) {
            return false;
        }
        vowels.chars().mapToObj(c -> (char) c).forEach(c -> {
            vowelsCount.addAndGet(StringUtils.countMatches(inputString, c.toString()));
        });

        var containsDouble = inputString.chars().mapToObj(c -> (char) c)
                .anyMatch(c -> inputString.contains(c.toString() + c.toString()));

        return vowelsCount.get() > 2 && containsDouble;

    }

    public boolean isItStillNice(String inputString) {

        String currentPair;
        var firstRule = false;

        for (var i = 0; i < inputString.length() - 3; i++) {
            currentPair = inputString.substring(i, i + 2);
            if (StringUtils.countMatches(inputString.substring(i + 2), currentPair) >= 1) {
                firstRule = true;
                break;
            }
        }

        if (!firstRule)
            return false;

        for (var i = 0; i < inputString.length() - 2; i++) {
            if (inputString.charAt(i) == inputString.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }

}
