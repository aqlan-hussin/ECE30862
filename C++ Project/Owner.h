#ifndef OWNER_H_
#define OWNER_H_

#include "Condition.h"
#include "rapidxml.hpp"
#include <stdio.h>
#include <map>
#include <string>
#include <vector>

using namespace std;

class Owner : public Condition {
public:
	Owner(rapidxml::xml_node<>*);
	~Owner();
	string has = "";
	string owner = "";
	bool check(Zork&);

private:
	bool checkInventory(Zork&);
	bool checkRoom(Zork&);
	bool checkContainer(Zork&);
	void setup(rapidxml::xml_node<>*);

};

#endif /* OWNER_H_ */
