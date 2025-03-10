#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main(int argc, char *argv[]) {
    // String & char Array
    char *s = "Hello World!";
    char sArr[] = "Hello World!";
    printf("%s\n", s);

    for (int i = 0; i < strlen(s); i++) {
        printf("%c\n", s[i]);
        printf("%c\n", sArr[i]);
    }
    // Modification
    // s[0] = 'I'; Error
    sArr[0] = 'I'; // Works!
    printf("%c\n", sArr[0]);

    // String termination: End marked by string terminator
    printf("----- Termination -----\n");

    char a = s[0];
    for (int i = 0; a != '\0'; i++) {
        a = s[i];
        printf("%c\n", a);
    }

    // Copy a string
    printf("----- Copy -----\n");
    char sArrCpy[strlen(sArr)];
    strcpy(sArrCpy, sArr);
    printf("%s\n", sArrCpy);

    return 0;
}
