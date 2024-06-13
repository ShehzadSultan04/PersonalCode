#include <errno.h>
#include <fcntl.h>
#include <glob.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#define MAX_COMMAND_LENGTH 1024
#define MAX_ARGS 100

void batch_mode(char *filename);
void exec_command(char *argv[]);
void exec_command_pipe(char *args1[], char *args2[]);
void wildcard_expansion(char *arg, char **argv, int *argc);
char* getPath(char *programName);
void interactive_mode();
void parse_input(char *input);

int lastSuccess; //Boolean value, checks if the last command was successful

void batch_mode(char *filename) {
    char input[MAX_COMMAND_LENGTH];
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("could not open file");
        exit(EXIT_FAILURE);
    }

    while (fgets(input, MAX_COMMAND_LENGTH, file)) {
        input[strlen(input) - 1] = 0;
        parse_input(input);
    }

    fclose(file);
}

void interactive_mode() {
    printf("Welcome to my shell!!! :( (Sam wanted the sad face, dw who Sam is) \n");
    char input[MAX_COMMAND_LENGTH];

    while (1) {
        printf("mysh> ");
        // read_line();
        if (!fgets(input, MAX_COMMAND_LENGTH, stdin)) {
            // if get wrong command
            break;
        }

        input[strlen(input) - 1] = 0; //Remove the newline character, put a null character instead

        if (strcmp(input, "exit") == 0) {
            printf("mysh: exiting\n");
            break;
        }

        parse_input(input);
    }
     
}

void parse_input(char *input) {
    char *args[MAX_ARGS]; //Array to store command arguments
    char *args_pipe[MAX_ARGS]; //Stores arguemnts after the pipe character

    char *token = strtok(input, " "); //This line splits it with the next space, must do in every loop
    
    if (strcmp(token, "then") == 0 && !lastSuccess) {
        return;
    }

    else if (strcmp(token, "then") == 0 && lastSuccess) {
        //when we want it to run
        token = strtok(NULL, " ");
    }

    else if (strcmp(token, "else") == 0 && lastSuccess) {
        return;
    }

    else if (strcmp(token, "else") == 0 && !lastSuccess) {
        //when we want it to run
        token = strtok(NULL, " ");

    }

    int argc = 0, argc_pipe = 0; //Counters
    int pipe_found = 0; //Boolean value, checks if pipe is found

    while (token != NULL && argc < MAX_ARGS - 1) {
        if (strcmp(token, "|") == 0) {
            pipe_found = 1; //Set the bool value to true; 
            break; // stop running the loop
        }

        else if (strchr(token, '*')) {
            wildcard_expansion(token, args, &argc);
        }

        else {
            char *tempWord = malloc(strlen(token) + 1); //malloc'd so this pointer will exist outside of this scope
            strcpy(tempWord, token); //Copies because malloc does not move data
            args[argc++] = tempWord; //Sets it equal so we can access this word outside
        }

        //strip leading and trailing space
        token = strtok(NULL, " "); //Iterates token to the next phrase
    }

    args[argc] = NULL; //Sets the last argument to NULL

    if (pipe_found) { //This will only run if a pipe is there, essentially does the same thing as before

        token = strtok(NULL, " ");
        while(token != NULL && argc_pipe < MAX_ARGS - 1) {
            char *tempWord = malloc(strlen(token) + 1); //malloc'd so this pointer will exist outside of this scope
            strcpy(tempWord, token); //Copies because malloc does not move data
            args_pipe[argc_pipe++] = tempWord; //Sets it equal so we can access this word outside
            token = strtok(NULL, " "); //move to the next token in the string
        }

        args_pipe[argc_pipe] = NULL; //Same as before, sets the argument to NULL

        exec_command_pipe(args, args_pipe); 

        for (int i = 0; i < argc_pipe; i++) { //Goes through and free's all the memory that was malloc'd, otherwise would result in leaky memory  
            free(args_pipe[i]);
        }
    }

    else {
        exec_command(args);
    }

    for (int i = 0; i < argc; i++) {
        free(args[i]);
    }

    return;
}

void wildcard_expansion(char *arg, char **argv, int *argc) {
    glob_t glob_res; 
    int i; 

    if (glob(arg, GLOB_NOCHECK | GLOB_TILDE, NULL, &glob_res) == 0) {
        for (int i = 0; i < glob_res.gl_pathc && *argc < MAX_ARGS - 1; i++) {
            char *tempWord = malloc(strlen(glob_res.gl_pathv[i]) + 1);
            strcpy(tempWord, glob_res.gl_pathv[i]);
            argv[(*argc)++] = tempWord;
        }
    }

    globfree(&glob_res);
    return;
}

