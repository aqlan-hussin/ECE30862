#ifndef CONTAINER_H_
#define CONTAINER_H_

#include "ZorkObject.h"
#include "rapidxml.hpp"
#include <stdio.h>
#include <string>
#include <vector>
#include <map>
#include <iostream>

using namespace std;

class Container : public ZorkObject {
public:
	Container(rapidxml::xml_node<>*);
	virtual ~Container();
	string name = "";
	string description = "";
	vector<string> accept;
	map<string, string> items;
	bool openFlag = false;

private:
	void setup(rapidxml::xml_node<>*);

};

#endif /* CONTAINER_H_ */