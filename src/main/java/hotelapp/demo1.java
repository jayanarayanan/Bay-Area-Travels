package hotelapp;

public class demo1 {
    public static int[][] divideB(int[] nums) {
        int[] a = new int[nums.length];
        int[] b = new int[nums.length];
        int n = nums.length;
        int[] powers = new int[n];
        powers[0] = 1;
        for (int i = 1; i < n; i++) {
            powers[i] = powers[i - 1] * 2;
        }
        int sumA = 0;
        int sumB = 0;
        int i = n - 1;
        while (i >= 0) {
            if (nums[i] == 0) {
                i--;
            }
            if (sumA == sumB) {
                if (nums[i] > 1) {
                    int num = nums[i] / 2;
                    a[i] += num;
                    b[i] += num;
                    sumA += num * powers[i];
                    sumB = sumA;
                    nums[i] %= 2;
                } else {
                    a[i] += 1;
                    sumA += powers[i];
                    nums[i] -= 1;
                }
            } else {
                int num = Math.min(nums[i], (sumA - sumB) / powers[i]);
                b[i] += num;
                sumB += num * powers[i];
                nums[i] -= num;
            }
        }
        if (sumA == sumB) {
            return new int[][]{a, b};
        }
        return null;
    }
}
