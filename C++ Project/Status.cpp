#include "Status.h"
#include "Zork.h"

Status::Status(rapidxml::xml_node<>* node) {
	setup(node->first_node());
}

bool Status::check(Zork& zork) {
	ZorkObject * checkObject = zork.objects.find(object)->second;
	if ((checkObject != NULL) && (checkObject->status == status)) {
		return true;
	}
	else {
		return false;
	}
}

void Status::setup(rapidxml::xml_node<>* node) {
	string tName;
	string tValue;
	while (node != NULL) {
		tName = node->name();
		tValue = node->value();
		if (tName == "object") {
			this->object = tValue;
		}
		else if (tName == "status") {
			this->status = tValue;
		}
		node = node->next_sibling();
	}
}