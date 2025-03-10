#include <stdio.h>

void foo(void) {
    printf("Hello World!\n"); 
    char vowels[2][5] = {
        {'A', 'E', 'I', 'O', 'U'},
        {'H', 'E', 'L', 'L', 'O'},
    };

    char (*ptr)[2][5] = &vowels;

    int length1d = sizeof(vowels[0]) / sizeof(char);
    printf("length1d = %d\n", length1d);


    int length0d = sizeof(vowels) / sizeof(vowels[0]);
    printf("length0d = %d\n", length0d);

    for (int y = 0; y < length0d; y++) {
        for (int x = 0; x < length1d; x++) {
            printf("vowels[%d][%d]: %c via pointer: %c\n", y, x, vowels[y][x], (*ptr)[y][x]);
        }
    }

    int totalLength = sizeof(vowels) / sizeof(char);
    printf("totalLength = %d\n", totalLength);
}
