#ifndef TRIGGER_H_
#define TRIGGER_H_

#include "Condition.h"
#include "Owner.h"
#include "Status.h"
#include "Command.h"
#include "rapidxml.hpp"

using namespace std;

class Trigger {
public:
	Trigger(rapidxml::xml_node<>*);
	~Trigger();
	vector<Condition*> conditions;
	string type = "single";
	bool commandFlag = false;
	vector<string> print;
	vector<string> action;
	bool check(Zork&);

private:
	void setup(rapidxml::xml_node<>*);
	void addCondition(rapidxml::xml_node<>*);

};

#endif /* TRIGGER_H_ */