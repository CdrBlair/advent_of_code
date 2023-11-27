package advent.of.code.twofifteen;

import java.util.ArrayList;
import java.util.List;

public class Day10 {


    public int getResultLength() {

        var input = "3113322113";
        var result = 0;


        var nextList = new ArrayList<>(input.chars().mapToObj(c -> String.valueOf((char) c)).toList());

        for (var i = 0; i < 50; i++) {

            var currentTime = System.currentTimeMillis();

            var currentLst = new ArrayList<>(List.copyOf(nextList));
            nextList.clear();

            while (!currentLst.isEmpty()) {
                var currentChar = currentLst.removeFirst();
                var count = 1;
                while (!currentLst.isEmpty() && currentLst.getFirst().equals(currentChar)) {
                    currentLst.removeFirst();
                    count++;
                }
                nextList.add(String.valueOf(count));
                nextList.add(currentChar);
            }

            var timeDiff = System.currentTimeMillis() - currentTime;
            // timeDiff in seconds
            System.out.println("timeDiff: " + timeDiff + "ms");

            System.out.println("nextList length: " + nextList.size());


        }

        result = nextList.size();


        return result;
    }
}
