#include <ctype.h>
#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

typedef struct words_node{
    char* word;
    int count; 
    struct words_node *next; 
}words_t;

void count_words(char *path);
void count (char *path);
int isRegChar(char c);
void addCountWord(char* word);

struct stat stats;

words_t *head = NULL;

int numOfWords = 0;

int isRegChar(char c) {
    if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '\'') {
        //This is a character or apostrophe
        return 1;
    }
    else if (c == '-') {
        //Dash character, only allowed if previous and next character are letters
        return 2;
    }
    else {
        //This is a space character, inlcuding white space, numbers, and punctuation
        return 0;
    }
}


void addCountWord(char *word) {
    //Find the node in the list
    // printf("The Word Is %s\n", word);
    
    words_t *temp = head; 

    if(numOfWords == 0) {
        head = (words_t *) malloc(sizeof(words_t));
        head->word = (char *) malloc(strlen(word) + 1);
        strcpy(head->word, word);
        head->count = 1;
        head->next = NULL;
        numOfWords++;
        return;
    }

    while (temp != NULL) { //If the node exists, just increase the count
        if (strcmp(temp->word, word) == 0) {
            temp->count++;
            return;
        }

        temp = temp->next;
    }

    //If it does not exist, add it to the list
    words_t *node = (words_t*) malloc(sizeof(words_t));
    node->word = (char *) malloc(strlen(word) + 1);
    strcpy(node->word, word);
    node->count = 1;
    node->next = NULL;

    words_t *linkedList = head; 

    while (linkedList->next != NULL)  {
        linkedList = linkedList->next;
    }

    linkedList->next = node;
}



void count_words(char *path) {
    //Count the words of a given file, not a directory

    // printf("OUTPUT: Path To Read From Is %s\n", path);

    int fd = open(path, O_RDONLY);

    if (fd == -1) {//Technically this should never be used, as we already know file exists
        perror("Open");
        return;
    }

    char *currWord = NULL; 
    char buff[20];
    long count = 0;

    char *curr = buff;

    char prevChar = '\0';
    char currChar;

    long currBuffIndex = 0; //Always less than 20
    long currFileIndex = 0; //Keep running the entire time
    long nextSize = 20; //Size of the next buff
    long fileLen = lseek(fd, 0, SEEK_END); //Length of the entire file

    // printf("File Length Is: %ld\n", fileLen);

    long endOfBufIndex = 20; //This is used when the buffer index may be smaller than 20, to keep track of when to end

    lseek(fd, 0, SEEK_SET); //Resets the current byte counter to 0;

    currWord = malloc(sizeof(char) * nextSize + 1); //Enough space for the next word plus null terminator

    while (fileLen - 1 - currFileIndex >= 20) {
        read(fd, buff, 20); //Read 20 characters
        while (currBuffIndex < endOfBufIndex) {
            currChar = *curr; 

            // printf("The Current Character Is %c, The File Index Is: %ld\n", currChar, currFileIndex);

            if (isRegChar(prevChar) && !isRegChar(currChar)) {
                //This is a character that seperates words, not inlcuding special case in dashes
                //Should add curr word, not inluding this character, to the linked list, then reset the word string
                if (count > 0){
                    currWord[count] = '\0';
                    addCountWord(currWord);
                }

                free(currWord); 
                count = 0;
                nextSize = 20;
                currWord = malloc(sizeof(char) * nextSize + 1);
            }
            else if (!isRegChar(prevChar) && !isRegChar(currChar)) {

            }

            else if (isRegChar(prevChar) == 2 && isRegChar(currChar) == 2) {
                //If previous character was dash and current character is a dash, remove a dash from the current word
                //string and add it to the linked list, and then clear string

                if (count > 0){
                    currWord[count-1] = '\0';
                    addCountWord(currWord);
                
                    free(currWord); 
                    count = 0;
                    nextSize = 20;
                    currWord = malloc(sizeof(char) * nextSize + 1);
                }
            }

            else {
                //The character is not a space character seperating words or double dash character, add the character
                //to the current string and reiterate
                // printf("Char to add: %c\n", currChar);
                currWord[count] = currChar;
                count++;

                if (count == nextSize) {//If you are at the end of the allocated size of the word, increase size and keep going, want to find end of word
                    char *tempWrd = malloc(sizeof(char) * nextSize + 1);
                    memcpy(tempWrd, currWord, nextSize);
                    free(currWord);

                    currWord = malloc(sizeof(char) * (2 * nextSize) + 1);
                    memcpy(currWord, tempWrd, nextSize);
                    nextSize = nextSize * 2;
                    free(tempWrd);
                }
            }

            curr++;
            currBuffIndex++;
            currFileIndex++;

            prevChar = currChar;
        }

        currBuffIndex = 0;
        curr = buff;
    }

    if (fileLen - currFileIndex <= 20 && fileLen - currFileIndex > 0) {
        read(fd, buff, fileLen - currFileIndex);
        endOfBufIndex =  fileLen - currFileIndex;
        while(currBuffIndex < endOfBufIndex) {
            currChar = *curr;
            // printf("The Current Character Is Here %c, The File Index Is: %ld\n", currChar, currFileIndex);

            if (isRegChar(prevChar) && !isRegChar(currChar)) {
                //This is a character that seperates words, not inlcuding special case in dashes
                //Should add curr word, not inluding this character, to the linked list, then reset the word string
                if (count > 0){
                    currWord[count] = '\0';
                    addCountWord(currWord);

                    free(currWord); 
                    count = 0;
            
                    nextSize = 20;
                    currWord = malloc(sizeof(char) * nextSize + 1);
                }
            }

            else if (isRegChar(prevChar) == 2 && isRegChar(currChar) == 2) {
                //If previous character was dash and current character is a dash, remove a dash from the current word
                //string and add it to the linked list, and then clear string

                if (count > 0){
                    currWord[count - 1] = '\0';
                    addCountWord(currWord);
                
                free(currWord); 
                count = 0;
                nextSize = 20;
                currWord = malloc(sizeof(char) * nextSize + 1);
                }
            }

            else if (!isRegChar(prevChar) && !isRegChar(currChar)) {
                
            }

            else if (currFileIndex == fileLen - 1 && !isspace(currChar)) {
                currWord[count] = currChar;
                currWord[count + 1] = '\0';
                addCountWord(currWord);
            }
            
            else {
                //The character is not a space character seperating words or double dash character, add the character
                //to the current string and reiterate
                currWord[count] = currChar;
                count++;

                if (count == nextSize) {//If you are at the end of the allocated size of the word, increase size and keep going, want to find end of word
                    char *tempWrd = malloc(sizeof(char)*nextSize + 1);
                    memcpy(tempWrd, currWord, nextSize);
                    free(currWord);

                    currWord = malloc(sizeof(char) * (nextSize * 2) + 1);
                    memcpy(currWord, tempWrd, nextSize);
                    nextSize = nextSize * 2;
                    free(tempWrd);
                }
            }
            
            curr++;
            currBuffIndex++;
            currFileIndex++;

            prevChar = currChar;

        }
    }

    free(currWord);
    close(fd);
}

