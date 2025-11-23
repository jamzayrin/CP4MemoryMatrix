import java.util.*;

public class CardValueGenerator {

    private static final Map<String, List<String>> THEMES = new HashMap<>();

    static {
        THEMES.put("Nature", generateImageList(
                "/images/Nature/", "N", 30
        ));

        THEMES.put("Art & Paintings", generateImageList(
                "/images/ArtPaintings/", "AP", 30
        ));

        // ADDING IMAGE THEME HERE (Space Exploration)
        THEMES.put("Space Exploration", generateImageList(
                "/images/SpaceExploration/", "SE", 30
        ));
    }

    private static List<String> generateImageList(String folder, String prefix, int count) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(folder + prefix + i + ".jpg");  // Correct resource path
        }
        return list;
    }

    public static List<String> generate(String theme, int pairsNeeded) {
        List<String> pool = THEMES.get(theme);

        if (pool == null) {
            throw new IllegalArgumentException("Theme not found: " + theme);
        }

        if (pairsNeeded > pool.size()) {
            throw new IllegalArgumentException("Not enough unique cards for theme: " + theme);
        }

        List<String> shuffledPool = new ArrayList<>(pool);
        Collections.shuffle(shuffledPool);

        List<String> result = new ArrayList<>();

        for (int i = 0; i < pairsNeeded; i++) {
            String val = shuffledPool.get(i);
            result.add(val);
            result.add(val);
        }

        Collections.shuffle(result);
        return result;
    }
}


