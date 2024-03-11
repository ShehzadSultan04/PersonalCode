#include <stdio.h>
#include <stdlib.h>

void *mymalloc(size_t size, char *file, int line);
void myfree(void *ptr, char *file, int line);

int getSizeOfChunk(char *ptr);
void setSizeOfChunk(char *ptr, int size);
int getStatus(char *ptr);
void setStatus (char *ptr, int state);

void mergeChunks(void *ptr1, void *ptr2);

void memClear(char *ptr);

#define malloc(s) mymalloc(s, __FILE__, __LINE__)
#define free(p) myfree(p, __FILE__, __LINE__)

