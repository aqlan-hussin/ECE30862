#ifndef STATUS_H_
#define STATUS_H_

#include "Condition.h"
#include "rapidxml.hpp"
#include <stdio.h>
#include <map>
#include <string>
#include <vector>

using namespace std;

class Status : public Condition {
public:
	Status(rapidxml::xml_node<>*);
	string status = "";
	bool check(Zork&);

private:
	void setup(rapidxml::xml_node<>*);

};

#endif /* STATUS_H_ */
