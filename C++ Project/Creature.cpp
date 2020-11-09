#include "Creature.h"

Creature::Creature(rapidxml::xml_node<>* node) {
	setup(node->first_node());
}

Creature::~Creature() {
	for (std::vector<Condition*>::iterator item = conditions.begin(); item != conditions.end(); ++item) {
		delete *item;
	}
}

bool Creature::attack(Zork& zork, string weapon) {
	if (vulnerabilities.find(weapon) == vulnerabilities.end()) {
		return false;
	}
	for (std::vector<Condition*>::iterator item = conditions.begin(); item != conditions.end(); ++item) {
		if (!((*item)->check(zork))) {
			return false;
		}
	}

	return true;
}

void Creature::setup(rapidxml::xml_node<>* node) {
	string tName;
	string tValue;
	while(node != NULL) {
		tName = node->name();
		tValue = node->value();
		if (tName == "name") {
			this->name = tValue;
		}
		else if (tName == "status") {
			this->status = tValue;
		}
		else if (tName == "vulnerability") {
			this->vulnerabilities[tValue] = tValue;
		}
		else if (tName == "trigger") {
			this->triggers.push_back(new Trigger(node));
		}
		else if (tName == "attack") {
			attackSetup(node);
		}
		node = node->next_sibling();
	}
}

void Creature::addCondition(rapidxml::xml_node<>* node) {
	rapidxml::xml_node<>* childNode = node->first_node();
	string nodeName;
	while (childNode != NULL) {
		nodeName = childNode->name();
		if (nodeName == "status") {
			conditions.push_back(new Status(node));
			return;
		}
		if (nodeName == "has") {
			conditions.push_back(new Owner(node));
			return;
		}
		childNode = childNode->next_sibling();
	}
}

void Creature::attackSetup(rapidxml::xml_node<>* node) {
	rapidxml::xml_node<>* childNode = node->first_node();
	string tName;
	string tValue;
	while (childNode != NULL) {
		tName = childNode->name();
		tValue = childNode->value();
		if (tName == "condition") {
			addCondition(childNode);
		}
		else if (tName == "print") {
			this->print.push_back(tValue);
		}
		else if (tName == "action") {
			this->action.push_back(tValue);
		}
		else if (tName == "trigger") {
			this->triggers.push_back(new Trigger(childNode));
		}
		childNode = childNode->next_sibling();
	}
}