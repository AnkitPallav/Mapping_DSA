import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class CacheMappingVisualizer extends JFrame {

    private int cacheSize;
    private int[] memoryAddresses;
    private int currentIndex;
    private Cache currentCache;
    private Timer timer;

    public CacheMappingVisualizer(int cacheSize, int[] memoryAddresses) {
        this.cacheSize = cacheSize;
        this.memoryAddresses = memoryAddresses;
        this.currentIndex = 0;
        setTitle("Cache Mapping Visualization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel cachePanel = new JPanel();
        panel.add(cachePanel, BorderLayout.CENTER);
        add(panel);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < memoryAddresses.length) {
                    updateCacheVisualization(cachePanel);
                    currentIndex++;
                } else {
                    timer.stop();
                }
            }
        });
    }

    public void startAnimation() {
        currentIndex = 0;
        currentCache = new DirectMappingCache(cacheSize);
        timer.start();
        setVisible(true);
    }

    private void updateCacheVisualization(JPanel cachePanel) {
        if (currentCache != null) {
            currentCache.accessMemory(memoryAddresses[currentIndex]);
            cachePanel.removeAll();
            cachePanel.add(new JLabel(Arrays.toString(currentCache.cache)));
            cachePanel.revalidate();
            cachePanel.repaint();
        }
    }

    public static void main(String[] args) {
        int cacheSize = 8;
        int[] memoryAddresses = {12, 15, 20, 12, 18, 25}; // Example memory addresses

        CacheMappingVisualizer visualizer = new CacheMappingVisualizer(cacheSize, memoryAddresses);
        visualizer.startAnimation();
    }

    static abstract class Cache {
        int cacheSize;
        int[] cache;

        Cache(int cacheSize) {
            this.cacheSize = cacheSize;
            this.cache = new int[cacheSize];
            Arrays.fill(cache, -1); // Initialize cache with -1 to indicate empty slots
        }

        abstract void accessMemory(int memoryAddress);
    }

    static class DirectMappingCache extends Cache {
        DirectMappingCache(int cacheSize) {
            super(cacheSize);
        }

        @Override
        void accessMemory(int memoryAddress) {
            int cacheIndex = memoryAddress % cacheSize;
            cache[cacheIndex] = memoryAddress; // Update cache with memory block
        }
    }
}
