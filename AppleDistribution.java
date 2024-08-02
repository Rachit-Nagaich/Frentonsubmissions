import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class AppleDistribution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Integer> weights = new ArrayList<>();

        while (true) {
            System.out.print("Enter apple weight in grams (-1 to stop): ");
            int weight = sc.nextInt();
            if (weight == -1) break;
            weights.add(weight);
        }

        Collections.sort(weights, Collections.reverseOrder());

        List<Integer> ram = new ArrayList<>();
        List<Integer> sham = new ArrayList<>();
        List<Integer> rahim = new ArrayList<>();

        int ramPaid = 50;
        int shamPaid = 30;
        int rahimPaid = 20;

        int totalPaid = ramPaid + shamPaid + rahimPaid;

        double ramRatio = (double) ramPaid / totalPaid;
        double shamRatio = (double) shamPaid / totalPaid;
        double rahimRatio = (double) rahimPaid / totalPaid;

        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();

        int ramTarget = (int) Math.round(totalWeight * ramRatio);
        int shamTarget = (int) Math.round(totalWeight * shamRatio);
        int rahimTarget = (int) Math.round(totalWeight * rahimRatio);

        int ramWeight = 0;
        int shamWeight = 0;
        int rahimWeight = 0;

        for (int weight : weights) {
            if (ramWeight + weight <= ramTarget) {
                ram.add(weight);
                ramWeight += weight;
            } else if (shamWeight + weight <= shamTarget) {
                sham.add(weight);
                shamWeight += weight;
            } else {
                rahim.add(weight);
                rahimWeight += weight;
            }
        }

        System.out.println("Distribution Result:");
        System.out.println("Ram: " + ram);
        System.out.println("Sham: " + sham);
        System.out.println("Rahim: " + rahim);
    }
}


