#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>
#include "mymalloc.h"

struct timeval start;
struct timeval end;

void test1() {
    //Stress Test 1
    gettimeofday(&start, NULL);

    for (int j = 0; j < 50; j++) {
        for (int i = 0; i < 120; i++) {
            char* ptr = malloc(1);
            free(ptr);
        }
    }

    gettimeofday(&end, NULL);
    printf("The average time for the first task in seconds is: %ld, in microseconds: %ld\n", (end.tv_sec-start.tv_sec)/50, (end.tv_usec-start.tv_usec)/50);
}
void test2() {
    //Stress Test 2
    gettimeofday(&start, NULL);

    for (int j = 0; j < 50; j++) {
        char *arr[120];

        for (int i = 0; i < 120; i++) {
            arr[i] = malloc(1);
        }

        for (int i = 0; i < 120; i++) {
            free(arr[i]);
        }
        if(j==0)break;
    }

    gettimeofday(&end, NULL);
    printf("The average time for the second task in seconds is: %ld, in microseconds: %ld\n", (end.tv_sec-start.tv_sec)/50, (end.tv_usec-start.tv_usec)/50);
}
void test3() {
    //Stress Test 3

    gettimeofday(&start, NULL);
    for (int j = 0; j < 50; j++) {
        char *arr[120];

        int alloc[120] = {0}; //initialize to all false
        int curr = 0;  //Start at place zero
        int numAlloc = 0; //Number of allocations that have been done

        while (numAlloc < 120) {
            if (curr == 0 || (rand() % 2 == 0 && curr < 120)) {
                arr[curr] = malloc(1); //Allocate one time
                numAlloc++;
                alloc[curr] = 1; //Set it to allocated true
                curr++;
            }

            else {
                curr--; //Dealloc once
                free(arr[curr]); //Free the block
                alloc[curr] = 0; //Current block is no longer allocated
            }
        }

        for (int i = 0; i < 120; i++) {
            if (alloc[i] == 1) {
                free(arr[i]);
            }
        }
    }
    gettimeofday(&end, NULL);
    printf("The average time for the third task in seconds is: %ld, in microseconds: %ld\n", (end.tv_sec-start.tv_sec)/50, (end.tv_usec-start.tv_usec)/50);
}

void test4() {
    //Stress Test 4

    gettimeofday(&start, NULL);
    for (int j = 0; j < 50; j++) {
        char *arr[120];

        for (int i = 0; i < 120; i++) {
            arr[i] = malloc(8);
        }

        for (int i = 0; i < 120; i++) {
            free(arr[i]);
        }
    }
    gettimeofday(&end, NULL);
    printf("The average time for the fourth task in seconds is: %ld, in microseconds: %ld\n", (end.tv_sec-start.tv_sec)/50, (end.tv_usec-start.tv_usec)/50);

}

void test5() {
    //Stress Test 5

    gettimeofday(&start, NULL);
    for (int j = 0; j < 50; j++) {
        char *arr[120];

        int alloc[120] = {0}; //initialize to all false
        int curr = 0; //Start at place zero

        while (curr < 120) {
            if (curr == 0 || (rand() % 2 == 0 && curr < 120)) {
                arr[curr] = malloc(1); //Allocate one time
                alloc[curr] = 1; //Set it to allocated true
                curr++;
            }

            else {
                curr--; //Dealloc once
                free(arr[curr]); //Free the block
                alloc[curr] = 0; //Current block is no longer allocated
            }
        }

        for (int i = 0; i < 120; i++) {
            if (alloc[i] == 1) {
                free(arr[i]);
            }
        }
    }
    gettimeofday(&end, NULL);
    printf("The average time for the fifth task in seconds is: %ld, in microseconds: %ld\n", (end.tv_sec-start.tv_sec)/50, (end.tv_usec-start.tv_usec)/50);

}

int main(int argc, char *argv[]) {
    test1();
    test2();
    test3();
    test4();
    test5();
}