

public class recursiveSum {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5};
        System.out.println(recursiveSum(array, 0));
    }

    public static int recursiveSum(int[] array, int startIndex) {
        if (startIndex == array.length) {
            return 0;
        }
        return array[startIndex] + recursiveSum(array, startIndex + 1);
    }
}