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

## Pointer *
```c
// Create a pointer to a memory space. Here to a allocated space of the size of n chars
char *pvowels = malloc(n * sizeof(char));
// Create a pointer to the memory space where i is stored.
int i;
char *pvowels = &i;
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
