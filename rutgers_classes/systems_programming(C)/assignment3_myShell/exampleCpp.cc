#include <stdlib.h> 
#include <stdio.h>

int main() {
    int *ptr = (int *) malloc(1000);
    if (ptr == NULL) {
        return 1;
    }
    else {
        free(ptr);
        printf("Memory allocated and freed successfully\n");
    }

}