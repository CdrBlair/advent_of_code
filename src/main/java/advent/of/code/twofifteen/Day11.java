package advent.of.code.twofifteen;

public class Day11 {

    public String nextSantaPW() {

        var result = "";
        var allRulesMatch = false;
        var currentPW = "vzbxxyzz";

        while (!allRulesMatch) {


            System.out.println("currentPW: " + currentPW);
            currentPW = passwordIterator(currentPW);
            allRulesMatch = checkThirdRule(currentPW) && checkSecondRule(currentPW) && checkFirstRule(currentPW);
            System.out.println("allRulesMatch: " + allRulesMatch);

        }
        result = currentPW;

        return result;
    }

    public boolean checkFirstRule(String password) {
        return password.matches(".*.(?:(?=ab|bc|cd|de|ef|fg|gh|hi|ij|jk|kl|lm|mn|no|op|pq|qr|rs|st|tu|uv|vw|wx|xy|yz).){2,}.*");
    }

    public boolean checkSecondRule(String password) {

        return !(password.contains("i") || password.contains("o") || password.contains("l"));

    }

    public boolean checkThirdRule(String password) {
        return password.chars().mapToObj(c -> (char) c).filter(c -> password.contains(c.toString() + c.toString())).distinct().count() >= 2;
    }


    public String passwordIterator(String currentPW) {

        var curIndex = currentPW.length() - 1;
        var iterated = false;
        while (!iterated) {
            var nextChar = nextChar(currentPW.charAt(curIndex));
            StringBuilder sb = new StringBuilder(currentPW);
            sb.setCharAt(curIndex, (char) nextChar);
            currentPW = sb.toString();
            if (nextChar == 97) {
                curIndex--;
            } else {
                iterated = true;
            }
        }
        return currentPW;
    }

    public int nextChar(char current) {
        var result = 0;

        if (current == 'z') {
            result = 97;
        } else {
            result = (int) current + 1;
        }

        return result;
    }
}
