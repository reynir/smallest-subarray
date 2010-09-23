import java.io.*;

public class Main {
    static final int VAL = 0;
    static final int NEXT = 1;
    static final int PREV = 2;
    /* The array that holds the sequence */
    static int[] N = new int[1000003];
    /* The array that acts as an array, a doubly linked list and a queue */
    static int[][] M = new int[1004][3];
    /* Head and tail for the queue */
    static int head, tail;

    public static void main(String... args) throws IOException {
        int T;
        String line;
        String[] s;

        BufferedReader in = new BufferedReader( new InputStreamReader(System.in));

        line = in.readLine();
        T = Integer.parseInt(line);

        N[0] = 1;
        N[1] = 2;
        N[2] = 3;

        for (int j=0; j<T; j++) {
            int n, m, k, res, min, nfound;
            // Read n, m, k
            line = in.readLine();
            s = line.split(" ");
            n = Integer.parseInt(s[0]);
            m = Integer.parseInt(s[1]);
            k = Integer.parseInt(s[2]);

            for (int i=3; i<n; i++) {
                N[i] = ((N[i-1] + N[i-2] + N[i-3]) % m) + 1;
            }

            res = -1;
            nfound = 0;

            for (int i=0; i<=k; i++) {
                M[i][NEXT] = -1;
                M[i][PREV] = -1;
            }

            for (int i=0; i<n; i++) {
                if (N[i] <= k) {
                    /* Store the position we've seen this number at. */
                    M[N[i]][VAL] = i;

                    if (nfound == 0) {
                        head = tail = N[i];
                        nfound++;
                    } else if (nfound < k-1) {
                        /* Do this while we have found 1..k-1 */
                        if (undiscovered(N[i])) {
                            nfound++;
                        }
                        update(N[i]);
                    } else if (nfound == k-1) {
                        if (undiscovered(N[i])) {
                            /* We have found the first solution! */
                            printList();
                            System.out.println("min1: "+(i-min()+1));
                            res = i - min() + 1;

                            nfound++;
                        }
                        update(N[i]);
                    } else {
                        if (N[i] == tail) {
                            update(N[i]);
                            min = min();
                            if (i-min+1 < res) {
                                res = i - min + 1;
                                printList();
                                System.out.println("min2: "+(i-min+1));
                            }
                        } else {
                            update(N[i]);
                        }
                    }
                }
            }
            if (res == -1) {
                System.out.printf("Case %d: sequence nai\n", j+1);
            } else {
                System.out.printf("Case %d: %d\n", j+1, res);
            }
        }
    }

    static void update(int pos) {
        if (pos == head) {
            return;
        }
        /* Remove... */
        if (M[pos][PREV]!=-1) {
            M[ M[pos][PREV] ][NEXT] = M[pos][NEXT];
        }
        if (M[pos][NEXT]!=-1) {
            M[ M[pos][NEXT] ][PREV] = M[pos][PREV];
        }
        if (tail == pos) {
            tail = M[pos][NEXT];
            M[tail][PREV] = -1;
        }

        /* Enqueue... */
        M[head][NEXT] = pos;
        M[pos][PREV] = head;
        head = pos;
        M[pos][NEXT] = -1;

        // printList();
    }
    static boolean undiscovered(int pos) {
        return (M[pos][NEXT] == -1 && M[pos][PREV] == -1);
    }

    static int min() {
        return M[tail][VAL];
    }
    static void printList() {
        int pos = head;
        System.out.print("f[");
        do {
            System.out.print(""+pos+":"+M[pos][VAL]+",");
            pos = M[pos][PREV];
        } while (pos != -1);
        System.out.println("]f");

        // System.out.print("b[");
        // printreverse(tail);
        // System.out.println("]b");
    }
    static void printreverse(int pos) {
        if (M[pos][NEXT]!=-1)
            printreverse(M[pos][NEXT]);
        System.out.print(""+pos+",");
    }
}


/* 
 * enqueue -> [10, 9, 4, 2, 1] <- dequeue
 */
