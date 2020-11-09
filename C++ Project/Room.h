#ifndef ROOM_H_
#define ROOM_H_

#include "ZorkObject.h"
#include "rapidxml.hpp"
#include <stdio.h>
#include <string>
#include <map>
#include <vector>

using namespace std;

class Room : public ZorkObject {
public:
	Room(rapidxml::xml_node<>*);
	virtual ~Room();
	string name = "";
	string type = "regular";
	string description;
	map<string, string> borders;
	map<string, string> containers;
	map<string, string> items;
	map<string, string> creatures;

private:
	void setup(rapidxml::xml_node<>*);
	void setupBorder(rapidxml::xml_node<>*);
};

#endif /* ROOM_H_ */