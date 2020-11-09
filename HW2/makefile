TARGET = main
GCC = g++
CFLAGS = -g -Wall -Wshadow -Werror
CC = $(GCC) $(CFLAGS)
SRCS = main.cpp Base.cpp Derived.cpp
OBJS = $(SRCS:%.cpp=%.o)
VALGRIND = valgrind --tool=memcheck --verbose --log-file

all:
	@$(CC) $(SRCS) -o $(TARGET)
	@./$(TARGET)

test:
	$(VALGRIND)=./logfile ./main 

.c.o:
	@$(GCC) $(CFLAGS) -c $*.c

clean:
	@rm -f $(OBJS) $(TARGET)
