#include "stdio.h"
#include <stdlib.h>

int main(int argc, char *argv[])
{
    char vowels[] = {'A', 'E', 'I', 'O', 'U'};
    char *pointer = vowels;

    int i;
    // Print the addresses
    for (i = 0; i < 5; i++) {
        printf("&vowels[%d]: %p, pvowels + %d: %p, vowels + %d: %p\n", i, &vowels[i], i, pointer + i, i, vowels + i);
    }

    // Print the values
    for (i = 0; i < 5; i++) {
        printf("vowels[%d]: %c, *(pvowels + %d): %c, *(vowels + %d): %c\n", i, vowels[i], i, *(pointer + i), i, *(vowels + i));
    }
    printf("\n");

    int n = 5;
    char *pvowels = malloc(n * sizeof(char));

    pvowels[0] = 'A';
    pvowels[1] = 'E';
    *(pvowels + 2) = 'I';
    for (i = 0; i < n; i++) {
        printf("%c ", pvowels[i]);
    }

    printf("\n%s\t%s\n", (pvowels + 2), pvowels);
    free(pvowels);
    return 0;
}
