#include "Item.h"

Item::Item() {}

Item::Item(rapidxml::xml_node<>* node) {
	setup(node);
}

Item::Item(const Item& item) {
	this->name = item.name;
	this->description = item.description;
	this->status = item.status;
	this->writing = item.writing;
	this->onMessages = item.onMessages;
	this->onActions = item.onActions;
}

Item::~Item() {}

void Item::setup(rapidxml::xml_node<>* node) {
	node = node->first_node();
	string tValue;
	string tName;
	while (node != NULL) {
		tName = node->name();
		tValue = node->value();
		if (tName == "name") {
			this->name = tValue;
		} 
		else if (tName == "writing") {
			this->writing = tValue;
		}
		else if (tName == "status") {
			this->status = tValue;
		}
		else if (tName == "turnon") {
			setupOn(node->first_node());
		}
		node = node->next_sibling();
	}
}

void Item::setupOn(rapidxml::xml_node<>* node) {
	string tName;
	string tValue;
	while (node != NULL) {
		tName = node->name();
		tValue = node->value();
		if (tName == "print") {
			this->onMessages.push_back(tValue);
		}
		if (tName == "action") {
			this->onActions.push_back(tValue);
		}
		node = node->next_sibling();
	}
}