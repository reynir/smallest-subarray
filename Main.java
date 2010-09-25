import java.io.*;

public class Main {
    /* Variables for indexing the list M. It's only to making the code more
     * readable */
    static final int VAL = 0;
    static final int NEXT = 1;
    static final int PREV = 2;
    static final int NIL = -1;

    /* The array that holds the sequence */
    static int[] N = new int[1000003];

    /* The array that acts as an array, a doubly linked list and a queue */
    static int[][] M = new int[1004][3];

    /* Head and tail for the queue */
    static int head, tail;

    /* It shouldn't throw any IOExceptions if the input is properly formatted */
    public static void main(String... args) throws IOException {
        int T;
        String line;
        String[] s;

        BufferedReader in = new BufferedReader( new
                InputStreamReader(System.in));

        line = in.readLine();
        T = Integer.parseInt(line);

        N[0] = 1;
        N[1] = 2;
        N[2] = 3;

        for (int j=0; j<T; j++) {
            int n, m, k, res, min, nfound;

            /* Read n, m and k */
            line = in.readLine();
            s = line.split(" ");
            n = Integer.parseInt(s[0]);
            m = Integer.parseInt(s[1]);
            k = Integer.parseInt(s[2]);

            /* A dirty fix for the special case k=1 that's not supposed to be allowed. */
            if (k==1) {
                System.out.printf("Case %d: 1", j+1);
                continue;
            }

            /* Initialize the sequence */
            for (int i=3; i<n; i++) {
                N[i] = ((N[i-1] + N[i-2] + N[i-3]) % m) + 1;
            }

            /* Reset variables */
            res = -1;
            nfound = 0;

            /* Reset all elements to 'undiscovered' */
            for (int i=0; i<=k; i++) {
                M[i][NEXT] = NIL;
                M[i][PREV] = NIL;
            }

            for (int i=0; i<n; i++) {
                if (N[i] <= k) {
                    /* Store the position we've seen this number at. */
                    M[N[i]][VAL] = i;

                    /* If we've found no numbers yet, make head and tail point
                     * to the only element in the list/queue */
                    if (nfound == 0) {
                        head = tail = N[i];
                        nfound++;
                    /* Keep incrementing on previously undiscovered numbers
                     * until we have found k-1 numbers */
                    } else if (nfound < k-1) {
                        /* Do this while we have found 1..k-1 */
                        if (undiscovered(N[i])) {
                            nfound++;
                        }
                        update(N[i]);
                    /* We have found all k numbers except for one. When we have
                     * found that number, we have our first solution */
                    } else if (nfound == k-1) {
                        if (undiscovered(N[i])) {
                            /* We have found the first solution! */
                            // printList();
                            // System.out.println("min1: "+(i-min()+1));
                            res = i - min() + 1;

                            nfound++;
                        }
                        update(N[i]);
                    /* Keep updating. If we find a number that is currently the
                     * tail of the list/queue, we have a new solution that we
                     * have to check */
                    } else {
                        if (N[i] == tail) {
                            update(N[i]);
                            min = min();
                            /* If it's better, assign it to res */
                            if (i-min+1 < res) {
                                res = i - min + 1;
                                // printList();
                                // System.out.println("min2: "+(i-min+1));
                            }
                        } else {
                            update(N[i]);
                        }
                    }
                }
            }
            /* Print "sequence nai" if there is no solution, otherwise print
             * the solution */
            if (res == -1) {
                System.out.printf("Case %d: sequence nai\n", j+1);
            } else {
                System.out.printf("Case %d: %d\n", j+1, res);
            }
        }
    }

    /* Update element at pos in the list/queue */
    static void update(int pos) {
        /* If it's the head, don't do anything */
        if (pos == head) {
            return;
        }

        /* Remove the number from the list/queue */
        if (M[pos][PREV]!=NIL) {
            M[ M[pos][PREV] ][NEXT] = M[pos][NEXT];
        }
        if (M[pos][NEXT]!=NIL) {
            M[ M[pos][NEXT] ][PREV] = M[pos][PREV];
        }
        if (tail == pos) {
            tail = M[pos][NEXT];
            M[tail][PREV] = NIL;
        }

        /* Insert/enqueue the number */
        M[head][NEXT] = pos;
        M[pos][PREV] = head;
        head = pos;
        M[pos][NEXT] = NIL;

        // printList();
    }

    /* Returns true if we have not previously discovered the element */
    static boolean undiscovered(int pos) {
        return (M[pos][NEXT] == NIL && M[pos][PREV] == NIL);
    }

    /* Return minimum/tail of the queue. Also known as dequeueing */
    static int min() {
        return M[tail][VAL];
    }

    /* Prints the list. Only for debugging */
    static void printList() {
        int pos = head;
        System.out.print("f[");
        do {
            System.out.print(""+pos+":"+M[pos][VAL]+",");
            pos = M[pos][PREV];
        } while (pos != NIL);
        System.out.println("]f");

        // System.out.print("b[");
        // printreverse(tail);
        // System.out.println("]b");
    }

    /* Prints the list, but using recursion and starting from the tail. Note
     * that it should print the elements in the same order as printList */
    static void printreverse(int pos) {
        if (M[pos][NEXT]!=NIL)
            printreverse(M[pos][NEXT]);
        System.out.print(""+pos+",");
    }
}
