#include "Owner.h"
#include "Zork.h"

Owner::Owner(rapidxml::xml_node<>*node) {
	setup(node->first_node());
}

Owner::~Owner(){}

bool Owner::check(Zork& zork) {
	if (owner == "inventory") {
		return checkInventory(zork);
	}
	else {
		if (zork.rooms.find(owner) != zork.rooms.end()) {
			return checkRoom(zork);
		}
		else if (zork.containers.find(owner) != zork.containers.end()) {
			return checkContainer(zork);
		}
	}

	return false;
}

bool Owner::checkInventory(Zork& zork) {
	if (zork.inventory.find(object) != zork.inventory.end()) {
		if (has == "yes") {
			return true;
		}
	}
	else {
		if (has == "no") {
			return true;
		}
	}

	return false;
}

bool Owner::checkRoom(Zork& zork) {
	Room * room = zork.rooms.find(owner)->second;
	bool itemPresent = (room->items.find(object) != room->items.end());
	if (itemPresent && (has == "yes") || !itemPresent && (has == "no")) {
		return true;
	}
	else {
		return false;
	}
}

bool Owner::checkContainer(Zork& zork) {
	Container * container = zork.containers.find(owner)->second;
	bool itemPresent = (container->items.find(object) != container->items.end());
	if (itemPresent && (has == "yes") || !itemPresent && (has == "no")) {
		return true;
	}
	else {
		return false;
	}
}

void Owner::setup(rapidxml::xml_node<>* node) {
	string tName;
	string tValue;
	while (node != NULL) {
		tName = node->name();
		tValue = node->value();
		if (tName == "has") {
			this->has = tValue;
		}
		else if (tName == "object") {
			this->object = tValue;
		}
		else if (tName == "owner") {
			this->owner = tValue;
		}
		node = node->next_sibling();
	} 
}