void count (char *path) {
    //If the path is a file, count words, BASE CASE
    //If the path is a directory, call count again on the directory

    DIR *d = opendir(path); //Open a directory to the path

    if (d == NULL) { //If directory does not open, return error
        printf("Failed To Open Directory\n");
        return; 
    }
            
    struct dirent *dir;

    while ((dir = readdir(d)) != NULL) {
        char d_path[257]; 
        sprintf(d_path, "%s/%s", path, dir->d_name);

        if (stat(d_path, &stats) == 0) {

            if (stats.st_nlink == 1) { //The path points to a file, not a directory
                // if(strlen(dir->d_name) > 4 && strcmp(dir->d_name + strlen(dir->d_name) - 4, ".txt"));
                count_words(d_path);
                continue;
            }

            else if (stats.st_nlink >= 2 && strcmp(dir->d_name, ".") != 0 && strcmp(dir->d_name, "..") != 0) {
                count(d_path);
            }
        }
    }
        closedir(d);
}




int main (int argc, char* argv[]) {
    // int *a = malloc(20 * sizeof(int));
    // int *b = a+10;
    // free(b);
    if (argc < 2) {
        printf("Incorrect Usage: Expected 1 Or More Arguments\n");
    }
    
    head = NULL; 

    for (int i = 1; i < argc; i++) { //start at first argument, since 0th argument is the program call
        if(strlen(argv[i]) > 4 && strcmp(argv[i] + strlen(argv[i]) - 4, ".txt") == 0)
            count_words(argv[i]); //If the given argument is a text file count words in it
        else 
            count(argv[i]); //Otherwise, recursively find text files in this directory and count words= counts
    }


    words_t *tempWord = head; 
    int length = 0; 
    while(tempWord != NULL) { 
        // printf("The current word is %s, frequency is %d\n", tempWord->word, tempWord->count);
        length++;
        tempWord = tempWord->next;
    } //counts the length of the list of words

    tempWord = head;
    if (length == 1) { //If it is just one element, delete the first node and finish
        printf("%s %d\n", tempWord->word, tempWord->count);
        tempWord = tempWord->next;
        free(head->word);
        free(head);
        head = tempWord;
        return 0; 
    }
    
    // tempWord = head; 
    // for (int i = 0; i < length; i++) {
    //     printf("The Word Is: %s, Its Count Is %d\n", tempWord->word, tempWord->count);
    //     tempWord = tempWord->next; 
    // }

    words_t *ptr = malloc(sizeof(words_t));
    words_t *ptrToHead = ptr; 
    words_t *prev = ptrToHead; 
    tempWord = head;

    int indexHigh = 0;
    int countHigh = 0;

    while(head != NULL) {
        ptrToHead->next = head;
        prev=ptrToHead;
        tempWord = head;
        countHigh = head->count;
        indexHigh = 0;

        for (int i = 1; i < length; i++) {
            if (tempWord->next->count > countHigh) {
                indexHigh = i;
                countHigh = tempWord->next->count;
                prev = tempWord;
            }
            else if (tempWord->next->count == countHigh) {
                // printf("The words it compared are %s, %s\n", tempWord->next->word, prev->next->word);
                if(strcmp(tempWord->next->word, prev->next->word) < 0) {
                    // printf("Switched Because Alphabetical To %s\n", tempWord->next->word);
                    indexHigh = i;
                    countHigh = tempWord->next->count;
                    prev = tempWord;
                }
            }

            tempWord = tempWord->next;
            // printf("The node to delete is %s, %s\n", prev->next->word, head->word);
        }

        tempWord = head; 

        if (indexHigh == 0) {
            // printf("Delete 1 With Word %s\n", tempWord->word);
            printf("%s %d\n", tempWord->word, tempWord->count);
            tempWord = tempWord->next;
            free(head->word);
            free(head);
            head = tempWord;
            // printf("Head is Currently: %s\n", head->word);
        }

        else {
            words_t *temp = prev->next;
            // printf("Delete 2 With Word: %s\n", prev->next->word);
            printf("%s %d\n", temp->word, temp->count);
            prev->next = prev->next->next;
            free(temp->word);
            free(temp);
        }
        length--;
    }

    free(ptr);
}