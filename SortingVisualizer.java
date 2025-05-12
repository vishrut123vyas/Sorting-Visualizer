import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortingVisualizer extends JPanel {
    private int[] array;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static final int MAX_ELEMENTS = WIDTH / 2;
    private static final int MIN_ELEMENTS = 10;
    private int delay = 50;

    public SortingVisualizer() {
        this.array = generateRandomArray(WIDTH / 5);
        setBackground(new Color(236, 240, 241));
    }

    private int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(HEIGHT - 50) + 50;
        }
        return array;
    }

    
    public void bubbleSort() {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    repaint();
                    sleep();
                }
            }
        }
    }

    public void insertionSort() {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
                repaint();
                sleep();
            }
            array[j + 1] = key;
            repaint();
            sleep();
        }
    }

    public void selectionSort() {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
            repaint();
            sleep();
        }
    }

    public void mergeSort() {
        mergeSortHelper(array, 0, array.length - 1);
    }

    private void mergeSortHelper(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortHelper(array, left, mid);
            mergeSortHelper(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
            repaint();
            sleep();
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
            repaint();
            sleep();
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
            repaint();
            sleep();
        }
    }

    public void quickSort() {
        quickSortHelper(array, 0, array.length - 1);
    }

    private void quickSortHelper(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSortHelper(array, low, pi - 1);
            quickSortHelper(array, pi + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        repaint();
        sleep();
        return i + 1;
    }

    private void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int barWidth = getWidth() / array.length;
        for (int i = 0; i < array.length; i++) {
            g.setColor(new Color(100, 149, 237));  // Cornflower Blue
            g.fillRect(i * barWidth, getHeight() - array[i], barWidth, array[i]);
        }
    }

    public void reset() {
        this.array = generateRandomArray(array.length);
        repaint();
    }

    public void setNumberOfElements(int numElements) {
        if (numElements < MIN_ELEMENTS) numElements = MIN_ELEMENTS;
        if (numElements > MAX_ELEMENTS) numElements = MAX_ELEMENTS;
        this.array = generateRandomArray(numElements);
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Visualizer");
        SortingVisualizer visualizer = new SortingVisualizer();
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(visualizer, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());
        controls.setBackground(new Color(52, 73, 94));  // Dark Blue
        controls.setPreferredSize(new Dimension(WIDTH, 60));

        String[] sortingAlgorithms = {"Bubble Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort"};
        for (String algo : sortingAlgorithms) {
            JButton button = new JButton(algo);
            button.setBackground(new Color(44, 62, 80));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                switch (algo) {
                    case "Bubble Sort" -> new Thread(visualizer::bubbleSort).start();
                    case "Insertion Sort" -> new Thread(visualizer::insertionSort).start();
                    case "Selection Sort" -> new Thread(visualizer::selectionSort).start();
                    case "Merge Sort" -> new Thread(visualizer::mergeSort).start();
                    case "Quick Sort" -> new Thread(visualizer::quickSort).start();
                }
            });
            controls.add(button);
        }

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.DARK_GRAY);
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> visualizer.reset());

        JSlider sizeSlider = new JSlider(MIN_ELEMENTS, MAX_ELEMENTS, WIDTH / 5);
        sizeSlider.setBackground(new Color(52, 73, 94));
        sizeSlider.setForeground(Color.WHITE);
        sizeSlider.addChangeListener(e -> visualizer.setNumberOfElements(sizeSlider.getValue()));

        JSlider speedSlider = new JSlider(1, 99, 50);  // Slower at left, faster at right
        speedSlider.setBackground(new Color(52, 73, 94));
        speedSlider.setForeground(Color.PINK);
        speedSlider.addChangeListener(e -> visualizer.delay = 100 - speedSlider.getValue());

        controls.add(new JLabel("Size:"));
        controls.add(sizeSlider);
        controls.add(new JLabel("Speed:"));
        controls.add(speedSlider);
        controls.add(resetButton);

        frame.add(controls, BorderLayout.NORTH);
        frame.setVisible(true);
    }
}