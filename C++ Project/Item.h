#ifndef ITEM_H_
#define ITEM_H_

#include "ZorkObject.h"
#include "rapidxml.hpp"
#include <stdio.h>
#include <string>
#include <vector>
#include <iostream>

using namespace std;

class Item : public ZorkObject {
public:
	Item(rapidxml::xml_node<>*);
	Item(const Item&);
	Item();
	virtual ~Item();
	string name = "";
	string description = "";
	string writing = "";
	vector<string> onMessages;
	vector<string> onActions;

private:
	void setup(rapidxml::xml_node<>*);
	void setupOn(rapidxml::xml_node<>*);

};

#endif /* ITEM_H_ */