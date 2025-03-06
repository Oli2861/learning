#include <stdio.h>
#include <string.h>
void changer(int *ptr) {
    // Dereferencing: Accessing the value stored at the memory address held by
    // the pointer
    *ptr = 20;
}

int main(int argc, char *argv[]) {
    int variable;
    int *ptr = &variable; // Use memory address of variable and let the pointer
                          // *ptr point to it
    *ptr = 10;
    printf("%d\n", *ptr);
    changer(ptr);
    printf("%d\n", *ptr);

    // Pointer arithmetic
    char *sentence = "When incrementing a pointer, it moves to the next memory "
                     "location of its data type's size.";
    for (int i = 0; i < strlen(sentence); i++) {
        printf("%c", *(sentence + i));
    }
    printf("\n");

    int arr[5] = {11, 22, 33, 44, 55};
    int *p = arr;  // Points to the first element

    printf("%d\n", *p);
    printf("%d\n", *(p+2));
    printf("%d\n", *(p+4));
    return 0;
}
