#include <stdio.h>

#define undiscovered(pos) (M[pos].next == NULL && M[pos].prev == NULL)

typedef struct pair_ pair;
struct pair_ {
    int val;
    pair *next;
    pair *prev;
};

static pair M[1004];
static int N[1000002];
static pair *head, *tail;

void update(pair *elm);

int main(void)
{
    int n, m, k, i, j, T, res, nfound;

    scanf("%d", &T);

    for (j=0; j<T; j++) {
        scanf("%d %d %d", &n, &m, &k);

        N[0]=1;
        N[1]=2;
        N[2]=3;

        if (k<=3) {
            printf("Case %d: %d\n", j+1,k);
            continue;
        }

        res = -1;
        nfound = 3;

        for (i=0; i<=k; i++) {
            M[i].next = NULL;
            M[i].prev = NULL;
        }

        M[1].val=0;
        M[2].val=1;
        M[3].val=2;
        head = tail = &M[1];
        update(&M[2]);
        update(&M[3]);

        for (i=3; i<n; i++) {
            N[i] = (N[i-1]+N[i-2]+N[i-3]) % m + 1;
            if (N[i]<=k) {
                M[N[i]].val = i;

                if (nfound < k-1) {
                    if (undiscovered(N[i])) 
                        nfound++;
                    update(&M[N[i]]);
                } else if (nfound == k-1) {
                    if (undiscovered(N[i])) {
                        res = i-tail->val+1;
                        if (res == k)
                            break;
                        nfound++;
                    }
                    update(&M[N[i]]);
                } else {
                    if (&M[N[i]] == tail) {
                        update(&M[N[i]]);
                        if (i-tail->val+1 < res) {
                            res = i - tail->val + 1;
                            if (res == k)
                                break;
                        }
                    } else {
                        update(&M[N[i]]);
                    }
                }
            }
        }
        if (res != -1)
            printf("Case %d: %d\n",j+1,res);
        else
            printf("Case %d: sequence nai\n",j+1);
    }

    return 0;
}

void update(pair *elm)
{
    if (elm == head)
        return;

    if (elm->prev != NULL)
        elm->prev->next = elm->next;
    if (elm->next != NULL)
        elm->next->prev = elm->prev;
    if (elm == tail) {
        tail = elm->next;
        tail->prev = NULL;
    }

    head->next = elm;
    elm->prev = head;
    head = elm;
    elm->next = NULL;
}
