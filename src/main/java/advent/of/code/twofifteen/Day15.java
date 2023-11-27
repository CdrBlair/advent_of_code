package advent.of.code.twofifteen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day15 {


    public int calculateBestRecipe() throws IOException {
        var result = 0;

        List<Ingredient> allIngredients = new ArrayList<>();
        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/ingriedients.txt"));) {


            br.lines().map(l -> l.replace(":", "").replace(",", "").split(" ")).
                    forEach(i -> allIngredients.add(new Ingredient(i[0], Integer.parseInt(i[2]), Integer.parseInt(i[4]), Integer.parseInt(i[6]), Integer.parseInt(i[8]), Integer.parseInt(i[10]))));


            for (var i = 0; i <= 100; i++) {
                for (var j = 0; j <= 100; j++) {
                    if (i + j > 100) {
                        break;
                    }
                    for (var k = 0; k <= 100; k++) {
                        if (i + j + k > 100) {
                            break;
                        }
                        for (var l = 0; l <= 100; l++) {

                            if (i + j + k + l > 100) {
                                break;
                            }
                            var currentScore = getCurrentScore(allIngredients, i, j, k, l);
                            if (currentScore > result && getCalories(allIngredients, i, j, k, l) == 500)
                                result = currentScore;
                        }


                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }


    private static int getCalories(List<Ingredient> allIngredients, int i, int j, int k, int l) {
        return allIngredients.get(0).calories * i + allIngredients.get(1).calories * j + allIngredients.get(2).calories * k + allIngredients.get(3).calories * l;
    }

    private static int getCurrentScore(List<Ingredient> allIngredients, int i, int j, int k, int l) {
        var capacitySum = allIngredients.get(0).capacity * i + allIngredients.get(1).capacity * j + allIngredients.get(2).capacity * k + allIngredients.get(3).capacity * l;
        var durabilitySum = allIngredients.get(0).durability * i + allIngredients.get(1).durability * j + allIngredients.get(2).durability * k + allIngredients.get(3).durability * l;
        var flavorSum = allIngredients.get(0).flavor * i + allIngredients.get(1).flavor * j + allIngredients.get(2).flavor * k + allIngredients.get(3).flavor * l;
        var textureSum = allIngredients.get(0).texture * i + allIngredients.get(1).texture * j + allIngredients.get(2).texture * k + allIngredients.get(3).texture * l;

        return Math.max(capacitySum, 0) * Math.max(durabilitySum, 0) * Math.max(flavorSum, 0) * Math.max(textureSum, 0);
    }

    record Ingredient(String name, int capacity, int durability, int flavor, int texture, int calories) {

    }

}
