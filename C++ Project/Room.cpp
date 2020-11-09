#include "Room.h"

Room::Room(rapidxml::xml_node<>* node) {
	setup(node->first_node());
}

Room::~Room() {}

void Room::setup(rapidxml::xml_node<>* node) {
	string tName;
	string tValue;
	while (node != NULL) {
		tName = node->name();
		tValue = node->value();
		if (tName == "name") {
			this->name = tValue;
		}
		else if (tName == "description") {
			this->description = tValue;
		}
		else if (tName == "item") {
			this->items[tValue] = tValue;
		}
		else if (tName == "trigger") {
			this->triggers.push_back(new Trigger(node));
		}
		else if (tName == "border") {
			setupBorder(node);
		}
		else if (tName == "creature") {
			this->creatures[tValue] = tValue;
		}
		else if (tName == "container") {
			this->containers[tValue] = tValue;
		}
		else if (tName == "type") {
			this->type = tValue;
		}
		node = node->next_sibling();
	}
}

void Room::setupBorder(rapidxml::xml_node<>* node) {
	rapidxml::xml_node<>* childNode = node->first_node();
	string tName;
	string tValue;
	string direction = "badValue";
	string name = "badValue";
	while (childNode != NULL) {
		tName = childNode->name();
		tValue = childNode->value();
		if (tName == "direction") {
			direction = tValue;
		}
		else if (tName == "name") {
			name = tValue;
		}
		childNode = childNode->next_sibling();
	}
	this->borders[direction] = name;
}