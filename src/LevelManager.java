import java.io.*;
import java.util.*;

public class LevelManager {
    private final String[] allLevels = {
            "Easy – 4x3", "Classic – 4x4", "Medium – 5x4", "Hard – 6x5",
            "Expert – 8x5", "Master – 8x6", "Grandmaster – 9x6", "Legendary – 10x6"
    };
    private Set<String> unlockedLevels = new HashSet<>(Collections.singletonList("Easy – 4x3"));
    private final File progressFile = new File("progress.dat");

    public void saveProgress() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(progressFile))) {
            oos.writeObject(unlockedLevels);
        } catch (IOException ignored) {}
    }

    @SuppressWarnings("unchecked")
    public void loadProgress() {
        if (progressFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(progressFile))) {
                Object read = ois.readObject();
                if (read instanceof Set) unlockedLevels = (Set<String>) read;
            } catch (Exception e) {
                unlockedLevels = new HashSet<>(Collections.singletonList("Easy – 4x3"));
            }
        }
    }

    public String[] getAllLevels() {
        return allLevels;
    }

    public String getDefaultLevel() {
        return "Easy – 4x3";
    }

    public Set<String> getUnlockedLevels() {
        return unlockedLevels;
    }

    public void unlockLevel(String level) {
        unlockedLevels.add(level);
        saveProgress();
    }
}
