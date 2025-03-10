#include <stdio.h>
#include <stdlib.h>
typedef struct {
    char *name;
    int age;
} person;

int main(int argc, char *argv[]) {
    person *mp =
        malloc(sizeof(person)); // sizeof isnt an actual function, the compiler
                                // interprets it & translates it to the actual
                                // memory size of the person struct
    mp->age = 25;
    mp->name = "John";

    printf("name: %s\n", mp->name);
    printf("Age: %d\n", mp->age);
    free(mp);

    int *p = malloc(sizeof(int));

    p = realloc(p, 2 * sizeof(int));
    p[0] = 10;
    p[1] = 200;
    // p[3] = 400; Undefined behavior
    printf("Should be 10: %d\n", p[0]);
    printf("Should be 200: %d\n", p[1]);
    free(p);

    return 0;
}
