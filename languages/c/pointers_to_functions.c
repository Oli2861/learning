#include <stdio.h>
#include <stdlib.h>
char *function(int arg, char *ptr) {
    size_t needed = snprintf(NULL, 0, "%s, %d", ptr, arg) + 1;
    char *buffer = malloc(needed);
    char res = sprintf(buffer, "%s, %d", ptr, arg);
    return buffer;
}

int compareToForDesc(const void *left, const void *right) {
    int *l = (int *)left;
    int *r = (int *)right;
    return *r - *l;
}

int main(int argc, char *argv[]) {
    // *pf pointer to a function with return type char* and a int and a *char as
    // arguments
    char *(*pf)(int, char *) = &function;
    char arg1[] = "Test";

    char *result = pf(2, arg1);
    printf("Thats the result %s\n", result);
    free(result);

    int (*cT)(const void *, const void *) = &compareToForDesc;
    int arr[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    int arrLength = sizeof(arr) / sizeof(*arr);
    qsort(arr, arrLength, sizeof(*arr), cT);

    for (int i = 0; i < arrLength; i++) {
        printf("%d\n", arr[i]);
    }

    return 0;
}
