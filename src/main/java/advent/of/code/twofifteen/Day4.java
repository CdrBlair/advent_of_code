package advent.of.code.twofifteen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

public class Day4 {

    public int lowestForMD5() throws NoSuchAlgorithmException {
        var secret = "ckczppom";
        var result = 0;
        var md5 = MessageDigest.getInstance("MD5");
        while (true) {
            var currentValue = secret + result;
            var hashString = DigestUtils.md5Hex(currentValue);

            System.out.println(hashString);
            // Part 1 was with 5 zeroes
            if (hashString.startsWith("000000")) {
                break;
            }
            result++;

        }

        return result;
    }

}
