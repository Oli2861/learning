#include <stdio.h>
#include <string.h>

int main() {
    int num = 2;

    for (int i = 0; i < 10; ++i) {
        printf("i: %d\n", i);
        char code;
        if (i % num == 0) {
            code = 'a';
            printf("%d is even\n", i);
        } else {
            code = 'b';
            printf("%d is odd\n", i);
        }
        printf("Code: %c\n", code);

        float result = (i / (float)num);
        int size = sizeof(result);
        printf("Size of the result: %d\n", size);
        printf("%d / %d = %f\n\n", i, num, result);
    }

    // float scientificNotationFloat = 12E4; // E4 = 10^4
    // printf("%f\n", scientificNotationFloat);
    double scientificNotation = 12.42E4; // E4 = number * 10^4
    printf("%lf\n", scientificNotation);
    printf("%.2f\n", scientificNotation);

    char names[15] = {};
    char *name = "Oliver";
    char nameEditable[] = "Oliver";
    strncat(names, name, 10);
    strncat(names, nameEditable, 10);
    nameEditable[5] = 'R';
    printf("Names: %s\nName: %s\nEdited name: %s\n%lu characters long\n", names,
           name, nameEditable, strlen(nameEditable));

    return 0;
}
