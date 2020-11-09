#include "Command.h"
#include "Zork.h"

Command::Command(rapidxml::xml_node<>*node) {
	command = node->value();
}

bool Command::check(Zork& zork) {
	if (command == zork.userInput)
	{
		return true;
	}
	else
	{
		return false;
	}
}