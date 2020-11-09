#include "Container.h"

Container::Container(rapidxml::xml_node<>* node) {
	setup(node->first_node());
}

Container::~Container(){}

void Container::setup(rapidxml::xml_node<>* node) {
	string tName;
	string tValue;
	while (node != NULL) {
		tName = node->name();
		tValue = node->value();
		if (tName == "name") {
			this->name = tValue;
		}
		else if (tName == "status") {
			this->status = tValue;
		}
		else if (tName == "accept") {
			this->accept.push_back(tValue);
			openFlag = true;
		}
		else if (tName == "item") {
			this->items[tValue] = tValue;
		}
		else if (tName == "trigger") {
			this->triggers.push_back(new Trigger(node));
		}
		node = node->next_sibling();
	}
}