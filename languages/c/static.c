#include <stdio.h>
int counting() {
    
    int count = 0;
    count ++;
    return count;
}

int countingStatic() {
    
    static int count = 0;
    count ++;
    return count;
}

int EXIT_SUCCESS = 0;

int main(int argc, char *argv[])
{
    printf("%d\n", counting());
    printf("%d\n", countingStatic());
    printf("%d\n", countingStatic());
    return EXIT_SUCCESS;
}
