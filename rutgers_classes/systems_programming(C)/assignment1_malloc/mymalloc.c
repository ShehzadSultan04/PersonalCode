#include <stdio.h>
#include <stdlib.h>
#include "mymalloc.h"

#define MEMLENGTH 512
#define ROUNDTO8(x) ((x + 7) & (-8))
static double memory[MEMLENGTH];

void *mymalloc(size_t size, char *file, int line) {
    if (size <= 0){
        printf("Cannot Allocate 0 Bytes\n");
        return NULL;
    }

    size = ROUNDTO8(size);

    if (size > ((MEMLENGTH * 8) - 8)) {
        printf("Not enough memory for an allocation of this size\n");
        return NULL;
    }
    
    void *ptrToReturn = NULL;
    char *currBlock = ((char *) memory) + 8; //Start at the first payload
    char *ptrToEnd = ((char *) (memory + MEMLENGTH)); //End of the memory array

    while (currBlock < ptrToEnd) { //Iterate through the memory array
        int chunkSize = getSizeOfChunk(currBlock); //Get the size of the current block
        int occupied = getStatus(currBlock); //Get the status of the current block

        // printf("The chunksize is %d, the state is %d\n", chunkSize, occupied);

        if ((currBlock == ((char *)memory + 8)) && chunkSize == 0) { //If memory is uninitialized (all zeros in memory)
            setSizeOfChunk(currBlock, size); //set the current pointers size to the size given plus 8
            setStatus(currBlock, 1); //set the block(the block to return) to occupied

            ptrToReturn = (void *) currBlock; //Set the pointer to return to current pointer

            if ((MEMLENGTH * 8 - 8) > size) {
                //Only do this code if there is extra space in memory after the first allocation
                currBlock += size + 8;
                setSizeOfChunk(currBlock, MEMLENGTH * 8 - size - 16); //Set the size of the free block to entire size in bytes, minus first block size, minus both metadatas
                setStatus(currBlock, 0); //Set the next block to free
            }
            return ptrToReturn; //Return the pointer at beggining of the payload
        }

        else if (occupied == 0 && chunkSize >= size) { //Check first for occupation, one less operation per occupied
            setSizeOfChunk(currBlock, size); //set the current pointers size to the size given
            setStatus(currBlock, 1); //set the block(the block to return) to occupied
            ptrToReturn = (void *) currBlock; //Skips the first 8 bytes for the metadata

            if (chunkSize > size) {
                currBlock += size + 8;
                setSizeOfChunk(currBlock, chunkSize - size - 8); // If the next chunk is a free chunk, reduce the size of the chunk by the allocated amount plus 8 for the next meta data for free block
                setStatus(currBlock, 0); 
            }
            
            return ptrToReturn; 
        }

        else if (occupied == 1 || chunkSize < size) {
            currBlock = currBlock + chunkSize + 8; 
        }
    }

    printf("System does not currently have enough space for allocation\n");
    return NULL; //Only way it will get here is if there is not enough space
}

int getSizeOfChunk(char *ptr) { //Returns the size of the payload
    return *((int *) (ptr-8)); 
}

void setSizeOfChunk(char *ptr, int size) { //Sets the size of the payload
    //ptr expects a memory address at the start of payload
    *((int *) (ptr - 8)) = size;
}

int getStatus(char *ptr) { //Returns 0 if free, 1 if occupied
    return *((int *) (ptr-4));
}

void setStatus (char *ptr, int state) { //State is 0 if free, 1 if occupied
    *((int *)(ptr-4)) = state; 
}

void myfree(void *ptr, char *file, int line) {
    char *heapCurr = ((char *) memory) + 8;
    char *ptrToEnd = ((char *) (memory + MEMLENGTH)); 

    if ((((char *)ptr > ptrToEnd)) || ((char *)ptr < ((char *) memory))) {
        //Check if the pointer is not in the range of the array
        printf("Not a valid pointer in range of memory\n");
        return;
    }

    while (heapCurr < ptrToEnd) {
        // printf("Current size: %d, Current Status: %d", getSizeOfChunk(heapCurr), getStatus(heapCurr));
        //First check if current pointer and pointer to free are adjacent
        if (getStatus(heapCurr) == 0 && ((heapCurr + getSizeOfChunk(heapCurr) + 8) == ptr)) {
            //Merge the current and the next
            if (getStatus(ptr + getSizeOfChunk(ptr) + 8) == 0) { 
                //If pointer that was given and the block after that is free, first coalesce these together
                mergeChunks(ptr, ptr + getSizeOfChunk(ptr) + 8);
            }

            mergeChunks(heapCurr, ptr); //Now coalesce the block before the pointer that is free and pointer block

            setStatus(ptr, 0); //sets the status of the meta data for the payload to zero
            // memClear(ptr); //sets all bytes inside of the payload to zero
            return; 
            
        }

        else if (heapCurr == ptr) { //The current ptr is the ptr we need, this would only hit when the previous block is not free

            if (getStatus(heapCurr) == 0) {
                //If the block is already free and user had the memory address, and did not guess the address, the address was already freed
                printf("Cannot provide same address twice to free up memory\n");
                return;
            }

            if (getStatus(ptr + getSizeOfChunk(ptr) + 8) == 0) { 
                //Check if the given block and the next block are free, if so, merge the two blocks
                mergeChunks(ptr, ptr + getSizeOfChunk(ptr) + 8); 
            }

            setStatus(ptr, 0); //Set the pointer to free now that we have freed it
            // memClear(ptr); //Set all bytes inside the payload to be zero
            return;  
        }

        heapCurr += getSizeOfChunk(heapCurr) + 8; //If none of the cases above are true, iterate pointer
    }

    //If this if statement is ever reached, and the pointer was in range of the memory allocated to us, then that means
    //user gave us a memory address that was not at the start of a malloc obtained payload block
    printf("Cannot provide an address not at the start of a chunk\n");
    return;

}

void mergeChunks(void *ptr1, void *ptr2) { 
    setSizeOfChunk(ptr1, getSizeOfChunk(ptr1) + getSizeOfChunk(ptr2) + 8); //Set the size of the first block to the size of the first block plus the size of the second block plus 8 for the metadata
}

void memClear(char *ptr) {
    //Sets all bytes inside of a given payload to zero
    for (int i = 0; i < getSizeOfChunk(ptr); i++) {
        *(ptr + i) = (unsigned char)0;
    }
}

// int main(int argc, char *argv[]) {
// }