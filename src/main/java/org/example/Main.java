// Online Java Compiler (Editor)
// Write and run Java online using this editor.

class Main {

    public static int[] findTrasactionID(int[] transactions, int transactionId)
    {

        int[] result = new int[]{-1, -1};


        int left=0,right=transactions.length-1;

        while(left<=right)
        {
            int mid = left+ (right-left)/2;


            if(transactions[mid]==transactionId)
            {
                right = mid-1;
                result[0] = mid;

            }
            else if( transactions[mid]> transactionId)
            {
                right = mid-1;

            }
            else{
                left = mid+1;

            }
        }
        left =0;
        right = transactions.length-1;

        while(left<=right)
        {
            int mid = left+ (right-left)/2;


            if(transactions[mid]==transactionId)
            {
                left = mid+1;
                result[1] = mid;

            }
            else if( transactions[mid]> transactionId)
            {
                right = mid-1;

            }
            else{
                left = mid+1;

            }
        }

        return result;


    }
    public static void main(String[] args) {

        int[] transactions = new int[]{1001, 1003, 1003, 1005, 1005, 1008};

        int transactionId = 1009;

        int[] result = findTrasactionID(transactions,transactionId);
        System.out.println(result[0]);
        System.out.println(result[1]);
    }
}