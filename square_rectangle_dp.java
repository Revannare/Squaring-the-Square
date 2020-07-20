import com.google.common.collect.Collections2;
import com.google.common.primitives.Ints;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class square_rectangle_dp {
    static HashMap<int[], Integer> results = new HashMap<>();
    static ConcurrentLinkedQueue<int[]> tempBuffer = new ConcurrentLinkedQueue<>();

    static boolean dpTable[][];

    public static boolean flow_check(int strip[], int lenth, int width) {
        ArrayList<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < strip.length; i++) {
            if (strip[i] == 1){
                tempList.add(i);
            }
        }

        int[] numbers = new int[tempList.size()];
        for (int i = 0; i < tempList.size(); i++) {
            numbers[i] = tempList.get(i);
        }

        List<Integer> vals = Ints.asList(numbers);
        Collection<List<Integer>> orderPerm = Collections2.permutations(vals);

        for (List<Integer> val : orderPerm) {
            Integer[] order = new Integer[val.size()];
            val.toArray(order);
            if (flow_check_single_step(order, lenth, width)){
                return true;
            }
        }
        return false;
    }

    // This function is to check by flow for one order
    public static boolean flow_check_single_step(Integer order[], int lenth, int width) {
        int[] flow = new int[lenth+1];
        flow[0] = width;
        for (int i = 1; i < flow.length; i++) {
            flow[i] = 0;
        }
        for (int i = 0; i < order.length; i++) {
            int targetIndex = 0;
            for (int k = 0; k < flow.length; k++)
            {
                if (flow[k] > 0 && flow[targetIndex] > flow[k]) {
                    targetIndex = k;
                }
            }
            int moveIndex = order[i] + 1 + targetIndex;

            if (moveIndex > lenth) {
                return false;
            } else {
                flow[targetIndex] = flow[targetIndex] - order[i] - 1;
                flow[moveIndex] = flow[moveIndex] + order[i] + 1;
//                if (flow[targetIndex]<0){
//                    return false;
//                }
            }
        }
        if (flow[lenth] != width)
        {
            return false;
        }
        for (int k = 0; k < lenth - 1; k++)
        {
            if (flow[k] != 0) {
                return false;
            }
        }
        for (int i = 0; i < order.length; i++) {
            System.out.print(order[i] + 1 + " ");
        }
        return true;
    }


    static void checkdp(ArrayList<Integer> p, int lenth, int width){
        int[] strip = new int[width];
        for (int i = 0; i < strip.length; i++) {
            strip[i] = 0;
        }
        for (int i = 0; i < p.size(); i++) {
            strip[(int) Math.sqrt(p.get(i)) - 1] = 1;
        }
        int count = 0;
        int max_1 = 0;
        int max_2 = 0;
        for (int i = strip.length - 1; i >= 0; i--) {
            if (strip[i] != 0 && count!=2) {
                count++;
                max_1 = max_1==0?i:max_1;
                max_2 = max_1==0?0:i;
            }
        }
        if (max_1+max_2+2 > lenth){
            return;
        }
        flow_check(strip, lenth, width);
    }

    public static void printSubsetsRec(int arr[], int i, int sum,
                                ArrayList<Integer> p, int lenth, int width)
    {
        // If we reached end and sum is non-zero. We print
        // p[] only if arr[0] is equal to sun OR dp[0][sum]
        // is true.
        if (i == 0 && sum != 0 && dpTable[0][sum])
        {
            p.add(arr[i]);
            checkdp(p, lenth, width);
            p.clear();
            return;
        }

        // If sum becomes 0
        if (i == 0 && sum == 0)
        {
            checkdp(p, lenth, width);
            p.clear();
            return;
        }

        // If given sum can be achieved after ignoring
        // current element.
        if (dpTable[i-1][sum])
        {
            // Create a new vector to store path
            ArrayList<Integer> b = new ArrayList<>();
            b.addAll(p);
            printSubsetsRec(arr, i-1, sum, b, lenth, width);
        }

        if (sum >= arr[i] && dpTable[i-1][sum-arr[i]])
        {
            p.add(arr[i]);
            printSubsetsRec(arr, i-1, sum-arr[i], p, lenth, width);
        }
    }

    public static void printAllSubsets(int arr[], int n, int sum, int lenth, int width)
    {
        if (n == 0 || sum < 0)
            return;

        // Sum 0 can always be achieved with 0 elements
        dpTable = new boolean[n][sum + 1];
        for (int i=0; i<n; ++i)
        {
            dpTable[i][0] = true;
        }

        if (arr[0] <= sum)
            dpTable[0][arr[0]] = true;

        // Fill rest of the entries in dp[][]
        for (int i = 1; i < n; ++i)
            for (int j = 0; j < sum + 1; ++j)
                dpTable[i][j] = (arr[i] <= j) ? (dpTable[i-1][j] ||
                        dpTable[i-1][j-arr[i]])
                        : dpTable[i - 1][j];
        if (dpTable[n-1][sum] == false)
        {
            System.out.println("There are no subsets with" +
                    " sum "+ sum);
            return;
        }

        
        ArrayList<Integer> p = new ArrayList<>();
        printSubsetsRec(arr, n-1, sum, p, lenth, width);
    }

    // main
    public static void main(String[] args) {

        int lenth = 0;
        int width = 0;
        Scanner input=new Scanner(System.in);
        System.out.println("input the length of the rectangle:");
        lenth=input.nextInt();
        System.out.println("input the width of the rectangle:");
        width=input.nextInt();

        int temp_w = Math.min(lenth, width);
        int temp_l = Math.max(lenth, width);
        width = temp_w;
        lenth = temp_l;

        System.out.println("start checking the length of the square: "+lenth+" x "+width);


        int arr[] = new int[width];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (i+1) * (i+1);
        }
        int n = arr.length;
        int sum = width * lenth;

        printAllSubsets(arr, n, sum, lenth, width);
    }
}
