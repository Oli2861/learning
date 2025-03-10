# C Notes
## Pointer *
- Same meaning: Index into memory = Address in memory = Location in memory
- Address != data stored at address, but a reference to a locaiton where data is stored
- Find address of data using `&`
- Print using `%p`
- *Dereferencing*: Pointers are *referring* to some location - access to the location is called *dereferencing* a pointer
```c
// Create a pointer to a memory space. Here to a allocated space of the size of n chars
int n = 5;
int *pointer; // Create pointer
pointer = &n; // Point to memory space, where n is stored
*pointer = 20; // Store 20 at that memory space
printf("20 == %d\n", *pointer); // Dereferencing: Read the value from the memory space

```
```c
// Point to NULL == point to nothing --> dereferencing will crash!
int *p = NULL;
```

### Pointers vs arrays
- Arrays = block of sequential memory locations
- Pointers store address of a memory location
- Memory
    - Arrays: `int arr[10];` - compiler allocates 10 integers worth of memory
    Pointers: `int *ptr;` - compiler allocates only enough memory for an address
- Assignment
    - Arrays cant be rassigned
    - Pointers can be reassigned

## Address-of &
```c
int x = 10;
int *ptr = &x; // Address of x

void increment(int *p) {
    (*p)++;  // Dereference the pointer and increment the value
}

int main() {
    int num = 5;
    increment(&num);  // Pass the address of num to the function
    printf("%d", num);  // Prints 6
    return 0;
}
```
## Arrays
- Length of an arrays
    - `sizeof` array to get the memory space allocated for it
    - `sizeof` the item to get the memory space needed per item
    - Length of int array = $\frac{sizeof arr}{sizeof int}$
- Initializers
    - `char vowels[] = {'A', 'E', 'I', 'O', 'U'};`
    - `char[5];`
    - `int[100] = {0}` (All initialized to 0)
    - `int a[10] = {0, 11, 22, [5]=55, 66, 77};` --> `0 11 22 0 0 55 66 77 0 0` (5 is start for 55)
- Out of bounds: C doesnt warn you

```c
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
/*
Output:
length1d = 5
length0d = 2
vowels[0][0]: A via pointer: A
vowels[0][1]: E via pointer: E
vowels[0][2]: I via pointer: I
vowels[0][3]: O via pointer: O
vowels[0][4]: U via pointer: U
vowels[1][0]: H via pointer: H
vowels[1][1]: E via pointer: E
vowels[1][2]: L via pointer: L
vowels[1][3]: L via pointer: L
vowels[1][4]: O via pointer: O
totalLength = 10
*/
```

