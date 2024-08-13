CC = gcc
CFLAGS = -g -std=c99 -Wvla -Wall -fsanitize=address,undefined

output: mymalloc.o memgrind.o clear
	$(CC) $(CFLAGS) mymalloc.o memgrind.o -o output

mymalloc.o: mymalloc.c mymalloc.h
	$(CC) $(CFLAGS) -c mymalloc.c

memgrind.o: memgrind.c mymalloc.h
	$(CC) $(CFLAGS) -c memgrind.c

clean:
	rm *.o output

clear:
	clear