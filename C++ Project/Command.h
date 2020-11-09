#ifndef COMMAND_H_
#define COMMAND_H_

#include <string>
#include "rapidxml.hpp"
#include "Condition.h"

using namespace std;

class Command : public Condition {
public:
	Command(rapidxml::xml_node<>*);
	string command = "";

private:
	bool check(Zork&);
	
};

#endif /* COMMAND_H_ */