#ifndef CREATURE_H_
#define CREATURE_H_

#include "ZorkObject.h"
#include "rapidxml.hpp"
#include <stdio.h>
#include <string>
#include <vector>
#include <map>
#include <iostream>

using namespace std;

class Creature : public ZorkObject {
public:
	Creature(rapidxml::xml_node<>*);
	virtual ~Creature();
	string name = "";
	string description = "";
	map<string, string> vulnerabilities;
	vector<Condition *> conditions;
	vector<string> print;
	vector<string> action;
	bool attack(Zork&, string);

private:
	void setup(rapidxml::xml_node<>*);
	void addCondition(rapidxml::xml_node<>*);
	void attackSetup(rapidxml::xml_node<>*);

};

#endif /* CREATURE_H_ */
