SOURCE=main.cpp Zork.cpp ZorkObject.cpp Trigger.cpp Room.cpp Item.cpp Creature.cpp Container.cpp Owner.cpp Status.cpp Command.cpp

MYPROGRAM=zork_exec


CC=g++

FLAGS= -w -std=c++11

all: $(MYPROGRAM)

$(MYPROGRAM): $(SOURCE)

	$(CC) $(FLAGS) $(SOURCE) -o $(MYPROGRAM) 

clean:

	rm -f $(MYPROGRAM)
