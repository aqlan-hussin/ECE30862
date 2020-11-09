#include "Trigger.h"
#include "Zork.h"

Trigger::Trigger(rapidxml::xml_node<>* node) {
	setup(node->first_node());
}

Trigger::~Trigger() {
	for (std::vector<Condition*>::iterator item = conditions.begin(); item != conditions.end(); ++item) {
		delete *item;
	}
}

bool Trigger::check(Zork& zork) {
	for (std::vector<Condition*>::iterator item = conditions.begin(); item != conditions.end(); ++item) {
		if (!(*item)->check(zork)) {
			return false;
		}
	}
	return true;
}

void Trigger::setup(rapidxml::xml_node<>* node) {
	string tName;
	string tValue;
	while (node != NULL) {
		tName = node->name();
		tValue = node->value();
		if (tName == "type") {
			this->type = tValue;
		}
		else if (tName == "command") {
			this->conditions.push_back(new Command(node));
			commandFlag = true;
		}
		else if (tName == "condition") {
			addCondition(node);
		}
		else if (tName == "print") {
			this->print.push_back(tValue);
		}
		else if (tName == "action") {
			this->action.push_back(tValue);
		}
		node = node->next_sibling();
	}
}

void Trigger::addCondition(rapidxml::xml_node<>* node) {
	rapidxml::xml_node<>* childNode = node->first_node();
	string tName;
	while (childNode != NULL) {
		tName = childNode->name();
		if (tName == "status") {
			conditions.push_back(new Status(node));
			return;
		}
		if (tName == "has") {
			conditions.push_back(new Owner(node));
		}
		childNode = childNode->next_sibling();
	}
}