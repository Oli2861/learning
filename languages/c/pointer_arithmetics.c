#include <stdio.h>

int main(int argc, char *argv[]) {
    int arr[5] = {1, 2, 3, 4, 5};
    for (int i = 0; i < 5; i++) {
        printf("%d, address: %p\n", arr[i], &arr[i]);
    
    }
    // first elment
    int *ptr = &arr[0];
    printf("Value: %d address: %p\n", *ptr, ptr);
    // 5th elment; shifted memory address by 4x4 bytes (int has 4 bytes)
    ptr += 4;
    printf("Value: %d address: %p\n", *ptr, ptr);
    // 5th elment
    ptr--;
    printf("Value: %d address: %p\n", *ptr, ptr);
    return 0;
}
