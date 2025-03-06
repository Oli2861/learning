#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
struct Node {
    int value;
    struct Node *next;
};

void print_list(struct Node *head) {

    int i = 0;
    struct Node *current = head;

    while (current != NULL) {
        printf("%d: %d\n", i++, current->value);
        current = current->next;
    }
}

void push(struct Node *head, int value) {
    struct Node *last = head;
    while (last->next != NULL) {
        last = last->next;
    }

    struct Node *new = malloc(sizeof(struct Node));
    last->next = new;
    new->value = value;
    new->next = NULL;
}

bool insert(struct Node *head, int position, int value) {
    struct Node *current = head;
    for (int i = 0; current != NULL; i++) {
        if (i == position) {
            struct Node *next = current->next;
            struct Node *new = malloc(sizeof(struct Node));
            new->next = next;
            new->value = value;
            current->next = new;
            return true;
        } else if (current->next != NULL) {
            current = current->next;
        } else {
            return false;
        }
    }
    return false;
}

void free_memory(struct Node *head) {
    struct Node *current = head;
    struct Node *temp;
    while (current != NULL) {
        temp = current;
        current = current->next;
        free(temp);
    }
}

int pop(struct Node **head) {
    if (head == NULL) {
        return -1;
    }

    struct Node *next = (*head)->next;
    int value = (*head)->value;
    *head = next;
    return value;
}

int removeItem(struct Node *head, int position) {

    struct Node *current = head;
    for (int i = 0; current != NULL; i++, current = current->next) {

        if (i == position - 1) {
            struct Node *tmp = current->next;
            current->next = current->next->next;

            int retVal = tmp->value;
            free(tmp);
            return retVal;

        } else if (current->next == NULL) {
            return -1;
        }
    }

    return 1;
}

int main(int argc, char *argv[]) {
    struct Node head;
    struct Node *head_ptr = malloc(sizeof(struct Node));
    (*head_ptr).value = 10;

    struct Node *otherNode = malloc(sizeof(struct Node));
    (*head_ptr).next = otherNode;

    (*otherNode).value = 20;
    (*otherNode).next = NULL;

    push(head_ptr, 30);
    bool insert_success = insert(head_ptr, 1, 40);
    printf("Inser success: %s\n", insert_success ? "true" : "false");

    print_list(head_ptr);
    struct Node **ptrToHeadPrt = &head_ptr;
    printf("Pop res: %d\n", pop(ptrToHeadPrt));

    print_list(head_ptr);
    removeItem(head_ptr, 1);
    printf("After remove item at 1:\n");
    print_list(head_ptr);

    free_memory(head_ptr);

    return 0;
}
