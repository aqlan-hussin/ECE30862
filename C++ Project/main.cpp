#include <stdlib.h>
#include "Zork.h"

int main(int argc, char const *argv[])
{

	if (argc < 2) {
		cout << "Usage: zork_exec <xmlFile>" << endl;
		return EXIT_FAILURE;
	}

	string xmlFile = string(argv[1]);

	Zork * newGame = new Zork(xmlFile);

	if (newGame->greenLight) {
		newGame->startGame();
	}

	delete newGame;

	return 0;
}