void exec_command(char *argv[]) {

    if (strcmp(argv[0], "cd") == 0) { //if the program is cd, we want it to be "build in"
        if (argv[1] == NULL || argv[2] != NULL) { 
            fprintf(stderr, "cd: wrong number of arguments, must be 1 argument\n");
        }
        else if (chdir(argv[1]) != 0) {
            fprintf(stderr, "error changing directory\n");
        }
    }

    else if (strcmp(argv[0], "pwd") == 0) {
        char cwd[MAX_COMMAND_LENGTH]; //String to store the current directory path

        if (getcwd(cwd, sizeof(cwd)) != NULL) {
            printf("%s\n", cwd);
        }

        else {
            perror("pwd");
        }
    }

    else if (strcmp(argv[0], "which") == 0) {
        if (argv[1] == NULL || argv[2] != NULL) {
            fprintf(stderr, "wrong number of arguments, must be 1 argument\n");
        }
        else {
            char *path = getPath(argv[1]);
            printf("%s\n", path);
            free(path);
        }
    }

    else {
        //not a "build in" program, we can just call the program

        pid_t pid = fork();
        if (pid == -1) {
            perror("fork");
        }

        else if (pid == 0) {
            //child
            // lastSuccess = 1;
            char *path = getPath(argv[0]); //Dont want it to be the first one because then or else is the first one
            if (execv(path, argv) == -1) {
                // printf("Error: %s\n", strerror(errno));
                // lastSuccess = 0; //If execv fails, then the last command was not successful
                perror("execv");
                exit(EXIT_FAILURE);
            }
        }

        else {
            //parent
            int status; 
            waitpid(pid, &status, 0);
        }
    }

    return; 
}

void exec_command_pipe(char *args1[], char *args2[]) {
    int pipe_fds[2];
    pid_t pid1, pid2; 

    if (pipe(pipe_fds) == -1){
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    pid1 = fork();
    if (pid1 == 0) {
        //child 1
        close(pipe_fds[0]);
        dup2(pipe_fds[1], STDOUT_FILENO);
        close(pipe_fds[1]);
        fprintf(stderr, "%i\n", STDOUT_FILENO);

        char *path = getPath(args1[0]);
        if(execv(path, args1) == -1) {
            lastSuccess = 0; //If execv fails, then the last command was not successful
            perror("execv");
            exit(EXIT_FAILURE);
        }
        
    }

    pid2 = fork();
    if (pid2 == 0) {
        //child 2
        close(pipe_fds[1]);
        dup2(pipe_fds[0], STDIN_FILENO);
        close(pipe_fds[0]);

        char *path = getPath(args2[0]);
        lastSuccess = 1; //If execv fails, then the last command was not successful
        if(execv(path, args2) == -1) {
            lastSuccess = 0; //If execv fails, then the last command was not successful
            perror("execv");
            exit(EXIT_FAILURE);
        }
        
    }
    printf("here\n");

    //father
    close (pipe_fds[0]);
    close (pipe_fds[1]);
    waitpid(pid1, NULL, 0);
    waitpid(pid2, NULL, 0);
    return; 
}

char* getPath(char *programName) {
    char *path = malloc(sizeof("/usr/local/bin/") + strlen(programName) + 1);
    strcpy(path, "/usr/local/bin/");
    strcat(path, programName);

    if (access(path, F_OK) == 0) {
        return path;
    }

    else {
        strcpy(path, "");
        strcat(path, "/usr/bin/");
        strcat(path, programName);

        if (access(path, F_OK) == 0) {
            return path;
        }

        else {
            strcpy(path, "");
            strcat(path, "/bin/");
            strcat(path, programName);

            if (access(path, F_OK) == 0) {
                return path; 
            }
        }
    }

    //Only frees path if no path was found, must be freed elsewhere if the path is found
    free(path);
    return NULL; 
}

int main(int argc, char *argv[]) {
    if (argc == 1) {
        //Interactive Mode
        interactive_mode();
    }

    else if (argc == 2) {
        //Batch Mode
        batch_mode(argv[1]);
    }

    else {
        printf("Expected Usage: Must Provide Either One Or Two Command Line Arguments\n");
    }

    return 0;
}