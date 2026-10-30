package org.example;
import java.util.*;

public class Patients {

    public int[] topKFrequent(int[] nums, int k) {
        // Your implementation
        Map<Integer,Integer> freq = new HashMap<>();
        for(int n:nums)
        {
            freq.put(n,freq.getOrDefault(n,0)+1);
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(freq::get));

        for(int key: freq.keySet())
        {
            pq.offer(key);
            if(pq.size()>k)
            {
                pq.poll();
            }

        }
        List<Integer> ans = new ArrayList<>();

        while(!pq.isEmpty()){
            ans.add(pq.poll());
        }
        return ans.stream().mapToInt(i -> i).toArray();
    }

    public static int maxPatients(int[][] patients)
    {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        Arrays.sort(patients, Comparator.comparingInt(a -> a[0]));

        int currentDay=1;
        int patientsSeen=0;
        int patientIndex =0;

        while(!pq.isEmpty() || patientIndex < patients.length)
        {
            if (pq.isEmpty() && patientIndex < patients.length) {
                currentDay = Math.max(currentDay, patients[patientIndex][0]);
            }
            while(patientIndex< patients.length &&  patients[patientIndex][0]<=currentDay)
            {
                pq.offer(patients[patientIndex][1]);
                patientIndex++;
            }
            while(!pq.isEmpty() && pq.peek()<currentDay)
            {
                pq.poll();
            }

            if(!pq.isEmpty())
            {
                pq.poll();
                patientsSeen++;

            }
            currentDay++;
        }

        return patientsSeen;
    }
    public static void  main(String[] args)
    {
        int[][] patients = {{1,2},{2,1},{2,2}};
        System.out.println(maxPatients(patients));

    }
}
