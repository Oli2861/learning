#include <stdlib.h>
#include <stdio.h>
typedef struct {
    char* name;
    int age;
} person;

int main(int argc, char *argv[])
{
    person * mp = malloc(sizeof(person)); // sizeof isnt an actual function, the compiler interprets it & translates it to the actual memory size of the person struct
    mp->age = 25;
    mp->name = "John";

    printf("%s\n", mp->name);
    printf("%d\n", mp->age);
    free(mp);
    return 0;
}
